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

public enum LogicOperations implements ErrorMessanger{
	GT("greater than") {
		@Override
		public boolean logicalExpresion(Double a,Double b) {
			return a > b;
		}
		
		@Override
		public boolean logicalExpresion(Integer a,Integer b) {
			return a > b;
		}
	},GTE("greater than or equal") {
		@Override
		public boolean logicalExpresion(Double a,Double b) {
			return a >= b;
		}
		
		@Override
		public boolean logicalExpresion(Integer a,Integer b) {
			return a >= b;
		}
	},LT("lesser than") {
		@Override
		public boolean logicalExpresion(Double a,Double b) {
			return a < b;
		}
		
		@Override
		public boolean logicalExpresion(Integer a,Integer b) {
			return a < b;
		}
	},LTE("lesser than or equal") {
		@Override
		public boolean logicalExpresion(Double a,Double b) {
			return a <= b;
		}
		
		@Override
		public boolean logicalExpresion(Integer a,Integer b) {
			return a <= b;
		}
	},EQ("equal") {
		@Override
		public boolean logicalExpresion(Double a,Double b) {
			return a.equals(b);
		}
		
		@Override
		public boolean logicalExpresion(Integer a,Integer b) {
			return a.equals(b);
		}
	},NEQ("not equal"){
		@Override
		public boolean logicalExpresion(Double a, Double b) {
			return !a.equals(b);
		}
		
		@Override
		public boolean logicalExpresion(Integer a,Integer b) {
			return !a.equals(b);
		}		
	};
	
	public abstract boolean logicalExpresion(Double a,Double b);
	public abstract boolean logicalExpresion(Integer a,Integer b);
	private String operationName;
	private LogicOperations(String name){
		operationName = name;
	}
	public String toString(){
		return operationName;
	}
	
	public String errorMessage(Value compared, String with){
		return "Value " + compared + " cannot be compared " + operationName + " to " + with;
	}
}
