/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vesalainen.parsers.date;

import org.vesalainen.regex.SyntaxErrorException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.vesalainen.grammar.AnnotatedGrammar;

/**
 * SimpleDateGrammar is build by using same patterns as in java.text.SimpleDateFormat. 
 * @author tkv
 */
public class SimpleDateGrammar extends AnnotatedGrammar
{
    protected DateFormatParser formatParser;
    protected Locale locale;
    protected DateFormatSymbols symbols;
    protected Method[] era;
    protected Method[] month;
    protected Method[] weekday;
    protected Method[] ampm;
    protected Set<String> patternSet = new HashSet<>();

    protected SimpleDateGrammar(Locale locale, Class<?> superClass) throws UnsupportedOperationException, IOException
    {
        this(locale, DateFormatSymbols.getInstance(locale), superClass);
    }

    protected SimpleDateGrammar(Locale locale, DateFormatSymbols symbols, Class<?> superClass) throws UnsupportedOperationException, IOException
    {
        super(DateReducers.class);
        this.locale = locale;
        this.symbols = symbols;
        try
        {
            era = new Method[]
            {
                DateReducers.class.getDeclaredMethod("ad", Calendar.class),
                DateReducers.class.getDeclaredMethod("bc", Calendar.class)
            };
            month = new Method[]
            {
                DateReducers.class.getDeclaredMethod("month1", Calendar.class),
                DateReducers.class.getDeclaredMethod("month2", Calendar.class),
                DateReducers.class.getDeclaredMethod("month3", Calendar.class),
                DateReducers.class.getDeclaredMethod("month4", Calendar.class),
                DateReducers.class.getDeclaredMethod("month5", Calendar.class),
                DateReducers.class.getDeclaredMethod("month6", Calendar.class),
                DateReducers.class.getDeclaredMethod("month7", Calendar.class),
                DateReducers.class.getDeclaredMethod("month8", Calendar.class),
                DateReducers.class.getDeclaredMethod("month9", Calendar.class),
                DateReducers.class.getDeclaredMethod("month10", Calendar.class),
                DateReducers.class.getDeclaredMethod("month11", Calendar.class),
                DateReducers.class.getDeclaredMethod("month12", Calendar.class)
            };
            weekday = new Method[]
            {
                DateReducers.class.getDeclaredMethod("weekday1", Calendar.class),
                DateReducers.class.getDeclaredMethod("weekday2", Calendar.class),
                DateReducers.class.getDeclaredMethod("weekday3", Calendar.class),
                DateReducers.class.getDeclaredMethod("weekday4", Calendar.class),
                DateReducers.class.getDeclaredMethod("weekday5", Calendar.class),
                DateReducers.class.getDeclaredMethod("weekday6", Calendar.class),
                DateReducers.class.getDeclaredMethod("weekday7", Calendar.class)
            };
            ampm = new Method[]
            {
                DateReducers.class.getDeclaredMethod("am", Calendar.class),
                DateReducers.class.getDeclaredMethod("pm", Calendar.class)
            };

            formatParser = DateFormatParser.newInstance(superClass);
        }
        catch (NoSuchMethodException | SecurityException ex)
        {
            throw new UnsupportedOperationException(ex);
        }
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
