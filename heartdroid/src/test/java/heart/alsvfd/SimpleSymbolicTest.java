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

import static heart.alsvfd.Command.EQ;
import static heart.alsvfd.Command.GT;
import static heart.alsvfd.Command.GTE;
import static heart.alsvfd.Command.IN;
import static heart.alsvfd.Command.LT;
import static heart.alsvfd.Command.LTE;
import static heart.alsvfd.Command.NEQ;
import static heart.alsvfd.Command.NOTIN;
import static heart.alsvfd.Command.ADD;
import static heart.alsvfd.Command.SUB;
import static heart.alsvfd.Command.DIV;
import static heart.alsvfd.Command.MUL;
import static heart.alsvfd.Command.UNION;
import static heart.alsvfd.Command.INTERSECT;
import static heart.alsvfd.Command.SIM;
import static heart.alsvfd.Command.NOTISM;
import static heart.alsvfd.Command.EXCEPT;
import static heart.alsvfd.Command.SUBSET;
import static heart.alsvfd.Command.SUPSET;
import static org.junit.Assert.*;
import heart.exceptions.BuilderException;
import heart.exceptions.NotInTheDomainException;
import heart.exceptions.RangeFormatException;
import heart.exceptions.UnknownValueException;
import heart.xtt.Type;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;


public class SimpleSymbolicTest extends AbstractTest {

	private SimpleSymbolic simpleSymbolic;

	private ExceptionTestParameters testParameters;

	@Before
	public void init() {
		simpleSymbolic = new SimpleSymbolic("value", 10);

		testParameters = new ExceptionTestParameters();
		testParameters.expectedException = Exception.class;
		testParameters.exceptedMessage = "";
		testParameters.thisValue = simpleSymbolic;
		testParameters.otherValue = null;
		testParameters.type = null;
	}
	
	@Test
	public void testUnimplementedMethodsThrowException()
			throws BuilderException {
		Command[] commands = { ADD, DIV, SUB, MUL, UNION, INTERSECT, EXCEPT
				,SUPSET, SUBSET, SIM, NOTISM};
		Value value = new SimpleNumeric(15.0);

		testParameters.expectedException = UnsupportedOperationException.class;
		testParameters.exceptedMessage = "Operation not applicable to "
				+ SimpleSymbolic.class.getSimpleName() + " class";
		testParameters.otherValue = value;
		testParameters.type = createTypeWithDomain(Arrays.<Value> asList(
				simpleSymbolic, value));

		for (Command command : commands) {
			testException(command, testParameters);
		}
	}
	
	@Test
	public void testEqAndNeqAcceptNullValue()
			throws UnsupportedOperationException, NotInTheDomainException {
		assertFalse(simpleSymbolic.eq(new Null(), null));
		assertTrue(simpleSymbolic.neq(new Null(), null));
	}

	@Test
	public void testNullValueThrowException() {
		Command[] commands = {IN, NOTIN, LT, LTE, GT, GTE };
		testParameters.expectedException = UnknownValueException.class;
		testParameters.exceptedMessage = getErrorMessage("Null");
		testParameters.otherValue = new Null();

		for (Command command : commands) {
			testException(command, testParameters);
		}
	}

	@Test
	public void testEqAndNeqAcceptAnyValue()
			throws UnsupportedOperationException, NotInTheDomainException {
		assertTrue(simpleSymbolic.eq(new Any(), null));
		assertFalse(simpleSymbolic.neq(new Any(), null));
	}

	@Test
	public void testAnyValueThrowExceptions() {
	   Command[] commands = {IN, NOTIN, LT, LTE, GT, GTE };

		testParameters.expectedException = UnsupportedOperationException.class;
		testParameters.exceptedMessage = getErrorMessage("Any");
		testParameters.otherValue = new Any();

		for (Command command : commands) {
			testException(command, testParameters);
		}
	}
	
	@Test
	public void testThisSetValueIsNotInTheDomainException()
			throws BuilderException {
		Command[] commands = {EQ, NEQ, IN, NOTIN, LT, LTE, GT, GTE };
		SimpleSymbolic value = new SimpleSymbolic("val", 5);

		testParameters.expectedException = NotInTheDomainException.class;
		testParameters.exceptedMessage = getNotInTheDomainMessage(simpleSymbolic);
		testParameters.otherValue = value;
		testParameters.type = createTypeWithDomain(Arrays.<Value> asList(value));

		for (Command command : commands) {
			testException(command, testParameters);
		}
	}

	// Wygląda na to że pusty SetValue zawsze jest w dzidzinie
	// Czy to dobrze?
	@Test
	public void testOtherValueIsNotInTheDomainException()
			throws BuilderException {
		Command[] commands = { EQ, NEQ, IN, NOTIN, LT, LTE, GT, GTE };
		SimpleSymbolic value = new SimpleSymbolic("val", 5);
		

		testParameters.expectedException = NotInTheDomainException.class;
		testParameters.exceptedMessage = getNotInTheDomainMessage(value);
		testParameters.otherValue = value;
		testParameters.type = createTypeWithDomain(Arrays
				.<Value> asList(simpleSymbolic));

		for (Command command : commands) {
			testException(command, testParameters);
		}
	}
	
	@Test
	public void testWrongValueThrowsException() throws BuilderException, RangeFormatException {
		Command[] commands = {EQ, NEQ, LT, LTE, GT, GTE };
		SetValue setValue= new SetValue();
		Range rangeValue = new Range(new SimpleNumeric(5.0), new SimpleNumeric(10.0));
		SimpleNumeric numericValue = new SimpleNumeric(11.0);
		SimpleSymbolic symbolicValue = new SimpleSymbolic("v", 10);
		
		testParameters.expectedException = UnsupportedOperationException.class;
		testParameters.exceptedMessage = getOperationErrorMessage(setValue);
		testParameters.otherValue = setValue;
		testParameters.type = createTypeWithDomain(Arrays.<Value> asList(
				simpleSymbolic, setValue, rangeValue, symbolicValue, numericValue));		
		for (Command command : commands) {
			testException(command, testParameters);					
		}
		
		testParameters.exceptedMessage = getOperationErrorMessage(rangeValue);
		testParameters.otherValue = rangeValue;
		for (Command command : commands) {
			testException(command, testParameters);					
		}
		
		testParameters.exceptedMessage = getOperationErrorMessage(numericValue);
		testParameters.otherValue = numericValue;
		
		Command[] commands2 = {IN, NOTIN };
		for (Command command : commands2) {
			testException(command, testParameters);					
		}
		
		testParameters.exceptedMessage = getOperationErrorMessage(symbolicValue);
		testParameters.otherValue = symbolicValue;
				
		for (Command command : commands2) {
			testException(command, testParameters);					
		}
	}
	
	// @Test
	public void testNotOrderedValue() throws BuilderException {
		Command[] commands = {LT, LTE, GT, GTE };
		SimpleSymbolic symbolicValue = new SimpleSymbolic("v");
		SimpleNumeric numericValue = new SimpleNumeric(10.0);
		
		simpleSymbolic.setOrder(null);
		
		testParameters.expectedException = UnsupportedOperationException.class;
		testParameters.exceptedMessage = SimpleSymbolic.NOT_ORDERED_ERROR_MESSAGE;
		testParameters.otherValue = symbolicValue;
		testParameters.type = createTypeWithDomain(Arrays.<Value> asList(
				simpleSymbolic, symbolicValue, numericValue));		
		for (Command command : commands) {
			testException(command, testParameters);					
		}
		
		testParameters.otherValue = numericValue;
		Command[] commands2 = {EQ, NEQ};	
		// test failuje nie bez powodu
		// findInTheDomain(SimpleNUmeric ma bugg, getOrder moze byc null

		for (Command command : commands2) {
			testException(command, testParameters);					
		}
	}
	
	@Test
	//Nie jestem pewny czy to ma prawo tutaj rzucac wyjatkiem
	public void testEqAndNeqLogic() throws BuilderException, UnsupportedOperationException, NotInTheDomainException{
		SimpleSymbolic value = new SimpleSymbolic("value", 1);
		SimpleSymbolic value2 = new SimpleSymbolic("value 2", 11);
		SimpleNumeric value3 = new SimpleNumeric(10.0);
		SimpleNumeric value4 = new SimpleNumeric(15.0);
		SimpleSymbolic value5 = new SimpleSymbolic("value 5", 15);
		Type type = createTypeWithDomain(Arrays.<Value> asList(
				simpleSymbolic, value, value2, value3, value4, value5));
		
		assertTrue(simpleSymbolic.eq(value, type));
		assertFalse(simpleSymbolic.neq(value, type));
		assertFalse(simpleSymbolic.eq(value2, type));
		assertTrue(simpleSymbolic.neq(value2, type));
		assertTrue(simpleSymbolic.eq(value3, type));
		assertFalse(simpleSymbolic.neq(value3, type));
		assertFalse(simpleSymbolic.eq(value4, type));
		assertTrue(simpleSymbolic.neq(value4, type));
	}
	
	@Test
	public void testComparisonOperators() throws BuilderException, UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		SimpleNumeric value = new SimpleNumeric(5.0);
		SimpleNumeric value2 = new SimpleNumeric(10.0);
		SimpleNumeric value3 = new SimpleNumeric(15.0);
		SimpleSymbolic value4 = new SimpleSymbolic("val4", 5);
		SimpleSymbolic value5 = new SimpleSymbolic("val5", 10);
		SimpleSymbolic value6 = new SimpleSymbolic("val6", 15);
		
		Type type = createTypeWithDomain(Arrays.<Value> asList(
				simpleSymbolic, value, value2, value3, value4, value5, value6));
		
		assertTrue(simpleSymbolic.gt(value, type));
		assertTrue(simpleSymbolic.gt(value4, type));
		assertFalse(simpleSymbolic.gt(value2, type));
		assertFalse(simpleSymbolic.gt(value3, type));
		assertFalse(simpleSymbolic.gt(value5, type));
		assertFalse(simpleSymbolic.gt(value6, type));
		
		assertTrue(simpleSymbolic.gte(value, type));
		assertTrue(simpleSymbolic.gte(value4, type));
		assertTrue(simpleSymbolic.gte(value2, type));
		assertTrue(simpleSymbolic.gte(value5, type));
		assertFalse(simpleSymbolic.gt(value3, type));
		assertFalse(simpleSymbolic.gt(value6, type));
		
		assertTrue(simpleSymbolic.lt(value3, type));
		assertTrue(simpleSymbolic.lt(value6, type));
		assertFalse(simpleSymbolic.lt(value, type));
		assertFalse(simpleSymbolic.lt(value2, type));
		assertFalse(simpleSymbolic.lt(value4, type));
		assertFalse(simpleSymbolic.lt(value5, type));
		
		assertTrue(simpleSymbolic.lte(value3, type));
		assertTrue(simpleSymbolic.lte(value6, type));
		assertFalse(simpleSymbolic.lte(value, type));
		assertTrue(simpleSymbolic.lte(value2, type));
		assertFalse(simpleSymbolic.lte(value4, type));
		assertTrue(simpleSymbolic.lte(value5, type));
	}	
	
	@Test
	public void testInAndNotinLogic() throws BuilderException, UnsupportedOperationException, NotInTheDomainException, RangeFormatException, UnknownValueException {
		SimpleSymbolic value = new SimpleSymbolic("value1", 5);
		SimpleSymbolic value2 = new SimpleSymbolic("value", 10);
		SimpleSymbolic value3 = new SimpleSymbolic("value 2", 15);
		SimpleSymbolic value4 = new SimpleSymbolic("v", 5);
		SimpleSymbolic value5 = new SimpleSymbolic("v2", 15);
		SimpleSymbolic value6 = new SimpleSymbolic("v3", 9);
		
		SetValue setValue1 = new SetValue(Arrays.<Value>asList(value,value2,value3));
		SetValue setValue2 = new SetValue(Arrays.<Value>asList(value, value3));
		
		Range rangeValue1 = new Range(value4, value5);
		Range rangeValue2 = new Range(value4, value6);
		Range rangeValue3 = new Range(new SimpleNumeric(5.0), new SimpleNumeric(15.0));
		Range rangeValue4 = new Range(new SimpleNumeric(5.0), new SimpleNumeric(9.0));
		
		Type type = createTypeWithDomain(Arrays.<Value> asList(
				simpleSymbolic, setValue1, setValue2, value, value2, value3,
				value4, value5, value6, rangeValue1, 
				rangeValue2, rangeValue3, rangeValue4));
		
		assertTrue(simpleSymbolic.in(setValue1, type));
		assertFalse(simpleSymbolic.notin(setValue1, type));
		assertTrue(simpleSymbolic.notin(setValue2, type));
		assertFalse(simpleSymbolic.in(setValue2, type));
		
		assertTrue(simpleSymbolic.in(rangeValue1, type));
		assertTrue(simpleSymbolic.in(rangeValue3, type));
		assertFalse(simpleSymbolic.notin(rangeValue1, type));
		assertFalse(simpleSymbolic.notin(rangeValue3, type));
		
		assertFalse(simpleSymbolic.in(rangeValue2, type));
		assertFalse(simpleSymbolic.in(rangeValue4, type));
		assertTrue(simpleSymbolic.notin(rangeValue2, type));
		assertTrue(simpleSymbolic.notin(rangeValue4, type));
		
	}
	
	private String getErrorMessage(String comparedWith) {
		return "SimpleSymbolic " + simpleSymbolic + " can not be compared with "
				+ comparedWith;
	}
	
	private String getOperationErrorMessage(Value value){
		return "This SimpleSymbolic operation is not defined for value " + value;
	}
}
