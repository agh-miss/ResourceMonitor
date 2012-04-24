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
 * File: GenericQueryTest.java
 * Created: 2011-10-06
 * Author: faber
 * $Id: GenericQueryTest.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.query;

import java.util.Collection;

import org.junit.Test;
import org.mockito.InOrder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

/**
 * Tests for the {@link GenericQuery} class.
 * 
 * @author AGH AgE Team
 */
@SuppressWarnings("static-method")
public class GenericQueryTest {

	/**
	 * Tests an empty query - whether it returns the same object.
	 */
	@Test
	public void testEmptyQuery() {
		GenericQuery<QueriedObject, QueriedObject> query = new GenericQuery<QueriedObject, QueriedObject>(
		        QueriedObject.class, QueriedObject.class);
		QueriedObject target = new QueriedObject();

		QueriedObject result = query.execute(target);
		assertEquals(target, result);
	}

	/**
	 * Tests a query with a single value filter (matching).
	 */
	@Test
	public void testQueryWithValueMatcher() {
		GenericQuery<QueriedObject, QueriedObject> query = new GenericQuery<QueriedObject, QueriedObject>(
		        QueriedObject.class, QueriedObject.class);
		query.matching(INT_VALUE_FIELD, ValueFilters.eq(INT_VALUE));
		QueriedObject target = new QueriedObject();

		QueriedObject result = query.execute(target);
		assertEquals(target, result);
	}

	/**
	 * Tests a query with a single value filter (non-matching).
	 */
	@Test
	public void testNonmatchingQueryWithValueMatcher() {
		GenericQuery<QueriedObject, QueriedObject> query = new GenericQuery<QueriedObject, QueriedObject>(
		        QueriedObject.class, QueriedObject.class);
		query.matching(INT_VALUE_FIELD, ValueFilters.eq(0));
		QueriedObject target = new QueriedObject();

		QueriedObject result = query.execute(target);
		assertEquals(null, result);
	}

	/**
	 * Tests a query that selects only one field from the class.
	 */
	@Test
	public void testQueryWithSingleValueSelector() {
		GenericQuery<QueriedObject, Integer> query = new GenericQuery<QueriedObject, Integer>(QueriedObject.class,
		        Integer.class);
		query.select(INT_VALUE_FIELD);
		QueriedObject target = new QueriedObject();

		Integer result = query.execute(target);
		assertEquals(new Integer(12), result);
	}

	/**
	 * Tests a query that selects many fields from the class (but not the class itself).
	 */
	@Test
	public void testQueryWithManyValueSelectors() {
		GenericQuery<QueriedObject, Collection<Object>> query = new GenericQuery<QueriedObject, Collection<Object>>(
		        QueriedObject.class, Collection.class);
		query.select(INT_VALUE_FIELD, STRING_VALUE_FIELD);
		QueriedObject target = new QueriedObject();

		Collection<Object> result = query.execute(target);

		assertNotNull(result);
		assertEquals(2, result.size());
		assertTrue(result.contains(new Integer(INT_VALUE)));
		assertTrue(result.contains(STRING_VALUE));
	}

	/**
	 * Tests a query with a single function (whether the function is called).
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testQueryWithFunction() {
		GenericQuery<QueriedObject, QueriedObject> query = new GenericQuery<QueriedObject, QueriedObject>(
		        QueriedObject.class, QueriedObject.class);
		IQueryFunction<QueriedObject> queryFunction = mock(IQueryFunction.class);
		QueriedObject target = new QueriedObject();

		query.process(queryFunction);

		query.execute(target);

		verify(queryFunction, only()).execute(target);
	}

	/**
	 * Tests the execution of a query over a IQueryAware target.
	 */
	@SuppressWarnings("unchecked")
	public void testQueryAwareTarget() {
		GenericQuery<Object, Object> query = new GenericQuery<Object, Object>(Object.class, Object.class);

		IQueryAware<Object, Object, GenericQuery<Object, Object>> target = mock(IQueryAware.class);
		query.execute(target);

		InOrder inOrder = inOrder(target);
		inOrder.verify(target).beforeExecute(query);
		inOrder.verify(target).afterExecute(query);
	}

	// Helpers

	private static final String INT_VALUE_FIELD = "intValue";

	private static final String STRING_VALUE_FIELD = "stringValue";

	private static final int INT_VALUE = 12;

	private static final String STRING_VALUE = "Lorem ipsum";

	/**
	 * The helper object.
	 * 
	 * @author AGH AgE Team
	 */
	static class QueriedObject {
		private int intValue = INT_VALUE;

		private String stringValue = STRING_VALUE;

		public int getIntValue() {
			return intValue;
		}

		public String getStringValue() {
			return stringValue;
		}
	}
}
