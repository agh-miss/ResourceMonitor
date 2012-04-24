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
 * File: InitialSelectorsTest.java
 * Created: 2011-09-19
 * Author: faber
 * $Id: InitialSelectorsTest.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.query;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link InitialSelectors} class.
 * 
 * @author AGH AgE Team
 */
public class InitialSelectorsTest {

	private IInitialSelector testedSelector;

	/**
	 * Tests the {@link org.jage.query.InitialSelectors#first(int)} method.
	 */
	@Test
	public void testFirst() {
		final int elementsCount = 20;
		testedSelector = InitialSelectors.first(elementsCount / 2);
		testedSelector.initialise(elementsCount);

		for (int i = 0; i < elementsCount; i++) {
			if (i < elementsCount / 2) {
				assertTrue(testedSelector.include());
			} else {
				assertFalse(testedSelector.include());
			}
		}
	}

	/**
	 * Tests the {@link org.jage.query.InitialSelectors#all()} method.
	 */
	@Test
	public void testAll() {
		testedSelector = InitialSelectors.all();

		for (int i = 0; i < 20; i++) {
			assertTrue(testedSelector.include());
		}
	}

}
