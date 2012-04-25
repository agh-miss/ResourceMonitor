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

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 
 * @author Adam Wos <adam.wos@gmail.com>
 */
// FIXME: This test is broken beacause of new auto wiring! Inner objects creates infinite recursion.
@Ignore
public class AutoWiringTest extends InstanceProvidingTestCase {

	public AutoWiringTest() {
		super("classpath:org/jage/config/noCollectionAutoWiring.xml");
	}

	@Test
	public void testCreateInstance() throws Exception {
		Object instance = getInstance("outerObject");
		Assert.assertNotNull(instance);
	}
}