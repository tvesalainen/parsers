/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vesalainen.parsers.xml;

/**
 *
 * @author tkv
 */
public interface SAX2Constants
{
    public static final String FEATURES = "http://xml.org/sax/features/";
    public static final String FEATURE_NAMES_SPACE = FEATURES+"namespaces";
    public static final String FEATURE_NAMES_SPACE_PREFIXES = FEATURES+"namespace-prefixes";
    public static final String FEATURE_ENTITY_RESOLVER2 = FEATURES+"use-entity-resolver2";
    public static final String FEATURE_LOCATOR2 = FEATURES+"use-locator2";
    public static final String FEATURE_ATTRIBUTES2 = FEATURES+"use-attributes2";
    public static final String FEATURE_IS_STANDALONE = FEATURES+"is-standalone";
    public static final String FEATURE_XML_1_1 = FEATURES+"xml-1.1";
    public static final String FEATURE_VALIDATION = FEATURES+"validation";

    public static final String PROPERTIES = "http://xml.org/sax/properties/";
    public static final String PROPERTY_DECLARATION_HANDLER = PROPERTIES+"declaration-handler";
    public static final String PROPERTY_DOCUMENT_XML_VERSION = PROPERTIES+"document-xml-version";
    public static final String PROPERTY_LEXICAL_HANDLER = PROPERTIES+"lexical-handler";
}
