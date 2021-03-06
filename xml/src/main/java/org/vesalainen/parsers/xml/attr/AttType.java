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
package org.vesalainen.parsers.xml.attr;

import java.util.List;
import org.vesalainen.grammar.Grammar;
import org.vesalainen.regex.Regex;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public abstract class AttType
{
    public static final StringType PCDATA = new StringType("attValue");
    public static final TokenizedType ID = new TokenizedType("attValueId");
    public static final TokenizedType IDREF = new TokenizedType("attValueIdRef");
    public static final TokenizedType IDREFS = new TokenizedType("attValueIdRefs", true);
    public static final TokenizedType ENTITY = new TokenizedType("attValueEntity");
    public static final TokenizedType ENTITIES = new TokenizedType("attValueEntities", true);
    public static final TokenizedType NMTOKEN = new TokenizedType("attValueNmToken");
    public static final TokenizedType NMTOKENS = new TokenizedType("attValueNmTokens", true);

    protected String nt;
    protected boolean list;

    private AttType()
    {
    }

    private AttType(String nt)
    {
        this.nt = nt;
    }

    private AttType(String nt, boolean list)
    {
        this.nt = nt;
        this.list = list;
    }

    public boolean isList()
    {
        return list;
    }

    abstract String createConstraint();
    
    public static class StringType extends AttType
    {
        private StringType(String nt)
        {
            super(nt);
        }

        @Override
        public String createConstraint()
        {
            return nt;
        }
    }
    public static class TokenizedType extends AttType
    {
        private TokenizedType(String nt)
        {
            super(nt);
        }

        public TokenizedType(String nt, boolean list)
        {
            super(nt, list);
        }

        @Override
        public String createConstraint()
        {
            return nt;
        }
    }
    public abstract static class EnumeratedType extends AttType
    {
    }
    public static class NotationType extends EnumeratedType
    {
        List<String> names;

        public NotationType(List<String> names)
        {
            this.names = names;
        }

        public List<String> getNames()
        {
            return names;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj == null)
            {
                return false;
            }
            if (getClass() != obj.getClass())
            {
                return false;
            }
            final NotationType other = (NotationType) obj;
            if (this.names != other.names && (this.names == null || !this.names.equals(other.names)))
            {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode()
        {
            int hash = 7;
            hash = 41 * hash + (this.names != null ? this.names.hashCode() : 0);
            return hash;
        }

        @Override
        public String createConstraint()
        {
            StringBuilder sb = new StringBuilder();
            sb.append('`');
            for (String name : names)
            {
                if (sb.length() > 1)
                {
                    sb.append('|');
                }
                String printable = Regex.printable(name);
                sb.append('"');
                sb.append(printable);
                sb.append('"');
                sb.append('|');
                sb.append("'");
                sb.append(printable);
                sb.append("'");
            }
            sb.append('´');
            return sb.toString();
        }

    }
    public static class Enumeration extends EnumeratedType
    {
        List<String> names;

        public Enumeration(List<String> names)
        {
            this.names = names;
        }

        public List<String> getNames()
        {
            return names;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj == null)
            {
                return false;
            }
            if (getClass() != obj.getClass())
            {
                return false;
            }
            final Enumeration other = (Enumeration) obj;
            if (this.names != other.names && (this.names == null || !this.names.equals(other.names)))
            {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode()
        {
            int hash = 7;
            hash = 41 * hash + (this.names != null ? this.names.hashCode() : 0);
            return hash;
        }

        @Override
        public String createConstraint()
        {
            StringBuilder sb = new StringBuilder();
            sb.append('`');
            for (String name : names)
            {
                if (sb.length() > 1)
                {
                    sb.append('|');
                }
                String printable = Regex.printable(name);
                sb.append('"');
                sb.append(printable);
                sb.append('"');
                sb.append('|');
                sb.append("'");
                sb.append(printable);
                sb.append("'");
            }
            sb.append('´');
            return sb.toString();
        }

    }
}
