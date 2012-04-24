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
 * File: TestDefaultNameProvider.java
 * Created: 2011-07-19
 * Author: faber
 * $Id: TestDefaultNameProvider.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.address.provider;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the DefaulNameProvider class.
 * 
 * @author AGH AgE Team
 */
public class TestDefaultNameProvider {

	private DefaultNameProvider service;

	/**
	 * Sets up tests.
	 */
	@Before
	public void setUp() {
		service = new DefaultNameProvider();
	}

	/**
	 * Tests an act of name generation (whether names are unique).
	 */
	@Test
	public void testGenerateNameFromTemplate() {
		Set<String> set = new HashSet<String>();
		for (int i = 0; i < 100000; i++) {
			String address = service.generateNameFromTemplate("myPrefix");
			assertNotNull(address);
			assertTrue(set.add(address));
		}
	}

}
