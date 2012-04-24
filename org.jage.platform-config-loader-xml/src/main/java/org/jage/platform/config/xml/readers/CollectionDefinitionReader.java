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
 * File: CollectionDefinitionReader.java
 * Created: 2012-01-28
 * Author: Krzywicki
 * $Id: CollectionDefinitionReader.java 59 2012-02-13 13:10:28Z krzywick $
 */

package org.jage.platform.config.xml.readers;

import java.util.Collection;

import org.dom4j.Element;

import org.jage.platform.component.definition.CollectionDefinition;
import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.component.definition.IComponentDefinition;
import org.jage.platform.component.definition.IArgumentDefinition;

import static org.jage.platform.config.xml.ConfigAttributes.IS_SINGLETON;
import static org.jage.platform.config.xml.ConfigAttributes.NAME;
import static org.jage.platform.config.xml.ConfigAttributes.TYPE;
import static org.jage.platform.config.xml.ConfigTags.REFERENCE;
import static org.jage.platform.config.xml.ConfigTags.VALUE;
import static org.jage.platform.config.xml.ConfigUtils.getChildrenExcluding;
import static org.jage.platform.config.xml.ConfigUtils.getChildrenIncluding;
import static org.jage.platform.config.xml.ConfigUtils.getDefaultIsSingleton;
import static org.jage.platform.config.xml.ConfigUtils.getDefaultType;
import static org.jage.platform.config.xml.ConfigUtils.getOptionalAttribute;
import static org.jage.platform.config.xml.ConfigUtils.getRequiredAttribute;
import static org.jage.platform.config.xml.ConfigUtils.toBoolean;
import static org.jage.platform.config.xml.ConfigUtils.toClass;

import com.google.common.base.Optional;

/**
 * Reader for collection definitions. Intended to process {@code <list>} and {@code <set>} tags.
 *
 * @author AGH AgE Team
 */
public class CollectionDefinitionReader implements IDefinitionReader<IComponentDefinition> {

	@SuppressWarnings("rawtypes")
	private final Class<? extends Collection> collectionClass;

	private IDefinitionReader<IArgumentDefinition> argumentReader;

	private IDefinitionReader<IComponentDefinition> instanceReader;

	/**
	 * Creates a {@link CollectionDefinitionReader} using a given collection class.
	 *
	 * @param collectionClass
	 *            the collection class to be used in created definitions
	 */
	public CollectionDefinitionReader(@SuppressWarnings("rawtypes") Class<? extends Collection> collectionClass) {
		this.collectionClass = collectionClass;
	}

	public void setArgumentReader(IDefinitionReader<IArgumentDefinition> argumentReader) {
		this.argumentReader = argumentReader;
	}

	public void setInstanceReader(IDefinitionReader<IComponentDefinition> instanceReader) {
		this.instanceReader = instanceReader;
	}

	@Override
	public CollectionDefinition read(Element element) throws ConfigurationException {
		String nameAttribute = getRequiredAttribute(element, NAME);
		Optional<String> typeAttribute = getOptionalAttribute(element, TYPE);
		Optional<String> isSingletonAttribute = getOptionalAttribute(element, IS_SINGLETON);

		CollectionDefinition definition = new CollectionDefinition(nameAttribute, collectionClass,
		        toClass(typeAttribute.or(getDefaultType())),
		        toBoolean(isSingletonAttribute.or(getDefaultIsSingleton())));

		for (Element child : getChildrenIncluding(element, REFERENCE, VALUE)) {
			IArgumentDefinition value = argumentReader.read(child);
			definition.addItem(value);
		}

		// FIXME: Backward compatibility. Remove in future ticket
		for (Element child : getChildrenExcluding(element, REFERENCE, VALUE)) {
			IComponentDefinition innerDefinition = instanceReader.read(child);
			definition.addInnerComponentDefinition(innerDefinition);
		}

		return definition;
	}
}
