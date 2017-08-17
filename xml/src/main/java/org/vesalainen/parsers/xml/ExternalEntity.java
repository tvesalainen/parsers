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
import java.io.PushbackReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.EntityResolver2;
import org.xml.sax.ext.Locator2;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public class ExternalEntity
{
    private InputSource base;
    private String publicId;
    private String systemId;
    private EntityResolver entityResolver;

    ExternalEntity(InputSource base, String pubId, String sysId, EntityResolver entityResolver)
    {
        this.base = base;
        this.publicId = pubId;
        this.systemId = sysId;
        this.entityResolver = entityResolver;
    }

    public String getPublicId()
    {
        return publicId;
    }

    public String getSystemId()
    {
        return systemId;
    }

    public PushbackReader createReader() throws IOException
    {
        try
        {
            InputSource embeddedInput = null;
            if (entityResolver != null)
            {
                if (entityResolver instanceof EntityResolver2)
                {
                    EntityResolver2 er2 = (EntityResolver2) entityResolver;
                    embeddedInput = er2.resolveEntity(null, publicId, base.getSystemId(), systemId);
                }
                else
                {
                    embeddedInput = entityResolver.resolveEntity(publicId, systemId);
                }
            }
            URI uri = new URI(base.getSystemId());
            String newSystemId = uri.resolve(systemId).toString();
            if (embeddedInput == null)
            {
                embeddedInput = new InputSource(newSystemId);
            }
            embeddedInput.setEncoding(base.getEncoding());
            embeddedInput.setPublicId(publicId);
            embeddedInput.setSystemId(newSystemId);
            Reader r = InputSourceHelper.getReader(embeddedInput);
            return new PushbackReader(r);
        }
        catch (URISyntaxException | SAXException ex)
        {
            throw new IOException(ex);
        }    
    }
}
