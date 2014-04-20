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
import java.util.Set;

/**
 * Note! Pair<T> returned by iterator next() should not be stored. For performance
 * reasons the Pair<T> object will stay the 
 * @author Timo Vesalainen
 */
public class CartesianPairSet<T> extends Pair<Set<T>> implements Iterable<Pair<T>>
{

    public CartesianPairSet(Set<T> item1, Set<T> item2)
    {
        super(item1, item2);
    }

    @Override
    public Iterator<Pair<T>> iterator()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public class PairIterator implements Iterator<Pair<T>>
    {
        private Iterator<T> iterator1;
        private Iterator<T> iterator2;
        private Pair<T> pair;

        public PairIterator()
        {
            iterator1 = item1.iterator();
            iterator2 = item2.iterator();
        }
        
        @Override
        public boolean hasNext()
        {
            return iterator1.hasNext() && iterator2.hasNext();
        }

        @Override
        public Pair<T> next()
        {
            if (pair == null)
            {
                T next1 = iterator1.next();
                T next2 = iterator2.next();
                pair = new Pair<>(next1, next2);
            }
            else
            {
                if (iterator2.hasNext())
                {
                    T next2 = iterator2.next();
                    pair.setItem2(next2);
                }
                else
                {
                    iterator2 = item2.iterator();
                    T next1 = iterator1.next();
                    T next2 = iterator2.next();
                    pair.setItem1(next1);
                    pair.setItem2(next2);
                }
            }
            return pair;
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
}
