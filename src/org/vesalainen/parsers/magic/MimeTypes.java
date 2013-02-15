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

import java.io.File;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;
import org.vesalainen.parser.GenClassFactory;
import org.vesalainen.parser.annotation.GenClassname;
import org.vesalainen.parser.annotation.MapDef;
import org.vesalainen.parser.util.InputReader;
import org.vesalainen.parsers.magic.Magic.MagicResult;
import org.vesalainen.regex.ant.MapParser;

/**
 * Note! This MimeTypes class is just an example of using DFAMaps. Using 
 * HashMap implementation is faster.
 * kind of 
 * @author Timo Vesalainen
 */
@GenClassname("org.vesalainen.parsers.magic.MimeTypesImpl")
@MapDef(mapClass=MimeMap.class)
public abstract class MimeTypes implements MapParser
{
    static final String ERROR = "Error";
    static final String EOF = "Eof";
    
    private static Magic magic;
    private InputReader reader = new InputReader("");
    private ReentrantLock lock = new ReentrantLock();

    public MimeTypes()
    {
        magic = Magic.getInstance();
    }
    
    public String getType(String extension)
    {
        lock.lock();
        try
        {
            reader.reuse(extension.toLowerCase());
            return input(reader);
        }
        finally
        {
            lock.unlock();
        }
    }
    public String getType(File file) throws IOException
    {
        String filename = file.getName();
        int idx = filename.lastIndexOf('.');
        if (idx != -1)
        {
            String ext = filename.substring(idx+1);
            String mimeType = getType(ext);
            if (mimeType != null)
            {
                return mimeType;
            }
            else
            {
                MagicResult guess = magic.guess(file);
                for (String extension : guess.getExtensions())
                {
                    mimeType = getType(extension);
                    if (mimeType != null)
                    {
                        return mimeType;
                    }
                }
            }
        }
        return "application/octet-stream";
    }
    public static MimeTypes getInstance()
    {
        MimeTypes mimeTypes = (MimeTypes) GenClassFactory.loadGenInstance(MimeTypes.class);
        if (mimeTypes == null)
        {
            throw new NullPointerException();
        }
        return mimeTypes;
    }

}
