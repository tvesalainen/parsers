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
package org.vesalainen.parsers.xml.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import org.vesalainen.bcc.model.Jav;
import org.vesalainen.parsers.xml.XMLParserBase;

/**
 * @deprecated Not migrated to annotation processor
 * @author Timo Vesalainen
 */
public abstract class Element implements Iterable<Element>
{
    private String $prefix;
    private List<Element> childrens;

    public String prefix()
    {
        return $prefix;
    }
    /*
    public static ExecutableElement getNewInstanceMethodWithContent(String systemId, String elementName) throws NoSuchMethodException
    {
        Type attrs = TypeFactory.createParameterizedType(Collection.class, null, Attribute.class);
        Type elems = TypeFactory.createParameterizedType(Collection.class, null, Element.class);
        return getNewInstanceMethod(systemId, elementName, attrs, elems, Document.class);
    }
    public static ExecutableElement getNewInstanceMethodWithoutContent(String systemId, String elementName) throws NoSuchMethodException
    {
        Type attrs = TypeFactory.createParameterizedType(Collection.class, null, Attribute.class);
        return getNewInstanceMethod(systemId, elementName, attrs, Document.class);
    }
    private static ExecutableElement getNewInstanceMethod(String systemId, String elementName, TypeMirror... parameters) throws NoSuchMethodException
    {
        String packageName = Document.getPackagename(systemId);
        String elementClassname = packageName+".el."+Jav.makeJavaClassname(elementName);
        Type enclosingClass = ClassWrapper.wrap(elementClassname, Element.class);
        MethodWrapper mw = new MethodWrapper(
                Modifier.PUBLIC|Modifier.STATIC, 
                enclosingClass, 
                "newInstance", 
                enclosingClass, 
                parameters
                );
        Method parse = XMLParserBase.class.getDeclaredMethod("parse", InputStream.class, Document.class);
        assert parse != null;
        Annotation document = parse.getParameterAnnotations()[1][0];
        mw.addParameterAnnotation(parameters.length-1, document);
        return mw;
    }
    */
    /**
     * Returns elements localname;
     * @return 
     */
    public abstract String localName();
    
    public void add(Element element)
    {
        childrens.add(element);
    }
    
    void write(String prefix, Writer writer) throws IOException
    {
        writer.append(prefix);
    }

    @Override
    public Iterator<Element> iterator()
    {
        return childrens.iterator();
    }
}
