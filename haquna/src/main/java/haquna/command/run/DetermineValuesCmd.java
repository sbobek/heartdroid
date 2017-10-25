package haquna.command.run;

import java.util.ArrayList;
import java.util.List;

import haquna.Haquna;
import haquna.HaqunaException;
import haquna.command.Command;
import haquna.utils.HaqunaUtils;
import heart.Configuration;
import heart.WorkingMemory;
import heart.inference.GoalDrivenInference;
import heart.inference.InferenceAlgorithm;
import heart.xtt.XTTModel;

public class DetermineValuesCmd implements Command {		
	//WorkingMemory = determineValues(Model, Wm, ['tabName1','tabName2'])
	public static final String pattern = "^" + Haquna.varName + "(\\s*)=(\\s*)determineValues[(]" +
									     Haquna.varName + "(\\s*)[,](\\s*)(.*)" + "[\\[](.*)[\\]](\\s*)" + "(.*)[)](\\s*)";
														
		
	private String commandStr;
	private String varName;
	private String modelName;
	private String[] attrNames;
	private String wmName = null;
	private String[] commandParts;
	
	private XTTModel model;
	private Configuration.Builder confBuilder;
	private WorkingMemory wm;
	
	public DetermineValuesCmd() {
		
	}
	
	public DetermineValuesCmd(String _commandStr) {
		this.commandStr = _commandStr.replace(" ", "");

		commandParts = this.commandStr.split("[(|)=|,|']");		
		this.varName = commandParts[0];
		this.modelName = commandParts[2];
								
	}
	
	@Override
	public boolean execute() {			
		try {									
			model= HaqunaUtils.getModel(modelName);
			confBuilder = new Configuration.Builder();
			
			setupTableNames(commandParts);
			setupWorkingMemory();
										
	    	Configuration cs = confBuilder.build();
	    	new GoalDrivenInference(wm, model, cs).start(new InferenceAlgorithm.TableParameters(attrNames));
	    			    								  
		    if(wmName == null) {
		    	Haquna.clearIfVarIsUsed(varName);
		    	Haquna.wmMap.put(varName, wm);
		    }
		    
		    return true;
			
		} catch(HaqunaException e) {
			//e.printStackTrace();
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
		return new DetermineValuesCmd(cmdStr);
	}
	
	private void setupTableNames(String[] commandParts) throws Exception{
		int tabBegin = 0;
		int tabEnd = 0;
		
		String commandParts2[] = commandParts;
		
		List<String> list = new ArrayList<String>();

	    for(String s : commandParts2) {
	       if(s != null && s.length() > 0) {
	          list.add(s);
	       }
	    }
	    commandParts2 = list.toArray(new String[list.size()]);
		
		for(int i = 0; i < commandParts2.length; i++) {
			if(commandParts2[i].equals("[")) {
				tabBegin = i;
			}
			
			if(commandParts2[i].equals("]")) {
				tabEnd = i;
			}
		}
				
		attrNames = new String[tabEnd - (tabBegin+1)];
		for(int i = tabBegin+1; i < tabEnd; i++) {
			attrNames[i-(tabBegin+1)] = commandParts2[i];				
		}
	}
	
	private void setupWorkingMemory() throws HaqunaException {
		
		if(commandParts[3].matches(Haquna.varName)) {
			if(Haquna.wmMap.containsKey(commandParts[3])) {
				this.wmName = commandParts[3];
			
			} else {
				  throw new HaqunaException("No '" + commandParts[3] + "' WorkingMemory object in memory");
			}
		}
		
		if(wmName == null) {
			HaqunaUtils.checkVarName(varName);
			wm = new WorkingMemory();
			wm.registerAllAttributes(model);
		
		} else {
			wm = HaqunaUtils.getWorkingMemory(wmName);
			confBuilder.setInitialState(wm.getCurrentState());
		}
	}
}
	
