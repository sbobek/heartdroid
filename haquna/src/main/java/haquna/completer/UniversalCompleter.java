package haquna.completer;

import java.util.List;

import haquna.HaqunaMain;

public class UniversalCompleter extends CompleterAbstract{

	@Override
	public int complete(String buffer, int cursor, List<CharSequence> candidates) {

        return 0;
	}

	@Override
	protected void setupStringCandidates() {
		HaqunaMain.log(0, this.getClass().getName(), "setupStringCandidates()", "argPos = " + argPos);
	}

}
