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
package org.vesalainen.parsers.xml;

import java.util.List;
import org.vesalainen.parser.annotation.GrammarDef;
import org.vesalainen.parser.annotation.Rule;
import org.vesalainen.parser.annotation.Rules;

/**
 *
 * @author tkv
 */
//@GrammarDef
public abstract class DTDParserCompilerBase extends XMLBaseGrammar
{
    @Rule({"xmlDecl?", "misc*"})
    protected abstract void prolog();

    @Rule
    protected void namePrefix()
    {
    }

    @Rule({"name", "':'"})
    protected void namePrefix(String prefix)
    {
    }

    @Rules({
    @Rule({"'\"'", "name", "'\"'"}),
    @Rule({"`'´", "name", "`'´"})
    })
    protected abstract String attValueId(String value);
    @Rules({
    @Rule({"'\"'", "name", "'\"'"}),
    @Rule({"`'´", "name", "`'´"})
    })
    protected abstract String attValueIdRef(String value);
    @Rules({
    @Rule({"'\"'", "names", "'\"'"}),
    @Rule({"`'´", "names", "`'´"})
    })
    protected abstract List<String> attValueIdRefs(List<String> value);
    @Rules({
    @Rule({"'\"'", "name", "'\"'"}),
    @Rule({"`'´", "name", "`'´"})
    })
    protected abstract String attValueEntity(String value);
    @Rules({
    @Rule({"'\"'", "names", "'\"'"}),
    @Rule({"`'´", "names", "`'´"})
    })
    protected abstract List<String> attValueEntities(List<String> value);
    @Rules({
    @Rule({"'\"'", "nmtoken", "'\"'"}),
    @Rule({"`'´", "nmtoken", "`'´"})
    })
    protected abstract String attValueNmToken(String value);
    @Rules({
    @Rule({"'\"'", "nmtokens", "'\"'"}),
    @Rule({"`'´", "nmtokens", "`'´"})
    })
    protected abstract List<String> attValueNmTokens(List<String> value);
}
