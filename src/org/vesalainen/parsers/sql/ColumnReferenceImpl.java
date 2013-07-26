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

import java.util.Collection;
import java.util.List;
import org.vesalainen.parsers.sql.util.ArrayMap;
import java.util.Objects;

/**
 * @author Timo Vesalainen
 */
public class ColumnReferenceImpl<R,C> extends ParserLocator2Impl implements ColumnReference<R,C>
{
    protected Table<R, C> table;
    protected String tablePart;
    protected String column;
    protected List<String> raw;
    private String title;

    public ColumnReferenceImpl(List<String> raw)
    {
        this.raw = raw;
    }

    public ColumnReferenceImpl(List<String> raw, String title)
    {
        this.raw = raw;
        this.title = title;
    }

    ColumnReferenceImpl(Table<R, C> table, String name)
    {
        this.table = table;
        this.tablePart = table.getName();
        this.column = name;
        this.title = name;
    }

    @Override
    public boolean resolvTable(Collection<Table<R, C>> tables)
    {
        for (Table<R, C> table : tables)
        {
            if (this.table != null)
            {
                return true;
            }
            String first = raw.get(0);
            if (first.equals(table.getCorrelationName()))
            {
                this.table = table;
                this.tablePart = first;
                setColumn(toString(raw.subList(1, raw.size())));
                raw = null;
                return true;
            }
            else
            {
                if (first.equals(table.getSchema()))
                {
                    this.table = table;
                    this.tablePart = toString(raw.subList(1, 2));
                    setColumn(toString(raw.subList(2, raw.size())));
                    raw = null;
                    return true;
                }
                else
                {
                    if (first.equals(table.getName()))
                    {
                        this.table = table;
                        this.tablePart = first;
                        setColumn(toString(raw.subList(1, raw.size())));
                        raw = null;
                        return true;
                    }
                }
            }
        }
        if (tables.size() == 1 && raw.size() == 1)
        {
            this.table = tables.iterator().next();
            this.tablePart = table.getName();
            setColumn(raw.get(0));
            raw = null;
            return true;
        }
        throwException("table not found");
        return false;
    }

    private void setColumn(String column)
    {
        this.column = column;
        if (this.title == null)
        {
            this.title = column;
        }
    }

    private String toString(List<String> sub)
    {
        StringBuilder sb = new StringBuilder();
        for (String str : sub)
        {
            if (sb.length() > 0)
            {
                sb.append('.');
            }
            sb.append(str);
        }
        return sb.toString();
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
        return tablePart;
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
        if (!Objects.equals(this.tablePart, other.tablePart))
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
        hash = 67 * hash + Objects.hashCode(this.tablePart);
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
    /**
     * Returns column reference as in select list.
     * @return 
     */
    @Override
    public String toString()
    {
        if (tablePart != null)
        {
            return tablePart + "." + column;
        }
        else
        {
            return column;
        }
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
        if (tablePart != null)
        {
            return start+tablePart.length()+1+column.length();
        }
        else
        {
            return start+column.length();
        }
    }

    @Override
    public String getTitle()
    {
        return title;
    }

    @Override
    public void setTitle(String title)
    {
        this.title = title;
    }

}
