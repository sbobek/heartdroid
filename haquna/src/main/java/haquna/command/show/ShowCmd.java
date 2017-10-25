package haquna.command.show;

import haquna.Haquna;
import haquna.XttModelUtils;
import haquna.command.Command;
import heart.xtt.Attribute;
import heart.xtt.Rule;
import heart.xtt.Table;
import heart.xtt.Type;
import heart.xtt.XTTModel;

public class ShowCmd implements Command {
	
	public static final String pattern = "^" + Haquna.varName + "[.]show[(][)](\\s*)";
	
	private String commandStr;
	private String varName;
	
	public ShowCmd() {
		
	}
	
	public ShowCmd(String _commandStr) {
		this.commandStr = _commandStr.replace(" ", "");
		
		String[] commandParts = this.commandStr.split("[.]");	
		this.varName = commandParts[0];
	}
	
	public boolean execute() {
		if(Haquna.tableMap.containsKey(varName)) {
			Table table = Haquna.tableMap.get(varName);
			System.out.println(XttModelUtils.tableToHMR(table));
		     					
		} else if(Haquna.attrMap.containsKey(varName)){
			Attribute attr = Haquna.attrMap.get(varName);
			System.out.println(XttModelUtils.attributeToHMR(attr));
		
		} else if(Haquna.typeMap.containsKey(varName)){
			Type type = Haquna.typeMap.get(varName);
			System.out.println(XttModelUtils.typeToHMR(type));		
		    		
		} else if(Haquna.ruleMap.containsKey(varName)){
			Rule rule = Haquna.ruleMap.get(varName);
			System.out.println(XttModelUtils.ruleToHMR(rule));
		
		} else if(Haquna.callbackMap.containsKey(varName)){
			String callback = Haquna.callbackMap.get(varName);
		    System.out.println("Callback: " + callback);
						
		} else if(Haquna.modelMap.containsKey(varName)){
			XTTModel model = Haquna.modelMap.get(varName);
			System.out.println(XttModelUtils.getHMR(model));
		
		} else if(Haquna.typeBuilderMap.containsKey(varName)){
			Type.Builder typeB = Haquna.typeBuilderMap.get(varName);
			System.out.println(XttModelUtils.typeBuilderToHMR(typeB));
		
		} else if(Haquna.attrBuilderMap.containsKey(varName)){
			Attribute.Builder attrB = Haquna.attrBuilderMap.get(varName);
			System.out.println(XttModelUtils.attributeBuilderToHMR(attrB));
		
		} else if(Haquna.tableBuilderMap.containsKey(varName)){
			Table.Builder tabB = Haquna.tableBuilderMap.get(varName);
			System.out.println(XttModelUtils.tableBuilderToHMR(tabB));
		
		} else if(Haquna.ruleBuilderMap.containsKey(varName)){
			Rule.Builder typeB = Haquna.ruleBuilderMap.get(varName);
			System.out.println(XttModelUtils.ruleBuilderToHMR(typeB));
			
		} else {
			System.out.println("No such object in memory");
		}
		
		return true;
	}
	
	public boolean matches(String commandStr) {
		return commandStr.matches(pattern);
	}
	
	public Command getNewCommand(String cmdStr) {
		return new ShowCmd(cmdStr);
	}
}
