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
 * ColumnCondition implements a condition related to tables column. Example is 
 * emp.name = 'John' or emp.name like 'J%'
 * @author Timo Vesalainen
 */
public interface ColumnCondition<R,C> extends Condition<R,C>
{
    /**
     * Returns the column name
     * @return 
     */
    String getColumn();
    /**
     * Tests the condition against one table row.
     * @param converter SQLCOnverter
     * @param row Tested row
     * @return 
     */
    TruthValue matches(SQLConverter<R,C> converter, R row);
    /**
     * Possibility to narrow columns value range
     * @param converter
     * @param range 
     */
    void narrow(SQLConverter<R,C> converter, Range<C> range);
    /**
     * Return the column reference
     * @return 
     */
    ColumnReference<R,C> getColumnReference();
}
