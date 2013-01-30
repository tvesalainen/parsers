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
public interface NMEAData extends Transactional
{
    /**
     * Talker Id of sending device.
     * @param c1
     * @param c2 
     */
    void talkerId(char c1, char c2);
    /**
     * Location in degrees. BWC, BWR, GGA, GLL, RMA, RMC 
     * @param latitude Latitude. South is negative.
     * @param longitude Longitude West is negative.
     */
    public void setLocation(float latitude, float longitude);
    /**
     * RMA, RMC
     * @param speedOverGround 
     */
    public void setSpeedOverGround(float speedOverGround);
    /**
     * RMA, RMC
     * @param trackMadeGood 
     */
    public void setTrackMadeGood(float trackMadeGood);
    /**
     * HDG, RMA, RMC
     * @param magneticVariation 
     */
    public void setMagneticVariation(float magneticVariation);
    /**
     * RMB, APA, APB, XTR
     * @param crossTrackError
     * @param directionToSteer
     * @param units 
     */
    public void setCrossTrackError(float crossTrackError, char directionToSteer, char units);
    /**
     * BOD, BWW, RMB, WNC
     * @param toWaypoint
     * @param fromWaypoint 
     */
    public void setWaypointToWaypoint(String toWaypoint, String fromWaypoint);
    /**
     * RMB, WPL
     * @param latitude
     * @param longitude 
     */
    public void setDestinationWaypointLocation(float latitude, float longitude);
    /**
     * RMB
     * @param rangeToDestination 
     */
    public void setRangeToDestination(float rangeToDestination);
    /**
     * RMB
     * @param bearingToDestination 
     */
    public void setBearingToDestination(float bearingToDestination);
    /**
     * RMB
     * @param destinationClosingVelocity 
     */
    public void setDestinationClosingVelocity(float destinationClosingVelocity);
    /**
     * GGA
     * @param gpsQualityIndicator 
     */
    public void setGpsQualityIndicator(int gpsQualityIndicator);
    /**
     * GGA
     * @param numberOfSatellitesInView 
     */
    public void setNumberOfSatellitesInView(int numberOfSatellitesInView);
    /**
     * GGA
     * @param horizontalDilutionOfPrecision 
     */
    public void setHorizontalDilutionOfPrecision(float horizontalDilutionOfPrecision);
    /**
     * GGA
     * @param antennaAltitude
     * @param unitsOfAntennaAltitude 
     */
    public void setAntennaAltitude(float antennaAltitude, char unitsOfAntennaAltitude);
    /**
     * GGA
     * @param geoidalSeparation
     * @param unitsOfGeoidalSeparation 
     */
    public void setGeoidalSeparation(float geoidalSeparation, char unitsOfGeoidalSeparation);
    /**
     * GGA
     * @param ageOfDifferentialGPSData 
     */
    public void setAgeOfDifferentialGPSData(int ageOfDifferentialGPSData);
    /**
     * GGA
     * @param differentialReferenceStationID 
     */
    public void setDifferentialReferenceStationID(int differentialReferenceStationID);
    /**
     * APA, APB, GLL, MWV, RMA, RMB, RMC, ROT, RSA, XTE
     * @param status 
     */
    public void setStatus(char status);
    /**
     * AAM, APA, APB
     * @param arrivalStatus 
     */
    public void setArrivalStatus(char arrivalStatus);
    /**
     * RMA
     * @param timeDifferenceA
     * @param timeDifferenceB 
     */
    public void setTimeDifference(float timeDifferenceA, float timeDifferenceB);
    /**
     * AAM, APA, APB
     * @param waypointStatus 
     */
    public void setWaypointStatus(char waypointStatus);
    /**
     * AAM
     * @param arrivalCircleRadius
     * @param units 
     */
    public void setArrivalCircleRadius(float arrivalCircleRadius, char units);
    /**
     * AAM, APA, APB, BWC, BWR, R00, WCV, WPL
     * @param waypoint 
     */
    public void setWaypoint(String waypoint);
    /**
     * ALM, RTE
     * @param totalNumberOfMessages 
     */
    public void setTotalNumberOfMessages(int totalNumberOfMessages);
    /**
     * ALM, RTE
     * @param messageNumber 
     */
    public void setMessageNumber(int messageNumber);
    /**
     * ALM
     * @param satellitePRNNumber 
     */
    public void setSatellitePRNNumber(int satellitePRNNumber);
    /**
     * ALM
     * @param gpsWeekNumber 
     */
    public void setGpsWeekNumber(int gpsWeekNumber);
    /**
     * ALM
     * @param svHealth 
     */
    public void setSvHealth(int svHealth);
    /**
     * ALM
     * @param eccentricity 
     */
    public void setEccentricity(float eccentricity);
    /**
     * ALM
     * @param almanacReferenceTime 
     */
    public void setAlmanacReferenceTime(float almanacReferenceTime);
    /**
     * ALM
     * @param inclinationAngle 
     */
    public void setInclinationAngle(float inclinationAngle);
    /**
     * ALM
     * @param rateOfRightAscension 
     */
    public void setRateOfRightAscension(float rateOfRightAscension);
    /**
     * ALM
     * @param rootOfSemiMajorAxis 
     */
    public void setRootOfSemiMajorAxis(float rootOfSemiMajorAxis);
    /**
     * ALM
     * @param argumentOfPerigee 
     */
    public void setArgumentOfPerigee(float argumentOfPerigee);
    /**
     * ALM
     * @param longitudeOfAscensionNode 
     */
    public void setLongitudeOfAscensionNode(float longitudeOfAscensionNode);
    /**
     * ALM
     * @param meanAnomaly 
     */
    public void setMeanAnomaly(float meanAnomaly);
    /**
     * ALM
     * @param f0ClockParameter 
     */
    public void setF0ClockParameter(float f0ClockParameter);
    /**
     * ALM
     * @param f1ClockParameter 
     */
    public void setF1ClockParameter(float f1ClockParameter);
    /**
     * APA, APB, RSA, XTE
     * @param status 
     */
    public void setStatus2(char status);
    /**
     * APA, APB
     * @param bearingOriginToDestination
     * @param mOrT 
     */
    public void setBearingOriginToDestination(float bearingOriginToDestination, char mOrT);
    /**
     * APB
     * @param bearingPresentPositionToDestination
     * @param mOrT 
     */
    public void setBearingPresentPositionToDestination(float bearingPresentPositionToDestination, char mOrT);
    /**
     * APB
     * @param headingToSteerToDestination
     * @param mOrT 
     */
    public void setHeadingToSteerToDestination(float headingToSteerToDestination, char mOrT);
    /**
     * BWC, GLL, XTE
     * @param faaModeIndicator 
     */
    public void setFAAModeIndicator(char faaModeIndicator);
    /**
     * RMM
     * @param horizontalDatum 
     */
    public void setHorizontalDatum(String horizontalDatum);
    /**
     * RTE
     * @param messageMode 
     */
    public void setMessageMode(char messageMode);
    /**
     * R00, RTE
     * @param list 
     */
    public void setWaypoints(List<String> list);
    /**
     * BWC, BWR WNC
     * @param distanceToWaypoint
     * @param units 
     */
    public void setDistanceToWaypoint(float distanceToWaypoint, char units);
    /**
     * DBK
     * @param depth
     * @param unit 
     */
    public void setDepthBelowKeel(float depth, char unit);
    /**
     * DBS
     * @param depth
     * @param unit 
     */
    public void setDepthBelowSurface(float depth, char unit);
    /**
     * DBT
     * @param depth
     * @param unit 
     */
    public void setDepthBelowTransducer(float depth, char unit);
    /**
     * BOD, BWC, BWR, BWW
     * @param bearing
     * @param unit 
     */
    public void setBearing(float bearing, char unit);
    /**
     * DBT
     * @param depth
     * @param offset 
     */
    public void setDepthOfWater(float depth, float offset);
    /**
     * HDG
     * @param magneticDeviation 
     */
    public void setMagneticDeviation(float magneticDeviation);
    /**
     * HDG
     * @param magneticSensorHeading 
     */
    public void setMagneticSensorHeading(float magneticSensorHeading);
    /**
     * HDM, HDT
     * @param heading
     * @param unit 
     */
    public void setHeading(float heading, char unit);
    /**
     * MTW
     * @param waterTemperature
     * @param unit 
     */
    public void setWaterTemperature(float waterTemperature, char unit);
    /**
     * MWV
     * @param windAngle Wind Angle, 0 to 360 degrees
     * @param unit Reference, R = Relative, T = True
     */
    public void setWindAngle(float windAngle, char unit);
    /**
     * MWV, VWR
     * @param windSpeed
     * @param unit Wind Speed Units, K/M/N
     */
    public void setWindSpeed(float windSpeed, char unit);
    /**
     * ROT
     * Rate Of Turn, degrees per minute, "-" means bow turns to port
     * @param rateOfTurn 
     */
    public void setRateOfTurn(float rateOfTurn);
    /**
     * RPM
     * Sourse, S = Shaft, E = Engine
     * @param rpmSource 
     */
    public void setRpmSource(char rpmSource);
    /**
     * RPM
     * Engine or shaft number
     * @param rpmSourceNumber 
     */
    public void setRpmSourceNumber(int rpmSourceNumber);
    /**
     * RPM
     * Speed, Revolutions per minute
     * @param rpm 
     */
    public void setRpm(int rpm);
    /**
     * RPM
     * Propeller pitch, % of maximum, "-" means astern
     * @param propellerPitch 
     */
    public void setPropellerPitch(float propellerPitch);
    /**
     * RSA
     * Starboard (or single) rudder sensor, "-" means Turn To Port
     * @param starboardRudderSensor 
     */
    public void setStarboardRudderSensor(float starboardRudderSensor);
    /**
     * RSA
     * Port rudder sensor
     * @param portRudderSensor 
     */
    public void setPortRudderSensor(float portRudderSensor);
    /**
     * VHW
     * @param waterHeading
     * @param unit 
     */
    public void setWaterHeading(float waterHeading, char unit);
    /**
     * VHW
     * @param waterSpeed
     * @param unit 
     */
    public void setWaterSpeed(float waterSpeed, char unit);
    /**
     * VWR
     * Relative wind angle
     * @param windDirection Wind direction magnitude in degrees
     * @param unit Wind direction Left/Right of bow
     */
    public void setWindDirection(float windDirection, char unit);
    /**
     * WCV
     * @param velocityToWaypoint
     * @param unit 
     */
    public void setVelocityToWaypoint(float velocityToWaypoint, char unit);

}
