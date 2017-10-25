package haquna.command.run;

import haquna.Haquna;
import haquna.HaqunaException;
import haquna.command.Command;
import haquna.utils.HaqunaUtils;
import heart.Configuration;
import heart.WorkingMemory;
import heart.inference.DataDrivenInference;
import heart.inference.FixedOrderInference;
import heart.inference.GoalDrivenInference;
import heart.inference.InferenceAlgorithm;
import heart.xtt.XTTModel;

public class RunWithoutWmCmd extends RunAbstactCmd {		
	public static final String pattern = "^" + Haquna.varName + "(\\s*)=(\\s*)" +
											   Haquna.varName + "[.]run[(](\\s*)" +
											   "(((inference(\\s*)=(\\s*)((ddi)|(gdi)|(foi)))|"
											   + "(tokens(\\s*)=(\\s*)((on)|(off)))|"
											   + "(uncertainty(\\s*)=(\\s*)((on)|(off)))|"
											   + "(conflict_resolution(\\s*)=(\\s*)((first)|(last)|(all))|"
											   + "(tables(\\s*)=(\\s*)[\\[]([']" + Haquna.varName + "['](\\s*)[,]?(\\s*))+[\\]])?))(\\s*)[,]?(\\s*))*" +
											   "[)](\\s*)";
		
	private String commandStr;
	private String varName;
	private String modelName;
	private String[] commandParts;	
	private XTTModel model;
	private WorkingMemory wm;
	
	public RunWithoutWmCmd() {
		
	}
	
	public RunWithoutWmCmd(String _commandStr) {
		this.commandStr = _commandStr.replace(" ", "");
				
		commandParts = this.commandStr.split("[(|)=|,|'|.]");	
		this.varName = commandParts[0];
		this.modelName = commandParts[1];								
	}
	
	@Override
	public boolean execute() {		
		try {									
			model = HaqunaUtils.getModel(modelName);
			confBuilder = new Configuration.Builder();
			
			HaqunaUtils.checkVarName(varName);
			wm = new WorkingMemory();
			wm.registerAllAttributes(model);
							
			setupNotMandatoryArgs(commandParts);
			setupTableNames(commandParts);

			setupToken();
			setupUncertainty();
			setupConflictStrategy();
									
			switch(inference) {
	    	  case "foi": {
	    		Configuration cs = confBuilder.build();
	    		new FixedOrderInference(wm, model, cs).start(new InferenceAlgorithm.TableParameters(tables));
	    			    		
	    		break;			    		
	    	  }
	    	 
	    	  case "ddi": {
	    		Configuration cs = confBuilder.build();
	    		new DataDrivenInference(wm, model, cs).start(new InferenceAlgorithm.TableParameters(tables));
	    			    		
	    		break;			    		
	    	  }
	    	
	    	  case "gdi": {
	    		Configuration cs = confBuilder.build();
	    		new GoalDrivenInference(wm, model, cs).start(new InferenceAlgorithm.TableParameters(tables));
	    			    		
	    		break;
	    	  }
			}
						  
			Haquna.clearIfVarIsUsed(varName);
		    Haquna.wmMap.put(varName, wm);
		    
			return true;
		    
		} catch(HaqunaException e) {
			e.printStackTrace();
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
		return new RunWithoutWmCmd(cmdStr);
	}
}
	
