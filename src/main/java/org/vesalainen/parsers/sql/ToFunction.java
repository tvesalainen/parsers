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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Converts types dynamically using reflect methods. 
 * @author Timo Vesalainen
 */
public class ToFunction extends AbstractFunction
{
    private Class<?> type;

    public ToFunction(ColumnReference inner, Class<?> type)
    {
        super(inner);
        this.type = type;
    }
    
    @Override
    public Object function(Object value)
    {
        if (value != null)
        {
            try
            {
                Constructor constructor = type.getConstructor(value.getClass());
                return constructor.newInstance(value);
            }
            catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex)
            {
                throw new IllegalArgumentException("conversion from "+value.getClass()+" to "+type+" not implemented");
            }
        }
        return null;
    }

}
