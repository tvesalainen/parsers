/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.vesalainen.parsers.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

/**
 *
 * @author tkv
 */
public class Test
{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        try
        {
            URL url = new URL("http://www.w3.org/2001/XMLSchema.dtd");
            byte[] buf = new byte[4096];
            InputStream in = url.openStream();
            StringBuilder content = new StringBuilder();
            int rc = in.read(buf);
            while (rc != -1)
            {
                String c = new String(buf, 0, rc);
                content.append(c);
                rc = in.read(buf);
            }
            String[] css = new String[] {"UTF-32LE", "UTF-32BE", "UTF-16LE", "UTF-16BE", "US-ASCII"};
            File dir = new File("c:\\temp");
            for (String cs : css)
            {
                File f = new File(dir, cs+".xml");
                FileOutputStream fos = new FileOutputStream(f);
                OutputStreamWriter osw = new OutputStreamWriter(fos, cs);
                osw.write("<?xml version=\"1.1\" encoding=\""+cs+"\" ?>\n");
                osw.write("<!DOCTYPE kml [\n");
                osw.write(content.toString());
                osw.write("]>\n");
                osw.close();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
