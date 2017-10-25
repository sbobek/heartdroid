package demos;

import heart.Configuration;
import heart.HeaRT;
import heart.State;
import heart.StateElement;
import heart.alsvfd.*;
import heart.alsvfd.expressions.ExpressionInterface;
import heart.exceptions.*;
import heart.parser.hmr.HMRParser;
import heart.parser.hmr.runtime.SourceFile;
import heart.uncertainty.CertaintyFactorsEvaluator;
import heart.xtt.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by sbk on 22.03.16.
 */
public class TimeBasedOperatorsDemo {
    public static void main(String [] args){
        try {
            XTTModel model = null;


            SourceFile hmr_threat_monitor = new SourceFile("./heartdroid/src/main/resources/app-recommender-tbo.hmr");
            HMRParser parser = new HMRParser();
            parser.parse(hmr_threat_monitor);
            model = parser.getModel();



            LinkedList<Type> types = model.getTypes();
            for(Type t : types){
                System.out.println("Type id: "+t.getId());
                System.out.println("Type name: "+t.getName());
                System.out.println("Type base: "+t.getBase());
                System.out.println("Type length: "+t.getLength());
                System.out.println("Type scale: "+t.getPrecision());
                System.out.println("desc: "+t.getDescription());

                for(Value v: t.getDomain().getValues()){
                    System.out.println("Value: "+v);
                }
                System.out.println("==========================");
            }

            LinkedList<Attribute> atts = model.getAttributes();
            for(Attribute att: atts){
                System.out.println("Att Id: "+att.getId());
                System.out.println("Att name: "+att.getName());
                System.out.println("Att typeName: "+att.getTypeId());
                System.out.println("Att abbrev: "+att.getAbbreviation());
                System.out.println("Att comm: "+att.getComm());
                System.out.println("Att desc: "+att.getDescription());
                System.out.println("Att class: "+att.getXTTClass());
                System.out.println("==========================");
            }


            LinkedList<Table> tables = model.getTables();
            for(Table t : tables){
                System.out.println("Table id:"+t.getId());
                System.out.println("Table name:"+t.getName());
                LinkedList<heart.xtt.Attribute> cond = t.getPrecondition();
                for(heart.xtt.Attribute a : cond){
                    System.out.println("schm Cond: "+a.getName());
                }
                LinkedList<heart.xtt.Attribute> concl = t.getConclusion();
                for(heart.xtt.Attribute a : concl){
                    System.out.println("schm Conclusion: "+a.getName());
                }

                System.out.println("RULES FOR TABLE "+t.getName());

                for(Rule r : t.getRules()){
                    System.out.print("Rule id: "+r.getId()+ ":\n\tIF ");
                    for(Formulae f : r.getConditions()){
                        System.out.print(f.getLHS()+" "+f.getOp()+" "+f.getRHS()+", ");
                    }

                    System.out.println("THEN ");

                    for(Decision d: r.getDecisions()){
                        System.out.print("\t"+d.getAttribute().getName()+" is set to ");

                        ExpressionInterface e = d.getDecision();
                        System.out.print(e);

                    }
                    System.out.println();

                }
                System.out.println();
                System.out.println("=============================");


            }

            for(int i = 0; i < 100;i++) {
                StateElement locationE = new StateElement();
                StateElement transportationE = new StateElement();
                StateElement hourE = new StateElement();
                StateElement dayE = new StateElement();

                locationE.setAttributeName("location");
                Value location  = null;
                if(i < 20) {
                    double prob = new Random().nextDouble();
                    if(prob<0.95) {
                        location = new SimpleSymbolic("home", null, 0.9f+new Random().nextFloat()*0.1f);//add noise
                    }else{
                        location = new SimpleSymbolic("outside", null, 0.3f+new Random().nextFloat()*0.1f);//add noise
                    }
                }else {
                    location = new SimpleSymbolic("outside", null, 0.85f+new Random().nextFloat()*0.1f); //add noise
                }
                //TODO: set differently, to fulfill 80%
                locationE.setValue(location);



                transportationE.setAttributeName("transportation");
                SimpleSymbolic transportation = null;
                if(i < 20) {
                    transportation = new SimpleSymbolic("idle", null, 0.9f+new Random().nextFloat()*0.1f); //add noise
                }else {
                    transportation = new SimpleSymbolic("walking", null, 0.9f+new Random().nextFloat()*0.1f); //add noise
                }
                transportationE.setValue(transportation);

                hourE.setAttributeName("hour");
                SimpleNumeric time = new SimpleNumeric(7.0+(i/100.0), 1.0f);
                hourE.setValue(time);

                dayE.setAttributeName("day");
                Value monday = new SimpleSymbolic("mon", null);
                dayE.setValue(monday);


                State XTTstate = new State();
                XTTstate.addStateElement(hourE);
                XTTstate.addStateElement(dayE);
                XTTstate.addStateElement(locationE);
                XTTstate.addStateElement(transportationE);


                System.err.println("PRINTING CURRENT STATE (BEFORE INFERENCE)");
                State current = HeaRT.getWm().getCurrentState(model);
                for (StateElement se : current) {
                    System.err.println("Attribute " + se.getAttributeName() + " = " + se.getValue());
                }

                try {
                    HeaRT.fixedOrderInference(model, new String[]{"DayTime", "Today", "Actions", "Applications"},
                            new Configuration.Builder()
                                    .setUte(new CertaintyFactorsEvaluator())
                                    .setInitialState(XTTstate)
                                    .build());

                } catch (UnsupportedOperationException e) {
                    e.printStackTrace();
                } catch (AttributeNotRegisteredException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                System.out.println("PRINTING CURRENT STATE (AFTER INFERENCE)");
                current = HeaRT.getWm().getCurrentState(model);
                for (StateElement se : current) {
                    System.out.println("Attribute " + se.getAttributeName() + " = " + se.getValue() + " #" + se.getValue().getCertaintyFactor());
                }
                System.out.println();
                Thread.sleep(1000);

            }
        }  catch (BuilderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedOperationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NotInTheDomainException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  catch (ModelBuildingException e) {
            e.printStackTrace();
        } catch (ParsingSyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
