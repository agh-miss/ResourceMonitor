/**
 * Copyright (C) 2006 - 2010
 *   Pawel Kedzior
 *   Tomasz Kmiecik
 *   Kamil Pietak
 *   Krzysztof Sikora
 *   Adam Wos
 *   and other students of AGH University of Science and Technology
 *   as indicated in each file separately.
 *
 * This file is part of jAgE.
 *
 * jAgE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jAgE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with jAgE.  If not, see http://www.gnu.org/licenses/
 */
/*
 * File: ConfigTestUtils.java
 * Created: 2012-01-28
 * Author: Krzywicki
 * $Id: ConfigTestUtils.java 15 2012-01-29 07:43:32Z krzywick $
 */

package org.jage.platform.config.xml;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.tree.DefaultElement;

import static org.dom4j.DocumentHelper.createDocument;
import static org.dom4j.DocumentHelper.createElement;
import static org.dom4j.DocumentHelper.createNamespace;
import static org.dom4j.DocumentHelper.createQName;

/**
 * Static utility methods for testing configuration.
 *
 * @author AGH AgE Team
 */
public class ConfigTestUtils {

	/**
	 * Creates an empty configuration Document, with appropriate namespace settings.
	 *
	 * @return an empty document.
	 */
	public static Document createEmptyDocument() {
		Namespace createNamespace = createNamespace("", ConfigNamespaces.DEFAULT.getUri());
		QName qName = createQName(ConfigTags.CONFIGURATION.toString(), createNamespace);
		return createDocument(createElement(qName));
	}

	public static final String EMPTY_STRING = "";
	public static final String ANY_TAG = "any";
	public static final String NAME_VALUE = "someName";
	public static final String CLASS_VALUE = "java.lang.String";
	public static final String IS_SINGLETON_VALUE = "true";

	public static ElementBuilder newElement(ConfigTags tag) {
		return new ElementBuilder(tag.toString());
	}

	public static ElementBuilder anyElement() {
		return new ElementBuilder(ANY_TAG);
	}

	public static ElementBuilder newComponentElement() {
		return newElement(ConfigTags.COMPONENT)
				.withAttribute(ConfigAttributes.NAME, NAME_VALUE)
				.withAttribute(ConfigAttributes.CLASS, CLASS_VALUE)
				.withAttribute(ConfigAttributes.IS_SINGLETON, IS_SINGLETON_VALUE);
    }

	public static ElementBuilder newArrayElement() {
		return newElement(ConfigTags.ARRAY)
				.withAttribute(ConfigAttributes.NAME, NAME_VALUE)
				.withAttribute(ConfigAttributes.TYPE, CLASS_VALUE)
				.withAttribute(ConfigAttributes.IS_SINGLETON, IS_SINGLETON_VALUE);
	}

	public static ElementBuilder newCollectionElement() {
		return newElement(ConfigTags.LIST)
				.withAttribute(ConfigAttributes.NAME, NAME_VALUE)
				.withAttribute(ConfigAttributes.TYPE, CLASS_VALUE)
				.withAttribute(ConfigAttributes.IS_SINGLETON, IS_SINGLETON_VALUE);
	}

	public static ElementBuilder newMapElement() {
		return newElement(ConfigTags.MAP)
				.withAttribute(ConfigAttributes.NAME, NAME_VALUE)
				.withAttribute(ConfigAttributes.KEY_TYPE, CLASS_VALUE)
				.withAttribute(ConfigAttributes.VALUE_TYPE, CLASS_VALUE)
				.withAttribute(ConfigAttributes.IS_SINGLETON, IS_SINGLETON_VALUE);
	}


	public static ElementBuilder newValueElement(String classAttribute, String valueAttribute) {
		return newElement(ConfigTags.VALUE)
				.withAttribute(ConfigAttributes.CLASS, classAttribute)
				.withAttribute(ConfigAttributes.VALUE, valueAttribute);
	}

	public static ElementBuilder newReferenceElement(String targetAttribute) {
		return newElement(ConfigTags.REFERENCE)
				.withAttribute(ConfigAttributes.TARGET, targetAttribute);
	}

	public static ElementBuilder newConstructorElement(ElementBuilder body) {
		return newElement(ConfigTags.CONSTRUCTOR_ARG)
				.withBody(body);
	}

	public static ElementBuilder newPropertyElement(String propertyName, ElementBuilder body) {
		return newElement(ConfigTags.PROPERTY)
				.withAttribute(ConfigAttributes.NAME, propertyName)
				.withBody(body);
	}

	/**
	 * Helper builder class for definition elements.
	 *
	 * @author AGH AgE Team
	 */
	public static class ElementBuilder {

		private Element element;

		public ElementBuilder(String elementName) {
			element = new DefaultElement(elementName);
		}

		public ElementBuilder withAttribute(ConfigAttributes attribute, String attributeValue) {
			element.addAttribute(attribute.toString(), attributeValue);
			return this;
		}

		public ElementBuilder withBody(ElementBuilder... body) {
			for (ElementBuilder innerElement : body) {
				element.add(innerElement.build());
			}
			return this;
		}

		public Element build() {
			return element;
		}
	}
}
