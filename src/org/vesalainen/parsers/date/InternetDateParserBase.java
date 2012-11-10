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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.vesalainen.parser.ParserFactory;
import org.vesalainen.parser.annotation.GenClassname;
import org.vesalainen.parser.annotation.GrammarDef;
import org.vesalainen.parser.annotation.ParseMethod;
import org.vesalainen.parser.annotation.ParserContext;

/**
 * InternetDateParserBase is an abstract base class for generated InternetDateParser.
 * InternetDateParser parses dates in either ISO8601, RFC1123, RFC850 or asctime format.
 * @author Timo Vesalainen
 */
@GenClassname("org.vesalainen.parsers.date.InternetDateParser")
@GrammarDef
public abstract class InternetDateParserBase extends DateReducers
{
    public static InternetDateParserBase newInstance() throws NoSuchMethodException, IOException, NoSuchFieldException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        return (InternetDateParserBase) ParserFactory.getParserInstance(InternetDateParserBase.class, new InternetDatePatterns());
    }
    /**
     * Parses dates in either ISO8601, RFC1123, RFC850 or asctime format.
     * @param text
     * @return
     * @throws IOException 
     * @see java.text.SimpleDateFormat
     */
    public Date parseDate(String text) throws IOException
    {
        return parseCalendar(text).getTime();
    }
    /**
     * Parses dates in either ISO8601, RFC1123, RFC850 or asctime format.
     * @param text
     * @return
     * @throws IOException 
     * @see java.text.SimpleDateFormat
     */
    public Calendar parseCalendar(String text) throws IOException
    {
        Calendar cal = getInstance();
        parse(text, cal);
        return cal;
    }
    /**
     * Parses date in RFC1123 format EEE, dd MMM yyyy HH:mm:ss z
     * @param text
     * @return
     * @throws IOException 
     * @see java.text.SimpleDateFormat
     */
    public Date parseRFC1123(String text) throws IOException
    {
        return parseCalendar(text).getTime();
    }
    /**
     * Parses date in RFC1123 format EEE, dd MMM yyyy HH:mm:ss z
     * @param text
     * @return
     * @throws IOException 
     * @see java.text.SimpleDateFormat
     */
    public Calendar parseRFC1123Calendar(String text) throws IOException
    {
        Calendar cal = getInstance();
        parseRFC1123(text, cal);
        return cal;
    }
    /**
     * Parses date in RFC850 format EEEE, dd-MMM-yy HH:mm:ss z
     * @param text
     * @return
     * @throws IOException 
     * @see java.text.SimpleDateFormat
     */
    public Date parseRFC850(String text) throws IOException
    {
        return parseCalendar(text).getTime();
    }
    /**
     * Parses date in RFC850 format EEEE, dd-MMM-yy HH:mm:ss z
     * @param text
     * @return
     * @throws IOException 
     * @see java.text.SimpleDateFormat
     */
    public Calendar parseRFC850Calendar(String text) throws IOException
    {
        Calendar cal = getInstance();
        parseRFC850(text, cal);
        return cal;
    }
    /**
     * Parses date in asc time format EEE, MMM dd HH:mm:ss yyyy
     * @param text
     * @return
     * @throws IOException 
     * @see java.text.SimpleDateFormat
     */
    public Date parseAscTime(String text) throws IOException
    {
        return parseCalendar(text).getTime();
    }
    /**
     * Parses date in asc time format EEE, MMM dd HH:mm:ss yyyy
     * @param text
     * @return
     * @throws IOException 
     * @see java.text.SimpleDateFormat
     */
    public Calendar parseAscTimeCalendar(String text) throws IOException
    {
        Calendar cal = getInstance();
        parseAscTime(text, cal);
        return cal;
    }
    /**
     * Parses date in ISO8601 format.
     * <PRE>
     * yyyy-MM-dd'T'HH:mm:ss.SSSz
     * yyyy-MM-dd'T'HH:mm:ssz
     * yyyy-MM-dd'T'HH:mmz
     * yyyy-MM-dd'T'HH:mm:ss.SSSZ
     * yyyy-MM-dd'T'HH:mm:ssZ
     * yyyy-MM-dd'T'HH:mmZ
     * yyyy-MM-dd
     * yyyy-MM
     * yyyy
     * </PRE>
     * @param text
     * @return
     * @throws IOException 
     * @see java.text.SimpleDateFormat
     */
    public Date parseISO8601(String text) throws IOException
    {
        return parseCalendar(text).getTime();
    }
    /**
     * Parses date in ISO8601 format.
     * <PRE>
     * yyyy-MM-dd'T'HH:mm:ss.SSSz
     * yyyy-MM-dd'T'HH:mm:ssz
     * yyyy-MM-dd'T'HH:mmz
     * yyyy-MM-dd'T'HH:mm:ss.SSSZ
     * yyyy-MM-dd'T'HH:mm:ssZ
     * yyyy-MM-dd'T'HH:mmZ
     * yyyy-MM-dd
     * yyyy-MM
     * yyyy
     * </PRE>
     * @param text
     * @return
     * @throws IOException 
     * @see java.text.SimpleDateFormat
     */
    public Calendar parseISO8601Calendar(String text) throws IOException
    {
        Calendar cal = getInstance();
        parseISO8601(text, cal);
        return cal;
    }

    private Calendar getInstance()
    {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.ZONE_OFFSET, 0);
        cal.set(Calendar.DST_OFFSET, 0);
        return cal;
    }
    @ParseMethod(start = "date", wideIndex = true)
    protected abstract void parse(String text, @ParserContext Calendar calendar) throws IOException;

    @ParseMethod(start = "rfc1123", wideIndex = true)
    protected abstract void parseRFC1123(String text, @ParserContext Calendar calendar) throws IOException;

    @ParseMethod(start = "rfc850", wideIndex = true)
    protected abstract void parseRFC850(String text, @ParserContext Calendar calendar) throws IOException;

    @ParseMethod(start = "ascTime", wideIndex = true)
    protected abstract void parseAscTime(String text, @ParserContext Calendar calendar) throws IOException;

    @ParseMethod(start = "iso8601", wideIndex = true)
    protected abstract void parseISO8601(String text, @ParserContext Calendar calendar) throws IOException;

    public static void main(String... args)
    {
        try
        {
            InternetDateParserBase p = InternetDateParserBase.newInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz");
            Date d = p.parseDate("1997-05-08T23:42:50.672EEST");
            String str = sdf.format(d);
            System.err.println(d+" "+str);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
