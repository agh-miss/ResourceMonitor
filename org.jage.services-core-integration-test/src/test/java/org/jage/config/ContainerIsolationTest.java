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
package org.jage.config;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.picocontainer.PicoCompositionException;

import org.jage.platform.component.definition.ClassWithProperties;
import org.jage.platform.component.definition.CollectionDefinition;
import org.jage.platform.component.definition.ComponentDefinition;
import org.jage.platform.component.definition.ConfigurationAssert;
import org.jage.platform.component.definition.IComponentDefinition;
import org.jage.platform.component.definition.ReferenceDefinition;
import org.jage.platform.component.pico.PicoComponentInstanceProvider;
import org.jage.platform.component.provider.IMutableComponentInstanceProvider;
import org.jage.platform.config.xml.ConfigurationLoader;

import com.google.common.collect.Lists;

/**
 *
 * @author Adam Wos <adam.wos@gmail.com>
 */
public class ContainerIsolationTest {

	@Before
	public void setUp() {

	}

	@Test
	public void testCreateInstance() throws Exception {
		ConfigurationLoader loader = new ConfigurationLoader();
		List<IComponentDefinition> definitions = loader.loadConfiguration("classpath:org/jage/config/containerIsolation1.xml");

		IMutableComponentInstanceProvider pico = new PicoComponentInstanceProvider();
		for (IComponentDefinition definition : definitions) {
			pico.addComponent(definition);
		}

		ClassWithProperties o1 = (ClassWithProperties)pico.getInstance("obj1");
		Assert.assertNotNull(o1);
		ClassWithProperties o2 = (ClassWithProperties)pico.getInstance("obj2");
		Assert.assertNotNull(o2);
		Assert.assertEquals(1, o1.getList().size());
		Assert.assertEquals(o2, o1.getList().get(0));
	}

	@Test
	public void testCreateInstance2() throws Exception {
		ConfigurationLoader loader = new ConfigurationLoader();
		List<IComponentDefinition> definitions = loader.loadConfiguration("classpath:org/jage/config/containerIsolation2.xml");

		IMutableComponentInstanceProvider pico = new PicoComponentInstanceProvider();
		for (IComponentDefinition definition : definitions) {
			pico.addComponent(definition);
		}

		try {
			pico.verify();
			pico.getInstance("obj1");
			Assert.fail("Should not create instance; subobj2 inaccessible!");
		} catch (PicoCompositionException e) {
		}

	}

	@Test
	public void testReadConfiguration() throws Exception {

		List<IComponentDefinition> expected = Lists.newArrayList();

		ComponentDefinition obj1 = new ComponentDefinition("obj1", ClassWithProperties.class, false);
		expected.add(obj1);
		CollectionDefinition list = new CollectionDefinition("list", ArrayList.class, false);
		list.addItem(new ReferenceDefinition("obj2"));
		obj1.addInnerComponentDefinition(list);
		obj1.addPropertyArgument("list", new ReferenceDefinition("list"));

		ComponentDefinition obj2 = new ComponentDefinition("obj2", ClassWithProperties.class, true);
		expected.add(obj2);
		obj2.addInnerComponentDefinition(new ComponentDefinition("subobj2", ClassWithProperties.class, true));

		ConfigurationLoader loader = new ConfigurationLoader();
		List<IComponentDefinition> definitions = loader.loadConfiguration("classpath:org/jage/config/containerIsolation1.xml");
		ConfigurationAssert.assertObjectDefinitionListsEqual(expected, definitions);
	}

	@Test
	public void testReadConfiguration2() throws Exception {

		List<IComponentDefinition> expected = Lists.newArrayList();

		ComponentDefinition obj1 = new ComponentDefinition("obj1", ClassWithProperties.class, false);
		expected.add(obj1);
		CollectionDefinition list = new CollectionDefinition("list", ArrayList.class, false);
		list.addItem(new ReferenceDefinition("subobj2"));
		obj1.addInnerComponentDefinition(list);
		obj1.addPropertyArgument("list", new ReferenceDefinition("list"));

		ComponentDefinition obj2 = new ComponentDefinition("obj2", ClassWithProperties.class, true);
		expected.add(obj2);
		obj2.addInnerComponentDefinition(new ComponentDefinition("subobj2", ClassWithProperties.class, true));

		ConfigurationLoader loader = new ConfigurationLoader();
		List<IComponentDefinition> definitions = loader.loadConfiguration("classpath:org/jage/config/containerIsolation2.xml");
		ConfigurationAssert.assertObjectDefinitionListsEqual(expected, definitions);
	}
}
