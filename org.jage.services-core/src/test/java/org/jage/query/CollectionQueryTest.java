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
 * $Id: CollectionQueryTest.java 76 2012-02-27 20:55:05Z faber $
 */

package org.jage.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyCollection;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jage.agent.IAgent;
import org.jage.agent.ISimpleAggregate;
import org.jage.agent.SimpleAgent;
import org.jage.agent.SimpleAggregate;

import static org.jage.query.ValueFilters.eq;
import static org.jage.query.ValueFilters.not;

/**
 * Tests for the {@link CollectionQuery} class.
 * 
 * @author AGH AgE Team
 */
public class CollectionQueryTest {

	private static final String FULL_STRING = "lorem ipsum dolor sit amet";

	private List<String> target;

	private CollectionQuery<String, String> query;

	@Before
	public void setUp() {
		query = new CollectionQuery<String, String>(String.class, Collection.class, ArrayList.class);

		target = new ArrayList<String>();
		target.add("lorem");
		target.add("ipsum");
		target.add("dolor");
		target.add("sit");
		target.add("amet");
	}

	/**
	 * Tests an empty query.
	 */
	@Test
	public void testEmptyQuery() {
		Collection<String> result = query.execute(target);
		assertEquals(target, result);
	}

	/**
	 * Tests a query with a single initial selector.
	 */
	@Test
	public void testInitialSelectors() {
		IInitialSelector initialSelectorMock = mock(IInitialSelector.class);
		when(initialSelectorMock.include()).thenReturn(true, true, true, false, false);

		List<String> expected = target.subList(0, 3);

		query.from(initialSelectorMock);
		Collection<String> result = query.execute(target);
		assertEquals(expected, result);

		verify(initialSelectorMock).initialise(target.size());
	}

	/**
	 * Tests a query with a single value filter.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testValueFilters() {
		IValueFilter<String> valueFilterMock = mock(IValueFilter.class);
		when(valueFilterMock.matches(anyString())).thenReturn(true);
		when(valueFilterMock.matches(target.get(0))).thenReturn(false);

		List<String> expected = target.subList(1, target.size());

		query.matching(valueFilterMock);
		Collection<String> result = query.execute(target);
		assertEquals(expected, result);
	}

	/**
	 * Tests a query with a single query function.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testQueryFunctions() {
		List<String> expected = new ArrayList<String>();
		expected.add(FULL_STRING);

		IQueryFunction<Collection<String>> queryFunctionMock = mock(IQueryFunction.class);
		when(queryFunctionMock.execute(anyCollection())).thenReturn(expected);

		query.process(queryFunctionMock);
		Collection<String> result = query.execute(target);
		assertEquals(expected, result);
	}

	/**
	 * Tests a full query.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testFullQuery() {
		// Expected values
		List<String> expectedAfterInitialSelector = target.subList(0, 3);
		List<String> expectedAfterValueFilter = expectedAfterInitialSelector.subList(1,
		        expectedAfterInitialSelector.size());
		List<String> expectedAfterExecute = new ArrayList<String>();
		expectedAfterExecute.add(FULL_STRING);

		// Mocks
		IInitialSelector initialSelectorMock = mock(IInitialSelector.class);
		when(initialSelectorMock.include()).thenReturn(true, true, true, false, false);

		IValueFilter<String> valueFilterMock = mock(IValueFilter.class);
		when(valueFilterMock.matches(anyString())).thenReturn(true);
		when(valueFilterMock.matches(expectedAfterInitialSelector.get(0))).thenReturn(false);

		IQueryFunction<Collection<String>> queryFunctionMock = mock(IQueryFunction.class);
		when(queryFunctionMock.execute(expectedAfterValueFilter)).thenReturn(expectedAfterExecute);

		// Execution
		query.from(initialSelectorMock);
		query.matching(valueFilterMock);
		query.process(queryFunctionMock);
		Collection<String> result = query.execute(target);
		assertEquals(expectedAfterExecute, result);
	}
}
