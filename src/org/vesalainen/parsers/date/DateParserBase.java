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
import java.util.Locale;
import org.vesalainen.parser.ParserFactory;
import org.vesalainen.parser.annotation.GenClassname;
import org.vesalainen.parser.annotation.GrammarDef;
import org.vesalainen.parser.annotation.ParseMethod;
import org.vesalainen.parser.annotation.ParserContext;

/**
 * @author Timo Vesalainen
 */
@GenClassname("org.vesalainen.parsers.date.DateParser")
@GrammarDef
public abstract class DateParserBase extends DateReducers
{
    private static final String PATTERNKEY = "__PatternKey__";

    public Date parse(String text) throws IOException
    {
        Calendar calendar = getInstance();
        parse(text, calendar);
        return calendar.getTime();
    }
    @ParseMethod(start = PATTERNKEY)
    protected abstract void parse(String text, @ParserContext Calendar calendar) throws IOException;

    public static DateParserBase newInstance(String... formats) throws NoSuchMethodException, IOException, NoSuchFieldException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        AbstractDatePatterns patterns = new AbstractDatePatterns(Locale.US, DateParserBase.class);
        for (String format : formats)
        {
            patterns.addPattern(PATTERNKEY, format);
        }
        return (DateParserBase) ParserFactory.getParserInstance(DateParserBase.class, patterns);
    }
    private Calendar getInstance()
    {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.ZONE_OFFSET, 0);
        cal.set(Calendar.DST_OFFSET, 0);
        return cal;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        try
        {
            DateParserBase parser = DateParserBase.newInstance("yyyy-MM");
            Date date = parser.parse("2012-12");
            System.err.println(date);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
