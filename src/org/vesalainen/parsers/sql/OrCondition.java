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

import java.util.Objects;
import org.vesalainen.parsers.sql.util.ArrayMap;

/**
 * @author Timo Vesalainen
 */
public class OrCondition<R,C> extends ParserLocator2Impl implements Condition<R,C> 
{
    private Condition condition1;
    private Condition condition2;

    public OrCondition(Condition condition1, Condition condition2)
    {
        this.condition1 = condition1;
        this.condition2 = condition2;
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
        final OrCondition<R, C> other = (OrCondition<R, C>) obj;
        if (!Objects.equals(this.condition1, other.condition1))
        {
            return false;
        }
        if (!Objects.equals(this.condition2, other.condition2))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.condition1);
        hash = 97 * hash + Objects.hashCode(this.condition2);
        return hash;
    }

    @Override
    public void associateCondition(SelectStatement select, boolean andPath)
    {
        condition1.associateCondition(select, false);
        condition2.associateCondition(select, false);
    }

    @Override
    public TruthValue matches(SQLConverter<R,C> selector, ArrayMap<Table<R,C>,R> rowCandidate)
    {
        TruthValue p = condition1.matches(selector, rowCandidate);
        if (TruthValue.TRUE.equals(p))
        {
            return TruthValue.TRUE;
        }
        TruthValue q = condition2.matches(selector, rowCandidate);
        return truthTable(p, q);
    }

    public static TruthValue truthTable(TruthValue p, TruthValue q)
    {
        switch (p)
        {
            case FALSE:
                switch (q)
                {
                    case TRUE:
                        return TruthValue.TRUE;
                    case FALSE:
                        return TruthValue.FALSE;
                    case UNKNOWN:
                        return TruthValue.UNKNOWN;
                }
            case UNKNOWN:
                switch (q)
                {
                    case TRUE:
                        return TruthValue.TRUE;
                    case FALSE:
                        return TruthValue.UNKNOWN;
                    case UNKNOWN:
                        return TruthValue.UNKNOWN;
                }
        }
        throw new IllegalArgumentException();
    }
    @Override
    public void walk(ConditionVisitor visitor, boolean andPath)
    {
        visitor.visit(this, andPath);
        condition1.walk(visitor, false);
        condition2.walk(visitor, false);
    }
}
