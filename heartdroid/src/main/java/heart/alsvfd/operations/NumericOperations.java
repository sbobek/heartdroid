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

import heart.alsvfd.Value;

public enum NumericOperations implements ErrorMessanger{
	ADD("Addition") {
		@Override
		public Double numericExpression(Double a, Double b) {
			return a + b;
		}
	},DIV("Division") {
		@Override
		public Double numericExpression(Double a, Double b) {
			return a / b;
		}
	},SUB("Subtraction") {
		@Override
		public Double numericExpression(Double a, Double b) {
			return a - b;
		}
	},MUL("Multiplication") {
		@Override
		public Double numericExpression(Double a, Double b) {
			return a * b;
		}
	},POW("Power"){
		@Override
		public Double numericExpression(Double a, Double b) {
			return Math.pow(a,b);
		}
	},MOD("Modulo"){
		@Override
		public Double numericExpression(Double a, Double b) {
			return a % b;
		}
	};
	
	public abstract Double numericExpression(Double a, Double b);
	private String operationName;
	private NumericOperations(String name){
		operationName = name;
	}
	
	public String toString(){
		return operationName;
	}
	
	public String errorMessage(Value compared, String with){
		return "Error! " + operationName + " is not permitted between Value " + this + " and " + with;
	}
}
