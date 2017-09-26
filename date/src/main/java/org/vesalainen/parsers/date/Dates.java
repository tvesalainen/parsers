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

import java.time.ZonedDateTime;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public final class Dates
{
    private static final InternetDateParser parser = InternetDateParser.newInstance();
    /**
     * Parses date in either ISO8601, RFC1123, RFC850 or asctime format.
     * @param text
     * @return 
     */
    public static final ZonedDateTime parse(String text)
    {
        return parser.parseZonedDateTime(text);
    }
    /**
     * Parses date in ISO8601 format.
     * <PRE>
     * yyyy-MM-dd'T'HH:mm:ss.SSSz
     * yyyy-MM-dd'T'HH:mm:ssz
     * yyyy-MM-dd'T'HH:mmz
     * yyyy-MM-dd'T'HH:mm:ss.SSSZ
     * yyyy-MM-dd'T'HH:mm:ssZ
     * yyyy-MM-dd'T'HH:mmZ
     * yyyy-MM-dd
     * yyyy-MM
     * yyyy
     * </PRE>
     * @param text
     * @return 
     */
    public static final ZonedDateTime parseISO8601(String text)
    {
        return parser.parseISO8601ZonedDateTime(text);
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
    public static final ZonedDateTime parseAscTime(String text)
    {
        return parser.parseAscTimeZonedDateTime(text);
    }
}
