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

import heart.xtt.annotation.Annotated;
import heart.xtt.annotation.AnnotatedBuilder;
import heart.xtt.annotation.Annotation;

import java.util.*;


public class Table implements Annotated {

	/**
	 * This field is used by the SQL module while storing/restoring model into/from database.
	 * If not provided, this should be null; 
	 */
	protected String id;
	
	/**
	 * A  field containing obligatory schema (table) name
	 */
	protected String name;
	
	/**
	 * A field containing an optional schema description.
	 */
	protected String description;


	protected List<Annotation> annotations;

	//XTT2 Schema
	/**
	 * It is a list that denotes attributes that are required to be 
	 * known by the rules that falls in this schema
	 */
	protected LinkedList<heart.xtt.Attribute> precondition;
	
	/**
	 * It is a list denoted attributes that values are calculate by the rules in given schema.
	 */
	protected LinkedList<heart.xtt.Attribute> conclusion;
	
	/**
	 * This is a list of rules that belongs to the table.
	 */
	protected LinkedList<Rule> rules;
	
	public Table() {
		rules = new LinkedList<Rule>();
		precondition = new LinkedList<heart.xtt.Attribute>();
		conclusion = new LinkedList<heart.xtt.Attribute>();
        annotations = new LinkedList<Annotation>();
	}

	public String getId() {
		return id;
	}

	private void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}

	public LinkedList<heart.xtt.Attribute> getPrecondition() {
		return new LinkedList<Attribute>(precondition);
	}

	private void setPrecondition(LinkedList<heart.xtt.Attribute> precondition) {
		this.precondition = precondition;
	}

	public LinkedList<heart.xtt.Attribute> getConclusion() {
		return new LinkedList<Attribute>(conclusion);
	}

	private void setConclusion(LinkedList<heart.xtt.Attribute> conclusion) {
		this.conclusion = conclusion;
	}

	public LinkedList<Rule> getRules() {
		return new LinkedList<Rule>(rules);
	}

	private void setRules(LinkedList<Rule> rules) {
		this.rules = rules;
	}
        
    protected void addRule(Rule rule) {
        if (rules == null) {
            rules = new LinkedList<Rule>();
        }

        int i;
        for (i = 0; i < rules.size(); i++) {
            Rule exr = rules.get(i);
            if (exr.orderNumber > rule.orderNumber) {
                break;
            }
        }
        rules.add(i, rule);
    }

	public String getDescription() {
		return description;
	}

	private void setDescription(String description) {
		this.description = description;
	}

    @Override
    public List<Annotation> annotations() {
        return new LinkedList<Annotation>(annotations);
    }

    @Override
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
        private String name;
        private String description;
        private LinkedList<String> conditionalAttributesNames;
        private LinkedList<String> decisiveAttributesNames;
        private List<Annotation.Builder> incAnnotations;
        private String debugInfo;
        
        public Table build(LinkedList<Attribute> conditionalAttributes, LinkedList<Attribute> decisiveAttributes) {
            Table t = new Table();
            t.setConclusion(decisiveAttributes);
            t.setPrecondition(conditionalAttributes);
            t.setName(this.getName());
            t.setDescription(this.getDescription());
            if (getIncompleteAnnotations() != null) {
                t.annotations = buildAnnotations();
            }

            return t;
        }
        
        public Builder setName(String name) {
            this.name = name;
            return this;
        }
        public String getName() {
            return this.name;
        }

        @Override
        public Builder addIncompleteAnnotation(Annotation.Builder annotation) {
            if (getIncompleteAnnotations() == null) {
                setIncompleteAnnotations(new LinkedList<Annotation.Builder>());
            }

            getIncompleteAnnotations().add(annotation);
            return this;
        }

        @Override
        public List<Annotation> buildAnnotations() {
            LinkedList<Annotation> completeAnnotations = new LinkedList<Annotation>();
            for (Annotation.Builder ab : incAnnotations) {
                completeAnnotations.add(ab.build());
            }
            return completeAnnotations;
        }


        public Builder setConditionalAttributesNames(LinkedList<String> condAtts) {
            this.conditionalAttributesNames = condAtts;
            return this;
        }
        public LinkedList<String> getConditionalAttributesNames() {
            return this.conditionalAttributesNames;
        }            
        
        public Builder setDecisiveAttributesNames(LinkedList<String> decAtts) {
            this.decisiveAttributesNames = decAtts;
            return this;
        }
        public LinkedList<String> getDecisiveAttributesNames() {
            return this.decisiveAttributesNames;
        }  

        public Builder setDebugInfo(String debugInfo) {
            this.debugInfo = debugInfo;
            return this;
        }         
        public String getDebugInfo() {
            return this.debugInfo;
        }
        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }
        public String getDescription() {
            return description;
        }

        public Builder setIncompleteAnnotations(List<Annotation.Builder> annotations) {
            this.incAnnotations = annotations;
            return this;
        }
        public List<Annotation.Builder> getIncompleteAnnotations() {
            return incAnnotations;
        }


    }
}
