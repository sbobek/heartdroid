package haquna.completer;

import haquna.Haquna;
import haquna.HaqunaMain;
import heart.State;
import heart.StateElement;
import heart.WorkingMemory;
import heart.xtt.Attribute;
import heart.xtt.Rule;
import heart.xtt.Table;
import heart.xtt.Type;
import heart.xtt.XTTModel;

public class ParametersCompleter extends CompleterAbstract {

	@Override
	protected void setupStringCandidates() {
		stringCandidates.clear();
		HaqunaMain.log(0, this.getClass().getName(), "setupStringCandidates()", "argPos = " + argPos);

		String varName = arguments[argPos-2];
		String functionName = arguments[argPos-1];
		
		if(varName.equals("new")) {
			if(functionName.equals("WorkingMemory") && CompleterMenager.getLastDelimiter(arguments,buff,argPos) == '(') {
				stringCandidates.addAll(Haquna.modelMap.keySet());
			}
		}

		if(Haquna.tableMap.containsKey(varName) && CompleterMenager.getLastDelimiter(arguments,buff,argPos) == '\'') {
			Table table = Haquna.tableMap.get(varName);
			
			if(functionName.contains("getRuleByName")) {
				for(Rule r : table.getRules()) {
					stringCandidates.add(r.getName());
				}				
			}						
		}     					
				
		if(Haquna.wmMap.containsKey(varName) && CompleterMenager.getLastDelimiter(arguments,buff,argPos) == '\''){
			WorkingMemory wm = Haquna.wmMap.get(varName);
			
			if(functionName.contains("showValueOf")) {
				State current = wm.getCurrentState();
				for(StateElement se : current) {
					stringCandidates.add(se.getAttributeName());					
				}				
			}
			
			if(functionName.contains("setValueOf")){
				State current = wm.getCurrentState();
				for(StateElement se : current) {
					stringCandidates.add(se.getAttributeName());
				}				
			}
		}			
		
		if(Haquna.modelMap.containsKey(varName) && CompleterMenager.getLastDelimiter(arguments,buff,argPos) == '\''){
			XTTModel model = Haquna.modelMap.get(varName);
			
			if(functionName.contains("getTableByName")) {
				for(Table t : model.getTables()) {
					stringCandidates.add(t.getName());
				}				
			}
			
			if(functionName.contains("getTypeByName")) {
				for(Type t : model.getTypes()) {
					stringCandidates.add(t.getName());
				}	
			}
			
			if(functionName.contains("getAttributeByName")) {
				for(Attribute a : model.getAttributes()) {
					stringCandidates.add(a.getName());
				}				
			}
			
			if(functionName.contains("add")) {
				for(String a : Haquna.attrMap.keySet()) {
					stringCandidates.add(a);
				}
				for(String t : Haquna.tableMap.keySet()) {
					stringCandidates.add(t);
				}	
				for(String t : Haquna.typeMap.keySet()) {
					stringCandidates.add(t);
				}
				for(String r : Haquna.ruleMap.keySet()) {
					stringCandidates.add(r);					
				}
				for(String a : Haquna.attrBuilderMap.keySet()) {
					stringCandidates.add(a);
				}
				for(String t : Haquna.tableBuilderMap.keySet()) {
					stringCandidates.add(t);
				}	
				for(String t : Haquna.typeBuilderMap.keySet()) {
					stringCandidates.add(t);
				}
				for(String r : Haquna.ruleBuilderMap.keySet()) {
					stringCandidates.add(r);					
				}
			}
			
			if(functionName.contains("remove")) {
				for(Attribute a : model.getAttributes()) {
					stringCandidates.add(a.getName());
				}
				for(Table t : model.getTables()) {
					stringCandidates.add(t.getName());
				}	
				for(Type t : model.getTypes()) {
					stringCandidates.add(t.getName());
				}
				for(Table t : model.getTables()) {
					for(Rule r : t.getRules()) {
						stringCandidates.add(r.getName());
					}
				}
			}
		}		
		
	}

}
