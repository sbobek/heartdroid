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
import heart.alsvfd.Formulae;
import heart.alsvfd.Value;
import heart.alsvfd.expressions.AttributeExpressionInterface;
import heart.alsvfd.expressions.ExpressionInterface;
import heart.exceptions.AttributeNotRegisteredException;
import heart.exceptions.NotInTheDomainException;
import heart.exceptions.UnknownValueException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;

public class ALSVEvaluator implements UncertainTrueEvaluator{
	
	public static final float MIN_CERTAINTY = 0;
	public static final float MAX_CERTAINTY = 1;
	private static final float SATISFIABILITY_THRESHOLD = 0.0f;

	@Override
	public UncertainTrue evaluateUncertainEq(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		Value attributeValue = lhs.evaluate(wm);
		boolean logicalValue = attributeValue.eq(rhs.evaluate(wm), lhs.evaluateType(wm));
		
		return (logicalValue ? new UncertainTrue(getMaxCertainty()): new UncertainTrue(getMinCertainty()));
	}

	@Override
	public UncertainTrue evaluateUncertainNeq(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		Value attributeValue = lhs.evaluate(wm);
		boolean logicalValue = attributeValue.neq(rhs.evaluate(wm), lhs.evaluateType(wm));
		
		return (logicalValue ? new UncertainTrue(getMaxCertainty()): new UncertainTrue(getMinCertainty()));
	}

	@Override
	public UncertainTrue evaluateUncertainIn(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		Value attributeValue = lhs.evaluate(wm);
		boolean logicalValue = attributeValue.in(rhs.evaluate(wm), lhs.evaluateType(wm));
		
		return (logicalValue ? new UncertainTrue(getMaxCertainty()): new UncertainTrue(getMinCertainty()));
	}

	@Override
	public UncertainTrue evaluateUncertainNotin(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		Value attributeValue = lhs.evaluate(wm);
		boolean logicalValue = attributeValue.notin(rhs.evaluate(wm), lhs.evaluateType(wm));
		
		return (logicalValue ? new UncertainTrue(getMaxCertainty()): new UncertainTrue(getMinCertainty()));
	}

	@Override
	public UncertainTrue evaluateUncertainSubset(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		Value attributeValue = lhs.evaluate(wm);
		boolean logicalValue = attributeValue.subset(rhs.evaluate(wm), lhs.evaluateType(wm));
		
		return (logicalValue ? new UncertainTrue(getMaxCertainty()): new UncertainTrue(getMinCertainty()));
	}

	@Override
	public UncertainTrue evaluateUncertainSupset(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		Value attributeValue = lhs.evaluate(wm);
		boolean logicalValue = attributeValue.supset(rhs.evaluate(wm), lhs.evaluateType(wm));
		
		return (logicalValue ? new UncertainTrue(getMaxCertainty()): new UncertainTrue(getMinCertainty()));
	}

	@Override
	public UncertainTrue evaluateUncertainSim(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		Value attributeValue = lhs.evaluate(wm);
		boolean logicalValue = attributeValue.sim(rhs.evaluate(wm), lhs.evaluateType(wm));
		
		return (logicalValue ? new UncertainTrue(getMaxCertainty()): new UncertainTrue(getMinCertainty()));
	}

	@Override
	public UncertainTrue evaluateUncertainNotsim(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		Value attributeValue = lhs.evaluate(wm);
		boolean logicalValue = attributeValue.notsim(rhs.evaluate(wm), lhs.evaluateType(wm));
		
		return (logicalValue ? new UncertainTrue(getMaxCertainty()): new UncertainTrue(getMinCertainty()));
	}

	@Override
	public UncertainTrue evaluateUncertainLt(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		Value attributeValue = lhs.evaluate(wm);
		boolean logicalValue = attributeValue.lt(rhs.evaluate(wm), lhs.evaluateType(wm));
		
		return (logicalValue ? new UncertainTrue(getMaxCertainty()): new UncertainTrue(getMinCertainty()));
	}

	@Override
	public UncertainTrue evaluateUncertainLte(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		Value attributeValue = lhs.evaluate(wm);
		boolean logicalValue = attributeValue.lte(rhs.evaluate(wm), lhs.evaluateType(wm));
		
		return (logicalValue ? new UncertainTrue(getMaxCertainty()): new UncertainTrue(getMinCertainty()));
	}

	@Override
	public UncertainTrue evaluateUncertainGt(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		Value attributeValue = lhs.evaluate(wm);
		boolean logicalValue = attributeValue.gt(rhs.evaluate(wm), lhs.evaluateType(wm));
		
		return (logicalValue ? new UncertainTrue(getMaxCertainty()): new UncertainTrue(getMinCertainty()));
	}

	@Override
	public UncertainTrue evaluateUncertainGte(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
		Value attributeValue = lhs.evaluate(wm);
		boolean logicalValue = attributeValue.gte(rhs.evaluate(wm), lhs.evaluateType(wm));
		
		return (logicalValue ? new UncertainTrue(getMaxCertainty()): new UncertainTrue(getMinCertainty()));
	}

	@Override
	public UncertainTrue evaluateTimeRange(AttributeExpressionInterface lhs, ExpressionInterface rhs, Formulae.TimeBasedParameter tbp, Method evaluatorMethod, WorkingMemory wm) throws NotInTheDomainException {
		float percentage=0;
		int count = 0;
		LinkedList<State> allStates = wm.getHistoryLogCopy();
		allStates.addLast(wm.getCurrentState());
		LinkedList<Value> oldValues = wm.findHistoricalValues(allStates, tbp.getRtp(), lhs.getAttributeName());

		try {
			for(Value o: oldValues) {
				WorkingMemory fakeWm = new WorkingMemory();
				fakeWm.setAttributeValue(lhs.getAttribute(),o,true);
				UncertainTrue ut = (UncertainTrue)evaluatorMethod.invoke(this,lhs, rhs, fakeWm);
				if(ut.getCertinatyFactor() > this.getSatisfiabilityThreshold()){
					count++;
				}

			}
			if(oldValues.size() > 0) {;
				percentage = ((float)count)/ oldValues.size();
			}

			switch(tbp.getQuantifier()){
				case MIN:
					if(percentage >= tbp.getPercentage()){
						return new UncertainTrue(1.0f);
					}
					break;
				case MAX:
					if(percentage <= tbp.getPercentage()){
						return new UncertainTrue(1.0f);
					}
					break;
				case EXACTLY:
					if(percentage == tbp.getPercentage()){
						return new UncertainTrue(1.0f);
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
		return new DefaultAmbiguityResolver();
	}

}
