/*
 * Copyright (C) 2018 Timo Vesalainen <timo.vesalainen@iki.fi>
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
package org.vesalainen.parsers.printf;

import java.util.List;
import static java.util.Locale.US;
import org.junit.Test;
import static org.junit.Assert.*;
import org.vesalainen.parsers.printf.FormatFactory.FormatPart;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public class PrintfParserTest
{
    
    public PrintfParserTest()
    {
        String format = String.format("|%3.2f|", 0.1234578);
        System.err.println(format);
    }

    @Test
    public void testString()
    {
        PrintfParser parser = PrintfParser.getInstance();
        FormatFactory factory = new FormatFactory();
        String format = "foo%-4.4sbar";
        List<FormatPart> list = parser.parse(format, factory);
        assertEquals(3, list.size());
        Object[] res = factory.parse(list, String.format(format, "abcd"));
        assertEquals(1, res.length);
        assertEquals("abcd", res[0]);
    }
    @Test
    public void testLong()
    {
        PrintfParser parser = PrintfParser.getInstance();
        FormatFactory factory = new FormatFactory();
        String format = "foo%4dbar";
        List<FormatPart> list = parser.parse(format, factory);
        assertEquals(3, list.size());
        Object[] res = factory.parse(list, String.format(format, 1234578));
        assertEquals(1, res.length);
        assertEquals(1234578L, res[0]);
    }
    @Test
    public void testFloat()
    {
        PrintfParser parser = PrintfParser.getInstance();
        FormatFactory factory = new FormatFactory();
        String format = "foo%fbar";
        List<FormatPart> list = parser.parse(format, factory);
        assertEquals(3, list.size());
        Object[] res = factory.parse(list, String.format(US, format, 1234.578));
        assertEquals(1, res.length);
        assertEquals(1234.578, res[0]);
    }
    @Test
    public void testMonth()
    {
        PrintfParser parser = PrintfParser.getInstance();
        FormatFactory factory = new FormatFactory();
        List<FormatPart> list = parser.parse("  %3.3s    %d          SSN =%f                Minimum Angle=%f degrees", factory);
        Object[] res = factory.parse(list, "  Feb    2018          SSN =  22.                Minimum Angle= 5.000 degrees");
    }
}
