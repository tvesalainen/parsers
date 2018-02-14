/*
 * Copyright (C) 2018 Timo Vesalainen <timo.vesalainen@iki.fi>
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
package org.vesalainen.parsers.printf;

import java.util.List;
import org.vesalainen.parser.GenClassFactory;
import org.vesalainen.parser.annotation.GenClassname;
import org.vesalainen.parser.annotation.GrammarDef;
import org.vesalainen.parser.annotation.ParseMethod;
import org.vesalainen.parser.annotation.ParserContext;
import org.vesalainen.parser.annotation.Rule;
import org.vesalainen.parser.annotation.Terminal;
import org.vesalainen.parsers.printf.FormatFactory.FormatPart;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
@GenClassname("org.vesalainen.parsers.printf.PrintfParserImpl")
@GrammarDef()
public abstract class PrintfParser
{
    public static PrintfParser getInstance()
    {
        return (PrintfParser) GenClassFactory.getGenInstance(PrintfParser.class);
    }
    @ParseMethod(start="formatList")
    public abstract List<FormatPart> parse(String text, @ParserContext("FormatFactory") FormatFactory factory);
    
    @Rule("(format|format2)+")
    protected abstract List<FormatPart> formatList(List<FormatPart> list);
    
    @Rule("'%' conversion") //---
    protected FormatPart format(char conversion, @ParserContext("FormatFactory") FormatFactory factory)
    {
        return factory.getInstance('x', -1, -1, conversion);
    }    
    @Rule("'%' flags conversion")   // x--
    protected FormatPart format(char flags, char conversion, @ParserContext("FormatFactory") FormatFactory factory)
    {
        return factory.getInstance(flags, -1, -1, conversion);
    }    
    @Rule("'%' flags integer conversion")   // xx-
    protected FormatPart format(char flags, int width, char conversion, @ParserContext("FormatFactory") FormatFactory factory)
    {
        return factory.getInstance(flags, width, -1, conversion);
    }    
    @Rule("'%' flags integer '\\.' integer conversion") // xxx
    protected FormatPart format2(char flags, int width, int precision, char conversion, @ParserContext("FormatFactory") FormatFactory factory)
    {
        return factory.getInstance(flags, width, precision, conversion);
    }    
    @Rule("'%' integer '\\.' integer conversion") // -xx
    protected FormatPart format2(int width, int precision, char conversion, @ParserContext("FormatFactory") FormatFactory factory)
    {
        return factory.getInstance('x', width, precision, conversion);
    }    
    @Rule("'%' integer conversion")   // -x-
    protected FormatPart format(int width, char conversion, @ParserContext("FormatFactory") FormatFactory factory)
    {
        return factory.getInstance('x', width, -1, conversion);
    }    
    @Rule("'%' '\\.' integer conversion")   //--x
    protected FormatPart format2(int precision, char conversion, @ParserContext("FormatFactory") FormatFactory factory)
    {
        return factory.getInstance('x', -1, precision, conversion);
    }    
    @Rule("'%' flags '\\.' integer conversion") //x-x
    protected FormatPart format2(char flags, int precision, char conversion, @ParserContext("FormatFactory") FormatFactory factory)
    {
        return factory.getInstance(flags, -1, precision, conversion);
    }    
    @Rule("literal")
    protected FormatPart format(String lit, @ParserContext("FormatFactory") FormatFactory factory)
    {
        return factory.getInstance(lit);
    }
    
    @Terminal(expression = "[\\-#\\+ 0\\,\\(]")
    protected abstract char flags(char flags);
    
    @Terminal(expression = "[bBhHsScCdoxXeEfgGaA%n]")
    protected abstract char conversion(char flags);
    
    @Terminal(expression = "[^%]+")
    protected abstract String literal(String lit);
    
    @Terminal(expression = "[1-9][0-9]*")
    protected abstract int integer(int value);

}
