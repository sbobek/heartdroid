package haquna.command.show;

import haquna.Haquna;
import haquna.HaqunaException;
import haquna.command.Command;
import haquna.utils.HaqunaUtils;
import heart.WorkingMemory;
import heart.alsvfd.Value;

public class ShowValueOfCmd implements Command {
	
	public static final String pattern = "^" + Haquna.varName +"(\\s*)[.]showValueOf[(][']" + Haquna.attrNamePattern + "['][)](\\s*)";
	
	private String commandStr;
	private String wmName;
	private String attributeName;
	
	public ShowValueOfCmd() {
		
	}
	
	public ShowValueOfCmd(String _commandStr) {
		this.commandStr = _commandStr.replace(" ", "");
		
		String[] commandParts = this.commandStr.split("[.|']");	
		this.wmName = commandParts[0];
		this.attributeName = commandParts[2];
	}
	
	public boolean execute() {
		try {
			WorkingMemory wm = HaqunaUtils.getWorkingMemory(wmName);
			printValue(wm);
			
			return true;
		
		} catch (HaqunaException e) {
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
		return new ShowValueOfCmd(cmdStr);
	}	
	
	public String getCommandStr() {
		return commandStr;
	}

	public String getVarName() {
		return wmName;
	}
	
	private void printValue(WorkingMemory wm) {
		Value attVal = wm.getAttributeValue(attributeName);
		System.out.println(attributeName + " = " + attVal);
	}
}
