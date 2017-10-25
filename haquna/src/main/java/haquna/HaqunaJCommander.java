package haquna;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.internal.Lists;

import java.util.ArrayList;
import java.util.List;

public class HaqunaJCommander {

    public static class JCommanderConfiguration {
        @Parameter
        public List<String> parameters = Lists.newArrayList();

        @Parameter(
                names = {"--model"},
                description = "path to the HMR file",
                required = true

        )
        private String modelPath;

        @Parameter(
                names = {"--tables"},
                description = " Order of tables used in the inference - comma separated"
        )
        private String tables = null;

        @Parameter(
                names = {"--inference"},
                description = "ddi|gdi|foi #optional, default = ddi"
        )
        private String inference = "ddi";

        @Parameter(
                names = {"--initial-state"},
                description = "initial-state [att=val,att=val] #optional - comma separated"
        )
        private String attributes = null;

        @Parameter(
                names = {"--conflict-resolution"},
                description = "conflict-resolution  all|last|first] # optional, default first"
        )
        private String conflictStrategy = "first";

        @Parameter(
                names = {"--uncertainty"},
                description = "uncertainty on|off # optional, default on"
        )
        private String uncertainty = "on";

        @Parameter(
                names = {"--tokens"}, 
                description = "tokens on|off # optional, default off"
        )
        private String tokens = "off";

        @Parameter(
                names = {"--help"},
                help = true,
                description = "display help (this message)"
        )
        private boolean help;

        @Parameter(
                names = {"--console"},
                help = true,
                description = "run "
        )
        private String scriptPath;

        public JCommanderConfiguration() {
        }
    }

    public static void commandline(String[] args, Haquna haquna) {
        JCommanderConfiguration jcc = new HaqunaJCommander.JCommanderConfiguration();
        JCommander jcomm = new JCommander(jcc, args);

        ArrayList<String> cmds = new ArrayList<>();

        if(jcc.help) {
            HaqunaMain.startConsole = false;
            jcomm.usage();

        } else if(jcc.scriptPath != null) {
            HaqunaMain.startConsole = true;
            HaqunaScript hs = new HaqunaScript(jcc.scriptPath);
            hs.executeScriptCmds(haquna);

        } else {
                String cmd;
                cmd = "Mod = new Model('" + jcc.modelPath + "')";
                cmds.add(cmd);

                cmd ="Wm = new WorkingMemory(Mod)";
                cmds.add(cmd);

                HaqunaMain.startConsole = false;
                if(jcc.attributes != null) {
                    String attrs = jcc.attributes;
                    attrs = attrs.replace("[", "");
                    attrs = attrs.replace("]", "");
                    attrs = attrs.replace(" ", "");

                    String[] splited = attrs.split("[=|,]");
                    for(int i = 0; i < splited.length; i=i+2 ) {
                        String argName = splited[i];
                        String argVal = splited[i+1];

                        cmd = "Wm.setValueOf('" + argName + "','" + argVal + "')";
                        cmds.add(cmd);
                    }
                }

                String tabs = jcc.tables != null ? ("tables="+jcc.tables) : "";
                tabs = tabs.replace("[", "['");
                tabs = tabs.replace("]", "']");
                tabs = tabs.replace(",", "','");

                //String token = jcc.tokens.equals("off") ? "false" : "true";
                //String uncertainty = jcc.uncertainty.equals("off") ? "false" : "true";

                cmd = "Mod.run(Wm, " +
                        "inference=" + jcc.inference + ", " +
                        "conflict_resolution=" + jcc.conflictStrategy+ ", " +
                        "tokens=" + jcc.tokens + ", " +
                        "uncertainty=" + jcc.uncertainty + ", " +
                        tabs + ")";
                cmds.add(cmd);

                cmd = "Wm.showCurrentState()";
                cmds.add(cmd);


                for(String c : cmds) {
                    if(!haquna.executeCmd(c)) {
                        break;
                    }
                }

            }

    }
}
