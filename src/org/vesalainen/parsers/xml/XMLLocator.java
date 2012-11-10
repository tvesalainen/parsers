/*
 * Copyright (C) 2011 Timo Vesalainen
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
package org.vesalainen.parsers.xml;

import org.vesalainen.parser.util.InputReader;
import org.xml.sax.ext.Locator2;

/**
 * @author Timo Vesalainen
 */
public class XMLLocator implements Locator2
{
    private InputReader inputReader;
    private String xmlVersion;
    private String publicId;

    public XMLLocator(InputReader inputReader)
    {
        this.inputReader = inputReader;
    }

    public void setPublicId(String publicId)
    {
        this.publicId = publicId;
    }

    public void setXmlVersion(String xmlVersion)
    {
        this.xmlVersion = xmlVersion;
    }

    public String getPublicId()
    {
        return publicId;
    }

    public String getXMLVersion()
    {
        return xmlVersion;
    }
    
    @Override
    public String getEncoding()
    {
        return inputReader.getEncoding();
    }

    @Override
    public String getSystemId()
    {
        return inputReader.getSource();
    }

    @Override
    public int getLineNumber()
    {
        return inputReader.getLineNumber();
    }

    @Override
    public int getColumnNumber()
    {
        return inputReader.getColumnNumber();
    }
    
}
