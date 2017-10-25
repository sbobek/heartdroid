package haquna.command.io;

import haquna.Haquna;
import haquna.HaqunaScript;
import haquna.command.Command;
import haquna.utils.HaqunaUtils;

public class LoadCmd implements Command {

    public static final String pattern = "^(\\s*)" + "load[(]['](.*)['][)](\\s*)";

    private String commandStr;
    private String scriptPath;

    public LoadCmd() {

    }

    public LoadCmd(String _commandStr) {
        this.commandStr = _commandStr.replace(" ", "");

        String[] commandParts = this.commandStr.split("'");
        this.scriptPath = commandParts[1];
    }

    @Override
    public boolean execute() {
        try {
            HaqunaScript hs = new HaqunaScript(scriptPath);
            hs.executeScriptCmds(Haquna.getInstance());

            return true;

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
        return new LoadCmd(cmdStr);
    }

    public String getCommandStr() {
        return commandStr;
    }

    public String getCsriptPath() {
        return scriptPath;
    }
}