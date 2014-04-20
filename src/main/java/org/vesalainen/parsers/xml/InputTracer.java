/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vesalainen.parsers.xml;

import java.io.IOException;
import java.io.PushbackReader;
import org.vesalainen.parser.util.InputReader;

/**
 *
 * @author tkv
 */
public class InputTracer extends InputReader
{

    public InputTracer(CharSequence text)
    {
        super(text);
    }

    public InputTracer(PushbackReader in, int size)
    {
        super(in, size);
    }

    @Override
    public String getString()
    {
        String s = super.getString();
        System.err.println("input='"+s+"'");
        return s;
    }

    @Override
    public int read() throws IOException
    {
        int cc = super.read();
        System.err.println("read='"+(char)cc+"'");
        return cc;
    }

    @Override
    public void unread() throws IOException
    {
        System.err.println("unread='"+getString()+"'");
        super.unread();
    }

}
