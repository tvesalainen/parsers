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

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Timo Vesalainen
 */
public class InsertStatement<R,C> extends Statement<R,C> 
{
    private Table<R, C> table;
    private Map<String,Literal<R,C>> valueMap = new HashMap<>();

    public InsertStatement(Engine<R, C> engine, LinkedHashMap<String, Placeholder> placeholderMap, Table<R, C> table, InsertColumnsAndSource<R,C> insertColumnsAndSource)
    {
        super(engine, placeholderMap);
        this.table = table;
        Iterator<String> columns = insertColumnsAndSource.getColumnList().iterator();
        Iterator<Literal<R,C>> values = insertColumnsAndSource.getValueList().iterator();
        while (columns.hasNext())
        {
            valueMap.put(columns.next(), values.next());
        }
        
    }
    @Override
    public FetchResult<R, C> execute()
    {
        engine.insert(this);
        return null;
    }

    public Map<String, Literal<R, C>> getValueMap()
    {
        return valueMap;
    }

    public Table<R, C> getTable()
    {
        return table;
    }

    @Override
    public void check(Metadata metadata, ErrorReporter reporter)
    {
    }

}
