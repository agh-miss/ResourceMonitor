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
 * File: CumulatedDistributionTest.java
 * Created: 2011-05-28
 * Author: Krzywicki
 * $Id: CumulativeDistributionTest.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link CumulativeDistribution}.
 *
 * @author AGH AgE Team
 */
public class CumulativeDistributionTest {

	@Test
	public void behavesProperlyOnSingleData() {
		// given
		double[] data = new double[] { 1.0 };
		CumulativeDistribution distribution = new CumulativeDistribution(data);

		// then
		assertEquals(0, distribution.getValueFor(0.0));
		assertEquals(0, distribution.getValueFor(0.5));
		assertEquals(0, distribution.getValueFor(1.0));
	}

	@Test
	public void behavesProperlyOnMultipleData() {
		// given
		double[] data = new double[] { 0.0, 0.5, 1.0 };
		CumulativeDistribution distribution = new CumulativeDistribution(data);

		// then
		assertEquals(0, distribution.getValueFor(0.0));
		assertEquals(1, distribution.getValueFor(0.25));
		assertEquals(1, distribution.getValueFor(0.5));
		assertEquals(2, distribution.getValueFor(0.75));
		assertEquals(2, distribution.getValueFor(1.0));
	}

	@Test
	public void behavesProperlyWhenDataDoesNotStartOnZero() {
		// given
		double[] data = new double[] { 0.5, 1.0 };
		CumulativeDistribution distribution = new CumulativeDistribution(data);

		// then
		assertEquals(0, distribution.getValueFor(0.0));
		assertEquals(0, distribution.getValueFor(0.25));
		assertEquals(0, distribution.getValueFor(0.5));
		assertEquals(1, distribution.getValueFor(0.75));
		assertEquals(1, distribution.getValueFor(1.0));
	}
}
