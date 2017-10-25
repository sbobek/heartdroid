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

public class AddCmd implements Command {		
	// NewModel = Model.add(Type)
	public static final String pattern = "^" + Haquna.varName + "(\\s*)=(\\s*)" + Haquna.varName + "[.]add[(]" + Haquna.varName + "[)](\\s*)";
	
	private String commandStr;
	private String modelName;
	private String newModelName;
	private String itemToAddName;
	
	public AddCmd() {
		
	}
	
	public AddCmd(String _commandStr) {
		this.commandStr = _commandStr.replace(" ", "");
		
		String[] commandParts = this.commandStr.split("[=|.|(|)]");		
		this.newModelName = commandParts[0];		
		this.modelName = commandParts[1];
		this.itemToAddName = commandParts[3];
	}
	
	@Override
	public boolean execute() {	
		try {
			HaqunaUtils.checkVarName(newModelName);
			HaqunaUtils.getModel(modelName);
			
			addItem();
									
			return true;
			
		} catch (HaqunaException | ModelBuildingException e) {
			HaqunaUtils.printRed(e.getMessage());
			//e.printStackTrace();

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
		return new AddCmd(cmdStr);
	}
	
	private void addItem() throws HaqunaException, ModelBuildingException {
		if(Haquna.typeBuilderMap.containsKey(itemToAddName)) {
			addType();		            				
		
		} else if(Haquna.typeMap.containsKey(itemToAddName)) {
			addTypeNoBuilder();
		
		} else if(Haquna.attrBuilderMap.containsKey(itemToAddName)) {
			addAttribute();
		
		} else if(Haquna.attrMap.containsKey(itemToAddName)) {
			addAttributeNoBuilder();
		
		} else if(Haquna.ruleBuilderMap.containsKey(itemToAddName)) {
			addRule();
		
		} else if(Haquna.ruleMap.containsKey(itemToAddName)) {
			addRuleNoBuilder();
		
		} else if(Haquna.tableBuilderMap.containsKey(itemToAddName)) {
			addTable();
										
		} else if(Haquna.tableMap.containsKey(itemToAddName)) {
			addTableNoBuilder();
										
		} else {
			throw new HaqunaException("No " + itemToAddName + " variable in memory");
		}
	}
	
	private void addType() throws ModelBuildingException {
		XTTModel model = Haquna.modelMap.get(modelName);
	         
		XTTModel.Builder builder = model.getBuilder();
		
		Type.Builder typeBuilder = Haquna.typeBuilderMap.get(itemToAddName);
        builder.addIncompleteType(typeBuilder);
		
        XTTModel newModel = builder.build();	
				
		Haquna.modelMap.put(newModelName, newModel);
	}
	
	private void addTypeNoBuilder() throws ModelBuildingException {
		XTTModel model = Haquna.modelMap.get(modelName);
		XTTModel.Builder builder = model.getBuilder();

        Type type = Haquna.typeMap.get(itemToAddName);
        
        String modelWithType = null;
        String typeName = null;
        for(String mn : Haquna.modelMap.keySet()) {
        	for(Type t : Haquna.modelMap.get(mn).getTypes()) {
        		if(t.getName().equals(type.getName()) &&
        		   t.getBase().equals(type.getBase()) &&
        		   t.getDomain().equals(type.getDomain())) {
        			modelWithType = mn;
        			typeName = t.getName();
        			break;
        		}
        	}
        }
        Type.Builder typeBuilder = Haquna.modelMap.get(modelWithType).getBuilder().getIncompleteTypeNamed(typeName);
        
        builder.addIncompleteType(typeBuilder);
		XTTModel newModel = builder.build();
			
		Haquna.modelMap.put(newModelName, newModel);
	}
	
	private void addAttribute() throws ModelBuildingException {
		XTTModel model = Haquna.modelMap.get(modelName);
		
		XTTModel.Builder builder = model.getBuilder();
		Attribute.Builder attrBuilder = Haquna.attrBuilderMap.get(itemToAddName);
       
		builder.addIncompleteAttribute(attrBuilder);
		XTTModel newModel = builder.build();	
		
		Haquna.modelMap.put(newModelName, newModel);		                
	}
	
	private void addAttributeNoBuilder() throws ModelBuildingException {
		XTTModel model = Haquna.modelMap.get(modelName);
		XTTModel.Builder builder = model.getBuilder();

        Attribute attr = Haquna.attrMap.get(itemToAddName);
        
        String modelWithAttr = null;
        String attrName = null;
        for(String mn : Haquna.modelMap.keySet()) {
        	for(Attribute a : Haquna.modelMap.get(mn).getAttributes()) {
        		if(a.getName().equals(attr.getName()) &&
        		   a.getAbbreviation().equals(attr.getAbbreviation()) &&
        		   a.getXTTClass().equals(attr.getXTTClass())) {
        			modelWithAttr = mn;
        			attrName = a.getName();
        			break;
        		}
        	}
        }
        Attribute.Builder attrBuilder = Haquna.modelMap.get(modelWithAttr).getBuilder().getIncompleteAttributeNamed(attrName);
        
   		builder.addIncompleteAttribute(attrBuilder);
		XTTModel newModel = builder.build();
			
		Haquna.modelMap.put(newModelName, newModel);
		                
	}
	
	private void addTable() throws ModelBuildingException {
		XTTModel model = Haquna.modelMap.get(modelName);

		XTTModel.Builder builder = model.getBuilder();

		Table.Builder tableBuilder = Haquna.tableBuilderMap.get(itemToAddName);
        
		builder.addIncompleteTable(tableBuilder);
		XTTModel newModel = builder.build();	
		
		Haquna.modelMap.put(newModelName, newModel);		           
	}
	
	private void addTableNoBuilder() throws ModelBuildingException {
		XTTModel model = Haquna.modelMap.get(modelName);
		XTTModel.Builder builder = model.getBuilder();

        Table table = Haquna.tableMap.get(itemToAddName);
        
        String modelWithTable = null;
        String tableName = null;
        for(String mn : Haquna.modelMap.keySet()) {
        	for(Table t : Haquna.modelMap.get(mn).getTables()) {
        		if(t.getName().equals(table.getName())) {
        			modelWithTable = mn;
        			tableName = t.getName();
        			break;
        		}
        	}
        }
        Table.Builder tableBuilder = Haquna.modelMap.get(modelWithTable).getBuilder().getIncompleteTableNamed(tableName);
        
        builder.addIncompleteTable(tableBuilder);
		XTTModel newModel = builder.build();
			
		Haquna.modelMap.put(newModelName, newModel);
	}
	
	private void addRule() throws ModelBuildingException {
		XTTModel model = Haquna.modelMap.get(modelName);
		XTTModel.Builder builder = model.getBuilder();

		Rule.Builder ruleBuilder = Haquna.ruleBuilderMap.get(itemToAddName);
		
		builder.addIncompleteRule(ruleBuilder);
		XTTModel newModel = builder.build();	
		
		Haquna.modelMap.put(newModelName, newModel);			      
	}
	
	private void addRuleNoBuilder() throws ModelBuildingException {
		XTTModel model = Haquna.modelMap.get(modelName);
		XTTModel.Builder builder = model.getBuilder();

        Rule rule = Haquna.ruleMap.get(itemToAddName);
        
        String modelWithRule = null;
        String ruleName = null;
        for(String mn : Haquna.modelMap.keySet()) {
        	for(Table t : Haquna.modelMap.get(mn).getTables()) {
        		for(Rule r : t.getRules()) {
        			if(r.getName().equals(rule.getName()) &&
        			   r.getOrderNumber() == rule.getOrderNumber()) {
            			modelWithRule = mn;
            			ruleName = r.getName();
            			
            			System.out.println(modelWithRule);
            			System.out.println(ruleName);
            			break;
        			}      		
        		}
        	}
        }
        
        Rule.Builder ruleBuilder = Haquna.modelMap.get(modelWithRule).getBuilder().getIncompleteRuleNamed(ruleName);
        
        builder.addIncompleteRule(ruleBuilder);
		XTTModel newModel = builder.build();
			
		Haquna.modelMap.put(newModelName, newModel);
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