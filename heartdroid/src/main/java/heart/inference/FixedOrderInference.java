package heart.inference;

import heart.Configuration;
import heart.Debug;
import heart.WorkingMemory;
import heart.exceptions.AttributeNotRegisteredException;
import heart.exceptions.NotInTheDomainException;
import heart.exceptions.UnknownValueException;
import heart.uncertainty.ConflictSet;
import heart.uncertainty.UncertainTrue;
import heart.xtt.Rule;
import heart.xtt.Table;
import heart.xtt.XTTModel;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Created by sbk on 21.03.15.
 */
public class FixedOrderInference extends InferenceAlgorithm {
    /**
     * The constructor that collect all the required information for the inference process to be executed.
     *
     * @param wm          a WorkingMemory element from HeaRT inference engine
     * @param model       a XTTModel that will be used  to infer data
     * @param conf        a Configuration object defining uncertainty handling mechanism, conflict set resolution,
     */
    public FixedOrderInference(WorkingMemory wm, XTTModel model, Configuration conf) {
        super(wm, model, conf);
    }



    @Override
    protected Stack<Table> initStackForAttributes(AttributeParameters ap) {
        throw new UnsupportedOperationException(
                "Fixed order inference does not allow for creating stack of tables based on the attribute name. " +
                        "Use DataDrivenInference or GoalDriven class for that");
    }

    @Override
    protected Stack<Table> initStackForTables(TableParameters tp) {
        Stack<Table> initStack = new Stack<Table>();
        for(int i = tp.getTableParameters().length-1; i >= 0; i--){
            for(Table table : getModel().getTables()){
                if(tp.getTableParameters()[i].equals(table.getName())){
                    initStack.push(table);
                }
            }
        }
        return initStack;
    }
}
