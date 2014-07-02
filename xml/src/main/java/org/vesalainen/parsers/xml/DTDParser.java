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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Member;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.lang.model.element.ExecutableElement;
import org.vesalainen.bcc.model.El;
import org.vesalainen.grammar.AnnotatedGrammar;
import org.vesalainen.grammar.Grammar;
import org.vesalainen.parser.GenClassFactory;
import org.vesalainen.parser.annotation.GenClassname;
import org.vesalainen.parser.annotation.GrammarDef;
import org.vesalainen.parser.annotation.Rule;
import org.vesalainen.parser.annotation.Rules;
import org.vesalainen.parser.annotation.Terminal;
import org.vesalainen.parser.util.Input;
import org.vesalainen.parser.util.InputReader;
import org.vesalainen.parsers.xml.attr.AttDef;
import org.vesalainen.parsers.xml.attr.AttType;
import org.vesalainen.parsers.xml.attr.AttType.NotationType;
import org.vesalainen.parsers.xml.attr.DefaultDecl;
import org.vesalainen.parsers.xml.attr.DefaultDecl.DefaultValue;
import org.vesalainen.parsers.xml.attr.NSAttDef;
import org.vesalainen.parsers.xml.attr.NormalAttDef;
import org.vesalainen.parsers.xml.model.Attribute;
import org.vesalainen.parsers.xml.model.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * @deprecated Not migrated to annotation processor
 * @author tkv
 */
//@GenClassname("org.vesalainen.parsers.xml.DTDParserImpl")
//@GrammarDef()
public abstract class DTDParser extends XMLDTDBaseGrammar
{
    public static final char ELPRF = '#';   // Element prefix in grammar
    private Map<QName,String> elementMap = new HashMap<>();
    private Map<QName,List<AttDef>> AttributeMap = new HashMap<>();
    private Set<QName> childElements = new HashSet<>();
    private Set<AttDef> uniqueAttributes = new HashSet<>();
    private String publicId;
    private String officialSystemId;

    public static DTDParser createDTDParser() throws IOException
    {
        return (DTDParser) GenClassFactory.getGenInstance(DTDParser.class);
    }
    public void parse(String officialSystemId) throws IOException, SAXException, URISyntaxException
    {
        parse(null, officialSystemId, officialSystemId);
    }
    public void parse(String publicId, String officialSystemId) throws IOException, SAXException, URISyntaxException
    {
        parse(publicId, officialSystemId, officialSystemId);
    }
    public void parse(String publicId, String officialSystemId, String effectiveSystemId) throws IOException, SAXException, URISyntaxException
    {
        this.publicId = publicId;
        this.officialSystemId = officialSystemId;
        parse(new InputSource(effectiveSystemId));
    }
    private void parse(InputSource input) throws IOException, SAXException
    {
        try
        {
            this.input = input;
            inputReader = Input.getInstance(input, BUFFERSIZE);
            locator = new XMLLocator(inputReader);
            parseExtSubset(inputReader);
            inputReader.close();
            createGrammar();
        }
        catch (IOException ex)
        {
            SAXParseException exception = new SAXParseException("syntax error", locator, ex);
            throw exception;
        }
    }

    private void createGrammar() throws IOException
    {
        //try
        {
            if (elementMap.size() - childElements.size() != 1)
            {
                throw new IllegalArgumentException("not exactly one root element");
            }
            Grammar g = new AnnotatedGrammar(El.getTypeElement(DTDParserCompilerBase.class.getCanonicalName()));
            g.addRule("any");
            for (QName name : elementMap.keySet())
            {
                String localPart = name.getLocalPart();
                String lhs = ELPRF+localPart;
                List<AttDef> attList = AttributeMap.get(name);
                String lhsAttrs = "";
                if (!attList.isEmpty())
                {
                    lhsAttrs = lhs+"Attrs";
                    g.addRule(El.getMethod(DTDParser.class, "attributeListStart"), lhsAttrs);
                    for (AttDef attr : attList)
                    {
                        ExecutableElement reducer = El.getMethod(DTDParser.class, "attributeListNext", List.class, Attribute.class);
                        g.addRule(reducer, lhsAttrs, lhsAttrs, attr.greateRhs());
                    }
                }
                if (!childElements.contains(name))
                {
                    g.addRule("element", lhs);
                }
                String rhs = elementMap.get(name);
                /* TODO
                if (rhs != null)
                {
                    Member reducer = Element.getNewInstanceMethodWithContent(officialSystemId, localPart);
                    String lhsContent = lhs+"Content";
                    g.addRule(reducer, lhs, "'<' namePrefix '"+localPart+"' "+lhsAttrs+" '>' "+lhsContent+" '</' namePrefix '"+localPart+"' '>'");
                    g.addRule(lhsContent, rhs);
                }
                else
                {
                    Member reducer = Element.getNewInstanceMethodWithoutContent(officialSystemId, localPart);
                    g.addRule(reducer, lhs, "'<' namePrefix '"+localPart+"' "+lhsAttrs+" '/>'");
                    g.addRule(reducer, lhs, "'<' namePrefix '"+localPart+"' "+lhsAttrs+" '>' '</' namePrefix '"+localPart+"' '>'");
                }
                */
                g.addRule("any", "any", lhs);
            }
            /*
            g.print(System.err);
            String packet = Document.getPackagename(officialSystemId);
            String className = packet+".DocumentParser";
            ClassWrapper parserClass = ClassWrapper.fromFullyQualifiedForm(className, XMLParserBase.class);
            ParserCompiler pc = new ParserCompiler(parserClass, g);
            BulkCompiler.compile(pc);
            XMLParserBase parserInstance = (XMLParserBase) pc.parserInstance();
            g = g.subGrammar("document");
            g.dump(System.err);
            g.checkGrammar();
             *
             */
        }


        //catch (ReflectiveOperationException ex)
        {
            //throw new IOException(ex);
        }
    }

    private AttDef addAttDef(AttDef attDef)
    {
        if (!uniqueAttributes.contains(attDef))
        {
            uniqueAttributes.add(attDef);
            return attDef;
        }
        for (AttDef a : uniqueAttributes)
        {
            if (a.equals(attDef))
            {
                return a;
            }
        }
        assert false;
        return null;
    }
    @Override
    @Rule({"'<!DOCTYPE'","qName", "optExternalID"})
    protected void doctypedeclStart(QName name, String[] externalID) throws SAXException, IOException
    {
        String pubId = externalID[0];
        String sysId = externalID[1];
        InputSource embeddedInput = null;
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
            parse(embeddedInput);
        }
        catch (Exception ex)
        {
            throw new SAXParseException("", locator, ex);
        }
    }
    @Override
    @Rule({"'>'"})
    protected void doctypedeclEnd() throws SAXException
    {
    }
    @Override
    @Rule("externalID")
    protected Object peDef(String[] externalID) throws SAXException, IOException
    {
        String pubId = externalID[0];
        String sysId = externalID[1];
        return new ExternalEntity(input, pubId, sysId, null);
    }
    /**
     * [39]   	element	   ::=   	 EmptyElemTag
     *                                  | STag content ETag
     */

    @Rules({
    @Rule("emptyElemTag"),
    @Rule({"sTag", "content", "eTag"})
    })
    protected abstract void element();

    /**
     * [40]   	STag	   ::=   	'<' Name (S Attribute)* S? '>'
     * [12]     STag	   ::=   	'<' QName (S Attribute)* S? '>'
     */
    @Rule({"'<'", "qName", "attributes", "'>'"})
    protected void sTag(QName name) throws SAXException
    {
    }
    @Rules({
    @Rule,
    @Rule({"attributes", "attribute"})
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
    }
    @Rule({"nsAttName", "eq", "attValue"})
    protected void attribute(String name, String uri) throws SAXException
    {
    }
    /**
     * [42] ETag	   ::=   	'</' Name S? '>'
     * [13]   	ETag	   ::=   	'</' QName S? '>'
     */
    @Rule({"'<'", "'/'", "qName", "'>'"})
    protected void eTag(QName name) throws SAXException
    {
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
    /**
     * [44]   	EmptyElemTag	   ::=   	'<' Name (S Attribute)* S? '/>'
     * EmptyElemTag	   ::=   	'<' QName (S Attribute)* S? '/>'
     */
    @Rule({"'<'", "qName", "attributes", "'/'", "'>'"})
    protected void emptyElemTag(QName name) throws SAXException
    {
    }
    /**
     * [45]   	elementdecl	   ::=   	'<!ELEMENT' S Name S contentspec S? '>'
     * [17]   	elementdecl	   ::=   	'<!ELEMENT' S QName S contentspec S? '>'
     */
    @Rule({"'<!ELEMENT'", "qName", "contentspec", "'>'"})
    protected void elementdecl(QName name, String content)
    {
        String old = elementMap.put(name, content);
        if (old != null)
        {
            throw new IllegalArgumentException("element "+name+" defined twice");
        }
    }
    /**
     * [46]   	contentspec	   ::=   	'EMPTY' | 'ANY' | Mixed | children
     */
    @Rule("'EMPTY'")
    protected String emptyContentspec()
    {
        return null;
    }
    @Rule("'ANY'")
    protected String anyContentspec()
    {
        return "any";
    }

    @Rules({
    @Rule("emptyContentspec"),
    @Rule("anyContentspec"),
    @Rule("mixed"),
    @Rule("children")
    })
    protected abstract String contentspec(String content);

    @Terminal(expression="[\\+\\*\\?]")
    protected abstract char quantifierChar(char cc);

    @Rule
    protected char quantifier()
    {
        return 0;
    }
    @Rule("quantifierChar")
    protected char quantifier(char cc)
    {
        return cc;
    }
    /**
     * [47]   	children	   ::=   	(choice | seq) ('?' | '*' | '+')?
     */
    @Rules({
    @Rule({"choice", "quantifier"}),
    @Rule({"seq", "quantifier"})
    })
    protected String children(String content, char quantifier)
    {
        if (quantifier != 0)
        {
            return content+quantifier;
        }
        else
        {
            return content;
        }
    }

    /**
     * [48]   	cp	   ::=   	(Name | choice | seq) ('?' | '*' | '+')?
     * [18]   	cp	   ::=   	(QName | choice | seq) ('?' | '*' | '+')?
     */
    @Rule({"qName", "quantifier"})
    protected String cp(QName name, char quantifier)
    {
        childElements.add(name);
        if (quantifier != 0)
        {
            return ELPRF+name.getLocalPart()+quantifier;
        }
        else
        {
            return ELPRF+name.getLocalPart();
        }
    }
    @Rules({
    @Rule({"choice", "quantifier"}),
    @Rule({"seq", "quantifier"})
    })
    protected String cp(String content, char quantifier)
    {
        if (quantifier != 0)
        {
            return content+quantifier;
        }
        else
        {
            return content;
        }
    }

    /**
     * [49]   	choice	   ::=   	'(' S? cp ( S? '|' S? cp )+ S? ')'
     */
    @Rule({"'\\('", "choices", "'\\)'"})
    protected String choice(String choices)
    {
        return '('+choices+')';
    }
    @Rules({
    @Rule({"cp", "'\\|'", "cp"}),
    @Rule({"choices", "'\\|'", "cp"})
    })
    protected String choices(String cp1, String cp2)
    {
        return cp1+'|'+cp2;
    }
    /**
     * [50]   	seq	   ::=   	'(' S? cp ( S? ',' S? cp )* S? ')'
     */
    @Rule({"'\\('", "seqs", "'\\)'"})
    protected String seq(String seqs)
    {
        return '('+seqs+')';
    }
    @Rule({"cp"})
    protected String seqs(String cp)
    {
        return cp;
    }
    @Rule({"seqs", "'\\,'", "cp"})
    protected String seqs(String seqs, String cp)
    {
        return seqs+' '+cp;
    }

    /**
     * [51]   	Mixed	   ::=   	'(' S? '#PCDATA' (S? '|' S? Name)* S? ')*'
     *                                  | '(' S? '#PCDATA' S? ')'
     * [19]   	Mixed	   ::=   	'(' S? '#PCDATA' (S? '|' S? QName)* S? ')*'
     *                                  | '(' S? '#PCDATA' S? ')'
     */
    @Rule({"'\\('", "'#PCDATA'", "'\\)'"})
    protected String mixed()
    {
        return "";
    }

    @Rule({"'\\('", "'#PCDATA'", "mixedNames", "'\\)'", "'\\*'"})
    protected String mixed(String qNames)
    {
        return '('+qNames+')';
    }

    @Rule
    protected String mixedNames()
    {
        return "";
    }

    @Rule({"mixedNames", "'\\|'", "qName"})
    protected String mixedNames(String qNames, QName name)
    {
        return qNames+'|'+name.toString();
    }
    /**
     * [52]   	AttlistDecl	   ::=   	'<!ATTLIST' S Name AttDef* S? '>'
     * [20]   	AttlistDecl	   ::=   	'<!ATTLIST' S QName AttDef* S? '>'
     * @param name
     */
    @Rule({"'<!ATTLIST'", "qName", "attDefs", "'>'"})
    protected void attlistDecl(QName name, List<AttDef> attDefs)
    {
        String element = elementMap.get(name);
        if (element == null)
        {
            throw new UnsupportedOperationException("attlistDecl without elementdecl not supported. element="+name);
        }
        if (AttributeMap.containsKey(name))
        {
            throw new UnsupportedOperationException("attlistDecl merge not supported. element="+name);
        }
        AttributeMap.put(name, attDefs);
    }
    @Rule
    protected List<AttDef> attDefs()
    {
        return new ArrayList<>();
    }

    @Rule({"attDefs", "attDef"})
    protected List<AttDef> attDefs(List<AttDef> attDefs, AttDef attDef)
    {
        if (attDef != null)
        {
            attDefs.add(attDef);
        }
        return attDefs;
    }

    /**
     * [53]   	AttDef	   ::=   	 S Name S AttType S DefaultDecl
     * [21]   	AttDef	   ::=   	S (QName | NSAttName) S AttType S DefaultDecl
     * @param name
     */
    @Rule({"qName", "attType", "defaultDecl" })
    protected AttDef attDef(QName name, AttType attType, DefaultDecl defaultDecl)
    {
        return addAttDef(new NormalAttDef(name, attType, defaultDecl));
    }
    @Rule({"nsAttName", "attType", "defaultDecl" })
    protected AttDef attDef(String name, AttType attType, DefaultDecl defaultDecl)
    {
        if (!name.isEmpty())
        {
            return addAttDef(new NSAttDef(name, attType, defaultDecl));
        }
        else
        {
            // empty NSAttName is not an attribute of any element (xmlns)
            return null;
        }
    }
    /**
     * [54]   	AttType	   ::=   	 StringType | TokenizedType | EnumeratedType
     */
    @Rules({
    @Rule("stringType"),
    @Rule("tokenizedType"),
    @Rule("enumeratedType")
    })
    protected abstract AttType attType(AttType attType);

    /**
     * [55]   	StringType	   ::=   	'CDATA'
     */
    @Rule({"'CDATA'"})
    protected AttType stringType()
    {
        return AttType.PCDATA;
    }

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
    @Rule("tokenizedTypeId"),
    @Rule("tokenizedTypeIdRef"),
    @Rule("tokenizedTypeIdRefs"),
    @Rule("tokenizedTypeEntity"),
    @Rule("tokenizedTypeEntities"),
    @Rule("tokenizedTypeNmToken"),
    @Rule("tokenizedTypeNmTokens")
    })
    protected abstract AttType tokenizedType(AttType attType);

    @Rule("'ID'")
    protected AttType tokenizedTypeId()
    {
        return AttType.ID;
    }
    @Rule({"'ID'", "'REF'"})
    protected AttType tokenizedTypeIdRef()
    {
        return AttType.IDREF;
    }
    @Rule({"'ID'", "'REF'", "'S'"})
    protected AttType tokenizedTypeIdRefs()
    {
        return AttType.IDREFS;
    }
    @Rule("'ENTITY'")
    protected AttType tokenizedTypeEntity()
    {
        return AttType.ENTITY;
    }
    @Rule("'ENTITIES'")
    protected AttType tokenizedTypeEntities()
    {
        return AttType.ENTITIES;
    }
    @Rule("'NMTOKEN'")
    protected AttType tokenizedTypeNmToken()
    {
        return AttType.NMTOKEN;
    }
    @Rule({"'NMTOKEN'", "'S'"})
    protected AttType tokenizedTypeNmTokens()
    {
        return AttType.NMTOKENS;
    }

    /**
     * [57]   	EnumeratedType	   ::=   	 NotationType | Enumeration
     */
    @Rules({
    @Rule("notationType"),
    @Rule("enumeration")
    })
    protected abstract AttType enumeratedType(AttType attType);

    /**
     * [58]   	NotationType	   ::=   	'NOTATION' S '(' S? Name (S? '|' S? Name)* S? ')'
     * @param name
     */
    @Rule({"'NOTATION'", "'\\('", "notationTypeNames", "'\\)'"})
    protected AttType notationType(List<String> names)
    {
        return new NotationType(names);
    }
    @Rule("name")
    protected List<String> notationTypeNames(String name)
    {
        List<String> list = new ArrayList<String>();
        list.add(name);
        return list;
    }

    @Rule({"notationTypeNames", "'\\|'", "name"})
    protected List<String> notationTypeNames(List<String> names, String name)
    {
        names.add(name);
        return names;
    }
    /**
     * [59]   	Enumeration	   ::=   	'(' S? Nmtoken (S? '|' S? Nmtoken)* S? ')'
     * @param name
     */
    @Rule({"'\\('", "enumerationNMTokens", "'\\)'"})
    protected AttType enumeration(List<String> names)
    {
        return new AttType.Enumeration(names);
    }
    @Rule("nmtoken")
    protected List<String> enumerationNMTokens(String name)
    {
        List<String> list = new ArrayList<>();
        list.add(name);
        return list;
    }

    @Rule({"enumerationNMTokens", "'\\|'", "nmtoken"})
    protected List<String> enumerationNMTokens(List<String> names, String name)
    {
        names.add(name);
        return names;
    }
    /**
     * [60]   	DefaultDecl	   ::=   	'#REQUIRED' | '#IMPLIED'
     *                                          | (('#FIXED' S)? AttValue)
     */
    @Rules({
    @Rule("defaultDeclRequired"),
    @Rule("defaultDeclImplied"),
    @Rule("defaultDeclFixed")
    })
    protected abstract DefaultDecl defaultDecl(DefaultDecl defaultDecl);

    @Rule("'#REQUIRED'")
    protected DefaultDecl defaultDeclRequired()
    {
        return DefaultDecl.REQUIRED;
    }
    @Rule("'#IMPLIED'")
    protected DefaultDecl defaultDeclImplied()
    {
        return DefaultDecl.IMPLIED;
    }

    @Rule({"'#FIXED'", "attValue"})
    protected DefaultDecl defaultDeclFixed(String value)
    {
        return new DefaultValue(value, true);
    }

    @Rule({"attValue"})
    protected DefaultDecl defaultDecl(String value)
    {
        return new DefaultValue(value, false);
    }

    public static List<Attribute> attributeListStart()
    {
        return new ArrayList<>();
    }
    
    public static List<Attribute> attributeListNext(List<Attribute> list, Attribute t)
    {
        list.add(t);
        return list;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        try
        {
            DTDParser parser = DTDParser .createDTDParser();
            File f = new File("c:\\temp\\XMLSchema.dtd");
            parser.parse("-//W3C//DTD XMLSCHEMA 200102//EN", "http://www.w3.org/2001/XMLSchema.dtd", f.toURI().toString());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
