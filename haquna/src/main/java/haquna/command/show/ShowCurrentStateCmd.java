package haquna.command.show;

import haquna.Haquna;
import haquna.HaqunaException;
import haquna.command.Command;
import haquna.utils.HaqunaUtils;
import heart.State;
import heart.StateElement;
import heart.WorkingMemory;

public class ShowCurrentStateCmd implements Command {
	
	public static final String pattern = "^" + Haquna.varName + "[.]showCurrentState[(][)](\\s*)";
	
	private String commandStr;
	private String varName;
	
	public ShowCurrentStateCmd() {
		
	}
	
	public ShowCurrentStateCmd(String _commandStr) {
		this.commandStr = _commandStr.replace(" ", "");
		
		String[] commandParts = this.commandStr.split("[.]");	
		this.varName = commandParts[0];
	}
	
	public boolean execute() {
		try {
			WorkingMemory wm = HaqunaUtils.getWorkingMemory(varName);
			printCurrentState(wm);
			
			return true;
		
		} catch (HaqunaException e) {
			HaqunaUtils.printRed(e.getMessage());
			
			return false;
		}				
	}
	
	public boolean matches(String commandStr) {
		return commandStr.matches(pattern);
	}

	public Command getNewCommand(String cmdStr) {
		return new ShowCurrentStateCmd(cmdStr);
	}	
	
	public String getCommandStr() {
		return commandStr;
	}

	public void setCommandStr(String commandStr) {
		this.commandStr = commandStr;
	}

	public String getVarName() {
		return varName;
	}

	public void setVarName(String varName) {
		this.varName = varName;
	}
	
	private void printCurrentState(WorkingMemory wm) {
		State current = wm.getCurrentState();
		for(StateElement se : current){
			//System.out.format("Attribute: %10s = %10s cf = %4f\n", se.getAttributeName(), se.getValue(), se.getValue().getCertaintyFactor());
	    	System.out.println("Attribute: " + se.getAttributeName()+"  \t= " + se.getValue() + "  \t cf = " + se.getValue().getCertaintyFactor());
	    }
	}
}
