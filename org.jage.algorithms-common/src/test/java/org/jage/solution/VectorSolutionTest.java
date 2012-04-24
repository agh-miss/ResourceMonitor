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
 * File: VectorSolutionTest.java
 * Created: 2011-10-20
 * Author: Krzywicki
 * $Id: VectorSolutionTest.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.solution;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for VectorSolution.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class VectorSolutionTest {

	@Mock
	private List<Double> representation;

	private VectorSolution<Double> solution;

	@Before
	public void setUp() {
		solution = new VectorSolution<Double>(representation);
	}

	@Test
	public void testGetRepresentation() {
		assertThat(solution.getRepresentation(), is(equalTo(representation)));
	}
}
