package haquna.command.get;

import java.util.LinkedList;

import haquna.Haquna;
import haquna.HaqunaException;
import haquna.command.Command;
import haquna.utils.HaqunaUtils;
import heart.xtt.Attribute;
import heart.xtt.XTTModel;

public class GetAttributeByIdCmd implements Command {		
	
	public static final String pattern = "^" + Haquna.varName +"(\\s*)=(\\s*)" + Haquna.varName + "[.]getAttributeById[(]['](.*)['][)](\\s*)";
	
	private String commandStr;
	private String varName;
	private String modelName;
	private String attribiuteId;
	
	public GetAttributeByIdCmd() {
		
	}
	
	public GetAttributeByIdCmd(String _commandStr) {
		this.commandStr = _commandStr.replace(" ", "");
		
		String[] commandParts = this.commandStr.split("[=.']");
		this.varName = commandParts[0];		
		this.modelName = commandParts[1];
		this.attribiuteId = commandParts[3];
	}
	
	@Override
	public boolean execute() {				
		try {
			HaqunaUtils.checkVarName(varName);
			XTTModel model = HaqunaUtils.getModel(modelName);
			getAttributeById(model);
			
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
		return new GetAttributeByIdCmd(cmdStr);
	}
	
	public String getVarName() {
		return varName;
	}

	public String getModelName() {
		return modelName;
	}

	public String getAttribiuteId() {
		return attribiuteId;
	}

	private void getAttributeById(XTTModel model) throws HaqunaException {
		LinkedList<Attribute> attribiutes = model.getAttributes();			
		for(Attribute att : attribiutes) {
			if(att.getId() != null && att.getId().equals(attribiuteId)){
				
				Haquna.clearIfVarIsUsed(varName);
				Haquna.attrMap.put(varName, att);
				return;
			}	     
		}
		throw new HaqunaException("No attribute with '" + attribiuteId + "' id in '" + modelName + "' model");
	}
}