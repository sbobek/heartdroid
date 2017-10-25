package haquna.command.add;

import haquna.Haquna;
import haquna.HaqunaException;
import haquna.command.Command;
import haquna.utils.HaqunaUtils;
import heart.exceptions.ModelBuildingException;
import heart.xtt.Attribute;
import heart.xtt.Rule;
import heart.xtt.Table;
import heart.xtt.Type;
import heart.xtt.XTTModel;

public class RemoveCmd implements Command {		
	// NewModel = Model.add(Type)
	public static final String pattern = "^" + Haquna.varName + "(\\s*)=(\\s*)" + Haquna.varName + "[.]remove[(][']" + "(.*)" + "['][)](\\s*)";
	
	private String commandStr;
	private String modelName;
	private String newModelName;
	private String itemToRemoveName;

	
	public RemoveCmd() {
		
	}
	
	public RemoveCmd(String _commandStr) {
		this.commandStr = _commandStr.replace(" ", "");
		
		String[] commandParts = this.commandStr.split("[=|.|']");		
		this.newModelName = commandParts[0];		
		this.modelName = commandParts[1];
		this.itemToRemoveName = commandParts[3];
				
	}
	
	@Override
	public boolean execute() {		
		try {
			HaqunaUtils.checkVarName(newModelName);
			XTTModel model = HaqunaUtils.getModel(modelName);
			
			removeItem(model, itemToRemoveName);
						
			return true;
			
		} catch (HaqunaException | ModelBuildingException e) {
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
		return new RemoveCmd(cmdStr);
	}
	
	private void removeItem(XTTModel model, String itemName) throws HaqunaException, ModelBuildingException {
		switch(determineWhatToRemove(model)) {
		  case "type": {
			System.out.println("type");
			removeType();
			break;
		
		  }				  
		  case "attribute": {
			System.out.println("attr");
			removeAttribute();
			break;
			
		  }
		  case "rule": {
			System.out.println("rule");
			removeRule();
			break;
			
		  }
		  case "table": {
		    System.out.println("table");
			removeTable();
			break;
			
		  }
		  default: {
			throw new HaqunaException("No type, attribute, rule nor table with '" + itemToRemoveName + "' name");
		  }
		}
	}
	
	private void removeType() throws ModelBuildingException {
		XTTModel model = Haquna.modelMap.get(modelName);

        XTTModel.Builder builder = model.getBuilder();       
        XTTModel newModel = null;
        
		builder.removeIncompleteTypeNamed(itemToRemoveName);
		newModel = builder.build();
		
		Haquna.modelMap.put(newModelName, newModel);
	}
	
	private void removeAttribute() throws ModelBuildingException {
		XTTModel model = Haquna.modelMap.get(modelName);

        XTTModel.Builder builder = model.getBuilder();       
        XTTModel newModel = null;

        builder.removeIncompleteAttributeNamed(itemToRemoveName);
		newModel = builder.build();
			
		Haquna.modelMap.put(newModelName, newModel);
	}
	
	private void removeTable() throws ModelBuildingException {
		XTTModel model = Haquna.modelMap.get(modelName);

        XTTModel.Builder builder = model.getBuilder();       
        XTTModel newModel = null;
        
		builder.removeIncompleteTableNamed(itemToRemoveName);
		newModel = builder.build();
		
		Haquna.modelMap.put(newModelName, newModel);
	}
	
	private void removeRule() throws ModelBuildingException {
		XTTModel model = Haquna.modelMap.get(modelName);

        XTTModel.Builder builder = model.getBuilder();       
        XTTModel newModel = null;

		builder.removeIncompleteRuleNamed(itemToRemoveName);
		newModel = builder.build();
		
		Haquna.modelMap.put(newModelName, newModel);
	}
	
	private String determineWhatToRemove(XTTModel model) {
		for(Type type : model.getTypes()) {
			if(type.getName().equals(itemToRemoveName)) {
				return "type";
			}
		}
		
		for(Attribute attr : model.getAttributes()) {
			if(attr.getName().equals(itemToRemoveName)) {
				return "attribute";
			}
		}
		
		for(Table table : model.getTables()) {
			for(Rule rule : table.getRules()) {
				if(rule.getName().equals(itemToRemoveName)) {
					return "rule";
				}
			}			
		}
		
		for(Table table : model.getTables()) {
			if(table.getName().equals(itemToRemoveName)) {
				return "table";
			}
		}
		
		return "nothing";
	}
	
	public String getCommandStr() {
		return commandStr;
	}

	public void setCommandStr(String commandStr) {
		this.commandStr = commandStr;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	
}