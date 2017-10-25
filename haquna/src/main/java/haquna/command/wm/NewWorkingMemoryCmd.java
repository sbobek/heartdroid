package haquna.command.wm;

import haquna.Haquna;
import haquna.HaqunaException;
import haquna.command.Command;
import haquna.utils.HaqunaUtils;
import heart.WorkingMemory;
import heart.xtt.XTTModel;

public class NewWorkingMemoryCmd implements Command {		
	
	public static final String pattern = "^" + Haquna.varName + "(\\s*)=(\\s*)new(\\s*)WorkingMemory[(]" + Haquna.varName + "[)](\\s*)";
	
	private String commandStr;
	private String varName;
	private String modelName;
	
	public NewWorkingMemoryCmd() {
		
	}
	
	public NewWorkingMemoryCmd(String _commandStr) {
		this.commandStr = _commandStr.replace(" ", "");
		
		String[] commandParts = this.commandStr.split("[=|.|'|\\(|\\)]");		
		this.varName = commandParts[0];		
		this.modelName = commandParts[2];
	}
	
	@Override
	public boolean execute() {				
		try {
			HaqunaUtils.checkVarName(varName);
			XTTModel model = HaqunaUtils.getModel(modelName);
			WorkingMemory wm = new WorkingMemory();				
			
			wm.registerAllAttributes(model);				
			Haquna.wmMap.put(varName, wm);
				
			return true;
				
		} catch(HaqunaException e) {
			HaqunaUtils.printRed(e.getMessage());
				
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
		return new NewWorkingMemoryCmd(cmdStr);
	}
}