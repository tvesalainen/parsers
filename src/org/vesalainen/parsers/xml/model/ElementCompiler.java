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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.List;
import org.vesalainen.bcc.MethodCompiler;
import org.vesalainen.bcc.SubClass;
import org.vesalainen.parsers.xml.attr.AttDef;
import org.vesalainen.parsers.xml.attr.AttType;
import org.vesalainen.parsers.xml.attr.DefaultDecl;
import org.vesalainen.parsers.xml.attr.DefaultDecl.DefaultValue;

/**
 * @deprecated Not migrated to annotation processor
 * @author Timo Vesalainen
 */
public class ElementCompiler
{
    private String packageName;
    private String localName;
    //private ClassWrapper thisClass;
    private SubClass subClass;
    private List<AttDef> attributes;

    public ElementCompiler(String packageName, String localName, List<AttDef> attributes)
    {
        this.packageName = packageName;
        this.localName = localName;
        this.attributes = attributes;
      //  thisClass = ClassWrapper.wrap(ClassWrapper.makeClassname(packageName, localName), Element.class);
    }

    public void compile() throws IOException
    {
//        subClass = new SubClass(thisClass);
        
        subClass.defineConstantField(Modifier.PRIVATE|Modifier.STATIC, "localName", localName);
        defineGetter(Modifier.PUBLIC|Modifier.STATIC, "localName", String.class);
        
        for (AttDef attr : attributes)
        {
        //    String name = lowerStart(MethodWrapper.makeJavaIdentifier(attr.getName()));
            AttType attType = attr.getAttType();
            DefaultDecl defaultDecl = attr.getDefaultDecl();
            Type classType = String.class;
            if (
                    attType == AttType.ENTITIES ||
                    attType == AttType.IDREFS ||
                    attType == AttType.NMTOKENS
                    )
            {
          //      classType = TypeFactory.createParameterizedType(List.class, null, String.class);
                if (defaultDecl instanceof DefaultValue)
                {
                    throw new UnsupportedOperationException("default value for "+attType+" nt supported");
                }
            }
            else
            {
                if (defaultDecl instanceof DefaultValue)
                {
                    DefaultValue defVal = (DefaultValue) defaultDecl;
                    if (defVal.isFixed())
                    {
            //            subClass.defineConstantField(Modifier.PRIVATE|Modifier.STATIC, name, defVal.getValue());
              //          defineGetter(Modifier.PUBLIC|Modifier.STATIC, name, classType);
                    }
                    else
                    {
                //        subClass.defineConstantField(Modifier.PRIVATE, name, defVal.getValue());
                  //      defineGetter(Modifier.PUBLIC, name, classType);
                    }
                }
            }
        }
    }
    
    public void saveAs(File classesDir, File srcDir)
    {
        
    }

    private void defineGetter(int modifier, String fieldName, Type type) throws IOException
    {
        /*
        String methodName = "get"+upperStart(fieldName);
        MethodCompiler mc = subClass.defineMethod(modifier, methodName, type);
        FieldWrapper fw = new FieldWrapper(modifier, fieldName, thisClass, type);
        mc.get(fw);
        mc.treturn();
        mc.end();
        */
    }
    
    private void defineSetter(int modifier, String fieldName, Type type) throws IOException
    {
        /*
        String methodName = "set"+upperStart(fieldName);
        MethodCompiler mc = subClass.defineMethod(modifier, methodName, void.class, type);
        mc.aload(1);
        FieldWrapper fw = new FieldWrapper(modifier, fieldName, thisClass, type);
        mc.put(fw);
        mc.end();
        */
    }
    
    private static String upperStart(String str)
    {
        return str.substring(0, 1).toUpperCase()+str.substring(1);
    }
    private static String lowerStart(String str)
    {
        return str.substring(0, 1).toLowerCase()+str.substring(1);
    }
}
