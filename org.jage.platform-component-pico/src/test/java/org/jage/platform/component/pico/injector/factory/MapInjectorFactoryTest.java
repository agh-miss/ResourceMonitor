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
 * File: MapInjectorFactoryTest.java
 * Created: 2012-03-15
 * Author: Krzywicki
 * $Id: MapInjectorFactoryTest.java 113 2012-03-15 12:47:00Z krzywick $
 */

package org.jage.platform.component.pico.injector.factory;

import java.util.HashMap;

import org.junit.Test;
import org.picocontainer.ComponentAdapter;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.jage.platform.component.definition.MapDefinition;
import org.jage.platform.component.pico.injector.MapInjector;

/**
 * Tests for MapInjectorFactory.
 * @author AGH AgE Team
 */
public class MapInjectorFactoryTest {

	private final MapInjectorFactory factory = new MapInjectorFactory();

	@Test(expected = NullPointerException.class)
	public void shouldThrowNPEForNullDefinition() {
		// when
		factory.createAdapter(null);
	}

	@Test
	public void shouldCreateArrayInjectors() {
		// given
		final MapDefinition definition = new MapDefinition("any", HashMap.class, Object.class, Object.class, false);

		// when
		final ComponentAdapter<Object> adapter = factory.createAdapter(definition);

		// then
		assertThat(adapter, is(MapInjector.class));
	}
}
