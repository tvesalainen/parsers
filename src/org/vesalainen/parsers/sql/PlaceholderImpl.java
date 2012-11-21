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

package org.vesalainen.parsers.sql;

/**
 * @author Timo Vesalainen
 */
public class PlaceholderImpl<R,C> extends LiteralImpl<R,C> implements Placeholder<R,C>
{
    private String name;
    private Class<? extends C> type;
    
    public PlaceholderImpl(String name, Class<? extends C> type)
    {
        super(null);
        this.name = name;
        this.type = type;
    }

    public PlaceholderImpl(String name, Literal<R,C> defaultValue)
    {
        super(null);
        this.name = name;
        C val = defaultValue.getValue();
        if (val == null)
        {
            throw new IllegalArgumentException("Placeholder :"+name+" default value = null! Nested placeholders not supported!");
        }
        this.value = val;
        this.type = (Class<? extends C>) val.getClass();
    }

    @Override
    public void bindValue(C value)
    {
        if (type.isAssignableFrom(value.getClass()))
        {
            this.value = value;
        }
        else
        {
            throw new IllegalArgumentException(value+" type is not "+type);
        }
    }

    @Override
    public void setType(Class<? extends C> type)
    {
        this.type = type;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public boolean isBound()
    {
        return value != null;
    }

    @Override
    public Class<? extends C> getType()
    {
        return type;
    }

}
