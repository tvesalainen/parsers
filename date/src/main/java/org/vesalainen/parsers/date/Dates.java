/*
 * Copyright (C) 2017 Timo Vesalainen <timo.vesalainen@iki.fi>
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
package org.vesalainen.parsers.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.Map;

/**
 * Dates class contains convenient static methods to InternetDateParser
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 * @see org.vesalainen.parsers.date.InternetDateParser
 */
public final class Dates
{
    private static final InternetDateParser parser = InternetDateParser.newInstance();
    /**
     * Sets zone id map to another from default or to default if null.
     * <p>
     * Note! This is static and therefore can have side effects.
     * @param shortIds 
     * @see java.time.ZoneId#SHORT_IDS
     */
    public static final void setShortIds(Map<String,String> shortIds)
    {
        InternetDateParser.setShortIds(shortIds);
    }
    /**
     * Parses date in either ISO8601, RFC1123 or RFC850.
     * @param text
     * @return 
     */
    public static final ZonedDateTime parse(String text)
    {
        return parser.parseZonedDateTime(text);
    }
    /**
     * Parses date in ISO8601 zoned format.
     * <PRE>
     * yyyy-MM-dd'T'HH:mm:ss.SSSz
     * yyyy-MM-dd'T'HH:mm:ssz
     * yyyy-MM-dd'T'HH:mmZ
     * </PRE>
     * @param text
     * @return 
     */
    public static final ZonedDateTime parseISO8601Zoned(String text)
    {
        return parser.parseISO8601ZonedDateTime(text);
    }
    /**
     * Parses date in ISO8601 format without zone.
     * <PRE>
     * yyyy-MM-dd'T'HH:mm:ss.SSS
     * yyyy-MM-dd'T'HH:mm:ss
     * yyyy-MM-dd'T'HH:mm
     * </PRE>
     * @param text
     * @return 
     */
    public static final LocalDateTime parseISO8601LocalDateTime(String text)
    {
        return parser.parseISO8601LocalDateTime(text);
    }
    /**
     * Parses date in ISO8601 format.
     * <PRE>
     * yyyy-MM-dd
     * </PRE>
     * @param text
     * @return 
     */
    public static final LocalDate parseISO8601LocalDate(String text)
    {
        return parser.parseISO8601LocalDate(text);
    }
    /**
     * Parses date in ISO8601 format.
     * <PRE>
     * yyyy-MM
     * </PRE>
     * @param text
     * @return 
     */
    public static final YearMonth parseISO8601YearMonth(String text)
    {
        return parser.parseISO8601YearMonth(text);
    }
    /**
     * Parses date in ISO8601 format.
     * <PRE>
     * yyyy
     * </PRE>
     * @param text
     * @return 
     */
    public static final Year parseISO8601Year(String text)
    {
        return parser.parseISO8601Year(text);
    }
    /**
     * Parses date in ISO8601 format.
     * <PRE>
     * HH:mm:ss.SSS
     * HH:mm:ss
     * HH:mm
     * </PRE>
     * @param text
     * @return 
     */
    public static final LocalTime parseISO8601LocalTime(String text)
    {
        return parser.parseISO8601LocalTime(text);
    }
    /**
     * Parses date in RFC1123 format EEE, dd MMM yyyy HH:mm:ss z
     * @param text
     * @return 
     */
    public static final ZonedDateTime parseRFC1123(String text)
    {
        return parser.parseRFC1123ZonedDateTime(text);
    }
    /**
     * Parses date in RFC850 format EEEE, dd-MMM-yy HH:mm:ss z
     * @param text
     * @return 
     */
    public static final ZonedDateTime parseRFC850(String text)
    {
        return parser.parseRFC850ZonedDateTime(text);
    }
    /**
     * Parses date in asc time format EEE, MMM dd HH:mm:ss yyyy
     * @param text
     * @return 
     */
    public static final LocalDateTime parseAscTime(String text)
    {
        return parser.parseAscTimeLocalDateTime(text);
    }
    public static final ZonedDateTime parseRMSExpress(String text)
    {
        return parser.parseRMSExpressZonedDateTime(text);
    }
}
