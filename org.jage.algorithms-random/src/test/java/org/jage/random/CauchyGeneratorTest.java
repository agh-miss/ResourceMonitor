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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Tests for CauchyGenerator.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class CauchyGeneratorTest {

	@Mock
	private INormalizedDoubleRandomGenerator rand;

	@InjectMocks
	private CauchyGenerator underTest = new CauchyGenerator();

	@Test
	public void testBounds() {
		assertThat(underTest.getLowerDouble(), is(equalTo(Double.MIN_VALUE)));
		assertThat(underTest.getUpperDouble(), is(equalTo(Double.MAX_VALUE)));
	}

	@Test
	public void testNextDouble() {
		// given
		given(rand.nextDouble()).willReturn(0.0, 0.5, 1.0);

		// then
		assertThat(underTest.nextDouble(), is(equalTo(Math.tan(Math.PI * -0.5))));
		assertThat(underTest.nextDouble(), is(equalTo(Math.tan(0.0))));
		assertThat(underTest.nextDouble(), is(equalTo(Math.tan(Math.PI * 0.5))));
	}
}
