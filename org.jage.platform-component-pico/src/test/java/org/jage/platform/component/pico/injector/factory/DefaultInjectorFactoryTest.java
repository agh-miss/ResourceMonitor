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
 * File: DefaultInjectorFactoryTest.java
 * Created: 2012-03-15
 * Author: Krzywicki
 * $Id: DefaultInjectorFactoryTest.java 113 2012-03-15 12:47:00Z krzywick $
 */

package org.jage.platform.component.pico.injector.factory;

import java.util.HashMap;
import java.util.Set;

import org.junit.Test;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.behaviors.Cached;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.jage.platform.component.definition.ArrayDefinition;
import org.jage.platform.component.definition.CollectionDefinition;
import org.jage.platform.component.definition.ComponentDefinition;
import org.jage.platform.component.definition.IComponentDefinition;
import org.jage.platform.component.definition.MapDefinition;
import org.jage.platform.component.pico.injector.ArrayInjector;
import org.jage.platform.component.pico.injector.CollectionInjector;
import org.jage.platform.component.pico.injector.ComponentInjector;
import org.jage.platform.component.pico.injector.MapInjector;

/**
 * Tests for DefaultInjectorFactory.
 *
 * @author AGH AgE Team
 */
public class DefaultInjectorFactoryTest {

    private static final String ANY = "any";

	private final DefaultInjectorFactory factory = new DefaultInjectorFactory();

	@Test(expected = NullPointerException.class)
	public void shouldThrowNPEForNullDefinition() {
		// when
		factory.createAdapter(null);
	}

	@Test
	public void shouldRecogniseArrayDefinitions() {
		// given
		final ArrayDefinition definition = new ArrayDefinition(ANY, Object.class, false);

		// when
		final ComponentAdapter<Object> adapter = factory.createAdapter(definition);

		// then
		assertThat(adapter, is(ArrayInjector.class));
	}

	@Test
	public void shouldRecogniseCollectionDefinitions() {
		// given
		final CollectionDefinition definition = new CollectionDefinition(ANY, Set.class, Object.class, false);

		// when
		final ComponentAdapter<Object> adapter = factory.createAdapter(definition);

		// then
		assertThat(adapter, is(CollectionInjector.class));
	}

	@Test
	public void shouldRecogniseMapDefinitions() {
		// given
		final MapDefinition definition = new MapDefinition(ANY, HashMap.class, Object.class, Object.class, false);

		// when
		final ComponentAdapter<Object> adapter = factory.createAdapter(definition);

		// then
		assertThat(adapter, is(MapInjector.class));
	}

	@Test
	public void shouldRecogniseComponentDefinitions() {
		// given
		final ComponentDefinition definition = new ComponentDefinition(ANY, Object.class, false);

		// when
		final ComponentAdapter<Object> adapter = factory.createAdapter(definition);

		// then
		assertThat(adapter, is(ComponentInjector.class));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionOnUnercognisedDefinition() {
		// given
		final IComponentDefinition definition = mock(IComponentDefinition.class);

		// when
		factory.createAdapter(definition);
	}

	@Test
	public void shouldWrapSingletonsInCachedAdapter() {
		// given
		final ComponentDefinition definition = new ComponentDefinition(ANY, Object.class, true);

		// when
		final ComponentAdapter<Object> adapter = factory.createAdapter(definition);

		// then
		assertThat(adapter, is(Cached.class));
		assertThat(adapter.getDelegate(), is(ComponentInjector.class));
	}
}
