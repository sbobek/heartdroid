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
import static org.junit.Assert.*;
import heart.exceptions.BuilderException;
import heart.exceptions.NotInTheDomainException;
import heart.exceptions.RangeFormatException;
import heart.exceptions.UnknownValueException;
import heart.xtt.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class SetValueTest extends AbstractTest {

	private SetValue setValue;

	private ExceptionTestParameters testParameters;

	@Before
	public void init() {
		setValue = new SetValue(Arrays.asList((Value) new SimpleNumeric(10.0)));

		testParameters = new ExceptionTestParameters();
		testParameters.expectedException = Exception.class;
		testParameters.exceptedMessage = "";
		testParameters.thisValue = setValue;
		testParameters.otherValue = null;
		testParameters.type = null;
	}

	@Test
	public void testEqAndNeqAcceptNullValue()
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		assertFalse(setValue.eq(new Null(), null));
		assertTrue(setValue.neq(new Null(), null));
	}

	@Test
	public void testNullValueThrowException() {
		Command[] commands = { SUPSET, SUBSET, SIM, NOTISM, INTERSECT, UNION,
				EXCEPT };
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
		assertTrue(setValue.eq(new Any(), null));
		assertFalse(setValue.neq(new Any(), null));
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
		testParameters.exceptedMessage = getNotInTheDomainMessage(setValue);
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
		Command[] commands = { SUPSET, SUBSET, SIM, NOTISM, INTERSECT, UNION,
				EXCEPT, EQ, NEQ };
		SetValue value = new SetValue(Arrays.asList((Value) new SimpleNumeric(
				15.0)));
		

		testParameters.expectedException = NotInTheDomainException.class;
		testParameters.exceptedMessage = getNotInTheDomainMessage(value);
		testParameters.otherValue = value;
		testParameters.type = createTypeWithDomain(Arrays
				.<Value> asList(setValue));

		for (Command command : commands) {
			testException(command, testParameters);
		}
	}

	@Test
	public void testOtherValuesThanSetValueAndRangeAreNotAccepted()
			throws BuilderException {
		Command[] commands = { SUPSET, SUBSET, SIM, NOTISM, INTERSECT, UNION,
				EQ, NEQ };
		Value value = new SimpleNumeric(15.0);

		testParameters.expectedException = UnsupportedOperationException.class;
		testParameters.exceptedMessage = SetValue.ERROR_MESSAGE;
		testParameters.otherValue = value;
		testParameters.type = createTypeWithDomain(Arrays.<Value> asList(
				setValue, value));

		for (Command command : commands) {
			testException(command, testParameters);
		}

		value = new SimpleSymbolic("value", 15);
		testParameters.otherValue = value;
		testParameters.type = createTypeWithDomain(Arrays.<Value> asList(
				setValue, value));

		for (Command command : commands) {
			testException(command, testParameters);
		}
	}

	@Test
	public void testUnimplementedMethodsThrowException()
			throws BuilderException {
		Command[] commands = { ADD, DIV, SUB, MUL, GT, GTE, LT, LTE, IN, NOTIN };
		Value value = new SimpleNumeric(15.0);

		testParameters.expectedException = UnsupportedOperationException.class;
		testParameters.exceptedMessage = "Operation not applicable to "
				+ SetValue.class.getSimpleName() + " class";
		testParameters.otherValue = value;
		testParameters.type = createTypeWithDomain(Arrays.<Value> asList(
				setValue, value));

		for (Command command : commands) {
			testException(command, testParameters);
		}
	}
	
	@Test
	public void testEqAndNeqLogic() throws BuilderException, UnsupportedOperationException, NotInTheDomainException, RangeFormatException, UnknownValueException {
		Value value = new Range(new SimpleNumeric(4.0), new SimpleNumeric(2.0));
		SetValue value2 = new SetValue(Arrays.<Value> asList(new SimpleNumeric(4.0), new SimpleNumeric(2.0)));
		SetValue value3 =  new SetValue(Arrays.<Value> asList(new SimpleNumeric(10.0)));
		SetValue value4 = new SetValue(Arrays.<Value> asList(new SimpleNumeric(20.0)));
		
		Type type = createTypeWithDomain(Arrays.<Value> asList(
				setValue, value, value2, value3, value4));
		
		assertFalse(setValue.eq(value, type));
		assertTrue(setValue.neq(value, type));
		
		assertFalse(setValue.eq(value2, type));
		assertTrue(setValue.neq(value2, type));
		
		assertTrue(setValue.eq(value3, type));
		assertFalse(setValue.neq(value3, type));
		
		assertFalse(setValue.eq(value4, type));
		assertTrue(setValue.neq(value4, type));
	}
	
	@Test
	public void testSupsetAndSubSet() throws BuilderException, UnsupportedOperationException, NotInTheDomainException, RangeFormatException, UnknownValueException {
		Range value = new Range(new SimpleNumeric(4.0), new SimpleNumeric(2.0));
		SetValue value2 = new SetValue(Arrays.<Value> asList(new SimpleNumeric(4.0), new SimpleNumeric(2.0)));
		Type type = createTypeWithDomain(Arrays.<Value> asList(
				setValue, value, value2, new SimpleNumeric(3.0)));
		
		assertFalse(setValue.supset(value, type));
		assertFalse(setValue.subset(value, type));
		assertFalse(setValue.supset(value2, type));
		assertFalse(setValue.subset(value2, type));
		setValue = new SetValue(Arrays.asList((Value) new SimpleNumeric(4.0)));
		assertTrue(setValue.subset(value, type));
		assertTrue(setValue.subset(value2, type));
		setValue = new SetValue(Arrays.asList((Value) new SimpleNumeric(4.0),(Value) new SimpleNumeric(2.0),(Value) new SimpleNumeric(10.0)));
		assertFalse(setValue.supset(value, type));
		assertTrue(setValue.supset(value2, type));
		assertFalse(setValue.subset(value2, type));
		assertFalse(value2.supset(setValue, type));
		
		//jest problem z tym Range ten supset chyba nie zadziała póki co
		/*setValue = new SetValue(Arrays.asList((Value) new SimpleNumeric(2.0),(Value) new SimpleNumeric(3.0),(Value) new SimpleNumeric(4.0)));
		value.setLeftInclusive(true);
		value.setRightInclusive(true);
		assertTrue(setValue.supset(value, type));*/
	}
	
	@Test
	public void testSimAndNotSimLogic() throws BuilderException, UnsupportedOperationException, NotInTheDomainException, RangeFormatException, UnknownValueException {
		Range value = new Range(new SimpleNumeric(4.0), new SimpleNumeric(2.0));
		SetValue value2 = new SetValue(Arrays.<Value> asList(new SimpleNumeric(4.0), new SimpleNumeric(2.0)));
		Type type = createTypeWithDomain(Arrays.<Value> asList(
				setValue, value, value2, new SimpleNumeric(3.0)));
		
		assertFalse(setValue.sim(value, type));
		assertFalse(setValue.sim(value2, type));
		assertTrue(setValue.notsim(value, type));
		assertTrue(setValue.notsim(value, type));
		
		setValue = new SetValue(Arrays.asList((Value) new SimpleNumeric(4.0),(Value) new SimpleNumeric(3.0)));
		assertTrue(setValue.sim(value, type));
		assertTrue(setValue.sim(value2, type));
		assertFalse(setValue.notsim(value, type));
		assertFalse(setValue.notsim(value2, type));
	}
	
	@Test
	public void testIntersectLogic() throws BuilderException, UnsupportedOperationException, NotInTheDomainException, RangeFormatException, UnknownValueException {
		List<Value> expectedResult = Arrays.<Value> asList(new SimpleNumeric(4.0), new SimpleNumeric(2.0));
		Range value = new Range(new SimpleNumeric(4.0), new SimpleNumeric(2.0));
		SetValue value2 = new SetValue(expectedResult);
		Type type = createTypeWithDomain(Arrays.<Value> asList(
				setValue, value, value2, new SimpleNumeric(3.0),new SimpleNumeric(5.0)));
		setValue = new SetValue(expectedResult);
		setValue.appendValue(new SimpleNumeric(10.0));
		
		List<Value> result = ((SetValue)setValue.intersect(value2, type)).getValues();
		assertTrue(result.containsAll(expectedResult));
		result = ((SetValue)setValue.intersect(value, type)).getValues();		
		assertTrue(result.containsAll(expectedResult));
		
		setValue = new SetValue(Arrays.asList((Value) new SimpleNumeric(5.0),(Value) new SimpleNumeric(3.0)));
		result = ((SetValue)setValue.intersect(value2, type)).getValues();
		assertTrue(result.isEmpty());
		result = ((SetValue)setValue.intersect(value, type)).getValues();		
		assertTrue(result.isEmpty());
	}
	
	@Test
	public void testUnionLogic() throws BuilderException, UnsupportedOperationException, NotInTheDomainException, RangeFormatException, UnknownValueException {
		List<Value> values = Arrays.<Value>asList(new SimpleNumeric(4.0), new SimpleNumeric(2.0));
		Range value = new Range(new SimpleNumeric(4.0), new SimpleNumeric(2.0));
		SetValue value2 = new SetValue(values);
		Type type = createTypeWithDomain(Arrays.<Value> asList(
				setValue, value, value2,new SimpleNumeric(1.0), new SimpleNumeric(5.0)));
		
		List<Value> setValues = Arrays.<Value> asList(new SimpleNumeric(1.0), new SimpleNumeric(5.0));
		setValue = new SetValue(setValues);
		List<Value> expectedResult = new ArrayList<Value>(setValues);
		expectedResult.add(value);
		
		List<Value> result = ((SetValue)setValue.union(value, type)).getValues();
		assertTrue(result.containsAll(expectedResult));
		
		expectedResult = new ArrayList<Value>(setValues);
		expectedResult.addAll(values);
		result = ((SetValue)setValue.union(value2, type)).getValues();		
		assertTrue(result.containsAll(expectedResult));		
	}
	
	@Test
	public void testExceptLogic() throws BuilderException, UnsupportedOperationException, NotInTheDomainException, RangeFormatException, UnknownValueException {
		SimpleNumeric value = new SimpleNumeric(5.0);
		//SimpleSymbolic value2 = new SimpleSymbolic("value", 10);
		Range value3 = new Range(new SimpleNumeric(2.0), new SimpleNumeric(6.0));
		SimpleNumeric value4 = new SimpleNumeric(15.0);		
		SimpleNumeric value6 = new SimpleNumeric(8.0);
		SetValue value5 = new SetValue(Arrays.<Value> asList(value,value3,value6));
		List<Value> setValues = Arrays.<Value> asList(value, value3,
				value4, new SimpleNumeric(3.0));
		setValue = new SetValue(setValues);
		Type type = createTypeWithDomain(Arrays.<Value> asList(
				setValue, value, value3,value4,value6,new SimpleSymbolic("val",5)));
		
		SetValue result = (SetValue) setValue.except(value, type);		
		assertFalse(result.getValues().contains(value));
		result = (SetValue) setValue.except(value3, type);
		assertTrue(result.getValues().contains(value4));
		assertEquals(result.getValues().size(),2);
		result = (SetValue) setValue.except(value5, type);
		assertTrue(result.getValues().contains(value3));
		assertTrue(result.getValues().contains(value4));
		//poki co klasa SimpleSymbolic jest zbuggowana
		/*result = (SetValue) setValue.except(value2, type);
		assertFalse(result.getValues().contains(value2));*/
	}	

	private String getErrorMessage(String comparedWith) {
		return "SetValue " + setValue + " can not be compared with "
				+ comparedWith;
	}
}
