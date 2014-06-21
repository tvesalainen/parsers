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

import java.util.Iterator;

/**
 * SubSet copies contents of FastSet. It behaves like Fast but parent FastSet
 * contents are anded before testing for contains. In another word this set doesn't contain any member
 * that parent set doesn't contain.
 * 
 * @author Timo Vesalainen
 */
public class SubSet<T> extends FastSet<T> 
{
    private FastSet<T> parent;
    
    protected SubSet(FastSet<T> parent)
    {
        super(parent);
        this.parent = parent;
        super.clear();
    }

    @Override
    protected boolean isSet(int index)
    {
        return super.isSet(index) && parent.isSet(index);
    }

    @Override
    public Iterator<T> iterator()
    {
        and(parent);
        return super.iterator();
    }

    @Override
    public int size()
    {
        and(parent);
        return super.size();
    }

    @Override
    public void clear()
    {
        parent.clear(this);
        super.clear();
    }

}
