/**
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


package heart.xtt;

import heart.Action;
import heart.Debug;
import heart.Debug.Level;
import heart.State;
import heart.WorkingMemory;
import heart.alsvfd.Formulae;
import heart.exceptions.*;
import heart.uncertainty.UncertainTrue;
import heart.uncertainty.UncertainTrueEvaluator;
import heart.xtt.annotation.Annotated;
import heart.xtt.annotation.AnnotatedBuilder;
import heart.xtt.annotation.Annotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Rule implements Annotated {
	/**
	 * This field is used by the SQL module while storing/restoring model into/from database.
	 * If not provided, this should be null; 
	 */
	protected String id;

	/**
	 * This field contains scheme's name to which the rule belongs. Used mainly in debugging.
	 */
	protected String schemeName;

	/**
	 * This field contains rule's order number. It should uniquely identify the rule in table.
	 * Moreover it's used during infence as a conflict resolver.
	 */
	protected Integer orderNumber;
	
	/**
	 * The certainty factor of a rule. It represents a confidence of the rule.
	 */
	protected float certaintyFactor;
	
	/**
	 * This is a list of rules that the inference engine should proceed to after firing this rule.
	 */
	protected LinkedList<Rule> ruleLinks;
	
	/**
	 * This is a list of rules that the inference engine should proceed to after firing this rule.
	 */
	protected LinkedList<Table> tabLinks;

	/**
	 * A list that contains a list of firing conditions for the rule. 
	 * Each condition has to be valid ALSV(FD) expression defined by the {@link Formulae}.
	 */
	protected LinkedList<Formulae> conditions;
	
	/**
	 * List that contains decisions that has to be fired when conditions are true.  
	 * Decision is defined by the {@link Decision} class.
	 */
	protected LinkedList<Decision> decisions;
	
	/**
	 * List of names for the {@link Action} interface implementations that
	 * should be executed when the rules conditions are true.
	 */
	protected LinkedList<String> actions;
	
	protected List<Annotation> annotations;

	public Rule() {
		conditions = new LinkedList<Formulae>();
		decisions = new LinkedList<Decision>();
		
		ruleLinks = new LinkedList<Rule>();
		tabLinks = new LinkedList<Table>();
		
		actions = new LinkedList<String>();
		annotations = new LinkedList<Annotation>();
		setCertaintyFactor(1.0f);
		
	}
	
	/**
	 * Method that evaluates all the rules conditions. 
	 * It does not execute the decision part of the rule, it only evaluates the conditional part.
	 * 
	 * @param wm a working memory object that contains information about attributes values
	 * @return true if all the rule's conditions are true, false otherwise
	 * @throws NotInTheDomainException
	 */
	public UncertainTrue evaluate(WorkingMemory wm, UncertainTrueEvaluator ute) throws NotInTheDomainException, UnknownValueException {
		Debug.debug(Debug.heartTag, Level.CONDITIONS, "Checking conditions of rule " + this.getName() + " (ID: " + this.getId() + ")");
		try{
			UncertainTrue finalResult = new UncertainTrue(ute.getMaxCertainty()); 
			for(Formulae f : conditions){
				Debug.debug(Debug.heartTag, Level.CONDITIONS, "Checking condition "+f);
				UncertainTrue partialResult = f.evaluate(wm, ute);
				Debug.debug(Debug.heartTag, Level.CONDITIONS, 
						"Condition "+f+" satisfied with certainty ("+partialResult.getCertinatyFactor()+").");
			
				if(partialResult.getCertinatyFactor() < finalResult.getCertinatyFactor()){
					finalResult = partialResult;
					Debug.debug(Debug.heartTag, Level.CONDITIONS, 
							"Changing the rule evaluation result to be true with certainty ("+finalResult.getCertinatyFactor()+").");
					
				}
			}
			return finalResult;
		}catch (NotInTheDomainException e) {
			// TODO Auto-generated catch block
			throw e;
		}
	}
	
	/**
	 * Method that fires the decision part of the rule. 
	 * It does not evaluate the rule conditions. 
	 * For that, refer to {@link #evaluate}.
	 * 
	 * @param wm a working memory object that contains information about attributes values
	 * @return true, if the rule was correctly executed, false if there are some errors
	 * @throws UnsupportedOperationException
	 * @throws NotInTheDomainException
	 */
	public boolean execute(WorkingMemory wm, UncertainTrue certainty) throws UnsupportedOperationException, NotInTheDomainException {
		try{	
			Debug.debug(Debug.heartTag, Level.RULES, "Executing decisinos of rule "+this.getName()+" (ID: "+this.getId()+")");
			//Exeute decisions - set new attributes values
			for(Decision d : decisions){
				Debug.debug(Debug.heartTag, Level.RULES, "Executing decisions "+d);
				if(!d.execute(wm,certainty)){
					Debug.debug(Debug.heartTag, Level.RULES, "Executing decisions "+d+" failed.");
					return false;
				}else{
					Debug.debug(Debug.heartTag, Level.RULES, "Executing decisions "+d+" succeeded.");
				}
			}
			//Execute actions
			Debug.debug(Debug.heartTag, Level.RULES, "Executing actions of rule "+this.getName()+" (ID: "+this.getId()+")");
			for(String a : actions){
				if(a != null){
					Class<?> actionClass =  Class.forName(a);
					//TODO this should be run in separate thread
					Object[] objToPass = {wm.getCurrentState()};
					Class<?> paramsTypes[] = {State.class};
					Method method = actionClass.getMethod("execute", paramsTypes);
					Action action = (Action)actionClass.newInstance();
					method.invoke(action,objToPass);
				}
			}
			
			//TODO pass tokens, what about rule-links?
			return true;
		}catch(UnsupportedOperationException e){
			//TODO rethrow with modified message
			throw e;
		} catch (NotInTheDomainException e) {
			// TODO Auto-generated catch block
			throw e;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (UnknownValueException e) {
            Debug.debug(Debug.heartTag, Level.VERBOS,
                    "Executing decision part od the rule "+this.getName()+" (ID: "+this.getId()+")"+
                            " failed du to unknown value in the expression.");
            return false;
        }
    }

	public String getId() {
		return id;
	}

	private void setId(String id) {
		this.id = id;
	}

	public String getSchemeName() {
		return schemeName;
	}

	private void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}

	public String getName() {
		return String.format("%s/%d", this.schemeName, this.orderNumber);
	}

	public Integer getOrderNumber() {
		return orderNumber;
	}

	private void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	public LinkedList<Rule> getRuleLinks() {
		return new LinkedList<Rule>(ruleLinks);
	}

	private void setRuleLinks(LinkedList<Rule> ruleLinks) {
		this.ruleLinks = ruleLinks;
	}

    void addRuleLink(Rule rule) {
        if (ruleLinks == null) {
            ruleLinks = new LinkedList<Rule>();
        }
        ruleLinks.add(rule);
    }
        
	public LinkedList<Table> getTabLinks() {
		return new LinkedList<Table>(tabLinks);
	}

	protected void setTabLinks(LinkedList<Table> tablinks) {
		this.tabLinks = tablinks;
	}

	void addTabLink(Table table) {
        if (tabLinks == null) {
            tabLinks = new LinkedList<Table>();
        }
        tabLinks.add(table);
    }

	public LinkedList<Formulae> getConditions() {
		return new LinkedList<Formulae>(conditions);
	}

	private void setConditions(LinkedList<Formulae> conditions) {
		this.conditions = conditions;
	}

	public LinkedList<Decision> getDecisions() {
		return new LinkedList<Decision>(decisions);
	}

	private void setDecisions(LinkedList<Decision> decisions) {
		this.decisions = decisions;
	}

	public float getCertaintyFactor() {
		return certaintyFactor;
	}

	private void setCertaintyFactor(float certaintyFactor) {
		this.certaintyFactor = certaintyFactor;
	}

	public LinkedList<String> getActions() {
		return new LinkedList<String>(actions);
	}

	private void setActions(LinkedList<String> actions) {
		this.actions = actions;
	}

	public List<Annotation> annotations() {
		return new LinkedList<Annotation>(this.annotations);
	}

	public List<Annotation> annotationsNamed(String name) {
		List<Annotation> annotationsNamed = new LinkedList<Annotation>();
		for (Annotation a : annotations) {
			if (name.equals(a.getName())) {
				annotationsNamed.add(a);
			}
		}
		return annotationsNamed;
	}

	public static class Builder implements AnnotatedBuilder {

		public static class IncompleteRuleId {
                public String schemeName;
                public Integer orderNumber;

                public String getName() {
                    if (orderNumber != null) {
                        return schemeName.concat("/").concat(String.valueOf(orderNumber));
                    }
                    return schemeName;
                }
            }
		private IncompleteRuleId ruleId;
		private LinkedList<IncompleteRuleId> links;
		private String debugInfo;

		private LinkedList<Formulae.Builder> incConditions;
		private LinkedList<Decision.Builder> incDecisions;
		private LinkedList<String> actions;

		private List<Annotation.Builder> incAnnotations;

		private float certaintyFactor;

		public Builder() {
			this.setLinks(new LinkedList<IncompleteRuleId>());
			this.setActions(new LinkedList<String>());
			this.setCertaintyFactor(1);
		}

		public Rule build(Map<String, Attribute> attributes) throws ModelBuildingException {
			Rule rule = new Rule();
			rule.setOrderNumber(getRuleId().orderNumber);
			rule.setSchemeName(getRuleId().schemeName);
			rule.setConditions(buildConditions(attributes));
			rule.setDecisions(buildDecisions(attributes));
			rule.setActions(getActions());
			rule.setCertaintyFactor(getCertaintyFactor());
			if (getIncompleteAnnotations() != null) {
				rule.annotations = buildAnnotations();
			}

			return rule;
		}

		LinkedList<Formulae> buildConditions(Map<String, Attribute> attributes) throws ModelBuildingException {
			LinkedList<Formulae> conditions = new LinkedList<Formulae>();
			for (Formulae.Builder fb : this.getIncompleteConditions()) {
				String fbAttrName = fb.getAttributeName();
				if (!attributes.containsKey(fbAttrName)) {
					throw new ModelBuildingException(String.format("Rule %s uses in conditional part an undefined Attribute %s.\n%s", this.getRuleId().getName(), fbAttrName, this.getDebugInfo()));
				}
				try {
					conditions.add(fb.build(attributes));
				} catch (BuilderException ex) {
					throw new ModelBuildingException(ex.getMessage());
				} catch (NotInTheDomainException ex) {
					throw new ModelBuildingException(ex.getMessage());
				} catch (StaticEvaluationException ex) {
					throw new ModelBuildingException(ex.getMessage());
				} catch (UnknownValueException ex) {
					throw new ModelBuildingException(ex.getMessage());
				}
			}
			return conditions;
		}
		LinkedList<Decision> buildDecisions(Map<String, Attribute> attributes) throws ModelBuildingException {
			LinkedList<Decision> decisions = new LinkedList<Decision>();
			for (Decision.Builder db : this.getIncompleteDecisions()) {
				String dbAttrName = db.getAttributeName();
				if (!attributes.containsKey(dbAttrName)) {
					throw new ModelBuildingException(String.format("Rule %s uses in decisive part an undefined Attribute %s.\n%s", this.getRuleId().getName(), dbAttrName, this.getDebugInfo()));
				}
				try {
					decisions.add(db.build(attributes));
				} catch (BuilderException ex) {
					throw new ModelBuildingException(ex.getMessage());
				}
			}
			return decisions;
		}

		public Builder setRuleId(IncompleteRuleId ruleId) {
			this.ruleId = ruleId;
			return this;
		}
		public IncompleteRuleId getRuleId() {
			return this.ruleId;
		}

		public Builder setDebugInfo(String debugInfo) {
			this.debugInfo = debugInfo;
			return this;
		}
		public String getDebugInfo() {
			return this.debugInfo;
		}

		public Builder setIncompleteConditions(LinkedList<Formulae.Builder> conds) {
			this.incConditions = conds;
			return this;
		}
		public Builder addIncompleteCondition(Formulae.Builder c) {
			if (incConditions == null) {
				incConditions = new LinkedList<Formulae.Builder>();
			}
			getIncompleteConditions().add(c);
			return this;
		}
		public LinkedList<Formulae.Builder> getIncompleteConditions() {
			return incConditions;
		}

		public Builder setIncompleteDecisions(LinkedList<Decision.Builder> decs) {
			this.incDecisions = decs;
			return this;
		}
		public Builder addIncompleteDecision(Decision.Builder d) {
			if (getIncompleteDecisions() == null) {
				this.incDecisions = new LinkedList<Decision.Builder>();
			}
			getIncompleteDecisions().add(d);
			return this;
		}
        public LinkedList<Decision.Builder> getIncompleteDecisions() {
            return incDecisions;
        }

		public Builder setActions(LinkedList<String> actions) {
			this.actions = actions;
			return this;
		}
		public Builder addAction(String a) {
			if (getActions() == null) {
				setActions(new LinkedList<String>());
			}
			getActions().add(a);
			return this;
		}
        public LinkedList<String> getActions() {
            return actions;
        }

		public Builder setLinks(LinkedList<IncompleteRuleId> links) {
			this.links = links;
			return this;
		}
        public Builder addLink(IncompleteRuleId l) {
            if (getLinks() == null) {
                setLinks(new LinkedList<IncompleteRuleId>());
            }
            getLinks().add(l);
            return this;
        }
        public LinkedList<IncompleteRuleId> getLinks() {
			return this.links;
		}

		public Builder setCertaintyFactor(float cf) {
			this.certaintyFactor = cf;
			return this;
		}
		public float getCertaintyFactor() {
                return this.certaintyFactor;
            }

        public Builder addIncompleteAnnotation(Annotation.Builder annotation) {
				if (getIncompleteAnnotations() == null) {
					incAnnotations = new LinkedList<Annotation.Builder>();
				}
				getIncompleteAnnotations().add(annotation);
				return this;
			}
        public Builder setIncompleteAnnotations(List<Annotation.Builder> incAnnotations) {
            this.incAnnotations = incAnnotations;
            return this;
        }
        public List<Annotation.Builder> getIncompleteAnnotations() {
            return incAnnotations;
        }

        public List<Annotation> buildAnnotations() {
            LinkedList<Annotation> completeAnnotations = new LinkedList<Annotation>();
            for (Annotation.Builder ab : incAnnotations) {
                completeAnnotations.add(ab.build());
            }
            return completeAnnotations;
        }
    }
}
