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

import java.util.Map;

/**
 *
 * @author Timo Vesalainen
 */
public abstract class Statement<R,C>
{
    protected Engine<R,C> engine;
    protected Map<String,Placeholder> placeholderMap;

    public Statement(Engine<R, C> engine, Map<String, Placeholder> placeholderMap)
    {
        this.engine = engine;
        this.placeholderMap = placeholderMap;
    }

    public Map<String, Placeholder> getPlaceholderMap()
    {
        return placeholderMap;
    }

    public void bindValue(String name, C value)
    {
        Placeholder ph = placeholderMap.get(name);
        if (ph == null)
        {
            throw new IllegalArgumentException("Placeholder "+name+" doesn't exist");
        }
        ph.bindValue(value);
    }
    public void check(Metadata metadata, ErrorReporter reporter)
    {
        for (Placeholder ph : placeholderMap.values())
        {
            if (!ph.isBound())
            {
                reporter.report("Unbound placeholder "+ph.getName(), ErrorReporter.Level.Fatal, ph.getSource(), ph.getStart(), ph.getEnd());
            }
        }
    }
    public abstract FetchResult<R,C> execute();
    
}
