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
package org.jage.platform.component.builder;

import org.jage.platform.component.definition.AbstractComponentDefinition;
import org.jage.platform.component.definition.ComponentDefinition;

public final class ComponentBuilder extends AbstractBuilder {

	private ComponentDefinition definition;

	ComponentBuilder(ConfigurationBuilder parent) {
		super(parent);
	}

	@Override
	protected AbstractComponentDefinition getCurrentDefinition() {
		return definition;
	}

	ComponentBuilder building(ComponentDefinition definition) {
		this.definition = definition;
		return this;
	}

	@Override
	public ComponentBuilder withConstructorArg(Class<?> type, String stringValue) {
		return (ComponentBuilder)super.withConstructorArg(type, stringValue);
	}

	@Override
	public ComponentBuilder withConstructorArgRef(String targetName) {
		return (ComponentBuilder)super.withConstructorArgRef(targetName);
	}

	/**
	 * Specifies a value property for the given Component definition.
	 *
	 * @param propertyName
	 *            The property name.
	 * @param type
	 *            The property type.
	 * @param stringValue
	 *            The property string value.
	 * @return This builder.
	 */
	public ComponentBuilder withProperty(String propertyName, Class<?> type, String stringValue) {
		definition.addPropertyArgument(propertyName, value(type, stringValue));
		return this;
	}

	/**
	 * Specifies a reference property for the given Component definition.
	 *
	 * @param propertyName
	 *            The property name.
	 * @param targetName
	 *            The target component name.
	 * @return This builder.
	 */
	public ComponentBuilder withPropertyRef(String propertyName, String targetName) {
		definition.addPropertyArgument(propertyName, reference(targetName));
		return this;
	}
}
