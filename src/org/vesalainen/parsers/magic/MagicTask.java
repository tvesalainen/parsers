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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.vesalainen.bcc.SubClass;
import org.vesalainen.bcc.type.ClassWrapper;
import org.vesalainen.bcc.type.MethodWrapper;
import org.vesalainen.grammar.state.DFA;
import org.vesalainen.grammar.state.DFAState;
import org.vesalainen.grammar.state.NFA;
import org.vesalainen.grammar.state.NFAState;
import org.vesalainen.grammar.state.Scope;
import org.vesalainen.parser.util.InputReader;
import org.vesalainen.regex.MatchCompiler;
import org.vesalainen.regex.RegexParser;

/**
 * Ant task for creating of MagicImpl. MagicImpl can be used in detecting file
 * type from file content.
 *
 * @author Timo Vesalainen
 * @see <a href="http://en.wikipedia.org/wiki/Magic_number_(programming)">Magic
 * number</a>
 * @see <a href="http://www.garykessler.net/library/file_sigs.html#acks">FILE
 * SIGNATURES TABLE</a>
 */
public class MagicTask extends Task
{

    private Map<String, String> map = new HashMap<>();
    private File destdir;
    private File srcdir;

    public MagicTask()
    {
        addMagic("00 00 00 0C 6A 50 20 20", " jp2", "Various JPEG-2000 image file formats");
        addMagic("00 00 00 14 66 74 79 70", " mov mp4", "QuickTime movie file");
        addMagic("00 00 00 18 66 74 79 70", " m4v mp4", "MPEG-4 video/QuickTime file");
        addMagic("00 00 00 1C 66 74 79 70", " mp4", "MPEG-4 video file");
        addMagic("00 00 00 20 66 74 79 70", " m4a", "Apple Lossless Audio Codec file");
        addMagic("00 00 01 00", " ico", "Windows icon file");
        addMagic("00 00 01 BA", " mpg vob", "DVD Video Movie File (video/dvd, video/mpeg) or DVD MPEG2");
        addMagic("00 00 02 00", " cur", "Windows cursor file");
        addMagic("00 00 02 00 06 04 06 00", " wk1", "Lotus 1-2-3 spreadsheet (v1) file");
        addMagic("00 00 1A 00 00 10 04 00", " wk3", "Lotus 1-2-3 spreadsheet (v3) file");
        addMagic("00 00 1A 00 02 10 04 00", " wk4 wk5", "Lotus 1-2-3 spreadsheet (v4, v5) file");
        addMagic("00 00 4D 4D 58 50 52", " qxd", "Quark Express document (Intel & Motorola, respectively)");
        addMagic("00 00 FE FF", " ", "Byte-order mark for 32-bit Unicode Transformation Format/");
        addMagic("00 01 00 00 4D 53 49 53", " mny", "Microsoft Money file");
        addMagic("00 01 00 00 53 74 61 6E", " mdb accdb", "Microsoft Access file");
        addMagic("00 01 00 08 00 01 00 01", " img", "GEM Raster file");
        addMagic("00 01 01", " flt", "Qimage filter");
        addMagic("00 01 42 41", " aba", "Palm Address Book Archive file");
        addMagic("00 01 42 44", " dba", "Palm DateBook Archive file");
        addMagic("00 06 15 61 00 00 00 02", " db", "SQLite database file");
        addMagic("00 11 AF", " fli", "FLIC Animation file");
        addMagic("00 1E 84 90 00 00 00 00", " snm", "Netscape Communicator (v4) mail folder");
        addMagic("00 5C 41 B1 FF", " enc", "Mujahideen Secrets 2 encrypted file");
        addMagic("00 BF", " sol", "Adobe Flash shared object file (e.g., Flash cookies)");
        addMagic("00 FF FF FF FF FF FF FF", " mdf", "Microsoft SQL Server 2000 database");
        addMagic("01 00 00 00", " emf", "Extended (Enhanced) Windows Metafile Format, printer spool file");
        addMagic("01 00 00 00 01", " pic", "Unknown type picture file");
        addMagic("01 00 09 00 00 03", " wmf", "Windows graphics metafile");
        addMagic("01 00 39 30", " gdb fdb", "VMapSource GPS Waypoint Database");
        addMagic("01 0F 00 00", " mdf", "Microsoft SQL Server 2000 database");
        addMagic("01 10", " tr1", "Novell LANalyzer capture file");
        addMagic("01 DA 01 01 00 03", " rgb", "Silicon Graphics RGB Bitmap");
        addMagic("01 FF 02 04 03 02", " drw", "A common signature and file extension for many drawing");
        addMagic("02 64 73 73", " dss", "Digital Speech Standard (Olympus, Grundig, & Phillips)");
        addMagic("03", " dat", "Access Data FTK evidence file");
        addMagic("03 00 00 00 41 50 50 52", " adx", "Dreamcast audio file");
        addMagic("04", " db4", "dBASE IV data file");
        addMagic("07", " drw", "A common signature and file extension for many drawing");
        addMagic("07 53 4B 46", " skf", "SkinCrafter skin file");
        addMagic("07 64 74 32 64 64 74 64", " dtd", "DesignTools 2D Design file");
        addMagic("08", " db", "SQLite database file");
        addMagic("0C ED", " mp", "Monochrome Picture TIFF bitmap file (unconfirmed)");
        addMagic("0D 44 4F 43", " doc", "Word 2.0 file");
        addMagic("0E 4E 65 72 6F 49 53 4F", " nri", "Nero CD Compilation");
        addMagic("0E 57 4B 53", " wks", "Works for Windows spreadsheet file");
        addMagic("11 00 00 00 53 43 43 41", " pf", "Windows prefetch file");
        addMagic("1A 00 00", " ntf", "National Imagery Transmission Format (NITF) file");
        addMagic("1A 00 00 04 00 00", " nsf", "NES Sound file");
        addMagic("1A 0B", " pak", "Quake archive file");
        addMagic("1A 35 01 00", " eth", "GN Nettest WinPharoah capture file");
        addMagic("1A 45 DF A3 93 42 82 88", " mkv", "Matroska stream file");
        addMagic("1A 52 54 53 20 43 4F 4D", " dat", "Access Data FTK evidence file");
        addMagic("1D 7D", " ws", "WordStar Version 5.0/6.0 document");
        addMagic("1F 8B 08", " gz tgz", "GZIP archive file");
        addMagic("1F 9D", " tar.z", "Compressed tape archive");
        addMagic("1F A0", " tar.z", "Compressed tape archive");
        addMagic("21", " bsb", "MapInfo Sea Chart");
        addMagic("21 12", " ain", "AIN Compressed Archive");
        addMagic("21 3C 61 72 63 68 3E 0A", " lib", "Unix archiver (ar) files and Microsoft Program Library");
        addMagic("21 42 44 4E", " pst", "Microsoft ");
        addMagic("23 20", " msi", "Cerius2 file");
        addMagic("23 20 44 69 73 6B 20 44", " vmdk", "VMware 4 Virtual Disk (monolitic disk) file");
        addMagic("23 20 4D 69 63 72 6F 73", " dsp", "Microsoft Developer Studio project file");
        addMagic("23 21 41 4D 52", " amr", "Adaptive Multi-Rate ACELP (Algebraic Code Excited Linear Prediction)");
        addMagic("23 3F 52 41 44 49 41 4E", " hdr", "Install Shield v5.x or 6.x compressed file");
        addMagic("24 46 4C 32 40 28 23 29", " sav", "SPSS Data file");
        addMagic("25 21 50 53 2D 41 64 6F", " eps", "Adobe encapsulated PostScript file");
        addMagic("25 50 44 46", " fdf pdf", "Adobe Portable Document Format and Forms Document file");
        addMagic("28 54 68 69 73 20 66 69", " hqx", "Macintosh BinHex 4 Compressed Archive");
        addMagic("2A 2A 2A 20 20 49 6E 73", " log", "Symantec Wise Installer log file");
        addMagic("2E 52 45 43", " ivr", "RealPlayer video file (V11 and later)");
        addMagic("2E 52 4D 46", " rm rmvb", "RealMedia streaming media file");
        addMagic("2E 52 4D 46 00 00 00 12", " ra", "RealAudio streaming media file");
        addMagic("2E 72 61 FD 00", " ra", "RealAudio streaming media file");
        addMagic("2E 73 6E 64", " au", "Audacity audio file");
        addMagic("30", " cat", "Microsoft security catalog file");
        addMagic("30 00 00 00 4C 66 4C 65", " evt", "Windows Event Viewer file");
        addMagic("30 26 B2 75 8E 66 CF 11", " asf wmv wma", "Microsoft Windows Media Audio/Video File");
        addMagic("30 31 4F 52 44 4E 41 4E", " ntf", "National Imagery Transmission Format (NITF) file");
        addMagic("31 BE", " 32 be", "2&#xBE;");
        addMagic("34 CD B2 A1", " ", "Byte-order mark for 32-bit Unicode Transformation Format/");
        addMagic("37 7A BC AF 27 1C", " 7z", "7-Zip compressed file");
        addMagic("37 E4 53 96 C9 DB D6 07", " ", "Byte-order mark for 32-bit Unicode Transformation Format/");
        addMagic("38 42 50 53", " psd", "Photoshop image file");
        addMagic("3A 56 45 52 53 49 4F 4E", " sle", "Steganos Security Suite virtual secure drive");
        addMagic("3C", " asx", "Advanced Stream redirector file");
        addMagic("3C 21 64 6F 63 74 79 70", " dci", "AOL HTML mail file");
        addMagic("3C 3F 78 6D 6C 20 76 65", " xul manifest msc", "XML User Interface Language file");
        addMagic("3C 4D 61 6B 65 72 46 69", " fm mif", "Adobe FrameMaker file");
        addMagic("3F 5F 03 00", " gid", "Windows Help index file");
        addMagic("41 43 31 30", " dwg", "Generic AutoCAD drawing");
        addMagic("41 43 53 44", " ", "Byte-order mark for 32-bit Unicode Transformation Format/");
        addMagic("41 43 76", " sle", "Steganos Security Suite virtual secure drive");
        addMagic("41 45 53", " aes", "AES Crypt");
        addMagic("41 4D 59 4F", " syw", "Harvard Graphics symbol graphic");
        addMagic("41 4F 4C 20 46 65 65 64", " bag", "AOL and AIM buddy list file");
        addMagic("41 4F 4C 44 42", " idx aby", "Quicken QuickFinder Information File");
        addMagic("41 4F 4C 49 44 58", " ind", "AOL client preferences/settings file (MAIN.IND)");
        addMagic("41 4F 4C 49 4E 44 45 58", " abi", "AOL address book index file");
        addMagic("41 4F 4C 56 4D 31 30 30", " pfc org", "AOL personal file cabinet (PFC) file");
        addMagic("41 56 47 36 5F 49 6E 74", " dat", "Access Data FTK evidence file");
        addMagic("41 72 43 01", " arc", "FreeArc compressed file");
        addMagic("42 41 41 44", " ", "Byte-order mark for 32-bit Unicode Transformation Format/");
        addMagic("42 45 47 49 4E 3A 56 43", " vcf", "vCard file");
        addMagic("42 4C 49 32 32 33 51", " bin", "Thomson Speedtouch series WLAN router firmware");
        addMagic("42 4D", " bmp dib", "Windows (or device-independent) bitmap image");
        addMagic("42 4F 4F 4B 4D 4F 42 49", " prc", "Palmpilot resource file");
        addMagic("42 5A 68", " tar.bz2 tb2 tbz2 bz2", "bzip2");
        addMagic("43 23 2B 44 A4 43 4D A5", " rtd", "RagTime document file");
        addMagic("43 42 46 49 4C 45", " cbd", "WordPerfect dictionary file (unconfirmed)");
        addMagic("43 44 30 30 31", " iso", "ISO-9660 CD Disc Image");
        addMagic("43 49 53 4F", " cso", "Compressed ISO (CISO) CD image");
        addMagic("43 4D 58 31", " clb", "COM+ Catalog file");
        addMagic("43 4F 4D 2B", " clb", "COM+ Catalog file");
        addMagic("43 4F 57 44", " vmdk", "VMware 4 Virtual Disk (monolitic disk) file");
        addMagic("43 50 54 37 46 49 4C 45", " cpt", "Corel Photopaint file");
        addMagic("43 50 54 46 49 4C 45", " cpt", "Corel Photopaint file");
        addMagic("43 52 45 47", " dat", "Access Data FTK evidence file");
        addMagic("43 52 55 53 48 20 76", " cru", "Crush compressed archive");
        addMagic("43 57 53", " swf", "Macromedia Shockwave Flash player file");
        addMagic("43 61 74 61 6C 6F 67 20", " ctf", "WhereIsIt Catalog file");
        addMagic("43 6C 69 65 6E 74 20 55", " dat", "Access Data FTK evidence file");
        addMagic("44 41 58 00", " dax", "DAX Compressed CD image");
        addMagic("44 42 46 48", " db", "SQLite database file");
        addMagic("44 4D 53 21", " dms", "Amiga DiskMasher compressed archive");
        addMagic("44 4F 53", " adf", "Antenna data file");
        addMagic("44 56 44", " dvr", "DVR-Studio stream file");
        addMagic("45 4C 49 54 45 20 43 6F", " cdr", "Sony Compressed Voice File");
        addMagic("45 4E 54 52 59 56 43 44", " vcd", "VideoVCD (GNU VCDImager) file");
        addMagic("45 50", " mdi", "Microsoft Document Imaging file");
        addMagic("45 52 46 53 53 41 56 45", " dat", "Access Data FTK evidence file");
        addMagic("45 56 46 09 0D 0A FF 00", " e", "Logical File Evidence Format (EWF-L01) as used in later versions of");
        addMagic("45 56 46 32 0D 0A 81", " ex", "EnCase&reg; Evidence File Format Version 2 (Ex01).");
        addMagic("45 6C 66 46 69 6C 65 00", " evtx", "Windows Vista event log file");
        addMagic("45 86 00 00 06 00", " qbb", "Intuit QuickBooks backup file");
        addMagic("46 41 58 43 4F 56 45 52", " cpe", "Microsoft Fax Cover Sheet");
        addMagic("46 44 42 48 00", " fdb", "Fiasco database definition file");
        addMagic("46 45 44 46", " sbv", "(Unknown file type)");
        addMagic("46 49 4C 45", " ", "Byte-order mark for 32-bit Unicode Transformation Format/");
        addMagic("46 4C 56 01", " flv", "Flash video file");
        addMagic("46 4F 52 4D 00", " aiff", "Audio Interchange File");
        addMagic("46 57 53", " swf", "Macromedia Shockwave Flash player file");
        addMagic("46 72 6F 6D 20 20 20", " 20 6f 3f 46 72 6d", "From ???");
        addMagic("46 72 6F 6D 3A 20", " eml", "A commmon file extension for e-mail files. This variant is");
        addMagic("47 46 31 50 41 54 43 48", " pat", "GIMP (GNU Image Manipulation Program) pattern file");
        addMagic("47 49 46 38 37 61", " gif", "GIF87a");
        addMagic("47 49 46 38 39 61", " gif", "GIF87a");
        addMagic("47 50 41 54", " pat", "GIMP (GNU Image Manipulation Program) pattern file");
        addMagic("47 58 32", " gx2", "Show Partner graphics file (not confirmed)");
        addMagic("47 65 6E 65 74 65 63 20", " g64", "Genetec video archive");
        addMagic("48 48 47 42 31", " sh3", "Harvard Graphics presentation file");
        addMagic("49 20 49", " tif tiff", "BigTIFF files; Tagged Image File Format files >4 GB");
        addMagic("49 44 33", " mp3", "MPEG-1 Audio Layer 3 (MP3) audio file");
        addMagic("49 44 33 03 00 00 00", " koz", "Sprint Music Store audio file (for mobile devices)");
        addMagic("49 49 1A 00 00 00 48 45", " crw", "Canon digital camera RAW file");
        addMagic("49 49 2A 00", " tif tiff", "BigTIFF files; Tagged Image File Format files >4 GB");
        addMagic("49 49 2A 00 10 00 00 00", " cr2", "Canon digital camera RAW file");
        addMagic("49 53 63 28", " cab hdr", "Microsoft cabinet file");
        addMagic("49 54 4F 4C 49 54 4C 53", " lit", "Microsoft Reader eBook file");
        addMagic("49 54 53 46", " chi chm", "Microsoft Compiled HTML Help File");
        addMagic("49 6E 6E 6F 20 53 65 74", " dat", "Access Data FTK evidence file");
        addMagic("49 6E 74 65 72 40 63 74", " ipd", "Inter@ctive Pager Backup (BlackBerry) backup file");
        addMagic("4A 41 52 43 53 00", " jar", "Jar archive");
        addMagic("4A 47 03 0E", " 47 04 0e 4a", "JG..");
        addMagic("4B 44 4D", " vmdk", "VMware 4 Virtual Disk (monolitic disk) file");
        addMagic("4B 44 4D 56", " vmdk", "VMware 4 Virtual Disk (monolitic disk) file");
        addMagic("4B 47 42 5F 61 72 63 68", " kgb", "KGB archive");
        addMagic("4B 49 00 00", " shd", "Windows Server 2003 printer spool file");
        addMagic("4B 57 41 4A 88 F0 27 D1", " ", "Byte-order mark for 32-bit Unicode Transformation Format/");
        addMagic("4C 00 00 00 01 14 02 00", " lnk", "Windows shortcut file. See also ");
        addMagic("4C 01", " obj", "Relocatable object code");
        addMagic("4C 4E 02 00", " gid", "Windows Help index file");
        addMagic("4C 56 46 09 0D 0A FF 00", " e", "Logical File Evidence Format (EWF-L01) as used in later versions of");
        addMagic("4D 2D 57 20 50 6F 63 6B", " pdb", "BGBlitz (professional Backgammon software) position database file");
        addMagic("4D 41 52 31 00", " mar", "MAr compressed archive");
        addMagic("4D 41 52 43", " mar", "MAr compressed archive");
        addMagic("4D 41 72 30 00", " mar", "MAr compressed archive");
        addMagic("4D 44 4D 50 93 A7", " hdmp", "Windows heap dump file");
        addMagic("4D 49 4C 45 53", " mls", "Milestones v2.1a project management and scheduling software");
        addMagic("4D 4C 53 57", " mls", "Milestones v2.1a project management and scheduling software");
        addMagic("4D 4D 00 2A", " tif tiff", "BigTIFF files; Tagged Image File Format files >4 GB");
        addMagic("4D 4D 00 2B", " tif tiff", "BigTIFF files; Tagged Image File Format files >4 GB");
        addMagic("4D 4D 4D 44 00 00", " mmf", "Yamaha Corp. Synthetic music Mobile Application Format (SMAF)");
        addMagic("4D 52 56 4E", " nvram", "VMware BIOS (non-volatile RAM) state file.");
        addMagic("4D 53 43 46", " cab", "Microsoft cabinet file");
        addMagic("4D 53 46 54 02 00 01 00", " tlb", "OLE, SPSS, or Visual C++ type library file");
        addMagic("4D 53 5F 56 4F 49 43 45", " dvf cdr", "Sony Compressed Voice File");
        addMagic("4D 54 68 64", " midi mid", "Musical Instrument Digital Interface (MIDI) sound file");
        addMagic("4D 56", " dsn", "CD Stomper Pro label file");
        addMagic("4D 56 32 31 34", " mls", "Milestones v2.1a project management and scheduling software");
        addMagic("4D 56 32 43", " mls", "Milestones v2.1a project management and scheduling software");
        addMagic("4D 5A", " drv com sys qtx dll qts pif exe", "Windows/DOS executable file");
        addMagic("4D 5A 90 00 03 00 00 00", " api", "Acrobat plug-in");
        addMagic("4D 69 63 72 6F 73 6F 66", " sln pdb", "Visual Studio .NET Solution file");
        addMagic("4D 73 52 63 66", " gdb", "VMapSource GPS Waypoint Database");
        addMagic("4E 41 56 54 52 41 46 46", " dat", "Access Data FTK evidence file");
        addMagic("4E 42 2A 00", " jtp jnt", "MS Windows journal file");
        addMagic("4E 45 53 4D 1A 01", " nsf", "NES Sound file");
        addMagic("4E 49 54 46 30", " ntf", "National Imagery Transmission Format (NITF) file");
        addMagic("4E 61 6D 65 3A 20", " cod", "Agent newsreader character map file");
        addMagic("4F 50 4C 44 61 74 61 62", " dbf", "Psion Series 3 Database file");
        addMagic("4F 67 67 53 00 02 00 00", " oga ogv ogg ogx", "Ogg Vorbis Codec compressed Multimedia file");
        addMagic("4F 7B", " dw4", "Visio/DisplayWrite 4 text file (unconfirmed)");
        addMagic("50 00 00 00 20 00 00 00", " idx", "Quicken QuickFinder Information File");
        addMagic("50 35 0A", " pgm", "Portable Graymap Graphic");
        addMagic("50 41 43 4B", " pak", "Quake archive file");
        addMagic("50 41 47 45 44 55 36 34", " dmp", "Windows memory dump");
        addMagic("50 41 47 45 44 55 4D 50", " dmp", "Windows memory dump");
        addMagic("50 41 58", " pax", "PAX password protected bitmap");
        addMagic("50 45 53 54", " dat", "Access Data FTK evidence file");
        addMagic("50 47 50 64 4D 41 49 4E", " pgd", "PGP disk image");
        addMagic("50 49 43 54 00 08", " img", "GEM Raster file");
        addMagic("50 4B 03 04", " zip", "ZLock Pro encrypted ZIP");
        addMagic("50 4B 03 04 14 00 01 00", " zip", "ZLock Pro encrypted ZIP");
        addMagic("50 4B 03 04 14 00 06 00", " xlsx docx pptx", "Microsoft Office Open XML Format (OOXML) Document");
        addMagic("50 4B 03 04 14 00 08 00", " jar", "Jar archive");
        addMagic("50 4B 05 06", " 08 4b 07 50", "PK..");
        addMagic("50 4D 43 43", " grp", "Windows Program Manager group file");
        addMagic("50 4E 43 49 55 4E 44 4F", " dat", "Access Data FTK evidence file");
        addMagic("51 46 49 FB", " img", "GEM Raster file");
        addMagic("51 57 20 56 65 72 2E 20", " abd qsd", "Quicken data file");
        addMagic("52 41 5A 41 54 44 42 31", " dat", "Access Data FTK evidence file");
        addMagic("52 45 47 45 44 49 54", " sud reg", "Windows NT Registry and Registry Undo files");
        addMagic("52 45 56 4E 55 4D 3A 2C", " adf", "Antenna data file");
        addMagic("52 49 46 46", " ani", "Windows animated cursor");
        addMagic("52 54 53 53", " cap", "Cinco NetXRay, Network General Sniffer, and");
        addMagic("52 61 72 21 1A 07 00", " rar", "WinRAR compressed archive file");
        addMagic("52 65 74 75 72 6E 2D 50", " eml", "A commmon file extension for e-mail files. This variant is");
        addMagic("53 43 48 6C", " ast", "Need for Speed: Underground Audio file");
        addMagic("53 43 4D 49", " img", "GEM Raster file");
        addMagic("53 48 4F 57", " shw", "Harvard Graphics DOS Ver. 2/x Presentation file");
        addMagic("53 49 45 54 52 4F 4E 49", " cpi", "Windows international code page");
        addMagic("53 49 54 21 00", " sit", "StuffIt compressed archive");
        addMagic("53 4D 41 52 54 44 52 57", " sdr", "SmartDraw Drawing file");
        addMagic("53 50 46 49 00", " spf", "StorageCraft ShadownProtect backup file");
        addMagic("53 51 4C 4F 43 4F 4E 56", " cnv", "DB2 conversion file");
        addMagic("53 51 4C 69 74 65 20 66", " db", "SQLite database file");
        addMagic("53 5A 20 88 F0 27 33 D1", " ", "Byte-order mark for 32-bit Unicode Transformation Format/");
        addMagic("53 5A 44 44 88 F0 27 33", " ", "Byte-order mark for 32-bit Unicode Transformation Format/");
        addMagic("53 6D 62 6C", " sym", "(Unconfirmed file type. Likely type is Harvard Graphics");
        addMagic("53 74 75 66 66 49 74 20", " sit", "StuffIt compressed archive");
        addMagic("53 75 70 65 72 43 61 6C", " cal", "Windows calendar file");
        addMagic("54 68 69 73 20 69 73 20", " info", "Amiga Icon file");
        addMagic("55 43 45 58", " uce", "Unicode extensions");
        addMagic("55 46 41 C6 D2 C1", " ufa", "UFA compressed archive");
        addMagic("55 46 4F 4F 72 62 69 74", " dat", "Access Data FTK evidence file");
        addMagic("56 43 50 43 48 30", " pch", "Visual C PreCompiled header file");
        addMagic("56 45 52 53 49 4F 4E 20", " ctl", "Visual Basic User-defined Control file");
        addMagic("56 65 72 73 69 6F 6E 20", " mif", "MapInfo Interchange Format file");
        addMagic("57 4D 4D 50", " dat", "Access Data FTK evidence file");
        addMagic("57 53 32 30 30 30", " ws2", "WordStar for Windows Ver. 2 document");
        addMagic("57 6F 72 64 50 72 6F", " lwp", "Lotus WordPro document.");
        addMagic("58 2D", " eml", "A commmon file extension for e-mail files. This variant is");
        addMagic("58 43 50 00", " cap", "Cinco NetXRay, Network General Sniffer, and");
        addMagic("58 50 43 4F 4D 0A 54 79", " xpt", "XPCOM type libraries for the XPIDL compiler");
        addMagic("58 54", " bdr", "MS Publisher border");
        addMagic("5A 4F 4F 20", " zoo", "ZOO compressed archive");
        addMagic("5B 47 65 6E 65 72 61 6C", " ecf", "MS Exchange 2007 extended configuration file");
        addMagic("5B 4D 53 56 43", " vcw", "Microsoft Visual C++ Workbench Information File");
        addMagic("5B 50 68 6F 6E 65 5D", " dun", "Dial-up networking file");
        addMagic("5B 56 45 52 5D", " 65 72 5b 5d 76", "[ver]");
        addMagic("5B 57 69 6E 64 6F 77 73", " cpx", "Microsoft Code Page Translation file");
        addMagic("5B 66 6C 74 73 69 6D 2E", " cfg", "Flight Simulator Aircraft Configuration file");
        addMagic("5B 70 6C 61 79 6C 69 73", " pls", "WinAmp Playlist file");
        addMagic("5F 27 A8 89", " jar", "Jar archive");
        addMagic("5F 43 41 53 45 5F", " cas cbk", "EnCase case file (and backup)");
        addMagic("60 EA", " arj", "Compressed archive file");
        addMagic("62 65 67 69 6E", " ", "Byte-order mark for 32-bit Unicode Transformation Format/");
        addMagic("62 70 6C 69 73 74", " plist", "Binary property list (plist)");
        addMagic("63 75 73 68 00 00 00 02", " csh", "Photoshop Custom Shape");
        addMagic("64 00 00 00", " p10", "Intel PROset/Wireless Profile");
        addMagic("64 65 78 0A 30 30 39 00", " dex", "Dalvik executable file (Android)");
        addMagic("64 6E 73 2E", " au", "Audacity audio file");
        addMagic("64 73 77 66 69 6C 65", " dsw", "Microsoft Visual Studio workspace file");
        addMagic("66 49 00 00", " shd", "Windows Server 2003 printer spool file");
        addMagic("66 4C 61 43 00 00 00 22", " flac", "Free Lossless Audio Codec file");
        addMagic("67 49 00 00", " shd", "Windows Server 2003 printer spool file");
        addMagic("68 49 00 00", " shd", "Windows Server 2003 printer spool file");
        addMagic("6C 33 33 6C", " dbb", "Skype user data file (profile and contacts)");
        addMagic("6F 3C", " ", "Byte-order mark for 32-bit Unicode Transformation Format/");
        addMagic("72 65 67 66", " dat", "Access Data FTK evidence file");
        addMagic("72 69 66 66", " acd", "Sonic Foundry Acid Music File (Sony)");
        addMagic("72 74 73 70 3A 2F 2F", " ram", "RealMedia metafile");
        addMagic("73 6C 68 21", " 68 2e 73 6c", "slh.");
        addMagic("73 6D 5F", " pdb", "BGBlitz (professional Backgammon software) position database file");
        addMagic("73 72 63 64 6F 63 69 64", " cal", "Windows calendar file");
        addMagic("73 7A 65 7A", " pdb", "BGBlitz (professional Backgammon software) position database file");
        addMagic("76 32 30 30 33 2E 31 30", " flt", "Qimage filter");
        addMagic("78", " dmg", "Mac OS X Disk Copy Disk Image file ");
        addMagic("7A 62 65 78", " info", "Amiga Icon file");
        addMagic("7B 0D 0A 6F 20", " lgd lgc", "Windows application log");
        addMagic("7B 5C 70 77 69", " pwi", "Microsoft Windows Mobile personal note file");
        addMagic("7B 5C 72 74 66 31", " rtf", "Rich text format word processing file");
        addMagic("7E 42 4B 00", " psp", "Corel Paint Shop Pro image file");
        addMagic("7F 45 4C 46", " ", "Byte-order mark for 32-bit Unicode Transformation Format/");
        addMagic("80", " obj", "Relocatable object code");
        addMagic("80 00 00 20 03 12 04", " adx", "Dreamcast audio file");
        addMagic("81 32 84 C1 85 05 D0 11", " wab", "Outlook address file");
        addMagic("81 CD AB", " wpf", "WordPerfect text file");
        addMagic("89 50 4E 47 0D 0A 1A 0A", " png", "Portable Network Graphics file");
        addMagic("8A 01 09 00 00 00 E1 08", " aw", "MS Answer Wizard file");
        addMagic("91 33 48 46", " hap", "Hamarsoft HAP 3.x compressed archive");
        addMagic("99", " gpg", "GNU Privacy Guard (GPG) public keyring");
        addMagic("99 01", " pkr", "PGP public keyring file");
        addMagic("9C CB CB 8D 13 75 D2 11", " wab", "Outlook address file");
        addMagic("A1 B2 C3 D4", " ", "Byte-order mark for 32-bit Unicode Transformation Format/");
        addMagic("A1 B2 CD 34", " ", "Byte-order mark for 32-bit Unicode Transformation Format/");
        addMagic("A9 0D 00 00 00 00 00 00", " dat", "Access Data FTK evidence file");
        addMagic("AC 9E BD 8F 00 00", " qdf", "Quicken data file");
        addMagic("AC ED", " ser", "Java serialization data");
        addMagic("AC ED 00 05 73 72 00 12", " pdb", "BGBlitz (professional Backgammon software) position database file");
        addMagic("B0 4D 46 43", " pwl", "Windows 98 password file");
        addMagic("B1 68 DE 3A", " dcx", "Graphics Multipage PCX bitmap file");
        addMagic("B4 6E 68 44", " tib", "Acronis True Image file");
        addMagic("B5 A2 B0 B3 B3 B0 A5 B5", " cal", "Windows calendar file");
        addMagic("BE 00 00 00 AB 00 00 00", " wri", "MS Write file");
        addMagic("C3 AB CD AB", " acs", "MS Agent Character file");
        addMagic("C5 D0 D3 C6", " eps", "Adobe encapsulated PostScript file");
        addMagic("C8 00 79 00", " lbk", "Jeppesen FliteLog file");
        addMagic("CA FE BA BE", " class", "Java bytecode file");
        addMagic("CA FE D0 0D", " class", "Java bytecode file");
        addMagic("CD 20 AA AA 02 00 00 00", " ", "Byte-order mark for 32-bit Unicode Transformation Format/");
        addMagic("CF 11 E0 A1 B1 1A E1 00", " doc", "Word 2.0 file");
        addMagic("CF AD 12 FE", " dbx", "Outlook Express e-mail folder");
        addMagic("D0 CF 11 E0 A1 B1 1A E1", " xla xls doc pps wiz ppt dot", "Microsoft Office applications (Word, Powerpoint, Excel, Wizard)");
        addMagic("D2 0A 00 00", " ftr", "GN Nettest WinPharoah filter file");
        addMagic("D4 2A", " arl aut", "AOL history (ARL) and typed URL (AUT) files");
        addMagic("D4 C3 B2 A1", " ", "Byte-order mark for 32-bit Unicode Transformation Format/");
        addMagic("D7 CD C6 9A", " wmf", "Windows graphics metafile");
        addMagic("DB", " msc", "Microsoft Common Console Document");
        addMagic("DB A5 2D 00", " doc", "Word 2.0 file");
        addMagic("DC DC", " cpl", "Corel color palette file");
        addMagic("DC FE", " efx", "eFax file format");
        addMagic("E3 10 00 01 00 00 00 00", " info", "Amiga Icon file");
        addMagic("E3 82 85 96", " pwl", "Windows 98 password file");
        addMagic("E4 52 5C 7B 8C D8 A7 4D", " one", "Microsoft OneNote note");
        addMagic("EB", " com sys", "Windows executable file");
        addMagic("EB 3C 90 2A", " img", "GEM Raster file");
        addMagic("ED AB EE DB", " rpm", "RedHat Package Manager file");
        addMagic("EF BB BF", " ", "Byte-order mark for 32-bit Unicode Transformation Format/");
        addMagic("FE FF", " ", "Byte-order mark for 32-bit Unicode Transformation Format/");
        addMagic("FF", " sys", "DOS system driver");
        addMagic("FF 00 02 00 04 04 05 54", " wks", "Works for Windows spreadsheet file");
        addMagic("FF 46 4F 4E 54", " cpi", "Windows international code page");
        addMagic("FF 4B 45 59 42 20 20 20", " sys", "DOS system driver");
        addMagic("FF 57 50 43", " wpg wpd wpp wp wp5 wp6", "WordPerfect text and graphics file");
        addMagic("FF D8", " jpeg", "Joint Photographic Experts Group, JPEG");
        addMagic("FF FE", " reg", "Windows Registry file");
        addMagic("FF FE 00 00", " ", "Byte-order mark for 32-bit Unicode Transformation Format/");
        addMagic("FF FE 23 00 6C 00 69 00", " mof", "Windows MSinfo file");
        addMagic("FF FF FF FF", " sys", "DOS system driver");
    }

    public void setDestdir(File destdir)
    {
        this.destdir = destdir;
        log(destdir.getPath());
        log("using " + destdir.getPath() + " for generated byte code");
    }

    public void setSrcdir(File srcdir)
    {
        this.srcdir = srcdir;
        log("using " + srcdir.getPath() + " for generated source");
    }

    private void addMagic(String magic, String ext, String description)
    {
        StringBuilder sb = new StringBuilder();
        for (String s : magic.split(" "))
        {
            sb.append("\\x");
            sb.append(s);
        }
        String old = map.put(sb.toString(), ext + ":" + description);
        if (old != null)
        {
            System.err.println(old + " duplicate with " + description);
        }
    }

    @Override
    public void execute() throws BuildException
    {
        try
        {
            RegexParser<String> regexParser = (RegexParser<String>) RegexParser.newInstance();
            Scope<NFAState<String>> nfaScope = new Scope<>("magic");
            Scope<DFAState<String>> dfaScope = new Scope<>("magic");
            NFA<String> nfa = null;
            for (Entry<String, String> entry : map.entrySet())
            {
                String expression = entry.getKey();
                String token = entry.getValue();
                if (nfa == null)
                {
                    nfa = regexParser.createNFA(nfaScope, expression, token);
                }
                else
                {
                    NFA<String> nfa2 = regexParser.createNFA(nfaScope, expression, token);
                    nfa = new NFA(nfaScope, nfa, nfa2);
                }
            }
            Class<?> superClass = Magic.class;
            ClassWrapper thisClass = ClassWrapper.wrap(superClass.getName() + "Impl", superClass);
            SubClass subClass = new SubClass(thisClass);
            subClass.codeDefaultConstructor();
            log("constructing magic DFA");
            DFA dfa = nfa.constructDFA(dfaScope);
            MethodWrapper mw = MethodWrapper.wrap(Magic.class.getDeclaredMethod("input", InputReader.class));
            MatchCompiler<String> ic = new MatchCompiler<>(dfa, Magic.ERROR, Magic.EOF);
            mw.setImplementor(ic);
            log("implementing " + mw);
            subClass.implement(mw);
            log("saving " + thisClass + " to " + destdir);
            subClass.save(destdir);
        }
        catch (NoSuchMethodException | SecurityException | IOException ex)
        {
            throw new BuildException("magic fails", ex, getLocation());
        }
    }
}
