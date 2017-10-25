package heart.uncertainty;

import heart.xtt.Rule;

import java.util.AbstractMap;
import java.util.LinkedList;

/**
 * Created by sbk on 28.12.16.
 */
public class ConflictSetLastWin implements ConflictSetResolution {
    @Override
    public LinkedList<AbstractMap.SimpleEntry<Rule, UncertainTrue>> resolveConflictSet(ConflictSet cs) {
        LinkedList<AbstractMap.SimpleEntry<Rule, UncertainTrue>> toExecute =  new LinkedList<AbstractMap.SimpleEntry<Rule, UncertainTrue>>();
        toExecute.add(cs.getLast());
        return toExecute;
    }
}
