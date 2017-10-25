package haquna.command.get;

import java.util.LinkedList;

import haquna.Haquna;
import haquna.HaqunaException;
import haquna.command.Command;
import haquna.utils.HaqunaUtils;
import heart.xtt.Table;
import heart.xtt.XTTModel;

public class GetTableByNameCmd implements Command {		
	
	public static final String pattern = "^" + Haquna.varName +"(\\s*)=(\\s*)" + Haquna.varName + "[.]getTableByName[(]['](.*)['][)](\\s*)";
	
	private String commandStr;
	private String varName;
	private String modelName;
	private String tableName;
	
	public GetTableByNameCmd() {
		
	}
	
	public GetTableByNameCmd(String _commandStr) {
		this.commandStr = _commandStr.replace(" ", "");
		
		String[] commandParts = this.commandStr.split("[=|.|']");		
		this.varName = commandParts[0];		
		this.modelName = commandParts[1];
		this.tableName = commandParts[3];
	}
	
	@Override
	public boolean execute() {				
		try {
			HaqunaUtils.checkVarName(varName);
			XTTModel model = HaqunaUtils.getModel(modelName);
			getTableByName(model);

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
		return new GetTableByNameCmd(cmdStr);
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

	public String getTableName() {
		return tableName;
	}

	private void getTableByName(XTTModel model) throws HaqunaException {
		LinkedList<Table> tables = model.getTables();		
		for(Table table : tables){
			if(table.getName().equals(tableName)){
				Haquna.clearIfVarIsUsed(varName);
				Haquna.tableMap.put(varName, table);
				return;
			}	     
		}
		throw new HaqunaException("No table with '" + tableName + "' name in '" + modelName + "' model");
	}
}