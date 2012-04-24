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
 * File: PairedRecombinePopulation.java
 * Created: 2011-10-20
 * Author: Krzywicki
 * $Id: PairedRecombinePopulation.java 124 2012-03-18 10:27:39Z krzywick $
 */

package org.jage.variation.recombination;

import java.util.Iterator;

import org.jage.platform.component.annotation.Inject;
import org.jage.population.IPopulation;
import org.jage.random.INormalizedDoubleRandomGenerator;
import org.jage.solution.ISolution;
import org.jage.strategy.AbstractStrategy;

/**
 * An {@link IRecombinePopulation} implementation which recombines each consecutive pair of solutions with a given
 * probability, using a specified {@link IRecombine} strategy to actually perform the recombination.
 *
 * @param <S>
 *            The type of solutions to be recombined.
 * @author AGH AgE Team
 */
public final class PairedRecombinePopulation<S extends ISolution> extends AbstractStrategy implements
        IRecombinePopulation<S> {

	private static final double DEFAULT_CHANCE_TO_RECOMBINE = 1.0;

	@Inject
	private INormalizedDoubleRandomGenerator rand;

	@Inject
	private IRecombine<S> recombine;

	private double chanceToRecombine;

	/**
	 * Creates an PairedRecombinePopulation with a default chance to recombine.
	 */
	public PairedRecombinePopulation() {
		this(DEFAULT_CHANCE_TO_RECOMBINE);
	}

	/**
	 * Creates an PairedRecombinePopulation with a given chance to recombine.
	 *
	 * @param chanceToRecombine
	 *            the chance to recombine
	 */
	public PairedRecombinePopulation(final double chanceToRecombine) {
		this.chanceToRecombine = chanceToRecombine;
	}

	public void setChanceToRecombine(final double chanceToRecombine) {
	    this.chanceToRecombine = chanceToRecombine;
    }

	@Override
	public void recombinePopulation(final IPopulation<S, ?> population) {
		for (final Iterator<S> iterator = population.iterator(); iterator.hasNext();) {
			final S s1 = iterator.next();
			if (iterator.hasNext()) {
				final S s2 = iterator.next();
				if (rand.nextDouble() < chanceToRecombine) {
					recombine.recombine(s1, s2);
				}
			}
		}
	}
}
