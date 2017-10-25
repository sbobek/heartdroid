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

package heart.parser.hmr;

import heart.Configuration;
import heart.HeaRT;
import heart.State;
import heart.StateElement;
import heart.alsvfd.*;
import heart.alsvfd.expressions.ExpressionInterface;
import heart.exceptions.*;
import heart.parser.hmr.runtime.SourceFile;
import heart.xtt.Decision;
import heart.xtt.Rule;
import heart.xtt.Table;
import heart.xtt.XTTModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StaticExpressions {

    private final String MODEL_PATH = "src/test/resources/static-expression-test.pl";
    private final String SS_NAME = "switch_simple";
    private final String SG_NAME = "switch_general";
    private final String R_NAME = "result";

    private final double ss_value = 1;
    private static final List<Value> sg_value;
    static {
        LinkedList<Value> sg_temp = new LinkedList<Value>();
        sg_temp.add(new SimpleNumeric(1.0));
//        sg_temp.add(new SimpleNumeric(2.0));
        sg_value = Collections.unmodifiableList(sg_temp);
    }
    private XTTModel model;
    private StateElement ss;
    private StateElement sg;

    public StaticExpressions() {
    }
    
    @Before
    public void setUp() {
        SourceFile expression_test = new SourceFile(MODEL_PATH);
	HMRParser parser = new HMRParser();
        try {
            parser.parse(expression_test);
            model = parser.getModel();
            ss = new StateElement();
            ss.setAttributeName(SS_NAME);
            sg = new StateElement();
            sg.setAttributeName(SG_NAME);
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
    public void testSIMPLE() {
        ss.setValue(new SimpleNumeric(ss_value));
        State state = new State();
        state.addStateElement(ss);

        try {
            HeaRT.fixedOrderInference(model, new String[]{"Test_simple"},
                    new Configuration.Builder()
                            .setInitialState(state)
                            .build());

            State current = HeaRT.getWm().getCurrentState(model);
            SimpleSymbolic result = (SimpleSymbolic)current.getValueOfAttribute(R_NAME);
            assertEquals(String.format("Simple attribute seems to got lost somewhere... "), "simple", result.getValue());
        } catch (NotInTheDomainException e) {
            e.printStackTrace();
        } catch (AttributeNotRegisteredException e) {
            e.printStackTrace();
        } catch (BuilderException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGENERAL(){
        sg.setValue(new SetValue(sg_value));
        State state = new State();
        state.addStateElement(sg);

        try {
            HeaRT.fixedOrderInference(model, new String[]{"Test_general"},
                    new Configuration.Builder()
                            .setInitialState(state)
                            .build());

            State current = HeaRT.getWm().getCurrentState(model);
            SimpleSymbolic result = (SimpleSymbolic)current.getValueOfAttribute(R_NAME);
            assertEquals(String.format("General attribute seems to got lost somewhere... "), "general", result.getValue());
        } catch (NotInTheDomainException e) {
            e.printStackTrace();
        } catch (AttributeNotRegisteredException e) {
            e.printStackTrace();
        } catch (BuilderException e) {
            e.printStackTrace();
        }
    }
}
