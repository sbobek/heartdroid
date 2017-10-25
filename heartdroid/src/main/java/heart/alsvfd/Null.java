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

import heart.WorkingMemory;
import heart.exceptions.NotInTheDomainException;
import heart.exceptions.UnknownValueException;
import heart.xtt.Type;

/**
 * The class representing a state of attribute in which we have o information about its values.
 * Only eq and neq operators are allowed for this kind of type.
 * @author sbk
 *
 */
public class Null extends Value{

	@Override
	public boolean eq(Value v, Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		if(t != null && !v.isInTheDomain(t))  throw new NotInTheDomainException(t.getDomain(), v, "Value "+v+" not in the domain");
		return (v instanceof Null);
	}

	@Override
	public boolean neq(Value v, Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		if(t != null && !v.isInTheDomain(t))  throw new NotInTheDomainException(t.getDomain(), v, "Value "+v+" not in the domain");
		return (v instanceof Null);
	}

	@Override
	public boolean gt(Value v, Type t) throws UnsupportedOperationException, UnknownValueException {
		throw new UnknownValueException("Error while evaluating "+this+" gt "+v+". Operator not supported for this types.");
	}

	@Override
	public boolean gte(Value v, Type t) throws UnsupportedOperationException, UnknownValueException {
		throw new UnknownValueException("Error while evaluating "+this+" gte "+v+". Operator not supported for this types.");
	}

	@Override
	public boolean lt(Value v, Type t) throws UnsupportedOperationException, UnknownValueException {
		throw new UnknownValueException("Error while evaluating "+this+" lt "+v+". Operator not supported for this types.");
	}

	@Override
	public boolean lte(Value v, Type t) throws UnsupportedOperationException, UnknownValueException {
		throw new UnknownValueException("Error while evaluating "+this+" lte "+v+". Operator not supported for this types.");
	}

	@Override
	public boolean in(Value v, Type t) throws UnsupportedOperationException, UnknownValueException {
		throw new UnknownValueException("Error while evaluating "+this+" in "+v+". Operator not supported for this types.");
	}

	@Override
	public boolean notin(Value v, Type t) throws UnsupportedOperationException, UnknownValueException {
		throw new UnknownValueException("Error while evaluating "+this+" notin "+v+". Operator not supported for this types.");
	}

	@Override
	public boolean supset(Value v, Type t) throws UnsupportedOperationException, UnknownValueException {
		throw new UnknownValueException("Error while evaluating "+this+" supset "+v+". Operator not supported for this types.");
	}

	@Override
	public boolean subset(Value v, Type t) throws UnsupportedOperationException, UnknownValueException {
		throw new UnknownValueException("Error while evaluating "+this+" subset "+v+". Operator not supported for this types.");
	}

	@Override
	public boolean sim(Value v, Type t) throws UnsupportedOperationException, UnknownValueException {
		throw new UnknownValueException("Error while evaluating "+this+" sim "+v+". Operator not supported for this types.");
	}

	@Override
	public boolean notsim(Value v, Type t) throws UnsupportedOperationException, UnknownValueException {
		throw new UnknownValueException("Error while evaluating "+this+" notsim "+v+". Operator not supported for this types.");
	}

	@Override
	public Value intersect(Value v, Type t)
            throws UnsupportedOperationException, UnknownValueException {
		throw new UnknownValueException("Error while evaluating "+this+" intersect "+v+". Operator not supported for this types.");
	}

	@Override
	public Value union(Value v, Type t) throws UnsupportedOperationException, UnknownValueException {
		throw new UnknownValueException("Error while evaluating "+this+" union "+v+". Operator not supported for this types.");
	}

	@Override
	public Value except(Value v, Type t) throws UnsupportedOperationException, UnknownValueException {
		throw new UnknownValueException("Error while evaluating "+this+" except "+v+". Operator not supported for this types.");
	}

	@Override
	public Value mul(Value v, Type t) throws UnsupportedOperationException, UnknownValueException {
		throw new UnknownValueException("Error while evaluating "+this+" mul "+v+". Operator not supported for this types.");
	}

	@Override
	public Value sub(Value v, Type t) throws UnsupportedOperationException, UnknownValueException {
		throw new UnknownValueException("Error while evaluating "+this+" sub "+v+". Operator not supported for this types.");
	}

	@Override
	public Value div(Value v, Type t) throws UnsupportedOperationException, UnknownValueException {
		throw new UnknownValueException("Error while evaluating "+this+" div "+v+". Operator not supported for this types.");
	}

	@Override
	public Value add(Value v, Type t) throws UnsupportedOperationException, UnknownValueException {
		throw new UnknownValueException("Error while evaluating "+this+" add "+v+". Operator not supported for this types.");
	}
	
	@Override
	public String toString() {
		return Value.NULL;
	}

	@Override
	public boolean isInTheDomain(Type t){
		return true;
	}

	@Override
	public int hashCode() {
		return NULL.hashCode();
	}

	@Override
	public Value clone() {
		return new Null();
	}


}
