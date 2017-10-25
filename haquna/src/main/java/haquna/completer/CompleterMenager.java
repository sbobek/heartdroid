package haquna.completer;

import java.util.LinkedList;

import jline.console.ConsoleReader;
import jline.console.completer.AggregateCompleter;
import jline.console.completer.CandidateListCompletionHandler;
import jline.console.completer.Completer;
import jline.console.completer.FileNameCompleter;
import jline.console.completer.StringsCompleter;

public class CompleterMenager {
	
	public void setupCompleter(ConsoleReader reader) {
		LinkedList<Completer> completers = new LinkedList<Completer>();
		
		for(Completer c : reader.getCompleters()){
			reader.removeCompleter(c);
		}

        completers = new LinkedList<Completer>();
        completers.add(new StringsCompleter("ls", "pwd", "printVars()", "cls", "clearMemory()"));
        HaqunaCompleter argComp8 = new HaqunaCompleter(new HaqunaDelimiter(), completers);

        // xload('script.hqn')
        completers = new LinkedList<Completer>();
        completers.add(new StringsCompleter("load"));
        completers.add(new FileNameCompleter());
        HaqunaCompleter argComp9 = new HaqunaCompleter(new HaqunaDelimiter('\'', ',', '=', '.', '(', '[', ']'), completers);

		// Model.showTableList()
		completers = new LinkedList<Completer>();
        completers.add(new VarNamesCompleter());
        completers.add(new FunctionNamesCompleter());
		HaqunaCompleter argComp1 = new HaqunaCompleter(new HaqunaDelimiter('\'', ',', '=', '.', '(', '[', ']'), completers);
        
		// Type = Attr.getType()
		completers = new LinkedList<Completer>();
        completers.add(new UniversalCompleter());
        completers.add(new VarNamesCompleter());
        completers.add(new FunctionNamesCompleter());
        HaqunaCompleter argComp2 = new HaqunaCompleter(new HaqunaDelimiter('\'', ',', '=', '.', '(', '[', ']'), completers);
        
    	// Attr = Model.getAttributeByName('day')
        completers = new LinkedList<Completer>();
        completers.add(new UniversalCompleter());
        completers.add(new VarNamesCompleter());
        completers.add(new FunctionNamesCompleter());
        completers.add(new ParametersCompleter());
        HaqunaCompleter argComp3 = new HaqunaCompleter(new HaqunaDelimiter('\'', ',', '=', '.', '(', '[', ']'), completers);
        
        // Model = xload('file.hmr')
        completers = new LinkedList<Completer>();
        completers.add(new UniversalCompleter());
        completers.add(new NewCompleter());
        completers.add(new StringsCompleter("Model"));
        completers.add(new FileNameCompleter());
        HaqunaCompleter argComp4 = new HaqunaCompleter(new HaqunaDelimiter('\'', ',', '=', '.', '(', '[', ']'), completers);
       
        // Wm = new WorkingMemory(Model) 
        completers = new LinkedList<Completer>();
        completers.add(new UniversalCompleter());
        completers.add(new NewCompleter());
        completers.add(new FunctionNamesCompleter());
        completers.add(new ParametersCompleter());       
        HaqunaCompleter argComp5 = new HaqunaCompleter(new HaqunaDelimiter('\'', ',', '=', '.', '(', '[', ']'), completers);
        
        // Wm.setValueOf('attr')
        completers = new LinkedList<Completer>();
        completers.add(new VarNamesCompleter());
        completers.add(new FunctionNamesCompleter());
        completers.add(new ParametersCompleter());         
		HaqunaCompleter argComp6 = new HaqunaCompleter(new HaqunaDelimiter('\'', ',', '=', '.', '(', '[', ']'), completers);

        completers = new LinkedList<Completer>();
        completers.add(new ModelNamesCompleter());
        completers.add(new RunCompleter());
        for(int i = 0; i < 15; i++) {
            completers.add(new RunCompleter());
        }
        HaqunaCompleter argComp7 = new HaqunaCompleter(new HaqunaDelimiter('\'', ',', '=', '.', '(', '[', ']'), completers);

        completers = new LinkedList<Completer>();
        completers.add(new UniversalCompleter());
        completers.add(new ModelNamesCompleter());
        completers.add(new RunCompleter2());
        for(int i = 0; i < 15; i++) {
            completers.add(new RunCompleter2());
        }
        HaqunaCompleter argComp10 = new HaqunaCompleter(new HaqunaDelimiter('\'', ',', '=', '.', '(', '[', ']'), completers);

        argComp1.setStrict(true);
        argComp2.setStrict(true);
        argComp3.setStrict(true);
        argComp4.setStrict(true);
        argComp5.setStrict(true);
        argComp6.setStrict(true);
        argComp7.setStrict(true);
        argComp8.setStrict(true);
        argComp9.setStrict(true);
        argComp10.setStrict(true);

        CandidateListCompletionHandler handler = new CandidateListCompletionHandler();        
        handler.setPrintSpaceAfterFullCompletion(false);
        
        AggregateCompleter aggComp = new AggregateCompleter(argComp1, 
        								argComp2,
                                        argComp3,
                                        argComp4,
        								argComp5,
                                        argComp6,
                                        argComp7,
        								argComp8,
                                        argComp9,
                                        argComp10);
        reader.setCompletionHandler(handler);
        reader.addCompleter(aggComp);
	}

	public static char getLastDelimiter(String[] arguments, String buff, int argPos) {
        /*int index = 0;
        for(int i = 0; i < argPos; i++) {
            index += arguments[i].length();
            if(buff.charAt(index) == ' ' ||
                    (buff.length() < index+1 && buff.charAt(index+1) == '=') ||
                    (buff.length() < index+1 && buff.charAt(index+1) == '.')) {
                index++;
            }
        }
        return buff.charAt(index);*/
        int last = 0;
        if(buff.lastIndexOf(".") != -1) {
            last = buff.lastIndexOf(".");
        }

        if(buff.lastIndexOf("=") != -1) {
           if(buff.lastIndexOf("=") > last) {
               last = buff.lastIndexOf("=");
           }
        }

        if(buff.lastIndexOf("'") != -1) {
            if(buff.lastIndexOf("'") > last) {
                last = buff.lastIndexOf("'");
            }
        }

        if(buff.lastIndexOf("(") != -1) {
            if(buff.lastIndexOf("(") > last) {
                last = buff.lastIndexOf("(");
            }
        }

        return buff.charAt(last);
    }
}
