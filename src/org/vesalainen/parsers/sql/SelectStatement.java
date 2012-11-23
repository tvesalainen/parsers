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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Timo Vesalainen
 */
public class SelectStatement<R,C> extends Statement<R,C> implements ConditionVisitor
{
    private List<ColumnReference> subList;
    private Set<Table> tableSet = new HashSet<>();
    private Map<String,Table> correlationMap;
    private Condition<R,C> condition;
    private List<SortSpecification> sortSpecification;
    private Metadata metadata;
    private ErrorReporter reporter;

    public SelectStatement(Engine<R, C> engine, LinkedHashMap<String, Placeholder> placeholderMap, List<ColumnReference> selectList, TableExpression tableExpression, Map<String,Table> correlationMap)
    {
        super(engine, placeholderMap);
        this.subList = selectList;
        this.correlationMap = correlationMap;
        tableSet.addAll(correlationMap.values());
        this.condition = tableExpression.getCondition();
        this.sortSpecification = tableExpression.getSortSpecificationList();
        if (condition != null)
        {
            condition.associateCondition(this, true);
        }
        if (subList == null)
        {
            // asterisk
            subList = new ArrayList<>();
            for (Table table : correlationMap.values())
            {
                TableMetadata tm = engine.getTableMetadata(table.getName());
                if (tm != null)
                {
                    for (ColumnMetadata cm : tm.getColumns())
                    {
                        subList.add(new ColumnReferenceImpl(table, cm.getName()));
                    }
                }
            }
        }
    }

    @Override
    public void check(Metadata metadata, ErrorReporter reporter)
    {
        super.check(metadata, reporter);
        this.metadata = metadata;
        this.reporter = reporter;
        for (ColumnReference cf : subList)
        {
            checkColumnReference(cf, metadata, reporter);
        }
        for (Table table : correlationMap.values())
        {
            if (table.getName() != null)
            {
                TableMetadata tm = metadata.getTableMetadata(table.getName());
                if (tm != null)
                {
                    if (!table.getName().equals(tm.getName()))
                    {
                        reporter.replace(tm.getName(), table.getStart(), table.getStart()+tm.getName().length());
                    }
                }
                else
                {
                    reporter.report("table "+table.getName()+" not defined", ErrorReporter.Level.Hint, table.getSource(), table.getStart(), table.getEnd());
                }
            }
        }
        if (condition != null)
        {
            condition.walk(this, true);
        }
        if (sortSpecification != null)
        {
            for (SortSpecification ss : sortSpecification)
            {
                RowValue rv = ss.getRv();
                if (rv instanceof ColumnReference)
                {
                    ColumnReference cf = (ColumnReference) rv;
                    checkColumnReference(cf, metadata, reporter);
                }
            }
        }
        this.metadata = null;
        this.reporter = null;
    }
    @Override
    public void visit(Condition condition, boolean andPath)
    {
        if (condition instanceof ColumnCondition)
        {
            ColumnCondition cc = (ColumnCondition) condition;
            ColumnReference cf = cc.getColumnReference();
            checkColumnReference(cf, metadata, reporter);
        }
        if (condition instanceof JoinCondition)
        {
            JoinCondition jc = (JoinCondition) condition;
            ColumnReference cf = jc.getColumnReference2();
            checkColumnReference(cf, metadata, reporter);
        }
    }

    private void checkColumnReference(ColumnReference cf, Metadata metadata, ErrorReporter reporter)
    {
        Table table = cf.getTable();
        if (table.getName() == null)
        {
            String msg = cf+" table not specified";
            reporter.report(msg, ErrorReporter.Level.Fatal, cf.getSource(), cf.getStart(), cf.getEnd());
        }
        else
        {
            TableMetadata tm = metadata.getTableMetadata(table.getName());
            if (tm != null)
            {
                if (!table.getName().equals(tm.getName()))
                {
                    reporter.replace(tm.getName(), cf.getStart(), cf.getStart()+tm.getName().length());
                }
                ColumnMetadata cm = tm.getColumnMetadata(cf.getColumn());
                if (cm != null)
                {
                    if (!cf.getColumn().equals(cm.getName()))
                    {
                        reporter.replace(cm.getName(), cf.getEnd()-cf.getColumn().length(), cf.getEnd());
                    }
                }
                else
                {
                    reporter.report("column "+cf.getColumn()+" not defined", ErrorReporter.Level.Hint, cf.getSource(), cf.getEnd()-cf.getColumn().length(), cf.getEnd());
                }
            }
            else
            {
                reporter.report("table "+table.getName()+" not defined", ErrorReporter.Level.Hint, cf.getSource(), cf.getStart(), cf.getEnd());
            }
        }
    }

    @Override
    public FetchResult execute()
    {
        return engine.select(this);
    }
    
    public UpdateableFetchResult selectForUpdate()
    {
        engine.beginTransaction();
        return engine.selectForUpdate(this);
    }
    
    public Collection<Table> getTables()
    {
        return tableSet;
    }

    void setTableReference(String table)
    {
        setTableReference(table, table);
    }

    void setTableReference(String tablename, String correlationName)
    {
        if (correlationName != null)
        {
            correlationName = correlationName.toUpperCase();
        }
        Table table = correlationMap.get(correlationName);
        table.setName(tablename);
    }

    void setWhereClause(Condition condition)
    {
        this.condition = condition;
    }

    void setOrderByClause(List<SortSpecification> list)
    {
        sortSpecification = list;
    }

    public Condition getCondition()
    {
        return condition;
    }

    public List<SortSpecification> getSortSpecification()
    {
        return sortSpecification;
    }

    public List<ColumnReference> getSubList()
    {
        return subList;
    }

    public List<ColumnReference> getReferencedColumns()
    {
        if (sortSpecification == null || sortSpecification.isEmpty())
        {
            return subList;
        }
        else
        {
            List<ColumnReference> list = new ArrayList<>();
            list.addAll(subList);
            for (SortSpecification ss : sortSpecification)
            {
                if (!list.contains(ss.getRv()))
                {
                    list.add((ColumnReference)ss.getRv());
                }
            }
            return list;
        }
    }

    public int getTableCount()
    {
        return tableSet.size();
    }

    public String[] getHeader()
    {
        String[] hdr = new String[subList.size()];
        int index = 0;
        for (ColumnReference cf : subList)
        {
            hdr[index++] = cf.getColumn();
        }
        return hdr;
    }

}
