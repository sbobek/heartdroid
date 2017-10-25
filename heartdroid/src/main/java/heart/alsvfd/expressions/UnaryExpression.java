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

import heart.Debug;
import heart.WorkingMemory;
import heart.alsvfd.*;
import heart.exceptions.BuilderException;
import heart.exceptions.NotInTheDomainException;
import heart.exceptions.StaticEvaluationException;
import heart.exceptions.UnknownValueException;
import heart.xtt.Attribute;
import heart.xtt.Type;

import java.awt.*;
import java.util.Map;


public class UnaryExpression implements ExpressionInterface {

    public static enum UnaryOperator {
        ABS("abs"), COSINUS("cos"), SINUS("sin"), TANGENS("tan"),
        FACTORIAL("fac"), LOGARITHM("log"), POWERSET("powerset");

        private final String text;
      
        UnaryOperator(String text) {
            this.text = text;
        }

        public String getText() {
            return this.text;
        }

        public static UnaryOperator fromString(String text) {
          if (text != null) {
            for (UnaryOperator b : UnaryOperator.values()) {
              if (text.equalsIgnoreCase(b.text)) {
                return b;
              }
            }
          }
          return null;
        }
    }
    
    private final ExpressionInterface arg;
    private final UnaryOperator op; 
            
    public UnaryExpression(ExpressionInterface arg, UnaryOperator op) {
        this.arg = arg;
        this.op = op;
    }

    private Value staticEvaluate(Value argument) throws UnsupportedOperationException, NotInTheDomainException, UnknownValueException {
        Value result = null;

        switch (op) {
            case ABS:
                if(argument instanceof SimpleNumeric){
                    result = new SimpleNumeric(Math.abs(((SimpleNumeric) argument).getValue()),argument.getCertaintyFactor());
                }else{
                    throw new UnsupportedOperationException("Operation "+ op.getText() +" is not supported for non numeric values.");
                }
                break;
            case COSINUS:
                if(argument instanceof SimpleNumeric){
                    result = new SimpleNumeric(Math.cos(((SimpleNumeric) argument).getValue()),argument.getCertaintyFactor());
                }else{
                    throw new UnsupportedOperationException("Operation "+ op.getText() +" is not supported for non numeric values.");
                }
                break;
            case SINUS:
                if(argument instanceof SimpleNumeric){
                    result = new SimpleNumeric(Math.sin(((SimpleNumeric) argument).getValue()),argument.getCertaintyFactor());
                }else{
                    throw new UnsupportedOperationException("Operation "+ op.getText() +" is not supported for non numeric values.");
                }
                break;
            case TANGENS:
                if(argument instanceof SimpleNumeric){
                    result = new SimpleNumeric(Math.tan(((SimpleNumeric) argument).getValue()),argument.getCertaintyFactor());
                }else{
                    throw new UnsupportedOperationException("Operation "+ op.getText() +" is not supported for non numeric values.");
                }
                break;
            case FACTORIAL:
                result = argument.factorial();
                break;
            case LOGARITHM:
                if(argument instanceof SimpleNumeric){
                    result = new SimpleNumeric(Math.log(((SimpleNumeric) argument).getValue()),argument.getCertaintyFactor());
                }else{
                    throw new UnsupportedOperationException("Operation "+ op.getText() +" is not supported for non numeric values.");
                }
                break;
            case POWERSET:
                result = argument.powerset();

        }
        return result;
    }

    @Override
    public Value evaluate(WorkingMemory wm) throws UnsupportedOperationException, NotInTheDomainException {
        Value argument = arg.evaluate(wm);
        try {
            return staticEvaluate(argument);
        }catch (UnknownValueException e) {
            Debug.debug(Debug.heartTag,
                    Debug.Level.WARNING,
                    "Evaluating expression "+this.toString()+" failed due to Null values. Returning Null as a result.");
            return new Null();
        }

    }

    /**
     * It returns the type of the operand.
     * It return null, as there is not always a warranty that the result will be of the same
     * type as operands.
     *
     * @param wm The {@link heart.WorkingMemory} object with respect to which a type of an expression should be evaluated
     * @return type of the expression
     */
    public Type evaluateType(WorkingMemory wm) {
        return null;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", this.op.getText(), this.arg);
    }


    public static class Builder implements ExpressionBuilderInterface {
        private ExpressionBuilderInterface argBuilder;
        private UnaryOperator op;
        private String debugInfo;

        @Override
        public Value staticEvaluate(Map<String, Attribute> atts) throws StaticEvaluationException, NotInTheDomainException {
            if (argBuilder == null || op == null) {
                throw new StaticEvaluationException(String.format("Error while evaluating UnaryExpression. The argument or operator has not been set.\n%s", debugInfo));
            }
            Value arg = argBuilder.staticEvaluate(atts);
            try {
                return new UnaryExpression(arg, op).staticEvaluate(arg);
            } catch (UnknownValueException e) {
                throw new StaticEvaluationException(e.getMessage());
            }
        }

        @Override
        public ExpressionInterface build(Map<String, Attribute> atts) throws BuilderException {
            if (argBuilder == null || op == null){
		throw new BuilderException(String.format("Error while building UnaryExpression. The argument or operator has not been set.\n%s", debugInfo));
            }
            ExpressionInterface arg = argBuilder.build(atts);
            return new UnaryExpression(arg, op);
        }
        
        public Builder setArgumentBuilder(ExpressionBuilderInterface argBuilder) {
            this.argBuilder = argBuilder;
            return this;
        }

        public Builder setOperator(UnaryOperator op) {
            this.op = op;
            return this;
        }

        public ExpressionBuilderInterface setDebugInfo(String debugInfo) {
            this.debugInfo = debugInfo;
            return this;
        }
        
        @Override
        public String toString() {
            return String.format("%s(%s)", this.op.getText(), this.argBuilder);
        }
    }
}
