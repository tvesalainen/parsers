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

import javax.xml.parsers.SAXParser;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

/**
 * @author Timo Vesalainen
 */
public class SAXParserImpl extends SAXParser implements SAX2Constants
{
    private XMLReader xmlReader;

    SAXParserImpl(XMLReader xmlReader)
    {
        this.xmlReader = xmlReader;
    }
    
    @Override
    public Parser getParser() throws SAXException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XMLReader getXMLReader() throws SAXException
    {
        return xmlReader;
    }

    @Override
    public boolean isNamespaceAware()
    {
        try
        {
            return xmlReader.getFeature(FEATURE_NAMES_SPACE);
        }

        catch (SAXNotRecognizedException | SAXNotSupportedException ex)
        {
            return false;
        }
    }

    @Override
    public boolean isValidating()
    {
        try
        {
            return xmlReader.getFeature(FEATURE_VALIDATION);
        }

        catch (SAXNotRecognizedException | SAXNotSupportedException ex)
        {
            return false;
        }
    }

    @Override
    public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException
    {
        xmlReader.setProperty(name, value);
    }

    @Override
    public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException
    {
        return xmlReader.getProperty(name);
    }
    
}
