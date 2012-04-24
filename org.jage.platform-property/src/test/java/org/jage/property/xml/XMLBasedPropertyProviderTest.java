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
/**
 *
 */
package org.jage.property.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jage.property.MetaPropertiesSet;
import org.jage.property.MetaProperty;
import org.jage.property.PropertyException;
import org.jage.property.xml.testHelpers.AdvancedExampleComponent;
import org.jage.property.xml.testHelpers.DifferentNameComponent;
import org.jage.property.xml.testHelpers.NoFieldComponent;
import org.jage.property.xml.testHelpers.NoMethodsComponent;
import org.jage.property.xml.testHelpers.NormalComponent;
import org.junit.Test;

/**
 * @author Tomasz Lukasik
 *
 */
public class XMLBasedPropertyProviderTest {

	AdvancedExampleComponent component = new AdvancedExampleComponent();

	/**
	 * Test method for {@link org.jage.property.xml.XMLBasedPropertyProvider#getMetaPropertiesSet(java.lang.Class)}.
	 * @throws ConfigurationException
	 */
	@Test
	public void testGetMetaPropertiesSet() throws PropertyException {
		MetaPropertiesSet set = XMLBasedPropertyProvider.getInstance().getMetaPropertiesSet(component.getClass());
		assertEquals(11, set.size());
		assertTrue(set.hasMetaProperty("intProperty"));
		assertTrue(set.getMetaProperty("intProperty") instanceof XMLBasedGetterSetterMetaProperty);
		assertTrue(set.hasMetaProperty("stringProperty"));
		assertTrue(set.getMetaProperty("stringProperty") instanceof XMLBasedGetterSetterMetaProperty);
		assertTrue(set.hasMetaProperty("floatProperty"));
		assertTrue(set.getMetaProperty("floatProperty") instanceof XMLBasedFieldMetaProperty);
		assertTrue(set.hasMetaProperty("objectProperty"));
		assertTrue(set.getMetaProperty("objectProperty") instanceof XMLBasedFieldMetaProperty);
		assertTrue(set.hasMetaProperty("complexArrayProperty"));
		assertTrue(set.getMetaProperty("complexArrayProperty") instanceof XMLBasedFieldMetaProperty);
		assertTrue(set.hasMetaProperty("changesNotifierProperty2"));
		MetaProperty metaProperty = set.getMetaProperty("changesNotifierProperty2");
		assertFalse(metaProperty.isWriteable());
	}

	/**
	 * Test method for {@link org.jage.property.xml.XMLBasedPropertyProvider#isAdaptable(java.lang.Class)}.
	 */
	@Test
	public void testIsAdaptable() {
		assertTrue(XMLBasedPropertyProvider.getInstance().isAdaptable(component.getClass()));
		assertFalse(XMLBasedPropertyProvider.getInstance().isAdaptable(XMLBasedPropertyProvider.class));
	}

	/**
	 * Test method for {@link org.jage.property.xml.XMLBasedPropertyProvider#getMetaPropertiesSet(java.lang.Class)}.
	 * @throws ConfigurationException
	 */
	@Test
	public void testGetMetaPropertiesSetNoGSF() throws PropertyException {
		MetaPropertiesSet set = XMLBasedPropertyProvider.getInstance().getMetaPropertiesSet(NormalComponent.class);
		assertEquals(2, set.size());
		assertTrue(set.hasMetaProperty("intProperty"));
		assertTrue(set.getMetaProperty("intProperty") instanceof XMLBasedGetterSetterMetaProperty);
		assertTrue(set.hasMetaProperty("stringProperty"));
		assertTrue(set.getMetaProperty("stringProperty") instanceof XMLBasedFieldMetaProperty);
	}

	/**
	 * Test method for {@link org.jage.property.xml.XMLBasedPropertyProvider#getMetaPropertiesSet(java.lang.Class)}.
	 * @throws ConfigurationException
	 */
	@Test
	public void testGetMetaPropertiesSetWrong() throws PropertyException {
		boolean exception = false;
		try {
			XMLBasedPropertyProvider.getInstance().getMetaPropertiesSet(NoMethodsComponent.class);
		} catch (PropertyException e) {
			exception = true;
		} finally {
			if (!exception)
				fail("ConfigurationException expected");
		}
		exception = false;
		try {
			XMLBasedPropertyProvider.getInstance().getMetaPropertiesSet(NoFieldComponent.class);
		} catch (PropertyException e) {
			exception = true;
		} finally {
			if (!exception)
				fail("ConfigurationException expected");
		}
		exception = false;
		try {
			XMLBasedPropertyProvider.getInstance().getMetaPropertiesSet(NoFieldComponent.class);
		} catch (PropertyException e) {
			exception = true;
		} finally {
			if (!exception)
				fail("ConfigurationException expected");
		}
		exception = false;
		try {
			XMLBasedPropertyProvider.getInstance().getMetaPropertiesSet(XMLBasedPropertyProvider.class);
		} catch (PropertyException e) {
			exception = true;
		} finally {
			if (!exception)
				fail("ConfigurationException expected");
		}
		exception = false;
		try {
			XMLBasedPropertyProvider.getInstance().getMetaPropertiesSet(DifferentNameComponent.class);
		} catch (PropertyException e) {
			exception = true;
		} finally {
			if (!exception)
				fail("ConfigurationException expected");
		}
	}
}
