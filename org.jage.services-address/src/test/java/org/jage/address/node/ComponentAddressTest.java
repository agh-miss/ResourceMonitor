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
 * File: ComponentAddressTest.java
 * Created: 2011-07-19
 * Author: faber
 * $Id: ComponentAddressTest.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.address.node;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the ComponentAddress class.
 * 
 * @author AGH AgE Team
 */
public class ComponentAddressTest {

	private ComponentAddress firstAddress;

	private NodeAddress nodeAddress;

	private String componentName = "component";

	@Before
	public void setUp() {
		nodeAddress = new NodeAddress("local", "hostname");
		firstAddress = new ComponentAddress(componentName, nodeAddress);
	}

	/**
	 * Tests some basic equality scenarios.
	 */
	@Test
	public void testEquals() {
		ComponentAddress secondAddress = new ComponentAddress(componentName, nodeAddress);

		assertTrue(firstAddress.equals(secondAddress));
		assertFalse(firstAddress.equals(new ComponentAddress("another", nodeAddress)));
		assertFalse(firstAddress.equals(null));
	}

}
