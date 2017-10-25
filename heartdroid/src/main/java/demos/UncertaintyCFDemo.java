package demos; /**
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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import heart.Configuration;
import heart.HeaRT;
import heart.State;
import heart.StateElement;
import heart.alsvfd.Formulae;
import heart.alsvfd.Range;
import heart.alsvfd.SetValue;
import heart.alsvfd.SimpleNumeric;
import heart.alsvfd.SimpleSymbolic;
import heart.alsvfd.Value;
import heart.alsvfd.expressions.ExpressionInterface;
import heart.exceptions.*;
import heart.parser.hmr.HMRParser;
import heart.parser.hmr.runtime.SourceFile;
import heart.uncertainty.CertaintyFactorsEvaluator;
import heart.uncertainty.UncertainTrue;
import heart.xtt.Attribute;
import heart.xtt.Decision;
import heart.xtt.Rule;
import heart.xtt.Table;
import heart.xtt.Type;
import heart.xtt.XTTModel;
import heart.xtt.Type.Builder;


public class UncertaintyCFDemo {
    public static void main(String [] args){
        try {
            XTTModel model = null;
            SourceFile hmr_threat_monitor = new SourceFile("./heartdroid/src/main/resources/poi-recommender.hmr");
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
	  
	  
	  StateElement weatherE = new StateElement();
      StateElement activityE = new StateElement();
      StateElement userProfileE = new StateElement();
      
      weatherE.setAttributeName("weather");
      Value rainy = new SimpleSymbolic("rainy", null, 0.6f);
      weatherE.setValue(rainy);
      
      userProfileE.setAttributeName("user_profile");
      SetValue profile = new SetValue();
      Value[] prifleElements  = {
    		  new SimpleSymbolic("eating",null,0.6f),
    		  new SimpleSymbolic("culture",null,0.2f),
    		  new SimpleSymbolic("entertainment", null, 0.8f),
    		  new SimpleSymbolic("sightseeing",null,0.2f)
    		  };
      profile.setValues(Arrays.asList(prifleElements));
      userProfileE.setValue(profile);
      
      activityE.setAttributeName("activity");
      SimpleSymbolic walking = new SimpleSymbolic("walking",null,0.8f);
      activityE.setValue(walking);
      
      
      
      State XTTstate = new State();
      XTTstate.addStateElement(activityE);
      XTTstate.addStateElement(weatherE);
      XTTstate.addStateElement(userProfileE);
      
	  
	  System.out.println("Printing current state");
	  State current = HeaRT.getWm().getCurrentState(model);
	  for(StateElement se : current){
		  System.out.println("Attribute "+se.getAttributeName()+" = "+se.getValue());
	  }

      try{
      	HeaRT.fixedOrderInference(model, new String[]{"Recommendations"},  
      			new Configuration.Builder()
      			.setUte(new CertaintyFactorsEvaluator())
      			.setInitialState(XTTstate)
      			.build());
    
      }catch(UnsupportedOperationException e){
      	e.printStackTrace();
      } catch (AttributeNotRegisteredException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  

	  System.out.println("Printing current state (after inference");
	  current = HeaRT.getWm().getCurrentState(model);
	  for(StateElement se : current){
		  System.out.println("Attribute "+se.getAttributeName()+" = "+se.getValue());
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
		} catch (ModelBuildingException e) {
            e.printStackTrace();
        } catch (ParsingSyntaxException e) {
            e.printStackTrace();
        }
    }
	

}
