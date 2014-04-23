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

import java.io.File;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;
import org.vesalainen.parser.GenClassFactory;
import org.vesalainen.parser.annotation.DFAMap;
import org.vesalainen.parser.annotation.DFAMapEntry;
import org.vesalainen.parser.annotation.GenClassname;
import org.vesalainen.parser.annotation.GrammarDef;
import org.vesalainen.parser.util.InputReader;
import org.vesalainen.parsers.magic.Magic.MagicResult;
import org.vesalainen.regex.ant.MapParser;

/**
 * Note! This MimeTypes class is just an example of using DFAMaps. Using 
 * HashMap implementation is faster.
 * kind of 
 * @author Timo Vesalainen
 */
@GenClassname("org.vesalainen.parsers.magic.MimeTypesImpl")
@DFAMap(
        error="Error", 
        eof="Eof",
        value={
@DFAMapEntry(key="ez", value="application/andrew-inset"),
@DFAMapEntry(key="hqx", value="application/mac-binhex40"),
@DFAMapEntry(key="cpt", value="application/mac-compactpro"),
@DFAMapEntry(key="doc", value="application/msword"),
@DFAMapEntry(key="bin", value="application/octet-stream"),
@DFAMapEntry(key="dms", value="application/octet-stream"),
@DFAMapEntry(key="lha", value="application/octet-stream"),
@DFAMapEntry(key="lzh", value="application/octet-stream"),
@DFAMapEntry(key="exe", value="application/octet-stream"),
@DFAMapEntry(key="class", value="application/octet-stream"),
@DFAMapEntry(key="so", value="application/octet-stream"),
@DFAMapEntry(key="dll", value="application/octet-stream"),
@DFAMapEntry(key="oda", value="application/oda"),
@DFAMapEntry(key="pdf", value="application/pdf"),
@DFAMapEntry(key="ai", value="application/postscript"),
@DFAMapEntry(key="eps", value="application/postscript"),
@DFAMapEntry(key="ps", value="application/postscript"),
@DFAMapEntry(key="rtf", value="application/rtf"),
@DFAMapEntry(key="smi", value="application/smil"),
@DFAMapEntry(key="smil", value="application/smil"),
@DFAMapEntry(key="mif", value="application/vnd.mif"),
@DFAMapEntry(key="xls", value="application/vnd.ms-excel"),
@DFAMapEntry(key="ppt", value="application/vnd.ms-powerpoint"),
@DFAMapEntry(key="wbxml", value="application/vnd.wap.wbxml"),
@DFAMapEntry(key="wmlc", value="application/vnd.wap.wmlc"),
@DFAMapEntry(key="wmlsc", value="application/vnd.wap.wmlscriptc"),
@DFAMapEntry(key="bcpio", value="application/x-bcpio"),
@DFAMapEntry(key="bz2", value="application/x-bzip2"),
@DFAMapEntry(key="vcd", value="application/x-cdlink"),
@DFAMapEntry(key="pgn", value="application/x-chess-pgn"),
@DFAMapEntry(key="cpio", value="application/x-cpio"),
@DFAMapEntry(key="csh", value="application/x-csh"),
@DFAMapEntry(key="dcr", value="application/x-director"),
@DFAMapEntry(key="dir", value="application/x-director"),
@DFAMapEntry(key="dxr", value="application/x-director"),
@DFAMapEntry(key="dvi", value="application/x-dvi"),
@DFAMapEntry(key="spl", value="application/x-futuresplash"),
@DFAMapEntry(key="gtar", value="application/x-gtar"),
@DFAMapEntry(key="gz", value="application/x-gzip"),
@DFAMapEntry(key="tgz", value="application/x-gzip"),
@DFAMapEntry(key="hdf", value="application/x-hdf"),
@DFAMapEntry(key="js", value="application/x-javascript"),
@DFAMapEntry(key="kwd", value="application/x-kword"),
@DFAMapEntry(key="kwt", value="application/x-kword"),
@DFAMapEntry(key="ksp", value="application/x-kspread"),
@DFAMapEntry(key="kpr", value="application/x-kpresenter"),
@DFAMapEntry(key="kpt", value="application/x-kpresenter"),
@DFAMapEntry(key="chrt", value="application/x-kchart"),
@DFAMapEntry(key="kil", value="application/x-killustrator"),
@DFAMapEntry(key="skp", value="application/x-koan"),
@DFAMapEntry(key="skd", value="application/x-koan"),
@DFAMapEntry(key="skt", value="application/x-koan"),
@DFAMapEntry(key="skm", value="application/x-koan"),
@DFAMapEntry(key="latex", value="application/x-latex"),
@DFAMapEntry(key="nc", value="application/x-netcdf"),
@DFAMapEntry(key="cdf", value="application/x-netcdf"),
@DFAMapEntry(key="ogg", value="application/x-ogg"),
@DFAMapEntry(key="rpm", value="application/x-rpm"),
@DFAMapEntry(key="sh", value="application/x-sh"),
@DFAMapEntry(key="shar", value="application/x-shar"),
@DFAMapEntry(key="swf", value="application/x-shockwave-flash"),
@DFAMapEntry(key="sit", value="application/x-stuffit"),
@DFAMapEntry(key="sv4cpio", value="application/x-sv4cpio"),
@DFAMapEntry(key="sv4crc", value="application/x-sv4crc"),
@DFAMapEntry(key="tar", value="application/x-tar"),
@DFAMapEntry(key="tcl", value="application/x-tcl"),
@DFAMapEntry(key="tex", value="application/x-tex"),
@DFAMapEntry(key="texinfo", value="application/x-texinfo"),
@DFAMapEntry(key="texi", value="application/x-texinfo"),
@DFAMapEntry(key="t", value="application/x-troff"),
@DFAMapEntry(key="tr", value="application/x-troff"),
@DFAMapEntry(key="roff", value="application/x-troff"),
@DFAMapEntry(key="man", value="application/x-troff-man"),
@DFAMapEntry(key="me", value="application/x-troff-me"),
@DFAMapEntry(key="ms", value="application/x-troff-ms"),
@DFAMapEntry(key="ustar", value="application/x-ustar"),
@DFAMapEntry(key="src", value="application/x-wais-source"),
@DFAMapEntry(key="xhtml", value="application/xhtml+xml"),
@DFAMapEntry(key="xht", value="application/xhtml+xml"),
@DFAMapEntry(key="zip", value="application/zip"),
@DFAMapEntry(key="au", value="audio/basic"),
@DFAMapEntry(key="snd", value="audio/basic"),
@DFAMapEntry(key="mid", value="audio/midi"),
@DFAMapEntry(key="midi", value="audio/midi"),
@DFAMapEntry(key="kar", value="audio/midi"),
@DFAMapEntry(key="mpga", value="audio/mpeg"),
@DFAMapEntry(key="mp2", value="audio/mpeg"),
@DFAMapEntry(key="mp3", value="audio/mpeg"),
@DFAMapEntry(key="aif", value="audio/x-aiff"),
@DFAMapEntry(key="aiff", value="audio/x-aiff"),
@DFAMapEntry(key="aifc", value="audio/x-aiff"),
@DFAMapEntry(key="m3u", value="audio/x-mpegurl"),
@DFAMapEntry(key="ram", value="audio/x-pn-realaudio"),
@DFAMapEntry(key="rm", value="audio/x-pn-realaudio"),
@DFAMapEntry(key="ra", value="audio/x-realaudio"),
@DFAMapEntry(key="wav", value="audio/x-wav"),
@DFAMapEntry(key="pdb", value="chemical/x-pdb"),
@DFAMapEntry(key="xyz", value="chemical/x-xyz"),
@DFAMapEntry(key="bmp", value="image/bmp"),
@DFAMapEntry(key="gif", value="image/gif"),
@DFAMapEntry(key="ief", value="image/ief"),
@DFAMapEntry(key="jpeg", value="image/jpeg"),
@DFAMapEntry(key="jpg", value="image/jpeg"),
@DFAMapEntry(key="jpe", value="image/jpeg"),
@DFAMapEntry(key="png", value="image/png"),
@DFAMapEntry(key="tiff", value="image/tiff"),
@DFAMapEntry(key="tif", value="image/tiff"),
@DFAMapEntry(key="djvu", value="image/vnd.djvu"),
@DFAMapEntry(key="djv", value="image/vnd.djvu"),
@DFAMapEntry(key="wbmp", value="image/vnd.wap.wbmp"),
@DFAMapEntry(key="ras", value="image/x-cmu-raster"),
@DFAMapEntry(key="pnm", value="image/x-portable-anymap"),
@DFAMapEntry(key="pbm", value="image/x-portable-bitmap"),
@DFAMapEntry(key="pgm", value="image/x-portable-graymap"),
@DFAMapEntry(key="ppm", value="image/x-portable-pixmap"),
@DFAMapEntry(key="rgb", value="image/x-rgb"),
@DFAMapEntry(key="xbm", value="image/x-xbitmap"),
@DFAMapEntry(key="xpm", value="image/x-xpixmap"),
@DFAMapEntry(key="xwd", value="image/x-xwindowdump"),
@DFAMapEntry(key="igs", value="model/iges"),
@DFAMapEntry(key="iges", value="model/iges"),
@DFAMapEntry(key="msh", value="model/mesh"),
@DFAMapEntry(key="mesh", value="model/mesh"),
@DFAMapEntry(key="silo", value="model/mesh"),
@DFAMapEntry(key="wrl", value="model/vrml"),
@DFAMapEntry(key="vrml", value="model/vrml"),
@DFAMapEntry(key="css", value="text/css"),
@DFAMapEntry(key="html", value="text/html"),
@DFAMapEntry(key="htm", value="text/html"),
@DFAMapEntry(key="asc", value="text/plain"),
@DFAMapEntry(key="txt", value="text/plain"),
@DFAMapEntry(key="rtx", value="text/richtext"),
@DFAMapEntry(key="rtf", value="text/rtf"),
@DFAMapEntry(key="sgml", value="text/sgml"),
@DFAMapEntry(key="sgm", value="text/sgml"),
@DFAMapEntry(key="tsv", value="text/tab-separated-values"),
@DFAMapEntry(key="wml", value="text/vnd.wap.wml"),
@DFAMapEntry(key="wmls", value="text/vnd.wap.wmlscript"),
@DFAMapEntry(key="etx", value="text/x-setext"),
@DFAMapEntry(key="xml", value="text/xml"),
@DFAMapEntry(key="xsl", value="text/xml"),
@DFAMapEntry(key="xsd", value="text/xml"),
@DFAMapEntry(key="mpeg", value="video/mpeg"),
@DFAMapEntry(key="mpg", value="video/mpeg"),
@DFAMapEntry(key="mpe", value="video/mpeg"),
@DFAMapEntry(key="qt", value="video/quicktime"),
@DFAMapEntry(key="mov", value="video/quicktime"),
@DFAMapEntry(key="mxu", value="video/vnd.mpegurl"),
@DFAMapEntry(key="avi", value="video/x-msvideo"),
@DFAMapEntry(key="movie", value="video/x-sgi-movie"),
@DFAMapEntry(key="ice", value="x-conference/x-cooltalk"),
@DFAMapEntry(key="ico", value="image/ico"),
@DFAMapEntry(key="kml", value="application/vnd.google-earth.kml+xml"),
@DFAMapEntry(key="kmz", value="application/vnd.google-earth.kmz")
})
public abstract class MimeTypes implements MapParser
{
    static final String ERROR = "Error";
    static final String EOF = "Eof";
    
    private static Magic magic;
    private final InputReader reader = new InputReader("");
    private final ReentrantLock lock = new ReentrantLock();

    public MimeTypes()
    {
        magic = Magic.getInstance();
    }
    
    public String getType(String extension)
    {
        lock.lock();
        try
        {
            reader.reuse(extension.toLowerCase());
            return input(reader);
        }
        finally
        {
            lock.unlock();
        }
    }
    public String getType(File file) throws IOException
    {
        String filename = file.getName();
        int idx = filename.lastIndexOf('.');
        if (idx != -1)
        {
            String ext = filename.substring(idx+1);
            String mimeType = getType(ext);
            if (mimeType != null)
            {
                return mimeType;
            }
            else
            {
                MagicResult guess = magic.guess(file);
                for (String extension : guess.getExtensions())
                {
                    mimeType = getType(extension);
                    if (mimeType != null)
                    {
                        return mimeType;
                    }
                }
            }
        }
        return "application/octet-stream";
    }
    public static MimeTypes getInstance()
    {
        MimeTypes mimeTypes = (MimeTypes) GenClassFactory.loadGenInstance(MimeTypes.class);
        if (mimeTypes == null)
        {
            throw new NullPointerException();
        }
        return mimeTypes;
    }

}
