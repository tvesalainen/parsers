package org.vesalainen.parsers.magic;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.spi.FileTypeDetector;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */


public class MagicFileTypeDetector extends FileTypeDetector
{
    private MimeTypes mimeTypes;

    public MagicFileTypeDetector()
    {
        mimeTypes = MimeTypes.getInstance();
    }
    
    @Override
    public String probeContentType(Path path) throws IOException
    {
        return mimeTypes.getType(path.toFile());
    }
    
}
