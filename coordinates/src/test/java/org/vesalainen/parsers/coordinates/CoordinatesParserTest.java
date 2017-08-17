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

import java.util.List;
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
        CoordinatesParser<String> parser = CoordinatesParser.getInstance((s, lat, lon)->String.format("%s %f %f", s, lat, lon));
        assertNotNull(parser);
        assertEquals("null 60,500000 25,500000", parser.parseCoordinate("N 60 30.0, E 25 30.0"));
        assertEquals("null 60,500000 25,500000", parser.parseCoordinate("60 30.0 N, 25 30.0 E"));
        assertEquals("Waypoint1 60,500000 25,500000", parser.parseCoordinate("Waypoint1 60 30.0 N, 25 30.0 E"));
        assertEquals("D02 12,074567 -68,857867", parser.parseCoordinate("D02 – 12o 04.474 N 68o 51.472 W"));
    }
    
    @Test
    public void testParseCoordinates()
    {
        CoordinatesParser<String> parser = CoordinatesParser.getInstance((s, lat, lon)->String.format("%s %f %f", s, lat, lon));
        assertNotNull(parser);
        List<String> res = parser.parseCoordinates("N 60 30.0, E 25 30.0 Waypoint1 60 30.0 N, 25 30.0 E D02 – 12o 04.474 N 68o 51.472 W");
        assertEquals(3, res.size());
        assertEquals("null 60,500000 25,500000", res.get(0));
        assertEquals("Waypoint1 60,500000 25,500000", res.get(1));
        assertEquals("D02 12,074567 -68,857867", res.get(2));
    }
    
}
