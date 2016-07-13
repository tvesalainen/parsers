/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vesalainen.parsers.unit.parser;

import java.util.concurrent.TimeUnit;
import org.vesalainen.parser.GenClassFactory;
import org.vesalainen.parser.annotation.GenClassname;
import org.vesalainen.parser.annotation.GrammarDef;
import org.vesalainen.parser.annotation.ParseMethod;
import org.vesalainen.parser.annotation.Rule;
import org.vesalainen.parser.annotation.Rules;
import org.vesalainen.parser.annotation.Terminal;
import org.vesalainen.regex.Regex;

/**
 * Parser that parses numbers with units. Units are hours, minutes, seconds, 
 * pico, nano, micro, milli, kilo, mega and tera. Parser is case-insensitive.
 * @author tkv
 */
@GenClassname("org.vesalainen.parsers.unit.parser.UnitParserImpl")
@GrammarDef()
public abstract class UnitParser
{
    @Rule("number")
    protected double value(double number)
    {
        return number;
    }
    
    @Rule("number sp? unit")
    protected double value(double number, double multiplier)
    {
        return number*multiplier;
    }
    
    @Terminal(expression="[\\+\\-]?[0-9]+(\\.[0-9]+)?([eE][\\+\\-]?[0-9]+)?")
    protected abstract double number(double value);

    @Terminal(expression="[ ]+")
    protected abstract void sp();

    @Rules({
        @Rule("days"),
        @Rule("hours"),
        @Rule("minutes"),
        @Rule("seconds"),
        @Rule("pico"),
        @Rule("nano"),
        @Rule("micro"),
        @Rule("milli"),
        @Rule("kilo"),
        @Rule("mega"),
        @Rule("kilo"),
        @Rule("giga"),
        @Rule("tera")
    })
    protected double unit(double multiplier)
    {
        return multiplier;
    }
    @Terminal(expression="days", options={Regex.Option.CASE_INSENSITIVE})
    protected double days()
    {
        return TimeUnit.DAYS.toMillis(1);
    }
    @Terminal(expression="hours", options={Regex.Option.CASE_INSENSITIVE})
    protected double hours()
    {
        return TimeUnit.HOURS.toMillis(1);
    }
    @Terminal(expression="minutes", options={Regex.Option.CASE_INSENSITIVE})
    protected double minutes()
    {
        return TimeUnit.MINUTES.toMillis(1);
    }
    @Terminal(expression="seconds", options={Regex.Option.CASE_INSENSITIVE})
    protected double seconds()
    {
        return TimeUnit.SECONDS.toMillis(1);
    }
    @Terminal(expression="pico", options={Regex.Option.CASE_INSENSITIVE})
    protected double pico()
    {
        return 1e-12;
    }
    @Terminal(expression="nano", options={Regex.Option.CASE_INSENSITIVE})
    protected double nano()
    {
        return 1e-9;
    }
    @Terminal(expression="micro", options={Regex.Option.CASE_INSENSITIVE})
    protected double micro()
    {
        return 1e-6;
    }
    @Terminal(expression="milli", options={Regex.Option.CASE_INSENSITIVE})
    protected double milli()
    {
        return 1e-3;
    }
    @Terminal(expression="kilo", options={Regex.Option.CASE_INSENSITIVE})
    protected double kilo()
    {
        return 1e3;
    }
    @Terminal(expression="mega", options={Regex.Option.CASE_INSENSITIVE})
    protected double mega()
    {
        return 1e6;
    }
    @Terminal(expression="giga", options={Regex.Option.CASE_INSENSITIVE})
    protected double giga()
    {
        return 1e9;
    }
    @Terminal(expression="tera", options={Regex.Option.CASE_INSENSITIVE})
    protected double tera()
    {
        return 1e12;
    }
    @ParseMethod(start="value")
    public abstract double parse(CharSequence text);
    
    public static UnitParser getInstance()
    {
        return (UnitParser) GenClassFactory.getGenInstance(UnitParser.class);
    }

}
