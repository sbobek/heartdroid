package demos;    /**
*
*     Copyright 2013-15 by Szymon Bobek, Grzegorz J. Nalepa, Mateusz Ślażyński
*
*
*     This file is part of HeaRTDroid.
*     HeaRTDroid is a rule engine that is based on HeaRT inference engine,
*     XTT2 representation and other concepts developed within the HeKatE project .
*
*     HeaRTDroid is free software: you can redistribute it and/or modify
*     it under the terms of the GNU General Public License as published by
*     the Free Software Foundation, either version 3 of the License, or
*     (at your option) any later version.
*
*     HeaRTDroid is distributed in the hope that it will be useful,
*     but WITHOUT ANY WARRANTY; without even the implied warranty of
*     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*     GNU General Public License for more details.
*
*     You should have received a copy of the GNU General Public License
*     along with HeaRTDroid.  If not, see <http://www.gnu.org/licenses/>.
*
**/



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import heart.Configuration;
import heart.HeaRT;
import heart.State;
import heart.StateElement;
import heart.alsvfd.Formulae;
import heart.alsvfd.SetValue;
import heart.alsvfd.SimpleNumeric;
import heart.alsvfd.SimpleSymbolic;
import heart.alsvfd.Value;
import heart.alsvfd.expressions.ExpressionInterface;
import heart.exceptions.AttributeNotRegisteredException;
import heart.exceptions.BuilderException;
import heart.exceptions.ModelBuildingException;
import heart.exceptions.NotInTheDomainException;
import heart.exceptions.ParsingSyntaxException;
import heart.exceptions.RangeFormatException;
import heart.parser.hmr.HMRParser;
import heart.parser.hmr.runtime.SourceFile;
import heart.uncertainty.ConflictSetFireAll;
import heart.xtt.Attribute;
import heart.xtt.Decision;
import heart.xtt.Rule;
import heart.xtt.Table;
import heart.xtt.Type;
import heart.xtt.XTTModel;

public class ComplexExpressionsDemo {


	public static void main(String [] args){
		try {
			XTTModel model = null;
            SourceFile hmr_threat_monitor = new SourceFile("./heartdroid/src/main/resources/ploc-xtt.hmr");
            HMRParser parser = new HMRParser();
            parser.parse(hmr_threat_monitor);
            model = parser.getModel();


		LinkedList<Type> types = model.getTypes();
        Collections.sort(types, new Comparator<Type>() {
				public int compare(Type t1, Type t2) {
					return Collator.getInstance().compare(t1.getName(), t2.getName());
				}
			});

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
		Collections.sort(atts, new Comparator<Attribute>() {
				public int compare(Attribute a1, Attribute a2) {
					return Collator.getInstance().compare(a1.getName(), a2.getName());
				}
			});
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
					  System.out.print("\t"+d.getAttribute().getName()+"is set to ");
					  
					  ExpressionInterface e = d.getDecision();
					  System.out.print(e);
				  }
				  System.out.println();
				  
			  }
			  System.out.println();
			  System.out.println("=============================");
			  
			  
		  }

		  // Sample state for the inference.
		  // Resulting value after inference should be: 
		  /*
		  xstat input/1: [insuranceCars,1].
		  xstat input/1: [driverClass,1].
		  xstat input/1: [insuranceContinue,1].
		  xstat input/1: [driverLicage,2].
		  xstat input/1: [carAccidents,0].
		  xstat input/1: [carTechnical,1].
		  xstat input/1: [carSeats,5].
		  xstat input/1: [driverAge,29].
		  xstat input/1: [carHistoric,0].
		  xstat input/1: [insuranceCertificate,1].
		  xstat input/1: [insurancePayment,single].
		  xstat input/1: [carAge,12].
		  xstat input/1: [carCapacity,997].
		  xstat input/1: [insuranceOtherins,0].
		  */
						  
		  State current  = new State();
		  StateElement s1 = new StateElement("insuranceCars", new SimpleNumeric(1.0));
		  StateElement s2 = new StateElement("driverClass", new SimpleNumeric(1.0));
		  StateElement s3 = new StateElement("insuranceContinue", new SimpleNumeric(1.0));
		  StateElement s4 = new StateElement("driverLicage", new SimpleNumeric(2.0));
		  StateElement s13 = new StateElement("carAccidents", new SimpleNumeric(0.0));
		  StateElement s5 = new StateElement("carTechnical", new SimpleNumeric(1.0));
		  StateElement s6 = new StateElement("carSeats", new SimpleNumeric(5.0));
		  StateElement s14 = new StateElement("driverAge", new SimpleNumeric(29.0));
		  StateElement s7 = new StateElement("carHistoric", new SimpleNumeric(0.0));
		  StateElement s8 = new StateElement("insuranceCertificate", new SimpleNumeric(1.0));
		  StateElement s9 = new StateElement("insurancePayment", new SimpleSymbolic("single"));
		  StateElement s10 = new StateElement("carAge", new SimpleNumeric(12.0));
		  StateElement s11 = new StateElement("carCapacity", new SimpleNumeric(997.0));
		  StateElement s12 = new StateElement("insuranceOtherins", new SimpleNumeric(0.0));
		  
		  current.addStateElement(s1);
		  current.addStateElement(s2);
		  current.addStateElement(s3);
		  current.addStateElement(s4);
		  current.addStateElement(s5);
		  current.addStateElement(s6);
		  current.addStateElement(s7);
		  current.addStateElement(s8);
		  current.addStateElement(s9);
		  current.addStateElement(s10);
		  current.addStateElement(s11);
		  current.addStateElement(s12);
		  current.addStateElement(s13);
		  current.addStateElement(s14);

		  System.out.println("Printing current state");
		  State current2 = HeaRT.getWm().getCurrentState(model);
		  for(StateElement se : current2){
			  System.out.println("Attribute "+se.getAttributeName()+" = "+se.getValue());
		  }
		
		try{

			HeaRT.fixedOrderInference(model, new String[]{"bonusMalus","baseCharge","baseChargeModifiers","main"},
					new Configuration.Builder()
					.setCsr(new ConflictSetFireAll()) //fires all rules from cs
					.setInitialState(current)
					.build());


	    } catch(UnsupportedOperationException e){
	      	e.printStackTrace();
	    } catch (AttributeNotRegisteredException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		System.out.println("Printing current state (after inference)");
		 current = HeaRT.getWm().getCurrentState(model);
		  for(StateElement se : current){
			  System.out.println("Attribute "+se.getAttributeName()+" = "+se.getValue());
		  }
		  
		  
	}  catch (BuilderException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (NotInTheDomainException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}  catch (Exception e) {
		// Catch all remaining exception
		e.printStackTrace();
	} 
	}
}
