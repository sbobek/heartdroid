package heart.uncertainty;

import heart.xtt.Rule;

import java.util.AbstractMap.SimpleEntry;
import java.util.LinkedList;

public class ConflictSetFireAll implements ConflictSetResolution {

	@Override
	public LinkedList<SimpleEntry<Rule, UncertainTrue>> resolveConflictSet(
			ConflictSet cs) {
		LinkedList<SimpleEntry<Rule, UncertainTrue>> result = new LinkedList<SimpleEntry<Rule, UncertainTrue>>();
		
		for(SimpleEntry se : cs){
			result.add(se);
		}
		
		return result;
	}

}
