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

import java.util.Comparator;

/**
 * @author Timo Vesalainen
 */
public class Range<C> 
{
    Comparator<C> comparator;
    private C lower;
    private C upper;

    public Range(Comparator<C> comparator)
    {
        this.comparator = comparator;
    }

    public Range(Comparator<C> comparator, C lower, C upper)
    {
        this.comparator = comparator;
        this.lower = lower;
        this.upper = upper;
    }

    public void narrow(C lower, C upper)
    {
        if (this.lower == null || comparator.compare(this.lower, lower) < 0)
        {
            this.lower = lower;
        }
        if (this.upper == null || comparator.compare(this.upper, upper) > 0)
        {
            this.upper = upper;
        }
    }
    public void lower(C lower)
    {
        if (this.lower == null || comparator.compare(this.lower, lower) < 0)
        {
            this.lower = lower;
        }
    }
    public void upper(C upper)
    {
        if (this.upper == null || comparator.compare(this.upper, upper) > 0)
        {
            this.upper = upper;
        }
    }
    public void exact(C item)
    {
        this.lower = item;
        this.upper = item;
    }
    public boolean inRange(C item)
    {
        return (this.lower == null || comparator.compare(this.lower, item) <= 0) &&
               (this.upper == null || comparator.compare(this.upper, item) >= 0);
    }
    public boolean isSingle()
    {
        return this.lower != null && this.lower.equals(this.upper);
    }
}
