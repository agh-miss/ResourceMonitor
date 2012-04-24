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
 * File: CollectionDefinitionReaderTest.java
 * Created: 2012-01-28
 * Author: Krzywicki
 * $Id: CollectionDefinitionReaderTest.java 59 2012-02-13 13:10:28Z krzywick $
 */

package org.jage.platform.config.xml.readers;

import java.util.ArrayList;

import org.dom4j.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.jage.platform.component.definition.CollectionDefinition;
import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.component.definition.IComponentDefinition;
import org.jage.platform.component.definition.IArgumentDefinition;
import org.jage.platform.config.xml.ConfigAttributes;
import org.jage.platform.config.xml.ConfigTags;

import static org.jage.platform.config.xml.ConfigTestUtils.CLASS_VALUE;
import static org.jage.platform.config.xml.ConfigTestUtils.NAME_VALUE;
import static org.jage.platform.config.xml.ConfigTestUtils.newCollectionElement;
import static org.jage.platform.config.xml.ConfigTestUtils.newElement;

/**
 * Unit tests for CollectionDefinitionReader.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class CollectionDefinitionReaderTest  {

	@Mock
	@SuppressWarnings("unused")
	private IDefinitionReader<IArgumentDefinition> argumentReader;

	@Mock
	@SuppressWarnings("unused")
	private IDefinitionReader<IComponentDefinition> instanceReader;

	@SuppressWarnings("rawtypes")
    private Class<ArrayList> collectionClass = ArrayList.class;

	@InjectMocks
	private CollectionDefinitionReader reader = new CollectionDefinitionReader(collectionClass);

	@Test
	public void testValidDefinition() throws ConfigurationException {
		// given
		Element element = newCollectionElement().build();

		// when
		CollectionDefinition definition = reader.read(element);

		// then
		assertNotNull(definition);
		assertThat(definition.getName(), is(NAME_VALUE));
		assertEquals(collectionClass, definition.getType());
		assertEquals(String.class, definition.getElementsType());
		assertThat(definition.isSingleton(), is(true));
	}

	@Test(expected = ConfigurationException.class)
	public void testNameAttributeIsRequired() throws ConfigurationException {
		// given
		Element element = newElement(ConfigTags.LIST)
				.build();

		// when
		reader.read(element);
	}

	@Test(expected = ConfigurationException.class)
	public void testNameAttributeIsNotEmpty() throws ConfigurationException {
		// given
		Element element = newElement(ConfigTags.LIST)
				.withAttribute(ConfigAttributes.NAME, "")
				.build();

		// when
		reader.read(element);
	}

	@Test
	public void testTypeAttributeIsOptional() throws ConfigurationException {
		// given
		Element element = newElement(ConfigTags.LIST)
				.withAttribute(ConfigAttributes.NAME, NAME_VALUE)
				.build();

		// when
		CollectionDefinition definition = reader.read(element);

		// then
		assertEquals(Object.class, definition.getElementsType());
	}

	@Test(expected = ConfigurationException.class)
	public void testTypeAttributeIsNotEmpty() throws ConfigurationException {
		// given
		Element element = newElement(ConfigTags.LIST)
				.withAttribute(ConfigAttributes.NAME, NAME_VALUE)
				.withAttribute(ConfigAttributes.TYPE, "")
				.build();

		// when
		reader.read(element);
	}

	@Test
	public void testSingletonAttributeIsOptional() throws ConfigurationException {
		// given
		Element element = newElement(ConfigTags.LIST)
				.withAttribute(ConfigAttributes.NAME, NAME_VALUE)
				.withAttribute(ConfigAttributes.TYPE, CLASS_VALUE)
				.build();

		// when
		CollectionDefinition definition = reader.read(element);

		// then
		assertThat(definition.isSingleton(), is(false));
	}

	@Test(expected = ConfigurationException.class)
	public void testSingletonAttributeIsNotEmpty() throws ConfigurationException {
		// given
		Element element = newElement(ConfigTags.LIST)
				.withAttribute(ConfigAttributes.NAME, NAME_VALUE)
				.withAttribute(ConfigAttributes.TYPE, CLASS_VALUE)
				.withAttribute(ConfigAttributes.IS_SINGLETON, "")
				.build();

		// when
		reader.read(element);
	}
}
