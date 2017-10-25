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

package heart.alsvfd.expressions;

import heart.RelativeTimestamp;
import heart.WorkingMemory;
import heart.alsvfd.Value;
import heart.exceptions.BuilderException;
import heart.exceptions.NotInTheDomainException;
import heart.exceptions.StaticEvaluationException;
import heart.xtt.Attribute;
import heart.xtt.Type;

import java.util.Map;

/**
 * Created by sbk on 19.12.14.
 */
public class Valat implements AttributeExpressionInterface {
    protected Attribute attribute;
    private RelativeTimestamp at;

    @Override
    public Value evaluate(WorkingMemory wm) throws UnsupportedOperationException, NotInTheDomainException {
        return wm.findHistoricalValue(wm.getHistoryLogCopy(),this.getAttributeName(),at);
    }

    /**
     * @param wm The {@link heart.WorkingMemory} object with respect to which a type of an expression should be evaluated
     * @return type of the expression
     */
    public Type evaluateType(WorkingMemory wm) {
        return wm.getAttribute(this.getAttributeName()).getType();
    }

    @Override
    public String toString() {
        return String.format("valat(%s)[%s]", this.attribute.getAttributeName(), this.at.getRelativeTimeDifference());
    }

    @Override
    public String getAttributeName() {
        return this.attribute.getAttributeName();
    }

    @Override
    public Attribute getAttribute() {
        return this.attribute;
    }

    public static class Builder implements AttributeExpressionBuilderInterface {
        private String attributeName;
        private Attribute attribute;
        private RelativeTimestamp at;
        private String debugInfo;

        @Override
        public Value staticEvaluate(Map<String, Attribute> atts) throws StaticEvaluationException, NotInTheDomainException {
            throw new StaticEvaluationException(String.format("Can't statically evaluate valat operator.\n%s",
                    debugInfo));
        }

        @Override
        public AttributeExpressionInterface build(Map<String, Attribute> atts) throws BuilderException {
            if (this.attributeName == null) {
                throw new BuilderException(String.format("Valat operator could not be built without attribute " +
                        "argument.\n%s", this.debugInfo));
            }
            if (! atts.containsKey(this.attributeName)) {
                throw new BuilderException(String.format("Valat operator could not be built with non-attribute " +
                        "argument.\n%s", this.debugInfo));
            }
            if (this.at == null) {
                throw new BuilderException(String.format("Valat operator could not be built without time index " +
                        "argument.\n%s", this.debugInfo));
            }
            Valat expression = new Valat();
            expression.attribute = this.attribute;
            expression.at = this.at;
            return expression;
        }

        public Builder setAttributeName(String attributeName) {
            this.attributeName = attributeName;
            return this;
        }

        public Builder setDebugInfo(String debugInfo) {
            this.debugInfo = debugInfo;
            return this;
        }

        public Builder setAt(RelativeTimestamp at) {
            this.at = at;
            return this;
        }

        @Override
        public String getAttributeName() {
            return this.attributeName;
        }

    }
}
