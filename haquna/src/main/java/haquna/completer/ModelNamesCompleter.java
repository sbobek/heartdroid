package haquna.completer;

import haquna.Haquna;

public class ModelNamesCompleter extends CompleterAbstract {

	@Override
	protected void setupStringCandidates() {
		stringCandidates.clear();

		if(argPos > 0) {
			if(CompleterMenager.getLastDelimiter(arguments,buff,argPos) == '=') {
				stringCandidates.addAll(Haquna.modelMap.keySet());
			}

		} else {
			stringCandidates.addAll(Haquna.modelMap.keySet());
		}
	}

}
