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
 * File: IComponentDescriptorReader.java
 * Created: 2010-03-08
 * Author: leszko, kpietak
 * $Id: IComponentDescriptorReader.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.platform.component.descriptor;

import org.jage.platform.component.definition.ConfigurationException;

/**
 * An interface representing reader which is responsible for creating {@link IComponentDescriptor} based on given
 * source.
 *
 * @author AGH AgE Team
 *
 */
public interface IComponentDescriptorReader {

	/**
	 * Analyzes a given source and than creates its component descriptor. Source type is dependent on concrete
	 * implementation of the reader (for instance, source can be a Class object).
	 *
	 * @param source
	 *            source object which will be analyzed to create a component descriptor
	 * @throws ConfigurationException
	 *             when any error occurs during reading the source
	 * @throws IllegalArgumentException
	 *             when source has wrong type
	 * @return component descriptor
	 */
	IComponentDescriptor readDescritptor(Object source) throws ConfigurationException, IllegalArgumentException;

}
