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

import java.util.List;
import org.vesalainen.parsers.nmea.ais.EPFDFixTypes;
import org.vesalainen.parsers.nmea.ais.ManeuverIndicator;
import org.vesalainen.parsers.nmea.ais.MessageTypes;
import org.vesalainen.parsers.nmea.ais.NavigationStatus;

/**
 * @author Timo Vesalainen
 */
public class Tracer implements NMEAData, AISData
{

    @Override
    public void talkerId(char c1, char c2)
    {
        System.err.println("talkerId="+c1+c2);
    }

    @Override
    public void commit()
    {
        System.err.println("commit");
    }

    @Override
    public void rollback(String reason)
    {
        System.err.println("rollback("+reason+")");
    }

    @Override
    public void setLocation(float latitude, float longitude)
    {
        System.err.println("setLocation("+latitude+", "+longitude+")");
    }

    @Override
    public void setSpeedOverGround(float speedOverGround)
    {
        System.err.println("setSpeedOverGround("+speedOverGround+")");
    }

    @Override
    public void setTrackMadeGood(float trackMadeGood)
    {
        System.err.println("setTrackMadeGood("+trackMadeGood+")");
    }

    @Override
    public void setMagneticVariation(float magneticVariation)
    {
        System.err.println("setMagneticVariation("+magneticVariation+")");
    }

    @Override
    public void setCrossTrackError(float crossTrackError, char directionToSteer, char units)
    {
        System.err.println("setCrossTrackError("+crossTrackError+", "+directionToSteer+", "+units+")");
    }

    @Override
    public void setWaypointToWaypoint(String toWaypoint, String fromWaypoint)
    {
        System.err.println("setWaypointToWaypoint("+toWaypoint+", "+fromWaypoint+")");
    }

    @Override
    public void setDestinationWaypointLocation(float latitude, float longitude)
    {
        System.err.println("setDestinationWaypointLocation("+latitude+", "+longitude+")");
    }

    @Override
    public void setRangeToDestination(float rangeToDestination)
    {
        System.err.println("setRangeToDestination("+rangeToDestination+")");
    }

    @Override
    public void setBearingToDestination(float bearingToDestination)
    {
        System.err.println("setBearingToDestination("+bearingToDestination+")");
    }

    @Override
    public void setDestinationClosingVelocity(float destinationClosingVelocity)
    {
        System.err.println("setDestinationClosingVelocity("+destinationClosingVelocity+")");
    }

    @Override
    public void setGpsQualityIndicator(int gpsQualityIndicator)
    {
        System.err.println("setGpsQualityIndicator("+gpsQualityIndicator+")");
    }

    @Override
    public void setNumberOfSatellitesInView(int numberOfSatellitesInView)
    {
        System.err.println("setNumberOfSatellitesInView("+numberOfSatellitesInView+")");
    }

    @Override
    public void setHorizontalDilutionOfPrecision(float horizontalDilutionOfPrecision)
    {
        System.err.println("setHorizontalDilutionOfPrecision("+horizontalDilutionOfPrecision+")");
    }

    @Override
    public void setAntennaAltitude(float antennaAltitude, char unitsOfAntennaAltitude)
    {
        System.err.println("setAntennaAltitude("+antennaAltitude+", "+unitsOfAntennaAltitude+")");
    }

    @Override
    public void setGeoidalSeparation(float geoidalSeparation, char unitsOfGeoidalSeparation)
    {
        System.err.println("setGeoidalSeparation("+geoidalSeparation+", "+unitsOfGeoidalSeparation+")");
    }

    @Override
    public void setAgeOfDifferentialGPSData(int ageOfDifferentialGPSData)
    {
        System.err.println("setAgeOfDifferentialGPSData("+ageOfDifferentialGPSData+")");
    }

    @Override
    public void setDifferentialReferenceStationID(int differentialReferenceStationID)
    {
        System.err.println("setDifferentialReferenceStationID("+differentialReferenceStationID+")");
    }

    @Override
    public void setStatus(char status)
    {
        System.err.println("setStatus("+status+")");
    }

    @Override
    public void setArrivalStatus(char arrivalStatus)
    {
        System.err.println("setArrivalStatus("+arrivalStatus+")");
    }

    @Override
    public void setTimeDifference(float timeDifferenceA, float timeDifferenceB)
    {
        System.err.println("setTimeDifference("+timeDifferenceA+", "+timeDifferenceB+")");
    }

    @Override
    public void setWaypointStatus(char waypointStatus)
    {
        System.err.println("setWaypointStatus("+waypointStatus+")");
    }

    @Override
    public void setArrivalCircleRadius(float arrivalCircleRadius, char units)
    {
        System.err.println("setArrivalCircleRadius("+arrivalCircleRadius+", "+units+")");
}

    @Override
    public void setWaypoint(String waypoint)
    {
        System.err.println("setWaypoint("+waypoint+")");
    }

    @Override
    public void setTotalNumberOfMessages(int totalNumberOfMessages)
    {
        System.err.println("setTotalNumberOfMessages("+totalNumberOfMessages+")");
    }

    @Override
    public void setMessageNumber(int messageNumber)
    {
        System.err.println("setMessageNumber("+messageNumber+")");
    }

    @Override
    public void setSatellitePRNNumber(int satellitePRNNumber)
    {
        System.err.println("setSatellitePRNNumber("+satellitePRNNumber+")");
    }

    @Override
    public void setGpsWeekNumber(int gpsWeekNumber)
    {
        System.err.println("setGpsWeekNumber("+gpsWeekNumber+")");
    }

    @Override
    public void setSvHealth(int svHealth)
    {
        System.err.println("setSvHealth("+svHealth+")");
    }

    @Override
    public void setEccentricity(float eccentricity)
    {
        System.err.println("setEccentricity("+eccentricity+")");
    }

    @Override
    public void setAlmanacReferenceTime(float almanacReferenceTime)
    {
        System.err.println("setAlmanacReferenceTime("+almanacReferenceTime+")");
    }

    @Override
    public void setInclinationAngle(float inclinationAngle)
    {
        System.err.println("setInclinationAngle("+inclinationAngle+")");
    }

    @Override
    public void setRateOfRightAscension(float rateOfRightAscension)
    {
        System.err.println("setRateOfRightAscension("+rateOfRightAscension+")");
    }

    @Override
    public void setRootOfSemiMajorAxis(float rootOfSemiMajorAxis)
    {
        System.err.println("setRootOfSemiMajorAxis("+rootOfSemiMajorAxis+")");
    }

    @Override
    public void setArgumentOfPerigee(float argumentOfPerigee)
    {
        System.err.println("setArgumentOfPerigee("+argumentOfPerigee+")");
    }

    @Override
    public void setLongitudeOfAscensionNode(float longitudeOfAscensionNode)
    {
        System.err.println("setLongitudeOfAscensionNode("+longitudeOfAscensionNode+")");
    }

    @Override
    public void setMeanAnomaly(float meanAnomaly)
    {
        System.err.println("setMeanAnomaly("+meanAnomaly+")");
    }

    @Override
    public void setF0ClockParameter(float f0ClockParameter)
    {
        System.err.println("setF0ClockParameter("+f0ClockParameter+")");
    }

    @Override
    public void setF1ClockParameter(float f1ClockParameter)
    {
        System.err.println("setF1ClockParameter("+f1ClockParameter+")");
    }

    @Override
    public void setStatus2(char status)
    {
        System.err.println("setStatus2("+status+")");
    }

    @Override
    public void setBearingOriginToDestination(float bearingOriginToDestination, char mOrT)
    {
        System.err.println("setBearingOriginToDestination("+bearingOriginToDestination+", "+mOrT+")");
    }

    @Override
    public void setBearingPresentPositionToDestination(float bearingPresentPositionToDestination, char mOrT)
    {
        System.err.println("setBearingPresentPositionToDestination("+bearingPresentPositionToDestination+", "+mOrT+")");
    }

    @Override
    public void setHeadingToSteerToDestination(float headingToSteerToDestination, char mOrT)
    {
        System.err.println("setHeadingToSteerToDestination("+headingToSteerToDestination+", "+mOrT+")");
    }

    @Override
    public void setFAAModeIndicator(char setFAAModeIndicator)
    {
        System.err.println("setFAAModeIndicator("+setFAAModeIndicator+")");
    }

    @Override
    public void setHorizontalDatum(String horizontalDatum)
    {
        System.err.println("setHorizontalDatum("+horizontalDatum+")");
    }

    @Override
    public void setMessageMode(char messageMode)
    {
        System.err.println("setMessageMode("+messageMode+")");
    }

    @Override
    public void setWaypoints(List<String> list)
    {
        System.err.println("setWaypointList("+list+")");
    }

    @Override
    public void setDistanceToWaypoint(float distanceToWaypoint, char units)
    {
        System.err.println("setDistanceToWaypoint("+distanceToWaypoint+", "+units+")");
    }

    @Override
    public void setDepthBelowKeel(float depth, char unit)
    {
        System.err.println("setDepthBelowKeel("+depth+", "+unit+")");
    }

    @Override
    public void setDepthBelowSurface(float depth, char unit)
    {
        System.err.println("setDepthBelowSurface("+depth+", "+unit+")");
    }

    @Override
    public void setDepthBelowTransducer(float depth, char unit)
    {
        System.err.println("setDepthBelowTransducer("+depth+", "+unit+")");
    }

    @Override
    public void setBearing(float bearing, char unit)
    {
        System.err.println("setBearing("+bearing+", "+unit+")");
    }

    @Override
    public void setDepthOfWater(float depth, float offset)
    {
        System.err.println("setDepthOfWater("+depth+", "+offset+")");
    }

    @Override
    public void setMagneticDeviation(float magneticDeviation)
    {
        System.err.println("setMagneticDeviation("+magneticDeviation+")");
    }

    @Override
    public void setMagneticSensorHeading(float magneticSensorHeading)
    {
        System.err.println("setMagneticSensorHeading("+magneticSensorHeading+")");
    }

    @Override
    public void setHeading(float heading, char unit)
    {
        System.err.println("setHeading("+heading+", "+unit+")");
    }

    @Override
    public void setWaterTemperature(float waterTemperature, char unit)
    {
        System.err.println("setWaterTemperature("+waterTemperature+", "+unit+")");
    }

    @Override
    public void setWindAngle(float windAngle, char unit)
    {
        System.err.println("setWindAngle("+windAngle+", "+unit+")");
    }

    @Override
    public void setWindSpeed(float windSpeed, char unit)
    {
        System.err.println("setWindSpeed("+windSpeed+", "+unit+")");
    }

    @Override
    public void setRateOfTurn(float rateOfTurn)
    {
        System.err.println("setRateOfTurn("+rateOfTurn+")");
    }

    @Override
    public void setRpmSource(char setRpmSource)
    {
        System.err.println("setRpmSource("+setRpmSource+")");
    }

    @Override
    public void setRpmSourceNumber(int rpmSourceNumber)
    {
        System.err.println("setRpmSourceNumber("+rpmSourceNumber+")");
    }

    @Override
    public void setRpm(int rpm)
    {
        System.err.println("setRpmS("+rpm+")");
    }

    @Override
    public void setPropellerPitch(float propellerPitch)
    {
        System.err.println("setPropellerPitch("+propellerPitch+")");
    }

    @Override
    public void setStarboardRudderSensor(float starboardRudderSensor)
    {
        System.err.println("setStarboardRudderSensor("+starboardRudderSensor+")");
    }

    @Override
    public void setPortRudderSensor(float portRudderSensor)
    {
        System.err.println("setPortRudderSensor("+portRudderSensor+")");
    }

    @Override
    public void setWaterHeading(float waterHeading, char unit)
    {
        System.err.println("setWaterHeading("+waterHeading+", "+unit+")");
    }

    @Override
    public void setWaterSpeed(float waterSpeed, char unit)
    {
        System.err.println("setWaterSpeed("+waterSpeed+", "+unit+")");
    }

    @Override
    public void setWindDirection(float windDirection, char unit)
    {
        System.err.println("setWindDirection("+windDirection+", "+unit+")");
    }

    @Override
    public void setVelocityToWaypoint(float velocityToWaypoint, char unit)
    {
        System.err.println("setVelocityToWaypoint("+velocityToWaypoint+", "+unit+")");
    }

    @Override
    public void setNumberOfSentences(int numberOfSentences)
    {
        System.err.println("setNumberOfSentences("+numberOfSentences+")");
    }

    @Override
    public void setSentenceNumber(int sentenceNumber)
    {
        System.err.println("setSentenceNumber("+sentenceNumber+")");
    }

    @Override
    public void setSequenceMessageId(int sequentialMessageId)
    {
        System.err.println("setSequenceMessageId("+sequentialMessageId+")");
    }

    @Override
    public void setChannel(char channel)
    {
        System.err.println("setChannel("+channel+")");
    }

    @Override
    public void setMessageType(MessageTypes messageTypes)
    {
        System.err.println("setMessageType("+messageTypes+")");
    }

    @Override
    public void setRepeatIndicator(int repeatIndicator)
    {
        System.err.println("setRepeatIndicator("+repeatIndicator+")");
    }

    @Override
    public void setMMSI(int mmsi)
    {
        System.err.println("setMMSI("+mmsi+")");
    }

    @Override
    public void setStatus(NavigationStatus navigationStatus)
    {
        System.err.println("setStatus("+navigationStatus+")");
    }

    @Override
    public void setTurn(float degreesPerMinute)
    {
        System.err.println("setTurn("+degreesPerMinute+")");
    }

    @Override
    public void setSpeed(float knots)
    {
        System.err.println("setSpeed("+knots+")");
    }

    @Override
    public void setAccuracy(boolean accuracy)
    {
        System.err.println("setAccuracy("+accuracy+")");
    }

    @Override
    public void setLongitude(double longitude)
    {
        System.err.println("setLongitude("+longitude+")");
    }

    @Override
    public void setLatitude(float latitude)
    {
        System.err.println("setLatitude("+latitude+")");
    }

    @Override
    public void setCourse(float setCourse)
    {
        System.err.println("setCourse("+setCourse+")");
    }

    @Override
    public void setHeading(int heading)
    {
        System.err.println("setHeading("+heading+")");
    }

    @Override
    public void setSecond(int second)
    {
        System.err.println("setSecond("+second+")");
    }

    @Override
    public void setManeuver(ManeuverIndicator maneuverIndicator)
    {
        System.err.println("setManeuver("+maneuverIndicator+")");
    }

    @Override
    public void setRAIM(boolean raim)
    {
        System.err.println("setRAIM("+raim+")");
    }

    @Override
    public void setRadioStatus(int radio)
    {
        System.err.println("setRadioStatus("+radio+")");
    }

    @Override
    public void setYear(int year)
    {
        System.err.println("setYear("+year+")");
    }

    @Override
    public void setMonth(int month)
    {
        System.err.println("setMonth("+month+")");
    }

    @Override
    public void setDay(int day)
    {
        System.err.println("setDay("+day+")");
    }

    @Override
    public void setHour(int hour)
    {
        System.err.println("setHour("+hour+")");
    }

    @Override
    public void setMinute(int minute)
    {
        System.err.println("setMinute("+minute+")");
    }

    @Override
    public void setEPFD(EPFDFixTypes epfdFixTypes)
    {
        System.err.println("setEPFD("+epfdFixTypes+")");
    }

    @Override
    public void setVersion(int version)
    {
        System.err.println("setVersion("+version+")");
    }

    @Override
    public void setIMONumber(int setIMONumber)
    {
        System.err.println("setIMONumber("+setIMONumber+")");
    }

}
