/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vesalainen.parsers.unit.parser;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tkv
 */
public class UnitParserTest
{
    private static final double Epsilon = 1e-12;
    public UnitParserTest()
    {
    }

    @Test
    public void test1()
    {
        UnitParser up = UnitParser.getInstance();
        assertEquals(1.23e3, up.parse("1.23 kilo"), Epsilon);
        assertEquals(1.23e6, up.parse("1.23 megas"), Epsilon);
        assertEquals(1.23e9, up.parse("1.23 giga"), Epsilon);
        assertEquals(1.23e12, up.parse("1.23 Teras"), Epsilon);
        assertEquals(1.23e-3, up.parse("1.23milli"), Epsilon);
        assertEquals(1.23e-6, up.parse("1.23 micro"), Epsilon);
        assertEquals(1.23e-9, up.parse("1.23nano"), Epsilon);
        assertEquals(1.23e-12, up.parse("1.23 pico"), Epsilon);
        assertEquals(1.23e-12, up.parse("1.23e-12"), Epsilon);
    }
    
    @Test
    public void test2()
    {
        UnitParser up = UnitParser.getInstance();
        assertEquals(1000, up.parse("1 seconds"), Epsilon);
        assertEquals(60000, up.parse("1 minutes"), Epsilon);
        assertEquals(3600000, up.parse("1 hours"), Epsilon);
        assertEquals(30*24*60*60, up.parseSeconds("1 months"), Epsilon);
        assertEquals(365*24*60*60, up.parseSeconds("1 years"), Epsilon);
    }
}
