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
 * File: PopulationFactory.java
 * Created: 2011-10-20
 * Author: Krzywicki
 * $Id: PopulationFactory.java 124 2012-03-18 10:27:39Z krzywick $
 */

package org.jage.population;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.platform.component.annotation.Inject;
import org.jage.property.ClassPropertyContainer;
import org.jage.solution.ISolution;
import org.jage.solution.ISolutionFactory;

import static org.jage.population.Populations.newPopulation;

/**
 * The default implementation of {@link IPopulationFactory}. <br />
 * <br />
 * It relies on a {@link ISolutionFactory} to create the solutions and then forms a population.
 *
 * @param <S>
 *            The type of solutions created by this factory
 * @author AGH AgE Team
 */
public class PopulationFactory<S extends ISolution> extends ClassPropertyContainer implements IPopulationFactory<S> {

	private static final Logger LOG = LoggerFactory.getLogger(PopulationFactory.class);

	private static final int DEFAULT_POPULATION_SIZE = 10;

	@Inject
	private ISolutionFactory<S> solutionFactory;

	private int populationSize = DEFAULT_POPULATION_SIZE;

	/**
	 * Creates a PopulationFactory with a default population size of 10.
	 */
	public PopulationFactory() {
		this(DEFAULT_POPULATION_SIZE);
	}

	/**
	 * Creates a PopulationFactory with a given population size.
	 *
	 * @param populationSize
	 *            the size of the population to be created by this factory
	 */
	private PopulationFactory(final int populationSize) {
		this.populationSize = populationSize;
	}

	public void setPopulationSize(final int populationSize) {
		this.populationSize = populationSize;
	}

	@Override
	public <E> IPopulation<S, E> createPopulation() {
		LOG.debug("Creating a population of size {}", populationSize);

		final List<S> solutions = new ArrayList<S>(populationSize);
		for (int i = 0; i < populationSize; i++) {
			solutions.add(solutionFactory.createInitializedSolution());
		}

		return newPopulation(solutions);
	}
}
