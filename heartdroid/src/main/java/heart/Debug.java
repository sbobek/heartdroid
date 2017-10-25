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

/**
 * A class that is responsible for printing debug information.
 * This is a wrapper class for Log class in Android, to provide
 * such functionality in desktop environment.
 *
 */
public class Debug {
	
	public static final String heartTag = "HEART";
	
	/**
	 * Enumerate type representing level of information that should be 
	 * presented to the user of the framework.
	 */
	public enum Level{
		/**
		 * No information is logged
		 */
		SILENT,
		/**
		 * Information about tables that are processed during inference processed is logged
		 */
		TABLES,
		/**
		 * Information about rules that are processed during inference process is logged
		 */
		RULES,
		/**
		 * Information about evaluation of conditions of rules is logged
		 */
		CONDITIONS,
		/**
		 * All information is logged.
		 */
		VERBOS,
        /**
         * No matter what level was set, the warnings are always printed.
         */
        WARNING
	}
	
	/**
	 * The debug level, that can take one of the values defined by {@link Level}.
	 * By default the silent mode is selected.
	 */
	public static Level debugLevel = Level.VERBOS;
	
	/**
	 * The method that logs current action and prints it either to the 
	 * System.err in desktop environment or with Log.d in Android.
	 * 
	 * @param tag a tag that is used for logging in Android. 
	 * This can be skipped in desktop environment.
	 * @param level indicating debug level of the message
	 * @param message that should be logged.
	 */
	public static void debug(String tag, Level level, String message){
		if(level.compareTo(debugLevel) <= 0){
			System.err.println(tag+": "+message);
			//Android
			//Log.d(tag,message);
		}else if(level == Level.WARNING){
            System.err.println(tag+" WARNING: "+message);
        }
	}
	
}
