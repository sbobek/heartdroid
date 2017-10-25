package heart.uncertainty;

import heart.WorkingMemory;
import heart.alsvfd.Null;
import heart.alsvfd.Value;
import heart.xtt.Rule;

import java.util.AbstractMap;
import java.util.LinkedList;

/**
 * Created by sbk on 10.12.16.
 */
public class DefaultAmbiguityResolver implements AmbiguityResolver {
    /**
     * When there is two or more values of the same atribtue in the memory, consider last (most recent)
     * value as the current value.
     *
     * @param ambiguousValues set of ambiguous values from which the  true value has
     *                        to be extracted, or calculated
     * @return the last value of the list of ambiguous values
     */
    @Override
    public Value resolveCumulativeConclusions(LinkedList<Value> ambiguousValues) {
        if(ambiguousValues.isEmpty()){
            return new Null();
        }else {
            return ambiguousValues.getLast();
        }
    }

    /**
     * This method does not consider uncertainty level of the rules, but treats all rules as
     * the equaly certina and immediatelly applies the standard conflict set resolution mechanism chosed for the inference process.
     * @param ambiguousRules set of rules that satisfy minimal satisfiability threshold,
     *                       yet there is still uncertain which of them should actually be fired
     * @param wm working memory component that should be used to evaluate rules decisions
     * @param csr conflict set resolution mechanism that should be applied in case when to or more rules have the same certainty level
     * @return the set of rules determined by the conflict resolution mechanism given in parameter
     */
    @Override
    public LinkedList<AbstractMap.SimpleEntry<Rule,UncertainTrue>> resolveDisjunctiveConclusions(ConflictSet ambiguousRules,  WorkingMemory wm, ConflictSetResolution csr) {
        return csr.resolveConflictSet(ambiguousRules);
    }


}
