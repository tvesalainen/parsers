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
