package haquna.command.run;

import java.util.ArrayList;
import java.util.List;

import haquna.command.Command;
import heart.Configuration;
import heart.uncertainty.*;

abstract public class RunAbstactCmd implements Command {					
	
	protected String[] tables = new String[]{};	
	protected String inference = "ddi";
	protected String tokens = "off";
	protected String uncertainty = "on";
	protected String conflict_resolution = "first";		
	protected Configuration.Builder confBuilder;
	
	@Override
	abstract public boolean execute();		
	
	abstract public boolean matches(String commandStr);
	
	abstract public Command getNewCommand(String cmdStr);
	
	protected void setupToken() {
		switch(tokens) {
		  case "on": {
			confBuilder.setTokenPassingEnabled(true);
			break;
		  }
		  case "off": {
			confBuilder.setTokenPassingEnabled(false);
			break;
		  }
		}
	}
	
	protected void setupUncertainty() {
		switch(uncertainty) {
		  case "on": {
			confBuilder.setUte(new CertaintyFactorsEvaluator());
			break;
		  }
		  case "off": {
			confBuilder.setUte(new ALSVEvaluator());
			break;
		  }
		}
	}
	
	protected void setupConflictStrategy() {
		switch(conflict_resolution) {
		  case "first": {
			confBuilder.setCsr(new ConflictSetFirstWin());
			break;
		  }
		  case "last": {
			confBuilder.setCsr(new ConflictSetLastWin());
			break;
		  }
		  case "all": {
			confBuilder.setCsr(new ConflictSetFireAll());
			break;
		  }
		}
	}
	
	/*protected void setupTableNames(String[] commandParts){
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
		try {			
			tableNames = new String[tabEnd - (tabBegin+1)];
			for(int i = tabBegin+1; i < tabEnd; i++) {
				tableNames[i-(tabBegin+1)] = commandParts2[i];
				
			}
		} catch(Exception e) {
			tableNames = new String[]{};
			System.out.println("Tables names were not initialized");
		}
	}*/
	
	protected void setupTableNames(String[] commandParts){
		for(int i = 0; i < commandParts.length-1; i++) {
			if(commandParts[i].equals("tables")) {
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
				
				for(int j = 0; j < commandParts2.length; j++) {
					if(commandParts2[j].equals("[")) {
						tabBegin = j;
					}
					
					if(commandParts2[j].equals("]")) {
						tabEnd = j;
					}
				}
				try {			
					tables = new String[tabEnd - (tabBegin+1)];
					for(int j = tabBegin+1; j < tabEnd; j++) {
						tables[j-(tabBegin+1)] = commandParts2[j];
						
					}
				} catch(Exception e) {
					tables = new String[]{};
					System.out.println("Tables names were not initialized");
				}
			}
		}
	}
	
	protected void setupNotMandatoryArgs(String[] commandParts) throws Exception{				
		for(int i = 0; i < commandParts.length-1; i++) {
			String nextArgument = commandParts[i+1];
			
			switch(commandParts[i]) {
			case "inference" : {
				if(nextArgument.matches("gdi|foi|ddi")) {					
					this.inference = nextArgument;
				
				} else {
					throw new Exception("Incorrect mode arg.");
				}
				break;
			}
			
			case "tokens": {
				if(nextArgument.matches("on|off")) {
					this.tokens = nextArgument;
				
				}else {
					throw new Exception("Incorrect token arg.");
				}
				
				break;
			}
			
			case "uncertainty": {
				if(nextArgument.matches("on|off")) {
					this.uncertainty = nextArgument;
				
				} else {
					throw new Exception("Incorrect uncertainty arg.");
				}
				
				break;
			}
			
			case "conflict_resolution": {
				if(nextArgument.matches("first|last|all")) {
					this.conflict_resolution= commandParts[i+1];
				
				} else {
					throw new Exception("Incorrect conflict_strategy arg");
				}
				
				break;
			}
			
			}
		}
		
	}

}
	
