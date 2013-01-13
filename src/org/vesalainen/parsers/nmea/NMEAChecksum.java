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

package org.vesalainen.parsers.nmea;

import java.util.zip.Checksum;

/**
 * @author Timo Vesalainen
 */
public class NMEAChecksum implements Checksum
{
    private boolean on;
    private int value;
    @Override
    public void update(int b)
    {
        if (b == '*')
        {
            on = false;
        }
        if (on)
        {
            value ^= b;
        }
        if (b == '$')
        {
            value = 0;
            on = true;
        }
    }

    @Override
    public void update(byte[] b, int off, int len)
    {
        for (int ii=0;ii<len;ii++)
        {
            update(b[ii+off]);
        }
    }

    @Override
    public long getValue()
    {
        return value;
    }

    @Override
    public void reset()
    {
        value = 0;
    }

}
