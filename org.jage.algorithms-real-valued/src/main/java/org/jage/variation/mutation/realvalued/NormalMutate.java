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
 * File: NormalMutate.java
 * Created: 2011-11-03
 * Author: Krzywicki
 * $Id: NormalMutate.java 124 2012-03-18 10:27:39Z krzywick $
 */

package org.jage.variation.mutation.realvalued;

import org.jage.platform.component.annotation.Inject;
import org.jage.random.INormalizedDoubleRandomGenerator;

/**
 * Simple population mutation strategy, that mutates each solution individually using provided solution mutation
 * strategy.
 *
 * @author AGH AgE Team
 */
public final class NormalMutate extends DoubleAbstractStochasticMutate {

	@Inject
	private INormalizedDoubleRandomGenerator rand;

	private double mutationRange;

	public void setMutationRange(final double mutationRange) {
		this.mutationRange = mutationRange;
	}

	@Override
	protected double doMutate(final double value) {
		// Based on Wikipedia's information on inverse cumulative
		// distribution function of the Cauchy distribution
		return value + mutationRange * Math.tan(Math.PI * (rand.nextDouble() - 0.5));
	}
}
