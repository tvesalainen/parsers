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
