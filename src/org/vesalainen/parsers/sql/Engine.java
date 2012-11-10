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

import org.vesalainen.parsers.sql.util.ArrayMap;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.vesalainen.parser.ParserFactory;
import org.vesalainen.parser.util.InputReader;

/**
 * 
 * @author Timo Vesalainen
 * @param <R>   DB row
 * @param <C>   DB column
 */
public abstract class Engine<R,C> implements SQLConverter<R, C>, Metadata
{
    private SqlParser parser;
    private ArrayMap<Table,TableContext<R,C>> others;
    
    public Engine()
    {
        parser = (SqlParser) ParserFactory.getParserInstance(SqlParser.class);
    }

    protected Engine(Class<?> grammar)
    {
        parser = (SqlParser) ParserFactory.getParserInstance(grammar);
    }

    public Statement prepare(String sql)
    {
        Map<String,Table> correlationMap = new HashMap<>();
        Map<String,Placeholder> placeholderMap = new HashMap<>();
        return parser.parse(sql, this, correlationMap, placeholderMap, null);
    }
    
    public Statement prepare(InputStream is)
    {
        Map<String,Table> correlationMap = new HashMap<>();
        Map<String,Placeholder> placeholderMap = new HashMap<>();
        return parser.parse(is, this, correlationMap, placeholderMap, null);
    }
    
    public FetchResult<R,C> show(String identifier)
    {
        FetchResult fr = new FetchResult<>(this, "Tablename");
        for (TableMetadata tm : getTables())
        {
            fr.addRow(tm.getName());
        }
        return fr;
    }

    public FetchResult<R,C> describe(String tablename)
    {
        FetchResult fr = new FetchResult<>(this, "Column", "Count", "Indexed", "Unique");
        TableMetadata tm = getTableMetadata(tablename);
        for (ColumnMetadata cm : tm.getColumns())
        {
            fr.addRow(cm.getName(), cm.getCount(), cm.isIndexed(), cm.isUnique());
        }
        return fr;
    }

    public void batch(String sql)
    {
        parser.execute(sql, this, new HashMap<>(), new HashMap<>(), null);
    }
    public void batch(InputStream is)
    {
        parser.execute(is, this, new HashMap<>(), new HashMap<>(), null);
    }

    public void check(String sql)
    {
        parser.check(sql, null);
    }
    
    public void check(InputReader reader)
    {
        parser.check(reader, null);
    }
    
    public void check(InputReader reader, SQLLocator locator)
    {
        parser.check(reader, locator);
    }
    
    public FetchResult<R,C> execute(String sql)
    {
        Statement prepared = prepare(sql);
        return prepared.execute();
    }
    public FetchResult<R,C> execute(InputStream is)
    {
        Statement prepared = prepare(is);
        return prepared.execute();
    }
    public OrderedFetchResult<R,C> select(SelectStatement<R,C> select)
    {
        others = new ArrayMap<>(select.getTables());
        TableContextComparator tableContextComparator = getTableContextComparator();
        ArrayMap<Table,TableContext<R,C>> tableResults = new ArrayMap<>(select.getTables());
        List<TableContext<R,C>> tableList = new ArrayList<>();
        for (Table table : select.getCorrelationMap().values())
        {
            TableContext<R, C> tc = createTableContext(table, others);
            others.put(table, tc);
            tableResults.put(table, tc);
            tableList.add(tc);
        }
        Condition condition = select.getCondition();
        OrderedFetchResult<R,C> result = new OrderedFetchResult<>(this, select);
        if (condition == null && select.getTableCount() > 1)
        {
            throw new IllegalArgumentException("no conditions");
        }
        int index = tableList.size();
        TableContext[] resultArray = new TableContext[index];
        while (!tableList.isEmpty())
        {
            index--;
            Collections.sort(tableList, tableContextComparator);
            TableContext<R,C> currentTable = tableList.get(0);
            Collection<R> rows = fetch(currentTable);
            resultArray[index] = currentTable;
            currentTable.setData(rows);
            tableList.remove(currentTable);
            currentTable.updateHints(tableList);
        }
        ArrayMap<Table,R> rowCandidate = new ArrayMap<>(select.getTables());
        cartesian(condition, result, resultArray, rowCandidate);
        return result;
    }
    
    private void cartesian(
            Condition condition, 
            OrderedFetchResult results,
            TableContext[] resultArray,
            ArrayMap<Table,R> rowCandidate
            )
    {
        int level = 0;
        Iterator[] iterator = new Iterator[resultArray.length];
        iterator[0] = resultArray[0].getAll().iterator();
        Map[] joinMap = new Map[resultArray.length-1];
        for (int ii=0;ii<joinMap.length;ii++)
        {
            joinMap[ii] = resultArray[ii].getJoinMapTo(resultArray[ii+1].getTable());
        }
        int[] tableIndex = new int[resultArray.length];
        for (int ii=0;ii<tableIndex.length;ii++)
        {
            tableIndex[ii] = rowCandidate.getIndexOf(resultArray[ii].getTable());
        }
        while (iterator[0].hasNext())
        {
            while (iterator[level].hasNext())
            {
                R row = (R) iterator[level].next();
                rowCandidate.put(tableIndex[level], row);
                if (level+1 < resultArray.length)
                {
                    Set<R> set = (Set<R>) joinMap[level].get(row);
                    iterator[level+1] = set.iterator();
                    level++;
                }
                else
                {
                    if (condition != null)
                    {
                        if (condition.matches(this, rowCandidate) == TruthValue.TRUE)
                        {
                            results.addRow(rowCandidate);
                        }
                    }
                    else
                    {
                        results.addRow(rowCandidate);
                    }
                }
            }
            level--;
        }
    }
    
    protected TableContextComparator getTableContextComparator()
    {
        return new TableContextComparator(this);
    }

    protected Table<R,C> createTable()
    {
        return new Table<>(this);
    }

    protected TableContext<R,C> createTableContext(Table table, ArrayMap<Table, TableContext<R, C>> others)
    {
        return new TableContext<>(this, table, others);
    }

    public abstract void beginTransaction();

    public abstract void commitTransaction();

    public abstract void delete(Collection<R> rows);

    public abstract Collection<R> fetch(Table<R, C> table);

    public abstract Collection<R> fetch(TableContext<R, C> tableRange);

    @Override
    public abstract TableMetadata getTableMetadata(String tablename);

    @Override
    public abstract Iterable<TableMetadata> getTables();

    public abstract void insert(InsertStatement<R, C> insertStatement);

    public abstract void rollbackTransaction();

    public abstract void update(Collection<R> rows);

    public abstract void exit();

    public abstract Class<? extends C> getDefaultPlaceholderType();
    
}
