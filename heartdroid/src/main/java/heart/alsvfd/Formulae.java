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

import heart.RelativeTimePeriod;
import heart.State;
import heart.WorkingMemory;
import heart.alsvfd.expressions.*;
import heart.exceptions.*;
import heart.uncertainty.UncertainTrue;
import heart.uncertainty.UncertainTrueEvaluator;
import heart.xtt.Attribute;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Formulae {

    public static enum ConditionalOperator {
        EQ("eq"), NEQ("neq"), IN("in"),
        NOTIN("notin"), SUBSET("subset"),
        SUPSET("supset"), SIM("sim"),
        NOTSIM("notsim"), LT("lt"),
        GT("gt"), LTE("lte"), GTE("gte");

        private final String text;

        ConditionalOperator(String text) {
            this.text = text;
        }

        public String getText() {
            return this.text;
        }

        public static ConditionalOperator fromString(String text) {
            if (text != null) {
                for (ConditionalOperator b : ConditionalOperator.values()) {
                    if (text.equalsIgnoreCase(b.text)) {
                        return b;
                    }
                }
            }
            return null;
        }

    }

    public static class TimeBasedParameter {
        public static enum Quantifier {
            MAX("max"),
            MIN("min"),
            EXACTLY("exactly");
            private final String text;

            Quantifier(String text) {
                this.text = text;
            }

            public String getText() {
                return this.text;
            }

            public static Quantifier fromString(String text) {
                if (text != null) {
                    for (Quantifier b : Quantifier.values()) {
                        if (text.equalsIgnoreCase(b.text)) {
                            return b;
                        }
                    }
                }
                return null;
            }
        }

        private RelativeTimePeriod rtp;
        private float percentage;
        private Quantifier quantifier;

        public TimeBasedParameter(RelativeTimePeriod rtp, float percentage, Quantifier quantifier) {
            this.rtp = rtp;
            this.percentage = percentage;
            this.quantifier = quantifier;
        }

        public float getPercentage() {
            return percentage;
        }

        public Quantifier getQuantifier() {
            return quantifier;
        }

        public RelativeTimePeriod getRtp() {
            return rtp;
        }

        @Override
        public String toString() {
            return "{"+quantifier.getText()+" "+percentage*100+"% in "+rtp+"}";
        }
    }
    

	
	protected AttributeExpressionInterface lhs;
	protected ConditionalOperator op;
    protected TimeBasedParameter tbp;
	protected ExpressionInterface rhs;
	
	public UncertainTrue evaluate(WorkingMemory wm, UncertainTrueEvaluator ute)
            throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
            try {
                switch (op) {
                    case EQ:
                        if (tbp != null) {
                            return ute.evaluateTimeRange(lhs,rhs,tbp,ute.getClass().getMethod("evaluateUncertainEq",ExpressionInterface.class,ExpressionInterface.class,WorkingMemory.class),wm);
                        } else {
                            return ute.evaluateUncertainEq(lhs, rhs, wm);
                        }
                    case NEQ:
                        if (tbp != null) {
                            return ute.evaluateTimeRange(lhs,rhs,tbp,ute.getClass().getMethod("evaluateUncertainNeq",ExpressionInterface.class,ExpressionInterface.class,WorkingMemory.class),wm);
                        } else {
                            return ute.evaluateUncertainNeq(lhs, rhs, wm);
                        }
                    case IN:
                        if (tbp != null) {
                            return ute.evaluateTimeRange(lhs,rhs,tbp,ute.getClass().getMethod("evaluateUncertainIn",ExpressionInterface.class,ExpressionInterface.class,WorkingMemory.class),wm);
                        } else {
                            return ute.evaluateUncertainIn(lhs, rhs, wm);
                        }
                    case NOTIN:
                        if (tbp != null) {
                            return ute.evaluateTimeRange(lhs,rhs,tbp,ute.getClass().getMethod("evaluateUncertainNotin",ExpressionInterface.class,ExpressionInterface.class,WorkingMemory.class),wm);
                        } else {
                            return ute.evaluateUncertainNotin(lhs, rhs, wm);
                        }
                    case SUBSET:
                        if (tbp != null) {
                            return ute.evaluateTimeRange(lhs,rhs,tbp,ute.getClass().getMethod("evaluateUncertainSubset",ExpressionInterface.class,ExpressionInterface.class,WorkingMemory.class),wm);
                        } else {
                            return ute.evaluateUncertainSubset(lhs, rhs, wm);
                        }
                    case SUPSET:
                        if (tbp != null) {
                            return ute.evaluateTimeRange(lhs,rhs,tbp,ute.getClass().getMethod("evaluateUncertainSupset",ExpressionInterface.class,ExpressionInterface.class,WorkingMemory.class),wm);
                        } else {
                            return ute.evaluateUncertainSupset(lhs, rhs, wm);
                        }
                    case SIM:
                        if (tbp != null) {
                            return ute.evaluateTimeRange(lhs,rhs,tbp,ute.getClass().getMethod("evaluateUncertainSim",ExpressionInterface.class,ExpressionInterface.class,WorkingMemory.class),wm);
                        } else {
                            return ute.evaluateUncertainSim(lhs, rhs, wm);
                        }
                    case NOTSIM:
                        if (tbp != null) {
                            return ute.evaluateTimeRange(lhs,rhs,tbp,ute.getClass().getMethod("evaluateUncertainNotsim",ExpressionInterface.class,ExpressionInterface.class,WorkingMemory.class),wm);
                        } else {
                            return ute.evaluateUncertainNotsim(lhs, rhs, wm);
                        }
                    case LT:
                        if (tbp != null) {
                            return ute.evaluateTimeRange(lhs,rhs,tbp,ute.getClass().getMethod("evaluateUncertainLt",ExpressionInterface.class,ExpressionInterface.class,WorkingMemory.class),wm);
                        } else {
                            return ute.evaluateUncertainLt(lhs, rhs, wm);
                        }
                    case LTE:
                        if (tbp != null) {
                            return ute.evaluateTimeRange(lhs,rhs,tbp,ute.getClass().getMethod("evaluateUncertainLte",ExpressionInterface.class,ExpressionInterface.class,WorkingMemory.class),wm);
                        } else {
                            return ute.evaluateUncertainLte(lhs, rhs, wm);
                        }
                    case GT:
                        if (tbp != null) {
                            return ute.evaluateTimeRange(lhs,rhs,tbp,ute.getClass().getMethod("evaluateUncertainGt",ExpressionInterface.class,ExpressionInterface.class,WorkingMemory.class),wm);
                        } else {
                            return ute.evaluateUncertainGt(lhs, rhs, wm);
                        }
                    case GTE:
                        if (tbp != null) {
                            return ute.evaluateTimeRange(lhs,rhs,tbp,ute.getClass().getMethod("evaluateUncertainGte",ExpressionInterface.class,ExpressionInterface.class,WorkingMemory.class),wm);
                        } else {
                            return ute.evaluateUncertainGte(lhs, rhs, wm);
                        }
                    default:
                        throw new UnsupportedOperationException("Operator " + op + " not defined in ALSV(FD).");
                }
            }catch(NoSuchMethodException e){
                // This will never happen, so just remain silent
                //return only to make the compiler happy
                return new UncertainTrue(0.0f);
            }
	}
	
	/**
	 * 
	 * @return the operator of the formula
	 */
	public ConditionalOperator getOp() {
		return op;
	}

	/**
	 * Sets the operator of the formula
	 * 
	 * @param op The operator to be set
	 */
	protected void setOp(ConditionalOperator op) {
		this.op = op;
	}

	public ExpressionInterface getLHS() {
		return lhs;
	}

	protected void setLHS(AttributeExpressionInterface lhs) {
		this.lhs = lhs;
	}

	public ExpressionInterface getRHS() {
		return rhs;
	}

	protected void setRHS(ExpressionInterface rhs) {
		this.rhs = rhs;
	}

    public boolean hasTimeBasedParameter(){
        return (tbp != null);
    }

    public TimeBasedParameter getTimeBasedParameter(){
        return tbp;
    }

    public Attribute getAttribute() {
        return this.lhs.getAttribute();
    }

	@Override
	public String toString() {
		return lhs+" "+op+(tbp != null ? tbp : "")+" "+rhs;
	}
        
    public static class Builder {
        private AttributeExpressionBuilderInterface lhs;
        private ConditionalOperator op;
        private ExpressionBuilderInterface rhs;
        private TimeBasedParameter tbp;
        private String debugInfo;

        public Builder() {
        }

        public Formulae build(Map<String,Attribute> attributes) throws BuilderException, StaticEvaluationException, NotInTheDomainException, UnknownValueException {
            Formulae f = new Formulae();
            if (getLHS() == null || getOp() == null || getRHS() == null) {
                throw new BuilderException(String.format("Can't build Formula, there is no attribute, operator, or value.\n%s", this.getDebugInfo()));
            }
            f.setLHS(getLHS().build(attributes));
            f.setOp(getOp());
            f.setRHS(getRHS().staticEvaluate(attributes));
            f.tbp = this.getTBP();
            return f;
        }


        public String getAttributeName() {
            return this.getLHS() != null ? this.getLHS().getAttributeName() : null;
        }
        public Builder setLHS(AttributeExpressionBuilderInterface lhs) {
            this.lhs = lhs;
            return this;
        }
        public AttributeExpressionBuilderInterface getLHS() {
            return lhs;
        }

        public Builder setOp(ConditionalOperator op) {
            this.op = op;
            return this;
        }
        public ConditionalOperator getOp() {
            return op;
        }

        public Builder setRHS(ExpressionBuilderInterface rhs) {
            this.rhs = rhs;
            return this;
        }
        public ExpressionBuilderInterface getRHS() {
            return rhs;
        }

        public Builder setTBP(TimeBasedParameter tbp) {
            this.tbp = tbp;
            return this;
        }
        public TimeBasedParameter getTBP() {
            return tbp;
        }

        public Builder setDebugInfo(String info) {
            this.debugInfo = info;
            return this;
        }
        public String getDebugInfo() {
            return debugInfo;
        }
    }
	
}