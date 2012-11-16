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

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This Map is a fast map for fixed number on uniquely numbered Table instances
 * @author Timo Vesalainen
 */
public class ArrayMap<T,V> extends AbstractMap<T,V> implements Cloneable
{
    private Map<T,Integer> map;
    private V[] array;

    public ArrayMap(Collection<T> init)
    {
        this.array = (V[]) new Object[init.size()];
        map = new HashMap<>(init.size()*2);
        int index = 0;
        for (T t : init)
        {
            map.put(t, index++);
        }
    }

    public ArrayMap(T... init)
    {
        this.array = (V[]) new Object[init.length];
        map = new HashMap<>(init.length*2);
        int index = 0;
        for (T t : init)
        {
            map.put(t, index++);
        }
    }

    public int getIndexOf(T key)
    {
        Integer index = map.get(key);
        if (index != null)
        {
            return index;
        }
        throw new IllegalArgumentException(key+" unknown");
    }
    
    @Override
    public int size()
    {
        int count = 0;
        for (Object ob : array)
        {
            if (ob != null)
            {
                count++;
            }
        }
        return count;
    }

    @Override
    public boolean isEmpty()
    {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key)
    {
        Integer index = map.get(key);
        if (index != null)
        {
            return array[index] != null;
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value)
    {
        for (Object v : array)
        {
            if (value.equals(v))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public V get(Object key)
    {
        Integer index = map.get(key);
        if (index != null)
        {
            return array[index];
        }
        return null;
    }

    public V get(int index)
    {
        return (V) array[index];
    }

    public int capacity()
    {
        return array.length;
    }
    
    @Override
    public V put(T key, V value)
    {
        Integer index = map.get(key);
        if (index != null)
        {
            V v = (V) array[index];
            array[index] = value;
            return v;
        }
        return null;
    }

    public V put(int index, V value)
    {
        V v = (V) array[index];
        array[index] = value;
        return v;
    }

    @Override
    public V remove(Object key)
    {
        Integer index = map.get(key);
        if (index != null)
        {
            V v = (V) array[index];
            array[index] = null;
            return v;
        }
        return null;
    }

    public V remove(int index)
    {
        V v = (V) array[index];
        array[index] = null;
        return v;
    }
    @Override
    public void clear()
    {
        Arrays.fill(array, null);
    }

    @Override
    public Set<Entry<T, V>> entrySet()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString()
    {
        return "ArrayMap{" + "map=" + map + '}';
    }

}
