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

import heart.alsvfd.Null;
import heart.alsvfd.Value;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * 
 * @author sbk
 * 
 * The State class represents a list of {@link StateElement} objects.
 * It represents the system state at some point in time.
 * The state is understand as a set of attributes and their values.
 *
 */
public class State implements Iterable<StateElement>{
	/**
	 * This field is used by the SQL module while storing/restoring model into/from database.
	 * If not provided, this should be null; 
	 */
	protected String id;
	
	/**
	 * This field contains state name. This should be the default identifier that
	 * state are referred. 
	 */
	protected String name;
	
	/**
	 * Timestamp in milliseconds indicating a time when the sate was created.
	 * This equals the "youngest" {@link StateElement} in the {@link State}.
	 */
	protected long timestamp;
	
	/**
	 * This is a list that contains {@link StateElement}, which stores attribute name and value.
	 */
	LinkedList<StateElement> stateElements; //TODO: change this to hashMap
	
	public State() {
		this.stateElements = new LinkedList<StateElement>();
		setCurrentTimestamp();
	}

	public LinkedList<StateElement> getStateElements() {
		return stateElements;
	}

	public void setStateElements(LinkedList<StateElement> stateElements) {
		this.stateElements = stateElements;
	}
	
	public void addStateElement(StateElement element){
		stateElements.add(element);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Value getValueOfAttribute(String name){
		for(StateElement se : stateElements){
			if(se.getAttributeName().equals(name)){
				return se.getValue();
			}
		}
		return new Null();
	}

	@Override
	public Iterator<StateElement> iterator() {
		return stateElements.iterator();
	}
	
	public void setCurrentTimestamp(){
		setTimestamp(System.currentTimeMillis());
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
}
