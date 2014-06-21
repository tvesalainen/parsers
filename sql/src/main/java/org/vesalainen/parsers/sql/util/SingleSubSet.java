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

import java.util.AbstractSet;
import java.util.Iterator;

/**
 * @author Timo Vesalainen
 */
public class SingleSubSet<T> extends AbstractSet<T> 
{
    private FastSet<T> parent;
    private T item;
    private int index = -1;

    SingleSubSet(FastSet<T> parent)
    {
        this.parent = parent;
    }

    SingleSubSet(FastSet<T> parent, T item)
    {
        this.parent = parent;
        this.item = item;
        this.index = parent.getIndexOf(item);
    }

    @Override
    public int size()
    {
        if (item != null)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }

    @Override
    public boolean isEmpty()
    {
        return item == null;
    }

    @Override
    public boolean contains(Object o)
    {
        return item != null && item.equals(o);
    }

    @Override
    public Iterator<T> iterator()
    {
        if (item != null)
        {
            if (!parent.isSet(index))
            {
                item = null;
                index = -1;
            }
        }
        return new Iter(item);
    }

    @Override
    public boolean add(T e)
    {
        if (item == null)
        {
            item = e;
            index = parent.getIndexOf(item);
            return true;
        }
        else
        {
            if (item.equals(e))
            {
                return false;
            }
            throw new IllegalArgumentException("set capasity (1) exceeded");
        }
    }

    @Override
    public boolean remove(Object o)
    {
        if (item != null && item.equals(o))
        {
            item = null;
            parent.resetBit(index);
            index = -1;
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void clear()
    {
        if (item != null)
        {
            parent.resetBit(index);
            item = null;
            index = -1;
        }
    }
    
    public class Iter implements Iterator<T>
    {
        T t;

        public Iter(T t)
        {
            this.t = t;
        }
        
        @Override
        public boolean hasNext()
        {
            return t != null;
        }

        @Override
        public T next()
        {
            T r = t;
            t = null;
            return r;
        }

        @Override
        public void remove()
        {
            item = null;
        }
        
    }
}
