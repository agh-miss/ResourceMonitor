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
 * File: ValueFiltersTest.java
 * Created: 2011-09-19
 * Author: faber
 * $Id: ValueFiltersTest.java 76 2012-02-27 20:55:05Z faber $
 */

package org.jage.query;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Tests for the {@link ValueFilters} class.
 * 
 * @author AGH AgE Team
 */
@SuppressWarnings("static-method")
public class ValueFiltersTest {

	/**
	 * Tests the {@link org.jage.query.ValueFilters#pattern(java.lang.String)} method.
	 */
	@Test
	public void testPattern() {
		IValueFilter<String> testedFilter = ValueFilters.pattern("testPattern.*");

		assertTrue(testedFilter.matches("testPattern"));
		assertTrue(testedFilter.matches("testPattern 3.14"));
		assertFalse(testedFilter.matches("tsetPattern"));
	}

	/**
	 * Tests the {@link org.jage.query.ValueFilters#pattern(java.lang.String)} method with <code>null</code> pattern.
	 */
	@Test(expected = NullPointerException.class)
	public void testPatternWithNull() {
		ValueFilters.pattern(null);
	}

	/**
	 * Tests the {@link org.jage.query.ValueFilters#eq(java.lang.Object)} method.
	 */
	@Test
	public void testEq() {
		Object object = new Object();
		IValueFilter<Object> testedFilter = ValueFilters.eq(object);

		assertTrue(testedFilter.matches(object));
		assertFalse(testedFilter.matches(new Object()));
		assertFalse(testedFilter.matches(null));

		testedFilter = ValueFilters.eq(null);
		assertFalse(testedFilter.matches(object));
		assertTrue(testedFilter.matches(null));
	}

	/**
	 * Tests the {@link org.jage.query.ValueFilters#lessThan(Comparable)} method.
	 */
	@Test
	public void testLessThan() {
		IValueFilter<Integer> testedFilter = ValueFilters.lessThan(10);

		assertTrue(testedFilter.matches(0));
		assertTrue(testedFilter.matches(-100));
		assertFalse(testedFilter.matches(10));
		assertFalse(testedFilter.matches(100));
	}

	/**
	 * Tests the {@link org.jage.query.ValueFilters#lessOrEqual(Comparable)} method.
	 */
	@Test
	public void testLessOrEqual() {
		IValueFilter<Integer> testedFilter = ValueFilters.lessOrEqual(10);

		assertTrue(testedFilter.matches(0));
		assertTrue(testedFilter.matches(-100));
		assertTrue(testedFilter.matches(10));
		assertFalse(testedFilter.matches(100));
	}

	/**
	 * Tests the {@link org.jage.query.ValueFilters#moreThan(Comparable)} method.
	 */
	@Test
	public void testMoreThan() {
		IValueFilter<Integer> testedFilter = ValueFilters.moreThan(10);

		assertFalse(testedFilter.matches(0));
		assertFalse(testedFilter.matches(-100));
		assertFalse(testedFilter.matches(10));
		assertTrue(testedFilter.matches(100));
	}

	/**
	 * Tests the {@link org.jage.query.ValueFilters#moreOrEqual(Comparable)} method.
	 */
	@Test
	public void testMoreOrEqual() {
		IValueFilter<Integer> testedFilter = ValueFilters.moreOrEqual(10);

		assertFalse(testedFilter.matches(0));
		assertFalse(testedFilter.matches(-100));
		assertTrue(testedFilter.matches(10));
		assertTrue(testedFilter.matches(100));
	}

	/**
	 * Tests the {@link org.jage.query.ValueFilters#anyOf(org.jage.query.IValueFilter[])} method.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testAnyOf() {
		IValueFilter<Object> innerFilter1 = mock(IValueFilter.class);
		when(innerFilter1.matches(any())).thenAnswer(new Answer<Boolean>() {
			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				Object obj = invocation.getArguments()[0];
				return obj instanceof Boolean;
			}
		});

		IValueFilter<Object> innerFilter2 = mock(IValueFilter.class);
		when(innerFilter2.matches(any())).thenAnswer(new Answer<Boolean>() {
			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				Object obj = invocation.getArguments()[0];
				return obj instanceof Integer;
			}
		});

		IValueFilter<Object> testedFilter = ValueFilters.anyOf(innerFilter1, innerFilter2);

		assertTrue(testedFilter.matches(10));
		assertTrue(testedFilter.matches(true));
		assertFalse(testedFilter.matches("test string"));
	}

	/**
	 * Tests the {@link org.jage.query.ValueFilters#allOf} method.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testAllOf() {
		IValueFilter<Object> innerFilter1 = mock(IValueFilter.class);
		when(innerFilter1.matches(any())).thenAnswer(new Answer<Boolean>() {
			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				Object obj = invocation.getArguments()[0];
				return obj instanceof Boolean || obj instanceof String;
			}
		});

		IValueFilter<Object> innerFilter2 = mock(IValueFilter.class);
		when(innerFilter2.matches(any())).thenAnswer(new Answer<Boolean>() {
			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				Object obj = invocation.getArguments()[0];
				return obj instanceof Integer || obj instanceof String;
			}
		});

		IValueFilter<Object> testedFilter = ValueFilters.allOf(innerFilter1, innerFilter2);

		assertFalse(testedFilter.matches(10));
		assertFalse(testedFilter.matches(true));
		assertTrue(testedFilter.matches("test string"));
	}

	/**
	 * Tests the {@link org.jage.query.ValueFilters#fieldValue(java.lang.String, org.jage.query.IValueFilter)} method.
	 */
	@Test
	public void testFieldValue() {
		/**
		 * A utility class.
		 *
		 * @author AGH AgE Team
		 */
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
		IValueFilter<Object> testedFilter = ValueFilters.fieldValue("field", ValueFilters.eq(1));

		assertTrue(testedFilter.matches(new TestingClass(1)));
		assertFalse(testedFilter.matches(new TestingClass(0)));
	}

	/**
	 * Tests the {@link org.jage.query.ValueFilters#any()} method.
	 */
	@Test
	public void testAny() {
		IValueFilter<String> testedFilter = ValueFilters.any();

		assertTrue(testedFilter.matches("testPattern"));
		assertTrue(testedFilter.matches("testPattern 3.14"));
		assertTrue(testedFilter.matches("tsetPattern"));
	}

	/**
	 * Tests the {@link org.jage.query.ValueFilters#key(org.jage.query.IValueFilter)} method.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testKey() {
		IValueFilter<String> keyFilter = mock(IValueFilter.class);
		IValueFilter<Entry<String, ?>> testedFilter = ValueFilters.key(keyFilter);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("string1", new Object());
		map.put("string2", new Object());
		map.put("string3", new Object());

		for (Entry<String, Object> entry : map.entrySet()) {
			testedFilter.matches(entry);
		}

		for (String key : map.keySet()) {
			verify(keyFilter).matches(key);
		}
		verifyNoMoreInteractions(keyFilter);
	}

	/**
	 * Tests the {@link org.jage.query.ValueFilters#value(org.jage.query.IValueFilter)} method.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testValue() {
		IValueFilter<String> valueFilter = mock(IValueFilter.class);
		IValueFilter<Entry<?, String>> testedFilter = ValueFilters.value(valueFilter);

		Map<Object, String> map = new HashMap<Object, String>();
		map.put(new Object(), "string1");
		map.put(new Object(), "string2");
		map.put(new Object(), "string3");

		for (Entry<Object, String> entry : map.entrySet()) {
			testedFilter.matches(entry);
		}

		for (String value : map.values()) {
			verify(valueFilter).matches(value);
		}
		verifyNoMoreInteractions(valueFilter);
	}

	/**
	 * Tests the {@link org.jage.query.ValueFilters#entry(org.jage.query.IValueFilter, org.jage.query.IValueFilter)}
	 * method.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testEntry() {
		IValueFilter<String> keyFilter = mock(IValueFilter.class);
		when(keyFilter.matches(anyString())).thenReturn(true);
		IValueFilter<String> valueFilter = mock(IValueFilter.class);
		IValueFilter<Entry<String, String>> testedFilter = ValueFilters.entry(keyFilter, valueFilter);

		Map<String, String> map = new HashMap<String, String>();
		map.put("string1", "value1");
		map.put("string2", "value2");
		map.put("string3", "value3");

		for (Entry<String, String> entry : map.entrySet()) {
			testedFilter.matches(entry);
		}

		for (Entry<String, String> entry : map.entrySet()) {
			verify(keyFilter).matches(entry.getKey());
			verify(valueFilter).matches(entry.getValue());
		}
		verifyNoMoreInteractions(keyFilter, valueFilter);
	}

}
