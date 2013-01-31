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

package org.vesalainen.parsers.nmea.ais;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Timo Vesalainen
 */
public class AISInputStream extends InputStream
{
    private InputStream in;
    private boolean decode;
    private int buffer;
    private int bit;
    
    public AISInputStream(InputStream in)
    {
        this.in = in;
    }

    public void setDecode(boolean decode)
    {
        this.decode = decode;
    }
    
    @Override
    public int read() throws IOException
    {
        if (decode)
        {
            int cc = readBit();
            if (cc != -1)
            {
                return cc;
            }
        }
        return in.read();
    }
    public int readBit() throws IOException
    {
        if (bit == 0)
        {
            buffer = in.read();
            if (buffer == ',')
            {
                decode = false;
                return -1;
            }
            if (buffer == -1)
            {
                throw new IOException("unexcpected EOF in AIS Data");
            }
            buffer -= 48;
            if (buffer > 40)
            {
                buffer -= 8;
            }
            bit = 6;
        }
        bit--;
        if ((buffer & (1<<bit)) == 0)
        {
            return '0';
        }
        else
        {
            return '1';
        }
    }
}
