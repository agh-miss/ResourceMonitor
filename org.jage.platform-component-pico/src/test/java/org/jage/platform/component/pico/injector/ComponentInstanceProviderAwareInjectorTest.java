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
 * File: ComponentInstanceProviderAwareInjectorTest.java
 * Created: 2012-03-15
 * Author: Krzywicki
 * $Id: ComponentInstanceProviderAwareInjectorTest.java 172 2012-03-30 16:08:11Z krzywick $
 */

package org.jage.platform.component.pico.injector;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.picocontainer.Injector;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.jage.platform.component.definition.ComponentDefinition;
import org.jage.platform.component.pico.IPicoComponentInstanceProvider;
import org.jage.platform.component.provider.IComponentInstanceProviderAware;
import org.jage.platform.component.provider.IMutableComponentInstanceProviderAware;
import org.jage.platform.component.provider.ISelfAwareComponentInstanceProvider;
import org.jage.platform.component.provider.ISelfAwareComponentInstanceProviderAware;

/**
 * Tests for StatefulComponentInjector.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class ComponentInstanceProviderAwareInjectorTest extends AbstractBaseInjectorTest {

	@Mock
	private IPicoComponentInstanceProvider container;

	@Test(expected = NullPointerException.class)
	public void shouldThrowNPEForNullDefinition() {
		// when
		new StatefulComponentInjector<Object>(null);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void shouldNotSupportCreatingComponents() {
		// given
		final Injector<Object> injector = injectorFor(anyDefinition());

		// when
		injector.getComponentInstance(container, null);
	}

	@Test
	public void shouldDoNothingIfInterfacesNotImplemented() {
		// given
		final Object instance = mock(Object.class);
		final Injector<Object> injector = injectorFor(instance);

		// when
		injector.decorateComponentInstance(container, null, instance);

		// then
		verifyNoMoreInteractions(instance);
	}

	@Test
	public void shouldInjectIComponentInstanceProviderAware() {
		// given
		final IComponentInstanceProviderAware instance = mock(IComponentInstanceProviderAware.class);
		final Injector<IComponentInstanceProviderAware> injector = injectorFor(instance);

		// when
		injector.decorateComponentInstance(container, null, instance);

		// then
		verify(instance).setInstanceProvider(container);
	}

	@Test
	public void shouldInjectIMutableComponentInstanceProviderAware() {
		// given
		final IMutableComponentInstanceProviderAware instance = mock(IMutableComponentInstanceProviderAware.class);
		final Injector<IMutableComponentInstanceProviderAware> injector = injectorFor(instance);

		// when
		injector.decorateComponentInstance(container, null, instance);

		// then
		verify(instance).setMutableComponentInstanceProvider(container);
	}

	@Test
	public void shouldInjectISelfAwareComponentInstanceProviderAware() {
		// given
		final ISelfAwareComponentInstanceProviderAware instance = mock(ISelfAwareComponentInstanceProviderAware.class);
		final ComponentDefinition definition = definitionFor(instance);
		final Injector<ISelfAwareComponentInstanceProviderAware> injector = injectorFor(definition);

		// when
		injector.decorateComponentInstance(container, null, instance);

		// then
		final ArgumentCaptor<ISelfAwareComponentInstanceProvider> argument = ArgumentCaptor.forClass(ISelfAwareComponentInstanceProvider.class);
		verify(instance).setSelfAwareComponentInstanceProvider(argument.capture());
		final ISelfAwareComponentInstanceProvider provider = argument.getValue();
		assertThat(provider.getName(), is(equalTo(definition.getName())));

		// when
		provider.getInstance();
		// then
		verify(container).getInstance(definition.getName());

	}

	@Override
	protected <T> Injector<T> injectorFor(final ComponentDefinition definition) {
		return new ComponentInstanceProviderAwareInjector<T>(definition);
	}
}
