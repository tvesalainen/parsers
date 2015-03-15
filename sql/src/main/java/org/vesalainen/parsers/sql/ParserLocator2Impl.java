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

import org.vesalainen.parser.util.OffsetLocatorException;


/**
 * @author Timo Vesalainen
 */
public class ParserLocator2Impl implements ParserLocator2
{
    private String source;
    private int start;
    private int end;

    @Override
    public void throwException(String message)
    {
        throw new OffsetLocatorException(message, source, start, end);
    }

    @Override
    public void setLocation(String source, int start, int end)
    {
        this.source = source;
        this.start = start;
        this.end = end;
    }

    @Override
    public int getEnd()
    {
        return end;
    }

    @Override
    public String getSource()
    {
        return source;
    }

    @Override
    public int getStart()
    {
        return start;
    }

}
