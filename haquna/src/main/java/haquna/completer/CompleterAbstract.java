package haquna.completer;

import static jline.internal.Preconditions.checkNotNull;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import haquna.HaqunaMain;
import jline.console.completer.Completer;

public abstract class CompleterAbstract implements Completer {
	protected final SortedSet<String> stringCandidates = new TreeSet<String>();
	protected String[] arguments;
	protected int argPos;
    protected String buff;
	
	public int complete(String buffer, int cursor, List<CharSequence> candidates){					
		setupStringCandidates();
		
		// buffer could be null
        checkNotNull(candidates);
        if (buffer == null) {
            candidates.addAll(stringCandidates);
        
        } else {
            for (String match : stringCandidates.tailSet(buffer)) {
                if (!match.startsWith(buffer)) {
                    break;
                }

                candidates.add(match);
            }
        }

        if(!candidates.isEmpty()){
            HaqunaMain.log(1, this.getClass().getName(), "setupStringCandidates()", "argPos = " + argPos);
        }
        return candidates.isEmpty() ? -1 : 0;
    }	
	
	public void setContext(String[] args, int argP, String buff) {
		this.arguments = args;
		this.argPos = argP;
        this.buff = buff;
	}
	
	abstract protected void setupStringCandidates();
}
