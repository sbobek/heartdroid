package haquna.command.wm;

import java.util.LinkedList;

import haquna.Haquna;
import haquna.HaqunaException;
import haquna.command.Command;
import haquna.utils.HaqunaUtils;
import heart.WorkingMemory;
import heart.alsvfd.Null;
import heart.alsvfd.Range;
import heart.alsvfd.SetValue;
import heart.alsvfd.SimpleNumeric;
import heart.alsvfd.SimpleSymbolic;
import heart.alsvfd.Value;
import heart.exceptions.AttributeNotRegisteredException;
import heart.exceptions.NotInTheDomainException;
import heart.exceptions.RangeFormatException;

public class SetValueOfCmd implements Command {
	
	public static final String pattern = "^" + Haquna.varName + "[.]setValueOf[(][']" +
											   Haquna.attrNamePattern + "['](\\s*)[,](\\s*)[']" +
											   Haquna.attrValuePattern+ "(.*)['][)](\\s*)";
	
	private String commandStr;
	private String wmName;
	private String attributeName;
	private String attributeValue;
	
	public SetValueOfCmd() {
		
	}
	
	public SetValueOfCmd(String commandStr) {
		setupCmdElems(commandStr);
	}
	
	public boolean execute() {
		try {
			WorkingMemory wm = HaqunaUtils.getWorkingMemory(wmName);
			setValue(wm);
			
			return true;
			
		} catch (HaqunaException | AttributeNotRegisteredException | NotInTheDomainException e) {
			HaqunaUtils.printRed(e.getMessage());
			
			return false;
		
		} catch (Exception e) {
			e.printStackTrace();
			
			return false;
		}
	}
	
	private void setupCmdElems(String commandStr) {
		this.commandStr = commandStr;
	
		this.commandStr = this.commandStr.replaceFirst("\\.", "@");
		String[] commandParts = this.commandStr.split("['|@]");				

		this.wmName = commandParts[0].replace(" ", "");
		this.attributeName = commandParts[2].replace(" ", "");
		this.attributeValue = commandParts[4];	
	}
	
	public boolean matches(String commandStr) {
		return commandStr.matches(pattern);
	}

	public Command getNewCommand(String cmdStr) {
		return new SetValueOfCmd(cmdStr);
	}	
	
	public String getCommandStr() {
		return commandStr;
	}

	public void setCommandStr(String commandStr) {
		this.commandStr = commandStr;
	}

	public String getVarName() {
		return wmName;
	}

	public void setVarName(String varName) {
		this.wmName = varName;
	}
	
	private void setValue(WorkingMemory wm) throws AttributeNotRegisteredException, NotInTheDomainException, HaqunaException, RangeFormatException {
			
		if(attributeValue.matches("[\\[].*[\\]](#\\d+(\\.\\d+)?)?")) {
			LinkedList<Value> values = new LinkedList<Value>();
			String attributeValueStr;
			float cf = (float) 1.0;
			
			if(attributeValue.matches("[\\[].*[\\]](#\\d+(\\.\\d+)?)")) {
				String [] splited = attributeValue.split("#");
				cf = Float.parseFloat(splited[splited.length-1]);				
				int cfPartLen = splited[splited.length-1].length()+2;				
				attributeValueStr = attributeValue.substring(1, attributeValue.length()-cfPartLen);
			
			} else {
				attributeValueStr = attributeValue.substring(1, attributeValue.length()-1);
			}
						
			for(String s : attributeValueStr.split("[,]")) {
				if(!s.matches("\\s*")) {
					values.add(getParsedAttrValue(s.trim()));
				}			
			}
			
			Value v = new SetValue(values);
			v.setCertaintyFactor(cf);
			
			wm.setAttributeValue(attributeName, v);
		
		} else {
			Value v = getParsedAttrValue(attributeValue);
			
			wm.setAttributeValue(attributeName, v);
		}
		
	}
	
	private Value getParsedAttrValue(String attrValueWithCfStr ) throws RangeFormatException {		
		float cf = (float) getCertanityFactor(attrValueWithCfStr);
		String attrValueStr = attrValueWithCfStr.split("#")[0];
		
		Value attVal;	
		
		if(attrValueStr == null) {
			attVal = new Null();
					
		} else if(isSimpleNumeric(attrValueStr)) {
			double d = Double.parseDouble(attrValueStr);
			attVal = new SimpleNumeric(d);
		
		} else if(isRange(attrValueStr)) {		
			String fromStr;
			String toStr;
			
			if(attrValueStr.charAt(0) == '[' || attrValueStr.charAt(attrValueStr.length()-1) == ']') {
				fromStr = attrValueStr.split("( to )|\\[|\\]")[1].replace(" ", "");
				toStr = attrValueStr.split("( to )|\\[|\\]")[2].replace(" ", "");
			} else {
				fromStr = attrValueStr.split("( to )|\\[|\\]")[0].replace(" ", "");
				toStr = attrValueStr.split("( to )|\\[|\\]")[1].replace(" ", "");
			}
			
			try {
				SimpleNumeric from =  (SimpleNumeric) getParsedAttrValue(fromStr);
				SimpleNumeric to =  (SimpleNumeric) getParsedAttrValue(toStr);
				
				attVal = new Range(from, true, to, true);
			
			} catch (ClassCastException e) {
				SimpleSymbolic from =  (SimpleSymbolic) getParsedAttrValue(fromStr);
				SimpleSymbolic to =  (SimpleSymbolic) getParsedAttrValue(toStr);
				
				attVal = new Range(from, true, to, true);
			}
			
		} else if( isOrderedSymbolic(attrValueStr)){
			String[] splited = attrValueStr.split("[/]");
			attVal = new SimpleSymbolic(splited[0].replace(" ", ""), Integer.parseInt(splited[1]));		
						
		} else {
			attVal = new SimpleSymbolic(attrValueStr.replace(" ", ""));						
		} 
		
		attVal.setCertaintyFactor(cf);
		
		return attVal;
	}
	
	private double getCertanityFactor(String attrValStr) {
		if(attrValStr.contains("#")) {
			String[] splitted = attrValStr.split("#");
			double cf = Double.parseDouble(splitted[1]);
			return (cf >= 0 && cf <= 1) ? cf : 1.0;
									
		} else {
			return 1.0;
		}
	}
	
	private boolean isSimpleNumeric(String attrValueStr) {
		return attrValueStr.matches("-?\\d+(\\.\\d+)?") ? true : false;
	}
	
	private boolean isRange(String attrValueStr) {
		 return attrValueStr.matches("[\\[]?.*( to ).*[\\]]?") ? true :false;
	}
	
	private boolean isOrderedSymbolic(String attrValueStr) {
		return attrValueStr.matches(".*[/].*") ? true : false;
	}
}
