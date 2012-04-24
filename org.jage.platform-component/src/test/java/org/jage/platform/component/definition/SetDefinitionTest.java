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
 * File: SetDefinitionTest.java
 * Created: 2009-04-20
 * Author: pkarolcz
 * $Id: SetDefinitionTest.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.platform.component.definition;

import java.util.HashSet;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the {@link CollectionDefinition} class when used for sets.
 * 
 * @author AGH AgE Team
 */
public class SetDefinitionTest {

	@Test
	public void constructorTest() {
		CollectionDefinition definition = new CollectionDefinition("a set", HashSet.class, false);
		assertEquals("a set", definition.getName());
		assertFalse(definition.isSingleton());
	}

	@Test
	public void innerDefinitionTest() {
		CollectionDefinition definition = new CollectionDefinition("a set", HashSet.class, false);
		CollectionDefinition innerDefinition = new CollectionDefinition("inner set", HashSet.class, true);
		definition.addInnerComponentDefinition(innerDefinition);
		assertEquals(1, definition.getInnerComponentDefinitions().size());
		assertEquals(innerDefinition, definition.getInnerComponentDefinitions().get(0));
	}

}
