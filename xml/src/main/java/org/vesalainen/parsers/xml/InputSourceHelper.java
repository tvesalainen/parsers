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
package org.vesalainen.parsers.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import org.xml.sax.InputSource;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public class InputSourceHelper
{

    public static InputSource resolv(InputSource input, String systemId) throws URISyntaxException
    {
        String sysId = input.getSystemId();
        if (sysId == null)
        {
            throw new IllegalArgumentException("systemId == null, cannot resolv");
        }
        URI uri = new URI(input.getSystemId());
        InputSource ni = new InputSource(uri.resolve(systemId).toString());
        ni.setByteStream(input.getByteStream());
        ni.setCharacterStream(input.getCharacterStream());
        ni.setEncoding(input.getEncoding());
        ni.setPublicId(input.getPublicId());
        return ni;
    }

    public static Reader getReader(InputSource input) throws UnsupportedEncodingException, MalformedURLException, IOException
    {
        Reader reader = input.getCharacterStream();
        if (reader != null)
        {
            return reader;
        }
        else
        {
            InputStream is = input.getByteStream();
            String encoding = input.getEncoding();
            if (is != null)
            {
                if (encoding != null)
                {
                    return new InputStreamReader(is, encoding);
                }
                else
                {
                    return new InputStreamReader(is, "US-ASCII");
                }
            }
            else
            {
                String sysId = input.getSystemId();
                try
                {
                    URI uri = new URI(sysId);
                    InputStream uis = uri.toURL().openStream();
                    if (encoding != null)
                    {
                        return new InputStreamReader(uis, encoding);
                    }
                    else
                    {
                        return new InputStreamReader(uis, "US-ASCII");
                    }
                }
                catch (URISyntaxException ex)
                {
                    throw new IOException(ex);
                }
            }
        }
    }

}
