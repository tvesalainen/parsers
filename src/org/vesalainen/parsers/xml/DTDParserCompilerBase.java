/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
@GrammarDef
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
