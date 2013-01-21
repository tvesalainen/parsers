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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;
import org.vesalainen.parser.ParserConstants;
import org.vesalainen.parser.ParserFactory;
import org.vesalainen.parser.ParserInfo;
import org.vesalainen.parser.annotation.GenClassname;
import org.vesalainen.parser.annotation.GrammarDef;
import org.vesalainen.parser.annotation.ParseMethod;
import org.vesalainen.parser.annotation.ParserContext;
import org.vesalainen.parser.annotation.RecoverMethod;
import org.vesalainen.parser.annotation.Rule;
import org.vesalainen.parser.annotation.Rules;
import org.vesalainen.parser.annotation.Terminal;
import org.vesalainen.parser.util.InputReader;
import org.vesalainen.parsers.nmea.ais.EPFDFixTypes;
import org.vesalainen.parsers.nmea.ais.ManeuverIndicator;
import org.vesalainen.parsers.nmea.ais.MessageTypes;
import org.vesalainen.parsers.nmea.ais.NavigationStatus;

/**
 * @author Timo Vesalainen
 * @see <a href="http://catb.org/gpsd/NMEA.html">NMEA Revealed</a>
 * @see <a href="http://gpsd.berlios.de/AIVDM.html">AIVDM/AIVDO protocol decoding</a>
 * @see <a href="doc-files/NMEAParser-statements.html#BNF">BNF Syntax for NMEA</a>
 */
@GenClassname("org.vesalainen.parsers.nmea.NMEAParserImpl")
@GrammarDef()
@Rules({
    @Rule(left="statements", value="statement*"),
    @Rule(left="statement", value="nmeaStatement"),
    @Rule(left="nmeaStatement", value="'\\$' talkerId nmeaSentence '\\*' checksum '\r\n'"),
    @Rule(left="nmeaStatement", value="'\\!AIVDM'  c numberOfSentences c sentenceNumber c sequentialMessageId c channel aisMessage '0\\*' checksum '\r\n'"),
    @Rule(left="nmeaSentence", value="'AAM' c arrivalStatus c waypointStatus c arrivalCircleRadius c waypoint"),
    @Rule(left="nmeaSentence", value="'ALM' c totalNumberOfMessages c messageNumber c satellitePRNNumber c gpsWeekNumber c svHealth c eccentricity c almanacReferenceTime c inclinationAngle c rateOfRightAscension c rootOfSemiMajorAxis c argumentOfPerigee c longitudeOfAscensionNode c meanAnomaly c f0ClockParameter c f1ClockParameter"),
    @Rule(left="nmeaSentence", value="'APA' c status c status2 c crossTrackError c arrivalStatus c waypointStatus c bearingOriginToDestination c waypoint"),
    @Rule(left="nmeaSentence", value="'APB' c status c status2 c crossTrackError c arrivalStatus c waypointStatus c bearingOriginToDestination c waypoint c bearingPresentPositionToDestination c headingToSteerToDestination"),
    @Rule(left="nmeaSentence", value="'BOD' c bearing c bearing c waypointToWaypoint"),
    @Rule(left="nmeaSentence", value="'BWC' c utc c location c bearing c bearing c distanceToWaypoint c waypoint faaModeIndicator"),
    @Rule(left="nmeaSentence", value="'BWR' c utc c location c bearing c bearing c distanceToWaypoint c waypoint"),
    @Rule(left="nmeaSentence", value="'BWW' c bearing c bearing c waypointToWaypoint"),
    @Rule(left="nmeaSentence", value="'DBK' c depthBelowKeel c depthBelowKeel c depthBelowKeel"),
    @Rule(left="nmeaSentence", value="'DBS' c depthBelowSurface c depthBelowSurface c depthBelowSurface"),
    @Rule(left="nmeaSentence", value="'DBT' c depthBelowTransducer c depthBelowTransducer c depthBelowTransducer"),
    @Rule(left="nmeaSentence", value="'DPT' c depthOfWater"),
    @Rule(left="nmeaSentence", value="'GGA' c utc c location c gpsQualityIndicator c numberOfSatellitesInView c horizontalDilutionOfPrecision c antennaAltitude c geoidalSeparation c ageOfDifferentialGPSData c differentialReferenceStationID"),
    @Rule(left="nmeaSentence", value="'GLL' c location c utc c status faaModeIndicator"),
    @Rule(left="nmeaSentence", value="'HDG' c magneticSensorHeading c magneticDeviation c magneticVariation"),
    @Rule(left="nmeaSentence", value="'HDM' c heading"),
    @Rule(left="nmeaSentence", value="'HDT' c heading"),
    @Rule(left="nmeaSentence", value="'MTW' c waterTemperature"),
    @Rule(left="nmeaSentence", value="'MWV' c windAngle c windSpeed c status"),
    @Rule(left="nmeaSentence", value="'R00' c waypoints"),
    @Rule(left="nmeaSentence", value="'RMA' c status c location c timeDifference c speedOverGround c trackMadeGood c magneticVariation"),
    @Rule(left="nmeaSentence", value="'RMB' c status c crossTrackErrorNM c waypointToWaypoint c destinationWaypointLocation c rangeToDestination c bearingToDestination c destinationClosingVelocity c arrivalStatus"),
    @Rule(left="nmeaSentence", value="'RMC' c utc c status c location c speedOverGround c trackMadeGood c date c magneticVariation"),
    @Rule(left="nmeaSentence", value="'RMM' c horizontalDatum"),
    @Rule(left="nmeaSentence", value="'ROT' c rateOfTurn c status"),
    @Rule(left="nmeaSentence", value="'RPM' c rpmSource c rpmSourceNumber c rpm c propellerPitch c status"),
    @Rule(left="nmeaSentence", value="'RSA' c starboardRudderSensor c status c portRudderSensor c status2"),
    @Rule(left="nmeaSentence", value="'RTE' c totalNumberOfMessages c messageNumber c messageMode c waypoints"),
    @Rule(left="nmeaSentence", value="'VHW' c waterHeading c waterHeading c waterSpeed c waterSpeed"),
    @Rule(left="nmeaSentence", value="'VWR' c windDirection c windSpeed c windSpeed c windSpeed"),
    @Rule(left="nmeaSentence", value="'WCV' c velocityToWaypoint c waypoint"),
    @Rule(left="nmeaSentence", value="'WNC' c distanceToWaypoint c distanceToWaypoint c waypointToWaypoint"),
    @Rule(left="nmeaSentence", value="'WPL' c destinationWaypointLocation c waypoint"),
    @Rule(left="nmeaSentence", value="'XTE' c status c status2 c crossTrackError faaModeIndicator"),
    @Rule(left="nmeaSentence", value="'XTR' c crossTrackError"),
    @Rule(left="nmeaSentence", value="'ZDA' c utc c day c month c year c localZoneHours c localZoneMinutes"),
    @Rule(left="rateOfTurn"),
    @Rule(left="waterTemperature"),
    @Rule(left="heading"),
    @Rule(left="magneticSensorHeading"),
    @Rule(left="magneticDeviation"),
    @Rule(left="horizontalDatum"),
    @Rule(left="faaModeIndicator"),
    @Rule(left="messageMode"),
    @Rule(left="distanceToWaypoint"),
    @Rule(left="depthBelowTransducer"),
    @Rule(left="depthBelowSurface"),
    @Rule(left="depthBelowKeel"),
    @Rule(left="f0ClockParameter"),
    @Rule(left="f1ClockParameter"),
    @Rule(left="meanAnomaly"),
    @Rule(left="longitudeOfAscensionNode"),
    @Rule(left="argumentOfPerigee"),
    @Rule(left="rootOfSemiMajorAxis"),
    @Rule(left="rateOfRightAscension"),
    @Rule(left="inclinationAngle"),
    @Rule(left="almanacReferenceTime"),
    @Rule(left="eccentricity"),
    @Rule(left="svHealth"),
    @Rule(left="gpsWeekNumber"),
    @Rule(left="satellitePRNNumber"),
    @Rule(left="messageNumber"),
    @Rule(left="totalNumberOfMessages"),
    @Rule(left="geoidalSeparation", value="c"),
    @Rule(left="ageOfDifferentialGPSData"),
    @Rule(left="differentialReferenceStationID"),
    @Rule(left="status"),
    @Rule(left="status2"),
    @Rule(left="waypointStatus"),
    @Rule(left="arrivalStatus"),
    @Rule(left="date"),
    @Rule(left="utc"),
    @Rule(left="waypoint"),
    @Rule(left="timeDifference", value="c"),
    @Rule(left="arrivalCircleRadius", value="c"),
    @Rule(left="depthOfWater", value="c"),
    @Rule(left="windSpeed"),
    @Rule(left=""),
    @Rule(left=""),
    @Rule(left="destinationWaypointLocation", value="c c c"),
    @Rule(left="location", value="c c c"),
    @Rule(left="trackMadeGood"),
    @Rule(left="speedOverGround"),
    @Rule(left="magneticVariation", value="c"),
    @Rule(left="crossTrackErrorNM", value="c"),
    @Rule(left="crossTrackError", value="c c"),
    @Rule(left="waypointToWaypoint", value="c"),
    @Rule(left="rangeToDestination"),
    @Rule(left="headingToSteerToDestination", value="c"),
    @Rule(left="bearingPresentPositionToDestination", value="c"),
    @Rule(left="bearingOriginToDestination", value="c"),
    @Rule(left="bearingToDestination"),
    @Rule(left="bearing", value="c"),
    @Rule(left="destinationClosingVelocity"),
    @Rule(left="gpsQualityIndicator"),
    @Rule(left="numberOfSatellitesInView"),
    @Rule(left="horizontalDilutionOfPrecision"),
    @Rule(left="antennaAltitude", value="c"),
    @Rule(left="starboardRudderSensor"),
    @Rule(left="portRudderSensor"),
    @Rule(left="rpmSource"),
    @Rule(left="rpmSourceNumber"),
    @Rule(left="rpm"),
    @Rule(left="propellerPitch"),
    @Rule(left="localZoneHours", value="c"),
    @Rule(left="localZoneMinutes", value="c"),
    @Rule(left="windDirection", value="c"),
    @Rule(left="waterHeading", value="c"),
    @Rule(left="waterSpeed", value="c"),
    @Rule(left="windAngle")
})
public abstract class NMEAParser implements ParserInfo
{
    protected void aisType(int messageType, @ParserContext("aisData") AISData aisData)
    {
        aisData.setMessageType(MessageTypes.values()[messageType]);
    }
    protected void aisRepeat(int repeatIndicator, @ParserContext("aisData") AISData aisData)
    {
        aisData.setRepeatIndicator(repeatIndicator);
    }
    protected void aisMmsi(int mmsi, @ParserContext("aisData") AISData aisData)
    {
        aisData.setMMSI(mmsi);
    }
    protected void aisStatus(int status, @ParserContext("aisData") AISData aisData)
    {
        aisData.setStatus(NavigationStatus.values()[status]);
    }
    /**
     *<p> Turn rate is encoded as follows:
     *<p> 0 = not turning
     *<p> 1…126 = turning right at up to 708 degrees per minute or higher
     *<p> 1…-126 = turning left at up to 708 degrees per minute or higher
     *<p> 127 = turning right at more than 5deg/30s (No TI available)
     *<p> -127 = turning left at more than 5deg/30s (No TI available)
     *<p> 128 (80 hex) indicates no turn information available (default)
     *<p>Values between 0 and 708 degrees/min coded by ROTAIS=4.733 * SQRT(ROTsensor) degrees/min where ROTsensor is the Rate of Turn as input by an external Rate of Turn Indicator. ROTAIS is rounded to the nearest integer value. Thus, to decode the field value, divide by 4.733 and then square it. Sign of the field value should be preserved when squaring it, otherwise the left/right indication will be lost.
     * @param turn
     * @param aisData 
     */
    protected void aisTurn(int turn, @ParserContext("aisData") AISData aisData)
    {
        switch (turn)
        {
            case 0:
                aisData.setTurn(0);
                break;
            case 127:
                aisData.setTurn(20);
                break;
            case -127:
                aisData.setTurn(-20);
                break;
            case 128:
                break;
            default:
                float f = turn;
                f = f / 4.733F;
                aisData.setTurn(Math.signum(f)*f*f);
                break;
        }
    }
    protected void aisSpeed(int speed, @ParserContext("aisData") AISData aisData)
    {
        if (speed != 1023)
        {
            float f = speed;
            aisData.setSpeed(f/10F);
        }
    }
    protected void aisAccuracy(int accuracy, @ParserContext("aisData") AISData aisData)
    {
        aisData.setAccuracy(accuracy == 1);
    }
    protected void aisLon(int lon, @ParserContext("aisData") AISData aisData)
    {
        if (lon != 0x6791AC0)
        {
            float f = lon;
            aisData.setLongitude(f / 600000L);
        }
    }
    protected void aisLat(int lat, @ParserContext("aisData") AISData aisData)
    {
        if (lat != 0x3412140)
        {
            float f = lat;
            aisData.setLatitude(f / 600000L);
        }
    }
    protected void aisCourse(int course, @ParserContext("aisData") AISData aisData)
    {
        if (course != 3600)
        {
            float f = course;
            aisData.setCourse(f / 10F);
        }
    }
    protected void aisHeading(int heading, @ParserContext("aisData") AISData aisData)
    {
        if (heading != 511)
        {
            aisData.setHeading(heading);
        }
    }
    protected void aisSecond(int second, @ParserContext("aisData") AISData aisData)
    {
        if (second != 60)
        {
            aisData.setSecond(second);
        }
    }
    protected void aisManeuver(int maneuver, @ParserContext("aisData") AISData aisData)
    {
        aisData.setManeuver(ManeuverIndicator.values()[maneuver]);
    }
    protected void aisRaim(int raim, @ParserContext("aisData") AISData aisData)
    {
        aisData.setRAIM(raim == 1);
    }
    protected void aisRadio(int radio, @ParserContext("aisData") AISData aisData)
    {
        aisData.setRadioStatus(radio);
    }
    protected void aisYear(int year, @ParserContext("aisData") AISData aisData)
    {
        if (year != 0)
        {
            aisData.setYear(year);
        }
    }
    protected void aisMonth(int month, @ParserContext("aisData") AISData aisData)
    {
        if (month != 0)
        {
            aisData.setMonth(month);
        }
    }
    protected void aisDay(int day, @ParserContext("aisData") AISData aisData)
    {
        if (day != 0)
        {
            aisData.setDay(day);
        }
    }
    protected void aisHour(int hour, @ParserContext("aisData") AISData aisData)
    {
        if (hour != 24)
        {
            aisData.setHour(hour);
        }
    }
    protected void aisMinute(int minute, @ParserContext("aisData") AISData aisData)
    {
        if (minute != 60)
        {
            aisData.setMinute(minute);
        }
    }
    protected void aisEpfd(int epfd, @ParserContext("aisData") AISData aisData)
    {
        aisData.setEPFD(EPFDFixTypes.values()[epfd]);
    }
    protected void aisAisVersion(int version, @ParserContext("aisData") AISData aisData)
    {
        aisData.setVersion(version);
    }
    protected void aisImo(int imo, @ParserContext("aisData") AISData aisData)
    {
        aisData.setIMONumber(imo);
    }
    @Rule("letter c")
    protected void channel(
            char channel,
            @ParserContext("aisData") AISData aisData,
            @ParserContext("aisInputStream") AISInputStream aisInputStream
            )
    {
        aisData.setChannel(channel);
        aisInputStream.setDecode(true);
    }
    @Rule("integer")
    protected void sentenceNumber(
            int sentenceNumber,
            @ParserContext("aisData") AISData aisData
            )
    {
        aisData.setSentenceNumber(sentenceNumber);
    }
    @Rule("integer")
    protected void numberOfSentences(
            int numberOfSentences,
            @ParserContext("aisData") AISData aisData
            )
    {
        aisData.setNumberOfSentences(numberOfSentences);
    }
    @Rule("integer")
    protected void sequentialMessageId(
            int sequentialMessageId,
            @ParserContext("aisData") AISData aisData
            )
    {
        aisData.setSequenceMessageId(sequentialMessageId);
    }
    @Rule("integer")
    protected void day(
            int day,
            @ParserContext("clock") Clock clock
            )
    {
        clock.setDay(day);
    }
    @Rule("integer")
    protected void month(
            int month,
            @ParserContext("clock") Clock clock
            )
    {
        clock.setMonth(month);
    }
    @Rule("integer")
    protected void year(
            int year,
            @ParserContext("clock") Clock clock
            )
    {
        clock.setYear(year);
    }
    @Rule("integer")
    protected void localZoneHours(
            int localZoneHours,
            @ParserContext("clock") Clock clock
            )
    {
        clock.setZoneHours(localZoneHours);
    }
    @Rule("integer")
    protected void localZoneMinutes(
            int localZoneMinutes,
            @ParserContext("clock") Clock clock
            )
    {
        clock.setZoneMinutes(localZoneMinutes);
    }
    @Rule("decimal c letter")
    protected void velocityToWaypoint(
            float velocityToWaypoint,
            char unit,
            @ParserContext("data") NMEAData data
            )
    {
        data.setVelocityToWaypoint(velocityToWaypoint, unit);
    }
    @Rule("decimal c letter")
    protected void windDirection(
            float windDirection,
            char unit,
            @ParserContext("data") NMEAData data
            )
    {
        data.setWindDirection(windDirection, unit);
    }
    @Rule("decimal c letter")
    protected void waterHeading(
            float waterHeading,
            char unit,
            @ParserContext("data") NMEAData data
            )
    {
        data.setWaterHeading(waterHeading, unit);
    }
    @Rule("decimal c letter")
    protected void waterSpeed(
            float waterSpeed,
            char unit,
            @ParserContext("data") NMEAData data
            )
    {
        data.setWaterSpeed(waterSpeed, unit);
    }
    @Rule("decimal")
    protected void starboardRudderSensor(
            float starboardRudderSensor,
            @ParserContext("data") NMEAData data
            )
    {
        data.setStarboardRudderSensor(starboardRudderSensor);
    }
    @Rule("decimal")
    protected void portRudderSensor(
            float portRudderSensor,
            @ParserContext("data") NMEAData data
            )
    {
        data.setPortRudderSensor(portRudderSensor);
    }
    @Rule("letter")
    protected void rpmSource(
            char rpmSource,
            @ParserContext("data") NMEAData data
            )
    {
        data.setRpmSource(rpmSource);
    }
    @Rule("integer")
    protected void rpmSourceNumber(
            int rpmSourceNumber,
            @ParserContext("data") NMEAData data
            )
    {
        data.setRpmSourceNumber(rpmSourceNumber);
    }
    @Rule("integer")
    protected void rpm(
            int rpm,
            @ParserContext("data") NMEAData data
            )
    {
        data.setRpm(rpm);
    }
    @Rule("decimal")
    protected void propellerPitch(
            float propellerPitch,
            @ParserContext("data") NMEAData data
            )
    {
        data.setPropellerPitch(propellerPitch);
    }
    @Rule("decimal")
    protected void rateOfTurn(
            float rateOfTurn,
            @ParserContext("data") NMEAData data
            )
    {
        data.setRateOfTurn(rateOfTurn);
    }
    @Rule("decimal c letter")
    protected void windAngle(
            float windAngle,
            char unit,
            @ParserContext("data") NMEAData data
            )
    {
        data.setWindAngle(windAngle, unit);
    }
    @Rule("decimal c letter")
    protected void windSpeed(
            float windSpeed,
            char unit,
            @ParserContext("data") NMEAData data
            )
    {
        data.setWindSpeed(windSpeed, unit);
    }
    @Rule("decimal c letter")
    protected void waterTemperature(
            float waterTemperature,
            char unit,
            @ParserContext("data") NMEAData data
            )
    {
        data.setWaterTemperature(waterTemperature, unit);
    }
    @Rule("decimal c letter")
    protected void heading(
            float heading,
            char unit,
            @ParserContext("data") NMEAData data
            )
    {
        data.setHeading(heading, unit);
    }
    @Rule("decimal")
    protected void magneticSensorHeading(
            float magneticSensorHeading,
            @ParserContext("data") NMEAData data
            )
    {
        data.setMagneticSensorHeading(magneticSensorHeading);
    }
    @Rule("decimal c ew")
    protected void magneticDeviation(
            float magneticDeviation,
            float sign,
            @ParserContext("data") NMEAData data
            )
    {
        data.setMagneticDeviation(sign*magneticDeviation);
    }
    @Rule("decimal c decimal")
    protected void depthOfWater(
            float depth,
            float offset,
            @ParserContext("data") NMEAData data
            )
    {
        data.setDepthOfWater(depth, offset);
    }
    @Rule("stringList")
    protected void waypoints(
            List<String> list,
            @ParserContext("data") NMEAData data
            )
    {
        data.setWaypoints(list);
    }
    @Rule("string")
    protected List<String> stringList(String str)
    {
        List<String> list = new ArrayList<>();
        list.add(str);
        return list;
    }
    @Rule("stringList c string")
    protected List<String> stringList(List<String> list, String str)
    {
        list.add(str);
        return list;
    }
    @Rule("string")
    protected void horizontalDatum(
            String horizontalDatum,
            @ParserContext("data") NMEAData data
            )
    {
        data.setHorizontalDatum(horizontalDatum);
    }
    @Rule("c letter")
    protected void faaModeIndicator(
            char faaModeIndicator,
            @ParserContext("data") NMEAData data
            )
    {
        data.setFAAModeIndicator(faaModeIndicator);
    }
    @Rule("letter")
    protected void messageMode(
            char messageMode,
            @ParserContext("data") NMEAData data
            )
    {
        data.setMessageMode(messageMode);
    }
    
    @Rule("decimal c letter")
    protected void distanceToWaypoint(
            float distanceToWaypoint,
            char units,
            @ParserContext("data") NMEAData data
            )
    {
        data.setDistanceToWaypoint(distanceToWaypoint, units);
    }
    @Rule("decimal c letter")
    protected void depthBelowTransducer(
            float depth,
            char unit,
            @ParserContext("data") NMEAData data
            )
    {
        data.setDepthBelowTransducer(depth, unit);
    }
    @Rule("decimal c letter")
    protected void depthBelowSurface(
            float depth,
            char unit,
            @ParserContext("data") NMEAData data
            )
    {
        data.setDepthBelowSurface(depth, unit);
    }
    @Rule("decimal c letter")
    protected void depthBelowKeel(
            float depth,
            char unit,
            @ParserContext("data") NMEAData data
            )
    {
        data.setDepthBelowKeel(depth, unit);
    }
    @Rule("decimal")
    protected void f1ClockParameter(
            float f1ClockParameter,
            @ParserContext("data") NMEAData data
            )
    {
        data.setF1ClockParameter(f1ClockParameter);
    }
    @Rule("decimal")
    protected void f0ClockParameter(
            float f0ClockParameter,
            @ParserContext("data") NMEAData data
            )
    {
        data.setF0ClockParameter(f0ClockParameter);
    }
    @Rule("decimal")
    protected void meanAnomaly(
            float meanAnomaly,
            @ParserContext("data") NMEAData data
            )
    {
        data.setMeanAnomaly(meanAnomaly);
    }
    @Rule("decimal")
    protected void longitudeOfAscensionNode(
            float longitudeOfAscensionNode,
            @ParserContext("data") NMEAData data
            )
    {
        data.setLongitudeOfAscensionNode(longitudeOfAscensionNode);
    }
    @Rule("decimal")
    protected void argumentOfPerigee(
            float argumentOfPerigee,
            @ParserContext("data") NMEAData data
            )
    {
        data.setArgumentOfPerigee(argumentOfPerigee);
    }
    @Rule("decimal")
    protected void rootOfSemiMajorAxis(
            float rootOfSemiMajorAxis,
            @ParserContext("data") NMEAData data
            )
    {
        data.setRootOfSemiMajorAxis(rootOfSemiMajorAxis);
    }
    @Rule("decimal")
    protected void rateOfRightAscension(
            float rateOfRightAscension,
            @ParserContext("data") NMEAData data
            )
    {
        data.setRateOfRightAscension(rateOfRightAscension);
    }
    @Rule("decimal")
    protected void inclinationAngle(
            float inclinationAngle,
            @ParserContext("data") NMEAData data
            )
    {
        data.setInclinationAngle(inclinationAngle);
    }
    @Rule("decimal")
    protected void almanacReferenceTime(
            float almanacReferenceTime,
            @ParserContext("data") NMEAData data
            )
    {
        data.setAlmanacReferenceTime(almanacReferenceTime);
    }
    @Rule("decimal")
    protected void eccentricity(
            float eccentricity,
            @ParserContext("data") NMEAData data
            )
    {
        data.setEccentricity(eccentricity);
    }
    @Rule("integer")
    protected void svHealth(
            int svHealth,
            @ParserContext("data") NMEAData data
            )
    {
        data.setSvHealth(svHealth);
    }
    @Rule("integer")
    protected void gpsWeekNumber(
            int gpsWeekNumber,
            @ParserContext("data") NMEAData data
            )
    {
        data.setGpsWeekNumber(gpsWeekNumber);
    }
    @Rule("integer")
    protected void satellitePRNNumber(
            int satellitePRNNumber,
            @ParserContext("data") NMEAData data
            )
    {
        data.setSatellitePRNNumber(satellitePRNNumber);
    }
    @Rule("integer")
    protected void messageNumber(
            int messageNumber,
            @ParserContext("data") NMEAData data
            )
    {
        data.setMessageNumber(messageNumber);
    }
    @Rule("integer")
    protected void totalNumberOfMessages(
            int totalNumberOfMessages,
            @ParserContext("data") NMEAData data
            )
    {
        data.setTotalNumberOfMessages(totalNumberOfMessages);
    }
    @Rule("decimal c letter")
    protected void arrivalCircleRadius(
            float arrivalCircleRadius,
            char units,
            @ParserContext("data") NMEAData data
            )
    {
        data.setArrivalCircleRadius(arrivalCircleRadius, units);
    }
    @Rule("decimal c decimal")
    protected void timeDifference(
            float timeDifferenceA,  // uS
            float timeDifferenceB,  // uS
            @ParserContext("data") NMEAData data
            )
    {
        data.setTimeDifference(timeDifferenceA, timeDifferenceB);
    }
    @Rule("string")
    protected void waypoint(
            String waypoint,
            @ParserContext("data") NMEAData data
            )
    {
        data.setWaypoint(waypoint);
    }
    @Rule("decimal")
    protected void utc(
            float utc,             // hhmmss.ss
            @ParserContext("clock") Clock clock
            )
    {
        clock.setTime(utc);
    }
    @Rule("integer")
    protected void date(
            int date,               // ddmmyy
            @ParserContext("clock") Clock clock
            )
    {
        clock.setDate(date);
    }
    @Rule("letter")
    protected void arrivalStatus(
            char arrivalStatus,
            @ParserContext("data") NMEAData data
            )
    {
        data.setArrivalStatus(arrivalStatus);
    }
    @Rule("letter")
    protected void waypointStatus(
            char waypointStatus,
            @ParserContext("data") NMEAData data
            )
    {
        data.setWaypointStatus(waypointStatus);
    }
    @Rule("letter")
    protected void status(
            char status,
            @ParserContext("data") NMEAData data
            )
    {
        data.setStatus(status);
    }
    @Rule("letter")
    protected void status2(
            char status,
            @ParserContext("data") NMEAData data
            )
    {
        data.setStatus2(status);
    }
    @Rule("integer")
    protected void differentialReferenceStationID(
            int differentialReferenceStationID,     //0000-1023            
            @ParserContext("data") NMEAData data
            )
    {
        data.setDifferentialReferenceStationID(differentialReferenceStationID);
    }
    @Rule("integer")
    protected void ageOfDifferentialGPSData(
            int ageOfDifferentialGPSData,   //time in seconds since last SC104 type 1 or 9 update, null field when DGPS is not used
            @ParserContext("data") NMEAData data
            )
    {
        data.setAgeOfDifferentialGPSData(ageOfDifferentialGPSData);
    }
    @Rule("decimal c letter")
    protected void geoidalSeparation(
            float geoidalSeparation,  //the difference between the WGS-84 earth ellipsoid and mean-sea-level (geoid), "-" means mean-sea-level below ellipsoid
            char unitsOfGeoidalSeparation,       // meters
            @ParserContext("data") NMEAData data
            )
    {
        data.setGeoidalSeparation(geoidalSeparation, unitsOfGeoidalSeparation);
    }
    @Rule("decimal c letter")
    protected void antennaAltitude(
            float antennaAltitude,    // above/below mean-sea-level (geoid) (in meters)
            char unitsOfAntennaAltitude,        //meters
            @ParserContext("data") NMEAData data
            )
    {
        data.setAntennaAltitude(antennaAltitude, unitsOfAntennaAltitude);
    }
    @Rule("decimal")
    protected void horizontalDilutionOfPrecision(
            float horizontalDilutionOfPrecision,  // (meters)
            @ParserContext("data") NMEAData data
            )
    {
        data.setHorizontalDilutionOfPrecision(horizontalDilutionOfPrecision);
    }
    @Rule("integer")
    protected void numberOfSatellitesInView(
            int numberOfSatellitesInView,
            @ParserContext("data") NMEAData data
            )
    {
        data.setNumberOfSatellitesInView(numberOfSatellitesInView);
    }
    @Rule("integer")
    protected void gpsQualityIndicator(
            int gpsQualityIndicator,
            @ParserContext("data") NMEAData data
            )
    {
        data.setGpsQualityIndicator(gpsQualityIndicator);
    }
    @Rule("decimal")
    protected void destinationClosingVelocity(
            float destinationClosingVelocity,  // knots
            @ParserContext("data") NMEAData data
            )
    {
        data.setDestinationClosingVelocity(destinationClosingVelocity);
    }
    @Rule("decimal c letter")
    protected void bearing(
            float bearing,  // degrees
            char unit,  // M = Magnetic, T = True
            @ParserContext("data") NMEAData data
            )
    {
        data.setBearing(bearing, unit);
    }
    @Rule("decimal")
    protected void bearingToDestination(
            float bearingToDestination,  // degrees
            @ParserContext("data") NMEAData data
            )
    {
        data.setBearingToDestination(bearingToDestination);
    }
    @Rule("decimal c letter")
    protected void bearingOriginToDestination(
            float bearingOriginToDestination,  // degrees
            char mOrT,  // M = Magnetic, T = True
            @ParserContext("data") NMEAData data
            )
    {
        data.setBearingOriginToDestination(bearingOriginToDestination, mOrT);
    }
    @Rule("decimal c letter")
    protected void bearingPresentPositionToDestination(
            float bearingPresentPositionToDestination,  // degrees
            char mOrT,  // M = Magnetic, T = True
            @ParserContext("data") NMEAData data
            )
    {
        data.setBearingPresentPositionToDestination(bearingPresentPositionToDestination, mOrT);
    }
    @Rule("decimal c letter")
    protected void headingToSteerToDestination(
            float headingToSteerToDestination,  // degrees
            char mOrT,  // M = Magnetic, T = True
            @ParserContext("data") NMEAData data
            )
    {
        data.setHeadingToSteerToDestination(headingToSteerToDestination, mOrT);
    }
    @Rule("decimal")
    protected void rangeToDestination(
            float rangeToDestination,  // NM
            @ParserContext("data") NMEAData data
            )
    {
        data.setRangeToDestination(rangeToDestination);
    }
    @Rule("string c string")
    protected void waypointToWaypoint(
            String toWaypoint,
            String fromWaypoint,
            @ParserContext("data") NMEAData data
            )
    {
        data.setWaypointToWaypoint(toWaypoint, fromWaypoint);
    }
    @Rule("decimal c letter c letter")
    protected void crossTrackError(
            float crossTrackError,  // NM
            char directionToSteer,
            char unit,
            @ParserContext("data") NMEAData data
            )
    {
        data.setCrossTrackError(crossTrackError, directionToSteer, unit);
    }
    @Rule("decimal c letter")
    protected void crossTrackErrorNM(
            float crossTrackError,  // NM
            char directionToSteer,
            @ParserContext("data") NMEAData data
            )
    {
        data.setCrossTrackError(crossTrackError, directionToSteer, 'N');
    }
    @Rule("decimal c ew")
    protected void magneticVariation(
            float magneticVariation,    // degrees
            float mew,
            @ParserContext("data") NMEAData data
            )
    {
        data.setMagneticVariation(mew*magneticVariation);
    }
    @Rule("decimal")
    protected void speedOverGround(
            float speedOverGround,  // knots
            @ParserContext("data") NMEAData data
            )
    {
        data.setSpeedOverGround(speedOverGround);
    }
    @Rule("decimal")
    protected void trackMadeGood(
            float trackMadeGood,  // knots
            @ParserContext("data") NMEAData data
            )
    {
        data.setTrackMadeGood(trackMadeGood);
    }
    @Rule("latitude c ns c longitude c ew")
    protected void location(
            float latitude,
            float ns,
            float longitude,
            float ew,
            @ParserContext("data") NMEAData data
            )
    {
        data.setLocation(ns*latitude, ew*longitude);
    }
    @Rule("latitude c ns c longitude c ew")
    protected void destinationWaypointLocation(
            float latitude,
            float ns,
            float longitude,
            float ew,
            @ParserContext("data") NMEAData data
            )
    {
        data.setDestinationWaypointLocation(ns*latitude, ew*longitude);
    }
    @Rule("letter letter")
    protected void talkerId(char c1, char c2, @ParserContext("data") NMEAData data)
    {
        data.talkerId(c1, c2);
    }
    @Rule("hexAlpha hexAlpha")
    protected void checksum(
            char x1, 
            char x2, 
            @ParserContext("checksum") Checksum checksum,
            @ParserContext("clock") Clock clock,
            @ParserContext("data") NMEAData data
            )
    {
        int sum = 16*parseHex(x1)+parseHex(x2);
        if (sum != checksum.getValue())
        {
            clock.rollback();
            data.rollback("checksum "+Integer.toHexString(sum)+" != "+Integer.toHexString((int)checksum.getValue()));
        }
        else
        {
            clock.commit();
            data.commit();
        }
    }
    @Terminal(expression="[0-9]+\\.[0-9]+")
    protected float latitude(float lat)
    {
        float degrees = (float)Math.floor(lat/100);
        float minutes = lat - 100F*degrees;
        float latitude = degrees + minutes/60F;
        assert latitude >= 0;
        assert latitude <= 90;
        return latitude;
    }
            
    @Terminal(expression="[0-9]+\\.[0-9]+")
    protected float longitude(float lat)
    {
        float degrees = (float)Math.floor(lat/100);
        float minutes = lat - 100F*degrees;
        float longitude = degrees + minutes/60F;
        assert longitude >= 0;
        assert longitude <= 180;
        return longitude;
    }
            
    @Terminal(expression="[NS]")
    protected float ns(char c)
    {
        return 'N' == c ? 1 : -1;
    }
            
    @Terminal(expression="[WE]")
    protected float ew(char c)
    {
        return 'E' == c ? 1 : -1;
    }
            
    @Terminal(expression="[a-zA-Z]")
    protected abstract char letter(char c);
            
    @Terminal(expression="[0-9A-Fa-f]")
    protected abstract char hexAlpha(char x);
         
    @Terminal(expression="[a-zA-Z0-9 \\.\\-]+")
    protected abstract String string(String s);
         
    @Terminal(expression="[\\+\\-]?[0-9]+")
    protected abstract int integer(int i);
         
    @Terminal(expression="[\\+\\-]?[0-9]+(\\.[0-9]+)*")
    protected abstract float decimal(float f);
         
    @Terminal(expression="[\\,]")
    protected abstract void c();
            
    private static final int parseHex(char x)
    {
        switch (x)
        {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return x - '0';
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
                return x - 'A' + 10;
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
                return x - 'a' + 10;
            default:
                throw new IllegalArgumentException(x+" is not hex digit");
        }
    }
    @RecoverMethod
    public void recover(
            @ParserContext("data") NMEAData data,
            @ParserContext(ParserConstants.INPUTREADER) InputReader reader
            ) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        int cc = reader.read();
        while (cc != '\n' && cc != -1)
        {
            sb.append((char)cc);
            cc = reader.read();
        }
        data.rollback("skipping "+sb);
    }
    public void parse(InputStream is, NMEAData data, AISData aisData)
    {
        Checksum checksum = new NMEAChecksum();
        Clock clock = new GPSClock();
        CheckedInputStream checkedInputStream = new CheckedInputStream(is, checksum);
        AISInputStream aisInputStream = new AISInputStream(checkedInputStream);
        parse(aisInputStream, checksum, clock, data, aisData, aisInputStream);
    }
    @ParseMethod(start = "statements", size=80)
    protected abstract void parse(
            InputStream is, 
            @ParserContext("checksum") Checksum checksum, 
            @ParserContext("clock") Clock clock, 
            @ParserContext("data") NMEAData data,
            @ParserContext("aisData") AISData aisData,
            @ParserContext("aisInputStream") AISInputStream aisInputStream
            );
    
    public static NMEAParser newInstance() throws NoSuchMethodException, IOException, NoSuchFieldException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        return (NMEAParser) ParserFactory.getParserInstance(NMEAParser.class);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        try
        {
            String pck = NMEAParser.class.getPackage().getName().replace('.', '/')+"/";
            InputStream is = NMEAParser.class.getClassLoader().getResourceAsStream(pck+"nmea-sample");
            //FileInputStream fis = new FileInputStream("y:\\NMEA data\\2007_05_25_122516.nmea");
            NMEAParser p = NMEAParser.newInstance();
            Tracer tracer = new Tracer();
            p.parse(is, tracer, tracer);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
