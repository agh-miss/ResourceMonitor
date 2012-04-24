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
 * File: ComponentDefinitionReaderTest.java
 * Created: 2012-01-28
 * Author: Krzywicki
 * $Id: ComponentDefinitionReaderTest.java 59 2012-02-13 13:10:28Z krzywick $
 */

package org.jage.platform.config.xml.readers;

import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.jage.platform.component.definition.ComponentDefinition;
import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.component.definition.IArgumentDefinition;
import org.jage.platform.component.definition.IComponentDefinition;
import org.jage.platform.config.xml.ConfigAttributes;
import org.jage.platform.config.xml.ConfigTags;

import static org.jage.platform.config.xml.ConfigTestUtils.CLASS_VALUE;
import static org.jage.platform.config.xml.ConfigTestUtils.EMPTY_STRING;
import static org.jage.platform.config.xml.ConfigTestUtils.NAME_VALUE;
import static org.jage.platform.config.xml.ConfigTestUtils.anyElement;
import static org.jage.platform.config.xml.ConfigTestUtils.newComponentElement;
import static org.jage.platform.config.xml.ConfigTestUtils.newConstructorElement;
import static org.jage.platform.config.xml.ConfigTestUtils.newElement;
import static org.jage.platform.config.xml.ConfigTestUtils.newPropertyElement;

/**
 * Unit tests for ComponentDefinitionReader.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class ComponentDefinitionReaderTest {

	@Mock
	private IDefinitionReader<IArgumentDefinition> argumentReader;

	@Mock
	private IDefinitionReader<IComponentDefinition> instanceReader;

	@InjectMocks
	private final ComponentDefinitionReader reader = new ComponentDefinitionReader();

	@Before
	public void setup() throws ConfigurationException {
	    IArgumentDefinition value = mock(IArgumentDefinition.class);
		given(argumentReader.read(Mockito.any(Element.class))).willReturn(value);
		IComponentDefinition instance = mock(IComponentDefinition.class);
		given(instanceReader.read(Mockito.any(Element.class))).willReturn(instance);
    }

	@Test
	public void testValidBasicDefinition() throws ConfigurationException {
		// given
		Element element = newComponentElement().build();

		// when
		ComponentDefinition definition = reader.read(element);

		// then
		assertNotNull(definition);
		assertThat(definition.getName(), is(NAME_VALUE));
		assertThat(definition.getType(), is(Object.class));
		assertThat(definition.isSingleton(), is(true));
		assertTrue(definition.getConstructorArguments().isEmpty());
		assertTrue(definition.getPropertyArguments().isEmpty());
		assertTrue(definition.getInnerComponentDefinitions().isEmpty());
	}

	@Test(expected = ConfigurationException.class)
	public void testNameAttributeIsRequired() throws ConfigurationException {
		// given
		Element element = newElement(ConfigTags.COMPONENT)
				.build();

		// when
		reader.read(element);
	}

	@Test(expected = ConfigurationException.class)
	public void testNameAttributeisNotEmpty() throws ConfigurationException {
		// given
		Element element = newElement(ConfigTags.COMPONENT)
				.withAttribute(ConfigAttributes.NAME, EMPTY_STRING)
				.build();

		// when
		reader.read(element);
	}

	@Test(expected = ConfigurationException.class)
	public void testClassAttributeIsRequired() throws ConfigurationException {
		// given
		Element element = newElement(ConfigTags.COMPONENT)
				.withAttribute(ConfigAttributes.NAME, NAME_VALUE)
				.build();

		// when
		reader.read(element);
	}

	@Test(expected = ConfigurationException.class)
	public void testClassAttributeisNotEmpty() throws ConfigurationException {
		// given
		Element element = newElement(ConfigTags.COMPONENT)
				.withAttribute(ConfigAttributes.NAME, NAME_VALUE)
				.withAttribute(ConfigAttributes.CLASS, EMPTY_STRING)
				.build();

		// when
		reader.read(element);
	}

	@Test(expected = ConfigurationException.class)
	public void testSingletonAttributeIsRequired() throws ConfigurationException {
		// given
		Element element = newElement(ConfigTags.COMPONENT)
				.withAttribute(ConfigAttributes.NAME, NAME_VALUE)
				.withAttribute(ConfigAttributes.CLASS, CLASS_VALUE)
				.build();

		// when
		reader.read(element);
	}

	@Test(expected = ConfigurationException.class)
	public void testSingletonAttributeisNotEmpty() throws ConfigurationException {
		// given
		Element element = newElement(ConfigTags.COMPONENT)
				.withAttribute(ConfigAttributes.NAME, NAME_VALUE)
				.withAttribute(ConfigAttributes.CLASS, CLASS_VALUE)
				.withAttribute(ConfigAttributes.IS_SINGLETON, EMPTY_STRING)
				.build();

		// when
		reader.read(element);
	}

	@Test
	public void testConstructorArgument() throws ConfigurationException {
		// given
		Element element = newComponentElement()
				.withBody(
						newConstructorElement(anyElement()),
						newConstructorElement(anyElement()))
				.build();

		// when
		ComponentDefinition definition = reader.read(element);

		// then
		List<IArgumentDefinition> constructorArgs = definition.getConstructorArguments();
		assertThat(constructorArgs.size(), is(2));
	}

	@Test(expected = ConfigurationException.class)
	public void testPropertyArgumentNameIsRequired() throws ConfigurationException {
		// given
		Element element = newComponentElement()
				.withBody(newElement(ConfigTags.PROPERTY))
				.build();

		// when
		reader.read(element);
	}

	@Test(expected = ConfigurationException.class)
	public void testPropertyArgumentNameisNotEmpty() throws ConfigurationException {
		// given
		Element element = newComponentElement()
				.withBody(newPropertyElement("", anyElement()))
				.build();

		// when
		reader.read(element);
	}

	@Test
	public void testPropertyArgument() throws ConfigurationException {
		// given
		Element element = newComponentElement()
				.withBody(
						newPropertyElement("someProperty1", anyElement()),
						newPropertyElement("someProperty2", anyElement()))
				.build();

		// when
		ComponentDefinition definition = reader.read(element);

		// then
		Map<String, IArgumentDefinition> propertyInitializers = definition.getPropertyArguments();
		assertThat(propertyInitializers.size(), is(2));
		assertTrue(propertyInitializers.containsKey("someProperty1"));
		assertTrue(propertyInitializers.containsKey("someProperty2"));
	}

	@Test
	public void testInnerDefinitions() throws ConfigurationException {
		// given
		Element element = newComponentElement()
				.withBody(
						anyElement(),
						anyElement(),
						anyElement())
				.build();

		// when
		ComponentDefinition definition = reader.read(element);

		// then
		assertThat(definition.getInnerComponentDefinitions().size(), is(3));
	}

	@Test
	public void testPropertyAndConstructorArgumentsNotInInner() throws ConfigurationException {
		// given
		Element element = newComponentElement()
				.withBody(
						anyElement(),
						newPropertyElement("someProperty", anyElement()),
						anyElement(),
						newConstructorElement(anyElement()),
						anyElement())
				.build();

		// when
		ComponentDefinition definition = reader.read(element);

		// then
		assertThat(definition.getInnerComponentDefinitions().size(), is(3));
	}
}
