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

/**
 * @author Timo Vesalainen
 */
public class NullCondition<R,C> extends ParserLocator2Impl implements ColumnCondition<R,C> 
{
    private ColumnReference<R,C> columnReference;

    public NullCondition(ColumnReference<R, C> rv)
    {
        this.columnReference = rv;
    }

    @Override
    public void associateCondition(SelectStatement select, boolean andPath)
    {
    }

    @Override
    public TruthValue matches(SQLConverter<R, C> selector, R row)
    {
        C col = columnReference.getValue(selector, row);
        if (col == null)
        {
            return TruthValue.TRUE;
        }
        else
        {
            return TruthValue.FALSE;
        }
    }

    @Override
    public TruthValue matches(SQLConverter<R, C> selector, ArrayMap<Table<R,C>, R> rowCandidate)
    {
        C col = columnReference.getValue(selector, rowCandidate);
        if (col == null)
        {
            return TruthValue.TRUE;
        }
        else
        {
            return TruthValue.FALSE;
        }
    }

    @Override
    public void narrow(SQLConverter<R, C> selector, Range<C> range)
    {
    }

    @Override
    public String getColumn()
    {
        return columnReference.getColumn();
    }

    @Override
    public ColumnReference<R, C> getColumnReference()
    {
        return columnReference;
    }

    @Override
    public void walk(ConditionVisitor visitor, boolean andPath)
    {
        visitor.visit(this, andPath);
    }

}
