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
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import org.vesalainen.parser.ParserConstants;
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
 * This SQL parser implements a subset of the standard
 * @author Timo Vesalainen 
 * @see <a href="doc-files/SqlParser-batchStatement.html#BNF">BNF Syntax for SQL-statements</a>
 * @see <a href="http://savage.net.au/SQL/sql-2003-2.bnf.html">BNF Grammar for ISO/IEC 9075-2:2003 - Database Language SQL (SQL-2003) SQL/Foundation</a>
 * 
 * <p>
 * Functions
 * @see Engine.createFunction
 */
//@GenClassname("org.vesalainen.parsers.sql.SqlParserImpl")
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
    @ParseMethod(start = "batchStatement", syntaxOnly = true, useOffsetLocatorException = true, whiteSpace =
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
    @ParseMethod(start = "batchStatement", syntaxOnly = true, useOffsetLocatorException = true, size = 1024, whiteSpace =
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
     * @param placeholderMap
     * @param locator
     * @return 
     * @see <a href="doc-files/SqlParser-statement.html#BNF">BNF Syntax for SQL-statement</a>
     */
    @ParseMethod(start = "batchStatement", useOffsetLocatorException = true, wideIndex= true, whiteSpace =
    {
        "whiteSpace", "doubleSlashComment", "hashComment", "cComment"
    })
    protected abstract Statement parse(
            String sql,
            @ParserContext("engine") Engine<R, C> engine,
            @ParserContext("tableListStack") Deque<List<Table<R, C>>> tableListStack,
            @ParserContext("placeholderMap") LinkedHashMap<String,Placeholder> placeholderMap,
            @ParserContext("locator") SQLLocator locator
            );
    /**
     * 
     * @param is
     * @param engine
     * @param placeholderMap
     * @param locator
     * @return 
     * @see <a href="doc-files/SqlParser-statement.html#BNF">BNF Syntax for SQL-statement</a>
     */
    @ParseMethod(start = "batchStatement", useOffsetLocatorException = true, size = 1024, wideIndex= true, whiteSpace =
    {
        "whiteSpace", "doubleSlashComment", "hashComment", "cComment"
    })
    protected abstract Statement parse(
            InputStream is,
            @ParserContext("engine") Engine<R, C> engine,
            @ParserContext("tableListStack") Deque<List<Table<R, C>>> tableListStack,
            @ParserContext("placeholderMap") LinkedHashMap<String,Placeholder> placeholderMap,
            @ParserContext("locator") SQLLocator locator
            );

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
    protected abstract Statement<R, C> statement(Statement<R, C> statement);

    @Rule("statementList")
    protected Statement<R, C> batchStatement(
            List<Statement<R, C>> list, 
            @ParserContext("tableListStack") Deque<List<Table<R, C>>> tableListStack,
            @ParserContext("placeholderMap") LinkedHashMap<String,Placeholder> placeholderMap,
            @ParserContext("engine") Engine<R, C> engine
            )
    {
        return new BatchStatement(engine, placeholderMap, list);
    }
    @Rule("statement ';'")
    protected List<Statement<R, C>> statementList(Statement<R, C> statement)
    {
        List<Statement<R, C>> list = new ArrayList<>();
        list.add(statement);
        return list;
    }

    @Rule("statementList statement ';'")
    protected List<Statement<R, C>> statementList(List<Statement<R, C>> list, Statement<R, C> statement)
    {
        list.add(statement);
        return list;
    }

    @Rules(
    {
        @Rule("start transaction"),
        @Rule("begin work?")
    })
    protected Statement<R, C> beginWork(
            @ParserContext("placeholderMap") LinkedHashMap<String,Placeholder<R,C>> placeholderMap,
            @ParserContext("engine") Engine<R, C> engine
            )
    {
        return new BeginWorkStatement<>(engine, placeholderMap);
    }

    @Rule("commit work?")
    protected Statement<R, C> commitWork(
            @ParserContext("placeholderMap") LinkedHashMap<String,Placeholder<R,C>> placeholderMap,
            @ParserContext("engine") Engine<R, C> engine
            )
    {
        return new CommitWorkStatement<>(engine, placeholderMap);
    }

    @Rule("rollback work?")
    protected Statement<R, C> rollbackWork(
            @ParserContext("placeholderMap") LinkedHashMap<String,Placeholder<R,C>> placeholderMap,
            @ParserContext("engine") Engine<R, C> engine
            )
    {
        return new RollbackWorkStatement<>(engine, placeholderMap);
    }

    @Rule("updateStart tableReference set setClause ( '\\,' setClause)*")
    protected Statement<R, C> updateStatementSearched(
            Table<R, C> table, 
            SetClause<R, C> setClause, 
            List<SetClause<R, C>> setClauseList, 
            @ParserContext("placeholderMap") LinkedHashMap<String,Placeholder<R,C>> placeholderMap,
            @ParserContext("engine") Engine<R, C> engine
            )
    {
        setClauseList.add(0, setClause);
        return new UpdateStatement<>(engine, placeholderMap, table, setClauseList);
    }

    @Rule("updateStart tableReference set setClause ( '\\,' setClause)* where searchCondition")
    protected Statement<R, C> updateStatementSearched(
            Table<R, C> table, 
            SetClause<R, C> setClause, 
            List<SetClause<R, C>> setClauseList, 
            Condition<R, C> condition, 
            @ParserContext("placeholderMap") LinkedHashMap<String,Placeholder<R,C>> placeholderMap,
            @ParserContext("engine") Engine<R, C> engine,
            @ParserContext("tableListStack") Deque<List<Table<R, C>>> tableListStack
            )
    {
        tableListStack.pop();
        setClauseList.add(0, setClause);
        return new UpdateStatement<>(engine, placeholderMap, table, setClauseList, condition);
    }

    @Rule("update")
    protected void updateStart(
            @ParserContext("tableListStack") Deque<List<Table<R, C>>> tableListStack
            )
    {
        tableListStack.push(new ArrayList<Table<R, C>>());
    }

    @Rule("identifier '=' literal")
    protected SetClause<R, C> setClause(String identifier, Literal<R, C> literal)
    {
        return new SetClause<>(identifier, literal);
    }

    @Rule("deleteStart from tableReference")
    protected Statement<R, C> deleteStatementSearched(
            Table<R, C> table, 
            @ParserContext("placeholderMap") LinkedHashMap<String,Placeholder<R,C>> placeholderMap,
            @ParserContext("engine") Engine<R, C> engine,
            @ParserContext("tableListStack") Deque<List<Table<R, C>>> tableListStack
            )
    {
        tableListStack.pop();
        return new DeleteStatement<>(engine, placeholderMap, table);
    }

    @Rule("deleteStart from tableReference where searchCondition")
    protected Statement<R, C> deleteStatementSearched(
            Table<R, C> table, 
            Condition<R, C> condition, 
            @ParserContext("placeholderMap") LinkedHashMap<String,Placeholder<R,C>> placeholderMap,
            @ParserContext("engine") Engine<R, C> engine,
            @ParserContext("tableListStack") Deque<List<Table<R, C>>> tableListStack
            )
    {
        tableListStack.pop();
        return new DeleteStatement<>(engine, placeholderMap, table, condition);
    }

    @Rule("delete")
    protected void deleteStart(
            @ParserContext("tableListStack") Deque<List<Table<R, C>>> tableListStack
            )
    {
        tableListStack.push(new ArrayList<Table<R, C>>());
    }

    @Rule("insertStart into tableReference insertColumnsAndSource")
    protected Statement<R, C> insertStatement(
            Table<R, C> table, 
            InsertColumnsAndSource<R, C> insertColumnsAndSource, 
            @ParserContext("placeholderMap") LinkedHashMap<String,Placeholder<R,C>> placeholderMap,
            @ParserContext("engine") Engine<R, C> engine,
            @ParserContext("tableListStack") Deque<List<Table<R, C>>> tableListStack
            )
    {
        tableListStack.pop();
        return new InsertStatement<>(engine, placeholderMap, table, insertColumnsAndSource);
    }

    @Rule("insert")
    protected void insertStart(
            @ParserContext("tableListStack") Deque<List<Table<R, C>>> tableListStack
            )
    {
        tableListStack.push(new ArrayList<Table<R, C>>());
    }

    @Rules(
    {
        @Rule("fromSubQuery"),
        @Rule("fromConstructor")
    //@Rule("fromDefault"),
    })
    protected abstract InsertColumnsAndSource<R, C> insertColumnsAndSource(InsertColumnsAndSource<R, C> insertColumnsAndSource);

    @Rule("'\\(' column ('\\,' column)* '\\)' querySpecification")
    protected InsertColumnsAndSource<R, C> fromSubQuery(String column, List<String> columnList, Statement subSelect)
    {
        SelectStatement select = (SelectStatement) subSelect;
        columnList.add(0, column);
        if (columnList.size() != select.getSelectList().size())
        {
            select.throwException("column list and value list sizes differ");
        }
        return new InsertColumnsAndSource<>(columnList, select);
    }

    @Rule("'\\(' column ('\\,' column)* '\\)' values '\\(' literal ('\\,' literal)* '\\)'")
    protected InsertColumnsAndSource<R, C> fromConstructor(String column, List<String> columnList, Literal<R, C> literal, List<Literal<R, C>> valueList)
    {
        columnList.add(0, column);
        valueList.add(0, literal);
        if (columnList.size() != valueList.size())
        {
            literal.throwException("column list and value list sizes differ");
        }
        return new InsertColumnsAndSource<>(columnList, valueList);
    }

    @Rule("identifier")
    protected abstract String column(String column);

    @Rule("show identifier")
    protected Statement showSpecification(
            String identifier, 
            @ParserContext("placeholderMap") LinkedHashMap<String,Placeholder> placeholderMap,
            @ParserContext("engine") Engine<R, C> engine
            )
    {
        return new ShowStatement(engine, placeholderMap, identifier);
    }

    @Rule("describe identifier")
    protected Statement describeSpecification(
            String identifier, 
            @ParserContext("placeholderMap") LinkedHashMap<String,Placeholder> placeholderMap,
            @ParserContext("engine") Engine<R, C> engine
            )
    {
        return new DescribeStatement(engine, placeholderMap, identifier);
    }

    @Rule("selectStart selectList tableExpression")
    protected Statement querySpecification(
            List<ColumnReference> selectList,
            TableExpression tableExpression,
            @ParserContext("placeholderMap") LinkedHashMap<String,Placeholder> placeholderMap,
            @ParserContext("engine") Engine<R, C> engine,
            @ParserContext("tableListStack") Deque<List<Table<R, C>>> tableListStack
            )
    {
        tableListStack.pop();
        return new SelectStatement(engine, placeholderMap, selectList, tableExpression);
    }

    @Rule("select")
    protected void selectStart(
            @ParserContext("tableListStack") Deque<List<Table<R, C>>> tableListStack
            )
    {
        tableListStack.push(new ArrayList<Table<R, C>>());
    }

    @Rule("asterisk")
    protected List<ColumnReference> selectList(
            @ParserContext("engine") Engine<R, C> engine
            )
    {
        return null;
    }

    @Rule("selectSublist ('\\,' selectSublist)*")
    protected List<ColumnReference> selectList(
            ColumnReference columnReference, 
            List<ColumnReference> selectList
            )
    {
        selectList.add(0, columnReference);
        return selectList;
    }

    @Rule("'\\*'")
    protected void asterisk()
    {
    }

    @Rule("columnReference")
    protected abstract ColumnReference selectSublist(ColumnReference columnReference);

    @Rule("identifier ('\\.' identifier)*")
    protected ColumnReference columnReference(String part, List<String> list)
    {
        list.add(0, part);
        return new ColumnReferenceImpl(list);
    }

    @Rule("function '\\(' selectSublist ('\\,' string)* '\\)'")
    protected ColumnReference selectSublist(
            String funcName,
            ColumnReference inner,
            List<String> args,
            @ParserContext(ParserConstants.INPUTREADER) InputReader reader,
            @ParserContext("engine") Engine<R, C> engine
            )
    {
        try
        {
            return engine.createFunction(inner, funcName, args.toArray(new String[args.size()]));
        }
        catch (IllegalArgumentException ex)
        {
            reader.throwSyntaxErrorException(ex.getMessage(), "");
            return null;
        }
    }

    @Rule("function '\\(' selectSublist '\\,' integer ('\\,' integer)* '\\)'")
    protected ColumnReference selectSublist(
            String funcName,
            ColumnReference inner,
            Number number,
            List<Number> args,
            @ParserContext(ParserConstants.INPUTREADER) InputReader reader,
            @ParserContext("engine") Engine<R, C> engine
            )
    {
        try
        {
            return engine.createFunction(inner, funcName, number, args.toArray(new Number[args.size()]));
        }
        catch (IllegalArgumentException ex)
        {
            reader.throwSyntaxErrorException(ex.getMessage(), "");
            return null;
        }
    }

    @Rule(value="identifier", doc=
            "<p>upper(col), Converts to uppercase"+
            "<p>lower(col), Converts to lowercase"+
            "<p>extract(col, day|hour|minute|second|week|year), extracts number from date. E.g extract(column2, 'YEAR')"+
            "<p>toint(col) Converts column value to integer"+
            "<p>todouble(col) Converts column value to double"+
            "<p>toboolean(col) Converts column value to boolean"+
            "<p>tochar(col) Converts column value to string"+
            "<p>tostring(col) Converts column value to string"+
            "<p>todate(col, format) Converts column value to date using java SimpleDateFormat style format"+
            "<p>format(col, format) Formats column value using String.format"+
            "<p>creditorreference(col) Converts string or number to <a href='http://www.europeanpaymentscouncil.eu/knowledge_bank_detail.cfm?documents_id=144'>Creditor Reference</a> adding check digits"+
            "<p>substr(col, begin, length) Converts to substring. Note begin starts at 0"+
            "<p>substring(col, begin, length) Converts to substring. Note begin starts at 0")
    protected abstract String function(String func);
    
    @Rule("fromClause whereClause? orderByClause?")
    protected TableExpression tableExpression(
            Condition condition, 
            List<SortSpecification> sortSpecificationList,
            @ParserContext("tableListStack") Deque<List<Table<R, C>>> tableListStack
            )
    {
        return new TableExpression(tableListStack.peek(), condition, sortSpecificationList);
    }

    @Rule("from tableReference ('\\,' tableReference)*")
    protected void fromClause(
            Table<R, C> table, 
            List<Table<R, C>> list
            )
    {
    }

    @Rule("(identifier '\\.')? identifier (as? identifier)?")
    protected Table<R, C> tableReference(
            String schema, 
            String tablename,
            String correlationName,
            @ParserContext("engine") Engine<R, C> engine,
            @ParserContext("tableListStack") Deque<List<Table<R, C>>> tableListStack
            )
    {
        Table<R, C> table = engine.createTable(schema, tablename, correlationName);
        tableListStack.peek().add(table);
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
    protected Condition comparisonPredicate1(RowValue rv1, RowValue rv2, @ParserContext("tableListStack") Deque<List<Table<R, C>>> tableListStack)
    {
        return newComparisonCondition(rv1, Relation.EQ, rv2, tableListStack.peek());
    }

    @Rule(left = "comparisonPredicate", value = "rowValuePredicant '<>' rowValuePredicant")
    protected Condition comparisonPredicate2(RowValue rv1, RowValue rv2, @ParserContext("tableListStack") Deque<List<Table<R, C>>> tableListStack)
    {
        return newComparisonCondition(rv1, Relation.NE, rv2, tableListStack.peek());
    }

    @Rule(left = "comparisonPredicate", value = "rowValuePredicant '<' rowValuePredicant")
    protected Condition comparisonPredicate3(RowValue rv1, RowValue rv2, @ParserContext("tableListStack") Deque<List<Table<R, C>>> tableListStack)
    {
        return newComparisonCondition(rv1, Relation.LT, rv2, tableListStack.peek());
    }

    @Rule(left = "comparisonPredicate", value = "rowValuePredicant '>' rowValuePredicant")
    protected Condition comparisonPredicate4(RowValue rv1, RowValue rv2, @ParserContext("tableListStack") Deque<List<Table<R, C>>> tableListStack)
    {
        return newComparisonCondition(rv1, Relation.GT, rv2, tableListStack.peek());
    }

    @Rule(left = "comparisonPredicate", value = "rowValuePredicant '<=' rowValuePredicant")
    protected Condition comparisonPredicate5(RowValue rv1, RowValue rv2, @ParserContext("tableListStack") Deque<List<Table<R, C>>> tableListStack)
    {
        return newComparisonCondition(rv1, Relation.LE, rv2, tableListStack.peek());
    }

    @Rule(left = "comparisonPredicate", value = "rowValuePredicant '>=' rowValuePredicant")
    protected Condition comparisonPredicate6(RowValue rv1, RowValue rv2, @ParserContext("tableListStack") Deque<List<Table<R, C>>> tableListStack)
    {
        return newComparisonCondition(rv1, Relation.GE, rv2, tableListStack.peek());
    }

    protected Condition newComparisonCondition(
            RowValue rv1, 
            Relation relation, 
            RowValue rv2,
            List<Table<R, C>> tableList
            )
    {
        if ((rv1 instanceof Literal) && (rv2 instanceof Literal))
        {
            throw new UnsupportedOperationException("comparing two literal values not supported");
        }
        if ((rv1 instanceof ColumnReference) && (rv2 instanceof ColumnReference))
        {
            ColumnReference cf1 = (ColumnReference) rv1;
            ColumnReference cf2 = (ColumnReference) rv2;
            cf1.resolvTable(tableList);
            cf2.resolvTable(tableList);
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
            cf1.resolvTable(tableList);
            Literal cf2 = (Literal) rv2;
            return new LiteralComparison<>(cf1, relation, cf2);
        }
        if ((rv1 instanceof Literal) && (rv2 instanceof ColumnReference))
        {
            Literal cf1 = (Literal) rv1;
            ColumnReference cf2 = (ColumnReference) rv2;
            cf2.resolvTable(tableList);
            return new LiteralComparison<>(cf2, relation, cf1);
        }
        throw new UnsupportedOperationException("unsupported comparison???");
    }

    @Rule(left = "rowValuePredicant", value = "columnReference")
    protected RowValue rowValuePredicant1(ColumnReference columnReference)
    {
        return columnReference;
    }

    @Rule(left = "rowValuePredicant", value = "literal")
    protected RowValue rowValuePredicant3(Literal<R, C> literal, @ParserContext("engine") Engine<R, C> engine)
    {
        return literal;
    }

    @Rule(left = "betweenPredicate", value = "rowValuePredicant between rowValuePredicant and rowValuePredicant")
    protected Condition betweenPredicate1(RowValue rv1, RowValue rv2, RowValue rv3, @ParserContext("tableListStack") Deque<List<Table<R, C>>> tableListStack)
    {
        return new AndCondition<>(
                newComparisonCondition(rv1, Relation.GE, rv2, tableListStack.peek()),
                newComparisonCondition(rv1, Relation.LE, rv3, tableListStack.peek()));
    }

    @Rule(left = "betweenPredicate", value = "rowValuePredicant not between rowValuePredicant and rowValuePredicant")
    protected Condition betweenPredicate2(RowValue rv1, RowValue rv2, RowValue rv3, @ParserContext("tableListStack") Deque<List<Table<R, C>>> tableListStack)
    {
        return new OrCondition<>(
                newComparisonCondition(rv1, Relation.LT, rv2, tableListStack.peek()),
                newComparisonCondition(rv1, Relation.GT, rv3, tableListStack.peek()));
    }

    @Rule(left = "inPredicate", value = "rowValuePredicant in inPredicateValue")
    protected Condition inPredicate1(RowValue rv, Collection<RowValue> inValues, @ParserContext("tableListStack") Deque<List<Table<R, C>>> tableListStack)
    {
        Iterator<RowValue> iterator = inValues.iterator();
        Condition<R, C> comp1 = newComparisonCondition(rv, Relation.EQ, iterator.next(), tableListStack.peek());
        if (inValues.size() == 1)
        {
            return comp1;
        }
        OrCondition<R, C> orCond = null;
        while (iterator.hasNext())
        {
            Condition<R, C> comp2 = newComparisonCondition(rv, Relation.EQ, iterator.next(), tableListStack.peek());
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
    protected Condition inPredicate2(RowValue rv, Collection<RowValue> inValues, @ParserContext("tableListStack") Deque<List<Table<R, C>>> tableListStack)
    {
        return new NotCondition(inPredicate1(rv, inValues, tableListStack));
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

    @Rule("placeholder")
    protected abstract Literal<R, C> literal(Literal<R, C> placeholder);

    @Rule("':' stringConstant placeholderType")
    protected Literal<R, C> placeholder(
            String identifier, 
            Class<? extends C> type,
            @ParserContext("placeholderMap") Map<String,Placeholder> placeholderMap
            )
    {
        Placeholder placeholder = new PlaceholderImpl<>(identifier, type);
        placeholderMap.put(identifier, placeholder);
        return placeholder;
    }

    @Rule("':' stringConstant literal")
    protected Literal<R, C> placeholder(
            String identifier, 
            Literal<R, C> lit,
            @ParserContext("placeholderMap") Map<String,Placeholder> placeholderMap
            )
    {
        Placeholder placeholder = new PlaceholderImpl<>(identifier, lit);
        placeholderMap.put(identifier, placeholder);
        return placeholder;
    }
    
    @Rule(value="':' stringConstant '\\(' querySpecification '\\)'", doc="query 1st column is used in comparison. If more than 1 column, then rest of the columns form the title")
    protected Literal<R, C> placeholder(
            String identifier, 
            Statement query,
            @ParserContext("placeholderMap") Map<String,Placeholder> placeholderMap
            )
    {
        SelectStatement select = (SelectStatement) query;
        Placeholder placeholder = new PlaceholderImpl<>(identifier, select);
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

    @Rules({
        @Rule(left = "literal", value ={"dateValue"}),
        @Rule(left = "literal", value ={"timeValue"}),
        @Rule(left = "literal", value ={"timestampValue"}),
        @Rule(left = "literal", value ={"currentYearValue"}),
        @Rule(left = "literal", value ={"currentTimestampValue"})
    })
    protected Literal<R, C> dateLiteral(C date, @ParserContext("engine") Engine<R, C> engine)
    {
        return new LiteralImpl<>(date);
    }

    @Rule(value="date string", doc="yyyy-MM-dd")
    protected C dateValue(String string, @ParserContext("engine") Engine<R, C> engine)
    {
        Date date = dateParser.parseDate(string);
        return engine.convertDate(date);
    }

    @Rule(value="time string", doc="HH:mm:ss or HH:mm:ssZ")
    protected C timeValue(String string, @ParserContext("engine") Engine<R, C> engine)
    {
        Date date = dateParser.parseTime(string);
        return engine.convertTime(date);
    }

    @Rule(value="timestamp string", doc="yyyy-MM-dd HH:mm:ss or yyyy-MM-dd HH:mm:ssZ")
    protected C timestampValue(String string, @ParserContext("engine") Engine<R, C> engine)
    {
        Date date = dateParser.parseTimestamp(string);
        return engine.convertTimestamp(date);
    }

    @Rule(value="currentyear '\\(' '\\)'", doc="Returns current year")
    protected C currentYearValue(@ParserContext("engine") Engine<R, C> engine)
    {
        Calendar cal = Calendar.getInstance();
        return engine.convert(cal.get(Calendar.YEAR));
    }

    @Rule(value="now '\\(' '\\)'", doc="Returns current date")
    protected C currentTimestampValue(@ParserContext("engine") Engine<R, C> engine)
    {
        Date date = new Date();
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
        "now",
        "currentyear"
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

    @Rule(left="stringConstant", value="identifier")
    protected abstract String stringConstant1(String str);
    
    @Rule(left="stringConstant", value="string")
    protected abstract String stringConstant2(String str);
    
    @Terminal(expression = "[a-zA-z][a-zA-z0-9_]*")
    protected abstract String identifier(String value);

    @Terminal(expression = "'[^']*'|\"[^\"]*\"|`[^´]´")
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
