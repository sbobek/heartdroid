/**
*
*     Copyright 2013-15 by Szymon Bobek, Grzegorz J. Nalepa, Mateusz Ślażyński
*
*
*     This file is part of HeaRTDroid.
*     HeaRTDroid is a rule engine that is based on HeaRT inference engine,
*     XTT2 representation and other concepts developed within the HeKatE project .
*
*     HeaRTDroid is free software: you can redistribute it and/or modify
*     it under the terms of the GNU General Public License as published by
*     the Free Software Foundation, either version 3 of the License, or
*     (at your option) any later version.
*
*     HeaRTDroid is distributed in the hope that it will be useful,
*     but WITHOUT ANY WARRANTY; without even the implied warranty of
*     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*     GNU General Public License for more details.
*
*     You should have received a copy of the GNU General Public License
*     along with HeaRTDroid.  If not, see <http://www.gnu.org/licenses/>.
*
**/


package heart.alsvfd;

import heart.exceptions.NotInTheDomainException;
import heart.exceptions.UnknownValueException;
import heart.xtt.Type;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SetValue extends Value implements Iterable<Value>{

	public static final String ERROR_MESSAGE = "Operation is not applicable for values other than set values.";
	/**
	 * An ArrayList representing values of a set.
	 */
	protected List<Value> values;

	/**
	 * Default constructor. It creates an empty set.
	 */
	public SetValue() {
		values = new ArrayList<Value>();
		setCertaintyFactor(0.0f);
	}

	public SetValue(SetValue other) {
		setValues(other.getValues());
		setCertaintyFactor(other.getCertaintyFactor());
		setTimestamp(other.getTimestamp());
	}

	/**
	 * Constructor that assign values given as a parameter to values that
	 * represents the set.
	 * 
	 * @param values to set
	 */
	public SetValue(List<Value> values) {
		setValues(values);
		setCertaintyFactor(1.0f);
	}
	
	/**
	 * Constructor that assign values given as a parameter to values that
	 * represents the set.
	 * 
	 * @param values to set
	 * @param certaintyFactor A certainty factor that the set have inside given values. 
	 */
	public SetValue(List<Value> values, float certaintyFactor) {
		setValues(values);
		setCertaintyFactor(certaintyFactor);
	}

	@Override
	public boolean eq(Value v, Type t) throws UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		Boolean checked = checkDomainConditions(v, t);
		if (checked != null)
			return checked;

		try {
			if (v instanceof Range) {
				if (this.cardinality(t) != ((Range) v).cardinality(t))
					return false;
			} else {
				if (this.cardinality(t) != ((SetValue) v).cardinality(t))
					return false;
			}

			if (((SetValue) this.except(v, t)).isEmpty()
					&& ((SetValue) v.except(this, t)).isEmpty()) {
				return true;
			}
			return false;
		} catch (ClassCastException e) {
            if(v instanceof Null){
                throw new UnknownValueException("Error while evaluating "+this+" add "+v+". Operator not supported for this types.");
            }else {
                throw new UnsupportedOperationException(ERROR_MESSAGE);
            }
		}
	}

	@Override
	public boolean neq(Value v, Type t) throws UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		return !eq(v, t);
	}

	@Override
	public boolean supset(Value v, Type t)
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		checkSetDomainConditions(v, t);

		if (v instanceof SetValue || v instanceof Range) {
			SetValue result = (SetValue) v.except(this, t);
			return result.isEmpty();
		}else if(v instanceof Null){
            throw new UnknownValueException("Error while evaluating "+this+" add "+v+". Operator not supported for this types.");
        }else {
			throw new UnsupportedOperationException(ERROR_MESSAGE);
		}
	}

	@Override
	public boolean subset(Value v, Type t)
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		checkSetDomainConditions(v, t);

		if (v instanceof SetValue || v instanceof Range) {
			SetValue result = (SetValue) this.except(v, t);
			return result.isEmpty();
		}else if(v instanceof Null){
            throw new UnknownValueException("Error while evaluating "+this+" add "+v+". Operator not supported for this types.");
        }else {
			throw new UnsupportedOperationException(ERROR_MESSAGE);
		}
	}

	@Override
	public boolean sim(Value v, Type t) throws UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		checkSetDomainConditions(v, t);

		if (v instanceof SetValue || v instanceof Range) {
			SetValue result = (SetValue) this.except(v, t);
			return result.neq(this, t);
		}else if(v instanceof Null){
            throw new UnknownValueException("Error while evaluating "+this+" add "+v+". Operator not supported for this types.");
        } else {
			throw new UnsupportedOperationException(ERROR_MESSAGE);
		}
	}

	@Override
	public boolean notsim(Value v, Type t)
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		return !sim(v, t);
	}

	@Override
	public Value intersect(Value v, Type t)
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		checkSetDomainConditions(v, t);

		if (v instanceof SetValue || v instanceof Range) {
			SetValue result = (SetValue) this.except(v, t);

			return this.except(result, t);
		} else {
			throw new UnsupportedOperationException(ERROR_MESSAGE);
		}
	}

	@Override
	public Value union(Value v, Type t) throws UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		checkSetDomainConditions(v, t);
		SetValue result = new SetValue(this);
		if (v instanceof SetValue) {
			result.appendValues((SetValue) v);
		} else if (v instanceof Range) {
			result.appendValue(v);
		} else {
			throw new UnsupportedOperationException(ERROR_MESSAGE);
		}
		return result;
	}

	@Override
	public Value except(Value v, Type t) throws UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		checkSetDomainConditions(v, t);
		SetValue result = new SetValue();	
		if (v instanceof SimpleSymbolic || v instanceof SimpleNumeric) {
			for (Value setElement : getValues()) {
				try {
					if (!v.eq(setElement, t))
						result.appendValue(setElement);
				} catch (UnsupportedOperationException e) {
					if (setElement instanceof Range) {
						result.appendValues((SetValue) setElement.except(v, t));
					} else {
						result.appendValue(setElement);
					}
				}
			}
		} else if (v instanceof SetValue) {
			// TODO: depend which set is shorter
			for (Value setElement : getValues()) {
				try {
					if (!setElement.in(v, t)) {
						result.appendValue(setElement);
					}
				} catch (UnsupportedOperationException e) {
					result.appendValue(setElement);
				}
			}
		} else if (v instanceof Range) {
			for (Value setElement : getValues()) {
				try {
					if (setElement instanceof Range) {
						result.appendValue((SetValue) setElement.except(v, t));
					} else {
						if (!setElement.in(v, t)) {
							result.appendValue(setElement);
						}
					}

				} catch (UnsupportedOperationException e) {
					result.appendValue(setElement);
				}
			}
		} else if(v instanceof Null){
            throw new UnknownValueException("Error while evaluating "+this+" add "+v+". Operator not supported for this types.");
        } else {
			throw new UnsupportedOperationException("Error while evaluating "
					+ this + " except " + v
					+ ". Operator not supported for this types.");
		}
		return result;
	}

	public void appendValue(Value v) {
		// TODO: some optimization can be performed to reject redundancies
		// TODO: check if all values are of the same type, check if we do not
		// add set to prevent from nesting sets
		getValues().add(v);
	}

	public void appendValues(SetValue sv) {
		// TODO: some optimization can be performed to reject redundancies
		getValues().addAll(sv.getValues());
	}

	public List<Value> getValues() {
		return values;
	}

	public void setValues(List<Value> values) {
		this.values = new ArrayList<Value>(values);
	}

	public boolean isEmpty() {
		return getValues().isEmpty();
	}

	/**
	 * Method that search for the maximum value in a set. If set contains
	 * unordered elements, an exception is thrown. The method apply only to sets
	 * containing simple values to which operators lt and gt can be applied. If
	 * the method is called on a set containing other type of elements, and
	 * UnsupportedOperationException is thrown
	 * 
	 * @return maximum value of a set
	 * @throws UnsupportedOperationException
	 * @throws NotInTheDomainException
	 */
	public Value max() throws UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		Value max = null;
		Value toCompare = null;
		for (Value v : values) {
			if (v instanceof Range) {
				toCompare = ((Range) v).getTo();
			} else if (v instanceof SetValue) {
				toCompare = ((SetValue) v).max();
			} else {
				toCompare = v;
			}

			if (max == null) {
				max = toCompare;
			} else if (toCompare != null && max.lt(toCompare, null)) {
				max = toCompare;
			}
		}
		return max;
	}

	/**
	 * Method that search for the minimum value in a set. If set contains
	 * unordered elements, an exception is thrown. The method apply only to sets
	 * containing simple values to which operators lt and gt can be applied. If
	 * the method is called on a set containing other type of elements, and
	 * UnsupportedOperationException is thrown
	 * 
	 * @return minimum value of a set
	 * @throws UnsupportedOperationException
	 * @throws NotInTheDomainException
	 */
	public Value min() throws UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		Value min = null;
		Value toCompare = null;
		for (Value v : values) {
			if (v instanceof Range) {
				toCompare = ((Range) v).getFrom();
			} else if (v instanceof SetValue) {
				toCompare = ((SetValue) v).min();
			} else {
				toCompare = v;
			}

			if (min == null) {
				min = toCompare;
			} else if (toCompare != null && min.gt(toCompare, null)) {
				min = toCompare;
			}
		}
		return min;
	}

	@Override
	public String toString() {
		String result = "[";
		Iterator<Value> it = values.iterator();
		while (it.hasNext()) {
			result += it.next().toString();
			if (it.hasNext())
				result += " ,";
		}
		result += "]";

		return result;
	}

	@Override
	public boolean isInTheDomain(Type t) throws UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		for (Value setElement : getValues()) {
			if (!setElement.isInTheDomain(t))
				return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return new Integer(getValues().size()).hashCode();
	}

	@Override
	public Value clone() {
		return new SetValue(this);
	}

	/**
	 * This returns number of elements in a set. The set is not flatten, so a
	 * set containing three sets will have cardinality equals 3 no matter how
	 * many elements the inner sets will have.
	 * 
	 * @param t
	 *            type with respect to which the cardinality should be
	 *            calculated. This is important for the numeric ranges
	 * @return number of elements in a set
	 * @throws NotInTheDomainException
	 * @throws UnsupportedOperationException
	 */
	public int cardinality(Type t) throws UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		int cardinality = 0;

		for (Value v : getValues()) {
			if (v instanceof Range) {
				cardinality = ((Range) v).cardinality(t);
			} else {
				cardinality++;
			}
		}
		return cardinality;
	}

	Boolean checkDomainConditions(Value v, Type t)
            throws NotInTheDomainException, UnknownValueException {
		if (v instanceof Null)
			return false;
		checkDomain(v, t);
		if (v instanceof Any)
			return true;

		// if no exception were thrown and v is neither Null nor Any
		return null;
	}

	void checkSetDomainConditions(Value v, Type t)
            throws NotInTheDomainException, UnknownValueException {
		if (v instanceof Null)
			throw new UnknownValueException("SetValue " + this
					+ " can not be compared with Null");
		checkDomain(v, t);
		if (v instanceof Any)
			throw new UnsupportedOperationException("SetValue " + this
					+ " can not be compared with Any");
	}

    @Override
    public Iterator<Value> iterator() {
        return getValues().iterator();
    }
}
