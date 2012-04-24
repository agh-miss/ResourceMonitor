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
 * File: CollectionInjector.java
 * Created: 2010-09-14
 * Author: Kamil
 * $Id: ArrayInjector.java 172 2012-03-30 16:08:11Z krzywick $
 */

package org.jage.platform.component.pico.injector;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.picocontainer.Parameter;
import org.picocontainer.Parameter.Resolver;
import org.picocontainer.PicoCompositionException;
import org.picocontainer.PicoContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.platform.component.definition.ArrayDefinition;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Injector implementation for arrays.
 *
 * @param <T>
 *            the type of components this injector can create
 *
 * @author AGH AgE Team
 */
public final class ArrayInjector<T> extends AbstractInjector<T> {

	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(ArrayInjector.class);

	private final ArrayDefinition definition;

	private final List<Parameter> parameters;

	/**
	 * Creates an ArrayInjector using a given array definition.
	 *
	 * @param definition
	 *            the array definition to use
	 */
	public ArrayInjector(final ArrayDefinition definition) {
		super(definition);
		this.definition = checkNotNull(definition);
		parameters = Parameters.fromList(definition.getItems());
	}

	@Override
	public T doCreate(final PicoContainer container) throws PicoCompositionException {
		log.trace("Constructing an array from definition {}.", definition);

		final List<Object> values = new ArrayList<Object>();
		for (Parameter parameter : parameters) {
			Object value = doResolve(parameter, container).resolveInstance();
			values.add(checkNotNull(value));
		}

		@SuppressWarnings("unchecked")
		final T instance = (T)values.toArray((Object[])Array.newInstance(definition.getElementsType(), values.size()));
		return instance;
	}

	private Resolver doResolve(final Parameter parameter, final PicoContainer container) {
		return parameter.resolve(container, this, null, definition.getElementsType(), null, false, null);
	}

	@Override
	public void doVerify(final PicoContainer container) throws PicoCompositionException {
		log.trace("Verifying ArrayInjector for definition {}.", definition);
		for (Parameter parameter : parameters) {
			parameter.verify(container, this, definition.getElementsType(), null, false, null);
		}
	}

	@Override
	public String getDescriptor() {
		return "Array injector";
	}
}
