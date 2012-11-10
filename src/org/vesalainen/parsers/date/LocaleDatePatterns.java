/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vesalainen.parsers.date;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * LocaleDatePatterns creates rules for date parsing in locale-sensitive way.
 * @author tkv
 */
public class LocaleDatePatterns extends AbstractDatePatterns
{

    public LocaleDatePatterns() throws UnsupportedOperationException, IOException
    {
        this(Locale.getDefault(), DateFormatSymbols.getInstance());
    }

    public LocaleDatePatterns(Locale locale) throws UnsupportedOperationException, IOException
    {
        this(locale, DateFormatSymbols.getInstance(locale));
    }

    public LocaleDatePatterns(Locale locale, DateFormatSymbols symbols) throws UnsupportedOperationException, IOException
    {
        super(locale, symbols, LocaleDateParserBase.class);
        this.locale = locale;
        SimpleDateFormat df = (SimpleDateFormat) DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, locale);
        addPattern("dateTime", df.toPattern());
        df = (SimpleDateFormat) DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
        addPattern("dateTime", df.toPattern());
        df = (SimpleDateFormat) DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, locale);
        addPattern("dateTime", df.toPattern());
        df = (SimpleDateFormat) DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);
        addPattern("dateTime", df.toPattern());
        df = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.FULL, locale);
        addPattern("date", df.toPattern());
        df = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.LONG, locale);
        addPattern("date", df.toPattern());
        df = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
        addPattern("date", df.toPattern());
        df = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.SHORT, locale);
        addPattern("date", df.toPattern());
        df = (SimpleDateFormat) DateFormat.getTimeInstance(DateFormat.FULL, locale);
        addPattern("time", df.toPattern());
        df = (SimpleDateFormat) DateFormat.getTimeInstance(DateFormat.LONG, locale);
        addPattern("time", df.toPattern());
        df = (SimpleDateFormat) DateFormat.getTimeInstance(DateFormat.MEDIUM, locale);
        addPattern("time", df.toPattern());
        df = (SimpleDateFormat) DateFormat.getTimeInstance(DateFormat.SHORT, locale);
        addPattern("time", df.toPattern());
    }

}
