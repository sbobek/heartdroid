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



package heart.alsvfd.operations;

import static org.junit.Assert.*;
import org.junit.Test;

public class LogicOperationsTest {
	private final double a = 10.0;
	private final double b = 15.0;
	
	@Test
	public void testEQ(){
		assertFalse(LogicOperations.EQ.logicalExpresion(a, b));
	}
	
	@Test
	public void testNEQ(){
		assertTrue(LogicOperations.NEQ.logicalExpresion(a, b));
	}
	
	@Test
	public void testGT(){
		assertTrue(LogicOperations.GT.logicalExpresion(b, a));
		assertFalse(LogicOperations.GT.logicalExpresion(a, b));
	}
	
	@Test
	public void testGTE(){
		assertTrue(LogicOperations.GTE.logicalExpresion(b, a));
		assertTrue(LogicOperations.GTE.logicalExpresion(a, a));
		assertFalse(LogicOperations.GTE.logicalExpresion(a, b));
	}
	
	@Test
	public void testLT(){
		assertTrue(LogicOperations.LT.logicalExpresion(a, b));	
		assertFalse(LogicOperations.LT.logicalExpresion(b, a));
	}	
	
	@Test
	public void testLTE(){
		assertTrue(LogicOperations.LTE.logicalExpresion(a, b));
		assertTrue(LogicOperations.LTE.logicalExpresion(a, a));
		assertFalse(LogicOperations.LTE.logicalExpresion(b, a));		
	}	
}
