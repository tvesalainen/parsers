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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Timo Vesalainen
 */
public class FastSet<T> extends AbstractSet<T>
{
    private Map<T,Integer> map;
    private T[] array;
    private long[] bits;
    
    public FastSet(Collection<T> all)
    {
        this(all, false);
    }
    public FastSet(Collection<T> all, boolean fill)
    {
        map = new HashMap<>();
        array = (T[]) new Object[all.size()];
        bits = new long[array.length / 64 + 1];
        if (fill)
        {
            Arrays.fill(bits, 0xffffffff);
        }
        int index = 0;
        for (T t : all)
        {
            map.put(t, index);
            array[index++] = t;
        }
    }

    protected FastSet(FastSet<T> other)
    {
        map = other.map;
        array = other.array;
        bits = Arrays.copyOf(other.bits, other.bits.length);
    }
    
    public FastSet<T> copy()
    {
        return new FastSet<>(this);
    }
    
    public FastSet<T> sub()
    {
        return new SubSet<>(this);
    }
    
    public SingleSubSet<T> singleSub()
    {
        return new SingleSubSet<>(this);
    }
    
    public void and(FastSet<T> other)
    {
        if (map != other.map)
        {
            throw new IllegalArgumentException("objects are not from the same base collection");
        }
        for (int ii=0;ii<bits.length;ii++)
        {
            bits[ii] &= other.bits[ii];
        }
    }

    public void or(FastSet<T> other)
    {
        if (map != other.map)
        {
            throw new IllegalArgumentException("objects are not from the same base collection");
        }
        for (int ii=0;ii<bits.length;ii++)
        {
            bits[ii] |= other.bits[ii];
        }
    }

    void clear(FastSet<T> other)
    {
        if (map != other.map)
        {
            throw new IllegalArgumentException("objects are not from the same base collection");
        }
        for (int ii=0;ii<bits.length;ii++)
        {
            bits[ii] &= ~other.bits[ii];
        }
    }

    void setBit(int index)
    {
        if (index < 0 || index > array.length)
        {
            throw new IndexOutOfBoundsException(index+" negative or > "+array.length);
        }
        bits[index / 64] |= 1L<<(index % 64);
    }
    void resetBit(int index)
    {
        if (index < 0 || index > array.length)
        {
            throw new IndexOutOfBoundsException(index+" negative or > "+array.length);
        }
        bits[index / 64] &= ~(1L<<(index % 64));
    }
    boolean isSet(int index)
    {
        if (index < 0 || index > array.length)
        {
            throw new IndexOutOfBoundsException(index+" negative or > "+array.length);
        }
        return (bits[index / 64] & 1L<<(index % 64)) != 0;
    }

    @Override
    public int size()
    {
        int count = 0;
        int c = 0;
        int max = array.length;
        for (int ii=0;ii<bits.length && c < max;ii++)
        {
            long b = bits[ii];
            if (b != 0)
            {
                for (int jj=0;jj<64 && c < max;jj++,c++)
                {
                    if ((b & 1L<<jj) != 0)
                    {
                        count++;
                    }
                }
            }
            else
            {
                c += 64;
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
    public boolean contains(Object o)
    {
        Integer index = map.get(o);
        if (index != null)
        {
            return isSet(index);
        }
        return false;
    }

    int getIndexOf(T e)
    {
        return map.get(e);
    }
    
    @Override
    public boolean add(T e)
    {
        Integer index = map.get(e);
        if (index != null)
        {
            boolean wasSet = isSet(index);
            setBit(index);
            return !wasSet;
        }
        throw new IllegalArgumentException(e+" was not in initial collection");
    }

    @Override
    public boolean remove(Object o)
    {
        Integer index = map.get(o);
        if (index != null)
        {
            boolean wasSet = isSet(index);
            resetBit(index);
            return wasSet;
        }
        throw new IllegalArgumentException(o+" was not in initial collection");
    }

    @Override
    public void clear()
    {
        Arrays.fill(bits, 0);
    }

    @Override
    public Iterator<T> iterator()
    {
        return new Iter();
    }
    
    private class Iter implements Iterator<T>
    {
        private int next;

        public Iter()
        {
            skipUnset();
        }
        
        private void skipUnset()
        {
            int length = array.length;
            while (
                    next < length &&
                    (bits[next / 64] & 1L<<(next % 64)) == 0
                    )
            {
                next++;
            }
            if (next == length)
            {
                next = -1;
            }
        }
        
        @Override
        public boolean hasNext()
        {
            return next != -1;
        }

        @Override
        public T next()
        {
            T t = array[next++];
            skipUnset();
            return t;
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        try
        {
            Set<String> set = new HashSet<>();
            for (int ii=0;ii<100;ii++)
            {
                set.add("item"+ii);
            }
            FastSet<String> fset = new FastSet<>(set, false);
            System.err.println(fset.size());
            for (int ii=0;ii<100;ii++)
            {
                System.err.println(ii+": "+fset.isSet(ii));
            }
            System.err.println(fset.size());
            System.err.println(fset.contains("item33"));
            FastSet<String> fset2 = fset.copy();
            fset2.clear();
            fset2.add("item24");
            fset.and(fset2);
            System.err.println(fset.size());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
