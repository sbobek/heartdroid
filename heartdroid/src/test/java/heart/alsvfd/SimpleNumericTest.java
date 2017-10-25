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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import heart.alsvfd.operations.LogicOperations;
import heart.alsvfd.operations.NumericOperations;
import heart.alsvfd.operations.SetOperations;
import heart.exceptions.BuilderException;
import heart.exceptions.NotInTheDomainException;
import heart.exceptions.RangeFormatException;
import heart.exceptions.UnknownValueException;
import heart.xtt.Type;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class SimpleNumericTest extends AbstractTest {

	private SimpleNumeric simpleNumeric;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	public SimpleNumericTest() {
		simpleNumeric = new SimpleNumeric();
		simpleNumeric.setValue(10.0);
	}

	@Test
	public void testAllLogicOperationsWithSimpleNumericValue()
            throws BuilderException, UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		SimpleNumeric value = new SimpleNumeric(10.0);
		Type type = createTypeWithDomain(Arrays.<Value> asList(simpleNumeric,
				value));

		assertTrue(simpleNumeric.computeLogicalExpression(value, type,
				LogicOperations.EQ));
		assertTrue(simpleNumeric.eq(value, type));
		assertFalse(simpleNumeric.computeLogicalExpression(value, type,
				LogicOperations.NEQ));
		assertFalse(simpleNumeric.neq(value, type));
		assertTrue(simpleNumeric.computeLogicalExpression(value, type,
				LogicOperations.GTE));
		assertTrue(simpleNumeric.gte(value, type));
		assertTrue(simpleNumeric.computeLogicalExpression(value, type,
				LogicOperations.LTE));
		assertTrue(simpleNumeric.lte(value, type));

		value.setValue(15.0);
		assertFalse(simpleNumeric.eq(value, type));
		assertFalse(simpleNumeric.computeLogicalExpression(value, type,
				LogicOperations.EQ));
		assertTrue(simpleNumeric.computeLogicalExpression(value, type,
				LogicOperations.NEQ));
		assertTrue(simpleNumeric.neq(value, type));
		assertFalse(simpleNumeric.computeLogicalExpression(value, type,
				LogicOperations.GTE));
		assertFalse(simpleNumeric.gte(value, type));
		assertFalse(simpleNumeric.computeLogicalExpression(value, type,
				LogicOperations.GT));
		assertFalse(simpleNumeric.gt(value, type));
		assertTrue(simpleNumeric.computeLogicalExpression(value, type,
				LogicOperations.LTE));
		assertTrue(simpleNumeric.lte(value, type));
		assertTrue(simpleNumeric.computeLogicalExpression(value, type,
				LogicOperations.LT));
		assertTrue(simpleNumeric.lt(value, type));

		value.setValue(5.0);
		assertTrue(simpleNumeric.computeLogicalExpression(value, type,
				LogicOperations.GTE));
		assertTrue(simpleNumeric.gte(value, type));
		assertTrue(simpleNumeric.computeLogicalExpression(value, type,
				LogicOperations.GT));
		assertTrue(simpleNumeric.gt(value, type));
		assertFalse(simpleNumeric.computeLogicalExpression(value, type,
				LogicOperations.LTE));
		assertFalse(simpleNumeric.lte(value, type));
		assertFalse(simpleNumeric.computeLogicalExpression(value, type,
				LogicOperations.LT));
		assertFalse(simpleNumeric.lt(value, type));

		SimpleSymbolic simpleSymbolic = new SimpleSymbolic("value", 10);
		type = createTypeWithDomain(Arrays.<Value> asList(simpleNumeric,
				simpleSymbolic));

		assertTrue(simpleNumeric.eq(simpleSymbolic, type));
		simpleSymbolic.setOrder(15);
		assertFalse(simpleNumeric.eq(simpleSymbolic, type));
	}

	@Test
	public void testAllLogicOperationsWithSimpleSymbolicValue()
            throws BuilderException, UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		SimpleSymbolic value = new SimpleSymbolic("value", 10);
		Type type = createTypeWithDomain(Arrays.<Value> asList(simpleNumeric,
				value));

		assertTrue(simpleNumeric.computeLogicalExpression(value, type,
				LogicOperations.EQ));
		assertTrue(simpleNumeric.eq(value, type));
		assertFalse(simpleNumeric.computeLogicalExpression(value, type,
				LogicOperations.NEQ));
		assertFalse(simpleNumeric.neq(value, type));
		assertTrue(simpleNumeric.computeLogicalExpression(value, type,
				LogicOperations.GTE));
		assertTrue(simpleNumeric.gte(value, type));
		assertTrue(simpleNumeric.computeLogicalExpression(value, type,
				LogicOperations.LTE));
		assertTrue(simpleNumeric.lte(value, type));
		value.setOrder(15);
		assertFalse(simpleNumeric.eq(value, type));
		assertFalse(simpleNumeric.computeLogicalExpression(value, type,
				LogicOperations.EQ));
		assertTrue(simpleNumeric.computeLogicalExpression(value, type,
				LogicOperations.NEQ));
		assertTrue(simpleNumeric.neq(value, type));
		assertFalse(simpleNumeric.computeLogicalExpression(value, type,
				LogicOperations.GTE));
		assertFalse(simpleNumeric.gte(value, type));
		assertFalse(simpleNumeric.computeLogicalExpression(value, type,
				LogicOperations.GT));
		assertFalse(simpleNumeric.gt(value, type));
		assertTrue(simpleNumeric.computeLogicalExpression(value, type,
				LogicOperations.LTE));
		assertTrue(simpleNumeric.lte(value, type));
		assertTrue(simpleNumeric.computeLogicalExpression(value, type,
				LogicOperations.LT));
		assertTrue(simpleNumeric.lt(value, type));

		value.setOrder(5);
		assertTrue(simpleNumeric.computeLogicalExpression(value, type,
				LogicOperations.GTE));
		assertTrue(simpleNumeric.gte(value, type));
		assertTrue(simpleNumeric.computeLogicalExpression(value, type,
				LogicOperations.GT));
		assertTrue(simpleNumeric.gt(value, type));
		assertFalse(simpleNumeric.computeLogicalExpression(value, type,
				LogicOperations.LTE));
		assertFalse(simpleNumeric.lte(value, type));
		assertFalse(simpleNumeric.computeLogicalExpression(value, type,
				LogicOperations.LT));
		assertFalse(simpleNumeric.lt(value, type));
	}

	@Test
	public void testGtForNotOrderedSimpleSymbolic() throws BuilderException,
            UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		SimpleSymbolic value = new SimpleSymbolic("value");
		Type type = createTypeWithDomain(Arrays.<Value> asList(simpleNumeric,
				value));
		exception.expect(UnsupportedOperationException.class);
		exception.expectMessage("Numeric " + simpleNumeric
				+ " cannot be compared " + LogicOperations.GT + " to symbolic "
				+ value + " unless it is ordered");

		simpleNumeric.gt(value, type);
	}

	@Test
	public void testGteForNotOrderedSimpleSymbolic() throws BuilderException,
            UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		SimpleSymbolic value = new SimpleSymbolic("value");
		Type type = createTypeWithDomain(Arrays.<Value> asList(simpleNumeric,
				value));
		exception.expect(UnsupportedOperationException.class);
		exception.expectMessage("Numeric " + simpleNumeric
				+ " cannot be compared " + LogicOperations.GTE
				+ " to symbolic " + value + " unless it is ordered");

		simpleNumeric.gte(value, type);
	}

	@Test
	public void testLtForNotOrderedSimpleSymbolic() throws BuilderException,
            UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		SimpleSymbolic value = new SimpleSymbolic("value");
		Type type = createTypeWithDomain(Arrays.<Value> asList(simpleNumeric,
				value));
		exception.expect(UnsupportedOperationException.class);
		exception.expectMessage("Numeric " + simpleNumeric
				+ " cannot be compared " + LogicOperations.LT + " to symbolic "
				+ value + " unless it is ordered");

		simpleNumeric.lt(value, type);
	}

	@Test
	public void testLteForNotOrderedSimpleSymbolic() throws BuilderException,
            UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		SimpleSymbolic value = new SimpleSymbolic("value");
		Type type = createTypeWithDomain(Arrays.<Value> asList(simpleNumeric,
				value));
		exception.expect(UnsupportedOperationException.class);
		exception.expectMessage("Numeric " + simpleNumeric
				+ " cannot be compared " + LogicOperations.LTE
				+ " to symbolic " + value + " unless it is ordered");

		simpleNumeric.lte(value, type);
	}

	@Test
	public void testEqForNotOrderedSimpleSymbolic() throws BuilderException,
            UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		SimpleSymbolic value = new SimpleSymbolic("value");
		Type type = createTypeWithDomain(Arrays.<Value> asList(simpleNumeric,
				value));
		exception.expect(UnsupportedOperationException.class);
		exception.expectMessage("Numeric " + simpleNumeric
				+ " cannot be compared " + LogicOperations.EQ + " to symbolic "
				+ value + " unless it is ordered");

		simpleNumeric.eq(value, type);
	}

	@Test
	public void testNeqForNotOrderedSimpleSymbolic() throws BuilderException,
            UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		SimpleSymbolic value = new SimpleSymbolic("value");
		Type type = createTypeWithDomain(Arrays.<Value> asList(simpleNumeric,
				value));
		exception.expect(UnsupportedOperationException.class);
		exception.expectMessage("Numeric " + simpleNumeric
				+ " cannot be compared " + LogicOperations.NEQ
				+ " to symbolic " + value + " unless it is ordered");

		simpleNumeric.neq(value, type);
	}

	@Test
	public void testEqThrowsUnsupportedException()
            throws UnsupportedOperationException, NotInTheDomainException,
            BuilderException, UnknownValueException {
		SetValue value = new SetValue();
		Type type = createTypeWithDomain(Arrays.<Value> asList(simpleNumeric,
				value));
		exception.expect(UnsupportedOperationException.class);
		exception.expectMessage(getComparisonErrorMessage(LogicOperations.EQ));

		simpleNumeric.eq(value, type);
	}

	@Test
	public void testNeqThrowsUnsupportedException()
            throws UnsupportedOperationException, NotInTheDomainException,
            BuilderException, UnknownValueException {
		SetValue value = new SetValue();
		Type type = createTypeWithDomain(Arrays.<Value> asList(simpleNumeric,
				value));
		exception.expect(UnsupportedOperationException.class);
		exception.expectMessage(getComparisonErrorMessage(LogicOperations.NEQ));

		simpleNumeric.neq(value, type);
	}

	@Test
	public void testGtThrowsUnsupportedException()
            throws UnsupportedOperationException, NotInTheDomainException,
            BuilderException, UnknownValueException {
		SetValue value = new SetValue();
		Type type = createTypeWithDomain(Arrays.<Value> asList(simpleNumeric,
				value));
		exception.expect(UnsupportedOperationException.class);
		exception.expectMessage(getComparisonErrorMessage(LogicOperations.GT));

		simpleNumeric.gt(value, type);
	}

	@Test
	public void testGteThrowsUnsupportedException()
            throws UnsupportedOperationException, NotInTheDomainException,
            BuilderException, UnknownValueException {
		SetValue value = new SetValue();
		Type type = createTypeWithDomain(Arrays.<Value> asList(simpleNumeric,
				value));
		exception.expect(UnsupportedOperationException.class);
		exception.expectMessage(getComparisonErrorMessage(LogicOperations.GTE));

		simpleNumeric.gte(value, type);
	}

	@Test
	public void testLtThrowsUnsupportedException()
            throws UnsupportedOperationException, NotInTheDomainException,
            BuilderException, UnknownValueException {
		SetValue value = new SetValue();
		Type type = createTypeWithDomain(Arrays.<Value> asList(simpleNumeric,
				value));
		exception.expect(UnsupportedOperationException.class);
		exception.expectMessage(getComparisonErrorMessage(LogicOperations.LT));

		simpleNumeric.lt(value, type);
	}

	@Test
	public void testLteThrowsUnsupportedException()
            throws UnsupportedOperationException, NotInTheDomainException,
            BuilderException, UnknownValueException {
		SetValue value = new SetValue();
		Type type = createTypeWithDomain(Arrays.<Value> asList(simpleNumeric,
				value));
		exception.expect(UnsupportedOperationException.class);
		exception.expectMessage(getComparisonErrorMessage(LogicOperations.LTE));

		simpleNumeric.lte(value, type);
	}

	@Test
	public void testAllNumericOperations() throws BuilderException,
            UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		SimpleNumeric value = new SimpleNumeric(10.0);
		Type type = createTypeWithDomain(Arrays.<Value> asList(simpleNumeric,
				value));

		SimpleNumeric answer = (SimpleNumeric) simpleNumeric.add(value, type);
		assertEquals(NumericOperations.ADD.numericExpression(
				simpleNumeric.getValue(), value.getValue()), answer.getValue());
		answer = (SimpleNumeric) simpleNumeric.sub(value, type);
		assertEquals(NumericOperations.SUB.numericExpression(
				simpleNumeric.getValue(), value.getValue()), answer.getValue());
		answer = (SimpleNumeric) simpleNumeric.mul(value, type);
		assertEquals(NumericOperations.MUL.numericExpression(
				simpleNumeric.getValue(), value.getValue()), answer.getValue());
		answer = (SimpleNumeric) simpleNumeric.div(value, type);
		assertEquals(NumericOperations.DIV.numericExpression(
				simpleNumeric.getValue(), value.getValue()), answer.getValue());
	}

	@Test
	public void testAllSetOperationsForIn() throws BuilderException,
            UnsupportedOperationException, NotInTheDomainException, RangeFormatException, UnknownValueException {
		SimpleSymbolic simpleSymbolic = new SimpleSymbolic("value", 10);
		SimpleNumeric notAccepted = new SimpleNumeric(25.0);
		SetValue value = new SetValue(Arrays.asList((Value) simpleNumeric));
		SimpleNumeric from = new SimpleNumeric(5.0);
		SimpleNumeric to = new SimpleNumeric(15.0);
		Range rangeValues = new Range(from, to);
		Range rangeValues2 = new Range(new SimpleNumeric(15.0), new SimpleNumeric(25.0));

		Type type = createTypeWithDomain(Arrays.<Value> asList(simpleNumeric,
				value, simpleSymbolic, rangeValues, notAccepted, rangeValues2));

		assertTrue(simpleNumeric.in(value, type));
		value.setValues((Arrays.asList((Value) simpleSymbolic)));
		assertTrue(simpleNumeric.in(value, type));
		value = new SetValue(Arrays.asList((Value) simpleSymbolic));
		assertTrue(simpleNumeric.in(value, type));
		value.setValues(new ArrayList<Value>());
		assertFalse(simpleNumeric.in(value, type));

		assertTrue(simpleNumeric.in(rangeValues, type));
		from.setValue(simpleNumeric.getValue());
		rangeValues.setLeftInclusive(true);
		assertTrue(simpleNumeric.in(rangeValues, type));

		rangeValues.setLeftInclusive(false);
		rangeValues.setRightInclusive(true);
		to.setValue(simpleNumeric.getValue());
		assertTrue(simpleNumeric.in(rangeValues, type));
		assertFalse(simpleNumeric.in(rangeValues2, type));
		
		exception.expect(UnsupportedOperationException.class);
		exception
				.expectMessage("Error while checking membership of numeric value. Set or range value expected, not "
						+ notAccepted);

		simpleNumeric.in(notAccepted, type);
	}

	@Test
	public void testAllSetOperationsForNotIn() throws BuilderException,
            UnsupportedOperationException, NotInTheDomainException, RangeFormatException, UnknownValueException {
		SimpleSymbolic simpleSymbolic = new SimpleSymbolic("value", 10);
		SimpleNumeric notAccepted = new SimpleNumeric(25.0);
		SetValue value = new SetValue(Arrays.asList((Value) simpleNumeric));
		SimpleNumeric from = new SimpleNumeric(5.0);
		SimpleNumeric to = new SimpleNumeric(15.0);
		Range rangeValues = new Range(from, to);

		Type type = createTypeWithDomain(Arrays.<Value> asList(simpleNumeric,
				value, simpleSymbolic, rangeValues, notAccepted));

		assertFalse(simpleNumeric.notin(value, type));
		value.setValues((Arrays.asList((Value) simpleSymbolic)));
		assertFalse(simpleNumeric.notin(value, type));
		value = new SetValue(Arrays.asList((Value) simpleSymbolic));
		assertFalse(simpleNumeric.notin(value, type));
		value.setValues(new ArrayList<Value>());
		assertTrue(simpleNumeric.notin(value, type));

		assertFalse(simpleNumeric.notin(rangeValues, type));
		from.setValue(simpleNumeric.getValue());
		rangeValues.setLeftInclusive(true);
		assertFalse(simpleNumeric.notin(rangeValues, type));

		rangeValues.setLeftInclusive(false);
		rangeValues.setRightInclusive(true);
		to.setValue(simpleNumeric.getValue());
		assertFalse(simpleNumeric.notin(rangeValues, type));

		exception.expect(UnsupportedOperationException.class);
		exception
				.expectMessage("Error while checking membership of numeric value. Set or range value expected, not "
						+ notAccepted);

		simpleNumeric.notin(notAccepted, type);
	}

	@Test
	public void testEqAndNeqAcceptNullValue()
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		assertFalse(simpleNumeric.eq(new Null(), null));
		assertTrue(simpleNumeric.neq(new Null(), null));
	}

	@Test
	public void testGtNotAcceptNullValue()
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		exception.expect(UnknownValueException.class);
		exception.expectMessage(LogicOperations.GT.errorMessage(simpleNumeric,
				"null"));

		simpleNumeric.gt(new Null(), null);
	}

	@Test
	public void testGteNotAcceptNullValue()
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		exception.expect(UnknownValueException.class);
		exception.expectMessage(LogicOperations.GTE.errorMessage(simpleNumeric,
				"null"));

		simpleNumeric.gte(new Null(), null);
	}

	@Test
	public void testLtNotAcceptNullValue()
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		exception.expect(UnknownValueException.class);
		exception.expectMessage(LogicOperations.LT.errorMessage(simpleNumeric,
				"null"));

		simpleNumeric.lt(new Null(), null);
	}

	@Test
	public void testLteNotAcceptNullValue()
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		exception.expect(UnknownValueException.class);
		exception.expectMessage(LogicOperations.LTE.errorMessage(simpleNumeric,
				"null"));

		simpleNumeric.lte(new Null(), null);
	}

	@Test
	public void testInNotAcceptNullValue()
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		exception.expect(UnknownValueException.class);
		exception.expectMessage(SetOperations.IN.errorMessage(simpleNumeric,
				"null"));

		simpleNumeric.in(new Null(), null);
	}

	@Test
	public void testNotInNotAcceptNullValue()
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		exception.expect(UnknownValueException.class);
		exception.expectMessage(SetOperations.NOTIN.errorMessage(simpleNumeric,
				"null"));

		simpleNumeric.notin(new Null(), null);
	}

	@Test
	public void testAddNotAcceptNullValue()
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		exception.expect(UnknownValueException.class);
		exception.expectMessage(NumericOperations.ADD.errorMessage(
				simpleNumeric, "null"));

		simpleNumeric.add(new Null(), null);
	}

	@Test
	public void testSubNotAcceptNullValue()
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		exception.expect(UnknownValueException.class);
		exception.expectMessage(NumericOperations.SUB.errorMessage(
				simpleNumeric, "null"));

		simpleNumeric.sub(new Null(), null);
	}

	@Test
	public void testMulNotAcceptNullValue()
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		exception.expect(UnknownValueException.class);
		exception.expectMessage(NumericOperations.MUL.errorMessage(
				simpleNumeric, "null"));

		simpleNumeric.mul(new Null(), null);
	}

	@Test
	public void testDivNotAcceptNullValue()
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		exception.expect(UnknownValueException.class);
		exception.expectMessage(NumericOperations.DIV.errorMessage(
				simpleNumeric, "null"));

		simpleNumeric.div(new Null(), null);
	}

	@Test
	public void testEqThisSimpleNumericIsNotInTheDomain()
            throws BuilderException, UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		Value value = new SimpleNumeric();
		Type type = createTypeWithDomain(Arrays.<Value> asList(value));
		exception.expect(NotInTheDomainException.class);
		exception.expectMessage(getNotInTheDomainMessage(simpleNumeric));

		simpleNumeric.eq(value, type);
	}

	@Test
	public void testNEqThisSimpleNumericIsNotInTheDomain()
            throws BuilderException, UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		Value value = new SimpleNumeric();
		Type type = createTypeWithDomain(Arrays.<Value> asList(value));
		exception.expect(NotInTheDomainException.class);
		exception.expectMessage(getNotInTheDomainMessage(simpleNumeric));

		simpleNumeric.neq(value, type);
	}

	@Test
	public void testGtThisSimpleNumericIsNotInTheDomain()
            throws BuilderException, UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		Value value = new SimpleNumeric();
		Type type = createTypeWithDomain(Arrays.<Value> asList(value));
		exception.expect(NotInTheDomainException.class);
		exception.expectMessage(getNotInTheDomainMessage(simpleNumeric));

		simpleNumeric.gt(value, type);
	}

	@Test
	public void testGteThisSimpleNumericIsNotInTheDomain()
            throws BuilderException, UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		Value value = new SimpleNumeric();
		Type type = createTypeWithDomain(Arrays.<Value> asList(value));
		exception.expect(NotInTheDomainException.class);
		exception.expectMessage(getNotInTheDomainMessage(simpleNumeric));

		simpleNumeric.gte(value, type);
	}

	@Test
	public void testLtThisSimpleNumericIsNotInTheDomain()
            throws BuilderException, UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		Value value = new SimpleNumeric();
		Type type = createTypeWithDomain(Arrays.<Value> asList(value));
		exception.expect(NotInTheDomainException.class);
		exception.expectMessage(getNotInTheDomainMessage(simpleNumeric));

		simpleNumeric.lt(value, type);
	}

	@Test
	public void testLteThisSimpleNumericIsNotInTheDomain()
            throws BuilderException, UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		Value value = new SimpleNumeric();
		Type type = createTypeWithDomain(Arrays.<Value> asList(value));
		exception.expect(NotInTheDomainException.class);
		exception.expectMessage(getNotInTheDomainMessage(simpleNumeric));

		simpleNumeric.lte(value, type);
	}

	@Test
	public void testInThisSimpleNumericIsNotInTheDomain()
            throws BuilderException, UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		Value value = new SimpleNumeric();
		Type type = createTypeWithDomain(Arrays.<Value> asList(value));
		exception.expect(NotInTheDomainException.class);
		exception.expectMessage(getNotInTheDomainMessage(simpleNumeric));

		simpleNumeric.in(value, type);
	}

	@Test
	public void testNotInThisSimpleNumericIsNotInTheDomain()
            throws BuilderException, UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		Value value = new SimpleNumeric();
		Type type = createTypeWithDomain(Arrays.<Value> asList(value));
		exception.expect(NotInTheDomainException.class);
		exception.expectMessage(getNotInTheDomainMessage(simpleNumeric));

		simpleNumeric.notin(value, type);
	}

	@Test
	public void testMulThisSimpleNumericIsNotInTheDomain()
            throws BuilderException, UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		Value value = new SimpleNumeric();
		Type type = createTypeWithDomain(Arrays.<Value> asList(value));
		exception.expect(NotInTheDomainException.class);
		exception.expectMessage(getNotInTheDomainMessage(simpleNumeric));

		simpleNumeric.mul(value, type);
	}

	@Test
	public void testAddThisSimpleNumericIsNotInTheDomain()
            throws BuilderException, UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		Value value = new SimpleNumeric();
		Type type = createTypeWithDomain(Arrays.<Value> asList(value));
		exception.expect(NotInTheDomainException.class);
		exception.expectMessage(getNotInTheDomainMessage(simpleNumeric));

		simpleNumeric.add(value, type);
	}

	@Test
	public void testSubThisSimpleNumericIsNotInTheDomain()
            throws BuilderException, UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		Value value = new SimpleNumeric();
		Type type = createTypeWithDomain(Arrays.<Value> asList(value));
		exception.expect(NotInTheDomainException.class);
		exception.expectMessage(getNotInTheDomainMessage(simpleNumeric));

		simpleNumeric.sub(value, type);
	}

	@Test
	public void testDivThisSimpleNumericIsNotInTheDomain()
            throws BuilderException, UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		Value value = new SimpleNumeric();
		Type type = createTypeWithDomain(Arrays.<Value> asList(value));
		exception.expect(NotInTheDomainException.class);
		exception.expectMessage(getNotInTheDomainMessage(simpleNumeric));

		simpleNumeric.div(value, type);
	}

	@Test
	public void testEqValueIsNotInTheDomain() throws BuilderException,
            UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		Value value = new SimpleNumeric(15.0);
		Type type = createTypeWithDomain(Arrays.<Value> asList(simpleNumeric));
		exception.expect(NotInTheDomainException.class);
		exception.expectMessage(getNotInTheDomainMessage(value));

		simpleNumeric.eq(value, type);
	}

	@Test
	public void testNEqValueIsNotInTheDomain() throws BuilderException,
            UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		Value value = new SimpleNumeric(15.0);
		Type type = createTypeWithDomain(Arrays.<Value> asList(simpleNumeric));
		exception.expect(NotInTheDomainException.class);
		exception.expectMessage(getNotInTheDomainMessage(value));

		simpleNumeric.neq(value, type);
	}

	@Test
	public void testGtValueIsNotInTheDomain() throws BuilderException,
            UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		Value value = new SimpleNumeric(15.0);
		Type type = createTypeWithDomain(Arrays.<Value> asList(simpleNumeric));
		exception.expect(NotInTheDomainException.class);
		exception.expectMessage(getNotInTheDomainMessage(value));

		simpleNumeric.gt(value, type);
	}

	@Test
	public void testGteValueIsNotInTheDomain() throws BuilderException,
            UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		Value value = new SimpleNumeric(15.0);
		Type type = createTypeWithDomain(Arrays.<Value> asList(simpleNumeric));
		exception.expect(NotInTheDomainException.class);
		exception.expectMessage(getNotInTheDomainMessage(value));

		simpleNumeric.gte(value, type);
	}

	@Test
	public void testLtValueIsNotInTheDomain() throws BuilderException,
            UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		Value value = new SimpleNumeric(15.0);
		Type type = createTypeWithDomain(Arrays.<Value> asList(simpleNumeric));
		exception.expect(NotInTheDomainException.class);
		exception.expectMessage(getNotInTheDomainMessage(value));

		simpleNumeric.lt(value, type);
	}

	@Test
	public void testLteValueIsNotInTheDomain() throws BuilderException,
            UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		Value value = new SimpleNumeric(15.0);
		Type type = createTypeWithDomain(Arrays.<Value> asList(simpleNumeric));
		exception.expect(NotInTheDomainException.class);
		exception.expectMessage(getNotInTheDomainMessage(value));

		simpleNumeric.lte(value, type);
	}

	@Test
	public void testInValueIsNotInTheDomain() throws BuilderException,
            UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		Value value = new SimpleNumeric(15.0);
		Type type = createTypeWithDomain(Arrays.<Value> asList(simpleNumeric));
		exception.expect(NotInTheDomainException.class);
		exception.expectMessage(getNotInTheDomainMessage(value));

		simpleNumeric.in(value, type);
	}

	@Test
	public void testNotInValueIsNotInTheDomain() throws BuilderException,
            UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		Value value = new SimpleNumeric(15.0);
		Type type = createTypeWithDomain(Arrays.<Value> asList(simpleNumeric));
		exception.expect(NotInTheDomainException.class);
		exception.expectMessage(getNotInTheDomainMessage(value));

		simpleNumeric.notin(value, type);
	}

	@Test
	public void testMulValueIsNotInTheDomain() throws BuilderException,
            UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		Value value = new SimpleNumeric(15.0);
		Type type = createTypeWithDomain(Arrays.<Value> asList(simpleNumeric));
		exception.expect(NotInTheDomainException.class);
		exception.expectMessage(getNotInTheDomainMessage(value));

		simpleNumeric.mul(value, type);
	}

	@Test
	public void testAddValueIsNotInTheDomain() throws BuilderException,
            UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		Value value = new SimpleNumeric(15.0);
		Type type = createTypeWithDomain(Arrays.<Value> asList(simpleNumeric));
		exception.expect(NotInTheDomainException.class);
		exception.expectMessage(getNotInTheDomainMessage(value));

		simpleNumeric.add(value, type);
	}

	@Test
	public void testSubValueIsNotInTheDomain() throws BuilderException,
            UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		Value value = new SimpleNumeric(15.0);
		Type type = createTypeWithDomain(Arrays.<Value> asList(simpleNumeric));
		exception.expect(NotInTheDomainException.class);
		exception.expectMessage(getNotInTheDomainMessage(value));

		simpleNumeric.sub(value, type);
	}

	@Test
	public void testDivValueIsNotInTheDomain() throws BuilderException,
            UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		Value value = new SimpleNumeric(15.0);
		Type type = createTypeWithDomain(Arrays.<Value> asList(simpleNumeric));
		exception.expect(NotInTheDomainException.class);
		exception.expectMessage(getNotInTheDomainMessage(value));

		simpleNumeric.div(value, type);
	}

	@Test
	public void testEqAndNeqAcceptAnyValue()
            throws UnsupportedOperationException, NotInTheDomainException,
            BuilderException, UnknownValueException {
		assertTrue(simpleNumeric.eq(new Any(), null));
		assertFalse(simpleNumeric.neq(new Any(), null));
	}

	@Test
	public void testGtNotAcceptAnyValue() throws UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		exception.expect(UnsupportedOperationException.class);
		exception.expectMessage(LogicOperations.GT.errorMessage(simpleNumeric,
				"any"));

		simpleNumeric.gt(new Any(), null);
	}

	@Test
	public void testGteNotAcceptAnyValue()
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		exception.expect(UnsupportedOperationException.class);
		exception.expectMessage(LogicOperations.GTE.errorMessage(simpleNumeric,
				"any"));

		simpleNumeric.gte(new Any(), null);
	}

	@Test
	public void testLtNotAcceptAnyValue() throws UnsupportedOperationException,
            NotInTheDomainException, UnknownValueException {
		exception.expect(UnsupportedOperationException.class);
		exception.expectMessage(LogicOperations.LT.errorMessage(simpleNumeric,
				"any"));

		simpleNumeric.lt(new Any(), null);
	}

	@Test
	public void testLteNotAcceptAnyValue()
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		exception.expect(UnsupportedOperationException.class);
		exception.expectMessage(LogicOperations.LTE.errorMessage(simpleNumeric,
				"any"));

		simpleNumeric.lte(new Any(), null);
	}

	@Test
	public void testGInNotAcceptAnyValue()
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		exception.expect(UnsupportedOperationException.class);
		exception.expectMessage(SetOperations.IN.errorMessage(simpleNumeric,
				"any"));

		simpleNumeric.in(new Any(), null);
	}

	@Test
	public void testNotInNotAcceptAnyValue()
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		exception.expect(UnsupportedOperationException.class);
		exception.expectMessage(SetOperations.NOTIN.errorMessage(simpleNumeric,
				"any"));

		simpleNumeric.notin(new Any(), null);
	}

	@Test
	public void testASubNotAcceptAnyValue()
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		exception.expect(UnsupportedOperationException.class);
		exception.expectMessage(NumericOperations.SUB.errorMessage(
				simpleNumeric, "any"));

		simpleNumeric.sub(new Any(), null);
	}

	@Test
	public void testMulNotAcceptAnyValue()
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		exception.expect(UnsupportedOperationException.class);
		exception.expectMessage(NumericOperations.MUL.errorMessage(
				simpleNumeric, "any"));

		simpleNumeric.mul(new Any(), null);
	}

	@Test
	public void testDivNotAcceptAnyValue()
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		exception.expect(UnsupportedOperationException.class);
		exception.expectMessage(NumericOperations.DIV.errorMessage(
				simpleNumeric, "any"));

		simpleNumeric.div(new Any(), null);
	}

	@Test
	public void testAddNotAcceptAnyValue()
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		exception.expect(UnsupportedOperationException.class);
		exception.expectMessage(NumericOperations.ADD.errorMessage(
				simpleNumeric, "any"));

		simpleNumeric.add(new Any(), null);
	}

	@Test
	public void testThrowUnsupportedExceptionForSim()
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		exception.expect(UnsupportedOperationException.class);
		exception.expectMessage(getUndefineMethodErrorMessage("sim"));

		simpleNumeric.sim(null, null);
	}

	@Test
	public void testThrowUnsupportedExceptionForNotSim()
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		exception.expect(UnsupportedOperationException.class);
		exception.expectMessage(getUndefineMethodErrorMessage("notsim"));

		simpleNumeric.notsim(null, null);
	}

	@Test
	public void testThrowUnsupportedExceptionForSubset()
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		exception.expect(UnsupportedOperationException.class);
		exception.expectMessage(getUndefineMethodErrorMessage("subset"));

		simpleNumeric.subset(null, null);
	}

	@Test
	public void testThrowUnsupportedExceptionForSupset()
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		exception.expect(UnsupportedOperationException.class);
		exception.expectMessage(getUndefineMethodErrorMessage("supset"));

		simpleNumeric.supset(null, null);
	}

	@Test
	public void testThrowUnsupportedExceptionForInterset()
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		exception.expect(UnsupportedOperationException.class);
		exception.expectMessage(getUndefineMethodErrorMessage("intersect"));

		simpleNumeric.intersect(null, null);
	}

	@Test
	public void testThrowUnsupportedExceptionForExcept()
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		exception.expect(UnsupportedOperationException.class);		
		exception.expectMessage(getUndefineMethodErrorMessage("except"));

		simpleNumeric.except(null, null);
	}

	@Test
	public void testThrowUnsupportedExceptionForUnion()
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		exception.expect(UnsupportedOperationException.class);
		exception.expectMessage(getUndefineMethodErrorMessage("union"));

		simpleNumeric.union(null, null);
	}

	private String getUndefineMethodErrorMessage(String operationName) {
		String className = simpleNumeric.getClass().getSimpleName();
		return "Operation not applicable to "+ className + " class";
	}

	@Test
	public void testEqWithSimpleSymbolicValue() throws BuilderException,
            UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		SimpleSymbolic simpleSymbolic = new SimpleSymbolic("value", null);
		Type type = createTypeWithDomain(Arrays.<Value> asList(simpleNumeric,
				simpleSymbolic));
		try {
			simpleNumeric.eq(simpleSymbolic, type);
			fail();
		} catch (NotInTheDomainException e) {
			fail();
		} catch (UnsupportedOperationException e) {
			assertEquals(e.getMessage(), "Numeric " + simpleNumeric
					+ " cannot be compared " + LogicOperations.EQ
					+ " to symbolic " + simpleSymbolic
					+ " unless it is ordered");
		}
	}

	private String getComparisonErrorMessage(LogicOperations operation) {
		return "Error while comapring symbolic values for " + operation
				+ " operation";
	}
	
}