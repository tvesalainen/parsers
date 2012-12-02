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

package org.vesalainen.parsers.sql;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Timo Vesalainen
 */
public class ToDateFunction extends AbstractFunction
{
    private SimpleDateFormat format;

    public ToDateFunction(ColumnReference inner, String... format)
    {
        super(inner);
        switch (format.length)
        {
            case 0:
                break;
            case 1:
                this.format = new SimpleDateFormat(format[0]);
            default:
                throw new IllegalArgumentException("wrong number of arguments");
        }
    }

    @Override
    public Object function(Object value)
    {
        if (value != null)
        {
            try
            {
                format.parse(value.toString());
            }
            catch (ParseException ex)
            {
                throw new IllegalArgumentException(ex);
            }
        }
        return null;
    }

}
