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

package org.vesalainen.parsers.nmea.ais;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Timo Vesalainen
 */
public class SwitchingInputStream extends InputStream
{
    private Semaphore mainSemaphore;
    private Semaphore sideSemaphore;
    private boolean starting = true;
    private char prefix;
    private InputStream is;

    public SwitchingInputStream(InputStream is)
    {
        this.is = is;
        mainSemaphore = new Semaphore(0);
        sideSemaphore = new Semaphore(0);
    }

    public Semaphore getMainSemaphore()
    {
        return mainSemaphore;
    }

    public Semaphore getSideSemaphore()
    {
        return sideSemaphore;
    }

    public void setPrefix(char prefix)
    {
        this.prefix = prefix;
    }

    @Override
    public int read() throws IOException
    {
        try
        {
            if (starting)
            {
                starting = false;
                sideSemaphore.acquire();
                if (prefix != 0)
                {
                    return prefix;
                }
            }
            int cc = is.read();
            if (cc == ',')
            {
                mainSemaphore.release();
                sideSemaphore.acquire();
                if (prefix != 0)
                {
                    return prefix;
                }
                cc = is.read();
            }
            return cc;
        }
        catch (InterruptedException ex)
        {
            throw new IOException(ex);
        }
    }
}
