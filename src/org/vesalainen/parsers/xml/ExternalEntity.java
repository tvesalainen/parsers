/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author tkv
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
