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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;

/**
 * @author Timo Vesalainen
 */
public class ObjectComparator implements Comparator<Object>
{
    public static final ObjectComparator STATIC = new ObjectComparator();
    
    @Override
    public int compare(Object o1, Object o2)
    {
        assert o1 != null && o2 != null;
        Class<?> c1 = o1.getClass();
        if (c1.equals(o2.getClass()) && (o1 instanceof Comparable))
        {
            try
            {
                Method m = c1.getMethod("compareTo", c1);
                return (int) m.invoke(o1, o2);
            }
            catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException ex)
            {
                throw new IllegalArgumentException(ex);
            }
        }
        if ((o1 instanceof Number) && (o2 instanceof Number))
        {
            Number n1 = (Number) o1;
            Number n2 = (Number) o2;
            return Double.compare(n1.doubleValue(), n2.doubleValue());
        }
        throw new UnsupportedOperationException("Comparing "+o1+" to "+o2+" not supported");
    }

}
