package heart.uncertainty;

import heart.WorkingMemory;
import heart.alsvfd.Value;
import heart.exceptions.NotInTheDomainException;
import heart.xtt.Rule;

import java.util.AbstractMap;
import java.util.LinkedList;

/**
 * Created by sbk on 10.12.16.
 */
public interface AmbiguityResolver {
    /**
     * This method is used when there is several ambiguous values in the memory and all of the values are considered cumulative
     * in terms of the certainty factors algebra. This is the case, when the values are assigned by different XTT2 tables.
     *
     * @param ambiguousValues set of ambiguous values from which the  true value has
     *                        to be extracted, or calculated
     * @return the resulting value that still may not be certain, yet is is less
     * ambiguous then the set
     */
    public Value resolveCumulativeConclusions(LinkedList<Value> ambiguousValues);

    /**
     * This method is used when there is a need to select rules to fire from ambiguous rule set.
     * It can be thought of as a conlict set resolution on the level of uncertainty.
     * When two or more rules have the same certainty level assigned, then the standard conflict set resolution mechanism
     * should be used.
     * @param ambiguousRules set of rules that satisfy minimal satisfiability threshold,
     *                       yet there is still uncertain which of them should actually be fire
     * @param wm working memory component that should be used to evaluate rules decisions
     * @param csr conflict set resolution mechanism that should be applied in case when to or more rules have the same certainty level
     * @return the set of rules that should be executed.
     */
    public LinkedList<AbstractMap.SimpleEntry<Rule,UncertainTrue>> resolveDisjunctiveConclusions(ConflictSet ambiguousRules, WorkingMemory wm, ConflictSetResolution csr) throws NotInTheDomainException;
}

