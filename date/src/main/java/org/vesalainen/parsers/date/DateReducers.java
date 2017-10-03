/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vesalainen.parsers.date;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.util.Map;
import org.vesalainen.parser.annotation.ParserContext;
import org.vesalainen.parser.annotation.Rule;
import org.vesalainen.parser.annotation.Terminal;
import org.vesalainen.time.MutableDateTime;

/**
 * DateReducers is an abstract class containing reducer methods for date parsing.
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public abstract class DateReducers
{
    private static Map<String,String> SHORT_IDS = ZoneId.SHORT_IDS;
    /**
     * Sets zone id map to another from default or to default if null.
     * <p>
     * Note! This is static and therefore can have side effects.
     * @param shortIds 
     * @see java.time.ZoneId#SHORT_IDS
     */
    public static final void setShortIds(Map<String,String> shortIds)
    {
        if (shortIds != null)
        {
            SHORT_IDS = shortIds;
        }
        else
        {
            SHORT_IDS = ZoneId.SHORT_IDS;
        }
    }
    protected void ad(@ParserContext MutableDateTime cal)
    {
        cal.set(ChronoField.ERA, 0);
    }

    protected void bc(@ParserContext MutableDateTime cal)
    {
        cal.set(ChronoField.ERA, 1);
    }

    @Rule("digit12")
    protected void year2(int y, @ParserContext MutableDateTime cal)
    {
        cal.setYear(y);
    }

    @Terminal(expression = "[0-9]{4}")
    protected void year4(int y, @ParserContext MutableDateTime cal)
    {
        cal.setYear(y);
    }

    @Rule("digit12")
    protected void month(int m, @ParserContext MutableDateTime cal)
    {
        cal.setMonth(m);
    }

    protected void month1(@ParserContext MutableDateTime cal)
    {
        cal.setMonth(1);
    }

    protected void month2(@ParserContext MutableDateTime cal)
    {
        cal.setMonth(2);
    }

    protected void month3(@ParserContext MutableDateTime cal)
    {
        cal.setMonth(3);
    }

    protected void month4(@ParserContext MutableDateTime cal)
    {
        cal.setMonth(4);
    }

    protected void month5(@ParserContext MutableDateTime cal)
    {
        cal.setMonth(5);
    }

    protected void month6(@ParserContext MutableDateTime cal)
    {
        cal.setMonth(6);
    }

    protected void month7(@ParserContext MutableDateTime cal)
    {
        cal.setMonth(7);
    }

    protected void month8(@ParserContext MutableDateTime cal)
    {
        cal.setMonth(8);
    }

    protected void month9(@ParserContext MutableDateTime cal)
    {
        cal.setMonth(9);
    }

    protected void month10(@ParserContext MutableDateTime cal)
    {
        cal.setMonth(10);
    }

    protected void month11(@ParserContext MutableDateTime cal)
    {
        cal.setMonth(11);
    }

    protected void month12(@ParserContext MutableDateTime cal)
    {
        cal.setMonth(12);
    }

    @Rule("digit13")
    protected void weekInYear(int w, @ParserContext MutableDateTime cal)
    {
        cal.set(ChronoField.ALIGNED_WEEK_OF_YEAR, w);
    }

    @Rule("digit1")
    protected void weekInMonth(int w, @ParserContext MutableDateTime cal)
    {
        cal.set(ChronoField.ALIGNED_WEEK_OF_MONTH, w);
    }

    @Rule("digit13")
    protected void dayInYear(int d, @ParserContext MutableDateTime cal)
    {
        cal.set(ChronoField.DAY_OF_YEAR, d);
    }

    @Rule("digit12")
    protected void dayInMonth(int d, @ParserContext MutableDateTime cal)
    {
        cal.set(ChronoField.DAY_OF_MONTH, d);
    }

    @Rule("digit1")
    protected void dayInWeekInMonth(int d, @ParserContext MutableDateTime cal)
    {
        cal.set(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH, d);
    }

    protected void weekday1(@ParserContext MutableDateTime cal)
    {
        cal.set(ChronoField.DAY_OF_WEEK, 1);
    }

    protected void weekday2(@ParserContext MutableDateTime cal)
    {
        cal.set(ChronoField.DAY_OF_WEEK, 2);
    }

    protected void weekday3(@ParserContext MutableDateTime cal)
    {
        cal.set(ChronoField.DAY_OF_WEEK, 3);
    }

    protected void weekday4(@ParserContext MutableDateTime cal)
    {
        cal.set(ChronoField.DAY_OF_WEEK, 4);
    }

    protected void weekday5(@ParserContext MutableDateTime cal)
    {
        cal.set(ChronoField.DAY_OF_WEEK, 5);
    }

    protected void weekday6(@ParserContext MutableDateTime cal)
    {
        cal.set(ChronoField.DAY_OF_WEEK, 6);
    }

    protected void weekday7(@ParserContext MutableDateTime cal)
    {
        cal.set(ChronoField.DAY_OF_WEEK, 7);
    }

    protected void am(@ParserContext MutableDateTime cal)
    {
        cal.set(ChronoField.AMPM_OF_DAY, 0);
    }

    protected void pm(@ParserContext MutableDateTime cal)
    {
        cal.set(ChronoField.AMPM_OF_DAY, 1);
    }

    @Rule("digit12")
    protected void hour23(int h, @ParserContext MutableDateTime cal)
    {
        cal.set(ChronoField.HOUR_OF_DAY, h);
    }

    @Rule("digit12")
    protected void hour24(int h, @ParserContext MutableDateTime cal)
    {
        cal.set(ChronoField.HOUR_OF_DAY, h - 1);
    }

    @Rule("digit12")
    protected void hour11(int h, @ParserContext MutableDateTime cal)
    {
        cal.set(ChronoField.CLOCK_HOUR_OF_AMPM, h + 1);
    }

    @Rule("digit12")
    protected void hour12(int h, @ParserContext MutableDateTime cal)
    {
        cal.set(ChronoField.CLOCK_HOUR_OF_AMPM, h);
    }

    @Rule("digit12")
    protected void minute(int m, @ParserContext MutableDateTime cal)
    {
        cal.setMinute(m);
    }

    @Rule("digit12")
    protected void second(int s, @ParserContext MutableDateTime cal)
    {
        cal.setSecond(s);
    }

    @Rule("digit3")
    protected void milliSecond(int m, @ParserContext MutableDateTime cal)
    {
        cal.setMilliSecond(m);
    }
    @Rule("military")
    @Rule("zoneOffset")
    @Rule("zoneId")
    @Rule("zoneOffset '\\[' zoneId '\\]'")
    protected void z()
    {
        
    }
    @Terminal(expression = "[A-Z]")
    protected void military(String zone, @ParserContext MutableDateTime cal)
    {
        ZoneId zoneId = ZoneId.of(zone, SHORT_IDS);
        cal.setZone(zoneId);
    }
    @Terminal(expression = "[\\+\\-][:0-9]+")
    protected void zoneOffset(String offset, @ParserContext MutableDateTime cal)
    {
        cal.setZone(ZoneOffset.of(offset));
    }
    @Terminal(expression = "[A-Za-z][A-Za-z0-9~/\\._\\+\\-:]+")
    protected void zoneId(String zone, @ParserContext MutableDateTime cal)
    {
        ZoneId zoneId = ZoneId.of(zone, SHORT_IDS);
        cal.setZone(zoneId);
    }

    @Terminal(expression = "[ ]+")
    protected void whiteSpace()
    {
    }

    @Terminal(expression = "[a-zA-Z]")
    protected char character(char d)
    {
        return d;
    }

    @Terminal(expression = "[\\+\\-]")
    protected char sign(char d)
    {
        return d;
    }

    @Terminal(expression = "[0-9]{1,3}")
    protected int digit13(int d)
    {
        return d;
    }

    @Terminal(expression = "[0-9]{3}")
    protected int digit3(int d)
    {
        return d;
    }

    @Terminal(expression = "[0-9]{2}")
    protected int digit2(int d)
    {
        return d;
    }

    @Terminal(expression = "[0-9]{1,2}")
    protected int digit12(int d)
    {
        return d;
    }

    protected String string(String s)
    {
        return s;
    }

}
