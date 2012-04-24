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
 * File: Populations.java
 * Created: 2011-12-19
 * Author: Krzywicki
 * $Id: Populations.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.population;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static java.util.Collections.singletonMap;

import org.jage.solution.ISolution;

/**
 * This class consists exclusively of static utility or syntactic sugar methods that return populations.
 *
 * @author AGH AgE Team
 */
public class Populations {

	@SuppressWarnings("rawtypes")
	private static final IPopulation EMPTY_POPULATION = newPopulation(Collections.<ISolution> emptyList());

	/**
	 * Returns the empty population (immutable).
	 * <p>
	 * This example illustrates the type-safe way to obtain an empty population:
	 *
	 * <pre>
	 * IPopulation&lt;ISolution&gt; s = Collections.emptyList();
	 * </pre>
	 *
	 * @param <S>
	 *            the type of solution
	 * @param <E>
	 *            the type of evaluation
	 *
	 * @return the empty population
	 */
	@SuppressWarnings("unchecked")
	public static <S extends ISolution, E> IPopulation<S, E> emptyPopulation() {
		return EMPTY_POPULATION;
	}

	/**
	 * Returns an immutable population containing only the specified solution. The solution has a null evaluation.
	 *
	 * @param <S>
	 *            the type of solution
	 * @param <E>
	 *            the type of evaluation
	 *
	 * @param solution
	 *            the sole solution to be stored in the returned population.
	 * @return an immutable population containing only the specified solution.
	 */
	public static <S extends ISolution, E> IPopulation<S, E> singletonPopulation(S solution) {
		return singletonPopulation(solution, null);
	}

	/**
	 * Returns an immutable population containing only the specified solution, with a given evaluation.
	 *
	 * @param <S>
	 *            the type of solution
	 * @param <E>
	 *            the type of evaluation
	 *
	 * @param solution
	 *            the sole solution to be stored in the returned population.
	 * @param evaluation
	 *            the sole solution's evaluation
	 * @return an immutable population containing only the specified solution.
	 */
	public static <S extends ISolution, E> IPopulation<S, E> singletonPopulation(S solution, E evaluation) {
		return newPopulation(singletonMap(solution, evaluation));
	}

	/**
	 * Creates an immutable {@code Population} instance, containing the given solutions, each with null evaluation.
	 *
	 * @param <S>
	 *            the type of solution
	 * @param <E>
	 *            the type of evaluation
	 *
	 * @param solutions
	 *            the solutions that the population should contain, in order
	 * @return a new {@code Population} containing those solutions
	 */
	public static <S extends ISolution, E> Population<S, E> newPopulation(Collection<? extends S> solutions) {
		return new Population<S, E>(solutions);
	}

	/**
	 * Creates an immutable {@code Population} instance, containing the given solutions, mapped to the given
	 * evaluations.
	 *
	 * @param <S>
	 *            the type of solution
	 * @param <E>
	 *            the type of evaluation
	 *
	 * @param map
	 *            a mapping between solutions, that the population should contain, and their evaluations
	 * @return a new {@code Population} containing those solutions mapped to those evaluations
	 */
	public static <S extends ISolution, E> Population<S, E> newPopulation(Map<? extends S, E> map) {
		return new Population<S, E>(map);
	}
}
