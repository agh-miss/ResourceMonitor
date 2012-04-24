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
 * File: IPopulation.java
 * Created: 2011-10-20
 * Author: Krzywicki
 * $Id: IPopulation.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.population;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.jage.solution.ISolution;

/**
 * An interface for a collection of solutions, along with their evaluations.
 * <p>
 * {@code IPopulation}s aggregate solutions, so a given solution may belong to multiple populations, along with multiple
 * evaluations. Thus, population instances define the context an evaluation.
 * <p>
 * Populations should nor allow null solutions.
 *
 * @param <S>
 *            The type of solutions held in this population
 * @param <E>
 *            The type of evaluation
 * @author AGH AgE Team
 */
public interface IPopulation<S extends ISolution, E> extends Iterable<S> {

	/**
	 * Returns <tt>true</tt> if this population contains the specified solution.
	 *
	 * @param solution
	 *            the solution whose presence in this population is to be tested
	 * @return <tt>true</tt> if this population contains the specified solution
	 * @throws NullPointerException
	 *             if the specified solution is null
	 */
	public boolean contains(S solution);

	/**
	 * Returns the evaluation of the specified solution, or null if there is none.
	 *
	 * @param solution
	 *            the solution whose evaluation is to be returned
	 * @return {@code solution}'s evaluation or null if there is none
	 * @throws NullPointerException
	 *             if {@code solution} is null
	 * @throws NoSuchElementException
	 *             if {@code solution} does not belong to this population.
	 */
	public E getEvaluation(S solution) throws NoSuchElementException;

	/**
	 * Sets the evaluation of the specified solution.
	 *
	 * @param solution
	 *            the solution whose evaluation is to be set
	 * @param evaluation
	 *            the evaluation value
	 * @return {@code solution}'s previous evaluation, or null if there was none
	 * @throws NullPointerException
	 *             if {@code solution} is null
	 * @throws NoSuchElementException
	 *             if {@code solution} does not belong to this population.
	 */
	public E setEvaluation(S solution, E evaluation) throws NoSuchElementException;

	/**
	 * Sets the evaluations of multiple solutions, according to the specified mapping.
	 * <p>
	 * If any solution in the given map does not belong to the population or is null, a corresponding exception is
	 * thrown and the previous evaluations are left unmodified (transactional behavior).
	 *
	 * @param map
	 *            a mapping between solutions and evaluations
	 * @throws NullPointerException
	 *             if any solution in {@code map} is null
	 * @throws NoSuchElementException
	 *             if any solution in {@code map} does not belong to this population.
	 */
	public void setAllEvaluations(Map<S, E> map) throws NoSuchElementException;

	/**
	 * Returns an immutable list view of this population's solutions.
	 * <p>
	 * The returned list order is consistent with those returned by {@code evaluationList()} and {@code tuplesList()}
	 *
	 * @return an immutable list view of this population's solution
	 */
	public List<S> asSolutionList();

	/**
	 * Returns an immutable list view of this population's solutions' evaluations.
	 * <p>
	 * The returned list order is consistent with those returned by {@code solutionList()} and {@code tuplesList()}
	 *
	 * @return an immutable list view of this population's solutions' evaluations
	 */
	public List<E> asEvaluationList();

	/**
	 * Returns an immutable list view of this population's solution-evaluation tuples.
	 * <p>
	 * The returned list order is consistent with those returned by {@code solutionList()} and {@code evaluationList()}
	 *
	 * @return an immutable list view of this population's solution-evaluation tuples.
	 */
	public List<Tuple<S, E>> asTupleList();

	/**
	 * Returns an immutable map view of this population's solutions and evaluations.
	 * <p>
	 * The returned map entrySet iteration order is consistent with the lists returned by {@code solutionList()},
	 * {@code evaluationList()} and {@code tuplesList()}
	 *
	 * @return an immutable map view of this population's solutions and evaluations.
	 */
	public Map<S, E> asMap();

	/**
	 * Returns <tt>true</tt> if this population contains no solutions.
	 *
	 * @return <tt>true</tt> if this population contains no solutions
	 */
	public boolean isEmpty();

	/**
	 * Returns the number of elements in this population.
	 *
	 * @return the number of elements in this population
	 */
	public int size();

	/**
	 * A helper class representing a solution-evaluation pair.
	 *
	 * @author AGH AgE Team
	 */
	public static final class Tuple<S extends ISolution, E> {

		private final S solution;

		private final E evaluation;

		/**
		 * Creates an immutable instance with the specified solution and evaluation.
		 *
		 * @param solution
		 *            the solution
		 * @param evaluation
		 *            the evaluation
		 */
		public Tuple(S solution, E evaluation) {
			this.solution = solution;
			this.evaluation = evaluation;
		}

		public S getSolution() {
			return solution;
		}

		public E getEvaluation() {
			return evaluation;
		}

		/**
		 * Creates an immutable instance with the specified solution and evaluation.
		 *
		 * @param <S>
		 *            the type of solution
		 * @param <E>
		 *            the type of evaluation
		 * @param solution
		 *            the solution
		 * @param evaluation
		 *            the evaluation
		 * @return a new Tuple
		 */
		public static <S extends ISolution, E> Tuple<S, E> newTuple(S solution, E evaluation) {
			return new Tuple<S, E>(solution, evaluation);
		}
	}
}
