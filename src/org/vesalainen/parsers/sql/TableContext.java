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
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import org.vesalainen.parsers.sql.util.CartesianMap;
import org.vesalainen.parsers.sql.util.FastSet;
import org.vesalainen.parsers.sql.util.JoinMap;
import org.vesalainen.parsers.sql.util.JoinMapImpl;

/**
 * @author Timo Vesalainen
 */
public class TableContext<R,C> 
{
    private Engine<R,C> selector;
    private Table<R,C> table;
    private Map<String,Range<C>> columnRanges = new HashMap<>();
    private Map<String,NavigableMap<C,Set<R>>> indexes = new HashMap<>();
    private FastSet<R> all;
    private ArrayMap<Table<R,C>,TableContext<R,C>> others;
    private Map<Table,JoinMap<R>> joinMaps = new HashMap<>();   // TODO use ArrayMap!!!
    private TableMetadata metadata;

    public TableContext(Engine<R,C> selector, Table<R,C> table, ArrayMap<Table<R,C>, TableContext<R, C>> others)
    {
        this.selector = selector;
        this.table = table;
        this.others = others;
        metadata = selector.getTableMetadata(table.getName());
        for (ColumnCondition cc : table.getAndConditions())
        {
            String column = cc.getColumn();
            Range<C> range = columnRanges.get(column);
            if (range == null)
            {
                range = new Range<>(selector.getComparator());
                columnRanges.put(column, range);
            }
            cc.narrow(selector, range);
        }
    }

    public JoinMap<R> getJoinMapTo(Table table)
    {
        JoinMap<R> map = joinMaps.get(table);
        if (map == null)
        {
            return new CartesianMap<>(all, others.get(table).all);
        }
        else
        {
            return map;
        }
    }
    public TableContext<R,C> getOther(Table table)
    {
        return others.get(table);
    }
    
    public void setData(Collection<R> rows)
    {
        assert all == null;
        all = new FastSet<>(rows, true);
        for (String column : table.getAndColumns())
        {
            NavigableMap<C,Set<R>> map = new TreeMap<>(selector.getComparator());
            indexes.put(column, map);
        }
        for (String column : indexes.keySet())
        {
            boolean unique = false;
            if (metadata != null)
            {
                ColumnMetadata columnMetadata = metadata.getColumnMetadata(column);
                if (columnMetadata != null)
                {
                    unique = columnMetadata.isUnique();
                }
            }
            NavigableMap<C,Set<R>> map = indexes.get(column);
            for (R row : rows)
            {
                C value = selector.get(row, column);
                if (value != null)
                {
                    Set<R> set = map.get(value);
                    if (set == null)
                    {
                        if (unique)
                        {
                            set = all.singleSub();
                        }
                        else
                        {
                            set = all.sub();
                        }
                        map.put(value, set);
                    }
                    set.add(row);
                }
            }
        }
        for (ColumnCondition cc : table.getAndConditions())
        {
            if (cc instanceof JoinCondition)
            {
                JoinCondition jc = (JoinCondition) cc;
                if (Relation.EQ.equals(jc.getRelation()))
                {
                    ColumnReference otherCr = jc.getColumnReference2();
                    Table otherTable = otherCr.getTable();
                    TableContext<R, C> otherCtx = others.get(otherTable);
                    if (otherCtx.hasData())
                    {
                        ColumnReference thisCr = jc.getColumnReference();
                        String thisColumn = thisCr.getColumn();
                        NavigableMap<C,Set<R>> thisMap = indexes.get(thisColumn);
                        String otherColumn = otherCr.getColumn();
                        NavigableMap<C,Set<R>> otherMap = otherCtx.indexes.get(otherColumn);
                        if (thisMap.isEmpty() || otherMap.isEmpty())
                        {
                            removeAll();
                            otherCtx.removeAll();
                        }
                        else
                        {
                            JoinMap<R>[] mergeMaps = merge(thisMap, otherMap);
                            JoinMap<R> oldMapOther = joinMaps.get(otherTable);
                            if (oldMapOther == null || oldMapOther.size() > mergeMaps[0].size())
                            {
                                joinMaps.put(otherTable, mergeMaps[0]);
                            }
                            JoinMap<R> oldMapThis = joinMaps.get(table);
                            if (oldMapThis == null || oldMapThis.size() > mergeMaps[1].size())
                            {
                                joinMaps.put(table, mergeMaps[1]);
                            }
                            System.err.println("merged "+table+" to "+this.all.size());
                            System.err.println("merged "+otherTable+" to "+otherCtx.all.size());
                        }
                    }
                }
            }
        }
    }
    private JoinMap<R>[] merge(NavigableMap<C,Set<R>> thisMap, NavigableMap<C,Set<R>> otherMap)
    {
        JoinMap<R> mapThis = new JoinMapImpl<>();
        JoinMap<R> mapOther = new JoinMapImpl<>();
        Comparator<? super C> comparator = thisMap.comparator();
        Iterator<C> thisIterator = thisMap.keySet().iterator();
        Iterator<C> otherIterator = otherMap.keySet().iterator();
        C thisValue = thisIterator.next();
        C otherValue = otherIterator.next();
        while (true)
        {
            int compare = comparator.compare(thisValue, otherValue);
            if (compare < 0)
            {
                Set<R> thisSet = thisMap.get(thisValue);
                thisSet.clear();
                thisIterator.remove();
                if (!thisIterator.hasNext())
                {
                    break;
                }
                thisValue = thisIterator.next();
            }
            else
            {
                if (compare > 0)
                {
                    Set<R> otherSet = otherMap.get(thisValue);
                    otherSet.clear();
                    otherIterator.remove();
                    if (!otherIterator.hasNext())
                    {
                        break;
                    }
                    otherValue = otherIterator.next();
                }
                else
                {
                    // accept the pair
                    Set<R> thisSet = thisMap.get(thisValue);
                    Set<R> otherSet = otherMap.get(thisValue);
                    for (R row : thisSet)
                    {
                        mapThis.put(row, otherSet);
                    }
                    for (R row : otherSet)
                    {
                        mapOther.put(row, thisSet);
                    }
                    if (!thisIterator.hasNext() || !otherIterator.hasNext())
                    {
                        break;
                    }
                    thisValue = thisIterator.next();
                    otherValue = otherIterator.next();
                }
            }
        }
        if (thisIterator.hasNext())
        {
            while (thisIterator.hasNext())
            {
                thisValue = thisIterator.next();
                Set<R> thisSet = thisMap.get(thisValue);
                thisSet.clear();
            }
        }
        if (otherIterator.hasNext())
        {
            while (otherIterator.hasNext())
            {
                otherValue = otherIterator.next();
                Set<R> otherSet = otherMap.get(otherValue);
                otherSet.clear();
            }
        }
        return new JoinMap[] {mapThis, mapOther};
    }

    public NavigableSet<C> getColumnValues(String column)
    {
        return indexes.get(column).navigableKeySet();
    }
    
    public boolean hasData()
    {
        return all != null && !all.isEmpty();
    }

    public void updateHints(List<TableContext<R,C>> tableRanges)
    {
        for (TableContext th : tableRanges)
        {
            if (!table.equals(th.getTable()))
            {
                for (String column : indexes.keySet())
                {
                    SortedMap<C,Set<R>> map = indexes.get(column);
                    if (!map.isEmpty())
                    {
                        th.narrow(table, column, map.firstKey(), map.lastKey());
                    }
                }
            }
        }
    }

    public void narrow(Table fromTable, String column, C lower, C upper)
    {
        for (ColumnCondition tc : table.getAndConditions())
        {
            if (tc instanceof JoinCondition)
            {
                JoinCondition jc = (JoinCondition) tc;
                Range<C> range = columnRanges.get(column);
                if (range == null)
                {
                    columnRanges.put(column, jc.narrow(selector, range, fromTable, column, lower, upper));
                }
                else
                {
                    jc.narrow(selector, range, fromTable, column, lower, upper);
                }
            }
        }
    }
    public Map<String, Range<C>> getColumnRanges()
    {
        return columnRanges;
    }

    public Table getTable()
    {
        return table;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final TableContext other = (TableContext) obj;
        if (!Objects.equals(this.table, other.table))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.table);
        return hash;
    }

    @Override
    public String toString()
    {
        return "TableContext{" + "table=" + table + '}';
    }

    public FastSet<R> getAll()
    {
        return all;
    }

    public Map<String, NavigableMap<C, Set<R>>> getIndexes()
    {
        return indexes;
    }

    public ArrayMap<Table<R,C>, TableContext<R, C>> getOthers()
    {
        return others;
    }

    public Engine<R, C> getSelector()
    {
        return selector;
    }

    private void removeAll()
    {
        all.clear();
        for (NavigableMap<C, Set<R>> map : indexes.values())
        {
            map.clear();
        }
    }

}
