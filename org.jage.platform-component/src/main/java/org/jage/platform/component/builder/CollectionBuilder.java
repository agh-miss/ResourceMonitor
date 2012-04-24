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
import org.jage.platform.component.definition.CollectionDefinition;
import org.jage.platform.component.definition.ReferenceDefinition;

/**
 * This Builder adds several methods specific to Collection Components.
 *
 * @author AGH AgE Team
 */
public final class CollectionBuilder extends AbstractBuilder {

	private CollectionDefinition definition;

	CollectionBuilder(ConfigurationBuilder parent) {
		super(parent);
	}

	@Override
	protected AbstractComponentDefinition getCurrentDefinition() {
		return definition;
	}

	CollectionBuilder building(CollectionDefinition definition) {
		this.definition = definition;
		return this;
	}

	@Override
	public CollectionBuilder withConstructorArg(Class<?> type, String stringValue) {
		return (CollectionBuilder)super.withConstructorArg(type, stringValue);
	}

	@Override
	public CollectionBuilder withConstructorArgRef(String targetName) {
		return (CollectionBuilder)super.withConstructorArgRef(targetName);
	}

	/**
	 * Adds a value item to the current Collection definition. The item's type will be inferred from the definition.
	 *
	 * @param stringValue
	 *            The item's string value.
	 * @return This builder.
	 */
	public CollectionBuilder withItem(String stringValue) {
		return withItem((Class<?>)definition.getElementsType(), stringValue);
	}

	/**
	 * Adds a value item to the current Collection definition.
	 *
	 * @param type
	 *            The item's type.
	 * @param stringValue
	 *            The item's string value.
	 * @return This builder.
	 */
	public CollectionBuilder withItem(Class<?> type, String stringValue) {
		definition.addItem(value(type, stringValue));
		return this;
	}

	/**
	 * Adds a single reference item to the current Collection definition.
	 *
	 * @param targetName
	 *            The target component's name.
	 * @return This builder.
	 */
	public CollectionBuilder withItemRef(String targetName) {
		definition.addItem(new ReferenceDefinition(targetName));
		return this;
	}
}
