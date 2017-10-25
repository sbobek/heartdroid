package haquna.command.get;

import java.util.LinkedList;

import haquna.Haquna;
import haquna.HaqunaException;
import haquna.command.Command;
import haquna.utils.HaqunaUtils;
import heart.xtt.Type;
import heart.xtt.XTTModel;

public class GetTypeByNameCmd implements Command {		
	
	public static final String pattern = "^" + Haquna.varName +"(\\s*)=(\\s*)" + Haquna.varName + "[.]getTypeByName[(]['](.*)['][)](\\s*)";
	
	private String commandStr;
	private String varName;
	private String modelName;
	private String typeName;
	
	public GetTypeByNameCmd() {
		
	}
	
	public GetTypeByNameCmd(String _commandStr) {
		this.commandStr = _commandStr.replace(" ", "");
		
		String[] commandParts = this.commandStr.split("[=|.|']");		
		this.varName = commandParts[0];		
		this.modelName = commandParts[1];
		this.typeName = commandParts[3];
	}
	
	@Override
	public boolean execute() {				
		try {
			HaqunaUtils.checkVarName(varName);
			XTTModel model = HaqunaUtils.getModel(modelName);
			getTypeByName(model);

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
		return new GetTypeByNameCmd(cmdStr);
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

	public String getTypeName() {
		return typeName;
	}
	
	private void getTypeByName(XTTModel model) throws HaqunaException {
		LinkedList<Type> types = model.getTypes();		
		for(Type type : types){
			if(type.getName().equals(typeName)){
				Haquna.clearIfVarIsUsed(varName);
				Haquna.typeMap.put(varName, type);
				return;
			}	     
		}
		throw new HaqunaException("No type with '" + typeName + "' name in '" + modelName + "' model");
	}
}