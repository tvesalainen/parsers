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
public class SetClause<R,C> 
{
    private String identifier;
    private Literal<R,C> literal;

    public SetClause(String identifier, Literal<R, C> literal)
    {
        this.identifier = identifier;
        this.literal = literal;
    }

    public String getColumn()
    {
        return identifier;
    }

    public Literal<R, C> getLiteral()
    {
        return literal;
    }
    
}
