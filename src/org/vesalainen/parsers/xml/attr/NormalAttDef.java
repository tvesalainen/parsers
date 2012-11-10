package org.vesalainen.parsers.xml.attr;

import org.vesalainen.parsers.xml.QName;
import org.vesalainen.parsers.xml.attr.DefaultDecl.DefaultValue;
import org.vesalainen.regex.Regex;

public class NormalAttDef extends AttDef
{

    private QName qName;

    public String getName()
    {
        return qName.toString();
    }

    public NormalAttDef(QName qName, AttType attType, DefaultDecl defaultDecl)
    {
        super(attType, defaultDecl);
        this.qName = qName;
    }

    @Override
    public String greateRhs()
    {
        StringBuilder rhs = new StringBuilder();
        rhs.append("namePrefix ");
        rhs.append("'" + qName.getLocalPart() + "'");
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
        final NormalAttDef other = (NormalAttDef) obj;
        if (this.qName != other.qName && (this.qName == null || !this.qName.equals(other.qName)))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 89 * hash + (this.qName != null ? this.qName.hashCode() : 0);
        return hash;
    }

}
