/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vesalainen.parsers.xml.attr;

/**
 *
 * @author tkv
 */
public class DefaultDecl
{
    public static final DefaultDecl IMPLIED = new DefaultDecl();
    public static final DefaultDecl REQUIRED = new DefaultDecl();

    public static class DefaultValue extends DefaultDecl
    {
        String value;
        boolean fixed;

        public DefaultValue(String value, boolean fixed)
        {
            this.value = value;
            this.fixed = fixed;
        }

        public boolean isFixed()
        {
            return fixed;
        }

        public String getValue()
        {
            return value;
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
            final DefaultValue other = (DefaultValue) obj;
            if ((this.value == null) ? (other.value != null) : !this.value.equals(other.value))
            {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode()
        {
            int hash = 7;
            hash = 43 * hash + (this.value != null ? this.value.hashCode() : 0);
            return hash;
        }

    }
}
