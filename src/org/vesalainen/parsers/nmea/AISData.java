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
    /**
     * Call Sign
     * @param fromSixBitCharacters 
     */
    public void setCallSign(String fromSixBitCharacters);
    /**
     * Vessel Name
     * @param fromSixBitCharacters 
     */
    public void setVesselName(String fromSixBitCharacters);
    /**
     * Dimension to Bow
     * @param dimension meters 
     */
    public void setDimensionToBow(int dimension);
    /**
     * Dimension to Stern
     * @param dimension meters 
     */
    public void setDimensionToStern(int dimension);
    /**
     * Dimension to Port
     * @param dimension meters 
     */
    public void setDimensionToPort(int dimension);
    /**
     * Dimension to Starboard
     * @param dimension meters 
     */
    public void setDimensionToStarboard(int dimension);
    /**
     * Draught
     * @param meters 
     */
    public void setDraught(float meters);
    /**
     * Destination
     * @param fromSixBitCharacters 
     */
    public void setDestination(String fromSixBitCharacters);
    /**
     * Data terminal ready
     * @param b 
     */
    public void setDTE(boolean ready);
    /**
     * Ship Type
     * @param codesForShipType 
     */
    public void setShipType(CodesForShipType codesForShipType);
    /**
     * Sequence Number
     * @param seq 
     */
    public void setSequenceNumber(int seq);
    /**
     * Destination MMSI
     * @see <a href="http://en.wikipedia.org/wiki/Maritime_Mobile_Service_Identity">Maritime Mobile Service Identity</a>
     * @param mmsi 
     */
    public void setDestinationMMSI(int mmsi);
    /**
     * Retransmit flag
     * @param retransmit 
     */
    public void setRetransmit(boolean retransmit);
    /**
     * Designated Area Code. The Designated Area Code, which is a jurisdiction code: 
     * 366 for the United States. It uses the same encoding as the area designator 
     * in MMMSIs; 1 designates international (ITU) messages.
     * @see <a href="http://www.itu.int/online/mms/glad/cga_mids.sh">Table of Maritime Identification Digits</a>
     * @param dac 
     */
    public void setDAC(int dac);
    /**
     * Functional ID for a message subtype. 
     * @param fid 
     */
    public void setFID(int fid);
    /**
     * Last Port Of Call. 
     * @param locode UN locode
     */
    public void setLastPort(String locode);

    /**
     * ETA Month (UTC). 
     * @param month 1-12
     */
    public void setLastPortMonth(int month);
    /**
     * ETA Day (UTC) 1-31
     * @param day 
     */
    public void setLastPortDay(int day);
    /**
     * ETA Hour (UTC) 0-23
     * @param hour 
     */
    public void setLastPortHour(int hour);
    /**
     * ETA Minute (UTC) 0-59
     * @param minute 
     */
    public void setLastPortMinute(int minute);
    /**
     * Next Port Of Call. 
     * @param locode UN locode
     */
    public void setNextPort(String locode);
    /**
     * ETA Month (UTC). 
     * @param month 1-12
     */
    public void setNextPortMonth(int month);
    /**
     * ETA Day (UTC) 1-31
     * @param day 
     */
    public void setNextPortDay(int day);
    /**
     * ETA Hour (UTC) 0-23
     * @param hour 
     */
    public void setNextPortHour(int hour);
    /**
     * ETA Minute (UTC) 0-59
     * @param minute 
     */
    public void setNextPortMinute(int minute);
    /**
     * Main Dangerous Good
     * @param fromSixBitCharacters 
     */
    public void setMainDangerousGood(String fromSixBitCharacters);
    /**
     * IMD Category
     * @param fromSixBitCharacters 
     */
    public void setIMDCategory(String fromSixBitCharacters);
    /**
     * UN Number
     * @param unid 
     */
    public void UNNumber(int unid);
    /**
     * Amount of Cargo
     * @param amount 
     */
    public void AmountOfCargo(int amount);
    /**
     * Unit of Quantity
     * @param cargoUnitCodes 
     */
    public void setUnitOfQuantity(CargoUnitCodes cargoUnitCodes);
    /**
     * From Hour (UTC) 0-23
     * @param hour 
     */
    public void setFromHour(int hour);
    /**
     * From Minute (UTC) 0-59
     * @param minute 
     */
    public void setFromMinute(int minute);
    /**
     * To Hour (UTC) 0-23
     * @param hour 
     */
    public void setToHour(int hour);
    /**
     * To Minute (UTC) 0-59
     * @param minute 
     */
    public void setToMinute(int minute);
    /**
     * Current Direction Predicted
     * @param currentDirection degrees 0-359
     */
    public void setCurrentDirection(int currentDirection);
    /**
     * Current Speed Predicted
     * @param knots 
     */
    public void setCurrentSpeed(float knots);
    /**
     * # persons on board. 
     * @param persons 
     */
    public void setPersonsOnBoard(int persons);
    /**
     * Message Linkage ID
     * @param id 
     */
    public void setLinkage(int id);
    /**
     * Name of Port & Berth
     * @param fromSixBitCharacters 
     */
    public void setPortname(String fromSixBitCharacters);
    /**
     * Notice Description
     * @param areaNoticeDescription 
     */
    public void setAreaNotice(AreaNoticeDescription areaNoticeDescription);
    /**
     * Duration
     * @param duration In minutes, 0 = cancel this notice.
     */
    public void setDuration(int duration);
    /**
     * Shape of area
     * @param subareaType 
     */
    public void setShape(SubareaType subareaType);
    /**
     * Scale factor
     * @param scale Exponent for area dimensions 1 = meters (default)
     */
    public void setScale(int scale);
    /**
     * Precision
     * @param precision Decimal places of precision (defaults to 4)
     */
    public void setPrecision(int precision);
    /**
     * Radius
     * @param radius Radius of area 0 = point (default), else 1-4095 * 10^scale m
     */
    public void setRadius(int radius);
    /**
     * E dimension
     * @param east Box dimension east 0 = N/S line (default), else 1-255 * 10^scale m
     */
    public void setEast(int east);
    /**
     * N dimension
     * @param north Box dimension north 0 = E/W line (default), else 1-255 * 10^scale m
     */
    public void setNorth(int north);
    /**
     * Orientation
     * @param orientation Degrees clockwise from true N, 0 = no rotation (default), else 1-359, 360-511 reserved.
     */
    public void setOrientation(int orientation);
    /**
     * Left boundary
     * @param left Degrees clockwise from true N, 0 = no rotation (default), else 1-359, 360-511 reserved.
     */
    public void setLeft(int left);
    /**
     * Right boundary
     * @param right Degrees clockwise from true N, 0 = no rotation (default), else 1-359, 360-511 reserved.
     */
    public void setRight(int right);
    /**
     * Bearing
     * @param bearing True bearing in half-degree steps from previous waypoint; 720 = N/A (default).
     */
    public void setBearing(int bearing);
    /**
     * Distance
     * @param distance Distance from prev. waypoint, 0 = no point (default), else 1-1023 * 10^scale m
     */
    public void setDistance(int distance);
    /**
     * Text
     * @param fromSixBitCharacters 
     */
    public void setText(String fromSixBitCharacters);
    /**
     * Berth length
     * @param meters In 1m steps, 1-510m, 511 = >= 511m 0 = N/A (default).
     */
    public void setBerthLength(int meters);
    /**
     * Berth Water Depth
     * @param meters 0.1-25.4m in 0.1 steps 255 = >= 25.5m 0 = N/A (default)
     */
    public void setBerthDepth(float meters );
    /**
     * Services Availability
     * @param available 
     */
    public void setServicesAvailability(boolean available );
    /**
     * Name of Berth
     * @param fromSixBitCharacters 
     */
    public void setBerthName(String fromSixBitCharacters);
    /**
     * Mooring Position
     * @param mooringPosition 
     */
    public void setMooringPosition(MooringPosition mooringPosition);
    /**
     * Agent
     * @param serviceStatus 
     */
    public void setAgentServiceStatus(ServiceStatus serviceStatus);
    /**
     * Bunker/fuel
     * @param serviceStatus 
     */
    public void setFuelServiceStatus(ServiceStatus serviceStatus);
    /**
     * Chandler
     * @param serviceStatus 
     */
    public void setChandlerServiceStatus(ServiceStatus serviceStatus);
    /**
     * Stevedore
     * @param serviceStatus 
     */
    public void setStevedoreServiceStatus(ServiceStatus serviceStatus);
    /**
     * Electrical
     * @param serviceStatus 
     */
    public void setElectricalServiceStatus(ServiceStatus serviceStatus);
    /**
     * Potable water
     * @param serviceStatus 
     */
    public void setWaterServiceStatus(ServiceStatus serviceStatus);
    /**
     * Customs house
     * @param serviceStatus 
     */
    public void setCustomsServiceStatus(ServiceStatus serviceStatus);
    /**
     * Cartage
     * @param serviceStatus 
     */
    public void setCartageServiceStatus(ServiceStatus serviceStatus);
    /**
     * Crane(s)
     * @param serviceStatus 
     */
    public void setCraneServiceStatus(ServiceStatus serviceStatus);
    /**
     * Lift(s)
     * @param serviceStatus 
     */
    public void setLiftServiceStatus(ServiceStatus serviceStatus);
    /**
     * Medical facilities
     * @param serviceStatus 
     */
    public void setMedicalServiceStatus(ServiceStatus serviceStatus);
    /**
     * Navigation repair
     * @param serviceStatus 
     */
    public void setNavrepairServiceStatus(ServiceStatus serviceStatus);
    /**
     * Provisions
     * @param serviceStatus 
     */
    public void setProvisionsServiceStatus(ServiceStatus serviceStatus);
    /**
     * Ship repair
     * @param serviceStatus 
     */
    public void setShiprepairServiceStatus(ServiceStatus serviceStatus);
    /**
     * Surveyor
     * @param serviceStatus 
     */
    public void setSurveyorServiceStatus(ServiceStatus serviceStatus);
    /**
     * Steam
     * @param serviceStatus 
     */
    public void setSteamServiceStatus(ServiceStatus serviceStatus);
    /**
     * Tugs
     * @param serviceStatus 
     */
    public void setTugsServiceStatus(ServiceStatus serviceStatus);
    /**
     * Waste disposal (solid)
     * @param serviceStatus 
     */
    public void setSolidwasteServiceStatus(ServiceStatus serviceStatus);
    /**
     * Waste disposal (liquid)
     * @param serviceStatus 
     */
    public void setLiquidwasteServiceStatus(ServiceStatus serviceStatus);
    /**
     * Waste disposal (hazardous)
     * @param serviceStatus 
     */
    public void setHazardouswasteServiceStatus(ServiceStatus serviceStatus);
    /**
     * Reserved ballast exchange
     * @param serviceStatus 
     */
    public void setBallastServiceStatus(ServiceStatus serviceStatus);
    /**
     * Additional services
     * @param serviceStatus 
     */
    public void setAdditionalServiceStatus(ServiceStatus serviceStatus);
    /**
     * Regional reserved 1
     * @param serviceStatus 
     */
    public void setRegional1ServiceStatus(ServiceStatus serviceStatus);
    /**
     * Regional reserved 2
     * @param serviceStatus 
     */
    public void setRegional2ServiceStatus(ServiceStatus serviceStatus);
    /**
     * Reserved for future
     * @param serviceStatus 
     */
    public void setFuture1ServiceStatus(ServiceStatus serviceStatus);
    /**
     * Reserved for future
     * @param serviceStatus 
     */
    public void setFuture2ServiceStatus(ServiceStatus serviceStatus);
    /**
     * Sender Class
     * @param sender 0 = ship (default), 1 = authority, 27 = reserved for future use
     */
    public void setSender(int sender);
    /**
     * Waypoint count
     * @param count 
     */
    public void setWaypointCount(int count);
    /**
     * Route Type
     * @param routeTypeCodes 
     */
    public void setRouteType(RouteTypeCodes routeTypeCodes);
    /**
     * Description
     * @param fromSixBitCharacters 
     */
    public void setDescription(String fromSixBitCharacters);
    /**
     * MMSI number 1
     * @param mmsi 
     */
    public void setMMSI1(int mmsi);
    /**
     * MMSI number 2
     * @param mmsi 
     */
    public void setMMSI2(int mmsi);
    /**
     * MMSI number 3
     * @param mmsi 
     */
    public void setMMSI3(int mmsi);
    /**
     * MMSI number 4
     * @param mmsi 
     */
    public void setMMSI4(int mmsi);
    /**
     * Average Wind Speed
     * @param knots 10-min avg wind speed, knots
     */
    public void setAverageWindSpeed(int knots);
    /**
     * Gust Speed
     * @param knots 10-min max wind speed, knots
     */
    public void setGustSpeed(int knots);
    /**
     * Wind Direction
     * @param degrees 0-359, degrees fom true north 
     */
    public void setWindDirection(int degrees);
    /**
     * Wind Gust Direction
     * @param degrees 0-359, degrees fom true north 
     */
    public void setWindGustDirection(int degrees);
    /**
     * Air Temperature
     * @param degrees C
     */
    public void setAirTemperature(float degrees);
    /**
     * Relative Humidity
     * @param humidity 0-100%, units of 1%, 127 = N/A (default).
     */
    public void setRelativeHumidity(int humidity);
    /**
     * Dew Point
     * @param degrees -20.0 to +50.0: 0.1 deg C
     */
    public void setDewPoint(float degrees);
    /**
     * 800-1200hPa: units 1hPa
     * @param pressure 
     */
    public void setAirPressure(int pressure);
    /**
     * Pressure Tendency
     * @param tendency 0 = steady, 1 = decreasing, 2 = increasing, 3 - N/A (default).
     */
    public void setAirPressureTendency(int tendency);
    /**
     * Horiz. Visibility
     * @param nm 
     */
    public void setVisibility(float nm);
    /**
     * Water Level
     * @param meters 
     */
    public void setWaterLevel(float meters);
    /**
     * Water Level Trend
     * @param trend 0 = steady, 1 = decreasing, 2 = increasing, 3 - N/A (default).
     */
    public void setWaterLevelTrend(int trend);
    /**
     * Surface Current Speed
     * @param knots 0.0-25.0 knots: units 0.1 knot
     */
    public void setSurfaceCurrentSpeed(float knots);
    /**
     * Current Speed #2
     * @param knots 0.0-25.0 in units of 0.1 knot, >=251 = N/A (default).
     */
    public void setCurrentSpeed2(float knots);
    /**
     * Current Direction #2
     * @param degrees 0-359: deg. fom true north, >=360 = N/A (default)
     */
    public void setCurrentDirection2(int degrees);
    /**
     * Measurement Depth #2
     * @param meters 0-30m down: units 0.1m, 31 = N/A (default).
     */
    public void setMeasurementDepth2(float meters);
    /**
     * Current Speed #3
     * @param knots 0.0-25.0 in units of 0.1 knot, >=251 = N/A (default).
     */
    public void setCurrentSpeed3(float knots);
    /**
     * Current Direction #3
     * @param degrees 0-359: deg. fom true north, >=360 = N/A (default)
     */
    public void setCurrentDirection3(int degrees);
    /**
     * Measurement Depth #3
     * @param meters 0-30m down: units 0.1m, 31 = N/A (default).
     */
    public void setMeasurementDepth3(float meters);
    /**
     * Wave height
     * @param meters 0-25m: units of 0.1m, >=251 = N/A (default).
     */
    public void setWaveHeight(float meters);
    /**
     * Wave period
     * @param seconds Seconds 0-60: >= 61 = N/A (default).
     */
    public void setWavePeriod(int seconds);
    /**
     * Wave direction
     * @param degrees 0-359: deg. fom true north, >=360 = N/A (default).
     */
    public void setWaveDirection(int degrees);
    /**
     * Swell height
     * @param meters 0-25m: units of 0.1m, >=251 = N/A (default).
     */
    public void setSwellHeight(float meters);
    /**
     * Swell period
     * @param seconds Seconds 0-60: >= 61 = N/A (default).
     */
    public void setSwellPeriod(int seconds);
    /**
     * Swell direction
     * @param degrees 0-359: deg. fom true north, >=360 = N/A (default).
     */
    public void setSwellDirection(int degrees);
    /**
     * Water Temperature
     * @param degrees -10.0 to 50.0: units 0.1 C
     */
    public void setWaterTemperature(float degrees);
    /**
     * Salinity
     * @param f 0.0-50.0%: units 0.1%
     */
    public void setSalinity(float f);
    /**
     * Ice
     * @param ice Yes/No (??? this is 2-bit field???)
     */
    public void setIce(int ice);
    /**
     * Precipitation
     * @param precipitationTypes 
     */
    public void setPrecipitation(PrecipitationTypes precipitationTypes);
    /**
     * Sea state
     * @param beaufortScale 
     */
    public void setSeaState(BeaufortScale beaufortScale);
    /**
     * Reason For Closing
     * @param fromSixBitCharacters 
     */
    public void setReasonForClosing(String fromSixBitCharacters);
    /**
     * Location Of Closing From
     * @param fromSixBitCharacters 
     */
    public void setClosingFrom(String fromSixBitCharacters);
    /**
     * Location of Closing To
     * @param fromSixBitCharacters 
     */
    public void setClosingTo(String fromSixBitCharacters);
    /**
     * Unit of extension
     * @param unit
     */
    public void setUnitOfExtension(ExtensionUnit unit);
    /**
     * From month (UTC)
     * @param month 1-12
     */
    public void setFromMonth(int month);
    /**
     * From day (UTC)
     * @param day 1-31
     */
    public void setFromDay(int day);
    /**
     * To month (UTC)
     * @param month 1-12
     */
    public void setToMonth(int month);
    /**
     * To day (UTC)
     * @param day 1-31
     */
    public void setToDay(int day);
    /**
     * Air Draught
     * @param meters Height in meters
     */
    public void setAirDraught(int meters);
    /**
     * Identifier type
     * @param targetIdentifierType 
     */
    public void setIdType(TargetIdentifierType targetIdentifierType);
    /**
     * Target identifier
     * @param id Target ID data.
     * @see #setIdType(TargetIdentifierType)
     */
    public void setId(long id);
    /**
     * Name of Signal Station
     * @param fromSixBitCharacters 
     */
    public void setStation(String fromSixBitCharacters);
    /**
     * Signal In Service
     * @param marineTrafficSignals 
     */
    public void setSignal(MarineTrafficSignals marineTrafficSignals);
    /**
     * Expected Next Signal
     * @param marineTrafficSignals 
     */
    public void setNextSignal(MarineTrafficSignals marineTrafficSignals);
    /**
     * Variant
     * @param variant 
     */
    public void setVariant(int variant);
    /**
     * Location
     * @param fromSixBitCharacters 
     */
    public void setLocation(String fromSixBitCharacters);
    /**
     * Present Weather
     * @param wmoCode45501 
     */
    public void setWeather(WMOCode45501 wmoCode45501);
    /**
     * Visibility Limit
     * @param reached when on, indicates that the maximum range of the 
     * visibility equipment was reached and the visibility reading shall be 
     * regarded as > x.x NM.
     */
    public void setVisibilityLimit(boolean reached);
    /**
     * Pressure at sea level
     * @param pressure 90-1100 hPa
     */
    public void setAirPressure(float pressure);
    /**
     * Pressure Change
     * @param delta -50-+50hPa: units of 0.1hPa averaged over last 3 hours.
     */
    public void setAirPressureChange(float delta);

}
