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
 * File: WorkplaceStepQueryResultCacheTest.java
 * Created: 20 Sep 2011
 * Author: nnidyu
 * $Id: WorkplaceStepQueryResultCacheTest.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.query.cache;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.jage.property.InvalidPropertyPathException;
import org.jage.property.Property;
import org.jage.query.IQuery;
import org.jage.workplace.IWorkplace;

/**
 * Tests for {@link WorkplaceStepQueryResultCache} class.
 * 
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class WorkplaceStepQueryResultCacheTest {

	private static final int REFRESH_AGE = 10;

	private IQuery<Object, Object> proxiedQuery;

	@Mock
	private IWorkplace workplace;

	@Mock
	private Property stepProperty;

	@Before
	public void setUp() throws InvalidPropertyPathException {
		when(workplace.getProperty("step")).thenReturn(stepProperty);
		proxiedQuery = new IQuery<Object, Object>() {
			@Override
			public Object execute(Object target) {
				return new Object();
			}
		};
	}

	/**
	 * Tests a cache-based execution of a query.
	 */
	@Test
	public void testExecute() {
		WorkplaceStepQueryResultCache<Object, Object> cache = new WorkplaceStepQueryResultCache<Object, Object>(
		        proxiedQuery, REFRESH_AGE);

		cache.init(workplace);
		Object target1 = new Object();
		Object expected1 = null;
		Object target2 = new Object();
		Object expected2 = null;
		for (long step = 0L; step < 100L; step++) {
			when(stepProperty.getValue()).thenReturn(step);
			System.out.println(step);
			if (step % REFRESH_AGE == 0) {
				expected1 = cache.execute(target1);
				expected2 = cache.execute(target2);
			} else {
				assertEquals(expected1, cache.execute(target1));
				assertEquals(expected2, cache.execute(target2));
			}
		}
	}
}
