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
public class BooleanTestCondition extends ParserLocator2Impl implements Condition
{
    private Condition booleanPrimary;
    private boolean is;
    private TruthValue truthValue;

    public BooleanTestCondition(Condition booleanPrimary, boolean is, TruthValue truthValue)
    {
        this.booleanPrimary = booleanPrimary;
        this.is = is;
        this.truthValue = truthValue;
    }
    
    @Override
    public void associateCondition(SelectStatement select, boolean andPath)
    {
    }

    @Override
    public TruthValue matches(SQLConverter selector, ArrayMap rowCandidate)
    {
        TruthValue matches = booleanPrimary.matches(selector, rowCandidate);
        return compare(matches);
    }

    private TruthValue compare(TruthValue matches)
    {
        if (is)
        {
            if (truthValue.equals(matches))
            {
                return TruthValue.TRUE;
            }
            else
            {
                return TruthValue.FALSE;
            }
        }
        else
        {
            if (truthValue.equals(matches))
            {
                return TruthValue.FALSE;
            }
            else
            {
                return TruthValue.TRUE;
            }
        }
    }

    @Override
    public void walk(ConditionVisitor visitor, boolean andPath)
    {
        visitor.visit(this, andPath);
        booleanPrimary.walk(visitor, false);
    }
}
