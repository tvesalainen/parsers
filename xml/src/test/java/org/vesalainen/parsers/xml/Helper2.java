/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.vesalainen.parsers.xml;

/**
 *
 * @author tkv
 */
public class Helper2
{
    public static boolean isIdentifier(String id)
    {
        if (!Character.isJavaIdentifierStart(id.charAt(0)))
        {
            return false;
        }
        for (int ii=1;ii<id.length();ii++)
        {
            if (!Character.isJavaIdentifierPart(id.charAt(ii)))
            {
                return false;
            }
        }
        return true;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        try
        {
            char[] c = new char[] {'a'};
            Object o = c;
            o = 1;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
