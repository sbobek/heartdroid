package haquna.utils;

import haquna.Haquna;
import haquna.HaqunaException;
import heart.WorkingMemory;
import heart.xtt.Attribute;
import heart.xtt.Table;
import heart.xtt.XTTModel;

public class HaqunaUtils {
	public static XTTModel getModel(String name) throws HaqunaException {
		if(Haquna.modelMap.containsKey(name)) {
			return Haquna.modelMap.get(name);
		
		} else {
			throw new HaqunaException("No '" + name + "' XTTModel object in memory");
		}
	}
	
	public static Attribute getAttribute(String name) throws HaqunaException {
		if(Haquna.attrMap.containsKey(name)) {
			return Haquna.attrMap.get(name);
		
		} else {
			throw new HaqunaException("No " + name + " attribute in memory");
		}
	}
	
	public static Table getTable(String name) throws HaqunaException {
		if(Haquna.tableMap.containsKey(name)) {
			return Haquna.tableMap.get(name);
		
		} else {
			throw new HaqunaException("No '" + name + "' Table object in memory");
		}
	}
	
	public static WorkingMemory getWorkingMemory(String name) throws HaqunaException {
		if(Haquna.wmMap.containsKey(name)) {
			return Haquna.wmMap.get(name);
		
		} else {
			throw new HaqunaException("No '" + name + "' WorkingMemory object in memory");
		}
	}
	
	public static void checkVarName(String varName) throws HaqunaException {
		if(Haquna.isVarUsed(varName)) {
			//Haquna.clearIfVarIsUsed(varName);
			System.out.println("Overriding variable name '" + varName + "'");
			//throw new HaqunaException("Variable name '" + varName + "' already in use");
		}
	}
	
	public static void printGreen(String line) {
		System.out.println("\u001B[32m======>" + line + "\u001B[0m");
	}
	
	public static void printRed(String line) {
		System.out.println("\u001B[31m======>" + line + "\u001B[0m");
	}

	public static void clearMemory() {
		Haquna.modelMap.clear();
		Haquna.wmMap.clear();
		Haquna.attrMap.clear();
		Haquna.tableMap.clear();
		Haquna.ruleMap.clear();
		Haquna.typeMap.clear();
		Haquna.callbackMap.clear();
		Haquna.attrBuilderMap.clear();
		Haquna.tableBuilderMap.clear();
		Haquna.ruleBuilderMap.clear();
		Haquna.typeBuilderMap.clear();
	}
}
