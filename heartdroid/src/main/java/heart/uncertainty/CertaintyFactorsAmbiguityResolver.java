package heart.uncertainty;

import heart.WorkingMemory;
import heart.alsvfd.Null;
import heart.alsvfd.SetValue;
import heart.alsvfd.Value;
import heart.exceptions.NotInTheDomainException;
import heart.xtt.Decision;
import heart.xtt.Rule;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * The resolver that takes certainty factors of values into consideration while
 * picking the most suitable value from ambiguous set.
 */
public class CertaintyFactorsAmbiguityResolver implements AmbiguityResolver{

    /**
     * The method evaluates the ambiguous values according tot the
     * cumulative rules formulae from he certainty factors algebra.
     * The timestamp of newly created value is set to the oldest timestamp
     * of all ambiguous values.
     * Null value has always current timestamp.
     *
     * @param ambiguousValues set of ambiguous values from which the  true value has
     *                        to be extracted, or calculated
     * @return the resulting value that still may not be certain, yet is is less
     * ambiguous then the set
     */
    @Override
    public Value resolveCumulativeConclusions(LinkedList<Value> ambiguousValues) {
        if(ambiguousValues.isEmpty()) return new Null();
        if(ambiguousValues.size() == 1) return ambiguousValues.getFirst();

        HashMap<Value, Float> register = new HashMap<Value, Float>();

        //create subsets of all possible values
        long timestamp = ambiguousValues.getFirst().getTimestamp();
        for(Value v : ambiguousValues){
            if(v.getTimestamp() < timestamp){
                timestamp = v.getTimestamp();
            }
            Float previous = register.get(v);
            if(previous != null){
                //caclulate cumulative certainty factor
                float newCertainty = CertaintyFactorsEvaluator.calculateCumulative(previous,v.getCertaintyFactor());

                Value newValue = v.clone();
                newValue.setTimestamp(timestamp);
                newValue.setCertaintyFactor(newCertainty);
                register.remove(v);
                register.put(newValue,newCertainty);

            }else{
                register.put(v.clone(),v.getCertaintyFactor());
            }
        }

        //pick the best choice
        Value bestChoice = null;
        for(Value v : register.keySet()){
            if(bestChoice == null) bestChoice = v;
            else if(bestChoice.getCertaintyFactor() <= v.getCertaintyFactor()){
                bestChoice = v;
            }
        }

        return bestChoice;

    }

    /**
     * The method follows the rules of certainty factors algebra in selecting rules to be executed.
     * By the convention every rule in the single XTT2 table is considered as disjunctive.
     * This means that the algorithm has to select choose rules that produces the same conclusions and choose
     * which of them should be fired.
     * If rules within a single uncertainty conflict set has different certainty level, the most certain rule is selected.
     * If there are two or more rules that has the same highest value of certainty, the standard conflict set resolution mechanisms is used.
     * Note, that in case of {@link ConflictSetFireAll} this violates the property of XTT2 table which says that the rules within a single
     * table should be disjunctive.
     * This happens, because values saved in the memory are later resolved by the {@link #resolveCumulativeConclusions(LinkedList)} method.
     * In case when rules have multiple decisions, all decisions that are not entirely the same, are considered different.
     *
     * @param ambiguousRules set of rules that satisfy minimal satisfiability threshold,
     *                       yet there is still uncertain which of them should actually be fired
     * @param wm working memory component that should be used to evaluate rules decisions
     * @param csr conflict set resolution mechanism that should be applied in case when to or more rules have the same certainty level
     * @return the list of rules to be executed
     */
    @Override
    public LinkedList<AbstractMap.SimpleEntry<Rule,UncertainTrue>> resolveDisjunctiveConclusions(ConflictSet ambiguousRules,  WorkingMemory wm, ConflictSetResolution csr) throws NotInTheDomainException {
        //select most certain rules for each of the possible value they produce
        //In case of multiple decisions create new SetValue.
        HashMap<SetValue,ConflictSet> result = new HashMap<>();
        for(AbstractMap.SimpleEntry<Rule, UncertainTrue> r : ambiguousRules){
            SetValue decisionsEvals = new SetValue();
            for(Decision d: r.getKey().getDecisions()) {
                decisionsEvals.appendValue(d.getDecision().evaluate(wm));
            }


            if(result.get(decisionsEvals) != null){
                //comapre certainty factors
                ConflictSet existingCS = result.get(decisionsEvals).copy();
                for(AbstractMap.SimpleEntry<Rule,UncertainTrue> existingRule : result.get(decisionsEvals)){
                    if(existingRule.getValue().getCertinatyFactor() < r.getValue().getCertinatyFactor()){
                        //clear and replace
                        existingCS.clear();
                        existingCS.add(r.getKey(),r.getValue());
                    }else if(existingRule.getValue().getCertinatyFactor() == r.getValue().getCertinatyFactor()){
                        existingCS.add(r.getKey(),r.getValue());
                    }
                }
                result.put(decisionsEvals,existingCS);
            }else{
                ConflictSet initialCS = new ConflictSet();
                initialCS.add(r.getKey(),r.getValue());
                result.put(decisionsEvals,initialCS);
            }

        }

        //for multiple values -- apply the conflict set resolution mechanism
        LinkedList<AbstractMap.SimpleEntry<Rule,UncertainTrue>> toExecute = new LinkedList<>();
        for(ConflictSet conflictSets : result.values()){
            toExecute.addAll(csr.resolveConflictSet(conflictSets));
        }

        return toExecute;
    }


}
