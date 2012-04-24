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
 * File: AbstractAttributeTest.java
 * Created: 2012-02-29
 * Author: Krzywicki
 * $Id: AbstractAttributeTest.java 77 2012-02-29 15:56:51Z krzywick $
 */

package org.jage.platform.reflect.attribute;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for AbstractAttribute.
 *
 * @author AGH AgE Team
 */
public class AbstractAttributeTest {

    private static final String NAME = "name";

	@Test(expected = NullPointerException.class)
	public void testNullNameThrowsNPE() {
		// given
		final String name = null;
		final Class<String> type = String.class;

		// when
		new MockAttribute<String>(name, type);
	}

	@Test(expected = NullPointerException.class)
	public void testNullTypeThrowsNPE() {
		// given
		final String name = NAME;
		final Class<String> type = null;

		// when
		new MockAttribute<String>(name, type);
	}

	@Test
	public void testName() {
		// given
		final String name = NAME;
		final Class<String> type = String.class;
		final Attribute<String> attribute = new MockAttribute<String>(name, type);

		// then
		assertEquals(name, attribute.getName());
	}

	@Test
	public void testType() {
		// given
		final String name = NAME;
		final Class<String> type = String.class;
		final Attribute<String> attribute = new MockAttribute<String>(name, type);

		// then
		assertEquals(type, attribute.getType());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidAsType() {
		// given
		final String name = NAME;
		final Class<Integer> sourceType = Integer.class;
		final Class<Number> targetType = Number.class;
		final Attribute<Integer> attribute = new MockAttribute<Integer>(name, sourceType);

		// then
		attribute.asType(targetType);
	}

	@Test
	public void testValidAsType() {
		// given
		final String name = NAME;
		final Class<Number> sourceType = Number.class;
		final Class<Integer> targetType = Integer.class;
		final Attribute<Number> attribute = new MockAttribute<Number>(name, sourceType);

		// then
		assertEquals(attribute, attribute.asType(targetType));
	}

	private static final class MockAttribute<T> extends AbstractAttribute<T> {
		MockAttribute(final String name, final Class<T> type) {
			super(name, type);
		}

		@Override
		public void setValue(final Object target, final T value) throws IllegalAccessException {
			throw new UnsupportedOperationException();
		}
	}

}
