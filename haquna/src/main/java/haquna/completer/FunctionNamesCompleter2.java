package haquna.completer;

import haquna.Haquna;

public class FunctionNamesCompleter2 extends CompleterAbstract{

	@Override
	protected void setupStringCandidates() {
		stringCandidates.clear();
		
		if(argPos < 1) {
			return;
		}
		
		String varName = arguments[argPos-1];
		
		if(argPos == 1 && Haquna.isVarUsed(varName)) {
			
			
			if(Haquna.tableMap.containsKey(varName)) {
				stringCandidates.add("showRulesList()");
				stringCandidates.add("show()");
			}
			
			if(Haquna.attrMap.containsKey(varName)){
				stringCandidates.add("show()");
			}
			
			if(Haquna.typeMap.containsKey(varName)){
				stringCandidates.add("show()");		
			}
			
			if(Haquna.ruleMap.containsKey(varName)){
				stringCandidates.add("show()");
			}
			
			if(Haquna.wmMap.containsKey(varName)){
				stringCandidates.add("showValueOf");
				stringCandidates.add("setValueOf");
				stringCandidates.add("showHistoricalValueOf");
				stringCandidates.add("showCurrentState()");
			}
			
			if(Haquna.modelMap.containsKey(varName)){
				stringCandidates.add("show()");
				stringCandidates.add("showTablesList()");
				stringCandidates.add("showAttributesList()");
				stringCandidates.add("showTypesList()");			;
				stringCandidates.add("xsave");
				//stringCandidates.add("run");
			}
			
		} else if(argPos == 2 && Haquna.isVarUsed(varName)){
			if(Haquna.tableMap.containsKey(varName)) {
				stringCandidates.add("getRuleByName");
				stringCandidates.add("getRuleById");
			}
			
			if(Haquna.attrMap.containsKey(varName)){
				stringCandidates.add("getType()");
				stringCandidates.add("getCallback()");
			}
								
			if(Haquna.modelMap.containsKey(varName)){
				stringCandidates.add("getTableByName");
				stringCandidates.add("getTableById");
				stringCandidates.add("getAttributeByName");
				stringCandidates.add("getAttributeById");
				stringCandidates.add("getTypeByName");
				stringCandidates.add("getTypeById");
				stringCandidates.add("add");
				stringCandidates.add("remove");
				//stringCandidates.add("run");
			}				
		} else if(argPos == 2 && arguments[argPos-2].matches(Haquna.varName)) {
			if(varName.equals("new")){
				stringCandidates.add("WorkingMemory");
				stringCandidates.add("Type");
				stringCandidates.add("Attribute");
				stringCandidates.add("Table");
				stringCandidates.add("Rule");
				stringCandidates.add("Model");				
			}
		}
		
	}

}
