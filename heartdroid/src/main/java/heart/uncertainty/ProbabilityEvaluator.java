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

import heart.WorkingMemory;
import heart.alsvfd.Formulae;
import heart.alsvfd.Null;
import heart.alsvfd.Value;
import heart.alsvfd.expressions.AttributeExpressionInterface;
import heart.alsvfd.expressions.ExpressionInterface;
import heart.exceptions.AttributeNotRegisteredException;
import heart.exceptions.NotInTheDomainException;
import heart.xtt.Attribute;
import heart.xtt.Rule;

import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.LinkedList;

public class ProbabilityEvaluator implements UncertainTrueEvaluator{
	public static final float MAX_CERTAINTY = 1;
	public static final float MIN_CERTAINTY = 0;
	private static final float SATISFIABILITY_THRESHOLD = 0.5f;

	/**
	 * The method calculated the certainty of the formula being true, by incorporating standard
	 * probability theory.
	 * 
	 * @param lhs The  LHS of the formula
	 * @param rhs The RHS of the formula
	 * @param wm a working memory object that contains information about attributes values
	 * @param op The operation
	 * @param logicalValue The logical value that was evaluated without taking into consideration the certainty of the values
	 * 
	 * @return The uncertainTrue value that represents the certainty of formula being true. 
	 * @throws NotInTheDomainException 
	 * @throws UnsupportedOperationException 
	 * @throws AttributeNotRegisteredException 
	 */
	private UncertainTrue evaluateUncertainTrueValue(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm,
			String op, boolean logicalValue)
			throws UnsupportedOperationException, NotInTheDomainException {
		
		/*
		 * This should treat all the set elements probabilities as a probability of presence 
		 * in the set. If the value is not present in the set it is equal to the situation
		 * when the value is present with probability 0.0.
		 * 
		 * To allow more robust reasoning it is worth introducing noise parameter 
		 * similar to noisy-or gateway, which will always add some probability
		 * to not present elements (as if they were very unlikely present in the set, 
		 * yet not completely impossible).
		 */
		
		if(lhs.evaluate(wm) instanceof Null){
			// Here use the attribute sistribution field, 
			// TODO: where should be callbacks triggered?
			// TODO: where should be mediation triggered?
		}
		
		return null;
	}

	@Override
	public UncertainTrue evaluateUncertainEq(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UncertainTrue evaluateUncertainNeq(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UncertainTrue evaluateUncertainIn(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UncertainTrue evaluateUncertainNotin(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UncertainTrue evaluateUncertainSubset(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UncertainTrue evaluateUncertainSupset(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UncertainTrue evaluateUncertainSim(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UncertainTrue evaluateUncertainNotsim(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UncertainTrue evaluateUncertainLt(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UncertainTrue evaluateUncertainLte(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UncertainTrue evaluateUncertainGt(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UncertainTrue evaluateUncertainGte(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UncertainTrue evaluateTimeRange(AttributeExpressionInterface lhs, ExpressionInterface rhs, Formulae.TimeBasedParameter tbp, Method evaluatorMethod, WorkingMemory wm) throws NotInTheDomainException {
		return null;
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
		return new ProbabilityAmbiguityResolver();
	}

	public class ProbabilityAmbiguityResolver implements AmbiguityResolver{

		@Override
		public Value resolveCumulativeConclusions(LinkedList<Value> ambiguousValues) {
			return null;
		}

		@Override
		public LinkedList<AbstractMap.SimpleEntry<Rule, UncertainTrue>> resolveDisjunctiveConclusions(ConflictSet ambiguousRules, WorkingMemory wm, ConflictSetResolution csr) throws NotInTheDomainException {
			return null;
		}


	}


}
