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
 * File: MapDefinitionTest.java
 * Created: 2009-04-20
 * Author: pkarolcz
 * $Id: MapDefinitionTest.java 59 2012-02-13 13:10:28Z krzywick $
 */

package org.jage.platform.component.definition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.TreeMap;

import org.junit.Test;

/**
 * Tests for the {@link MapDefinition} class.
 *
 * @author AGH AgE Team
 */
public class MapDefinitionTest {

	private static final String MAP_NAME = "map name";

	/**
	 * Tests the short constructor.
	 */
	@Test
	public void testCollectionDefinitionConstructor1() {
		MapDefinition definition = new MapDefinition(MAP_NAME, true);

		assertEquals(MAP_NAME, definition.getName());
		assertEquals(HashMap.class, definition.getType());
		assertTrue(definition.isSingleton());
		assertEquals(2, definition.getTypeParameters().size());
		assertEquals(Object.class, definition.getElementsKeyType());
		assertEquals(Object.class, definition.getElementsValueType());
	}

	/**
	 * Tests the long constructor.
	 */
	@Test
	public void testCollectionDefinitionConstructor2() {
		MapDefinition definition = new MapDefinition(MAP_NAME, TreeMap.class, String.class, Integer.class, true);

		assertEquals(MAP_NAME, definition.getName());
		assertEquals(TreeMap.class, definition.getType());
		assertTrue(definition.isSingleton());
		assertEquals(2, definition.getTypeParameters().size());
		assertEquals(String.class, definition.getElementsKeyType());
		assertEquals(Integer.class, definition.getElementsValueType());
	}

	@Test
	public void innerDefinitionTest() {
		MapDefinition definition = new MapDefinition("a map", false);
		MapDefinition innerDefinition = new MapDefinition("inner map", true);
		definition.addInnerComponentDefinition(innerDefinition);

		assertEquals(1, definition.getInnerComponentDefinitions().size());
		assertSame(innerDefinition, definition.getInnerComponentDefinitions().get(0));
	}
}

