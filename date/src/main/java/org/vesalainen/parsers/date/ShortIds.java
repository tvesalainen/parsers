/*
 * Copyright (C) 2017 Timo Vesalainen <timo.vesalainen@iki.fi>
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

import au.com.bytecode.opencsv.CSVReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.time.ZoneId;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This map contains all entries from ZoneId.SHORT_IDS plus entries from
 * time zone database.
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 * @see <a href="https://timezonedb.com/download">Time Zone database</a>
 * @see java.time.ZoneId#SHORT_IDS
 */
public class ShortIds extends HashMap<String,String>
{
    public static final Map<String,String> SHORT_IDS;
    static
    {
        try
        {
            SHORT_IDS = Collections.unmodifiableMap(new ShortIds());
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex);
        }
    }
    private ShortIds() throws IOException
    {
        Map<Integer,String> zones = new HashMap<>();
        try (InputStream is = ShortIds.class.getResourceAsStream("/zone.csv");
                InputStreamReader isr = new InputStreamReader(is, UTF_8))
        {
            CSVReader reader = new CSVReader(isr);
            String[] line = reader.readNext();
            while (line != null)
            {
                zones.put(Integer.parseInt(line[0]), line[2]);
                line = reader.readNext();
            }
        }
        Map<String,Integer> timezones = new HashMap<>();
        try (InputStream is = ShortIds.class.getResourceAsStream("/timezone.csv");
                InputStreamReader isr = new InputStreamReader(is, UTF_8))
        {
            CSVReader reader = new CSVReader(isr);
            String[] line = reader.readNext();
            while (line != null)
            {
                timezones.put(line[1], Integer.parseInt(line[0]));
                line = reader.readNext();
            }
        }
        timezones.forEach((a,n)->
        {
            if (Character.isLetter(a.charAt(0)))
            {
                String z = zones.get(n);
                put(a,z);
            }
        });
        putAll(ZoneId.SHORT_IDS);
        for (int ii=0;ii<12;ii++)
        {
            int id = ii+1;
            put(String.valueOf((char)('A'+ii)), String.format("+%02d:00", id));
            put(String.valueOf((char)('N'+ii)), String.format("-%02d:00", id));
        }
        put("Z", "+00:00");
    }
    
}
