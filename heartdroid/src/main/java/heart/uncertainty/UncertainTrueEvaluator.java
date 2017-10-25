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
import heart.alsvfd.Value;
import heart.alsvfd.expressions.AttributeExpressionInterface;
import heart.alsvfd.expressions.ExpressionInterface;
import heart.exceptions.AttributeNotRegisteredException;
import heart.exceptions.NotInTheDomainException;
import heart.exceptions.UnknownValueException;
import heart.xtt.Attribute;

import java.lang.reflect.Method;

public interface UncertainTrueEvaluator {
	public UncertainTrue evaluateUncertainEq(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException;
	public UncertainTrue evaluateUncertainNeq(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException;
	public UncertainTrue evaluateUncertainIn(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException;
	public UncertainTrue evaluateUncertainNotin(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException;
	public UncertainTrue evaluateUncertainSubset(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException;
	public UncertainTrue evaluateUncertainSupset(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException;
	public UncertainTrue evaluateUncertainSim(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException;
	public UncertainTrue evaluateUncertainNotsim(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException;
	public UncertainTrue evaluateUncertainLt(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException;
	public UncertainTrue evaluateUncertainLte(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException;
	public UncertainTrue evaluateUncertainGt(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException;
	public UncertainTrue evaluateUncertainGte(ExpressionInterface lhs, ExpressionInterface rhs, WorkingMemory wm)
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException;

	UncertainTrue evaluateTimeRange(AttributeExpressionInterface lhs, ExpressionInterface rhs, Formulae.TimeBasedParameter tbp, Method evaluatorMethod, WorkingMemory wm)
			throws NotInTheDomainException;


	public float getMinCertainty();
	public float getMaxCertainty();
	public float getSatisfiabilityThreshold();
	public AmbiguityResolver getAmbiguityResolver();

}
