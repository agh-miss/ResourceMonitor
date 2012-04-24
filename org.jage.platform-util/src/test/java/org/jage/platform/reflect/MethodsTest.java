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
 * File: MethodsTest.java
 * Created: 2012-02-29
 * Author: Krzywicki
 * $Id: MethodsTest.java 113 2012-03-15 12:47:00Z krzywick $
 */

package org.jage.platform.reflect;

import java.lang.reflect.Method;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static org.jage.platform.reflect.Methods.getGetterName;
import static org.jage.platform.reflect.Methods.getGetterType;
import static org.jage.platform.reflect.Methods.isBooleanGetter;
import static org.jage.platform.reflect.Methods.isGetter;
import static org.jage.platform.reflect.Methods.isSetter;

/**
 * Tests for Methods.
 *
 * @author AGH AgE Team
 */
public class MethodsTest {

    private static final String NAME = "property";

	@Test
	public void testValidIsBooleanGetter() throws Exception {
		// given
		final Method booleanGetter = getBooleanGetter(new BooleanTarget());

		// then
		assertTrue(isBooleanGetter(booleanGetter));
	}

	@Test
	public void testBooleanGetterName() throws Exception {
		// given
		final Method booleanGetter = getBooleanGetter(new BooleanTarget());

		// then
		assertEquals(NAME, getGetterName(booleanGetter));
	}

	@Test
	public void testBooleanGetterType() throws Exception {
		// given
		final Method booleanGetter = getBooleanGetter(new BooleanTarget());

		// then
		assertEquals(boolean.class, getGetterType(booleanGetter));
	}

	@Test
	public void testValidIsGetter() throws Exception {
		// given
		final Method getter = getGetter(new StringTarget());

		// then
		assertTrue(isGetter(getter));
	}

	@Test
	public void testGetterName() throws Exception {
		// given
		final Method getter = getGetter(new StringTarget());

		// then
		assertEquals(NAME, getGetterName(getter));
	}

	@Test
	public void testGetterType() throws Exception {
		// given
		final Method getter = getGetter(new StringTarget());

		// then
		assertEquals(String.class, getGetterType(getter));
	}

	@Test
	public void testValidIsSetter() throws Exception {
		// given
		final Method setter = getSetter(new StringTarget());

		// then
		assertTrue(isSetter(setter));
	}

	@Test
	public void testSetterName() throws Exception {
		// given
		final Method setter = getSetter(new StringTarget());

		// then
		assertEquals(NAME, Methods.getSetterName(setter));
	}

	@Test
	public void testSetterType() throws Exception {
		// given
		final Method setter = getSetter(new StringTarget());

		// then
		assertEquals(String.class, Methods.getSetterType(setter));
	}


    private Method getBooleanGetter(final Object target) throws Exception {
    	return target.getClass().getDeclaredMethod("isProperty");
    }

    private Method getGetter(final Object target) throws Exception {
    	return target.getClass().getDeclaredMethod("getProperty");
    }

    private Method getSetter(final Object target) throws Exception {
    	return target.getClass().getDeclaredMethod("setProperty", String.class);
    }

	private static class BooleanTarget {
		@SuppressWarnings("unused")
        public boolean isProperty() {
			return true;
        }
	}

	private static class StringTarget {
		@SuppressWarnings("unused")
        public String getProperty() {
			return null;
        }

		@SuppressWarnings("unused")
        public void setProperty(final String property) {
        }
	}
}
