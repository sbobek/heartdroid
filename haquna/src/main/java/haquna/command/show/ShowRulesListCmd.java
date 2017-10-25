package haquna.command.show;

import java.util.LinkedList;

import haquna.Haquna;
import haquna.HaqunaException;
import haquna.command.Command;
import haquna.utils.HaqunaUtils;
import heart.xtt.Rule;
import heart.xtt.Table;

public class ShowRulesListCmd implements Command {
	
	public static final String pattern = "^" + Haquna.varName + "(\\s*)" + "[.]showRulesList[(][)](\\s*)";
	
	private String commandStr;
	private String tableName;
	
	public ShowRulesListCmd() {
		
	}
	
	public ShowRulesListCmd(String _commandStr) {
		this.commandStr = _commandStr.replace(" ", "");
		
		String[] commandParts = this.commandStr.split("[.]");	
		this.tableName = commandParts[0];
	}
	
	public boolean execute() {
		try {
			Table table = HaqunaUtils.getTable(tableName);
			printRulesList(table);
			
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
		return new ShowRulesListCmd(cmdStr);
	}	
	
	public String getCommandStr() {
		return commandStr;
	}

	public String getVarName() {
		return tableName;
	}
	
	private void printRulesList(Table table) {
		LinkedList<Rule> rules = table.getRules();
		
		System.out.print("[");					  
		for(Rule rule : rules){
			System.out.print(rule.getName());
			
			if(rule != rules.getLast()) {
				System.out.print(", ");
			}
			  
		}
		System.out.println("]");
	}
}
