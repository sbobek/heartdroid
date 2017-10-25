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
import heart.alsvfd.SimpleNumeric;
import heart.alsvfd.SimpleSymbolic;
import heart.alsvfd.Value;
import heart.exceptions.AttributeNotRegisteredException;
import heart.exceptions.NotInTheDomainException;
import heart.exceptions.RelativeTimestampException;
import heart.exceptions.UnknownValueException;
import heart.inference.InferenceAlgorithm;
import heart.uncertainty.AmbiguityResolver;
import heart.uncertainty.DefaultAmbiguityResolver;
import heart.xtt.Attribute;
import heart.xtt.Type;
import heart.xtt.XTTModel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;

/**
 * 
 * @author sbk
 * 
 * This is the class that is responsible for state management of the system
 * Every attributes values changes has to be done with this class methods.
 * It also gives access to the current and past system states.
 *
 */
public class WorkingMemory {

    /**
     * This constant defines maximal number of states stored in the {@link #historyLog}.
     * This limit does not apply to snapshots.
     */
    public static final int MAX_HISTORY_SIZE = 1000000;

    /**
     * The timestamp that is valid at the time. This is used to guarantee the atomicity of inference process.
     * This timestamp is only used when {@link #isTimeLocked()} return true.
     */
    private long currentTimestamp;

    /**
     * True if in the reasoning mode, false otherwise.
     * It assures that all the operations performed during reasoning
     * are atomic as a whole
     */
    private boolean timeLock;

    /**
     * Ambiguity resolver that in case of several ambiguous values returns
     * always the last one (this operation follows an intuition that will
     * occur in real world, when the most recent assignment overrides
     * previous assignment. This is also default resolver for {@link heart.uncertainty.ALSVEvaluator}
     */
    private AmbiguityResolver ar = new DefaultAmbiguityResolver();

	/**
	 * The list of attributes names and real attributes and their values that are matched against the names.
	 * In usual case this will be redundant, but allows faster search thanks to HashMap.
	 */
	private HashMap<String, AVEntry> registeredAttributes; 
	
	/**
	 * The list of states of the system. The resolution of how often the system state is logged is set
	 * by the flag.
	 */
	private LinkedList<State> historyLog;
	
	/**
	 * The list of snapshots of the system state. This is different from the {@link #historyLog} as it allows custom names,
	 * and can be invoked by the user on demand.
	 */
	private HashMap<String,State> snapshots;
	
	/**
	 * Default constructor for the working memory object.
	 */
	public WorkingMemory() {
		registeredAttributes = new HashMap<String, AVEntry>();
		historyLog = new LinkedList<State>();
		snapshots = new HashMap<String, State>();
        timeLock = false;
	}
	
	/**
	 * The method sets values of the attributes defined by state parameter.
	 * Depending on the autoregister parameter, the system registers all the attributes
	 * from the model.
	 * 
	 * @param state a state to be set
	 * @param model the model from which the attributes values re to be set
	 * @param autoregister the parameter indicating if the method should register all the attributes from the model (if true) or none (if false).
	 * @throws NotInTheDomainException 
	 * @throws AttributeNotRegisteredException 
	 */
	public void setCurrentState(State state, XTTModel model, boolean autoregister) throws NotInTheDomainException, AttributeNotRegisteredException{
		for(Attribute attr : model.getAttributes()){
            Value v = state.getValueOfAttribute(attr.getName());
			setAttributeValue(attr, v, autoregister);
		}
	}
	
	/**
	 * The method returns values of all registered attributes in a form of {@link State} object.
	 * 
	 * @return the state that contains values of all registered attributes.
	 * 
	 */
	public State getCurrentState(){
		State current = new State();
		for(Entry<String, AVEntry> se : registeredAttributes.entrySet()){
			current.addStateElement(new StateElement(se.getKey(),se.getValue().getValue()));
		}
		return current;
	}
	
	/**
	 * Returns the state of the system only for the registered attributes within the model
	 * 
	 * @param model a model which attributes should be returned as a state
	 * @return the current state of the attributes values from the model given as a parameter
	 */
	public State getCurrentState(XTTModel model){
		State current = new State();
		for(Attribute a: model.getAttributes()){
			Value  v = getAttributeValue(a);
			current.addStateElement(new StateElement(a.getName(),v));
		}
		
		return current;
	}
	
	/**
	 * It registers the attribute that later will be accessible and will be included in snapshots and current state.
	 * Registering and unregistering is based on attributes names, not object refferences.
	 * 
	 * @param attribute an attribute to be registered
	 */
	public void registerAttribute(Attribute attribute){
		try {
			if(!registeredAttributes.containsKey(attribute.getName())) {
				registeredAttributes.put(attribute.getName(), new AVEntry(attribute, new Null()));
			}
		} catch (NotInTheDomainException e) {
			// This will not happend, as the Null ca always be assigned as an attribute value
		}
	}
	
	/**
	 * It registers the attribute that later will be accessible and will be included in snapshots and current state.
	 * Registering and unregistering is based on attributes names, not object refferences.
	 * 
	 * @param name a name of the attribute to register
	 * @param model a model where the attribute is located
	 */
	public void registerAttribute(String name, XTTModel model){
		Attribute attr = model.getAttributeByName(name);
		registerAttribute(attr);
	}
	
	/**
	 * It registers all attributes from a given model
	 * Registering and unregistering is based on attributes names, not object refferences.
	 * 
	 * @param model a model from which the attributes have to be regisered
	 */
	public void registerAllAttributes(XTTModel model){
		for(Attribute a: model.getAttributes()){
			registerAttribute(a);
		}
	}
	
	/**
	 * It unregisters the attribute of a given name. 
	 * If such attribute is no present in the register, nothing happens. 
	 * Registering and unregistering is based on attributes names, not object refferences.
	 * 
	 * @param name a name of the attribute to unregister
	 */
	public void unregisterAttribute(String name){
		registeredAttributes.remove(name);
	}
	
	/**
	 * The method unregisters all the attribute from a model given as a parameter.
	 * Registering and unregistering is based on attributes names, not object refferences.
	 * 
	 * @param model a model with attributes to unregister
	 */
	public void unregisterAll(XTTModel model){
		for(Attribute a: model.getAttributes()){
			unregisterAttribute(a.getName());
		}
	}

	public Attribute getAttribute(String attributeName){
		return  registeredAttributes.get(attributeName).getAttribute();
	}

	/**
	 * The method return a value of an attribute of a given name.
	 * If the attribute is not registered, {@link Null} is returned.
	 * 
	 * @param attributeName the attribute name which value is needed
	 * @return the value of the attribute or {@link Null} id the attribute is not registered
	 */
	public Value getAttributeValue(String attributeName){
		AVEntry ave = registeredAttributes.get(attributeName);
		if(ave != null){
			return ave.getValue();
		}else{
			return new Null();
		}
	}
	
	/**
	 * The method return a value of an attribute of a given name.
	 * If the attribute is not registered, {@link Null} is returned.
	 * 
	 * @param attr the attribute which value is needed
	 * @return the value of the attribute or Null if it is not registered
	 */
	public Value getAttributeValue(Attribute attr){
		AVEntry ave = registeredAttributes.get(attr.getName());
		if(ave != null){
			return ave.getValue();
		}else{
			return new Null();
		}
	}

	/**
	 * It returns the historical value of the attribute which name was passed as the parameter from
	 * the point in time indicated by the #rt parameter.
	 * In case the relative timestamp is given as the number of milliseconds,
	 * the method may return a value that timestamp does not equal exactly the desired timestamp.
	 * This may happen when there is no such state in the registry.
	 * If there is no state of the given timestamp, the value from the state right before the timestamp is returned.
	 *
	 * When the relative timestamp is given in a form of state difference, the methods searches
	 * for the closest state that contains the value of the attribute of a name given as the parameters.
	 *
	 *
	 * @param states list of states where the value of the attribute is searched
	 * @param attributeName the name of the attribute which value should be found
	 * @param rt the number of states or milliseconds (counting backward from current state) where the search
	 *                        should begin
	 * @return the value of the attribute, or {@link heart.alsvfd.Null} if such value does not exist
	 */
	public Value findHistoricalValue(LinkedList<State> states, String attributeName, RelativeTimestamp rt){
		State s = findHistoricalState(states, rt);
		return s.getValueOfAttribute(attributeName);

	}

	/**
	 * It returns the historical state  from the point in time indicated by the #rt parameter.
	 * In case the relative timestamp is given as the number of milliseconds,
	 * the method may return a state that timestamp does not equal exactly the desired timestamp.
	 * This may happen when there is no such state in the registry.
	 * If there is no state of the given timestamp, the state from  right before the timestamp is returned.
	 *
	 * When the relative timestamp is given in a form of state difference, the method
	 * returns exactly the state of an index (counting backward) given by the relative timestamp.
	 *
	 *
	 * @param states list of states where the value of the attribute is searched
	 * @param rt the number of states or milliseconds (counting backward from current state) where the search
	 *                        should begin
	 * @return the state from a given point in time
	 */
	public State findHistoricalState(LinkedList<State> states, RelativeTimestamp rt){
		State result = new State();
		switch(rt.getTimeType()){
			case MILISCOUNT:
				long timestamp = this.getCurrentTimestamp() + rt.getRelativeTimeDifference();
				for(State s : states){
					if(s.getTimestamp() >= timestamp){
                        if(timestamp < states.getFirst().getTimestamp()){
                            Debug.debug(Debug.heartTag,
                                    Debug.Level.WARNING,
                                    "Timestamp to obtain the state value is smaller than the first existing state in Working Memory. " +
                                            "The result of the operation may be different than desired.");
                        }
						return s;
					}
				}
				return result;
			case STATECOUNT:
				if(rt.getRelativeTimeDifference() + states.size() < 0) return states.getFirst();
				State s = states.get(states.size()+ (int)rt.getRelativeTimeDifference());
				return s;
		}
		return result;
	}


	/**
	 * The methods searches for values of an attribute given as a parameter in a specified time period.
	 * Time period is given as a relative time period with a step.
	 * If the step is smaller than intervals at which the states were saved, the virtual sampling
	 * is triggered.
     * If the starting point timestamp is older than the first state in the {@link #historyLog},
     * the time span is filled with {@link Null} values.
	 *
     * @param states list of states where the value of the attribute is searched
	 * @param period relative time period
	 * @param attributeName name of the attribute which values has to be found
	 * @return list of values of the attribute in a given period
	 */
	public LinkedList<Value> findHistoricalValues(LinkedList<State> states, RelativeTimePeriod period, String attributeName){

        if(period.getFrom().getTimeType().equals(RelativeTimestamp.TimeType.MILISCOUNT)) {
            return findHistoricalValuesOverTime(states,period,attributeName);
        }else {
            return findHistoricalValuesOverStatecount(states,period,attributeName);
        }

	}

    private LinkedList<Value> findHistoricalValuesOverTime(LinkedList<State> states, RelativeTimePeriod period, String attributeName){
        LinkedList<Value> result = new LinkedList<Value>();

        State previousState = null;
        try {
            State startingPoint = findHistoricalState(states, period.from);
            long timestamp = this.getCurrentTimestamp() + period.getFrom().getRelativeTimeDifference();
            if(timestamp < startingPoint.getTimestamp()){
                long nullingLimit = (startingPoint.getTimestamp()-timestamp)/(period.getStep());
                for(int counter = 0; counter < nullingLimit; counter++){
                    result.add(new Null());
                }
            }
            int startIndex = states.indexOf(startingPoint);
            Iterator<State> it = states.listIterator(startIndex);
            while (it.hasNext()) {
                State s = it.next();
                if (previousState == null) {
                    // we are at the begining;
                    previousState = s;
                    result.add(s.getValueOfAttribute(attributeName));
                } else {
                    Value previousValue = previousState.getValueOfAttribute(attributeName);
                    for (long virtualSample = previousState.getTimestamp() + period.step;
                         (virtualSample - period.to.getRelativeTimeDifference() <= getCurrentTimestamp());
                         virtualSample += period.step) {
                        if (virtualSample < s.getTimestamp()) {
                            result.add(previousValue);
                        } else {
                            previousState = s;
                            result.add(s.getValueOfAttribute(attributeName));
                            break;
                        }

                    }
                }
            }
        }catch(IndexOutOfBoundsException e){
            // This happens when the state is not present in the list
        }

        return result;
    }

    private LinkedList<Value> findHistoricalValuesOverStatecount(LinkedList<State> states, RelativeTimePeriod period, String attributeName){
        LinkedList<Value> result = new LinkedList<Value>();
        try {
            State startingPoint = findHistoricalState(states, period.from);
            for(long countDiff = period.getFrom().getRelativeTimeDifference() + states.size();
                countDiff < 0;
                countDiff+=period.getStep()){
                result.add(new Null());
            }

            int startIndex = states.indexOf(startingPoint);
            Iterator<State> it = states.listIterator(startIndex);
            for(int index = startIndex;
                    index - period.to.getRelativeTimeDifference() <= states.size();
                    index+=period.step ){
                    State s = states.get(index);
                    result.add(s.getValueOfAttribute(attributeName));
            }
        }catch(IndexOutOfBoundsException e){
            // This happens when the state is not present in the list
        }

        return result;
    }



    /**
	 * The method sets a given value to the attribute which name is passed as a parameter.
	 * It the attribute is not present in the WorkingMemmory registry, the {@link AttributeNotRegisteredException} is thrown.
	 * 
	 * @param attributeName name of the attribute which value has to be set
	 * @param value the value to set
	 * @throws AttributeNotRegisteredException
	 * @throws NotInTheDomainException 
	 */
	public void setAttributeValue(String attributeName, Value value) throws AttributeNotRegisteredException, NotInTheDomainException{
		AVEntry ave = registeredAttributes.get(attributeName);
		if(ave != null){
			ave.setAttributeValue(value);
		}else{
			throw new AttributeNotRegisteredException("Attribute "+attributeName+" was not registered.", attributeName);
		}
	}
	
	/**
	 * The method sets a given value to the attribute which  is passed as a parameter.
	 * It the attribute is not present in the WorkingMemmory registry, the {@link AttributeNotRegisteredException} is thrown in case
	 * when autoregister is set to false. In case when autoregister is set to true, the Attribute will be automatically 
	 * registered in Working Memory registry.
	 * 
	 * @param attribute the attribute which value has to be set
	 * @param value the value to set
	 * @param autoregister the parameter indicating if the method should register the attribute (if true) or none (if false).
	 * @throws AttributeNotRegisteredException
	 * @throws NotInTheDomainException 
	 */
	public void setAttributeValue(Attribute attribute, Value value, boolean autoregister) throws AttributeNotRegisteredException, NotInTheDomainException{
        //TODO: sould the timestamp for the value be reset to the current timestamp?
		if(autoregister) registerAttribute(attribute);
		AVEntry ave = registeredAttributes.get(attribute.getName());
		ave.setAttributeValue(value);
	}

    /**
     * A method that resolves all the ambiguous assignments that are present in the working memory.
     * This method is automatically called before reasoning process starts and in {@link InferenceAlgorithm#onPostExecute()} method.
     * Before the reasoning, the ambiguity resolution is performed with the ambiguity resolver provided for the inference process.
     * This methods clears the ambiguous values list.
     */
	public void resolveAmbiguousAttributesValues(){
	    for(AVEntry av : registeredAttributes.values()){
	        av.resolveAmbiguousAttributeValue();
	        av.clearAmbiguousValues();
        }
    }

    /**
     * The method sets ambiguity resolver that resolves ambiguous assignments
     * of values to the same attribute.
     * It is not suggested to use this method while setting attributes values
     * for the initial state as The ambiguity resolver will be used only during
     * the inference process (when {@link #isTimeLocked()} returns true.
     *
     * For other cases, calling {@link #setAttributeValue(String, Value)}
     * would override current attribute value without making the assignment ambiguous,
     * even if called several times.
     *
     * @param ar Ambiguity resolver to be used. By default it is {@link DefaultAmbiguityResolver}
     */
	public void setAmbiguityResolver(AmbiguityResolver ar){
	    if(!isTimeLocked()){
	        Debug.debug(Debug.heartTag,
                    Debug.Level.WARNING,
                    "Setting ambiguity resolver while not in reasoning mode." +
                            "Ambiguity resolver will be overridden for the reasoning process.");
        }
	    this.ar = ar;
    }

    /**
     * Returns ambiguity resolver that is currently set.
     * @see {@link #setAmbiguityResolver(AmbiguityResolver)}
     * @return current ambiguity resolver
     */
    public AmbiguityResolver getAmbiguityResolver(){
	    return ar;
    }
	
	/**
	 * The method makes a snapshot of a current system state, by saving all the values of the registered attributes.
	 * It also automatically invokes {@link #recordLog()} method.
	 * 
	 * @param snapshotName the name of the snapshot.
	 */
	public void makeSnapshot(String snapshotName){
		State snapshot = getCurrentState();
		snapshot.setName(snapshotName);
		//TODO: what if the snapshot of a given name exists?
		snapshots.put(snapshotName, snapshot);
		recordLog();
	}
	
	/**
	 * It returns the state that represents the snapshot of a given name.
	 * 
	 * @param snapshotName a name of the snapshot to return
	 * @return a state representing the snapshot or null if the snapshot of a given name is not present
	 */
	public State getSnapshot(String snapshotName){
		return snapshots.get(snapshotName);
	}
	
	/**
	 * It saves the state of the system at the time of the method is being call
	 * to the history log. It differs from the {@link #makeSnapshot(String)} method
	 * as the logs are more anonymous, as they cannot have custom names, and what is more
	 * logs may be recorded automatically be the logging mechanism, whereas snapshots 
	 * can only be made by the programmer explicitly with the {@link #makeSnapshot(String)} call.
	 */
	public void recordLog(){
		State s = getCurrentState();
		s.setName("log_"+s.getTimestamp());
		if(historyLog.size() >= MAX_HISTORY_SIZE){
		    historyLog.removeFirst();
        }
		historyLog.addLast(s);
	}

	/**
	 * It returns shallow copy of the entire history log with all the records made.
	 * It invokes clone on the {@link #historyLog}.
	 * 
	 * @return the list of all States made with the {@link #recordLog()} method.
	 */
	public LinkedList<State> getHistoryLogCopy() {
		return (LinkedList<State>)historyLog.clone();
	}


	/**
	 * The method returns list of all snapshots made with the {@link #makeSnapshot(String)}
	 * method.
	 * 
	 * @return the list of all the snapshots made.
	 */
	public LinkedList<State> getSnapshots() {
		return new LinkedList<State>(snapshots.values());
	}

    public long getCurrentTimestamp() {
        if(isTimeLocked()) {
            return currentTimestamp;
        }else{
            return System.currentTimeMillis();
        }
    }

    protected void setCurrentTimestamp(long currentTimestamp) {
        this.currentTimestamp = currentTimestamp;
    }

    public void lockTime(){
        timeLock = true;
        setCurrentTimestamp(System.currentTimeMillis());
    }

    public void unlockTime(){
        timeLock = false;
    }

    public boolean isTimeLocked(){
        return timeLock;
    }


    protected class AVEntry{
		/**
		 * The attribute which value is stored within the entry
		 */
		private Attribute attr;

        /**
         * List of possible, yet ambiguous values of an attribute.
         * This list is filled only during the reasoning process.
         */

        private LinkedList<Value> ambiguousValues;
		
		/**
		 * current value of the Attribute. This is where the current state is stored
		 */
		private Value value;
		
		public AVEntry(Attribute attr, Value value) throws NotInTheDomainException{
		    ambiguousValues = new LinkedList<Value>();
			this.attr = attr;
			this.value = new Null();
			setAttributeValue(value);
		}
		
		protected Attribute getAttribute(){
			return attr;
		}

		/**
		 * It return value of the attribute.
		 * It the attribute has callback of type in defined, it obtains the value using this callback.
		 * If uses callback if the current value of the attribute is {@link Null}, or the value is outadet.
		 * All the values that were set in past reasoning cycle are considered outdated.
         * If called from outside the reasoning process (i.e. time is not locked), it does not trigger callabck.
         * Instead the last set value is returned.
         * If the attribute has ambiguous values assigned during the reasoning process, the method resolves the
         * ambiguity, but leaves the list of ambiguous values intact, so that the second call of this method
         * will yeld the same result assuming, no new value was assigned to the working memory.
		 *
		 * @return value of the attribute in the entry
         * @see  #isTimeLocked()
         * @see  #lockTime()
		 */
		protected Value getValue(){
            resolveAmbiguousAttributeValue();
			try {
				if (attr.getComm().equals(attr.COMM_IN) && attr.getCallback() != null && isTimeLocked()) {
					if (value instanceof Null || value.getTimestamp() < getCurrentTimestamp()) {
						String callbackClass = attr.getCallback();
						Class<?> actionClass = Class.forName(callbackClass);
						Object[] objToPass = {attr,WorkingMemory.this};
						Class<?> paramsTypes[] = {Attribute.class,WorkingMemory.class};
						Method method = actionClass.getMethod("execute",paramsTypes);
						Callback callback = (Callback) actionClass.newInstance();
						method.invoke(callback,objToPass);

						if(!ambiguousValues.isEmpty()){
                            this.value = getAmbiguityResolver().resolveCumulativeConclusions(ambiguousValues);
                            ambiguousValues.clear();
                        }
					}

				}
			}catch (InstantiationException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

            return this.value;

		}
		
		/**
		 * A method that sets a value of the attribute for a value given as a parameter.
		 * The method checks if the value that is to be assigned is in a domain of the 
		 * attribute type. If not, the {@link NotInTheDomainException} is thrown.
		 * 
		 * Casting between {@link SimpleSymbolic} and {@link SimpleNumeric} is allowed if
		 * SimpleSimbolic type is ordered. In every other case, the {@link NotInTheDomainException}
		 * is thrown.
         *
         * During inference process the method does not assign a value directly to the attribute
         * Instead, it stores the value (or values if multiple rules set the same attribute value)
         * and when {@link #getValue()} method is called, the ambiguity resolver chooses which
         * value to assign to the attribute.
         *
         * After the {@link #getValue()} is called, the list of ambiguous values is cleared.
		 * 
		 * The only exception is {@link Null} value, that despite not being in any domain
		 * can be assigned to the attribute value.
		 * 
		 * @param value to set
		 * @throws NotInTheDomainException
		 */
		protected void setAttributeValue(Value value) throws NotInTheDomainException {
		    Value toAssign = null;
            try {
                if (value instanceof Null) {
                    // This should be served by the catch block, but left just in case
                    toAssign= value;
                } else if (value.isInTheDomain(attr.getType())) {
                    if (attr.getXTTClass().equals(Attribute.CLASS_SIMPLE)) {
                        if (value instanceof SimpleNumeric && attr.getType().getBase().endsWith(Type.BASE_SYMBOLIC)) {
                            // Add casting between symbolic and numeric if ordered
                            toAssign = SimpleSymbolic.findInTheDomain((SimpleNumeric) value, attr.getType());
                            if (toAssign == null) {
                                throw new NotInTheDomainException(attr.getType().getDomain(), value,
                                        "Setting value of attribute '" + attr.getName() +
                                                "' for a value '" + value + "' that is not in the domain that is not 'Null'.");
                            }
                        } else if (value instanceof SimpleSymbolic && attr.getType().getBase().endsWith(Type.BASE_NUMERIC)) {
                            toAssign = SimpleSymbolic.findInTheDomain((SimpleSymbolic) value, attr.getType());
                            if (toAssign == null) {
                                throw new NotInTheDomainException(attr.getType().getDomain(), value,
                                        "Setting value of attribute '" + attr.getName() +
                                                "' for a value '" + value + "' that is not in the domain that is not 'Null'.");
                            }
                        } else {
                            toAssign = value;
                        }
                    } else if (attr.getXTTClass().equals(Attribute.CLASS_GENERAL)) {
                        toAssign = value;
                    }
                } else {
                    throw new NotInTheDomainException(attr.getType().getDomain(), value,
                            "Setting value of attribute '" + attr.getName() +
                                    "' for a value '" + value + "' that is not in the domain that is not 'Null'.");
                }

            }catch(UnknownValueException e){
                // In case whren the value is unknown in some place (eg. Range with null boundaries, set the whole value to Null
                toAssign = new Null();
            }

            if(isTimeLocked()){
                setAmbiguousAttributeValue(toAssign);
            }else {
                this.value = toAssign;
            }

        }



        void resolveAmbiguousAttributeValue(){
            if(!ambiguousValues.isEmpty()){
                this.value = getAmbiguityResolver().resolveCumulativeConclusions(ambiguousValues);
            }
        }

        void clearAmbiguousValues(){
            ambiguousValues.clear();
        }

        void setAmbiguousAttributeValue(Value v){
		    if(v instanceof Null){
		        ambiguousValues.clear();
		        value = v;
            }else {
                ambiguousValues.addLast(v);
            }
        }



	}
	
	
	
	
	
	
}
