/*
 * *
 *  *
 *  *     Copyright 2013-15 by Szymon Bobek, Grzegorz J. Nalepa, Mateusz Ślażyński
 *  *
 *  *
 *  *     This file is part of HeaRTDroid.
 *  *     HeaRTDroid is a rule engine that is based on HeaRT inference engine,
 *  *     XTT2 representation and other concepts developed within the HeKatE project .
 *  *
 *  *     HeaRTDroid is free software: you can redistribute it and/or modify
 *  *     it under the terms of the GNU General Public License as published by
 *  *     the Free Software Foundation, either version 3 of the License, or
 *  *     (at your option) any later version.
 *  *
 *  *     HeaRTDroid is distributed in the hope that it will be useful,
 *  *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  *     GNU General Public License for more details.
 *  *
 *  *     You should have received a copy of the GNU General Public License
 *  *     along with HeaRTDroid.  If not, see <http://www.gnu.org/licenses/>.
 *  *
 *  *
 */

package heart.alsvfd.expressions;

import heart.WorkingMemory;
import heart.alsvfd.SetValue;
import heart.alsvfd.Value;
import heart.exceptions.BuilderException;
import heart.exceptions.NotInTheDomainException;
import heart.exceptions.StaticEvaluationException;
import heart.xtt.Attribute;
import heart.xtt.Type;

import java.util.Map;

/**
 * Created by msl on 06/11/14.
 */
public class DomainExpression implements ExpressionInterface {
    private SetValue value;
    private String attributeName;

    DomainExpression(SetValue value, String attributeName) {
        this.value = value;
        this.attributeName = attributeName;
    }

    @Override
    public Value evaluate(WorkingMemory wm) throws UnsupportedOperationException, NotInTheDomainException {
        return value;
    }

    @Override
    /**
     * Tt returns a Type associated with the attribute which the domain concerns.
     *
     * @param wm The {@link heart.WorkingMemory} object with respect to which a type of an expression should be evaluated
     * @return type of the expression
     */
    public Type evaluateType(WorkingMemory wm) {
        return wm.getAttribute(attributeName).getType();
    }

    @Override
    public String toString() {
        return String.format("dom(%s)[%s]", this.attributeName, this.value);
    }

    public static class Builder implements ExpressionBuilderInterface {
        private String attributeName;
        private String debugInfo;

        @Override
        public Value staticEvaluate(Map<String, Attribute> atts) throws StaticEvaluationException, NotInTheDomainException {
            if (this.attributeName == null) {
                throw new StaticEvaluationException(String.format("Domain operator could not be evaluated without attribute argument.\n%s", this.debugInfo));
            }
            if (! atts.containsKey(this.attributeName)) {
                throw new StaticEvaluationException(String.format("Domain operator could not be evaluated with non-attribute argument.\n%s", this.debugInfo));
            }
            Attribute attr = atts.get(this.attributeName);
            return attr.getType().getDomain();
        }

        @Override
        public ExpressionInterface build(Map<String, Attribute> atts) throws BuilderException {
            if (this.attributeName == null) {
                throw new BuilderException(String.format("Domain operator could not be built without attribute argument.\n%s", this.debugInfo));
            }
            if (! atts.containsKey(this.attributeName)) {
                throw new BuilderException(String.format("Domain operator could not be built with non-attribute argument.\n%s", this.debugInfo));
            }
            Attribute attr = atts.get(this.attributeName);
            return new DomainExpression(attr.getType().getDomain(), this.attributeName);
        }

        public Builder setAttributeName(String attributeName) {
            this.attributeName = attributeName;
            return this;
        }
        public Builder setDebugInfo(String debugInfo) {
            this.debugInfo = debugInfo;
            return this;
        }
    }
}

