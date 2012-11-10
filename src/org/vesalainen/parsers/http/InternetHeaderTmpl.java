/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.vesalainen.parsers.http;

import org.vesalainen.parser.ParserCompiler;
import org.vesalainen.parser.annotation.GrammarDef;
import org.vesalainen.parser.annotation.ParseMethod;
import org.vesalainen.parser.annotation.ParserContext;
import org.vesalainen.parser.annotation.Rule;
import org.vesalainen.parser.annotation.Terminal;
import org.vesalainen.parser.annotation.Terminals;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.vesalainen.parser.annotation.GenClassname;

/**
 * InternetHeader class parses header fields used for example in HTTP and SMTP
 * protocols. Simplified grammar is:
 *
 * <p>messageHeader ::= messageHeaders CRLF
 * <p>messageHeaders ::=
 * <p>messageHeaders ::= fieldName COLON fieldValue CRLF
 * <p>fieldName ::= token
 * <p>fieldValue ::= line
 * <p>fieldValue ::= line LWS fieldValue
 *
 * <p>
 * @author tkv
 */
@GenClassname("org.vesalainen.parsers.http.InternetHeaderParser")
@GrammarDef()
@Terminals({
@Terminal(left="COLON", expression="[ \t]*:[ \t]*"),
@Terminal(left="CRLF", expression="\r\n"),
@Terminal(left="LWS", expression="\r\n[ \t]+")
})
public abstract class InternetHeaderTmpl
{
    public Map<String,List<String>> parse(String str)
    {
        Map<String,List<String>> hdr = new HashMap<>();
        parse(str, hdr);
        return hdr;
    }
    public Map<String,List<String>> parse(InputStream is)
    {
        Map<String,List<String>> hdr = new HashMap<>();
        parse(is, hdr);
        return hdr;
    }
    @ParseMethod(start="messageHeader", size=80)
    protected abstract Map<String,List<String>> parse(String str, @ParserContext Map<String,List<String>> hdr);

    @ParseMethod(start="messageHeader", size=80)
    protected abstract Map<String,List<String>> parse(InputStream is, @ParserContext Map<String,List<String>> hdr);

    @Rule({"messageHeaders", "CRLF"})
    protected void messageHeader(@ParserContext Map<String,List<String>> hdr)
    {
    }
    @Rule()
    protected void messageHeaders()
    {
    }
    @Rule({"messageHeaders", "fieldName", "COLON", "fieldValue", "CRLF"})
    protected void messageHeaders(String fieldName, String fieldValue, @ParserContext Map<String,List<String>> hdr)
    {
        List<String> list = hdr.get(fieldName);
        if (list == null)
        {
            list = new ArrayList<>();
            hdr.put(fieldName, list);
        }
        list.add(fieldValue);
    }
    @Terminal(expression="[^\\x00-\\x20\\(\\)<>@\\,;:\\\\\"/\\[\\]\\?=\\{\\}\t]+")
    protected abstract String fieldName(String name);

    @Terminal(expression="[^\r\n]+")
    protected abstract String line(String value);

    @Rule({"line"})
    protected String fieldValue(String s1)
    {
        return s1;
    }
    @Rule({"line", "LWS", "fieldValue"})
    protected String fieldValue(String s1, String s2)
    {
        return s1+" "+s2;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        try
        {
            ParserCompiler pc = new ParserCompiler(InternetHeaderTmpl.class);
            pc.compile();
            InternetHeaderTmpl ih = (InternetHeaderTmpl) pc.parserInstance();
            String sr = "a: a-hdr\r\nb: b-hdr\r\nc: c-hdr\r\n c-cont1\r\n c-cont2\r\n\r\n";
            Map<String,List<String>> hdr = ih.parse(sr);
            System.err.println(hdr);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
