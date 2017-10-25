package heart.inference;

import heart.Configuration;
import heart.WorkingMemory;
import heart.exceptions.InferenceInterruptedException;
import heart.xtt.Attribute;
import heart.xtt.Table;
import heart.xtt.XTTModel;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Created by sbk on 31.03.15.
 */
public class GoalDrivenInference extends InferenceAlgorithm {

    /**
     * The constructor that collect all the required information for the inference process to be executed.
     *
     * @param wm    a WorkingMemory element from HeaRT inference engine
     * @param model a XTTModel that will be used  to infer data
     * @param conf  a Configuration object defining uncertainty handling mechanism, conflict set resolution,
     */
    public GoalDrivenInference(WorkingMemory wm, XTTModel model, Configuration conf) {
        super(wm, model, conf);
    }


    @Override
    protected Stack<Table> initStackForAttributes(AttributeParameters attributeParameters) {
        LinkedList<Table> tables = new LinkedList<Table>();
        for(String attributeName : attributeParameters.getAttributeParameters()){
            Attribute coclusion = getModel().getAttributeByName(attributeName);
            tables.addAll(findTablesWithConclusions(coclusion));
        }

        return initStackForTables(tables);

    }

    @Override
    protected Stack<Table> initStackForTables(TableParameters tableParameters){
        LinkedList<Table> tables = new LinkedList<Table>();
        for(String name : tableParameters.getTableParameters()){
            Table t = resolveTable(name);
            if(t != null) {
                tables.push(t);
            }
        }
        return initStackForTables(tables);

    }

    private Stack<Table> initStackForTables(LinkedList<Table> tables){
        LinkedList<Table> grey = new LinkedList<Table>();
        LinkedList<Table> result = new LinkedList<Table>();
        for(Table starter : tables){
            LinkedList<Table> reasoningBranch = BFSDive(new LinkedList<Table>(),grey,starter);
            result.addAll(0,reasoningBranch);
        }

        Iterator<Table> it = result.iterator();
        Stack<Table> initStack = new Stack<Table>();
        while(it.hasNext()){
            initStack.push(it.next());
        }
        return initStack;
    }

    protected LinkedList<Table> BFSDive(LinkedList<Table> visited, LinkedList<Table> grey, Table starter){
        LinkedList<Table> Q = new LinkedList<Table>();

        Q.push(starter);
        while(!Q.isEmpty()){
            Table vertex = Q.pop();
            LinkedList<Table> neighbourhood = new LinkedList<Table>();
            for(Attribute a: vertex.getPrecondition()){
                LinkedList<Table> partialNeighbourhood = findTablesWithConclusions(a);
                neighbourhood.removeAll(partialNeighbourhood);
                neighbourhood.addAll(partialNeighbourhood);
            }

            for(Table n : neighbourhood){
                if(!grey.contains(n) && !visited.contains(n)){
                    grey.push(n);
                    Q.push(n);
                }
            }
            visited.add(vertex);

        }
        return visited;
    }

    protected LinkedList<Table> findTablesWithConclusions(Attribute condition){
        LinkedList<Table> predecessors = new LinkedList<Table>();
        for(Table t: getModel().getTables()){
            if(t.getConclusion().contains(condition)){
                predecessors.add(t);
            }
        }
        return predecessors;
    }

    protected Table resolveTable(String tableName){
        for(Table table : getModel().getTables()){
            if(tableName.equals(table.getName())){
                return table;
            }
        }
        return null;
    }
}
