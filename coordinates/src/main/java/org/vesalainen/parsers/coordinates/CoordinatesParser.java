/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vesalainen.parsers.coordinates;

import org.vesalainen.parser.GenClassFactory;
import org.vesalainen.parser.ParserConstants;
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
 * @author tkv
 * @param <T> Coordinate type
 */
@GenClassname("org.vesalainen.parsers.coordinates.CoordinatesParserImpl")
@GrammarDef()
public abstract class CoordinatesParser<T>
{
    private CoordinateSupplier<T> supplier;

    public CoordinatesParser(CoordinateSupplier<T> supplier)
    {
        this.supplier = supplier;
    }
    
    public static <T> CoordinatesParser<T> getInstance(CoordinateSupplier<T> supplier)
    {
        return (CoordinatesParser) GenClassFactory.getGenInstance(CoordinatesParser.class, supplier);
    }
    
    @ParseMethod(start="coordinate", whiteSpace ="whiteSpace")
    public abstract T parseCoordinate(String text);

    @Rule("decimal decimal")
    protected T coordinate(double lat, double lon)
    {
        return supplier.supply(null, lat, lon);
    }

    @Rule("string? ns latitude we longitude")
    protected T coordinate(String name, int ns, double lat, int we, double lon)
    {
        return supplier.supply(name, ns*lat, we*lon);
    }
    
    @Rule("string? latitude ns longitude we")
    protected T coordinate(String name, double lat, int ns, double lon, int we)
    {
        return supplier.supply(name, ns*lat, we*lon);
    }
    
    @Rule("integer degreeChar? decimal secondChar?")
    protected double latitude(int deg, double min,
            @ParserContext(ParserConstants.INPUTREADER) InputReader reader)
    {
        double d = deg + min/60.0;
        if (d < 0 || d > 90 || min < 0 || min > 60)
        {
            reader.throwSyntaxErrorException("latitude coordinate", String.valueOf(d));
        }
        return new Double(d);
    }
    
    @Rule("integer degreeChar? integer secondChar? integer minuteChar?")
    protected double latitude(int deg, int min, int sec,
            @ParserContext(ParserConstants.INPUTREADER) InputReader reader)
    {
        double d = deg + min/60.0 + sec/3600.0;
        if (d < 0 || d > 90 || min < 0 || min > 60 || sec < 0 || sec > 60)
        {
            reader.throwSyntaxErrorException("latitude coordinate", String.valueOf(d));
        }
        return d;
    }
    
    @Rule("integer degreeChar? decimal secondChar?")
    protected double longitude(int deg, double min,
            @ParserContext(ParserConstants.INPUTREADER) InputReader reader)
    {
        double d = deg + min/60.0;
        if (d < 0 || d > 180 || min < 0 || min > 60)
        {
            reader.throwSyntaxErrorException("longitude coordinate", String.valueOf(d));
        }
        return d;
    }
    
    @Rule("integer degreeChar? integer secondChar? integer minuteChar?")
    protected double longitude(int deg, int min, int sec,
            @ParserContext(ParserConstants.INPUTREADER) InputReader reader)
    {
        double d = deg + min/60.0 + sec/3600.0;
        if (d < 0 || d > 180 || min < 0 || min > 60 || sec < 0 || sec > 60)
        {
            reader.throwSyntaxErrorException("longitude coordinate", String.valueOf(d));
        }
        return d;
    }

    @Terminal(expression="[\u00b0oO]")
    protected abstract void degreeChar();
    
    @Terminal(expression="\"")
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
    
    @Terminal(expression = "[A-Za-z][A-Za-z0-9]+")
    protected abstract String string(String value);

    @Terminal(expression = "[\\+\\-]?[0-9]+")
    protected abstract int integer(int value);

    @Terminal(expression = "[\\+\\-]?[0-9]+\\.[0-9]+")
    protected abstract double decimal(double value);

    @Terminal(expression = "[ \t\r\n\\,\\-–]+")
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
