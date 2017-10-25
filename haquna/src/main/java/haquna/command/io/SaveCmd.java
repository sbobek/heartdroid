package haquna.command.io;

import haquna.Haquna;
import haquna.HaqunaException;
import haquna.XttModelUtils;
import haquna.command.Command;
import haquna.utils.HaqunaUtils;
import heart.xtt.XTTModel;

public class SaveCmd implements Command {		
	
	public static final String pattern = "^" + Haquna.varName + "(\\s*)" + "[.]"  + "(\\s*)" + "save[(]['](.*)['][)](\\s*)";
	
	private String commandStr;
	private String modelName;
	private String modelPath;
	
	public SaveCmd() {
		
	}
	
 	public SaveCmd(String _commandStr) {
		this.commandStr = _commandStr.replace(" ", "");
		
		String[] commandParts = this.commandStr.split(".xsave|'");		
		this.modelName = commandParts[0];		
		this.modelPath = commandParts[2];
		
		if(!this.modelPath.contains("/home/")) {
			this.modelPath = System.getProperty("user.dir") + "/" + this.modelPath;
		}
	}
	
	@Override
	public boolean execute() {						
		try {
			XTTModel model = HaqunaUtils.getModel(modelName);
			
			XttModelUtils.saveToFile(model, modelPath);
			
			return true;
			
		} catch (HaqunaException e) {
			HaqunaUtils.printRed(e.getMessage());
			//e.printStackTrace();
			return false;
		
		} catch (Exception e) {
			HaqunaUtils.printRed(e.getMessage());
			//e.printStackTrace();
			return false;
		}			
	}		
	
	public boolean matches(String commandStr) {
		return commandStr.matches(pattern);
	}
	
	public Command getNewCommand(String cmdStr) {
		return new SaveCmd(cmdStr);
	}
	
	public String getCommandStr() {
		return commandStr;
	}

	public String getModelPath() {
		return modelPath;
	}

	public String getModelName() {
		return modelName;
	}	
}
