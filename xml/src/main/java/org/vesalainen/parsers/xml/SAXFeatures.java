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

import static org.vesalainen.parsers.xml.SAX2Constants.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * @author Timo Vesalainen
 * @see <a href="http://www.saxproject.org/apidoc/org/xml/sax/package-summary.html#package_description" />The complete list of standard SAX2 features and properties</a>
 */
public class SAXFeatures implements Cloneable
{
    private boolean isNamespaceAware = false;
    private boolean supportNameSpacePrefixes = false;
    private boolean isValidating = false;
    private boolean isXIncludeAware = false;
    private boolean isStandalone;

    @Override
    public SAXFeatures clone() throws CloneNotSupportedException
    {
        return (SAXFeatures) super.clone();
    }

    
    public void setNamespaceAware(boolean awareness) throws SAXException
    {
        isNamespaceAware = awareness;
    }
    public void setValidating(boolean validating)
    {
        isValidating = validating;
    }
    public void setXIncludeAware(boolean state)
    {
        isXIncludeAware = state;
    }

    public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException
    {
        if (FEATURE_NAMES_SPACE.equals(name))
        {
            return isNamespaceAware;
        }
        if (FEATURE_NAMES_SPACE_PREFIXES.equals(name))
        {
            return supportNameSpacePrefixes;
        }
        if (FEATURE_VALIDATION.equals(name))
        {
            return isValidating;
        }
        if (FEATURE_ATTRIBUTES2.equals(name))
        {
            return true;
        }
        if (FEATURE_ENTITY_RESOLVER2.equals(name))
        {
            return true;
        }
        if (FEATURE_LOCATOR2.equals(name))
        {
            return true;
        }
        if (FEATURE_XML_1_1.equals(name))
        {
            return true;
        }
        if (FEATURE_IS_STANDALONE.equals(name))
        {
            return isStandalone;
        }
        throw new SAXNotRecognizedException(name);
    }

    public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException
    {
        if (FEATURE_NAMES_SPACE.equals(name))
        {
            isNamespaceAware = value;
            return;
        }
        if (FEATURE_NAMES_SPACE_PREFIXES.equals(name))
        {
            supportNameSpacePrefixes = value;
            return;
        }
        if (FEATURE_VALIDATION.equals(name))
        {
            isValidating = value;
            return;
        }
        if (FEATURE_ENTITY_RESOLVER2.equals(name))
        {
            throw new SAXNotSupportedException(name);
        }
        if (FEATURE_ATTRIBUTES2.equals(name))
        {
            throw new SAXNotSupportedException(name);
        }
        if (FEATURE_LOCATOR2.equals(name))
        {
            throw new SAXNotSupportedException(name);
        }
        if (FEATURE_XML_1_1.equals(name))
        {
            throw new SAXNotSupportedException(name);
        }
        if (FEATURE_IS_STANDALONE.equals(name))
        {
            throw new SAXNotSupportedException(name);
        }
        throw new SAXNotRecognizedException(name);
    }

}
