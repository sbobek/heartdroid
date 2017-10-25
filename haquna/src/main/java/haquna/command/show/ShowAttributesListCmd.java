package haquna.command.show;

import java.util.LinkedList;

import haquna.Haquna;
import haquna.HaqunaException;
import haquna.command.Command;
import haquna.utils.HaqunaUtils;
import heart.xtt.Attribute;
import heart.xtt.XTTModel;

public class ShowAttributesListCmd implements Command {
	
	public static final String pattern = "^" + Haquna.varName + "(\\s*)" + "[.]showAttributesList[(][)](\\s*)";
	
	private String commandStr;
	private String modelName;
	
	public ShowAttributesListCmd() {
		
	}
	
	public ShowAttributesListCmd(String _commandStr) {
		this.commandStr = _commandStr.replace(" ", "");
		
		String[] commandParts = this.commandStr.split("[.]");	
		this.modelName = commandParts[0];
	}
	
	public boolean execute() {
		
		try {
			XTTModel model = HaqunaUtils.getModel(modelName);
			printAttributesList(model);
						
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
		return new ShowAttributesListCmd(cmdStr);
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
	
	private void printAttributesList(XTTModel model) {
		LinkedList<Attribute> attributes = model.getAttributes();
		System.out.print("[");
		for(Attribute attr: attributes){			  
		    System.out.print(attr.getName());
		    
		    if(attr != attributes.getLast()) {
		    	System.out.print(", ");
		    }
		}
		System.out.println("]");
	}
}
