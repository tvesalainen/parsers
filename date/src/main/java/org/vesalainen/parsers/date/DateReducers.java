/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vesalainen.parsers.date;

import org.vesalainen.parser.annotation.ParserContext;
import java.util.Calendar;
import org.vesalainen.parser.annotation.GrammarDef;
import org.vesalainen.parser.annotation.Rule;
import org.vesalainen.parser.annotation.Rules;
import org.vesalainen.parser.annotation.Terminal;

/**
 * DateReducers is an abstract class containing reducer methods for date parsing.
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public abstract class DateReducers
{

    protected void ad(@ParserContext Calendar cal)
    {
        cal.set(Calendar.ERA, 0);
    }

    protected void bc(@ParserContext Calendar cal)
    {
        cal.set(Calendar.ERA, 1);
    }

    @Rule("digit12")
    protected void year2(int y, @ParserContext Calendar cal)
    {
        if (y > 70)
        {
            cal.set(Calendar.YEAR, 1900 + y);
        }
        else
        {
            cal.set(Calendar.YEAR, 2000 + y);
        }
    }

    @Terminal(expression = "[0-9]{4}")
    protected void year4(int y, @ParserContext Calendar cal)
    {
        cal.set(Calendar.YEAR, y);
    }

    @Rule("digit12")
    protected void month(int m, @ParserContext Calendar cal)
    {
        cal.set(Calendar.MONTH, m - 1);
    }

    protected void month1(@ParserContext Calendar cal)
    {
        cal.set(Calendar.MONTH, Calendar.JANUARY);
    }

    protected void month2(@ParserContext Calendar cal)
    {
        cal.set(Calendar.MONTH, Calendar.FEBRUARY);
    }

    protected void month3(@ParserContext Calendar cal)
    {
        cal.set(Calendar.MONTH, Calendar.MARCH);
    }

    protected void month4(@ParserContext Calendar cal)
    {
        cal.set(Calendar.MONTH, Calendar.APRIL);
    }

    protected void month5(@ParserContext Calendar cal)
    {
        cal.set(Calendar.MONTH, Calendar.MAY);
    }

    protected void month6(@ParserContext Calendar cal)
    {
        cal.set(Calendar.MONTH, Calendar.JUNE);
    }

    protected void month7(@ParserContext Calendar cal)
    {
        cal.set(Calendar.MONTH, Calendar.JULY);
    }

    protected void month8(@ParserContext Calendar cal)
    {
        cal.set(Calendar.MONTH, Calendar.AUGUST);
    }

    protected void month9(@ParserContext Calendar cal)
    {
        cal.set(Calendar.MONTH, Calendar.SEPTEMBER);
    }

    protected void month10(@ParserContext Calendar cal)
    {
        cal.set(Calendar.MONTH, Calendar.OCTOBER);
    }

    protected void month11(@ParserContext Calendar cal)
    {
        cal.set(Calendar.MONTH, Calendar.NOVEMBER);
    }

    protected void month12(@ParserContext Calendar cal)
    {
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
    }

    @Rule("digit13")
    protected void weekInYear(int w, @ParserContext Calendar cal)
    {
        cal.set(Calendar.WEEK_OF_YEAR, w);
    }

    @Rule("digit1")
    protected void weekInMonth(int w, @ParserContext Calendar cal)
    {
        cal.set(Calendar.WEEK_OF_MONTH, w);
    }

    @Rule("digit13")
    protected void dayInYear(int d, @ParserContext Calendar cal)
    {
        cal.set(Calendar.DAY_OF_YEAR, d);
    }

    @Rule("digit12")
    protected void dayInMonth(int d, @ParserContext Calendar cal)
    {
        cal.set(Calendar.DAY_OF_MONTH, d);
    }

    @Rule("digit1")
    protected void dayInWeekInMonth(int d, @ParserContext Calendar cal)
    {
        cal.set(Calendar.DAY_OF_WEEK_IN_MONTH, d);
    }

    protected void weekday1(@ParserContext Calendar cal)
    {
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
    }

    protected void weekday2(@ParserContext Calendar cal)
    {
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    }

    protected void weekday3(@ParserContext Calendar cal)
    {
        cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
    }

    protected void weekday4(@ParserContext Calendar cal)
    {
        cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
    }

    protected void weekday5(@ParserContext Calendar cal)
    {
        cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
    }

    protected void weekday6(@ParserContext Calendar cal)
    {
        cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
    }

    protected void weekday7(@ParserContext Calendar cal)
    {
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
    }

    protected void am(@ParserContext Calendar cal)
    {
        cal.set(Calendar.AM_PM, Calendar.AM);
    }

    protected void pm(@ParserContext Calendar cal)
    {
        cal.set(Calendar.AM_PM, Calendar.PM);
    }

    @Rule("digit12")
    protected void hour23(int h, @ParserContext Calendar cal)
    {
        cal.set(Calendar.HOUR_OF_DAY, h);
    }

    @Rule("digit12")
    protected void hour24(int h, @ParserContext Calendar cal)
    {
        cal.set(Calendar.HOUR_OF_DAY, h + 1);
    }

    @Rule("digit12")
    protected void hour11(int h, @ParserContext Calendar cal)
    {
        cal.set(Calendar.HOUR, h);
    }

    @Rule("digit12")
    protected void hour12(int h, @ParserContext Calendar cal)
    {
        cal.set(Calendar.HOUR, h + 1);
    }

    @Rule("digit12")
    protected void minute(int m, @ParserContext Calendar cal)
    {
        cal.set(Calendar.MINUTE, m);
    }

    @Rule("digit12")
    protected void second(int s, @ParserContext Calendar cal)
    {
        cal.set(Calendar.SECOND, s);
    }

    @Rule("digit3")
    protected void milliSecond(int m, @ParserContext Calendar cal)
    {
        cal.set(Calendar.MILLISECOND, m);
    }
    
    //@Rule("tzName")
    protected void generalTZ(int offset, @ParserContext Calendar cal)
    {
        cal.set(Calendar.ZONE_OFFSET, offset);
    }

    @Terminal(expression="GMT[\\+\\-][0-9]{2}:[0-9]{2}")    
    protected int rfc822String1(String s)
    {
        char sign = s.charAt(3);
        int h = Integer.parseInt(s.substring(4, 6));
        int m = Integer.parseInt(s.substring(7, 9));
        if (sign == '+')
        {
            return h * 3600000 + m * 60000;
        }
        else
        {
            return -(h * 3600000 + m * 60000);
        }
    }
    
    @Terminal(expression="GMT[\\+\\-][0-9]{2}[0-9]{2}")    
    protected int rfc822String2(String s)
    {
        char sign = s.charAt(3);
        int h = Integer.parseInt(s.substring(4, 6));
        int m = Integer.parseInt(s.substring(6, 8));
        if (sign == '+')
        {
            return h * 3600000 + m * 60000;
        }
        else
        {
            return -(h * 3600000 + m * 60000);
        }
    }
    
    @Terminal(expression="[\\+\\-][0-9]{2}:[0-9]{2}")    
    protected int rfc822String3(String s)
    {
        char sign = s.charAt(0);
        int h = Integer.parseInt(s.substring(1, 3));
        int m = Integer.parseInt(s.substring(4, 6));
        if (sign == '+')
        {
            return h * 3600000 + m * 60000;
        }
        else
        {
            return -(h * 3600000 + m * 60000);
        }
    }
    
    @Terminal(expression="[\\+\\-][0-9]{2}[0-9]{2}")    
    protected int rfc822String4(String s)
    {
        char sign = s.charAt(0);
        int h = Integer.parseInt(s.substring(1, 3));
        int m = Integer.parseInt(s.substring(3, 5));
        if (sign == '+')
        {
            return h * 3600000 + m * 60000;
        }
        else
        {
            return -(h * 3600000 + m * 60000);
        }
    }
    @Rules({
    @Rule("rfc822String1"),
    @Rule("rfc822String2"),
    @Rule("rfc822String3"),
    @Rule("rfc822String4")
    })
    protected void rfc822(int offset, @ParserContext Calendar cal)
    {
        cal.set(Calendar.ZONE_OFFSET, offset);
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
