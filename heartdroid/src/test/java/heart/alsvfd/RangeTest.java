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

import static heart.alsvfd.Command.ADD;
import static heart.alsvfd.Command.DIV;
import static heart.alsvfd.Command.EQ;
import static heart.alsvfd.Command.EXCEPT;
import static heart.alsvfd.Command.GT;
import static heart.alsvfd.Command.GTE;
import static heart.alsvfd.Command.IN;
import static heart.alsvfd.Command.INTERSECT;
import static heart.alsvfd.Command.LT;
import static heart.alsvfd.Command.LTE;
import static heart.alsvfd.Command.MUL;
import static heart.alsvfd.Command.NEQ;
import static heart.alsvfd.Command.NOTIN;
import static heart.alsvfd.Command.NOTISM;
import static heart.alsvfd.Command.SIM;
import static heart.alsvfd.Command.SUB;
import static heart.alsvfd.Command.SUBSET;
import static heart.alsvfd.Command.SUPSET;
import static heart.alsvfd.Command.UNION;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import heart.exceptions.BuilderException;
import heart.exceptions.NotInTheDomainException;
import heart.exceptions.RangeFormatException;
import heart.exceptions.UnknownValueException;
import heart.xtt.Type;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;


public class RangeTest extends AbstractTest {

	private Range range;

	private ExceptionTestParameters testParameters;

	@Before
	public void init() throws RangeFormatException {
		range = new Range(new SimpleNumeric(5.0), new SimpleNumeric(10.0));

		testParameters = new ExceptionTestParameters();
		testParameters.expectedException = Exception.class;
		testParameters.exceptedMessage = "";
		testParameters.thisValue = range;
		testParameters.otherValue = null;
		testParameters.type = null;
	}
	
	@Test
	public void testUnimplementedMethodsThrowException()
			throws BuilderException {
		Command[] commands = { ADD, DIV, SUB, MUL, GT, GTE, LT, LTE, IN, NOTIN };
		Value value = new SimpleNumeric(15.0);

		testParameters.expectedException = UnsupportedOperationException.class;
		testParameters.exceptedMessage = "Operation not applicable to "
				+ Range.class.getSimpleName() + " class";
		testParameters.otherValue = value;
		testParameters.type = createTypeWithDomain(Arrays.<Value> asList(
				range, value));

		for (Command command : commands) {
			testException(command, testParameters);
		}
	}
	
	@Test
	public void testEqAndNeqAcceptNullValue()
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		assertFalse(range.eq(new Null(), null));
		assertTrue(range.neq(new Null(), null));
	}

	@Test
	public void testNullValueThrowException() {
		Command[] commands = { SUPSET, SUBSET , SIM, NOTISM, INTERSECT, UNION,
				EXCEPT
        };
		testParameters.expectedException = UnknownValueException.class;
		testParameters.exceptedMessage = getErrorMessage("Null");
		testParameters.otherValue = new Null();

		for (Command command : commands) {
			testException(command, testParameters);
		}
	}

	@Test
	public void testEqAndNeqAcceptAnyValue()
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		assertTrue(range.eq(new Any(), null));
		assertFalse(range.neq(new Any(), null));
	}

	@Test
	public void testAnyValueThrowExceptions() {
		Command[] commands = { SUPSET, SUBSET, SIM, NOTISM, INTERSECT, UNION,
				EXCEPT };

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
		Command[] commands = { SUPSET, SUBSET, SIM, NOTISM, INTERSECT, UNION,
				EXCEPT, EQ, NEQ };
		SetValue value = new SetValue();

		testParameters.expectedException = NotInTheDomainException.class;
		testParameters.exceptedMessage = getNotInTheDomainMessage(range);
		testParameters.otherValue = value;
		testParameters.type = createTypeWithDomain(Arrays.<Value> asList(value));

		for (Command command : commands) {
			testException(command, testParameters);
		}
	}

	@Test
	public void testOtherValueIsNotInTheDomainException()
			throws BuilderException {
		Command[] commands = { SUPSET, SUBSET, SIM, NOTISM, INTERSECT, UNION,
				EXCEPT, EQ, NEQ };
		SetValue value = new SetValue(Arrays.asList((Value) new SimpleNumeric(
				15.0)));
		

		testParameters.expectedException = NotInTheDomainException.class;
		testParameters.exceptedMessage = getNotInTheDomainMessage(value);
		testParameters.otherValue = value;
		testParameters.type = createTypeWithDomain(Arrays
				.<Value> asList(range));

		for (Command command : commands) {
			testException(command, testParameters);
		}
	}
	
	@Test
	public void testOtherValuesThanSetValueAndRangeAreNotAccepted()
			throws BuilderException {
		Command[] commands = { SUPSET, SIM, NOTISM, INTERSECT, UNION};
		Value value = new SimpleNumeric(15.0);

		testParameters.expectedException = UnsupportedOperationException.class;
		testParameters.exceptedMessage = Range.RANGE_SET_ERROR_MESSAGE;
		testParameters.otherValue = value;
		testParameters.type = createTypeWithDomain(Arrays.<Value> asList(
				range, value));

		for (Command command : commands) {
			testException(command, testParameters);
		}
		
		testParameters.type = createTypeWithDomain(Arrays.<Value> asList(
				range, value));

		for (Command command : commands) {
			testException(command, testParameters);
		}
		
		testParameters.exceptedMessage = Range.EQ_ERROR_MESSAGE;
		Command[] commands2 = {EQ,NEQ};
		for (Command command : commands2) {
			testException(command, testParameters);
		}
	}
	
	@Test
	public void testEqAndNeqLogic() throws BuilderException, UnsupportedOperationException, NotInTheDomainException, RangeFormatException, UnknownValueException {
		Range value = new Range(new SimpleNumeric(5.0), new SimpleNumeric(10.0));
		Range value2 = new Range(new SimpleNumeric(15.0), new SimpleNumeric(20.0));
		Type type = createTypeWithDomain(Arrays.<Value>asList(range, value, value2));
		
		assertTrue(range.eq(value, type));
		assertFalse(range.neq(value, type));
		value.setLeftInclusive(false);
		assertFalse(range.eq(value, type));
		assertTrue(range.neq(value, type));
		assertFalse(range.eq(value2, type));
		assertTrue(range.neq(value2, type));
		value.setLeftInclusive(true);
		value.setRightInclusive(false);
		assertFalse(range.eq(value, type));
		assertTrue(range.neq(value, type));
	}
	
	@Test
	public void testSimNotsimLogic() throws BuilderException, UnsupportedOperationException, NotInTheDomainException, RangeFormatException, UnknownValueException {
		Range value = new Range(new Range(new SimpleNumeric(4.0), new SimpleNumeric(8.0)));		
		SetValue value2 = new SetValue(Arrays.<Value>asList(new SimpleNumeric(6.0)));
		Range value3 = new Range(new Range(new SimpleNumeric(15.0), new SimpleNumeric(20.0)));
		SetValue value4 = new SetValue(Arrays.<Value>asList(new SimpleNumeric(12.0)));
		
		Type type = createTypeWithDomain(Arrays.<Value>asList(range, value, value2, value3, value4));
		
		assertTrue(range.sim(value, type));
		assertTrue(range.sim(value2, type));
		assertFalse(range.notsim(value, type));
		assertFalse(range.notsim(value2, type));
		assertTrue(range.notsim(value3, type));
		assertFalse(range.sim(value3, type));
		assertTrue(range.notsim(value4, type));
		assertFalse(range.sim(value4, type));
	}
	
	@Test
	public void testSupsetAndSubsetLogic() throws BuilderException, UnsupportedOperationException, NotInTheDomainException, RangeFormatException, UnknownValueException {
		Range value = new Range(new Range(new SimpleNumeric(4.0), new SimpleNumeric(18.0)));		
		SetValue value2 = new SetValue(Arrays.<Value>asList(new SimpleNumeric(6.0)));
		Range value3 = new Range(new Range(new SimpleNumeric(6.0), new SimpleNumeric(9.0)));
		SetValue value4 = new SetValue(Arrays.<Value>asList(new SimpleNumeric(5.0), new SimpleNumeric(10.0)));
		Type type = createTypeWithDomain(Arrays.<Value>asList(range, value, value2, value3, value4));
		
		assertFalse(range.supset(value, type));
		assertTrue(range.subset(value, type));
		assertTrue(range.supset(value3, type));
		assertFalse(range.subset(value3, type));
		assertTrue(range.supset(value2, type));
		assertFalse(range.subset(value2, type));
		//nie umiem wymyślić przypadku dla którego range mogłby być podzbiorem set value
		//assertTrue(range.subset(value4, type));
	}
	
	@Test
	public void testIntersectLogic() throws BuilderException, UnsupportedOperationException, NotInTheDomainException, RangeFormatException, UnknownValueException {
		Range value = new Range(new SimpleNumeric(4.0), new SimpleNumeric(18.0));		
		SetValue value2 = new SetValue(Arrays.<Value>asList(new SimpleNumeric(6.0)));
		Range value3 = new Range(new SimpleNumeric(6.0), new SimpleNumeric(9.0));
		SetValue value4 = new SetValue(Arrays.<Value>asList(new SimpleNumeric(2.0), new SimpleNumeric(18.0)));
		Range value5 = new Range(new SimpleNumeric(4.0), new SimpleNumeric(8.0));
		Range expected = new Range(new SimpleNumeric(5.0), new SimpleNumeric(8.0));
		Range value6 = new Range(new SimpleNumeric(14.0), new SimpleNumeric(18.0));
		Type type = createTypeWithDomain(Arrays.<Value>asList(range, value, value2, 
				value3, value4, value5));
		
		SetValue result = (SetValue) range.intersect(value, type);
		assertTrue(result.values.get(0).eq(range, type));
		result = (SetValue) range.intersect(value3, type);
		assertTrue(result.values.get(0).eq(value3, type));
		result = (SetValue) range.intersect(value5, type);
		assertTrue(result.values.get(0).eq(expected, type));
		result = (SetValue) range.intersect(value2, type);		
		assertTrue(result.values.get(0).eq(new SimpleNumeric(6.0), type));
		result = (SetValue) range.intersect(value4, type);
		assertTrue(result.values.isEmpty());
		result = (SetValue) range.intersect(value6, type);
		assertTrue(result.values.isEmpty());		
	}	
	
	// @Test
	public void testUnionLogic() throws BuilderException, UnsupportedOperationException, NotInTheDomainException, RangeFormatException, UnknownValueException {
		Range value = new Range(new SimpleNumeric(4.0), new SimpleNumeric(18.0));		
		SetValue value2 = new SetValue(Arrays.<Value>asList(new SimpleNumeric(6.0)));
		Range value3 = new Range(new SimpleNumeric(6.0), new SimpleNumeric(9.0));
		SetValue value4 = new SetValue(Arrays.<Value>asList(new SimpleNumeric(2.0), new SimpleNumeric(18.0)));
		Range value5 = new Range(new SimpleNumeric(2.0), new SimpleNumeric(8.0));		
		Range expected = new Range(new SimpleNumeric(2.0), new SimpleNumeric(10.0));
		SetValue value6 = new SetValue(Arrays.<Value>asList(new SimpleNumeric(4.0),
				new SimpleNumeric(8.0), value4, value));
		Type type = createTypeWithDomain(Arrays.<Value>asList(range, value, value2, 
				value3, value4, value5, value6));
		
		SetValue result = (SetValue) range.union(value, type);
		assertTrue(result.values.get(0).eq(value, type));
		result = (SetValue) range.union(value3, type);
		assertTrue(result.values.get(0).eq(range, type));
		result = (SetValue) range.union(value5, type);
		assertTrue(result.values.get(0).eq(expected, type));
		result = (SetValue) range.union(value2, type);
		assertTrue(result.values.get(0).eq(range, type));
		result = (SetValue) range.union(value4, type);
		assertTrue(result.values.get(0).eq(range, type));
		assertTrue(result.values.get(1).eq(new SimpleNumeric(2.0), type));
		assertTrue(result.values.get(2).eq(new SimpleNumeric(18.0), type));
		
		result = (SetValue) range.union(value6, type);
		assertTrue(result.values.get(0).eq(value, type));		
		
	}	
	
	@Test
	public void testUnionLogicForSetValues() throws BuilderException, UnsupportedOperationException, NotInTheDomainException, RangeFormatException, UnknownValueException {
		SetValue value = new SetValue(Arrays.<Value>asList(new SimpleNumeric(7.0),new SimpleNumeric(9.0)));
		SetValue value2 = new SetValue();
		SetValue value3 = new SetValue(Arrays.<Value>asList(new SimpleNumeric(5.0),new SimpleNumeric(10.0)));
		SetValue value4 = new SetValue(Arrays.<Value>asList(new SimpleNumeric(3.0),new SimpleNumeric(8.0)));
		SetValue value5 = new SetValue(Arrays.<Value>asList(new SimpleNumeric(7.0),new SimpleNumeric(18.0)));
		SetValue value6 = new SetValue(Arrays.<Value>asList(new SimpleNumeric(7.0),new SimpleSymbolic("value9",9)));
		Type type = createTypeWithDomain(Arrays.<Value>asList(range, value, value2, 
				value3, value4, value5, value6, new SimpleSymbolic("value9",9)));
		
		SetValue result = (SetValue) range.union(value, type);		
		assertTrue(result.values.get(0).eq(new Range(new SimpleNumeric(5.0), true, new SimpleNumeric(10.0),true), type));		
		
		result = (SetValue) range.union(value2, type);		
		assertTrue(result.values.get(0).eq(new Range(new SimpleNumeric(5.0), true, new SimpleNumeric(10.0),true), type));
		
		result = (SetValue) range.union(value3, type);		
		assertTrue(result.values.get(0).eq(new Range(new SimpleNumeric(5.0), true, new SimpleNumeric(10.0),true), type));
		
		result = (SetValue) range.union(value4, type);			
		assertTrue(result.values.get(0).eq(new Range(new SimpleNumeric(5.0), true, new SimpleNumeric(10.0),true), type));
		assertTrue(result.values.get(1).eq(new SimpleNumeric(3.0), type));
		
		result = (SetValue) range.union(value5, type);		
		assertTrue(result.values.get(0).eq(new Range(new SimpleNumeric(5.0), true, new SimpleNumeric(10.0),true), type));
		assertTrue(result.values.get(1).eq(new SimpleNumeric(18.0), type));
		
		//No mixed values. Set has to contain values of the same type (symbolic, or numeric)
		//result = (SetValue) range.union(value6, type);		
	}
	
	@Test
	public void testUnionForRangeValues() throws BuilderException, UnsupportedOperationException, NotInTheDomainException, RangeFormatException, UnknownValueException {
		Range value = new Range(new SimpleNumeric(8.0), new SimpleNumeric(8.0));		
		Range value2 = new Range(new SimpleNumeric(4.0), new SimpleNumeric(8.0));
		Range value3 = new Range(new SimpleNumeric(8.0), new SimpleNumeric(12.0));			
		Range value4 = new Range(new SimpleNumeric(3.0), new SimpleNumeric(15.0));
		Range value5 = new Range(new SimpleNumeric(1.0), new SimpleNumeric(4.0));
		Range value6 = new Range(new SimpleNumeric(1.0), new SimpleNumeric(5.0));
		Range value7 = new Range(new SimpleNumeric(10.0), new SimpleNumeric(15.0));
		Range value8 = new Range(new SimpleNumeric(5.0), new SimpleNumeric(10.0));
		Type type = createTypeWithDomain(Arrays.<Value>asList(range, value, 
				value2, value3, value4, value5));
		
		SetValue result = (SetValue) range.union(value, type);
		assertTrue(result.values.get(0).eq(range, type));
		
		result = (SetValue) range.union(value2, type);
		Range expected = new Range(new SimpleNumeric(4.0), new SimpleNumeric(10.0));
		assertTrue(result.values.get(0).eq(expected, type));
		
		result = (SetValue) range.union(value3, type);
		expected = new Range(new SimpleNumeric(5.0), new SimpleNumeric(12.0));
		assertTrue(result.values.get(0).eq(expected, type));
	
		result = (SetValue) range.union(value4, type);		
		assertTrue(result.values.get(0).eq(value4, type));		
		
		result = (SetValue) range.union(value5, type);		
		assertTrue(result.values.get(0).eq(range, type));
		assertTrue(result.values.get(1).eq(value5, type));
		
		result = (SetValue) range.union(value6, type);
		expected = new Range(new SimpleNumeric(1.0), new SimpleNumeric(10.0));
		assertTrue(result.values.get(0).eq(expected, type));
		
		result = (SetValue) range.union(value7, type);
		expected = new Range(new SimpleNumeric(5.0), new SimpleNumeric(15.0));
		assertTrue(result.values.get(0).eq(expected, type));
		
		result = (SetValue) range.union(value8, type);		
		assertTrue(result.values.get(0).eq(range, type));
	}
	
	@Test
	public void testExceptLogicForNumericRangeValues() throws BuilderException, UnsupportedOperationException, NotInTheDomainException, RangeFormatException, UnknownValueException {
		Range value = new Range(new SimpleNumeric(4.0), new SimpleNumeric(18.0));		
		Range value2 = new Range(new SimpleNumeric(6.0), new SimpleNumeric(8.0));	
		Range value3 = new Range(new SimpleNumeric(3.0), new SimpleNumeric(8.0));
		Range value4 = new Range(new SimpleNumeric(7.0), new SimpleNumeric(12.0));
		Range value5 = new Range(new SimpleNumeric(2.0), new SimpleNumeric(5.0));		
		Range value6 = new Range(new SimpleNumeric(10.0), new SimpleNumeric(12.0));		
		Type type = createTypeWithDomain(Arrays.<Value>asList(range, value, value2, 
				value3, value4, value5, value6,new SimpleSymbolic("value5", 5), new SimpleSymbolic("value10",10)));
		
		SetValue result = (SetValue) range.except(value, type);
		assertTrue(result.values.get(0).eq(new Range(new SimpleNumeric(4.0), true, new SimpleNumeric(5.0),false), type));
		assertTrue(result.values.get(1).eq(new Range(new SimpleNumeric(10.0), false, new SimpleNumeric(18.0),true), type));
		
		result = (SetValue) range.except(value2, type);
		assertTrue(result.values.get(0).eq(new Range(new SimpleNumeric(5.0), true, new SimpleNumeric(6.0),false), type));
		assertTrue(result.values.get(1).eq(new Range(new SimpleNumeric(8.0), false, new SimpleNumeric(10.0),true), type));
		
		result = (SetValue) range.except(value3, type);
		assertTrue(result.values.get(0).eq(new Range(new SimpleNumeric(8.0), false, new SimpleNumeric(10.0),true), type));
		
		result = (SetValue) range.except(value4, type);
		assertTrue(result.values.get(0).eq(new Range(new SimpleNumeric(5.0), true, new SimpleNumeric(7.0),false), type));
		
		result = (SetValue) range.except(value5, type);
		assertTrue(result.values.get(0).eq(new Range(new SimpleNumeric(5.0), false, new SimpleNumeric(10.0),true), type));
		
		result = (SetValue) range.except(value6, type);
		assertTrue(result.values.get(0).eq(new Range(new SimpleNumeric(5.0), true, new SimpleNumeric(10.0),false), type));
		
		result = (SetValue) range.except(range, type);
		assertTrue(result.values.isEmpty());		
	}	
	
	@Test
	public void testExceptLogicForSimpeNumeric() throws BuilderException, UnsupportedOperationException, NotInTheDomainException, RangeFormatException, UnknownValueException {
		SimpleNumeric value = new SimpleNumeric(7.0);
		SimpleNumeric value2 = new  SimpleNumeric(3.0);
		SimpleNumeric value3 = new SimpleNumeric(12.0);
		SimpleNumeric value4 = new  SimpleNumeric(5.0);
		SimpleNumeric value5 = new SimpleNumeric(10.0);
		Type type = createTypeWithDomain(Arrays.<Value>asList(range, value, value2, value3,
				value4, value5));
		
		SetValue result = (SetValue) range.except(value, type);		
		assertTrue(result.values.get(0).eq(new Range(new SimpleNumeric(5.0), true, new SimpleNumeric(7.0),false), type));
		assertTrue(result.values.get(1).eq(new Range(new SimpleNumeric(7.0), false, new SimpleNumeric(10.0),true), type));
		
		result = (SetValue) range.except(value2, type);
		assertTrue(result.values.get(0).eq(range, type));
		
		result = (SetValue) range.except(value3, type);
		assertTrue(result.values.get(0).eq(range, type));
		
		result = (SetValue) range.except(value4, type);
		range.setLeftInclusive(false);
		assertTrue(result.values.get(0).eq(range, type));
		range.setLeftInclusive(true);
		
		range.setRightInclusive(false);
		result = (SetValue) range.except(value5, type);
		assertTrue(result.values.get(0).eq(range, type));		
	}
	
	@Test
	public void testExceptLogicForSetValues() throws BuilderException, UnsupportedOperationException, NotInTheDomainException, RangeFormatException, UnknownValueException {
		SetValue value = new SetValue(Arrays.<Value>asList(new SimpleNumeric(7.0),new SimpleNumeric(9.0)));
		SetValue value2 = new SetValue();
		SetValue value3 = new SetValue(Arrays.<Value>asList(new SimpleNumeric(5.0),new SimpleNumeric(10.0)));
		SetValue value4 = new SetValue(Arrays.<Value>asList(new SimpleNumeric(3.0),new SimpleNumeric(8.0)));
		SetValue value5 = new SetValue(Arrays.<Value>asList(new SimpleNumeric(7.0),new SimpleNumeric(18.0)));
		SetValue value6 = new SetValue(Arrays.<Value>asList(new SimpleNumeric(7.0),new SimpleSymbolic("value9",9)));
		Type type = createTypeWithDomain(Arrays.<Value>asList(range, value, value2, 
				value3, value4, value5, value6, new SimpleSymbolic("value9",9)));
		
		SetValue result = (SetValue) range.except(value, type);
		assertTrue(result.values.get(0).eq(new Range(new SimpleNumeric(5.0), true, new SimpleNumeric(7.0),false), type));
		assertTrue(result.values.get(1).eq(new Range(new SimpleNumeric(7.0), false, new SimpleNumeric(9.0),false), type));
		assertTrue(result.values.get(2).eq(new Range(new SimpleNumeric(9.0), false, new SimpleNumeric(10.0),true), type));
		
		result = (SetValue) range.except(value2, type);
		assertTrue(result.values.get(0).eq(range, type));
		
		result = (SetValue) range.except(value3, type);
		range.setLeftInclusive(false);
		range.setRightInclusive(false);
		assertTrue(result.values.get(0).eq(range, type));
		
		range.setLeftInclusive(true);
		range.setRightInclusive(true);
		result = (SetValue) range.except(value4, type);		
		assertTrue(result.values.get(0).eq(new Range(new SimpleNumeric(5.0), true, new SimpleNumeric(8.0),false), type));
		assertTrue(result.values.get(1).eq(new Range(new SimpleNumeric(8.0), false, new SimpleNumeric(10.0),true), type));
		
		result = (SetValue) range.except(value5, type);
		assertTrue(result.values.get(0).eq(new Range(new SimpleNumeric(5.0), true, new SimpleNumeric(7.0),false), type));
		assertTrue(result.values.get(1).eq(new Range(new SimpleNumeric(7.0), false, new SimpleNumeric(10.0),true), type));
		
		//No mixed values. Set has to contain values of the same type (symbolic, or numeric)
		//result = (SetValue) range.except(value6, type);		
	}
		
	@Test
	public void testExceptLogicForSimpeSymbolic() throws BuilderException, UnsupportedOperationException, NotInTheDomainException, RangeFormatException, UnknownValueException {
		SimpleSymbolic value = new SimpleSymbolic("value7",7);
		SimpleSymbolic value2 = new  SimpleSymbolic("value3",3);
		SimpleSymbolic value3 = new SimpleSymbolic("value12",12);
		SimpleSymbolic value4 = new  SimpleSymbolic("value5",5);
		SimpleSymbolic value5 = new SimpleSymbolic("value10",10);
		Type type = createTypeWithDomain(Arrays.<Value>asList(range, value, value2, value3,
				value4, value5));
		
		//Czy Range(Numeric,Numeric) można porównywać do Symbolic i vice versa?
	
		SetValue result = (SetValue) range.except(value, type);				
		assertTrue(result.values.get(0).eq(new Range(new SimpleNumeric(5.0), true, new SimpleNumeric(7.0),false), type));
		assertTrue(result.values.get(1).eq(new Range(new SimpleNumeric(7.0), false, new SimpleNumeric(10.0),true), type));
		
		result = (SetValue) range.except(value2, type);
		assertTrue(result.values.get(0).eq(range, type));
		
		result = (SetValue) range.except(value3, type);
		assertTrue(result.values.get(0).eq(range, type));
		
		result = (SetValue) range.except(value4, type);
		range.setLeftInclusive(false);
		assertTrue(result.values.get(0).eq(range, type));
		range.setLeftInclusive(true);
		
		range.setRightInclusive(false);
		result = (SetValue) range.except(value5, type);
		assertTrue(result.values.get(0).eq(range, type));	
	}
	
	private String getErrorMessage(String comparedWith) {
		return "Range " + range + " can not be compared with "
				+ comparedWith;
	}
}
