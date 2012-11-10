/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vesalainen.parsers.date;

import org.vesalainen.bcc.MethodCompiler;
import org.vesalainen.parser.annotation.GrammarDef;
import org.vesalainen.parser.annotation.ParseMethod;
import org.vesalainen.parser.annotation.ParserContext;
import org.vesalainen.parser.annotation.Rule;
import org.vesalainen.parser.annotation.Rules;
import org.vesalainen.parser.annotation.Terminal;
import org.vesalainen.regex.Regex;
import java.io.IOException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import org.vesalainen.bcc.MethodImplementor;
import org.vesalainen.bcc.type.ClassWrapper;
import org.vesalainen.bcc.type.MethodWrapper;
import org.vesalainen.grammar.Grammar;
import org.vesalainen.parser.ParserFactory;
import org.vesalainen.parser.annotation.GenClassname;

/**
 * DateFormatGrammar is an abstract base class for DateFormatParser. Generated
 * DateFormatParser parses java.text.SimpleDateFormat style patterns and creates
 * grammar rules actual date parser.
 * @author tkv
 */
@GenClassname("org.vesalainen.parsers.date.DateFormatParser")
@GrammarDef()
public abstract class DateFormatGrammar
{
    private Class<?> superClass;

    public void setSuperClass(Class<?> superClass)
    {
        this.superClass = superClass;
    }
    
    public static DateFormatGrammar newInstance(Class<?> superClass)
    {
        DateFormatGrammar dfg = (DateFormatGrammar) ParserFactory.getParserInstance(DateFormatGrammar.class);
        dfg.setSuperClass(superClass);
        return dfg;
    }
    @ParseMethod(start="rhs")
    public abstract List<String> parse(
            String format,
            @ParserContext("GRAMMAR") Grammar grammar,
            @ParserContext("LOCALE") Locale locale,
            @ParserContext("SYMBOLS") DateFormatSymbols symbols,
            @ParserContext("ERA") Method[] era,
            @ParserContext("MONTH") Method[] month,
            @ParserContext("WEEKDAY") Method[] weekday,
            @ParserContext("AMPM") Method[] ampm
            ) throws IOException;
    @Rule()
    protected List<String> rhs()
    {
        return new ArrayList<>();
    }
    @Rules({
    @Rule({"rhs", "literal"}),
    @Rule({"rhs", "delimiter"})
    })
    protected List<String> rhs(
            List<String> rhs, 
            String literal, 
            @ParserContext("GRAMMAR") Grammar grammar)
    {
        rhs.add(Regex.escape(literal));
        return rhs;
    }
    @Rule(left="rhs", value={"rhs", "tG"})
    protected List<String> era(
            List<String> rhs, String name,
            @ParserContext("GRAMMAR") Grammar grammar,
            @ParserContext("SYMBOLS") DateFormatSymbols symbols,
            @ParserContext("ERA") Method[] era)
    {
        if (!grammar.hasNonterminal(name))
        {
            addRules(grammar, name, symbols.getEras(), era);
            rhs.add(name);
        }
        else
        {
            rhs.add(name);
        }
        return rhs;
    }
    @Rule(left="rhs", value={"rhs", "ty"})
    protected List<String> year(List<String> rhs, String name, @ParserContext("GRAMMAR") Grammar grammar) throws NoSuchMethodException
    {
        if (!grammar.hasNonterminal(name))
        {
            int len = name.length();
            if (len == 2)
            {
                grammar.addRule(name, "year2");
            }
            else
            {
                grammar.addRule(name, "year4");
            }
        }
        rhs.add(name);
        return rhs;
    }
    @Rule(left="rhs", value={"rhs", "tM"})
    protected List<String> month(List<String> rhs, String name, 
            @ParserContext("GRAMMAR") Grammar grammar,
            @ParserContext("SYMBOLS") DateFormatSymbols symbols,
            @ParserContext("MONTH") Method[] month) throws NoSuchMethodException
    {
        int len = name.length();
        if (len >= 3)
        {
            name = "MMM";
        }
        else
        {
            name = "MM";
        }
        if (!grammar.hasNonterminal(name))
        {
            if (len >= 3)
            {
                addRules(grammar, name, symbols.getShortMonths(), symbols.getMonths(), month);
            }
            else
            {
                grammar.addRule(name, "month");
            }
        }
        rhs.add(name);
        return rhs;
    }
    @Rule(left="rhs", value={"rhs", "tw"})
    protected List<String> weekInYear(List<String> rhs, String name, @ParserContext("GRAMMAR") Grammar grammar) throws NoSuchMethodException
    {
        if (!grammar.hasNonterminal(name))
        {
            grammar.addRule(name, "weekInYear");
        }
        rhs.add(name);
        return rhs;
    }
    @Rule(left="rhs", value={"rhs", "tW"})
    protected List<String> weekInMonth(List<String> rhs, String name, @ParserContext("GRAMMAR") Grammar grammar) throws NoSuchMethodException
    {
        if (!grammar.hasNonterminal(name))
        {
            grammar.addRule(name, "weekInMonth");
        }
        rhs.add(name);
        return rhs;
    }
    @Rule(left="rhs", value={"rhs", "tD"})
    protected List<String> dayInYear(List<String> rhs, String name, @ParserContext("GRAMMAR") Grammar grammar) throws NoSuchMethodException
    {
        if (!grammar.hasNonterminal(name))
        {
            grammar.addRule(name, "dayInYear");
        }
        rhs.add(name);
        return rhs;
    }
    @Rule(left="rhs", value={"rhs", "td"})
    protected List<String> dayInMonth(List<String> rhs, String name, @ParserContext("GRAMMAR") Grammar grammar) throws NoSuchMethodException
    {
        if (!grammar.hasNonterminal(name))
        {
            grammar.addRule(name, "dayInMonth");
        }
        rhs.add(name);
        return rhs;
    }
    @Rule(left="rhs", value={"rhs", "tF"})
    protected List<String> dayOfWeekInMonth(List<String> rhs, String name, @ParserContext("GRAMMAR") Grammar grammar) throws NoSuchMethodException
    {
        if (!grammar.hasNonterminal(name))
        {
            grammar.addRule(name, "dayInWeekInMonth");
        }
        rhs.add(name);
        return rhs;
    }
    @Rule(left="rhs", value={"rhs", "tE"})
    protected List<String> dayInWeek(
            List<String> rhs,
            String name,
            @ParserContext("GRAMMAR") Grammar grammar,
            @ParserContext("SYMBOLS") DateFormatSymbols symbols,
            @ParserContext("WEEKDAY") Method[] weekday) throws NoSuchMethodException
    {
        if (!grammar.hasNonterminal(name))
        {
            addRules(grammar, name, symbols.getShortWeekdays(), symbols.getWeekdays(), weekday);
        }
        rhs.add(name);
        return rhs;
    }
    @Rule(left="rhs", value={"rhs", "ta"})
    protected List<String> ampm(
            List<String> rhs,
            String name,
            @ParserContext("GRAMMAR") Grammar grammar,
            @ParserContext("SYMBOLS") DateFormatSymbols symbols,
            @ParserContext("AMPM") Method[] ampm) throws NoSuchMethodException
    {
        if (!grammar.hasNonterminal(name))
        {
            addRules(grammar, name, symbols.getAmPmStrings(), ampm);
        }
        rhs.add(name);
        return rhs;
    }
    @Rule(left="rhs", value={"rhs", "tH"})
    protected List<String> hour23(List<String> rhs, String name, @ParserContext("GRAMMAR") Grammar grammar) throws NoSuchMethodException
    {
        if (!grammar.hasNonterminal(name))
        {
            grammar.addRule(name, "hour23");
        }
        rhs.add(name);
        return rhs;
    }
    @Rule(left="rhs", value={"rhs", "tk"})
    protected List<String> hour24(List<String> rhs, String name, @ParserContext("GRAMMAR") Grammar grammar) throws NoSuchMethodException
    {
        if (!grammar.hasNonterminal(name))
        {
            grammar.addRule(name, "hour24");
        }
        rhs.add(name);
        return rhs;
    }
    @Rule(left="rhs", value={"rhs", "tK"})
    protected List<String> hour11(List<String> rhs, String name, @ParserContext("GRAMMAR") Grammar grammar) throws NoSuchMethodException
    {
        if (!grammar.hasNonterminal(name))
        {
            grammar.addRule(name, "hour11");
        }
        rhs.add(name);
        return rhs;
    }
    @Rule(left="rhs", value={"rhs", "th"})
    protected List<String> hour12(List<String> rhs, String name, @ParserContext("GRAMMAR") Grammar grammar) throws NoSuchMethodException
    {
        if (!grammar.hasNonterminal(name))
        {
            grammar.addRule(name, "hour12");
        }
        rhs.add(name);
        return rhs;
    }
    @Rule(left="rhs", value={"rhs", "tm"})
    protected List<String> minute(List<String> rhs, String name, @ParserContext("GRAMMAR") Grammar grammar) throws NoSuchMethodException
    {
        if (!grammar.hasNonterminal(name))
        {
            grammar.addRule(name, "minute");
        }
        rhs.add(name);
        return rhs;
    }
    @Rule(left="rhs", value={"rhs", "ts"})
    protected List<String> second(List<String> rhs, String name, @ParserContext("GRAMMAR") Grammar grammar) throws NoSuchMethodException
    {
        if (!grammar.hasNonterminal(name))
        {
            grammar.addRule(name, "second");
        }
        rhs.add(name);
        return rhs;
    }
    @Rule(left="rhs", value={"rhs", "tS"})
    protected List<String> millis(List<String> rhs, String name, @ParserContext("GRAMMAR") Grammar grammar) throws NoSuchMethodException
    {
        if (!"SSS".equals(name))
        {
            throw new IllegalArgumentException(name+" for milliseconds illegal");
        }
        if (!grammar.hasNonterminal(name))
        {
            grammar.addRule(name, "milliSecond");
        }
        rhs.add(name);
        return rhs;
    }
    @Rule(left="rhs", value={"rhs", "tz"})
    protected List<String> generalTZ(List<String> rhs, String name, @ParserContext("GRAMMAR") Grammar grammar) throws NoSuchMethodException
    {
        if (!grammar.hasNonterminal(name))
        {
            createTZRules(name, grammar);
        }
        rhs.add(name);
        return rhs;
    }
    @Rule(left="rhs", value={"rhs", "tX"})
    protected List<String> iso8601(List<String> rhs, String name, @ParserContext("GRAMMAR") Grammar grammar) throws NoSuchMethodException
    {
        if (!grammar.hasNonterminal(name))
        {
            switch (name)
            {
                case "X":
                    grammar.addRule(name, "iso8601OneDigit");
                    break;
                case "XX":
                    grammar.addRule(name, "iso8601TwoDigit");
                    break;
                case "XXX":
                    grammar.addRule(name, "iso8601ThreeDigit");
                    break;
                default:
                    throw new IllegalArgumentException(name+" illegal tz");
            }
        }
        rhs.add(name);
        return rhs;
    }
    @Rule(left="rhs", value={"rhs", "tZ"})
    protected List<String> rfc822TZ(List<String> rhs, String name, @ParserContext("GRAMMAR") Grammar grammar) throws NoSuchMethodException
    {
        return timezone(rhs, grammar);
    }
    private List<String> timezone(List<String> rhs, Grammar grammar) throws NoSuchMethodException
    {
        String name = "Z";
        if (!grammar.hasNonterminal(name))
        {
            grammar.addRule(name, "rfc822");
        }
        rhs.add(name);
        return rhs;
    }
    @Terminal(expression="'[^']*'")
    protected String literal(String text, @ParserContext("GRAMMAR") Grammar grammar)
    {
        return text;
    }
    @Terminal(expression="[^A-Za-z']+")
    protected String delimiter(String text)
    {
        return "'"+text+"'";
    }
    @Terminal(expression="G+")
    protected String tG(String text)
    {
        return text;
    }
    @Terminal(expression="y+")
    protected String ty(String text)
    {
        return text;
    }
    @Terminal(expression="M+")
    protected String tM(String text)
    {
        return text;
    }
    @Terminal(expression="w+")
    protected String tw(String text)
    {
        return text;
    }
    @Terminal(expression="W+")
    protected String tW(String text)
    {
        return text;
    }
    @Terminal(expression="D+")
    protected String tD(String text)
    {
        return text;
    }
    @Terminal(expression="d+")
    protected String td(String text)
    {
        return text;
    }
    @Terminal(expression="F+")
    protected String tF(String text)
    {
        return text;
    }
    @Terminal(expression="E+")
    protected String tE(String text)
    {
        return text;
    }
    @Terminal(expression="a+")
    protected String ta(String text)
    {
        return text;
    }
    @Terminal(expression="H+")
    protected String tH(String text)
    {
        return text;
    }
    @Terminal(expression="k+")
    protected String tk(String text)
    {
        return text;
    }
    @Terminal(expression="K+")
    protected String tK(String text)
    {
        return text;
    }
    @Terminal(expression="h+")
    protected String th(String text)
    {
        return text;
    }
    @Terminal(expression="m+")
    protected String tm(String text)
    {
        return text;
    }
    @Terminal(expression="s+")
    protected String ts(String text)
    {
        return text;
    }
    @Terminal(expression="S+")
    protected String tS(String text)
    {
        return text;
    }
    @Terminal(expression="z+")
    protected String tz(String text)
    {
        return text;
    }
    @Terminal(expression="Z+")
    protected String tZ(String text)
    {
        return text;
    }
    @Terminal(expression="X+")
    protected String tX(String text)
    {
        return text;
    }
    private static void addRules(Grammar grammar, String nt, String[] choice, Method[] reducer)
    {
        int c;
        if (choice[0].isEmpty())
        {
            c = 1;
        }
        else
        {
            c = 0;
        }
        for (int ii=0;ii<reducer.length;ii++)
        {
            grammar.addRule(reducer[ii], nt, "'"+Regex.escape(choice[c])+"'");
            c++;
        }
    }
    private static void addRules(Grammar grammar, String nt, String[] shortChoice, String[] longChoice, Method[] reducer)
    {
        int c;
        if (shortChoice[0].isEmpty())
        {
            c = 1;
        }
        else
        {
            c = 0;
        }
        for (int ii=0;ii<reducer.length;ii++)
        {
            if (longChoice[c].startsWith(shortChoice[c]))
            {
                String expr = makeSuffixOptional(longChoice[c], shortChoice[c].length());
                grammar.addRule(reducer[ii], nt, "'"+expr+"'");
            }
            else
            {
                String expr = Regex.escape(longChoice[c]);
                grammar.addRule(reducer[ii], nt, "'"+expr+"'");
                expr = Regex.escape(shortChoice[c]);
                grammar.addRule(reducer[ii], nt, "'"+expr+"'");
            }
            c++;
        }
    }
    private static String makeSuffixOptional(String s, int prefix)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(Regex.escape(s.substring(0, prefix)));
        for (int ii=prefix;ii<s.length();ii++)
        {
            sb.append("|");
            sb.append(Regex.escape(s.substring(0, ii+1)));
        }
        return sb.toString();
    }

    private void createTZRules(String name, Grammar grammar)
    {
        grammar.addRule("z", "generalTZ");
        Map<Integer,StringBuilder> map = new HashMap<>();
        Set<String> set = new HashSet<>();
        for (String id : TimeZone.getAvailableIDs())
        {
            TimeZone tz = TimeZone.getTimeZone(id);
            int offset = tz.getRawOffset();
            StringBuilder sb = map.get(offset);
            if (sb == null)
            {
                sb = new StringBuilder();
                map.put(offset, sb);
            }
            appendTZ(set, sb, id);
            id = tz.getDisplayName(false, TimeZone.SHORT);
            appendTZ(set, sb, id);
            id = tz.getDisplayName(false, TimeZone.LONG);
            appendTZ(set, sb, id);
            int dst = tz.getDSTSavings();
            if (dst == 0 || dst == 3600000)
            {
                offset += 3600000;
            }
            else
            {
                offset += dst;
            }
            sb = map.get(offset);
            if (sb == null)
            {
                sb = new StringBuilder();
                map.put(offset, sb);
            }
            id = tz.getDisplayName(true, TimeZone.SHORT);
            appendTZ(set, sb, id);
            id = tz.getDisplayName(true, TimeZone.LONG);
            appendTZ(set, sb, id);
        }
        for (char mtz='A';mtz<='Z';mtz++)
        {
            switch (mtz)
            {
                case 'Z':
                    addMTZ(map, set, mtz, 0);
                    break;
                case 'A':
                case 'B':
                case 'C':
                case 'D':
                case 'E':
                case 'F':
                case 'G':
                case 'H':
                case 'I':
                case 'J':
                case 'K':
                case 'L':
                case 'M':
                    addMTZ(map, set, mtz, mtz-'A'+1);
                    break;
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
                case 'S':
                case 'T':
                case 'U':
                case 'V':
                case 'W':
                case 'X':
                case 'Y':
                    addMTZ(map, set, mtz, -(mtz-'N'+1));
                    break;
            }
            
        }
        for (int offset : map.keySet())
        {
            TZOffsetMethod om = new TZOffsetMethod(offset);
            om.setImplementor(om);
            StringBuilder sb = map.get(offset);
            if (sb.length() > 0)
            {
                grammar.addRule(om, "tzName", "'"+Grammar.literal(sb.toString())+"'");
            }
        }
    }

    private void addMTZ(Map<Integer, StringBuilder> map, Set<String> set, char mtz, int hours)
    {
        int offset = hours*60*60*1000;
        StringBuilder sb = map.get(offset);
        appendTZ(set, sb, String.valueOf(mtz));
    }

    private void appendTZ(Set<String> set, StringBuilder sb, String id)
    {
        if (!set.contains(id) && (id.equals("GMT") || id.equals("GMT0") || !id.startsWith("GMT")))
        {
            set.add(id);
            if (sb.length() > 0)
            {
                sb.append('|');
            }
            sb.append(Regex.escape(id));
        }
    }

    private class TZOffsetMethod extends MethodWrapper implements MethodImplementor
    {
        private int offset;

        public TZOffsetMethod(int offset)
        {
            this(offset, "tzOffset"+offset);
        }
        private TZOffsetMethod(int offset, String name)
        {
            super(
                    Modifier.PRIVATE, 
                    ClassWrapper.wrap(superClass.getAnnotation(GenClassname.class).value(), superClass), 
                    name.replace('-', '_'), 
                    int.class
                    );
            this.offset = offset;
        }

        @Override
        public void implement(MethodCompiler mc, Member mw) throws IOException
        {
            mc.iconst(offset);
            mc.ireturn();
            mc.end();
        }
        
    }
}
