/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vesalainen.parsers.xml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

/**
 *
 * @author tkv
 */
public class InputStreamDecoderReader extends Reader
{
    private static final int BUFFERSIZE = 8192;
    private InputStream in;
    private CharsetDecoder decoder;
    private byte[] buffer;
    private ByteBuffer byteBuffer;
    private CharBuffer charBuffer;
    private String charset;

    public InputStreamDecoderReader(InputStream in, String charset)
    {
        this.in = in;
        Charset cs = Charset.forName(charset);
        decoder = cs.newDecoder();
        buffer = new byte[BUFFERSIZE];
        byteBuffer = ByteBuffer.wrap(buffer);
        byteBuffer.flip();
        charBuffer = CharBuffer.allocate(1);
        charBuffer.flip();
    }

    public void setEncoding(String charset)
    {
        if (this.charset == null)
        {
            this.charset = charset;
            Charset cs = Charset.forName(charset);
            decoder = cs.newDecoder();
            assert !charBuffer.hasRemaining();
            charBuffer = CharBuffer.allocate(BUFFERSIZE);
            charBuffer.flip();
        }
        else
        {
            Charset cs = Charset.forName(charset);
            if (!this.charset.equals(charset) && !cs.aliases().contains(charset))
            {
                throw new IllegalArgumentException("attempt to replace "+this.charset+" with "+charset);
            }
        }
    }

    @Override
    public int read() throws IOException
    {
        if (charBuffer.hasRemaining())
        {
            return charBuffer.get();
        }
        if (!byteBuffer.hasRemaining())
        {
            int rc = in.read(buffer);
            if (rc == -1)
            {
                return -1;
            }
            byteBuffer.position(0);
            byteBuffer.limit(rc);
        }
        charBuffer.clear();
        CoderResult res = decoder.decode(byteBuffer, charBuffer, false);
        while (res.isUnderflow())
        {
            assert !byteBuffer.hasRemaining();
            int rc = in.read(buffer);
            byteBuffer.position(0);
            if (rc != -1)
            {
                byteBuffer.limit(rc);
                res = decoder.decode(byteBuffer, charBuffer, false);
            }
            else
            {
                byteBuffer.limit(0);
                decoder.decode(byteBuffer, charBuffer, true);
                decoder.flush(charBuffer);
                break;
            }
        }
        if (res.isError())
        {
            throw new IOException(res.toString());
        }
        charBuffer.flip();
        if (!charBuffer.hasRemaining())
        {
            return -1;
        }
        return charBuffer.get();
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void close() throws IOException
    {
        in.close();
    }
    public static void main(String[] args)
    {
        try
        {
            FileInputStream fis = new FileInputStream ("C:\\Users\\tkv\\Documents\\NetBeansProjects\\KML\\src\\fi\\sw_nets\\kml\\Placemark.xml");
            InputStreamDecoderReader isdr = new InputStreamDecoderReader(fis, "US-ASCII");
            int cc = isdr.read();
            while (cc != -1)
            {
                System.err.print((char)cc);
                cc = isdr.read();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
