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

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Timo Vesalainen
 */
public class BatchStatement<R,C> extends Statement<R,C>
{
    private List<Statement<R, C>> statementList;
    
    public BatchStatement(Engine engine, LinkedHashMap placeholderMap, List<Statement<R, C>> list)
    {
        super(engine, placeholderMap);
        this.statementList = list;
    }

    @Override
    public FetchResult execute()
    {
        if (statementList.size() == 1)
        {
            return statementList.get(0).execute();
        }
        for (Statement<R, C> statement : statementList)
        {
            statement.execute();
        }
        return null;
    }

    public List<Statement<R, C>> getStatementList()
    {
        return statementList;
    }

}
