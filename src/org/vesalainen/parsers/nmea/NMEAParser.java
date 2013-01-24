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
import org.vesalainen.parsers.nmea.ais.AreaNoticeDescription;
import org.vesalainen.parsers.nmea.ais.BeaufortScale;
import org.vesalainen.parsers.nmea.ais.CargoUnitCodes;
import org.vesalainen.parsers.nmea.ais.CodesForShipType;
import org.vesalainen.parsers.nmea.ais.EPFDFixTypes;
import org.vesalainen.parsers.nmea.ais.ExtensionUnit;
import org.vesalainen.parsers.nmea.ais.ManeuverIndicator;
import org.vesalainen.parsers.nmea.ais.MarineTrafficSignals;
import org.vesalainen.parsers.nmea.ais.MessageTypes;
import org.vesalainen.parsers.nmea.ais.MooringPosition;
import org.vesalainen.parsers.nmea.ais.NavigationStatus;
import org.vesalainen.parsers.nmea.ais.PrecipitationTypes;
import org.vesalainen.parsers.nmea.ais.RouteTypeCodes;
import org.vesalainen.parsers.nmea.ais.ServiceStatus;
import org.vesalainen.parsers.nmea.ais.SubareaType;
import org.vesalainen.parsers.nmea.ais.TargetIdentifierType;
import org.vesalainen.parsers.nmea.ais.WMOCode45501;

/**
 * @author Timo Vesalainen
 * @see <a href="http://catb.org/gpsd/NMEA.html">NMEA Revealed</a>
 * @see <a href="http://gpsd.berlios.de/AIVDM.html">AIVDM/AIVDO protocol
 * decoding</a>
 * @see <a href="doc-files/NMEAParser-statements.html#BNF">BNF Syntax for
 * NMEA</a>
 */
@GenClassname("org.vesalainen.parsers.nmea.NMEAParserImpl")
@GrammarDef()
@Rules(
{
    @Rule(left = "statements", value = "statement*"),
    @Rule(left = "statement", value = "nmeaStatement"),
    @Rule(left = "nmeaStatement", value = "'\\$' talkerId nmeaSentence '\\*' checksum '\r\n'"),
    @Rule(left = "nmeaStatement", value = "'\\!AIVDM'  c numberOfSentences c sentenceNumber c sequentialMessageId c channel aisMessage '0\\*' checksum '\r\n'"),
    @Rule(left = "nmeaSentence", value = "'AAM' c arrivalStatus c waypointStatus c arrivalCircleRadius c waypoint"),
    @Rule(left = "nmeaSentence", value = "'ALM' c totalNumberOfMessages c messageNumber c satellitePRNNumber c gpsWeekNumber c svHealth c eccentricity c almanacReferenceTime c inclinationAngle c rateOfRightAscension c rootOfSemiMajorAxis c argumentOfPerigee c longitudeOfAscensionNode c meanAnomaly c f0ClockParameter c f1ClockParameter"),
    @Rule(left = "nmeaSentence", value = "'APA' c status c status2 c crossTrackError c arrivalStatus c waypointStatus c bearingOriginToDestination c waypoint"),
    @Rule(left = "nmeaSentence", value = "'APB' c status c status2 c crossTrackError c arrivalStatus c waypointStatus c bearingOriginToDestination c waypoint c bearingPresentPositionToDestination c headingToSteerToDestination"),
    @Rule(left = "nmeaSentence", value = "'BOD' c bearing c bearing c waypointToWaypoint"),
    @Rule(left = "nmeaSentence", value = "'BWC' c utc c location c bearing c bearing c distanceToWaypoint c waypoint faaModeIndicator"),
    @Rule(left = "nmeaSentence", value = "'BWR' c utc c location c bearing c bearing c distanceToWaypoint c waypoint"),
    @Rule(left = "nmeaSentence", value = "'BWW' c bearing c bearing c waypointToWaypoint"),
    @Rule(left = "nmeaSentence", value = "'DBK' c depthBelowKeel c depthBelowKeel c depthBelowKeel"),
    @Rule(left = "nmeaSentence", value = "'DBS' c depthBelowSurface c depthBelowSurface c depthBelowSurface"),
    @Rule(left = "nmeaSentence", value = "'DBT' c depthBelowTransducer c depthBelowTransducer c depthBelowTransducer"),
    @Rule(left = "nmeaSentence", value = "'DPT' c depthOfWater"),
    @Rule(left = "nmeaSentence", value = "'GGA' c utc c location c gpsQualityIndicator c numberOfSatellitesInView c horizontalDilutionOfPrecision c antennaAltitude c geoidalSeparation c ageOfDifferentialGPSData c differentialReferenceStationID"),
    @Rule(left = "nmeaSentence", value = "'GLL' c location c utc c status faaModeIndicator"),
    @Rule(left = "nmeaSentence", value = "'HDG' c magneticSensorHeading c magneticDeviation c magneticVariation"),
    @Rule(left = "nmeaSentence", value = "'HDM' c heading"),
    @Rule(left = "nmeaSentence", value = "'HDT' c heading"),
    @Rule(left = "nmeaSentence", value = "'MTW' c waterTemperature"),
    @Rule(left = "nmeaSentence", value = "'MWV' c windAngle c windSpeed c status"),
    @Rule(left = "nmeaSentence", value = "'R00' c waypoints"),
    @Rule(left = "nmeaSentence", value = "'RMA' c status c location c timeDifference c speedOverGround c trackMadeGood c magneticVariation"),
    @Rule(left = "nmeaSentence", value = "'RMB' c status c crossTrackErrorNM c waypointToWaypoint c destinationWaypointLocation c rangeToDestination c bearingToDestination c destinationClosingVelocity c arrivalStatus"),
    @Rule(left = "nmeaSentence", value = "'RMC' c utc c status c location c speedOverGround c trackMadeGood c date c magneticVariation"),
    @Rule(left = "nmeaSentence", value = "'RMM' c horizontalDatum"),
    @Rule(left = "nmeaSentence", value = "'ROT' c rateOfTurn c status"),
    @Rule(left = "nmeaSentence", value = "'RPM' c rpmSource c rpmSourceNumber c rpm c propellerPitch c status"),
    @Rule(left = "nmeaSentence", value = "'RSA' c starboardRudderSensor c status c portRudderSensor c status2"),
    @Rule(left = "nmeaSentence", value = "'RTE' c totalNumberOfMessages c messageNumber c messageMode c waypoints"),
    @Rule(left = "nmeaSentence", value = "'VHW' c waterHeading c waterHeading c waterSpeed c waterSpeed"),
    @Rule(left = "nmeaSentence", value = "'VWR' c windDirection c windSpeed c windSpeed c windSpeed"),
    @Rule(left = "nmeaSentence", value = "'WCV' c velocityToWaypoint c waypoint"),
    @Rule(left = "nmeaSentence", value = "'WNC' c distanceToWaypoint c distanceToWaypoint c waypointToWaypoint"),
    @Rule(left = "nmeaSentence", value = "'WPL' c destinationWaypointLocation c waypoint"),
    @Rule(left = "nmeaSentence", value = "'XTE' c status c status2 c crossTrackError faaModeIndicator"),
    @Rule(left = "nmeaSentence", value = "'XTR' c crossTrackError"),
    @Rule(left = "nmeaSentence", value = "'ZDA' c utc c day c month c year c localZoneHours c localZoneMinutes"),
    @Rule(left = "rateOfTurn"),
    @Rule(left = "waterTemperature"),
    @Rule(left = "heading"),
    @Rule(left = "magneticSensorHeading"),
    @Rule(left = "magneticDeviation"),
    @Rule(left = "horizontalDatum"),
    @Rule(left = "faaModeIndicator"),
    @Rule(left = "messageMode"),
    @Rule(left = "distanceToWaypoint"),
    @Rule(left = "depthBelowTransducer"),
    @Rule(left = "depthBelowSurface"),
    @Rule(left = "depthBelowKeel"),
    @Rule(left = "f0ClockParameter"),
    @Rule(left = "f1ClockParameter"),
    @Rule(left = "meanAnomaly"),
    @Rule(left = "longitudeOfAscensionNode"),
    @Rule(left = "argumentOfPerigee"),
    @Rule(left = "rootOfSemiMajorAxis"),
    @Rule(left = "rateOfRightAscension"),
    @Rule(left = "inclinationAngle"),
    @Rule(left = "almanacReferenceTime"),
    @Rule(left = "eccentricity"),
    @Rule(left = "svHealth"),
    @Rule(left = "gpsWeekNumber"),
    @Rule(left = "satellitePRNNumber"),
    @Rule(left = "messageNumber"),
    @Rule(left = "totalNumberOfMessages"),
    @Rule(left = "geoidalSeparation", value = "c"),
    @Rule(left = "ageOfDifferentialGPSData"),
    @Rule(left = "differentialReferenceStationID"),
    @Rule(left = "status"),
    @Rule(left = "status2"),
    @Rule(left = "waypointStatus"),
    @Rule(left = "arrivalStatus"),
    @Rule(left = "date"),
    @Rule(left = "utc"),
    @Rule(left = "waypoint"),
    @Rule(left = "timeDifference", value = "c"),
    @Rule(left = "arrivalCircleRadius", value = "c"),
    @Rule(left = "depthOfWater", value = "c"),
    @Rule(left = "windSpeed"),
    @Rule(left = ""),
    @Rule(left = ""),
    @Rule(left = "destinationWaypointLocation", value = "c c c"),
    @Rule(left = "location", value = "c c c"),
    @Rule(left = "trackMadeGood"),
    @Rule(left = "speedOverGround"),
    @Rule(left = "magneticVariation", value = "c"),
    @Rule(left = "crossTrackErrorNM", value = "c"),
    @Rule(left = "crossTrackError", value = "c c"),
    @Rule(left = "waypointToWaypoint", value = "c"),
    @Rule(left = "rangeToDestination"),
    @Rule(left = "headingToSteerToDestination", value = "c"),
    @Rule(left = "bearingPresentPositionToDestination", value = "c"),
    @Rule(left = "bearingOriginToDestination", value = "c"),
    @Rule(left = "bearingToDestination"),
    @Rule(left = "bearing", value = "c"),
    @Rule(left = "destinationClosingVelocity"),
    @Rule(left = "gpsQualityIndicator"),
    @Rule(left = "numberOfSatellitesInView"),
    @Rule(left = "horizontalDilutionOfPrecision"),
    @Rule(left = "antennaAltitude", value = "c"),
    @Rule(left = "starboardRudderSensor"),
    @Rule(left = "portRudderSensor"),
    @Rule(left = "rpmSource"),
    @Rule(left = "rpmSourceNumber"),
    @Rule(left = "rpm"),
    @Rule(left = "propellerPitch"),
    @Rule(left = "localZoneHours", value = "c"),
    @Rule(left = "localZoneMinutes", value = "c"),
    @Rule(left = "windDirection", value = "c"),
    @Rule(left = "waterHeading", value = "c"),
    @Rule(left = "waterSpeed", value = "c"),
    @Rule(left = "windAngle")
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
     * <p> Turn rate is encoded as follows: <p> 0 = not turning <p> 1…126 =
     * turning right at up to 708 degrees per minute or higher <p> 1…-126 =
     * turning left at up to 708 degrees per minute or higher <p> 127 = turning
     * right at more than 5deg/30s (No TI available) <p> -127 = turning left at
     * more than 5deg/30s (No TI available) <p> 128 (80 hex) indicates no turn
     * information available (default) <p>Values between 0 and 708 degrees/min
     * coded by ROTAIS=4.733 * SQRT(ROTsensor) degrees/min where ROTsensor is
     * the Rate of Turn as input by an external Rate of Turn Indicator. ROTAIS
     * is rounded to the nearest integer value. Thus, to decode the field value,
     * divide by 4.733 and then square it. Sign of the field value should be
     * preserved when squaring it, otherwise the left/right indication will be
     * lost.
     *
     * @param turn
     * @param aisData
     */
    protected void aisTurn_I3(int turn, @ParserContext("aisData") AISData aisData)
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
                aisData.setTurn(Math.signum(f) * f * f);
                break;
        }
    }

    protected void aisSpeed_U1(int speed, @ParserContext("aisData") AISData aisData)
    {
        if (speed != 1023)
        {
            float f = speed;
            aisData.setSpeed(f / 10F);
        }
    }

    protected void aisAccuracy(int accuracy, @ParserContext("aisData") AISData aisData)
    {
        aisData.setAccuracy(accuracy == 1);
    }

    protected void aisLon_I4(int lon, @ParserContext("aisData") AISData aisData)
    {
        if (lon != 0x6791AC0)
        {
            float f = lon;
            aisData.setLongitude(f / 600000L);
        }
    }

    protected void aisLat_I4(int lat, @ParserContext("aisData") AISData aisData)
    {
        if (lat != 0x3412140)
        {
            float f = lat;
            aisData.setLatitude(f / 600000L);
        }
    }

    protected void aisLon_I3(int lon, @ParserContext("aisData") AISData aisData)
    {
        if (lon != 0x6791AC0)
        {
            float f = lon;
            aisData.setLongitude(f / 60000L);
        }
    }

    protected void aisLat_I3(int lat, @ParserContext("aisData") AISData aisData)
    {
        if (lat != 0x3412140)
        {
            float f = lat;
            aisData.setLatitude(f / 60000L);
        }
    }

    protected void aisCourse_U1(int course, @ParserContext("aisData") AISData aisData)
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

    protected void aisCallsign(InputReader reader, @ParserContext("aisData") AISData aisData)
    {
        aisData.setCallSign(fromSixBitCharacters(reader));
    }

    protected void aisShipname(InputReader reader, @ParserContext("aisData") AISData aisData)
    {
        aisData.setVesselName(fromSixBitCharacters(reader));
    }

    protected void aisShiptype(int shiptype, @ParserContext("aisData") AISData aisData)
    {
        aisData.setShipType(CodesForShipType.values()[shiptype]);
    }

    protected void aisToBow(int dimension, @ParserContext("aisData") AISData aisData)
    {
        aisData.setDimensionToBow(dimension);
    }

    protected void aisToStern(int dimension, @ParserContext("aisData") AISData aisData)
    {
        aisData.setDimensionToStern(dimension);
    }

    protected void aisToPort(int dimension, @ParserContext("aisData") AISData aisData)
    {
        aisData.setDimensionToPort(dimension);
    }

    protected void aisToStarboard(int dimension, @ParserContext("aisData") AISData aisData)
    {
        aisData.setDimensionToStarboard(dimension);
    }

    protected void aisDraught_U1(int draught, @ParserContext("aisData") AISData aisData)
    {
        float f = draught;
        aisData.setDraught(f / 10F);
    }

    protected void aisDestination(InputReader reader, @ParserContext("aisData") AISData aisData)
    {
        aisData.setDestination(fromSixBitCharacters(reader));
    }

    protected void aisDte(int dte, @ParserContext("aisData") AISData aisData)
    {
        aisData.setDTE(dte != 1);
    }

    protected void aisSeqno(int seq, @ParserContext("aisData") AISData aisData)
    {
        aisData.setSequenceNumber(seq);
    }

    protected void aisDestMmsi(int mmsi, @ParserContext("aisData") AISData aisData)
    {
        aisData.setDestinationMMSI(mmsi);
    }

    protected void aisRetransmit(int retransmit, @ParserContext("aisData") AISData aisData)
    {
        aisData.setRetransmit(retransmit == 1);
    }

    protected void aisDac(int dac, @ParserContext("aisData") AISData aisData)
    {
        aisData.setDAC(dac);
    }

    protected void aisFid(int fid, @ParserContext("aisData") AISData aisData)
    {
        aisData.setFID(fid);
    }

    protected void aisLastport(InputReader reader, @ParserContext("aisData") AISData aisData)
    {
        aisData.setLastPort(fromSixBitCharacters(reader));
    }

    protected void aisLmonth(int month, @ParserContext("aisData") AISData aisData)
    {
        if (month != 0)
        {
            aisData.setLastPortMonth(month);
        }
    }

    protected void aisLday(int day, @ParserContext("aisData") AISData aisData)
    {
        if (day != 0)
        {
            aisData.setLastPortDay(day);
        }
    }

    protected void aisLhour(int hour, @ParserContext("aisData") AISData aisData)
    {
        if (hour != 24)
        {
            aisData.setLastPortHour(hour);
        }
    }

    protected void aisLminute(int minute, @ParserContext("aisData") AISData aisData)
    {
        if (minute != 60)
        {
            aisData.setLastPortMinute(minute);
        }
    }

    protected void aisNextport(InputReader reader, @ParserContext("aisData") AISData aisData)
    {
        aisData.setNextPort(fromSixBitCharacters(reader));
    }

    protected void aisNmonth(int month, @ParserContext("aisData") AISData aisData)
    {
        if (month != 0)
        {
            aisData.setNextPortMonth(month);
        }
    }

    protected void aisNday(int day, @ParserContext("aisData") AISData aisData)
    {
        if (day != 0)
        {
            aisData.setNextPortDay(day);
        }
    }

    protected void aisNhour(int hour, @ParserContext("aisData") AISData aisData)
    {
        if (hour != 24)
        {
            aisData.setNextPortHour(hour);
        }
    }

    protected void aisNminute(int minute, @ParserContext("aisData") AISData aisData)
    {
        if (minute != 60)
        {
            aisData.setNextPortMinute(minute);
        }
    }

    protected void aisDangerous(InputReader reader, @ParserContext("aisData") AISData aisData)
    {
        aisData.setMainDangerousGood(fromSixBitCharacters(reader));
    }

    protected void aisImdcat(InputReader reader, @ParserContext("aisData") AISData aisData)
    {
        aisData.setIMDCategory(fromSixBitCharacters(reader));
    }

    protected void aisUnid(int unid, @ParserContext("aisData") AISData aisData)
    {
        aisData.UNNumber(unid);
    }

    protected void aisAmount(int amount, @ParserContext("aisData") AISData aisData)
    {
        aisData.AmountOfCargo(amount);
    }

    protected void aisUnit(int unit, @ParserContext("aisData") AISData aisData)
    {
        aisData.setUnitOfQuantity(CargoUnitCodes.values()[unit]);
    }

    protected void aisFromHour(int hour, @ParserContext("aisData") AISData aisData)
    {
        if (hour != 24)
        {
            aisData.setFromHour(hour);
        }
    }

    protected void aisFromMin(int minute, @ParserContext("aisData") AISData aisData)
    {
        if (minute != 60)
        {
            aisData.setFromMinute(minute);
        }
    }

    protected void aisToHour(int hour, @ParserContext("aisData") AISData aisData)
    {
        if (hour != 24)
        {
            aisData.setToHour(hour);
        }
    }

    protected void aisToMin(int minute, @ParserContext("aisData") AISData aisData)
    {
        if (minute != 60)
        {
            aisData.setToMinute(minute);
        }
    }

    protected void aisCdir(int currentDirection, @ParserContext("aisData") AISData aisData)
    {
        if (currentDirection != 360)
        {
            aisData.setCurrentDirection(currentDirection);
        }
    }

    protected void aisCspeed_U1(int currentSpeed, @ParserContext("aisData") AISData aisData)
    {
        if (currentSpeed != 127)
        {
            float f = currentSpeed;
            aisData.setCurrentSpeed(f / 10F);
        }
    }

    protected void aisPersons(int persons, @ParserContext("aisData") AISData aisData)
    {
        if (persons != 0)
        {
            aisData.setPersonsOnBoard(persons);
        }
    }

    protected void aisLinkage(int id, @ParserContext("aisData") AISData aisData)
    {
        aisData.setLinkage(id);
    }

    protected void aisPortname(InputReader reader, @ParserContext("aisData") AISData aisData)
    {
        aisData.setPortname(fromSixBitCharacters(reader));
    }

    protected void aisNotice(int notice, @ParserContext("aisData") AISData aisData)
    {
        aisData.setAreaNotice(AreaNoticeDescription.values()[notice]);
    }

    protected void aisDuration(int duration, @ParserContext("aisData") AISData aisData)
    {
        if (duration != 262143)
        {
            aisData.setDuration(duration);
        }
    }

    protected void aisShape(int shape, @ParserContext("aisData") AISData aisData)
    {
        aisData.setShape(SubareaType.values()[shape]);
    }

    protected void aisScale(int scale, @ParserContext("aisData") AISData aisData)
    {
        aisData.setScale(scale);
    }

    protected void aisPrecision(int precision, @ParserContext("aisData") AISData aisData)
    {
        aisData.setPrecision(precision);
    }

    protected void aisRadius(int radius, @ParserContext("aisData") AISData aisData)
    {
        aisData.setRadius(radius);
    }

    protected void aisEast(int east, @ParserContext("aisData") AISData aisData)
    {
        aisData.setEast(east);
    }

    protected void aisNorth(int north, @ParserContext("aisData") AISData aisData)
    {
        aisData.setNorth(north);
    }

    protected void aisOrientation(int orientation, @ParserContext("aisData") AISData aisData)
    {
        aisData.setOrientation(orientation);
    }

    protected void aisLeft(int left, @ParserContext("aisData") AISData aisData)
    {
        aisData.setLeft(left);
    }

    protected void aisRight(int right, @ParserContext("aisData") AISData aisData)
    {
        aisData.setRight(right);
    }

    protected void aisBearing(int bearing, @ParserContext("aisData") AISData aisData)
    {
        aisData.setBearing(bearing);
    }

    protected void aisDistance(int distance, @ParserContext("aisData") AISData aisData)
    {
        aisData.setDistance(distance);
    }

    protected void aisText(InputReader reader, @ParserContext("aisData") AISData aisData)
    {
        aisData.setText(fromSixBitCharacters(reader));
    }

    protected void aisBerthLength(int meters, @ParserContext("aisData") AISData aisData)
    {
        aisData.setBerthLength(meters);
    }

    protected void aisBerthDepth_U1(int meters, @ParserContext("aisData") AISData aisData)
    {
        float f = meters;
        aisData.setBerthDepth(f / 10F);
    }

    protected void aisPosition(int position, @ParserContext("aisData") AISData aisData)
    {
        aisData.setMooringPosition(MooringPosition.values()[position]);
    }

    protected void aisAvailability(int available, @ParserContext("aisData") AISData aisData)
    {
        aisData.setServicesAvailability(available == 1);
    }

    protected void aisAgent(int status, @ParserContext("aisData") AISData aisData)
    {
        aisData.setAgentServiceStatus(ServiceStatus.values()[status]);
    }

    protected void aisFuel(int status, @ParserContext("aisData") AISData aisData)
    {
        aisData.setFuelServiceStatus(ServiceStatus.values()[status]);
    }

    protected void aisChandler(int status, @ParserContext("aisData") AISData aisData)
    {
        aisData.setChandlerServiceStatus(ServiceStatus.values()[status]);
    }

    protected void aisStevedore(int status, @ParserContext("aisData") AISData aisData)
    {
        aisData.setStevedoreServiceStatus(ServiceStatus.values()[status]);
    }

    protected void aisElectrical(int status, @ParserContext("aisData") AISData aisData)
    {
        aisData.setElectricalServiceStatus(ServiceStatus.values()[status]);
    }

    protected void aisWater(int status, @ParserContext("aisData") AISData aisData)
    {
        aisData.setWaterServiceStatus(ServiceStatus.values()[status]);
    }

    protected void aisCustoms(int status, @ParserContext("aisData") AISData aisData)
    {
        aisData.setCustomsServiceStatus(ServiceStatus.values()[status]);
    }

    protected void aisCartage(int status, @ParserContext("aisData") AISData aisData)
    {
        aisData.setCartageServiceStatus(ServiceStatus.values()[status]);
    }

    protected void aisCrane(int status, @ParserContext("aisData") AISData aisData)
    {
        aisData.setCraneServiceStatus(ServiceStatus.values()[status]);
    }

    protected void aisLift(int status, @ParserContext("aisData") AISData aisData)
    {
        aisData.setLiftServiceStatus(ServiceStatus.values()[status]);
    }

    protected void aisMedical(int status, @ParserContext("aisData") AISData aisData)
    {
        aisData.setMedicalServiceStatus(ServiceStatus.values()[status]);
    }

    protected void aisNavrepair(int status, @ParserContext("aisData") AISData aisData)
    {
        aisData.setNavrepairServiceStatus(ServiceStatus.values()[status]);
    }

    protected void aisProvisions(int status, @ParserContext("aisData") AISData aisData)
    {
        aisData.setProvisionsServiceStatus(ServiceStatus.values()[status]);
    }

    protected void aisShiprepair(int status, @ParserContext("aisData") AISData aisData)
    {
        aisData.setShiprepairServiceStatus(ServiceStatus.values()[status]);
    }

    protected void aisSurveyor(int status, @ParserContext("aisData") AISData aisData)
    {
        aisData.setSurveyorServiceStatus(ServiceStatus.values()[status]);
    }

    protected void aisSteam(int status, @ParserContext("aisData") AISData aisData)
    {
        aisData.setSteamServiceStatus(ServiceStatus.values()[status]);
    }

    protected void aisTugs(int status, @ParserContext("aisData") AISData aisData)
    {
        aisData.setTugsServiceStatus(ServiceStatus.values()[status]);
    }

    protected void aisSolidwaste(int status, @ParserContext("aisData") AISData aisData)
    {
        aisData.setSolidwasteServiceStatus(ServiceStatus.values()[status]);
    }

    protected void aisLiquidwaste(int status, @ParserContext("aisData") AISData aisData)
    {
        aisData.setLiquidwasteServiceStatus(ServiceStatus.values()[status]);
    }

    protected void aisHazardouswaste(int status, @ParserContext("aisData") AISData aisData)
    {
        aisData.setHazardouswasteServiceStatus(ServiceStatus.values()[status]);
    }

    protected void aisBallast(int status, @ParserContext("aisData") AISData aisData)
    {
        aisData.setBallastServiceStatus(ServiceStatus.values()[status]);
    }

    protected void aisAdditional(int status, @ParserContext("aisData") AISData aisData)
    {
        aisData.setAdditionalServiceStatus(ServiceStatus.values()[status]);
    }

    protected void aisRegional1(int status, @ParserContext("aisData") AISData aisData)
    {
        aisData.setRegional1ServiceStatus(ServiceStatus.values()[status]);
    }

    protected void aisRegional2(int status, @ParserContext("aisData") AISData aisData)
    {
        aisData.setRegional2ServiceStatus(ServiceStatus.values()[status]);
    }

    protected void aisFuture1(int status, @ParserContext("aisData") AISData aisData)
    {
        aisData.setFuture1ServiceStatus(ServiceStatus.values()[status]);
    }

    protected void aisFuture2(int status, @ParserContext("aisData") AISData aisData)
    {
        aisData.setFuture2ServiceStatus(ServiceStatus.values()[status]);
    }

    protected void aisBerthName(InputReader reader, @ParserContext("aisData") AISData aisData)
    {
        aisData.setBerthName(fromSixBitCharacters(reader));
    }

    protected void aisBerthLon_I3(int lon, @ParserContext("aisData") AISData aisData)
    {
        if (lon != 0x6791AC0)
        {
            float f = lon;
            aisData.setLongitude(f / 60000L);
        }
    }

    protected void aisBerthLat_I3(int lat, @ParserContext("aisData") AISData aisData)
    {
        if (lat != 0x3412140)
        {
            float f = lat;
            aisData.setLatitude(f / 60000L);
        }
    }

    protected void aisSender(int sender, @ParserContext("aisData") AISData aisData)
    {
        aisData.setSender(sender);
    }

    protected void aisRtype(int type, @ParserContext("aisData") AISData aisData)
    {
        aisData.setRouteType(RouteTypeCodes.values()[type]);
    }

    protected void aisWaycount(int count, @ParserContext("aisData") AISData aisData)
    {
        aisData.setWaypointCount(count);
    }

    protected void aisDescription(InputReader reader, @ParserContext("aisData") AISData aisData)
    {
        aisData.setDescription(fromSixBitCharacters(reader));
    }

    protected void aisMmsi1(int mmsi, @ParserContext("aisData") AISData aisData)
    {
        aisData.setMMSI1(mmsi);
    }

    protected void aisMmsi2(int mmsi, @ParserContext("aisData") AISData aisData)
    {
        aisData.setMMSI2(mmsi);
    }

    protected void aisMmsi3(int mmsi, @ParserContext("aisData") AISData aisData)
    {
        aisData.setMMSI3(mmsi);
    }

    protected void aisMmsi4(int mmsi, @ParserContext("aisData") AISData aisData)
    {
        aisData.setMMSI4(mmsi);
    }

    protected void aisWspeed(int knots, @ParserContext("aisData") AISData aisData)
    {
        if (knots != 127)
        {
            aisData.setAverageWindSpeed(knots);
        }
    }

    protected void aisWgust(int knots, @ParserContext("aisData") AISData aisData)
    {
        if (knots != 127)
        {
            aisData.setGustSpeed(knots);
        }
    }

    protected void aisWdir(int degrees, @ParserContext("aisData") AISData aisData)
    {
        if (degrees <= 360)
        {
            aisData.setWindDirection(degrees);
        }
    }

    protected void aisWgustdir(int degrees, @ParserContext("aisData") AISData aisData)
    {
        if (degrees <= 360)
        {
            aisData.setWindGustDirection(degrees);
        }
    }

    protected void aisTemperature(int degrees, @ParserContext("aisData") AISData aisData)
    {
        float f = degrees;
        aisData.setAirTemperature((f / 10F) - 60F);
    }

    protected void aisHumidity(int humidity, @ParserContext("aisData") AISData aisData)
    {
        aisData.setRelativeHumidity(humidity);
    }

    protected void aisDewpoint(int degrees, @ParserContext("aisData") AISData aisData)
    {
        float f = degrees;
        aisData.setDewPoint((f / 10F) - 20F);
    }

    protected void aisPressure(int pressure, @ParserContext("aisData") AISData aisData)
    {
        aisData.setAirPressure(pressure + 400);
    }

    protected void aisPressuretend(int tendency, @ParserContext("aisData") AISData aisData)
    {
        aisData.setAirPressureTendency(tendency);
    }

    protected void aisVisibility_U1(int visibility, @ParserContext("aisData") AISData aisData)
    {
        float f = visibility;
        aisData.setVisibility(f / 10F);
    }

    protected void aisWaterlevel_U1(int level, @ParserContext("aisData") AISData aisData)
    {
        float f = level;
        aisData.setWaterLevel((f / 10F) - 10F);
    }

    protected void aisLeveltrend(int trend, @ParserContext("aisData") AISData aisData)
    {
        aisData.setWaterLevelTrend(trend);
    }

    protected void aisCspeed2_U1(int speed, @ParserContext("aisData") AISData aisData)
    {
        float f = speed;
        aisData.setCurrentSpeed2(f / 10F);
    }

    protected void aisCdir2(int degrees, @ParserContext("aisData") AISData aisData)
    {
        if (degrees <= 360)
        {
            aisData.setCurrentDirection2(degrees);
        }
    }

    protected void aisCdepth2_U1(int depth, @ParserContext("aisData") AISData aisData)
    {
        float f = depth;
        aisData.setMeasurementDepth2(f / 10F);
    }

    protected void aisCspeed3_U1(int speed, @ParserContext("aisData") AISData aisData)
    {
        float f = speed;
        aisData.setCurrentSpeed3(f / 10F);
    }

    protected void aisCdir3(int degrees, @ParserContext("aisData") AISData aisData)
    {
        if (degrees <= 360)
        {
            aisData.setCurrentDirection3(degrees);
        }
    }

    protected void aisCdepth3_U1(int depth, @ParserContext("aisData") AISData aisData)
    {
        float f = depth;
        aisData.setMeasurementDepth3(f / 10F);
    }

    protected void aisWaveheight_U1(int height, @ParserContext("aisData") AISData aisData)
    {
        float f = height;
        aisData.setWaveHeight(f / 10F);
    }

    protected void aisWaveperiod(int seconds, @ParserContext("aisData") AISData aisData)
    {
        aisData.setWavePeriod(seconds);
    }

    protected void aisWavedir(int degrees, @ParserContext("aisData") AISData aisData)
    {
        aisData.setWaveDirection(degrees);
    }

    protected void aisSwellheight_U1(int height, @ParserContext("aisData") AISData aisData)
    {
        float f = height;
        aisData.setSwellHeight(f / 10F);
    }

    protected void aisSwellperiod(int seconds, @ParserContext("aisData") AISData aisData)
    {
        aisData.setSwellPeriod(seconds);
    }

    protected void aisSwelldir(int degrees, @ParserContext("aisData") AISData aisData)
    {
        aisData.setSwellDirection(degrees);
    }

    protected void aisSeastate(int state, @ParserContext("aisData") AISData aisData)
    {
        aisData.setSeaState(BeaufortScale.values()[state]);
    }

    protected void aisWatertemp_U1(int temp, @ParserContext("aisData") AISData aisData)
    {
        float f = temp;
        aisData.setWaterTemperature((f / 10F) - 10F);
    }

    protected void aisPreciptype(int type, @ParserContext("aisData") AISData aisData)
    {
        aisData.setPrecipitation(PrecipitationTypes.values()[type]);
    }

    protected void aisSalinity_U1(int salinity, @ParserContext("aisData") AISData aisData)
    {
        float f = salinity;
        aisData.setSalinity(f / 10F);
    }

    protected void aisIce(int ice, @ParserContext("aisData") AISData aisData)
    {
        aisData.setIce(ice);
    }

    protected void aisReason(InputReader reader, @ParserContext("aisData") AISData aisData)
    {
        aisData.setReasonForClosing(fromSixBitCharacters(reader));
    }

    protected void aisClosefrom(InputReader reader, @ParserContext("aisData") AISData aisData)
    {
        aisData.setClosingFrom(fromSixBitCharacters(reader));
    }

    protected void aisCloseto(InputReader reader, @ParserContext("aisData") AISData aisData)
    {
        aisData.setClosingTo(fromSixBitCharacters(reader));
    }

    protected void aisExtunit(int unit, @ParserContext("aisData") AISData aisData)
    {
        aisData.setUnitOfExtension(ExtensionUnit.values()[unit]);
    }

    protected void aisFmonth(int month, @ParserContext("aisData") AISData aisData)
    {
        if (month != 0)
        {
            aisData.setFromMonth(month);
        }
    }

    protected void aisFday(int day, @ParserContext("aisData") AISData aisData)
    {
        if (day != 0)
        {
            aisData.setFromDay(day);
        }
    }

    protected void aisFhour(int hour, @ParserContext("aisData") AISData aisData)
    {
        if (hour != 24)
        {
            aisData.setFromHour(hour);
        }
    }

    protected void aisFminute(int minute, @ParserContext("aisData") AISData aisData)
    {
        if (minute != 60)
        {
            aisData.setFromMinute(minute);
        }
    }

    protected void aisTmonth(int month, @ParserContext("aisData") AISData aisData)
    {
        if (month != 0)
        {
            aisData.setToMonth(month);
        }
    }

    protected void aisTday(int day, @ParserContext("aisData") AISData aisData)
    {
        if (day != 0)
        {
            aisData.setToDay(day);
        }
    }

    protected void aisThour(int hour, @ParserContext("aisData") AISData aisData)
    {
        if (hour != 24)
        {
            aisData.setToHour(hour);
        }
    }

    protected void aisTminute(int minute, @ParserContext("aisData") AISData aisData)
    {
        if (minute != 60)
        {
            aisData.setToMinute(minute);
        }
    }

    protected void aisAirdraught(int meters, @ParserContext("aisData") AISData aisData)
    {
        if (meters != 0)
        {
            aisData.setAirDraught(meters);
        }
    }

    protected void aisIdtype(int type, @ParserContext("aisData") AISData aisData)
    {
        aisData.setIdType(TargetIdentifierType.values()[type]);
    }

    protected void aisId(long id, @ParserContext("aisData") AISData aisData)
    {
        aisData.setId(id);
    }

    protected void aisCourse(int degrees, @ParserContext("aisData") AISData aisData)
    {
        if (degrees < 360)
        {
            aisData.setCourse(degrees);
        }
    }

    protected void aisSpeed(int knots, @ParserContext("aisData") AISData aisData)
    {
        if (knots < 255)
        {
            aisData.setSpeed(knots);
        }
    }

    protected void aisStation(InputReader reader, @ParserContext("aisData") AISData aisData)
    {
        aisData.setStation(fromSixBitCharacters(reader));
    }

    protected void aisSignal(int signal, @ParserContext("aisData") AISData aisData)
    {
        aisData.setSignal(MarineTrafficSignals.values()[signal]);
    }

    protected void aisNextsignal(int signal, @ParserContext("aisData") AISData aisData)
    {
        aisData.setNextSignal(MarineTrafficSignals.values()[signal]);
    }

    protected void aisWmo(int variant, @ParserContext("aisData") AISData aisData)
    {
        aisData.setVariant(variant);
    }

    protected void aisLocation(InputReader reader, @ParserContext("aisData") AISData aisData)
    {
        aisData.setLocation(fromSixBitCharacters(reader));
    }

    protected void aisWeather(int code, @ParserContext("aisData") AISData aisData)
    {
        aisData.setWeather(WMOCode45501.values()[code]);
    }

    protected void aisVislimit(int reached, @ParserContext("aisData") AISData aisData)
    {
        aisData.setVisibilityLimit(reached != 0);
    }

    protected void aisAirtemp_U1(int degrees, @ParserContext("aisData") AISData aisData)
    {
        float f = degrees;
        aisData.setAirTemperature((f / 10F) - 60F);
    }

    protected void aisPressure_U1(int pressure, @ParserContext("aisData") AISData aisData)
    {
        float f = pressure;
        aisData.setAirPressure((f / 10F) - 900F);   // ???? 90-1100 hPa: P = (value/10)+900 for 0-2000
    }

    protected void aisPdelta_U1(int delta, @ParserContext("aisData") AISData aisData)
    {
        float f = delta;
        aisData.setAirPressureChange((f / 10F) - 50F);
    }

    protected void aisPtend(int tend, @ParserContext("aisData") AISData aisData)
    {
        aisData.setAirPressureTendency(tend);
    }

    protected void aisTwinddir(int arg, @ParserContext("aisData") AISData aisData)
    {
    }

    protected void aisTwindspeed(int arg, @ParserContext("aisData") AISData aisData)
    {
    }

    protected void aisRwinddir(int arg, @ParserContext("aisData") AISData aisData)
    {
    }

    protected void aisRwindspeed_U1(int arg, @ParserContext("aisData") AISData aisData)
    {
    }

    protected void aisMgustspeed_U1(int arg, @ParserContext("aisData") AISData aisData)
    {
    }

    protected void aisMgustdir(int arg, @ParserContext("aisData") AISData aisData)
    {
    }

    protected void aisSurftemp_U1(int arg, @ParserContext("aisData") AISData aisData)
    {
    }

    protected void aisVisibility_U2(int arg, @ParserContext("aisData") AISData aisData)
    {
    }

    protected void aisPweather1(int arg, @ParserContext("aisData") AISData aisData)
    {
    }

    protected void aisPweather2(int arg, @ParserContext("aisData") AISData aisData)
    {
    }

    protected void aisTotalcloud(int arg, @ParserContext("aisData") AISData aisData)
    {
    }

    protected void aisLowclouda(int arg, @ParserContext("aisData") AISData aisData)
    {
    }

    protected void aisLowcloudt(int arg, @ParserContext("aisData") AISData aisData)
    {
    }

    protected void aisMidcloudt(int arg, @ParserContext("aisData") AISData aisData)
    {
    }

    protected void aisHighcloudt(int arg, @ParserContext("aisData") AISData aisData)
    {
    }

    protected void aisCloudbase_U2(int arg, @ParserContext("aisData") AISData aisData)
    {
    }

    protected void aisWwperiod(int arg, @ParserContext("aisData") AISData aisData)
    {
    }

    protected void aisWwheight(int arg, @ParserContext("aisData") AISData aisData)
    {
    }

    protected void aisSwelldir1(int arg, @ParserContext("aisData") AISData aisData)
    {
    }

    protected void aisSwperiod1(int arg, @ParserContext("aisData") AISData aisData)
    {
    }

    protected void aisSwheight1_U1(int arg, @ParserContext("aisData") AISData aisData)
    {
    }

    protected void aisSwelldir2(int arg, @ParserContext("aisData") AISData aisData)
    {
    }

    protected void aisSwperiod2(int arg, @ParserContext("aisData") AISData aisData)
    {
    }

    protected void aisSwheight2_U1(int arg, @ParserContext("aisData") AISData aisData)
    {
    }

    protected void aisIcedeposit(int arg, @ParserContext("aisData") AISData aisData)
    {
    }

    protected void aisIcerate(int arg, @ParserContext("aisData") AISData aisData)
    {
    }

    protected void aisIcecause(int arg, @ParserContext("aisData") AISData aisData)
    {
    }

    protected void aisSeaice(int arg, @ParserContext("aisData") AISData aisData)
    {
    }

    protected void aisIcetype(int arg, @ParserContext("aisData") AISData aisData)
    {
    }

    protected void aisIcestate(int arg, @ParserContext("aisData") AISData aisData)
    {
    }

    protected void aisIcedevel(int arg, @ParserContext("aisData") AISData aisData)
    {
    }

    protected void aisIcebearing(int arg, @ParserContext("aisData") AISData aisData)
    {
    }
protected void aisSensor(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisSite(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisPayload(int arg, @ParserContext("aisData") AISData aisData){}
// SiteLocationPayload
protected void aisAlt(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisOwner(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisTimeout(int arg, @ParserContext("aisData") AISData aisData){}
// StationIDPayload
protected void aisName(InputReader arg, @ParserContext("aisData") AISData aisData){}
// WindReportPayload
protected void aisSensortype(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisFwspeed(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisFwgust(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisFwdir(int arg, @ParserContext("aisData") AISData aisData){}
// CurrentFlow2DReportPayload
protected void aisCspeed1_U1(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisCdir1(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisCdepth1(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisCdepth2(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisCdepth3(int arg, @ParserContext("aisData") AISData aisData){}
// CurrentFlow3DPayload
protected void aisCnorth1_U1(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisCeast1_U1(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisCup1_U1(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisCnorth2_U1(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisCeast2_U1(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisCup2_U1(int arg, @ParserContext("aisData") AISData aisData){}
// HorizontalCurrentReportPayload
protected void aisBearing1(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisDistance1(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisSpeed1_U1(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisDirection1(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisDepth1(int arg, @ParserContext("aisData") AISData aisData){}
// SeaStateReportPayload
protected void aisSwheight_U1(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisSwperiod(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisSwelltype(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisDistance1_U1(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisDepthtype(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisWavetype(int arg, @ParserContext("aisData") AISData aisData){}
// SalinityReportPayload
protected void aisConductivity_U1(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisSalinitytype(int arg, @ParserContext("aisData") AISData aisData){}
// WeatherReportPayload
protected void aisDewtype(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisPressuretype(int arg, @ParserContext("aisData") AISData aisData){}
// AirGapAirDraftReportPayload
protected void aisAirdraught_U1(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisAirgap_U1(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisGaptrend(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisFairgap_U1(int arg, @ParserContext("aisData") AISData aisData){}
// IMO289RouteInformationBroadcast
// IMO289TextDescriptionBroadcast
// IMO289ExtendedShipStaticAndVoyageRelatedData
protected void aisSecondport(InputReader arg, @ParserContext("aisData") AISData aisData){}
protected void aisAisState(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisAtaState(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisBnwasState(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisEcdisbState(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisChartState(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisSounderState(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisEpaidState(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisSteerState(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisGnssState(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisGyroState(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisLritState(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisMagcompState(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisNavtexState(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisArpaState(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisSbandState(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisXbandState(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisHfradioState(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisInmarsatState(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisMfradioState(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisVhfradioState(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisGrndlogState(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisWaterlogState(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisThdState(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisTcsState(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisVdrState(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisIceclass(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisHorsepower(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisVhfchan(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisLshiptype(InputReader arg, @ParserContext("aisData") AISData aisData){}
protected void aisTonnage(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisLading(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisHeavyoil(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisLightoil(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisDieseloil(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisTotaloil(int arg, @ParserContext("aisData") AISData aisData){}
// MeteorologicalAndHydrologicalDataIMO289
protected void aisDewpoint_U1(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisVisgreater(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisWaterlevel_U2(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisPrecipitation(int arg, @ParserContext("aisData") AISData aisData){}
// Type9StandardSARAircraftPositionReport
protected void aisRegional(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisAssigned(int arg, @ParserContext("aisData") AISData aisData){}
// Type10UTCDateInquiry
// Type12AddressedSafetyRelatedMessage
// Type14SafetyRelatedBroadcastMessage
// Type15Interrogation
protected void aisType11(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisOffset11(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisType12(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisOffset12(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisType21(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisOffset21(int arg, @ParserContext("aisData") AISData aisData){}
// Type16AssignmentModeCommand
protected void aisOffset1(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisIncrement1(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisOffset2(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisIncrement2(int arg, @ParserContext("aisData") AISData aisData){}
// Type17DGNSSBroadcastBinaryMessage
protected void aisLon_I1(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisLat_I1(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisData(int arg, @ParserContext("aisData") AISData aisData){}
// Type18StandardClassBCSPositionReport
protected void aisReserved(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisCs(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisDisplay(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisDsc(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisBand(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisMsg22(int arg, @ParserContext("aisData") AISData aisData){}
// Type19ExtendedClassBCSPositionReport
protected void aisShipname(int arg, @ParserContext("aisData") AISData aisData){}
// Type20DataLinkManagementMessage
protected void aisNumber1(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisTimeout1(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisNumber2(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisTimeout2(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisOffset3(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisNumber3(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisTimeout3(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisIncrement3(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisOffset4(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisNumber4(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisTimeout4(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisIncrement4(int arg, @ParserContext("aisData") AISData aisData){}
// Type21AidToNavigationReport
protected void aisAidType(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisOffPosition(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisVirtualAid(int arg, @ParserContext("aisData") AISData aisData){}
// Type22ChannelManagement
protected void aisChannelA(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisChannelB(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisTxrx(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisPower(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisNeLon_I1(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisNeLat_I1(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisSwLon_I1(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisSwLat_I1(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisDest1(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisDest2(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisAddressed(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisBandA(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisBandB(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisZonesize(int arg, @ParserContext("aisData") AISData aisData){}
// Type23GroupAssignmentCommand
protected void aisNeLon(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisNeLat(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisSwLon(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisSwLat(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisStationType(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisShipType(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisInterval(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisQuiet(int arg, @ParserContext("aisData") AISData aisData){}
// Type24StaticDataReport
protected void aisPartno(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisVendorid(InputReader arg, @ParserContext("aisData") AISData aisData){}
protected void aisMothershipMmsi(int arg, @ParserContext("aisData") AISData aisData){}
// Type25SingleSlotBinaryMessage
protected void aisStructured(int arg, @ParserContext("aisData") AISData aisData){}
protected void aisGnss(int arg, @ParserContext("aisData") AISData aisData){}
// ###################################################################################

    private String fromSixBitCharacters(InputReader reader)
    {
        StringBuilder sb = new StringBuilder();
        char[] array = reader.getArray();
        int start = reader.getStart();
        int length = reader.getLength();
        assert length % 6 == 0;
        int bit = 0;
        int cc = 0;
        for (int ii = 0; ii < length; ii++)
        {
            bit++;
            cc <<= 1;
            cc += array[start + ii % array.length];
            if (bit == 6)
            {
                if (cc < 32)
                {
                    sb.append((char) (cc + '@'));
                }
                else
                {
                    sb.append((char) cc);
                }
            }
        }
        return sb.toString().trim();
    }

    @Rule("letter c")
    protected void channel(
            char channel,
            @ParserContext("aisData") AISData aisData,
            @ParserContext("aisInputStream") AISInputStream aisInputStream)
    {
        aisData.setChannel(channel);
        aisInputStream.setDecode(true);
    }

    @Rule("integer")
    protected void sentenceNumber(
            int sentenceNumber,
            @ParserContext("aisData") AISData aisData)
    {
        aisData.setSentenceNumber(sentenceNumber);
    }

    @Rule("integer")
    protected void numberOfSentences(
            int numberOfSentences,
            @ParserContext("aisData") AISData aisData)
    {
        aisData.setNumberOfSentences(numberOfSentences);
    }

    @Rule("integer")
    protected void sequentialMessageId(
            int sequentialMessageId,
            @ParserContext("aisData") AISData aisData)
    {
        aisData.setSequenceMessageId(sequentialMessageId);
    }

    @Rule("integer")
    protected void day(
            int day,
            @ParserContext("clock") Clock clock)
    {
        clock.setDay(day);
    }

    @Rule("integer")
    protected void month(
            int month,
            @ParserContext("clock") Clock clock)
    {
        clock.setMonth(month);
    }

    @Rule("integer")
    protected void year(
            int year,
            @ParserContext("clock") Clock clock)
    {
        clock.setYear(year);
    }

    @Rule("integer")
    protected void localZoneHours(
            int localZoneHours,
            @ParserContext("clock") Clock clock)
    {
        clock.setZoneHours(localZoneHours);
    }

    @Rule("integer")
    protected void localZoneMinutes(
            int localZoneMinutes,
            @ParserContext("clock") Clock clock)
    {
        clock.setZoneMinutes(localZoneMinutes);
    }

    @Rule("decimal c letter")
    protected void velocityToWaypoint(
            float velocityToWaypoint,
            char unit,
            @ParserContext("data") NMEAData data)
    {
        data.setVelocityToWaypoint(velocityToWaypoint, unit);
    }

    @Rule("decimal c letter")
    protected void windDirection(
            float windDirection,
            char unit,
            @ParserContext("data") NMEAData data)
    {
        data.setWindDirection(windDirection, unit);
    }

    @Rule("decimal c letter")
    protected void waterHeading(
            float waterHeading,
            char unit,
            @ParserContext("data") NMEAData data)
    {
        data.setWaterHeading(waterHeading, unit);
    }

    @Rule("decimal c letter")
    protected void waterSpeed(
            float waterSpeed,
            char unit,
            @ParserContext("data") NMEAData data)
    {
        data.setWaterSpeed(waterSpeed, unit);
    }

    @Rule("decimal")
    protected void starboardRudderSensor(
            float starboardRudderSensor,
            @ParserContext("data") NMEAData data)
    {
        data.setStarboardRudderSensor(starboardRudderSensor);
    }

    @Rule("decimal")
    protected void portRudderSensor(
            float portRudderSensor,
            @ParserContext("data") NMEAData data)
    {
        data.setPortRudderSensor(portRudderSensor);
    }

    @Rule("letter")
    protected void rpmSource(
            char rpmSource,
            @ParserContext("data") NMEAData data)
    {
        data.setRpmSource(rpmSource);
    }

    @Rule("integer")
    protected void rpmSourceNumber(
            int rpmSourceNumber,
            @ParserContext("data") NMEAData data)
    {
        data.setRpmSourceNumber(rpmSourceNumber);
    }

    @Rule("integer")
    protected void rpm(
            int rpm,
            @ParserContext("data") NMEAData data)
    {
        data.setRpm(rpm);
    }

    @Rule("decimal")
    protected void propellerPitch(
            float propellerPitch,
            @ParserContext("data") NMEAData data)
    {
        data.setPropellerPitch(propellerPitch);
    }

    @Rule("decimal")
    protected void rateOfTurn(
            float rateOfTurn,
            @ParserContext("data") NMEAData data)
    {
        data.setRateOfTurn(rateOfTurn);
    }

    @Rule("decimal c letter")
    protected void windAngle(
            float windAngle,
            char unit,
            @ParserContext("data") NMEAData data)
    {
        data.setWindAngle(windAngle, unit);
    }

    @Rule("decimal c letter")
    protected void windSpeed(
            float windSpeed,
            char unit,
            @ParserContext("data") NMEAData data)
    {
        data.setWindSpeed(windSpeed, unit);
    }

    @Rule("decimal c letter")
    protected void waterTemperature(
            float waterTemperature,
            char unit,
            @ParserContext("data") NMEAData data)
    {
        data.setWaterTemperature(waterTemperature, unit);
    }

    @Rule("decimal c letter")
    protected void heading(
            float heading,
            char unit,
            @ParserContext("data") NMEAData data)
    {
        data.setHeading(heading, unit);
    }

    @Rule("decimal")
    protected void magneticSensorHeading(
            float magneticSensorHeading,
            @ParserContext("data") NMEAData data)
    {
        data.setMagneticSensorHeading(magneticSensorHeading);
    }

    @Rule("decimal c ew")
    protected void magneticDeviation(
            float magneticDeviation,
            float sign,
            @ParserContext("data") NMEAData data)
    {
        data.setMagneticDeviation(sign * magneticDeviation);
    }

    @Rule("decimal c decimal")
    protected void depthOfWater(
            float depth,
            float offset,
            @ParserContext("data") NMEAData data)
    {
        data.setDepthOfWater(depth, offset);
    }

    @Rule("stringList")
    protected void waypoints(
            List<String> list,
            @ParserContext("data") NMEAData data)
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
            @ParserContext("data") NMEAData data)
    {
        data.setHorizontalDatum(horizontalDatum);
    }

    @Rule("c letter")
    protected void faaModeIndicator(
            char faaModeIndicator,
            @ParserContext("data") NMEAData data)
    {
        data.setFAAModeIndicator(faaModeIndicator);
    }

    @Rule("letter")
    protected void messageMode(
            char messageMode,
            @ParserContext("data") NMEAData data)
    {
        data.setMessageMode(messageMode);
    }

    @Rule("decimal c letter")
    protected void distanceToWaypoint(
            float distanceToWaypoint,
            char units,
            @ParserContext("data") NMEAData data)
    {
        data.setDistanceToWaypoint(distanceToWaypoint, units);
    }

    @Rule("decimal c letter")
    protected void depthBelowTransducer(
            float depth,
            char unit,
            @ParserContext("data") NMEAData data)
    {
        data.setDepthBelowTransducer(depth, unit);
    }

    @Rule("decimal c letter")
    protected void depthBelowSurface(
            float depth,
            char unit,
            @ParserContext("data") NMEAData data)
    {
        data.setDepthBelowSurface(depth, unit);
    }

    @Rule("decimal c letter")
    protected void depthBelowKeel(
            float depth,
            char unit,
            @ParserContext("data") NMEAData data)
    {
        data.setDepthBelowKeel(depth, unit);
    }

    @Rule("decimal")
    protected void f1ClockParameter(
            float f1ClockParameter,
            @ParserContext("data") NMEAData data)
    {
        data.setF1ClockParameter(f1ClockParameter);
    }

    @Rule("decimal")
    protected void f0ClockParameter(
            float f0ClockParameter,
            @ParserContext("data") NMEAData data)
    {
        data.setF0ClockParameter(f0ClockParameter);
    }

    @Rule("decimal")
    protected void meanAnomaly(
            float meanAnomaly,
            @ParserContext("data") NMEAData data)
    {
        data.setMeanAnomaly(meanAnomaly);
    }

    @Rule("decimal")
    protected void longitudeOfAscensionNode(
            float longitudeOfAscensionNode,
            @ParserContext("data") NMEAData data)
    {
        data.setLongitudeOfAscensionNode(longitudeOfAscensionNode);
    }

    @Rule("decimal")
    protected void argumentOfPerigee(
            float argumentOfPerigee,
            @ParserContext("data") NMEAData data)
    {
        data.setArgumentOfPerigee(argumentOfPerigee);
    }

    @Rule("decimal")
    protected void rootOfSemiMajorAxis(
            float rootOfSemiMajorAxis,
            @ParserContext("data") NMEAData data)
    {
        data.setRootOfSemiMajorAxis(rootOfSemiMajorAxis);
    }

    @Rule("decimal")
    protected void rateOfRightAscension(
            float rateOfRightAscension,
            @ParserContext("data") NMEAData data)
    {
        data.setRateOfRightAscension(rateOfRightAscension);
    }

    @Rule("decimal")
    protected void inclinationAngle(
            float inclinationAngle,
            @ParserContext("data") NMEAData data)
    {
        data.setInclinationAngle(inclinationAngle);
    }

    @Rule("decimal")
    protected void almanacReferenceTime(
            float almanacReferenceTime,
            @ParserContext("data") NMEAData data)
    {
        data.setAlmanacReferenceTime(almanacReferenceTime);
    }

    @Rule("decimal")
    protected void eccentricity(
            float eccentricity,
            @ParserContext("data") NMEAData data)
    {
        data.setEccentricity(eccentricity);
    }

    @Rule("integer")
    protected void svHealth(
            int svHealth,
            @ParserContext("data") NMEAData data)
    {
        data.setSvHealth(svHealth);
    }

    @Rule("integer")
    protected void gpsWeekNumber(
            int gpsWeekNumber,
            @ParserContext("data") NMEAData data)
    {
        data.setGpsWeekNumber(gpsWeekNumber);
    }

    @Rule("integer")
    protected void satellitePRNNumber(
            int satellitePRNNumber,
            @ParserContext("data") NMEAData data)
    {
        data.setSatellitePRNNumber(satellitePRNNumber);
    }

    @Rule("integer")
    protected void messageNumber(
            int messageNumber,
            @ParserContext("data") NMEAData data)
    {
        data.setMessageNumber(messageNumber);
    }

    @Rule("integer")
    protected void totalNumberOfMessages(
            int totalNumberOfMessages,
            @ParserContext("data") NMEAData data)
    {
        data.setTotalNumberOfMessages(totalNumberOfMessages);
    }

    @Rule("decimal c letter")
    protected void arrivalCircleRadius(
            float arrivalCircleRadius,
            char units,
            @ParserContext("data") NMEAData data)
    {
        data.setArrivalCircleRadius(arrivalCircleRadius, units);
    }

    @Rule("decimal c decimal")
    protected void timeDifference(
            float timeDifferenceA, // uS
            float timeDifferenceB, // uS
            @ParserContext("data") NMEAData data)
    {
        data.setTimeDifference(timeDifferenceA, timeDifferenceB);
    }

    @Rule("string")
    protected void waypoint(
            String waypoint,
            @ParserContext("data") NMEAData data)
    {
        data.setWaypoint(waypoint);
    }

    @Rule("decimal")
    protected void utc(
            float utc, // hhmmss.ss
            @ParserContext("clock") Clock clock)
    {
        clock.setTime(utc);
    }

    @Rule("integer")
    protected void date(
            int date, // ddmmyy
            @ParserContext("clock") Clock clock)
    {
        clock.setDate(date);
    }

    @Rule("letter")
    protected void arrivalStatus(
            char arrivalStatus,
            @ParserContext("data") NMEAData data)
    {
        data.setArrivalStatus(arrivalStatus);
    }

    @Rule("letter")
    protected void waypointStatus(
            char waypointStatus,
            @ParserContext("data") NMEAData data)
    {
        data.setWaypointStatus(waypointStatus);
    }

    @Rule("letter")
    protected void status(
            char status,
            @ParserContext("data") NMEAData data)
    {
        data.setStatus(status);
    }

    @Rule("letter")
    protected void status2(
            char status,
            @ParserContext("data") NMEAData data)
    {
        data.setStatus2(status);
    }

    @Rule("integer")
    protected void differentialReferenceStationID(
            int differentialReferenceStationID, //0000-1023            
            @ParserContext("data") NMEAData data)
    {
        data.setDifferentialReferenceStationID(differentialReferenceStationID);
    }

    @Rule("integer")
    protected void ageOfDifferentialGPSData(
            int ageOfDifferentialGPSData, //time in seconds since last SC104 type 1 or 9 update, null field when DGPS is not used
            @ParserContext("data") NMEAData data)
    {
        data.setAgeOfDifferentialGPSData(ageOfDifferentialGPSData);
    }

    @Rule("decimal c letter")
    protected void geoidalSeparation(
            float geoidalSeparation, //the difference between the WGS-84 earth ellipsoid and mean-sea-level (geoid), "-" means mean-sea-level below ellipsoid
            char unitsOfGeoidalSeparation, // meters
            @ParserContext("data") NMEAData data)
    {
        data.setGeoidalSeparation(geoidalSeparation, unitsOfGeoidalSeparation);
    }

    @Rule("decimal c letter")
    protected void antennaAltitude(
            float antennaAltitude, // above/below mean-sea-level (geoid) (in meters)
            char unitsOfAntennaAltitude, //meters
            @ParserContext("data") NMEAData data)
    {
        data.setAntennaAltitude(antennaAltitude, unitsOfAntennaAltitude);
    }

    @Rule("decimal")
    protected void horizontalDilutionOfPrecision(
            float horizontalDilutionOfPrecision, // (meters)
            @ParserContext("data") NMEAData data)
    {
        data.setHorizontalDilutionOfPrecision(horizontalDilutionOfPrecision);
    }

    @Rule("integer")
    protected void numberOfSatellitesInView(
            int numberOfSatellitesInView,
            @ParserContext("data") NMEAData data)
    {
        data.setNumberOfSatellitesInView(numberOfSatellitesInView);
    }

    @Rule("integer")
    protected void gpsQualityIndicator(
            int gpsQualityIndicator,
            @ParserContext("data") NMEAData data)
    {
        data.setGpsQualityIndicator(gpsQualityIndicator);
    }

    @Rule("decimal")
    protected void destinationClosingVelocity(
            float destinationClosingVelocity, // knots
            @ParserContext("data") NMEAData data)
    {
        data.setDestinationClosingVelocity(destinationClosingVelocity);
    }

    @Rule("decimal c letter")
    protected void bearing(
            float bearing, // degrees
            char unit, // M = Magnetic, T = True
            @ParserContext("data") NMEAData data)
    {
        data.setBearing(bearing, unit);
    }

    @Rule("decimal")
    protected void bearingToDestination(
            float bearingToDestination, // degrees
            @ParserContext("data") NMEAData data)
    {
        data.setBearingToDestination(bearingToDestination);
    }

    @Rule("decimal c letter")
    protected void bearingOriginToDestination(
            float bearingOriginToDestination, // degrees
            char mOrT, // M = Magnetic, T = True
            @ParserContext("data") NMEAData data)
    {
        data.setBearingOriginToDestination(bearingOriginToDestination, mOrT);
    }

    @Rule("decimal c letter")
    protected void bearingPresentPositionToDestination(
            float bearingPresentPositionToDestination, // degrees
            char mOrT, // M = Magnetic, T = True
            @ParserContext("data") NMEAData data)
    {
        data.setBearingPresentPositionToDestination(bearingPresentPositionToDestination, mOrT);
    }

    @Rule("decimal c letter")
    protected void headingToSteerToDestination(
            float headingToSteerToDestination, // degrees
            char mOrT, // M = Magnetic, T = True
            @ParserContext("data") NMEAData data)
    {
        data.setHeadingToSteerToDestination(headingToSteerToDestination, mOrT);
    }

    @Rule("decimal")
    protected void rangeToDestination(
            float rangeToDestination, // NM
            @ParserContext("data") NMEAData data)
    {
        data.setRangeToDestination(rangeToDestination);
    }

    @Rule("string c string")
    protected void waypointToWaypoint(
            String toWaypoint,
            String fromWaypoint,
            @ParserContext("data") NMEAData data)
    {
        data.setWaypointToWaypoint(toWaypoint, fromWaypoint);
    }

    @Rule("decimal c letter c letter")
    protected void crossTrackError(
            float crossTrackError, // NM
            char directionToSteer,
            char unit,
            @ParserContext("data") NMEAData data)
    {
        data.setCrossTrackError(crossTrackError, directionToSteer, unit);
    }

    @Rule("decimal c letter")
    protected void crossTrackErrorNM(
            float crossTrackError, // NM
            char directionToSteer,
            @ParserContext("data") NMEAData data)
    {
        data.setCrossTrackError(crossTrackError, directionToSteer, 'N');
    }

    @Rule("decimal c ew")
    protected void magneticVariation(
            float magneticVariation, // degrees
            float mew,
            @ParserContext("data") NMEAData data)
    {
        data.setMagneticVariation(mew * magneticVariation);
    }

    @Rule("decimal")
    protected void speedOverGround(
            float speedOverGround, // knots
            @ParserContext("data") NMEAData data)
    {
        data.setSpeedOverGround(speedOverGround);
    }

    @Rule("decimal")
    protected void trackMadeGood(
            float trackMadeGood, // knots
            @ParserContext("data") NMEAData data)
    {
        data.setTrackMadeGood(trackMadeGood);
    }

    @Rule("latitude c ns c longitude c ew")
    protected void location(
            float latitude,
            float ns,
            float longitude,
            float ew,
            @ParserContext("data") NMEAData data)
    {
        data.setLocation(ns * latitude, ew * longitude);
    }

    @Rule("latitude c ns c longitude c ew")
    protected void destinationWaypointLocation(
            float latitude,
            float ns,
            float longitude,
            float ew,
            @ParserContext("data") NMEAData data)
    {
        data.setDestinationWaypointLocation(ns * latitude, ew * longitude);
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
            @ParserContext("data") NMEAData data)
    {
        int sum = 16 * parseHex(x1) + parseHex(x2);
        if (sum != checksum.getValue())
        {
            clock.rollback();
            data.rollback("checksum " + Integer.toHexString(sum) + " != " + Integer.toHexString((int) checksum.getValue()));
        }
        else
        {
            clock.commit();
            data.commit();
        }
    }

    @Terminal(expression = "[0-9]+\\.[0-9]+")
    protected float latitude(float lat)
    {
        float degrees = (float) Math.floor(lat / 100);
        float minutes = lat - 100F * degrees;
        float latitude = degrees + minutes / 60F;
        assert latitude >= 0;
        assert latitude <= 90;
        return latitude;
    }

    @Terminal(expression = "[0-9]+\\.[0-9]+")
    protected float longitude(float lat)
    {
        float degrees = (float) Math.floor(lat / 100);
        float minutes = lat - 100F * degrees;
        float longitude = degrees + minutes / 60F;
        assert longitude >= 0;
        assert longitude <= 180;
        return longitude;
    }

    @Terminal(expression = "[NS]")
    protected float ns(char c)
    {
        return 'N' == c ? 1 : -1;
    }

    @Terminal(expression = "[WE]")
    protected float ew(char c)
    {
        return 'E' == c ? 1 : -1;
    }

    @Terminal(expression = "[a-zA-Z]")
    protected abstract char letter(char c);

    @Terminal(expression = "[0-9A-Fa-f]")
    protected abstract char hexAlpha(char x);

    @Terminal(expression = "[a-zA-Z0-9 \\.\\-]+")
    protected abstract String string(String s);

    @Terminal(expression = "[\\+\\-]?[0-9]+")
    protected abstract int integer(int i);

    @Terminal(expression = "[\\+\\-]?[0-9]+(\\.[0-9]+)*")
    protected abstract float decimal(float f);

    @Terminal(expression = "[\\,]")
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
                throw new IllegalArgumentException(x + " is not hex digit");
        }
    }

    @RecoverMethod
    public void recover(
            @ParserContext("data") NMEAData data,
            @ParserContext(ParserConstants.INPUTREADER) InputReader reader) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        int cc = reader.read();
        while (cc != '\n' && cc != -1)
        {
            sb.append((char) cc);
            cc = reader.read();
        }
        data.rollback("skipping " + sb);
    }

    public void parse(InputStream is, NMEAData data, AISData aisData)
    {
        Checksum checksum = new NMEAChecksum();
        Clock clock = new GPSClock();
        CheckedInputStream checkedInputStream = new CheckedInputStream(is, checksum);
        AISInputStream aisInputStream = new AISInputStream(checkedInputStream);
        parse(aisInputStream, checksum, clock, data, aisData, aisInputStream);
    }

    @ParseMethod(start = "statements", size = 80)
    protected abstract void parse(
            InputStream is,
            @ParserContext("checksum") Checksum checksum,
            @ParserContext("clock") Clock clock,
            @ParserContext("data") NMEAData data,
            @ParserContext("aisData") AISData aisData,
            @ParserContext("aisInputStream") AISInputStream aisInputStream);

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
            String pck = NMEAParser.class.getPackage().getName().replace('.', '/') + "/";
            InputStream is = NMEAParser.class.getClassLoader().getResourceAsStream(pck + "nmea-sample");
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
