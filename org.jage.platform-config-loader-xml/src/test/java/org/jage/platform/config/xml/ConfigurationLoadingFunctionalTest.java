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
 * File: ConfigurationLoadingFunctionalTest.java
 * Created: 2012-01-28
 * Author: Krzywicki
 * $Id: ConfigurationLoadingFunctionalTest.java 183 2012-03-31 20:33:32Z krzywick $
 */

package org.jage.platform.config.xml;

import java.util.List;

import org.junit.Test;

import org.jage.platform.component.definition.ClassWithProperties;
import org.jage.platform.component.definition.ConfigurationAssert;
import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.component.definition.IComponentDefinition;

import static org.jage.platform.component.builder.ConfigurationBuilder.Configuration;

/**
 * Functional tests for ConfigurationLoader.
 *
 * @author AGH AgE Team
 */
public class ConfigurationLoadingFunctionalTest {

	@Test
	public void performFullFunctionalTest() throws ConfigurationException {
	    // given
		String source = "classpath:full.xml";
		ConfigurationLoader loader = new ConfigurationLoader();
		List<IComponentDefinition> expected = createExpectedList();

		// when
		List<IComponentDefinition> configuration = loader.loadConfiguration(source);

		// then
		ConfigurationAssert.assertObjectDefinitionListsEqual(expected, configuration);
    }

	private List<IComponentDefinition> createExpectedList() {
		return Configuration()
				.Component("outerComponent", ClassWithProperties.class, true)
					.withConstructorArg(String.class, "ABC")
					.withConstructorArg(Integer.class, "123")
					.withProperty("a", Integer.class, "4")
					.withProperty("b", Float.class, "3.14")
					.withPropertyRef("list", "list")
					.withPropertyRef("map", "map")
					.withPropertyRef("set", "set")
					.withPropertyRef("objectArray", "objectArray")
					.withPropertyRef("longArray", "longArray")
					.withInner(Configuration()
							.Component("innerComponent", ClassWithProperties.class, true)
							.Component("innerAgent", ClassWithProperties.class, false)
							.Component("innerStrategy", ClassWithProperties.class, true)
							.List("list", Object.class, true)
								.withItemRef("innerComponent")
								.withItemRef("innerAgent")
							.Map("map", Object.class, Object.class, true)
								.withItem()
									.key(String.class, "2")
									.valueRef("innerComponent")
								.withItem()
									.key(String.class, "1")
									.valueRef("innerAgent")
							.Set("set", Object.class, true)
								.withItemRef("innerComponent")
								.withItem(Integer.class, "2")
							.Array("objectArray", Object.class, true)
								.withItemRef("innerComponent")
								.withItem(Integer.class, "2")
							.Array("longArray", Long.class, true)
								.withItem(Long.class, "2")
								.withItem(Long.class, "4")
								.withItem(Long.class, "8"))
				.build();
    }
}
