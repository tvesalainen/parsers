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

package org.vesalainen.parsers.nmea;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.vesalainen.grammar.Grammar;
import org.vesalainen.parser.ParserFactory;
import org.vesalainen.parser.annotation.GenClassname;
import org.vesalainen.parser.annotation.GrammarDef;
import org.vesalainen.parser.annotation.ParseMethod;
import org.vesalainen.parser.annotation.ParserContext;
import org.vesalainen.parser.annotation.Rule;
import org.vesalainen.parser.annotation.Rules;
import org.vesalainen.parser.annotation.Terminal;
import org.vesalainen.regex.Regex;

/**
 * @author Timo Vesalainen
 */
@GenClassname("org.vesalainen.parsers.nmea.ais.AISGrammarGeneratorImpl")
@GrammarDef
@Rules({
    @Rule(left="structures", value="structure*"),
    @Rule(left="structure", value="text"),
    @Rule(left="structure", value="title"),
    @Rule(left="structure", value="table")
})
public abstract class AISGrammarGenerator
{
    protected String lastTitle;
    private Set<String> references = new HashSet<>();
    
    @Rule("tableTitle? start line+ end")
    protected void table(String title, List<List<String>> lines, @ParserContext("grammar") Grammar grammar)
    {
        Iterator<List<String>> iterator = lines.iterator();
        List<String> prev = null;
        while (iterator.hasNext())
        {
            List<String> next = iterator.next();
            if (next.size() == 1)
            {
                prev.set(prev.size()-1, prev.get(prev.size()-1)+" "+next.get(0).trim());
                iterator.remove();
            }
            else
            {
                prev = next;
            }
        }
        /*
        if (title != null && ".Message types".equals(title.trim()))
        {
            createEnum(".Message types", lines);
        }
        for (String ref : references)
        {
            if (title != null && ref.equals(title.trim()))
            {
                createEnum(ref, lines);
            }
        }
        */
        createMessageRule(grammar, lines);
    }
            
    @Rule("('\\|' cell?)+ '[\r\n]+'")
    protected List<String> line(List<String> cellList)
    {
        return cellList;
    }
            
    @Rule("cell '[\r\n]+'")
    protected List<String> line(String cell)
    {
        List<String> list = new ArrayList<>();
        list.add(cell);
        return list;
    }
            
    @Terminal(expression="=[^\n]*[\r\n]+")
    protected void title(String title)
    {
        lastTitle = title;
    }
    
    @Terminal(expression="\\.[^\n]*[\r\n]+")
    protected abstract String tableTitle(String title);
    
    @Terminal(expression="[^\\|\r\n]+")
    protected String cell(String text)
    {
        return text.trim();
    }
    
    @Terminal(expression="(\\[[^\n]*[\r\n]+)?\\|[=]+[\r\n]+")
    protected void start(String title)
    {
        System.err.println(title);
    }
    
    @Terminal(expression="\\|[=]+[\r\n]+")
    protected void end(String title)
    {
    }
    
    @Terminal(expression="[^=\\.\\[\\|].*[\n]{2}", options={Regex.Option.FIXED_ENDER})
    protected abstract void text();
    
    @ParseMethod(start="structures", size=1024)
    public abstract void parse(InputStream is, @ParserContext("grammar") Grammar grammar);
    
    public static AISGrammarGenerator newInstance()
    {
        return (AISGrammarGenerator) ParserFactory.getParserInstance(AISGrammarGenerator.class);
    }
    public Grammar parse()
    {
        Grammar g = new Grammar();
        String pkg = AISGrammarGenerator.class.getPackage().getName().replace('.', '/')+"/";
        InputStream is = AISGrammarGenerator.class.getClassLoader().getResourceAsStream(pkg+"AIVDM.txt");
        AISGrammarGenerator gen = AISGrammarGenerator.newInstance();
        gen.parse(is, g);
        return g;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        try
        {
            String pkg = AISGrammarGenerator.class.getPackage().getName().replace('.', '/')+"/";
            InputStream is = AISGrammarGenerator.class.getClassLoader().getResourceAsStream(pkg+"AIVDM.txt");
            AISGrammarGenerator gen = AISGrammarGenerator.newInstance();
            gen.parse(is, new Grammar());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void createMessageRule(Grammar grammar, List<List<String>> lines)
    {
        try
        {
            Iterator<List<String>> iterator = lines.iterator();
            List<String> header = iterator.next();
            if (check(header))
            {
                List<String> rhs = new ArrayList<>();
                while (iterator.hasNext())
                {
                    List<String> line = iterator.next();
                    String t = line.get(4);
                    if (t.startsWith("a"))
                    {
                        return;
                    }
                    int radix = 2;
                    if (t.toLowerCase().startsWith("i"))
                    {
                        radix = -2;
                    }
                    int[] len = bounds(line.get(1));
                    String description = line.get(2);
                    String member = line.get(3);
                    String units = line.get(5);
                    if ("Constant: 25".equals(units) || "Constant: 26".equals(units))
                    {
                        return;
                    }
                    if (units == null)
                    {
                        units = "";
                    }
                    String expression = createExpression(len, t, units);
                    if (member.isEmpty())
                    {
                        grammar.addAnonymousTerminal(expression);
                        rhs.add(expression);
                    }
                    else
                    {
                        Class<?> type = int.class;
                        if (len[0] > 32)
                        {
                            type = long.class;
                        }
                        String reducer = "ais"+camel(member);
                        grammar.addTerminal(
                                NMEAParser.class.getDeclaredMethod(reducer, type, AISData.class), 
                                member, 
                                expression, 
                                description, 
                                0, 
                                radix);
                        rhs.add(member);
                    }
                }
                grammar.addRule("aisMessage", rhs);
            }
        }
        catch (NoSuchMethodException ex)
        {
            throw new IllegalArgumentException(ex);
        }
    }

    private boolean check(List<String> header)
    {
        return (
                header.size() == 6 &&
                "Field".equals(header.get(0)) &&
                "Len".equals(header.get(1)) &&
                "Description".equals(header.get(2)) &&
                ("Member".equals(header.get(3)) || "Member/Type".equals(header.get(3))) &&
                "T".equals(header.get(4)) &&
                ("Units".equals(header.get(5)) || "Encoding".equals(header.get(5)))
                );
    }

    private String createExpression(int[] len, String t, String units)
    {
        if ("u".equals(t) && units.startsWith("Constant:"))
        {
            String c = units.substring(9).trim();
            int[] bounds = bounds(c);
            int from = bounds[0];
            int to = bounds[1];
            StringBuilder sb = new StringBuilder();
            for (int ii=from;ii<=to;ii++)
            {
                if (sb.length() > 0)
                {
                    sb.append("|");
                }
                String bin = Integer.toBinaryString(ii);
                for (int jj=bin.length();jj<len[0];jj++)
                {
                    sb.append("0");
                }
                sb.append(bin);
            }
            return sb.toString();
        }
        else
        {
            if (len.length == 1)
            {
                return "[01]{"+len+"}";
            }
            else
            {
                return "[01]{"+len[0]+","+len[1]+"}";
            }
        }
    }

    private int[] bounds(String s)
    {
        String[] ss = s.trim().split("-");
        if (ss.length == 1)
        {
            int l = Integer.parseInt(ss[0]);
            return new int[] {l, l};
        }
        else
        {
            int from = Integer.parseInt(ss[0]);
            int to = Integer.parseInt(ss[1]);
            return new int[] {from, to};
        }
    }
    private void checkReference(String units)
    {
        if (units.startsWith("See \"") && units.endsWith("\""))
        {
            String ref = units.substring(5, units.length()-1);
            references.add('.'+ref);
        }
    }

    private void createEnum(String title, List<List<String>> lines)
    {
        Set<String> set = new HashSet<>();
        int next = 0;
        title = camel(title.substring(1));
        System.err.println("public enum "+title);
        System.err.println("{");
        for (List<String> line : lines)
        {
            assert line.size() == 2;
            String code = line.get(0);
            if (!"Code".equals(code))
            {
                int[] bounds = bounds(line.get(0));
                String text = line.get(1);
                for (int e=bounds[0];e<=bounds[1];e++)
                {
                    assert e == next;
                    next++;
                    String camel = camel(text);
                    if (set.contains(camel))
                    {
                        camel = camel+e;
                    }
                    set.add(camel);
                    System.err.println("/**");
                    System.err.println(" * "+text);
                    System.err.println(" */");
                    System.err.println(camel+"(\""+text+"\"),");
                }
            }
        }
        System.err.println("private String description;");
        System.err.println(title+"(String description)");
        System.err.println("{");
        System.err.println("this.description = description;");
        System.err.println("}");
        System.err.println("public String toString()");
        System.err.println("{");
        System.err.println("return description;");
        System.err.println("}");
        System.err.println("}");
    }

    private String camel(String str)
    {
        StringBuilder sb = new StringBuilder();
        String[] ss = str.split("[ \\:\\(\\)\\,/\\-\\.\\=_]+");
        for (String s : ss)
        {
            if (!s.isEmpty())
            {
                sb.append(upperStart(s));
            }
        }
        return sb.toString();
    }
    private String upperStart(String str)
    {
        return str.substring(0, 1).toUpperCase()+str.substring(1);
    }
}
