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
package org.jage.population;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.jage.population.IPopulation.Tuple;
import org.jage.solution.ISolution;

import static org.jage.population.Populations.newPopulation;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import static com.google.common.collect.Iterables.elementsEqual;

public class PopulationTest {

	private Population<ISolution, Double> population;

	@Test
	public void testAsSolutionListConsistentWithCollectionConstructor() {
		// given
		List<ISolution> solutions = ImmutableList.of(mock(ISolution.class), mock(ISolution.class));
		population = newPopulation(solutions);

		// when
		List<ISolution> asSolutionList = population.asSolutionList();

		// then
		assertTrue(elementsEqual(solutions, asSolutionList));
	}

	@Test
	public void testAsEvaluationListConsistentWithCollectionConstructor() {
		// given
		ISolution solution1 = mock(ISolution.class);
		ISolution solution2 = mock(ISolution.class);
		Double evaluation1 = 2.0;
		Double evaluation2 = 1.0;
		population = newPopulation(ImmutableList.of(solution1, solution2));
		population.setEvaluation(solution2, evaluation2);
		population.setEvaluation(solution1, evaluation1);

		// when
		List<Double> asEvaluationList = population.asEvaluationList();

		// then
		assertThat(asEvaluationList.size(), is(2));
		assertThat(asEvaluationList.get(0), is(evaluation1));
		assertThat(asEvaluationList.get(1), is(evaluation2));
	}

	@Test
	public void testAsTupleListConsistentWithCollectionConstructor() {
		// given
		ISolution solution1 = mock(ISolution.class);
		ISolution solution2 = mock(ISolution.class);
		Double evaluation1 = 2.0;
		Double evaluation2 = 1.0;
		population = newPopulation(ImmutableList.of(solution1, solution2));
		population.setEvaluation(solution2, evaluation2);
		population.setEvaluation(solution1, evaluation1);

		// when
		List<Tuple<ISolution, Double>> asTupleList = population.asTupleList();

		// then
		assertThat(asTupleList.size(), is(2));

		Tuple<ISolution, Double> tuple1 = asTupleList.get(0);
		assertThat(tuple1.getSolution(), is(solution1));
		assertThat(tuple1.getEvaluation(), is(evaluation1));

		Tuple<ISolution, Double> tuple2 = asTupleList.get(1);
		assertThat(tuple2.getSolution(), is(solution2));
		assertThat(tuple2.getEvaluation(), is(evaluation2));
	}

	@Test
	public void testAsMapConsistentWithCollectionConstructor() {
		// given
		ISolution solution1 = mock(ISolution.class);
		ISolution solution2 = mock(ISolution.class);
		Double evaluation1 = 2.0;
		Double evaluation2 = 1.0;
		population = newPopulation(ImmutableList.of(solution1, solution2));
		population.setEvaluation(solution2, evaluation2);
		population.setEvaluation(solution1, evaluation1);

		// when
		Map<ISolution, Double> asMap = population.asMap();

		// then
		assertThat(asMap.size(), is(2));
		Iterator<Entry<ISolution, Double>> iter = asMap.entrySet().iterator();

		Entry<ISolution, Double> entry1 = iter.next();
		assertThat(entry1.getKey(), is(solution1));
		assertThat(entry1.getValue(), is(evaluation1));

		Entry<ISolution, Double> entry2 = iter.next();
		assertThat(entry2.getKey(), is(solution2));
		assertThat(entry2.getValue(), is(evaluation2));
	}

	@Test
	public void testAsSolutionListConsistentWithMapConstructor() {
		// given
		ISolution solution1 = mock(ISolution.class);
		ISolution solution2 = mock(ISolution.class);
		Double evaluation1 = 2.0;
		Double evaluation2 = 1.0;
		Map<ISolution, Double> map = ImmutableMap.of(solution1, evaluation1, solution2, evaluation2);
		population = newPopulation(map);

		// when
		List<ISolution> asSolutionList = population.asSolutionList();

		// then
		assertTrue(elementsEqual(map.keySet(), asSolutionList));
	}

	@Test
	public void testAsEvaluationListConsistentWithMapConstructor() {
		// given
		ISolution solution1 = mock(ISolution.class);
		ISolution solution2 = mock(ISolution.class);
		Double evaluation1 = 2.0;
		Double evaluation2 = 1.0;
		Map<ISolution, Double> map = ImmutableMap.of(solution1, evaluation1, solution2, evaluation2);
		population = newPopulation(map);

		// when
		List<Double> asEvaluationList = population.asEvaluationList();

		// then
		assertTrue(elementsEqual(map.values(), asEvaluationList));
	}

	@Test
	public void testAsTupleListConsistentWithMapConstructor() {
		// given
		ISolution solution1 = mock(ISolution.class);
		ISolution solution2 = mock(ISolution.class);
		Double evaluation1 = 2.0;
		Double evaluation2 = 1.0;
		Map<ISolution, Double> map = ImmutableMap.of(solution1, evaluation1, solution2, evaluation2);
		population = newPopulation(map);

		// when
		List<Tuple<ISolution, Double>> asTupleList = population.asTupleList();

		// then
		assertThat(asTupleList.size(), is(2));
		Iterator<Entry<ISolution, Double>> iter = map.entrySet().iterator();

		Entry<ISolution, Double> entry1 = iter.next();
		Tuple<ISolution, Double> tuple1 = asTupleList.get(0);
		assertEquals(entry1.getKey(), tuple1.getSolution());
		assertEquals(entry1.getValue(), tuple1.getEvaluation());

		Entry<ISolution, Double> entry2 = iter.next();
		Tuple<ISolution, Double> tuple2 = asTupleList.get(1);
		assertEquals(entry2.getKey(), tuple2.getSolution());
		assertEquals(entry2.getValue(), tuple2.getEvaluation());
	}

	@Test
	public void testAsMapConsistentWithMapConstructor() {
		// given
		ISolution solution1 = mock(ISolution.class);
		ISolution solution2 = mock(ISolution.class);
		Double evaluation1 = 2.0;
		Double evaluation2 = 1.0;
		Map<ISolution, Double> map = ImmutableMap.of(solution1, evaluation1, solution2, evaluation2);
		population = newPopulation(map);

		// when
		Map<ISolution, Double> asMap = population.asMap();

		// then
		assertEquals(map, asMap);
	}

}
