/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.vesalainen.parsers.sql;

import java.util.Comparator;

/**
 * @author Timo Vesalainen
 */
public class TableContextComparator<R,C> implements Comparator<TableContext<R,C>>
{
    private Engine engine;

    public TableContextComparator(Engine selector)
    {
        this.engine = selector;
    }

    @Override
    public int compare(TableContext<R,C> o1, TableContext<R,C> o2)
    {
        return estimateRows(o1) - estimateRows(o2);
    }

    private int estimateRows(TableContext<R,C> tc)
    {
        int estimate = Integer.MAX_VALUE;
        Table<R,C> table = tc.getTable();
        TableMetadata tableMetadata = engine.getTableMetadata(table.getName());
        if (tableMetadata != null)
        {
            estimate = (int) tableMetadata.getCount();
            for (ColumnCondition columnCondition : table.getAndConditions())
            {
                if (columnCondition instanceof ValueComparisonCondition)
                {
                    ValueComparisonCondition cc = (ValueComparisonCondition) columnCondition;
                    if (Relation.EQ.equals(cc.getRelation()))
                    {
                        ColumnMetadata columnMetadata = tableMetadata.getColumnMetadata(cc.getColumn());
                        if (columnMetadata != null)
                        {
                            estimate = Math.min(estimate, (int) columnMetadata.getCount());
                        }
                    }
                }
            }
        }
        return estimate;
    }
}
