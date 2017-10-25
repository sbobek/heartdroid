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

/**
 * This is the class that represent a true/false value of an evaluation
 * of a formuale with uncertain value.
 * 
 * @author sbk
 *
 */
public class UncertainTrue {
	private float certinatyFactor;
	
	
	public UncertainTrue(float certaintyFactor) {
		setCertinatyFactor(certaintyFactor);
	}

	/**
	 * @return the certinatyFactor
	 */
	public float getCertinatyFactor() {
		return certinatyFactor;
	}
	/**
	 * @param certinatyFactor the certinatyFactor to set
	 */
	public void setCertinatyFactor(float certinatyFactor) {
		this.certinatyFactor = certinatyFactor;
	}
	
	@Override
	public String toString() {
		return "true ("+ getCertinatyFactor()+")";
	}

}
