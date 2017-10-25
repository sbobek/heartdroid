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

import heart.xtt.Attribute;
import heart.xtt.XTTModel;

/**
 * 
 * @author sbk
 * 
 * An interface that has to be implemented by the class that is specified by the
 * {@link Attribute#setCallback(String)} method. The callback is also specified in
 * the HMR (or HML) code in the attribute definition.
 * 
 * Callbacks are called automatically in cases when the value of the attribute is not 
 * set.
 *
 */
public interface Callback {
	/**
	 * Method that implements the callback.
	 *
	 * @param wmm The working memory object that contains attributes values. 
	 * This should be used to set the attribute value.
	 */
	public void execute(Attribute subject, WorkingMemory wmm);
}
