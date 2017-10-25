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

import heart.alsvfd.Any;
import heart.alsvfd.Null;
import heart.alsvfd.SimpleSymbolic;
import heart.alsvfd.Value;
import heart.exceptions.BuilderException;
import heart.exceptions.StaticEvaluationException;
import heart.xtt.Attribute;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AttributeExpressionBuilder implements AttributeExpressionBuilderInterface {

    private static final Map<String, Value> reservedStringValues;
    static {
        HashMap<String, Value> _reservedStringValues;
        _reservedStringValues = new HashMap<String, Value>();
        _reservedStringValues.put("any", new Any());
        _reservedStringValues.put("null", new Null());
        reservedStringValues = Collections.unmodifiableMap(_reservedStringValues);
    }
    private String name;
    private String debugInfo;
    private Attribute attribute;

    @Override
    public Value staticEvaluate(Map<String, Attribute> atts) throws StaticEvaluationException {
        if (name == null) throw new StaticEvaluationException(String.format("Can't evaluate Attribute without " +
                "name.\n%s", debugInfo));

        throw new StaticEvaluationException(String.format("Can't statically evaluate Attribute value.\n%s", debugInfo));
    }

    @Override
    public Attribute build(Map<String, Attribute> atts) throws BuilderException {
        if (name == null) throw new BuilderException(String.format("Can't build Attribute without name " +
                "attribute.\n%s", debugInfo));

        if (atts.containsKey(name)) {
            return atts.get(name);
        }

        throw new BuilderException(String.format("There is no attribute with the given name \n%s", debugInfo));
    }


    public AttributeExpressionBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public String getAttributeName() {
        return this.name;
    }

    public AttributeExpressionBuilder setAttributeName(String attributeName) {
        this.name = attributeName;
        return this;
    }


    public AttributeExpressionBuilder setDebugInfo(String debugInfo) {
        this.debugInfo = debugInfo;
        return this;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
