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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.jage.property.testHelpers.ChangesNotifierStub;
import org.jage.property.testHelpers.ClassWithProperties;
import org.jage.property.testHelpers.InnerClassWithProperties;
import org.junit.Test;

public class ClassAnnotatedPropertiesFactoryTest {

	@Test
	public void testAllGetterSetterProperties()
			throws InvalidPropertyDefinitionException {
		ClassAnnotatedPropertiesFactory factory = ClassAnnotatedPropertiesFactory.INSTANCE;
		ClassWithProperties propertiesObject = new ClassWithProperties();
		PropertiesSet set = factory.getAllProperties(propertiesObject);

		Property intProperty = set.getProperty("intProperty");
		Property stringProperty = set.getProperty("stringProperty");
		Property notExistingProperty = set.getProperty("notExisting");

		assertEquals(int.class, intProperty.getMetaProperty()
				.getPropertyClass());
		assertEquals(String.class, stringProperty.getMetaProperty()
				.getPropertyClass());
		assertNull(notExistingProperty);
	}

	@Test
	public void testAllFieldProperties() throws Exception {
		ClassAnnotatedPropertiesFactory factory = ClassAnnotatedPropertiesFactory.INSTANCE;
		ClassWithProperties propertiesObject = new ClassWithProperties();
		PropertiesSet set = factory.getAllProperties(propertiesObject);

		Property floatProperty = set.getProperty("floatProperty");
		Property objectProperty = set.getProperty("objectProperty");

		assertEquals(float.class, floatProperty.getMetaProperty()
				.getPropertyClass());
		assertEquals(Object.class, objectProperty.getMetaProperty()
				.getPropertyClass());
	}

	@Test
	public void testInvalidFieldPropertyDefinition() {
		try {
			ClassAnnotatedPropertiesFactory factory = ClassAnnotatedPropertiesFactory.INSTANCE;
			ClassWithInvalidProperties1 propertiesObject = new ClassWithInvalidProperties1();
			factory.getAllProperties(propertiesObject);
			fail();
		} catch (InvalidPropertyDefinitionException ex) {
		}
	}

	@Test
	public void testInvalidGetterPropertyDefinition() {
		try {
			ClassAnnotatedPropertiesFactory factory = ClassAnnotatedPropertiesFactory.INSTANCE;
			ClassWithInvalidProperties2 propertiesObject = new ClassWithInvalidProperties2();
			factory.getAllProperties(propertiesObject);
			fail();
		} catch (InvalidPropertyDefinitionException ex) {
		}
	}

	@Test
	public void testGetMetaProperties() throws Exception {
		ClassAnnotatedPropertiesFactory factory = ClassAnnotatedPropertiesFactory.INSTANCE;
		ClassWithProperties propertiesObject = new ClassWithProperties();

		MetaPropertiesSet set = factory.getAllMetaProperties(propertiesObject);
		assertEquals(10, set.size());
		assertEquals(InnerClassWithProperties.class, set.getMetaProperty(
				"complexProperty").getPropertyClass());
		assertEquals(InnerClassWithProperties[].class, set.getMetaProperty(
				"complexArrayProperty").getPropertyClass());
		assertEquals(ChangesNotifierStub.class, set.getMetaProperty(
				"changesNotifierProperty").getPropertyClass());
		assertEquals(float.class, set.getMetaProperty("floatProperty")
				.getPropertyClass());
		assertEquals(Object.class, set.getMetaProperty("objectProperty")
				.getPropertyClass());
		assertEquals(ChangesNotifierStub.class, set.getMetaProperty(
				"changesNotifierProperty2").getPropertyClass());
		assertEquals(String.class, set.getMetaProperty("stringProperty")
				.getPropertyClass());
		assertEquals(Object.class, set.getMetaProperty(
				"monitorableObjectProperty").getPropertyClass());
		assertEquals(String.class, set.getMetaProperty(
				"monitorableStringProperty").getPropertyClass());

	}

	private class ClassWithInvalidProperties1 {

		@SuppressWarnings("unused")
		@PropertyField(propertyName = "invalid.")
		private int _invalidPropertyName = 1;
	}

	private class ClassWithInvalidProperties2 {
		@PropertyGetter(propertyName = "a[]")
		public String getInvalidProperty2() {
			return "";
		}
	}
}
