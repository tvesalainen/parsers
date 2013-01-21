/*
 * Copyright (C) 2013 Timo Vesalainen
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.vesalainen.parsers.nmea;

import org.vesalainen.parsers.nmea.ais.EPFDFixTypes;
import org.vesalainen.parsers.nmea.ais.ManeuverIndicator;
import org.vesalainen.parsers.nmea.ais.MessageTypes;
import org.vesalainen.parsers.nmea.ais.NavigationStatus;

/**
 *
 * @author Timo Vesalainen
 */
public interface AISData
{   
    public void setMessageType(MessageTypes messageTypes);
    
    /**
     * Number of Sentences (some messages need more then one)
     * @param numberOfSentences 
     */
    public void setNumberOfSentences(int numberOfSentences);
    /**
     * Sentence Number (1 unless it´s a multi-sentence message)
     * @param sentenceNumber 
     */
    public void setSentenceNumber(int sentenceNumber);

    public void setSequenceMessageId(int sequentialMessageId);
    /**
     * The AIS Channel (A or B)
     * @param channel 
     */
    public void setChannel(char channel);
    /**
     * Repeat Indicator. Message repeat count.
     * @param repeatIndicator 
     */
    public void setRepeatIndicator(int repeatIndicator);
    /**
     * Maritime Mobile Service Identity. 9 decimal digits.
     * @see <a href="http://en.wikipedia.org/wiki/Maritime_Mobile_Service_Identity">Maritime Mobile Service Identity</a>
     * @param mmsi 
     */
    public void setMMSI(int mmsi);
    /**
     * Navigation Status
     * @param navigationStatus 
     */
    public void setStatus(NavigationStatus navigationStatus);
    /**
     * Rate of Turn (ROT)
     * @param degreesPerMinute degrees / minute
     */
    public void setTurn(float degreesPerMinute);
    /**
     * Speed Over Ground (SOG). 
     * @param knots speed in knots. value 102.2 indicates 102.2 knots or higher.
     */
    public void setSpeed(float knots);
    /**
     * The position accuracy flag indicates the accuracy of the fix. 
     * A value of true indicates a DGPS-quality fix with an accuracy of &lt; 10ms. false, 
     * the default, indicates an unaugmented GNSS fix with accuracy &gt; 10m.
     * @param accuracy 
     */
    public void setAccuracy(boolean accuracy);
    /**
     * Values up to plus or minus 180 degrees, East = positive, West = negative. 
     * @param degrees longitude in degrees
     */
    public void setLongitude(double degrees);
    /**
     * Values up to plus or minus 90 degrees, North = positive, South = negative.
     * @param degrees latitude in degrees
     */
    public void setLatitude(float degrees);
    /**
     * Course Over Ground (COG). Relative to true north, to 0.1 degree precision.
     * @param cog 
     */
    public void setCourse(float cog);
    /**
     * True Heading (HDG)
     * @param heading 0 to 359 degrees
     */
    public void setHeading(int heading);
    /**
     * Second of UTC timestamp
     * @param second 
     */
    public void setSecond(int second);
    /**
     * Maneuver Indicator
     * @param maneuverIndicator 
     */
    public void setManeuver(ManeuverIndicator maneuverIndicator);
    /**
     * The RAIM flag indicates whether Receiver Autonomous Integrity Monitoring 
     * is being used to check the performance of the EPFD. 
     * false = RAIM not in use(default), true = RAIM in use.
     * @see <a href="http://en.wikipedia.org/wiki/Receiver_Autonomous_Integrity_Monitoring">Receiver autonomous integrity monitoring</a>
     * @param raim 
     */
    public void setRAIM(boolean raim);
    /**
     * Diagnostic information for the radio system.
     * @param radio 
     */
    public void setRadioStatus(int radio);
    /**
     * Year (UTC). UTC, 1-999
     * @param year 
     */
    public void setYear(int year);
    /**
     * Month (UTC). 
     * @param month 1-12
     */
    public void setMonth(int month);
    /**
     * Day (UTC) 1-31
     * @param day 
     */
    public void setDay(int day);
    /**
     * Hour (UTC) 0-23
     * @param hour 
     */
    public void setHour(int hour);
    /**
     * Minute (UTC) 0-59
     * @param minute 
     */
    public void setMinute(int minute);
    /**
     * Type of EPFD
     * @param epfdFixTypes 
     */
    public void setEPFD(EPFDFixTypes epfdFixTypes);
    /**
     * AIS Version. 0 = ITU1371
     * @see <a href="http://www.itu.int/rec/R-REC-M.1371-4-201004-I/en">Technical characteristics for an automatic identification system using time-division multiple access in the VHF maritime mobile band</a>
     * @param version 
     */
    public void setVersion(int version);
    /**
     * IMO ship ID number
     * @param imo 
     */
    public void setIMONumber(int imo);

}
