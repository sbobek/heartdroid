package haquna;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

public class HaqunaScript {
	private List<String> scriptCommands;
	
	public HaqunaScript(String filePath) {
		scriptCommands = new LinkedList<String>();
		String line;
		try (
		    InputStream fis = new FileInputStream(filePath);
		    InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
		    BufferedReader br = new BufferedReader(isr);
		) {
		    while ((line = br.readLine()) != null) {
		    	if(!line.isEmpty() && !line.matches("[%].*")) {
		    		scriptCommands.add(line);
		    	}
		    	
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
	}
	
	public List<String> getScriptCommands() {
		return this.scriptCommands;
	}

	public void executeScriptCmds(Haquna haquna) {
		List<String> scriptCmds = getScriptCommands();

		if(scriptCmds != null) {
			for(String cmd : scriptCmds) {
				if(!haquna.executeCmd(cmd)) {
					haquna.executeCmd("clearMemory()");
					break;
				}
			}
		}
	}
}
