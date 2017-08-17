/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.vesalainen.parsers.date;

import org.vesalainen.regex.SyntaxErrorException;
import java.io.IOException;
import java.util.Locale;

/**
 * InternetDateGrammar makes rules for parses parsing dates in one of ISO8601 formats
 * <PRE>
 * yyyy-MM-dd'T'HH:mm:ss.SSSz
 * yyyy-MM-dd'T'HH:mm:ssz
 * yyyy-MM-dd'T'HH:mmz
 * yyyy-MM-dd'T'HH:mm:ss.SSSZ
 * yyyy-MM-dd'T'HH:mm:ssZ
 * yyyy-MM-dd'T'HH:mmZ
 * yyyy-MM-dd
 * yyyy-MM
 * yyyy
 * </PRE>
 * Or
 *  RFC1123 EEE, dd MMM yyyy HH:mm:ss z
 * Or
 * RFC 850 EEEE, dd-MMM-yy HH:mm:ss z
 * or
 * ASC Time EEE, MMM dd HH:mm:ss yyyy
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public class InternetDateGrammar extends SimpleDateGrammar
{
    public InternetDateGrammar() throws IOException
    {
        super(Locale.US, InternetDateParser.class);
        try
        {
            // RFC1123
            addPattern("rfc1123", "EEE, dd MMM yyyy HH:mm:ss z");
            // RFC 850
            addPattern("rfc850", "EEEE, dd-MMM-yy HH:mm:ss z");
            // ASC Time
            addPattern("ascTime", "EEE, MMM dd HH:mm:ss yyyy");
            // ISO 8601
            addPattern("iso8601", "yyyy-MM-dd'T'HH:mm:ss.SSSz");
            addPattern("iso8601", "yyyy-MM-dd'T'HH:mm:ssz");
            addPattern("iso8601", "yyyy-MM-dd'T'HH:mmz");
            addPattern("iso8601", "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            addPattern("iso8601", "yyyy-MM-dd'T'HH:mm:ssZ");
            addPattern("iso8601", "yyyy-MM-dd'T'HH:mmZ");
            addPattern("iso8601", "yyyy-MM-dd");
            addPattern("iso8601", "yyyy-MM");
            addPattern("iso8601", "yyyy");
        }
        catch (IOException | SyntaxErrorException ex)
        {
            throw new UnsupportedOperationException(ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        try
        {
            InternetDateGrammar g = new InternetDateGrammar();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
