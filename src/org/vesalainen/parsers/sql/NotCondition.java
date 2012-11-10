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
class NotCondition<R,C> extends ParserLocator2Impl implements Condition<R,C> 
{
    private Condition test;
    
    public NotCondition(Condition test)
    {
        this.test = test;
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
        final NotCondition other = (NotCondition) obj;
        if (!Objects.equals(this.test, other.test))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.test);
        return hash;
    }

    @Override
    public void associateCondition(SelectStatement select, boolean andPath)
    {
        test.associateCondition(select, false);
    }

    @Override
    public TruthValue matches(SQLConverter<R,C> selector, ArrayMap<Table<R,C>,R> rowCandidate)
    {
        return truthTable(test.matches(selector, rowCandidate));
    }

    public static TruthValue truthTable(TruthValue p)
    {
        switch (p)
        {
            case TRUE:
                return TruthValue.FALSE;
            case FALSE:
                return TruthValue.TRUE;
            case UNKNOWN:
                return TruthValue.UNKNOWN;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public void walk(ConditionVisitor visitor, boolean andPath)
    {
        visitor.visit(this, andPath);
        test.walk(visitor, false);
    }
}
