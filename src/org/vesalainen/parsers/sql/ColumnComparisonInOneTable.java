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
public class ColumnComparisonInOneTable<R,C> extends BaseComparison<R, C> implements ColumnComparisonCondition<R,C> 
{
    private ColumnReference<R,C> columnReference2;

    public ColumnComparisonInOneTable(ColumnReference<R, C> columnReference, Relation relation, ColumnReference<R, C> columnReference2)
    {
        super(columnReference, relation);
        this.columnReference2 = columnReference2;
    }
    
    @Override
    public void associateCondition(SelectStatement select, boolean andPath)
    {
        columnReference.associateCondition(this, andPath);
        columnReference2.associateCondition(this, andPath);
    }

    @Override
    public TruthValue matches(SQLConverter<R, C> selector, R row)
    {
        C col1 = columnReference.getValue(selector, row);
        C col2 = columnReference2.getValue(selector, row);
        if (col1 == null || col2 == null)
        {
            return TruthValue.UNKNOWN;
        }
        if (matches(selector, col1, col2))
        {
            return TruthValue.TRUE;
        }
        else
        {
            return TruthValue.FALSE;
        }
    }

    @Override
    public TruthValue matches(SQLConverter<R,C> selector, ArrayMap<Table<R,C>,R> rowCandidate)
    {
        C col1 = columnReference.getValue(selector, rowCandidate);
        C col2 = columnReference2.getValue(selector, rowCandidate);
        if (col1 == null || col2 == null)
        {
            return TruthValue.UNKNOWN;
        }
        if (matches(selector, col1, col2))
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
    public void walk(ConditionVisitor visitor, boolean andPath)
    {
        visitor.visit(this, andPath);
    }

    @Override
    public ColumnReference<R, C> getColumnReference()
    {
        return columnReference;
    }

}
