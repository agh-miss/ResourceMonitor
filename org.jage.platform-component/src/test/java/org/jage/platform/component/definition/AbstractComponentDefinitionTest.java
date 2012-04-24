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
 * File: AbstractComponentDefinitionTest.java
 * Created: 2011-07-29
 * Author: faber
 * $Id: AbstractComponentDefinitionTest.java 59 2012-02-13 13:10:28Z krzywick $
 */

package org.jage.platform.component.definition;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Tests for the {@link AbstractComponentDefinition} class.
 * 
 * @author AGH AgE Team
 */
@SuppressWarnings("serial")
public class AbstractComponentDefinitionTest {

	private static final String COMPONENT_NAME = "component name";

	/**
	 * Tests the short constructor.
	 */
	@Test
	public void testAbstractComponentDefinitionConstructor1() {
		AbstractComponentDefinition definition = new AbstractComponentDefinition(COMPONENT_NAME, String.class, true) {
			// Empty
		};
		assertEquals(COMPONENT_NAME, definition.getName());
		assertEquals(String.class, definition.getType());
		assertTrue(definition.isSingleton());
		assertNotNull(definition.getTypeParameters());
		assertTrue(definition.getTypeParameters().isEmpty());
	}

	/**
	 * Tests the long constructor.
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testAbstractComponentDefinitionConstructor2() {
		List<Type> typeParameters = new ArrayList<Type>();
		typeParameters.add(String.class);
		typeParameters.add(mock(Type.class));
		AbstractComponentDefinition definition = new AbstractComponentDefinition(COMPONENT_NAME, String.class,
		        typeParameters, true) {
			// Empty
		};
		assertEquals(COMPONENT_NAME, definition.getName());
		assertEquals(String.class, definition.getType());
		assertTrue(definition.isSingleton());
		assertEquals(typeParameters, definition.getTypeParameters());
		definition.getTypeParameters().add(Integer.class); // Should throw
	}

	/**
	 * Tests constructor arguments operations.
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testConstructorArguments() {
		AbstractComponentDefinition definition = new AbstractComponentDefinition(COMPONENT_NAME, String.class, true) {
			// Empty
		};

		assertTrue(definition.getConstructorArguments().isEmpty());

		IArgumentDefinition singleValueProvider = mock(IArgumentDefinition.class);
		definition.addConstructorArgument(singleValueProvider);

		assertEquals(1, definition.getConstructorArguments().size());
		assertTrue(definition.getConstructorArguments().contains(singleValueProvider));
		definition.getConstructorArguments().remove(singleValueProvider); // Should throw
	}

	/**
	 * Tests inner components operations.
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testInnerComponentDefinitions() {
		AbstractComponentDefinition definition = new AbstractComponentDefinition(COMPONENT_NAME, String.class, true) {
			// Empty
		};

		assertTrue(definition.getInnerComponentDefinitions().isEmpty());

		IComponentDefinition innerComponentDefinition = mock(IComponentDefinition.class);
		definition.addInnerComponentDefinition(innerComponentDefinition);

		assertEquals(1, definition.getInnerComponentDefinitions().size());
		assertTrue(definition.getInnerComponentDefinitions().contains(innerComponentDefinition));

		assertTrue(definition.removeInnerComponentDefinition(innerComponentDefinition));

		assertTrue(definition.getInnerComponentDefinitions().isEmpty());

		definition.getInnerComponentDefinitions().add(innerComponentDefinition); // Should throw
	}

}
