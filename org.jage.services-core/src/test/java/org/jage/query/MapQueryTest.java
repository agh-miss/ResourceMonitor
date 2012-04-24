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
 * File: CollectionQueryTest.java
 * Created: 2011-09-19
 * Author: faber
 * $Id: MapQueryTest.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.query;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for the {@link CollectionQuery} class.
 * 
 * @author AGH AgE Team
 */
public class MapQueryTest {

	private static final String FULL_STRING = "lorem ipsum dolor sit amet";

	private Map<String, Integer> target;

	private MapQuery<String, Integer> query;

	@Before
	public void setUp() {
		query = new MapQuery<String, Integer>(HashMap.class);

		target = new HashMap<String, Integer>();
		target.put("lorem", 2);
		target.put("ipsum", 4);
		target.put("dolor", 8);
		target.put("sit", 16);
		target.put("amet", 32);
	}

	/**
	 * Tests an empty query.
	 */
	@Test
	public void testEmptyQuery() {
		Map<String, Integer> result = query.execute(target);
		assertEquals(target, result);
	}

	/**
	 * Tests a query with a single initial selector.
	 */
	@Test
	public void testInitialSelectors() {
		IInitialSelector initialSelectorMock = mock(IInitialSelector.class);
		when(initialSelectorMock.include()).thenReturn(true, true, true, false, false);

		// Map is unsorted, so we need to iterate over data manually
		Map<String, Integer> expected = new HashMap<String, Integer>();
		int i = 0;
		for (Entry<String, Integer> entry : target.entrySet()) {
			if (i < 3) {
				expected.put(entry.getKey(), entry.getValue());
			}
			i++;
		}

		query.from(initialSelectorMock);
		Map<String, Integer> result = query.execute(target);
		assertEquals(expected, result);

		verify(initialSelectorMock).initialise(target.size());
	}

	/**
	 * Tests a query with a single value filter.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testValueFilters() {
		IValueFilter<Entry<String, Integer>> valueFilterMock = mock(IValueFilter.class);
		when(valueFilterMock.matches(any(Entry.class))).thenAnswer(new Answer<Boolean>() {
			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				Entry<String, Integer> entry = (Entry<String, Integer>)invocation.getArguments()[0];
				return entry.getValue() < 10;
			}
		});

		Map<String, Integer> expected = new HashMap<String, Integer>();
		for (Entry<String, Integer> entry : target.entrySet()) {
			if (entry.getValue() < 10) {
				expected.put(entry.getKey(), entry.getValue());
			}
		}

		query.matching(valueFilterMock);
		Map<String, Integer> result = query.execute(target);
		assertEquals(expected, result);
	}

	/**
	 * Tests a query with a single query function.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testQueryFunctions() {
		Map<String, Integer> expected = new HashMap<String, Integer>();
		expected.put(FULL_STRING, 1024);

		IQueryFunction<Map<String, Integer>> queryFunctionMock = mock(IQueryFunction.class);
		when(queryFunctionMock.execute(anyMap())).thenReturn(expected);

		query.process(queryFunctionMock);
		Map<String, Integer> result = query.execute(target);
		assertEquals(expected, result);
	}

	/**
	 * Tests a full query.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testFullQuery() {
		// Expected values
		// Map is unsorted, so we need to iterate over data manually
		Map<String, Integer> expectedAfterInitialSelector = new HashMap<String, Integer>();
		int i = 0;
		for (Entry<String, Integer> entry : target.entrySet()) {
			if (i < 3) {
				expectedAfterInitialSelector.put(entry.getKey(), entry.getValue());
			}
			i++;
		}

		Map<String, Integer> expectedAfterValueFilter = new HashMap<String, Integer>();
		for (Entry<String, Integer> entry : target.entrySet()) {
			if (entry.getValue() < 8) {
				expectedAfterValueFilter.put(entry.getKey(), entry.getValue());
			}
			i++;
		}

		Map<String, Integer> expectedAfterExecute = new HashMap<String, Integer>();
		expectedAfterExecute.put(FULL_STRING, 1024);

		// Mocks
		IInitialSelector initialSelectorMock = mock(IInitialSelector.class);
		when(initialSelectorMock.include()).thenReturn(true, true, true, false, false);

		IValueFilter<Entry<String, Integer>> valueFilterMock = mock(IValueFilter.class);
		when(valueFilterMock.matches(any(Entry.class))).thenAnswer(new Answer<Boolean>() {
			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				Entry<String, Integer> entry = (Entry<String, Integer>)invocation.getArguments()[0];
				return entry.getValue() < 8;
			}
		});

		IQueryFunction<Map<String, Integer>> queryFunctionMock = mock(IQueryFunction.class);
		when(queryFunctionMock.execute(expectedAfterValueFilter)).thenReturn(expectedAfterExecute);

		// Execution
		query.from(initialSelectorMock);
		query.matching(valueFilterMock);
		query.process(queryFunctionMock);
		Map<String, Integer> result = query.execute(target);
		assertEquals(expectedAfterExecute, result);
	}
}
