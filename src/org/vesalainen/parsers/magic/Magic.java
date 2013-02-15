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

import org.vesalainen.regex.ant.MapParser;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.vesalainen.parser.GenClassFactory;
import org.vesalainen.parser.annotation.GenClassname;
import org.vesalainen.parser.annotation.MapDef;
import org.vesalainen.parser.util.InputReader;

/**
 * @author Timo Vesalainen
 */
@GenClassname("org.vesalainen.parsers.magic.MagicImpl")
@MapDef(mapClass=MagicMap.class)
public abstract class Magic implements MapParser
{
    static final String ERROR = "Error";
    static final String EOF = "Eof";
    static final String UNKNOWN = ":unknown";
    private static final MimeMap mimeMap = new MimeMap();
    
    public MagicResult guess(byte[] bytes)
    {
        if (bytes != null && bytes.length > 0)
        {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            InputReader reader = new InputReader(bais, 64, "US-ASCII");
            String result = input(reader);
            return getResult(result);
        }
        else
        {
            return getResult(UNKNOWN);
        }
    }
    public MagicResult guess(InputStream is) throws IOException
    {
        try (InputReader reader = new InputReader(is, 64, "US-ASCII"))
        {
            String result = input(reader);
            return getResult(result);
        }
    }
    public MagicResult guess(File file) throws IOException
    {
        try (InputReader reader = new InputReader(file, 64, "US-ASCII"))
        {
            String result = input(reader);
            return getResult(result);
        }
    }
    /**
     * Returns mime type for extension or null if no match
     * @param extension
     * @return 
     */
    public static String getMimeType(String extension)
    {
        return mimeMap.get(extension);
    }
    
    public static Magic getInstance()
    {
        Magic magic = (Magic) GenClassFactory.getGenInstance(Magic.class);
        if (magic == null)
        {
            throw new NullPointerException();
        }
        return magic;
    }

    private MagicResult getResult(String result)
    {
        switch (result)
        {
            case "Error":
            case "Eof":
                return new MagicResult(UNKNOWN);
            default:
                return new MagicResult(result);
        }
    }
    public class MagicResult
    {
        private String[] extensions = new String[]{};
        private String description;

        private MagicResult(String str)
        {
            int idx = str.indexOf(':');
            if (idx > 0)
            {
                extensions = str.substring(0, idx).split("[ ,]+");
            }
            description = str.substring(idx+1);
        }

        public String[] getExtensions()
        {
            return extensions;
        }

        public String getDescription()
        {
            return description;
        }

        @Override
        public String toString()
        {
            return "MagicResult{" + description + '}';
        }
        
    }
}
