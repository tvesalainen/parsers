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
 * @author Timo Vesalainen
 * @see <a href="http://en.wikipedia.org/wiki/Magic_number_(programming)">Magic number</a>
 * @see <a href="http://www.garykessler.net/library/file_sigs.html#acks">FILE SIGNATURES TABLE</a>
 */
public class MagicTask extends Task
{

    private Map<String, String> map = new HashMap<>();
    private File destdir;
    private File srcdir;

    public MagicTask()
    {
        addMagic("AC ED", "SER", "Java Serialized Object");
        addMagic("FF D8", "JPEG", "JPEG");
        //addMagic("00", "PIC", "IBM Storyboard bitmap file");
        addMagic("00 00 00 0C 6A 50 20 20", "JP2", "Various JPEG-2000 image file formats");
        addMagic("00 00 00 14 66 74 79 70", "MP4", "ISO Base Media file (MPEG-4) v1");
        addMagic("00 00 00 14 66 74 79 70", "MOV", "QuickTime movie file");
        addMagic("00 00 00 18 66 74 79 70", "MP4", "MPEG-4 video files");
        addMagic("00 00 00 18 66 74 79 70", "M4V", "MPEG-4 video/QuickTime file");
        addMagic("00 00 00 1C 66 74 79 70", "MP4", "MPEG-4 video file");
        addMagic("00 00 00 20 66 74 79 70", "M4A", "Apple Lossless Audio Codec file");
        addMagic("00 00 01 00", "ICO", "Windows icon file");
        addMagic("00 00 01 BA", "MPG, VOB", "DVD Video Movie File (video/dvd, video/mpeg) or DVD MPEG2");
        addMagic("00 00 02 00", "CUR", "Windows cursor file");
        addMagic("00 00 02 00 06 04 06 00", "WK1", "Lotus 1-2-3 spreadsheet (v1) file");
        addMagic("00 00 1A 00 00 10 04 00", "WK3", "Lotus 1-2-3 spreadsheet (v3) file");
        addMagic("00 00 1A 00 02 10 04 00", "WK4, WK5", "Lotus 1-2-3 spreadsheet (v4, v5) file");
        addMagic("123", "00 00 49 49 58 50 52", "..IIXPR");
        addMagic("00 00 4D 4D 58 50 52", "QXD", "Quark Express document (Intel & Motorola, respectively)");
        addMagic("00 00 FE FF", "n/a", "Byte-order mark for 32-bit Unicode Transformation Format/");
        addMagic("00 01 00 00 4D 53 49 53", "MNY", "Microsoft Money file");
        addMagic("00 01 00 00 53 74 61 6E", "ACCDB", "Microsoft Access 2007 file");
        addMagic("00 01 00 00 53 74 61 6E", "MDB", "Microsoft Access file");
        addMagic("00 01 00 08 00 01 00 01", "IMG", "Ventura Publisher/GEM VDI Image Format Bitmap file");
        addMagic("00 01 01", "FLT", "OpenFlight 3D file");
        addMagic("00 01 42 41", "ABA", "Palm Address Book Archive file");
        addMagic("00 01 42 44", "DBA", "Palm DateBook Archive file");
        addMagic("00 06 15 61 00 00 00 02", "DB", "Netscape Navigator (v4) database file");
        addMagic("00 11 AF", "FLI", "FLIC Animation file");
        addMagic("00 1E 84 90 00 00 00 00", "SNM", "Netscape Communicator (v4) mail folder");
        addMagic("00 5C 41 B1 FF", "ENC", "Mujahideen Secrets 2 encrypted file");
        addMagic("00 BF", "SOL", "Adobe Flash shared object file (e.g., Flash cookies)");
        addMagic("00 FF FF FF FF FF FF FF", "MDF", "Alcohol 120% CD image");
        addMagic("01 00 00 00", "EMF", "Extended (Enhanced) Windows Metafile Format, printer spool file");
        addMagic("01 00 00 00 01", "PIC", "Unknown type picture file");
        addMagic("01 00 09 00 00 03", "WMF", "Windows Metadata file (Win 3.x format)");
        addMagic("01 00 39 30", "FDB, GDB", "Firebird and Interbase database files, respectively. See");
        addMagic("01 0F 00 00", "MDF", "Microsoft SQL Server 2000 database");
        addMagic("01 10", "TR1", "Novell LANalyzer capture file");
        addMagic("01 DA 01 01 00 03", "RGB", "Silicon Graphics RGB Bitmap");
        addMagic("01 FF 02 04 03 02", "DRW", "Micrografx vector graphic file");
        addMagic("02 64 73 73", "DSS", "Digital Speech Standard (Olympus, Grundig, & Phillips)");
        addMagic("03", "DAT", "MapInfo Native Data Format");
        addMagic("DB3", "03 00 00 00", "....");
        addMagic("03 00 00 00 41 50 50 52", "ADX", "Approach index file");
        addMagic("04", "DB4", "dBASE IV data file");
        addMagic("07", "DRW", "A common signature and file extension for many drawing");
        addMagic("07 53 4B 46", "SKF", "SkinCrafter skin file");
        addMagic("07 64 74 32 64 64 74 64", "DTD", "DesignTools 2D Design file");
        addMagic("08", "DB", "dBASE IV or dBFast configuration file");
        addMagic("0C ED", "MP", "Monochrome Picture TIFF bitmap file (unconfirmed)");
        addMagic("0D 44 4F 43", "DOC", "DeskMate Document file");
        addMagic("0E 4E 65 72 6F 49 53 4F", "NRI", "Nero CD Compilation");
        addMagic("0E 57 4B 53", "WKS", "DeskMate Worksheet");
        addMagic("11 00 00 00 53 43 43 41", "PF", "Windows prefetch file");
        addMagic("1A 00 00", "NTF", "Lotus Notes database template");
        addMagic("1A 00 00 04 00 00", "NSF", "Lotus Notes database");
        addMagic("1A 0B", "PAK", "Compressed archive file");
        addMagic("1A 35 01 00", "ETH", "GN Nettest WinPharoah capture file");
        addMagic("1A 45 DF A3 93 42 82 88", "MKV", "Matroska stream file");
        addMagic("1A 52 54 53 20 43 4F 4D", "DAT", "Runtime Software disk image");
        addMagic("1D 7D", "WS", "WordStar Version 5.0/6.0 document");
        addMagic("1F 8B 08", "GZ, TGZ", "GZIP archive file");
        addMagic("1F 9D", "TAR.Z", "Compressed tape archive");
        addMagic("1F A0", "TAR.Z", "Compressed tape archive");
        addMagic("21", "BSB", "MapInfo Sea Chart");
        addMagic("21 12", "AIN", "AIN Compressed Archive");
        addMagic("21 3C 61 72 63 68 3E 0A", "LIB", "Unix archiver (ar) files and Microsoft Program Library");
        addMagic("21 42 44 4E", "PST", "Microsoft ");
        addMagic("23 20", "MSI", "Cerius2 file");
        addMagic("23 20 44 69 73 6B 20 44", "VMDK", "VMware 4 Virtual Disk description file (split disk)");
        addMagic("23 20 4D 69 63 72 6F 73", "DSP", "Microsoft Developer Studio project file");
        addMagic("23 21 41 4D 52", "AMR", "Adaptive Multi-Rate ACELP (Algebraic Code Excited Linear Prediction)");
        addMagic("23 3F 52 41 44 49 41 4E", "HDR", "Radiance High Dynamic Range image file");
        addMagic("24 46 4C 32 40 28 23 29", "SAV", "SPSS Data file");
        addMagic("25 21 50 53 2D 41 64 6F", "EPS", "Adobe encapsulated PostScript file");
        addMagic("25 50 44 46", "PDF, FDF", "Adobe Portable Document Format and Forms Document file");
        addMagic("28 54 68 69 73 20 66 69", "HQX", "Macintosh BinHex 4 Compressed Archive");
        addMagic("2A 2A 2A 20 20 49 6E 73", "LOG", "Symantec Wise Installer log file");
        addMagic("2E 52 45 43", "IVR", "RealPlayer video file (V11 and later)");
        addMagic("2E 52 4D 46", "RM, RMVB", "RealMedia streaming media file");
        addMagic("2E 52 4D 46 00 00 00 12", "RA", "RealAudio file");
        addMagic("2E 72 61 FD 00", "RA", "RealAudio streaming media file");
        addMagic("2E 73 6E 64", "AU", "NeXT/Sun Microsystems &#xB5;-Law audio file");
        addMagic("30", "CAT", "Microsoft security catalog file");
        addMagic("30 00 00 00 4C 66 4C 65", "EVT", "Windows Event Viewer file");
        addMagic("30 26 B2 75 8E 66 CF 11", "ASF, WMA, WMV", "Microsoft Windows Media Audio/Video File");
        addMagic("30 31 4F 52 44 4E 41 4E", "NTF", "National Transfer Format Map File");
        addMagic("31 BE", "32 BE", "2&#xBE;");
        addMagic("34 CD B2 A1", "n/a", "Extended tcpdump (libpcap) capture file (Linux/Unix)");
        addMagic("37 7A BC AF 27 1C", "7Z", "7-Zip compressed file");
        addMagic("37 E4 53 96 C9 DB D6 07", "n/a", "zisofs compression format, recognized by some Linux kernels. See the");
        addMagic("38 42 50 53", "PSD", "Photoshop image file");
        addMagic("3A 56 45 52 53 49 4F 4E", "SLE", "Surfplan kite project file");
        addMagic("3C", "ASX", "Advanced Stream redirector file");
        addMagic("3C 21 64 6F 63 74 79 70", "DCI", "AOL HTML mail file");
        addMagic("3C 3F 78 6D 6C 20 76 65", "MANIFEST", "Windows Visual Stylesheet XML file");
        addMagic("3C 3F 78 6D 6C 20 76 65", "XUL", "XML User Interface Language file");
        addMagic("3C 3F 78 6D 6C 20 76 65", "MSC", "Microsoft Management Console Snap-in Control file");
        addMagic("3C 4D 61 6B 65 72 46 69", "FM, MIF", "Adobe FrameMaker file");
        addMagic("3F 5F 03 00", "GID", "Windows Help index file");
        addMagic("41 43 31 30", "DWG", "Generic AutoCAD drawing");
        addMagic("41 43 76", "SLE", "Steganos Security Suite virtual secure drive");
        addMagic("41 43 53 44", "n/a", "Miscellaneous AOL parameter and information files");
        addMagic("41 45 53", "AES", "AES Crypt");
        addMagic("41 4D 59 4F", "SYW", "Harvard Graphics symbol graphic");
        addMagic("41 4F 4C 20 46 65 65 64", "BAG", "AOL and AIM buddy list file");
        addMagic("41 4F 4C 44 42", "ABY, IDX", "AOL database files: address book (ABY) and user configuration");
        addMagic("41 4F 4C 49 44 58", "IND", "AOL client preferences/settings file (MAIN.IND)");
        addMagic("41 4F 4C 49 4E 44 45 58", "ABI", "AOL address book index file");
        addMagic("41 4F 4C 56 4D 31 30 30", "ORG, PFC", "AOL personal file cabinet (PFC) file");
        addMagic("41 56 47 36 5F 49 6E 74", "DAT", "AVG6 Integrity database file");
        addMagic("41 72 43 01", "ARC", "FreeArc compressed file");
        addMagic("42 41 41 44", "n/a", "NTFS Master File Table (MFT) entry (1,024 bytes)");
        addMagic("42 45 47 49 4E 3A 56 43", "VCF", "vCard file");
        addMagic("42 4C 49 32 32 33 51", "BIN", "Thomson Speedtouch series WLAN router firmware");
        addMagic("42 4D", "BMP, DIB", "Windows (or device-independent) bitmap image");
        addMagic("42 4F 4F 4B 4D 4F 42 49", "PRC", "Palmpilot resource file");
        addMagic("42 5A 68", "BZ2, TAR.BZ2, TBZ2, TB2", "bzip2");
        addMagic("43 23 2B 44 A4 43 4D A5", "RTD", "RagTime document file");
        addMagic("43 42 46 49 4C 45", "CBD", "WordPerfect dictionary file (unconfirmed)");
        addMagic("43 44 30 30 31", "ISO", "ISO-9660 CD Disc Image");
        addMagic("43 49 53 4F", "CSO", "Compressed ISO (CISO) CD image");
        addMagic("43 4D 58 31", "CLB", "Corel Binary metafile");
        addMagic("43 4F 4D 2B", "CLB", "COM+ Catalog file");
        addMagic("43 4F 57 44", "VMDK", "VMware 3 Virtual Disk (portion of a split disk) file");
        addMagic("43 50 54 37 46 49 4C 45", "CPT", "Corel Photopaint file");
        addMagic("43 50 54 46 49 4C 45", "CPT", "Corel Photopaint file");
        addMagic("43 52 45 47", "DAT", "Windows 9x registry hive");
        addMagic("43 52 55 53 48 20 76", "CRU", "Crush compressed archive");
        addMagic("43 57 53", "SWF", "Shockwave Flash file (v5+)");
        addMagic("43 61 74 61 6C 6F 67 20", "CTF", "WhereIsIt Catalog file");
        addMagic("43 6C 69 65 6E 74 20 55", "DAT", "IE History (");
        addMagic("44 41 58 00", "DAX", "DAX Compressed CD image");
        addMagic("44 42 46 48", "DB", "Palm Zire photo database");
        addMagic("44 4D 53 21", "DMS", "Amiga DiskMasher compressed archive");
        addMagic("44 4F 53", "ADF", "Amiga disk file");
        addMagic("44 56 44", "DVR", "DVR-Studio stream file");
        addMagic("45 4C 49 54 45 20 43 6F", "CDR", "Elite Plus Commander saved game file");
        addMagic("45 4E 54 52 59 56 43 44", "VCD", "VideoVCD (GNU VCDImager) file");
        addMagic("45 52 46 53 53 41 56 45", "DAT", "Kroll EasyRecovery Saved Recovery State file");
        addMagic("45 50", "MDI", "Microsoft Document Imaging file");
        addMagic("45 56 46 09 0D 0A FF 00", "E", "Expert Witness Compression Format (EWF) file, including EWF-E01");
        addMagic("45 56 46 32 0D 0A 81", "Ex", "EnCase&reg; Evidence File Format Version 2 (Ex01).");
        addMagic("45 6C 66 46 69 6C 65 00", "EVTX", "Windows Vista event log file");
        addMagic("45 86 00 00 06 00", "QBB", "Intuit QuickBooks backup file");
        addMagic("46 41 58 43 4F 56 45 52", "CPE", "Microsoft Fax Cover Sheet");
        addMagic("46 44 42 48 00", "FDB", "Fiasco database definition file");
        addMagic("46 45 44 46", "SBV", "(Unknown file type)");
        addMagic("46 49 4C 45", "n/a", "NTFS Master File Table (MFT) entry (1,024 bytes)");
        addMagic("46 4C 56 01", "FLV", "Flash video file");
        addMagic("46 4F 52 4D 00", "AIFF", "Audio Interchange File");
        addMagic("46 57 53", "SWF", "Macromedia Shockwave Flash player file");
        addMagic("46 72 6F 6D 20 20 20", "46 72 6F 6D 20 3F 3F 3F", "From ???");
        addMagic("46 72 6F 6D 3A 20", "EML", "A commmon file extension for e-mail files. Signatures shown here");
        addMagic("47 46 31 50 41 54 43 48", "PAT", "Advanced Gravis Ultrasound patch file");
        addMagic("47 49 46 38 37 61", "GIF", "GIF89a");
        addMagic("47 49 46 38 39 61", "GIF", "GIF87a");
        addMagic("47 50 41 54", "PAT", "GIMP (GNU Image Manipulation Program) pattern file");
        addMagic("47 58 32", "GX2", "Show Partner graphics file (not confirmed)");
        addMagic("47 65 6E 65 74 65 63 20", "G64", "Genetec video archive");
        addMagic("48 48 47 42 31", "SH3", "Harvard Graphics presentation file");
        addMagic("49 20 49", "TIF, TIFF", "Tagged Image File Format file");
        addMagic("49 44 33", "MP3", "MPEG-1 Audio Layer 3 (MP3) audio file");
        addMagic("49 44 33 03 00 00 00", "KOZ", "Sprint Music Store audio file (for mobile devices)");
        addMagic("49 49 1A 00 00 00 48 45", "CRW", "Canon digital camera RAW file");
        addMagic("49 49 2A 00", "TIF, TIFF", "Tagged Image File Format file (little");
        addMagic("49 49 2A 00 10 00 00 00", "CR2", "Canon digital camera RAW file");
        addMagic("49 53 63 28", "CAB, HDR", "Install Shield v5.x or 6.x compressed file");
        addMagic("49 54 4F 4C 49 54 4C 53", "LIT", "Microsoft Reader eBook file");
        addMagic("49 54 53 46", "CHI, CHM", "Microsoft Compiled HTML Help File");
        addMagic("49 6E 6E 6F 20 53 65 74", "DAT", "Inno Setup Uninstall Log file");
        addMagic("49 6E 74 65 72 40 63 74", "IPD", "Inter@ctive Pager Backup (BlackBerry) backup file");
        addMagic("4A 41 52 43 53 00", "JAR", "JARCS compressed archive");
        addMagic("4A 47 03 0E", "4A 47 04 0E", "JG..");
        addMagic("4B 44 4D", "VMDK", "VMware 4 Virtual Disk (portion of a split disk) file");
        addMagic("4B 44 4D 56", "VMDK", "VMware 4 Virtual Disk (monolitic disk) file");
        addMagic("4B 47 42 5F 61 72 63 68", "KGB", "KGB archive");
        addMagic("4B 49 00 00", "SHD", "Windows 9x printer spool file");
        addMagic("4B 57 41 4A 88 F0 27 D1", "n/a", "KWAJ file format used by DOS ");
        addMagic("4C 00 00 00 01 14 02 00", "LNK", "Windows shortcut file. See also ");
        addMagic("4C 01", "OBJ", "Microsoft ");
        addMagic("4C 4E 02 00", "GID", "Windows Help index file");
        addMagic("4C 56 46 09 0D 0A FF 00", "E", "Logical File Evidence Format (EWF-L01) as used in later versions of");
        addMagic("4D 2D 57 20 50 6F 63 6B", "PDB", "Merriam-Webster Pocket Dictionary file");
        addMagic("4D 41 52 31 00", "MAR", "Mozilla archive");
        addMagic("4D 41 52 43", "MAR", "Microsoft/MSN MARC archive");
        addMagic("4D 41 72 30 00", "MAR", "MAr compressed archive");
        addMagic("4D 44 4D 50 93 A7", "HDMP", "Windows heap dump file");
        addMagic("4D 49 4C 45 53", "MLS", "Milestones v1.0 project management and scheduling software");
        addMagic("4D 4C 53 57", "MLS", "Skype localization data file");
        addMagic("4D 4D 00 2A", "TIF, TIFF", "Tagged Image File Format file (big");
        addMagic("4D 4D 00 2B", "TIF, TIFF", "BigTIFF files; Tagged Image File Format files >4 GB");
        addMagic("4D 4D 4D 44 00 00", "MMF", "Yamaha Corp. Synthetic music Mobile Application Format (SMAF)");
        addMagic("4D 52 56 4E", "NVRAM", "VMware BIOS (non-volatile RAM) state file.");
        addMagic("4D 53 43 46", "CAB", "Microsoft cabinet file");
        addMagic("4D 53 46 54 02 00 01 00", "TLB", "OLE, SPSS, or Visual C++ type library file");
        addMagic("4D 53 5F 56 4F 49 43 45", "CDR, DVF", "Sony Compressed Voice File");
        addMagic("4D 54 68 64", "MID, MIDI", "Musical Instrument Digital Interface (MIDI) sound file");
        addMagic("4D 56", "DSN", "CD Stomper Pro label file");
        addMagic("4D 56 32 31 34", "MLS", "Milestones v2.1b project management and scheduling software");
        addMagic("4D 56 32 43", "MLS", "Milestones v2.1a project management and scheduling software");
        addMagic("4D 5A", "COM, DLL, DRV, EXE, PIF, QTS, QTX, SYS", "Windows/DOS executable file");
        addMagic("4D 5A 90 00 03 00 00 00", "API", "Acrobat plug-in");
        addMagic("4D 5A 90 00 03 00 00 00", "ZAP", "ZoneAlam data file");
        addMagic("4D 69 63 72 6F 73 6F 66", "PDB", "Microsoft C++ debugging symbols file");
        addMagic("4D 69 63 72 6F 73 6F 66", "SLN", "Visual Studio .NET Solution file");
        addMagic("4D 73 52 63 66", "GDB", "VMapSource GPS Waypoint Database");
        addMagic("4E 41 56 54 52 41 46 46", "DAT", "TomTom traffic data file");
        addMagic("4E 42 2A 00", "JNT, JTP", "MS Windows journal file");
        addMagic("4E 45 53 4D 1A 01", "NSF", "NES Sound file");
        addMagic("4E 49 54 46 30", "NTF", "National Imagery Transmission Format (NITF) file");
        addMagic("4E 61 6D 65 3A 20", "COD", "Agent newsreader character map file");
        addMagic("4F 50 4C 44 61 74 61 62", "DBF", "Psion Series 3 Database file");
        addMagic("4F 67 67 53 00 02 00 00", "OGA, OGG, OGV, OGX", "Ogg Vorbis Codec compressed Multimedia file");
        addMagic("4F 7B", "DW4", "Visio/DisplayWrite 4 text file (unconfirmed)");
        addMagic("50 00 00 00 20 00 00 00", "IDX", "Quicken QuickFinder Information File");
        addMagic("50 35 0A", "PGM", "Portable Graymap Graphic");
        addMagic("50 41 43 4B", "PAK", "Quake archive file");
        addMagic("50 41 47 45 44 55 36 34", "DMP", "Windows 64-bit memory dump");
        addMagic("50 41 47 45 44 55 4D 50", "DMP", "Windows memory dump");
        addMagic("50 41 58", "PAX", "PAX password protected bitmap");
        addMagic("50 45 53 54", "DAT", "PestPatrol data/scan strings");
        addMagic("50 47 50 64 4D 41 49 4E", "PGD", "PGP disk image");
        addMagic("50 49 43 54 00 08", "IMG", "ADEX Corp. ChromaGraph Graphics Card Bitmap Graphic file");
        addMagic("50 4B 03 04", "ZIP", "PKZIP archive file (");
        addMagic("50 4B 03 04 14 00 01 00", "ZIP", "ZLock Pro encrypted ZIP");
        addMagic("50 4B 03 04 14 00 06 00", "DOCX, PPTX, XLSX", "Microsoft Office Open XML Format (OOXML) Document");
        addMagic("50 4B 03 04 14 00 08 00", "JAR", "Java archive");
        addMagic("50 4B 05 06", "50 4B 07 08", "PK..");
        addMagic("50 4D 43 43", "GRP", "Windows Program Manager group file");
        addMagic("50 4E 43 49 55 4E 44 4F", "DAT", "Norton Disk Doctor undo file");
        addMagic("51 46 49 FB", "IMG", "QEMU Qcow Disk Image");
        addMagic("51 57 20 56 65 72 2E 20", "ABD, QSD", "Quicken data file");
        addMagic("52 41 5A 41 54 44 42 31", "DAT", "Shareaza (Windows P2P client) thumbnail");
        addMagic("52 45 47 45 44 49 54", "REG, SUD", "Windows NT Registry and Registry Undo files");
        addMagic("52 45 56 4E 55 4D 3A 2C", "ADF", "Antenna data file");
        addMagic("52 49 46 46", "ANI", "Windows animated cursor");
        addMagic("CDA", "52 49 46 46 xx xx xx xx", "RIFF....");
        addMagic("52 54 53 53", "CAP", "Windows NT Netmon capture file");
        addMagic("52 61 72 21 1A 07 00", "RAR", "WinRAR compressed archive file");
        addMagic("52 65 74 75 72 6E 2D 50", "EML", "A commmon file extension for e-mail files.");
        addMagic("53 43 48 6C", "AST", "Need for Speed: Underground Audio file");
        addMagic("53 43 4D 49", "IMG", "Img Software Set Bitmap");
        addMagic("53 48 4F 57", "SHW", "Harvard Graphics DOS Ver. 2/x Presentation file");
        addMagic("53 49 45 54 52 4F 4E 49", "CPI", "Sietronics CPI XRD document");
        addMagic("53 49 54 21 00", "SIT", "StuffIt compressed archive");
        addMagic("53 4D 41 52 54 44 52 57", "SDR", "SmartDraw Drawing file");
        addMagic("53 50 46 49 00", "SPF", "StorageCraft ShadownProtect backup file");
        addMagic("53 51 4C 4F 43 4F 4E 56", "CNV", "DB2 conversion file");
        addMagic("53 51 4C 69 74 65 20 66", "DB", "SQLite database file");
        addMagic("53 5A 20 88 F0 27 33 D1", "n/a", "QBASIC SZDD file header variant. (See the SZDD or KWAJ format entries");
        addMagic("53 5A 44 44 88 F0 27 33", "n/a", "SZDD file format used by DOS ");
        addMagic("53 6D 62 6C", "SYM", "(Unconfirmed file type. Likely type is Harvard Graphics");
        addMagic("53 74 75 66 66 49 74 20", "SIT", "StuffIt compressed archive");
        addMagic("53 75 70 65 72 43 61 6C", "CAL", "SuperCalc worksheet");
        addMagic("54 68 69 73 20 69 73 20", "INFO", "UNIX GNU Info Reader File");
        addMagic("55 43 45 58", "UCE", "Unicode extensions");
        addMagic("55 46 41 C6 D2 C1", "UFA", "UFA compressed archive");
        addMagic("55 46 4F 4F 72 62 69 74", "DAT", "UFO Capture v2 map file");
        addMagic("56 43 50 43 48 30", "PCH", "Visual C PreCompiled header file");
        addMagic("56 45 52 53 49 4F 4E 20", "CTL", "Visual Basic User-defined Control file");
        addMagic("56 65 72 73 69 6F 6E 20", "MIF", "MapInfo Interchange Format file");
        addMagic("57 4D 4D 50", "DAT", "Walkman MP3 container file");
        addMagic("57 53 32 30 30 30", "WS2", "WordStar for Windows Ver. 2 document");
        addMagic("57 6F 72 64 50 72 6F", "LWP", "Lotus WordPro document.");
        addMagic("58 2D", "EML", "A commmon file extension for e-mail files. This variant is");
        addMagic("58 43 50 00", "CAP", "Cinco NetXRay, Network General Sniffer, and");
        addMagic("58 50 43 4F 4D 0A 54 79", "XPT", "XPCOM type libraries for the XPIDL compiler");
        addMagic("58 54", "BDR", "MS Publisher border");
        addMagic("5A 4F 4F 20", "ZOO", "ZOO compressed archive");
        addMagic("5B 47 65 6E 65 72 61 6C", "ECF", "MS Exchange 2007 extended configuration file");
        addMagic("5B 4D 53 56 43", "VCW", "Microsoft Visual C++ Workbench Information File");
        addMagic("5B 50 68 6F 6E 65 5D", "DUN", "Dial-up networking file");
        addMagic("5B 56 45 52 5D", "5B 76 65 72 5D", "[ver]");
        addMagic("5B 57 69 6E 64 6F 77 73", "CPX", "Microsoft Code Page Translation file");
        addMagic("5B 66 6C 74 73 69 6D 2E", "CFG", "Flight Simulator Aircraft Configuration file");
        addMagic("5B 70 6C 61 79 6C 69 73", "PLS", "WinAmp Playlist file");
        addMagic("5F 27 A8 89", "JAR", "Jar archive");
        addMagic("5F 43 41 53 45 5F", "CAS, CBK", "EnCase case file (and backup)");
        addMagic("60 EA", "ARJ", "Compressed archive file");
        addMagic("62 65 67 69 6E", "n/a", "UUencoded files start with a string:");
        addMagic("62 70 6C 69 73 74", "plist", "Binary property list (plist)");
        addMagic("63 75 73 68 00 00 00 02", "CSH", "Photoshop Custom Shape");
        addMagic("64 00 00 00", "P10", "Intel PROset/Wireless Profile");
        addMagic("64 65 78 0A 30 30 39 00", "dex", "Dalvik executable file (Android)");
        addMagic("64 73 77 66 69 6C 65", "DSW", "Microsoft Visual Studio workspace file");
        addMagic("64 6E 73 2E", "AU", "Audacity audio file");
        addMagic("66 49 00 00", "SHD", "Windows NT printer spool file");
        addMagic("66 4C 61 43 00 00 00 22", "FLAC", "Free Lossless Audio Codec file");
        addMagic("67 49 00 00", "SHD", "Windows 2000/XP printer spool file");
        addMagic("68 49 00 00", "SHD", "Windows Server 2003 printer spool file");
        addMagic("6C 33 33 6C", "DBB", "Skype user data file (profile and contacts)");
        addMagic("6F 3C", "n/a", "Short Message Service (SMS), or text, message stored on a");
        addMagic("72 65 67 66", "DAT", "Windows NT registry hive file");
        addMagic("72 69 66 66", "ACD", "Sonic Foundry Acid Music File (Sony)");
        addMagic("72 74 73 70 3A 2F 2F", "RAM", "RealMedia metafile");
        addMagic("73 6C 68 21", "73 6C 68 2E", "slh.");
        addMagic("73 6D 5F", "PDB", "PalmOS SuperMemo file");
        addMagic("73 72 63 64 6F 63 69 64", "CAL", "CALS raster bitmap file");
        addMagic("73 7A 65 7A", "PDB", "PowerBASIC Debugger Symbols file");
        addMagic("76 32 30 30 33 2E 31 30", "FLT", "Qimage filter");
        addMagic("78", "DMG", "Mac OS X Disk Copy Disk Image file ");
        addMagic("7A 62 65 78", "INFO", "ZoomBrowser Image Index file (ZbThumbnal.info)");
        addMagic("7B 0D 0A 6F 20", "LGC, LGD", "Windows application log");
        addMagic("7B 5C 70 77 69", "PWI", "Microsoft Windows Mobile personal note file");
        addMagic("7B 5C 72 74 66 31", "RTF", "Rich text format word processing file");
        addMagic("7E 42 4B 00", "PSP", "Corel Paint Shop Pro image file");
        addMagic("7F 45 4C 46", "n/a", "Executable and Linking Format executable file (Linux/Unix)");
        addMagic("80", "OBJ", "Relocatable object code");
        addMagic("80 00 00 20 03 12 04", "ADX", "Dreamcast audio file");
        addMagic("81 32 84 C1 85 05 D0 11", "WAB", "Outlook Express address book (Win95)");
        addMagic("81 CD AB", "WPF", "WordPerfect text file");
        addMagic("89 50 4E 47 0D 0A 1A 0A", "PNG", "Portable Network Graphics file");
        addMagic("8A 01 09 00 00 00 E1 08", "AW", "MS Answer Wizard file");
        addMagic("91 33 48 46", "HAP", "Hamarsoft HAP 3.x compressed archive");
        addMagic("95 00", "95 01", "&#x95;.");
        addMagic("99", "GPG", "GNU Privacy Guard (GPG) public keyring");
        addMagic("99 01", "PKR", "PGP public keyring file");
        addMagic("9C CB CB 8D 13 75 D2 11", "WAB", "Outlook address file");
        addMagic("A1 B2 C3 D4", "n/a", "tcpdump (libpcap) capture file (Linux/Unix)");
        addMagic("A1 B2 CD 34", "n/a", "Extended tcpdump (libpcap) capture file (Linux/Unix)");
        addMagic("A9 0D 00 00 00 00 00 00", "DAT", "Access Data FTK evidence file");
        addMagic("AC 9E BD 8F 00 00", "QDF", "Quicken data file");
        addMagic("AC ED", "n/a", "Java serialization data (see ");
        addMagic("AC ED 00 05 73 72 00 12", "PDB", "BGBlitz (professional Backgammon software) position database file");
        addMagic("B0 4D 46 43", "PWL", "Windows 95 password file");
        addMagic("B1 68 DE 3A", "DCX", "Graphics Multipage PCX bitmap file");
        addMagic("B4 6E 68 44", "TIB", "Acronis True Image file");
        addMagic("B5 A2 B0 B3 B3 B0 A5 B5", "CAL", "Windows calendar file");
        addMagic("BE 00 00 00 AB 00 00 00", "WRI", "MS Write file");
        addMagic("C3 AB CD AB", "ACS", "MS Agent Character file");
        addMagic("C5 D0 D3 C6", "EPS", "Adobe encapsulated PostScript file");
        addMagic("C8 00 79 00", "LBK", "Jeppesen FliteLog file");
        addMagic("CA FE BA BE", "CLASS", "Java bytecode file");
        addMagic("CA FE D0 0D", "CLASS", "Java bytecode file");
        addMagic("CD 20 AA AA 02 00 00 00", "n/a", "Norton Anti-Virus quarantined virus file");
        addMagic("CF 11 E0 A1 B1 1A E1 00", "DOC", "Perfect Office document");
        addMagic("CF AD 12 FE", "DBX", "Outlook Express e-mail folder");
        addMagic("D0 CF 11 E0 A1 B1 1A E1", "DOC, DOT, PPS, PPT, XLA, XLS, WIZ", "Microsoft Office applications (Word, Powerpoint, Excel, Wizard)");
        addMagic("DB", "MSC", "Microsoft Common Console Document");
        addMagic("D2 0A 00 00", "FTR", "GN Nettest WinPharoah filter file");
        addMagic("D4 2A", "ARL, AUT", "AOL history (ARL) and typed URL (AUT) files");
        addMagic("D4 C3 B2 A1", "n/a", "WinDump (winpcap) capture file (Windows)");
        addMagic("D7 CD C6 9A", "WMF", "Windows graphics metafile");
        addMagic("DB A5 2D 00", "DOC", "Word 2.0 file");
        addMagic("DC DC", "CPL", "Corel color palette file");
        addMagic("DC FE", "EFX", "eFax file format");
        addMagic("E3 10 00 01 00 00 00 00", "INFO", "Amiga Icon file");
        addMagic("E3 82 85 96", "PWL", "Windows 98 password file");
        addMagic("E4 52 5C 7B 8C D8 A7 4D", "ONE", "Microsoft OneNote note");
        addMagic("E8", "E9", "&#xE9;");
        addMagic("EB", "COM, SYS", "Windows executable file");
        addMagic("EB 3C 90 2A", "IMG", "GEM Raster file");
        addMagic("ED AB EE DB", "RPM", "RedHat Package Manager file");
        addMagic("EF BB BF", "n/a", "Byte-order mark for 8-bit Unicode Transformation Format");
        addMagic("DB", "FE EF", "&#xFE;&#xEF;");
        addMagic("FE FF", "n/a", "Byte-order mark for 16-bit Unicode Transformation Format/");
        addMagic("FF", "SYS", "Windows executable (SYS) file");
        addMagic("FF 00 02 00 04 04 05 54", "WKS", "Works for Windows spreadsheet file");
        addMagic("FF 46 4F 4E 54", "CPI", "Windows international code page");
        addMagic("FF 4B 45 59 42 20 20 20", "SYS", "Keyboard driver file");
        addMagic("FF 57 50 43", "WP, WPD, WPG, WPP, WP5, WP6", "WordPerfect text and graphics file");
        addMagic("FF FE", "REG", "Windows Registry file");
        addMagic("FF FE 00 00", "n/a", "Byte-order mark for 32-bit Unicode Transformation Format/");
        addMagic("FF FE 23 00 6C 00 69 00", "MOF", "Windows MSinfo file");
        addMagic("FF FF FF FF", "SYS", "DOS system driver");
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
        map.put(sb.toString(), ext + ":" + description);
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
            log("implementing "+mw);
            subClass.implement(mw);
            log("saving "+thisClass+" to "+destdir);
            subClass.save(destdir);
        }
        catch (NoSuchMethodException | SecurityException | IOException ex)
        {
            throw new BuildException("magic fails", ex, getLocation());
        }
    }

}
