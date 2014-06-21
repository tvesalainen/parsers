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

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;

/**
 * @author Timo Vesalainen
 */
public class PlaceholderImpl<R,C> extends LiteralImpl<R,C> implements Placeholder<R,C>
{
    private String name;
    private Class<?> type;
    private SelectStatement<R,C> select;
    
    public PlaceholderImpl(String name, Class<? extends C> type)
    {
        super(null);
        this.name = name;
        this.type = type;
    }

    public PlaceholderImpl(String name, Literal<R,C> defaultValue)
    {
        super(null);
        this.name = name;
        C val = defaultValue.getValue();
        if (val == null)
        {
            throw new IllegalArgumentException("Placeholder :"+name+" default value = null! Nested placeholders not supported!");
        }
        this.value = val;
        this.type = (Class<? extends C>) val.getClass();
    }

    public PlaceholderImpl(String name, SelectStatement<R,C> select)
    {
        super(null);
        this.name = name;
        this.select = select;
        this.type = ComboBoxModel.class;
    }

    @Override
    public Object getDefaultValue()
    {
        if (select != null)
        {
            OrderedFetchResult<R,C> result = select.execute();
            FetchResultComboBoxModel<R,C> model = new FetchResultComboBoxModel<>(result);
            return model;
        }
        return super.getValue();
    }

    @Override
    public void bindValue(C newValue)
    {
        if (newValue instanceof FetchResultComboBoxModel)
        {
            FetchResultComboBoxModel<R,C> model = (FetchResultComboBoxModel) newValue;
            this.value = (C) model.getOriginalSelectedItem();
        }
        else
        {
            if (type.isAssignableFrom(newValue.getClass()))
            {
                this.value = newValue;
            }
            else
            {
                throw new IllegalArgumentException(newValue+" type is not "+type);
            }
        }
    }

    @Override
    public void setType(Class<? extends C> type)
    {
        this.type = type;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public boolean isBound()
    {
        return value != null;
    }

    @Override
    public Class<?> getType()
    {
        return type;
    }

}
