package haquna.command.show;

import java.util.LinkedList;

import haquna.Haquna;
import haquna.HaqunaException;
import haquna.command.Command;
import haquna.utils.HaqunaUtils;
import heart.xtt.Type;
import heart.xtt.XTTModel;

public class ShowTypesListCmd implements Command {
	
	public static final String pattern = "^" + Haquna.varName + "(\\s*)" + "[.]" + "(\\s*)" + "showTypesList[(][)](\\s*)";
	
	private String commandStr;
	private String modelName;
	
	public ShowTypesListCmd() {
		
	}
	
	public ShowTypesListCmd(String _commandStr) {
		this.commandStr = _commandStr.replace(" ", "");
		
		String[] commandParts = this.commandStr.split("[.]");	
		this.modelName = commandParts[0];
	}
	
	public boolean execute() {
		try {
			XTTModel model = HaqunaUtils.getModel(modelName);
			printTypesList(model);
			
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
		return new ShowTypesListCmd(cmdStr);
	}
	
	public String getCommandStr() {
		return commandStr;
	}

	public void setCommandStr(String commandStr) {
		this.commandStr = commandStr;
	}

	public String getVarName() {
		return modelName;
	}

	public void setVarName(String varName) {
		this.modelName = varName;
	}
	
	private void printTypesList(XTTModel model) {
		LinkedList<Type> types = model.getTypes();
		System.out.print("[");
		for(Type type : types){				
		    System.out.print(type.getName());
		    
		    if(type != types.getLast()) {
		    	System.out.print(", ");
		    }			    
		}
		System.out.println("]");
	}
}
