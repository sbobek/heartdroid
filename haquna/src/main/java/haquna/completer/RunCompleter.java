package haquna.completer;

import java.util.Arrays;
import java.util.List;

import haquna.Haquna;
import heart.xtt.Table;

public class RunCompleter extends CompleterAbstract {

	@Override
	protected void setupStringCandidates() {
		stringCandidates.clear();
				   
		if(argPos == 2) {
			stringCandidates.addAll(Haquna.wmMap.keySet());
		}

		if(argPos == 1 && CompleterMenager.getLastDelimiter(arguments,buff,argPos) == '.') {
			stringCandidates.add("run");
		}

		if(argPos > 2) {
			String prevArg = arguments[argPos - 1];
			if (prevArg.equals("inference")) {
				stringCandidates.add("gdi");
				stringCandidates.add("foi");
				stringCandidates.add("ddi");
			} else if (prevArg.equals("tokens")) {
				stringCandidates.add("on");
				stringCandidates.add("off");
			} else if (prevArg.equals("uncertainty")) {
				stringCandidates.add("on");
				stringCandidates.add("off");
			} else if (prevArg.equals("conflict_resolution")) {
				stringCandidates.add("first");
				stringCandidates.add("last");
				stringCandidates.add("all");
			}
			else {
				if(buff.contains("[") && CompleterMenager.getLastDelimiter(arguments,buff,argPos) == '\''
						&& !buff.contains("]")) {
					for (Table t : Haquna.modelMap.get(arguments[0]).getTables()) {
						if (!Arrays.asList(arguments).contains(t.getName())) {
							stringCandidates.add(t.getName());
						}
					}
				} else {
					List<String> args = Arrays.asList(arguments);
					if (!args.contains("inference")) stringCandidates.add("inference");
					if (!args.contains("tokens")) stringCandidates.add("tokens");
					if (!args.contains("uncertainty")) stringCandidates.add("uncertainty");
					if (!args.contains("conflict_resolution")) stringCandidates.add("conflict_resolution");
					if (!args.contains("tables")) stringCandidates.add("tables");               
				}


			}
		}
		
	}

}
