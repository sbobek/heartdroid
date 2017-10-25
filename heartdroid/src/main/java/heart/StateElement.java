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


package heart;

import heart.alsvfd.SetValue;
import heart.alsvfd.Value;
import heart.xtt.Attribute;

/**
 * 
 * @author sbk
 * 
 * The StateElement class represents the value of a single attribute at some point of time.
 *
 */
public class StateElement {
	/**
	 * This field is used by the SQL module while storing/restoring model into/from database.
	 * If not provided, this should be null; 
	 */
	protected String id;
	
	/**
	 * The name of the attribute that the value is stored by this state element.
	 */
	protected String attributeName;
	
	/**
	 * The value of the attribute.
	 */
	protected Value value;
	
	/**
	 * Timestamp in milliseconds indicating when the attribute value was assigned.
	 * This is the copy of a timestamp from the {@link Attribute} object.
	 */
	protected long timestamp;
	
	/**
	 * default constructor. It sets {@link #timestamp}to the current system time
	 * and leaves all the other fields to be <code>null</code>
	 */
	public StateElement() {
		setCurrentTimestamp();
	}
	
	/**
	 * The constructor creates StateElement object, and it sets the {@link #timestamp}
	 * to the current system time.
	 * 
	 * @param attributeName the attribute name that value should be tored in the state element
	 * @param value the value of the attribute that should be assigned to it
	 */
	public StateElement(String attributeName, Value value) {
		setAttributeName(attributeName);
		setValue(value);
		setCurrentTimestamp();
	}
	
	/**
	 * The method that retrievs the attribute name that value is retained in this state element
	 * 
	 * @return the attributes name that value is stored within this state element.
	 */
	public String getAttributeName() {
		return attributeName;
	}
	/**
	 * The method sets the attribute name for this state element.
	 * 
	 * @param attributeName a name to be set
	 */
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	
	/**
	 * The method returns the value of the attribute 
	 * 
	 * @return the value of the attribute 
	 */
	public Value getValue() {
		return value;
	}
	
	/**
	 * The method that sets the value for the attribute in this state element.
	 * Note, that his method does not check if the value can be assigned to the attribute.
	 * In particular you can assign a {@link SetValue} to a simple attribute and it will pass.
	 * 
	 * The exception will be thrown only when you try to assign this state to the model.
	 * 
	 * @param value
	 */
	public void setValue(Value value) {
		this.value = value;
	}
	
	/**
	 * The method sets the timestam of the state element to the current system time.
	 * It uses {@link System#currentTimeMillis()} method.
	 */
	public void setCurrentTimestamp(){
		setTimestamp(System.currentTimeMillis());
	}

	/**
	 * A method returns the timestamp of the state element.
	 * 
	 * @return the timestam of the state element
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * The method allow to set the timestamp for the state element.
	 * In most cases you would preffer to use {@link #setCurrentTimestamp()}
	 * instead.
	 * 
	 * @param timestamp a timestamp to set
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	
}
