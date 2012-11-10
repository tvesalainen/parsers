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

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.vesalainen.parsers.sql.util.ArrayMap;

/**
 * @author Timo Vesalainen
 */
public class FetchResult<R, C> implements Iterable<C[]>
{

    protected Engine<R, C> engine;
    protected String[] header;
    protected int[] columnLength;
    protected List<C[]> data;
    protected int length;

    public FetchResult(Engine<R, C> selector, String... header)
    {
        this.engine = selector;
        this.header = header;
        data = new ArrayList<>();
        columnLength = new int[header.length];
        int index = 0;
        for (String h : header)
        {
            columnLength[index++] = h.length();
        }
    }

    public String[] getHeader()
    {
        return header;
    }

    public void addRow(C... row)
    {
        data.add(row);
        int len = Math.min(columnLength.length, row.length);
        for (int ii=0;ii<len;ii++)
        {
            columnLength[ii] = Math.max(columnLength[ii], row[ii].toString().length());
        }
    }
    
    @Override
    public Iterator<C[]> iterator()
    {
        return data.iterator();
    }

    public void print(PrintStream out)
    {
        int index = 0;
        for (String h : header)
        {
            out.printf("%" + columnLength[index++] + "s ", h);
        }
        out.println();
        for (C[] ar : this)
        {
            index = 0;
            for (C col : ar)
            {
                out.printf("%" + columnLength[index++] + "s ", col);
            }
            out.println();
        }
    }

    public int getRowCount()
    {
        return data.size();
    }
    
    public int getColumnCount()
    {
        return header.length;
    }
    
    public C getValueAt(int row, int column)
    {
        return data.get(row)[column];
    }
    
    public String getColumnName(int columnIndex)
    {
        return header[columnIndex];
    }
}
