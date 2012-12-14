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
import java.util.Iterator;
import java.util.LinkedHashMap;
import org.vesalainen.parsers.sql.util.ArrayMap;

/**
 * @author Timo Vesalainen
 */
public abstract class WhereStatement<R, C> extends Statement<R, C> implements ConditionVisitor 
{
    protected Table<R,C> table;
    protected Condition<R,C> condition;

    public WhereStatement(Engine<R, C> engine, LinkedHashMap<String, Placeholder> placeholderMap, Table<R,C> table, Condition<R,C> condition)
    {
        super(engine, placeholderMap);
        this.table = table;
        this.condition = condition;
        walk();
    }

    public WhereStatement(Engine<R, C> engine, LinkedHashMap<String, Placeholder> placeholderMap, Table<R,C> table)
    {
        super(engine, placeholderMap);
        this.table = table;
    }

    private void walk()
    {
        condition.walk(this, true);
    }
    
    protected Collection<R> getTarget()
    {
        Collection<R> rows = engine.fetch(table);
        ArrayMap<Table<R,C>,R> rowCandidate = new ArrayMap<>(table);
        int index = rowCandidate.getIndexOf(table);
        Iterator<R> iterator = rows.iterator();
        while (iterator.hasNext())
        {
            R row = iterator.next();
            rowCandidate.put(index, row);
            if (condition != null && condition.matches(engine, rowCandidate) != TruthValue.TRUE)
            {
                iterator.remove();
            }
        }
        return rows;
    }
    
    public Condition getCondition()
    {
        return condition;
    }

    public Table getTable()
    {
        return table;
    }

    @Override
    public void visit(Condition condition, boolean andPath)
    {
        if (condition instanceof ColumnCondition)
        {
            ColumnCondition cc = (ColumnCondition) condition;
            table.associateCondition(cc, andPath);
        }
    }

}
