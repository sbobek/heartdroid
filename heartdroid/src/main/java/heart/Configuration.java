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

import heart.exceptions.BuilderException;
import heart.uncertainty.ALSVEvaluator;
import heart.uncertainty.ConflictSetFirstWin;
import heart.uncertainty.ConflictSetResolution;
import heart.uncertainty.UncertainTrueEvaluator;

/**
 * This is the class that contains configuration for the inference process for HeaRT
 * It is used to provide appropriate UncertainTrue evaluators, conflict set resolution mechanisms
 * and other parameters that may have the impact on the inference process.
 * 
 * The configuration itself does not define the inference mode. The inference modes are fixed for HeaRT.
 * 
 * @author sbk
 *
 */
public class Configuration{
	private UncertainTrueEvaluator uncertainTrueEvaluator;
	private ConflictSetResolution conflictSetResolution;
	private State initialState;
    private boolean tokenPassingEnabled;
	
	/**
	 * A private constructor that is called by the {@link Builder} object to build
	 * the {@link Configuration} object.
	 * 
	 * @param builder the builder with the parameters
	 */
	private Configuration(Builder builder){
		setConflictSetResolution(builder.getCsr());
		setUncertainTrueEvaluator(builder.getUte());
		setInitialState(builder.getInitialState());
        setTokenPassingEnabled(builder.isTokenPassingEnabled());
	}



	/**
	 * @return the uncertainTrueEvaluator
	 */
	public UncertainTrueEvaluator getUncertainTrueEvaluator() {
		return uncertainTrueEvaluator;
	}

	/**
	 * @param uncertainTrueEvaluator the uncertainTrueEvaluator to set
	 */
	private void setUncertainTrueEvaluator(UncertainTrueEvaluator uncertainTrueEvaluator) {
		this.uncertainTrueEvaluator = uncertainTrueEvaluator;
	}

	/**
	 * @return the conflictSetResolution
	 */
	public ConflictSetResolution getConflictSetResolution() {
		return conflictSetResolution;
	}

	/**
	 * @param conflictSetResolution the conflictSetResolution to set
	 */
	private void setConflictSetResolution(ConflictSetResolution conflictSetResolution) {
		this.conflictSetResolution = conflictSetResolution;
	}
	/**
	 * Returns the initial state for the inference process
	 * @return initial state
	 */
	public State getInitialState() {
		return initialState;
	}
	
	/**
	 * sets the initial state for the inference process
	 * @param initialState a state to be set
	 */
	private void setInitialState(State initialState) {
		this.initialState = initialState;
	}


    public boolean isTokenPassingEnabled() {
        return tokenPassingEnabled;
    }

    public void setTokenPassingEnabled(boolean tokenPassingEnabled) {
        this.tokenPassingEnabled = tokenPassingEnabled;
    }

	public static class Builder{
		private UncertainTrueEvaluator ute;
		private ConflictSetResolution csr;
		private State initialState;
        private boolean tokenPassingEnabled = false;
		
		/**
		 * The method build the {@link Configuration} object that is later user during the inference process.
		 * For the values of the elements that were not specified, the default values are used. 
		 * See the {@link #getDefaultConfiguration()} for details on the default values of the elements.
		 * 
		 * @return The {@link Configuration} object.
		 * @throws BuilderException an exception thrown then some parameters are set 
		 * so that they cannot build proper {@link Configuration}.
		 */
		public Configuration build() throws BuilderException{
			if(ute == null) setUte( new ALSVEvaluator());
			if(csr == null) setCsr(new ConflictSetFirstWin());
			if(initialState == null) setInitialState(new State());
			
			return new Configuration(this);
		}
		
		/**
		 * The method returns default configuration for the inference process.
		 * This includes
		 * <ul>
		 * <li> {@link UncertainTrueEvaluator} to be {@link ALSVEvaluator}
		 * <li> {@link ConflictSetResolution} to be {@link ConflictSetFirstWin}
		 * <li> initial state to be empty {@link State}.
		 * </ul>
		 * 
		 * @return The {@link Configuration} object.
		 */
		public Configuration getDefaultConfiguration(){
			setUte(new ALSVEvaluator());
			setCsr(new ConflictSetFirstWin());
			return new Configuration(this);
		}
		
		/**
		 * The method sets the {@link ConflictSetResolution} object to be used while evaluating rules.
		 * 
		 * @param csr the {@link ConflictSetResolution} object
		 * @return the {@link Builder} reference
		 */
		public Builder setCsr(ConflictSetResolution csr) {
			this.csr = csr;
			return this;
		}
		
		/**
		 * The method sets the {@link UncertainTrueEvaluator} object to be used while evaluating 
		 * ALSV(FD) formulae.
		 * 
		 * @param ute the {@link UncertainTrueEvaluator} object
		 * @return the {@link Builder} reference
		 */
		public Builder setUte(UncertainTrueEvaluator ute) {
			this.ute = ute;
			return this;
		}
		/**
		 * The method gets the {@link UncertainTrueEvaluator} object previously set.
		 * 
		 * @return the {@link UncertainTrueEvaluator} object that was set
		 */
		public UncertainTrueEvaluator getUte() {
			return ute;
		}
		
		/**
		 * 
		 * @return the csr
		 */
		public ConflictSetResolution getCsr() {
			return csr;
		}
		/**
		 * 
		 * @return the initial state
		 */
		public State getInitialState() {
			return initialState;
		}
		
		/**
		 * Sets the initial state given as a parameter
		 * @param initialState
		 * @return the builder
		 */
		public Builder setInitialState(State initialState) {
			this.initialState = initialState;
			return this;
		}

        public boolean isTokenPassingEnabled() {
            return tokenPassingEnabled;
        }

        public void setTokenPassingEnabled(boolean tokenPassingEnabled) {
            this.tokenPassingEnabled = tokenPassingEnabled;
        }
    }
}