/*
 * Copyright (C) 2013 Timo Vesalainen
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

import java.math.BigInteger;
import java.util.Arrays;

/**
 * @author Timo Vesalainen
 */
public class CreditorReferenceFunction extends AbstractFunction
{
    private static final char[] mask = new char[]
    {
        '7', '1', '3', '7', '1', '3', '7', '1', '3', '7', '1', '3', '7', '1', '3', '7', '1', '3', '7'
    };

    public CreditorReferenceFunction(ColumnReference inner)
    {
        super(inner);
    }

    @Override
    public Object function(Object value)
    {
        if (value == null)
        {
            return null;
        }
        if (value instanceof String)
        {
            String str = (String) value;
            CreditorReference vn = new CreditorReference(str, false);
            return vn.toFormattedRFString();
        }
        if (value instanceof Number)
        {
            Number number = (Number) value;
            CreditorReference vn = new CreditorReference(number.longValue(), false);
            return vn.toFormattedRFString();
        }
        throw new IllegalArgumentException("creditorreference cannot process type "+value.getClass());
    }

    public static String create(String prefix, String code)
    {
        BigInteger bbban = new BigInteger(code);
        StringBuilder sb = new StringBuilder();
        sb.append(code);
        for (char cc : prefix.toCharArray())
        {
            int k = cc - 'A' + 10;
            sb.append(k);
        }
        sb.append("00");
        BigInteger bi = new BigInteger(sb.toString());
        BigInteger remainder = bi.remainder(new BigInteger("97"));
        int cd = 98 - remainder.intValue();
        return String.format("%s%02d%d", prefix, cd, bbban);
    }
    public static void check(String str)
    {
        String code = str.replace(" ", "");
        String bbanString = code.substring(4);
        String prefix = code.substring(0,2);
        String iban = create(prefix, bbanString);
        if (!code.equals(iban))
        {
            throw new IllegalArgumentException(str + " illegal format");
        }
    }
    public static String format(String code)
    {
        check(code);
        StringBuilder sb = new StringBuilder();
        for (int ii=0;ii<code.length();ii+=4)
        {
            if (sb.length() > 0)
            {
                sb.append(' ');
            }
            sb.append(code.substring(ii, Math.min(ii+4, code.length())));
        }
        return sb.toString();
    }
    public class CreditorReference
    {

        private char[] buffer = new char[]
        {
            '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0'
        };

        public CreditorReference(String str, boolean check)
        {
            str = str.replace(" ", "");
            if (str.startsWith("RF"))
            {
                str = str.substring(4);
                str.getChars(0, str.length(), buffer, (buffer.length) - str.length());
            }
            else
            {
                int ii;
                if (check)
                {
                    if (str.length() > (buffer.length))
                    {
                        throw new IllegalArgumentException(str + " is too long");
                    }
                }
                else
                {
                    if (str.length() > (buffer.length - 1))
                    {
                        throw new IllegalArgumentException(str + " is too long");
                    }
                }
                for (ii = 0; ii < str.length(); ii++)
                {
                    if (str.charAt(ii) < '0' || str.charAt(ii) > '9')
                    {
                        throw new IllegalArgumentException(str + " contains non numeric characters");
                    }
                }
                if (check)
                {
                    str.getChars(0, str.length(), buffer, (buffer.length) - str.length());
                }
                else
                {
                    str.getChars(0, str.length(), buffer, (buffer.length - 1) - str.length());
                }
                calcCheckDigit(check);
            }
        }

        public CreditorReference(long n, boolean check)
        {
            String str = String.valueOf(n);
            if (check)
            {
                str.getChars(0, str.length(), buffer, (buffer.length) - str.length());
            }
            else
            {
                str.getChars(0, str.length(), buffer, (buffer.length - 1) - str.length());
            }
            calcCheckDigit(check);
        }

        private void calcCheckDigit(boolean check)
        {
            int ii;
            int chk = 0;
            for (ii = 0; ii < (buffer.length - 1); ii++)
            {
                chk += (mask[ii] - '0') * (buffer[ii] - '0');
            }
            if (check)
            {
                if (buffer[(buffer.length - 1)] != (char) ('0' + (char) (((10 - (chk % 10)) % 10))))
                {
                    throw new IllegalArgumentException(this + " has wrong check digit");
                }
            }
            else
            {
                buffer[(buffer.length - 1)] = (char) ('0' + (char) (((10 - (chk % 10)) % 10)));
            }
        }

        public String toBankingBarcodeString()
        {
            int ii;
            StringBuilder sb = new StringBuilder();
            for (ii = 0; ii < buffer.length; ii++)
            {
                sb.append(buffer[ii]);
            }
            return sb.toString();
        }

        public String toRFString()
        {
            return create("RF", new String(buffer));
        }

        public String toFormattedRFString()
        {
            return format(create("RF", new String(buffer)));
        }

        @Override
        public String toString()
        {
            int ii;
            boolean startzero = true;
            StringBuilder sb = new StringBuilder();
            for (ii = 0; ii < buffer.length; ii++)
            {
                if (startzero)
                {
                    if (buffer[ii] != '0')
                    {
                        sb.append(buffer[ii]);
                        startzero = false;
                    }
                }
                else
                {
                    if ((ii % 5) == 0)
                    {
                        sb.append(' ');
                    }
                    sb.append(buffer[ii]);
                }
            }
            return sb.toString();
        }

        public int fill(char version, int start, char[] buf)
        {
            switch (version)
            {
                case '2':
                case '4':
                    for (int ii = 0; ii < buffer.length; ii++)
                    {
                        buf[start + ii] = buffer[ii];
                    }
                    return start + buffer.length;
                case '5':
                    Arrays.fill(buf, start, start + 23, '0');
                    String rfRef = toRFString();
                    char[] cb = rfRef.substring(2, 4).toCharArray();
                    for (int ii = 0; ii < 2; ii++)
                    {
                        buf[start + ii] = cb[ii];
                    }
                    cb = rfRef.substring(4).toCharArray();
                    int fillen = 23 - cb.length;
                    for (int ii = 0; ii < cb.length; ii++)
                    {
                        buf[start + fillen + ii] = cb[ii];
                    }
                    return start + 23;
                default:
                    throw new IllegalArgumentException("wrong version " + version);
            }
        }
    }
}
