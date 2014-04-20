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
package org.vesalainen.parsers.xml.model;

import java.io.Writer;
import java.nio.charset.Charset;

/**
 * @author Timo Vesalainen
 */
public class Document<R extends Element>
{
    private String version;
    private Charset encoding;
    private R root;

    public static String getPackagename(String systemId)
    {
        //String identifier = MethodWrapper.makeJavaIdentifier(systemId);
        //return "org.vesalainen.parsers.xml."+identifier;
        throw new UnsupportedOperationException();
    }
    public void setRoot(R element)
    {
        root = element;
    }
    public Element getRoot()
    {
        return root;
    }

    public void setEncoding(Charset encoding)
    {
        this.encoding = encoding;
    }
    
    public void write(Writer writer)
    {
        //root.write(writer);
    }

}
