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

import java.util.HashSet;
import java.util.Set;

/**
 * @author Timo Vesalainen
 */
public class Table<R,C> extends ParserLocator2Impl
{
    protected Engine<R,C> engine;
    protected String name;
    private final String schema;
    private final String correlationName;
    protected Set<String> selectListColumns = new HashSet<>();
    protected Set<String> conditionColumns = new HashSet<>();
    protected Set<String> andColumns = new HashSet<>();
    protected Set<String> sortColumns = new HashSet<>();
    protected Set<ColumnCondition<R,C>> andConditions = new HashSet<>();
    protected Set<ColumnCondition<R,C>> conditions = new HashSet<>();

    protected Table(Engine<R,C> engine, String schema, String tablename, String correlationName)
    {
        this.engine = engine;
        this.schema = schema;
        this.name = tablename;
        this.correlationName = correlationName;
    }

    public boolean nameMatches(String str)
    {
        // TODO schema
        return str.equalsIgnoreCase(name) || str.equalsIgnoreCase(correlationName);
    }
    public String getName()
    {
        return name;
    }

    public String getSchema()
    {
        return schema;
    }

    public String getCorrelationName()
    {
        return correlationName;
    }
    
    /**
     * Add column found in select list
     * @param column 
     */
    public void addSelectListColumn(String column)
    {
        selectListColumns.add(column);
    }
    /**
     * Add column found in condition
     * @param column 
     */
    public void addConditionColumn(String column)
    {
        conditionColumns.add(column);
    }

    public void associateCondition(ColumnCondition<R,C> condition, boolean andPath)
    {
        conditions.add(condition);
        if (andPath)
        {
            andConditions.add(condition);
            andColumns.add(condition.getColumn());
        }
    }
    /**
     * Return select list columns
     * @return 
     */
    public Set<String> getSelectListColumns()
    {
        return selectListColumns;
    }
    /**
     * Return condition columns
     * @return 
     */
    public Set<String> getConditionColumns()
    {
        return conditionColumns;
    }
    /**
     * Add condition column in and path. 
     * @param column 
     */
    public void addAndColumn(String column)
    {
        andColumns.add(column);
    }
    /**
     * Return condition columns in and path
     * @return 
     */
    public Set<String> getAndColumns()
    {
        return andColumns;
    }

    public Set<ColumnCondition<R,C>> getConditions()
    {
        return conditions;
    }

    public Set<ColumnCondition<R,C>> getAndConditions()
    {
        return andConditions;
    }

    @Override
    public String toString()
    {
        return name;
    }

    public void addSortColumn(String column)
    {
        sortColumns.add(column);
    }

    public Set<String> getSortColumns()
    {
        return sortColumns;
    }

}
