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
import java.util.List;

/**
 * @author Timo Vesalainen
 */
public class TableExpression<R,C> 
{
    private Condition condition;
    private List<SortSpecification> sortSpecificationList;
    private List<Table<R,C>> tableList;

    TableExpression(List<Table<R,C>> tableList, Condition condition, List<SortSpecification> sortSpecificationList)
    {
        this.tableList = tableList;
        this.condition = condition;
        this.sortSpecificationList = sortSpecificationList;
    }

    public List<Table<R,C>> getTableList()
    {
        return tableList;
    }

    public Condition getCondition()
    {
        return condition;
    }

    public List<SortSpecification> getSortSpecificationList()
    {
        return sortSpecificationList;
    }

}
