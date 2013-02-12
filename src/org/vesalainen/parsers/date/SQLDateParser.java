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
import java.util.Calendar;
import java.util.Date;
import org.vesalainen.parser.ParserFactory;
import org.vesalainen.parser.annotation.GenClassname;
import org.vesalainen.parser.annotation.GrammarDef;
import org.vesalainen.parser.annotation.ParseMethod;
import org.vesalainen.parser.annotation.ParserContext;

/**
 * @author Timo Vesalainen
 * @see <a href="http://savage.net.au/SQL/sql-2003-2.bnf.html#date string">BNF Syntax for SQL Date</a>
 * @see <a href="http://savage.net.au/SQL/sql-2003-2.bnf.html#time string">BNF Syntax for SQL Time</a>
 * @see <a href="http://savage.net.au/SQL/sql-2003-2.bnf.html#timestamp string">BNF Syntax for SQL Timestamp</a>
 * @see <a href="doc-files/SQLDateParser-sqlDate.html#BNF">Implemented BNF Syntax for SQL Date</a>
 * @see <a href="doc-files/SQLDateParser-sqlTime.html#BNF">Implemented BNF Syntax for SQL Time</a>
 * @see <a href="doc-files/SQLDateParser-sqlTimestamp.html#BNF">Implemented BNF Syntax for SQL Timestamp</a>
 */
@GenClassname("org.vesalainen.parsers.date.SQLDateParserImpl")
@GrammarDef(grammarClass=SQLDateGrammar.class)
public abstract class SQLDateParser extends DateReducers
{
    public static SQLDateParser newInstance() throws NoSuchMethodException, IOException, NoSuchFieldException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        return (SQLDateParser) ParserFactory.getParserInstance(SQLDateParser.class);
    }
    public Date parseDate(String text)
    {
        Calendar calendar = getInstance();
        parseDate(text, calendar);
        return calendar.getTime();
    }
    public Date parseTime(String text)
    {
        Calendar calendar = getInstance();
        parseTime(text, calendar);
        return calendar.getTime();
    }
    public Date parseTimestamp(String text)
    {
        Calendar calendar = getInstance();
        parseTimestamp(text, calendar);
        return calendar.getTime();
    }
    /**
     * 
     * @param text
     * @param calendar 
     * @see <a href="doc-files/SQLDateParser-sqlDate.html#BNF">BNF Syntax for SQL Date</a>
     */
    @ParseMethod(start = "sqlDate")
    protected abstract void parseDate(String text, @ParserContext Calendar calendar);
    /**
     * 
     * @param text
     * @param calendar 
     * @see <a href="doc-files/SQLDateParser-sqlTime.html#BNF">BNF Syntax for SQL Time</a>
     */
    @ParseMethod(start = "sqlTime")
    protected abstract void parseTime(String text, @ParserContext Calendar calendar);
    /**
     * 
     * @param text
     * @param calendar 
     * @see <a href="doc-files/SQLDateParser-sqlTimestamp.html#BNF">BNF Syntax for SQL Timestamp</a>
     */
    @ParseMethod(start = "sqlTimestamp")
    protected abstract void parseTimestamp(String text, @ParserContext Calendar calendar);
    
    private Calendar getInstance()
    {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.ZONE_OFFSET, 0);
        cal.set(Calendar.DST_OFFSET, 0);
        return cal;
    }
}
