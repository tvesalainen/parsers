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

/**
 * @author Timo Vesalainen
 */
public interface Measurement 
{
    void talkerId(char c1, char c2);

    public void rollback(String reason);

    public void commit();

    public void setLocation(float latitude, float longitude);

    public void setSpeedOverGround(float speedOverGround);

    public void setTrackMadeGood(float trackMadeGood);

    public void setMagneticVariation(float magneticVariation);

    public void setCrossTrackError(float crossTrackError, char directionToSteer);

    public void setCrossTrackError(float crossTrackError, char directionToSteer, char units);

    public void setWaypointToWaypoint(String toWaypoint, String fromWaypoint);

    public void setDestinationWaypointLocation(float latitude, float longitude);

    public void setRangeToDestination(float rangeToDestination);

    public void setBearingToDestination(float bearingToDestination);

    public void setDestinationClosingVelocity(float destinationClosingVelocity);

    public void setGpsQualityIndicator(int gpsQualityIndicator);

    public void setNumberOfSatellitesInView(int numberOfSatellitesInView);

    public void setHorizontalDilutionOfPrecision(float horizontalDilutionOfPrecision);

    public void setAntennaAltitude(float antennaAltitude, char unitsOfAntennaAltitude);

    public void setGeoidalSeparation(float geoidalSeparation, char unitsOfGeoidalSeparation);

    public void setAgeOfDifferentialGPSData(int ageOfDifferentialGPSData);

    public void setDifferentialReferenceStationID(int differentialReferenceStationID);

    public void setStatus(char status);

    public void setArrivalStatus(char arrivalStatus);

    public void setTimeDifference(float timeDifferenceA, float timeDifferenceB);

    public void setWaypointStatus(char waypointStatus);

    public void setArrivalCircleRadius(float arrivalCircleRadius, char units);

    public void setWaypoint(String waypoint);

    public void setTotalNumberOfMessages(int totalNumberOfMessages);

    public void setMessageNumber(int messageNumber);

    public void setSatellitePRNNumber(int satellitePRNNumber);

    public void setGpsWeekNumber(int gpsWeekNumber);

    public void setSvHealth(int svHealth);

    public void setEccentricity(float eccentricity);

    public void setAlmanacReferenceTime(float almanacReferenceTime);

    public void setInclinationAngle(float inclinationAngle);

    public void setRateOfRightAscension(float rateOfRightAscension);

    public void setRootOfSemiMajorAxis(float rootOfSemiMajorAxis);

    public void setArgumentOfPerigee(float argumentOfPerigee);

    public void setLongitudeOfAscensionNode(float longitudeOfAscensionNode);

    public void setMeanAnomaly(float meanAnomaly);

    public void setF0ClockParameter(float f0ClockParameter);

    public void setF1ClockParameter(float f1ClockParameter);

    public void setStatus2(char status);

    public void setBearingOriginToDestination(float bearingOriginToDestination, char mOrT);

    public void setBearingPresentPositionToDestination(float bearingPresentPositionToDestination, char mOrT);

    public void setHeadingToSteerToDestination(float headingToSteerToDestination, char mOrT);

    public void setBearingTrue(float bearingTrue, char mOrT);

    public void setBearingMagnetic(float bearingMagnetic, char mOrT);

    public void setFAAModeIndicator(char faaModeIndicator);

    public void setHorizontalDatum(String horizontalDatum);

    public void setMessageMode(char messageMode);

    public void setWaypointList(List<String> list);

    public void setDistanceToWaypoint(float distanceToWaypoint, char units);

    public void setDepthBelowKeel(float depth, char unit);

    public void setDepthBelowSurface(float depth, char unit);

    public void setDepthBelowTransducer(float depth, char unit);

    public void setBearing(float bearing, char unit);

    public void setDepthOfWater(float depth, float offset);

    public void setMagneticDeviation(float magneticDeviation);

    public void setMagneticSensorHeading(float magneticSensorHeading);

    public void setMagneticHeading(float magneticHeading);

    public void setHeading(float heading, char unit);

    public void setWaterTemperature(float waterTemperature, char unit);

    public void setWindAngle(float windAngle, char unit);

    public void setWindSpeed(float windSpeed, char unit);

}
