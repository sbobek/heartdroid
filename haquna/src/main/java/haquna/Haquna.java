package haquna;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import haquna.command.Command;
import haquna.command.CommandFactory;
import haquna.completer.CompleterMenager;
import heart.WorkingMemory;
import heart.xtt.Attribute;
import heart.xtt.Rule;
import heart.xtt.Table;
import heart.xtt.Type;
import heart.xtt.XTTModel;
import jline.console.ConsoleReader;

public class Haquna {
	public static final String varName = "[a-zA-Z_$]([a-zA-Z_$0-9])*";
	public static final String attrNamePattern = "[a-z|_]+";
	public static final String attrValuePattern = "[a-z|A-Z|0-9|_|.\\]\\[]+[/]?[0-9]*";
			
	public static Map<String, XTTModel> modelMap = new HashMap<String, XTTModel>();
	public static Map<String, Table> tableMap = new HashMap<String, Table>();
	public static Map<String, Attribute> attrMap = new HashMap<String, Attribute>();
	public static Map<String, Type> typeMap = new HashMap<String, Type>();
	public static Map<String, Rule> ruleMap = new HashMap<String, Rule>();
	public static Map<String, String> callbackMap = new HashMap<String, String>();
	public static Map<String, WorkingMemory> wmMap = new HashMap<String, WorkingMemory>();
	
	public static Map<String, Type.Builder> typeBuilderMap = new HashMap<String, Type.Builder>();
	public static Map<String, Attribute.Builder> attrBuilderMap = new HashMap<String, Attribute.Builder>();
	public static Map<String, Table.Builder> tableBuilderMap = new HashMap<String, Table.Builder>();
	public static Map<String, Rule.Builder> ruleBuilderMap = new HashMap<String, Rule.Builder>();
	
		
	/////////////////////////////
	private static Haquna singleton = new Haquna();
	
	private ConsoleReader reader;
	private CommandFactory cmdFactory;
	private CompleterMenager complMenager;
	private PrintWriter out;
		
	private final String prompt = "\u001B[35;1m" + "HaQuNa> " + "\u001B[0m";
	
	private Haquna() {
		try {
			reader = new ConsoleReader();
			cmdFactory = new CommandFactory();
	        complMenager = new CompleterMenager();
	        out = new PrintWriter(reader.getOutput());
	        
	        reader.setPrompt(prompt);
	        complMenager.setupCompleter(reader);
		
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}       
	}
	
	public static Haquna getInstance() {
		return singleton;
	}
	
	public boolean executeCmd(String cmdStr) {
		Command cmd = cmdFactory.createCommand(cmdStr);
		
		boolean result = false;
		if(cmd != null) result = cmd.execute();
		
		if(result == true) {
			printGreen(cmdStr);
		} else {
			printRed(cmdStr);
		}
		out.flush();
				
		return result;
	}
	
	public void loop() throws IOException {
		String line;
		while ((line = reader.readLine()) != null) {          	           	                            
            executeCmd(line);
			
	        if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit")) {
	            break;
	        }
	        
	        if (line.equalsIgnoreCase("cls")) {
	        	reader.clearScreen();
	    	}
        }
	}

	public static boolean isVarUsed(String varName) {
		if(modelMap.containsKey(varName) ||
		   tableMap.containsKey(varName) ||
		   attrMap.containsKey(varName) ||
		   typeMap.containsKey(varName) ||
		   ruleMap.containsKey(varName) ||
		   callbackMap.containsKey(varName) ||
		   wmMap.containsKey(varName) ||
		   tableBuilderMap.containsKey(varName) ||
		   attrBuilderMap.containsKey(varName) ||
		   typeBuilderMap.containsKey(varName) ||
		   ruleBuilderMap.containsKey(varName)) {

			return true;

		} else {
			return false;
		}
	}
	
	public static void clearIfVarIsUsed(String varName) {
		if(modelMap.containsKey(varName)) {
			modelMap.remove(varName);
		}
		
		if(tableMap.containsKey(varName)){
			tableMap.remove(varName);
		}
		
		if(attrMap.containsKey(varName)) {
			attrMap.remove(varName);
		}
		
		if(typeMap.containsKey(varName)) {
			typeMap.remove(varName);
		}
		
		if(ruleMap.containsKey(varName)) {
			ruleMap.remove(varName);
		}
		
		if(callbackMap.containsKey(varName)) {
			callbackMap.remove(varName);
		}
		
		if(wmMap.containsKey(varName)) {
			tableBuilderMap.remove(varName);
		}		
		
		if(attrBuilderMap.containsKey(varName)) {
			attrBuilderMap.remove(varName);
		}
		
		if(typeBuilderMap.containsKey(varName)) {
			typeBuilderMap.remove(varName);
		}
		
		if(ruleBuilderMap.containsKey(varName)) {
			ruleBuilderMap.remove(varName)	;		
		}
	}
	
	private void printRed(String line) {
		out.println("\u001B[31m======>" + line + "\u001B[0m");
	}
	
	private void printGreen(String line) {
		out.println("\u001B[32m======>" + line + "\u001B[0m");
	}	
}
