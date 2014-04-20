/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vesalainen.parsers.xml.attr;

import org.vesalainen.grammar.Nonterminal;


/**
 *
 * @author tkv
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
