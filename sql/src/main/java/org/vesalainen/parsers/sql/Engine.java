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
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import org.vesalainen.parser.GenClassFactory;
import org.vesalainen.parser.util.InputReader;
import org.vesalainen.parsers.sql.util.JoinMap;

/**
 * 
 * @author Timo Vesalainen
 * @param <R>   DB row
 * @param <C>   DB column
 */
public abstract class Engine<R,C> implements SQLConverter<R, C>, Metadata
{
    private SqlParser parser;
    private ArrayMap<Table<R,C>,TableContext<R,C>> others;
    
    public Engine()
    {
        parser = (SqlParser) GenClassFactory.getGenInstance(SqlParser.class);
    }

    protected Engine(Class<?> grammar)
    {
        parser = (SqlParser) GenClassFactory.getGenInstance(grammar);
    }

    public Statement prepare(String sql)
    {
        Deque<List<Table<R,C>>> tableListStack = new ArrayDeque<>();
        LinkedHashMap<String,Placeholder> placeholderMap = new LinkedHashMap<>();
        return parser.parse(sql, this, tableListStack, placeholderMap, null);
    }
    
    public Statement prepare(InputStream is)
    {
        Deque<List<Table<R,C>>> tableListStack = new ArrayDeque<>();
        LinkedHashMap<String,Placeholder> placeholderMap = new LinkedHashMap<>();
        return parser.parse(is, this, tableListStack, placeholderMap, null);
    }
    
    public FetchResult<R,C> show(String identifier)
    {
        FetchResult fr = new FetchResult<>(this, "Tablename");
        for (TableMetadata tm : getTables())
        {
            fr.addRowArray(tm.getName());
        }
        return fr;
    }

    public FetchResult<R,C> describe(String tablename)
    {
        FetchResult fr = new FetchResult<>(this, "Column", "Count", "Indexed", "Unique");
        TableMetadata tm = getTableMetadata(tablename);
        for (ColumnMetadata cm : tm.getColumns())
        {
            fr.addRowArray(cm.getName(), cm.getCount(), cm.isIndexed(), cm.isUnique());
        }
        return fr;
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
    public UpdateableFetchResult<R,C> selectForUpdate(SelectStatement<R,C> select)
    {
        UpdateableFetchResult<R,C> result = new UpdateableFetchResult<>(this, select);
        select(select, result, true);
        return result;
    }
    public OrderedFetchResult<R,C> select(SelectStatement<R,C> select)
    {
        OrderedFetchResult<R,C> result = new OrderedFetchResult<>(this, select);
        select(select, result, false);
        return result;
    }
    private void select(SelectStatement<R,C> select, OrderedFetchResult<R,C> result, boolean update)
    {
        startProgressMonitor(0, select.getTableCount()*3+1);
        int progress = 0;
        others = new ArrayMap<>(select.getTables());
        TableContextComparator tableContextComparator = getTableContextComparator();
        ArrayMap<Table<R,C>,TableContext<R,C>> tableResults = new ArrayMap<>(select.getTables());
        List<TableContext<R,C>> tableList = new ArrayList<>();
        for (Table<R,C> table : select.getTables())
        {
            TableContext<R, C> tc = createTableContext(table, others);
            others.put(table, tc);
            tableResults.put(table, tc);
            tableList.add(tc);
        }
        Condition condition = select.getCondition();
        if (condition == null && select.getTableCount() > 1)
        {
            throw new IllegalArgumentException("no conditions");
        }
        int index = tableList.size();
        TableContext[] resultArray = new TableContext[index];
        while (!tableList.isEmpty())
        {
            updateProgressMonitor(++progress);
            index--;
            Collections.sort(tableList, tableContextComparator);
            TableContext<R,C> currentTable = tableList.get(0);
            Collection<R> rows = fetch(currentTable, update);
            updateProgressMonitor(++progress);
            resultArray[index] = currentTable;
            currentTable.setData(rows);
            updateProgressMonitor(++progress);
            tableList.remove(currentTable);
            currentTable.updateHints(tableList);
        }
        sort(resultArray);
        ArrayMap<Table<R,C>,R> rowCandidate = new ArrayMap<>(select.getTables());
        cartesian(condition, result, resultArray, rowCandidate);
        stopProgressMonitor();
    }
    
    private void cartesian(
            Condition condition, 
            OrderedFetchResult results,
            TableContext[] resultArray,
            ArrayMap<Table<R,C>,R> rowCandidate
            )
    {
        int level = 0;
        Iterator[] iterator = new Iterator[resultArray.length];
        iterator[0] = resultArray[0].getAll().iterator();
        JoinMap<R>[] joinMap = new JoinMap[resultArray.length-1];
        for (int ii=0;ii<joinMap.length;ii++)
        {
            joinMap[ii] = resultArray[ii].getJoinMapTo(resultArray[ii+1].getTable());
        }
        int[] tableIndex = new int[resultArray.length];
        for (int ii=0;ii<tableIndex.length;ii++)
        {
            tableIndex[ii] = rowCandidate.getIndexOf(resultArray[ii].getTable());
        }
        while (level >= 0)
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
    /**
     * Factory method for creating TableContextComparator
     * @return 
     */
    protected TableContextComparator getTableContextComparator()
    {
        return new TableContextComparator(this);
    }
    /**
     * Factory method for creating Table
     * @return 
     */
    protected Table<R,C> createTable(String schema, String tablename, String correlationName)
    {
        return new Table<>(this, schema, tablename, correlationName);
    }
    /**
     * Factory method for creating TableContext
     * @param table Current table
     * @param others Connection map to other joining tables
     * @return 
     */
    protected TableContext<R,C> createTableContext(Table<R,C> table, ArrayMap<Table<R,C>, TableContext<R, C>> others)
    {
        return new TableContext<>(this, table, others);
    }
    /**
     * Begin transaction
     */
    public abstract void beginTransaction();
    /**
     * Commit transaction
     */
    public abstract void commitTransaction();
    /**
     * Delete collection of rows
     * @param rows 
     */
    public abstract void delete(Collection<R> rows);
    /**
     * Non joined fetch
     * @param table Fetched table
     * @return 
     */
    public abstract Collection<R> fetch(Table<R, C> table);
    /**
     * Joined fetch
     * @param tableContext 
     * @param update If true the resulting rows will be updated.
     * @return 
     */
    public abstract Collection<R> fetch(TableContext<R, C> tableContext, boolean update);
    /**
     * Execute insert statement
     * @param insertStatement 
     */
    public abstract void insert(InsertStatement<R, C> insertStatement);
    /**
     * Rolls back transaction
     */
    public abstract void rollbackTransaction();
    /**
     * Update rows.
     * @param rows 
     */
    public abstract void update(Collection<R> rows);
    /**
     * Application is about to exit
     */
    public abstract void exit();
    /**
     * Returns the default placeholder type.
     * @return 
     */
    public abstract Class<? extends C> getDefaultPlaceholderType();
    /**
     * Returns a created function
     * @param funcName
     * @param inner
     * @return 
     */
    public ColumnReference createFunction(ColumnReference inner, String funcName, String... args)
    {
        switch (funcName.toLowerCase())
        {
            case "upper":
                check(funcName, args.length, 0, 0);
                return new AbstractFunction(inner) 
                {
                    @Override
                    public Object function(Object value)
                    {
                        return value != null ? value.toString().toUpperCase() : null;
                    }
                };
            case "lower":
                check(funcName, args.length, 0, 0);
                return new AbstractFunction(inner) 
                {
                    @Override
                    public Object function(Object value)
                    {
                        return value != null ? value.toString().toLowerCase() : null;
                    }
                };
            case "extract":
                check(funcName, args.length, 1, 1);
                return new ExtractFunction(inner, args[0]);
            case "toint":
                check(funcName, args.length, 0, 0);
                return new ToFunction(inner, Integer.class);
            case "todouble":
                check(funcName, args.length, 0, 0);
                return new ToFunction(inner, Double.class);
            case "toboolean":
                check(funcName, args.length, 0, 0);
                return new ToFunction(inner, Boolean.class);
            case "tochar":
            case "tostring":
                check(funcName, args.length, 0, 1);
                return new ToStringFunction(inner, args);
            case "todate":
                check(funcName, args.length, 1, 1);
                return new ToDateFunction(inner, args);
            case "format":
                check(funcName, args.length, 1, 1);
                return new FormatFunction(inner, args[0]);
            case "creditorreference":
                check(funcName, args.length, 0, 0);
                return new CreditorReferenceFunction(inner);
            default:
                throw new IllegalArgumentException("This version doesn't support "+funcName+" function");
        }
    }
    public ColumnReference createFunction(ColumnReference inner, String funcName, Number number, Number... args)
    {
        switch (funcName.toLowerCase())
        {
            case "substr":
            case "substring":
                check(funcName, args.length, 0, 1);
                return new SubStringFunction(inner, number, args);
            default:
                throw new IllegalArgumentException("This version doesn't support "+funcName+" function");
        }
    }
    protected void check(String funcName, int len, int min, int max)
    {
        if (len < min || len > max)
        {
            throw new IllegalArgumentException("wrong number of string arguments for function "+funcName);
        }
    }

    private void sort(TableContext[] resultArray)
    {
        if (resultArray.length == 1)
        {
            return;
        }
        HashSet<TableContext> rest = new HashSet<>();
        for (TableContext tc : resultArray)
        {
            rest.add(tc);
        }
        OptRes min = findMin(Float.MAX_VALUE, 0, rest, new ArrayDeque<TableContext>());
        for (int ii=resultArray.length-1;ii>=0;ii--)
        {
            resultArray[ii] = min.stack.pop();
        }
    }
    private OptRes findMin(
            float lastMin,
            float cum,
            HashSet<TableContext> rest,
            ArrayDeque<TableContext> stack
            )
    {
        OptRes best = null;
        if (stack.isEmpty())
        {
            for (TableContext tc : rest)
            {
                HashSet<TableContext> clone = (HashSet<TableContext>) rest.clone();
                clone.remove(tc);
                stack.push(tc);
                OptRes res = findMin(lastMin, 0, clone, stack);
                stack.pop();
                if (res != null && res.min < lastMin)
                {
                    lastMin = res.min;
                    best = res;
                }
            }
        }
        else
        {
            if (!rest.isEmpty())
            {
                TableContext prev = stack.peek();
                for (TableContext tc : rest)
                {
                    JoinMap joinMapTo = prev.getJoinMapTo(tc.getTable());
                    float cur = cum + joinMapTo.getRatio();
                    if (cur < lastMin)
                    {
                        HashSet<TableContext> clone = (HashSet<TableContext>) rest.clone();
                        clone.remove(tc);
                        stack.push(tc);
                        OptRes res = findMin(lastMin, cur, clone, stack);
                        stack.pop();
                        if (res != null && res.min < lastMin)
                        {
                            lastMin = res.min;
                            best = res;
                        }
                    }
                }
            }
            else
            {
                return new OptRes(stack.clone(), cum);
            }
        }
        return best;
    }

    private class OptRes
    {
        private ArrayDeque<TableContext> stack;
        private float min;

        public OptRes(ArrayDeque<TableContext> stack, float min)
        {
            this.stack = stack;
            this.min = min;
        }
        
    }
    /**
     * Start progress monitoring for execution. Default implementation does nothing.
     * @param min
     * @param max 
     */
    protected void startProgressMonitor(int min, int max)
    {
    }
    /**
     * Update progress.  Default implementation does nothing.
     * @param now 
     */
    protected void updateProgressMonitor(int now)
    {
    }
    /**
     * Sets progress note. Default implementation prints to stderr.
     * @param note 
     */
    protected void progressNote(String note)
    {
        System.err.println(note);
    }
    /**
     * Stops progress monitoring. Default implementation does nothing.
     */
    private void stopProgressMonitor()
    {
    }
}
