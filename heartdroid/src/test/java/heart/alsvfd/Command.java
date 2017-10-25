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

import heart.exceptions.NotInTheDomainException;
import heart.exceptions.UnknownValueException;
import heart.xtt.Type;

public enum Command {
	EQ {
		@Override
		public void execute(Value thisValue, Value otherValue, Type type) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
			thisValue.eq(otherValue, type);			
		}
		
	},
	NEQ
	
 {
		@Override
		public void execute(Value thisValue, Value otherValue, Type type) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
			thisValue.neq(otherValue, type);			
		}
	},GT{
		@Override
		public void execute(Value thisValue, Value otherValue, Type type)
                throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
			thisValue.gt(otherValue, type);
		}
	},GTE{
		@Override
		public void execute(Value thisValue, Value otherValue, Type type)
                throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
			thisValue.gte(otherValue, type);
		}
	},LT{
		@Override
		public void execute(Value thisValue, Value otherValue, Type type)
                throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
			thisValue.lt(otherValue, type);
		}
	},LTE{
		@Override
		public void execute(Value thisValue, Value otherValue, Type type)
                throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
			thisValue.lte(otherValue, type);
		}
	},ADD{
		@Override
		public void execute(Value thisValue, Value otherValue, Type type)
                throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
			thisValue.add(otherValue, type);
		}
	},SUB{
		@Override
		public void execute(Value thisValue, Value otherValue, Type type)
                throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
			thisValue.sub(otherValue, type);
		}
	},MUL{
		@Override
		public void execute(Value thisValue, Value otherValue, Type type)
                throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
			thisValue.mul(otherValue, type);
		}
	},DIV{
		@Override
		public void execute(Value thisValue, Value otherValue, Type type)
                throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
			thisValue.div(otherValue, type);
		}
	},IN{
		@Override
		public void execute(Value thisValue, Value otherValue, Type type)
                throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
			thisValue.in(otherValue, type);
		}
	},NOTIN{
		@Override
		public void execute(Value thisValue, Value otherValue, Type type)
                throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
			thisValue.notin(otherValue, type);
		}
	},SIM{
		@Override
		public void execute(Value thisValue, Value otherValue, Type type)
                throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
			thisValue.sim(otherValue, type);
		}
	},NOTISM{
		@Override
		public void execute(Value thisValue, Value otherValue, Type type)
                throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
			thisValue.notsim(otherValue, type);
		}
	},UNION{
		@Override
		public void execute(Value thisValue, Value otherValue, Type type)
                throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
			thisValue.union(otherValue, type);
		}
	},INTERSECT{
		@Override
		public void execute(Value thisValue, Value otherValue, Type type)
                throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
			thisValue.intersect(otherValue, type);
		}
	},EXCEPT{
		@Override
		public void execute(Value thisValue, Value otherValue, Type type)
                throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
			thisValue.except(otherValue, type);
		}
	},SUPSET{
		@Override
		public void execute(Value thisValue, Value otherValue, Type type)
                throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
			thisValue.supset(otherValue, type);
		}
	},SUBSET{
		@Override
		public void execute(Value thisValue, Value otherValue, Type type)
                throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
			thisValue.subset(otherValue, type);
		}
	};	
		
	public abstract void execute(Value thisValue, Value otherValue, Type type) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException;
}
