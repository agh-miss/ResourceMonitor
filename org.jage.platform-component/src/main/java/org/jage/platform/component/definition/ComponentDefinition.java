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
 * File: ComponentDefinition.java
 * Created: 2008-10-24
 * Author: kpietak, awos
 * $Id: ComponentDefinition.java 59 2012-02-13 13:10:28Z krzywick $
 */

package org.jage.platform.component.definition;

import java.util.Map;

import static java.util.Collections.unmodifiableMap;

import com.google.common.base.Preconditions;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Maps.newLinkedHashMap;

/**
 * Default implementation of {@link IComponentDefinition} which describes a custom component.
 * <p>
 * It can be given arguments to its properties.
 *
 * @author AGH AgE Team
 */
public class ComponentDefinition extends AbstractComponentDefinition {

	private static final long serialVersionUID = 1L;

	private final Map<String, IArgumentDefinition> propertyArguments = newLinkedHashMap();

	/**
	 * Creates a new component definition with a given name and of a given type.
	 *
	 * @param name
	 *            the name of the component
	 * @param type
	 *            the array type of the component
	 * @param isSingleton
	 *            whether the component has a singleton scope
	 */
	public ComponentDefinition(final String name, final Class<?> type, final boolean isSingleton) {
		super(name, type, isSingleton);
	}

	/**
	 * Adds a new property argument to this definition.
	 *
	 * @param propertyName
	 *            the name of the property
	 * @param argument
	 *            the argument for that property
	 * @throws IllegalArgumentException
	 *             if an argument was already defined for a property with the given name
	 */
	public void addPropertyArgument(String propertyName, IArgumentDefinition argument) {
		Preconditions.checkArgument(!propertyArguments.containsKey(propertyName),
		        "This definition already contains an argument for property %s", propertyName);
		propertyArguments.put(checkNotNull(propertyName), checkNotNull(argument));
	}

	public Map<String, IArgumentDefinition> getPropertyArguments() {
		return unmodifiableMap(propertyArguments);
	}
}
