package heart.exceptions;

public class AttributeNotRegisteredException extends Exception{
	String attributeName;
	
	public AttributeNotRegisteredException(String message, String attrName) {
		super(message);
		setAttributeName(attrName);
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
}
