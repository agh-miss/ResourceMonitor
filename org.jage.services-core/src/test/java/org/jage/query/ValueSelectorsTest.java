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
 * File: ValueSelectorsTest.java
 * Created: 2011-10-13
 * Author: faber
 * $Id: ValueSelectorsTest.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.query;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link ValueSelectors} class.
 * 
 * @author AGH AgE Team
 */
public class ValueSelectorsTest {

	/**
	 * Tests the {@link org.jage.query.ValueSelectors#field(java.lang.String)} method.
	 */
	@Test
	public void testField() {
		class TestingClass {
			private int field;

			public TestingClass(int field) {
				this.field = field;
			}

			@SuppressWarnings("unused")
			public int getField() {
				return field;
			}
		}

		IValueSelector<TestingClass, Integer> testedSelector = ValueSelectors.field("field");

		assertEquals(new Integer(1), testedSelector.selectValue(new TestingClass(1)));
	}

}
