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
import java.io.InputStream;
import org.vesalainen.parser.annotation.GrammarDef;
import org.vesalainen.parser.annotation.ParseMethod;
import org.vesalainen.parser.annotation.ParserContext;
import org.vesalainen.parser.annotation.Rule;
import org.vesalainen.parsers.xml.model.Document;
import org.vesalainen.parsers.xml.model.Element;
import org.xml.sax.SAXException;

/**
 * @author Timo Vesalainen
 */
//@GrammarDef
public abstract class XMLParserBase<R extends Element> extends XMLDTDBaseGrammar
{
    public Document parse(InputStream in, String officialSystemID) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        Document<R> document = new Document<>();
        parse(in, document);
        return document;
    }
    /**
     * 
     * @param in
     * @param document
     * @return
     * @throws IOException 
     * @see <a href="doc-files/XMLParserBase-document.html#BNF">BNF Syntax for document</a>
     */
    @ParseMethod(start="document", size=4096)
    protected abstract Document parse(InputStream in, @ParserContext("Document") Document document) throws IOException;

    /**
     * [1]   	document	   ::=   	 ( prolog element Misc* ) - ( Char* RestrictedChar Char* )
     */
    @Rule({"prolog", "element", "misc*"})
    protected void document(Element element, @ParserContext("Document") Document document) throws SAXException
    {
        document.setRoot(element);
    }
    
}
