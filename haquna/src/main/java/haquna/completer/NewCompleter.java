package haquna.completer;

import haquna.Haquna;

public class NewCompleter extends CompleterAbstract {
    @Override
    protected void setupStringCandidates() {
        stringCandidates.clear();

        if(argPos < 1) {
            return;
        }

        String varName = arguments[argPos-1];

        if(argPos == 1 && !Haquna.isVarUsed(varName) && CompleterMenager.getLastDelimiter(arguments,buff,argPos) == '=') {
            stringCandidates.add("new");
        }

    }
}
