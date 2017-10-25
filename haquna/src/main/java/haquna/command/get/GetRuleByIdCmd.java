package haquna.command.get;

import java.util.LinkedList;

import haquna.Haquna;
import haquna.HaqunaException;
import haquna.command.Command;
import haquna.utils.HaqunaUtils;
import heart.xtt.Rule;
import heart.xtt.Table;

public class GetRuleByIdCmd implements Command {		
	
	public static final String pattern = "^" + Haquna.varName +"(\\s*)=(\\s*)" + Haquna.varName + "[.]getRuleById[(]['](.*)['][)](\\s*)";
	
	private String commandStr;
	private String varName;
	private String ruleId;
	private String tableName;
	
	public GetRuleByIdCmd() {
		
	}
	
	public GetRuleByIdCmd(String _commandStr) {
		this.commandStr = _commandStr.replace(" ", "");
		
		String[] commandParts = this.commandStr.split("[=|.|']");		
		this.varName = commandParts[0];		
		this.tableName = commandParts[1];
		this.ruleId = commandParts[3];
	}
	
	@Override
	public boolean execute() {
		try {
			HaqunaUtils.checkVarName(varName);
			Table table = HaqunaUtils.getTable(tableName);
			getRuleById(table);
			
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
		return new GetRuleByIdCmd(cmdStr);
	}
	
	public String getVarName() {
		return varName;
	}

	public String getRuleId() {
		return ruleId;
	}

	public String getTableName() {
		return tableName;
	}

	private void getRuleById(Table table) throws HaqunaException {
		LinkedList<Rule> rules = table.getRules();		
		for(Rule rule : rules) {
			if(rule.getId() != null && rule.getId().equals(ruleId)) {
				
				Haquna.clearIfVarIsUsed(varName);
				Haquna.ruleMap.put(varName, rule);
				return;
			}
		}
		throw new HaqunaException("No rule with '" + ruleId + "' id in '" + tableName + "' table");
	}
}