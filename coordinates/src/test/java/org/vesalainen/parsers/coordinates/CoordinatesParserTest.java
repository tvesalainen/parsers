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
package org.vesalainen.parsers.coordinates;

import java.util.Locale;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public class CoordinatesParserTest
{
    
    public CoordinatesParserTest()
    {
    }

    @Test
    public void testParseCoordinate()
    {
        CoordinatesParser<String> parser = CoordinatesParser.getInstance((lat, lon)->String.format(Locale.US, "%f %f", lat, lon));
        assertNotNull(parser);
        assertEquals("60.500000 25.500000", parser.parseCoordinate("N 60 30.0, E 25 30.0"));
        assertEquals("60.500000 25.500000", parser.parseCoordinate("60 30.0 N, 25 30.0 E"));
        assertEquals("60.500000 25.500000", parser.parseCoordinate("60 30.0 N, 25 30.0 E"));
        assertEquals("12.074567 -68.857867", parser.parseCoordinate("12o 04.474 N 68o 51.472 W"));
        assertEquals("3.866667 30.233333", parser.parseCoordinate("03 52 N   30 14 E"));
        assertEquals("-10.000000 -120.000000", parser.parseCoordinate("10S120W"));
        assertEquals("55.100000 179.400000", parser.parseCoordinate("55.1N179.4E"));
        assertEquals("46.379500 -121.583500", parser.parseCoordinate("46-22.77N 121-35.01W"));
        assertEquals("55.100000 179.400000", parser.parseCoordinate("+55.1, +179.4"));
    }

}
