package demos;

import heart.*;
import heart.alsvfd.Formulae;
import heart.alsvfd.SimpleNumeric;
import heart.alsvfd.SimpleSymbolic;
import heart.alsvfd.Value;
import heart.alsvfd.expressions.ExpressionInterface;
import heart.exceptions.*;
import heart.parser.hmr.HMRParser;
import heart.parser.hmr.runtime.SourceFile;
import heart.uncertainty.ConflictSetFireAll;
import heart.xtt.*;

import java.util.LinkedList;

/**
 * Created by sbk on 25.10.16.
 */
public class CallbackDemo {

    public static void main(String [] args){
        try {
            //Loading a file with a model
            XTTModel model = null;
            SourceFile hmr_threat_monitor = new SourceFile("./heartdroid/src/main/resources/threat-monitor-callbacks.hmr");
            HMRParser parser = new HMRParser();

            //Parsing the file with the model
            parser.parse(hmr_threat_monitor);
            model = parser.getModel();




            // Creating StateElements objects, one for each attribute
            //StateElement hourE = new StateElement();
            //StateElement dayE = new StateElement();
            StateElement locationE = new StateElement();
            StateElement activityE = new StateElement();

            // Setting the values of the state elements
            //hourE.setAttributeName("hour");
            //hourE.setValue(new SimpleNumeric(16d));
            //dayE.setAttributeName("day");
            //dayE.setValue(new SimpleSymbolic("mon",1));

            locationE.setAttributeName("location");
            locationE.setValue(new SimpleSymbolic("work"));

            activityE.setAttributeName("activity");
            activityE.setValue(new SimpleSymbolic("idle"));

            //Creating a XTTState object that agregates all the StateElements
            State XTTstate = new State();
            //XTTstate.addStateElement(hourE);
            //XTTstate.addStateElement(dayE);
            XTTstate.addStateElement(locationE);
            XTTstate.addStateElement(activityE);




            try{
                Debug.debugLevel = Debug.Level.SILENT;
                HeaRT.fixedOrderInference(model, new String[]{"DayTime", "Today", "Actions", "Threats"},
                        new Configuration.Builder().setCsr(new ConflictSetFireAll())
                                .setInitialState(XTTstate)
                                .build());



                System.out.println("Printing current state (after inference FOI)");
                State current = HeaRT.getWm().getCurrentState(model);
                for(StateElement se : current){
                    System.out.println("Attribute "+se.getAttributeName()+" = "+se.getValue());
                }

                System.out.println("\n\n");

                HeaRT.dataDrivenInference(model, new String[]{"DayTime", "Today"},
                        new Configuration.Builder().setCsr(new ConflictSetFireAll())
                                .setInitialState(XTTstate)
                                .build());

                System.out.println("Printing current state (after inference DDI)");
                current = HeaRT.getWm().getCurrentState(model);
                for(StateElement se : current){
                    System.out.println("Attribute "+se.getAttributeName()+" = "+se.getValue());
                }

                System.out.println("\n\n");

                HeaRT.goalDrivenInference(model, new String[]{"Threats"},
                        new Configuration.Builder().setCsr(new ConflictSetFireAll())
                                .setInitialState(XTTstate)
                                .build());

                System.out.println("Printing current state (after inference GDI)");
                current = HeaRT.getWm().getCurrentState(model);
                for(StateElement se : current){
                    System.out.println("Attribute "+se.getAttributeName()+" = "+se.getValue());
                }

                System.out.println("\n\n");


            }catch(UnsupportedOperationException e){
                e.printStackTrace();
            } catch (AttributeNotRegisteredException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        } catch (BuilderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedOperationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NotInTheDomainException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (ModelBuildingException e) {
            e.printStackTrace();
        } catch (ParsingSyntaxException e) {
            e.printStackTrace();
        }
    }
}
