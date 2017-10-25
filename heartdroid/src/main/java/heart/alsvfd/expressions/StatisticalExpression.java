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


import heart.*;
import heart.alsvfd.Null;
import heart.alsvfd.SimpleNumeric;
import heart.alsvfd.SimpleSymbolic;
import heart.alsvfd.Value;
import heart.exceptions.BuilderException;
import heart.exceptions.NotInTheDomainException;
import heart.exceptions.StaticEvaluationException;
import heart.exceptions.UnknownValueException;
import heart.xtt.Attribute;
import heart.xtt.Type;


import java.util.*;

/**
 * Created by sbk on 19.12.14.
 * This class is responsible for implementation of all the statistical operations available in HeaRTDroid.
 * Methods from this class  will be later invoked by other components that implements particular operators.
 */
public class StatisticalExpression implements AttributeExpressionInterface {

    private RelativeTimePeriod period;
    private StatisticalOperator op;
    protected Attribute attribute;

    @Override
    public String getAttributeName() {
        return this.attribute.getAttributeName();
    }

    @Override
    public Attribute getAttribute() {
        return this.attribute;
    }

    public static enum StatisticalOperator {
        MAX("max"), MIN("min"), MEAN("mean"), VAR("var"),
        STDEV("stdev"), TREND("trend"), MODE("mode"), MED("med"),ENT("entropy");

        private final String text;

        StatisticalOperator(String text) {
            this.text = text;
        }

        public String getText() {
            return this.text;
        }

        public static StatisticalOperator fromString(String text) {
            if (text != null) {
                for (StatisticalOperator s : StatisticalOperator.values()) {
                    if (text.equalsIgnoreCase(s.text)) {
                        return s;
                    }
                }
            }
            return null;
        }
    }

    /**
     * Returns the biggest value of an attribute from specified period of time.
     * It is applicable only to the numeric attributes.
     *
     * @param attributeName the attribute name for which the operation has to be performed
     * @param period relative time period from which the values for calculation should be taken.
     * @param wm WorkingMemory object where the historical information about the attribute values are stored
     * @return maximum over all the values from the given time period
     *
     * @throws NotInTheDomainException
     */
    public Value max(String attributeName, RelativeTimePeriod period, WorkingMemory wm) throws NotInTheDomainException {
        LinkedList<State> allStates = wm.getHistoryLogCopy();
        allStates.addLast(wm.getCurrentState());
        LinkedList<Value> values = wm.findHistoricalValues(allStates,period, attributeName);

        Value result = values.getFirst();
        for(Value v : values){
            try {
                if (result.lt(v, null)) {
                    result = v;
                }
            }catch (UnknownValueException e){
                Debug.debug(Debug.heartTag, Debug.Level.VERBOS, "Skipping Null value while calculating maximum.");
            }
        }
        return result;

    }

    /**
     * Returns the smallest value of an attribute from specified period of time
     * It is applicable only to the numeric attributes.
     *
     * @param attributeName the attribute name for which the operation has to be performed
     * @param period relative time period from which the values for calculation should be taken.
     * @param wm WorkingMemory object where the historical information about the attribute values are stored
     * @return maximum over all the values from the given time period
     *
     * @throws NotInTheDomainException
     */
    public Value min(String attributeName, RelativeTimePeriod period, WorkingMemory wm) throws NotInTheDomainException {
        LinkedList<State> allStates = wm.getHistoryLogCopy();
        allStates.addLast(wm.getCurrentState());
        LinkedList<Value> values = wm.findHistoricalValues(allStates,period, attributeName);
        Value result = values.getFirst();
        for(Value v : values){
            try {
                if(result.gt(v, null)){
                    result = v;
                }
            } catch (UnknownValueException e) {
                Debug.debug(Debug.heartTag, Debug.Level.VERBOS, "Skipping Null value while calculating minimum.");
            }
        }
        return result;

    }

    /**
     * Returns median of the attribute values from specified  period of time.
     * It is applicable only to the numeric attributes.
     *
     * @param attributeName the attribute name for which the operation has to be performed
     * @param period relative time period from which the values for calculation should be taken.
     * @param wm WorkingMemory object where the historical information about the attribute values are stored
     * @return median over the values from the given time period
     *
     * @throws NotInTheDomainException
     */
    public Value med(String attributeName, RelativeTimePeriod period, WorkingMemory wm) throws NotInTheDomainException{
        LinkedList<State> allStates = wm.getHistoryLogCopy();
        allStates.addLast(wm.getCurrentState());
        LinkedList<Value> values = wm.findHistoricalValues(allStates,period, attributeName);
        LinkedList<Value> filtered = new LinkedList<Value>();
        for(Value v : values){
            if(v instanceof Null){
                Debug.debug(Debug.heartTag, Debug.Level.VERBOS, "Null value while calculating median. Skipping. ");
                continue;
            }else if(!(v instanceof SimpleNumeric)){
                throw new UnsupportedOperationException("Calculating median for non numeric values is not possible.");
            }else {
                filtered.add(v);
            }
        }

        if(filtered.size() == 0){
            return new Null();
        }else if(filtered.size() == 1){
            return filtered.getFirst();
        }else if(filtered.size() == 2){
            try {
                return (filtered.getFirst().add(filtered.getLast(),null)).div(new SimpleNumeric(2.0, 1.0f), null);
            } catch (UnknownValueException e) {
                //This will never happen as we filtered data
            }
        }

        Collections.sort(filtered, new Comparator<Value>() {
            @Override
            public int compare(Value value, Value t1) {
                try {
                    if (value.lt(t1, null)) {
                        return -1;
                    } else if (value.eq(t1, null)) {
                        return 0;
                    } else {
                        return 1;
                    }
                } catch (UnknownValueException e) {
                    //That is not gonna happen as we filter data for null values
                    return 0;
                } catch (NotInTheDomainException e) {
                    //That is not gonna happen as we do not provide type
                    return 0;
                }
            }
        });

        if(filtered.size() % 2 == 0){
            int index = filtered.size() / 2;
            Value v1 = filtered.get(index);
            Value v2 = filtered.get(index+1);

            try {
                return v1.add(v2,null).div(new SimpleNumeric(2.0),null);
            } catch (UnknownValueException e) {
                // This will never happen as we filtered data at the begining
            }
        }else{
            int index = filtered.size() / 2;
            return filtered.get(index);
        }
        return new Null();
    }

    /**
     * Returns standard deviation of the attribute values from specified period of time.
     *
     * @param attributeName the attribute name for which the operation has to be performed
     * @param period relative time period from which the values for calculation should be taken.
     * @param wm WorkingMemory object where the historical information about the attribute values are stored
     * @return the standard deviation over the values from the given time period
     *
     * @throws NotInTheDomainException
     */
    public Value stdev(String attributeName, RelativeTimePeriod period, WorkingMemory wm) throws NotInTheDomainException {

        SimpleNumeric variance = (SimpleNumeric)var(attributeName,period,wm);

        return new SimpleNumeric(Math.sqrt(variance.getValue()));
    }

    /**
     * Returns slope of the trend line fitted to attribute values using the least--squares fit.
     * It is applicable only to the numeric attributes.
     *
     * @param attributeName the attribute name for which the operation has to be performed
     * @param period relative time period from which the values for calculation should be taken.
     * @param wm WorkingMemory object where the historical information about the attribute values are stored
     * @return trend over the values from the given time period
     * @throws NotInTheDomainException
     */
    public Value trend(String attributeName, RelativeTimePeriod period, WorkingMemory wm){
        LinkedList<State> allStates = wm.getHistoryLogCopy();
        allStates.addLast(wm.getCurrentState());
        LinkedList<Value> values = wm.findHistoricalValues(allStates,period, attributeName);
        LinkedList<Value> filtered = new LinkedList<Value>();
        LinkedList<Double> timevector = new LinkedList<Double>();

        if(values.size() < 2){
            return new SimpleNumeric(0.0,0.0f);
        }

        double timeValueCovariance = 0;
        double timeVariance = 0;
        double timeMean = 0;
        double valueMean = 0;
        double nextTime = 0;

        // we can skip the real values of time as we are interested only in a in y=ax+b,
        // b is not important in trend calculation
        for(Value v : values){
            if(v instanceof Null){
                Debug.debug(Debug.heartTag, Debug.Level.VERBOS, "Null value while calculating trend. Skipping. ");
                nextTime++;
                continue;
            }else if(!(v instanceof SimpleNumeric)){
                throw new UnsupportedOperationException("Calculating trend for non numeric values is not possible.");
            }else {
                filtered.add(v);
                valueMean += ((SimpleNumeric) v).getValue();
                timeMean += nextTime++;
                timevector.add(nextTime);

            }
        }
        // calculate means
        valueMean /= filtered.size();
        timeMean /= timevector.size();

        //calculate variance time
        for(Double t : timevector){
            timeVariance += (t-timeMean)*(t-timeMean);
        }
        timeVariance /= timevector.size()-1;

        //calculate covariance time,value
        Iterator<Value> valueIterator = filtered.iterator();
        Iterator<Double> timeIterator = timevector.iterator();
        while(valueIterator.hasNext()){
            SimpleNumeric v = (SimpleNumeric)valueIterator.next();
            Double t = timeIterator.next();
            timeValueCovariance += (t-timeMean)*(v.getValue()-valueMean);

        }
        timeValueCovariance /= filtered.size()-1;

        //calculate trend  by fitting y  = ax + b, where  a = cov(x,y)/var(x)
        double rawTrend = timeValueCovariance/timeVariance;

        return new SimpleNumeric(rawTrend);
    }

    /**
     * Returns variance of the attribute  values from specified period of time.
     * It is applicable only to the numeric attributes.
     *
     * @param attributeName the attribute name for which the operation has to be performed
     * @param period relative time period from which the values for calculation should be taken.
     * @param wm WorkingMemory object where the historical information about the attribute values are stored
     * @return variance over the values from the given time period
     * @throws NotInTheDomainException
     */
    public Value var(String attributeName, RelativeTimePeriod period,WorkingMemory wm) throws NotInTheDomainException {
        LinkedList<State> allStates = wm.getHistoryLogCopy();
        allStates.addLast(wm.getCurrentState());
        LinkedList<Value> values = wm.findHistoricalValues(allStates,period, attributeName);
        LinkedList<Value> filtered = new LinkedList<Value>();
        for(Value v : values){
            if(v instanceof Null){
                Debug.debug(Debug.heartTag, Debug.Level.VERBOS, "Null value while calculating variance. Skipping. ");
                continue;
            }else if(!(v instanceof SimpleNumeric)){
                throw new UnsupportedOperationException("Calculating standard variance for non numeric values is not possible. " +
                        "Use \"entropy\" instead");
            }else {
                filtered.add(v);
            }
        }

        SimpleNumeric mean = (SimpleNumeric) mean(attributeName, period, wm);

        Double rawMean = mean.getValue();
        Double rawVar = new Double(0);
        for(Value v : filtered){
            Double rawElement = ((SimpleNumeric) v).getValue();
            rawVar += (rawElement-rawMean)*(rawElement-rawMean);
        }

        return new SimpleNumeric(rawVar/(filtered.size()-1));
    }

    /**
     * Returns entropy of the attribute  values from specified period of time.
     * It is applicable only to the simple attributes.
     *
     * @param attributeName the attribute name for which the operation has to be performed
     * @param period relative time period from which the values for calculation should be taken.
     * @param wm WorkingMemory object where the historical information about the attribute values are stored
     * @return entropy of the values from the given time period
     * @throws NotInTheDomainException
     */
    public Value entropy(String attributeName, RelativeTimePeriod period, WorkingMemory wm) throws NotInTheDomainException {
        LinkedList<State> allStates = wm.getHistoryLogCopy();
        allStates.addLast(wm.getCurrentState());
        LinkedList<Value> values = wm.findHistoricalValues(allStates,period, attributeName);
        LinkedList<Value> filtered = new LinkedList<Value>();
        for(Value v : values) {
            if (v instanceof Null) {
                Debug.debug(Debug.heartTag, Debug.Level.VERBOS, "Null value while calculating entropy. Skipping. ");
                continue;
            } else if (!(v instanceof SimpleSymbolic)) {
                throw new UnsupportedOperationException("Calculating standard variance for numeric values is not possible. " +
                        "Use \"var\" instead");
            } else {
                filtered.add(v);
            }
        }
        Attribute att = wm.getAttribute(attributeName);
        Value entropy = new Null();

        try {
            Hashtable<Value,Integer> statistics = new Hashtable<Value, Integer>();

            for(Value v : filtered) {
                Integer count = statistics.get(v);
                if (count == null) {
                    statistics.put(v, 1);
                } else {
                    statistics.put(v, count + 1);
                }
            }
            double numberOfClasses = att.getType().getDomain().cardinality(att.getType());
            double rawEntropy = 0;
            for(Value v : att.getType().getDomain()){
                double probabilityOfV = statistics.get(v); // TODO: ensure it works
                rawEntropy += probabilityOfV*Math.log(probabilityOfV);

            }
            entropy = new SimpleNumeric(rawEntropy);

        } catch (UnknownValueException e) {
            //this should never happen
        }

        return entropy;
    }

    /**
     * Returns the most frequent attribute value from specified period of time
     *
     * @param attributeName the attribute name for which the operation has to be performed
     * @param period relative time period from which the values for calculation should be taken.
     * @param wm WorkingMemory object where the historical information about the attribute values are stored
     * @return the mode over the values from the given time period
     * @throws NotInTheDomainException
     */
    public Value mode(String attributeName, RelativeTimePeriod period, WorkingMemory wm){
        LinkedList<State> allStates = wm.getHistoryLogCopy();
        allStates.addLast(wm.getCurrentState());
        LinkedList<Value> values = wm.findHistoricalValues(allStates,period, attributeName);
        Hashtable<Value,Integer> statistics = new Hashtable<Value, Integer>();

        Value maxCountValue = new Null();
        Integer maxCount = 0;

        for(Value v : values){
            if(v instanceof Null){
                Debug.debug(Debug.heartTag, Debug.Level.VERBOS, "Null value while calculating mode. Skipping. ");
                continue;
            }
            Integer count = statistics.get(v); //TODO: ensure it works
            if(count == null){
                statistics.put(v,1);
            }else{
                statistics.put(v,count+1);
            }

            if(count > maxCount){
                maxCountValue = v;
            }
        }

        return maxCountValue;
    }

    /**
     * Returns mean of the attribute values from specified period of time.
     * It is applicable only to the numeric attributes.
     *
     * @param attributeName the attribute name for which the operation has to be performed
     * @param period relative time period from which the values for calculation should be taken.
     * @param wm WorkingMemory object where the historical information about the attribute values are stored
     * @return the mean over the values from the given time period
     * @throws NotInTheDomainException
     */
    public Value mean(String attributeName, RelativeTimePeriod period, WorkingMemory wm) throws NotInTheDomainException {
        LinkedList<State> allStates = wm.getHistoryLogCopy();
        allStates.addLast(wm.getCurrentState());
        LinkedList<Value> values = wm.findHistoricalValues(allStates,period, attributeName);
        Value sum = new SimpleNumeric(0.0);
        Double no = 0.0;
        for(Value v : values){
            try {
                sum = sum.add(v,null);
                no = no + 1;
            }  catch (UnknownValueException e) {
                Debug.debug(Debug.heartTag, Debug.Level.VERBOS, "Null value while calculating mean. Skipping. ");
            }

        }
        try {
            return sum.div(new SimpleNumeric(no),null);
        } catch (UnknownValueException e) {
            //This will never happen
        }catch (ArithmeticException zeroDiv){
            Debug.debug(Debug.heartTag, Debug.Level.VERBOS, "No values to calculate mean. Returning Null.");
            return new Null();
        }
        return new Null();
    }

    @Override
    public Value evaluate(WorkingMemory wm) throws UnsupportedOperationException, NotInTheDomainException {
        String attributeName = this.getAttributeName();
        switch (op) {
            case MAX:
                return max(attributeName, period, wm);
            case MIN:
                return min(attributeName, period, wm);
            case MEAN:
                return mean(attributeName, period, wm);
            case MED:
                return med(attributeName, period, wm);
            case MODE:
                return mode(attributeName, period, wm);
            case STDEV:
                return stdev(attributeName, period, wm);
            case TREND:
                return trend(attributeName, period, wm);
            case VAR:
                return var(attributeName, period, wm);
            case ENT:
                return entropy(attributeName,period,wm);
            default:
                break;
        }
        return new Null();
    }

    /**
     * It always returns null, as it cannot be always determined what type is the returned value of.
     *
     * @param wm The {@link heart.WorkingMemory} object with respect to which a type of an expression should be evaluated
     * @return type of the expression
     */
    public Type evaluateType(WorkingMemory wm) {
        return null;
    }

    public static class Builder implements AttributeExpressionBuilderInterface {
        private String name;
        private RelativeTimePeriod period;
        private StatisticalOperator op;
        private String debugInfo;

        @Override
        public Value staticEvaluate(Map<String, Attribute> atts) throws StaticEvaluationException, NotInTheDomainException {
            throw new StaticEvaluationException(String.format("Can't statically evaluate statistical operator.\n%s",
                    debugInfo));
        }

        @Override
        public AttributeExpressionInterface build(Map<String, Attribute> atts) throws BuilderException {
            if (this.name == null) {
                throw new BuilderException(String.format("Statistical operator could not be built without attribute " +
                        "argument.\n%s", this.debugInfo));
            }
            if (! atts.containsKey(this.name)) {
                throw new BuilderException(String.format("Statistical operator could not be built with non-attribute " +
                        "argument.\n%s", this.debugInfo));
            }
            if (this.period == null) {
                throw new BuilderException(String.format("Statistical operator could not be built without time period" +
                        " " +
                        "argument.\n%s", this.debugInfo));
            }
            if (this.op == null) {
                throw new BuilderException(String.format("Statistical operator could not be built without the " +
                        "specified operator" +
                        " " +
                        "argument.\n%s", this.debugInfo));
            }
            StatisticalExpression expression = new StatisticalExpression();
            expression.attribute = atts.get(getAttributeName());
            expression.period = this.period;
            expression.op = this.op;
            return expression;
        }

        public Builder setAttributeName(String attributeName) {
            this.name = attributeName;
            return this;
        }

        public Builder setDebugInfo(String debugInfo) {
            this.debugInfo = debugInfo;
            return this;
        }

        public Builder setOperator(StatisticalOperator op) {
            this.op = op;
            return this;
        }

        public Builder setPeriod(RelativeTimePeriod period) {
            this.period = period;
            return this;
        }

        @Override
        public String getAttributeName() {
            return this.name;
        }

    }
}
