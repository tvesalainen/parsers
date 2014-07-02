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
package org.vesalainen.parsers.xml;

/**
 *
 * @author tkv
 */
public class QName
{
    private String prefix;
    private String localPart;

    public QName(String name)
    {
        int idx = name.indexOf(':');
        if (idx == -1)
        {
            this.prefix = "";
            this.localPart = name;
        }
        else
        {
            this.prefix = name.substring(0, idx);
            this.localPart = name.substring(idx+1);
        }
    }

    public QName(String prefix, String localPart)
    {
        this.prefix = prefix;
        this.localPart = localPart;
    }

    public String getLocalPart()
    {
        return localPart;
    }

    public String getPrefix()
    {
        return prefix;
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
        final QName other = (QName) obj;
        if ((this.prefix == null) ? (other.prefix != null) : !this.prefix.equals(other.prefix))
        {
            return false;
        }
        if ((this.localPart == null) ? (other.localPart != null) : !this.localPart.equals(other.localPart))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 11 * hash + (this.prefix != null ? this.prefix.hashCode() : 0);
        hash = 11 * hash + (this.localPart != null ? this.localPart.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString()
    {
        if (prefix.isEmpty())
        {
            return localPart;
        }
        else
        {
            return prefix + ":" + localPart;
        }
    }

}
