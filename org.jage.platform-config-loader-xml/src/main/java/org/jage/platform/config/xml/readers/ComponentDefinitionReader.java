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
 * File: ComponentDefinitionReader.java
 * Created: 2012-01-28
 * Author: Krzywicki
 * $Id: ComponentDefinitionReader.java 59 2012-02-13 13:10:28Z krzywick $
 */

package org.jage.platform.config.xml.readers;

import org.dom4j.Element;

import org.jage.platform.component.definition.ComponentDefinition;
import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.component.definition.IComponentDefinition;
import org.jage.platform.component.definition.IArgumentDefinition;
import org.jage.platform.config.xml.ConfigAttributes;

import static org.jage.platform.config.xml.ConfigAttributes.CLASS;
import static org.jage.platform.config.xml.ConfigAttributes.IS_SINGLETON;
import static org.jage.platform.config.xml.ConfigAttributes.NAME;
import static org.jage.platform.config.xml.ConfigTags.CONSTRUCTOR_ARG;
import static org.jage.platform.config.xml.ConfigTags.PROPERTY;
import static org.jage.platform.config.xml.ConfigUtils.getChild;
import static org.jage.platform.config.xml.ConfigUtils.getChildrenExcluding;
import static org.jage.platform.config.xml.ConfigUtils.getChildrenIncluding;
import static org.jage.platform.config.xml.ConfigUtils.getRequiredAttribute;
import static org.jage.platform.config.xml.ConfigUtils.toBoolean;
import static org.jage.platform.config.xml.ConfigUtils.toClass;

/**
 * Reader for component definitions. Intended to process {@code <component>} tags.
 *
 * @author AGH AgE Team
 */
public class ComponentDefinitionReader implements IDefinitionReader<IComponentDefinition> {

	private IDefinitionReader<IArgumentDefinition> argumentReader;

	private IDefinitionReader<IComponentDefinition> instanceReader;

	public void setArgumentReader(IDefinitionReader<IArgumentDefinition> argumentReader) {
    	this.argumentReader = argumentReader;
    }

	public void setInstanceReader(IDefinitionReader<IComponentDefinition> instanceReader) {
    	this.instanceReader = instanceReader;
    }

	@Override
	public ComponentDefinition read(Element element) throws ConfigurationException {
		String nameAttribute = getRequiredAttribute(element, NAME);
		String classAttribute = getRequiredAttribute(element, CLASS);
		String isSingletonAttribute = getRequiredAttribute(element, IS_SINGLETON);

		ComponentDefinition definition = new ComponentDefinition(nameAttribute, toClass(classAttribute),
		        toBoolean(isSingletonAttribute));

		for (Element constructorArg : getChildrenIncluding(element, CONSTRUCTOR_ARG)) {
			IArgumentDefinition argument = argumentReader.read(getChild(constructorArg));
			definition.addConstructorArgument(argument);
		}

		for (Element propertyArg : getChildrenIncluding(element, PROPERTY)) {
			String propertyNameAttribute = getRequiredAttribute(propertyArg, ConfigAttributes.NAME);
			IArgumentDefinition argument = argumentReader.read(getChild(propertyArg));
			definition.addPropertyArgument(propertyNameAttribute, argument);
		}

		for (Element innerElement : getChildrenExcluding(element, CONSTRUCTOR_ARG, PROPERTY)) {
			IComponentDefinition innerDefinition = instanceReader.read(innerElement);
			definition.addInnerComponentDefinition(innerDefinition);
		}

		return definition;
	}
}
