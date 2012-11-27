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

package org.vesalainen.parsers.magic;

import java.util.concurrent.locks.ReentrantLock;
import org.vesalainen.parser.ParserFactory;
import org.vesalainen.parser.annotation.GenClassname;
import org.vesalainen.parser.util.InputReader;
import org.vesalainen.regex.ant.MapParser;

/**
 * @author Timo Vesalainen
 */
@GenClassname("org.vesalainen.parsers.magic.MimeTypesImpl")
public abstract class MimeTypes implements MapParser
{
    static final String ERROR = "Error";
    static final String EOF = "Eof";
    
    private InputReader reader = new InputReader("");
    private ReentrantLock lock = new ReentrantLock();
    
    public String getType(String extension)
    {
        lock.lock();
        try
        {
            reader.reuse(extension);
            return input(reader);
        }
        finally
        {
            lock.unlock();
        }
    }
    public static MimeTypes getInstance()
    {
        MimeTypes mimeTypes = (MimeTypes) ParserFactory.loadParserInstance(MimeTypes.class);
        if (mimeTypes == null)
        {
            throw new NullPointerException();
        }
        return mimeTypes;
    }

}
