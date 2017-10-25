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


package heart.exceptions;

import heart.alsvfd.SetValue;
import heart.alsvfd.Value;

/**
 * The class represents exception thrown when a value in an ALSV(FD) formulae
 * does not belong to the domain that it should.
 * 
 * @author sbk
 *
 */
public class NotInTheDomainException extends Exception{
	/**
	 * A value representing a domain that is concerned
	 */
	private SetValue domain;
	
	/**
	 * A value that caused a problem
	 */
	private Value value;
	
	
	/**
	 * Constructor of an exception
	 * @param domain
	 * @param value
	 * @param message
	 */
	public NotInTheDomainException(SetValue domain, Value value, String message) {
		super(message);
		this.domain = domain;
		this.value = value;
	}

	public SetValue getDomain() {
		return domain;
	}

	public Value getValue() {
		return value;
	}


}
