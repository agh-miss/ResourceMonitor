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
 * File: ClassesTest.java
 * Created: 2012-02-29
 * Author: Krzywicki
 * $Id: ClassesTest.java 122 2012-03-18 09:41:07Z krzywick $
 */

package org.jage.platform.reflect;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import static org.jage.platform.reflect.Classes.equivalentWhenAutoBoxing;
import static org.jage.platform.reflect.Classes.isAssignable;
import static org.jage.platform.reflect.Classes.isWrapperOfPrimitive;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;

/**
 * Tests for Classes.
 *
 * @author AGH AgE Team
 */
public class ClassesTest {

	@Test
	public void testIsWrapperOfPrimitive() {
		assertFalse(isWrapperOfPrimitive(Object.class, Integer.class));
		assertFalse(isWrapperOfPrimitive(Object.class, int.class));
		assertFalse(isWrapperOfPrimitive(Integer.class, Object.class));
		assertFalse(isWrapperOfPrimitive(int.class, Object.class));

		assertFalse(isWrapperOfPrimitive(Integer.class, Integer.class));
		assertFalse(isWrapperOfPrimitive(int.class, Integer.class));
		assertTrue(isWrapperOfPrimitive(Integer.class, int.class));
		assertFalse(isWrapperOfPrimitive(int.class, int.class));
	}

	@Test
	public void testEquivalentWhenAutoBoxing() {
		assertFalse(equivalentWhenAutoBoxing(Object.class, Integer.class));
		assertFalse(equivalentWhenAutoBoxing(Object.class, int.class));
		assertFalse(equivalentWhenAutoBoxing(Integer.class, Object.class));
		assertFalse(equivalentWhenAutoBoxing(int.class, Object.class));

		assertTrue(equivalentWhenAutoBoxing(int.class, int.class));
		assertTrue(equivalentWhenAutoBoxing(Integer.class, int.class));
		assertTrue(equivalentWhenAutoBoxing(int.class, Integer.class));
		assertTrue(equivalentWhenAutoBoxing(Integer.class, Integer.class));
	}

	@Test
	public void testIsAssignable() {
		assertTrue(isAssignable(int.class, int.class));
		assertTrue(isAssignable(Integer.class, int.class));
		assertTrue(isAssignable(int.class, Integer.class));
		assertTrue(isAssignable(Integer.class, Integer.class));

		assertTrue(isAssignable(int.class, Number.class));
		assertTrue(isAssignable(Integer.class, Number.class));
		assertFalse(isAssignable(Number.class, Integer.class));
		assertFalse(isAssignable(Number.class, int.class));

		assertTrue(isAssignable(int.class, Object.class));
		assertTrue(isAssignable(Integer.class, Object.class));
		assertFalse(isAssignable(Object.class, Integer.class));
		assertFalse(isAssignable(Object.class, int.class));
	}

	@Test
	public void testSimilarIterablesAssignable() {
		// given
		final List<Class<?>> source = Arrays.asList(new Class<?>[] { int.class, String.class, Object.class });
		final List<Class<?>> target = Arrays.asList(new Class<?>[] { Number.class, Object.class, Object.class });

		// then
		assertTrue(Classes.isAssignable(source, target));
	}

	@Test
	public void testAsimilarIterablesNotAssignable() {
		// given
		final List<Class<?>> source = Arrays.asList(new Class<?>[] { Number.class, String.class, Object.class });
		final List<Class<?>> target = Arrays.asList(new Class<?>[] { int.class, Object.class, Object.class });

		// then
		assertFalse(Classes.isAssignable(source, target));
	}

	@Test
	public void testDifferentLengthsIterablesNotAssignable() {
		// given
		final List<Class<?>> source = Arrays.asList(new Class<?>[] { int.class, String.class, Object.class });
		final List<Class<?>> target = Arrays.asList(new Class<?>[] { Number.class, Object.class });

		// then
		assertFalse(Classes.isAssignable(source, target));
	}

	@Test
	public void shouldWrapDeclaredConstructors() {
		// given
		final Class<SomeConstructors> target = SomeConstructors.class;
		final List<Constructor<?>> expected = Arrays.asList(target.getDeclaredConstructors());

		// when
		final List<Constructor<SomeConstructors>> constructors = Classes.getDeclaredConstructors(target);

		// then
		assertTrue(Iterables.elementsEqual(constructors, expected));
	}

	@Test
	public void shouldFindDefaultDeclaredConstructor() throws Exception {
		// given
		final Class<DefaultConstructor> target = DefaultConstructor.class;
		final Constructor<DefaultConstructor> expected = target.getDeclaredConstructor();

		// when
		final Optional<Constructor<DefaultConstructor>> optional = Classes.getDeclaredDefaultConstructor(target);

		// then
		assertTrue(optional.isPresent());
		assertThat(optional.get(), is(equalTo(expected)));
	}

	@Test
	public void shouldNotFindDefaultDeclaredConstructor() {
		// given
		final Class<NoDefaultConstructor> target = NoDefaultConstructor.class;

		// when
		final Optional<Constructor<NoDefaultConstructor>> optional = Classes.getDeclaredDefaultConstructor(target);

		// then
		assertFalse(optional.isPresent());
	}

	@SuppressWarnings("unused")
	private static class SomeConstructors {

        public SomeConstructors() {
		}

		public SomeConstructors(final String foo) {
		}

		public SomeConstructors(final String foo, final int bar) {
		}
	}

	private static class DefaultConstructor {
	}

	private static class NoDefaultConstructor {
		@SuppressWarnings("unused")
		public NoDefaultConstructor(final String value) {
		}
	}
}
