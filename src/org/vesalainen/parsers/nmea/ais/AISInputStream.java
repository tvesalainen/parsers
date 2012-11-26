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
import java.io.StringBufferInputStream;

/**
 * @author Timo Vesalainen
 */
public class AISInputStream extends InputStream
{
    private InputStream in;
    private int buffer;
    private int bit;
    
    public AISInputStream(InputStream in)
    {
        this.in = in;
    }
    
    @Override
    public int read() throws IOException
    {
        if (bit == 0)
        {
            buffer = in.read();
            if (buffer == -1)
            {
                return -1;
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
