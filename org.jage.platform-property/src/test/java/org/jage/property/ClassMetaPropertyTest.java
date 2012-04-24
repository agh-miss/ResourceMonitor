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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.jage.property.testHelpers.ClassWithProperties;
import org.junit.Test;

public class ClassMetaPropertyTest {

	@Test
	public void testGetterSetter() throws Exception {
		ClassWithProperties propertiesObject = new ClassWithProperties();
		Method intPropertyGetter = propertiesObject.getIntPropertyGetter();
		Method intPropertySetter = propertiesObject.getIntPropertySetter();
		PropertyGetter intAnnotation = intPropertyGetter.getAnnotation(PropertyGetter.class);
		GetterSetterMetaProperty metaProperty = new GetterSetterMetaProperty(
		        intAnnotation.propertyName(), intPropertyGetter, intPropertySetter, intAnnotation.isMonitorable());

		assertEquals(propertiesObject.getIntPropertyGetter(), metaProperty
				.getGetter());
		assertEquals(propertiesObject.getIntPropertySetter(), metaProperty
				.getSetter());
	}

	@Test
	public void testIsMonitorable() throws Exception {
		ClassWithProperties propertiesObject = new ClassWithProperties();

		Method intPropertyGetter = propertiesObject.getIntPropertyGetter();
		PropertyGetter intAnnotation = intPropertyGetter.getAnnotation(PropertyGetter.class);
		GetterSetterMetaProperty monitorableMetaProperty = new GetterSetterMetaProperty(
		        intAnnotation.propertyName(), intPropertyGetter, intAnnotation.isMonitorable());

		Method stringPropertyGetter = propertiesObject.getStringPropertyGetter();
		PropertyGetter stringAnnotation = stringPropertyGetter.getAnnotation(PropertyGetter.class);
		GetterSetterMetaProperty notMonitorableMetaProperty = new GetterSetterMetaProperty(
		        stringAnnotation.propertyName(), stringPropertyGetter, stringAnnotation.isMonitorable());

		assertTrue(monitorableMetaProperty.isMonitorable());
		assertFalse(notMonitorableMetaProperty.isMonitorable());
	}

	@Test
	public void testPropertyName() throws Exception {
		ClassWithProperties propertiesObject = new ClassWithProperties();
		Method intPropertyGetter = propertiesObject.getIntPropertyGetter();
		Method intPropertySetter = propertiesObject.getIntPropertySetter();
		PropertyGetter intAnnotation = intPropertyGetter.getAnnotation(PropertyGetter.class);
		GetterSetterMetaProperty intMetaProperty = new GetterSetterMetaProperty(
		        intAnnotation.propertyName(), intPropertyGetter, intPropertySetter, intAnnotation.isMonitorable());

		Method stringPropertyGetter = propertiesObject.getStringPropertyGetter();
		Method stringPropertySetter = propertiesObject.getStringPropertySetter();
		PropertyGetter stringAnnotation = stringPropertyGetter.getAnnotation(PropertyGetter.class);
		GetterSetterMetaProperty stringMetaProperty = new GetterSetterMetaProperty(
		        stringAnnotation.propertyName(), stringPropertyGetter, stringPropertySetter, stringAnnotation.isMonitorable());

		assertEquals("intProperty", intMetaProperty.getName());
		assertEquals("stringProperty", stringMetaProperty.getName());
	}

	@Test
	public void testPropertyClass() throws Exception {
		ClassWithProperties propertiesObject = new ClassWithProperties();
		Method intPropertyGetter = propertiesObject.getIntPropertyGetter();
		PropertyGetter intAnnotation = intPropertyGetter.getAnnotation(PropertyGetter.class);
		GetterSetterMetaProperty metaProperty = new GetterSetterMetaProperty(
		        intAnnotation.propertyName(), intPropertyGetter, intAnnotation.isMonitorable());
		assertEquals(int.class, metaProperty.getPropertyClass());
	}
}
