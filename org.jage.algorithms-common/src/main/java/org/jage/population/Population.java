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
 * File: Population.java
 * Created: 2011-10-20
 * Author: Krzywicki
 * $Id: Population.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.population;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;

import org.jage.solution.ISolution;

import com.google.common.collect.ImmutableList;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayListWithCapacity;
import static com.google.common.collect.Maps.newLinkedHashMap;

/**
 * Default implementation of {@link IPopulation}.
 *
 * @param <S>
 *            The type of solutions held in this population
 * @param <E>
 *            the type of evaluation
 *
 * @author AGH AgE Team
 */
public final class Population<S extends ISolution, E> implements IPopulation<S, E> {

	private final Map<S, E> evaluationMap;

	// Immutable list view of the solutions, precomputed for efficiency
	private final List<S> solutionList;

	/**
	 * Creates a population containing the specified solutions and no evaluations.
	 *
	 * @param solutions
	 *            the solutions of this population
	 * @throws NullPointerException
	 *             if any of {@code solutions} is null
	 */
	public Population(Collection<? extends S> solutions) {
		this.evaluationMap = newLinkedHashMap();
		for(S solution: solutions) {
			evaluationMap.put(solution, null);
		}
		this.solutionList = ImmutableList.copyOf(evaluationMap.keySet());
	}

	/**
	 * Creates a population containing the specified solutions mapped to evaluations.
	 *
	 * @param map
	 *            a mapping between solutions and evaluations
	 * @throws NullPointerException
	 *             if any of the solutions in {@code map} is null
	 */
	public Population(Map<? extends S, E> map) {
		this.evaluationMap = newLinkedHashMap(map);
		this.solutionList = ImmutableList.copyOf(evaluationMap.keySet());
	}

	@Override
	public boolean contains(S solution) {
		return evaluationMap.containsKey(checkNotNull(solution));
	}

	@Override
	public E getEvaluation(S solution) {
		return evaluationMap.get(checkContains(solution));
	}

	@Override
	public E setEvaluation(S solution, E evaluation) {
		return setEvaluation0(checkContains(solution), evaluation);
	}

	@Override
	public void setAllEvaluations(Map<S, E> map) {
		for (S solution : map.keySet()) {
			checkContains(solution);
		}
		for (Entry<S, E> e : map.entrySet()) {
			setEvaluation0(e.getKey(), e.getValue());
		}
	}

	private E setEvaluation0(S solution, E evaluation) {
		return evaluationMap.put(solution, evaluation);
	}

	@Override
	public List<S> asSolutionList() {
		return solutionList;
	}

	@Override
	public List<E> asEvaluationList() {
		return ImmutableList.copyOf(evaluationMap.values());
	}

	@Override
	public List<Tuple<S, E>> asTupleList() {
		List<Tuple<S, E>> tuplesList = newArrayListWithCapacity(size());
		for(Entry<S, E> e : evaluationMap.entrySet()) {
			tuplesList.add(new Tuple<S, E>(e.getKey(), e.getValue()));
		}
		return unmodifiableList(tuplesList);
	}

	@Override
	public Map<S, E> asMap() {
//		ImmutableMap.copyOf(evaluationMap)
		return unmodifiableMap(evaluationMap);
	}

	@Override
	public ListIterator<S> iterator() {
		return solutionList.listIterator();
	}

	@Override
	public boolean isEmpty() {
		return solutionList.isEmpty();
	}

	@Override
	public int size() {
		return solutionList.size();
	}

	private S checkContains(S solution) {
		if (!contains(solution)) {
			throw new NoSuchElementException(solution + " does not belong to this population");
		}
		return solution;
	}
}
