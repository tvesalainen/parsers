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

import java.io.FileInputStream;
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

/**
 * @author Timo Vesalainen
 * @see <a href="http://catb.org/gpsd/NMEA.html">NMEA Revealed</a>
 * @see <a href="doc-files/NMEAParser-statements.html#BNF">BNF Syntax for NMEA</a>
 */
@GenClassname("org.vesalainen.parsers.nmea.NMEAParserImpl")
@GrammarDef()
@Rules({
    @Rule(left="statements", value="statement*"),
    @Rule(left="statement", value="nmeaStatement"),
    @Rule(left="nmeaStatement", value="'\\$' talkerId nmeaSentence '\\*' checksum '\r\n'"),
    @Rule(left="nmeaSentence", value="'AAM' c arrivalStatus c waypointStatus c arrivalCircleRadius c waypoint"),
    @Rule(left="nmeaSentence", value="'ALM' c totalNumberOfMessages c messageNumber c satellitePRNNumber c gpsWeekNumber c svHealth c eccentricity c almanacReferenceTime c inclinationAngle c rateOfRightAscension c rootOfSemiMajorAxis c argumentOfPerigee c longitudeOfAscensionNode c meanAnomaly c f0ClockParameter c f1ClockParameter"),
    @Rule(left="nmeaSentence", value="'APA' c status c status2 c crossTrackError c arrivalStatus c waypointStatus c bearingOriginToDestination c waypoint"),
    @Rule(left="nmeaSentence", value="'APB' c status c status2 c crossTrackError c arrivalStatus c waypointStatus c bearingOriginToDestination c waypoint c bearingPresentPositionToDestination c headingToSteerToDestination"),
    @Rule(left="nmeaSentence", value="'BOD' c bearingTrue c bearingMagnetic c waypointToWaypoint"),
    @Rule(left="nmeaSentence", value="'RMA' c status c location c timeDifference c speedOverGround c trackMadeGood c magneticVariation"),
    @Rule(left="nmeaSentence", value="'RMB' c status c crossTrackErrorNM c waypointToWaypoint c destinationWaypointLocation c rangeToDestination c bearingToDestination c destinationClosingVelocity c arrivalStatus"),
    @Rule(left="nmeaSentence", value="'RMC' c utc c status c location c speedOverGround c trackMadeGood c date c magneticVariation"),
    @Rule(left="nmeaSentence", value="'GGA' c utc c location c gpsQualityIndicator c numberOfSatellitesInView c horizontalDilutionOfPrecision c antennaAltitude c geoidalSeparation c ageOfDifferentialGPSData c differentialReferenceStationID"),
    @Rule(left="nmeaSentence", value="'GLL' c location c utc c status faaModeIndicator"),
    @Rule(left="nmeaSentence", value="'RMM' c horizontalDatum"),
    @Rule(left="nmeaSentence", value="'RTE' c totalNumberOfMessages c messageNumber c messageMode c waypoints")
})
public abstract class NMEAParser implements ParserInfo
{
    @Rule("stringList")
    protected void waypoints(
            List<String> list,
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setWaypointList(list);
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
    @Rule
    protected void horizontalDatum()
    {
    }
    @Rule("string")
    protected void horizontalDatum(
            String horizontalDatum,
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setHorizontalDatum(horizontalDatum);
    }
    @Rule
    protected void faaModeIndicator()
    {
    }
    @Rule("c letter")
    protected void faaModeIndicator(
            char faaModeIndicator,
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setFAAModeIndicator(faaModeIndicator);
    }
    @Rule
    protected void messageMode()
    {
    }
    @Rule("letter")
    protected void messageMode(
            char messageMode,
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setMessageMode(messageMode);
    }
    @Rule
    protected void f1ClockParameter()
    {
    }
    @Rule("decimal")
    protected void f1ClockParameter(
            float f1ClockParameter,
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setF1ClockParameter(f1ClockParameter);
    }
    @Rule
    protected void f0ClockParameter()
    {
    }
    @Rule("decimal")
    protected void f0ClockParameter(
            float f0ClockParameter,
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setF0ClockParameter(f0ClockParameter);
    }
    @Rule
    protected void meanAnomaly()
    {
    }
    @Rule("decimal")
    protected void meanAnomaly(
            float meanAnomaly,
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setMeanAnomaly(meanAnomaly);
    }
    @Rule
    protected void longitudeOfAscensionNode()
    {
    }
    @Rule("decimal")
    protected void longitudeOfAscensionNode(
            float longitudeOfAscensionNode,
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setLongitudeOfAscensionNode(longitudeOfAscensionNode);
    }
    @Rule
    protected void argumentOfPerigee()
    {
    }
    @Rule("decimal")
    protected void argumentOfPerigee(
            float argumentOfPerigee,
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setArgumentOfPerigee(argumentOfPerigee);
    }
    @Rule
    protected void rootOfSemiMajorAxis()
    {
    }
    @Rule("decimal")
    protected void rootOfSemiMajorAxis(
            float rootOfSemiMajorAxis,
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setRootOfSemiMajorAxis(rootOfSemiMajorAxis);
    }
    @Rule
    protected void rateOfRightAscension()
    {
    }
    @Rule("decimal")
    protected void rateOfRightAscension(
            float rateOfRightAscension,
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setRateOfRightAscension(rateOfRightAscension);
    }
    @Rule
    protected void inclinationAngle()
    {
    }
    @Rule("decimal")
    protected void inclinationAngle(
            float inclinationAngle,
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setInclinationAngle(inclinationAngle);
    }
    @Rule
    protected void almanacReferenceTime()
    {
    }
    @Rule("decimal")
    protected void almanacReferenceTime(
            float almanacReferenceTime,
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setAlmanacReferenceTime(almanacReferenceTime);
    }
    @Rule
    protected void eccentricity()
    {
    }
    @Rule("decimal")
    protected void eccentricity(
            float eccentricity,
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setEccentricity(eccentricity);
    }
    @Rule
    protected void svHealth()
    {
    }
    @Rule("integer")
    protected void svHealth(
            int svHealth,
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setSvHealth(svHealth);
    }
    @Rule
    protected void gpsWeekNumber()
    {
    }
    @Rule("integer")
    protected void gpsWeekNumber(
            int gpsWeekNumber,
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setGpsWeekNumber(gpsWeekNumber);
    }
    @Rule
    protected void satellitePRNNumber()
    {
    }
    @Rule("integer")
    protected void satellitePRNNumber(
            int satellitePRNNumber,
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setSatellitePRNNumber(satellitePRNNumber);
    }
    @Rule
    protected void messageNumber()
    {
    }
    @Rule("integer")
    protected void messageNumber(
            int messageNumber,
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setMessageNumber(messageNumber);
    }
    @Rule
    protected void totalNumberOfMessages()
    {
    }
    @Rule("integer")
    protected void totalNumberOfMessages(
            int totalNumberOfMessages,
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setTotalNumberOfMessages(totalNumberOfMessages);
    }
    @Rule("c")
    protected void arrivalCircleRadius()
    {
    }
    @Rule("decimal c letter")
    protected void arrivalCircleRadius(
            float arrivalCircleRadius,
            char units,
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setArrivalCircleRadius(arrivalCircleRadius, units);
    }
    @Rule("c")
    protected void timeDifference()
    {
    }
    @Rule("decimal c decimal")
    protected void timeDifference(
            float timeDifferenceA,  // uS
            float timeDifferenceB,  // uS
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setTimeDifference(timeDifferenceA, timeDifferenceB);
    }
    @Rule
    protected void waypoint()
    {
    }
    @Rule("string")
    protected void waypoint(
            String waypoint,
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setWaypoint(waypoint);
    }
    @Rule
    protected void utc()
    {
    }
    @Rule("decimal")
    protected void utc(
            float utc,             // hhmmss.ss
            @ParserContext("clock") Clock clock
            )
    {
        clock.setTime(utc);
    }
    @Rule
    protected void date()
    {
    }
    @Rule("integer")
    protected void date(
            int date,               // ddmmyy
            @ParserContext("clock") Clock clock
            )
    {
        clock.setDate(date);
    }
    @Rule
    protected void arrivalStatus()
    {
    }
    @Rule("letter")
    protected void arrivalStatus(
            char arrivalStatus,
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setArrivalStatus(arrivalStatus);
    }
    @Rule
    protected void waypointStatus()
    {
    }
    @Rule("letter")
    protected void waypointStatus(
            char waypointStatus,
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setWaypointStatus(waypointStatus);
    }
    @Rule
    protected void status()
    {
    }
    @Rule("letter")
    protected void status(
            char status,
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setStatus(status);
    }
    @Rule
    protected void status2()
    {
    }
    @Rule("letter")
    protected void status2(
            char status,
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setStatus2(status);
    }
    @Rule
    protected void differentialReferenceStationID()
    {
    }
    @Rule("integer")
    protected void differentialReferenceStationID(
            int differentialReferenceStationID,     //0000-1023            
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setDifferentialReferenceStationID(differentialReferenceStationID);
    }
    @Rule
    protected void ageOfDifferentialGPSData()
    {
    }
    @Rule("integer")
    protected void ageOfDifferentialGPSData(
            int ageOfDifferentialGPSData,   //time in seconds since last SC104 type 1 or 9 update, null field when DGPS is not used
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setAgeOfDifferentialGPSData(ageOfDifferentialGPSData);
    }
    @Rule("c")
    protected void geoidalSeparation()
    {
    }
    @Rule("decimal c letter")
    protected void geoidalSeparation(
            float geoidalSeparation,  //the difference between the WGS-84 earth ellipsoid and mean-sea-level (geoid), "-" means mean-sea-level below ellipsoid
            char unitsOfGeoidalSeparation,       // meters
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setGeoidalSeparation(geoidalSeparation, unitsOfGeoidalSeparation);
    }
    @Rule("c")
    protected void antennaAltitude()
    {
    }
    @Rule("decimal c letter")
    protected void antennaAltitude(
            float antennaAltitude,    // above/below mean-sea-level (geoid) (in meters)
            char unitsOfAntennaAltitude,        //meters
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setAntennaAltitude(antennaAltitude, unitsOfAntennaAltitude);
    }
    @Rule
    protected void horizontalDilutionOfPrecision()
    {
    }
    @Rule("decimal")
    protected void horizontalDilutionOfPrecision(
            float horizontalDilutionOfPrecision,  // (meters)
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setHorizontalDilutionOfPrecision(horizontalDilutionOfPrecision);
    }
    @Rule
    protected void numberOfSatellitesInView()
    {
    }
    @Rule("integer")
    protected void numberOfSatellitesInView(
            int numberOfSatellitesInView,
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setNumberOfSatellitesInView(numberOfSatellitesInView);
    }
    @Rule
    protected void gpsQualityIndicator()
    {
    }
    @Rule("integer")
    protected void gpsQualityIndicator(
            int gpsQualityIndicator,
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setGpsQualityIndicator(gpsQualityIndicator);
    }
    @Rule
    protected void destinationClosingVelocity()
    {
    }
    @Rule("decimal")
    protected void destinationClosingVelocity(
            float destinationClosingVelocity,  // knots
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setDestinationClosingVelocity(destinationClosingVelocity);
    }
    @Rule("c")
    protected void bearingTrue()
    {
    }
    @Rule("decimal c letter")
    protected void bearingTrue(
            float bearingTrue,  // degrees
            char mOrT,  // M = Magnetic, T = True
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setBearingTrue(bearingTrue, mOrT);
    }
    @Rule("c")
    protected void bearingMagnetic()
    {
    }
    @Rule("decimal c letter")
    protected void bearingMagnetic(
            float bearingMagnetic,  // degrees
            char mOrT,  // M = Magnetic, T = True
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setBearingMagnetic(bearingMagnetic, mOrT);
    }
    @Rule
    protected void bearingToDestination()
    {
    }
    @Rule("decimal")
    protected void bearingToDestination(
            float bearingToDestination,  // degrees
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setBearingToDestination(bearingToDestination);
    }
    @Rule("c")
    protected void bearingOriginToDestination()
    {
    }
    @Rule("decimal c letter")
    protected void bearingOriginToDestination(
            float bearingOriginToDestination,  // degrees
            char mOrT,  // M = Magnetic, T = True
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setBearingOriginToDestination(bearingOriginToDestination, mOrT);
    }
    @Rule("c")
    protected void bearingPresentPositionToDestination()
    {
    }
    @Rule("decimal c letter")
    protected void bearingPresentPositionToDestination(
            float bearingPresentPositionToDestination,  // degrees
            char mOrT,  // M = Magnetic, T = True
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setBearingPresentPositionToDestination(bearingPresentPositionToDestination, mOrT);
    }
    @Rule("c")
    protected void headingToSteerToDestination()
    {
    }
    @Rule("decimal c letter")
    protected void headingToSteerToDestination(
            float headingToSteerToDestination,  // degrees
            char mOrT,  // M = Magnetic, T = True
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setHeadingToSteerToDestination(headingToSteerToDestination, mOrT);
    }
    @Rule
    protected void rangeToDestination()
    {
    }
    @Rule("decimal")
    protected void rangeToDestination(
            float rangeToDestination,  // NM
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setRangeToDestination(rangeToDestination);
    }
    @Rule("c")
    protected void waypointToWaypoint()
    {
    }
    @Rule("string c string")
    protected void fromWaypoint(
            String toWaypoint,
            String fromWaypoint,
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setWaypointToWaypoint(toWaypoint, fromWaypoint);
    }
    @Rule("c c")
    protected void crossTrackError()
    {
    }
    @Rule("decimal c letter c letter")
    protected void crossTrackError(
            float crossTrackError,  // NM
            char directionToSteer,
            char units,
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setCrossTrackError(crossTrackError, directionToSteer, units);
    }
    @Rule("c")
    protected void crossTrackErrorNM()
    {
    }
    @Rule("decimal c letter")
    protected void crossTrackErrorNM(
            float crossTrackError,  // NM
            char directionToSteer,
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setCrossTrackError(crossTrackError, directionToSteer);
    }
    @Rule("c")
    protected void magneticVariation()
    {
    }
    @Rule("decimal c ew")
    protected void magneticVariation(
            float magneticVariation,    // degrees
            float mew,
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setMagneticVariation(mew*magneticVariation);
    }
    @Rule
    protected void speedOverGround()
    {
    }
    @Rule("decimal")
    protected void speedOverGround(
            float speedOverGround,  // knots
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setSpeedOverGround(speedOverGround);
    }
    @Rule
    protected void trackMadeGood()
    {
    }
    @Rule("decimal")
    protected void trackMadeGood(
            float trackMadeGood,  // knots
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setTrackMadeGood(trackMadeGood);
    }
    @Rule("c c c")
    protected void location()
    {
    }
    @Rule("latitude c ns c longitude c ew")
    protected void location(
            float latitude,
            float ns,
            float longitude,
            float ew,
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setLocation(ns*latitude, ew*longitude);
    }
    @Rule("c c c")
    protected void destinationWaypointLocation()
    {
    }
    @Rule("latitude c ns c longitude c ew")
    protected void destinationWaypointLocation(
            float latitude,
            float ns,
            float longitude,
            float ew,
            @ParserContext("measurement") Measurement measurement
            )
    {
        measurement.setDestinationWaypointLocation(ns*latitude, ew*longitude);
    }
    @Rule("letter letter")
    protected void talkerId(char c1, char c2, @ParserContext("measurement") Measurement measurement)
    {
        measurement.talkerId(c1, c2);
    }
    @Rule("hexAlpha hexAlpha")
    protected void checksum(
            char x1, 
            char x2, 
            @ParserContext("checksum") Checksum checksum,
            @ParserContext("clock") Clock clock,
            @ParserContext("measurement") Measurement measurement
            )
    {
        int sum = 16*parseHex(x1)+parseHex(x2);
        if (sum != checksum.getValue())
        {
            clock.rollback();
            measurement.rollback("checksum "+Integer.toHexString(sum)+" != "+Integer.toHexString((int)checksum.getValue()));
        }
        else
        {
            clock.commit();
            measurement.commit();
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
         
    @Terminal(expression="[0-9]+")
    protected abstract int integer(int i);
         
    @Terminal(expression="[0-9]+(\\.[0-9]+)*")
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
            @ParserContext("measurement") Measurement measurement,
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
        measurement.rollback("skipping "+sb);
    }
    public void parse(InputStream is, Measurement measurement)
    {
        Checksum checksum = new NMEAChecksum();
        Clock clock = new GPSClock();
        parse(new CheckedInputStream(is, checksum), checksum, clock, measurement);
    }
    @ParseMethod(start = "statements", size=80)
    protected abstract void parse(
            InputStream is, 
            @ParserContext("checksum") Checksum checksum, 
            @ParserContext("clock") Clock clock, 
            @ParserContext("measurement") Measurement measurement
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
            InputStream is = NMEAParser.class.getClassLoader().getResourceAsStream(pck+"test.nmea");
            FileInputStream fis = new FileInputStream("y:\\NMEA data\\2007_05_25_122516.nmea");
            NMEAParser p = NMEAParser.newInstance();
            p.parse(fis, new Tracer());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
