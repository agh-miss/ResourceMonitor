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
 * File: ArrayDefinitionTest.java
 * Created: 2011-07-13
 * Author: faber
 * $Id: ArrayDefinitionTest.java 59 2012-02-13 13:10:28Z krzywick $
 */

package org.jage.platform.component.definition;

import org.junit.Test;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jage.platform.component.provider.IMutableComponentInstanceProvider;

public class ArrayDefinitionTest {

	@Test
	public void constructorTest() {
		ArrayDefinition definition = new ArrayDefinition("an array", Object.class, true);
		assertEquals("an array", definition.getName());
		assertEquals(Object.class, definition.getType());
		assertTrue(definition.isSingleton());
	}

	@Test
	public void innerDefinitionTest() {
		ArrayDefinition definition = new ArrayDefinition("an array", Object.class, true);
		ArrayDefinition innerDefinition = new ArrayDefinition("inner", Object.class, false);
		definition.addInnerComponentDefinition(innerDefinition);
		assertEquals(1, definition.getInnerComponentDefinitions().size());
		assertEquals(innerDefinition, definition.getInnerComponentDefinitions().get(0));
	}

	@Test
	public void createArrayTest() throws ConfigurationException {
		final Integer a = new Integer(2);
		final String b = "abc";

		IMutableComponentInstanceProvider instanceProvider = createMock(IMutableComponentInstanceProvider.class);

		expect(instanceProvider.getInstance("a")).andReturn(a).once();
		expect(instanceProvider.getInstance("b")).andReturn(b).once();

		replay(instanceProvider);

		ArrayDefinition definition = new ArrayDefinition("an array", Object.class, true);

		definition.addItem(new ValueDefinition(Integer.class, "3"));
		definition.addItem(new ReferenceDefinition("a"));
		definition.addItem(new ReferenceDefinition("b"));
	}

}
