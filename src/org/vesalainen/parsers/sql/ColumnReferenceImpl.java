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
import java.util.Objects;

/**
 * @author Timo Vesalainen
 */
public class ColumnReferenceImpl<R,C> extends ParserLocator2Impl implements ColumnReference<R,C>
{
    protected Table<R, C> table;
    protected String correlation;
    protected String column;

    public ColumnReferenceImpl(Table<R, C> table, String column)
    {
        this.table = table;
        this.column = column;
    }

    public ColumnReferenceImpl(Table<R, C> table, String correlation, String column)
    {
        this.table = table;
        this.correlation = correlation;
        this.column = column;
    }

    public Table<R, C> getTable()
    {
        return table;
    }

    public String getColumn()
    {
        return column;
    }

    public String getCorrelation()
    {
        return correlation;
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
        final ColumnReferenceImpl other = (ColumnReferenceImpl) obj;
        if (!Objects.equals(this.correlation, other.correlation))
        {
            return false;
        }
        if (!Objects.equals(this.column, other.column))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.correlation);
        hash = 67 * hash + Objects.hashCode(this.column);
        return hash;
    }

    @Override
    public void associateCondition(Condition<R, C> condition, boolean andPath)
    {
        if (condition instanceof ColumnCondition)
        {
            ColumnCondition<R, C> tc = (ColumnCondition) condition;
            table.associateCondition(tc, andPath);
        }
    }

    @Override
    public C getValue(SQLConverter<R, C> selector, R row)
    {
        assert row != null;
        return selector.get(row, column);
    }

    @Override
    public C getValue(SQLConverter<R,C> selector, ArrayMap<Table<R,C>,R> rowCandidate)
    {
        assert rowCandidate != null;
        R row = (R) rowCandidate.get(table);
        if (row != null)
        {
            return selector.get(row, column);
        }
        return null;
    }

    @Override
    public String toString()
    {
        return table + "." + column;
    }

    @Override
    public void setTable(Table<R, C> table)
    {
        this.table = table;
    }

    @Override
    public int getEnd()
    {
        int start = getStart();
        if (correlation != null)
        {
            return start+correlation.length()+1+column.length();
        }
        else
        {
            return start+column.length();
        }
    }

}
