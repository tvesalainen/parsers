/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.vesalainen.parsers.xml;

import org.vesalainen.parser.annotation.Rule;
import org.vesalainen.parser.annotation.Rules;
import org.vesalainen.parser.annotation.Terminal;
import org.vesalainen.regex.Regex;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.vesalainen.parser.ParserConstants;
import org.vesalainen.parser.annotation.ParseMethod;
import org.vesalainen.parser.annotation.ParserContext;
import org.vesalainen.parser.util.InputReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author tkv
 */
//@GrammarDef(start="document")
public abstract class XMLBaseGrammar
{
    public enum Context {COMMENT, CONTENT, ATTR_VALUE, AS_ATTR_VALUE, ENTITY_VALUE, DTD};
    public static final int BUFFERSIZE = 4096*2;

    //@GenRegex("<\\?[^\\?]*>")
    //protected static Regex TEXTDECL;
    /**
     * [2]   	Char	   ::=   	[#x1-#xD7FF] | [#xE000-#xFFFD] | [#x10000-#x10FFFF]
     */
    protected static final String Char = "\\x01-\\uD7FF\\uE000-\\uFFFD\\x{10000}-\\x{10FFFF}";
    /**
     * [2a]   	RestrictedChar	   ::=   	[#x1-#x8] | [#xB-#xC] | [#xE-#x1F] | [#x7F-#x84] | [#x86-#x9F]
     */
    protected static final String RestrictedChar = "\\x1-\\x8\\xB-\\xC\\xE-\\x1F\\x7F-\\x84\\x86-\\x9F";
    /**
     * [3]   	S	   ::=   	(#x20 | #x9 | #xD | #xA)+
     */
    protected static final String S = "\\x20\\x09\\x0A";    // #xD is taken care by eol() whiteSpace
    /**
     * [4]   	NameStartChar	   ::=   	":" | [A-Z] | "_" | [a-z] | [#xC0-#xD6] | [#xD8-#xF6] | [#xF8-#x2FF] | [#x370-#x37D] | [#x37F-#x1FFF] | [#x200C-#x200D] | [#x2070-#x218F] | [#x2C00-#x2FEF] | [#x3001-#xD7FF] | [#xF900-#xFDCF] | [#xFDF0-#xFFFD] | [#x10000-#xEFFFF]
     */
    protected static final String NameStartChar = ":A-Z_a-z\\xC0-\\xD6\\xD8-\\xF6\\xF8-\\u02FF\\u0370-\\u037D\\u037F-\\u1FFF\\u200C-\\u200D\\u2070-\\u218F\\u2C00-\\u2FEF\\u3001-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFFD\\x{10000}-\\x{10FFFF}";
    /**
     * [4a]   	NameChar	   ::=   	 NameStartChar | "-" | "." | [0-9] | #xB7 | [#x0300-#x036F] | [#x203F-#x2040]
     */
    protected static final String NameChar = NameStartChar+"\\-\\.0-9\\xB7\\u0300-\\u036F\\u203F-\\u2040";
    /**
     * [13]   	PubidChar	   ::=   	#x20 | #xD | #xA | [a-zA-Z0-9] | [-'()+,./:=?;!*#@$_%]
     */
    protected static final String PubidChar = "\\x20\\x0D\\x0Aa-zA-Z0-9\\-'\\(\\)\\+\\,\\./:=\\?;!\\*#@\\$_%";
    protected static final String PubidCharMinusApostrophe = "\\x20\\x0D\\x0Aa-zA-Z0-9\\-\\(\\)\\+\\,\\./:=\\?;!\\*#@\\$_%";

    /**
     * [4]   	NCNameStartChar	   ::=   	[A-Z] | "_" | [a-z] | [#xC0-#xD6] | [#xD8-#xF6] | [#xF8-#x2FF] | [#x370-#x37D] | [#x37F-#x1FFF] | [#x200C-#x200D] | [#x2070-#x218F] | [#x2C00-#x2FEF] | [#x3001-#xD7FF] | [#xF900-#xFDCF] | [#xFDF0-#xFFFD] | [#x10000-#xEFFFF]
     */
    protected static final String NCNameStartChar = "A-Z_a-z\\xC0-\\xD6\\xD8-\\xF6\\xF8-\\u02FF\\u0370-\\u037D\\u037F-\\u1FFF\\u200C-\\u200D\\u2070-\\u218F\\u2C00-\\u2FEF\\u3001-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFFD\\x{10000}-\\x{10FFFF}";
    /**
     * [4a]   	NameChar	   ::=   	 NameStartChar | "-" | "." | [0-9] | #xB7 | [#x0300-#x036F] | [#x203F-#x2040]
     */
    protected static final String NCNameChar = NCNameStartChar+"\\-\\.0-9\\xB7\\u0300-\\u036F\\u203F-\\u2040";

    protected InputReader inputReader;
    protected InputSource input;
    protected XMLLocator locator;
    protected Map<String,Object> generalReferences;
    protected Map<String,Object> parameterReferences;
    protected boolean external;

    public XMLBaseGrammar()
    {   
        generalReferences = new HashMap<>();
        parameterReferences = new HashMap<>();
        generalReferences.put("lt", "&<");
        generalReferences.put("gt", ">");
        generalReferences.put("amp", "&&");
        generalReferences.put("apos", "'");
        generalReferences.put("quot", "\"");
    }
    /**
     * 
     * @param reader 
     * @see <a href="doc-files/XMLBaseGrammar-extSubset.html#BNF">BNF Syntax for extSubset</a>
     */
    @ParseMethod(start="extSubset", whiteSpace={"eol", "s", "pi", "comment", "peReference"})
    protected abstract void parseExtSubset(InputReader reader);
    /**
     * 
     * @param reader 
     * @see <a href="doc-files/XMLBaseGrammar-document.html#BNF">BNF Syntax for document</a>
     */
    @ParseMethod(start="document", whiteSpace={"eol", "s"})
    protected abstract void parseDocument(InputReader reader);
    /**
     * 
     * @param reader
     * @return 
     * @see <a href="doc-files/XMLBaseGrammar-quotAttValueSub.html#BNF">BNF Syntax for quotAttValueSub</a>
     */
    @ParseMethod(start="quotAttValueSub", eof="singleQuot")
    protected abstract String parseQuotAttValue(InputReader reader);
    /**
     * 
     * @param reader
     * @return 
     * @see <a href="doc-files/XMLBaseGrammar-aposAttValueSub.html#BNF">BNF Syntax for aposAttValueSub</a>
     */
    @ParseMethod(start="aposAttValueSub", eof="singleApostrophe")
    protected abstract String parseAposAttValue(InputReader reader);
    /**
     * 
     * @param reader
     * @return 
     * @see <a href="doc-files/XMLBaseGrammar-quotEntityValueSub.html#BNF">BNF Syntax for quotEntityValueSub</a>
     */
    @ParseMethod(start="quotEntityValueSub", eof="singleQuot", whiteSpace={"peReference"})
    protected abstract String parseQuotEntityValue(InputReader reader);
    /**
     * 
     * @param reader
     * @return 
     * @see <a href="doc-files/XMLBaseGrammar-aposEntityValueSub.html#BNF">BNF Syntax for aposEntityValueSub</a>
     */
    @ParseMethod(start="aposEntityValueSub", eof="singleApostrophe", whiteSpace={"peReference"})
    protected abstract String parseAposEntityValue(InputReader reader);
    
    /**
     * [1]   	document	   ::=   	 ( prolog element Misc* ) - ( Char* RestrictedChar Char* )
     */
    @Rule({"prolog", "element", "misc*"})
    protected abstract void document() throws SAXException;
    /**
     * [5]   	Name	   ::=   	 NameStartChar (NameChar)*
     * @param name
     * @return
     */
    @Terminal(expression="["+NameStartChar+"]["+NameChar+"]*")
    protected abstract String name(String name);

    /**
     * [6]   	Names	   ::=   	 Name (#x20 Name)*
     */
    @Rule("name")
    protected List<String> names(String name)
    {
        List<String> res = new ArrayList<>();
        res.add(name);
        return res;
    }
    @Rule({"names", "' '", "name"})
    protected List<String> names(List<String> names, String name)
    {
        names.add(name);
        return names;
    }
    /**
     * [7]   	Nmtoken	   ::=   	(NameChar)+
     */
    @Terminal(expression="["+NameChar+"]+")
    protected abstract String nmtoken(String nmtoken);
    /**
     * [8]   	Nmtokens	   ::=   	 Nmtoken (#x20 Nmtoken)*
     */
    @Rule("nmtoken")
    protected List<String> nmtokens(String nmtoken)
    {
        List<String> res = new ArrayList<>();
        res.add(nmtoken);
        return res;
    }
    @Rule({"nmtokens", "' '", "nmtoken"})
    protected List<String> nmtokens(List<String> nmtokens, String nmtoken)
    {
        nmtokens.add(nmtoken);
        return nmtokens;
    }
    /**
     * [9]   	EntityValue	   ::=   	'"' ([^%&"] | PEReference | Reference)* '"'
     *                                          |  "'" ([^%&'] | PEReference | Reference)* "'"
     */
    @Rule("'\"'")
    protected String quotEntityValueStart(@ParserContext(ParserConstants.INPUTREADER) InputReader reader) throws IOException
    {
        String s = parseQuotEntityValue(reader);
        return s;
    }
    @Rule("`'´")
    protected String aposEntityValueStart(@ParserContext(ParserConstants.INPUTREADER) InputReader reader) throws IOException
    {
        return parseAposEntityValue(reader);
    }
    @Rules({
    @Rule({"quotEntityValueStart"}),
    @Rule({"aposEntityValueStart"})
    })
    protected String entityValue(String str) throws IOException
    {
        return str;
    }
    @Rule({"quotEntityValue"})
    protected String quotEntityValueSub(String str)
    {
        return str;
    }
    @Rule({"aposEntityValue"})
    protected String aposEntityValueSub(String str)
    {
        return str;
    }
    @Rule
    protected String quotEntityValue()
    {
        return "";
    }
    @Rule
    protected String aposEntityValue()
    {
        return "";
    }
    @Rules({
        @Rule({"quotEntityValue", "notQuotEntityValue"}),
        @Rule({"quotEntityValue", "reference"})
    })
    protected String quotEntityValue(String str, String value)
    {
        return str+value;
    }

    @Rules({
        @Rule({"aposEntityValue", "notAposEntityValue"}),
        @Rule({"aposEntityValue", "reference"})
    })
    protected String aposEntityValue(String str, String value)
    {
        return str+value;
    }
    /**
     * [10]   	AttValue	   ::=   	'"' ([^<&"] | Reference)* '"'
     *                                          |  "'" ([^<&'] | Reference)* "'"
     */
    @Rule("'\"'")
    protected String quotAttValueStart(@ParserContext(ParserConstants.INPUTREADER) InputReader reader) throws IOException
    {
        return parseQuotAttValue(reader);
    }
    @Rule("`'´")
    protected String aposAttValueStart(@ParserContext(ParserConstants.INPUTREADER) InputReader reader) throws IOException
    {
        return parseAposAttValue(reader);
    }
    @Rules({
    @Rule({"quotAttValueStart"}),
    @Rule({"aposAttValueStart"})
    })
    protected String attValue(String str) throws IOException
    {
       return str;
    }

    @Terminal(expression="\"")
    protected abstract void singleQuot();
    
    @Terminal(expression="'")
    protected abstract void singleApostrophe();
    
    @Rule({"quotAttValue"})
    protected String quotAttValueSub(String str)
    {
        return str;
    }
    @Rule({"aposAttValue"})
    protected String aposAttValueSub(String str)
    {
        return str;
    }
    @Rule
    protected String quotAttValue()
    {
        return "";
    }
    @Rule
    protected String aposAttValue()
    {
        return "";
    }
    @Rules({
        @Rule({"quotAttValue", "notQuotAttValue"}),
        @Rule({"quotAttValue", "reference"})
    })
    protected String quotAttValue(String str, String value)
    {
        return str+value;
    }

    @Rules({
        @Rule({"aposAttValue", "notAposAttValue"}),
        @Rule({"aposAttValue", "reference"})
    })
    protected String aposAttValue(String str, String value)
    {
        return str+value;
    }

    @Rules({
        @Rule({"'\"\"'"}),
        @Rule({"`'´'"})
    })
    protected String emptyQuote()
    {
        return "";
    }
    /**
     * [11]   	SystemLiteral	   ::=   	('"' [^"]* '"') | ("'" [^']* "'")
     */
    @Terminal(expression="\"[^\"]*\"|'[^']*'")
    protected String systemLiteral(String literal)
    {
        return literal.substring(1, literal.length()-1);
    }
    /**
     * [12]   	PubidLiteral	   ::=   	'"' PubidChar* '"' | "'" (PubidChar - "'")* "'"
     */
    @Rules({
        @Rule({"pubidLiteralWithApostrophe"}),
        @Rule({"pubidLiteralMinusApostrophe"})
    })
    protected abstract String pubidLiteral(String literal);

    @Terminal(expression="[^\\&\"]+")
    protected abstract String notQuotAttValue(String value);
    @Terminal(expression="[^\\&']+")
    protected abstract String notAposAttValue(String value);

    @Terminal(expression="[^%\\&\"]+")
    protected abstract String notQuotEntityValue(String value);
    @Terminal(expression="[^%\\&']+")
    protected abstract String notAposEntityValue(String value);

    @Terminal(expression="[^\"]+")
    protected abstract String notQuot(String value);
    @Terminal(expression="[^']+")
    protected abstract String notApos(String value);

    @Terminal(expression="\"["+PubidChar+"]*\"")
    protected String pubidLiteralWithApostrophe(String value)
    {
        return value.substring(1, value.length()-1);
    }
    @Terminal(expression="'["+PubidCharMinusApostrophe+"]*'")
    protected String pubidLiteralMinusApostrophe(String value)
    {
        return value.substring(1, value.length()-1);
    }
    /**
     * [14]   	CharData	   ::=   	[^<&]* - ([^<&]* ']]>' [^<&]*)
     */
    @Terminal(expression="[^<\\&\\x0d\\x85\\u2028]+")
    protected void charData(InputReader reader) throws SAXException
    {

    }

    /**
     * [15]   	Comment	   ::=   	'<!--' ((Char - '-') | ('-' (Char - '-')))* '-->'
     */
    @Terminal(expression="<!\\-\\-["+Char+"]*\\-\\->", options={Regex.Option.FIXED_ENDER})
    protected void comment(InputReader reader) throws SAXException
    {

    }

    @Terminal(expression="["+S+"]+")
    protected abstract void s();
    /**
     * [16]   	PI	   ::=   	'<?' PITarget (S (Char* - (Char* '?>' Char*)))? '?>'
     */
    
    @Terminal(expression="<\\?["+Char+"]*\\?>", options={Regex.Option.FIXED_ENDER})
    protected void pi()
    {
    }
    /**
     * [18]   	CDSect	   ::=   	 CDStart CData CDEnd
     * [19]   	CDStart	   ::=   	'<![CDATA['
     * [21]   	CDEnd	   ::=   	']]>'
     */

    /**
     * [20]   	CData	   ::=   	(Char* - (Char* ']]>' Char*))
     */
    @Terminal(expression="<!\\[CDATA\\[["+Char+"]*\\]\\]>", options={Regex.Option.FIXED_ENDER})
    protected void cdSect(InputReader r) throws SAXException
    {
        charData(r);
    }

    @Terminal(expression="[0-9]+")
    protected abstract int digit(int value);

    @Terminal(expression="[0-9a-fA-F]+")
    protected int hex(String value)
    {
        return Integer.parseInt(value, 16);
    }

    /**
     * [23]   	XMLDecl	   ::=   	'<?xml' VersionInfo EncodingDecl? SDDecl? S? '?>'
     */
    @Rule({"byteOrderMark?", "xmlPrefix", "versionInfo", "encodingDecl?", "sdDecl?", "'\\?>'"})
    protected abstract void xmlDecl();

    @Rules({
    @Rule("utf32BE"),
    @Rule("utf32LE"),
    @Rule("utf16BE"),
    @Rule("utf16LE"),
    @Rule("utf8")
    })
    protected abstract void byteOrderMark();

    @Rule({"'\\x00\\x00\\xFE\\xFF'"})
    protected void utf32BE() throws SAXParseException
    {
        try
        {
            setEncoding("UTF-32BE");
        }
        catch (IllegalArgumentException ex)
        {
            throw new SAXParseException("unsupported encoding", locator, ex);
        }
    }
    @Rule({"'\\xFF\\xFE\\x00\\x00'"})
    protected void utf32LE() throws SAXParseException
    {
        try
        {
            setEncoding("UTF-32LE");
        }
        catch (IllegalArgumentException ex)
        {
            throw new SAXParseException("unsupported encoding", locator, ex);
        }
    }
    @Rule("'\\xFE\\xFF'")
    protected void utf16BE() throws SAXParseException
    {
        try
        {
            setEncoding("UTF-16BE");
        }
        catch (IllegalArgumentException ex)
        {
            throw new SAXParseException("unsupported encoding", locator, ex);
        }
    }
    @Rule("'\\xFF\\xFE'")
    protected void utf16LE() throws SAXParseException
    {
        try
        {
            setEncoding("UTF-16LE");
        }
        catch (IllegalArgumentException ex)
        {
                      throw new SAXParseException("unsupported encoding", locator, ex);
        }
    }
    @Rule("'\\xEF\\xBB\\xBF'")
    protected void utf8() throws SAXParseException
    {
        try
        {
                 input.setEncoding("UTF-8");
        }
        catch (IllegalArgumentException ex)
        {
            throw new SAXParseException("unsupported encoding", locator, ex);
        }
    }
    @Rules({
    @Rule("utf32BEPrefix"),
    @Rule("utf32LEPrefix"),
    @Rule("utf16BEPrefix"),
    @Rule("utf16LEPrefix"),
    @Rule("utf8Prefix")
    })
    protected abstract void xmlPrefix();

    @Terminal(expression="\\x00\\x00\\x00<\\x00\\x00\\x00\\?\\x00\\x00\\x00x\\x00\\x00\\x00m\\x00\\x00\\x00l", options={Regex.Option.ACCEPT_IMMEDIATELY})
    protected void utf32BEPrefix() throws SAXParseException
    {
        try
        {
            setEncoding("UTF-32BE");
        }
        catch (IllegalArgumentException ex)
        {
            throw new SAXParseException("unsupported encoding", locator, ex);
        }
    }
    @Terminal(expression="<\\x00\\x00\\x00\\?\\x00\\x00\\x00x\\x00\\x00\\x00m\\x00\\x00\\x00l\\x00\\x00\\x00", options={Regex.Option.ACCEPT_IMMEDIATELY})
    protected void utf32LEPrefix() throws SAXParseException
    {
        try
        {
            setEncoding("UTF-32LE");
        }
        catch (IllegalArgumentException ex)
        {
            throw new SAXParseException("unsupported encoding", locator, ex);
        }
    }
    @Terminal(expression="\\x00<\\x00\\?\\x00x\\x00m\\x00l", options={Regex.Option.ACCEPT_IMMEDIATELY})
    protected void utf16BEPrefix() throws SAXParseException
    {
        try
        {
            setEncoding("UTF-16BE");
        }
        catch (IllegalArgumentException ex)
        {
            throw new SAXParseException("unsupported encoding", locator, ex);
        }
    }
    @Terminal(expression="<\\x00\\?\\x00x\\x00m\\x00l\\x00", options={Regex.Option.ACCEPT_IMMEDIATELY})
    protected void utf16LEPrefix() throws SAXParseException
    {
        try
        {
            setEncoding("UTF-16LE");
        }
        catch (IllegalArgumentException ex)
        {
            throw new SAXParseException("unsupported encoding", locator, ex);
        }
    }
    @Terminal(expression="<\\?xml", options={Regex.Option.ACCEPT_IMMEDIATELY})
    protected abstract void utf8Prefix();// doesn't set anything. Might be utf8, US-ASCII, ISO8859-X, ...

    /**
     * [24]   	VersionInfo	   ::=   	 S 'version' Eq ("'" VersionNum "'" | '"' VersionNum '"')
     */
    @Rule({"'version'", "eq", "versionNum"})
    protected void versionInfo(String version) throws SAXParseException
    {
        if (!"1.0".equals(version) && !"1.1".equals(version))
        {
            throw new SAXParseException("unsupported xml version "+version, locator);
        }
        locator.setXmlVersion(version);
    }
    /**
     * [26]   	VersionNum	   ::=   	'1.1'
     */
    @Rule({"version"})
    protected abstract String versionNum(String version);

    @Terminal(expression="\"[0-9]\\.[0-9]\"|'[0-9]\\.[0-9]'")
    protected String version(String version)
    {
        return version.substring(1, version.length()-1);
    }
    /**
     * [25]   	Eq	   ::=   	 S? '=' S?
     */
    @Rule({"'='"})
    protected abstract void eq();
    /**
     * [27]   	Misc	   ::=   	 Comment | PI | S
     */
    @Rule({"(comment|pi)"})
    protected abstract void misc();
    
    /**
     * [32]   	SDDecl	   ::=   	 S 'standalone' Eq (("'" ('yes' | 'no') "'") | ('"' ('yes' | 'no') '"'))
     */
    @Rules({
    @Rule({"'standalone'", "eq", "yesNo", "`'´"}),
    @Rule({"'standalone'", "eq", "yesNo", "'\"'"})
    })
    protected void sdDecl(boolean opt)
    {

    }
    @Terminal(expression="(yes)|(no)")
    protected boolean yesNo(String opt)
    {
        return "yes".equals(opt);
    }
    /**
     * [66]   	CharRef	   ::=  	'&#' [0-9]+ ';'
     *                                  | '&#x' [0-9a-fA-F]+ ';'
     * @param value
     * @return
     */
    @Rules({
        @Rule({"charRefDecimal"}),
        @Rule({"charRefHex"})
    })
    protected String charRef(int value)
    {
        return Character.toString((char)value);
    }
    
    @Terminal(expression="\\&#[0-9]+;")
    protected int charRefDecimal(String s)
    {
        return Integer.parseInt(s.substring(2, s.length()-1));
    }
    @Terminal(expression="\\&#x[0-9a-fA-F]+;")
    protected int charRefHex(String s)
    {
        return Integer.parseInt(s.substring(3, s.length()-1), 16);
    }
    /**
     * [67]   	Reference	   ::=   	 EntityRef | CharRef
     * @param value
     * @return
     */
    @Rules({
        @Rule("entityRef"),
        @Rule("charRef")
    })
    protected abstract String reference(String value);
    /**
     * [68]   	EntityRef	   ::=   	'&' Name ';'
     * @param name
     * @return
     * @throws SAXParseException
     */
    @Terminal(expression="\\&["+NameStartChar+"]["+NameChar+"]*;")
    protected String entityRef(String name) throws SAXParseException
    {
        Object res = generalReferences.get(name.substring(1, name.length()-1));
        if (res == null)
        {
            throw new SAXParseException("EntityRef "+name+" not defined", locator);
        }
        return res.toString();
    }
    /**
     * [69]   	PEReference	   ::=   	'%' Name ';'
     * @param name
     * @return
     * @throws SAXParseException
     */
    @Terminal(expression="%["+NameStartChar+"]["+NameChar+"]*;")
    protected String peReference(String name, @ParserContext(ParserConstants.INPUTREADER) InputReader reader) throws SAXParseException, IOException
    {
        Object res = parameterReferences.get(name.substring(1, name.length()-1));
        if (res == null)
        {
            throw new SAXParseException("PEReference "+name+" not defined", locator);
        }
        if (res instanceof ExternalEntity)
        {
            ExternalEntity ee = (ExternalEntity) res;
            reader.include(ee.createReader(), ee.getSystemId());
            return "";
        }
        return res.toString();
    }
    /**
     * [70]   	EntityDecl	   ::=   	 GEDecl | PEDecl
     */
    @Rules({
    @Rule("geDecl"),
    @Rule("peDecl")
    })
    protected abstract void entityDecl();

    /**
     * [71]   	GEDecl	   ::=   	'<!ENTITY' S Name S EntityDef S? '>'
     * @param name
     */
    @Rule({"'<!ENTITY'", "name", "entityDef", "'>'"})
    protected void geDecl(String name)
    {

    }
    /**
     * [72]   	PEDecl	   ::=   	'<!ENTITY' S '%' S Name S PEDef S? '>'
     * @param name
     */
    @Rule({"'<!ENTITY'", "'%'", "name", "peDef", "'>'"})
    protected void peDecl(String name, Object value) throws SAXException
    {
        Object old = parameterReferences.put(name, value);
    }
    /**
     * [73]   	EntityDef	   ::=   	 EntityValue | (ExternalID NDataDecl?)
     * @param value
     */
    @Rule("entityValue")
    protected void entityDef(String entityValue)
    {

    }
    @Rule({"externalID", "nDataDecl?"})
    protected void entityDef(String[] externalID)
    {

    }
    /**
     * [74]   	PEDef	   ::=   	 EntityValue | ExternalID
     * @param value
     */
    @Rule("entityValue")
    protected Object peDef(String entityValue)
    {
        return entityValue;
    }
    @Rule("externalID")
    protected Object peDef(String[] externalID) throws SAXException, IOException
    {
        return null;
    }
    /**
     * [75]   	ExternalID	   ::=   	'SYSTEM' S SystemLiteral
     *                                          | 'PUBLIC' S PubidLiteral S SystemLiteral
     * @param systemLiteral
     * @return
     */
    /**
     * (S ExternalID)?
     * @return
     */
    @Rule
    protected String[] optExternalID()
    {
        return new String[] {null, null};
    }
    @Rule({"externalID"})
    protected String[] optExternalID(String[] externalID)
    {
        return externalID;
    }
    @Rule({"'SYSTEM'", "systemLiteral"})
    protected String[] externalID(String systemLiteral)
    {
        return new String[] {null, systemLiteral};
    }
    @Rule({"'PUBLIC'", "pubidLiteral", "systemLiteral"})
    protected String[] externalID(String pubidLiteral, String systemLiteral)
    {
        return new String[] {pubidLiteral, systemLiteral};
    }
    /**
     * [76]   	NDataDecl	   ::=   	 S 'NDATA' S Name
     * @param name
     */
    @Rule({"'NDATA'", "name"})
    protected void nDataDecl(String name)
    {

    }
    /**
     * [80]   	EncodingDecl	   ::=   	 S 'encoding' Eq ('"' EncName '"' | "'" EncName "'" )
     * @param encName
     */
    @Rules({
    @Rule({"'encoding'", "eq", "'\"'", "encName", "'\"'"}),
    @Rule({"'encoding'", "eq", "`'´", "encName", "`'´"})
    })
    protected void encodingDecl(String charset)
    {
        setEncoding(charset);
    }
    /**
     * [81]   	EncName	   ::=   	[A-Za-z] ([A-Za-z0-9._] | '-')*
     * @param name
     * @return
     */
    @Terminal(expression="[A-Za-z][A-Za-z0-9\\._\\-]*")
    protected abstract String encName(String name);

    /**
     * [82]   	NotationDecl	   ::=   	'<!NOTATION' S Name S (ExternalID | PublicID) S? '>'
     * @param name
     * @param id
     */
    @Rule({"'<!NOTATION'", "name", "externalID", "'>'"})
    protected void notationDecl(String name, String[] externalID) throws SAXException
    {
    }
    @Rule({"'<!NOTATION'", "name", "publicID", "'>'"})
    protected void notationDecl(String name, String publicID) throws SAXException
    {
    }
    /**
     * [83]   	PublicID	   ::=   	'PUBLIC' S PubidLiteral
     * @param id
     * @return
     */
    @Rule({"'PUBLIC'", "pubidLiteral"})
    protected abstract String publicID(String id);

    /**
     * Namespace
     */

    /**
     * [1]   	NSAttName	   ::=   	PrefixedAttName
     *                                          | DefaultAttName
     */
    @Terminal(expression="xmlns(:["+NCNameStartChar+"]["+NCNameChar+"]*)?", priority=1)
    protected String nsAttName(String s) throws IOException
    {
        int idx = s.indexOf(':');
        if (idx == -1)
        {
            return "";
        }
        else
        {
            return s.substring(idx+1);
        }
    }


    /**
     * [4]   	NCName	   ::=   	NCNameStartChar NCNameChar*
     * @param name
     * @return
     */
    /*
    @Terminal(expression="["+NCNameStartChar+"]["+NCNameChar+"]*")
    protected abstract String ncName(String name);

     * 
     */
    @Terminal(expression="<["+NCNameStartChar+"]["+NCNameChar+"]*(:["+NCNameStartChar+"]["+NCNameChar+"]*)?")
    protected QName sTagName(String s) throws IOException
    {
        return new QName(s.substring(1));
    }
    @Terminal(expression="</["+NCNameStartChar+"]["+NCNameChar+"]*(:["+NCNameStartChar+"]["+NCNameChar+"]*)?["+S+"]*>")
    protected QName eTagName(String s) throws IOException
    {
        return new QName(s.substring(2));
    }
    @Terminal(expression="["+NCNameStartChar+"]["+NCNameChar+"]*(:["+NCNameStartChar+"]["+NCNameChar+"]*)?")
    protected QName qName(String s) throws IOException
    {
        return new QName(s);
    }

    private static char[] eolChar = new char[] {'\n'};
    
    @Terminal(expression="\\x0d\\x0a|\\x0d\\x85|\\x85|\\x0d|\\u2028")
    protected char[] eol() throws IOException
    {
        return eolChar;
    }

    public String getEncoding()
    {
        return input.getEncoding();
    }


    private void setEncoding(String charset)
    {
        input.setEncoding(charset);
        inputReader.setCharset(charset);
    }

    public String getPublicId()
    {
        return input.getPublicId();
    }

    public void setPublicId(String publicId)
    {
        input.setPublicId(publicId);
    }

    public String getSystemId()
    {
        return input.getSystemId();
    }

    public void setSystemId(String systemId) throws URISyntaxException
    {
        input.setSystemId(systemId);
    }

    public String getXmlVersion()
    {
        return locator.getXMLVersion();
    }

    public void setXmlVersion(String xmlVersion)
    {
        locator.setXmlVersion(xmlVersion);
    }

    public void setExternal(boolean external)
    {
        this.external = external;
    }
}
