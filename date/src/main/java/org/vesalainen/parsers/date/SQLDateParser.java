/*
 * Copyright (C) 2012 Timo Vesalainen
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
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import org.vesalainen.parser.GenClassFactory;
import static org.vesalainen.parser.ParserFeature.SingleThread;
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
 * @author Timo Vesalainen
 * @see <a href="https://ronsavage.github.io/SQL/sql-2003-2.bnf.html">BNF Syntax for SQL</a>
 * @see <a href="doc-files/SQLDateParser-sqlDate.html#BNF">Implemented BNF Syntax for SQL Date</a>
 * @see <a href="doc-files/SQLDateParser-sqlTime.html#BNF">Implemented BNF Syntax for SQL Time</a>
 * @see <a href="doc-files/SQLDateParser-sqlTimestamp.html#BNF">Implemented BNF Syntax for SQL Timestamp</a>
 */
@GenClassname("org.vesalainen.parsers.date.SQLDateParserImpl")
@GrammarDef //(grammarClass=SQLDateGrammar.class)
@Terminals({
@Terminal(left="':'", expression=":")
,@Terminal(left="'\\-'", expression="\\-")
,@Terminal(left="' '", expression=" ")
})
@Rules({
@Rule(left="mm", value={"minute"})
,@Rule(left="dd", value={"dayInMonth"})
,@Rule(left="HH", value={"hour23"})
,@Rule(left="sqlTime", value={"HH", "':'", "mm", "':'", "ss"})
,@Rule(left="sqlTime", value={"HH", "':'", "mm", "':'", "ss", "z"})
,@Rule(left="MM", value={"month"})
,@Rule(left="yyyy", value={"year4"})
,@Rule(left="ss", value={"second"})
,@Rule(left="date", value={"sqlDate"})
,@Rule(left="date", value={"sqlTimestamp"})
,@Rule(left="date", value={"sqlTime"})
,@Rule(left="sqlDate", value={"yyyy", "'\\-'", "MM", "'\\-'", "dd"})
,@Rule(left="sqlTimestamp", value={"yyyy", "'\\-'", "MM", "'\\-'", "dd", "' '", "HH", "':'", "mm", "':'", "ss", "z"})
,@Rule(left="sqlTimestamp", value={"yyyy", "'\\-'", "MM", "'\\-'", "dd", "' '", "HH", "':'", "mm", "':'", "ss"})
})
public abstract class SQLDateParser extends DateReducers
{
    public static SQLDateParser newInstance() throws NoSuchMethodException, IOException, NoSuchFieldException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        return (SQLDateParser) GenClassFactory.getGenInstance(SQLDateParser.class);
    }
    /**
     * @deprecated Use parseZonedTimestamp
     * @param text
     * @return 
     */
    public Date parseDate(String text)
    {
        SimpleMutableDateTime calendar = new SimpleMutableDateTime(0, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        parseDate(text, calendar);
        return Date.from(Instant.from(ZonedDateTime.from(calendar)));
    }
    /**
     * @deprecated Use parseLocalTime
     * @param text
     * @return 
     */
    public Date parseTime(String text)
    {
        SimpleMutableDateTime calendar = new SimpleMutableDateTime(0, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        parseTime(text, calendar);
        return Date.from(Instant.from(ZonedDateTime.from(calendar)));
    }
    public LocalTime parseLocalTime(String text)
    {
        SimpleMutableDateTime calendar = new SimpleMutableDateTime(0, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        parseTime(text, calendar);
        return LocalTime.from(calendar);
    }
    /**
     * @deprecated Use parseZonedTimestamp
     * @param text
     * @return 
     */
    public Date parseTimestamp(String text)
    {
        SimpleMutableDateTime calendar = new SimpleMutableDateTime(0, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        parseTimestamp(text, calendar);
        return Date.from(Instant.from(ZonedDateTime.from(calendar)));
    }
    public ZonedDateTime parseZonedTimestamp(String text)
    {
        SimpleMutableDateTime calendar = new SimpleMutableDateTime();
        parseTimestamp(text, calendar);
        return ZonedDateTime.from(calendar);
    }
    /**
     * 
     * @param text
     * @param dateTime 
     * @see <a href="doc-files/SQLDateParser-sqlDate.html#BNF">BNF Syntax for SQL Date</a>
     */
    @ParseMethod(start = "sqlDate", features={SingleThread})
    protected abstract void parseDate(String text, @ParserContext MutableDateTime dateTime);
    /**
     * 
     * @param text
     * @param dateTime 
     * @see <a href="doc-files/SQLDateParser-sqlTime.html#BNF">BNF Syntax for SQL Time</a>
     */
    @ParseMethod(start = "sqlTime", features={SingleThread})
    protected abstract void parseTime(String text, @ParserContext MutableDateTime dateTime);
    /**
     * 
     * @param text
     * @param dateTime 
     * @see <a href="doc-files/SQLDateParser-sqlTimestamp.html#BNF">BNF Syntax for SQL Timestamp</a>
     */
    @ParseMethod(start = "sqlTimestamp", features={SingleThread})
    protected abstract void parseTimestamp(String text, @ParserContext MutableDateTime dateTime);
    
}
