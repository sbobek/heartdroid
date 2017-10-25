package haquna.command.show;

import java.util.LinkedList;

import haquna.Haquna;
import haquna.HaqunaException;
import haquna.command.Command;
import haquna.utils.HaqunaUtils;
import heart.xtt.Table;
import heart.xtt.XTTModel;

public class ShowTablesListCmd implements Command {
	
	public static final String pattern = "^" + Haquna.varName + "(\\s*)" + "[.]showTablesList[(][)](\\s*)";
	
	private String commandStr;
	private String modelName;
	
	public ShowTablesListCmd() {
		
	}
	
	public ShowTablesListCmd(String _commandStr) {
		this.commandStr = _commandStr.replace(" ", "");
		
		String[] commandParts = this.commandStr.split("[.]");	
		this.modelName = commandParts[0];
	}
	
	public boolean execute() {
		try {
			XTTModel model = HaqunaUtils.getModel(modelName);
			printTablesList(model);
			
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
		return new ShowTablesListCmd(cmdStr);
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
	
	private void printTablesList(XTTModel model) {
		LinkedList<Table> tables = model.getTables();
		System.out.print("[");
		for(Table ta : tables){
			System.out.print(ta.getName());
			
			if(ta != tables.getLast()) {
				System.out.print( ", ");
			}
		}
		System.out.println("]");
	}
}
