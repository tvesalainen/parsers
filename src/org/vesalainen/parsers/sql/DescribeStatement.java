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

/**
 * @author Timo Vesalainen
 */
public class DescribeStatement<R,C> extends  Statement<R,C>
{
    private String tablename;

    public DescribeStatement(Engine<R, C> engine, LinkedHashMap<String, Placeholder> placeholderMap, String identifier)
    {
        super(engine, placeholderMap);
        this.tablename = identifier;
    }

    @Override
    public FetchResult<R, C> execute()
    {
        return engine.describe(tablename);
    }
    @Override
    public void check(Metadata metadata, ErrorReporter reporter)
    {
    }

}
