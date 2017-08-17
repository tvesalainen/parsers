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

import org.vesalainen.grammar.Nonterminal;


/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public abstract class AttDef
{
    protected AttType attType;
    protected DefaultDecl defaultDecl;
    protected Nonterminal nt;

    public AttDef(AttType attType, DefaultDecl defaultDecl)
    {
        this.attType = attType;
        this.defaultDecl = defaultDecl;
    }

    public AttType getAttType()
    {
        return attType;
    }

    public DefaultDecl getDefaultDecl()
    {
        return defaultDecl;
    }

    public abstract String getName();

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
        final AttDef other = (AttDef) obj;
        if (this.attType != other.attType && (this.attType == null || !this.attType.equals(other.attType)))
        {
            return false;
        }
        if (this.defaultDecl != other.defaultDecl && (this.defaultDecl == null || !this.defaultDecl.equals(other.defaultDecl)))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 11 * hash + (this.attType != null ? this.attType.hashCode() : 0);
        hash = 11 * hash + (this.defaultDecl != null ? this.defaultDecl.hashCode() : 0);
        return hash;
    }

    public abstract String greateRhs();
}
