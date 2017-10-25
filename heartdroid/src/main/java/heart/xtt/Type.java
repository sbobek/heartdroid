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

import heart.alsvfd.SetValue;
import heart.exceptions.BuilderException;
import heart.xtt.annotation.Annotated;
import heart.xtt.annotation.AnnotatedBuilder;
import heart.xtt.annotation.Annotation;

import java.util.LinkedList;
import java.util.List;


/**
 * 
 * @author sbk
 * The class that defines kind, structure and domain for further attributes definitions.
 * @see Attribute
 */
public class Type implements Annotated {
	/**
	 * This field is used by the SQL module while storing/restoring model into/from database.
	 * If not provided, this should be null; 
	 */
	private String id;
	
	/**
	 * This field contains attribute name. This should be the default identifier that
	 * types are referred.
	 */
	private String name;
	
	/**
	 * 
	 */
	private Integer length;
	
	/**
	 * A field describing base of the type. It can be either numeric, or symbolic.
	 * It can take one of the values: {@link #BASE_NUMERIC}, {@link #BASE_SYMBOLIC}, or
	 * {@link #BASE_UNKNOWN}
	 */
	private String base;
	
	/**
	 * A field indicating whether domain is ordered or not. 
	 * Every numeric type has ordered domain by default.
	 * It can take one of the values {@link #ORDERED_NO}, {@link #ORDERED_YES},
	 * or {@link #ORDERED_UNKNOWN}.
	 */
	private String ordered;
	
	/**
	 * A filed containing longer description of the type
	 */
	private String description;
	
	/**
	 * A field denoting precision for floating point numbers.
	 * This is a different name for scale in HMR language.
	 */
	private Integer precision;

	/**
	 * A filed that contains a list of all acceptable values for this type. 
	 * For ordered symbolic domains, it is possible to assign order to the values. 
	 * It allows referring to the values using this number instead of symbolic name. 
	 * If a symbolic domain is marked as ordered, and there are no weights assigned 
	 * explicitly to the domain's values, default numbering is assumed that starts 
	 * from 1 and ends on n, where n is the number of elements in the domain.
	 */
	
	private SetValue domain;

	public static final String BASE_NUMERIC = "numeric";
	public static final String BASE_SYMBOLIC = "symbolic";
	public static final String BASE_UNKNOWN ="unknown";
	

	public static final String ORDERED_YES = "yes";
	public static final String ORDERED_NO = "no";
	public static final String ORDERED_UNKNOWN ="unknown";
		
	protected List<Annotation> annotations;

	public List<Annotation> annotations() {
		return new LinkedList<Annotation>(this.annotations);
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

	/**
	 * A constructor that is used for SQL Mapping with ORM.
	 */
	Type(){}
	
	private Type(Builder builder) throws BuilderException{			
		this.setBase(builder.getBase());
		this.setDescription(builder.getDescription());
		this.setDomain(builder.getDomain());
		this.setId(builder.getId());
		this.setLength(builder.getLength());
		this.setName(builder.getName());
		this.setOrdered(builder.getOrdered());
		this.setPrecision(builder.getPrecision());
		this.annotations = new LinkedList<Annotation>();
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

	public String getBase() {
		return base;
	}

    private void setBase(String base) {
		this.base = base;
	}

	public boolean isNumeric() {
		return this.base.equals(BASE_NUMERIC);
	}

	public boolean isSymbolic() {
		return this.base.equals(BASE_SYMBOLIC);
	}

	public boolean isOrdered() {
		return this.ordered.equals(ORDERED_YES);
	}

	public String getOrdered() {
		return ordered;
	}

    private void setOrdered(String ordered) {
		this.ordered = ordered;
	}

	public String getDescription() {
		return description;
	}

    private void setDescription(String description) {
		this.description = description;
	}

	public Integer getPrecision() {
		return precision;
	}

    private void setPrecision(Integer precision) {
		this.precision = precision;
	}

	public SetValue getDomain() {
		return domain;
	}

    private void setDomain(SetValue domain) {
		this.domain = domain;
	}

	public Integer getLength() {
		return length;
	}

    private void setLength(Integer length) {
		this.length = length;
	}

	
	public static class Builder implements AnnotatedBuilder {
		private String id;
		private String name;
		private Integer length;
		private String base;
		private String ordered = Type.ORDERED_NO;
		private String description;
		private Integer precision;
		private SetValue domain;
		private String debugInfo;
		private List<Annotation.Builder> incAnnotations;

//        TODO: this method should have only package access;
        /**
         * This method shouldn't be used directly. Currently it is public only because for the test purposes.
         * @return new immutable Type
         * @throws BuilderException
         */
		public Type build() throws BuilderException{
			if (getName() == null || getBase() == null || getDomain() == null){
				throw new BuilderException(String.format("Error while building Type. The name, base or domain attribute has not been set.\n%s", this.getDebugInfo()));
			} else{
				Type type = new Type(this);
				if (getIncompleteAnnotations() != null) {
					type.annotations = buildAnnotations();
				}
				return type;
			}
		}

		public Builder setId(String id) {
			this.id = id;
			return this;
		}
		public Builder setName(String name) {
			this.name = name;
			return this;
		}
		public Builder setBase(String base) {
			this.base = base;
			return this;
		}
		public Builder setOrdered(String ordered) {
			this.ordered = ordered;
			return this;
		}
		public Builder setDescription(String description) {
			this.description = description;
			return this;
		}
		public Builder setPrecision(Integer precision) {
			this.precision = precision;
			return this;
		}
		public Builder setDomain(SetValue domain) {
			this.domain = domain;
			return this;
		}
		public Builder setLength(Integer length) {
			this.length = length;
			return this;
		}
		public Builder setDebugInfo(String debugInfo) {
			this.debugInfo = debugInfo;
			return this;
		}
		public Builder setIncompleteAnnotations(List<Annotation.Builder> annotations) {
			this.incAnnotations = annotations;
			return this;
		}

		public String getId(){
			return this.id;
		}
		public String getName() {
			return name;
		}
		public Integer getLength() {
			return length;
		}
		public String getBase() {
			return base;
		}
		public String getOrdered() {
			return ordered;
		}
		public String getDescription() {
			return description;
		}
		public Integer getPrecision() {
			return precision;
		}
		public SetValue getDomain() {
			return domain;
		}
		public String getDebugInfo() {
                    return this.debugInfo;
                }
		public List<Annotation.Builder> getIncompleteAnnotations() {
			return incAnnotations;
		}

        @Override
		public AnnotatedBuilder addIncompleteAnnotation(Annotation.Builder annotation) {
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
    }
}
