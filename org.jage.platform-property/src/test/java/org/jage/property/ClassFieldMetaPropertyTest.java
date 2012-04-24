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
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import org.jage.property.testHelpers.ClassWithProperties;
import org.junit.Test;

public class ClassFieldMetaPropertyTest {

	@Test
	public void testGetField() throws Exception {
		ClassWithProperties propertiesObject = new ClassWithProperties();
		Field field = propertiesObject.getFloatPropertyField();
		PropertyField annotation = field.getAnnotation(PropertyField.class);
		FieldMetaProperty metaProperty = new FieldMetaProperty(annotation.propertyName(), field, annotation.isMonitorable());
		assertEquals(propertiesObject.getFloatPropertyField(), metaProperty
				.getPropertyField());
	}

	@Test
	public void testPropertyName() throws Exception {
		ClassWithProperties propertiesObject = new ClassWithProperties();

		Field floatField = propertiesObject.getFloatPropertyField();
		PropertyField floatAnnotation = floatField.getAnnotation(PropertyField.class);
		FieldMetaProperty floatMetaProperty = new FieldMetaProperty(floatAnnotation.propertyName(), floatField, floatAnnotation.isMonitorable());

		Field objectField = propertiesObject.getObjectPropertyField();
		PropertyField objectAnnotation = objectField.getAnnotation(PropertyField.class);
		FieldMetaProperty objectMetaProperty = new FieldMetaProperty(objectAnnotation.propertyName(), objectField, objectAnnotation.isMonitorable());

		assertEquals("floatProperty", floatMetaProperty.getName());
		assertEquals("objectProperty", objectMetaProperty.getName());
	}

	@Test
	public void testPropertyClass() throws Exception {
		ClassWithProperties propertiesObject = new ClassWithProperties();

		Field floatField = propertiesObject.getFloatPropertyField();
		PropertyField floatAnnotation = floatField.getAnnotation(PropertyField.class);
		FieldMetaProperty floatMetaProperty = new FieldMetaProperty(floatAnnotation.propertyName(), floatField, floatAnnotation.isMonitorable());

		Field objectField = propertiesObject.getObjectPropertyField();
		PropertyField objectAnnotation = objectField.getAnnotation(PropertyField.class);
		FieldMetaProperty objectMetaProperty = new FieldMetaProperty(objectAnnotation.propertyName(), objectField, objectAnnotation.isMonitorable());

		assertTrue(float.class.equals(floatMetaProperty.getPropertyClass()));
		assertTrue(Object.class.equals(objectMetaProperty.getPropertyClass()));
	}
}
