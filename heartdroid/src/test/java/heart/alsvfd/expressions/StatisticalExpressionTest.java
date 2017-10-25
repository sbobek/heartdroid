package heart.alsvfd.expressions;

import heart.RelativeTimePeriod;
import heart.WorkingMemory;
import heart.alsvfd.Value;
import heart.xtt.Attribute;
import heart.xtt.Type;
import heart.xtt.XTTModel;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class StatisticalExpressionTest {

    //min/max
    // 1 2 3 4 5 6 7 6 5
    // 4 4 4 4 4 4 3.4 5
    // -1 2 -1 2 -1 -1 -1
    // 2 -1 2 -1 2 2 2
    // 6 5 4 3 2 1 0 -1 -2
    // 0 null, null, -1
    // null, null, null, -1
    // null, null, null, null
    @Test
    public void testMax() throws Exception {
        LinkedList<Value> values = createNumericValues(new Double[]  {1.,2.,3.,4.,5.,6.,6., 6., 5.});
        Double expectedMax = 6.;

    }

    @Test
    public void testMin() throws Exception {

    }

    //different size odd/even

    @Test
    public void testMed() throws Exception {

    }

    //division by zero check
    @Test
    public void testStdev() throws Exception {

    }

    //division by zero check
    @Test
    public void testTrend() throws Exception {

    }

    //division by zero check
    @Test
    public void testVar() throws Exception {

    }

    // empty set, nulls check
    // symbolic check
    // numeric check
    @Test
    public void testEntropy() throws Exception {

    }

    //1 2 3 4 5
    //1 1 2 3 4
    //1 1 2 2 3
    //nulls
    @Test
    public void testMode() throws Exception {

    }

    //empty set check
    //nulls
    @Test
    public void testMean() throws Exception {

    }

    @Test
    public void testEvaluate() throws Exception {

    }

    @Test
    public void testEvaluateType() throws Exception {


    }

    /************************ TOOLS *****************************/

    public static LinkedList<Value> createSymbolicValues(String [] values){
        return null;
    }

    public static LinkedList<Value> createNumericValues(Double [] values){
        return null;
    }

    public static WorkingMemory createWmObject(Attribute attributeName, LinkedList<Value> stateValues){
        return null;
    }

    public static RelativeTimePeriod coverValuesWithStampPeriod(LinkedList<Value> stateValues, int startIndex, int endIndex){
        return null;
    }

}