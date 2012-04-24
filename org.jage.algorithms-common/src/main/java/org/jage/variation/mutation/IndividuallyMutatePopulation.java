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
 * File: IndividuallyMutatePopulation.java
 * Created: 2011-10-20
 * Author: Krzywicki
 * $Id: IndividuallyMutatePopulation.java 124 2012-03-18 10:27:39Z krzywick $
 */

package org.jage.variation.mutation;

import org.jage.platform.component.annotation.Inject;
import org.jage.population.IPopulation;
import org.jage.random.INormalizedDoubleRandomGenerator;
import org.jage.solution.ISolution;
import org.jage.strategy.AbstractStrategy;

/**
 * An {@link IMutatePopulation} implementation which mutates each solution individually, based on a chanceToMutate
 * property to decide whether mutation should occur, and a specified {@link IMutateSolution} strategy to perform it.
 *
 * @param <S>
 *            The type of the solutions held by the population to be mutated
 * @author AGH AgE Team
 */
public final class IndividuallyMutatePopulation<S extends ISolution> extends AbstractStrategy implements
        IMutatePopulation<S> {

	static final double DEFAULT_CHANCE_TO_MUTATE = 0.5;

	@Inject
	private INormalizedDoubleRandomGenerator rand;

	@Inject
	private IMutateSolution<S> mutate;

	private double chanceToMutate;

	/**
	 * Creates an IndividuallyMutatePopulation with a default chance to mutate.
	 */
	public IndividuallyMutatePopulation() {
		this(DEFAULT_CHANCE_TO_MUTATE);
	}

	/**
	 * Creates an IndividuallyMutatePopulation with a given chance to mutate.
	 *
	 * @param chanceToMutate
	 *            the chance to mutate
	 */
	public IndividuallyMutatePopulation(final double chanceToMutate) {
		this.chanceToMutate = chanceToMutate;
	}

    public void setChanceToMutate(final double chanceToMutate) {
	    this.chanceToMutate = chanceToMutate;
    }

	@Override
	public void mutatePopulation(final IPopulation<S, ?> population) {
		for (final S solution : population) {
			if (rand.nextDouble() < chanceToMutate) {
				mutate.mutateSolution(solution);
			}
		}
	}
}
