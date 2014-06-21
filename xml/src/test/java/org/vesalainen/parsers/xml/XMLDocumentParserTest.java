/*
 * Copyright (C) 2014 Timo Vesalainen
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

import java.io.InputStream;
import static org.junit.Assert.*;
import org.junit.Test;
import org.vesalainen.parser.util.InputReader;
import org.xml.sax.InputSource;

/**
 *
 * @author Timo Vesalainen
 */
public class XMLDocumentParserTest
{
    
    public XMLDocumentParserTest()
    {
    }

    /**
     * Test of parse method, of class XMLDocumentParser.
     */
    @Test
    public void testParse_InputSource() throws Exception
    {
        XMLDocumentParser parser = XMLDocumentParser.getInstance();
        InputStream ras = XMLDocumentParserTest.class.getClassLoader().getResourceAsStream("xhtml1-strict.xsd");
        InputSource is = new InputSource(ras);
        parser.parse(is);
    }

}