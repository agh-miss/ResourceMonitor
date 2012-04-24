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
 * File: ConfigUtils.java
 * Created: 2012-01-28
 * Author: Krzywicki
 * $Id: ConfigUtils.java 15 2012-01-29 07:43:32Z krzywick $
 */

package org.jage.platform.config.xml;

import java.util.List;
import java.util.Set;

import org.dom4j.Element;

import org.jage.platform.component.definition.ConfigurationException;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.Iterables.getOnlyElement;

/**
 * Static utility methods for configuration processing.
 *
 * @author AGH AgE Team
 */
public final class ConfigUtils {

	public static String getRequiredAttribute(Element element, ConfigAttributes attribute) throws ConfigurationException {
		return getRequiredAttribute(element, attribute, false);
	}

	public static String getRequiredAttribute(Element element, ConfigAttributes attribute, boolean allowEmptyString)
	        throws ConfigurationException {
		String attributeValue = element.attributeValue(attribute.toString());
		if (attributeValue == null) {
			throw new ConfigurationException(attribute + " attribute is required");
		}
		if (!allowEmptyString && attributeValue.isEmpty()) {
			throw new ConfigurationException(attribute + " attribute must not be empty");
		}
		return attributeValue;
	}

	public static Optional<String> getOptionalAttribute(Element element, ConfigAttributes attribute)
	        throws ConfigurationException {
		String attributeValue = element.attributeValue(attribute.toString());
		if (attributeValue != null && attributeValue.isEmpty()) {
			throw new ConfigurationException(attribute + " attribute must not be empty");
		}
		return Optional.fromNullable(attributeValue);
	}

	@SuppressWarnings("unchecked")
	public static List<Element> getAllChildren(Element element) {
		return element.elements();
	}

	@SuppressWarnings("unchecked")
	public static List<Element> getChildrenIncluding(Element element, ConfigTags tag) {
		return element.elements(tag.toString());
	}

	public static List<Element> getChildrenIncluding(Element element, ConfigTags... tags) {
		final Set<String> includedTags = toSet(tags);
		return copyOf(filter(getAllChildren(element), new Predicate<Element>() {
			@Override
			public boolean apply(Element element) {
				return includedTags.contains(element.getName());
			}
		}));
	}

	public static List<Element> getChildrenExcluding(Element element, ConfigTags... tags) {
		final Set<String> excludedTags = toSet(tags);
		return copyOf(filter(getAllChildren(element), new Predicate<Element>() {
			@Override
			public boolean apply(Element element) {
				return !excludedTags.contains(element.getName());
			}
		}));
	}

	public static Element getChild(Element element, ConfigTags tag) throws ConfigurationException {
		return getOnlyElement(getChildrenIncluding(element, tag));
	}

	public static Element getChild(Element element) throws ConfigurationException {
		return getOnlyElement(getAllChildren(element));
	}

	public static String getDefaultType() {
		return "java.lang.Object";
	}

	public static String getDefaultIsSingleton() {
		return "false";
	}

	public static boolean toBoolean(String booleanAsString) {
		return Boolean.parseBoolean(booleanAsString);
	}

	public static Class<?> toClass(String classAsString) throws ConfigurationException {
		try {
			if (!classAsString.contains(".")) {
				classAsString = "java.lang." + classAsString;
			}
			return Class.forName(classAsString);
		} catch (ClassNotFoundException e) {
			throw new ConfigurationException(classAsString + " class couldn't be found");
		}
	}

	private static Set<String> toSet(ConfigTags[] tags) {
		Set<String> set = Sets.newHashSetWithExpectedSize(tags.length);
		for(ConfigTags tag : tags) {
			set.add(tag.toString());
		}
		return set;
    }
}
