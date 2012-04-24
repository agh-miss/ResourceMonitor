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
 * File: ListDefinitionTest.java
 * Created: 2009-04-20
 * Author: pkarolcz
 * $Id: ListDefinitionTest.java 59 2012-02-13 13:10:28Z krzywick $
 */

package org.jage.platform.component.definition;

import java.util.LinkedList;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link CollectionDefinition} class when used for lists.
 * 
 * @author AGH AgE Team
 */
public class ListDefinitionTest {

	/**
	 * Tests the constructor correctness.
	 */
	@Test
	public void constructorTest() {
		CollectionDefinition definition = new CollectionDefinition("a list", LinkedList.class, true);
		assertEquals("a list", definition.getName());
		assertTrue(definition.isSingleton());
		assertEquals(Object.class, definition.getElementsType());
	}

	@Test
	public void innerDefinitionTest() {
		CollectionDefinition definition = new CollectionDefinition("a list", LinkedList.class, true);
		CollectionDefinition innerDefinition = new CollectionDefinition("inner", LinkedList.class, false);
		definition.addInnerComponentDefinition(innerDefinition);
		assertEquals(1, definition.getInnerComponentDefinitions().size());
		assertEquals(innerDefinition, definition.getInnerComponentDefinitions().get(0));
	}

}
