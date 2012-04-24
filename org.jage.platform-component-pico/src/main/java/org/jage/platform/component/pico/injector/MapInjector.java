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
 * File: MapInjector.java
 * Created: 2010-09-14
 * Author: Kamil
 * $Id: MapInjector.java 172 2012-03-30 16:08:11Z krzywick $
 */

package org.jage.platform.component.pico.injector;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Map.Entry;

import org.picocontainer.Parameter;
import org.picocontainer.Parameter.Resolver;
import org.picocontainer.PicoCompositionException;
import org.picocontainer.PicoContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.platform.component.definition.MapDefinition;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Injector implementation for maps.
 *
 * @param <T>
 *            the type of components this injector can create
 *
 * @author AGH AgE Team
 */
public final class MapInjector<T> extends AbstractInjector<T> {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(MapInjector.class);

	private final MapDefinition definition;

	private final Map<Parameter, Parameter> parameters;

	/**
	 * Creates an MapInjector using a given map definition.
	 *
	 * @param definition
	 *            the map definition to use
	 */
	public MapInjector(final MapDefinition definition) {
		super(definition);
		this.definition = checkNotNull(definition);
		parameters = Parameters.fromMap(definition.getItems());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public T doCreate(final PicoContainer container) throws PicoCompositionException {
		LOG.trace("Constructing a map from definition {}.", definition);

		try {
			final Map instance = (Map)definition.getType().newInstance();
			for (final Entry<Parameter, Parameter> entry : parameters.entrySet()) {
				Object key = doResolveKey(entry, container).resolveInstance();
				Object value = doResolveValue(entry, container).resolveInstance();
				instance.put(checkNotNull(key), checkNotNull(value));
			}
			return (T)instance;
		} catch (final InstantiationException e) {
			throw wrappedException(e);
		} catch (final IllegalAccessException e) {
			throw wrappedException(e);
		}
	}

	private Resolver doResolveKey(final Entry<Parameter, Parameter> e, final PicoContainer container) {
		return doResolve(e.getKey(), container, definition.getElementsKeyType());
	}

	private Resolver doResolveValue(final Entry<Parameter, Parameter> e, final PicoContainer container) {
		return doResolve(e.getValue(), container, definition.getElementsValueType());
	}

	private Resolver doResolve(final Parameter parameter, final PicoContainer container, final Type expectedType) {
		return parameter.resolve(container, this, null, expectedType, null, false, null);
	}

	@Override
	protected void doVerify(final PicoContainer container) {
		LOG.trace("Verifying MapInjector for definition {}.", definition);
		for (Parameter parameter : parameters.keySet()) {
			parameter.verify(container, this, definition.getElementsKeyType(), null, false, null);
		}
		for (Parameter parameter : parameters.values()) {
			parameter.verify(container, this, definition.getElementsValueType(), null, false, null);
		}
	}

	@Override
	public String getDescriptor() {
		return "Collection injector";
	}
}
