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
 * File: IDefinitionReader.java
 * Created: 2012-01-28
 * Author: Krzywicki
 * $Id: IDefinitionReader.java 16 2012-01-29 14:39:31Z krzywick $
 */

package org.jage.platform.config.xml.readers;

import org.dom4j.Element;

import org.jage.platform.component.definition.ConfigurationException;

/**
 * Basic interface for reading configuration definitions from a DOM element.
 *
 * @param <T>
 *            the type of definition returned
 *
 * @author AGH AgE Team
 */
public interface IDefinitionReader<T> {

	/**
	 * Reads a configuration definition from the given DOM element.
	 *
	 * @param element
	 *            the element to read from
	 * @return a definition
	 * @throws ConfigurationException
	 *             if the element is invalid or some error happens
	 */
	public T read(Element element) throws ConfigurationException;
}
