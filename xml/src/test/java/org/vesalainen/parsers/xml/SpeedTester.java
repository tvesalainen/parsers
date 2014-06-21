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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

/**
 * @author Timo Vesalainen
 */
public class SpeedTester implements SAX2Constants
{
    public static XMLReader configure(boolean original) throws SAXException, ParserConfigurationException
    {
        boolean isNamespaceAware = true;
        boolean supportNameSpacePrefixes = true;
        boolean isValidating = false;
        boolean isXIncludeAware = false;

        SAXParserFactory factory = null;
        if (original)
        {
            factory = SAXParserFactory.newInstance();
        }
        else
        {
            factory = SAXParserFactoryImpl.newInstance("org.vesalainen.parsers.xml.SAXParserFactoryImpl", null);
        }
        factory.setNamespaceAware(isNamespaceAware);
        factory.setFeature(FEATURE_NAMES_SPACE_PREFIXES, supportNameSpacePrefixes);
        factory.setValidating(isValidating);
        factory.setXIncludeAware(isXIncludeAware);

        SAXParser parser = factory.newSAXParser();
        return parser.getXMLReader();

    }
    public static void test(XMLReader xmlReader, File out) throws FileNotFoundException, SAXNotRecognizedException, SAXNotSupportedException, IOException, SAXException
    {
        File f = new File ("C:\\Users\\tkv\\Documents\\NetBeansProjects\\KML\\src\\org\\vesalainen\\kml\\kml21.xsd");
        //File f = new File ("C:\\Users\\tkv\\Documents\\NetBeansProjects\\KML\\nbproject\\build-impl.xml");
        //File f = new File ("C:\\temp\\US-ASCII.xml");
        //File f = new File ("C:\\temp\\xhtml1-20020801\\DTD\\US-ASCII.xml");
        InputSource input = new InputSource(f.toURI().toString());
        try (PrintStream out1 = new PrintStream(out))
        {
            PrintingHandler hdlr1 = new PrintingHandler(out1);
            xmlReader.setContentHandler(hdlr1);
            xmlReader.setDTDHandler(hdlr1);
            xmlReader.setEntityResolver(hdlr1);
            xmlReader.setErrorHandler(hdlr1);
            xmlReader.setProperty(PROPERTY_LEXICAL_HANDLER, hdlr1);
            xmlReader.parse(input);
        }

    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)    
    {
        try
        {

            XMLReader reader1 = configure(true);
            File f1 = File.createTempFile("out1", "txt");
            long startOriginal = System.nanoTime();
            test(reader1, f1);
            long endOriginal = System.nanoTime();
            
            System.gc();
            
            reader1 = configure(true);
            f1 = File.createTempFile("out2", "txt");
            startOriginal = System.nanoTime();
            test(reader1, f1);
            endOriginal = System.nanoTime();
            
            System.gc();
            
            XMLReader reader2 = configure(false);
            File f2 = new File("c:\\temp\\out2.txt");
            long startMy = System.nanoTime();
            test(reader2, f2);
            long endMy = System.nanoTime();

            LineNumberReader r1 = new LineNumberReader(new FileReader(f1));
            LineNumberReader r2 = new LineNumberReader(new FileReader(f2));
            String line1 = r1.readLine();
            String line2 = r2.readLine();
            while (line1 != null && line2 != null)
            {
                if (line1.equals(line2))
                {
                    System.err.println(line1);
                }
                else
                {
                    System.err.println(line1+" !!!");
                    System.err.println(line2+" !!!");
                    //break;
                }
                line1 = r1.readLine();
                line2 = r2.readLine();
            }
            long tOriginal = endOriginal-startOriginal;
            long tMy = endMy-startMy;
            System.err.println("time Original ="+tOriginal);
            System.err.println("time This impl="+tMy);
        }
        catch (SAXParseException ex)
        {
            System.err.println("At "+ex.getSystemId()+" line "+ex.getLineNumber()+" col "+ex.getColumnNumber());
            ex.printStackTrace();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
