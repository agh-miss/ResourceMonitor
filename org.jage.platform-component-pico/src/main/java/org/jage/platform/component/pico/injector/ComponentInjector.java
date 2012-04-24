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
 * File: ComponentInjector.java
 * Created: 2012-03-01
 * Author: Krzywicki
 * $Id: ComponentInjector.java 172 2012-03-30 16:08:11Z krzywick $
 */

package org.jage.platform.component.pico.injector;

import java.util.List;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.PicoCompositionException;
import org.picocontainer.PicoContainer;

import org.jage.platform.component.definition.ComponentDefinition;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This injector is responsible for the lifecycle of component creation and injection.
 * <p>
 * It will first create an instance using constructor injection.
 * <p>
 * Then, it will use setter injection, annotated field and metod injection, and finally some interface injection.
 *
 * @param <T>
 *            the type of components this injector can create
 *
 * @author AGH AgE Team
 */
public final class ComponentInjector<T> extends AbstractInjector<T> {

	private static final long serialVersionUID = 1L;

	private final ComponentAdapter<T> instantiator;

	private final List<Injector<T>> injectors;

	/**
	 * Creates an ComponentInjector using a given component definition.
	 *
	 * @param definition
	 *            the component definition to use
	 */
	public ComponentInjector(final ComponentDefinition definition, final ComponentAdapter<T> instantiator,
	        final List<Injector<T>> injectors) {
		super(checkNotNull(definition));
		this.instantiator = checkNotNull(instantiator);
		this.injectors = checkNotNull(injectors);
	}

	@Override
	public T doCreate(final PicoContainer container) throws PicoCompositionException {
		final T instance = instantiator.getComponentInstance(container, ComponentAdapter.NOTHING.class);
		for (final Injector<T> injector : injectors) {
			injector.decorateComponentInstance(container, ComponentAdapter.NOTHING.class, instance);
		}
		return instance;
	}

	@Override
	protected void doVerify(final PicoContainer container) {
		instantiator.verify(container);
		for (Injector<T> injector : injectors) {
			injector.verify(container);
		}
	}

	@Override
	protected PicoCompositionException rewrittenException(final String message, final PicoCompositionException e) {
		return e;
	}

	@Override
	public String getDescriptor() {
		return "Component Injector";
	}
}
