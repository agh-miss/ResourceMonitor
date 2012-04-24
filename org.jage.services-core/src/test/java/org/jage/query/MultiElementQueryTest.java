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
 * File: MultiElementQueryTest.java
 * Created: 2011-10-12
 * Author: faber
 * $Id: MultiElementQueryTest.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;

/**
 * Tests for the {@link MultiElementQuery} class.
 * 
 * @author AGH AgE Team
 */
public class MultiElementQueryTest {

	private Collection<QueriedObject> target;

	@Before
	public void setUp() {
		target = new ArrayList<QueriedObject>();
		for (int i = 0; i < 20; i++) {
			target.add(new QueriedObject(i, STRING_VALUE + i));
		}
	}

	/**
	 * Tests a query with a incorrect result class.
	 */
	@Test(expected = QueryException.class)
	public void testQueryWithIncorrectResultClass() {
		MultiElementQuery<?, Collection<?>, Collection<?>> query = new MultiElementQuery<Object, Collection<?>, Collection<?>>(
		        Object.class, Collection.class, Collection.class);

		query.execute(target);
	}

	/**
	 * Tests an empty query - whether it returns the same object.
	 */
	@Test
	public void testEmptyQuery() {
		MultiElementQuery<QueriedObject, Collection<QueriedObject>, Collection<QueriedObject>> query = new MultiElementQuery<QueriedObject, Collection<QueriedObject>, Collection<QueriedObject>>(
		        QueriedObject.class, Collection.class, ArrayList.class);

		Collection<QueriedObject> result = query.execute(target);
		assertEquals(target, result);
	}

	/**
	 * Tests a query with an initial selector.
	 */
	@Test
	public void testQueryWithInitialSelector() {
		MultiElementQuery<QueriedObject, Collection<QueriedObject>, Collection<QueriedObject>> query = new MultiElementQuery<QueriedObject, Collection<QueriedObject>, Collection<QueriedObject>>(
		        QueriedObject.class, Collection.class, ArrayList.class);
		query.from(InitialSelectors.first(10));

		Collection<QueriedObject> result = query.execute(target);
		assertNotSame(target, result);
		assertTrue(result.size() == 10);
	}

	/**
	 * Tests a query with a single value filter (matching).
	 */
	@Test
	public void testQueryWithValueMatcher() {
		MultiElementQuery<QueriedObject, Collection<QueriedObject>, Collection<QueriedObject>> query = new MultiElementQuery<QueriedObject, Collection<QueriedObject>, Collection<QueriedObject>>(
		        QueriedObject.class, Collection.class, ArrayList.class);
		query.matching(INT_VALUE_FIELD, ValueFilters.lessThan(20));

		Collection<QueriedObject> result = query.execute(target);
		assertEquals(target, result);
	}

	/**
	 * Tests a query with a single value filter (non-matching).
	 */
	@Test
	public void testNonmatchingQueryWithValueMatcher() {
		MultiElementQuery<QueriedObject, Collection<QueriedObject>, Collection<QueriedObject>> query = new MultiElementQuery<QueriedObject, Collection<QueriedObject>, Collection<QueriedObject>>(
		        QueriedObject.class, Collection.class, ArrayList.class);
		query.matching(INT_VALUE_FIELD, ValueFilters.eq(-1));

		Collection<QueriedObject> result = query.execute(target);
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	/**
	 * Tests a query that selects only one field from the class.
	 */
	@Test
	public void testQueryWithSingleValueSelector() {
		MultiElementQuery<QueriedObject, Collection<QueriedObject>, List<List<Integer>>> query = new MultiElementQuery<QueriedObject, Collection<QueriedObject>, List<List<Integer>>>(
		        QueriedObject.class, Collection.class, ArrayList.class);
		query.select(INT_VALUE_FIELD);

		List<List<Integer>> result = query.execute(target);
		assertNotNull(result);
		assertEquals(20, result.size());
		for (int i = 0; i < 20; i++) {
			List<Integer> list = result.get(i);
			assertNotNull(list);
			assertEquals(1, list.size());
			assertTrue(list.contains(i));
		}
	}

	/**
	 * Tests a query that selects many fields from the class (but not the class itself).
	 */
	@Test
	public void testQueryWithManyValueSelectors() {
		MultiElementQuery<QueriedObject, Collection<QueriedObject>, List<List<Object>>> query = new MultiElementQuery<QueriedObject, Collection<QueriedObject>, List<List<Object>>>(
		        QueriedObject.class, Collection.class, ArrayList.class);
		query.select(INT_VALUE_FIELD, STRING_VALUE_FIELD);

		List<List<Object>> result = query.execute(target);

		assertNotNull(result);
		assertEquals(20, result.size());
		for (int i = 0; i < 20; i++) {
			List<Object> list = result.get(i);
			assertNotNull(list);
			assertEquals(2, list.size());
			assertTrue(list.contains(i));
			assertTrue(list.contains(STRING_VALUE + i));
		}
	}

	/**
	 * Tests a query with a single function (whether the function is called).
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testQueryWithFunction() {
		MultiElementQuery<QueriedObject, Collection<QueriedObject>, Collection<QueriedObject>> query = new MultiElementQuery<QueriedObject, Collection<QueriedObject>, Collection<QueriedObject>>(
		        QueriedObject.class, Collection.class, ArrayList.class);
		IQueryFunction<Collection<QueriedObject>> queryFunction = mock(IQueryFunction.class);

		query.process(queryFunction);

		query.execute(target);

		verify(queryFunction, only()).execute(target);
	}

	/**
	 * Tests the execution of a query over a IQueryAware target.
	 */
	@SuppressWarnings("unchecked")
	public static void testQueryAwareTarget() {
		MultiElementQuery<Object, Collection<Object>, Collection<Object>> query = new MultiElementQuery<Object, Collection<Object>, Collection<Object>>(
		        Object.class, Collection.class, ArrayList.class);

		IQueryAware<Collection<Object>, Collection<Object>, MultiElementQuery<Object, Collection<Object>, Collection<Object>>> target = mock(
		        IQueryAware.class, withSettings().extraInterfaces(Collection.class));
		query.execute((Collection<Object>)target);

		InOrder inOrder = inOrder(target);
		inOrder.verify(target).beforeExecute(query);
		inOrder.verify(target).afterExecute(query);
	}

	// Helpers

	private static final String INT_VALUE_FIELD = "intValue";

	private static final String STRING_VALUE_FIELD = "stringValue";

	private static final String STRING_VALUE = "Lorem ipsum";

	/**
	 * The helper object.
	 * 
	 * @author AGH AgE Team
	 */
	static class QueriedObject {
		private int intValue;

		private String stringValue;

		public QueriedObject(int intValue, String stringValue) {
			this.intValue = intValue;
			this.stringValue = stringValue;
		}

		public int getIntValue() {
			return intValue;
		}

		public String getStringValue() {
			return stringValue;
		}
	}
}
