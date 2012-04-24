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
package org.jage.property;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.jage.property.functions.FunctionsTestPropertyContainer;
import org.junit.Before;
import org.junit.Test;

public class PropertiesPathParserTest {
	private FunctionsTestPropertyContainer _root;

	@Before
	public void setUp() throws Exception {
		_root = new FunctionsTestPropertyContainer();

		FunctionsTestPropertyContainer property11 = new FunctionsTestPropertyContainer();
		FunctionsTestPropertyContainer property12a = new FunctionsTestPropertyContainer();
		FunctionsTestPropertyContainer property12b = new FunctionsTestPropertyContainer();

		_root.addProperty("first", property11);
		_root.addProperty("second", new IPropertyContainer[] { property12a,
				property12b });

		property11.addProperty("a", 1);
		property11.addProperty("aa", "propertyAA");
		property12a.addProperty("b", 2);
		property12b.addProperty("c", "c");
	}

	@Test
	public void testGetPropertyForPath() throws Exception {
		PropertyPathParser parser = new PropertyPathParser(_root);

		Property property11a = parser.getPropertyForPath("first.a");
		Property property11aa = parser.getPropertyForPath("first.aa");
		Property property12ab = parser.getPropertyForPath("second[0].b");
		Property property12ac = parser.getPropertyForPath("second[1].c");

		assertEquals("a", property11a.getMetaProperty().getName());
		assertEquals(1, property11a.getValue());
		assertEquals("aa", property11aa.getMetaProperty().getName());
		assertEquals("propertyAA", property11aa.getValue());
		assertEquals("b", property12ab.getMetaProperty().getName());
		assertEquals(2, property12ab.getValue());
		assertEquals("c", property12ac.getMetaProperty().getName());
		assertEquals("c", property12ac.getValue());
	}

	@Test
	public void testGetPropertiesOnPath() throws Exception {
		PropertyPathParser parser = new PropertyPathParser(_root);

		List<Property> properties11a = parser.getPropertiesOnPath("first.a");
		assertEquals(2, properties11a.size());
		assertEquals("first", properties11a.get(0).getMetaProperty().getName());
		assertEquals("a", properties11a.get(1).getMetaProperty().getName());

		List<Property> properties12ab = parser
				.getPropertiesOnPath("second[0].b");
		assertEquals(2, properties12ab.size());
		assertEquals("second", properties12ab.get(0).getMetaProperty()
				.getName());
		assertEquals("b", properties12ab.get(1).getMetaProperty().getName());
	}

	@Test
	public void testInvalidPropertyPath() {
		try {
			PropertyPathParser parser = new PropertyPathParser(_root);
			parser.getPropertyForPath("notExisintPath");
			fail();
		} catch (InvalidPropertyPathException ex) {
		}
	}
}
