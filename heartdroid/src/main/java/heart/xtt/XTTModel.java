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


import heart.alsvfd.Formulae;
import heart.exceptions.BuilderException;
import heart.exceptions.ModelBuildingException;
import heart.exceptions.NotInTheDomainException;
import heart.xtt.Rule.Builder.IncompleteRuleId;

import java.util.*;


public class XTTModel {
	public static final int SOURCE_HML = 0;
	public static final int SOURCE_SQL = 1;
	public static final int SOURCE_HMR = 3;
	
	private String version;
	private int source;
	
	private LinkedList<Table> tables;
	private LinkedList<Type> types;
	private LinkedList<Attribute> attributes;

    private XTTModel.Builder builder;

	public XTTModel(int source){
		this.source = source;
		tables = new LinkedList<Table>();
		types = new LinkedList<Type>();
		attributes = new LinkedList<Attribute>();
	}

	public String getVersion() {
        return version;
    }
    public LinkedList<Table> getTables() {
        return tables;
    }
    public LinkedList<Type> getTypes() {
        return types;
    }
    public LinkedList<Attribute> getAttributes() {
        return attributes;
    }
    public Attribute getAttributeById(String id){
        for(Attribute a: attributes){
            if(a.getId().equals(id)) return a;
        }

        return null;
    }
    public Attribute getAttributeByName(String name){
        for(Attribute a: attributes){
            if(a.getName().equals(name)) return a;
        }

        return null;
    }
    public int getSource() {
        return source;
    }

	private void setVersion(String version) {
		this.version = version;
	}
	private void setTables(LinkedList<Table> tables) {
		this.tables = tables;
	}
	private void setTypes(LinkedList<Type> types) {
		this.types = types;
	}
	private void setAttributes(LinkedList<Attribute> attributes) {
		this.attributes = attributes;
	}
    private void setSource(int source) {
        this.source = source;
    }

    /**
     * Builder object is mutable and can be used to build similar
     * models.
     * However, the returned builder is a shallow copy of the original builder,
     * hence any modification to the builder does not change the original builder
     * in the XTTModel class.
     *
     * Yet, the modifications of the Rules' builder will affect the original builder
     * This is not a feature and will be fixes in the future.
     *
     * @return shallow copy of a model builder
     */
    public XTTModel.Builder getBuilder() {
        return builder.copy();
    }


    private void setBuilder(XTTModel.Builder builder) {
        this.builder = builder;
    }

	public static class Builder {
               
        private int source;
        private int version;

        private final Map<String, Type.Builder> incTypes;
        private final Map<String, Attribute.Builder> incAttributes;
        private final Map<String, Table.Builder> incTables;
        private final Map<String, Rule.Builder> incRules;

            
        public Builder() {
            this.source = SOURCE_HML;
            this.incAttributes = new HashMap<String, Attribute.Builder>();
            this.incTables = new HashMap<String, Table.Builder>();
            this.incRules = new HashMap<String, Rule.Builder>();
            this.incTypes = new HashMap<String, Type.Builder>();
        }

        public XTTModel.Builder copy(){
            //TODO: make it deep copy
            XTTModel.Builder b = new Builder();

            b.incTypes.clear();
            b.incAttributes.clear();
            b.incTables.clear();
            b.incRules.clear();
            try {
                b.setIncompleteTypes(new LinkedList<Type.Builder>(this.getIncompleteTypes()));
                b.setIncompleteAttributes(new LinkedList<Attribute.Builder>(new HashSet<Attribute.Builder>(this.getIncompleteAttributes())));
                b.setIncompleteTables(new LinkedList<Table.Builder>(this.getIncompleteTables()));
                b.setIncompleteRules(new LinkedList<Rule.Builder>(this.getIncompleteRules()));
            }catch(ModelBuildingException e){
                // Nothing to do -- never happens
            }

            return b;
        }

        public XTTModel build() throws ModelBuildingException {
            Map<String, Type> types;
            Map<String, Attribute> attributes;
            Map<String, Table> tables;
            Map<String, Rule> rules;

            try {
                types = buildTypes();
                attributes = buildAttributes(types);
                tables = buildTables(attributes);
                rules = buildRules(attributes, tables);
            } catch (ModelBuildingException ex) {
                throw new ModelBuildingException(ex.getMessage());
            } catch (BuilderException ex) {
                throw new ModelBuildingException(ex.getMessage());
            } catch (NotInTheDomainException ex) {
                throw new ModelBuildingException(ex.getMessage());
            }
            XTTModel model = new XTTModel(this.source);
            model.setTypes(new LinkedList<Type>(types.values()));
            model.setAttributes(new LinkedList<Attribute>(new HashSet<Attribute>(attributes.values())));
            model.setTables(new LinkedList<Table>(tables.values()));
            model.setBuilder(this);
            return model;
        }

        private Map<String, Type> buildTypes() throws BuilderException {
            Map<String, Type> types = new HashMap<String, Type>();
            for (String typeName : this.incTypes.keySet()) {
                Type.Builder incType = this.incTypes.get(typeName);
                Type type = incType.build();
                types.put(typeName, type);
            }
            return types;
        }

        private Map<String, Attribute> buildAttributes(Map<String, Type> types) throws ModelBuildingException,
                BuilderException,
                NotInTheDomainException {
            Map<String, Attribute> attributes = new HashMap<String, Attribute>();
            for (Attribute.Builder incAttr : this.incAttributes.values()) {
                String typeName = incAttr.getTypeName();

                if (!types.containsKey(typeName)) {
                    throw new ModelBuildingException(String.format("Attribute %s uses undefined Type %s.\n%s", incAttr.getName(), typeName, incAttr.getDebugInfo()));
                }

                Type attrType = types.get(typeName);
                Attribute attr = incAttr.build(attrType);
                attributes.put(attr.getName(), attr);
                attributes.put(attr.getAbbreviation(), attr);
            }
            return attributes;
        }

        private Map<String, Table> buildTables(Map<String, Attribute> attributes) throws ModelBuildingException {
            Map<String, Table> tables = new HashMap<String, Table>();
            for (Table.Builder incTable : this.incTables.values()) {
                LinkedList<Attribute> condAtts = new LinkedList<Attribute>();
                LinkedList<Attribute> decAtts = new LinkedList<Attribute>();

                for (String attrName : incTable.getConditionalAttributesNames()) {
                    if (!attributes.containsKey(attrName)) {
                        throw new ModelBuildingException(String.format("Table %s uses in preconditions an undefined Attribute %s.\n%s", incTable.getName(), attrName, incTable.getDebugInfo()));
                    }
                    condAtts.add(attributes.get(attrName));
                }

                for (String attrName : incTable.getDecisiveAttributesNames()) {
                    if (!attributes.containsKey(attrName)) {
                        throw new ModelBuildingException(String.format("Table %s uses decisions with an undefined Attribute %s.\n%s", incTable.getName(), attrName, incTable.getDebugInfo()));
                    }
                    decAtts.add(attributes.get(attrName));
                }

                Table table = incTable.build(condAtts, decAtts);
                tables.put(incTable.getName(), table);
            }
            return tables;
        }

        private Map<String, Rule> buildRules(Map<String, Attribute> attributes, Map<String, Table> tables) throws
                ModelBuildingException {

            Map<String, Rule> rules = new HashMap<String, Rule>();
            Map<Rule, LinkedList<String>> ruleToSchemes = new HashMap<Rule, LinkedList<String>>();
            Map<Rule, LinkedList<String>> ruleToRules = new HashMap<Rule, LinkedList<String>>();

            for (Rule.Builder incRule : this.incRules.values()) {
                IncompleteRuleId ruleId = incRule.getRuleId();
                String schemeName = ruleId.schemeName;
                String ruleName = ruleId.getName();
                if (!tables.containsKey(schemeName)) {
                    throw new ModelBuildingException(String.format("Rule %s uses belongs to undefined scheme %s.\n%s", ruleName, schemeName, incRule.getDebugInfo()));
                }

                if (rules.containsKey(ruleName)) {
                    throw new ModelBuildingException(String.format("Rule named %s is already defined.\n%s", ruleName, incRule.getDebugInfo()));
                }

                LinkedList<IncompleteRuleId> links = incRule.getLinks();
                LinkedList<String> schemeLinks = new LinkedList<String>();
                LinkedList<String> ruleLinks = new LinkedList<String>();

                for (IncompleteRuleId rId : links) {
                    if (rId.orderNumber != null) {
                        ruleLinks.add(rId.getName());
                    }
                    else {
                        schemeLinks.add(rId.schemeName);
                    }
                }

                Table table = tables.get(schemeName);
                Rule rule = incRule.build(attributes);

                for (Decision d : rule.getDecisions()) {
                    if (!table.getConclusion().contains(d.attr)) {
                        throw new ModelBuildingException(String.format("Rule %s uses in decisive part an Attribute %s not present in its Scheme definition.\n%s", ruleName, d.attr.getName(), incRule.getDebugInfo()));
                    }
                }

                for (Formulae f : rule.getConditions()) {
                    if (!table.getPrecondition().contains(f.getAttribute())) {
                        throw new ModelBuildingException(String.format("Rule %s uses in conditional part an Attribute %s not present in its Scheme definition.\n%s", ruleName, f.getLHS(), incRule.getDebugInfo()));
                    }
                }

                table.addRule(rule);
                ruleToSchemes.put(rule, schemeLinks);
                ruleToRules.put(rule, ruleLinks);
                rules.put(rule.getName(), rule);
            }

            for (Map.Entry<Rule,LinkedList<String>> entry : ruleToSchemes.entrySet()) {
                Rule rule = entry.getKey();
                for (String schemeName : entry.getValue()) {
                    rule.addTabLink(tables.get(schemeName));
                }
            }

            for (Map.Entry<Rule,LinkedList<String>> entry : ruleToRules.entrySet()) {
                Rule rule = entry.getKey();
                for (String ruleName : entry.getValue()) {
                    rule.addRuleLink(rules.get(ruleName));
                }
            }
            return rules;
        }

        public Builder setVersion(int version) {
            this.version = version;
            return this;
        }
        public Builder setSource(int src) {
            this.source = src;
            return this;
        };
        public int getVersion() {
        return version;
    }
        public int getSource() {
        return source;
    }

        public Builder setIncompleteTypes(Collection<Type.Builder> incTypes) throws ModelBuildingException {
            this.incTypes.clear();
            for (Type.Builder tb: incTypes) {
                this.addIncompleteType(tb);
            }
            return this;
        }
        public Builder setIncompleteAttributes(Collection<Attribute.Builder> incAttributes) throws ModelBuildingException {
            this.incAttributes.clear();
            for (Attribute.Builder ia: incAttributes) {
                this.addIncompleteAttribute(ia);
            }
            return this;
        }
        public Builder setIncompleteRules(Collection<Rule.Builder> incRules) throws ModelBuildingException {
            this.incRules.clear();
            for (Rule.Builder ir: incRules) {
                this.addIncompleteRule(ir);
            }
            return this;
        }
        public Builder setIncompleteTables(Collection<Table.Builder> incTables) throws ModelBuildingException {
        this.incTables.clear();
        for (Table.Builder it: incTables) {
            this.addIncompleteTable(it);
        }
        return this;
    }

        public Collection<Type.Builder> getIncompleteTypes() {
            return this.incTypes.values();
        }
        public Collection<Attribute.Builder> getIncompleteAttributes() {
            return this.incAttributes.values();
        }
        
        public Collection<Rule.Builder> getIncompleteRules() { return this.incRules.values(); }
        public Collection<Table.Builder> getIncompleteTables() { return this.incTables.values(); }

        public Builder addIncompleteType(Type.Builder incType) throws ModelBuildingException {
            String key = incType.getName();
            if (this.incTypes.containsKey(key)) {
                throw new ModelBuildingException(String.format("Type %s is already defined.\n", key));
            }
            this.incTypes.put(key, incType);
            return this;
        }
        public Builder addIncompleteAttribute(Attribute.Builder incAttr) throws ModelBuildingException {
            String keyName = incAttr.getName();
            String keyAbbrev = incAttr.getAbbreviation();
            if (this.incAttributes.containsKey(keyName) || this.incAttributes.containsKey(keyAbbrev)) {
                throw new ModelBuildingException(String.format("Attribute %s (%s) is already defined.\n%s", keyName, keyAbbrev, incAttr.getDebugInfo()));
            }
            this.incAttributes.put(keyName, incAttr);

            if (keyAbbrev != null) {
                this.incAttributes.put(keyAbbrev, incAttr);
            }

            return this;
        }
        public Builder addIncompleteRule(Rule.Builder incRule) throws ModelBuildingException {
            String key = incRule.getRuleId().getName();
            if (this.incRules.containsKey(key)) {
                throw new ModelBuildingException(String.format("Rule %s is already defined\n%s", key, incRule.getDebugInfo()));
            }
            this.incRules.put(key, incRule);
            return this;
        }
        public Builder addIncompleteTable(Table.Builder incTable) throws ModelBuildingException {
            String key = incTable.getName();
            if (this.incTables.containsKey(key)) {
                throw new ModelBuildingException(String.format("Table %s is already defined\n%s", key, incTable.getDebugInfo()));
            }
            this.incTables.put(key, incTable);
            return this;
        }

        public Builder removeIncompleteTypeNamed(String name) {
            if (this.incTypes.containsKey(name)) {
                this.incTypes.remove(name);
            }
            return this;
        }
        public Builder removeIncompleteType(Type.Builder incType) {
            return this.removeIncompleteTypeNamed(incType.getName
                    ());
        }
        public Builder removeIncompleteAttributeWithId(String id) {
            String keyToRemove = null;

            for (String key: incAttributes.keySet()) {
                Attribute.Builder attribute = this.incAttributes.get(key);
                if (attribute.getId().equals(id)) {
                    keyToRemove = key;
                    break;
                }
            }

            if (keyToRemove != null) {
                this.incAttributes.remove(keyToRemove);
            }

            return this;
        }
        public Builder removeIncompleteAttributeNamed(String name) {
            if (this.incAttributes.containsKey(name)) {
            	heart.xtt.Attribute.Builder attr = this.incAttributes.get(name);
            	String abbrev = attr.getAbbreviation();
                this.incAttributes.remove(name);
                this.incAttributes.remove(abbrev);                
            }
            return this;
        }
        public Builder removeIncompleteAttributeAbbreviated(String abbrev) {
            return this.removeIncompleteAttributeNamed(abbrev);
        }
        public Builder removeIncompleteAttribute(Attribute.Builder incAttribute) {
            return this.removeIncompleteAttributeNamed(incAttribute.getName());
        }
        public Builder removeIncompleteRuleWithId(Rule.Builder.IncompleteRuleId ruleId) {
            String name = ruleId.getName();
            if (this.incRules.containsKey(name)) {
                this.incRules.remove(name);
            }
            return this;
        }
        public Builder removeIncompleteRule(Rule.Builder incRule) {
            return this.removeIncompleteRuleWithId(incRule.getRuleId());
        }
        public Builder removeIncompleteRuleNamed(String name) {
        	if (this.incRules.containsKey(name)) {
                this.incRules.remove(name);
            }
            return this;
        }
        
        public Builder removeIncompleteTableNamed(String name) {
            if (this.incTables.containsKey(name)) {
                this.incTables.remove(name);
            }
            return this;
        }
        public Builder removeIncompleteTable(Table.Builder incTable) {
            return this.removeIncompleteTableNamed(incTable.getName());
        }

        public Type.Builder getIncompleteTypeNamed(String name) {
            return incTypes.get(name);
        }
        public Attribute.Builder getIncompleteAttributeWithId(String id) {
            for (Attribute.Builder a: incAttributes.values()){
                if (a.getId().equals(id)) {
                    return a;
                }
            }

            return null;
        }
        public Attribute.Builder getIncompleteAttributeNamed(String name) {
            return incAttributes.get(name);
        }
        public Attribute.Builder getIncompleteAttributeAbbreviated(String abbrev) {
            return incAttributes.get(abbrev);
        }
        public Rule.Builder getIncompleteRuleWithId(Rule.Builder.IncompleteRuleId ruleId) {
            return this.incRules.get(ruleId.getName());
        }
        public Rule.Builder getIncompleteRuleNamed(String name) {
            return this.incRules.get(name);
        }
        
        public Table.Builder getIncompleteTableNamed(String name) {
            return incTables.get(name);
        }
    }
	
}
