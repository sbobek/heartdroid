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

package heart.inference;

import heart.Configuration;
import heart.Debug;
import heart.WorkingMemory;
import heart.exceptions.AttributeNotRegisteredException;
import heart.exceptions.InferenceInterruptedException;
import heart.exceptions.NotInTheDomainException;
import heart.exceptions.UnknownValueException;
import heart.uncertainty.AmbiguityResolver;
import heart.uncertainty.ConflictSet;
import heart.uncertainty.ConflictSetFireAll;
import heart.uncertainty.UncertainTrue;
import heart.xtt.Attribute;
import heart.xtt.Rule;
import heart.xtt.Table;
import heart.xtt.XTTModel;

import java.rmi.activation.ActivationSystem;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Created by sbk on 20.03.15.
 * This is an abstract class for inference algorithms used by HeaRTDroid inference engine.
 * There are three obligatory methods that has to eb implemented, and these are:
 * {@link #runRules(Table t)} method that processes rules within a table, handles conflict set and so on,
 * {@link #initStackForAttributes(heart.inference.InferenceAlgorithm.AttributeParameters)} that prepares initial stack for a given attribute that should be processed to obtain the attribute value,
 * {@link #initStackForTables(heart.inference.InferenceAlgorithm.TableParameters)} that prepares initial stack for tables that should be processed.
 * There are also two methods {@link #onPreExecute()} and {@link #onPostExecute()}, which defined actions which
 * should be performed before and after inference.
 * The original implementation performs  locking and unlocking time in WorkingMemory object, and thus
 * even while overriding this methods a super implementation should always be called first.
 *
 * The intentional use of Inference algorithm is new InferenceAlgorithm(...).start(...),
 * which is similar to running Threads in Java.
 *
 */
public abstract class InferenceAlgorithm {
    private WorkingMemory wm;
    private XTTModel model;
    private Configuration conf;
    private Stack<Table> tablesToFire;


    /**
     * The constructor that collect all the required information for the inference process to be executed.
     *
     * @param wm a WorkingMemory element from HeaRT inference engine
     * @param model a XTTModel that will be used  to infer data
     * @param conf a Configuration object defining uncertainty handling mechanism, conflict set resolution,
     *             initial state and others.
     */
    public InferenceAlgorithm(WorkingMemory wm, XTTModel model, Configuration conf){
        this.wm = wm;
        this.model = model;
        this. conf = conf;
    }


    /**
     * This methods contains all operations that should be performed before inference process starts.
     * This includes locking time in Working memory component.
     * If you override this methods make sure to call its super implementation.
     */
    public void onPreExecute(){
        wm.lockTime();
    }

    /**
     * This methods contains all operations that should be performed after inference process starts.
     * This includes unlocking time in Working memory component.
     * If you override this methods make sure to call its super implementation.
     */
    public void onPostExecute(){
        wm.resolveAmbiguousAttributesValues();
        wm.unlockTime();
        wm.recordLog();
    }

    /**
     * The method pushes Table given as a parameter to the stack.
     *
     * @param t table to be pushed to the stack
     * @return table that have been pushed to the stack or null in case of failure.
     */
    public Table pushTable(Table t){
        return tablesToFire.push(t);
    }

    /**
     * It removes the first Table from stack and returns it.
     * @return first table form the stack
     */
    public Table popTable(){
        return tablesToFire.pop();
    }

    /**
     * It returned first Table form the stack without removeing it.
     * @return first table form the stack
     */
    public Table peakTable(){
        return tablesToFire.peek();
    }

    /**
     * It checks if the table stack is empty
     * @return true if empty, false in other cases
     */
    public boolean isTableStackEmpty(){
        return tablesToFire.isEmpty();
    }

    /**
     * The method that is called to perform inference.
     * This method should process all the rules within a Table given as a parameter.
     * This method should not be called manually, but instead a {@link #start(heart.inference.InferenceAlgorithm.TableParameters) or
     * {@link #start(heart.inference.InferenceAlgorithm.AttributeParameters)}} method should be used
     * @param table an XTT2 table form the model which rules should be processed
     * @throws InferenceInterruptedException the exception that should be thrown in order to stop processing current table
     * and move to another table from the stack
     */
    protected void runRules(Table table) throws InferenceInterruptedException, UnsupportedOperationException{
        ConflictSet conflictSet = new ConflictSet();

        try{
            for(Rule rule : table.getRules()){
                Debug.debug(Debug.heartTag, Debug.Level.RULES, "Processing rule "+rule.getName()+" (ID: "+rule.getId()+")");
                UncertainTrue partialResult = null;
                try {
                    partialResult = rule.evaluate(getWm(), getConf().getUncertainTrueEvaluator());
                } catch (UnknownValueException e) {
                    Debug.debug(Debug.heartTag, Debug.Level.RULES,
                            "Evaluating rule "+rule.getName()+" (ID: "+rule.getId()+") failed due to Null values. Skipping the rule.");
                    continue;
                }

                partialResult.setCertinatyFactor(partialResult.getCertinatyFactor()*rule.getCertaintyFactor());

                if(partialResult.getCertinatyFactor() > getConf().getUncertainTrueEvaluator().getSatisfiabilityThreshold()){
                    conflictSet.add(rule, partialResult);
                }

                Debug.debug(Debug.heartTag, Debug.Level.RULES, "Finished evaluating rule "+rule.getName()+" (ID: "+rule.getId()+"). "+
                        "SATISFIED with ("+partialResult.getCertinatyFactor()+") certainty.");


            }

            //If the conflict set is empty, then fire the ruleToFire
            //Otherwise, launch conflict resolution mechanism
            if(conflictSet.isEmpty()){
                Debug.debug(Debug.heartTag, Debug.Level.RULES,
                            "No rule to fire in table "+table.getName()+" (ID: "+table.getId()+").");
            }else{
                Debug.debug(Debug.heartTag, Debug.Level.RULES,
                        "Conflict set of table "+table.getName()+" (ID: "+table.getId()+
                                ") is not empty (contains "+conflictSet.size()+" rules).");

                LinkedList<AbstractMap.SimpleEntry<Rule, UncertainTrue>> toExecute =
                        getConf().getUncertainTrueEvaluator().getAmbiguityResolver().resolveDisjunctiveConclusions(conflictSet,wm,getConf().getConflictSetResolution());
                for(AbstractMap.SimpleEntry<Rule, UncertainTrue> se: toExecute){
                    Rule r = se.getKey();
                    if(r.execute(getWm(), se.getValue())) {
                        Debug.debug(Debug.heartTag, Debug.Level.RULES,
                                "Rule " + r.getName() + " (ID: " + r.getId() + ") fired.");
                    }else {
                        Debug.debug(Debug.heartTag, Debug.Level.RULES,
                                "Rule " + r.getName() + " (ID: " + r.getId() + ") execution failed.");
                    }
                    //TODO: if link to table -- pass token if tdi enabled, otherwise -- ignore
                    //TODO: if link to rule -- pass token to rule if tdi enables (this rule will be chosen once conflict resolution is fired), otherwise -- ignore
                }

            }

        }catch(UnsupportedOperationException e){
            Debug.debug(Debug.heartTag, Debug.Level.TABLES, "Unsupported operation found during inference. Inference interrupted.");
            throw e;
        } catch (NotInTheDomainException e) {
            Debug.debug(Debug.heartTag, Debug.Level.TABLES, "Value "+e.getValue().toString()+" not in the domain "+e.getDomain().toString()+". Inference interrupted.");
        }
    }


    /**
     * The method creates an initial stack of Tables that should be processed by the {@link #runRules(Table t)} method.
     * This is only an initial state and it can be modified during the inference. However for some inference modes
     * this initial stack will be the final.
     *
     * @param attributeParameters attributes names which values should be obtain in the inference process
     * @return stack fo tables to be fired.
     * This stack can be later modified during the inference process in {@link #runRules(Table t)} method.
     */
    protected abstract Stack<Table> initStackForAttributes(AttributeParameters attributeParameters);

    /**
     * The method creates an initial stack of Tables that should be processed by the {@link #runRules(Table t)} method
     * This is only an initial state and it can be modified during the inference. However for some inference modes
     * this initial stack will be the final.
     *
     * @param tableParameters tables names that are the parameters to the inference engine.
     *                             This could mean different things in different reasoning modes.
     * @return stack fo tables to be fired.
     * This stack can be later modified during the inference process in {@link #runRules(Table t)} method.
     */
    protected abstract Stack<Table> initStackForTables(TableParameters tableParameters);

    /**
     * A method that executes all the inference methods in the following order:
     * {@link #onPreExecute()},
     * then {@link #initStackForAttributes(heart.inference.InferenceAlgorithm.AttributeParameters)} i}
     * is called to create the staring point for the algorithm,
     * then, in a loop, {@link #runRules(Table t)} until there are tables on the stack,
     * and after the loop is finished {@link #onPostExecute()}
     *
     * @param ap attributes names which values should be obtain in the inference process
     */
    public final void start(AttributeParameters ap) {
        onPreExecute();
        setTablesToFire(initStackForAttributes(ap));
        try {
            getWm().setAmbiguityResolver(conf.getUncertainTrueEvaluator().getAmbiguityResolver());
            getWm().setCurrentState(getConf().getInitialState(), getModel(), true);
            getWm().resolveAmbiguousAttributesValues();
            while (!tablesToFire.isEmpty()) {
                Table table = tablesToFire.pop();
                Debug.debug(Debug.heartTag, Debug.Level.TABLES, "Processing table " + table.getName() + " (ID: " + table.getId() + ")");
                try {
                    runRules(table);
                } catch (InferenceInterruptedException e) {
                    Debug.debug(Debug.heartTag, Debug.Level.TABLES, "Processing table " + table.getName() + " (ID: " + table.getId() + ") interrupted.");
                }
                Debug.debug(Debug.heartTag, Debug.Level.TABLES, "Processing table " + table.getName() + " (ID: " + table.getId() + ") finished.");

            }
        } catch (NotInTheDomainException e) {
            Debug.debug(Debug.heartTag, Debug.Level.TABLES, "Value "+e.getValue().toString()+" not in the domain "+e.getDomain().toString()+". Inference interrupted.");
        } catch (AttributeNotRegisteredException e) {
            Debug.debug(Debug.heartTag, Debug.Level.TABLES, "Attribute "+e.getAttributeName()+" not registered in WirkingMemory. Inference interrupted.");
        }finally {
            onPostExecute();
        }
    }


    /**
     *A method that executes all the inference methods in the following order:
     * {@link #onPreExecute()},
     * then {@link #initStackForTables(heart.inference.InferenceAlgorithm.TableParameters)}
     * is called to create the staring point for the algorithm,
     * then, in a loop, {@link #runRules(Table t)} until there are tables on the stack,
     * and after the loop is finished {@link #onPostExecute()}
     *
     * @param tp tables names that are the parameters to the inference engine.
     *                             This could mean different things in different reasoning modes.
     */
    public final void start(TableParameters tp) {
        onPreExecute();
        setTablesToFire(initStackForTables(tp));
        try {
            getWm().setAmbiguityResolver(conf.getUncertainTrueEvaluator().getAmbiguityResolver());
            getWm().setCurrentState(getConf().getInitialState(), getModel(), true);
            getWm().resolveAmbiguousAttributesValues();
            while (!tablesToFire.isEmpty()) {
                Table table = tablesToFire.pop();
                Debug.debug(Debug.heartTag, Debug.Level.TABLES, "Processing table " + table.getName() + " (ID: " + table.getId() + ")");
                try {
                    runRules(table);
                } catch (InferenceInterruptedException e) {
                    Debug.debug(Debug.heartTag, Debug.Level.TABLES, "Processing table " + table.getName() + " (ID: " + table.getId() + ") interrupted.");
                }
                Debug.debug(Debug.heartTag, Debug.Level.TABLES, "Processing table " + table.getName() + " (ID: " + table.getId() + ") finished.");

            }
        } catch (NotInTheDomainException e) {
            Debug.debug(Debug.heartTag, Debug.Level.TABLES, "Value "+e.getValue().toString()+" not in the domain "+e.getDomain().toString()+". Inference interrupted.");
        } catch (AttributeNotRegisteredException e) {
            Debug.debug(Debug.heartTag, Debug.Level.TABLES, "Attribute "+e.getAttributeName()+" not registered in WirkingMemory. Inference interrupted.");
        }finally{
            onPostExecute();
        }
    }


    public WorkingMemory getWm() {
        return wm;
    }

    public void setWm(WorkingMemory wm) {
        this.wm = wm;
    }

    public XTTModel getModel() {
        return model;
    }

    public void setModel(XTTModel model) {
        this.model = model;
    }


    public Configuration getConf() {
        return conf;
    }

    public void setConf(Configuration conf) {
        this.conf = conf;
    }

    public Stack<Table> getTablesToFire() {
        return tablesToFire;
    }

    public void setTablesToFire(Stack<Table> tablesToFire) {
        this.tablesToFire = tablesToFire;
    }


    public static class AttributeParameters{

        String [] attributeParameters;

        public AttributeParameters(String... attributeParameters){
            this.attributeParameters = attributeParameters;
        }
        public String[] getAttributeParameters() {
            return attributeParameters;
        }

    }

    public static class TableParameters{
        String [] tableParameters;

        public TableParameters(String... tableParameters){
            this.tableParameters = tableParameters;
        }

        public String[] getTableParameters() {
            return tableParameters;
        }
    }

}
