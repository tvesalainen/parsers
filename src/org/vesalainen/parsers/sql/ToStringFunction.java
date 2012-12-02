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

/**
 * Conversion function using String.format
 * @author Timo Vesalainen
 */
public class ToStringFunction extends AbstractFunction
{
    private String format;

    public ToStringFunction(ColumnReference inner, String... format)
    {
        super(inner);
        switch (format.length)
        {
            case 0:
                break;
            case 1:
                this.format = format[0];
            default:
                throw new IllegalArgumentException("wrong number of arguments");
        }
    }
    
    @Override
    public Object function(Object value)
    {
        if (value != null)
        {
            if (format != null)
            {
                return String.format(format, value);
            }
            else
            {
                return value.toString();
            }
        }
        return null;
    }

}
