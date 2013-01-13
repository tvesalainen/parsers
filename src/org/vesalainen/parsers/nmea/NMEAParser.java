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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;
import org.vesalainen.parser.ParserConstants;
import org.vesalainen.parser.ParserFactory;
import org.vesalainen.parser.annotation.GenClassname;
import org.vesalainen.parser.annotation.GrammarDef;
import org.vesalainen.parser.annotation.ParseMethod;
import org.vesalainen.parser.annotation.ParserContext;
import org.vesalainen.parser.annotation.RecoverMethod;
import org.vesalainen.parser.annotation.Rule;
import org.vesalainen.parser.annotation.Rules;
import org.vesalainen.parser.annotation.Terminal;
import org.vesalainen.parser.util.InputReader;

/**
 * @author Timo Vesalainen
 */
@GenClassname("org.vesalainen.parsers.nmea.NMEAParserImpl")
@GrammarDef()
@Rules({
    @Rule(left="statements", value="statement*"),
    @Rule(left="statement", value="nmeaStatement"),
    @Rule(left="nmeaStatement", value="'\\$' talkerId '[^\\*]*\\*' checksum")
})
public abstract class NMEAParser
{
    @Rule("talker")
    protected void talkerId(String id, @ParserContext("measurement") Measurement measurement)
    {
        measurement.talkerId(id);
    }
    @Rule("cs")
    protected void checksum(
            String str, 
            @ParserContext("checksum") Checksum checksum,
            @ParserContext("measurement") Measurement measurement
            )
    {
        int sum = Integer.parseInt(str, 16);
        if (sum != checksum.getValue())
        {
            measurement.rollback();
        }
        else
        {
            measurement.commit();
        }
    }
    @Terminal(expression="[A-Z]{2}")
    protected abstract String talker(String s);
            
    @Terminal(expression="[0-9A-Fa-f]{2}")
    protected abstract String cs(String s);
            
    @RecoverMethod
    public void recover(
            @ParserContext(ParserConstants.INPUTREADER) InputReader reader
            ) throws IOException
    {
        int cc = reader.read();
        while (cc != '\n' && cc != -1)
        {
            cc = reader.read();
        }
    }
    public void parse(InputStream is, Measurement measurement)
    {
        Checksum checksum = new NMEAChecksum();
        parse(new CheckedInputStream(is, checksum), checksum, measurement);
    }
    @ParseMethod(start = "statements", size=80)
    protected abstract void parse(
            InputStream is, 
            @ParserContext("checksum") Checksum checksum, 
            @ParserContext("measurement") Measurement measurement
            );
    
    public static NMEAParser newInstance() throws NoSuchMethodException, IOException, NoSuchFieldException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        return (NMEAParser) ParserFactory.getParserInstance(NMEAParser.class);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        try
        {
            FileInputStream fis = new FileInputStream("y:\\NMEA data\\2007_05_25_122516.nmea");
            NMEAParser p = NMEAParser.newInstance();
            p.parse(fis, new Tracer());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
