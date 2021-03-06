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
import java.util.Locale;
import org.vesalainen.regex.SyntaxErrorException;

/**
 * @author Timo Vesalainen
 */
public class SQLDateGrammar extends SimpleDateGrammar
{
    public SQLDateGrammar() throws UnsupportedOperationException, IOException
    {
        super(Locale.US, SQLDateParser.class);
        try
        {
            addPattern("sqlDate", "yyyy-MM-dd");
            addPattern("sqlTime", "HH:mm:ss");
            addPattern("sqlTime", "HH:mm:ssZ");
            addPattern("sqlTimestamp", "yyyy-MM-dd HH:mm:ss");
            addPattern("sqlTimestamp", "yyyy-MM-dd HH:mm:ssZ");
        }
        catch (IOException | SyntaxErrorException ex)
        {
            throw new UnsupportedOperationException(ex);
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        try
        {
            SQLDateGrammar g = new SQLDateGrammar();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
