/*
 * Copyright (C) 2011 Timo Vesalainen
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
package org.vesalainen.parsers.date;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.vesalainen.parser.ParserFactory;
import org.vesalainen.parser.annotation.GenClassname;
import org.vesalainen.parser.annotation.GrammarDef;
import org.vesalainen.parser.annotation.ParseMethod;
import org.vesalainen.parser.annotation.ParserContext;

/**
 * LocaleDateParserBase is a base class for generated LocaleDateParser class.
 * LocaleDateParser parses dates in locale-sensitive way.
 * <p>
 * Note that it is possible that in some locales there will
 * be conflicting rules in grammar. If that happens, try to remove some of the
 * patterns in LocaleDatePatterns. 
 * @author Timo Vesalainen
 */
@GenClassname("org.vesalainen.parsers.date.LocaleDateParser")
@GrammarDef
public abstract class LocaleDateParserBase extends DateReducers
{
    public Date parseDate(String text) throws IOException
    {
        return parseCalendarDate(text).getTime();
    }

    public Calendar parseCalendarDate(String text) throws IOException
    {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.ZONE_OFFSET, 0);
        cal.set(Calendar.DST_OFFSET, 0);
        parseDate(text, cal);
        return cal;
    }

    public Date parseDateTime(String text) throws IOException
    {
        return parseCalendarDate(text).getTime();
    }

    public Calendar parseCalendarDateTime(String text) throws IOException
    {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.ZONE_OFFSET, 0);
        cal.set(Calendar.DST_OFFSET, 0);
        parseDateTime(text, cal);
        return cal;
    }

    public Date parseTime(String text) throws IOException
    {
        return parseCalendarDate(text).getTime();
    }

    public Calendar parseCalendarTime(String text) throws IOException
    {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.ZONE_OFFSET, 0);
        cal.set(Calendar.DST_OFFSET, 0);
        parseTime(text, cal);
        return cal;
    }

    @ParseMethod(start = "dateTime", wideIndex = true)
    protected abstract void parseDateTime(String text, @ParserContext Calendar calendar) throws IOException;

    @ParseMethod(start = "date", wideIndex = true)
    protected abstract void parseDate(String text, @ParserContext Calendar calendar) throws IOException;

    @ParseMethod(start = "time", wideIndex = true)
    protected abstract void parseTime(String text, @ParserContext Calendar calendar) throws IOException;

    public static LocaleDateParserBase newInstance() throws IOException
    {
        return (LocaleDateParserBase) ParserFactory.getParserInstance(LocaleDateParserBase.class, new LocaleDatePatterns());
    }
    
    public static void main(String... args)
    {
        try
        {
            Locale.setDefault(new Locale("fi", "FI"));
            LocaleDateParserBase p = LocaleDateParserBase.newInstance();
            
            Date d = p.parseDate("2011-12-17T16:48EET");
            System.err.println(d);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
