package haquna.completer;

import haquna.Haquna;
import haquna.HaqunaMain;

public class FunctionNamesCompleter extends CompleterAbstract{

	@Override
	protected void setupStringCandidates() {
		stringCandidates.clear();

		HaqunaMain.log(0, this.getClass().getName(), "setupStringCandidates()", "argPos = " + argPos);

		if(argPos < 1) {
			return;
		}
		
		String varName = arguments[argPos-1];

		if(argPos == 1 && Haquna.isVarUsed(varName) && CompleterMenager.getLastDelimiter(arguments,buff,argPos) == '.') {

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
				stringCandidates.add("save");
			}
			
			if(Haquna.typeBuilderMap.containsKey(varName)){
				stringCandidates.add("show()");
			}
			
			if(Haquna.attrBuilderMap.containsKey(varName)){
				stringCandidates.add("show()");
			}
			
			if(Haquna.tableBuilderMap.containsKey(varName)){
				stringCandidates.add("show()");
			}
			
			if(Haquna.ruleBuilderMap.containsKey(varName)){
				stringCandidates.add("show()");
			}
			
		} else if(argPos == 2 && Haquna.isVarUsed(varName) && CompleterMenager.getLastDelimiter(arguments,buff,argPos) == '.'){
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
		} else if(argPos == 2 && arguments[argPos-2].matches(Haquna.varName) && CompleterMenager.getLastDelimiter(arguments,buff,argPos) == '=') {
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
