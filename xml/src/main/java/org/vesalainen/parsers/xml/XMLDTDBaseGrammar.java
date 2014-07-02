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

import org.vesalainen.parser.annotation.Rule;
import org.vesalainen.parser.annotation.Rules;
import org.vesalainen.parser.annotation.Terminal;
import org.vesalainen.regex.Regex;
import java.io.IOException;
import org.xml.sax.SAXException;

/**
 *
 * @author tkv
 */
public abstract class XMLDTDBaseGrammar extends XMLBaseGrammar
{
    /**
     * [22]   	prolog	   ::=   	 XMLDecl Misc* (doctypedecl Misc*)?
     * Note! Allows missing xmlDecl
     */
    @Rule({"xmlDecl? misc* (doctypedecl misc*)?"})
    protected abstract void prolog();
    /**
     * [28]   	doctypedecl	   ::=   	'<!DOCTYPE' S Name (S ExternalID)? S? ('[' intSubset ']' S?)? '>'
     * [16]   	doctypedecl	   ::=   	'<!DOCTYPE' S QName (S ExternalID)? S? ('[' (markupdecl | PEReference | S)* ']' S?)? '>'
     */
    @Rule({"doctypedeclStart", "doctypedeclContent", "doctypedeclEnd"})
    protected abstract void doctypedecl() throws SAXException;

    @Rule({"'<!DOCTYPE'","qName", "optExternalID"})
    protected void doctypedeclStart(QName name, String[] externalID) throws SAXException, IOException
    {
    }
    /**
     * S? ('[' (markupdecl | PEReference | S)* ']' S?)? '>'
     * @throws SAXException
     */
    @Rule({"('\\[' intSubset '\\]')?"})
    protected abstract void doctypedeclContent() throws SAXException;

    @Rule({"'>'"})
    protected void doctypedeclEnd() throws SAXException
    {
        
    }
    /**
     * [28a]   	DeclSep	   ::=   	 PEReference | S
     */
    /**
     * [30]   	extSubset	   ::=   	 TextDecl? extSubsetDecl
     */
    @Rule({"textDecl?", "extSubsetDecl*"})
    protected abstract void extSubset();

    /**
     * [31]   	extSubsetDecl	   ::=   	( markupdecl | conditionalSect | DeclSep)*
     */
    @Rules({
    @Rule("markupdecl"),
    @Rule("conditionalSect")
    })
    protected abstract void extSubsetDecl();

    /**
     * [28b]   	intSubset	   ::=   	(markupdecl | DeclSep)*
     */
    @Rule({"markupdecl*"})
    protected abstract void intSubset();

    /**
     * [29]   	markupdecl	   ::=   	 elementdecl | AttlistDecl | EntityDecl | NotationDecl | PI | Comment
     */
    @Rules({
    @Rule("elementdecl"),
    @Rule("attlistDecl"),
    @Rule("entityDecl"),
    @Rule("notationDecl")
    })
    protected abstract void markupdecl();

    /**
     * [61]   	conditionalSect	   ::=   	 includeSect | ignoreSect
     */
    @Rules({
    @Rule("includeSect"),
    @Rule("ignoreSect")
    })
    protected abstract void conditionalSect();

    /**
     * [62]   	includeSect	   ::=   	'<![' S? 'INCLUDE' S? '[' extSubsetDecl ']]>'
     */
    @Rule({"'<!\\['", "'INCLUDE'", "'\\['", "extSubsetDecl", "'\\]\\]>'"})
    protected abstract void includeSect();

    /**
     * [63]   	ignoreSect	   ::=   	'<![' S? 'IGNORE' S? '[' ignoreSectContents* ']]>'
     */
    @Rule({"'<!\\['", "'IGNORE'", "'\\['", "ignoreSectContents", "'\\]\\]>'"})
    protected abstract void ignoreSect();

    /**
     * [64]   	ignoreSectContents	   ::=   	 Ignore ('<![' ignoreSectContents ']]>' Ignore)*
     */
    @Rule({"ignore", "('<!\\[' ignoreSectContents '\\]\\]>' ignore)*"})
    protected abstract void ignoreSectContents();

    /**
     * [65]   	Ignore	   ::=   	 Char* - (Char* ('<![' | ']]>') Char*)
     * @param ignore
     */
    @Rules({
    //@Rule("ignoreStart"),     // forget nested ignore TODO
    @Rule("ignoreEnd")
    })
    protected void ignore(String ignore)
    {

    }
    @Terminal(expression="["+Char+"]*<!\\[", options={Regex.Option.FIXED_ENDER})
    protected abstract String ignoreStart(String value);

    @Terminal(expression="["+Char+"]*\\]\\]>", options={Regex.Option.FIXED_ENDER})
    protected abstract String ignoreEnd(String value);

    /**
     * [77]   	TextDecl	   ::=   	'<?xml' VersionInfo? EncodingDecl S? '?>'
     */
    @Rule({"'<\\?xml'", "versionInfo?", "encodingDecl", "'>'"})
    protected abstract void textDecl();

    @Rule({"textDecl?", "content"})
    protected abstract void extParsedEntity();

}
