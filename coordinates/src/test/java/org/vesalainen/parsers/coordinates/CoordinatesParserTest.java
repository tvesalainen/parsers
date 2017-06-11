/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vesalainen.parsers.coordinates;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tkv
 */
public class CoordinatesParserTest
{
    
    public CoordinatesParserTest()
    {
    }

    @Test
    public void testParse()
    {
        CoordinatesParser<String> parser = CoordinatesParser.getInstance((s, lat, lon)->String.format("%s %f %f", s, lat, lon));
        assertNotNull(parser);
        assertEquals("null 60,500000 25,500000", parser.parseCoordinate("N 60 30.0, E 25 30.0"));
        assertEquals("null 60,500000 25,500000", parser.parseCoordinate("60 30.0 N, 25 30.0 E"));
        assertEquals("Waypoint1 60,500000 25,500000", parser.parseCoordinate("Waypoint1 60 30.0 N, 25 30.0 E"));
        assertEquals("D02 12,074567 -68,857867", parser.parseCoordinate("D02 – 12o 04.474 N 68o 51.472 W"));
    }
    
}
