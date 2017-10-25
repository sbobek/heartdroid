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
import heart.exceptions.RangeFormatException;
import heart.exceptions.UnknownValueException;
import heart.xtt.Type;

public class Range extends Value{
	
	public static final String RANGE_SET_ERROR_MESSAGE = "Operation is not applicable for values other than range or set value.";
	public static final String EQ_ERROR_MESSAGE = "Operation eq(neq) is not applicable for values other than range value.";
	/**
	 * A value representing beginning of the range
	 */
	private Value from;
	
	/**
	 * A value representing the end of the range
	 */
	private Value to;
	
	/**
	 * A variable indicating weather the left boundary of a range should be considered as a member of this range.
	 * In other words, the variable indicates weather the left boundary is inclusive.
	 * By default it is set to be true.
	 */
	private boolean leftInclusive = true;
	
	/**
	 * A variable indicating weather the right boundary of a range should be considered as a member of this range.
	 * In other words, the variable indicates weather the right boundary is inclusive.
	 * By default it is set to be true.
	 */
	private boolean rightInclusive = true;
	
	/**
	 * Default constructor.
	 * Sets boundaries to be null;
	 * By default the right and left inclusion of the boundaries are set to be true.
	 */
	public Range() {
		setCertaintyFactor(0.0f);
	}
	
	/**
	 * Copy constructor. It creates new range that is a copy of a range given by a parameter.
	 * The constructor is a shallow copy of the {@link #from} and {@link #to} values.
	 * 
	 * @param other range to be copied
	 */
	public Range(Range other){
		try {
			this.from = other.from;
			this.to = other.to;
			this.setLeftInclusive(other.isLeftInclusive());
			this.setRightInclusive(other.isRightInclusive());
			this.setCertaintyFactor(other.getCertaintyFactor());
			this.setTimestamp(other.getTimestamp());
		} catch (RangeFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Range constructor that sets the range boundaries according to given parameters.
	 * It also sets the boundaries to be included within a range.
	 * @param from A left boundary of the range
	 * @param to A right boundary of the range
	 * @throws RangeFormatException 
	 */
	public Range(SimpleNumeric from, SimpleNumeric to) throws RangeFormatException{
		setRange(from, to);
	}
	
	/**
	 * Range constructor that sets the range boundaries according to given parameters.
	 * It also sets the boundaries to be included within a range.
	 * @param from A left boundary of the range
	 * @param to A right boundary of the range
	 * @param certaintyFactor A certainty factor that the range have inside given values. 
	 * @throws RangeFormatException 
	 */
	public Range(SimpleNumeric from, SimpleNumeric to, float certaintyFactor) throws RangeFormatException{
		setRange(from, to);
		setCertaintyFactor(certaintyFactor);
	}
	
	/**
	 * Range constructor that sets the range boundaries according to given parameters.
	 * It also sets the boundaries to be included within a range.
	 * @param from A left boundary of the range
	 * @param to A right boundary of the range
	 * @throws RangeFormatException 
	 */
	public Range(SimpleSymbolic from, SimpleSymbolic to) throws RangeFormatException{
		setRange(from, to);
	}
	
	/**
	 * Range constructor that sets the range boundaries according to given parameters.
	 * It also sets the boundaries to be included within a range.
	 * @param from A left boundary of the range
	 * @param to A right boundary of the range
	 * @param certaintyFactor A certainty factor that the range have inside given values. 
	 * @throws RangeFormatException 
	 */
	public Range(SimpleSymbolic from, SimpleSymbolic to, float certaintyFactor) throws RangeFormatException{
		setRange(from, to);
		setCertaintyFactor(certaintyFactor);
	}
	
	
	public Range(SimpleNumeric from, boolean includeFrom, SimpleNumeric to, boolean includeTo) throws RangeFormatException{
		setRange(from, to);
		setLeftInclusive(includeFrom);
		setRightInclusive(includeTo);
	}
	
	public Range(SimpleSymbolic from, boolean includeFrom, SimpleSymbolic to, boolean includeTo) throws RangeFormatException{
		setRange(from, to);
		setLeftInclusive(includeFrom);
		setRightInclusive(includeTo);
	}
	
	public Range(SimpleNumeric from, boolean includeFrom, SimpleNumeric to, boolean includeTo, float certaintyFactor) throws RangeFormatException{
		setRange(from, to);
		setLeftInclusive(includeFrom);
		setRightInclusive(includeTo);
		setCertaintyFactor(certaintyFactor);
	}
	
	public Range(SimpleSymbolic from, boolean includeFrom, SimpleSymbolic to, boolean includeTo, float certaintyFactor) throws RangeFormatException{
		setRange(from, to);
		setLeftInclusive(includeFrom);
		setRightInclusive(includeTo);
		setCertaintyFactor(certaintyFactor);
	}

	@Override
	public boolean eq(Value v, Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		Boolean checked = checkDomainConditions(v, t);
		if(checked != null)
			return checked;
		try{
			Range rn = (Range) v;
			if(rn.getFrom().eq(this.getFrom(), t) && rn.getTo().eq(this.getTo(), t) && 
					this.isLeftInclusive() == rn.isLeftInclusive() && 
					this.isRightInclusive() == rn.isRightInclusive()){
				return true;
			}else{
				return false;
			}			
		}catch(ClassCastException e){
			throw new UnsupportedOperationException(EQ_ERROR_MESSAGE);
		}
	}
	
	@Override
	public boolean neq(Value v, Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		return !eq(v,t);
	}

	@Override
	public boolean supset(Value v, Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		checkOtherDomainConditions(v, t);
		
		if(v instanceof Range){
			Range rangeNumeric = (Range)v;
			//to chyba jest zle
			if(this.getFrom().lte(rangeNumeric.getFrom(), t) && this.getTo().gte(rangeNumeric.getTo(), t)){
				if(this.getFrom().eq(rangeNumeric.getFrom(), t) && !this.isLeftInclusive()){
					return false;
				}		
				if(this.getTo().gte(rangeNumeric.getTo(), t) && !this.isRightInclusive()){
					return false;
				}
				return true;
			}else{
				return false;
			}
		}else if(v instanceof SetValue){
			SetValue setValue = (SetValue) v;
			for(Value setElement : setValue.getValues()){
				if(setElement instanceof SimpleSymbolic || setElement instanceof SimpleNumeric){
					if(!setElement.in(this, t)) return false;
				}else{ //if(!this.supset(setElement, t)){  this assumes that we flatten sets. consider this return always false;
					return false;					
				}
			}
		    return true;	
		}else if(v instanceof Null){
            throw new UnknownValueException("Error while evaluating "+this+" add "+v+". Operator not supported for this types.");
        }else{
			throw new UnsupportedOperationException(RANGE_SET_ERROR_MESSAGE);
		}
	}

	@Override
	public boolean subset(Value v, Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		checkOtherDomainConditions(v, t);
		return v.supset(this, t);
	}

	@Override
	public boolean sim(Value v, Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		checkOtherDomainConditions(v, t);
		
		SetValue result = (SetValue) this.intersect(v, t);
		return !result.isEmpty();
	}
	
	@Override
	public boolean notsim(Value v, Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		return !sim(v,t);
	}

	@Override
	public Value intersect(Value v, Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		checkOtherDomainConditions(v, t);
		
		SetValue result = new SetValue();
		if(v instanceof Range){
			Range rangeNumeric = (Range)v;
			result.appendValues(this.intersect(rangeNumeric,t));
		}else if(v instanceof SetValue){
			SetValue setValue = (SetValue) v;
			for(Value setElement : setValue.getValues()){
				if(setElement instanceof SimpleSymbolic || setElement instanceof SimpleNumeric){
					if(setElement.in(this, t)) result.appendValue(setElement);
				}
			}	
		}else if(v instanceof Null){
            throw new UnknownValueException("Error while evaluating "+this+" add "+v+". Operator not supported for this types.");
        }else{
			throw new UnsupportedOperationException(RANGE_SET_ERROR_MESSAGE);
		}
		
		return result;
	}


	@Override	
	public Value union(Value v, Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		checkOtherDomainConditions(v, t);
		
		SetValue result = new SetValue();
		if(v instanceof Range){
			Range rangeNumeric = (Range)v;
			result.appendValues(this.union(rangeNumeric,t));
		}else if(v instanceof SetValue){
			result.appendValue(this);
			SetValue setValue = (SetValue) v;
			for(Value setElement : setValue.getValues()){
				if(setElement instanceof SimpleSymbolic || setElement instanceof SimpleNumeric){									
					if(!setElement.in(this, t)) result.appendValue(setElement);
				}
			}	
		}else if(v instanceof Null){
            throw new UnknownValueException("Error while evaluating "+this+" add "+v+". Operator not supported for this types.");
        }else{
			throw new UnsupportedOperationException(RANGE_SET_ERROR_MESSAGE);
		}
		
		return result;
	}

	@Override
	public Value except(Value v, Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		checkOtherDomainConditions(v, t);
		SetValue result = new SetValue();
		try{
			if(v instanceof SimpleSymbolic || v instanceof SimpleNumeric){
				//TODO: wrong! if we delete integer value, the next should be next double according to the precision. 
				//TODO: the same with symbolic - the only condition is existence of type
				if(this.getFrom().eq(v, t) && this.isLeftInclusive()){
					Range rangeResult = new Range(this);
					rangeResult.setLeftInclusive(false);
					result.appendValue(rangeResult);			
				}else if(this.getTo().eq(v, t)){
					Range rangeResult = new Range(this);
					rangeResult.setRightInclusive(false);
					result.appendValue(rangeResult);
				}else if(v.in(this,t)){				
					Value fromOne = this.getFrom();
					Value toOne = v; //exclusive
					Value fromTwo = v; //exclusive
					Value toTwo = this.getTo();	
					
	
					if(fromOne instanceof SimpleSymbolic){
						if(toOne instanceof SimpleNumeric){
							toOne = fromTwo = SimpleSymbolic.findInTheDomain(toOne, t);
						}
						result.appendValue(new Range((SimpleSymbolic)fromOne, this.isLeftInclusive(), 
								(SimpleSymbolic)toOne, false));
						result.appendValue(new Range((SimpleSymbolic)fromTwo, false, 
								(SimpleSymbolic)toTwo, this.isRightInclusive()));
					}else if(fromOne instanceof SimpleNumeric){
						if(toOne instanceof SimpleSymbolic){
							toOne = fromTwo = new SimpleNumeric(((SimpleSymbolic)v).getOrder().doubleValue());
						}
						result.appendValue(new Range((SimpleNumeric)fromOne, this.isLeftInclusive(), 
								(SimpleNumeric)toOne, false));
						result.appendValue(new Range((SimpleNumeric)fromTwo, false, 
								(SimpleNumeric)toTwo, this.isRightInclusive()));
					}
					
				}else{
					result.appendValue(this);
				}
				
			}else if(v instanceof Range){
				Range rangeNumeric = (Range)v;
				result.appendValues(this.except(rangeNumeric,t));
			}else if(v instanceof SetValue){
				SetValue setValue = (SetValue) v;
				result.appendValue(new Range(this));
				for(Value setElement : setValue.getValues()){
					result = (SetValue) result.except(setElement, t);
				}	
			}else if(v instanceof Null){
                throw new UnknownValueException("Error while evaluating "+this+" add "+v+". Operator not supported for this types.");
            }else{
				throw new UnsupportedOperationException(RANGE_SET_ERROR_MESSAGE);
			}
		}catch(RangeFormatException rfe){
			//DEBUG: in case when the range created as a result of the except operation is wrong, return empty set.
		}
		return result;
	}
	
	/**
	 * It sets the range boundaries.
	 * Left and right inclusion is set to true.
	 * 
	 * @param from left boundary of a range
	 * @param to right boundary of a range
	 */
	public void setRange(SimpleSymbolic from, SimpleSymbolic to) throws RangeFormatException {
		//TODO check if from < to (or if there are any numbers within eg. [0.1 to 0.2] prec ) and in other case throw an exception
		this.from = from;
		this.to = to;
	}
	
	/**
	 * It sets the range boundaries.
	 * Left and right inclusion is set to true.
	 * 
	 * @param from left boundary of a range
	 * @param to right boundary of a range
	 */
	public void setRange(SimpleNumeric from, SimpleNumeric to) throws RangeFormatException {
		//TODO check if from < to  and in other case throw an exception
		this.from = from;
		this.to = to;
	}

	/**
	 * Getter for a left boundary of a range.
	 * @return left boundary of a range
	 */
	public Value getFrom() {
		return from;
	}

	/**
	 * Getter for a right boundary of a range.
	 * @return right boundary of a range
	 */
	public Value getTo() {
		return to;
	}

	
	@Override
	public String toString() {
		return ((isLeftInclusive() ? "<" : "(")+from+" ; "+to+(isRightInclusive() ? ">" : ")"));
	}
	
	/**
	 * Method that performs intersection of two ranges.
	 * If the intersection is one element set it is also returned as 
	 * one element set.
	 * 
	 * @param rn the range that should be checked for intersection
	 * @param t according to which the operation should be performed
	 * @return result of the intersection that is a {@link SetValue}.
	 * @throws NotInTheDomainException 
	 * @throws UnsupportedOperationException 
	 */
	private SetValue intersect(Range rn, Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		Value from = null;
		Value to = null;
		boolean includeRight = true;
		boolean includeLeft = true;
		SetValue sv = new SetValue();
		
		//this is the equality case |               |
		if(this.getFrom().eq(rn.getFrom(), t) && this.getTo().eq(rn.getTo(), t)){
			includeLeft = this.isLeftInclusive() && rn.isLeftInclusive();
			includeRight = this.isRightInclusive() && rn.isRightInclusive();
			from = this.from;
			to = this.to;
		}
		// this is a case: {     [     ]     } 
		else if(this.getFrom().lt(rn.getFrom(), t) && this.getTo().gt(rn.getTo(), t)){
			from = rn.getFrom();
			to = rn.getTo();
			includeLeft = rn.isLeftInclusive();
			includeRight = rn.isRightInclusive();
		}	
		// this is a case:  [     {     ]     }      
		else if(this.getFrom().lte(rn.getTo(), t) && this.getTo().gte(rn.getTo(), t)){
			from = this.getFrom();
			to = rn.getTo();
			includeLeft = this.isLeftInclusive();
			includeRight = rn.isRightInclusive();
		}	
		// this is a case:  {     [     }     ]
		else if(this.getFrom().lte(rn.getFrom(), t) && this.getTo().gte(rn.getFrom(), t)){
			from = rn.getFrom();
			to = this.getTo();
			includeLeft = rn.isLeftInclusive();
			includeRight = this.isRightInclusive();
		}
		// this is a case : [     {     }     ]
		else if(rn.getFrom().lt(this.getFrom(), t) && rn.getTo().gt(this.getTo(), t)){
			from = this.getFrom();
			to = this.getTo();
			includeLeft = this.isLeftInclusive();
			includeRight = this.isRightInclusive();
		}
		
			
		//check if boundaries are equal and we allow including boundaries values
		if(from != null && from.eq(to, null) && includeLeft && includeRight){
			sv.appendValue(from);
		}else{
			try{
				if(from instanceof SimpleSymbolic){
					sv.appendValue(new Range((SimpleSymbolic)from, includeLeft, (SimpleSymbolic)to, includeRight));
				}else if(from instanceof SimpleNumeric){
					sv.appendValue(new Range((SimpleNumeric)from, includeLeft, (SimpleNumeric)to, includeRight));
				}
			}catch(RangeFormatException rfe){
				//DEBUG: in case when the range that was created as a result of the intersect operation is wrong (e.g. empty)
				//       do not add it to the result set
			}
		}
			
		return sv;
	}
	
	/**
	 * A method that performs union of two sets.
	 * It always returns set. 
	 * 
	 * @param range a range with which the union should be performed.
	 * @param t according to which the operation should be performed
	 * @return result of an union operation performed on the ranges.
	 * @throws NotInTheDomainException 
	 * @throws UnsupportedOperationException 
	 */
	private SetValue union(Range range, Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		Value from = null;
		Value to = null;
		boolean includeRight = true;
		boolean includeLeft = true;
		SetValue sv = new SetValue();
		
		// this is a case: {     [     ]     } 
		if(this.getFrom().lt(range.getFrom(), t) && this.getTo().gt(range.getTo(), t)){
			from = this.getFrom();
			to = this.getTo();
			includeLeft = this.isLeftInclusive();
			includeRight = this.isRightInclusive();
		}	
		// this is a case:  [     {     ]     }      
		else if(this.getFrom().lt(range.getTo(), t) && this.getTo().gt(range.getTo(), t)){
			from = range.getFrom();
			to = this.getTo();
			includeLeft = range.isLeftInclusive();
			includeRight = this.isRightInclusive();
		}	
		// this is a case:  {     [     }     ]
		else if(this.getFrom().lt(range.getFrom(), t) && this.getTo().gt(range.getFrom(), t)){
			from = this.getFrom();
			to = range.getTo();
			includeLeft = this.isLeftInclusive();
			includeRight = range.isRightInclusive();
		}
		// this is a case : [     {     }     ]
		else if(range.getFrom().lt(this.getFrom(), t) && range.getTo().gt(this.getTo(), t)){
			from = range.getFrom();
			to = range.getTo();
			includeLeft = range.isLeftInclusive();
			includeRight = range.isRightInclusive();
		}
		// this is a case : [     ]     {     }  and {     }     [     ] 
		else if(this.getFrom().gt(range.getTo(), t) || this.getTo().lt(range.getFrom(), t)){
			sv.appendValue(this);
			sv.appendValue(range);
			return sv;
		}
		//this is a case : [      |      } 
		else if(this.getFrom().eq(range.getTo(), t)){
			if(!this.isLeftInclusive() && !range.isRightInclusive()){
				// two sets
				sv.appendValue(this);
				sv.appendValue(range);
				return sv;
			}else{
				from = range.getFrom();
				to = this.getTo();
				includeLeft = range.isLeftInclusive();
				includeRight = this.isRightInclusive();
			}
			
		}
		// this is a case : {      |      ]  
		else if(this.getTo().eq(range.getFrom(), t)){
			if(!this.isRightInclusive() && !range.isLeftInclusive()){
				//two sets
				sv.appendValue(this);
				sv.appendValue(range);
				return sv;
			}else{
				from  = this.getFrom();
				to = range.getTo();
				includeLeft = this.isLeftInclusive();
				includeRight = range.isRightInclusive();
			}
		}
		//this is the equality case |               |
		else if(this.getFrom().eq(range.getFrom(), t) && this.getTo().eq(range.getTo(), t)){
			includeLeft = this.isLeftInclusive() || range.isLeftInclusive();
			includeRight = this.isRightInclusive() || range.isRightInclusive();
			from = this.from;
			to = this.to;
		}
		
		try{
			if(from instanceof SimpleSymbolic){
				sv.appendValue(new Range((SimpleSymbolic)from, includeLeft, (SimpleSymbolic)to, includeRight));
			}else if(from instanceof SimpleNumeric){
				sv.appendValue(new Range((SimpleNumeric)from, includeLeft, (SimpleNumeric)to, includeRight));
			}
		}catch(RangeFormatException rfe){
			//DEBUG: in case when the range that was created as a result of the intersect operation is wrong (e.g. empty)
			//       do not add it to the result set
		}
		
		return sv;
	}
	
	/**
	 * A method that removes from the range that calls the method 
	 * the intersection between that range and a range given as a parameter.
	 * 
	 * @param range the range to be subtracted 
	 * @param t according to which the operation should be performed
	 * @return set containing the remaining values.
	 * @throws NotInTheDomainException 
	 * @throws UnsupportedOperationException 
	 */
	private SetValue except(Range range, Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		Value from = null;
		Value to = null;
		boolean includeRight = true;
		boolean includeLeft = true;
		SetValue sv = new SetValue();
		try{
			// this is a case: {     [     ]     } 
			if(this.getFrom().lt(range.getFrom(), t) && this.getTo().gt(range.getTo(), t)){
				//two separate sets
				from = this.getFrom();
				to = range.getFrom();
				includeLeft = this.isLeftInclusive();
				includeRight = !range.isLeftInclusive();
				Range first = null;
				Range second = null;
				if(from instanceof SimpleNumeric){
					first = new Range((SimpleNumeric)from,includeLeft, (SimpleNumeric)to, includeRight);
				}else{
					first = new Range((SimpleNumeric)from,includeLeft, (SimpleNumeric)to, includeRight);
				}
				
				from = range.getTo();
				to  = this.getTo();
				includeLeft = !range.isRightInclusive();
				includeRight = this.isRightInclusive();
				
				if(from instanceof SimpleNumeric){
					second = new Range((SimpleNumeric)from,includeLeft, (SimpleNumeric)to, includeRight);
				}else{
					second = new Range((SimpleNumeric)from,includeLeft, (SimpleNumeric)to, includeRight);
				}
				
				sv.appendValue(first);
				sv.appendValue(second);
				
				return sv;
				
			}	
			// this is a case:  [     {     ]     }      
			else if(this.getFrom().lt(range.getTo(), t) && this.getTo().gt(range.getTo(), t)){
				from = range.getTo();
				to = this.getTo();
				includeLeft = !range.isRightInclusive();
				includeRight = this.isRightInclusive();
			}	
			// this is a case:  {     [     }     ]
			else if(this.getFrom().lt(range.getFrom(), t) && this.getTo().gt(range.getFrom(), t)){
				from = this.getFrom();
				to = range.getFrom();
				includeLeft = this.isLeftInclusive();
				includeRight = !range.isLeftInclusive();
			}
			// this is a case : [     {     }     ]
			else if(range.getFrom().lt(this.getFrom(), t) && range.getTo().gt(this.getTo(), t)){
				from = range.getFrom();
				to = this.getFrom();
				includeLeft = range.isLeftInclusive();
				includeRight = !this.isLeftInclusive();
				Value first = null;
				Value second = null;
				
				if(from instanceof SimpleNumeric){
					first = new Range((SimpleNumeric)from,includeLeft, (SimpleNumeric)to, includeRight);
				}else{
					first = new Range((SimpleNumeric)from,includeLeft, (SimpleNumeric)to, includeRight);
				}
				
				from = this.getTo();
				to  = range.getTo();
				includeLeft = !this.isRightInclusive();
				includeRight = range.isRightInclusive();
				
				if(from instanceof SimpleNumeric){
					second = new Range((SimpleNumeric)from,includeLeft, (SimpleNumeric)to, includeRight);
				}else{
					second = new Range((SimpleNumeric)from,includeLeft, (SimpleNumeric)to, includeRight);
				}
				
				sv.appendValue(first);
				sv.appendValue(second);
				return sv;
			}
			//this is a case : [      |      } 
			else if(this.getFrom().eq(range.getTo(), t)){
				includeLeft = this.isLeftInclusive() && !range.isRightInclusive();
				includeRight = this.isRightInclusive();
				from = this.getFrom();
				to = this.getTo();	
			}
			// this is a case : {      |      ]  
			else if(this.getTo().eq(range.getFrom(), t)){
				includeLeft = this.isLeftInclusive();
				includeRight =  this.isRightInclusive() && !range.isLeftInclusive();
				from = this.getFrom();
				to = this.getTo();
			}
			//this is the equality case |               |
			else if(this.getFrom().eq(range.getFrom(), t) && this.getTo().eq(range.getTo(), t)){
				if(this.isLeftInclusive() && !range.isLeftInclusive()){
					//left value can be added
					sv.appendValue(this.getFrom());
				}
				if(this.isRightInclusive() && !range.isRightInclusive()){
					//right value can be added
					sv.appendValue(this.getTo());
				}
				return sv;
				
			}
			
			if(from instanceof SimpleSymbolic){
				sv.appendValue(new Range((SimpleSymbolic)from, includeLeft, (SimpleSymbolic)to, includeRight));
			}else if(from instanceof SimpleNumeric){
				sv.appendValue(new Range((SimpleNumeric)from, includeLeft, (SimpleNumeric)to, includeRight));
			}
		}catch(RangeFormatException rfe){
			//DEBUG: in case when the range that was created as a result of the except operation is wrong (e.g. empty)
			//       do not add it to the result set
		}

		return sv;
	}

	/**
	 * @return the leftInclusive
	 */
	public boolean isLeftInclusive() {
		return leftInclusive;
	}

	/**
	 * @param leftInclusive the leftInclusive to set
	 */
	public void setLeftInclusive(boolean leftInclusive) throws RangeFormatException {
		this.leftInclusive = leftInclusive;
	}

	/**
	 * @return the rightInclusive
	 */
	public boolean isRightInclusive() {
		return rightInclusive;
	}

	/**
	 * @param rightInclusive the rightInclusive to set
	 */
	public void setRightInclusive(boolean rightInclusive) throws RangeFormatException {
		this.rightInclusive = rightInclusive;
	}

	@Override
	public boolean isInTheDomain(Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {

		return getFrom().in(t.getDomain(), null) && getTo().in(t.getDomain(), null);
	}

	@Override
	public int hashCode() {
		return getFrom().hashCode();
	}

    @Override
    public Value clone() {
        return new Range(this);
    }

    /**
	 * This returns number of elements in a  set.
	 * The set is not flatten, so a set containing X sets will have cardinality equals X
	 * no matter how many elements the inner sets will have.
	 * 
	 * @return number of elements in a set
	 * @throws NotInTheDomainException 
	 * @throws UnsupportedOperationException 
	 */
	public int cardinality(Type t) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		int cardinality = 0;
		//When there is no type, then the cardinality of a range is infinite
		if(t == null){
			return Integer.MAX_VALUE;
		}
		if(t.getBase().equals(Type.BASE_SYMBOLIC)){
			// iterate and count number of elements, as order values can be 2 5 9 
			// so subtracting boundaries may not be good solution
			for(Value v: t.getDomain().getValues()){
				if(v.in(this, t)) cardinality++;
			}
		}else if(t.getBase().equals(Type.BASE_NUMERIC)){
			//If the range is outside the domain, return infinity
			if(!this.isInTheDomain(t)) return Integer.MAX_VALUE;
			cardinality = (int) (((SimpleNumeric)this.getTo()).getValue()-((SimpleNumeric)this.getFrom()).getValue());
			cardinality *= Math.pow(10, t.getPrecision());
			if(!this.isLeftInclusive()) cardinality--;
			if(!this.isRightInclusive()) cardinality--;
		}
		
		
		return cardinality;
	}

	private Boolean checkDomainConditions(Value v, Type t)
            throws NotInTheDomainException, UnknownValueException {
		if(v instanceof Null)  return false;
		checkDomain(v, t);
		if(v instanceof Any) return true;
		
		//if no exception were thrown and v is neither Null nor Any
		return null;
	}
	
	private void checkOtherDomainConditions(Value v, Type t) throws NotInTheDomainException, UnknownValueException {
		if (v instanceof Null)
			throw new UnknownValueException("Range " + this
					+ " can not be compared with Null");
		checkDomain(v, t);
		if (v instanceof Any)
			throw new UnsupportedOperationException("Range " + this
					+ " can not be compared with Any");
	}

}
