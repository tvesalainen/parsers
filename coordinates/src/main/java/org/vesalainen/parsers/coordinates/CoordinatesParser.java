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

import org.vesalainen.lang.Primitives;
import org.vesalainen.parser.GenClassFactory;
import org.vesalainen.parser.ParserConstants;
import org.vesalainen.parser.ParserInfo;
import org.vesalainen.parser.annotation.GenClassname;
import org.vesalainen.parser.annotation.GrammarDef;
import org.vesalainen.parser.annotation.ParseMethod;
import org.vesalainen.parser.annotation.ParserContext;
import org.vesalainen.parser.annotation.ReservedWords;
import org.vesalainen.parser.annotation.Rule;
import org.vesalainen.parser.annotation.Rules;
import org.vesalainen.parser.annotation.Terminal;
import org.vesalainen.parser.util.InputReader;
import org.vesalainen.regex.Regex;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 * @param <T> Coordinate type
 */
@GenClassname("org.vesalainen.parsers.coordinates.CoordinatesParserImpl")
@GrammarDef()
public abstract class CoordinatesParser<T> implements ParserInfo
{
    private CoordinateSupplier<T> supplier;

    protected CoordinatesParser(CoordinateSupplier<T> supplier)
    {
        this.supplier = supplier;
    }
    
    public static <T> CoordinatesParser<T> getInstance(CoordinateSupplier<T> supplier)
    {
        return (CoordinatesParser) GenClassFactory.getGenInstance(CoordinatesParser.class, supplier);
    }
    
    @ParseMethod(start="coordinate", whiteSpace ="whiteSpace")
    public abstract T parseCoordinate(String text);
    @Rule("sign? number '\\,' sign? number")
    protected T coordinate(Integer sign1, double lat, Integer sign2, double lon)
    {
        return supplier.supply(lat, lon);
    }

    @Rule("ns latitude '\\,'? we longitude")
    protected T coordinate(int ns, double lat, int we, double lon)
    {
        return supplier.supply(ns*lat, we*lon);
    }
    
    @Rule("latitude ns '\\,'? longitude we")
    protected T coordinate(double lat, int ns, double lon, int we)
    {
        return supplier.supply(ns*lat, we*lon);
    }
    
    @Rule("number degreeChar?")
    protected double latitude(double deg,
            @ParserContext(ParserConstants.INPUTREADER) InputReader reader)
    {
        double d = deg;
        if (d < 0 || d > 90)
        {
            reader.throwSyntaxErrorException("latitude coordinate", String.valueOf(d));
        }
        return new Double(d);
    }
    @Rule("number degreeChar? number secondChar?")
    protected double latitude(double deg, double min,
            @ParserContext(ParserConstants.INPUTREADER) InputReader reader)
    {
        double d = deg + min/60.0;
        if (d < 0 || d > 90 || min < 0 || min > 60)
        {
            reader.throwSyntaxErrorException("latitude coordinate", String.valueOf(d));
        }
        return new Double(d);
    }
    
    @Rule("number degreeChar? number secondChar? number minuteChar?")
    protected double latitude(double deg, double min, double sec,
            @ParserContext(ParserConstants.INPUTREADER) InputReader reader)
    {
        double d = deg + min/60.0 + sec/3600.0;
        if (d < 0 || d > 90 || min < 0 || min > 60 || sec < 0 || sec > 60)
        {
            reader.throwSyntaxErrorException("latitude coordinate", String.valueOf(d));
        }
        return d;
    }
    
    @Rule("number degreeChar?")
    protected double longitude(double deg,
            @ParserContext(ParserConstants.INPUTREADER) InputReader reader)
    {
        double d = deg;
        if (d < 0 || d > 180)
        {
            reader.throwSyntaxErrorException("longitude coordinate", String.valueOf(d));
        }
        return d;
    }
    
    @Rule("number degreeChar? number secondChar?")
    protected double longitude(double deg, double min,
            @ParserContext(ParserConstants.INPUTREADER) InputReader reader)
    {
        double d = deg + min/60.0;
        if (d < 0 || d > 180 || min < 0 || min > 60)
        {
            reader.throwSyntaxErrorException("longitude coordinate", String.valueOf(d));
        }
        return d;
    }
    
    @Rule("number degreeChar? number secondChar? number minuteChar?")
    protected double longitude(double deg, double min, double sec,
            @ParserContext(ParserConstants.INPUTREADER) InputReader reader)
    {
        double d = deg + min/60.0 + sec/3600.0;
        if (d < 0 || d > 180 || min < 0 || min > 60 || sec < 0 || sec > 60)
        {
            reader.throwSyntaxErrorException("longitude coordinate", String.valueOf(d));
        }
        return d;
    }

    @Terminal(expression="[\u00b0oO\\-]")
    protected abstract void degreeChar();
    
    @Terminal(expression="(\"|'')")
    protected abstract void minuteChar();
    
    @Terminal(expression="'")
    protected abstract void secondChar();
    
    @Rules({
        @Rule("north"),
        @Rule("south")
    })
    protected abstract int ns(int sign);
    
    @Rules({
        @Rule("west"),
        @Rule("east")
    })
    protected abstract int we(int sign);
    
    @Rule("plus")
    @Rule("minus")
    protected abstract Integer sign(Integer sign);
    
    @Rule("n")
    protected int north()
    {
        return 1;
    }
    
    @Rule("e")
    protected int east()
    {
        return 1;
    }
    
    @Rule("s")
    protected int south()
    {
        return -1;
    }
    
    @Rule("w")
    protected int west()
    {
        return -1;
    }
    
    @Terminal(expression = "[\\+]", priority=1)
    protected Integer plus()
    {
        return 1;
    }
    
    @Terminal(expression = "[\\-]", priority=1)
    protected Integer minus()
    {
        return -1;
    }
    
    @Terminal(expression = "[A-Za-z][A-Za-z0-9]+")
    protected abstract String string(String value);

    @Terminal(expression = "[0-9]+([\\.\\,][0-9]+)?")
    protected double number(String value)
    {
        return Primitives.parseDouble(value.replace(',', '.'));
    }

    @Terminal(expression = "[ \t\r\n]+")
    protected abstract void whiteSpace();

    @ReservedWords(value =
    {
        "n",
        "s",
        "w",
        "e"
    },
    options =
    {
        Regex.Option.CASE_INSENSITIVE
    })
    protected void reservedWordsD()
    {
        
    }
}
