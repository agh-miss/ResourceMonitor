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

import org.jage.property.xml.testHelpers.AdvancedExampleComponent;
import org.junit.Test;

public class XMLBasedMetaPropertyTest {

	@Test
	public void testGetterSetter() throws Exception {
		AdvancedExampleComponent component = new AdvancedExampleComponent();
		XMLBasedGetterSetterMetaProperty metaProperty = new XMLBasedGetterSetterMetaProperty("_intProperty", int.class,
				component.getIntPropertyGetter(), component.getIntPropertySetter());
		assertEquals(component.getIntPropertyGetter(), metaProperty.getGetter());
		assertEquals(component.getIntPropertySetter(), metaProperty.getSetter());
	}

	/*
	 * XMLGetterSetterProperties are non-monitorable by definition, because it requires special operations in setter
	 * methods of component, which we cannot change.
	 * 
	 * @Test public void testIsMonitorable() throws Exception { ClassWithProperties propertiesObject = new
	 * ClassWithProperties(); ClassGetterSetterMetaProperty monitorableMetaProperty = new ClassGetterSetterMetaProperty(
	 * ClassWithProperties.class, propertiesObject.getIntPropertyGetter(), null); ClassGetterSetterMetaProperty
	 * notMonitorableMetaProperty = new ClassGetterSetterMetaProperty( ClassWithProperties.class,
	 * propertiesObject.getStringPropertyGetter(), null); assertTrue(monitorableMetaProperty.isMonitorable());
	 * assertFalse(notMonitorableMetaProperty.isMonitorable()); }
	 */

	@Test
	public void testPropertyName() throws Exception {
		AdvancedExampleComponent component = new AdvancedExampleComponent();
		XMLBasedGetterSetterMetaProperty intMetaProperty = new XMLBasedGetterSetterMetaProperty("_intProperty",
				int.class, component.getIntPropertyGetter(), component.getIntPropertySetter());
		XMLBasedGetterSetterMetaProperty stringMetaProperty = new XMLBasedGetterSetterMetaProperty("_stringProperty",
				int.class, component.getStringPropertyGetter(), component.getStringPropertySetter());
		assertEquals("_intProperty", intMetaProperty.getName());
		assertEquals("_stringProperty", stringMetaProperty.getName());
	}

	@Test
	public void testPropertyClass() throws Exception {
		AdvancedExampleComponent component = new AdvancedExampleComponent();
		XMLBasedGetterSetterMetaProperty metaProperty = new XMLBasedGetterSetterMetaProperty("_intProperty", int.class,
				component.getIntPropertyGetter(), component.getIntPropertySetter());
		assertEquals(int.class, metaProperty.getPropertyClass());
	}
}
