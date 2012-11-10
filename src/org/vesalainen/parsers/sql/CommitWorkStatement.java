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
 * @author Timo Vesalainen
 */
public class CommitWorkStatement<R,C,T> extends Statement<R,C>
{

    public CommitWorkStatement(Engine<R, C> engine, Map<String, Placeholder> placeholderMap)
    {
        super(engine, placeholderMap);
    }

    @Override
    public FetchResult execute()
    {
        engine.commitTransaction();
        return null;
    }

    @Override
    public void check(Metadata metadata, ErrorReporter reporter)
    {
    }

}
