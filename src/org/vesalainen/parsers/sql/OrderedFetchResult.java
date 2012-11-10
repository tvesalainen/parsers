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

import org.vesalainen.parsers.sql.util.ArrayMap;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * @author Timo Vesalainen
 */
public class OrderedFetchResult<R,C> extends FetchResult<R,C>
{
    protected List<ColumnReference> columnReferences;
    protected boolean sorted;
    protected final List<SortSpecification> sortSpecification;

    public OrderedFetchResult(Engine<R,C> engine, SelectStatement<R,C> select)
    {
        super(engine, select.getHeader());
        data = new ArrayList<>();
        this.columnReferences = select.getReferencedColumns();
        length = columnReferences.size();
        sortSpecification = select.getSortSpecification();
    }
    
    public void addRow(ArrayMap<Table, R> rowCandidate)
    {
        C[] row = (C[]) new Object[length];
        data.add(row);
        int index = 0;
        for (ColumnReference cf : columnReferences)
        {
            C col = engine.get(rowCandidate.get(cf.getTable()), cf.getColumn());
            if (col != null)
            {
                columnLength[index] = Math.max(columnLength[index], col.toString().length());
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
            Collections.sort(data, comparator);
            sorted = true;
        }
        return data.iterator();
    }

    private class ArrayComparator implements Comparator<C[]>
    {
        private Comparator<C> comp;
        private int[] cols;
        private int[] signs;

        public ArrayComparator(Comparator<C> comp, List<ColumnReference> columnReferences, List<SortSpecification> sortSpecification)
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
        public int compare(C[] o1, C[] o2)
        {
            int cmp = 0;
            for (int ii=0;ii<cols.length;ii++)
            {
                cmp = signs[ii]*comp.compare(o1[cols[ii]], o2[cols[ii]]);
                if (cmp != 0)
                {
                    break;
                }
            }
            return cmp;
        }

    }
}
