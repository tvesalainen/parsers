/*
 * Copyright (C) 2011 Timo Vesalainen
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
package org.vesalainen.parsers.date;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static junit.framework.Assert.*;

/**
 *
 * @author Timo Vesalainen
 */
public class TestInternetDateGrammar
{
    InternetDateParser dp;
    private static int TESTCYCLES = 1000;
    public TestInternetDateGrammar()
    {
        dp = InternetDateParser.newInstance();
        Locale.setDefault(Locale.US);
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }
/*    
 * yyyy-MM-dd'T'HH:mm:ss.SSSz
 * yyyy-MM-dd'T'HH:mm:ssz
 * yyyy-MM-dd'T'HH:mmz
 * yyyy-MM-dd'T'HH:mm:ss.SSSZ
 * yyyy-MM-dd'T'HH:mm:ssZ
 * yyyy-MM-dd'T'HH:mmZ
 * yyyy-MM-dd
 * yyyy-MM
 * yyyy
     * 
     */

    @Test
    public void testISO8601_1() throws Exception
    {
        long bound = System.currentTimeMillis();
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        for (int ii=0;ii<TESTCYCLES;ii++)
        {
            Date d1 = new Date(rand.nextLong(bound/2, bound));
            String exp = sdf.format(d1);
            Date d2 = dp.parseDate(exp);
            String got = sdf.format(d2);
            assertEquals(exp, got);
        }
    }

    @Test
    public void testISO8601_2() throws Exception
    {
        long bound = System.currentTimeMillis();
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        for (int ii=0;ii<TESTCYCLES;ii++)
        {
            Date d1 = new Date(rand.nextLong(bound/2, bound));
            String exp = sdf.format(d1);
            Date d2 = dp.parseDate(exp);
            String got = sdf.format(d2);
            assertEquals(exp, got);
        }
    }

    @Test
    public void testISO8601_3() throws Exception
    {
        long bound = System.currentTimeMillis();
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmz");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        for (int ii=0;ii<TESTCYCLES;ii++)
        {
            Date d1 = new Date(rand.nextLong(bound/2, bound));
            String exp = sdf.format(d1);
            Date d2 = dp.parseDate(exp);
            String got = sdf.format(d2);
            assertEquals(exp, got);
        }
    }

    @Test
    public void testISO8601_4() throws Exception
    {
        long bound = System.currentTimeMillis();
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        for (int ii=0;ii<TESTCYCLES;ii++)
        {
            Date d1 = new Date(rand.nextLong(bound/2, bound));
            String exp = sdf.format(d1);
            Date d2 = dp.parseDate(exp);
            String got = sdf.format(d2);
            assertEquals(exp, got);
        }
    }

    @Test
    public void testISO8601_5() throws Exception
    {
        long bound = System.currentTimeMillis();
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        for (int ii=0;ii<TESTCYCLES;ii++)
        {
            Date d1 = new Date(rand.nextLong(bound/2, bound));
            String exp = sdf.format(d1);
            Date d2 = dp.parseDate(exp);
            String got = sdf.format(d2);
            assertEquals(exp, got);
        }
    }

    @Test
    public void testISO8601_6() throws Exception
    {
        long bound = System.currentTimeMillis();
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        for (int ii=0;ii<TESTCYCLES;ii++)
        {
            Date d1 = new Date(rand.nextLong(bound/2, bound));
            String exp = sdf.format(d1);
            Date d2 = dp.parseDate(exp);
            String got = sdf.format(d2);
            assertEquals(exp, got);
        }
    }
/*
    @Test
    public void testISO8601_7() throws Exception
    {
        long bound = System.currentTimeMillis();
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int ii=0;ii<TESTCYCLES;ii++)
        {
            Date d1 = new Date(rand.nextLong(bound/2, bound));
            String exp = sdf.format(d1);
            Date d2 = dp.parseDate(exp);
            String got = sdf.format(d2);
            assertEquals(exp, got);
        }
    }

    @Test
    public void testISO8601_8() throws Exception
    {
        long bound = System.currentTimeMillis();
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        for (int ii=0;ii<TESTCYCLES;ii++)
        {
            Date d1 = new Date(rand.nextLong(bound/2, bound));
            String exp = sdf.format(d1);
            Date d2 = dp.parseDate(exp);
            String got = sdf.format(d2);
            assertEquals(exp, got);
        }
    }

    @Test
    public void testISO8601_9() throws Exception
    {
        long bound = System.currentTimeMillis();
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        for (int ii=0;ii<TESTCYCLES;ii++)
        {
            Date d1 = new Date(rand.nextLong(bound/2, bound));
            String exp = sdf.format(d1);
            Date d2 = dp.parseDate(exp);
            String got = sdf.format(d2);
            assertEquals(exp, got);
        }
    }
*/
    @Test
    public void testRFC1123() throws Exception
    {
        long bound = System.currentTimeMillis();
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        for (int ii=0;ii<TESTCYCLES;ii++)
        {
            Date d1 = new Date(rand.nextLong(bound/2, bound));
            String exp = sdf.format(d1);
            Date d2 = dp.parseDate(exp);
            String got = sdf.format(d2);
            assertEquals(exp, got);
        }
    }

    @Test
    public void testRFC850() throws Exception
    {
        long bound = System.currentTimeMillis();
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yy HH:mm:ss z");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        for (int ii=0;ii<TESTCYCLES;ii++)
        {
            Date d1 = new Date(rand.nextLong(bound/2, bound));
            String exp = sdf.format(d1);
            Date d2 = dp.parseDate(exp);
            String got = sdf.format(d2);
            assertEquals(exp, got);
        }
    }

    @Test
    public void testASCTime() throws Exception
    {
        long bound = System.currentTimeMillis();
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM dd HH:mm:ss yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        for (int ii=0;ii<TESTCYCLES;ii++)
        {
            Date d1 = new Date(rand.nextLong(bound/2, bound));
            String exp = sdf.format(d1);
            Date d2 = dp.parseDate(exp);
            String got = sdf.format(d2);
            assertEquals(exp, got);
        }
    }

    @Test
    public void testTZ1() throws Exception
    {
        Date d1 = dp.parseDate("1997-05-08T12:00:00EEST");
        Date d2 = dp.parseDate("1997-05-08T12:00:00GMT+0300");
        assertEquals(d1, d2);
    }

    @Test
    public void testTZ2() throws Exception
    {
        Date d1 = dp.parseDate("1997-12-08T12:00:00Europe/Helsinki");
        Date d2 = dp.parseDate("1997-12-08T12:00:00GMT+0200");
        assertEquals(d1, d2);
    }

    @Test
    public void testTZ3() throws Exception
    {
        Date d1 = dp.parseDate("1997-12-08T12:00:00B");
        Date d2 = dp.parseDate("1997-12-08T12:00:00GMT+0200");
        assertEquals(d1, d2);
    }

    @Test
    public void testTZ4() throws Exception
    {
        Date d1 = dp.parseDate("1997-12-08T12:00:00B");
        Date d2 = dp.parseDate("1997-12-08T12:00:00GMT+02:00");
        assertEquals(d1, d2);
    }

    @Test
    public void testTZ5() throws Exception
    {
        Date d1 = dp.parseDate("1997-12-08T12:00:00B");
        Date d2 = dp.parseDate("1997-12-08T12:00:00+0200");
        assertEquals(d1, d2);
    }

    @Test
    public void testTZ6() throws Exception
    {
        Date d1 = dp.parseDate("1997-12-08T12:00:00B");
        Date d2 = dp.parseDate("1997-12-08T12:00:00+02:00");
        assertEquals(d1, d2);
    }

    @Test
    public void testTZ7() throws Exception
    {
        Date d1 = dp.parseDate("2011-12-08T12:00:00Lord Howe Summer Time");
        Date d2 = dp.parseDate("2011-12-08T12:00:00+11:00");
        assertEquals(d1, d2);
    }

}
