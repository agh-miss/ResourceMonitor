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
 * File: TestDefaultAgentAddressProvider.java
 * Created: 2011-07-27
 * Author: faber
 * $Id: TestDefaultAgentAddressProvider.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.address.provider;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jage.address.IAgentAddress;
import org.jage.platform.component.exception.ComponentException;

/**
 * Tests for the DefaultAgentAddressProvider class.
 * 
 * @author AGH AgE Team
 */
public class TestDefaultAgentAddressProvider {

	private DefaultAgentAddressProvider service;

	/**
	 * Sets up tests.
	 */
	@Before
	public void setUp() throws ComponentException {
		service = new DefaultAgentAddressProvider();
		service.init();
	}

	/**
	 * Tests an act of obtaining a address for the first time (not known address).
	 */
	@Test
	public void testObtainAddressNotKnown() {
		String initializer = "initializer";
		IAgentAddress address = service.obtainAddress(initializer);
		assertNotNull(address);
		assertTrue(address.getName().contains(initializer));
	}

	/**
	 * Tests an act of obtaining a address for the first time (not known address) for a <code>null</code> initialisation
	 * value.
	 */
	@Test
	public void testObtainAddressNotKnownNullInitializer() {
		IAgentAddress address = service.obtainAddress(null);
		assertNotNull(address);
		assertNotNull(address.getName());
	}

}
