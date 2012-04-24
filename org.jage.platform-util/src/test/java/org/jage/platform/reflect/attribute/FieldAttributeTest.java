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
 * File: FieldAttributeTest.java
 * Created: 2012-02-23
 * Author: Krzywicki
 * $Id: FieldAttributeTest.java 77 2012-02-29 15:56:51Z krzywick $
 */

package org.jage.platform.reflect.attribute;

import java.lang.reflect.Field;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import static org.jage.platform.reflect.attribute.FieldAttribute.newFieldAttribute;

/**
 * Tests for FieldAttribute.
 *
 * @author AGH AgE Team
 */
public class FieldAttributeTest {

	@Test(expected = NullPointerException.class)
	public void testNullFieldThrowsNPE() {
		// when
		newFieldAttribute(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPrivateFieldThrowsIllegalArgumentException() throws Exception {
		// given
		final Field field = getField(new FinalTarget());

		// when
		newFieldAttribute(field);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testStaticFieldThrowsIllegalArgumentException() throws Exception {
		// given
		final Field field = getField(new StaticTarget());

		// when
		newFieldAttribute(field);
	}

	@Test
	public void testAttributeInfersNameFromField() throws Exception {
		// given
		final Field field = getField(new PrivateTarget());

		// when
		final Attribute<String> attribute = newFieldAttribute(field);

		// then
		assertEquals(field.getName(), attribute.getName());
	}

	@Test
	public void testAttributeInfersTypeFromField() throws Exception {
		// given
		final Field field = getField(new PrivateTarget());

		// when
		final Attribute<String> attribute = newFieldAttribute(field);

		// then
		assertEquals(field.getType(), attribute.getType());
	}

	@Test
	public void testInjectionInPublicField() throws Exception {
		// given
		final PublicTarget target = new PublicTarget();
		final Field field = getField(target);
		final Attribute<String> attribute = newFieldAttribute(field);
		final String value = "value";

		// when
		attribute.setValue(target, value);

		// then
		assertThat(target.field, is(equalTo(value)));
	}

	@Test
	public void testInjectionInPackageField() throws Exception {
		// given
		final PackageTarget target = new PackageTarget();
		final Field field = getField(target);
		final Attribute<String> attribute = newFieldAttribute(field);
		final String value = "value";

		// when
		attribute.setValue(target, value);

		// then
		assertThat(target.field, is(equalTo(value)));
	}

	@Test
	public void testInjectionInPrivateField() throws Exception {
		// given
		final PrivateTarget target = new PrivateTarget();
		final Field field = getField(target);
		final Attribute<String> attribute = newFieldAttribute(field);
		final String value = "value";

		// when
		attribute.setValue(target, value);

		// then
		assertThat(target.field, is(equalTo(value)));
	}

	@Test
	public void testEquals() throws Exception {
		// given
		final Attribute<?> attribute = newFieldAttribute(getField(new PrivateTarget()));
		final Attribute<?> wrongAttributeType = mock(Attribute.class);
		final Attribute<?> wrongField = newFieldAttribute(getField(new PublicTarget()));
		final Attribute<?> sameField = newFieldAttribute(getField(new PrivateTarget()));

		// then
		assertTrue(attribute.equals(attribute));
		assertFalse(attribute.equals(null));
		assertFalse(attribute.equals(wrongAttributeType));
		assertFalse(attribute.equals(wrongField));
		assertTrue(attribute.equals(sameField));
	}

	@Test
	public void testHashcode() throws Exception {
		// given
		final Attribute<?> attribute = newFieldAttribute(getField(new PrivateTarget()));
		final Attribute<?> other = newFieldAttribute(getField(new PrivateTarget()));

		// then
		assertEquals(attribute.hashCode(), other.hashCode());
	}

	private Field getField(final Object target) throws Exception {
		return target.getClass().getDeclaredField("field");
	}

	private static class FinalTarget {
		@SuppressWarnings("unused")
		private final String field = "value";
	}

	private static class StaticTarget {
		@SuppressWarnings("unused")
		private static String field;
	}

	private static class PublicTarget {
		public String field;
	}

	private static class PackageTarget {
		String field;
	}

	private static class PrivateTarget {
		private String field;
	}
}
