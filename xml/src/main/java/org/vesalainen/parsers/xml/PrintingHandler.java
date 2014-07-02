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
package org.vesalainen.parsers.xml;

import java.io.IOException;
import java.io.PrintStream;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.DefaultHandler2;

/**
 *
 * @author tkv
 */
public class PrintingHandler extends DefaultHandler2
{
    private PrintStream out;

    public PrintingHandler(PrintStream out)
    {
        this.out = out;
    }

    @Override
    public void attributeDecl(String eName, String aName, String type, String mode, String value) throws SAXException
    {
        out.println("attributeDecl("+eName+", "+aName+", "+type+", "+mode+", "+value+");");
    }

    @Override
    public void comment(char[] ch, int start, int length) throws SAXException
    {
        for (int ii=0;ii<length;ii++)
        {
            out.print(ch[start+ii]);
        }
    }

    @Override
    public void elementDecl(String name, String model) throws SAXException
    {
        out.println("elementDecl("+name+", "+model+");");
    }

    @Override
    public void endCDATA() throws SAXException
    {
        out.println("endCDATA();");
    }

    @Override
    public void endDTD() throws SAXException
    {
        out.println("endDTD();");
    }

    @Override
    public void endEntity(String name) throws SAXException
    {
        //out.println("endEntity("+name+");");
    }

    @Override
    public void externalEntityDecl(String name, String publicId, String systemId) throws SAXException
    {
        out.println("externalEntityDecl("+name+", "+publicId+", "+systemId+");");
    }

    @Override
    public InputSource getExternalSubset(String name, String baseURI) throws SAXException, IOException
    {
        out.println("getExternalSubset("+name+", "+baseURI+");");
        return null;
    }

    @Override
    public void internalEntityDecl(String name, String value) throws SAXException
    {
        out.println("internalEntityDecl("+name+", "+value+");");
    }

    @Override
    public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId) throws SAXException, IOException
    {
        out.println("resolveEntity("+name+", "+publicId+", "+baseURI+", "+systemId+");");
        return null;
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException
    {
        out.println("resolveEntity("+publicId+", "+systemId+");");
        return null;
    }

    @Override
    public void startCDATA() throws SAXException
    {
        out.println("startCDATA();");
    }

    @Override
    public void startDTD(String name, String publicId, String systemId) throws SAXException
    {
        out.println("startDTD("+name+", "+publicId+", "+systemId+");");
    }

    @Override
    public void startEntity(String name) throws SAXException
    {
        //out.println("startEntity("+name+");");
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException
    {
        for (int ii=0;ii<length;ii++)
        {
            out.print(ch[start+ii]);
        }
    }

    @Override
    public void endDocument() throws SAXException
    {
        out.println("endDocument();");
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        out.println("endElement("+uri+", "+localName+", "+qName+");");
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException
    {
        out.println("endPrefixMapping("+prefix+");");
    }

    @Override
    public void error(SAXParseException e) throws SAXException
    {
        out.println("error("+e+");");
    }

    @Override
    public void fatalError(SAXParseException e) throws SAXException
    {
        out.println("fatalError("+e+");");
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException
    {
        for (int ii=0;ii<length;ii++)
        {
            out.print(ch[start+ii]);
        }
    }

    @Override
    public void notationDecl(String name, String publicId, String systemId) throws SAXException
    {
        out.println("notationDecl("+name+", "+publicId+", "+systemId+");");
    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException
    {
        out.println("processingInstruction("+target+", "+data+");");
    }

    @Override
    public void setDocumentLocator(Locator locator)
    {
        out.println("setDocumentLocator();");
    }

    @Override
    public void skippedEntity(String name) throws SAXException
    {
        out.println("skippedEntity("+name+");");
    }

    @Override
    public void startDocument() throws SAXException
    {
        out.println("startDocument();");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
        StringBuilder sb = new StringBuilder();
        for (int ii=0;ii<attributes.getLength();ii++)
        {
            sb.append(" "+attributes.getLocalName(ii)+"=\""+attributes.getValue(ii)+"\"");
        }
        out.println("startElement("+uri+", "+localName+", "+qName+", "+sb.toString()+");");
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException
    {
        out.println("startPrefixMapping("+prefix+", "+uri+");");
    }

    @Override
    public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) throws SAXException
    {
        out.println("unparsedEntityDecl("+name+", "+publicId+", "+systemId+", "+notationName+");");
    }

    @Override
    public void warning(SAXParseException e) throws SAXException
    {
        out.println("warning("+e+");");
    }

}
