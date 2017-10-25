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

import heart.alsvfd.operations.LogicOperations;
import heart.exceptions.NotInTheDomainException;
import heart.exceptions.UnknownValueException;
import heart.xtt.Type;

public class SimpleSymbolic extends Value {
	public static final String NOT_ORDERED_ERROR_MESSAGE = "Can not use this operation for not ordered values!";
	
	private String value;
	private Integer order = null;

	public SimpleSymbolic() {
		setCertaintyFactor(0.0f);
	}

	public SimpleSymbolic(String value) {
		setValue(value);
	}
	

	public SimpleSymbolic(String value, Integer order) {
		setValue(value);
		setOrder(order);
	}
	
	public SimpleSymbolic(String value, Integer order, float certaintyFactor) {
		setValue(value);
		setOrder(order);
		setCertaintyFactor(certaintyFactor);
	}

	public SimpleSymbolic(SimpleSymbolic otherValue) {
		setValue(otherValue.getValue());
		setOrder(otherValue.getOrder());
		setCertaintyFactor(otherValue.getCertaintyFactor());
		setTimestamp(otherValue.getTimestamp());
	}

	@Override
	public boolean eq(Value v, Type t) throws UnsupportedOperationException,
			NotInTheDomainException {
		if (v instanceof Null)
			return false;		
		if (v instanceof Any)
			return true;
		
		SimpleSymbolic left = (SimpleSymbolic) findSymbolicInDomain(this, t);
		if (v instanceof SimpleSymbolic) {
			SimpleSymbolic right = (SimpleSymbolic) findSymbolicInDomain(v, t);						
			if (left.getValue().equals(right.getValue())) {
				return true;
			}
		} else if (v instanceof SimpleNumeric) {			
			SimpleSymbolic right = (SimpleSymbolic) findSymbolicInDomain(v, t);			
			if (left.isOrdered()) {
				return (left.getOrder().equals(right.getOrder()));
			} else {
				throw new UnsupportedOperationException(NOT_ORDERED_ERROR_MESSAGE);
			}
		} else {
			throw new UnsupportedOperationException(getWrongValueMessage(v));
		}

		return false;
	}

	@Override
	public boolean neq(Value v, Type t) throws UnsupportedOperationException,
			NotInTheDomainException {
		return !eq(v, t);
	}

	@Override
	public boolean gt(Value v, Type t) throws UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		return computeLogicalExpresion(v, t, LogicOperations.GT);
	}

	@Override
	public boolean gte(Value v, Type t) throws UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		return computeLogicalExpresion(v, t, LogicOperations.GTE);
	}

	@Override
	public boolean lt(Value v, Type t) throws UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		return computeLogicalExpresion(v, t, LogicOperations.LT);
	}

	@Override
	public boolean lte(Value v, Type t) throws UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		return computeLogicalExpresion(v, t, LogicOperations.LTE);
	}

	@Override
	public boolean in(Value v, Type t) throws UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		checkNullAndAny(v);		
		try {
			if (t != null) {
				if (!this.isInTheDomain(t))
					throw new NotInTheDomainException(t.getDomain(), this,
							"Value " + this + " not in the domain");
				if (!v.isInTheDomain(t))
					throw new NotInTheDomainException(t.getDomain(), v,
							"Value " + v + " not in the domain");
			}			
			SetValue sv = (SetValue) v;
			for (Value subset : sv.getValues()) {
				if (subset instanceof SimpleSymbolic
						|| subset instanceof SimpleNumeric) {
					// In a case when a subset is just a value, compare
					if (this.eq(subset, t))
						return true;
				} else {
					// In a case when subset is another set, or range value, go
					// recursively
					if (this.in(subset, t))
						return true;
				}
			}
			return false;

		} catch (ClassCastException e) {
			if (v instanceof Range) {
				Range rnv = (Range) v;
				if (this.gt(rnv.getFrom(), t) && this.lt(rnv.getTo(), t)) {
					return true;
				} else if (rnv.isLeftInclusive() && this.eq(rnv.getFrom(), t)) {
					return true;
				} else if (rnv.isRightInclusive() && this.eq(rnv.getTo(), t)) {
					return true;
				}
			} else if(v instanceof Null) {
                throw new UnknownValueException("Error while evaluating "+this+" gt "+v+". Operator not supported for this types.");
            }else{

				throw new UnsupportedOperationException(getWrongValueMessage(v));
			}
		}
		return false;
	}

	@Override
	public boolean notin(Value v, Type t) throws UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		return !in(v, t);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	@Override
	public String toString() {
		if (order != null)
			return value + "/" + order;
		else
			return value;
	}

	public boolean isOrdered() {
		return (order != null);

	}

	/**
	 * A method that searches for a value in a domain of {@link Type}. The
	 * method will return the value with its ordered number if exists.
	 * 
	 * @param v
	 *            a value to be searched in the domain
	 * @param t
	 *            a type that contains a value
	 * @return a copy of a value from a domain that matches value given as a
	 *         parameter. If no match is found null is returned.
	 */
	public static SimpleSymbolic findInTheDomain(Value v, Type t) {
		if (v instanceof SimpleSymbolic) {
			return findInTheDomain((SimpleSymbolic) v, t);
		} else {
			return findInTheDomain((SimpleNumeric) v, t);
		}
	}

	/**
	 * A method that searches for a value in a domain of {@link Type}. This is
	 * useful if ordered field in {@link SimpleSymbolic} value is not known. The
	 * method will return the value with its ordered number if exists.
	 * 
	 * @param v
	 *            a value to be searched in the domain
	 * @param t
	 *            a type that contains a value
	 * @return a copy of a value from a domain that matches value given as a
	 *         parameter. If no match is found null is returned.
	 */
	public static SimpleSymbolic findInTheDomain(SimpleSymbolic v, Type t) {
		for (Value domainElement : t.getDomain().getValues()) {
			if (domainElement instanceof SimpleSymbolic) {
				SimpleSymbolic domainValue = (SimpleSymbolic) domainElement;
				// check for symbolic names
				if (domainValue.getValue().equals(v.getValue())) {
					return new SimpleSymbolic(domainValue);
				}
			}
		}
		return null;
	}

	/**
	 * A method that searches for a value in a domain of {@link Type}. This is
	 * useful if symbolic field in {@link SimpleSymbolic} value is not known.
	 * The method will return the value with its symbolic number if exists.
	 * 
	 * @param v
	 *            a value to be searched in the domain
	 * @param t
	 *            a type that contains a value
	 * @return a copy of a value from a domain that matches value given as a
	 *         parameter. If no match is found null is returned.
	 */
	public static SimpleSymbolic findInTheDomain(SimpleNumeric v, Type t) {
		if(!t.getOrdered().equals(Type.ORDERED_YES)) 
			throw new UnsupportedOperationException(SimpleSymbolic.NOT_ORDERED_ERROR_MESSAGE);
		for (Value domainElement : t.getDomain().getValues()) {
			if (domainElement instanceof SimpleSymbolic) {
				SimpleSymbolic domainValue = (SimpleSymbolic) domainElement;
				// check for symbolic names
				if (domainValue.getOrder().equals(v.getValue().intValue())) {
					return new SimpleSymbolic(domainValue);
				}
			}
		}
		return null;
	}

	@Override
	public boolean isInTheDomain(Type t) {
		return (findInTheDomain(this, t) != null);
	}

	@Override
	public int hashCode() {
		return getValue().hashCode();
	}

	@Override
	public Value clone() {
		return new SimpleSymbolic(this);
	}

	private SimpleSymbolic findSymbolicInDomain(Value value, Type type) throws NotInTheDomainException{
		if(type == null && value instanceof SimpleNumeric){
			return new SimpleSymbolic("ghost", ((SimpleNumeric)value).getValue().intValue());
		}
		if(type == null){
			return (SimpleSymbolic) value;
		}
		Value temp = value;
		value = findInTheDomain(value, type);		
		// not in the domain
		if (value == null) {
			throw new NotInTheDomainException(type.getDomain(),temp,
					"Value " + temp + " not in the domain");
		}
		
		return (SimpleSymbolic) value;
	}
	
	private void checkNullAndAny(Value value) throws UnknownValueException {
		if (value instanceof Null)
			throw new UnknownValueException("SimpleSymbolic " + this
					+ " can not be compared with Null");
		if (value instanceof Any)
			throw new UnsupportedOperationException("SimpleSymbolic " + this
					+ " can not be compared with Any");
	}
	
	boolean computeLogicalExpresion(Value v, Type t, LogicOperations operation) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		checkNullAndAny(v);		
		SimpleSymbolic left = findSymbolicInDomain(this, t);
		if (v instanceof SimpleSymbolic) {
			SimpleSymbolic right = findSymbolicInDomain(v, t);					

			if (left.isOrdered() && right.isOrdered()) {
				// If two operands are ordered compare them according to their
				return operation.logicalExpresion(left.getOrder(), right.getOrder());
			} else {
				throw new UnsupportedOperationException(NOT_ORDERED_ERROR_MESSAGE);
			}
		} else if (v instanceof SimpleNumeric) {			
			SimpleSymbolic right = findSymbolicInDomain(v, t);	
			if (left.isOrdered()) {
				return operation.logicalExpresion(left.getOrder(), right.getOrder());
			}
		} else if(v instanceof Null) {
            throw new UnknownValueException("Error while evaluating "+this+" gt "+v+". Operator not supported for this types.");
        }else {
			throw new UnsupportedOperationException(getWrongValueMessage(v));
		}
		return false;
	}

	private String getWrongValueMessage(Value v) {
		return "This SimpleSymbolic operation is not defined for value " + v;
	}	
}
