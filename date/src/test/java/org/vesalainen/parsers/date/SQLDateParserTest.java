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

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public class SQLDateParserTest
{
    private SQLDateParser parser;
    public SQLDateParserTest() throws NoSuchMethodException, IOException, NoSuchFieldException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        parser = SQLDateParser.newInstance();
    }

    @Test
    public void testOldTime() throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date exp = sdf.parse("10:11:12");
        Date time = parser.parseTime("10:11:12");   
        // not crashing fine!!!
    }
    @Test
    public void testLocalTime() throws ParseException
    {
        LocalTime exp = LocalTime.of(10, 11, 12);
        LocalTime time = parser.parseLocalTime("10:11:12");   
        assertEquals(exp, time);
    }
    @Test
    public void testOldTimestamp() throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy'-'MM'-'dd' 'HH':'mm':'ssz");
        String format = sdf.format(new Date());
        Date exp = sdf.parse(format);   // to get rid of millis
        Date date = parser.parseTimestamp(format);
        assertEquals(exp, date);
    }
    @Test
    public void testZonedTimestamp() throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy'-'MM'-'dd' 'HH':'mm':'ssz");
        ZonedDateTime exp = ZonedDateTime.of(2017, 7, 1, 10, 11, 12, 0, ZoneId.systemDefault());
        Date date = Date.from(Instant.from(exp));
        String format = sdf.format(date);
        ZonedDateTime zdt = parser.parseZonedTimestamp(format);
        assertEquals(exp, zdt);
    }
    
}
