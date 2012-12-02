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
package org.vesalainen.parsers.sql;

import java.util.Comparator;
import java.util.Date;

/**
 *
 * @author Timo Vesalainen
 */
public interface SQLConverter<R, C>
{

    C convert(String string);

    C convert(Number number);

    C convertDate(Date date);

    C convertTime(Date date);

    C convertTimestamp(Date date);

    C get(R r, String column);
    /**
     * Returns Updateable instance.
     * @param r Row
     * @param column Column name
     * @param value Current value. If column is referenced inside function the
     * value might differ
     * @return 
     */
    Updateable<R, C> getUpdateable(R r, String column, C value);

    void set(R r, String column, C value);
    
    Comparator<C> getComparator();

}
