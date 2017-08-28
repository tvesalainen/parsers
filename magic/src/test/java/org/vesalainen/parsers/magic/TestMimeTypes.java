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
package org.vesalainen.parsers.magic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Timo Vesalainen
 */
public class TestMimeTypes
{
    MimeMap map = new MimeMap();
    MimeTypes instance = MimeTypes.getInstance();

    public TestMimeTypes()
    {
    }

    @Test
    public void testFileTypeDetector() throws IOException
    {
        Path pom = Paths.get("pom.xml");
        assertEquals("text/xml", Files.probeContentType(pom));
    }
    
    /**
     * Test of getType method, of class MimeTypes.
     */
    @Test
    public void testGetType()
    {
        System.out.println("getType");
        String extension = "";
        String expResult = "text/rtf";
        String result = instance.getType("rtf");
        assertEquals(expResult, result);
    }

    @Test
    public void testRegexCreateSpeed()
    {
        MimeTypes instance1 = MimeTypes.getInstance();
    }

    @Test
    public void testMapCreateSpeed()
    {
        MimeMap mimeMap = new MimeMap();
    }

    @Test
    public void testRegexSpeed()
    {
        instance.getType("mpg");
        instance.getType("wmls");
        instance.getType("tif");
        instance.getType("mpe");
        instance.getType("kil");
        instance.getType("shar");
        instance.getType("swf");
        instance.getType("spl");
        instance.getType("m3u");
        instance.getType("wmlc");
        instance.getType("wml");
        instance.getType("ogg");
        instance.getType("rpm");
        instance.getType("mpga");
        instance.getType("lha");
        instance.getType("tiff");
        instance.getType("kar");
        instance.getType("mpeg");
        instance.getType("djvu");
        instance.getType("kwd");
        instance.getType("gz");
        instance.getType("texinfo");
        instance.getType("rgb");
        instance.getType("ppt");
        instance.getType("dxr");
        instance.getType("mov");
        instance.getType("sgm");
        instance.getType("asc");
        instance.getType("ppm");
        instance.getType("etx");
        instance.getType("pgn");
        instance.getType("tar");
        instance.getType("pgm");
        instance.getType("bin");
        instance.getType("css");
        instance.getType("gif");
        instance.getType("cdf");
        instance.getType("xyz");
        instance.getType("tcl");
        instance.getType("xhtml");
        instance.getType("dms");
        instance.getType("aif");
        instance.getType("jpe");
        instance.getType("midi");
        instance.getType("igs");
        instance.getType("ksp");
        instance.getType("tsv");
        instance.getType("me");
        instance.getType("src");
        instance.getType("sgml");
        instance.getType("iges");
        instance.getType("man");
        instance.getType("xsl");
        instance.getType("dvi");
        instance.getType("ez");
        instance.getType("ms");
        instance.getType("movie");
        instance.getType("silo");
        instance.getType("jpg");
        instance.getType("kpr");
        instance.getType("sit");
        instance.getType("kpt");
        instance.getType("vrml");
        instance.getType("pnm");
        instance.getType("t");
        instance.getType("png");
        instance.getType("nc");
        instance.getType("xsd");
        instance.getType("doc");
        instance.getType("mif");
        instance.getType("latex");
        instance.getType("mid");
        instance.getType("lzh");
        instance.getType("xls");
        instance.getType("jpeg");
        instance.getType("smil");
        instance.getType("tr");
        instance.getType("pdf");
        instance.getType("js");
        instance.getType("kmz");
        instance.getType("bcpio");
        instance.getType("sv4cpio");
        instance.getType("sv4crc");
        instance.getType("ief");
        instance.getType("wbmp");
        instance.getType("kml");
        instance.getType("avi");
        instance.getType("pdb");
        instance.getType("htm");
        instance.getType("mesh");
        instance.getType("skm");
        instance.getType("so");
        instance.getType("aifc");
        instance.getType("cpt");
        instance.getType("class");
        instance.getType("skp");
        instance.getType("xml");
        instance.getType("aiff");
        instance.getType("rtf");
        instance.getType("sh");
        instance.getType("skt");
        instance.getType("texi");
        instance.getType("msh");
        instance.getType("xbm");
        instance.getType("bmp");
        instance.getType("vcd");
        instance.getType("eps");
        instance.getType("tex");
        instance.getType("wrl");
        instance.getType("dll");
        instance.getType("skd");
        instance.getType("wmlsc");
        instance.getType("exe");
        instance.getType("rtx");
        instance.getType("oda");
        instance.getType("rm");
        instance.getType("ico");
        instance.getType("mp3");
        instance.getType("wav");
        instance.getType("pbm");
        instance.getType("ras");
        instance.getType("txt");
        instance.getType("xpm");
        instance.getType("cpio");
        instance.getType("ra");
        instance.getType("dir");
        instance.getType("ice");
        instance.getType("mp2");
        instance.getType("kwt");
        instance.getType("chrt");
        instance.getType("gtar");
        instance.getType("bz2");
        instance.getType("qt");
        instance.getType("wbxml");
        instance.getType("roff");
        instance.getType("snd");
        instance.getType("zip");
        instance.getType("mxu");
        instance.getType("djv");
        instance.getType("xwd");
        instance.getType("au");
        instance.getType("dcr");
        instance.getType("ai");
        instance.getType("ram");
        instance.getType("hqx");
        instance.getType("hdf");
        instance.getType("ustar");
        instance.getType("html");
        instance.getType("smi");
        instance.getType("tgz");
        instance.getType("csh");
        instance.getType("xht");
        instance.getType("ps");
    }

    @Test
    public void testMapSpeed()
    {
        map.get("mpg");
        map.get("wmls");
        map.get("tif");
        map.get("mpe");
        map.get("kil");
        map.get("shar");
        map.get("swf");
        map.get("spl");
        map.get("m3u");
        map.get("wmlc");
        map.get("wml");
        map.get("ogg");
        map.get("rpm");
        map.get("mpga");
        map.get("lha");
        map.get("tiff");
        map.get("kar");
        map.get("mpeg");
        map.get("djvu");
        map.get("kwd");
        map.get("gz");
        map.get("texinfo");
        map.get("rgb");
        map.get("ppt");
        map.get("dxr");
        map.get("mov");
        map.get("sgm");
        map.get("asc");
        map.get("ppm");
        map.get("etx");
        map.get("pgn");
        map.get("tar");
        map.get("pgm");
        map.get("bin");
        map.get("css");
        map.get("gif");
        map.get("cdf");
        map.get("xyz");
        map.get("tcl");
        map.get("xhtml");
        map.get("dms");
        map.get("aif");
        map.get("jpe");
        map.get("midi");
        map.get("igs");
        map.get("ksp");
        map.get("tsv");
        map.get("me");
        map.get("src");
        map.get("sgml");
        map.get("iges");
        map.get("man");
        map.get("xsl");
        map.get("dvi");
        map.get("ez");
        map.get("ms");
        map.get("movie");
        map.get("silo");
        map.get("jpg");
        map.get("kpr");
        map.get("sit");
        map.get("kpt");
        map.get("vrml");
        map.get("pnm");
        map.get("t");
        map.get("png");
        map.get("nc");
        map.get("xsd");
        map.get("doc");
        map.get("mif");
        map.get("latex");
        map.get("mid");
        map.get("lzh");
        map.get("xls");
        map.get("jpeg");
        map.get("smil");
        map.get("tr");
        map.get("pdf");
        map.get("js");
        map.get("kmz");
        map.get("bcpio");
        map.get("sv4cpio");
        map.get("sv4crc");
        map.get("ief");
        map.get("wbmp");
        map.get("kml");
        map.get("avi");
        map.get("pdb");
        map.get("htm");
        map.get("mesh");
        map.get("skm");
        map.get("so");
        map.get("aifc");
        map.get("cpt");
        map.get("class");
        map.get("skp");
        map.get("xml");
        map.get("aiff");
        map.get("rtf");
        map.get("sh");
        map.get("skt");
        map.get("texi");
        map.get("msh");
        map.get("xbm");
        map.get("bmp");
        map.get("vcd");
        map.get("eps");
        map.get("tex");
        map.get("wrl");
        map.get("dll");
        map.get("skd");
        map.get("wmlsc");
        map.get("exe");
        map.get("rtx");
        map.get("oda");
        map.get("rm");
        map.get("ico");
        map.get("mp3");
        map.get("wav");
        map.get("pbm");
        map.get("ras");
        map.get("txt");
        map.get("xpm");
        map.get("cpio");
        map.get("ra");
        map.get("dir");
        map.get("ice");
        map.get("mp2");
        map.get("kwt");
        map.get("chrt");
        map.get("gtar");
        map.get("bz2");
        map.get("qt");
        map.get("wbxml");
        map.get("roff");
        map.get("snd");
        map.get("zip");
        map.get("mxu");
        map.get("djv");
        map.get("xwd");
        map.get("au");
        map.get("dcr");
        map.get("ai");
        map.get("ram");
        map.get("hqx");
        map.get("hdf");
        map.get("ustar");
        map.get("html");
        map.get("smi");
        map.get("tgz");
        map.get("csh");
        map.get("xht");
        map.get("ps");
    }
}
