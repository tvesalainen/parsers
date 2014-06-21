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

import java.util.LinkedHashMap;
import org.vesalainen.parsers.sql.ErrorReporter.Level;

/**
 *
 * @author Timo Vesalainen
 */
public abstract class Statement<R,C> extends ParserLocator2Impl
{
    protected Engine<R,C> engine;
    protected LinkedHashMap<String,Placeholder<R,C>> placeholderMap;

    public Statement(Engine<R, C> engine, LinkedHashMap<String, Placeholder<R,C>> placeholderMap)
    {
        this.engine = engine;
        this.placeholderMap = placeholderMap;
    }

    public LinkedHashMap<String, Placeholder<R,C>> getPlaceholderMap()
    {
        return placeholderMap;
    }

    public void bindValue(String name, C value)
    {
        Placeholder<R,C> ph = placeholderMap.get(name);
        if (ph == null)
        {
            throw new IllegalArgumentException("Placeholder "+name+" doesn't exist");
        }
        ph.bindValue(value);
    }
    public void check(Metadata metadata, ErrorReporter reporter)
    {
    }
    public abstract FetchResult<R,C> execute();

    public boolean isSelectStatement()
    {
        return false;
    }
    
}
