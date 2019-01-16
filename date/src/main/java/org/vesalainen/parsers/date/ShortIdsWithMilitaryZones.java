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

import java.io.IOException;
import java.time.ZoneId;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This map contains all entries from ZoneId.SHORT_IDS plus military time zones.
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 * @see java.time.ZoneId#SHORT_IDS
 */
public class ShortIdsWithMilitaryZones extends HashMap<String,String>
{
    public static final Map<String,String> SHORT_IDS;
    static
    {
        try
        {
            SHORT_IDS = Collections.unmodifiableMap(new ShortIdsWithMilitaryZones());
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex);
        }
    }
    private ShortIdsWithMilitaryZones() throws IOException
    {
        putAll(ZoneId.SHORT_IDS);
        for (int ii=0;ii<12;ii++)
        {
            int id = ii+1;
            put(String.valueOf((char)('A'+ii)), String.format("+%02d:00", id));
            put(String.valueOf((char)('N'+ii)), String.format("-%02d:00", id));
        }
        put("Z", "+00:00");
        put("MART", "-09:30");
    }
    
}
