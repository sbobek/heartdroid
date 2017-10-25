package haquna.command.create;

import haquna.Haquna;
import haquna.HaqunaException;
import haquna.command.Command;
import haquna.utils.HaqunaUtils;
import heart.exceptions.ParsingSyntaxException;
import heart.parser.hmr.HMRParser;
import heart.parser.hmr.runtime.SourceString;
import heart.xtt.Attribute;

public class NewAttributeCmd implements Command {		
	
	public static final String pattern = "^" + Haquna.varName + "(\\s*)" + "[=]" + "(\\s*)" + "new" + "(\\s*)" + "Attribute[(]['](.*)['][)](\\s*)";
	
	private String commandStr;
	private String varName;
	private String hmrCode;
	
	public NewAttributeCmd() {
		
	}
	
	public NewAttributeCmd(String _commandStr) {
		this.commandStr = _commandStr.replace(" ", "");
		
		String[] commandParts = this.commandStr.split("[=|(|)]");		
		this.varName = commandParts[0];		
		this.hmrCode = commandParts[2].substring(1, commandParts[2].length()-1);
		
		if(!hmrCode.substring(hmrCode.length()-1).equals(".")) {
			hmrCode += ".";
		}
	}
	
	@Override
	public boolean execute() {		
		try {
			HaqunaUtils.checkVarName(varName);
			
	        SourceString hmr_code = new SourceString(hmrCode);
	        HMRParser parser = new HMRParser();
	        
	        parser.parse(hmr_code);
	        Attribute.Builder attrBuilder = parser.getAttributeBuilder();
			
	        Haquna.clearIfVarIsUsed(varName);
	        Haquna.attrBuilderMap.put(varName, attrBuilder);
			
			return true;
			
		} catch (HaqunaException | ParsingSyntaxException e) {
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
		return new NewAttributeCmd(cmdStr);
	}

	public String getCommandStr() {
		return commandStr;
	}
	
	public String getVarName() {
		return varName;
	}
}