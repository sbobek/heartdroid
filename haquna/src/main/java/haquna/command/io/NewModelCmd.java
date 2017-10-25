package haquna.command.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

import haquna.Haquna;
import haquna.HaqunaException;
import haquna.command.Command;
import haquna.utils.HaqunaUtils;
import heart.exceptions.ModelBuildingException;
import heart.exceptions.ParsingSyntaxException;
import heart.parser.hmr.HMRParser;
import heart.parser.hmr.runtime.SourceFile;
import heart.xtt.XTTModel;

public class NewModelCmd implements Command {		
	
	public static final String pattern = "^" + Haquna.varName + "(\\s*)=(\\s*)new(\\s*)Model[(]['](.*)['][)](\\s*)";
	
	private String commandStr;
	private String varName;
	private String modelPath;
	private String path;
	
	public NewModelCmd() {
		
	}
	
 	public NewModelCmd(String _commandStr) {
		this.commandStr = _commandStr.replace(" ", "");
		String[] commandParts = this.commandStr.split("[(|)|=]");		
		this.varName = commandParts[0];		
		this.modelPath = commandParts[2].substring(1, commandParts[2].length()-1);;		
	}
	
	@Override
	public boolean execute() {						
		try {	
			setupPath();
			HaqunaUtils.checkVarName(varName);
			
			XTTModel model = parseHMR();
			
			Haquna.clearIfVarIsUsed(varName);
			Haquna.modelMap.put(varName, model);
				
			return true;
			
		} catch(HaqunaException e) {
			HaqunaUtils.printRed(e.getMessage());
			//e.printStackTrace();
			return false;
		
		} catch (Exception e) {
			HaqunaUtils.printRed(e.getMessage());
			//e.printStackTrace();
			
			return false;

		} finally {
			File f = new File("umlHMRFile.hmr");
			if (f.exists()) {
				f.delete();
			}
	}


	}		
	
	public boolean matches(String commandStr) {
		return commandStr.matches(pattern);
	}
	
	public Command getNewCommand(String cmdStr) {
		return new NewModelCmd(cmdStr);
	}
	
	public String getCommandStr() {
		return commandStr;
	}

	public String getVarName() {
		return varName;
	}

	public String getModelPath() {
		return modelPath;
	}

	private void setupPath() throws IOException {
		URL website = null;
		try {
			website = new URL(modelPath);
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos = new FileOutputStream("umlHMRFile.hmr");
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);			
			closeQuietly(fos);
			
			this.path = "umlHMRFile.hmr";
		
		} catch (MalformedURLException e) {

			Path p = Paths.get(modelPath);
			if(!p.isAbsolute()) {
				this.modelPath = System.getProperty("user.dir") + "/" + this.modelPath;
			}
			
			path = modelPath;
			return;
		}				
	}
	
	private XTTModel parseHMR() throws HaqunaException {
		try {
			XTTModel model = null;
			SourceFile hmr = new SourceFile(path);
			HMRParser parser = new HMRParser();
		
		    parser.parse(hmr);
		    model = parser.getModel();

		    return model;
	   	} catch(ParsingSyntaxException | ModelBuildingException e) {
			throw new HaqunaException("ParsingSynatxException parsing '" + path + "'");

		}
	}
	
	private void closeQuietly(FileOutputStream out) {
	    try { out.flush(); out.close(); } catch(Exception e) {} 
	}
	
}
