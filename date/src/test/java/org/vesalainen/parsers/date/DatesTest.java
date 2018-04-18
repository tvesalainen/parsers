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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public class DatesTest
{
    
    public DatesTest()
    {
        //ZoneId.getAvailableZoneIds().stream().forEach((z)->System.err.println(z));
    }

    @Test
    public void testRFC1123()
    {
        ZonedDateTime d = Dates.parseRFC1123("Mon, 22 Mar 2010 00:37:31 +0100");
        assertEquals(DayOfWeek.MONDAY, d.getDayOfWeek());
        assertEquals(22, d.getDayOfMonth());
        assertEquals(Month.MARCH, d.getMonth());
        assertEquals(2010, d.getYear());
        assertEquals(0, d.getHour());
        assertEquals(37, d.getMinute());
        assertEquals(31, d.getSecond());
        assertEquals(ZoneOffset.of("+1"), d.getOffset());
    }
    @Test
    public void testRFC850()
    {
        ZonedDateTime d = Dates.parseRFC850("Friday, 19-Nov-82 16:14:55 EST");
        assertEquals(DayOfWeek.FRIDAY, d.getDayOfWeek());
        assertEquals(19, d.getDayOfMonth());
        assertEquals(Month.NOVEMBER, d.getMonth());
        assertEquals(1982, d.getYear());
        assertEquals(16, d.getHour());
        assertEquals(14, d.getMinute());
        assertEquals(55, d.getSecond());
        assertEquals(ZoneOffset.of("-5"), d.getOffset());
    }
    @Test
    public void testAscTime()
    {
        LocalDateTime d = Dates.parseAscTime("Sat, Jul 16 02:03:55 1994");
        assertEquals(DayOfWeek.SATURDAY, d.getDayOfWeek());
        assertEquals(16, d.getDayOfMonth());
        assertEquals(Month.JULY, d.getMonth());
        assertEquals(1994, d.getYear());
        assertEquals(2, d.getHour());
        assertEquals(3, d.getMinute());
        assertEquals(55, d.getSecond());
    }
    @Test
    public void testISO8601()
    {
        ZonedDateTime d = Dates.parseISO8601Zoned("1994-07-16T02:03:55Z");
        assertEquals(DayOfWeek.SATURDAY, d.getDayOfWeek());
        assertEquals(16, d.getDayOfMonth());
        assertEquals(Month.JULY, d.getMonth());
        assertEquals(1994, d.getYear());
        assertEquals(2, d.getHour());
        assertEquals(3, d.getMinute());
        assertEquals(55, d.getSecond());
        assertEquals(ZoneOffset.UTC, d.getOffset());
    }
    @Test
    public void testISO8601LocalDateTime()
    {
        LocalDateTime d = Dates.parseISO8601LocalDateTime("1994-07-16T02:03:55");
        assertEquals(DayOfWeek.SATURDAY, d.getDayOfWeek());
        assertEquals(16, d.getDayOfMonth());
        assertEquals(Month.JULY, d.getMonth());
        assertEquals(1994, d.getYear());
        assertEquals(2, d.getHour());
        assertEquals(3, d.getMinute());
        assertEquals(55, d.getSecond());
    }
    @Test
    public void testISO8601LocalDate()
    {
        LocalDate d = Dates.parseISO8601LocalDate("1994-07-16");
        assertEquals(DayOfWeek.SATURDAY, d.getDayOfWeek());
        assertEquals(16, d.getDayOfMonth());
        assertEquals(Month.JULY, d.getMonth());
        assertEquals(1994, d.getYear());
    }
    @Test
    public void testISO8601YearMonth()
    {
        YearMonth d = Dates.parseISO8601YearMonth("1994-07");
        assertEquals(Month.JULY, d.getMonth());
        assertEquals(1994, d.getYear());
    }
    @Test
    public void testISO8601Year()
    {
        Year d = Dates.parseISO8601Year("1994");
        assertEquals(1994, d.getValue());
    }
    @Test
    public void testISO8601LocalTime()
    {
        LocalTime d = Dates.parseISO8601LocalTime("02:03:55");
        assertEquals(2, d.getHour());
        assertEquals(3, d.getMinute());
        assertEquals(55, d.getSecond());
    }
    @Test
    public void testRFC1123All()
    {
        ZonedDateTime d = Dates.parse("Mon, 22 Mar 2010 00:37:31 +0100");
        assertEquals(DayOfWeek.MONDAY, d.getDayOfWeek());
        assertEquals(22, d.getDayOfMonth());
        assertEquals(Month.MARCH, d.getMonth());
        assertEquals(2010, d.getYear());
        assertEquals(0, d.getHour());
        assertEquals(37, d.getMinute());
        assertEquals(31, d.getSecond());
        assertEquals(ZoneOffset.of("+1"), d.getOffset());
    }
    @Test
    public void testRFC850All()
    {
        ZonedDateTime d = Dates.parse("Friday, 19-Nov-82 16:14:55 EST");
        assertEquals(DayOfWeek.FRIDAY, d.getDayOfWeek());
        assertEquals(19, d.getDayOfMonth());
        assertEquals(Month.NOVEMBER, d.getMonth());
        assertEquals(1982, d.getYear());
        assertEquals(16, d.getHour());
        assertEquals(14, d.getMinute());
        assertEquals(55, d.getSecond());
        assertEquals(ZoneOffset.of("-5"), d.getOffset());
    }
    @Test
    public void testISO8601All()
    {
        ZonedDateTime d = Dates.parse("1994-07-16T02:03:55Z");
        assertEquals(DayOfWeek.SATURDAY, d.getDayOfWeek());
        assertEquals(16, d.getDayOfMonth());
        assertEquals(Month.JULY, d.getMonth());
        assertEquals(1994, d.getYear());
        assertEquals(2, d.getHour());
        assertEquals(3, d.getMinute());
        assertEquals(55, d.getSecond());
        assertEquals(ZoneOffset.UTC, d.getOffset());
    }
    @Test
    public void testISO8601All_2()
    {
        ZonedDateTime d = Dates.parse("1994-07-16T02:03:55GMT-04:00");
        assertEquals(DayOfWeek.SATURDAY, d.getDayOfWeek());
        assertEquals(16, d.getDayOfMonth());
        assertEquals(Month.JULY, d.getMonth());
        assertEquals(1994, d.getYear());
        assertEquals(2, d.getHour());
        assertEquals(3, d.getMinute());
        assertEquals(55, d.getSecond());
        assertEquals(ZoneOffset.ofHours(-4), d.getOffset());
    }
    @Test
    public void testZonedDateTime()
    {
        ZonedDateTime zdt = ZonedDateTime.of(1994, 7, 16, 2, 3, 55, 0, ZoneId.of("Europe/Helsinki"));
        String exp = zdt.toString();
        ZonedDateTime d = Dates.parse(exp);
        assertEquals(DayOfWeek.SATURDAY, d.getDayOfWeek());
        assertEquals(16, d.getDayOfMonth());
        assertEquals(Month.JULY, d.getMonth());
        assertEquals(1994, d.getYear());
        assertEquals(2, d.getHour());
        assertEquals(3, d.getMinute());
        assertEquals(55, d.getSecond());
        assertEquals(ZoneOffset.ofHours(3), d.getOffset());
    }
    @Test
    public void testRMSExpress()
    {
        ZonedDateTime d = Dates.parseRMSExpress("2018-04-18 18:34:28Z");
    }    
}
