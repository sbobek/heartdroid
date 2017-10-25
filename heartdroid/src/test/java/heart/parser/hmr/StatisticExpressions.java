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

public class StatisticExpressions {

    private final String MODEL_PATH = "src/test/resources/statistic-expression-test.pl";


    private XTTModel model;

    public StatisticExpressions() {
    }
    
    @Before
    public void setUp() {
        SourceFile expression_test = new SourceFile(MODEL_PATH);
	HMRParser parser = new HMRParser();
        try {
            parser.parse(expression_test);
            model = parser.getModel();
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
}
