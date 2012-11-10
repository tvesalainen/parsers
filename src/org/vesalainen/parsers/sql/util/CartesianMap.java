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

package org.vesalainen.parsers.sql.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author Timo Vesalainen
 */
public class CartesianMap<T> implements Map<T,Set<T>> 
{
    private Set<T> keys;
    private Set<T> values;

    public CartesianMap(Set<T> keys, Set<T> values)
    {
        this.keys = keys;
        this.values = values;
    }
    
    @Override
    public int size()
    {
        return keys.size();
    }

    @Override
    public boolean isEmpty()
    {
        return keys.isEmpty();
    }

    @Override
    public boolean containsKey(Object key)
    {
        return keys.contains(key);
    }

    @Override
    public boolean containsValue(Object value)
    {
        return values.contains(value);
    }

    @Override
    public Set<T> get(Object key)
    {
        if (keys.contains(key))
        {
            return values;
        }
        return null;
    }

    @Override
    public Set<T> put(T key, Set<T> value)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<T> remove(Object key)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void putAll(Map<? extends T, ? extends Set<T>> m)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void clear()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<T> keySet()
    {
        return keys;
    }

    @Override
    public Collection<Set<T>> values()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<Entry<T, Set<T>>> entrySet()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
