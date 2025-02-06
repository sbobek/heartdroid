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

import com.beust.jcommander.DynamicParameter;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import heart.Configuration;
import heart.HeaRT;
import heart.State;
import heart.StateElement;
import heart.alsvfd.SimpleNumeric;
import heart.alsvfd.SimpleSymbolic;
import heart.exceptions.*;
import heart.parser.hmr.HMRParser;
import heart.parser.hmr.runtime.SourceFile;
import heart.uncertainty.CertaintyFactorsEvaluator;
import heart.uncertainty.ConflictSetFireAll;
import heart.xtt.Attribute;
import heart.xtt.Table;
import heart.xtt.Type;
import heart.xtt.XTTModel;

import java.util.*;

/**
 * Created by msl on 14/11/14.
 */
public class HMRCommandLine {

    public static class JCommanderConfiguration {
        @Parameter(description = "<path to the HMR file>", required = true)
        private List<String> files = new LinkedList<String>();

        @Parameter(names = "-tabs", description = "Order of tables used in the inference", required = true,
                variableArity = true)
        private List<String> tabs = new LinkedList<String>();

        @DynamicParameter(names = "-A", description = "Initial values of attributes")
        private Map<String, String> attributes = new HashMap<String, String>();

        @Parameter(names = "--help", help = true, description = "Display help (this message)")
        private boolean help;
    }

    public static void main(String [] args){
        JCommanderConfiguration jcc = new JCommanderConfiguration();
        JCommander jcomm = new JCommander(jcc, args);

        if (jcc.help) {
            jcomm.usage();
            return;
        }

        SourceFile hmr_file = new SourceFile(jcc.files.get(0));
        HMRParser parser = new HMRParser();
        try {
            parser.parse(hmr_file);
            XTTModel model = parser.getModel();

            State initial  = new State();
            for (String attname : jcc.attributes.keySet()) {
                String strval = jcc.attributes.get(attname);
                Attribute att = model.getAttributeByName(attname);

                if (att == null) {
                    throw new Exception("Attribute: \"" + attname + "\" is not registered in model");
                }
                Type type = att.getType();
                if (type.isNumeric()) {
                    SimpleNumeric value = new SimpleNumeric(Double.valueOf(strval));
                    if (value.isInTheDomain(type)) {
                        initial.addStateElement(new StateElement(attname, value));
                    }
                }
                else if (type.isSymbolic()) {
                    SimpleSymbolic value = new SimpleSymbolic(strval);
                    initial.addStateElement(new StateElement(attname, value));
                }
            }

            Set<String> tableNames = new HashSet<String>();
            for (Table t : model.getTables()) {
                tableNames.add(t.getName());
            }

            for (String tname : jcc.tabs) {
                if (!tableNames.contains(tname)) {
                    throw new Exception("Table: " + tname + " is not registered in model");
                }
            }

            String[] tabs = new String[jcc.tabs.size()];
            tabs = jcc.tabs.toArray(tabs);
            HeaRT.fixedOrderInference(model, tabs,
                    new Configuration.Builder()
                            .setInitialState(initial)
                            .setUte(new CertaintyFactorsEvaluator())
                            .setCsr(new ConflictSetFireAll()) //fires all rules from cs
                            .build());

            System.out.println("Printing current state (after inference)");
            State current = HeaRT.getWm().getCurrentState(model);
            for(StateElement se : current){
                System.out.println("Attribute "+se.getAttributeName()+" = "+se.getValue());
            }

        } catch (ModelBuildingException e) {
            e.printStackTrace();
        } catch (ParsingSyntaxException e) {
            e.printStackTrace();
        } catch (NotInTheDomainException e) {
            e.printStackTrace();
        } catch (BuilderException e) {
            e.printStackTrace();
        } catch (AttributeNotRegisteredException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
