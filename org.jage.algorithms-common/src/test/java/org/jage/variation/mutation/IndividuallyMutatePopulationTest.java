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
 * File: IndividuallyMutatePopulationTest.java
 * Created: 2011-10-20
 * Author: Krzywicki
 * $Id: IndividuallyMutatePopulationTest.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.variation.mutation;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.jage.population.IPopulation;
import org.jage.random.INormalizedDoubleRandomGenerator;
import org.jage.solution.ISolution;

import static org.jage.population.Populations.newPopulation;
import static org.jage.variation.mutation.IndividuallyMutatePopulation.DEFAULT_CHANCE_TO_MUTATE;

/**
 * Tests for IndividuallyMutatePopulation.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class IndividuallyMutatePopulationTest {

	@Mock
	private INormalizedDoubleRandomGenerator rand;

	@Mock
	private IMutateSolution<ISolution> mutate;

	@InjectMocks
	private IndividuallyMutatePopulation<ISolution> underTest = new IndividuallyMutatePopulation<ISolution>();

	@Test
	public void shouldMutateOnLower() {
		// given
		List<ISolution> solutions = Arrays.asList(mock(ISolution.class), mock(ISolution.class));
		IPopulation<ISolution, Object> population = newPopulation(solutions);
		given(rand.nextDouble()).willReturn(0.9 * DEFAULT_CHANCE_TO_MUTATE);

		// when
		underTest.mutatePopulation(population);

		// then
		verify(mutate, times(2)).mutateSolution(any(ISolution.class));
	}

	@Test
	public void shouldNotMutateOnEqual() {
		// given
		List<ISolution> solutions = Arrays.asList(mock(ISolution.class), mock(ISolution.class));
		IPopulation<ISolution, Object> population = newPopulation(solutions);
		given(rand.nextDouble()).willReturn(DEFAULT_CHANCE_TO_MUTATE);

		// when
		underTest.mutatePopulation(population);

		// then

		verifyZeroInteractions(mutate);
	}

	@Test
	public void shouldNotMutateOnBigger() {
		// given
		List<ISolution> solutions = Arrays.asList(mock(ISolution.class), mock(ISolution.class));
		IPopulation<ISolution, Object> population = newPopulation(solutions);
		given(rand.nextDouble()).willReturn(1.1 * DEFAULT_CHANCE_TO_MUTATE);

		// when
		underTest.mutatePopulation(population);

		// then

		verifyZeroInteractions(mutate);
	}
}
