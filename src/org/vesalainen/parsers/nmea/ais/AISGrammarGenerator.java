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

package org.vesalainen.parsers.nmea.ais;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.vesalainen.parser.ParserFactory;
import org.vesalainen.parser.annotation.GenClassname;
import org.vesalainen.parser.annotation.GrammarDef;
import org.vesalainen.parser.annotation.ParseMethod;
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
    
    @Rule("tableTitle? start line+ end")
    protected void table(String title, List<List<String>> lines)
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
        if (title != null)
        {
            System.err.println(title);
        }
        else
        {
            System.err.println(lastTitle);
        }
        for (List<String> list : lines)
        {
            System.err.println(list);
        }
        System.err.println();
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
    protected abstract String cell(String text);
    
    @Terminal(expression="(\\[[^\n]*[\r\n]+)?\\|[=]+[\r\n]+")
    protected void start(String title)
    {
        System.err.println(title);
    }
    
    @Terminal(expression="\\|[=]+[\r\n]+")
    protected void end(String title)
    {
        System.err.println(title);
    }
    
    @Terminal(expression="[^=\\.\\[\\|].*[\n]{2}", options={Regex.Option.FIXED_ENDER})
    protected abstract void text();
    
    @ParseMethod(start="structures", size=1024)
    public abstract void parse(InputStream is);
    
    public static AISGrammarGenerator newInstance()
    {
        return (AISGrammarGenerator) ParserFactory.getParserInstance(AISGrammarGenerator.class);
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
            gen.parse(is);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
