/*
 * Copyright (C) 2013 Timo Vesalainen
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

import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;

/**
 * @author Timo Vesalainen
 */
public class FetchResultComboBoxModel<R,C> extends DefaultComboBoxModel<String>
{
    private FetchResult<R,C> result;
    private Map<String,C> map = new HashMap<>();
    public FetchResultComboBoxModel(FetchResult<R,C> result)
    {
        this.result = result;
        int count = result.getColumnCount();
        for (int row=0;row<result.getRowCount();row++)
        {
            String key = null;
            switch (count)
            {
                case 1:
                    key = result.getValueAt(row, 0).toString();
                    break;
                case 2:
                    key = result.getValueAt(row, 1).toString();
                    break;
                default:
                    StringBuilder sb = new StringBuilder();
                    for (int ii=1;ii<count;ii++)
                    {
                        sb.append(result.getValueAt(row, ii).toString()+" ");
                    }
                    key = sb.toString().trim();
                    break;
            }
            addElement(key);
            map.put(key, result.getValueAt(row, 0));
        }
    }

    @Override
    public Object getSelectedItem()
    {
        return map.get((String)super.getSelectedItem());
    }

}
