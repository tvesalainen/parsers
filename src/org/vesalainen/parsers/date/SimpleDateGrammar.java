/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vesalainen.parsers.date;

import org.vesalainen.regex.SyntaxErrorException;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.vesalainen.bcc.model.El;
import org.vesalainen.grammar.Grammar;

/**
 * SimpleDateGrammar is build by using same patterns as in java.text.SimpleDateFormat. 
 * @author tkv
 */
public class SimpleDateGrammar extends Grammar
{
    protected DateFormatParser formatParser;
    protected Locale locale;
    protected DateFormatSymbols symbols;
    protected ExecutableElement[] era;
    protected ExecutableElement[] month;
    protected ExecutableElement[] weekday;
    protected ExecutableElement[] ampm;
    protected Set<String> patternSet = new HashSet<>();

    protected SimpleDateGrammar(Locale locale, Class<?> superClass) throws UnsupportedOperationException, IOException
    {
        this(locale, DateFormatSymbols.getInstance(locale), superClass);
    }

    protected SimpleDateGrammar(Locale locale, DateFormatSymbols symbols, Class<?> superClass) throws UnsupportedOperationException, IOException
    {
        this.locale = locale;
        this.symbols = symbols;
        TypeElement parserClass = El.getTypeElement("org.vesalainen.parsers.date.DateReducers");
        TypeElement calendarElement = El.getTypeElement(Calendar.class.getCanonicalName());
        TypeMirror calendarType = calendarElement.asType();
        era = new ExecutableElement[]
        {
            El.getMethod(parserClass, "ad", calendarType),
            El.getMethod(parserClass, "bc", calendarType)
        };
        month = new ExecutableElement[]
        {
            El.getMethod(parserClass, "month1", calendarType),
            El.getMethod(parserClass, "month2", calendarType),
            El.getMethod(parserClass, "month3", calendarType),
            El.getMethod(parserClass, "month4", calendarType),
            El.getMethod(parserClass, "month5", calendarType),
            El.getMethod(parserClass, "month6", calendarType),
            El.getMethod(parserClass, "month7", calendarType),
            El.getMethod(parserClass, "month8", calendarType),
            El.getMethod(parserClass, "month9", calendarType),
            El.getMethod(parserClass, "month10", calendarType),
            El.getMethod(parserClass, "month11", calendarType),
            El.getMethod(parserClass, "month12", calendarType)
        };
        weekday = new ExecutableElement[]
        {
            El.getMethod(parserClass, "weekday1", calendarType),
            El.getMethod(parserClass, "weekday2", calendarType),
            El.getMethod(parserClass, "weekday3", calendarType),
            El.getMethod(parserClass, "weekday4", calendarType),
            El.getMethod(parserClass, "weekday5", calendarType),
            El.getMethod(parserClass, "weekday6", calendarType),
            El.getMethod(parserClass, "weekday7", calendarType)
        };
        ampm = new ExecutableElement[]
        {
            El.getMethod(parserClass, "am", calendarType),
            El.getMethod(parserClass, "pm", calendarType)
        };

        formatParser = DateFormatParser.newInstance(superClass);
    }
    /**
     * Adds a date pattern in java.text.SimpleDateFormat format. Rules left hand side will 
     * be 'date'
     * @param pattern
     * @throws IOException
     * @throws SyntaxErrorException
     * @throws NoSuchMethodException 
     */
    protected void addPattern(String pattern) throws IOException, SyntaxErrorException, NoSuchMethodException
    {
        addPattern("date", pattern);
    }
    /**
     * Adds a date pattern in java.text.SimpleDateFormat format.
     * @param lhs
     * @param pattern
     * @throws IOException 
     */
    protected void addPattern(String lhs, String pattern) throws IOException
    {
        if (patternSet.contains(pattern))
        {
            return;
        }
        patternSet.add(pattern);
        List<String> rhs = (List<String>) formatParser.parse(pattern, this, locale, symbols, era, month, weekday, ampm);
        addRule(lhs, rhs);
        if (!"date".equals(lhs))
        {
            addRule("date", lhs);
        }
    }

}
