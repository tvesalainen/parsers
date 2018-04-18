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

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.vesalainen.parser.GenClassFactory;
import static org.vesalainen.parser.ParserFeature.*;
import org.vesalainen.parser.annotation.GenClassname;
import org.vesalainen.parser.annotation.GrammarDef;
import org.vesalainen.parser.annotation.ParseMethod;
import org.vesalainen.parser.annotation.ParserContext;
import org.vesalainen.parser.annotation.Rule;
import org.vesalainen.parser.annotation.Rules;
import org.vesalainen.parser.annotation.Terminal;
import org.vesalainen.parser.annotation.Terminals;
import org.vesalainen.time.MutableDateTime;
import org.vesalainen.time.SimpleMutableDateTime;

/**
 * InternetDateParser parses dates in either ISO8601, RFC1123, RFC850 or asctime format.
 * @author Timo Vesalainen
 * @see <a href="doc-files/InternetDateParser-date.html#BNF">BNF Syntax for RFC1123 or RFC850 or AscTime or ISO8601 date</a>
 * @see <a href="doc-files/InternetDateParser-rfc1123.html#BNF">BNF Syntax for RFC1123 date</a>
 * @see <a href="doc-files/InternetDateParser-rfc850.html#BNF">BNF Syntax for RFC850 date</a>
 * @see <a href="doc-files/InternetDateParser-ascTime.html#BNF">BNF Syntax for AscTime date</a>
 * @see <a href="doc-files/InternetDateParser-iso8601.html#BNF">BNF Syntax for ISO8601 date</a>
 */
@GenClassname("org.vesalainen.parsers.date.InternetDateParserImpl")
@GrammarDef
@Terminals({
@Terminal(left="'T'", expression="T")
,@Terminal(left="'Tue|Tues|Tuesd|Tuesda|Tuesday'", expression="Tue|Tues|Tuesd|Tuesda|Tuesday")
,@Terminal(left="'Sat|Satu|Satur|Saturd|Saturda|Saturday'", expression="Sat|Satu|Satur|Saturd|Saturda|Saturday")
,@Terminal(left="'\\, '", expression="\\, ")
,@Terminal(left="'Jun|June'", expression="Jun|June")
,@Terminal(left="'\\.'", expression="\\.")
,@Terminal(left="'Jan|Janu|Janua|Januar|January'", expression="Jan|Janu|Janua|Januar|January")
,@Terminal(left="'Wed|Wedn|Wedne|Wednes|Wednesd|Wednesda|Wednesday'", expression="Wed|Wedn|Wedne|Wednes|Wednesd|Wednesda|Wednesday")
,@Terminal(left="'\\-'", expression="\\-")
,@Terminal(left="'Thu|Thur|Thurs|Thursd|Thursda|Thursday'", expression="Thu|Thur|Thurs|Thursd|Thursda|Thursday")
,@Terminal(left="' '", expression=" ")
,@Terminal(left="'Oct|Octo|Octob|Octobe|October'", expression="Oct|Octo|Octob|Octobe|October")
,@Terminal(left="'Sep|Sept|Septe|Septem|Septemb|Septembe|September'", expression="Sep|Sept|Septe|Septem|Septemb|Septembe|September")
,@Terminal(left="'Dec|Dece|Decem|Decemb|Decembe|December'", expression="Dec|Dece|Decem|Decemb|Decembe|December")
,@Terminal(left="'May'", expression="May")
,@Terminal(left="'Apr|Apri|April'", expression="Apr|Apri|April")
,@Terminal(left="'Sun|Sund|Sunda|Sunday'", expression="Sun|Sund|Sunda|Sunday")
,@Terminal(left="'Nov|Nove|Novem|Novemb|Novembe|November'", expression="Nov|Nove|Novem|Novemb|Novembe|November")
,@Terminal(left="'Jul|July'", expression="Jul|July")
,@Terminal(left="'Feb|Febr|Febru|Februa|Februar|February'", expression="Feb|Febr|Febru|Februa|Februar|February")
,@Terminal(left="':'", expression=":")
,@Terminal(left="'Aug|Augu|Augus|August'", expression="Aug|Augu|Augus|August")
,@Terminal(left="'Fri|Frid|Frida|Friday'", expression="Fri|Frid|Frida|Friday")
,@Terminal(left="'Mar|Marc|March'", expression="Mar|Marc|March")
,@Terminal(left="'Mon|Mond|Monda|Monday'", expression="Mon|Mond|Monda|Monday")
})
@Rules({
@Rule(left="MMM", reducer="org.vesalainen.parsers.date.DateReducers month12(org.vesalainen.time.MutableDateTime)", value={"'Dec|Dece|Decem|Decemb|Decembe|December'"})
,@Rule(left="MMM", reducer="org.vesalainen.parsers.date.DateReducers month8(org.vesalainen.time.MutableDateTime)", value={"'Aug|Augu|Augus|August'"})
,@Rule(left="MMM", reducer="org.vesalainen.parsers.date.DateReducers month4(org.vesalainen.time.MutableDateTime)", value={"'Apr|Apri|April'"})
,@Rule(left="MMM", reducer="org.vesalainen.parsers.date.DateReducers month9(org.vesalainen.time.MutableDateTime)", value={"'Sep|Sept|Septe|Septem|Septemb|Septembe|September'"})
,@Rule(left="MMM", reducer="org.vesalainen.parsers.date.DateReducers month2(org.vesalainen.time.MutableDateTime)", value={"'Feb|Febr|Febru|Februa|Februar|February'"})
,@Rule(left="MMM", reducer="org.vesalainen.parsers.date.DateReducers month6(org.vesalainen.time.MutableDateTime)", value={"'Jun|June'"})
,@Rule(left="MMM", reducer="org.vesalainen.parsers.date.DateReducers month3(org.vesalainen.time.MutableDateTime)", value={"'Mar|Marc|March'"})
,@Rule(left="MMM", reducer="org.vesalainen.parsers.date.DateReducers month7(org.vesalainen.time.MutableDateTime)", value={"'Jul|July'"})
,@Rule(left="MMM", reducer="org.vesalainen.parsers.date.DateReducers month5(org.vesalainen.time.MutableDateTime)", value={"'May'"})
,@Rule(left="MMM", reducer="org.vesalainen.parsers.date.DateReducers month1(org.vesalainen.time.MutableDateTime)", value={"'Jan|Janu|Janua|Januar|January'"})
,@Rule(left="MMM", reducer="org.vesalainen.parsers.date.DateReducers month11(org.vesalainen.time.MutableDateTime)", value={"'Nov|Nove|Novem|Novemb|Novembe|November'"})
,@Rule(left="MMM", reducer="org.vesalainen.parsers.date.DateReducers month10(org.vesalainen.time.MutableDateTime)", value={"'Oct|Octo|Octob|Octobe|October'"})
,@Rule(left="mm", value={"minute"})
,@Rule(left="HH", value={"hour23"})
,@Rule(left="EEEE", reducer="org.vesalainen.parsers.date.DateReducers weekday5(org.vesalainen.time.MutableDateTime)", value={"'Thu|Thur|Thurs|Thursd|Thursda|Thursday'"})
,@Rule(left="EEEE", reducer="org.vesalainen.parsers.date.DateReducers weekday4(org.vesalainen.time.MutableDateTime)", value={"'Wed|Wedn|Wedne|Wednes|Wednesd|Wednesda|Wednesday'"})
,@Rule(left="EEEE", reducer="org.vesalainen.parsers.date.DateReducers weekday7(org.vesalainen.time.MutableDateTime)", value={"'Sat|Satu|Satur|Saturd|Saturda|Saturday'"})
,@Rule(left="EEEE", reducer="org.vesalainen.parsers.date.DateReducers weekday2(org.vesalainen.time.MutableDateTime)", value={"'Mon|Mond|Monda|Monday'"})
,@Rule(left="EEEE", reducer="org.vesalainen.parsers.date.DateReducers weekday3(org.vesalainen.time.MutableDateTime)", value={"'Tue|Tues|Tuesd|Tuesda|Tuesday'"})
,@Rule(left="EEEE", reducer="org.vesalainen.parsers.date.DateReducers weekday1(org.vesalainen.time.MutableDateTime)", value={"'Sun|Sund|Sunda|Sunday'"})
,@Rule(left="EEEE", reducer="org.vesalainen.parsers.date.DateReducers weekday6(org.vesalainen.time.MutableDateTime)", value={"'Fri|Frid|Frida|Friday'"})
,@Rule(left="rfc1123", value={"EEE", "('\\,')?", "dd", "MMM", "yyyy", "HH", "':'", "mm", "':'", "ss", "z"})
,@Rule(left="MM", value={"month"})
,@Rule(left="yy", value={"year2"})
,@Rule(left="dateZ", value={"rfc850"})
,@Rule(left="dateZ", value={"iso8601Z"})
,@Rule(left="dateZ", value={"rfc1123"})
,@Rule(left="dateDT", value={"ascTime"})
,@Rule(left="dateDT", value={"iso8601DT"})
,@Rule(left="ascTime", value={"EEE", "('\\,')?", "MMM", "dd", "HH", "':'", "mm", "':'", "ss", "yyyy"})
,@Rule(left="dd", value={"dayInMonth"})
,@Rule(left="rfc850", value={"EEEE", "('\\,')?", "dd", "'\\-'", "MMM", "'\\-'", "yy", "HH", "':'", "mm", "':'", "ss", "z"})
,@Rule(left="iso8601D", value={"yyyy", "'\\-'", "MM", "'\\-'", "dd"})
,@Rule(left="iso8601Y", value={"yyyy"})
,@Rule(left="iso8601YM", value={"yyyy", "('\\-')?", "MM"})

,@Rule(left="iso8601T", value={"HH", "':'", "mm"})
,@Rule(left="iso8601T", value={"HH", "':'", "mm", "':'", "ss"})
,@Rule(left="iso8601T", value={"HH", "':'", "mm", "':'", "ss", "'\\.'", "SSS"})
        
,@Rule(left="iso8601DT", value={"yyyy", "'\\-'", "MM", "'\\-'", "dd", "'T'", "HH", "':'", "mm"})
,@Rule(left="iso8601DT", value={"yyyy", "'\\-'", "MM", "'\\-'", "dd", "'T'", "HH", "':'", "mm", "':'", "ss"})
,@Rule(left="iso8601DT", value={"yyyy", "'\\-'", "MM", "'\\-'", "dd", "'T'", "HH", "':'", "mm", "':'", "ss", "'\\.'", "SSS"})
        
,@Rule(left="iso8601Z", value={"yyyy", "'\\-'", "MM", "'\\-'", "dd", "'T'", "HH", "':'", "mm", "z"})
,@Rule(left="iso8601Z", value={"yyyy", "'\\-'", "MM", "'\\-'", "dd", "'T'", "HH", "':'", "mm", "':'", "ss", "z"})
,@Rule(left="iso8601Z", value={"yyyy", "'\\-'", "MM", "'\\-'", "dd", "'T'", "HH", "':'", "mm", "':'", "ss", "'\\.'", "SSS", "z"})
        
,@Rule(left="rmsExpress", value={"yyyy", "'\\-'", "MM", "'\\-'", "dd", "' '", "HH", "':'", "mm", "':'", "ss", "z"})

,@Rule(left="EEE", reducer="org.vesalainen.parsers.date.DateReducers weekday7(org.vesalainen.time.MutableDateTime)", value={"'Sat|Satu|Satur|Saturd|Saturda|Saturday'"})
,@Rule(left="EEE", reducer="org.vesalainen.parsers.date.DateReducers weekday4(org.vesalainen.time.MutableDateTime)", value={"'Wed|Wedn|Wedne|Wednes|Wednesd|Wednesda|Wednesday'"})
,@Rule(left="EEE", reducer="org.vesalainen.parsers.date.DateReducers weekday6(org.vesalainen.time.MutableDateTime)", value={"'Fri|Frid|Frida|Friday'"})
,@Rule(left="EEE", reducer="org.vesalainen.parsers.date.DateReducers weekday5(org.vesalainen.time.MutableDateTime)", value={"'Thu|Thur|Thurs|Thursd|Thursda|Thursday'"})
,@Rule(left="EEE", reducer="org.vesalainen.parsers.date.DateReducers weekday1(org.vesalainen.time.MutableDateTime)", value={"'Sun|Sund|Sunda|Sunday'"})
,@Rule(left="EEE", reducer="org.vesalainen.parsers.date.DateReducers weekday3(org.vesalainen.time.MutableDateTime)", value={"'Tue|Tues|Tuesd|Tuesda|Tuesday'"})
,@Rule(left="EEE", reducer="org.vesalainen.parsers.date.DateReducers weekday2(org.vesalainen.time.MutableDateTime)", value={"'Mon|Mond|Monda|Monday'"})
,@Rule(left="yyyy", value={"year4"})
,@Rule(left="SSS", value={"milliSecond"})
,@Rule(left="ss", value={"second"})
})
public abstract class InternetDateParser extends DateReducers
{
    public static InternetDateParser newInstance()
    {
        return (InternetDateParser) GenClassFactory.getGenInstance(InternetDateParser.class);
    }
    /**
     * Parses dates in either ISO8601, RFC1123, RFC850 or asctime format.
     * @param text
     * @return 
     * @see java.text.SimpleDateFormat
     */
    public Date parseZonedDate(String text)
    {
        return Date.from(Instant.from(parseZonedDateTime(text)));
    }
    /**
     * Parses dates in either ISO8601, RFC1123 or RFC850.
     * @param text
     * @return 
     * @see java.text.SimpleDateFormat
     */
    public Calendar parseCalendar(String text)
    {
        return GregorianCalendar.from(parseZonedDateTime(text));
    }
    public ZonedDateTime parseZonedDateTime(String text)
    {
        return ZonedDateTime.from(parseZonedMutableDateTime(text));
    }
    private SimpleMutableDateTime parseZonedMutableDateTime(String text)
    {
        SimpleMutableDateTime cal = getInstance();
        parseZ(text, cal);
        return cal;
    }
    public LocalDateTime parseLocalDateTime(String text)
    {
        return LocalDateTime.from(parseMutableDateTime(text));
    }
    private SimpleMutableDateTime parseMutableDateTime(String text)
    {
        SimpleMutableDateTime cal = getInstance();
        parseDT(text, cal);
        return cal;
    }
    /**
     * Parses date in RFC1123 format EEE, dd MMM yyyy HH:mm:ss z
     * @param text
     * @return 
     * @see java.text.SimpleDateFormat
     */
    public Date parseRFC1123(String text)
    {
        return Date.from(Instant.from(parseRFC1123ZonedDateTime(text)));
    }
    public String formatRFC1123(Date date)
    {
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        return format.format(date);
    }
    /**
     * Parses date in RFC1123 format EEE, dd MMM yyyy HH:mm:ss z
     * @param text
     * @return 
     * @see java.text.SimpleDateFormat
     */
    public Calendar parseRFC1123Calendar(String text)
    {
        return GregorianCalendar.from(parseRFC1123ZonedDateTime(text));
    }
    public ZonedDateTime parseRFC1123ZonedDateTime(String text)
    {
        return ZonedDateTime.from(parseRFC1123MutableDateTime(text));
    }
    private SimpleMutableDateTime parseRFC1123MutableDateTime(String text)
    {
        SimpleMutableDateTime cal = getInstance();
        parseRFC1123(text, cal);
        return cal;
    }
    /**
     * Parses date in RFC850 format EEEE, dd-MMM-yy HH:mm:ss z
     * @param text
     * @return 
     * @see java.text.SimpleDateFormat
     */
    public Date parseRFC850(String text)
    {
        return Date.from(Instant.from(parseRFC850ZonedDateTime(text)));
    }
    public String formatRFC850(Date date)
    {
        SimpleDateFormat format = new SimpleDateFormat("EEEE, dd-MMM-yy HH:mm:ss z");
        return format.format(date);
    }
    /**
     * Parses date in RFC850 format EEEE, dd-MMM-yy HH:mm:ss z
     * @param text
     * @return 
     * @see java.text.SimpleDateFormat
     */
    public Calendar parseRFC850Calendar(String text)
    {
        return GregorianCalendar.from(parseRFC850ZonedDateTime(text));
    }
    public ZonedDateTime parseRFC850ZonedDateTime(String text)
    {
        return ZonedDateTime.from(parseRFC850MutableDateTime(text));
    }
    private SimpleMutableDateTime parseRFC850MutableDateTime(String text)
    {
        SimpleMutableDateTime cal = getInstance();
        parseRFC850(text, cal);
        return cal;
    }
    public String formatAscTime(Date date)
    {
        SimpleDateFormat format = new SimpleDateFormat("EEE, MMM dd HH:mm:ss yyyy");
        return format.format(date);
    }
    public LocalDateTime parseAscTimeLocalDateTime(String text)
    {
        return LocalDateTime.from(parseAscTimeMutableDateTime(text));
    }
    private SimpleMutableDateTime parseAscTimeMutableDateTime(String text)
    {
        SimpleMutableDateTime cal = getInstance();
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
     * @see java.text.SimpleDateFormat
     */
    public Date parseISO8601(String text)
    {
        return Date.from(Instant.from(parseISO8601ZonedDateTime(text)));
    }
    public String formatISO8601(Date date)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz");
        return format.format(date);
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
     * @see java.text.SimpleDateFormat
     */
    public Calendar parseISO8601Calendar(String text)
    {
        return GregorianCalendar.from(parseISO8601ZonedDateTime(text));
    }
    public ZonedDateTime parseISO8601ZonedDateTime(String text)
    {
        return ZonedDateTime.from(parseISO8601MutableZonedDateTime(text));
    }
    private SimpleMutableDateTime parseISO8601MutableZonedDateTime(String text)
    {
        SimpleMutableDateTime cal = getInstance();
        parseISO8601Z(text, cal);
        return cal;
    }
    
    public ZonedDateTime parseRMSExpressZonedDateTime(String text)
    {
        return ZonedDateTime.from(parseRMSExpressMutableZonedDateTime(text));
    }
    private SimpleMutableDateTime parseRMSExpressMutableZonedDateTime(String text)
    {
        SimpleMutableDateTime cal = getInstance();
        parseRMSExpress(text, cal);
        return cal;
    }

    public LocalDateTime parseISO8601LocalDateTime(String text)
    {
        return LocalDateTime.from(parseISO8601MutableLocalDateTime(text));
    }
    private SimpleMutableDateTime parseISO8601MutableLocalDateTime(String text)
    {
        SimpleMutableDateTime cal = getInstance();
        parseISO8601DT(text, cal);
        return cal;
    }

    public LocalDate parseISO8601LocalDate(String text)
    {
        return LocalDate.from(parseISO8601MutableLocalDate(text));
    }
    private SimpleMutableDateTime parseISO8601MutableLocalDate(String text)
    {
        SimpleMutableDateTime cal = getInstance();
        parseISO8601D(text, cal);
        return cal;
    }

    public YearMonth parseISO8601YearMonth(String text)
    {
        return YearMonth.from(parseISO8601MutableYearMonth(text));
    }
    private SimpleMutableDateTime parseISO8601MutableYearMonth(String text)
    {
        SimpleMutableDateTime cal = getInstance();
        parseISO8601YM(text, cal);
        return cal;
    }

    public Year parseISO8601Year(String text)
    {
        return Year.from(parseISO8601MutableYear(text));
    }
    private SimpleMutableDateTime parseISO8601MutableYear(String text)
    {
        SimpleMutableDateTime cal = getInstance();
        parseISO8601Y(text, cal);
        return cal;
    }

    public LocalTime parseISO8601LocalTime(String text)
    {
        return LocalTime.from(parseISO8601MutableLocalTime(text));
    }
    private SimpleMutableDateTime parseISO8601MutableLocalTime(String text)
    {
        SimpleMutableDateTime cal = getInstance();
        parseISO8601T(text, cal);
        return cal;
    }

    private SimpleMutableDateTime getInstance()
    {
        return new SimpleMutableDateTime();
    }
    /**
     * 
     * @param text
     * @param dateTime 
     * @see <a href="doc-files/InternetDateParser-date.html#BNF">BNF Syntax for RFC1123 or RFC850 or AscTime or ISO8601 date</a>
     */
    @ParseMethod(start = "dateZ", whiteSpace = "whiteSpace", features={WideIndex, SingleThread})
    protected abstract void parseZ(String text, @ParserContext MutableDateTime dateTime);
    @ParseMethod(start = "dateDT", whiteSpace = "whiteSpace", features={WideIndex, SingleThread})
    protected abstract void parseDT(String text, @ParserContext MutableDateTime dateTime);
    /**
     * 
     * @param text
     * @param dateTime 
     * @see <a href="doc-files/InternetDateParser-rfc1123.html#BNF">BNF Syntax for RFC1123 date</a>
     */
    @ParseMethod(start = "rfc1123", whiteSpace = "whiteSpace", features={WideIndex, SingleThread})
    protected abstract void parseRFC1123(String text, @ParserContext MutableDateTime dateTime);
    /**
     * 
     * @param text
     * @param dateTime 
     * @see <a href="doc-files/InternetDateParser-rfc850.html#BNF">BNF Syntax for RFC850 date</a>
     */
    @ParseMethod(start = "rfc850", whiteSpace = "whiteSpace", features={WideIndex, SingleThread})
    protected abstract void parseRFC850(String text, @ParserContext MutableDateTime dateTime);
    /**
     * 
     * @param text
     * @param dateTime 
     * @see <a href="doc-files/InternetDateParser-ascTime.html#BNF">BNF Syntax for AscTime date</a>
     */
    @ParseMethod(start = "ascTime", whiteSpace = "whiteSpace", features={WideIndex, SingleThread})
    protected abstract void parseAscTime(String text, @ParserContext MutableDateTime dateTime);
    /**
     * 
     * @param text
     * @param dateTime 
     * @see <a href="doc-files/InternetDateParser-iso8601.html#BNF">BNF Syntax for ISO8601 date</a>
     */
    @ParseMethod(start = "iso8601Z", whiteSpace = "whiteSpace", features={WideIndex, SingleThread})
    protected abstract void parseISO8601Z(String text, @ParserContext MutableDateTime dateTime);
    @ParseMethod(start = "iso8601DT", whiteSpace = "whiteSpace", features={WideIndex, SingleThread})
    protected abstract void parseISO8601DT(String text, @ParserContext MutableDateTime dateTime);
    @ParseMethod(start = "iso8601D", whiteSpace = "whiteSpace", features={WideIndex, SingleThread})
    protected abstract void parseISO8601D(String text, @ParserContext MutableDateTime dateTime);
    @ParseMethod(start = "iso8601Y", whiteSpace = "whiteSpace", features={WideIndex, SingleThread})
    protected abstract void parseISO8601Y(String text, @ParserContext MutableDateTime dateTime);
    @ParseMethod(start = "iso8601YM", whiteSpace = "whiteSpace", features={WideIndex, SingleThread})
    protected abstract void parseISO8601YM(String text, @ParserContext MutableDateTime dateTime);
    @ParseMethod(start = "iso8601T", whiteSpace = "whiteSpace", features={WideIndex, SingleThread})
    protected abstract void parseISO8601T(String text, @ParserContext MutableDateTime dateTime);
    @ParseMethod(start = "rmsExpress", features={WideIndex, SingleThread})
    protected abstract void parseRMSExpress(String text, @ParserContext MutableDateTime dateTime);

}
