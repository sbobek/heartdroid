package haquna.command.get;

import java.util.LinkedList;

import haquna.Haquna;
import haquna.HaqunaException;
import haquna.command.Command;
import haquna.utils.HaqunaUtils;
import heart.xtt.Attribute;
import heart.xtt.XTTModel;

public class GetAttributeByNameCmd implements Command {		
	
	public static final String pattern = "^" + Haquna.varName +"(\\s*)=(\\s*)" + Haquna.varName + "[.]getAttributeByName[(]['](.*)['][)](\\s*)";
	
	private String commandStr;
	private String varName;
	private String modelName;
	private String attributeName;
	
	public GetAttributeByNameCmd() {
		
	}
	
	public GetAttributeByNameCmd(String _commandStr) {
		this.commandStr = _commandStr.replace(" ", "");
		
		String[] commandParts = this.commandStr.split("[=|.|']");		
		this.varName = commandParts[0];		
		this.modelName = commandParts[1];
		this.attributeName = commandParts[3];
	}
	
	@Override
	public boolean execute() {				
		try {
			HaqunaUtils.checkVarName(varName);
			XTTModel model = HaqunaUtils.getModel(modelName);
			getAttributeByName(model);
			
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
		return new GetAttributeByNameCmd(cmdStr);
	}

	public String getCommandStr() {
		return commandStr;
	}

	public String getVarName() {
		return varName;
	}

	public String getModelName() {
		return modelName;
	}

	public String getAttribiuteName() {
		return attributeName;
	}
	
	private void getAttributeByName(XTTModel model) throws HaqunaException {
		LinkedList<Attribute> attributes = model.getAttributes();		
		for(Attribute att : attributes){					
			if(att.getName().equals(attributeName)){
				
				Haquna.clearIfVarIsUsed(varName);
				Haquna.attrMap.put(varName, att);
				return;
			}	     
		}
		throw new HaqunaException("No attribute with '" + attributeName + "' name in '" + modelName + "' model");
	}
	
}