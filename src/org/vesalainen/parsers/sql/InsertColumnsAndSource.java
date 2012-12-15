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

import java.util.List;

/**
 * @author Timo Vesalainen
 */
public class InsertColumnsAndSource<R,C> 
{
    private List<String> columnList;
    private List<Literal<R,C>> valueList;
    private SelectStatement select;

    public InsertColumnsAndSource(List<String> columnList, List<Literal<R,C>> valueList)
    {
        this.columnList = columnList;
        this.valueList = valueList;
    }

    public InsertColumnsAndSource(List<String> columnList, SelectStatement select)
    {
        this.columnList = columnList;
        this.select = select;
    }

    public List<String> getColumnList()
    {
        return columnList;
    }

    public List<Literal<R,C>> getValueList()
    {
        return valueList;
    }

    public SelectStatement getSelect()
    {
        return select;
    }
    
}
