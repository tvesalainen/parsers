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

import org.vesalainen.parsers.xml.attr.DefaultDecl.DefaultValue;
import org.vesalainen.regex.Regex;

public class NSAttDef extends AttDef
{

    private String name;

    public NSAttDef(String name, AttType attType, DefaultDecl defaultDecl)
    {
        super(attType, defaultDecl);
        this.name = name;
    }

    @Override
    public String greateRhs()
    {
        StringBuilder rhs = new StringBuilder();
        rhs.append("namePrefix ");
        rhs.append("'" + name + "'");
        rhs.append(" eq ");
        boolean handled = false;
        if (defaultDecl instanceof DefaultValue)
        {
            DefaultValue defVal = (DefaultValue) defaultDecl;
            if (defVal.isFixed())
            {
                String value = defVal.getValue();
                String printable = Regex.printable(value);
                rhs.append("`\""+printable+"\"|'"+printable+"'´");
                handled = true;
            }
        }
        if (!handled)
        {
            String constraint = attType.createConstraint();
            if (constraint == null)
            {
                throw new NullPointerException();
            }
            rhs.append(constraint);
        }
        return rhs.toString();
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
        if (!super.equals(obj))
        {
            return false;
        }
        final NSAttDef other = (NSAttDef) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 11 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    @Override
    public String getName()
    {
        return name;
    }

}
