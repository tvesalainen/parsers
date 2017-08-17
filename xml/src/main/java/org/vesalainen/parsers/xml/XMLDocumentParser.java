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
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.vesalainen.parser.GenClassFactory;
import org.vesalainen.parser.ParserConstants;
import org.vesalainen.parser.ParserInfo;
import org.vesalainen.parser.Trace;
import org.vesalainen.parser.TraceHelper;
import org.vesalainen.parser.annotation.GenClassname;
import org.vesalainen.parser.annotation.GrammarDef;
import org.vesalainen.parser.annotation.ParseMethod;
import org.vesalainen.parser.annotation.ParserContext;
import org.vesalainen.parser.annotation.Rule;
import org.vesalainen.parser.annotation.Rules;
import org.vesalainen.parser.annotation.Terminal;
import org.vesalainen.parser.util.Input;
import org.vesalainen.parser.util.InputReader;
import org.vesalainen.regex.Regex;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.ext.EntityResolver2;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
@GenClassname("org.vesalainen.parsers.xml.XMLDocumentParserImpl")
@GrammarDef()
public abstract class XMLDocumentParser extends XMLDTDBaseGrammar implements XMLReader, SAX2Constants, ParserInfo
{
    private final DefaultHandler2 defaultHandler = new DefaultHandler2();
    private ContentHandler contentHandler = defaultHandler;
    private DTDHandler dtdHandler = defaultHandler;
    private EntityResolver entityResolver = defaultHandler;
    private ErrorHandler errorHandler = defaultHandler;
    private DeclHandler declHandler = defaultHandler;
    private LexicalHandler lexicalHandler = defaultHandler;

    // Features
    private SAXFeatures features = new SAXFeatures();

    private AttributesImpl attributes = new AttributesImpl();
    private int level;
    private final Map<Integer,List<String>> nsScope = new HashMap<>();
    private final Map<String,String> nameSpaces;

    public XMLDocumentParser()
    {
        nameSpaces = new HashMap<>();
        nameSpaces.put("", "");
    }
    public static XMLDocumentParser getInstance() throws IOException
    {
        return (XMLDocumentParser) GenClassFactory.getGenInstance(XMLDocumentParser.class);
    }

    void setFeatures(SAXFeatures features)
    {
        this.features = features;
    }
    
    private void parseExternal(InputSource input) throws IOException, SAXException
    {
        external = true;
        parse(input);
    }
    @Override
    public void parse(InputSource input) throws IOException, SAXException
    {
        try
        {
            this.input = input;
            inputReader = Input.getInstance(input, BUFFERSIZE);
            locator = new XMLLocator(inputReader);
            if (contentHandler != null && !external)
            {
                contentHandler.setDocumentLocator(locator);
                contentHandler.startDocument();
            }
            if (external)
            {
                parseExtSubset(inputReader);
            }
            else
            {
                parseDocument(inputReader);
            }
            inputReader.close();
        }
        catch (IOException | SAXException ex)
        {
            SAXParseException exception = new SAXParseException("syntax error", locator, ex);
            if (errorHandler != null)
            {
                    errorHandler.fatalError(exception);
            }
            throw exception;
        }
    }

    public void parseText(String text) throws IOException, SAXException
    {
        parse(new InputSource(new StringReader(text)));
    }

    @Override
    public void parse(String systemId) throws IOException, SAXException
    {
        parse(new InputSource(systemId));
    }

    /**
     * [1]   	document	   ::=   	 ( prolog element Misc* ) - ( Char* RestrictedChar Char* )
     */
    @Rule({"prolog", "element", "misc*"})
    protected void document() throws SAXException
    {
        contentHandler.endDocument();
    }
    /**
     * [39]   	element	   ::=   	 EmptyElemTag
     *                                  | STag content ETag
     */
    @Rules({
    @Rule({"sTag"})
    })
    protected abstract void element();
    
    @Rule({"content"})
    protected abstract void elementContent();
    /**
     * 
     * @param reader 
     * @see <a href="doc-files/XMLDocumentParser-elementContent.html#BNF">BNF Syntax for Element Content</a>
     */
    @ParseMethod(start="elementContent", eof="eTag", whiteSpace={"eol", "pi", "comment", "charData"})
    protected abstract void parseContent(InputReader reader);

    /**
     * [40]   	STag	   ::=   	'<' Name (S Attribute)* S? '>'
     * [12]     STag	   ::=   	'<' QName (S Attribute)* S? '>'
     * [44]   	EmptyElemTag	   ::=   	'<' Name (S Attribute)* S? '/>'
     * EmptyElemTag	   ::=   	'<' QName (S Attribute)* S? '/>'
     */
    @Rule({"sTagName"})
    protected void sTag(QName name, @ParserContext(ParserConstants.INPUTREADER) InputReader reader) throws SAXException
    {
        boolean empty = !parseAttributes(reader);
        if (isNamespaceAware())
        {
            contentHandler.startElement(nameSpaces.get(name.getPrefix()), name.getLocalPart(), name.toString(), attributes);
            if (empty)
            {
                contentHandler.endElement(nameSpaces.get(name.getPrefix()), name.getLocalPart(), name.toString());
            }
        }
        else
        {
            contentHandler.startElement("", "", name.toString(), attributes);
            if (empty)
            {
                contentHandler.endElement("", "", name.toString());
            }
        }
        attributes = new AttributesImpl();
        level++;
        if (!empty)
        {
            parseContent(reader);
        }
    }

    @Terminal(expression=">")
    protected void endTag()
    {
    }

    @Rule
    protected boolean contentTag()
    {
        return true;
    }
    @Rule("'/'")
    protected boolean emptyTag()
    {
        return false;
    }
    @Rules({
    @Rule("contentTag"),
    @Rule("emptyTag")
    })
    protected boolean tagType(boolean hasContent)
    {
        return hasContent;
    }
    /**
     * 
     * @param reader
     * @return 
     * @see <a href="doc-files/XMLDocumentParser-attributesSub.html#BNF">BNF Syntax for Attributes Sub</a>
     */
    @ParseMethod(start="attributesSub", eof="endTag")
    protected abstract boolean parseAttributes(InputReader reader);
    
    @Rule({"attributes", "s?", "tagType"})
    protected abstract boolean attributesSub(boolean hasContent);
    
    @Rules({
    @Rule,
    @Rule({"attributes", "s", "attribute"})
    })
    protected abstract void attributes();

    /**
     * [41]   	Attribute	   ::=   	 Name Eq AttValue
     * [15] 	Attribute	   ::=   	NSAttName Eq AttValue
     *                                          | QName Eq AttValue
     */
    @Rule({"qName", "eq", "attValue"})
    protected void attribute(QName name, String value)
    {
        if (isNamespaceAware())
        {
            attributes.addAttribute(nameSpaces.get(name.getPrefix()), name.getLocalPart(), name.toString(), "", value);
        }
        else
        {
            attributes.addAttribute("", "", name.toString(), "", value);
        }
    }
    @Rule({"nsAttName", "eq", "attValue"})
    protected void attribute(String name, String uri) throws SAXException
    {
        if (isNamespaceAware())
        {
            if (uri.isEmpty())
            {
                String remove = nameSpaces.remove(name);
                if (remove == null)
                {
                    throw new SAXParseException("removing prefix "+name+" which is out of scope", locator);
                }
                if (supportNameSpacePrefixes())
                {
                    contentHandler.endPrefixMapping(name);
                }
                List<String> prefixes = nsScope.get(level);
                prefixes.remove(name);
            }
            else
            {
                nameSpaces.put(name, uri);
                if (supportNameSpacePrefixes())
                {
                    contentHandler.startPrefixMapping(name, uri);
                }
                List<String> prefixes = nsScope.get(level);
                if (prefixes == null)
                {
                    prefixes = new ArrayList<>();
                    nsScope.put(level, prefixes);
                }
                prefixes.add(name);
            }
            if (supportNameSpacePrefixes())
            {
                attributes.addAttribute(nameSpaces.get("xmlns"), name, name, "", uri);
            }
        }
        else
        {
            if (supportNameSpacePrefixes())
            {
                attributes.addAttribute("", "", name.toString(), "", uri);
            }
        }
    }
    /**
     * [42] ETag	   ::=   	'</' Name S? '>'
     * [13]   	ETag	   ::=   	'</' QName S? '>'
     */
    @Terminal(expression="</["+NCNameStartChar+"]["+NCNameChar+"]*(:["+NCNameStartChar+"]["+NCNameChar+"]*)?["+S+"]*>")
    protected void eTag(String s) throws SAXException
    {
        QName name = new QName(s.substring(2, s.length()-1).trim());
        level--;
        if (isNamespaceAware())
        {
            contentHandler.endElement(nameSpaces.get(name.getPrefix()), name.getLocalPart(), name.toString());
        }
        else
        {
            contentHandler.endElement("", "", name.toString());
        }
        List<String> prefixes = nsScope.get(level);
        if (prefixes != null)
        {
            if (supportNameSpacePrefixes())
            {
                for (String prefix : prefixes)
                {
                    contentHandler.endPrefixMapping(prefix);
                }
            }
            nsScope.remove(level);
        }
    }
    /**
     * [43] content	   ::=   	 CharData? ((element | Reference | CDSect | PI | Comment) CharData?)*
     */
    @Rules({
    @Rule,
    @Rule({"content", "element"}),
    @Rule({"content", "cdSect"})
    })
    protected abstract void content();

    @Rules({
    @Rule({"content", "reference"})
    })
    protected void content(String charData)
    {

    }
    @Rule("externalID")
    protected Object peDef(String[] externalID) throws SAXException, IOException
    {
        String pubId = externalID[0];
        String sysId = externalID[1];
        return new ExternalEntity(input, pubId, sysId, entityResolver);
    }
    /**
     * [14]   	CharData	   ::=   	[^<&]* - ([^<&]* ']]>' [^<&]*)
     */
    @Terminal(expression="[^<\\&\\x0d\\x85\\u2028]+")
    protected void charData(String str) throws SAXException
    {
        contentHandler.characters(str.toCharArray(), 0, str.length());
    }

    /**
     * [15]   	Comment	   ::=   	'<!--' ((Char - '-') | ('-' (Char - '-')))* '-->'
     */
    @Terminal(expression="<!\\-\\-["+Char+"]*\\-\\->", options={Regex.Option.FIXED_ENDER})
    protected void comment(String str) throws SAXException
    {
        lexicalHandler.comment(str.toCharArray(), 0, str.length());
    }

    @Rule({"'<!DOCTYPE'","qName", "optExternalID"})
    protected void doctypedeclStart(QName name, String[] externalID) throws SAXException, IOException
    {
        String pubId = externalID[0];
        String sysId = externalID[1];
        InputSource embeddedInput = null;
        lexicalHandler.startDTD(name.toString(), pubId, sysId);
        if (entityResolver instanceof EntityResolver2)
        {
            EntityResolver2 er2 = (EntityResolver2) entityResolver;
            embeddedInput = er2.resolveEntity(null, pubId, input.getSystemId(), sysId);
        }
        else
        {
            embeddedInput = entityResolver.resolveEntity(pubId, sysId);
        }
        lexicalHandler.startEntity("[dtd]");
        try
        {
            URI uri = new URI(input.getSystemId());
            String newSystemId = uri.resolve(sysId).toString();
            if (embeddedInput == null)
            {
                embeddedInput = new InputSource(newSystemId);
            }
            embeddedInput.setEncoding(input.getEncoding());
            embeddedInput.setPublicId(pubId);
            embeddedInput.setSystemId(newSystemId);
            parseExternal(embeddedInput);
        }
        catch (URISyntaxException | IOException | SAXException ex)
        {
            throw new SAXParseException("", locator, ex);
        }
    }
    @Rule({"'>'"})
    protected void doctypedeclEnd() throws SAXException
    {
        lexicalHandler.endDTD();
    }
    /**
     * [72]   	PEDecl	   ::=   	'<!ENTITY' S '%' S Name S PEDef S? '>'
     * @param name
     */
    @Rule({"'<!ENTITY'", "'%'", "name", "peDef", "'>'"})
    protected void peDecl(String name, Object value) throws SAXException
    {
        Object old = parameterReferences.put(name, value);
        if (old != null)
        {
            errorHandler.warning(new SAXParseException(name+" parameter reference defined more than once", locator));
        }
    }
    /**
     * [82]   	NotationDecl	   ::=   	'<!NOTATION' S Name S (ExternalID | PublicID) S? '>'
     * @param name
     * @param id
     */
    @Rule({"'<!NOTATION'", "name", "externalID", "'>'"})
    protected void notationDecl(String name, String[] externalID) throws SAXException
    {
        dtdHandler.notationDecl(name, externalID[0], externalID[1]);
    }
    @Rule({"'<!NOTATION'", "name", "publicID", "'>'"})
    protected void notationDecl(String name, String publicID) throws SAXException
    {
        dtdHandler.notationDecl(name, publicID, null);
    }
    public ContentHandler getContentHandler()
    {
        return contentHandler;
    }

    public void setContentHandler(ContentHandler contentHandler)
    {
        if (contentHandler != null)
        {
            this.contentHandler = contentHandler;
        }
        else
        {
            this.contentHandler = defaultHandler;
        }
    }

    public DeclHandler getDeclHandler()
    {
        return declHandler;
    }

    public void setDeclHandler(DeclHandler declHandler)
    {
        if (declHandler != null)
        {
            this.declHandler = declHandler;
        }
        else
        {
            this.declHandler = defaultHandler;
        }
    }

    public DTDHandler getDTDHandler()
    {
        return dtdHandler;
    }

    public void setDTDHandler(DTDHandler dtdHandler)
    {
        if (dtdHandler != null)
        {
            this.dtdHandler = dtdHandler;
        }
        else
        {
            this.dtdHandler = defaultHandler;
        }
    }

    public EntityResolver getEntityResolver()
    {
        return entityResolver;
    }

    public void setEntityResolver(EntityResolver entityResolver)
    {
        if (entityResolver != null)
        {
            this.entityResolver = entityResolver;
        }
        else
        {
            this.entityResolver = defaultHandler;
        }
    }

    public ErrorHandler getErrorHandler()
    {
        return errorHandler;
    }

    public void setErrorHandler(ErrorHandler errorHandler)
    {
        if (errorHandler != null)
        {
            this.errorHandler = errorHandler;
        }
        else
        {
            this.errorHandler = defaultHandler;
        }
    }

    public LexicalHandler getLexicalHandler()
    {
        return lexicalHandler;
    }

    public void setLexicalHandler(LexicalHandler lexicalHandler)
    {
        if (lexicalHandler != null)
        {
            this.lexicalHandler = lexicalHandler;
        }
        else
        {
            this.lexicalHandler = defaultHandler;
        }
    }

    public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException
    {
        if (PROPERTY_DECLARATION_HANDLER.equals(name))
        {
            return declHandler;
        }
        if (PROPERTY_LEXICAL_HANDLER.equals(name))
        {
            return lexicalHandler;
        }
        if (PROPERTY_DOCUMENT_XML_VERSION.equals(name))
        {
            return locator.getXMLVersion();
        }
        throw new SAXNotRecognizedException(name);
    }

    public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException
    {
        if (PROPERTY_DECLARATION_HANDLER.equals(name))
        {
            declHandler = (DeclHandler) value;
            return;
        }
        if (PROPERTY_LEXICAL_HANDLER.equals(name))
        {
                lexicalHandler = (LexicalHandler) value;
            return;
        }
        if (PROPERTY_DOCUMENT_XML_VERSION.equals(name))
        {
            throw new SAXNotSupportedException(name);
        }
        throw new SAXNotRecognizedException(name);
    }


//    @TraceMethod
    protected void trace(
            int ord,
            int ctx,
            @ParserContext("$inputReader") InputReader reader,
            @ParserContext("$token") int token,
            @ParserContext("$laToken") int laToken,
            @ParserContext("$curTok") int curtok,
            @ParserContext("$stateStack") int[] stack,
            @ParserContext("$sp") int sp,
            @ParserContext("$typeStack") int[] typeStack,
            @ParserContext("$valueStack") Object[] valueStack
            )
    {
        Trace trace = Trace.values()[ord];
        switch (trace)
        {
            case STATE:
                System.err.println("state "+stack[sp]);
                break;
            case INPUT:
                System.err.println("input"+ctx+"='"+reader.getString()+"' token="+getToken(token));
                break;
            case LAINPUT:
                System.err.println("lainput"+ctx+"='"+reader.getString()+"' token="+getToken(laToken));
                break;
            case PUSHVALUE:
                System.err.println("push value");
                break;
            case EXITLA:
                System.err.println("exit La");
                TraceHelper.printStacks(System.err, stack, typeStack, valueStack, sp);
                break;
            case BEFOREREDUCE:
                System.err.println("Before reducing rule "+getRule(ctx));
                TraceHelper.printStacks(System.err, stack, typeStack, valueStack, sp);
                break;
            case AFTERREDUCE:
                System.err.println("After reducing rule "+getRule(ctx));
                TraceHelper.printStacks(System.err, stack, typeStack, valueStack, sp);
                break;
            case GOTO:
                System.err.println("Goto "+ctx);
                break;
            case SHIFT:
                System.err.println("Shift "+ctx);
                break;
            case SHRD:
                System.err.println("Shift/Reduce");
                break;
            case LASHRD:
                System.err.println("La Shift/Reduce");
                break;
            case GTRD:
                System.err.println("Goto/Reduce");
                break;
            case LASHIFT:
                System.err.println("LaShift State "+ctx);
                TraceHelper.printStacks(System.err, stack, typeStack, valueStack, sp);
                break;
            default:
                System.err.println("unknown action "+trace);
                break;
        }
    }
    /**
     * [45]   	elementdecl	   ::=   	'<!ELEMENT' S Name S contentspec S? '>'
     * [17]   	elementdecl	   ::=   	'<!ELEMENT' S QName S contentspec S? '>'
     */
    @Rule({"'<!ELEMENT'", "qName", "contentspec", "'>'"})
    protected void elementdecl(QName name)
    {
    }
    /**
     * [46]   	contentspec	   ::=   	'EMPTY' | 'ANY' | Mixed | children
     */
    @Rules({
    @Rule("'EMPTY'"),
    @Rule("'ANY'"),
    @Rule("mixed"),
    @Rule("children")
    })
    protected abstract void contentspec();

    /**
     * [47]   	children	   ::=   	(choice | seq) ('?' | '*' | '+')?
     */
    @Rules({
    @Rule({"choice", "('[\\+\\*\\?]')?"}),
    @Rule({"seq", "('[\\+\\*\\?]')?"})
    })
    protected abstract void children();

    /**
     * [48]   	cp	   ::=   	(Name | choice | seq) ('?' | '*' | '+')?
     * [18]   	cp	   ::=   	(QName | choice | seq) ('?' | '*' | '+')?
     */
    @Rule({"qName", "('[\\+\\*\\?]')?"})
    protected void cp(QName name)
    {

    }
    @Rules({
    @Rule({"choice", "('[\\+\\*\\?]')?"}),
    @Rule({"seq", "('[\\+\\*\\?]')?"})
    })
    protected abstract void cp();

    /**
     * [49]   	choice	   ::=   	'(' S? cp ( S? '|' S? cp )+ S? ')'
     */
    @Rule({"'\\('", "cp", "('\\|' cp)+", "'\\)'"})
    protected abstract void choice();

    /**
     * [50]   	seq	   ::=   	'(' S? cp ( S? ',' S? cp )* S? ')'
     */
    @Rule({"'\\('", "cp", "('\\,' cp)*", "'\\)'"})
    protected abstract void seq();

    /**
     * [51]   	Mixed	   ::=   	'(' S? '#PCDATA' (S? '|' S? Name)* S? ')*'
     *                                  | '(' S? '#PCDATA' S? ')'
     * [19]   	Mixed	   ::=   	'(' S? '#PCDATA' (S? '|' S? QName)* S? ')*'
     *                                  | '(' S? '#PCDATA' S? ')'
     */
    @Rules({
    @Rule({"'\\('", "'#PCDATA'", "mixedNames", "'\\)'", "'\\*'"}),
    @Rule({"'\\('", "'#PCDATA'", "'\\)'"})
    })
    protected abstract void mixed();

    @Rule
    protected abstract void mixedNames();

    @Rule({"mixedNames", "'\\|'", "qName"})
    protected void mixedNames(QName name)
    {

    }
    /**
     * [52]   	AttlistDecl	   ::=   	'<!ATTLIST' S Name AttDef* S? '>'
     * [20]   	AttlistDecl	   ::=   	'<!ATTLIST' S QName AttDef* S? '>'
     * @param name
     */
    @Rule({"'<!ATTLIST'", "qName", "attDef*", "'>'"})
    protected void attlistDecl(QName name)
    {

    }
    /**
     * [53]   	AttDef	   ::=   	 S Name S AttType S DefaultDecl
     * [21]   	AttDef	   ::=   	S (QName | NSAttName) S AttType S DefaultDecl
     * @param name
     */
    @Rule({"qName", "attType", "defaultDecl" })
    protected void attDef(QName name)
    {

    }
    @Rule({"nsAttName", "attType", "defaultDecl" })
    protected void attDef(String name)
    {

    }
    /**
     * [54]   	AttType	   ::=   	 StringType | TokenizedType | EnumeratedType
     */
    @Rules({
    @Rule("stringType"),
    @Rule("tokenizedType"),
    @Rule("enumeratedType")
    })
    protected abstract void attType();

    /**
     * [55]   	StringType	   ::=   	'CDATA'
     */
    @Rule({"'CDATA'"})
    protected abstract void stringType();

    /**
     * [56]   	TokenizedType	   ::=   	'ID'
     *                                          | 'IDREF'
     *                                          | 'IDREFS'
     *                                          | 'ENTITY'
     *                                          | 'ENTITIES'
     *                                          | 'NMTOKEN'
     *                                          | 'NMTOKENS'
     */
    @Rules({
    @Rule("'ID'"),
    @Rule({"'ID'", "'REF'"}),
    @Rule({"'ID'", "'REF'", "'S'"}),
    @Rule("'ENTITY'"),
    @Rule("'ENTITIES'"),
    @Rule("'NMTOKEN'"),
    @Rule({"'NMTOKEN'", "'S'"})
    })
    protected abstract void tokenizedType();

    /**
     * [57]   	EnumeratedType	   ::=   	 NotationType | Enumeration
     */
    @Rules({
    @Rule("notationType"),
    @Rule("enumeration")
    })
    protected abstract void enumeratedType();

    /**
     * [58]   	NotationType	   ::=   	'NOTATION' S '(' S? Name (S? '|' S? Name)* S? ')'
     * @param name
     */
    @Rule({"'NOTATION'", "'\\('", "name", "notationTypeNames", "'\\)'"})
    protected void notationType(String name)
    {

    }
    @Rule
    protected abstract void notationTypeNames();

    @Rule({"notationTypeNames", "'\\|'", "name"})
    protected void notationTypeNames(String name)
    {

    }
    /**
     * [59]   	Enumeration	   ::=   	'(' S? Nmtoken (S? '|' S? Nmtoken)* S? ')'
     * @param name
     */
    @Rule({"'\\('", "nmtoken", "enumerationNMTokens", "'\\)'"})
    protected void enumeration(String name)
    {

    }
    @Rule
    protected abstract void enumerationNMTokens();

    @Rule({"enumerationNMTokens", "'\\|'", "nmtoken"})
    protected void enumerationNMTokens(String name)
    {

    }
    /**
     * [60]   	DefaultDecl	   ::=   	'#REQUIRED' | '#IMPLIED'
     *                                          | (('#FIXED' S)? AttValue)
     */
    @Rules({
    @Rule("'#REQUIRED'"),
    @Rule("'#IMPLIED'")
    })
    protected abstract void defaultDecl();

    @Rule({"'#FIXED'?", "attValue"})
    protected void defaultDecl(String value)
    {

    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        try
        {
            
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private boolean isNamespaceAware()
    {
        try
        {
            return features.getFeature(FEATURE_NAMES_SPACE);
        }

        catch (SAXNotRecognizedException | SAXNotSupportedException ex)
        {
            return false;
        }
    }

    private boolean supportNameSpacePrefixes()
    {
        try
        {
            return features.getFeature(FEATURE_NAMES_SPACE_PREFIXES);
        }

        catch (SAXNotRecognizedException | SAXNotSupportedException ex)
        {
            return false;
        }
    }
}
