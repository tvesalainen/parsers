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

import java.util.HashMap;
import java.util.Map;

/**
 * @author Timo Vesalainen
 */
public class MimeMap extends HashMap<String,String>
{

    public MimeMap()
    {
        put("ez", "application/andrew-inset");
        put("hqx", "application/mac-binhex40");
        put("cpt", "application/mac-compactpro");
        put("doc", "application/msword");
        put("bin", "application/octet-stream");
        put("dms", "application/octet-stream");
        put("lha", "application/octet-stream");
        put("lzh", "application/octet-stream");
        put("exe", "application/octet-stream");
        put("class", "application/octet-stream");
        put("so", "application/octet-stream");
        put("dll", "application/octet-stream");
        put("oda", "application/oda");
        put("pdf", "application/pdf");
        put("ai", "application/postscript");
        put("eps", "application/postscript");
        put("ps", "application/postscript");
        put("rtf", "application/rtf");
        put("smi", "application/smil");
        put("smil", "application/smil");
        put("mif", "application/vnd.mif");
        put("xls", "application/vnd.ms-excel");
        put("ppt", "application/vnd.ms-powerpoint");
        put("wbxml", "application/vnd.wap.wbxml");
        put("wmlc", "application/vnd.wap.wmlc");
        put("wmlsc", "application/vnd.wap.wmlscriptc");
        put("bcpio", "application/x-bcpio");
        put("bz2", "application/x-bzip2");
        put("vcd", "application/x-cdlink");
        put("pgn", "application/x-chess-pgn");
        put("cpio", "application/x-cpio");
        put("csh", "application/x-csh");
        put("dcr", "application/x-director");
        put("dir", "application/x-director");
        put("dxr", "application/x-director");
        put("dvi", "application/x-dvi");
        put("spl", "application/x-futuresplash");
        put("gtar", "application/x-gtar");
        put("gz", "application/x-gzip");
        put("tgz", "application/x-gzip");
        put("hdf", "application/x-hdf");
        put("js", "application/x-javascript");
        put("kwd", "application/x-kword");
        put("kwt", "application/x-kword");
        put("ksp", "application/x-kspread");
        put("kpr", "application/x-kpresenter");
        put("kpt", "application/x-kpresenter");
        put("chrt", "application/x-kchart");
        put("kil", "application/x-killustrator");
        put("skp", "application/x-koan");
        put("skd", "application/x-koan");
        put("skt", "application/x-koan");
        put("skm", "application/x-koan");
        put("latex", "application/x-latex");
        put("nc", "application/x-netcdf");
        put("cdf", "application/x-netcdf");
        put("ogg", "application/x-ogg");
        put("rpm", "application/x-rpm");
        put("sh", "application/x-sh");
        put("shar", "application/x-shar");
        put("swf", "application/x-shockwave-flash");
        put("sit", "application/x-stuffit");
        put("sv4cpio", "application/x-sv4cpio");
        put("sv4crc", "application/x-sv4crc");
        put("tar", "application/x-tar");
        put("tcl", "application/x-tcl");
        put("tex", "application/x-tex");
        put("texinfo", "application/x-texinfo");
        put("texi", "application/x-texinfo");
        put("t", "application/x-troff");
        put("tr", "application/x-troff");
        put("roff", "application/x-troff");
        put("man", "application/x-troff-man");
        put("me", "application/x-troff-me");
        put("ms", "application/x-troff-ms");
        put("ustar", "application/x-ustar");
        put("src", "application/x-wais-source");
        put("xhtml", "application/xhtml+xml");
        put("xht", "application/xhtml+xml");
        put("zip", "application/zip");
        put("au", "audio/basic");
        put("snd", "audio/basic");
        put("mid", "audio/midi");
        put("midi", "audio/midi");
        put("kar", "audio/midi");
        put("mpga", "audio/mpeg");
        put("mp2", "audio/mpeg");
        put("mp3", "audio/mpeg");
        put("aif", "audio/x-aiff");
        put("aiff", "audio/x-aiff");
        put("aifc", "audio/x-aiff");
        put("m3u", "audio/x-mpegurl");
        put("ram", "audio/x-pn-realaudio");
        put("rm", "audio/x-pn-realaudio");
        put("ra", "audio/x-realaudio");
        put("wav", "audio/x-wav");
        put("pdb", "chemical/x-pdb");
        put("xyz", "chemical/x-xyz");
        put("bmp", "image/bmp");
        put("gif", "image/gif");
        put("ief", "image/ief");
        put("jpeg", "image/jpeg");
        put("jpg", "image/jpeg");
        put("jpe", "image/jpeg");
        put("png", "image/png");
        put("tiff", "image/tiff");
        put("tif", "image/tiff");
        put("djvu", "image/vnd.djvu");
        put("djv", "image/vnd.djvu");
        put("wbmp", "image/vnd.wap.wbmp");
        put("ras", "image/x-cmu-raster");
        put("pnm", "image/x-portable-anymap");
        put("pbm", "image/x-portable-bitmap");
        put("pgm", "image/x-portable-graymap");
        put("ppm", "image/x-portable-pixmap");
        put("rgb", "image/x-rgb");
        put("xbm", "image/x-xbitmap");
        put("xpm", "image/x-xpixmap");
        put("xwd", "image/x-xwindowdump");
        put("igs", "model/iges");
        put("iges", "model/iges");
        put("msh", "model/mesh");
        put("mesh", "model/mesh");
        put("silo", "model/mesh");
        put("wrl", "model/vrml");
        put("vrml", "model/vrml");
        put("css", "text/css");
        put("html", "text/html");
        put("htm", "text/html");
        put("asc", "text/plain");
        put("txt", "text/plain");
        put("rtx", "text/richtext");
        put("rtf", "text/rtf");
        put("sgml", "text/sgml");
        put("sgm", "text/sgml");
        put("tsv", "text/tab-separated-values");
        put("wml", "text/vnd.wap.wml");
        put("wmls", "text/vnd.wap.wmlscript");
        put("etx", "text/x-setext");
        put("xml", "text/xml");
        put("xsl", "text/xml");
        put("xsd", "text/xml");
        put("mpeg", "video/mpeg");
        put("mpg", "video/mpeg");
        put("mpe", "video/mpeg");
        put("qt", "video/quicktime");
        put("mov", "video/quicktime");
        put("mxu", "video/vnd.mpegurl");
        put("avi", "video/x-msvideo");
        put("movie", "video/x-sgi-movie");
        put("ice", "x-conference/x-cooltalk");
        put("ico", "image/ico");
        put("kml", "application/vnd.google-earth.kml+xml");
        put("kmz", "application/vnd.google-earth.kmz");
    }

    public static void main(String... args)
    {
        try
        {
            MimeMap m = new MimeMap();
            for (Map.Entry<String, String> e : m.entrySet())
            {
                System.err.println("@DFAMapEntry(key=\""+e.getKey().replace("\\", "\\\\") +"\", value=\""+e.getValue()+"\"),");
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
}
