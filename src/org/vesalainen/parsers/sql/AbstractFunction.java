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
import org.vesalainen.parsers.sql.util.ArrayMap;

/**
 * @author Timo Vesalainen
 */
public abstract class AbstractFunction<R,C> extends ParserLocator2Impl implements ColumnReference<R,C>
{
    private ColumnReference inner;

    public AbstractFunction(ColumnReference inner)
    {
        this.inner = inner;
    }

    @Override
    public boolean resolvTable(Collection<Table<R, C>> tables)
    {
        return inner.resolvTable(tables);
    }

    @Override
    public void associateCondition(Condition condition, boolean andPath)
    {
        inner.associateCondition(condition, andPath);
    }

    @Override
    public String getColumn()
    {
        return inner.getColumn();
    }

    @Override
    public String getCorrelation()
    {
        return inner.getCorrelation();
    }

    @Override
    public Table getTable()
    {
        return inner.getTable();
    }

    @Override
    public void setTable(Table table)
    {
        inner.setTable(table);
    }

    @Override
    public Object getValue(SQLConverter selector, Object row)
    {
        return function(inner.getValue(selector, row));
    }

    @Override
    public Object getValue(SQLConverter selector, ArrayMap rowCandidate)
    {
        return function(inner.getValue(selector, rowCandidate));
    }
    /**
     * Implements the specified function
     * @param value
     * @return Returns the modified value 
     */
    public abstract Object function(Object value);

    @Override
    public String toString()
    {
        return inner.toString();
    }
    
    
}
