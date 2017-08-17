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
 * Parser that parses numbers with units. Units are years, months, hours, minutes, seconds, 
 * pico, nano, micro, milli, kilo, mega and tera. Parser is case-insensitive.
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
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
        @Rule("years"),
        @Rule("months"),
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
    @Terminal(expression="years?", options={Regex.Option.CASE_INSENSITIVE})
    protected double years()
    {
        return TimeUnit.DAYS.toMillis(1)*365;
    }
    @Terminal(expression="months?", options={Regex.Option.CASE_INSENSITIVE})
    protected double months()
    {
        return TimeUnit.DAYS.toMillis(1)*30;
    }
    @Terminal(expression="days?", options={Regex.Option.CASE_INSENSITIVE})
    protected double days()
    {
        return TimeUnit.DAYS.toMillis(1);
    }
    @Terminal(expression="hours?", options={Regex.Option.CASE_INSENSITIVE})
    protected double hours()
    {
        return TimeUnit.HOURS.toMillis(1);
    }
    @Terminal(expression="minutes?", options={Regex.Option.CASE_INSENSITIVE})
    protected double minutes()
    {
        return TimeUnit.MINUTES.toMillis(1);
    }
    @Terminal(expression="seconds?", options={Regex.Option.CASE_INSENSITIVE})
    protected double seconds()
    {
        return TimeUnit.SECONDS.toMillis(1);
    }
    @Terminal(expression="picos?", options={Regex.Option.CASE_INSENSITIVE})
    protected double pico()
    {
        return 1e-12;
    }
    @Terminal(expression="nanos?", options={Regex.Option.CASE_INSENSITIVE})
    protected double nano()
    {
        return 1e-9;
    }
    @Terminal(expression="micros?", options={Regex.Option.CASE_INSENSITIVE})
    protected double micro()
    {
        return 1e-6;
    }
    @Terminal(expression="millis?", options={Regex.Option.CASE_INSENSITIVE})
    protected double milli()
    {
        return 1e-3;
    }
    @Terminal(expression="kilos?", options={Regex.Option.CASE_INSENSITIVE})
    protected double kilo()
    {
        return 1e3;
    }
    @Terminal(expression="megas?", options={Regex.Option.CASE_INSENSITIVE})
    protected double mega()
    {
        return 1e6;
    }
    @Terminal(expression="gigas?", options={Regex.Option.CASE_INSENSITIVE})
    protected double giga()
    {
        return 1e9;
    }
    @Terminal(expression="teras?", options={Regex.Option.CASE_INSENSITIVE})
    protected double tera()
    {
        return 1e12;
    }
    public long parseDays(CharSequence text)
    {
        return parseHours(text)/24;
    }
    public long parseHours(CharSequence text)
    {
        return parseMinutes(text)/60;
    }
    public long parseMinutes(CharSequence text)
    {
        return parseSeconds(text)/60;
    }
    /**
     * Parses string 'number prefix' and returns number multiplied according to
     * prefix.
     * <p>Divides parseMillis with 1000.
     * @param text
     * @return 
     */
    public long parseSeconds(CharSequence text)
    {
        return parseMillis(text)/1000;
    }
    /**
     * Parses string 'number prefix' and returns number multiplied according to
     * prefix.
     * <p>This actually casts double returned from parse method to long.
     * @param text
     * @return 
     */
    public long parseMillis(CharSequence text)
    {
        return (long) parse(text);
    }
    /**
     * Parses string 'number prefix' and returns number multiplied according to
     * prefix.
     * @param text
     * @return 
     */
    @ParseMethod(start="value")
    public abstract double parse(CharSequence text);
    
    public static UnitParser getInstance()
    {
        return (UnitParser) GenClassFactory.getGenInstance(UnitParser.class);
    }

}
