package heart.xtt.annotation;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by msl on 26/06/15.
 */
public class Annotation {
    public static final String RelationAnnotation = "Relation";
    public static final String AttributeAnnotation = "Attribute";
    public static final String ValueKey = "value";
    public static final String NameKey = "name";
    public static final String SubjectKey = "subject";

    public Map<String, String> entries;
    public String type;

    public boolean definesAttribute() {
        return type.equals(AttributeAnnotation) || entries.containsKey(ValueKey);
    }

    public boolean definesRelation() {
        return type.equals(RelationAnnotation) || entries.containsKey(SubjectKey);
    }

    public String getAttributeValue() {
        if (definesAttribute()) {
            return entries.get(ValueKey);
        }
        return null;
    };

    public String getName() {
        if (entries.containsKey(NameKey)) {
            return entries.get(NameKey);
        } else {
            return type;
        }
    }

    public String getRelationSubject() {
        if (definesRelation()) {
            return entries.get(SubjectKey);
        }
        return null;
    }

    public String toString() {
        return new String(new StringBuilder().append("@").append(type).append(entries.toString()));
    }

    public static class Builder {
        public Map<String, String> entries;
        public String type;

        public Builder() {
            entries = new HashMap<String, String>();
        }

        public Annotation build() {
            Annotation annotation = new Annotation();
            annotation.entries = this.entries;
            annotation.type = this.type;
            return annotation;
        };

        public Builder setEntries(Map<String, String> entries) {
            this.entries = entries;
            return this;
        }

        public Map<String, String> getEntries() {
            return this.entries;
        }

        public Builder addEntry(String key, String value) {
            entries.put(key, value);
            return this;
        }

        public String getEntry(String key) {
            return entries.get(key);
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public String getType() {
            return this.type;
        }
    }
}
