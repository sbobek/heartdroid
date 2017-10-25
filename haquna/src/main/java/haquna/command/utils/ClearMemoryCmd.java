package haquna.command.utils;

import haquna.Haquna;
import haquna.command.Command;
import haquna.utils.HaqunaUtils;

public class ClearMemoryCmd implements Command {		
	
	public static final String pattern = "^(\\s*)clearMemory[(][)](\\s*)";
	
	private String commandStr;
	
	public ClearMemoryCmd() {
		
	}
	
	public ClearMemoryCmd(String _commandStr) {
		this.commandStr = _commandStr.replace(" ", "");
	
	}
	
	@Override
	public boolean execute() {		
		try {
			Haquna.modelMap.clear();
			Haquna.wmMap.clear();
			Haquna.attrMap.clear();
			Haquna.tableMap.clear();
			Haquna.ruleMap.clear();
			Haquna.typeMap.clear();
			Haquna.callbackMap.clear();
			Haquna.attrBuilderMap.clear();
			Haquna.tableBuilderMap.clear();
			Haquna.ruleBuilderMap.clear();
			Haquna.typeBuilderMap.clear();
			
			return true;
					
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
		return new ClearMemoryCmd(cmdStr);
	}

	public String getCommandStr() {
		return commandStr;
	}
}