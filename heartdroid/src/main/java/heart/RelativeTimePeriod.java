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
 */
public class RelativeTimePeriod {
    /**
     * Starting times of the period (inclusive)
     */
    RelativeTimestamp from;

    /**
     * Ending point of the period (inclusive)
     */
    RelativeTimestamp to;

    /**
     * This is only applicable if times are in milliseconds.
     */
    long step;

    public RelativeTimePeriod(long from, long to, long step, RelativeTimestamp.TimeType type){
        this.from = new RelativeTimestamp(from,type);
        this.to  = new RelativeTimestamp(to, type);
        this.step = step;
    }


    public long getStep() {
        return step;
    }

    public RelativeTimestamp getFrom() {
        return from;
    }

    public RelativeTimestamp getTo() {
        return to;
    }

    @Override
    public String toString() {
        String fromString = Long.toString(from.getRelativeTimeDifference());
        String toString = Long.toString(to.getRelativeTimeDifference());
        String unit = "";
        if(getFrom().getTimeType() ==  RelativeTimestamp.TimeType.MILISCOUNT){
            if(-getFrom().getRelativeTimeDifference() < RelativeTimestamp.SECOND) {
                fromString = Double.toString(from.getRelativeTimeDifference());
                toString = Double.toString(to.getRelativeTimeDifference());
                unit = "ms";
            }else if(-getFrom().getRelativeTimeDifference() < (double)RelativeTimestamp.MINUTE){
                fromString = Double.toString(from.getRelativeTimeDifference()/(double)RelativeTimestamp.SECOND);
                toString = Double.toString(to.getRelativeTimeDifference()/(double)RelativeTimestamp.SECOND);
                unit = "s";
            }else if(-getFrom().getRelativeTimeDifference() < (double)RelativeTimestamp.HOUR){
                fromString = Double.toString(from.getRelativeTimeDifference()/(double)RelativeTimestamp.MINUTE);
                toString = Double.toString(to.getRelativeTimeDifference()/(double)RelativeTimestamp.MINUTE);
                unit = "m";
            }else{
                fromString = Double.toString(from.getRelativeTimeDifference()/(double)RelativeTimestamp.HOUR);
                toString = Double.toString(to.getRelativeTimeDifference()/(double)RelativeTimestamp.HOUR);
                unit = "h";
            }
        }
        return fromString+unit+" to "+toString;
    }
}
