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

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * @author Timo Vesalainen
 */
public class SAXParserFactoryImpl extends SAXParserFactory
{

    private SAXFeatures features = new SAXFeatures();
    
    @Override
    public SAXParser newSAXParser() throws ParserConfigurationException, SAXException
    {
        try
        {
            XMLReaderFactory reader = XMLReaderFactory.getInstance();
            SAXFeatures f = features.clone();
            f.setNamespaceAware(isNamespaceAware());
            f.setValidating(isValidating());
            //f.setXIncludeAware(isXIncludeAware());
            reader.setFeatures(f.clone());
            return new SAXParserImpl(reader);
        }

        catch (CloneNotSupportedException | IOException ex)
        {
            throw new ParserConfigurationException(ex.getMessage());
        }
    }
    
    @Override
    public void setFeature(String name, boolean value) throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException
    {
        features.setFeature(name, value);
    }
    
    @Override
    public boolean getFeature(String name) throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException
    {
        return features.getFeature(name);
    }
}
