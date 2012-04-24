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
 * File: ValueDefinitionReaderTest.java
 * Created: 2012-01-28
 * Author: Krzywicki
 * $Id: ValueDefinitionReaderTest.java 59 2012-02-13 13:10:28Z krzywick $
 */

package org.jage.platform.config.xml.readers;

import org.dom4j.Element;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.component.definition.ValueDefinition;
import org.jage.platform.config.xml.ConfigAttributes;
import org.jage.platform.config.xml.ConfigTags;

import static org.jage.platform.config.xml.ConfigTestUtils.newElement;
import static org.jage.platform.config.xml.ConfigTestUtils.newValueElement;

/**
 * Unit tests for ValueDefinitionReader.
 *
 * @author AGH AgE Team
 */
public class ValueDefinitionReaderTest  {

	private ValueDefinitionReader reader = new ValueDefinitionReader();

	@Test
	public void testValidStringDefinition() throws ConfigurationException {
		// given
		Element element = newValueElement("String", "abc")
				.build();

		// when
		ValueDefinition definition = reader.read(element);

		// then
		assertNotNull(definition);
		assertEquals(definition.getDesiredClass(), String.class);
		assertThat(definition.getStringValue(), is("abc"));
	}

	@Test
	public void testValidIntegerDefinition() throws ConfigurationException {
		// given
		Element element = newValueElement("Integer", "123")
				.build();

		// when
		ValueDefinition definition = reader.read(element);

		// then
		assertNotNull(definition);
		assertEquals(definition.getDesiredClass(), Integer.class);
		assertThat(definition.getStringValue(), is("123"));
	}

	@Test(expected = ConfigurationException.class)
	public void testClassAttributeisRequired() throws ConfigurationException {
		// given
		Element element = newElement(ConfigTags.VALUE)
				.build();

		// when
		reader.read(element);
	}

	@Test(expected = ConfigurationException.class)
	public void testClassAttributeisNotEmpty() throws ConfigurationException {
		// given
		Element element = newElement(ConfigTags.VALUE)
				.withAttribute(ConfigAttributes.CLASS, "")
				.build();

		// when
		reader.read(element);
	}

	@Test(expected = ConfigurationException.class)
	public void testValueAttributeisRequired() throws ConfigurationException {
		// given
		Element element = newElement(ConfigTags.VALUE)
				.withAttribute(ConfigAttributes.CLASS, "String")
				.build();

		// when
		reader.read(element);
	}

	@Test
	public void testEmptyValueAttributeisValid() throws ConfigurationException {
		// given
		Element element = newElement(ConfigTags.VALUE)
				.withAttribute(ConfigAttributes.CLASS, "String")
				.withAttribute(ConfigAttributes.VALUE, "")
				.build();

		// when
		ValueDefinition definition = reader.read(element);

		// then
		assertNotNull(definition);
		assertThat(definition.getStringValue(), is(""));
	}
}
