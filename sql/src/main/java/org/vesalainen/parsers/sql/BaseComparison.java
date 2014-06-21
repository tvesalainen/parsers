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


/**
 * @author Timo Vesalainen
 */
public abstract class BaseComparison<R, C> extends ParserLocator2Impl implements Condition<R, C>
{

    protected ColumnReference<R,C> columnReference;
    protected Relation relation;

    public BaseComparison(ColumnReference<R, C> columnReference, Relation relation)
    {
        this.columnReference = columnReference;
        this.relation = relation;
    }

    public Relation getRelation()
    {
        return relation;
    }

    public boolean matches(SQLConverter<R,C> selector, C col1, C col2)
    {
        int cmp = selector.getComparator().compare(col1, col2);
        return matches(cmp, relation);
    }

    public static boolean matches(int cmp, Relation relation)
    {
        switch (relation)
        {
            case EQ:
                return cmp == 0;
            case NE:
                return cmp != 0;
            case LT:
                return cmp < 0;
            case GT:
                return cmp > 0;
            case LE:
                return cmp <= 0;
            case GE:
                return cmp >= 0;
            default:
                throw new UnsupportedOperationException("unknown relation "+relation);
        }
    }

    public String getColumn()
    {
        return columnReference.getColumn();
    }

    public ColumnReference<R, C> getColumnReference()
    {
        return columnReference;
    }

}
