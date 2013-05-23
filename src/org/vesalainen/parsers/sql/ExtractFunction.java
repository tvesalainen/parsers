/*
 * Copyright (C) 2013 Timo Vesalainen
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

import java.util.Calendar;
import java.util.Date;

/**
 * @author Timo Vesalainen
 */
public class ExtractFunction extends AbstractFunction
{
    public enum Type {
        SECOND,
        MINUTE,
        HOUR,
        DAY,
        WEEK,
        MONTH,
        YEAR        
    };
    private Type type;
    
    public ExtractFunction(ColumnReference inner, String type)
    {
        super(inner);
        try
        {
            this.type = Type.valueOf(type.toUpperCase());
        }
        catch (IllegalArgumentException ex)
        {
            inner.throwException(type+" not supported in extract");
        }
    }

    @Override
    public Object function(Object value)
    {
        if (value instanceof Date)
        {
            Date date = (Date) value;
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            switch (type)
            {
                case SECOND:
                    return cal.get(Calendar.SECOND);
                case MINUTE:
                    return cal.get(Calendar.MINUTE);
                case HOUR:
                    return cal.get(Calendar.HOUR_OF_DAY);
                case DAY:
                    return cal.get(Calendar.DAY_OF_MONTH);
                case WEEK:
                    return cal.get(Calendar.WEEK_OF_YEAR);
                case MONTH:
                    return cal.get(Calendar.MONTH);
                case YEAR:
                    return cal.get(Calendar.YEAR);
                default:
                    throw new UnsupportedOperationException(type.name());
            }
        }
        throw new UnsupportedOperationException("extract not supported for "+value);
    }

}
