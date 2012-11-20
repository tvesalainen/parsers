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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.vesalainen.parser.ParserConstants;
import org.vesalainen.parser.annotation.GenClassname;
import org.vesalainen.parser.annotation.GrammarDef;
import org.vesalainen.parser.annotation.ParseMethod;
import org.vesalainen.parser.annotation.ParserContext;
import org.vesalainen.parser.annotation.ReservedWords;
import org.vesalainen.parser.annotation.Rule;
import org.vesalainen.parser.annotation.Rules;
import org.vesalainen.parser.annotation.Terminal;
import org.vesalainen.parser.util.InputReader;
import org.vesalainen.parsers.date.SQLDateParser;
import org.vesalainen.regex.Regex;

/**
 * This SQL parser implements a very small subset of the standard
 * @author Timo Vesalainen 
 * @see <a href="doc-files/SqlParser-statement.html#BNF">BNF Syntax for SQL-statement</a>
 * @see <a href="doc-files/SqlParser-statements.html#BNF">BNF Syntax for SQL-statements</a>
 * @see <a href="http://savage.net.au/SQL/sql-2003-2.bnf.html">BNF Grammar for ISO/IEC 9075-2:2003 - Database Language SQL (SQL-2003) SQL/Foundation</a>
 */
@GenClassname("org.vesalainen.parsers.sql.SqlParserImpl")
@GrammarDef()
public abstract class SqlParser<R, C>
{

    protected SQLDateParser dateParser;

    public SqlParser()
    {
        try
        {
            dateParser = SQLDateParser.newInstance();
        }
        catch (NoSuchMethodException | IOException | NoSuchFieldException | ClassNotFoundException | InstantiationException | IllegalAccessException ex)
        {
            throw new IllegalArgumentException(ex);
        }
    }
    /**
     * 
     * @param sql
     * @param locator 
     * @see <a href="doc-files/SqlParser-statement.html#BNF">BNF Syntax for SQL-statement</a>
     */
    @ParseMethod(start = "statement", syntaxOnly = true, useOffsetLocatorException = true, whiteSpace =
    {
        "whiteSpace", "doubleSlashComment", "hashComment", "cComment"
    })
    protected abstract void check(
            String sql,
            @ParserContext("locator") SQLLocator locator
            );
    /**
     * 
     * @param reader
     * @param locator 
     * @see <a href="doc-files/SqlParser-statement.html#BNF">BNF Syntax for SQL-statement</a>
     */
    @ParseMethod(start = "statement", syntaxOnly = true, useOffsetLocatorException = true, size = 1024, whiteSpace =
    {
        "whiteSpace", "doubleSlashComment", "hashComment", "cComment"
    })
    protected abstract void check(
            InputReader reader,
            @ParserContext("locator") SQLLocator locator
            );
    /**
     * 
     * @param sql
     * @param engine
     * @param correlationMap
     * @param placeholderMap
     * @param locator 
     * @see <a href="doc-files/SqlParser-statements.html#BNF">BNF Syntax for SQL-statements</a>
     */
    @ParseMethod(start = "statements", useOffsetLocatorException = true, whiteSpace =
    {
        "whiteSpace", "doubleSlashComment", "hashComment", "cComment"
    })
    protected abstract void execute(
            String sql,
            @ParserContext("engine") Engine<R, C> engine,
            @ParserContext("correlationMap") Map<String, Table> correlationMap,
            @ParserContext("placeholderMap") Map<String,Placeholder> placeholderMap,
            @ParserContext("locator") SQLLocator locator
            );
    /**
     * 
     * @param is
     * @param engine
     * @param correlationMap
     * @param placeholderMap
     * @param locator 
     * @see <a href="doc-files/SqlParser-statements.html#BNF">BNF Syntax for SQL-statements</a>
     */
    @ParseMethod(start = "statements", useOffsetLocatorException = true, size = 1024, whiteSpace =
    {
        "whiteSpace", "doubleSlashComment", "hashComment", "cComment"
    })
    protected abstract void execute(
            InputStream is,
            @ParserContext("engine") Engine<R, C> engine,
            @ParserContext("correlationMap") Map<String, Table> correlationMap,
            @ParserContext("placeholderMap") Map<String,Placeholder> placeholderMap,
            @ParserContext("locator") SQLLocator locator
            );
    /**
     * 
     * @param sql
     * @param engine
     * @param correlationMap
     * @param placeholderMap
     * @param locator
     * @return 
     * @see <a href="doc-files/SqlParser-statement.html#BNF">BNF Syntax for SQL-statement</a>
     */
    @ParseMethod(start = "statement", useOffsetLocatorException = true, whiteSpace =
    {
        "whiteSpace", "doubleSlashComment", "hashComment", "cComment"
    })
    protected abstract Statement parse(
            String sql,
            @ParserContext("engine") Engine<R, C> engine,
            @ParserContext("correlationMap") Map<String, Table> correlationMap,
            @ParserContext("placeholderMap") Map<String,Placeholder> placeholderMap,
            @ParserContext("locator") SQLLocator locator
            );
    /**
     * 
     * @param is
     * @param engine
     * @param correlationMap
     * @param placeholderMap
     * @param locator
     * @return 
     * @see <a href="doc-files/SqlParser-statement.html#BNF">BNF Syntax for SQL-statement</a>
     */
    @ParseMethod(start = "statement", useOffsetLocatorException = true, size = 1024, whiteSpace =
    {
        "whiteSpace", "doubleSlashComment", "hashComment", "cComment"
    })
    protected abstract Statement parse(
            InputStream is,
            @ParserContext("engine") Engine<R, C> engine,
            @ParserContext("correlationMap") Map<String, Table> correlationMap,
            @ParserContext("placeholderMap") Map<String,Placeholder> placeholderMap,
            @ParserContext("locator") SQLLocator locator
            );

    @Rules(
    {
        @Rule("statement ';'"),
        @Rule("statements statement ';'")
    })
    protected void statements(Statement<R, C> statement, @ParserContext("correlationMap") Map<String, Table> correlationMap)
    {
        statement.execute();
        correlationMap.clear();
    }

    @Rules(
    {
        @Rule("beginWork"),
        @Rule("commitWork"),
        @Rule("rollbackWork"),
        @Rule("updateStatementSearched"),
        @Rule("deleteStatementSearched"),
        @Rule("insertStatement"),
        @Rule("querySpecification"),
        @Rule("showSpecification"),
        @Rule("describeSpecification")
    })
    protected abstract Statement<R, C> statement(Statement statement);

    @Rules(
    {
        @Rule("start transaction"),
        @Rule("begin work?")
    })
    protected Statement<R, C> beginWork(
            @ParserContext("placeholderMap") Map<String,Placeholder> placeholderMap,
            @ParserContext("engine") Engine<R, C> engine
            )
    {
        return new BeginWorkStatement<>(engine, placeholderMap);
    }

    @Rule("commit work?")
    protected Statement<R, C> commitWork(
            @ParserContext("placeholderMap") Map<String,Placeholder> placeholderMap,
            @ParserContext("engine") Engine<R, C> engine
            )
    {
        return new CommitWorkStatement<>(engine, placeholderMap);
    }

    @Rule("rollback work?")
    protected Statement<R, C> rollbackWork(
            @ParserContext("placeholderMap") Map<String,Placeholder> placeholderMap,
            @ParserContext("engine") Engine<R, C> engine
            )
    {
        return new RollbackWorkStatement<>(engine, placeholderMap);
    }

    @Rule("update targetTable set setClause+")
    protected Statement<R, C> updateStatementSearched(
            Table<R, C> table, 
            List<SetClause<R, C>> setClauseList, 
            @ParserContext("placeholderMap") Map<String,Placeholder> placeholderMap,
            @ParserContext("engine") Engine<R, C> engine
            )
    {
        return new UpdateStatement<>(engine, placeholderMap, table, setClauseList);
    }

    @Rule("update targetTable set setClause+ where searchCondition")
    protected Statement<R, C> updateStatementSearched(
            Table<R, C> table, 
            List<SetClause<R, C>> setClauseList, 
            Condition<R, C> condition, 
            @ParserContext("placeholderMap") Map<String,Placeholder> placeholderMap,
            @ParserContext("engine") Engine<R, C> engine)
    {
        return new UpdateStatement<>(engine, placeholderMap, table, setClauseList, condition);
    }

    @Rule("identifier '=' literal")
    protected SetClause<R, C> setClause(String identifier, Literal<R, C> literal)
    {
        return new SetClause<>(identifier, literal);
    }

    @Rule("delete from targetTable")
    protected Statement<R, C> deleteStatementSearched(
            Table<R, C> table, 
            @ParserContext("placeholderMap") Map<String,Placeholder> placeholderMap,
            @ParserContext("engine") Engine<R, C> engine
            )
    {
        return new DeleteStatement<>(engine, placeholderMap, table);
    }

    @Rule("delete from targetTable where searchCondition")
    protected Statement<R, C> deleteStatementSearched(
            Table<R, C> table, 
            Condition<R, C> condition, 
            @ParserContext("placeholderMap") Map<String,Placeholder> placeholderMap,
            @ParserContext("engine") Engine<R, C> engine
            )
    {
        return new DeleteStatement<>(engine, placeholderMap, table, condition);
    }

    @Rule("insert into targetTable insertColumnsAndSource")
    protected Statement<R, C> insertStatement(
            Table<R, C> table, 
            InsertColumnsAndSource<R, C> insertColumnsAndSource, 
            @ParserContext("placeholderMap") Map<String,Placeholder> placeholderMap,
            @ParserContext("engine") Engine<R, C> engine
            )
    {
        return new InsertStatement<>(engine, placeholderMap, table, insertColumnsAndSource);
    }

    @Rule("identifier")
    protected Table<R, C> targetTable(
            String tablename,
            @ParserContext("engine") Engine<R, C> engine,
            @ParserContext("correlationMap") Map<String, Table> correlationMap)
    {
        Table table = getTableForCorrelation(null, engine, correlationMap);
        table.setName(tablename);
        return table;
    }

    @Rules(
    {
        //@Rule("fromSubQuery"),
        @Rule("fromConstructor")
    //@Rule("fromDefault"),
    })
    protected abstract InsertColumnsAndSource insertColumnsAndSource(InsertColumnsAndSource<R, C> insertColumnsAndSource);

    @Rule("'\\(' column ('\\,' column)* '\\)' values '\\(' literal ('\\,' literal)* '\\)'")
    protected InsertColumnsAndSource fromConstructor(String column, List<String> columnList, Literal<R, C> literal, List<Literal<R, C>> valueList)
    {
        columnList.add(0, column);
        valueList.add(0, literal);
        if (columnList.size() != valueList.size())
        {
            throw new IllegalArgumentException("column list and value list sizes differ");
        }
        return new InsertColumnsAndSource(columnList, valueList);
    }

    @Rule("identifier")
    protected abstract String column(String column);

    @Rule("show identifier")
    protected Statement showSpecification(
            String identifier, 
            @ParserContext("placeholderMap") Map<String,Placeholder> placeholderMap,
            @ParserContext("engine") Engine<R, C> engine
            )
    {
        return new ShowStatement(engine, placeholderMap, identifier);
    }

    @Rule("describe identifier")
    protected Statement describeSpecification(
            String identifier, 
            @ParserContext("placeholderMap") Map<String,Placeholder> placeholderMap,
            @ParserContext("engine") Engine<R, C> engine
            )
    {
        return new DescribeStatement(engine, placeholderMap, identifier);
    }

    @Rule("select selectList tableExpression")
    protected Statement querySpecification(
            List<ColumnReference> selectList,
            TableExpression tableExpression,
            @ParserContext("placeholderMap") Map<String,Placeholder> placeholderMap,
            @ParserContext("engine") Engine<R, C> engine,
            @ParserContext("correlationMap") Map<String, Table> correlationMap)
    {
        return new SelectStatement(engine, placeholderMap, selectList, tableExpression, correlationMap);
    }

    @Rule("asterisk")
    protected List<ColumnReference> selectList(
            @ParserContext("engine") Engine<R, C> engine,
            @ParserContext("correlationMap") Map<String, Table> correlationMap)
    {
        getTableForCorrelation(null, engine, correlationMap);
        return null;
    }

    @Rule("selectSublist ('\\,' selectSublist)*")
    protected List<ColumnReference> selectList(ColumnReference columnReference, List<ColumnReference> selectList)
    {
        selectList.add(0, columnReference);
        return selectList;
    }

    @Rule("'\\*'")
    protected void asterisk()
    {
    }

    @Rule("identifier")
    protected ColumnReference selectSublist(
            String column,
            @ParserContext("engine") Engine<R, C> engine,
            @ParserContext("correlationMap") Map<String, Table> correlationMap)
    {
        Table table = getTableForCorrelation(null, engine, correlationMap);
        table.addSelectListColumn(column);
        return new ColumnReferenceImpl(table, column);
    }

    @Rule("identifier '\\.' identifier")
    protected ColumnReference selectSublist(
            String correlationName,
            String column,
            @ParserContext("engine") Engine<R, C> engine,
            @ParserContext("correlationMap") Map<String, Table> correlationMap)
    {
        Table table = getTableForCorrelation(correlationName, engine, correlationMap);
        table.addSelectListColumn(column);
        return new ColumnReferenceImpl(table, correlationName, column);
    }

    @Rule("fromClause whereClause? orderByClause?")
    protected TableExpression tableExpression(Condition condition, List<SortSpecification> sortSpecificationList)
    {
        return new TableExpression(condition, sortSpecificationList);
    }

    @Rule("from tableReference ('\\,' tableReference)*")
    protected void fromClause(Table table, List<Table> list)
    {
        // tables are put in correlationMap in this point already.
        // tableReference returns table object just to have them located.
    }

    @Rule("identifier")
    protected Table tableReference(
            String tablename,
            @ParserContext("engine") Engine<R, C> engine,
            @ParserContext("correlationMap") Map<String, Table> correlationMap)
    {
        if (correlationMap.size() == 1 && correlationMap.containsKey(null))
        {
            Table table = getTableForCorrelation(null, engine, correlationMap);
            table.setName(tablename);
            return table;
        }
        else
        {
            Table table = getTableForCorrelation(tablename, engine, correlationMap);
            table.setName(tablename);
            return table;
        }
    }

    @Rule("identifier as? identifier")
    protected Table tableReference(
            String tablename,
            String correlationName,
            @ParserContext("engine") Engine<R, C> engine,
            @ParserContext("correlationMap") Map<String, Table> correlationMap)
    {
        Table table = getTableForCorrelation(correlationName, engine, correlationMap);
        table.setName(tablename);
        return table;
    }

    protected Table getTableForCorrelation(String correlationName, Engine engine, Map<String, Table> correlationMap)
    {
        if (correlationName != null)
        {
            correlationName = correlationName.toUpperCase();
        }
        Table table = correlationMap.get(correlationName);
        if (table == null)
        {
            table = engine.createTable();
            correlationMap.put(correlationName, table);
        }
        return table;
    }

    @Rule("where searchCondition")
    protected Condition whereClause(Condition condition)
    {
        return condition;
    }

    @Rule("booleanValueExpression")
    protected Condition searchCondition(Condition expression)
    {
        return expression;
    }

    @Rule("booleanTerm")
    protected Condition booleanValueExpression(Condition term)
    {
        return term;
    }

    @Rule("booleanValueExpression or booleanTerm")
    protected Condition booleanValueExpression(Condition expression, Condition term)
    {
        return new OrCondition<>(expression, term);
    }

    @Rule("booleanFactor")
    protected Condition booleanTerm(Condition factor)
    {
        return factor;
    }

    @Rule("booleanTerm and booleanFactor")
    protected Condition booleanTerm(Condition term, Condition factor)
    {
        return new AndCondition<>(term, factor);
    }

    @Rule(left = "booleanFactor", value = "booleanTest")
    protected Condition booleanFactor1(Condition test)
    {
        return test;
    }

    @Rule(left = "booleanFactor", value = "not booleanTest")
    protected Condition booleanFactor2(Condition test)
    {
        return new NotCondition<>(test);
    }

    @Rule(left = "booleanTest", value = "booleanPrimary")
    protected Condition booleanTest1(Condition primary)
    {
        return primary;
    }

    @Rule(left = "booleanTest", value = "booleanPrimary is truthValue")
    protected Condition booleanTest2(Condition primary, TruthValue tvl)
    {
        return new BooleanTestCondition(primary, true, tvl);
    }

    @Rule(left = "booleanTest", value = "booleanPrimary is not truthValue")
    protected Condition booleanTest3(Condition primary, TruthValue tvl)
    {
        return new BooleanTestCondition(primary, true, tvl);
    }

    @Rules(
    {
        @Rule("trueL"),
        @Rule("falseL"),
        @Rule("unknown")
    })
    protected TruthValue truthValue(TruthValue tvl)
    {
        return tvl;
    }

    @Rules(
    {
        @Rule("predicate"),
        @Rule("booleanPredicate")
    })
    protected Condition booleanPrimary(Condition predicate)
    {
        return predicate;
    }

    @Rule("parenthesizedBooleanValueExpression")
    protected Condition booleanPredicate(Condition predicate)
    {
        return predicate;
    }

    @Rule("'\\(' booleanValueExpression '\\)'")
    protected Condition parenthesizedBooleanValueExpression(Condition predicate)
    {
        return predicate;
    }

    @Rules(
    {
        @Rule("comparisonPredicate"),
        @Rule("betweenPredicate"),
        @Rule("inPredicate"),
        @Rule("likePredicate"),
        @Rule("nullPredicate")
    })
    protected abstract Condition predicate(Condition predicate);

    @Rule(left = "comparisonPredicate", value = "rowValuePredicant '=' rowValuePredicant")
    protected Condition comparisonPredicate1(RowValue rv1, RowValue rv2)
    {
        return newComparisonCondition(rv1, Relation.EQ, rv2);
    }

    @Rule(left = "comparisonPredicate", value = "rowValuePredicant '<>' rowValuePredicant")
    protected Condition comparisonPredicate2(RowValue rv1, RowValue rv2)
    {
        return newComparisonCondition(rv1, Relation.NE, rv2);
    }

    @Rule(left = "comparisonPredicate", value = "rowValuePredicant '<' rowValuePredicant")
    protected Condition comparisonPredicate3(RowValue rv1, RowValue rv2)
    {
        return newComparisonCondition(rv1, Relation.LT, rv2);
    }

    @Rule(left = "comparisonPredicate", value = "rowValuePredicant '>' rowValuePredicant")
    protected Condition comparisonPredicate4(RowValue rv1, RowValue rv2)
    {
        return newComparisonCondition(rv1, Relation.GT, rv2);
    }

    @Rule(left = "comparisonPredicate", value = "rowValuePredicant '<=' rowValuePredicant")
    protected Condition comparisonPredicate5(RowValue rv1, RowValue rv2)
    {
        return newComparisonCondition(rv1, Relation.LE, rv2);
    }

    @Rule(left = "comparisonPredicate", value = "rowValuePredicant '>=' rowValuePredicant")
    protected Condition comparisonPredicate6(RowValue rv1, RowValue rv2)
    {
        return newComparisonCondition(rv1, Relation.GE, rv2);
    }

    protected Condition newComparisonCondition(RowValue rv1, Relation relation, RowValue rv2)
    {
        if ((rv1 instanceof Literal) && (rv2 instanceof Literal))
        {
            throw new UnsupportedOperationException("comparing two literal values not supported");
        }
        if ((rv1 instanceof ColumnReference) && (rv2 instanceof ColumnReference))
        {
            ColumnReference cf1 = (ColumnReference) rv1;
            ColumnReference cf2 = (ColumnReference) rv2;
            if (cf1.getTable().equals(cf2.getTable()))
            {
                return new ColumnComparisonInOneTable<>(cf1, relation, cf2);
            }
            else
            {
                return new JoinComparison<>(cf1, relation, cf2);
            }
        }
        if ((rv1 instanceof ColumnReference) && (rv2 instanceof Literal))
        {
            ColumnReference cf1 = (ColumnReference) rv1;
            Literal cf2 = (Literal) rv2;
            return new LiteralComparison<>(cf1, relation, cf2);
        }
        if ((rv1 instanceof Literal) && (rv2 instanceof ColumnReference))
        {
            Literal cf1 = (Literal) rv1;
            ColumnReference cf2 = (ColumnReference) rv2;
            return new LiteralComparison<>(cf2, relation, cf1);
        }
        throw new UnsupportedOperationException("unsupported comparison???");
    }

    @Rule(left = "rowValuePredicant", value = "identifier")
    protected RowValue rowValuePredicant1(
            String column,
            @ParserContext("engine") Engine<R, C> engine,
            @ParserContext("correlationMap") Map<String, Table> correlationMap)
    {
        Table table = getTableForCorrelation(null, engine, correlationMap);
        table.addConditionColumn(column);
        return new ColumnReferenceImpl<>(table, column);
    }

    @Rule(left = "rowValuePredicant", value = "identifier '\\.' identifier")
    protected RowValue rowValuePredicant2(
            String correlationName,
            String column,
            @ParserContext("engine") Engine<R, C> engine,
            @ParserContext("correlationMap") Map<String, Table> correlationMap)
    {
        Table table = getTableForCorrelation(correlationName, engine, correlationMap);
        table.addConditionColumn(column);
        return new ColumnReferenceImpl<>(table, correlationName, column);
    }

    @Rule(left = "rowValuePredicant", value = "literal")
    protected RowValue rowValuePredicant3(Literal<R, C> literal, @ParserContext("engine") Engine<R, C> engine)
    {
        return literal;
    }

    @Rule(left = "betweenPredicate", value = "rowValuePredicant between rowValuePredicant and rowValuePredicant")
    protected Condition betweenPredicate1(RowValue rv1, RowValue rv2, RowValue rv3)
    {
        return new AndCondition<>(
                newComparisonCondition(rv1, Relation.GE, rv2),
                newComparisonCondition(rv1, Relation.LE, rv3));
    }

    @Rule(left = "betweenPredicate", value = "rowValuePredicant not between rowValuePredicant and rowValuePredicant")
    protected Condition betweenPredicate2(RowValue rv1, RowValue rv2, RowValue rv3)
    {
        return new OrCondition<>(
                newComparisonCondition(rv1, Relation.LT, rv2),
                newComparisonCondition(rv1, Relation.GT, rv3));
    }

    @Rule(left = "inPredicate", value = "rowValuePredicant in inPredicateValue")
    protected Condition inPredicate1(RowValue rv, Collection<RowValue> inValues)
    {
        Iterator<RowValue> iterator = inValues.iterator();
        Condition<R, C> comp1 = newComparisonCondition(rv, Relation.EQ, iterator.next());
        if (inValues.size() == 1)
        {
            return comp1;
        }
        OrCondition<R, C> orCond = null;
        while (iterator.hasNext())
        {
            Condition<R, C> comp2 = newComparisonCondition(rv, Relation.EQ, iterator.next());
            if (orCond == null)
            {
                orCond = new OrCondition<>(comp1, comp2);
            }
            else
            {
                orCond = new OrCondition<>(orCond, comp2);
            }
        }
        return orCond;
    }

    @Rule(left = "inPredicate", value = "rowValuePredicant not in inPredicateValue")
    protected Condition inPredicate2(RowValue rv, Collection<RowValue> inValues)
    {
        return new NotCondition(inPredicate1(rv, inValues));
    }

    @Rule(left = "inPredicateValue", value = "'\\(' inValueList '\\)'")
    protected abstract Collection<RowValue> inPredicateValue1(Collection<RowValue> inValues);

    @Rule("rowValuePredicant")
    protected Collection<RowValue> inValueList(RowValue rv)
    {
        Collection<RowValue> inValues = new HashSet<>();
        inValues.add(rv);
        return inValues;
    }

    @Rule("inValueList '\\,' rowValuePredicant")
    protected Collection<RowValue> inValueList(Collection<RowValue> inValues, RowValue rv)
    {
        inValues.add(rv);
        return inValues;
    }

    @Rule(left = "likePredicate", value = "rowValuePredicant like string")
    protected Condition likePredicate1(RowValue rv, String pattern)
    {
        if (rv instanceof ColumnReference)
        {
            ColumnReference cr = (ColumnReference) rv;
            return new LikeCondition((ColumnReference) rv, pattern);
        }
        throw new UnsupportedOperationException("using literal in like not supported");
    }

    @Rule(left = "likePredicate", value = "rowValuePredicant like string escape string")
    protected Condition likePredicate2(RowValue rv, String pattern, String escape)
    {
        if (rv instanceof ColumnReference)
        {
            ColumnReference cr = (ColumnReference) rv;
            return new LikeCondition(cr, pattern, escape);
        }
        throw new UnsupportedOperationException("using literal in like not supported");
    }

    @Rule(left = "likePredicate", value = "rowValuePredicant not like string")
    protected Condition likePredicate3(RowValue rv, String pattern)
    {
        return new NotCondition(likePredicate1(rv, pattern));
    }

    @Rule(left = "likePredicate", value = "rowValuePredicant not like string escape string")
    protected Condition likePredicate4(RowValue rv, String pattern, String escape)
    {
        return new NotCondition(likePredicate2(rv, pattern, escape));
    }

    @Rule(left = "nullPredicate", value = "rowValuePredicant is null")
    protected Condition nullPredicate1(RowValue rv)
    {
        if (rv instanceof ColumnReference)
        {
            ColumnReference cr = (ColumnReference) rv;
            return new NullCondition(cr);
        }
        throw new UnsupportedOperationException("using literal in is null not supported");
    }

    @Rule(left = "nullPredicate", value = "rowValuePredicant is not null")
    protected Condition nullPredicate2(RowValue rv)
    {
        return new NotCondition(nullPredicate1(rv));
    }

    @Rule("':' identifier placeholderType")
    protected Literal<R, C> literal(
            String identifier, 
            Class<? extends C> type,
            @ParserContext("placeholderMap") Map<String,Placeholder> placeholderMap
            )
    {
        Placeholder placeholder = new PlaceholderImpl<>(identifier, type);
        placeholderMap.put(identifier, placeholder);
        return placeholder;
    }

    @Rule()
    protected Class<? extends C>  placeholderType(@ParserContext("engine") Engine<R, C> engine)
    {
        return engine.getDefaultPlaceholderType();
    }

    @Rules(
    {
        @Rule("integer"),
        @Rule("decimal")
    })
    protected Literal<R, C> literal(Number number, @ParserContext("engine") Engine<R, C> engine)
    {
        return new LiteralImpl<>(engine.convert(number));
    }

    @Rule("string")
    protected Literal<R, C> literal(String string, @ParserContext("engine") Engine<R, C> engine)
    {
        return new LiteralImpl<>(engine.convert(string));
    }

    @Rules(
    {
        @Rule(left = "literal", value =
        {
            "dateValue"
        }),
        @Rule(left = "literal", value =
        {
            "timeValue"
        }),
        @Rule(left = "literal", value =
        {
            "timestampValue"
        })
    })
    protected Literal<R, C> dateLiteral(C date, @ParserContext("engine") Engine<R, C> engine)
    {
        return new LiteralImpl<>(date);
    }

    @Rule("date string")
    protected C dateValue(String string, @ParserContext("engine") Engine<R, C> engine)
    {
        Date date = dateParser.parseDate(string);
        return engine.convertDate(date);
    }

    @Rule("time string")
    protected C timeValue(String string, @ParserContext("engine") Engine<R, C> engine)
    {
        Date date = dateParser.parseTime(string);
        return engine.convertTime(date);
    }

    @Rule("timestamp string")
    protected C timestampValue(String string, @ParserContext("engine") Engine<R, C> engine)
    {
        Date date = dateParser.parseTimestamp(string);
        return engine.convertTimestamp(date);
    }

    @Rule("order by sortSpecification ('\\,' sortSpecification)*")
    protected List<SortSpecification> orderByClause(SortSpecification ss, List<SortSpecification> list)
    {
        list.add(0, ss);
        return list;
    }

    @Rule(left = "sortSpecification", value = "rowValuePredicant")
    protected SortSpecification sortSpecification1(RowValue rv)
    {
        return new SortSpecification(rv, true);
    }

    @Rule(left = "sortSpecification", value = "rowValuePredicant asc")
    protected SortSpecification sortSpecification2(RowValue rv)
    {
        return new SortSpecification(rv, true);
    }

    @Rule(left = "sortSpecification", value = "rowValuePredicant desc")
    protected SortSpecification sortSpecification3(RowValue rv)
    {
        return new SortSpecification(rv, false);
    }

    @ReservedWords(value =
    {
        "start",
        "transaction",
        "begin",
        "work",
        "commit",
        "rollback",
        "select",
        "insert",
        "delete",
        "update",
        "set",
        "into",
        "values",
        "from",
        "where",
        "order",
        "by",
        "as",
        "or",
        "and",
        "not",
        "is",
        "in",
        "between",
        "like",
        "escape",
        "null",
        "asc",
        "desc",
        "date",
        "time",
        "timestamp",
        "show",
        "tables",
    },
    options =
    {
        Regex.Option.CASE_INSENSITIVE
    })
    protected void reservedWords(
            @ParserContext(ParserConstants.INPUTREADER) InputReader reader,
            @ParserContext("locator") SQLLocator locator)
    {
        if (locator != null)
        {
            locator.locate(reader.getStart(), reader.getEnd(), SQLLocator.Type.RESERVED_WORD);
        }
    }

    @ReservedWords(left = "describe", value = {"desc|descr|descri|describ|describe",},  options = {Regex.Option.CASE_INSENSITIVE})
    protected void reservedWords2(
            @ParserContext(ParserConstants.INPUTREADER) InputReader reader,
            @ParserContext("locator") SQLLocator locator)
    {
        reservedWords(reader, locator);
    }

    @Terminal(expression = "true", options =
    {
        Regex.Option.CASE_INSENSITIVE
    }, priority = 1)
    protected TruthValue trueL(
            @ParserContext(ParserConstants.INPUTREADER) InputReader reader,
            @ParserContext("locator") SQLLocator locator
            )
    {
        reservedWords(reader, locator);
        return TruthValue.TRUE;
    }

    @Terminal(expression = "false", options =
    {
        Regex.Option.CASE_INSENSITIVE
    }, priority = 1)
    protected TruthValue falseL(
            @ParserContext(ParserConstants.INPUTREADER) InputReader reader,
            @ParserContext("locator") SQLLocator locator
            )
    {
        reservedWords(reader, locator);
        return TruthValue.FALSE;
    }

    @Terminal(expression = "unknown", options =
    {
        Regex.Option.CASE_INSENSITIVE
    }, priority = 1)
    protected TruthValue unknown(
            @ParserContext(ParserConstants.INPUTREADER) InputReader reader,
            @ParserContext("locator") SQLLocator locator
            )
    {
        reservedWords(reader, locator);
        return TruthValue.UNKNOWN;
    }

    @Terminal(expression = "[a-zA-z][a-zA-z0-9_]*")
    protected abstract String identifier(String value);

    @Terminal(expression = "'[^']*'|\"[^\"]*\"")
    protected String string(String value)
    {
        return value.substring(1, value.length() - 1);
    }

    @Terminal(expression = "[\\+\\-]?[0-9]+")
    protected Number integer(String value)
    {
        return Long.parseLong(value);
    }

    @Terminal(expression = "[\\+\\-]?[0-9]+\\.[0-9]+")
    protected Number decimal(String value)
    {
        return Double.parseDouble(value);
    }

    @Terminal(expression = "[ \t\r\n]+")
    protected abstract void whiteSpace();

    @Terminal(expression = "\\-\\-[^\n]*\n")
    protected void doubleSlashComment(
            @ParserContext(ParserConstants.INPUTREADER) InputReader reader,
            @ParserContext("locator") SQLLocator locator
            )
    {
        if (locator != null)
        {
            locator.locate(reader.getStart(), reader.getEnd(), SQLLocator.Type.COMMENT);
        }
    }

    @Terminal(expression = "#[^\n]*\n")
    protected void hashComment(
            @ParserContext(ParserConstants.INPUTREADER) InputReader reader,
            @ParserContext("locator") SQLLocator locator
            )
    {
        doubleSlashComment(reader, locator);
    }

    @Terminal(expression = "/\\*.*\\*/", options =
    {
        Regex.Option.FIXED_ENDER
    })
    protected void cComment(
            @ParserContext(ParserConstants.INPUTREADER) InputReader reader,
            @ParserContext("locator") SQLLocator locator
            )
    {
        doubleSlashComment(reader, locator);
    }
}
