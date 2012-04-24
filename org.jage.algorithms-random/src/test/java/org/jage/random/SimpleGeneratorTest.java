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
/**
 * @author Tomasz Sławek & Marcin Świątek
 */
package org.jage.random;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for SimpleGenerator.
 *
 * @author AGH AgE Team
 */
public class SimpleGeneratorTest {

	private long seed = 1234567890L;

	private SimpleGenerator generator;

	@Before
	public void setUp() throws Exception {
		generator = new SimpleGenerator(seed);
	}

	@Test
	public void testBounds() {
		assertThat(generator.getLowerDouble(), is(equalTo(0.0)));
		assertThat(generator.getUpperDouble(), is(equalTo(1.0)));

		assertThat(generator.getLowerInt(), is(equalTo(Integer.MIN_VALUE)));
		assertThat(generator.getUpperInt(), is(equalTo(Integer.MAX_VALUE)));
	}

	@Test
	public void testSeed() {
		// given
		SimpleGenerator generator2 = new SimpleGenerator(seed);

		// then
		assertThat(generator.nextInt(), is(equalTo(generator2.nextInt())));
		assertThat(generator.nextDouble(), is(equalTo(generator2.nextDouble())));
	}

	@Test
	public void testNextDouble() {
		// given
		Random rand = new Random(seed);

		// then
		for (int i = 0; i < 10000; i++) {
			assertThat(generator.nextDouble(), is(equalTo(rand.nextDouble())));
		}
	}

	@Test
	public void testNextInt() {
		// given
		Random rand = new Random(seed);

		// then
		for (int i = 0; i < 10000; i++) {
			assertThat(generator.nextInt(), is(equalTo(rand.nextInt())));
		}
	}
}
