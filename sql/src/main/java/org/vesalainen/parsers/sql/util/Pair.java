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

import java.util.Objects;

/**
 * @author Timo Vesalainen
 */
public class Pair<T> 
{
    protected T item1;
    protected T item2;

    public Pair(T item1, T item2)
    {
        this.item1 = item1;
        this.item2 = item2;
    }

    public T getItem1()
    {
        return item1;
    }

    public T getItem2()
    {
        return item2;
    }

    public void setItem1(T item1)
    {
        this.item1 = item1;
    }

    public void setItem2(T item2)
    {
        this.item2 = item2;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Pair<T> other = (Pair<T>) obj;
        if (!Objects.equals(this.item1, other.item1))
        {
            return false;
        }
        if (!Objects.equals(this.item2, other.item2))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.item1);
        hash = 89 * hash + Objects.hashCode(this.item2);
        return hash;
    }

    @Override
    public String toString()
    {
        return "Pair{" + item1 + ", " + item2 + '}';
    }
    
}
