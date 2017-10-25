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

import heart.xtt.Decision;
import heart.xtt.Rule;

/**
 * 
 * @author sbk
 * The interface that has to be implemented by the class that is specified to be
 * an action in the  {@link Rule}.
 * 
 * Actions are executed only if the conditional part of the rule is satisfied.
 * Actions are not {@link Decision}. They should not be used to change attributes values.
 * 
 *
 */
public interface Action {
	/**
	 * Method that implements the action to execute.
	 */
     public void execute(State currentState);
}
