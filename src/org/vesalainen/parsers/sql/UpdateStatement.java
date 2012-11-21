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
import java.util.List;
import java.util.Map;
import org.vesalainen.parsers.sql.util.ArrayMap;

/**
 * @author Timo Vesalainen
 */
public class UpdateStatement<R,C> extends WhereStatement<R,C>
{
    private List<SetClause<R,C>> setClauseList;

    public UpdateStatement(Engine<R, C> engine, LinkedHashMap<String, Placeholder> placeholderMap, Table<R,C> table, List<SetClause<R,C>> setClauseList)
    {
        super(engine, placeholderMap, table);
        this.setClauseList = setClauseList;
    }

    public UpdateStatement(Engine<R, C> engine, LinkedHashMap<String, Placeholder> placeholderMap, Table<R,C> table, List<SetClause<R,C>> setClauseList, Condition<R,C> condition)
    {
        super(engine, placeholderMap, table, condition);
        this.setClauseList = setClauseList;
    }
    
    @Override
    public FetchResult<R, C> execute()
    {
        Collection<R> rows = getTarget();
        for (R row : rows)
        {
            for (SetClause<R,C> sc : setClauseList)
            {
                engine.set(row, sc.getColumn(), sc.getLiteral().getValue());
            }
        }
        engine.update(rows);
        return null;
    }
    @Override
    public void check(Metadata metadata, ErrorReporter reporter)
    {
    }

}
