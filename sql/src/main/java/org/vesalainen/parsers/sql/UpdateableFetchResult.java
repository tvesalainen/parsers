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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.vesalainen.parsers.sql.util.ArrayMap;

/**
 * @author Timo Vesalainen
 */
public class UpdateableFetchResult<R,C> extends OrderedFetchResult<R,C>
{
    protected List<Updateable<R,C>[]> updateable;
    protected Set<R> updated = new HashSet<>();
    protected Set<R> deleted = new HashSet<>();
    
    public UpdateableFetchResult(Engine<R, C> engine, SelectStatement<R,C> select)
    {
        super(engine, select);
        updateable = new ArrayList<>();
    }
    
    public void addRow(ArrayMap<Table<R, C>, R> rowCandidate)
    {
        Updateable<R,C>[] row = (Updateable<R,C>[]) new Updateable[length];
        updateable.add(row);
        int index = 0;
        for (ColumnReference<R,C> cf : columnReferences)
        {
            Updateable<R,C> col = engine.getUpdateable(rowCandidate.get(cf.getTable()), cf.getColumn(), cf.getValue(engine, rowCandidate));
            if (col != null && col.getValue() != null)
            {
                columnLength[index] = Math.max(columnLength[index], col.getValue().toString().length());
            }
            row[index++] = col;
        }
    }
    
    @Override
    public Iterator<C[]> iterator()
    {
        if (!sorted && sortSpecification != null)
        {
            ArrayComparator comparator = new ArrayComparator(engine.getComparator(), columnReferences, sortSpecification);
            Collections.sort(updateable, comparator);
            sorted = true;
        }
        return new DataIterator();
    }

    @Override
    public int getRowCount()
    {
        return updateable.size();
    }

    @Override
    public C getValueAt(int row, int column)
    {
        return updateable.get(row)[column].getValue();
    }

    @Override
    public C getValueAt(int row, String column)
    {
        Integer col = columnMap.get(column);
        if (col == null)
        {
            throw new IllegalArgumentException(column+" not found");
        }
        return updateable.get(row)[col].getValue();
    }
    
    public void setValueAt(C value, int row, int column)
    {
        Updateable<R,C> u = updateable.get(row)[column];
        R updatedRow = u.setValue(value);
        if (updatedRow != null)
        {
            updated.add(updatedRow);
        }
    }

    public void deleteRow(int row)
    {
        for (Updateable<R,C> u : updateable.get(row))
        {
            R r = u.getRow();
            deleted.add(r);
            updated.remove(r);
        }
        updateable.remove(row);
    }
    
    public void insertRow()
    {
        
    }

    public void rollback()
    {
        engine.rollbackTransaction();
    }
    
    public void updateAndCommit()
    {
        engine.delete(deleted);
        engine.update(updated);
        engine.commitTransaction();
    }
    
    public class DataIterator implements Iterator<C[]>
    {
        private Iterator<Updateable<R,C>[]> iterator;
        private C[] next = (C[]) new Object[header.length];
        
        @Override
        public boolean hasNext()
        {
            return iterator.hasNext();
        }

        @Override
        public C[] next()
        {
            Updateable<R, C>[] nextUpd = iterator.next();
            for (int ii=0;ii<next.length;ii++)
            {
                next[ii] = nextUpd[ii].getValue();
            }
            return next;
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
    private class ArrayComparator implements Comparator<Updateable<R,C>[]>
    {
        private Comparator<C> comp;
        private int[] cols;
        private int[] signs;

        public ArrayComparator(Comparator<C> comp, List<ColumnReference<R,C>> columnReferences, List<SortSpecification> sortSpecification)
        {
            this.comp = comp;
            cols = new int[sortSpecification.size()];
            signs = new int[sortSpecification.size()];
            int index = 0;
            for (SortSpecification ss : sortSpecification)
            {
                cols[index] = columnReferences.indexOf(ss.getRv());
                if (ss.isAscending())
                {
                    signs[index] = 1;
                }
                else
                {
                    signs[index] = -1;
                }
                index++;
            }
        }
        
        @Override
        public int compare(Updateable<R,C>[] o1, Updateable<R,C>[] o2)
        {
            int cmp = 0;
            for (int ii=0;ii<cols.length;ii++)
            {
                cmp = signs[ii]*comp.compare(o1[cols[ii]].getValue(), o2[cols[ii]].getValue());
                if (cmp != 0)
                {
                    break;
                }
            }
            return cmp;
        }

    }
}
