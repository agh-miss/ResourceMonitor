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
 * File: StatefulComponentInjectorTest.java
 * Created: 2012-03-15
 * Author: Krzywicki
 * $Id: StatefulComponentInjectorTest.java 172 2012-03-30 16:08:11Z krzywick $
 */

package org.jage.platform.component.pico.injector;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.picocontainer.Injector;
import org.picocontainer.PicoCompositionException;

import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.definition.ComponentDefinition;
import org.jage.platform.component.exception.ComponentException;
import org.jage.platform.component.pico.IPicoComponentInstanceProvider;
/**
 * Tests for StatefulComponentInjector.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class StatefulComponentInjectorTest extends AbstractBaseInjectorTest {

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
	public void shouldDoNothingIfNotStatefulComponent() {
		// given
		final Object instance = mock(Object.class);
		final Injector<Object> injector = injectorFor(instance);

		// when
		injector.decorateComponentInstance(container, null, instance);

		// then
		verifyZeroInteractions(instance);
	}

	@Test
	public void shouldCallInitOnStatefulComponent() throws ComponentException {
		// given
		final IStatefulComponent instance = mock(IStatefulComponent.class);
		final Injector<IStatefulComponent> injector = injectorFor(instance);

		// when
		injector.decorateComponentInstance(container, null, instance);

		// then
		verify(instance).init();
	}

	@Test(expected = PicoCompositionException.class)
	public void shouldPropagateStatefulComponentExceptions() throws ComponentException {
		// given
		final IStatefulComponent instance = mock(IStatefulComponent.class);
		final Injector<IStatefulComponent> injector = injectorFor(instance);
		willThrow(ComponentException.class).given(instance).init();

		// when
		injector.decorateComponentInstance(container, null, instance);
	}

	@Override
	protected <T> Injector<T> injectorFor(final ComponentDefinition definition) {
		return new StatefulComponentInjector<T>(definition);
	}
}
