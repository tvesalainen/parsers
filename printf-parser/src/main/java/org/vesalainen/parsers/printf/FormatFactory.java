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
import java.util.function.ObjIntConsumer;
import org.vesalainen.lang.Primitives;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public class FormatFactory
{
    public Object[] parse(List<FormatPart> parts, String text)
    {
        int size = 0;
        for (FormatPart p : parts)
        {
            if (!p.isLiteral())
            {
                size++;
            }
        }
        Object[] result = new Object[size];
        parse(parts, text, (o, i)->result[i] = o);
        return result;
    }
    public void parse(List<FormatPart> parts, String text, ObjIntConsumer func)
    {
        int pos = 0;
        int index = 0;
        for (FormatPart p : parts)
        {
            Object value = p.parse(text, pos);
            if (!p.isLiteral())
            {
                func.accept(value, index++);
            }
            pos += p.getWidth();
        }
    }
    public FormatPart getInstance(String lit)
    {
        return new LiteralPart(lit);
    }
    public FormatPart getInstance(char flags, int width, int precision, char conversion)
    {
        switch (conversion)
        {
            case 's':
            case 'S':
                return new StringPart(flags, width, precision, conversion);
            case 'd':
                return new LongPart(flags, width, precision, conversion);
            case 'f':
                return new DoublePart(flags, width, precision, conversion);
            case 'n':
                return new LiteralPart(System.lineSeparator());
            case '%':
                return new LiteralPart("%");
            default:
                throw new UnsupportedOperationException(conversion+" conversion not supported");
        }
    }
    public abstract class FormatPart<T>
    {
        protected int width;
        protected boolean literal;

        public FormatPart()
        {
        }

        public FormatPart(int width)
        {
            this.width = width;
            if (width <= 0)
            {
                throw new IllegalArgumentException(width+" is illegal");
            }
        }

        public abstract T parse(String text, int start);

        public boolean isLiteral()
        {
            return literal;
        }

        public int getWidth()
        {
            return width;
        }

    }
    public class DoublePart extends FormatPart<Double>
    {

        public DoublePart(char flags, int width, int precision, char conversion)
        {
            super(width);
            if (width+2 < precision)
            {
                throw new IllegalArgumentException("precision="+precision+" doesn't fit");
            }
        }

        @Override
        public Double parse(String text, int start)
        {
            return Primitives.findDouble(text.subSequence(start, start+width));
        }
        
    }
    public class LongPart extends FormatPart<Long>
    {

        public LongPart(char flags, int width, int precision, char conversion)
        {
            super(width);
        }

        @Override
        public Long parse(String text, int start)
        {
            return Primitives.findLong(text.subSequence(start, start+width));
        }
        
    }
    public class StringPart extends FormatPart<String>
    {

        public StringPart(char flags, int width, int precision, char conversion)
        {
            super(Math.max(width, precision));
        }

        @Override
        public String parse(String text, int start)
        {
            return text.substring(start, Math.min(start+width, text.length())).trim();
        }
        
    }
    public class LiteralPart extends FormatPart<String>
    {
        private String value;
        public LiteralPart(String literal)
        {
            super(literal.length());
            this.value = literal;
            this.literal = true;
        }

        @Override
        public String parse(String text, int start)
        {
            if (!text.startsWith(value, start))
            {
                throw new IllegalArgumentException(text.substring(start)+" at "+start+" not starting with "+value);
            }
            return value;
        }
        
    }
}
