package haquna.command.get;

import java.util.LinkedList;

import haquna.Haquna;
import haquna.HaqunaException;
import haquna.command.Command;
import haquna.utils.HaqunaUtils;
import heart.xtt.Type;
import heart.xtt.XTTModel;

public class GetTypeByIdCmd implements Command {		
	
	public static final String pattern = "^" + Haquna.varName +"(\\s*)=(\\s*)" + Haquna.varName + "[.]getTypeById[(]['](.*)['][)](\\s*)";
	
	private String commandStr;
	private String varName;
	private String modelName;
	private String typeId;
	
	public GetTypeByIdCmd() {
		
	}
	
	public GetTypeByIdCmd(String _commandStr) {
		this.commandStr = _commandStr.replace(" ", "");
		
		String[] commandParts = this.commandStr.split("[=|.|']");		
		this.varName = commandParts[0];		
		this.modelName = commandParts[1];
		this.typeId = commandParts[3];
	}
	
	@Override
	public boolean execute() {				
		try {
			HaqunaUtils.checkVarName(varName);
			XTTModel model = HaqunaUtils.getModel(modelName);
			getTypeById(model);
			
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
		return new GetTypeByIdCmd(cmdStr);
	}
		
	public String getVarName() {
		return varName;
	}

	public String getModelName() {
		return modelName;
	}

	public String getTypeId() {
		return typeId;
	}

	private void getTypeById(XTTModel model) throws HaqunaException {
		LinkedList<Type> types = model.getTypes();		
		for(Type type : types){
			if(type.getId() != null && type.getId().equals(typeId)){
				Haquna.clearIfVarIsUsed(varName);
				Haquna.typeMap.put(varName, type);
				return;
			}	     
		}
		throw new HaqunaException("No type with '" + typeId + "' id in '" + modelName + "' model");
	}
}