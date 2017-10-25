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

import heart.alsvfd.expressions.ExpressionInterface;
import heart.WorkingMemory;
import heart.alsvfd.expressions.ExpressionBuilderInterface;
import heart.exceptions.BuilderException;
import heart.exceptions.NotInTheDomainException;
import heart.exceptions.StaticEvaluationException;
import heart.exceptions.UnknownValueException;
import heart.xtt.Attribute;
import heart.xtt.Type;
import java.util.Map;

/**
 * An abstract class representing a Value in ALSV(FD) logic.
 * Values can be of five types in ALSV(FD): simple symbolic, simple numeric
 * range symbolic, range numeric and set.
 * 
 * This is a base class for all of the more specific classes.
 * 
 * @author sbk
 *
 */
public abstract class Value implements ExpressionInterface, ExpressionBuilderInterface {
	
	/**
	 * A meta value ID representing any value. 
	 * This value is one of the ALSV(FD) values that an attribute can take.
	 * It means that the attribute have some value, but we do not know what and it does not matter.
	 * It is not equivalent to null, which means that there is no knowledge about the value of a variable.
	 * This value is considered with respect to domains of attributes, so comparing "a lt any" 
	 * is not always true, as "a" may be the biggest value in the domain.
	 * 
	 * Any value is different form null value. Null means that we have no knowledge about the value.
	 */
	public static final String ANY = "any";
	
	/**
	 * A meta value ID representing a state of absolute lack of knowledge about the attribute value.
	 */
	public static final String NULL = "null";
	
	
	/**
	 * A variable representing a certainty factor of a value assigned to it.
	 * By default this value equals 1, which means that the value is absolutely certain
	 * and there is no doubts that it may be different.
	 */
	private float certaintyFactor = 1;
	
	
	/**
	 * Timestamp in milliseconds indicating a time when the value was created/read/obtained.
	 */
	private long timestamp;
	
	/**
	 * This is the constructor that is invoked every time a Value is being created.
	 * It sets the timestamp of a value to the current system time.
	 */
	protected Value(){
		setTimestamp(System.currentTimeMillis());
	}
	
	/**
	 * A method that should be used to compare two values for equality.
	 * If one or two of the values equal {@link #ANY}, function will return true immediately.
	 * 
	 * @param v - a value to compare
	 * @param t - a type with respect to witch the operator should work. If null, no domain is taken into consideration while evaluating.
	 * @return true if the values are equal or at least one equals {@link #ANY}, false otherwise.
	 * @throws UnsupportedOperationException in cases when operator is not applicable to the values being parameters of the operation.
	 * @throws NotInTheDomainException 
	 */
	public  abstract boolean eq(Value v, Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException;
	
	/**
	 * A method that should be used to compare two values for not equality.
	 * It is opposite to {@link #eq}.
	 * @param v - a value to compare
	 * @param t - a type with respect to witch the operator should work. If null, no domain is taken into consideration while evaluating.
	 * @return true if values are different, or in case when parameter v equals null, false otherwise.
	 * @throws UnsupportedOperationException in cases when operator is not applicable to the values being parameters of the operation.
	 * @throws NotInTheDomainException 
	 */
	public abstract boolean neq(Value v, Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException;
	
	/**
	 * A method that should be used  to compare two values.
	 * It checks weather the value represented by an object that calls the method is greater than the value of a parameter.
	 * The method should be implemented in simple classes that operates on numbers 
	 * like {@link SimpleNumeric}, but also in simple classes that introduce order in domain,
	 * like {@link SimpleSymbolic} with ordered domain. 
	 * @param v - a value to compare
	 * @param t - a type with respect to witch the operator should work. If null, no domain is taken into consideration while evaluating.
	 * @return true if the Value v is greater than the value of an object, false otherwise.
	 * @throws UnsupportedOperationException in cases when operator is not applicable to the values being parameters of the operation.
	 * @throws NotInTheDomainException 
	 */
	public boolean gt(Value v, Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		throw new UnsupportedOperationException(generateErrorMessage());
	}
	
	/**
	 * A method that should be used to compare two values.
	 * It checks weather the value represented by an object that calls the method is greater or equal to the value of a parameter.
	 * The method should be implemented in simple classes that operates on numbers 
	 * like {@link SimpleNumeric}, but also in simple classes that introduce order in domain,
	 * like {@link SimpleSymbolic} with ordered domain. 
	 * @param v - a value to compare
	 * @param t - a type with respect to witch the operator should work. If null, no domain is taken into consideration while evaluating.
	 * @return true if the Value v is greater or equal the value of an object, or in case when one or both of compared values are equal to {@link #ANY}, false otherwise.
	 * @throws UnsupportedOperationException in cases when operator is not applicable to the values being parameters of the operation.
	 * @throws NotInTheDomainException 
	 */
	public boolean gte(Value v, Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		throw new UnsupportedOperationException(generateErrorMessage());
	}
	
	/**
	 * A method that should be used to compare two values.
	 * It checks weather the value represented by an object that calls the method is less than the value of a parameter.
	 * The method should be implemented in simple classes that operates on numbers 
	 * like {@link SimpleNumeric}, but also in simple classes that introduce order in domain,
	 * like {@link SimpleSymbolic} with ordered domain. 
	 * @param v - a value to compare
	 * @param t - a type with respect to witch the operator should work. If null, no domain is taken into consideration while evaluating.
	 * @return true if the Value v is less than the value of an object, false otherwise.
	 * @throws UnsupportedOperationException in cases when operator is not applicable to the values being parameters of the operation.
	 * @throws NotInTheDomainException 
	 */
	public boolean lt(Value v, Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		throw new UnsupportedOperationException(generateErrorMessage());
	}
	
	/**
	 * A method that should be used to compare two values.
	 * It checks weather the value represented by an object that calls the method is less or equal to the value of a parameter.
	 * The method should be implemented in simple classes that operates on numbers 
	 * like {@link SimpleNumeric}, but also in simple classes that introduce order in domain,
	 * like {@link SimpleSymbolic} with ordered domain. 
	 * @param v - a value to compare
	 * @param t - a type with respect to witch the operator should work. If null, no domain is taken into consideration while evaluating.
	 * @return true if the Value v is less than or equal the value of an object, or in case when one or both of compared values are equal to {@link #ANY}, false otherwise.
	 * @throws UnsupportedOperationException in cases when operator is not applicable to the values being parameters of the operation.
	 * @throws NotInTheDomainException 
	 */
	public boolean lte(Value v, Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		throw new UnsupportedOperationException(generateErrorMessage());
	}
	
	/**
	 * An operator that should be used to check membership of a value of an object that calls the method 
	 * within a value given by the parameter.
	 * The value of the parameter is intended to be a {@link SetValue}.
	 * @param v - a value to be processed
	 * @param t - a type with respect to witch the operator should work. If null, no domain is taken into consideration while evaluating.
	 * @return true if the value is a member of the value given as a parameter, false otherwise
	 * @throws UnsupportedOperationException in cases when operator is not applicable to the values being parameters of the operation.
	 * @throws NotInTheDomainException 
	 */
	//sa sprzeczne wersje co do sugesti jakiej metody uzyc SetValue tweirdzi ze subset Range ze intersect
	public boolean in(Value v, Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		throw new UnsupportedOperationException(generateErrorMessage());
	}
	
	/**
	 * An operator that should be used to check membership of a value of an object that calls the method 
	 * within a value given by the parameter.
	 * This method is opposite to {@link #in}.
	 * The value of the parameter is intended to be a {@link SetValue}.
	 * @param v - a value to be processed
	 * @param t - a type with respect to witch the operator should work. If null, no domain is taken into consideration while evaluating.
	 * @return false if the value is a member of the value given as a parameter, true otherwise or if a parameter equal {link #ANY}.
	 * @throws UnsupportedOperationException in cases when operator is not applicable to the values being parameters of the operation.
	 * @throws NotInTheDomainException 
	 */
	public boolean notin(Value v, Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		throw new UnsupportedOperationException(generateErrorMessage());
	}
	
	/**
	 * A method that should be used to check if the value is a superset of a value given as a parameter.
	 *
	 * @param v - a value to be processed. Both values should be a sets, or ranges.
	 * @param t - a type with respect to witch the operator should work. If null, no domain is taken into consideration while evaluating.
	 * @return true if the value is superset of v, false otherwise
	 * @throws UnsupportedOperationException in cases when operator is not applicable to the values being parameters of the operation.
	 * @throws NotInTheDomainException 
	 */
	public boolean supset(Value v, Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		throw new UnsupportedOperationException(generateErrorMessage());
	}
	
	/**
	 * A method that should be used to check is the value is a subset of value given as a parameter.
	 * 
	 * @param v - a value to be processed. Both values should be a sets, or ranges.
	 * @param t - a type with respect to witch the operator should work. If null, no domain is taken into consideration while evaluating.
	 * @return true if the value is subset of v, false otherwise
	 * @throws UnsupportedOperationException in cases when operator is not applicable to the values being parameters of the operation.
	 * @throws NotInTheDomainException 
	 */
	public boolean subset(Value v, Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		throw new UnsupportedOperationException(generateErrorMessage());
	}
	
	/**
	 * A method that should be used to check if the value has a nonempty intersection with a value given as a parameter.
	 * @param v - a value to be processed. Both values should be sets or ranges.
	 * @param t - a type with respect to witch the operator should work. If null, no domain is taken into consideration while evaluating.
	 * @return true if the value has non-empty intersection with parameter v, false otherwise
	 * @throws UnsupportedOperationException in cases when operator is not applicable to the values being parameters of the operation.
	 * @throws NotInTheDomainException 
	 */
	public boolean sim(Value v, Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		throw new UnsupportedOperationException(generateErrorMessage());
	}
	
	/**
	 * A method that should be used to check if the value has a nonempty intersection with a value given as a parameter.
	 * This method is opposite to {@link #sim}.
	 * @param v - a value to be processed. Both values should be sets or ranges.
	 * @param t - a type with respect to witch the operator should work. If null, no domain is taken into consideration while evaluating.
	 * @return false if the value has non-empty intersection with parameter v or if a parameter equal {link #ANY}, true otherwise.
	 * @throws UnsupportedOperationException in cases when operator is not applicable to the values being parameters of the operation.
	 * @throws NotInTheDomainException 
	 */
	public boolean notsim(Value v, Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		throw new UnsupportedOperationException(generateErrorMessage());
	}
	
	/**
	 * A method that should be used to calculate intersection with the value given as a parameter.
	 * @param v - a value to be processed. Both values should be a sets, or ranges.
	 * @param t - a type with respect to witch the operator should work. If null, no domain is taken into consideration while evaluating.
	 * @return A value that represent intersection between a set represented by an object and the parameter
	 * @throws UnsupportedOperationException in cases when operator is not applicable to the values being parameters of the operation.
	 * @throws NotInTheDomainException 
	 */
	public Value intersect(Value v, Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		throw new UnsupportedOperationException(generateErrorMessage());
	}
	
	/**
	 * A method that should be used to calculate a sum of two sets.
	 * 
	 * @param v - a value to be processed.  Both values should be a sets, or ranges.
	 * @param t - a type with respect to witch the operator should work. If null, no domain is taken into consideration while evaluating.
	 * @return sum of a set represented by an object and the parameter
	 * @throws UnsupportedOperationException in cases when operator is not applicable to the values being parameters of the operation.
	 * @throws NotInTheDomainException 
	 */
	public Value union(Value v, Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		throw new UnsupportedOperationException(generateErrorMessage());
	}
	
	/**
	 * An abstract method that should be used to calculate set that subtracts the value given as the parameter from the set.
	 * 
	 * @param v - a value to be processed. It can be both simple type or general type.
	 * @param t - a type with respect to witch the operator should work. If null, no domain is taken into consideration while evaluating.
	 * @return Resulting set that represents the value of the object without the value represented by parameter v.
	 * @throws UnsupportedOperationException in cases when operator is not applicable to the values being parameters of the operation.
	 * @throws NotInTheDomainException 
	 */
	public Value except(Value v, Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		throw new UnsupportedOperationException(generateErrorMessage());
	}

	
	/**
	 * A method that should be used to calculate  multiplication between a value represented 
	 * by an object that calls the method and a value given as a parameter.
	 * 
	 * @param v - a value to be processed. It should be a numerical type.
	 * @param t - a type with respect to witch the operator should work. If null, no domain is taken into consideration while evaluating.
	 * @return multiplication of the value represented by an object that calls the method and a value given as a parameter.
	 * @throws UnsupportedOperationException in cases when operator is not applicable to the values being parameters of the operation.
	 * @throws NotInTheDomainException 
	 */
	public Value mul(Value v, Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		throw new UnsupportedOperationException(generateErrorMessage());
	}
	
	/**
	 * A method that should be used to calculate difference between a value represented 
	 * by an object that calls the method and a value given as a parameter.
	 * 
	 * @param v - a value to be processed. It should be a numerical type, or ordered symbolic.
	 * @param t - a type with respect to witch the operator should work. If null, no domain is taken into consideration while evaluating.
	 * @return difference between the value represented by an object that calls the method and the value given as a parameter.
	 * @throws UnsupportedOperationException in cases when operator is not applicable to the values being parameters of the operation.
	 * @throws NotInTheDomainException 
	 */
	public Value sub(Value v, Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		throw new UnsupportedOperationException(generateErrorMessage());
	}
	
	/**
	 * A method that should be used to calculate integral between a value represented 
	 * by an object that calls the method and a value given as a parameter.
	 * 
	 * @param v - a value to be processed. It should be a numerical type.
	 * @param t - a type with respect to witch the operator should work. If null, no domain is taken into consideration while evaluating.
	 * @return integral between the value represented by an object that calls the method and the value given as a parameter.
	 * @throws UnsupportedOperationException in cases when operator is not applicable to the values being parameters of the operation.
	 * @throws NotInTheDomainException 
	 */
	public Value div(Value v, Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		throw new UnsupportedOperationException(generateErrorMessage());
	}
	
	/**
	 * A method that should be used to calculate sum between a value represented 
	 * by an object that calls the method and a value given as a parameter.
	 * 
	 * @param v - a value to be processed. It should be a numerical type, or ordered symbolic.
	 * @param t - a type with respect to witch the operator should work. If null, no domain is taken into consideration while evaluating.
	 * @return sum between the value represented by an object that calls the method and the value given as a parameter.
	 * @throws UnsupportedOperationException in cases when operator is not applicable to the values being parameters of the operation.
	 * @throws NotInTheDomainException 
	 */
	public Value add(Value v, Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		throw new UnsupportedOperationException(generateErrorMessage());
	}

	/**
	 * A method that should be used to calculate power of a value represented
	 * by an object that calls the method to the value given as a parameter.
	 *
	 * @param v - a value to be processed. It should be a numerical type.
	 * @param t - a type with respect to witch the operator should work. If null, no domain is taken into consideration while evaluating.
	 * @return power of value represented by an object that calls the method to the value given as a parameter.
	 * @throws UnsupportedOperationException in cases when operator is not applicable to the values being parameters of the operation.
	 * @throws NotInTheDomainException
	 */
	public Value pow(Value v, Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		throw new UnsupportedOperationException(generateErrorMessage());
	}

	/**
	 * A method that should be used to calculate modulo integral between a value represented
	 * by an object that calls the method and a value given as a parameter.
	 *
	 * @param v - a value to be processed. It should be a numerical type.
	 * @param t - a type with respect to witch the operator should work. If null, no domain is taken into consideration while evaluating.
	 * @return modulo integral between the value represented by an object that calls the method and the value given as a parameter.
	 * @throws UnsupportedOperationException in cases when operator is not applicable to the values being parameters of the operation.
	 * @throws NotInTheDomainException
	 */
	public Value mod(Value v, Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		throw new UnsupportedOperationException(generateErrorMessage());
	}

	/**
	 * A method that should be used to calculate factorial of the value represented by an object that calls the method.
	 *
	 * @return factorial of the value represented by an object that calls the method.
	 * @throws UnsupportedOperationException in cases when operator is not applicable to the values being parameters of the operation.
	 * @throws NotInTheDomainException
	 */
	public Value factorial() throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		throw new UnsupportedOperationException(generateErrorMessage());
	}

	/**
	 * An abstract method that should be used to calculate powerset of a value represented by an object that
	 * calls the method.
	 * @return Resulting set that represents the powerset of a value represented by an object that calls the method.
	 * @throws UnsupportedOperationException in cases when operator is not applicable to the values being parameters of the operation.
	 * @throws NotInTheDomainException
	 */
	public Value powerset() throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		throw new UnsupportedOperationException(generateErrorMessage());
	}
	
	/**
	 * A method that searches for a value in a domain of {@link Type}.
	 * This is useful if ordered field in {@link SimpleSymbolic} value is not known.
	 * The method will return the value with its ordered number if exists.
	 * 
	 * If {@link Null} calls this method, false is always returned.
	 * If {@link Any} calls this method true is returned if the domain is not empty.
	 * 
	 * @return true if the value is in the domain, false otherwise
	 * @throws NotInTheDomainException 
	 * @throws UnsupportedOperationException 
	 */
	public abstract boolean isInTheDomain(Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException;

	/**
	 * A method used to get the certainty factor of a value. 
	 * The certainty factor vary from -1 to 1 representing respectively
	 * Absolute negation of the value and absolute certainty of the value.
	 * 
	 * @return certainty factor associated with the value
	 */
	public float getCertaintyFactor() {
		return certaintyFactor;
	}

	/**
	 * Sets the certainty factor of a value.
	 * 
	 * @param certaintyFactor
	 */
	public void setCertaintyFactor(float certaintyFactor) {
		this.certaintyFactor = certaintyFactor;
	}
	
	/**
	 * Checks if current instance and specified value are in the domain
	 * @param v - a value to be processed. It may be any of the Value implementation class.
	 * @param t - a type with respect to witch the operator should work.
	 * @throws NotInTheDomainException thrown if either this instance or v param is not in the domain
	 */
	protected void checkDomain(Value v, Type t) throws NotInTheDomainException, UnknownValueException {
		if(t != null){
			if(!this.isInTheDomain(t)) throw new NotInTheDomainException(t.getDomain(), this, "Value "+this+" not in the domain");
			if(!v.isInTheDomain(t)) throw new NotInTheDomainException(t.getDomain(), v, "Value "+v+" not in the domain");
		}
	}
	
	/**
	 * Create error message for UnsupportedOperationExsception
	 * @return String representing error message
	 */
	private String generateErrorMessage() {
		return "Operation not applicable to "+ this.getClass().getSimpleName() + " class";
	}	
	
	/**
	 * Returns the time when the value was achieved.
	 * 
	 * @return timestamp in milliseconds
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * Sets the timestamp of the value. This represents the time in milliseconds 
	 * which represents a point in time when the value was read/obtained
	 * 
	 * @param timestamp
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public Value evaluate(WorkingMemory wm) {
		return this;
	}

	@Override
	public ExpressionInterface build(Map<String, Attribute> atts) throws BuilderException {
		return this;
	}

	@Override
	public Value staticEvaluate(Map<String, Attribute> atts) throws StaticEvaluationException {
		return this;
	}

    @Override
    public boolean equals(Object obj) {
        try {
            return this.eq((Value)obj,null);
        } catch (NotInTheDomainException e) {
            return false;
        } catch (UnknownValueException e) {
            return false;
        }catch(Exception e){
            //For any other case return false, to preserve consistency in Java methods that uses equals
            return false;
        }
    }

	@Override
	public abstract int hashCode();

	public abstract Value clone();

	/**
	 * A value should always return null for the type.
	 * Types of values are determined by the attributes with which they are compared.
	 *
	 * @param wm The {@link heart.WorkingMemory} object with respect to which a type of an expression should be evaluated
	 * @return type of the expression
	 */
	public  Type evaluateType(WorkingMemory wm){
		return null;
	}
}
