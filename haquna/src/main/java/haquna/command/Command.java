package haquna.command;

public interface Command {
	
	public boolean execute();
		
	public boolean matches(String commandStr);
	
	public Command getNewCommand(String commandStr);
}
