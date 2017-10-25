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

import heart.alsvfd.operations.ErrorMessanger;
import heart.alsvfd.operations.LogicOperations;
import heart.alsvfd.operations.NumericOperations;
import heart.alsvfd.operations.SetOperations;
import heart.exceptions.NotInTheDomainException;
import heart.exceptions.UnknownValueException;
import heart.xtt.Type;

public class SimpleNumeric extends Value {
	public static final String INF_PLUS = "+inf";
	public static final String INF_MINUS = "-inf";

	private Double value;

	/**
	 * Default constructor that sets the value to null.;
	 */
	public SimpleNumeric() {
		setCertaintyFactor(0.0f);
	}

	public SimpleNumeric(Double value) {
		setValue(value);
	}
	
	public SimpleNumeric(Double value, float certaintyFactor) {
		setValue(value);
		setCertaintyFactor(certaintyFactor);
	}

	public SimpleNumeric(SimpleNumeric other){
		setValue(other.getValue());
		setCertaintyFactor(other.getCertaintyFactor());
		setTimestamp(other.getTimestamp());
	}

	@Override
	public boolean eq(Value v, Type t) throws UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		if (v instanceof Null)
			return false;
		checkDomain(v, t);
		if (v instanceof Any)
			return true;
		return computeLogicalExpression(v, t, LogicOperations.EQ);
	}

	@Override
	public boolean neq(Value v, Type t) throws UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		if (v instanceof Null)
			return true;
		checkDomain(v, t);
		if (v instanceof Any)
			return false;
		return computeLogicalExpression(v, t, LogicOperations.NEQ);
	}

	@Override
	public boolean gt(Value v, Type t) throws UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		return computeLogicalExpression(v, t, LogicOperations.GT);
	}

	@Override
	public boolean gte(Value v, Type t) throws UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		return computeLogicalExpression(v, t, LogicOperations.GTE);
	}

	@Override
	public boolean lt(Value v, Type t) throws UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		return computeLogicalExpression(v, t, LogicOperations.LT);
	}

	@Override
	public boolean lte(Value v, Type t) throws UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		return computeLogicalExpression(v, t, LogicOperations.LTE);
	}

	boolean computeLogicalExpression(Value v, Type t, LogicOperations operation) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		checkForExceptions(v, t, operation);
		try{
			SimpleNumeric right = (SimpleNumeric)v;
			return operation.logicalExpresion(getValue(), right.getValue());
		}catch(ClassCastException e){
			if(v instanceof SimpleSymbolic){
				SimpleSymbolic sv = (SimpleSymbolic) v;
			    if(sv.isOrdered()){
			    		return operation.logicalExpresion(getValue(),sv.getOrder().doubleValue());
			    }else{
			    	throw new UnsupportedOperationException("Numeric "+this+" cannot be compared " + operation +" to symbolic "+v+" unless it is ordered");
			    }
			} else if(v instanceof Null) {
                throw new UnknownValueException("Error while evaluating "+this+" gt "+v+". Operator not supported for this types.");
            }else{
				throw new UnsupportedOperationException("Error while comapring symbolic values for " + operation + " operation");
			}
		}
	}

	@Override
	public boolean in(Value v, Type t) throws UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		return computeSetExpresion(v, t, SetOperations.IN);
	}

	@Override
	public boolean notin(Value v, Type t) throws UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		return !computeSetExpresion(v, t, SetOperations.NOTIN);
	}

	private boolean computeSetExpresion(Value v, Type t, SetOperations operation)
            throws NotInTheDomainException, UnknownValueException {
		checkForExceptions(v, t, operation);
		try {
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
			} else {
                if(v instanceof Null) {
                    throw new UnknownValueException("Error while evaluating "+this+" gt "+v+". Operator not supported for this types.");
                }else {
                    throw new UnsupportedOperationException(
                            "Error while checking membership of numeric value. Set or range value expected, not "
                                    + v);
                }
			}

		}

		return false;
	}

	@Override
	public Value mul(Value v, Type t) throws UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		return computeNumericExpression(v, t, NumericOperations.MUL);
	}

	@Override
	public Value sub(Value v, Type t) throws UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		return computeNumericExpression(v, t, NumericOperations.SUB);
	}

	@Override
	public Value div(Value v, Type t) throws UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		return computeNumericExpression(v, t, NumericOperations.DIV);
	}

	@Override
	public Value add(Value v, Type t) throws UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		return computeNumericExpression(v, t, NumericOperations.ADD);
	}

	@Override
	public Value pow(Value v, Type t) throws NotInTheDomainException, UnknownValueException {
		return computeNumericExpression(v, t, NumericOperations.POW);
	}

	@Override
	public Value factorial(){
		if(this.getValue() % 1 != 0){
			throw new UnsupportedOperationException("Factorial error! SimpleNumeric value  without decimal part expected, not " + this.getValue());
		}
		double result = 1;
		for(double counter = 1; counter <= this.getValue(); counter++){
			result *=counter;
		}
		return new SimpleNumeric(result,this.getCertaintyFactor());
	}

	@Override
	public Value mod(Value v, Type t) throws NotInTheDomainException, UnknownValueException {
		return computeNumericExpression(v, t, NumericOperations.MOD);
	}

	private Value computeNumericExpression(Value v, Type t,
										   NumericOperations operation) throws UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		checkForExceptions(v, t, operation);
		try {
			SimpleNumeric right = (SimpleNumeric) v;
			return new SimpleNumeric(operation.numericExpression(getValue(),
					right.getValue()));

		} catch (ClassCastException e) {
            if(v instanceof Null) {
                throw new UnknownValueException("Error while evaluating "+this+" gt "+v+". Operator not supported for this types.");
            }else {
                throw new UnsupportedOperationException(operation
                        + " error! SimpleNumeric value expected, not " + v);
            }
		}
	}

	public Double getValue() {
		// TODO: precision
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value.toString();

	}

	@Override
	public boolean isInTheDomain(Type t) throws UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		return this.in(t.getDomain(), null);
	}

	@Override
	public int hashCode() {
		return getValue().hashCode();
	}

	@Override
	public Value clone() {
		return new SimpleNumeric(this);
	}

	private void checkForExceptions(Value v, Type t, ErrorMessanger message)
            throws NotInTheDomainException, UnsupportedOperationException, UnknownValueException {
		if (v instanceof Null)
			throw new UnknownValueException(message.errorMessage(this, "null"));
		checkDomain(v, t);
		if (v instanceof Any)
			throw new UnsupportedOperationException(message.errorMessage(this, "any"));		
	}
}
