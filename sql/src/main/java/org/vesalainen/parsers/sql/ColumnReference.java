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

import java.util.Collection;
import org.vesalainen.parsers.sql.util.ArrayMap;

/**
 *
 * @author Timo Vesalainen
 */
public interface ColumnReference<R,C> extends RowValue<R, C>
{
    void associateCondition(Condition<R, C> condition, boolean andPath);
    /**
     * Returns columns real name
     * @return 
     */
    String getColumn();
    /**
     * Returns columns display name.
     * @return 
     */
    String getTitle();
    /**
     * Sets the columns display name
     * @param title 
     */
    void setTitle(String title);
    String getCorrelation();
    Table<R, C> getTable();
    void setTable(Table<R, C> table);
    C getValue(SQLConverter<R,C> engine, R row);
    C getValue(SQLConverter<R,C> engine, ArrayMap<Table<R,C>,R> rowCandidate);

    boolean resolvTable(Collection<Table<R, C>> tables);
}
