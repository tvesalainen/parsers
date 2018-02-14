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
import java.util.function.IntPredicate;
import java.util.function.ObjIntConsumer;
import org.vesalainen.lang.Primitives;
import org.vesalainen.util.CharSequences;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public class FormatFactory
{
    private IntPredicate isWhiteSpace;

    public FormatFactory()
    {
        this(Character::isWhitespace);
    }

    public FormatFactory(IntPredicate isWhiteSpace)
    {
        this.isWhiteSpace = isWhiteSpace;
    }

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
            pos = scanWhiteSpace(text, pos);
            pos = p.parse(text, pos);
            if (!p.isLiteral())
            {
                func.accept(p.getValue(), index++);
            }
        }
    }
    protected int scanWhiteSpace(String text, int start)
    {
        int len = text.length()-start;
        for (int ii=0;ii<len;ii++)
        {
            if (!isWhiteSpace.test(text.charAt(start+ii)))
            {
                return start+ii;
            }
        }
        return start+len;
    }
    private static boolean isFloat(int cc)
    {
        return isDigit(cc) || cc == '.';
    }
    private static boolean isDigit(int cc)
    {
        switch (cc)
        {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return true;
            default:
                return false;
        }
    }
    private static boolean isHex(int cc)
    {
        switch (cc)
        {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
                return true;
            default:
                return false;
        }
    }
    private static boolean isOctal(int cc)
    {
        switch (cc)
        {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
                return true;
            default:
                return false;
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
        protected char flags;
        protected int width;
        protected int precision;
        protected char conversion;
        protected T value;
        protected boolean literal;

        public FormatPart()
        {
        }

        public FormatPart(char flags, int width, int precision, char conversion)
        {
            this.flags = flags;
            this.width = width;
            this.precision = precision;
            this.conversion = conversion;
        }

        public abstract int parse(String text, int start);

        public T getValue()
        {
            return value;
        }

        public boolean isLiteral()
        {
            return literal;
        }

        public char getFlags()
        {
            return flags;
        }

        public int getWidth()
        {
            return width;
        }

        public int getPrecision()
        {
            return precision;
        }

        public char getConversion()
        {
            return conversion;
        }
    }
    public class DoublePart extends FormatPart<Double>
    {

        public DoublePart(char flags, int width, int precision, char conversion)
        {
            super(flags, width, precision, conversion);
        }

        @Override
        public int parse(String text, int start)
        {
            int begin = CharSequences.indexOf(text, FormatFactory::isDigit, start);
            int end = CharSequences.indexOf(text, (c)->!FormatFactory.isFloat(c), begin);
            value = Primitives.parseDouble(text, begin, end);
            return end;
        }
        
    }
    public class LongPart extends FormatPart<Long>
    {

        public LongPart(char flags, int width, int precision, char conversion)
        {
            super(flags, width, precision, conversion);
        }

        @Override
        public int parse(String text, int start)
        {
            int begin = CharSequences.indexOf(text, FormatFactory::isDigit, start);
            int end = CharSequences.indexOf(text, (c)->!FormatFactory.isDigit(c), begin);
            value = Primitives.parseLong(text, begin, end);
            return end;
        }
        
    }
    public class StringPart extends FormatPart<String>
    {

        public StringPart(char flags, int width, int precision, char conversion)
        {
            super(flags, width, precision, conversion);
        }

        @Override
        public int parse(String text, int start)
        {
            if (precision != -1)
            {
                value = text.substring(start, Math.min(start+precision, text.length()));
                return start+precision;
            }
            else
            {
                int end = CharSequences.indexOf(text, isWhiteSpace, start);
                value = text.substring(start, end != -1 ? end : text.length());
                return start+value.length();
            }
        }
        
    }
    public class LiteralPart extends FormatPart<String>
    {
        private int length;
        public LiteralPart(String literal)
        {
            this.value = CharSequences.trim(literal, isWhiteSpace).toString();
            this.length = value.length();
            this.literal = true;
        }

        @Override
        public int parse(String text, int start)
        {
            if (text.regionMatches(start, value, 0, length))
            {
                return start+length;
            }
            else
            {
                throw new IllegalArgumentException(text.substring(start)+" not starting with "+value);
            }
        }
        
    }
}
