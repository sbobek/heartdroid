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

package heart.parser.hmr;

import heart.Configuration;
import heart.HeaRT;
import heart.State;
import heart.StateElement;
import heart.alsvfd.SimpleNumeric;
import heart.alsvfd.SimpleSymbolic;
import heart.alsvfd.Value;
import heart.exceptions.AttributeNotRegisteredException;
import heart.exceptions.BuilderException;
import heart.exceptions.ModelBuildingException;
import heart.exceptions.NotInTheDomainException;
import heart.exceptions.ParsingSyntaxException;
import heart.parser.hmr.HMRParser;
import heart.parser.hmr.runtime.SourceFile;
import heart.xtt.XTTModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArithmeticExpressions {

    private final String MODEL_PATH = "src/test/resources/expression-test.pl";
    private final String X_NAME = "x";
    private final String Y_NAME = "y";
    private final String Z_NAME = "z";
    private final String S_NAME = "switch";
    private final String R_NAME = "result";
    
    private final double x_value = 3.0;
    private final double y_value = -5.0;
    private final double z_value = 7.0;
    private XTTModel model;
    private StateElement x, y, z;
    private StateElement s;
    
    public ArithmeticExpressions() {
    }
    
    @Before
    public void setUp() {

        SourceFile expression_test = new SourceFile(MODEL_PATH);
        HMRParser parser = new HMRParser();
        try {
            parser.parse(expression_test);
            model = parser.getModel();
            x = new StateElement();
            x.setAttributeName(X_NAME);
            x.setValue(new SimpleNumeric(x_value));
            y = new StateElement();
            y.setAttributeName(Y_NAME);
            y.setValue(new SimpleNumeric(y_value));
            z = new StateElement();
            z.setAttributeName(Z_NAME);
            z.setValue(new SimpleNumeric(z_value));
            s = new StateElement();
            s.setAttributeName(S_NAME);
        }
        catch (ModelBuildingException ex) {
            ex.printStackTrace();
        }
        catch (ParsingSyntaxException ex) {
            ex.printStackTrace();
        }
    }
    
    @After
    public void tearDown() {
        HeaRT.getWm().unregisterAll(model);
    }

    @Test
    public void testFILE() {
        SourceFile expression_test = new SourceFile(MODEL_PATH);
        assertNotNull(expression_test);
    }

    @Test
    public void testBUILD() {
        assertNotNull("XTT Model hasn't been built succesfully. Check logs.", model);
    }
    
    @Test
    public void testADD() {
        genericTest("add", x_value + y_value);
    }
    
    @Test
    public void testSUBTRACT() {
        genericTest("subtract", x_value - y_value);
    }
    
    @Test
    public void testMULTIPLY() {
        genericTest("multiply", x_value * y_value);
    }
    
    @Test
    public void testDIVIDE() {
        genericTest("divide", x_value / y_value);
    }
    
    @Test
    public void testBRACKETS() {
        genericTest("brackets", (x_value + y_value) * (x_value + z_value));
    }
    
    @Test
    public void testPRECEDENCE() {
        genericTest("precedence", x_value + y_value * y_value - z_value / z_value);
    }
    
    @Test
    public void testASSOCIAVITY() {
        genericTest("associavity", x_value - y_value - z_value + x_value + y_value + z_value);
    }

    @Test
    public void testUNDEFINED() {
        genericTest("undefined", x_value - y_value - z_value + x_value + Math.pow(x_value,Math.pow(y_value,z_value)) + z_value);
    }

    @Test
    public void testWACKO() {
        genericTest("wacko", (x_value + (x_value + y_value * z_value)) * ((x_value - z_value) + y_value));
    }
    
    public void genericTest(String switchValue, Double expectedValue) {
        try {
            s.setValue(new SimpleSymbolic(switchValue));
            State state = new State();
            state.addStateElement(x);
            state.addStateElement(y);
            state.addStateElement(z);
            state.addStateElement(s);

            HeaRT.fixedOrderInference(model, new String[]{"Test"},
                    new Configuration.Builder()
                            .setInitialState(state)
                            .build());

            State current = HeaRT.getWm().getCurrentState(model);
            SimpleNumeric result = (SimpleNumeric)current.getValueOfAttribute(R_NAME);
            assertEquals(String.format("%s seems to got lost somewhere... ", switchValue), expectedValue, result.getValue());

        } catch (UnsupportedOperationException ex) {
            ex.printStackTrace();
        } catch (NotInTheDomainException ex) {
            ex.printStackTrace();
        } catch (AttributeNotRegisteredException ex) {
            ex.printStackTrace();
        } catch (BuilderException ex) {
            ex.printStackTrace();
        }
    }

}
