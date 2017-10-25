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
 * Created by sbk on 19.12.14.
 * This class represents relative timestamp starting from the current point in time.
 * Thus, its values cannot be greater than 0, which represents current point in time.
 *
 */
public class RelativeTimestamp {
    public static final long SECOND = 1000;
    public static final long MINUTE = 60000;
    public static final long HOUR = 3600000;
    /**
     * RelativeTimestamp  either in milliseconds or in the number of past states.
     */
    private long relativeTimeDifference;

    /**
     * Variable representing what is the unit of time.
     * It can be either (@link TimeType#STATECOUNT} or {@link TimeType#MILISCOUNT}.
     */
    private TimeType timeType;

    /**
     * It represents a unit of time.
     * It allows to distinguish between times given as the number of states and the times
     * given as a number of milliseconds
     */
    public static enum TimeType{
        STATECOUNT,
        MILISCOUNT;
    }

    public RelativeTimestamp(long relativeTimeDifference, TimeType type) throws NumberFormatException{
        this.relativeTimeDifference = relativeTimeDifference;
        this.timeType = type;
    }

    public long getRelativeTimeDifference() {
        return relativeTimeDifference;
    }

    public TimeType getTimeType() {
        return timeType;
    }
}
