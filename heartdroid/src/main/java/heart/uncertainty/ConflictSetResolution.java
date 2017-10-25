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


package heart.uncertainty;

import heart.xtt.Rule;

import java.util.LinkedList;
import java.util.AbstractMap.SimpleEntry;

public interface ConflictSetResolution {
	/**
	 * This method performs conflict set resolution and returns rules that should be fired.
	 * 
	 * @param cs the conflict set containing all rules that were designated to fire by the reasoning
	 * algorithm
	 * @return rules to be fired after the conflict set resolution together with the certainty of the decision part.
	 * Note that this is not the certainty of the rule itself, but the certainty of the rule with respect to 
	 * the certainty of its conditions being true.
	 */
	public LinkedList<SimpleEntry<Rule, UncertainTrue>>  resolveConflictSet(ConflictSet cs);

}
