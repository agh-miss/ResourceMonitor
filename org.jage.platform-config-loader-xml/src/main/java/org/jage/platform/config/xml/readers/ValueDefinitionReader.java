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
 * File: ValueDefinitionReader.java
 * Created: 2012-01-28
 * Author: Krzywicki
 * $Id: ValueDefinitionReader.java 59 2012-02-13 13:10:28Z krzywick $
 */

package org.jage.platform.config.xml.readers;

import org.dom4j.Element;

import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.component.definition.IArgumentDefinition;
import org.jage.platform.component.definition.ValueDefinition;

import static org.jage.platform.config.xml.ConfigAttributes.CLASS;
import static org.jage.platform.config.xml.ConfigAttributes.VALUE;
import static org.jage.platform.config.xml.ConfigUtils.getRequiredAttribute;
import static org.jage.platform.config.xml.ConfigUtils.toClass;

/**
 * Reader for value definitions. Intended to process {@code <value>} tags.
 *
 * @author AGH AgE Team
 */
public class ValueDefinitionReader implements IDefinitionReader<IArgumentDefinition> {

	@Override
    public ValueDefinition read(Element element) throws ConfigurationException {
		String classAttribute = getRequiredAttribute(element, CLASS);
		String valueAttribute = getRequiredAttribute(element, VALUE, true);

		return new ValueDefinition(toClass(classAttribute), valueAttribute);
    }
}
