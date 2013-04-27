/*
 * Copyright (C) 2011 Timo Vesalainen
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
package org.vesalainen.parsers.date;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import org.vesalainen.parser.GenClassFactory;
import org.vesalainen.parser.annotation.GenClassname;
import org.vesalainen.parser.annotation.GrammarDef;
import org.vesalainen.parser.annotation.ParseMethod;
import org.vesalainen.parser.annotation.ParserContext;
import org.vesalainen.parser.annotation.Rule;
import org.vesalainen.parser.annotation.Rules;
import org.vesalainen.parser.annotation.Terminal;
import org.vesalainen.parser.annotation.Terminals;

/**
 * LocaleDateParser parses dates in locale-sensitive way.
 * <p>
 * Note that it is possible that in some locales there will
 * be conflicting rules in grammar. If that happens, try to remove some of the
 * patterns in LocaleDateGrammar. 
 * @author Timo Vesalainen
 * @see <a href="doc-files/LocaleDateParser-dateTime.html#BNF">BNF Syntax for locale datetime</a>
 * @see <a href="doc-files/LocaleDateParser-date.html#BNF">BNF Syntax for locale date</a>
 * @see <a href="doc-files/LocaleDateParser-time.html#BNF">BNF Syntax for locale time</a>
 */
@GenClassname("org.vesalainen.parsers.date.LocaleDateParserImpl")
@GrammarDef //(grammarClass=LocaleDateGrammar.class)
@Terminals({
@Terminal(left="'\\, '", expression="\\, ")
,@Terminal(left="'heinä|heinäk|heinäku|heinäkuu'", expression="heinä|heinäk|heinäku|heinäkuu")
,@Terminal(left="'/'", expression="/")
,@Terminal(left="'ti|tii|tiis|tiist|tiista|tiistai'", expression="ti|tii|tiis|tiist|tiista|tiistai")
,@Terminal(left="'loka|lokak|lokaku|lokakuu'", expression="loka|lokak|lokaku|lokakuu")
,@Terminal(left="'pe|per|perj|perja|perjan|perjant|perjanta|perjantai'", expression="pe|per|perj|perja|perjan|perjant|perjanta|perjantai")
,@Terminal(left="'huhti|huhtik|huhtiku|huhtikuu'", expression="huhti|huhtik|huhtiku|huhtikuu")
,@Terminal(left="'ip\\.'", expression="ip\\.")
,@Terminal(left="'joulu|jouluk|jouluku|joulukuu'", expression="joulu|jouluk|jouluku|joulukuu")
,@Terminal(left="' '", expression=" ")
,@Terminal(left="'ap\\.'", expression="ap\\.")
,@Terminal(left="'elo|elok|eloku|elokuu'", expression="elo|elok|eloku|elokuu")
,@Terminal(left="'su|sun|sunn|sunnu|sunnun|sunnunt|sunnunta|sunnuntai'", expression="su|sun|sunn|sunnu|sunnun|sunnunt|sunnunta|sunnuntai")
,@Terminal(left="'tammi|tammik|tammiku|tammikuu'", expression="tammi|tammik|tammiku|tammikuu")
,@Terminal(left="'kesä|kesäk|kesäku|kesäkuu'", expression="kesä|kesäk|kesäku|kesäkuu")
,@Terminal(left="'touko|toukok|toukoku|toukokuu'", expression="touko|toukok|toukoku|toukokuu")
,@Terminal(left="'ke|kes|kesk|keski|keskiv|keskivi|keskivii|keskiviik|keskiviikk|keskiviikko'", expression="ke|kes|kesk|keski|keskiv|keskivi|keskivii|keskiviik|keskiviikk|keskiviikko")
,@Terminal(left="'marras|marrask|marrasku|marraskuu'", expression="marras|marrask|marrasku|marraskuu")
,@Terminal(left="'la|lau|laua|lauan|lauant|lauanta|lauantai'", expression="la|lau|laua|lauan|lauant|lauanta|lauantai")
,@Terminal(left="'helmi|helmik|helmiku|helmikuu'", expression="helmi|helmik|helmiku|helmikuu")
,@Terminal(left="':'", expression=":")
,@Terminal(left="'to|tor|tors|torst|torsta|torstai'", expression="to|tor|tors|torst|torsta|torstai")
,@Terminal(left="'syys|syysk|syysku|syyskuu'", expression="syys|syysk|syysku|syyskuu")
,@Terminal(left="'maalis|maalisk|maalisku|maaliskuu'", expression="maalis|maalisk|maalisku|maaliskuu")
,@Terminal(left="'ma|maa|maan|maana|maanan|maanant|maananta|maanantai'", expression="ma|maa|maan|maana|maanan|maanant|maananta|maanantai")
})
@Rules({
@Rule(left="MMM", reducer="org.vesalainen.parsers.date.DateReducers month4(java.util.Calendar)", value={"'huhti|huhtik|huhtiku|huhtikuu'"})
,@Rule(left="MMM", reducer="org.vesalainen.parsers.date.DateReducers month12(java.util.Calendar)", value={"'joulu|jouluk|jouluku|joulukuu'"})
,@Rule(left="MMM", reducer="org.vesalainen.parsers.date.DateReducers month7(java.util.Calendar)", value={"'heinä|heinäk|heinäku|heinäkuu'"})
,@Rule(left="MMM", reducer="org.vesalainen.parsers.date.DateReducers month11(java.util.Calendar)", value={"'marras|marrask|marrasku|marraskuu'"})
,@Rule(left="MMM", reducer="org.vesalainen.parsers.date.DateReducers month2(java.util.Calendar)", value={"'helmi|helmik|helmiku|helmikuu'"})
,@Rule(left="MMM", reducer="org.vesalainen.parsers.date.DateReducers month1(java.util.Calendar)", value={"'tammi|tammik|tammiku|tammikuu'"})
,@Rule(left="MMM", reducer="org.vesalainen.parsers.date.DateReducers month6(java.util.Calendar)", value={"'kesä|kesäk|kesäku|kesäkuu'"})
,@Rule(left="MMM", reducer="org.vesalainen.parsers.date.DateReducers month9(java.util.Calendar)", value={"'syys|syysk|syysku|syyskuu'"})
,@Rule(left="MMM", reducer="org.vesalainen.parsers.date.DateReducers month10(java.util.Calendar)", value={"'loka|lokak|lokaku|lokakuu'"})
,@Rule(left="MMM", reducer="org.vesalainen.parsers.date.DateReducers month3(java.util.Calendar)", value={"'maalis|maalisk|maalisku|maaliskuu'"})
,@Rule(left="MMM", reducer="org.vesalainen.parsers.date.DateReducers month8(java.util.Calendar)", value={"'elo|elok|eloku|elokuu'"})
,@Rule(left="MMM", reducer="org.vesalainen.parsers.date.DateReducers month5(java.util.Calendar)", value={"'touko|toukok|toukoku|toukokuu'"})
,@Rule(left="mm", value={"minute"})
,@Rule(left="d", value={"dayInMonth"})
,@Rule(left="dateTime", value={"MM", "'/'", "d", "'/'", "yy", "' '", "h", "':'", "mm", "' '", "a"})
,@Rule(left="dateTime", value={"EEEE", "'\\, '", "MMM", "' '", "d", "'\\, '", "yyyy", "' '", "h", "':'", "mm", "':'", "ss", "' '", "a", "' '", "z"})
,@Rule(left="dateTime", value={"MMM", "' '", "d", "'\\, '", "yyyy", "' '", "h", "':'", "mm", "':'", "ss", "' '", "a"})
,@Rule(left="dateTime", value={"MMM", "' '", "d", "'\\, '", "yyyy", "' '", "h", "':'", "mm", "':'", "ss", "' '", "a", "' '", "z"})
,@Rule(left="EEEE", reducer="org.vesalainen.parsers.date.DateReducers weekday3(java.util.Calendar)", value={"'ti|tii|tiis|tiist|tiista|tiistai'"})
,@Rule(left="EEEE", reducer="org.vesalainen.parsers.date.DateReducers weekday7(java.util.Calendar)", value={"'la|lau|laua|lauan|lauant|lauanta|lauantai'"})
,@Rule(left="EEEE", reducer="org.vesalainen.parsers.date.DateReducers weekday5(java.util.Calendar)", value={"'to|tor|tors|torst|torsta|torstai'"})
,@Rule(left="EEEE", reducer="org.vesalainen.parsers.date.DateReducers weekday2(java.util.Calendar)", value={"'ma|maa|maan|maana|maanan|maanant|maananta|maanantai'"})
,@Rule(left="EEEE", reducer="org.vesalainen.parsers.date.DateReducers weekday1(java.util.Calendar)", value={"'su|sun|sunn|sunnu|sunnun|sunnunt|sunnunta|sunnuntai'"})
,@Rule(left="EEEE", reducer="org.vesalainen.parsers.date.DateReducers weekday6(java.util.Calendar)", value={"'pe|per|perj|perja|perjan|perjant|perjanta|perjantai'"})
,@Rule(left="EEEE", reducer="org.vesalainen.parsers.date.DateReducers weekday4(java.util.Calendar)", value={"'ke|kes|kesk|keski|keskiv|keskivi|keskivii|keskiviik|keskiviikk|keskiviikko'"})
,@Rule(left="a", reducer="org.vesalainen.parsers.date.DateReducers am(java.util.Calendar)", value={"'ap\\.'"})
,@Rule(left="a", reducer="org.vesalainen.parsers.date.DateReducers pm(java.util.Calendar)", value={"'ip\\.'"})
,@Rule(left="MM", value={"month"})
,@Rule(left="yy", value={"year2"})
,@Rule(left="date", value={"MMM", "' '", "d", "'\\, '", "yyyy"})
,@Rule(left="date", value={"time"})
,@Rule(left="date", value={"dateTime"})
,@Rule(left="date", value={"MM", "'/'", "d", "'/'", "yy"})
,@Rule(left="date", value={"EEEE", "'\\, '", "MMM", "' '", "d", "'\\, '", "yyyy"})
,@Rule(left="h", value={"hour12"})
,@Rule(left="time", value={"h", "':'", "mm", "':'", "ss", "' '", "a"})
,@Rule(left="time", value={"h", "':'", "mm", "':'", "ss", "' '", "a", "' '", "z"})
,@Rule(left="time", value={"h", "':'", "mm", "' '", "a"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset54000000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset20700000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset50400000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset_28800000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset24300000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset_43200000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset19800000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset_12600000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset35100000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset_7200000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset_14400000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset39600000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset10800000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset36000000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset32400000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset0"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset3600000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset18000000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset_32400000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset43200000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset16200000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset27000000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset14400000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset_3600000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset_36000000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset7200000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset_21600000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset23400000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset_39600000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset_10800000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset12600000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset25200000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset_25200000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset45000000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset11224000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset31500000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset34200000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset41400000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset49500000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset37800000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset_16200000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset21600000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset_30600000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset45900000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset_34200000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset_9000000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset28800000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset46800000"})
,@Rule(left="generalTZ", reducer="org.vesalainen.parsers.date.DateReducers generalTZ(int,java.util.Calendar)", value={"tzOffset_18000000"})
,@Rule(left="yyyy", value={"year4"})
,@Rule(left="ss", value={"second"})
,@Rule(left="z", value={"generalTZ"})
})
public abstract class LocaleDateParser extends DateReducers
{
    public Date parseDate(String text) throws IOException
    {
        return parseCalendarDate(text).getTime();
    }

    public Calendar parseCalendarDate(String text) throws IOException
    {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.ZONE_OFFSET, 0);
        cal.set(Calendar.DST_OFFSET, 0);
        parseDate(text, cal);
        return cal;
    }

    public Date parseDateTime(String text) throws IOException
    {
        return parseCalendarDate(text).getTime();
    }

    public Calendar parseCalendarDateTime(String text) throws IOException
    {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.ZONE_OFFSET, 0);
        cal.set(Calendar.DST_OFFSET, 0);
        parseDateTime(text, cal);
        return cal;
    }

    public Date parseTime(String text) throws IOException
    {
        return parseCalendarDate(text).getTime();
    }

    public Calendar parseCalendarTime(String text) throws IOException
    {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.ZONE_OFFSET, 0);
        cal.set(Calendar.DST_OFFSET, 0);
        parseTime(text, cal);
        return cal;
    }
    /**
     * 
     * @param text
     * @param calendar
     * @throws IOException 
     * @see <a href="doc-files/LocaleDateParser-dateTime.html#BNF">BNF Syntax for locale datetime</a>
     */
    @ParseMethod(start = "dateTime", wideIndex = true)
    protected abstract void parseDateTime(String text, @ParserContext Calendar calendar) throws IOException;
    /**
     * 
     * @param text
     * @param calendar
     * @throws IOException 
     * @see <a href="doc-files/LocaleDateParser-date.html#BNF">BNF Syntax for locale date</a>
     */
    @ParseMethod(start = "date", wideIndex = true)
    protected abstract void parseDate(String text, @ParserContext Calendar calendar) throws IOException;
    /**
     * 
     * @param text
     * @param calendar
     * @throws IOException 
     * @see <a href="doc-files/LocaleDateParser-time.html#BNF">BNF Syntax for locale time</a>
     */
    @ParseMethod(start = "time", wideIndex = true)
    protected abstract void parseTime(String text, @ParserContext Calendar calendar) throws IOException;

    public static LocaleDateParser newInstance() throws IOException
    {
        return (LocaleDateParser) GenClassFactory.getGenInstance(LocaleDateParser.class);
    }
    
@Terminal(expression="EGST|Eastern Greenland Summer Time|AZOST|Azores Summer Time|CVST|Cape Verde Summer Time|Africa/Abidjan|GMT|Greenwich Mean Time|Africa/Accra|Ghana Mean Time|Africa/Bamako|Africa/Banjul|Africa/Bissau|Africa/Casablanca|WET|Western European Time|Africa/Conakry|Africa/Dakar|Africa/El_Aaiun|Africa/Freetown|Africa/Lome|Africa/Monrovia|Africa/Nouakchott|Africa/Ouagadougou|Africa/Sao_Tome|Africa/Timbuktu|America/Danmarkshavn|Atlantic/Canary|Atlantic/Faeroe|Atlantic/Faroe|Atlantic/Madeira|Atlantic/Reykjavik|Atlantic/St_Helena|Eire|Etc/GMT|Etc/GMT\\+0|Etc/GMT\\-0|Etc/GMT0|Etc/Greenwich|Etc/UCT|UTC|Coordinated Universal Time|Etc/UTC|Etc/Universal|Etc/Zulu|Europe/Belfast|Europe/Dublin|Europe/Guernsey|Europe/Isle_of_Man|Europe/Jersey|Europe/Lisbon|Europe/London|GB|GB\\-Eire|GMT0|Greenwich|Iceland|Portugal|UCT|Universal|Zulu|Z")
protected int tzOffset0() { return 0;}
@Terminal(expression="FNST|Fernando de Noronha Summer Time|GDT|South Georgia Daylight Time|America/Scoresbysund|EGT|Eastern Greenland Time|Atlantic/Azores|AZOT|Azores Time|Atlantic/Cape_Verde|CVT|Cape Verde Time|Etc/GMT\\+1|N")
protected int tzOffset_3600000() { return -3600000;}
@Terminal(expression="Australia/Eucla|CWST|Central Western Standard Time \\(Australia\\)")
protected int tzOffset31500000() { return 31500000;}
@Terminal(expression="NPST|Nepal Summer Time")
protected int tzOffset24300000() { return 24300000;}
@Terminal(expression="Central Western Summer Time \\(Australia\\)")
protected int tzOffset35100000() { return 35100000;}
@Terminal(expression="ACT|Central Standard Time \\(Northern Territory\\)|Australia/Adelaide|Central Standard Time \\(South Australia\\)|Australia/Broken_Hill|Central Standard Time \\(South Australia/New South Wales\\)|Australia/Darwin|Australia/North|Australia/South|Australia/Yancowinna")
protected int tzOffset34200000() { return 34200000;}
@Terminal(expression="Etc/GMT\\+11|Pacific/Midway|SST|Samoa Standard Time|Pacific/Niue|NUT|Niue Time|Pacific/Pago_Pago|Pacific/Samoa|US/Samoa|X")
protected int tzOffset_39600000() { return -39600000;}
@Terminal(expression="PDT|Pacific Daylight Time|MeDT|Metlakatla Daylight Time|Pitcairn Daylight Time|America/Boise|MST|Mountain Standard Time|America/Cambridge_Bay|America/Chihuahua|America/Creston|America/Dawson_Creek|America/Denver|America/Edmonton|America/Hermosillo|America/Inuvik|America/Mazatlan|America/Ojinaga|America/Phoenix|America/Shiprock|America/Yellowknife|Canada/Mountain|Etc/GMT\\+7|MST7MDT|Mexico/BajaSur|Navajo|PNT|SystemV/MST7|SystemV/MST7MDT|US/Arizona|US/Mountain|T")
protected int tzOffset_25200000() { return -25200000;}
@Terminal(expression="MARST|Marquesas Summer Time")
protected int tzOffset_30600000() { return -30600000;}
@Terminal(expression="Eastern African Summer Time|SYOST|Syowa Summer Time|Arabia Daylight Time|FEST|Further\\-eastern European Summer Time|Asia/Baku|AZT|Azerbaijan Time|Asia/Dubai|Gulf Standard Time|Asia/Muscat|Asia/Tbilisi|GET|Georgia Time|Asia/Yerevan|Armenia Time|Etc/GMT\\-4|Europe/Moscow|MSK|Moscow Standard Time|Europe/Samara|SAMT|Samara Time|Europe/Volgograd|VOLT|Volgograd Time|Indian/Mahe|SCT|Seychelles Time|Indian/Mauritius|MUT|Mauritius Time|Indian/Reunion|RET|Reunion Time|NET|W\\-SU|D")
protected int tzOffset14400000() { return 14400000;}
@Terminal(expression="IRDT|Iran Daylight Time|Asia/Kabul|AFT|Afghanistan Time")
protected int tzOffset16200000() { return 16200000;}
@Terminal(expression="TLST|Timor\\-Leste Summer Time|IRKST|Irkutsk Summer Time|EIST|East Indonesia Summer Time|KDT|Korea Daylight Time|JDT|Japan Daylight Time|PWST|Palau Summer Time|AET|Eastern Standard Time \\(New South Wales\\)|Antarctica/DumontDUrville|DDUT|Dumont\\-d'Urville Time|Asia/Yakutsk|YAKT|Yakutsk Time|Australia/ACT|Australia/Brisbane|Eastern Standard Time \\(Queensland\\)|Australia/Canberra|Australia/Currie|Australia/Hobart|Eastern Standard Time \\(Tasmania\\)|Australia/Lindeman|Australia/Melbourne|Eastern Standard Time \\(Victoria\\)|Australia/NSW|Australia/Queensland|Australia/Sydney|Australia/Tasmania|Australia/Victoria|Etc/GMT\\-10|Pacific/Chuuk|CHUT|Chuuk Time|Pacific/Guam|ChST|Chamorro Standard Time|Pacific/Port_Moresby|PGT|Papua New Guinea Time|Pacific/Saipan|Pacific/Truk|Pacific/Yap|J")
protected int tzOffset36000000() { return 36000000;}
@Terminal(expression="Pacific/Norfolk|NFT|Norfolk Time")
protected int tzOffset41400000() { return 41400000;}
@Terminal(expression="HADT|Hawaii\\-Aleutian Daylight Time|HDT|Hawaii Daylight Time|CKHST|Cook Is\\. Summer Time|TAHST|Tahiti Summer Time|AST|AKST|Alaska Standard Time|America/Anchorage|America/Juneau|America/Nome|America/Sitka|America/Yakutat|Etc/GMT\\+9|Pacific/Gambier|GAMT|Gambier Time|SystemV/YST9|SystemV/YST9YDT|US/Alaska|V")
protected int tzOffset_32400000() { return -32400000;}
@Terminal(expression="NZ\\-CHAT|CHAST|Chatham Standard Time|Pacific/Chatham")
protected int tzOffset45900000() { return 45900000;}
@Terminal(expression="ARST|Argentine Summer Time|BRST|Brasilia Summer Time|GFST|French Guiana Summer Time|WGST|Western Greenland Summer Time|PMDT|Pierre \\& Miquelon Daylight Time|UYST|Uruguay Summer Time|SRST|Suriname Summer Time|ROTST|Rothera Summer Time|FKST|Falkland Is\\. Summer Time|America/Noronha|FNT|Fernando de Noronha Time|Atlantic/South_Georgia|GST|South Georgia Standard Time|Brazil/DeNoronha|Etc/GMT\\+2|O")
protected int tzOffset_7200000() { return -7200000;}
@Terminal(expression="MAWST|Mawson Summer Time|AQTST|Aqtau Summer Time|Aqtobe Summer Time|TMST|Turkmenistan Summer Time|TJST|Tajikistan Summer Time|PKST|Pakistan Summer Time|ORAST|Oral Summer Time|UZST|Uzbekistan Summer Time|TFST|French Southern \\& Antarctic Lands Summer Time|MVST|Maldives Summer Time|Antarctica/Vostok|VOST|Vostok Time|Asia/Almaty|ALMT|Alma\\-Ata Time|Asia/Bishkek|KGT|Kirgizstan Time|Asia/Dacca|BDT|Bangladesh Time|Asia/Dhaka|Asia/Qyzylorda|QYZT|Qyzylorda Time|Asia/Thimbu|BTT|Bhutan Time|Asia/Thimphu|Asia/Yekaterinburg|YEKT|Yekaterinburg Time|Etc/GMT\\-6|Indian/Chagos|IOT|Indian Ocean Territory Time|F")
protected int tzOffset21600000() { return 21600000;}
@Terminal(expression="India Daylight Time|Asia/Rangoon|MMT|Myanmar Time|Indian/Cocos|CCT|Cocos Islands Time")
protected int tzOffset23400000() { return 23400000;}
@Terminal(expression="ADT|Atlantic Daylight Time|WARST|Western Argentine Summer Time|PYST|Paraguay Summer Time|AMST|Amazon Summer Time|GYST|Guyana Summer Time|BOST|Bolivia Summer Time|CLST|Chile Summer Time|AGT|ART|Argentine Time|America/Araguaina|BRT|Brasilia Time|America/Argentina/Buenos_Aires|America/Argentina/Catamarca|America/Argentina/ComodRivadavia|America/Argentina/Cordoba|America/Argentina/Jujuy|America/Argentina/La_Rioja|America/Argentina/Mendoza|America/Argentina/Rio_Gallegos|America/Argentina/Salta|America/Argentina/San_Juan|America/Argentina/Tucuman|America/Argentina/Ushuaia|America/Bahia|America/Belem|America/Buenos_Aires|America/Catamarca|America/Cayenne|GFT|French Guiana Time|America/Cordoba|America/Fortaleza|America/Godthab|WGT|Western Greenland Time|America/Jujuy|America/Maceio|America/Mendoza|America/Miquelon|PMST|Pierre \\& Miquelon Standard Time|America/Montevideo|UYT|Uruguay Time|America/Paramaribo|SRT|Suriname Time|America/Recife|America/Rosario|America/Santarem|America/Sao_Paulo|Antarctica/Rothera|ROTT|Rothera Time|Atlantic/Stanley|FKT|Falkland Is\\. Time|BET|Brazil/East|Etc/GMT\\+3|P")
protected int tzOffset_10800000() { return -10800000;}
@Terminal(expression="Asia/Tehran|IRST|Iran Standard Time|Iran")
protected int tzOffset12600000() { return 12600000;}
@Terminal(expression="Etc/GMT\\+12|Y")
protected int tzOffset_43200000() { return -43200000;}
@Terminal(expression="CHADT|Chatham Daylight Time")
protected int tzOffset49500000() { return 49500000;}
@Terminal(expression="CDT|Central Daylight Time|EASST|Easter Is\\. Summer Time|GALST|Galapagos Summer Time|America/Atikokan|EST|Eastern Standard Time|America/Bogota|COT|Colombia Time|America/Cayman|America/Coral_Harbour|America/Detroit|America/Fort_Wayne|America/Grand_Turk|America/Guayaquil|ECT|Ecuador Time|America/Havana|Cuba Standard Time|America/Indiana/Indianapolis|America/Indiana/Marengo|America/Indiana/Petersburg|America/Indiana/Vevay|America/Indiana/Vincennes|America/Indiana/Winamac|America/Indianapolis|America/Iqaluit|America/Jamaica|America/Kentucky/Louisville|America/Kentucky/Monticello|America/Lima|PET|Peru Time|America/Louisville|America/Montreal|America/Nassau|America/New_York|America/Nipigon|America/Panama|America/Pangnirtung|America/Port\\-au\\-Prince|America/Thunder_Bay|America/Toronto|Canada/Eastern|Cuba|EST5EDT|Etc/GMT\\+5|IET|Jamaica|SystemV/EST5|SystemV/EST5EDT|US/East\\-Indiana|US/Eastern|US/Michigan|R")
protected int tzOffset_18000000() { return -18000000;}
@Terminal(expression="America/Caracas|VET|Venezuela Time")
protected int tzOffset_16200000() { return -16200000;}
@Terminal(expression="DAVST|Davis Summer Time|ICST|Indochina Summer Time|HOVST|Hovd Summer Time|WIST|West Indonesia Summer Time|NOVST|Novosibirsk Summer Time|OMSST|Omsk Summer Time|CXST|Christmas Island Summer Time|Antarctica/Casey|WST|Western Standard Time \\(Australia\\)|Asia/Brunei|BNT|Brunei Time|Asia/Choibalsan|CHOT|Choibalsan Time|Asia/Chongqing|China Standard Time|Asia/Chungking|Asia/Harbin|Asia/Hong_Kong|HKT|Hong Kong Time|Asia/Kashgar|Asia/Krasnoyarsk|KRAT|Krasnoyarsk Time|Asia/Kuala_Lumpur|MYT|Malaysia Time|Asia/Kuching|Asia/Macao|Asia/Macau|Asia/Makassar|CIT|Central Indonesia Time|Asia/Manila|PHT|Philippines Time|Asia/Shanghai|Asia/Singapore|SGT|Singapore Time|Asia/Taipei|Asia/Ujung_Pandang|Asia/Ulaanbaatar|ULAT|Ulaanbaatar Time|Asia/Ulan_Bator|Asia/Urumqi|Australia/Perth|Australia/West|CTT|Etc/GMT\\-8|Hongkong|PRC|Singapore|H")
protected int tzOffset28800000() { return 28800000;}
@Terminal(expression="Western Summer Time \\(Australia\\)|BNST|Brunei Summer Time|CHOST|Choibalsan Summer Time|China Daylight Time|HKST|Hong Kong Summer Time|KRAST|Krasnoyarsk Summer Time|MYST|Malaysia Summer Time|CIST|Central Indonesia Summer Time|PHST|Philippines Summer Time|SGST|Singapore Summer Time|ULAST|Ulaanbaatar Summer Time|Asia/Dili|TLT|Timor\\-Leste Time|Asia/Irkutsk|IRKT|Irkutsk Time|Asia/Jayapura|EIT|East Indonesia Time|Asia/Pyongyang|KST|Korea Standard Time|Asia/Seoul|Asia/Tokyo|JST|Japan Standard Time|Etc/GMT\\-9|Japan|Pacific/Palau|PWT|Palau Time|ROK|I")
protected int tzOffset32400000() { return 32400000;}
@Terminal(expression="NDT|Newfoundland Daylight Time")
protected int tzOffset_9000000() { return -9000000;}
@Terminal(expression="MDT|Mountain Daylight Time|America/Bahia_Banderas|CST|Central Standard Time|America/Belize|America/Cancun|America/Chicago|America/Costa_Rica|America/El_Salvador|America/Guatemala|America/Indiana/Knox|America/Indiana/Tell_City|America/Knox_IN|America/Managua|America/Matamoros|America/Menominee|America/Merida|America/Mexico_City|America/Monterrey|America/North_Dakota/Beulah|America/North_Dakota/Center|America/North_Dakota/New_Salem|America/Rainy_River|America/Rankin_Inlet|America/Regina|America/Resolute|America/Swift_Current|America/Tegucigalpa|America/Winnipeg|CST6CDT|Canada/Central|Canada/East\\-Saskatchewan|Canada/Saskatchewan|Chile/EasterIsland|EAST|Easter Is\\. Time|Etc/GMT\\+6|Mexico/General|Pacific/Easter|Pacific/Galapagos|GALT|Galapagos Time|SystemV/CST6|SystemV/CST6CDT|US/Central|US/Indiana\\-Starke|S")
protected int tzOffset_21600000() { return -21600000;}
@Terminal(expression="CEST|Central European Summer Time|WAST|Western African Summer Time|MEST|Middle Europe Summer Time|EET|Eastern European Time|Africa/Blantyre|CAT|Central African Time|Africa/Bujumbura|Africa/Cairo|Africa/Gaborone|Africa/Harare|Africa/Johannesburg|SAST|South Africa Standard Time|Africa/Kigali|Africa/Lubumbashi|Africa/Lusaka|Africa/Maputo|Africa/Maseru|Africa/Mbabane|Africa/Tripoli|Asia/Amman|Asia/Beirut|Asia/Damascus|Asia/Gaza|Asia/Hebron|Asia/Istanbul|Asia/Jerusalem|Israel Standard Time|Asia/Nicosia|Asia/Tel_Aviv|Egypt|Etc/GMT\\-2|Europe/Athens|Europe/Bucharest|Europe/Chisinau|Europe/Helsinki|Europe/Istanbul|Europe/Kiev|Europe/Mariehamn|Europe/Nicosia|Europe/Riga|Europe/Simferopol|Europe/Sofia|Europe/Tallinn|Europe/Tiraspol|Europe/Uzhgorod|Europe/Vilnius|Europe/Zaporozhye|Israel|Libya|Turkey|B")
protected int tzOffset7200000() { return 7200000;}
@Terminal(expression="AZST|Azerbaijan Summer Time|Gulf Daylight Time|GEST|Georgia Summer Time|Armenia Summer Time|MSD|Moscow Daylight Time|SAMST|Samara Summer Time|VOLST|Volgograd Summer Time|SCST|Seychelles Summer Time|MUST|Mauritius Summer Time|REST|Reunion Summer Time|Antarctica/Mawson|MAWT|Mawson Time|Asia/Aqtau|AQTT|Aqtau Time|Asia/Aqtobe|Aqtobe Time|Asia/Ashgabat|TMT|Turkmenistan Time|Asia/Ashkhabad|Asia/Dushanbe|TJT|Tajikistan Time|Asia/Karachi|PKT|Pakistan Time|Asia/Oral|ORAT|Oral Time|Asia/Samarkand|UZT|Uzbekistan Time|Asia/Tashkent|Etc/GMT\\-5|Indian/Kerguelen|TFT|French Southern \\& Antarctic Lands Time|Indian/Maldives|MVT|Maldives Time|PLT|E")
protected int tzOffset18000000() { return 18000000;}
@Terminal(expression="Asia/Kathmandu|NPT|Nepal Time|Asia/Katmandu")
protected int tzOffset20700000() { return 20700000;}
@Terminal(expression="AKDT|Alaska Daylight Time|GAMST|Gambier Summer Time|America/Dawson|PST|Pacific Standard Time|America/Ensenada|America/Los_Angeles|America/Metlakatla|MeST|Metlakatla Standard Time|America/Santa_Isabel|America/Tijuana|America/Vancouver|America/Whitehorse|Canada/Pacific|Canada/Yukon|Etc/GMT\\+8|Mexico/BajaNorte|PST8PDT|Pacific/Pitcairn|Pitcairn Standard Time|SystemV/PST8|SystemV/PST8PDT|US/Pacific|US/Pacific\\-New|U")
protected int tzOffset_28800000() { return -28800000;}
@Terminal(expression="EEST|Eastern European Summer Time|CAST|Central African Summer Time|South Africa Summer Time|IDT|Israel Daylight Time|Africa/Addis_Ababa|EAT|Eastern African Time|Africa/Asmara|Africa/Asmera|Africa/Dar_es_Salaam|Africa/Djibouti|Africa/Juba|Africa/Kampala|Africa/Khartoum|Africa/Mogadishu|Africa/Nairobi|Antarctica/Syowa|SYOT|Syowa Time|Asia/Aden|Arabia Standard Time|Asia/Baghdad|Asia/Bahrain|Asia/Kuwait|Asia/Qatar|Asia/Riyadh|Etc/GMT\\-3|Europe/Kaliningrad|FET|Further\\-eastern European Time|Europe/Minsk|Indian/Antananarivo|Indian/Comoro|Indian/Mayotte|C")
protected int tzOffset10800000() { return 10800000;}
@Terminal(expression="SDT|Samoa Daylight Time|NUST|Niue Summer Time|America/Adak|HAST|Hawaii\\-Aleutian Standard Time|America/Atka|Etc/GMT\\+10|HST|Hawaii Standard Time|Pacific/Honolulu|Pacific/Johnston|Pacific/Rarotonga|CKT|Cook Is\\. Time|Pacific/Tahiti|TAHT|Tahiti Time|SystemV/HST10|US/Aleutian|US/Hawaii|W")
protected int tzOffset_36000000() { return -36000000;}
@Terminal(expression="VEST|Venezuela Summer Time|America/St_Johns|NST|Newfoundland Standard Time|CNT|Canada/Newfoundland")
protected int tzOffset_12600000() { return -12600000;}
@Terminal(expression="WSDT|West Samoa Daylight Time|PHOST|Phoenix Is\\. Summer Time|TOST|Tonga Summer Time|Etc/GMT\\-14|Pacific/Fakaofo|TKT|Tokelau Time|Pacific/Kiritimati|LINT|Line Is\\. Time")
protected int tzOffset50400000() { return 50400000;}
@Terminal(expression="Macquarie Island Summer Time|SAKST|Sakhalin Summer Time|VLAST|Vladivostok Summer Time|VUST|Vanuatu Summer Time|SBST|Solomon Is\\. Summer Time|KOSST|Kosrae Summer Time|NCST|New Caledonia Summer Time|PONST|Pohnpei Summer Time|Antarctica/McMurdo|NZST|New Zealand Standard Time|Antarctica/South_Pole|Asia/Anadyr|ANAT|Anadyr Time|Asia/Kamchatka|PETT|Petropavlovsk\\-Kamchatski Time|Asia/Magadan|MAGT|Magadan Time|Etc/GMT\\-12|Kwajalein|MHT|Marshall Islands Time|NZ|Pacific/Auckland|Pacific/Fiji|FJT|Fiji Time|Pacific/Funafuti|TVT|Tuvalu Time|Pacific/Kwajalein|Pacific/Majuro|Pacific/Nauru|NRT|Nauru Time|Pacific/Tarawa|GILT|Gilbert Is\\. Time|Pacific/Wake|WAKT|Wake Time|Pacific/Wallis|WFT|Wallis \\& Futuna Time|L")
protected int tzOffset43200000() { return 43200000;}
@Terminal(expression="NFST|Norfolk Summer Time")
protected int tzOffset45000000() { return 45000000;}
@Terminal(expression="GHST|Ghana Summer Time|WEST|Western European Summer Time|SLST|Sierra Leone Summer Time|IST|Irish Summer Time|BST|British Summer Time|Africa/Algiers|CET|Central European Time|Africa/Bangui|WAT|Western African Time|Africa/Brazzaville|Africa/Ceuta|Africa/Douala|Africa/Kinshasa|Africa/Lagos|Africa/Libreville|Africa/Luanda|Africa/Malabo|Africa/Ndjamena|Africa/Niamey|Africa/Porto\\-Novo|Africa/Tunis|Africa/Windhoek|Arctic/Longyearbyen|Atlantic/Jan_Mayen|Etc/GMT\\-1|Europe/Amsterdam|Europe/Andorra|Europe/Belgrade|Europe/Berlin|Europe/Bratislava|Europe/Brussels|Europe/Budapest|Europe/Copenhagen|Europe/Gibraltar|Europe/Ljubljana|Europe/Luxembourg|Europe/Madrid|Europe/Malta|Europe/Monaco|Europe/Oslo|Europe/Paris|Europe/Podgorica|Europe/Prague|Europe/Rome|Europe/San_Marino|Europe/Sarajevo|Europe/Skopje|Europe/Stockholm|Europe/Tirane|Europe/Vaduz|Europe/Vatican|Europe/Vienna|Europe/Warsaw|Europe/Zagreb|Europe/Zurich|MET|Middle Europe Time|Poland|A")
protected int tzOffset3600000() { return 3600000;}
@Terminal(expression="NZDT|New Zealand Daylight Time|ANAST|Anadyr Summer Time|PETST|Petropavlovsk\\-Kamchatski Summer Time|MAGST|Magadan Summer Time|MHST|Marshall Islands Summer Time|FJST|Fiji Summer Time|TVST|Tuvalu Summer Time|NRST|Nauru Summer Time|GILST|Gilbert Is\\. Summer Time|WAKST|Wake Summer Time|WFST|Wallis \\& Futuna Summer Time|Etc/GMT\\-13|MIT|West Samoa Time|Pacific/Apia|Pacific/Enderbury|PHOT|Phoenix Is\\. Time|Pacific/Tongatapu|TOT|Tonga Time|M")
protected int tzOffset46800000() { return 46800000;}
@Terminal(expression="VOSST|Vostok Summer Time|ALMST|Alma\\-Ata Summer Time|KGST|Kirgizstan Summer Time|BDST|Bangladesh Summer Time|QYZST|Qyzylorda Summer Time|BTST|Bhutan Summer Time|YEKST|Yekaterinburg Summer Time|IOST|Indian Ocean Territory Summer Time|Antarctica/Davis|DAVT|Davis Time|Asia/Bangkok|ICT|Indochina Time|Asia/Ho_Chi_Minh|Asia/Hovd|HOVT|Hovd Time|Asia/Jakarta|WIT|West Indonesia Time|Asia/Novokuznetsk|NOVT|Novosibirsk Time|Asia/Novosibirsk|Asia/Omsk|OMST|Omsk Time|Asia/Phnom_Penh|Asia/Pontianak|Asia/Saigon|Asia/Vientiane|Etc/GMT\\-7|Indian/Christmas|CXT|Christmas Island Time|VST|G")
protected int tzOffset25200000() { return 25200000;}
@Terminal(expression="Eastern Summer Time \\(New South Wales\\)|DDUST|Dumont\\-d'Urville Summer Time|YAKST|Yakutsk Summer Time|Eastern Summer Time \\(Queensland\\)|Eastern Summer Time \\(Tasmania\\)|Eastern Summer Time \\(Victoria\\)|CHUST|Chuuk Summer Time|ChDT|Chamorro Daylight Time|PGST|Papua New Guinea Summer Time|Lord Howe Summer Time|Antarctica/Macquarie|MIST|Macquarie Island Time|Asia/Sakhalin|SAKT|Sakhalin Time|Asia/Vladivostok|VLAT|Vladivostok Time|Etc/GMT\\-11|Pacific/Efate|VUT|Vanuatu Time|Pacific/Guadalcanal|SBT|Solomon Is\\. Time|Pacific/Kosrae|KOST|Kosrae Time|Pacific/Noumea|NCT|New Caledonia Time|Pacific/Pohnpei|PONT|Pohnpei Time|Pacific/Ponape|K")
protected int tzOffset39600000() { return 39600000;}
@Terminal(expression="TKST|Tokelau Summer Time|LINST|Line Is\\. Summer Time")
protected int tzOffset54000000() { return 54000000;}
@Terminal(expression="Central Summer Time \\(Northern Territory\\)|Central Summer Time \\(South Australia\\)|Central Summer Time \\(South Australia/New South Wales\\)|Australia/LHI|LHST|Lord Howe Standard Time|Australia/Lord_Howe")
protected int tzOffset37800000() { return 37800000;}
@Terminal(expression="EDT|Eastern Daylight Time|COST|Colombia Summer Time|ECST|Ecuador Summer Time|Cuba Daylight Time|PEST|Peru Summer Time|America/Anguilla|Atlantic Standard Time|America/Antigua|America/Argentina/San_Luis|WART|Western Argentine Time|America/Aruba|America/Asuncion|PYT|Paraguay Time|America/Barbados|America/Blanc\\-Sablon|America/Boa_Vista|AMT|Amazon Time|America/Campo_Grande|America/Cuiaba|America/Curacao|America/Dominica|America/Eirunepe|America/Glace_Bay|America/Goose_Bay|America/Grenada|America/Guadeloupe|America/Guyana|GYT|Guyana Time|America/Halifax|America/Kralendijk|America/La_Paz|BOT|Bolivia Time|America/Lower_Princes|America/Manaus|America/Marigot|America/Martinique|America/Moncton|America/Montserrat|America/Port_of_Spain|America/Porto_Acre|America/Porto_Velho|America/Puerto_Rico|America/Rio_Branco|America/Santiago|CLT|Chile Time|America/Santo_Domingo|America/St_Barthelemy|America/St_Kitts|America/St_Lucia|America/St_Thomas|America/St_Vincent|America/Thule|America/Tortola|America/Virgin|Antarctica/Palmer|Atlantic/Bermuda|Brazil/Acre|Brazil/West|Canada/Atlantic|Chile/Continental|Etc/GMT\\+4|PRT|SystemV/AST4|SystemV/AST4ADT|Q")
protected int tzOffset_14400000() { return -14400000;}
@Terminal(expression="AFST|Afghanistan Summer Time|Asia/Calcutta|India Standard Time|Asia/Colombo|Asia/Kolkata")
protected int tzOffset19800000() { return 19800000;}
@Terminal(expression="MMST|Myanmar Summer Time|CCST|Cocos Islands Summer Time")
protected int tzOffset27000000() { return 27000000;}
@Terminal(expression="Asia/Riyadh87|Asia/Riyadh88|Asia/Riyadh89|Mideast/Riyadh87|Mideast/Riyadh88|Mideast/Riyadh89")
protected int tzOffset11224000() { return 11224000;}
@Terminal(expression="Pacific/Marquesas|MART|Marquesas Time")
protected int tzOffset_34200000() { return -34200000;}
}
