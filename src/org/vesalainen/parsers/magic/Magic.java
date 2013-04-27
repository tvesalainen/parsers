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

import org.vesalainen.regex.ant.MapParser;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.vesalainen.parser.GenClassFactory;
import org.vesalainen.parser.annotation.DFAMap;
import org.vesalainen.parser.annotation.DFAMapEntry;
import org.vesalainen.parser.annotation.GenClassname;
import org.vesalainen.parser.util.InputReader;

/**
 * @author Timo Vesalainen
 */
@GenClassname("org.vesalainen.parsers.magic.MagicImpl")
@DFAMap(
        error="Error", 
        eof="Eof",
        value={
@DFAMapEntry(key="\\x00\\x00\\x00\\x0C\\x6A\\x50\\x20\\x20", value="jp2:Various JPEG-2000 image file formats"),
@DFAMapEntry(key="\\x00\\x00\\x00\\x14\\x66\\x74\\x79\\x70", value="mov mp4:QuickTime movie file"),
@DFAMapEntry(key="\\x00\\x00\\x00\\x18\\x66\\x74\\x79\\x70", value="m4v mp4:MPEG-4 video/QuickTime file"),
@DFAMapEntry(key="\\x00\\x00\\x00\\x1C\\x66\\x74\\x79\\x70", value="mp4:MPEG-4 video file"),
@DFAMapEntry(key="\\x00\\x00\\x00\\x20\\x66\\x74\\x79\\x70", value="m4a:Apple Lossless Audio Codec file"),
@DFAMapEntry(key="\\x00\\x00\\x01\\x00", value="ico:Windows icon file"),
@DFAMapEntry(key="\\x00\\x00\\x01\\xBA", value="mpg vob:DVD Video Movie File (video/dvd, video/mpeg) or DVD MPEG2"),
@DFAMapEntry(key="\\x00\\x00\\x02\\x00", value="cur:Windows cursor file"),
@DFAMapEntry(key="\\x00\\x00\\x02\\x00\\x06\\x04\\x06\\x00", value="wk1:Lotus 1-2-3 spreadsheet (v1) file"),
@DFAMapEntry(key="\\x00\\x00\\x1A\\x00\\x00\\x10\\x04\\x00", value="wk3:Lotus 1-2-3 spreadsheet (v3) file"),
@DFAMapEntry(key="\\x00\\x00\\x1A\\x00\\x02\\x10\\x04\\x00", value="wk4 wk5:Lotus 1-2-3 spreadsheet (v4, v5) file"),
@DFAMapEntry(key="\\x00\\x00\\x4D\\x4D\\x58\\x50\\x52", value="qxd:Quark Express document (Intel & Motorola, respectively)"),
@DFAMapEntry(key="\\x00\\x00\\xFE\\xFF", value=":Byte-order mark for 32-bit Unicode Transformation Format/"),
@DFAMapEntry(key="\\x00\\x01\\x00\\x00\\x4D\\x53\\x49\\x53", value="mny:Microsoft Money file"),
@DFAMapEntry(key="\\x00\\x01\\x00\\x00\\x53\\x74\\x61\\x6E", value="mdb accdb:Microsoft Access file"),
@DFAMapEntry(key="\\x00\\x01\\x00\\x08\\x00\\x01\\x00\\x01", value="img:GEM Raster file"),
@DFAMapEntry(key="\\x00\\x01\\x01", value="flt:Qimage filter"),
@DFAMapEntry(key="\\x00\\x01\\x42\\x41", value="aba:Palm Address Book Archive file"),
@DFAMapEntry(key="\\x00\\x01\\x42\\x44", value="dba:Palm DateBook Archive file"),
@DFAMapEntry(key="\\x00\\x06\\x15\\x61\\x00\\x00\\x00\\x02", value="db:SQLite database file"),
@DFAMapEntry(key="\\x00\\x11\\xAF", value="fli:FLIC Animation file"),
@DFAMapEntry(key="\\x00\\x1E\\x84\\x90\\x00\\x00\\x00\\x00", value="snm:Netscape Communicator (v4) mail folder"),
@DFAMapEntry(key="\\x00\\x5C\\x41\\xB1\\xFF", value="enc:Mujahideen Secrets 2 encrypted file"),
@DFAMapEntry(key="\\x00\\xBF", value="sol:Adobe Flash shared object file (e.g., Flash cookies)"),
@DFAMapEntry(key="\\x00\\xFF\\xFF\\xFF\\xFF\\xFF\\xFF\\xFF", value="mdf:Microsoft SQL Server 2000 database"),
@DFAMapEntry(key="\\x01\\x00\\x00\\x00", value="emf:Extended (Enhanced) Windows Metafile Format, printer spool file"),
@DFAMapEntry(key="\\x01\\x00\\x00\\x00\\x01", value="pic:Unknown type picture file"),
@DFAMapEntry(key="\\x01\\x00\\x09\\x00\\x00\\x03", value="wmf:Windows graphics metafile"),
@DFAMapEntry(key="\\x01\\x00\\x39\\x30", value="gdb fdb:VMapSource GPS Waypoint Database"),
@DFAMapEntry(key="\\x01\\x0F\\x00\\x00", value="mdf:Microsoft SQL Server 2000 database"),
@DFAMapEntry(key="\\x01\\x10", value="tr1:Novell LANalyzer capture file"),
@DFAMapEntry(key="\\x01\\xDA\\x01\\x01\\x00\\x03", value="rgb:Silicon Graphics RGB Bitmap"),
@DFAMapEntry(key="\\x01\\xFF\\x02\\x04\\x03\\x02", value="drw:A common signature and file extension for many drawing"),
@DFAMapEntry(key="\\x02\\x64\\x73\\x73", value="dss:Digital Speech Standard (Olympus, Grundig, & Phillips)"),
@DFAMapEntry(key="\\x03", value="dat:Access Data FTK evidence file"),
@DFAMapEntry(key="\\x03\\x00\\x00\\x00\\x41\\x50\\x50\\x52", value="adx:Dreamcast audio file"),
@DFAMapEntry(key="\\x04", value="db4:dBASE IV data file"),
@DFAMapEntry(key="\\x07", value="drw:A common signature and file extension for many drawing"),
@DFAMapEntry(key="\\x07\\x53\\x4B\\x46", value="skf:SkinCrafter skin file"),
@DFAMapEntry(key="\\x07\\x64\\x74\\x32\\x64\\x64\\x74\\x64", value="dtd:DesignTools 2D Design file"),
@DFAMapEntry(key="\\x08", value="db:SQLite database file"),
@DFAMapEntry(key="\\x0C\\xED", value="mp:Monochrome Picture TIFF bitmap file (unconfirmed)"),
@DFAMapEntry(key="\\x0D\\x44\\x4F\\x43", value="doc:Word 2.0 file"),
@DFAMapEntry(key="\\x0E\\x4E\\x65\\x72\\x6F\\x49\\x53\\x4F", value="nri:Nero CD Compilation"),
@DFAMapEntry(key="\\x0E\\x57\\x4B\\x53", value="wks:Works for Windows spreadsheet file"),
@DFAMapEntry(key="\\x11\\x00\\x00\\x00\\x53\\x43\\x43\\x41", value="pf:Windows prefetch file"),
@DFAMapEntry(key="\\x1A\\x00\\x00", value="ntf:National Imagery Transmission Format (NITF) file"),
@DFAMapEntry(key="\\x1A\\x00\\x00\\x04\\x00\\x00", value="nsf:NES Sound file"),
@DFAMapEntry(key="\\x1A\\x0B", value="pak:Quake archive file"),
@DFAMapEntry(key="\\x1A\\x35\\x01\\x00", value="eth:GN Nettest WinPharoah capture file"),
@DFAMapEntry(key="\\x1A\\x45\\xDF\\xA3\\x93\\x42\\x82\\x88", value="mkv:Matroska stream file"),
@DFAMapEntry(key="\\x1A\\x52\\x54\\x53\\x20\\x43\\x4F\\x4D", value="dat:Access Data FTK evidence file"),
@DFAMapEntry(key="\\x1D\\x7D", value="ws:WordStar Version 5.0/6.0 document"),
@DFAMapEntry(key="\\x1F\\x8B\\x08", value="gz tgz:GZIP archive file"),
@DFAMapEntry(key="\\x1F\\x9D", value="tar.z:Compressed tape archive"),
@DFAMapEntry(key="\\x1F\\xA0", value="tar.z:Compressed tape archive"),
@DFAMapEntry(key="\\x21", value="bsb:MapInfo Sea Chart"),
@DFAMapEntry(key="\\x21\\x12", value="ain:AIN Compressed Archive"),
@DFAMapEntry(key="\\x21\\x3C\\x61\\x72\\x63\\x68\\x3E\\x0A", value="lib:Unix archiver (ar) files and Microsoft Program Library"),
@DFAMapEntry(key="\\x21\\x42\\x44\\x4E", value="pst:Microsoft "),
@DFAMapEntry(key="\\x23\\x20", value="msi:Cerius2 file"),
@DFAMapEntry(key="\\x23\\x20\\x44\\x69\\x73\\x6B\\x20\\x44", value="vmdk:VMware 4 Virtual Disk (monolitic disk) file"),
@DFAMapEntry(key="\\x23\\x20\\x4D\\x69\\x63\\x72\\x6F\\x73", value="dsp:Microsoft Developer Studio project file"),
@DFAMapEntry(key="\\x23\\x21\\x41\\x4D\\x52", value="amr:Adaptive Multi-Rate ACELP (Algebraic Code Excited Linear Prediction)"),
@DFAMapEntry(key="\\x23\\x3F\\x52\\x41\\x44\\x49\\x41\\x4E", value="hdr:Install Shield v5.x or 6.x compressed file"),
@DFAMapEntry(key="\\x24\\x46\\x4C\\x32\\x40\\x28\\x23\\x29", value="sav:SPSS Data file"),
@DFAMapEntry(key="\\x25\\x21\\x50\\x53\\x2D\\x41\\x64\\x6F", value="eps:Adobe encapsulated PostScript file"),
@DFAMapEntry(key="\\x25\\x50\\x44\\x46", value="fdf pdf:Adobe Portable Document Format and Forms Document file"),
@DFAMapEntry(key="\\x28\\x54\\x68\\x69\\x73\\x20\\x66\\x69", value="hqx:Macintosh BinHex 4 Compressed Archive"),
@DFAMapEntry(key="\\x2A\\x2A\\x2A\\x20\\x20\\x49\\x6E\\x73", value="log:Symantec Wise Installer log file"),
@DFAMapEntry(key="\\x2E\\x52\\x45\\x43", value="ivr:RealPlayer video file (V11 and later)"),
@DFAMapEntry(key="\\x2E\\x52\\x4D\\x46", value="rm rmvb:RealMedia streaming media file"),
@DFAMapEntry(key="\\x2E\\x52\\x4D\\x46\\x00\\x00\\x00\\x12", value="ra:RealAudio streaming media file"),
@DFAMapEntry(key="\\x2E\\x72\\x61\\xFD\\x00", value="ra:RealAudio streaming media file"),
@DFAMapEntry(key="\\x2E\\x73\\x6E\\x64", value="au:Audacity audio file"),
@DFAMapEntry(key="\\x30", value="cat:Microsoft security catalog file"),
@DFAMapEntry(key="\\x30\\x00\\x00\\x00\\x4C\\x66\\x4C\\x65", value="evt:Windows Event Viewer file"),
@DFAMapEntry(key="\\x30\\x26\\xB2\\x75\\x8E\\x66\\xCF\\x11", value="asf wmv wma:Microsoft Windows Media Audio/Video File"),
@DFAMapEntry(key="\\x30\\x31\\x4F\\x52\\x44\\x4E\\x41\\x4E", value="ntf:National Imagery Transmission Format (NITF) file"),
@DFAMapEntry(key="\\x31\\xBE", value="32 be:2&#xBE;"),
@DFAMapEntry(key="\\x34\\xCD\\xB2\\xA1", value=":Byte-order mark for 32-bit Unicode Transformation Format/"),
@DFAMapEntry(key="\\x37\\x7A\\xBC\\xAF\\x27\\x1C", value="7z:7-Zip compressed file"),
@DFAMapEntry(key="\\x37\\xE4\\x53\\x96\\xC9\\xDB\\xD6\\x07", value=":Byte-order mark for 32-bit Unicode Transformation Format/"),
@DFAMapEntry(key="\\x38\\x42\\x50\\x53", value="psd:Photoshop image file"),
@DFAMapEntry(key="\\x3A\\x56\\x45\\x52\\x53\\x49\\x4F\\x4E", value="sle:Steganos Security Suite virtual secure drive"),
@DFAMapEntry(key="\\x3C", value="asx:Advanced Stream redirector file"),
@DFAMapEntry(key="\\x3C\\x21\\x64\\x6F\\x63\\x74\\x79\\x70", value="dci:AOL HTML mail file"),
@DFAMapEntry(key="\\x3C\\x3F\\x78\\x6D\\x6C\\x20\\x76\\x65", value="xul manifest msc:XML User Interface Language file"),
@DFAMapEntry(key="\\x3C\\x4D\\x61\\x6B\\x65\\x72\\x46\\x69", value="fm mif:Adobe FrameMaker file"),
@DFAMapEntry(key="\\x3F\\x5F\\x03\\x00", value="gid:Windows Help index file"),
@DFAMapEntry(key="\\x41\\x43\\x31\\x30", value="dwg:Generic AutoCAD drawing"),
@DFAMapEntry(key="\\x41\\x43\\x53\\x44", value=":Byte-order mark for 32-bit Unicode Transformation Format/"),
@DFAMapEntry(key="\\x41\\x43\\x76", value="sle:Steganos Security Suite virtual secure drive"),
@DFAMapEntry(key="\\x41\\x45\\x53", value="aes:AES Crypt"),
@DFAMapEntry(key="\\x41\\x4D\\x59\\x4F", value="syw:Harvard Graphics symbol graphic"),
@DFAMapEntry(key="\\x41\\x4F\\x4C\\x20\\x46\\x65\\x65\\x64", value="bag:AOL and AIM buddy list file"),
@DFAMapEntry(key="\\x41\\x4F\\x4C\\x44\\x42", value="idx aby:Quicken QuickFinder Information File"),
@DFAMapEntry(key="\\x41\\x4F\\x4C\\x49\\x44\\x58", value="ind:AOL client preferences/settings file (MAIN.IND)"),
@DFAMapEntry(key="\\x41\\x4F\\x4C\\x49\\x4E\\x44\\x45\\x58", value="abi:AOL address book index file"),
@DFAMapEntry(key="\\x41\\x4F\\x4C\\x56\\x4D\\x31\\x30\\x30", value="pfc org:AOL personal file cabinet (PFC) file"),
@DFAMapEntry(key="\\x41\\x56\\x47\\x36\\x5F\\x49\\x6E\\x74", value="dat:Access Data FTK evidence file"),
@DFAMapEntry(key="\\x41\\x72\\x43\\x01", value="arc:FreeArc compressed file"),
@DFAMapEntry(key="\\x42\\x41\\x41\\x44", value=":Byte-order mark for 32-bit Unicode Transformation Format/"),
@DFAMapEntry(key="\\x42\\x45\\x47\\x49\\x4E\\x3A\\x56\\x43", value="vcf:vCard file"),
@DFAMapEntry(key="\\x42\\x4C\\x49\\x32\\x32\\x33\\x51", value="bin:Thomson Speedtouch series WLAN router firmware"),
@DFAMapEntry(key="\\x42\\x4D", value="bmp dib:Windows (or device-independent) bitmap image"),
@DFAMapEntry(key="\\x42\\x4F\\x4F\\x4B\\x4D\\x4F\\x42\\x49", value="prc:Palmpilot resource file"),
@DFAMapEntry(key="\\x42\\x5A\\x68", value="tar.bz2 tb2 tbz2 bz2:bzip2"),
@DFAMapEntry(key="\\x43\\x23\\x2B\\x44\\xA4\\x43\\x4D\\xA5", value="rtd:RagTime document file"),
@DFAMapEntry(key="\\x43\\x42\\x46\\x49\\x4C\\x45", value="cbd:WordPerfect dictionary file (unconfirmed)"),
@DFAMapEntry(key="\\x43\\x44\\x30\\x30\\x31", value="iso:ISO-9660 CD Disc Image"),
@DFAMapEntry(key="\\x43\\x49\\x53\\x4F", value="cso:Compressed ISO (CISO) CD image"),
@DFAMapEntry(key="\\x43\\x4D\\x58\\x31", value="clb:COM+ Catalog file"),
@DFAMapEntry(key="\\x43\\x4F\\x4D\\x2B", value="clb:COM+ Catalog file"),
@DFAMapEntry(key="\\x43\\x4F\\x57\\x44", value="vmdk:VMware 4 Virtual Disk (monolitic disk) file"),
@DFAMapEntry(key="\\x43\\x50\\x54\\x37\\x46\\x49\\x4C\\x45", value="cpt:Corel Photopaint file"),
@DFAMapEntry(key="\\x43\\x50\\x54\\x46\\x49\\x4C\\x45", value="cpt:Corel Photopaint file"),
@DFAMapEntry(key="\\x43\\x52\\x45\\x47", value="dat:Access Data FTK evidence file"),
@DFAMapEntry(key="\\x43\\x52\\x55\\x53\\x48\\x20\\x76", value="cru:Crush compressed archive"),
@DFAMapEntry(key="\\x43\\x57\\x53", value="swf:Macromedia Shockwave Flash player file"),
@DFAMapEntry(key="\\x43\\x61\\x74\\x61\\x6C\\x6F\\x67\\x20", value="ctf:WhereIsIt Catalog file"),
@DFAMapEntry(key="\\x43\\x6C\\x69\\x65\\x6E\\x74\\x20\\x55", value="dat:Access Data FTK evidence file"),
@DFAMapEntry(key="\\x44\\x41\\x58\\x00", value="dax:DAX Compressed CD image"),
@DFAMapEntry(key="\\x44\\x42\\x46\\x48", value="db:SQLite database file"),
@DFAMapEntry(key="\\x44\\x4D\\x53\\x21", value="dms:Amiga DiskMasher compressed archive"),
@DFAMapEntry(key="\\x44\\x4F\\x53", value="adf:Antenna data file"),
@DFAMapEntry(key="\\x44\\x56\\x44", value="dvr:DVR-Studio stream file"),
@DFAMapEntry(key="\\x45\\x4C\\x49\\x54\\x45\\x20\\x43\\x6F", value="cdr:Sony Compressed Voice File"),
@DFAMapEntry(key="\\x45\\x4E\\x54\\x52\\x59\\x56\\x43\\x44", value="vcd:VideoVCD (GNU VCDImager) file"),
@DFAMapEntry(key="\\x45\\x50", value="mdi:Microsoft Document Imaging file"),
@DFAMapEntry(key="\\x45\\x52\\x46\\x53\\x53\\x41\\x56\\x45", value="dat:Access Data FTK evidence file"),
@DFAMapEntry(key="\\x45\\x56\\x46\\x09\\x0D\\x0A\\xFF\\x00", value="e:Logical File Evidence Format (EWF-L01) as used in later versions of"),
@DFAMapEntry(key="\\x45\\x56\\x46\\x32\\x0D\\x0A\\x81", value="ex:EnCase&reg; Evidence File Format Version 2 (Ex01)."),
@DFAMapEntry(key="\\x45\\x6C\\x66\\x46\\x69\\x6C\\x65\\x00", value="evtx:Windows Vista event log file"),
@DFAMapEntry(key="\\x45\\x86\\x00\\x00\\x06\\x00", value="qbb:Intuit QuickBooks backup file"),
@DFAMapEntry(key="\\x46\\x41\\x58\\x43\\x4F\\x56\\x45\\x52", value="cpe:Microsoft Fax Cover Sheet"),
@DFAMapEntry(key="\\x46\\x44\\x42\\x48\\x00", value="fdb:Fiasco database definition file"),
@DFAMapEntry(key="\\x46\\x45\\x44\\x46", value="sbv:(Unknown file type)"),
@DFAMapEntry(key="\\x46\\x49\\x4C\\x45", value=":Byte-order mark for 32-bit Unicode Transformation Format/"),
@DFAMapEntry(key="\\x46\\x4C\\x56\\x01", value="flv:Flash video file"),
@DFAMapEntry(key="\\x46\\x4F\\x52\\x4D\\x00", value="aiff:Audio Interchange File"),
@DFAMapEntry(key="\\x46\\x57\\x53", value="swf:Macromedia Shockwave Flash player file"),
@DFAMapEntry(key="\\x46\\x72\\x6F\\x6D\\x20\\x20\\x20", value="20 6f 3f 46 72 6d:From ???"),
@DFAMapEntry(key="\\x46\\x72\\x6F\\x6D\\x3A\\x20", value="eml:A commmon file extension for e-mail files. This variant is"),
@DFAMapEntry(key="\\x47\\x46\\x31\\x50\\x41\\x54\\x43\\x48", value="pat:GIMP (GNU Image Manipulation Program) pattern file"),
@DFAMapEntry(key="\\x47\\x49\\x46\\x38\\x37\\x61", value="gif:GIF87a"),
@DFAMapEntry(key="\\x47\\x49\\x46\\x38\\x39\\x61", value="gif:GIF87a"),
@DFAMapEntry(key="\\x47\\x50\\x41\\x54", value="pat:GIMP (GNU Image Manipulation Program) pattern file"),
@DFAMapEntry(key="\\x47\\x58\\x32", value="gx2:Show Partner graphics file (not confirmed)"),
@DFAMapEntry(key="\\x47\\x65\\x6E\\x65\\x74\\x65\\x63\\x20", value="g64:Genetec video archive"),
@DFAMapEntry(key="\\x48\\x48\\x47\\x42\\x31", value="sh3:Harvard Graphics presentation file"),
@DFAMapEntry(key="\\x49\\x20\\x49", value="tif tiff:BigTIFF files; Tagged Image File Format files >4 GB"),
@DFAMapEntry(key="\\x49\\x44\\x33", value="mp3:MPEG-1 Audio Layer 3 (MP3) audio file"),
@DFAMapEntry(key="\\x49\\x44\\x33\\x03\\x00\\x00\\x00", value="koz:Sprint Music Store audio file (for mobile devices)"),
@DFAMapEntry(key="\\x49\\x49\\x1A\\x00\\x00\\x00\\x48\\x45", value="crw:Canon digital camera RAW file"),
@DFAMapEntry(key="\\x49\\x49\\x2A\\x00", value="tif tiff:BigTIFF files; Tagged Image File Format files >4 GB"),
@DFAMapEntry(key="\\x49\\x49\\x2A\\x00\\x10\\x00\\x00\\x00", value="cr2:Canon digital camera RAW file"),
@DFAMapEntry(key="\\x49\\x53\\x63\\x28", value="cab hdr:Microsoft cabinet file"),
@DFAMapEntry(key="\\x49\\x54\\x4F\\x4C\\x49\\x54\\x4C\\x53", value="lit:Microsoft Reader eBook file"),
@DFAMapEntry(key="\\x49\\x54\\x53\\x46", value="chi chm:Microsoft Compiled HTML Help File"),
@DFAMapEntry(key="\\x49\\x6E\\x6E\\x6F\\x20\\x53\\x65\\x74", value="dat:Access Data FTK evidence file"),
@DFAMapEntry(key="\\x49\\x6E\\x74\\x65\\x72\\x40\\x63\\x74", value="ipd:Inter@ctive Pager Backup (BlackBerry) backup file"),
@DFAMapEntry(key="\\x4A\\x41\\x52\\x43\\x53\\x00", value="jar:Jar archive"),
@DFAMapEntry(key="\\x4A\\x47\\x03\\x0E", value="47 04 0e 4a:JG.."),
@DFAMapEntry(key="\\x4B\\x44\\x4D", value="vmdk:VMware 4 Virtual Disk (monolitic disk) file"),
@DFAMapEntry(key="\\x4B\\x44\\x4D\\x56", value="vmdk:VMware 4 Virtual Disk (monolitic disk) file"),
@DFAMapEntry(key="\\x4B\\x47\\x42\\x5F\\x61\\x72\\x63\\x68", value="kgb:KGB archive"),
@DFAMapEntry(key="\\x4B\\x49\\x00\\x00", value="shd:Windows Server 2003 printer spool file"),
@DFAMapEntry(key="\\x4B\\x57\\x41\\x4A\\x88\\xF0\\x27\\xD1", value=":Byte-order mark for 32-bit Unicode Transformation Format/"),
@DFAMapEntry(key="\\x4C\\x00\\x00\\x00\\x01\\x14\\x02\\x00", value="lnk:Windows shortcut file. See also "),
@DFAMapEntry(key="\\x4C\\x01", value="obj:Relocatable object code"),
@DFAMapEntry(key="\\x4C\\x4E\\x02\\x00", value="gid:Windows Help index file"),
@DFAMapEntry(key="\\x4C\\x56\\x46\\x09\\x0D\\x0A\\xFF\\x00", value="e:Logical File Evidence Format (EWF-L01) as used in later versions of"),
@DFAMapEntry(key="\\x4D\\x2D\\x57\\x20\\x50\\x6F\\x63\\x6B", value="pdb:BGBlitz (professional Backgammon software) position database file"),
@DFAMapEntry(key="\\x4D\\x41\\x52\\x31\\x00", value="mar:MAr compressed archive"),
@DFAMapEntry(key="\\x4D\\x41\\x52\\x43", value="mar:MAr compressed archive"),
@DFAMapEntry(key="\\x4D\\x41\\x72\\x30\\x00", value="mar:MAr compressed archive"),
@DFAMapEntry(key="\\x4D\\x44\\x4D\\x50\\x93\\xA7", value="hdmp:Windows heap dump file"),
@DFAMapEntry(key="\\x4D\\x49\\x4C\\x45\\x53", value="mls:Milestones v2.1a project management and scheduling software"),
@DFAMapEntry(key="\\x4D\\x4C\\x53\\x57", value="mls:Milestones v2.1a project management and scheduling software"),
@DFAMapEntry(key="\\x4D\\x4D\\x00\\x2A", value="tif tiff:BigTIFF files; Tagged Image File Format files >4 GB"),
@DFAMapEntry(key="\\x4D\\x4D\\x00\\x2B", value="tif tiff:BigTIFF files; Tagged Image File Format files >4 GB"),
@DFAMapEntry(key="\\x4D\\x4D\\x4D\\x44\\x00\\x00", value="mmf:Yamaha Corp. Synthetic music Mobile Application Format (SMAF)"),
@DFAMapEntry(key="\\x4D\\x52\\x56\\x4E", value="nvram:VMware BIOS (non-volatile RAM) state file."),
@DFAMapEntry(key="\\x4D\\x53\\x43\\x46", value="cab:Microsoft cabinet file"),
@DFAMapEntry(key="\\x4D\\x53\\x46\\x54\\x02\\x00\\x01\\x00", value="tlb:OLE, SPSS, or Visual C++ type library file"),
@DFAMapEntry(key="\\x4D\\x53\\x5F\\x56\\x4F\\x49\\x43\\x45", value="dvf cdr:Sony Compressed Voice File"),
@DFAMapEntry(key="\\x4D\\x54\\x68\\x64", value="midi mid:Musical Instrument Digital Interface (MIDI) sound file"),
@DFAMapEntry(key="\\x4D\\x56", value="dsn:CD Stomper Pro label file"),
@DFAMapEntry(key="\\x4D\\x56\\x32\\x31\\x34", value="mls:Milestones v2.1a project management and scheduling software"),
@DFAMapEntry(key="\\x4D\\x56\\x32\\x43", value="mls:Milestones v2.1a project management and scheduling software"),
@DFAMapEntry(key="\\x4D\\x5A", value="drv com sys qtx dll qts pif exe:Windows/DOS executable file"),
@DFAMapEntry(key="\\x4D\\x5A\\x90\\x00\\x03\\x00\\x00\\x00", value="api:Acrobat plug-in"),
@DFAMapEntry(key="\\x4D\\x69\\x63\\x72\\x6F\\x73\\x6F\\x66", value="sln pdb:Visual Studio .NET Solution file"),
@DFAMapEntry(key="\\x4D\\x73\\x52\\x63\\x66", value="gdb:VMapSource GPS Waypoint Database"),
@DFAMapEntry(key="\\x4E\\x41\\x56\\x54\\x52\\x41\\x46\\x46", value="dat:Access Data FTK evidence file"),
@DFAMapEntry(key="\\x4E\\x42\\x2A\\x00", value="jtp jnt:MS Windows journal file"),
@DFAMapEntry(key="\\x4E\\x45\\x53\\x4D\\x1A\\x01", value="nsf:NES Sound file"),
@DFAMapEntry(key="\\x4E\\x49\\x54\\x46\\x30", value="ntf:National Imagery Transmission Format (NITF) file"),
@DFAMapEntry(key="\\x4E\\x61\\x6D\\x65\\x3A\\x20", value="cod:Agent newsreader character map file"),
@DFAMapEntry(key="\\x4F\\x50\\x4C\\x44\\x61\\x74\\x61\\x62", value="dbf:Psion Series 3 Database file"),
@DFAMapEntry(key="\\x4F\\x67\\x67\\x53\\x00\\x02\\x00\\x00", value="oga ogv ogg ogx:Ogg Vorbis Codec compressed Multimedia file"),
@DFAMapEntry(key="\\x4F\\x7B", value="dw4:Visio/DisplayWrite 4 text file (unconfirmed)"),
@DFAMapEntry(key="\\x50\\x00\\x00\\x00\\x20\\x00\\x00\\x00", value="idx:Quicken QuickFinder Information File"),
@DFAMapEntry(key="\\x50\\x35\\x0A", value="pgm:Portable Graymap Graphic"),
@DFAMapEntry(key="\\x50\\x41\\x43\\x4B", value="pak:Quake archive file"),
@DFAMapEntry(key="\\x50\\x41\\x47\\x45\\x44\\x55\\x36\\x34", value="dmp:Windows memory dump"),
@DFAMapEntry(key="\\x50\\x41\\x47\\x45\\x44\\x55\\x4D\\x50", value="dmp:Windows memory dump"),
@DFAMapEntry(key="\\x50\\x41\\x58", value="pax:PAX password protected bitmap"),
@DFAMapEntry(key="\\x50\\x45\\x53\\x54", value="dat:Access Data FTK evidence file"),
@DFAMapEntry(key="\\x50\\x47\\x50\\x64\\x4D\\x41\\x49\\x4E", value="pgd:PGP disk image"),
@DFAMapEntry(key="\\x50\\x49\\x43\\x54\\x00\\x08", value="img:GEM Raster file"),
@DFAMapEntry(key="\\x50\\x4B\\x03\\x04", value="zip:ZLock Pro encrypted ZIP"),
@DFAMapEntry(key="\\x50\\x4B\\x03\\x04\\x14\\x00\\x01\\x00", value="zip:ZLock Pro encrypted ZIP"),
@DFAMapEntry(key="\\x50\\x4B\\x03\\x04\\x14\\x00\\x06\\x00", value="xlsx docx pptx:Microsoft Office Open XML Format (OOXML) Document"),
@DFAMapEntry(key="\\x50\\x4B\\x03\\x04\\x14\\x00\\x08\\x00", value="jar:Jar archive"),
@DFAMapEntry(key="\\x50\\x4B\\x05\\x06", value="08 4b 07 50:PK.."),
@DFAMapEntry(key="\\x50\\x4D\\x43\\x43", value="grp:Windows Program Manager group file"),
@DFAMapEntry(key="\\x50\\x4E\\x43\\x49\\x55\\x4E\\x44\\x4F", value="dat:Access Data FTK evidence file"),
@DFAMapEntry(key="\\x51\\x46\\x49\\xFB", value="img:GEM Raster file"),
@DFAMapEntry(key="\\x51\\x57\\x20\\x56\\x65\\x72\\x2E\\x20", value="abd qsd:Quicken data file"),
@DFAMapEntry(key="\\x52\\x41\\x5A\\x41\\x54\\x44\\x42\\x31", value="dat:Access Data FTK evidence file"),
@DFAMapEntry(key="\\x52\\x45\\x47\\x45\\x44\\x49\\x54", value="sud reg:Windows NT Registry and Registry Undo files"),
@DFAMapEntry(key="\\x52\\x45\\x56\\x4E\\x55\\x4D\\x3A\\x2C", value="adf:Antenna data file"),
@DFAMapEntry(key="\\x52\\x49\\x46\\x46", value="ani:Windows animated cursor"),
@DFAMapEntry(key="\\x52\\x54\\x53\\x53", value="cap:Cinco NetXRay, Network General Sniffer, and"),
@DFAMapEntry(key="\\x52\\x61\\x72\\x21\\x1A\\x07\\x00", value="rar:WinRAR compressed archive file"),
@DFAMapEntry(key="\\x52\\x65\\x74\\x75\\x72\\x6E\\x2D\\x50", value="eml:A commmon file extension for e-mail files. This variant is"),
@DFAMapEntry(key="\\x53\\x43\\x48\\x6C", value="ast:Need for Speed: Underground Audio file"),
@DFAMapEntry(key="\\x53\\x43\\x4D\\x49", value="img:GEM Raster file"),
@DFAMapEntry(key="\\x53\\x48\\x4F\\x57", value="shw:Harvard Graphics DOS Ver. 2/x Presentation file"),
@DFAMapEntry(key="\\x53\\x49\\x45\\x54\\x52\\x4F\\x4E\\x49", value="cpi:Windows international code page"),
@DFAMapEntry(key="\\x53\\x49\\x54\\x21\\x00", value="sit:StuffIt compressed archive"),
@DFAMapEntry(key="\\x53\\x4D\\x41\\x52\\x54\\x44\\x52\\x57", value="sdr:SmartDraw Drawing file"),
@DFAMapEntry(key="\\x53\\x50\\x46\\x49\\x00", value="spf:StorageCraft ShadownProtect backup file"),
@DFAMapEntry(key="\\x53\\x51\\x4C\\x4F\\x43\\x4F\\x4E\\x56", value="cnv:DB2 conversion file"),
@DFAMapEntry(key="\\x53\\x51\\x4C\\x69\\x74\\x65\\x20\\x66", value="db:SQLite database file"),
@DFAMapEntry(key="\\x53\\x5A\\x20\\x88\\xF0\\x27\\x33\\xD1", value=":Byte-order mark for 32-bit Unicode Transformation Format/"),
@DFAMapEntry(key="\\x53\\x5A\\x44\\x44\\x88\\xF0\\x27\\x33", value=":Byte-order mark for 32-bit Unicode Transformation Format/"),
@DFAMapEntry(key="\\x53\\x6D\\x62\\x6C", value="sym:(Unconfirmed file type. Likely type is Harvard Graphics"),
@DFAMapEntry(key="\\x53\\x74\\x75\\x66\\x66\\x49\\x74\\x20", value="sit:StuffIt compressed archive"),
@DFAMapEntry(key="\\x53\\x75\\x70\\x65\\x72\\x43\\x61\\x6C", value="cal:Windows calendar file"),
@DFAMapEntry(key="\\x54\\x68\\x69\\x73\\x20\\x69\\x73\\x20", value="info:Amiga Icon file"),
@DFAMapEntry(key="\\x55\\x43\\x45\\x58", value="uce:Unicode extensions"),
@DFAMapEntry(key="\\x55\\x46\\x41\\xC6\\xD2\\xC1", value="ufa:UFA compressed archive"),
@DFAMapEntry(key="\\x55\\x46\\x4F\\x4F\\x72\\x62\\x69\\x74", value="dat:Access Data FTK evidence file"),
@DFAMapEntry(key="\\x56\\x43\\x50\\x43\\x48\\x30", value="pch:Visual C PreCompiled header file"),
@DFAMapEntry(key="\\x56\\x45\\x52\\x53\\x49\\x4F\\x4E\\x20", value="ctl:Visual Basic User-defined Control file"),
@DFAMapEntry(key="\\x56\\x65\\x72\\x73\\x69\\x6F\\x6E\\x20", value="mif:MapInfo Interchange Format file"),
@DFAMapEntry(key="\\x57\\x4D\\x4D\\x50", value="dat:Access Data FTK evidence file"),
@DFAMapEntry(key="\\x57\\x53\\x32\\x30\\x30\\x30", value="ws2:WordStar for Windows Ver. 2 document"),
@DFAMapEntry(key="\\x57\\x6F\\x72\\x64\\x50\\x72\\x6F", value="lwp:Lotus WordPro document."),
@DFAMapEntry(key="\\x58\\x2D", value="eml:A commmon file extension for e-mail files. This variant is"),
@DFAMapEntry(key="\\x58\\x43\\x50\\x00", value="cap:Cinco NetXRay, Network General Sniffer, and"),
@DFAMapEntry(key="\\x58\\x50\\x43\\x4F\\x4D\\x0A\\x54\\x79", value="xpt:XPCOM type libraries for the XPIDL compiler"),
@DFAMapEntry(key="\\x58\\x54", value="bdr:MS Publisher border"),
@DFAMapEntry(key="\\x5A\\x4F\\x4F\\x20", value="zoo:ZOO compressed archive"),
@DFAMapEntry(key="\\x5B\\x47\\x65\\x6E\\x65\\x72\\x61\\x6C", value="ecf:MS Exchange 2007 extended configuration file"),
@DFAMapEntry(key="\\x5B\\x4D\\x53\\x56\\x43", value="vcw:Microsoft Visual C++ Workbench Information File"),
@DFAMapEntry(key="\\x5B\\x50\\x68\\x6F\\x6E\\x65\\x5D", value="dun:Dial-up networking file"),
@DFAMapEntry(key="\\x5B\\x56\\x45\\x52\\x5D", value="65 72 5b 5d 76:[ver]"),
@DFAMapEntry(key="\\x5B\\x57\\x69\\x6E\\x64\\x6F\\x77\\x73", value="cpx:Microsoft Code Page Translation file"),
@DFAMapEntry(key="\\x5B\\x66\\x6C\\x74\\x73\\x69\\x6D\\x2E", value="cfg:Flight Simulator Aircraft Configuration file"),
@DFAMapEntry(key="\\x5B\\x70\\x6C\\x61\\x79\\x6C\\x69\\x73", value="pls:WinAmp Playlist file"),
@DFAMapEntry(key="\\x5F\\x27\\xA8\\x89", value="jar:Jar archive"),
@DFAMapEntry(key="\\x5F\\x43\\x41\\x53\\x45\\x5F", value="cas cbk:EnCase case file (and backup)"),
@DFAMapEntry(key="\\x60\\xEA", value="arj:Compressed archive file"),
@DFAMapEntry(key="\\x62\\x65\\x67\\x69\\x6E", value=":Byte-order mark for 32-bit Unicode Transformation Format/"),
@DFAMapEntry(key="\\x62\\x70\\x6C\\x69\\x73\\x74", value="plist:Binary property list (plist)"),
@DFAMapEntry(key="\\x63\\x75\\x73\\x68\\x00\\x00\\x00\\x02", value="csh:Photoshop Custom Shape"),
@DFAMapEntry(key="\\x64\\x00\\x00\\x00", value="p10:Intel PROset/Wireless Profile"),
@DFAMapEntry(key="\\x64\\x65\\x78\\x0A\\x30\\x30\\x39\\x00", value="dex:Dalvik executable file (Android)"),
@DFAMapEntry(key="\\x64\\x6E\\x73\\x2E", value="au:Audacity audio file"),
@DFAMapEntry(key="\\x64\\x73\\x77\\x66\\x69\\x6C\\x65", value="dsw:Microsoft Visual Studio workspace file"),
@DFAMapEntry(key="\\x66\\x49\\x00\\x00", value="shd:Windows Server 2003 printer spool file"),
@DFAMapEntry(key="\\x66\\x4C\\x61\\x43\\x00\\x00\\x00\\x22", value="flac:Free Lossless Audio Codec file"),
@DFAMapEntry(key="\\x67\\x49\\x00\\x00", value="shd:Windows Server 2003 printer spool file"),
@DFAMapEntry(key="\\x68\\x49\\x00\\x00", value="shd:Windows Server 2003 printer spool file"),
@DFAMapEntry(key="\\x6C\\x33\\x33\\x6C", value="dbb:Skype user data file (profile and contacts)"),
@DFAMapEntry(key="\\x6F\\x3C", value=":Byte-order mark for 32-bit Unicode Transformation Format/"),
@DFAMapEntry(key="\\x72\\x65\\x67\\x66", value="dat:Access Data FTK evidence file"),
@DFAMapEntry(key="\\x72\\x69\\x66\\x66", value="acd:Sonic Foundry Acid Music File (Sony)"),
@DFAMapEntry(key="\\x72\\x74\\x73\\x70\\x3A\\x2F\\x2F", value="ram:RealMedia metafile"),
@DFAMapEntry(key="\\x73\\x6C\\x68\\x21", value="68 2e 73 6c:slh."),
@DFAMapEntry(key="\\x73\\x6D\\x5F", value="pdb:BGBlitz (professional Backgammon software) position database file"),
@DFAMapEntry(key="\\x73\\x72\\x63\\x64\\x6F\\x63\\x69\\x64", value="cal:Windows calendar file"),
@DFAMapEntry(key="\\x73\\x7A\\x65\\x7A", value="pdb:BGBlitz (professional Backgammon software) position database file"),
@DFAMapEntry(key="\\x76\\x32\\x30\\x30\\x33\\x2E\\x31\\x30", value="flt:Qimage filter"),
@DFAMapEntry(key="\\x78", value="dmg:Mac OS X Disk Copy Disk Image file "),
@DFAMapEntry(key="\\x7A\\x62\\x65\\x78", value="info:Amiga Icon file"),
@DFAMapEntry(key="\\x7B\\x0D\\x0A\\x6F\\x20", value="lgd lgc:Windows application log"),
@DFAMapEntry(key="\\x7B\\x5C\\x70\\x77\\x69", value="pwi:Microsoft Windows Mobile personal note file"),
@DFAMapEntry(key="\\x7B\\x5C\\x72\\x74\\x66\\x31", value="rtf:Rich text format word processing file"),
@DFAMapEntry(key="\\x7E\\x42\\x4B\\x00", value="psp:Corel Paint Shop Pro image file"),
@DFAMapEntry(key="\\x7F\\x45\\x4C\\x46", value=":Byte-order mark for 32-bit Unicode Transformation Format/"),
@DFAMapEntry(key="\\x80", value="obj:Relocatable object code"),
@DFAMapEntry(key="\\x80\\x00\\x00\\x20\\x03\\x12\\x04", value="adx:Dreamcast audio file"),
@DFAMapEntry(key="\\x81\\x32\\x84\\xC1\\x85\\x05\\xD0\\x11", value="wab:Outlook address file"),
@DFAMapEntry(key="\\x81\\xCD\\xAB", value="wpf:WordPerfect text file"),
@DFAMapEntry(key="\\x89\\x50\\x4E\\x47\\x0D\\x0A\\x1A\\x0A", value="png:Portable Network Graphics file"),
@DFAMapEntry(key="\\x8A\\x01\\x09\\x00\\x00\\x00\\xE1\\x08", value="aw:MS Answer Wizard file"),
@DFAMapEntry(key="\\x91\\x33\\x48\\x46", value="hap:Hamarsoft HAP 3.x compressed archive"),
@DFAMapEntry(key="\\x99", value="gpg:GNU Privacy Guard (GPG) public keyring"),
@DFAMapEntry(key="\\x99\\x01", value="pkr:PGP public keyring file"),
@DFAMapEntry(key="\\x9C\\xCB\\xCB\\x8D\\x13\\x75\\xD2\\x11", value="wab:Outlook address file"),
@DFAMapEntry(key="\\xA1\\xB2\\xC3\\xD4", value=":Byte-order mark for 32-bit Unicode Transformation Format/"),
@DFAMapEntry(key="\\xA1\\xB2\\xCD\\x34", value=":Byte-order mark for 32-bit Unicode Transformation Format/"),
@DFAMapEntry(key="\\xA9\\x0D\\x00\\x00\\x00\\x00\\x00\\x00", value="dat:Access Data FTK evidence file"),
@DFAMapEntry(key="\\xAC\\x9E\\xBD\\x8F\\x00\\x00", value="qdf:Quicken data file"),
@DFAMapEntry(key="\\xAC\\xED", value="ser:Java serialization data"),
@DFAMapEntry(key="\\xAC\\xED\\x00\\x05\\x73\\x72\\x00\\x12", value="pdb:BGBlitz (professional Backgammon software) position database file"),
@DFAMapEntry(key="\\xB0\\x4D\\x46\\x43", value="pwl:Windows 98 password file"),
@DFAMapEntry(key="\\xB1\\x68\\xDE\\x3A", value="dcx:Graphics Multipage PCX bitmap file"),
@DFAMapEntry(key="\\xB4\\x6E\\x68\\x44", value="tib:Acronis True Image file"),
@DFAMapEntry(key="\\xB5\\xA2\\xB0\\xB3\\xB3\\xB0\\xA5\\xB5", value="cal:Windows calendar file"),
@DFAMapEntry(key="\\xBE\\x00\\x00\\x00\\xAB\\x00\\x00\\x00", value="wri:MS Write file"),
@DFAMapEntry(key="\\xC3\\xAB\\xCD\\xAB", value="acs:MS Agent Character file"),
@DFAMapEntry(key="\\xC5\\xD0\\xD3\\xC6", value="eps:Adobe encapsulated PostScript file"),
@DFAMapEntry(key="\\xC8\\x00\\x79\\x00", value="lbk:Jeppesen FliteLog file"),
@DFAMapEntry(key="\\xCA\\xFE\\xBA\\xBE", value="class:Java bytecode file"),
@DFAMapEntry(key="\\xCA\\xFE\\xD0\\x0D", value="class:Java bytecode file"),
@DFAMapEntry(key="\\xCD\\x20\\xAA\\xAA\\x02\\x00\\x00\\x00", value=":Byte-order mark for 32-bit Unicode Transformation Format/"),
@DFAMapEntry(key="\\xCF\\x11\\xE0\\xA1\\xB1\\x1A\\xE1\\x00", value="doc:Word 2.0 file"),
@DFAMapEntry(key="\\xCF\\xAD\\x12\\xFE", value="dbx:Outlook Express e-mail folder"),
@DFAMapEntry(key="\\xD0\\xCF\\x11\\xE0\\xA1\\xB1\\x1A\\xE1", value="xla xls doc pps wiz ppt dot:Microsoft Office applications (Word, Powerpoint, Excel, Wizard)"),
@DFAMapEntry(key="\\xD2\\x0A\\x00\\x00", value="ftr:GN Nettest WinPharoah filter file"),
@DFAMapEntry(key="\\xD4\\x2A", value="arl aut:AOL history (ARL) and typed URL (AUT) files"),
@DFAMapEntry(key="\\xD4\\xC3\\xB2\\xA1", value=":Byte-order mark for 32-bit Unicode Transformation Format/"),
@DFAMapEntry(key="\\xD7\\xCD\\xC6\\x9A", value="wmf:Windows graphics metafile"),
@DFAMapEntry(key="\\xDB", value="msc:Microsoft Common Console Document"),
@DFAMapEntry(key="\\xDB\\xA5\\x2D\\x00", value="doc:Word 2.0 file"),
@DFAMapEntry(key="\\xDC\\xDC", value="cpl:Corel color palette file"),
@DFAMapEntry(key="\\xDC\\xFE", value="efx:eFax file format"),
@DFAMapEntry(key="\\xE3\\x10\\x00\\x01\\x00\\x00\\x00\\x00", value="info:Amiga Icon file"),
@DFAMapEntry(key="\\xE3\\x82\\x85\\x96", value="pwl:Windows 98 password file"),
@DFAMapEntry(key="\\xE4\\x52\\x5C\\x7B\\x8C\\xD8\\xA7\\x4D", value="one:Microsoft OneNote note"),
@DFAMapEntry(key="\\xEB", value="com sys:Windows executable file"),
@DFAMapEntry(key="\\xEB\\x3C\\x90\\x2A", value="img:GEM Raster file"),
@DFAMapEntry(key="\\xED\\xAB\\xEE\\xDB", value="rpm:RedHat Package Manager file"),
@DFAMapEntry(key="\\xEF\\xBB\\xBF", value=":Byte-order mark for 32-bit Unicode Transformation Format/"),
@DFAMapEntry(key="\\xFE\\xFF", value=":Byte-order mark for 32-bit Unicode Transformation Format/"),
@DFAMapEntry(key="\\xFF", value="sys:DOS system driver"),
@DFAMapEntry(key="\\xFF\\x00\\x02\\x00\\x04\\x04\\x05\\x54", value="wks:Works for Windows spreadsheet file"),
@DFAMapEntry(key="\\xFF\\x46\\x4F\\x4E\\x54", value="cpi:Windows international code page"),
@DFAMapEntry(key="\\xFF\\x4B\\x45\\x59\\x42\\x20\\x20\\x20", value="sys:DOS system driver"),
@DFAMapEntry(key="\\xFF\\x57\\x50\\x43", value="wpg wpd wpp wp wp5 wp6:WordPerfect text and graphics file"),
@DFAMapEntry(key="\\xFF\\xD8", value="jpg jpeg:Joint Photographic Experts Group, JPEG"),
@DFAMapEntry(key="\\xFF\\xFE", value="reg:Windows Registry file"),
@DFAMapEntry(key="\\xFF\\xFE\\x00\\x00", value=":Byte-order mark for 32-bit Unicode Transformation Format/"),
@DFAMapEntry(key="\\xFF\\xFE\\x23\\x00\\x6C\\x00\\x69\\x00", value="mof:Windows MSinfo file"),
@DFAMapEntry(key="\\xFF\\xFF\\xFF\\xFF", value="sys:DOS system driver")        }
)
public abstract class Magic implements MapParser
{
    static final String ERROR = "Error";
    static final String EOF = "Eof";
    static final String UNKNOWN = ":unknown";
    private static final MimeMap mimeMap = new MimeMap();
    
    public MagicResult guess(byte[] bytes)
    {
        if (bytes != null && bytes.length > 0)
        {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            InputReader reader = new InputReader(bais, 64, "US-ASCII");
            String result = input(reader);
            return getResult(result);
        }
        else
        {
            return getResult(UNKNOWN);
        }
    }
    public MagicResult guess(InputStream is) throws IOException
    {
        try (InputReader reader = new InputReader(is, 64, "US-ASCII"))
        {
            String result = input(reader);
            return getResult(result);
        }
    }
    public MagicResult guess(File file) throws IOException
    {
        try (InputReader reader = new InputReader(file, 64, "US-ASCII"))
        {
            String result = input(reader);
            return getResult(result);
        }
    }
    /**
     * Returns mime type for extension or null if no match
     * @param extension
     * @return 
     */
    public static String getMimeType(String extension)
    {
        return mimeMap.get(extension);
    }
    
    public static Magic getInstance()
    {
        Magic magic = (Magic) GenClassFactory.getGenInstance(Magic.class);
        if (magic == null)
        {
            throw new NullPointerException();
        }
        return magic;
    }

    private MagicResult getResult(String result)
    {
        switch (result)
        {
            case "Error":
            case "Eof":
                return new MagicResult(UNKNOWN);
            default:
                return new MagicResult(result);
        }
    }
    public class MagicResult
    {
        private String[] extensions = new String[]{};
        private String description;

        private MagicResult(String str)
        {
            int idx = str.indexOf(':');
            if (idx > 0)
            {
                extensions = str.substring(0, idx).split("[ ,]+");
            }
            description = str.substring(idx+1);
        }

        public String[] getExtensions()
        {
            return extensions;
        }

        public String getDescription()
        {
            return description;
        }

        @Override
        public String toString()
        {
            return "MagicResult{" + description + '}';
        }
        
    }
}
