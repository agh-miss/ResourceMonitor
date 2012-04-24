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
 * File: AbstractPreselection.java
 * Created: 2011-05-05
 * Author: Krzywicki
 * $Id: AbstractPreselection.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.genetic.preselection;

import java.util.List;

import org.jage.platform.component.annotation.Inject;
import org.jage.population.IPopulation;
import org.jage.solution.ISolutionFactory;
import org.jage.solution.IVectorSolution;
import org.jage.strategy.AbstractStrategy;

import static org.jage.population.Populations.newPopulation;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayListWithCapacity;
import static com.google.common.primitives.Doubles.toArray;

/**
 * Abtract {@link IPreselection} implementation. Relies on subclasses to provide the indices of the preselected solution
 * (note that any solution may be chosen multiple times).
 *
 * @author AGH AgE Team
 */
public abstract class AbstractPreselection extends AbstractStrategy implements
        IPreselection<IVectorSolution<Double>, Double> {

	@Inject
	private ISolutionFactory<IVectorSolution<Double>> solutionFactory;

	private IntSet preselectedIndices = new IntOpenHashSet();

	@Override
	public final IPopulation<IVectorSolution<Double>, Double> preselect(
	        IPopulation<IVectorSolution<Double>, Double> population) {
		checkNotNull(population, "The population must not be null");

		int populationSize = population.size();
		if (populationSize < 2) {
			return population;
		}

		List<IVectorSolution<Double>> solutionList = population.asSolutionList();
		List<Double> evaluationList = population.asEvaluationList();

		List<IVectorSolution<Double>> preselectedSolutions = newArrayListWithCapacity(populationSize);
		for (int index : getPreselectedIndices(toArray(evaluationList))) {
			IVectorSolution<Double> solution = solutionList.get(index);
			if (!preselectedIndices.add(index)) {
				solution = solutionFactory.copySolution(solution);
			}
			preselectedSolutions.add(solution);
		}
		preselectedIndices.clear();

		return newPopulation(preselectedSolutions);
	}

	/**
	 * Performs the actual preselection. Given an array of evaluation values, returns an array containing indices of the
	 * preselected solutions. Any given solution may be preselected multiple times, in which case it will be copied in
	 * the resulting preselected population.
	 *
	 * @param values
	 *            The solutions evaluation values
	 * @return The indices of the preselected solutions
	 */
	protected abstract int[] getPreselectedIndices(double[] values);
}
