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
 * File: AttributesTest.java
 * Created: 2012-02-29
 * Author: Krzywicki
 * $Id: AttributesTest.java 77 2012-02-29 15:56:51Z krzywick $
 */

package org.jage.platform.reflect.attribute;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for Attributes.
 *
 * @author AGH AgE Team
 */
public class AttributesTest {

	@Test
	public void testFieldAttribute() throws Exception {
		// given
		final Field field = new Target().getClass().getDeclaredField("field");
		final Attribute<String> expected = FieldAttribute.newFieldAttribute(field);
		// when
		final Attribute<String> attribute = Attributes.forField(field);

		// then
		assertThat(attribute, is(equalTo(expected)));
	}

	@Test
	public void testMethodAttribute() throws Exception {
		// given
		final Method setter = new Target().getClass().getDeclaredMethod("setField", String.class);
		final Attribute<String> expected = MethodAttribute.newSetterAttribute(setter);
		// when
		final Attribute<String> attribute = Attributes.forSetter(setter);

		// then
		assertThat(attribute, is(equalTo(expected)));
	}

	private static class Target {
		@SuppressWarnings("unused")
		private String field;

		@SuppressWarnings("unused")
		private void setField(final String field) {
		}
	}
}
