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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.jage.platform.component.definition.ArrayDefinition;
import org.jage.platform.component.definition.CollectionDefinition;
import org.jage.platform.component.definition.ComponentDefinition;
import org.jage.platform.component.definition.IComponentDefinition;
import org.jage.platform.component.definition.MapDefinition;

import static com.google.common.collect.Lists.newArrayList;

/**
 * The default implementation of {@link IConfigurationBuilder}.
 *
 * @author AGH AgE Team
 */
public class ConfigurationBuilder implements IConfigurationBuilder {

	// The configuration being internally built
	private final List<IComponentDefinition> configuration = newArrayList();

	// Builders for components are being cached, to avoid recreating them for each component instance

	private final ComponentBuilder componentBuilder= new ComponentBuilder(this);

	private final CollectionBuilder collectionBuilder = new CollectionBuilder(this);

	private final ArrayBuilder arrayBuilder= new ArrayBuilder(this);

	private final MapBuilder mapBuilder = new MapBuilder(this);

	/**
	 * Creates an empty {@link IConfigurationBuilder}
	 * @return an empty builder
	 */
	public static IConfigurationBuilder Configuration() {
		return new ConfigurationBuilder();
	}

	@Override
    public ComponentBuilder Component(final String name, final Class<?> type, final boolean isSingleton) {
		ComponentDefinition definition = new ComponentDefinition(name, type, isSingleton);
		configuration.add(definition);
		return componentBuilder.building(definition);
	}

	@Override
    public ComponentBuilder Component(final String name, final Class<?> type) {
		return Component(name, type, false);
	}

	@Override
    public ComponentBuilder Agent(final String name, final Class<?> type) {
		return Component(name, type, false);
	}

	@Override
    public ComponentBuilder Strategy(final String name, final Class<?> type) {
		return Component(name, type, true);
	}

	@Override
    public CollectionBuilder Set(final String name) {
		return Set(name, Object.class);
	}

	@Override
    public CollectionBuilder Set(final String name, final Class<?> elementType) {
		return Set(name, elementType, false);
	}

	@Override
    public CollectionBuilder Set(final String name, final Class<?> elementType, final boolean isSingleton) {
		CollectionDefinition definition = new CollectionDefinition(name, HashSet.class, elementType, isSingleton);
		configuration.add(definition);
		return collectionBuilder.building(definition);
	}

	@Override
    public CollectionBuilder List(final String name) {
		return List(name, Object.class);
	}

	@Override
    public CollectionBuilder List(final String name, final Class<?> elementType) {
		return List(name, elementType, false);
	}

	@Override
    public CollectionBuilder List(final String name, final Class<?> elementType, final boolean isSingleton) {
		CollectionDefinition definition = new CollectionDefinition(name, ArrayList.class, elementType, isSingleton);
		configuration.add(definition);
		return collectionBuilder.building(definition);
	}

	@Override
    public ArrayBuilder Array(final String name, final Class<?> elementType) {
		return Array(name, elementType, false);
	}

	@Override
    public ArrayBuilder Array(final String name, final Class<?> elementType, final boolean isSingleton) {
		checkPrimitive(elementType);
		ArrayDefinition definition = new ArrayDefinition(name, Array.newInstance(elementType, 0).getClass(), isSingleton);
		configuration.add(definition);
		return arrayBuilder.building(definition);
	}

	@Override
    public MapBuilder Map(final String name) {
		return Map(name, Object.class, Object.class);
	}

	@Override
    public MapBuilder Map(final String name, final Class<?> keyType, final Class<?> valueType) {
		return Map(name, keyType, valueType, false);
	}

	@Override
    public MapBuilder Map(final String name, final Class<?> keyType, final Class<?> valueType, final boolean isSingleton) {
		MapDefinition definition = new MapDefinition(name, HashMap.class, keyType, valueType, isSingleton);
		configuration.add(definition);
		return mapBuilder.building(definition);
	}

	@Override
    public List<IComponentDefinition> build() {
		return configuration;
	}

	private void checkPrimitive(final Class<?> type) throws IllegalArgumentException{
		if(type.isPrimitive()) {
			throw new IllegalArgumentException("The type argument cannot be a primitive");
		}
    }
}
