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
 * File: ComponentInstanceProviderAwareInjector.java
 * Created: 2010-09-14
 * Author: Kamil
 * $Id: ComponentInstanceProviderAwareInjector.java 172 2012-03-30 16:08:11Z krzywick $
 */

package org.jage.platform.component.pico.injector;

import org.picocontainer.PicoCompositionException;
import org.picocontainer.PicoContainer;

import org.jage.platform.component.definition.ComponentDefinition;
import org.jage.platform.component.pico.IPicoComponentInstanceProvider;
import org.jage.platform.component.provider.IComponentInstanceProviderAware;
import org.jage.platform.component.provider.IMutableComponentInstanceProviderAware;
import org.jage.platform.component.provider.ISelfAwareComponentInstanceProviderAware;
import org.jage.platform.component.provider.SelfAwareComponentInstanceProvider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class provides an injector that is able to inject an instance provider into ComponentInstanceProvider-aware
 * components.
 *
 * @param <T>
 *            the type of components this injector can inject into
 *
 * @author AGH AgE Team
 */
public final class ComponentInstanceProviderAwareInjector<T> extends AbstractInjector<T> {

	private static final long serialVersionUID = 1L;

	private final ComponentDefinition definition;

	/**
	 * Creates an ComponentInstanceProviderAwareInjector using a given component definition.
	 *
	 * @param definition
	 *            the component definition to use
	 */
	public ComponentInstanceProviderAwareInjector(final ComponentDefinition definition) {
		super(definition);
		this.definition = checkNotNull(definition);
	}

	@Override
	public void doInject(final PicoContainer container, final T instance) throws PicoCompositionException {
		final IPicoComponentInstanceProvider provider = (IPicoComponentInstanceProvider)container;

		if (instance instanceof IComponentInstanceProviderAware) {
			((IComponentInstanceProviderAware)instance).setInstanceProvider(provider);
		}
		if (instance instanceof IMutableComponentInstanceProviderAware) {
			((IMutableComponentInstanceProviderAware)instance).setMutableComponentInstanceProvider(provider);
		}
		if (instance instanceof ISelfAwareComponentInstanceProviderAware) {
			((ISelfAwareComponentInstanceProviderAware)instance)
			        .setSelfAwareComponentInstanceProvider(new SelfAwareComponentInstanceProvider(provider, definition));
		}
	}

	@Override
	protected void doVerify(final PicoContainer container) {
		if (!(container instanceof IPicoComponentInstanceProvider)) {
			throw new PicoCompositionException("The container needs to implement IPicoComponentInstanceProvider");
		}
	}

	@Override
	public String getDescriptor() {
		return "ComponentInstanceProviderAware Injector";
	}
}
