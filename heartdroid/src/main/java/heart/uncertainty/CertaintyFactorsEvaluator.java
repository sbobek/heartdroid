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


package heart.uncertainty;

import heart.State;
import heart.WorkingMemory;
import heart.alsvfd.*;

import static heart.alsvfd.Formulae.ConditionalOperator;

import heart.alsvfd.expressions.AttributeExpressionInterface;
import heart.alsvfd.expressions.ExpressionInterface;
import heart.exceptions.AttributeNotRegisteredException;
import heart.exceptions.NotInTheDomainException;
import heart.exceptions.UnknownValueException;
import heart.xtt.Type;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class CertaintyFactorsEvaluator implements UncertainTrueEvaluator{
	
	public static final float MAX_CERTAINTY = 1;
	public static final float MIN_CERTAINTY = -1;
	public static final float SATISFIABILITY_THRESHOLD = 0.0f;

	@Override
	public UncertainTrue evaluateUncertainEq(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		
		Value attributeValue = lhs.evaluate(wm);
		Value rhsv = rhs.evaluate(wm);
		Type type = lhs.evaluateType(wm);

		// It means that once we consider evaluation in terms of uncertainty, 
		// If the value is completely unknown everything is possible.
		if(attributeValue instanceof Null && !(rhs.evaluate(wm) instanceof Null)){
			return new UncertainTrue(0.0f);
		}

        boolean logicalValue = false;
        try {
            logicalValue = attributeValue.eq(rhsv, type);
        } catch (UnknownValueException e) {
            // This in fact will never happen, as the checking condition is checked above
            return new UncertainTrue(0.0f);
        }

        return evaluateUncertainTrueValue(attributeValue, rhsv, wm, ConditionalOperator.EQ, type, logicalValue);

	}


	@Override
	public UncertainTrue evaluateUncertainNeq(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		Value attributeValue = lhs.evaluate(wm);
		Value rhsv = rhs.evaluate(wm);
		Type type = lhs.evaluateType(wm);
		// It means that once we consider evaluation in terms of uncertainty, 
		// If the value is completely unknown everything is possible.
		if(attributeValue instanceof Null && !(rhs instanceof Null)){
			return new UncertainTrue(0.0f);
		}

        boolean logicalValue = false;
        try {
            logicalValue = attributeValue.neq(rhsv, type);
        } catch (UnknownValueException e) {
            // This in fact will never happen, as the checking condition is checked above
            return new UncertainTrue(0.0f);
        }
        return evaluateUncertainTrueValue(attributeValue, rhsv, wm,  ConditionalOperator.NEQ, type, logicalValue);
	}


	@Override
	public UncertainTrue evaluateUncertainIn(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		Value attributeValue = lhs.evaluate(wm);
		Value rhsv = rhs.evaluate(wm);
		Type type = lhs.evaluateType(wm);
		// It means that once we consider evaluation in terms of uncertainty, 
		// If the value is completely unknown everything is possible.
		if(attributeValue instanceof Null && !(rhsv instanceof Null)){
			return new UncertainTrue(0.0f);
		}

        boolean logicalValue = false;
        try {
            logicalValue = attributeValue.in(rhsv, type);
        } catch (UnknownValueException e) {
            // This in fact will never happen, as the checking condition is checked above
            return new UncertainTrue(0.0f);
        }
        return evaluateUncertainTrueValue(attributeValue, rhsv,wm, ConditionalOperator.IN, type, logicalValue);
	}


	@Override
	public UncertainTrue evaluateUncertainNotin(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		Value attributeValue = lhs.evaluate(wm);
		Value rhsv = rhs.evaluate(wm);
		Type type = lhs.evaluateType(wm);
		// It means that once we consider evaluation in terms of uncertainty, 
		// If the value is completely unknown everything is possible.
		if(attributeValue instanceof Null && !(rhsv instanceof Null)){
			return new UncertainTrue(0.0f);
		}

        boolean logicalValue = false;
        try {
            logicalValue = attributeValue.notin(rhsv, type);
        } catch (UnknownValueException e) {
            // This in fact will never happen, as the checking condition is checked above
            return new UncertainTrue(0.0f);
        }
        return evaluateUncertainTrueValue(attributeValue, rhsv, wm, ConditionalOperator.NOTIN, type, logicalValue);
	}


	@Override
	public UncertainTrue evaluateUncertainSubset(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		Value attributeValue = lhs.evaluate(wm);
		Value rhsv = rhs.evaluate(wm);
		Type type = lhs.evaluateType(wm);
		// It means that once we consider evaluation in terms of uncertainty, 
		// If the value is completely unknown everything is possible.
		if(attributeValue instanceof Null && !(rhsv instanceof Null)){
			return new UncertainTrue(0.0f);
		}

        boolean logicalValue = false;
        try {
            logicalValue = attributeValue.subset(rhsv, type);
        } catch (UnknownValueException e) {
            // This in fact will never happen, as the checking condition is checked above
            return new UncertainTrue(0.0f);
        }
        return evaluateUncertainTrueValue(attributeValue, rhsv, wm, ConditionalOperator.SUBSET, type, logicalValue);
	}


	@Override
	public UncertainTrue evaluateUncertainSupset(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
			throws UnsupportedOperationException,	NotInTheDomainException {
		Value attributeValue = lhs.evaluate(wm);
		Value rhsv = rhs.evaluate(wm);
		Type type = lhs.evaluateType(wm);
		// It means that once we consider evaluation in terms of uncertainty, 
		// If the value is completely unknown everything is possible.
		if(attributeValue instanceof Null && !(rhsv instanceof Null)){
			return new UncertainTrue(0.0f);
		}

        boolean logicalValue = false;
        try {
            logicalValue = attributeValue.supset(rhsv, type);
        } catch (UnknownValueException e) {
            // This in fact will never happen, as the checking condition is checked above
            return new UncertainTrue(0.0f);
        }
        return evaluateUncertainTrueValue(attributeValue, rhsv, wm, ConditionalOperator.SUPSET, type, logicalValue);
	}


	@Override
	public UncertainTrue evaluateUncertainSim(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		Value attributeValue = lhs.evaluate(wm);
		Value rhsv = rhs.evaluate(wm);
		Type type = lhs.evaluateType(wm);
		// It means that once we consider evaluation in terms of uncertainty, 
		// If the value is completely unknown everything is possible.
		if(attributeValue instanceof Null && !(rhsv instanceof Null)){
			return new UncertainTrue(0.0f);
		}

        boolean logicalValue = false;
        try {
            logicalValue = attributeValue.sim(rhsv, type);
        } catch (UnknownValueException e) {
            // This in fact will never happen, as the checking condition is checked above
            return new UncertainTrue(0.0f);
        }
        return evaluateUncertainTrueValue(attributeValue, rhsv, wm, ConditionalOperator.SIM, type, logicalValue);
	}


	@Override
	public UncertainTrue evaluateUncertainNotsim(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
			throws UnsupportedOperationException,NotInTheDomainException {
		Value attributeValue = lhs.evaluate(wm);
		Value rhsv = rhs.evaluate(wm);
		Type type = lhs.evaluateType(wm);
		// It means that once we consider evaluation in terms of uncertainty, 
		// If the value is completely unknown everything is possible.
		if(attributeValue instanceof Null && !(rhsv instanceof Null)){
			return new UncertainTrue(0.0f);
		}

        boolean logicalValue = false;
        try {
            logicalValue = attributeValue.notsim(rhsv, type);
        } catch (UnknownValueException e) {
            // This in fact will never happen, as the checking condition is checked above
            return new UncertainTrue(0.0f);
        }
        return evaluateUncertainTrueValue(attributeValue, rhsv, wm, ConditionalOperator.NOTSIM, type, logicalValue);
	}
	
	@Override
	public UncertainTrue evaluateUncertainLt(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		Value attributeValue = lhs.evaluate(wm);
		Value rhsv = rhs.evaluate(wm);
		Type type = lhs.evaluateType(wm);
		// It means that once we consider evaluation in terms of uncertainty, 
		// If the value is completely unknown everything is possible.
		if(attributeValue instanceof Null && !(rhsv instanceof Null)){
			return new UncertainTrue(0.0f);
		}

        boolean logicalValue = false;
        try {
            logicalValue = attributeValue.lt(rhsv, type);
        } catch (UnknownValueException e) {
            // This in fact will never happen, as the checking condition is checked above
            return new UncertainTrue(0.0f);
        }
        return evaluateUncertainTrueValue(attributeValue, rhsv, wm, ConditionalOperator.LT, type, logicalValue);
	}

	

	@Override
	public UncertainTrue evaluateUncertainLte(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		Value attributeValue = lhs.evaluate(wm);
		Value rhsv = rhs.evaluate(wm);
		Type type = lhs.evaluateType(wm);
		// It means that once we consider evaluation in terms of uncertainty, 
		// If the value is completely unknown everything is possible.
		if(attributeValue instanceof Null && !(rhsv instanceof Null)){
			return new UncertainTrue(0.0f);
		}

        boolean logicalValue = false;
        try {
            logicalValue = attributeValue.lte(rhsv, type);
        } catch (UnknownValueException e) {
            // This in fact will never happen, as the checking condition is checked above
            return new UncertainTrue(0.0f);
        }
        return evaluateUncertainTrueValue(attributeValue, rhsv, wm, ConditionalOperator.LTE, type, logicalValue);
	}


	@Override
	public UncertainTrue evaluateUncertainGt(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		Value attributeValue = lhs.evaluate(wm);
		Value rhsv = rhs.evaluate(wm);
		Type type = lhs.evaluateType(wm);
		// It means that once we consider evaluation in terms of uncertainty, 
		// If the value is completely unknown everything is possible.
		if(attributeValue instanceof Null && !(rhsv instanceof Null)){
			return new UncertainTrue(0.0f);
		}

        boolean logicalValue = false;
        try {
            logicalValue = attributeValue.gt(rhsv, type);
        } catch (UnknownValueException e) {
            // This in fact will never happen, as the checking condition is checked above
            return new UncertainTrue(0.0f);
        }
        return evaluateUncertainTrueValue(attributeValue, rhsv, wm, ConditionalOperator.GT, type, logicalValue);
	}


	@Override
	public UncertainTrue evaluateUncertainGte(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		Value attributeValue = lhs.evaluate(wm);
		Value rhsv = rhs.evaluate(wm);
		Type type = lhs.evaluateType(wm);
		// It means that once we consider evaluation in terms of uncertainty, 
		// If the value is completely unknown everything is possible.
		if(attributeValue instanceof Null && !(rhsv instanceof Null)){
			return new UncertainTrue(0.0f);
		}

        boolean logicalValue = false;
        try {
            logicalValue = attributeValue.gte(rhsv, type);
        } catch (UnknownValueException e) {
            // This in fact will never happen, as the checking condition is checked above
            return new UncertainTrue(0.0f);
        }
        return evaluateUncertainTrueValue(attributeValue, rhsv, wm, ConditionalOperator.GTE, type, logicalValue);
	}

	@Override
	public UncertainTrue evaluateTimeRange(AttributeExpressionInterface lhs, ExpressionInterface rhs, Formulae.TimeBasedParameter tbp, Method evaluatorMethod, WorkingMemory wm) throws NotInTheDomainException {
		LinkedList<State> allStates = wm.getHistoryLogCopy();
		allStates.addLast(wm.getCurrentState());
		LinkedList<Value> oldValues = wm.findHistoricalValues(allStates, tbp.getRtp(), lhs.getAttributeName());
		ArrayList<Float> evaluationCertainties = new ArrayList<>(oldValues.size());


		try {
			for(Value o: oldValues) {
				WorkingMemory fakeWm = new WorkingMemory();
				fakeWm.setAttributeValue(lhs.getAttribute(),o,true);
				UncertainTrue ut = (UncertainTrue)evaluatorMethod.invoke(this,lhs, rhs, fakeWm);
				evaluationCertainties.add(ut.getCertinatyFactor());
			}
			Collections.sort(evaluationCertainties,Collections.reverseOrder());
			int lastElement = (int)(Math.round(evaluationCertainties.size()*tbp.getPercentage()+0.5)-1);

			switch(tbp.getQuantifier()) {
				case MIN:
					if (evaluationCertainties.get(lastElement) > 0) {
						float result = evaluationCertainties.get(lastElement);
						for (int index = lastElement + 1;
							 (index < evaluationCertainties.size() && evaluationCertainties.get(index) >= 0);
							 index++) {
							result = this.calculateCumulative(result, evaluationCertainties.get(index));
						}
						return new UncertainTrue(result);
					} else if (evaluationCertainties.get(lastElement) == 0) {
						return new UncertainTrue(0.0f);
					} else {
						float result = evaluationCertainties.get(lastElement);
						for (int index = lastElement - 1;
							 (index >= 0 && evaluationCertainties.get(index) <= 0);
							 index--) {
							result = this.calculateCumulative(result, evaluationCertainties.get(index));
						}
						return new UncertainTrue(result);

					}
				case MAX:
					if ((lastElement + 1) < evaluationCertainties.size() && evaluationCertainties.get(lastElement + 1) < 0) {
						float result = -evaluationCertainties.get(lastElement - 1);
						for (int index = lastElement;
							 (index >= 0 && evaluationCertainties.get(index) <= 0);
							 index--) {
							result = this.calculateCumulative(result, -evaluationCertainties.get(index));
						}
						return new UncertainTrue(result);
					}else if ((lastElement + 1) < evaluationCertainties.size() && evaluationCertainties.get(lastElement + 1) == 0){
						return new UncertainTrue(0.0f);
					}else if ((lastElement+1) < evaluationCertainties.size() && evaluationCertainties.get(lastElement+1) > 0){
						float result = -evaluationCertainties.get(lastElement+1);
						for(int index = lastElement+2;
							(index < evaluationCertainties.size() && evaluationCertainties.get(index) >= 0);
							index++){
							result = this.calculateCumulative(result, -evaluationCertainties.get(index));
						}
						return new UncertainTrue(result);

					}
					break;
				case EXACTLY:
					if(evaluationCertainties.get(lastElement) > 0 &&
							(lastElement+1) < evaluationCertainties.size() &&
							evaluationCertainties.get(lastElement+1) < 0){
						return new UncertainTrue(evaluationCertainties.get(lastElement));
					}else if(evaluationCertainties.get(lastElement) == 0 ||
							((lastElement+1) < evaluationCertainties.size() &&
									evaluationCertainties.get(lastElement+1) == 0)){
						return new UncertainTrue(0.0f);
					}else if((lastElement+1) < evaluationCertainties.size() &&
							evaluationCertainties.get(lastElement+1) > 0){
						//min
						float result = evaluationCertainties.get(lastElement+1);
						for(int index = lastElement+2;
							(index < evaluationCertainties.size() && evaluationCertainties.get(index) >= 0);
							index++){
							result += this.calculateCumulative(result, -evaluationCertainties.get(index));
						}
						return new UncertainTrue(result);
					}else if(evaluationCertainties.get(lastElement) < 0 ){
						float result = evaluationCertainties.get(lastElement);
						for(int index  = lastElement-1; (index >= 0 && evaluationCertainties.get(index) <=0);index--){
							result += this.calculateCumulative(result, evaluationCertainties.get(index));
						}
						return new UncertainTrue(result);
					}
					break;
				default:
					break;
			}

		} catch (IllegalAccessException e) {
			//TODO
		} catch (InvocationTargetException e) {
			//TODO
		} catch (AttributeNotRegisteredException e) {
			// This will never happen, as autoregister flag is on
		}
		return new UncertainTrue(-1.0f);
	}

	/**
	 * The method calculates the certainty of the formula being true. 
	 * It uses the certainty factors algebra, but slightly modified 
	 * to allow evaluating expressions like [a(0.2),b(0.3),c(0.9)] neq [c,d].
	 * In standard certainty factors algebra this could not be evaluated and it sometimes lead to 
	 * Now, the cumulative rules equations are used to measure the similarities between sets.
	 * The certainty that the above formula is true is now calculated as 
	 * cf(sim([a(0.2),b(0.3),c(0.9)] AND [a(0.2),b(0.3),c(0.9)]\[c,d])))
	 * It can be translated as cumulative certainty that the LHS set 
	 * is similar the intersection of the LHS set and the set complement to the RHS set
	 * 
	 * @param lhsv The LHS of the formula
	 * @param lhsv The RHS of the formula
	 * @param wm The working memeory element
	 * @param op The operation
	 * @param type The type of the lhs
	 * @param logicalValue The logical value that was evaluated without taking into consideration the certaintyFactors
	 * 
	 * @return The uncertainTrue value that represents the certainty of formula being true. 
	 * In case the formula is false it is represented by the UncertainTrue with negative certainty.
	 * @throws NotInTheDomainException 
	 * @throws UnsupportedOperationException 
	 */
	private UncertainTrue evaluateUncertainTrueValue(Value lhsv, Value rhsv, WorkingMemory wm,
			ConditionalOperator op, Type type, boolean logicalValue) throws UnsupportedOperationException, NotInTheDomainException {
		
		if(rhsv instanceof Null || rhsv instanceof Any){
			if(logicalValue == false)
				return new UncertainTrue(getMinCertainty());
			else
				return new UncertainTrue(getMaxCertainty());
		}else{
			float formulaCertainty = 0;
			//For simple attributes just return the certainty value of an attribute
			if(lhsv instanceof SimpleSymbolic || lhsv instanceof SimpleNumeric){
				formulaCertainty = lhsv.getCertaintyFactor();
				formulaCertainty = (logicalValue==true ? formulaCertainty : -formulaCertainty );
			}else {
                try {
                    if (op.equals(ConditionalOperator.SIM)) {
                        if (logicalValue == true) {
                            formulaCertainty = sim(lhsv, rhsv, type, wm);
                        } else {
                            formulaCertainty = -sim(lhsv, getComplement(lhsv, rhsv, type, wm), type, wm);
                        }

                    } else if (op.equals(ConditionalOperator.NOTSIM)) {
                        if (logicalValue == true) {
                            sim(lhsv, getComplement(lhsv, rhsv, type, wm), type, wm);
                        } else {
                            formulaCertainty = -sim(lhsv, rhsv, type, wm);
                        }

                    } else if (op.equals(ConditionalOperator.EQ)) {
                        if (logicalValue == true) {
                            formulaCertainty = getMinCF(((SetValue) lhsv).getValues());
                        } else {
                            SetValue diff = getDifference(lhsv, rhsv, type, wm);
                            formulaCertainty = -sim(lhsv, diff, type, wm);
                        }

                    } else if (op.equals(ConditionalOperator.NEQ)) {
                        if (logicalValue == true) {
                            SetValue diff = getDifference(lhsv, rhsv, type, wm);
                            formulaCertainty = sim(lhsv, diff, type, wm);
                        } else {
                            formulaCertainty = -getMinCF(((SetValue) lhsv).getValues());
                        }
                    } else if (op.equals(ConditionalOperator.SUPSET)) {
                        if (logicalValue == true) {
                            //return the weakest chain element that makes it superset
                            SetValue intersection = null;
                            intersection = (SetValue) lhsv.intersect(rhsv, type);
                            formulaCertainty = getMinCF(intersection.getValues());

                        } else {
                            SetValue comp = getComplement(lhsv, rhsv, type, wm);
                            SetValue intersect = null;
                            intersect = (SetValue) lhsv.intersect(comp, type);
                            // return  how the {Domain\value} is similar to the attribute value
                            formulaCertainty = -sim(lhsv, intersect, type, wm);

                        }
                    } else if (op.equals(ConditionalOperator.SUBSET)) {
                        if (logicalValue == true) {
                            formulaCertainty = getMinCF(((SetValue) lhsv).getValues());
                        } else {
                            SetValue comp = getComplement(lhsv, rhsv, type, wm);
                            SetValue intersect = null;
                            intersect = (SetValue) lhsv.intersect(comp, type);
                            // return  how the {Domain\value} is similar to the attribute value
                            formulaCertainty = -sim(lhsv, intersect, type, wm);


                        }

                    }

                }catch(UnknownValueException e) {
                    formulaCertainty = 0.0f;
                }
            }
		    return new UncertainTrue(formulaCertainty);
			
		}
	}
	
	private static float sim(Value lhsv, Value rhsv, Type type,  WorkingMemory wm) throws UnsupportedOperationException, NotInTheDomainException {
		float formulaCertainty = 0;

        SetValue intersection = null;
        try {
            intersection = (SetValue) lhsv.intersect(rhsv, type);
        } catch (UnknownValueException e) {
            return formulaCertainty;
        }

        if(!intersection.getValues().isEmpty()){
				List<Value> vals = intersection.getValues();
				float ci=intersection.getValues().get(0).getCertaintyFactor();
				for(int j=1; j < vals.size();j++){
					float cj = vals.get(j).getCertaintyFactor();
					ci = calculateCumulative(ci,cj);
				}
				formulaCertainty = ci;
			}
	
		return formulaCertainty;
	}

	public static float calculateCumulative(float ci, float cj){
		if(ci >= 0 && cj >= 0){
			ci = ci+cj-ci*cj;
		}else if(ci <= 0 && cj <= 0){
			ci = ci+cj + ci*cj;
		}else if(ci*cj != 0 && ci*cj != -1){
			ci = (ci+cj)/(1-(ci<cj?ci:cj));
		}
		return ci;
	}
	
	private static float getMinCF(List<Value> values){
		float cf = MAX_CERTAINTY;
		for(Value v: values){
			if(v.getCertaintyFactor() < cf){
				cf = v.getCertaintyFactor();
			}
		}
		return cf;
	}
	
	private static SetValue getComplement(Value lhsv, Value rhsv, Type type,  WorkingMemory wm) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		SetValue complement = (SetValue)type.getDomain().except(rhsv, type);
		for(Value atValue: ((SetValue)lhsv).getValues()){
			for(Value compValue: complement.getValues()){
				if(atValue.eq(compValue, type)){
					compValue.setCertaintyFactor(atValue.getCertaintyFactor());
				}
			}
		}
		return complement;
	}
	
	private static SetValue getDifference(Value lhsv, Value rhsv, Type type,  WorkingMemory wm) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		SetValue diff = (SetValue)lhsv.except(rhsv, type);
		
		for(Value atValue: ((SetValue)lhsv).getValues()){
			for(Value compValue: diff.getValues()){
				if(atValue.eq(compValue, type)){
					compValue.setCertaintyFactor(atValue.getCertaintyFactor());
				}
			}
		}
		return diff;
	}


	@Override
	public float getMinCertainty() {
		return MIN_CERTAINTY;
	}


	@Override
	public float getMaxCertainty() {
		return MAX_CERTAINTY;
	}

	@Override
	public float getSatisfiabilityThreshold() {
		return SATISFIABILITY_THRESHOLD;
	}

	@Override
	public AmbiguityResolver getAmbiguityResolver() {
		return new CertaintyFactorsAmbiguityResolver();
	}





}
