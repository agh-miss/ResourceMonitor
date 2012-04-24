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
import org.jage.platform.component.definition.IArgumentDefinition;
import org.jage.platform.component.definition.MapDefinition;
import org.jage.platform.component.definition.ReferenceDefinition;
import org.jage.platform.component.definition.ValueDefinition;

/**
 * This Builder adds several methods specific to Map Components.
 *
 * @author AGH AgE Team
 */
public final class MapBuilder extends AbstractBuilder {

	private MapDefinition definition;

	private MapKey mapKey = new MapKey();

	private MapValue mapValue = new MapValue();

	MapBuilder(ConfigurationBuilder parent) {
		super(parent);
	}

	@Override
	protected AbstractComponentDefinition getCurrentDefinition() {
		return definition;
	}

	MapBuilder building(MapDefinition definition) {
		this.definition = definition;
		return this;
	}

	@Override
	public MapBuilder withConstructorArg(Class<?> type, String stringValue) {
		return (MapBuilder)super.withConstructorArg(type, stringValue);
	}

	@Override
	public MapBuilder withConstructorArgRef(String targetName) {
		return (MapBuilder)super.withConstructorArgRef(targetName);
	}

	/**
	 * Adds an item to the current Map definition.
	 *
	 * @return This builder
	 */
	public MapKey withItem() {
		return mapKey;
	}

	public final class MapKey {

		private IArgumentDefinition key;

		/**
		 * Specifies the key of the item being added.
		 *
		 * @param type
		 *            The key's type.
		 * @param stringValue
		 *            The key's string value.
		 * @return This builder.
		 */
		public MapValue key(Class<?> type, String stringValue) {
			key = new ValueDefinition(type, stringValue);
			return mapValue;
		}

		/**
		 * Specifies the key of the item being added. The key's type will be inferred from the definition.
		 *
		 * @param stringValue
		 *            The key's string value.
		 * @return This builder.
		 */
		public MapValue key(String stringValue) {
			return key((Class<?>)definition.getElementsKeyType(), stringValue);
		}

		/**
		 * Specifies the key of the item being added.
		 *
		 * @param targetName
		 *            The key target component's name.
		 * @return This builder.
		 */
		public MapValue keyRef(String targetName) {
			key = new ReferenceDefinition(targetName);
			return mapValue;
		}
	}

	public final class MapValue {

		/**
		 * Specifies the value of the item being added.
		 *
		 * @param type
		 *            The value's type.
		 * @param stringValue
		 *            The value's string value.
		 * @return This builder.
		 */
		public MapBuilder value(Class<?> type, String stringValue) {
			IArgumentDefinition value = new ValueDefinition(type, stringValue);
			definition.addItem(mapKey.key, value);
			return MapBuilder.this;
		}

		/**
		 * Specifies the value of the item being added. The value's type will be inferred from the definition.
		 *
		 * @param stringValue
		 *            The value's string value.
		 * @return This builder.
		 */
		public MapBuilder value(String stringValue) {
			return value((Class<?>)definition.getElementsValueType(), stringValue);
		}

		/**
		 * Specifies the value of the item being added.
		 *
		 * @param targetName
		 *            The value target component's name.
		 * @return This builder.
		 */
		public MapBuilder valueRef(String targetName) {
			IArgumentDefinition value = new ReferenceDefinition(targetName);
			definition.addItem(mapKey.key, value);
			return MapBuilder.this;
		}
	}
}
