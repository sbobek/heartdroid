package haquna.command.show;

import haquna.Haquna;
import haquna.HaqunaException;
import haquna.command.Command;
import haquna.utils.HaqunaUtils;
import heart.RelativeTimestamp;
import heart.RelativeTimestamp.TimeType;
import heart.WorkingMemory;
import heart.alsvfd.Value;

public class ShowHistoryValueOfCmd implements Command {
	
	public static final String pattern = "^" + Haquna.varName + "(\\s*)[.](\\s*)showHistoricalValueOf[(]['](.+)['][,]['](.+)['][)](\\s*)";
	
	private String commandStr;
	private String wmName;
	private String attributeName;
	private String relativeTime;
	
	public ShowHistoryValueOfCmd() {
		
	}
	
	public ShowHistoryValueOfCmd(String _commandStr) {
		this.commandStr = _commandStr.replace(" ", "");
		
		String[] commandParts = this.commandStr.split("[.|']");	
		this.wmName = commandParts[0];
		this.attributeName = commandParts[2];
		this.relativeTime = commandParts[4];		
		 
	}
	
	public boolean execute() {
		try {
			WorkingMemory wm = HaqunaUtils.getWorkingMemory(wmName);
			Long timeSec = Long.parseLong(relativeTime);
			Long time = timeSec * 1000; 
			
			System.out.println(time);
			
			RelativeTimestamp rt = new RelativeTimestamp(-time, TimeType.MILISCOUNT);
			Value val = wm.findHistoricalValue(wm.getHistoryLogCopy(), attributeName, rt);
			
			String ds = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS").format(new java.util.Date (val.getTimestamp()));
			System.out.println("now:    " +  new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS").format(new java.util.Date()));
			System.out.println(attributeName + " at "+ ds +" was: " + val);
			
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
		return new ShowHistoryValueOfCmd(cmdStr);
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
	
	
}
