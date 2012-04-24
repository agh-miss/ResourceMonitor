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
 * File: ParallelProblemTest.java
 * Created: 2011-10-20
 * Author: Krzywicki
 * $Id: ParallelProblemTest.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.problem;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for ParralelProblem.
 *
 * @author AGH AgE Team
 */
public class ParallelProblemTest {

	private ParallelProblem<Integer> problem;
	private int dimension = 3;
	private Integer min = -1;
	private Integer max = 2;

	@Before
	public void setUp() throws Exception {
		problem = new ParallelProblem<Integer>(dimension, min, max);
	}

	@Test
	public void testDimension() {
		Assert.assertEquals(dimension, problem.getDimension());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeLowerBound() {
		problem.lowerBound(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTooHighLowerBound() {
		problem.upperBound(dimension);
	}

	@Test
	public void testLowerBound() {
		Assert.assertEquals(min, problem.lowerBound(0));
		Assert.assertEquals(min, problem.lowerBound(1));
		Assert.assertEquals(min, problem.lowerBound(2));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeUpperBound() {
		problem.upperBound(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTooHighUpperBound() {
		problem.upperBound(dimension);
	}

	@Test
	public void testUpperBound() {
		Assert.assertEquals(max, problem.upperBound(0));
		Assert.assertEquals(max, problem.upperBound(1));
		Assert.assertEquals(max, problem.upperBound(2));
	}
}
