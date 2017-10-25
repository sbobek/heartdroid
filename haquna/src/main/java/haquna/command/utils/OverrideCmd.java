package haquna.command.utils;

import haquna.Haquna;
import haquna.command.Command;
import haquna.utils.HaqunaUtils;
import heart.WorkingMemory;
import heart.xtt.Attribute;
import heart.xtt.Rule;
import heart.xtt.Table;
import heart.xtt.Type;
import heart.xtt.XTTModel;

public class OverrideCmd implements Command {
public static final String pattern = "^" + Haquna.varName +"(\\s*)[=](\\s*)" + Haquna.varName + "(\\s*)";
	
	private String commandStr;
	private String firstVarName;
	private String secondVarName;
	
	public OverrideCmd() {
		
	}
	
	public OverrideCmd(String _commandStr) {
		this.commandStr = _commandStr.replace(" ", "");
		
		String[] commandParts = this.commandStr.split("[=]");		
		this.firstVarName = commandParts[0];		
		this.secondVarName = commandParts[1];
	}
	
	@Override
	public boolean execute() {				
		try {		
			if(firstVarName.equals(secondVarName)) {
				return false;
			}
						
			if(Haquna.modelMap.containsKey(secondVarName)) {
				XTTModel model = Haquna.modelMap.get(secondVarName);
				Haquna.clearIfVarIsUsed(firstVarName);
				Haquna.modelMap.put(firstVarName, model);
				
				return true;
			}
			
			if(Haquna.tableMap.containsKey(secondVarName)){
				Table tab = Haquna.tableMap.get(secondVarName);
				Haquna.clearIfVarIsUsed(firstVarName);
				Haquna.tableMap.put(firstVarName, tab);
				
				return true;
			}
			
			if(Haquna.attrMap.containsKey(secondVarName)) {
				Attribute attr = Haquna.attrMap.get(secondVarName);
				Haquna.clearIfVarIsUsed(firstVarName);
				Haquna.attrMap.put(firstVarName, attr);
				
				return true;
			}
			
			if(Haquna.typeMap.containsKey(secondVarName)) {
				Type type = Haquna.typeMap.get(secondVarName);
				Haquna.clearIfVarIsUsed(firstVarName);
				Haquna.typeMap.put(firstVarName, type);
				
				return true;
			}
			
			if(Haquna.ruleMap.containsKey(secondVarName)) {
				Rule rule = Haquna.ruleMap.get(secondVarName);
				Haquna.clearIfVarIsUsed(firstVarName);
				Haquna.ruleMap.put(firstVarName, rule);
				
				return true;
			}
			
			if(Haquna.callbackMap.containsKey(secondVarName)) {
				String c = Haquna.callbackMap.get(secondVarName);
				Haquna.clearIfVarIsUsed(secondVarName);
				Haquna.callbackMap.put(firstVarName, c);
			}
			
			if(Haquna.wmMap.containsKey(secondVarName)) {
				WorkingMemory wm = Haquna.wmMap.get(secondVarName);
				Haquna.clearIfVarIsUsed(secondVarName);
				Haquna.wmMap.put(firstVarName, wm);
			}		
			
			/*if(attrBuilderMap.containsKey(varName)) {
				attrBuilderMap.remove(varName);
			}
			
			if(typeBuilderMap.containsKey(varName)) {
				typeBuilderMap.remove(varName);
			}
			
			if(ruleBuilderMap.containsKey(varName)) {
				ruleBuilderMap.remove(varName)	;		
			}*/
			
			return false;
		
		} catch (Exception e) {
			HaqunaUtils.printRed(e.getMessage());
			e.printStackTrace();
			
			return false;
		}
	}		
	
	public boolean matches(String commandStr) {
		return commandStr.matches(pattern);
	}
	
	public Command getNewCommand(String cmdStr) {
		return new OverrideCmd(cmdStr);
	}

	public String getCommandStr() {
		return commandStr;
	}

}
