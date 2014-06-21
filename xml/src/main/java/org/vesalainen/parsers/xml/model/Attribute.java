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
package org.vesalainen.parsers.xml.model;

import java.util.Arrays;
import java.util.List;

/**
 * @author Timo Vesalainen
 */
public abstract class Attribute
{
    private String name;

    public Attribute(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    
    public abstract Object getValue();
    
    public static Attribute createSingleAttribute(String name, String value)
    {
        return new SingleAttribute(name, value);
    }
    public static Attribute createListAttribute(String name, String value)
    {
        return new ListAttribute(name, Arrays.asList(value.split(" ")));
    }
    private static class SingleAttribute extends Attribute
    {
        private String value;

        public SingleAttribute(String name, String value)
        {
            super(name);
            this.value = value;
        }

        public String getValue()
        {
            return value;
        }

    }
    private static class ListAttribute extends Attribute
    {
        private List<String> value;

        public ListAttribute(String name, List<String> value)
        {
            super(name);
            this.value = value;
        }

        public List<String> getValue()
        {
            return value;
        }
        
    }
}
