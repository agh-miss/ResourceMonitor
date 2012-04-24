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
package org.jage.property.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jage.property.xml.testHelpers.AdvancedExampleComponent;
import org.junit.Test;

public class XMLBasedFieldMetaPropertyTest {

	@Test
	public void testGetField() throws Exception {
		AdvancedExampleComponent component = new AdvancedExampleComponent();
		XMLBasedFieldMetaProperty metaProperty = new XMLBasedFieldMetaProperty("floatProperty", component
				.getFloatPropertyField());
		assertEquals(component.getFloatPropertyField(), metaProperty.getPropertyField());
	}

	@Test
	public void testPropertyName() throws Exception {
		AdvancedExampleComponent component = new AdvancedExampleComponent();
		XMLBasedFieldMetaProperty floatMetaProperty = new XMLBasedFieldMetaProperty("floatProperty", component
				.getFloatPropertyField());
		XMLBasedFieldMetaProperty objectMetaProperty = new XMLBasedFieldMetaProperty("objectProperty", component
				.getObjectPropertyField());
		assertEquals("floatProperty", floatMetaProperty.getName());
		assertEquals("objectProperty", objectMetaProperty.getName());
	}

	@Test
	public void testPropertyClass() throws Exception {
		AdvancedExampleComponent component = new AdvancedExampleComponent();
		XMLBasedFieldMetaProperty floatMetaProperty = new XMLBasedFieldMetaProperty("floatProperty", component
				.getFloatPropertyField());
		XMLBasedFieldMetaProperty objectMetaProperty = new XMLBasedFieldMetaProperty("objectProperty", component
				.getObjectPropertyField());
		assertTrue(float.class.equals(floatMetaProperty.getPropertyClass()));
		assertTrue(Object.class.equals(objectMetaProperty.getPropertyClass()));
	}
}
