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
 * File: RoulettePreselection.java
 * Created: 2011-05-05
 * Author: Krzywicki
 * $Id: RoulettePreselection.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.genetic.preselection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.genetic.scaling.IScaling;
import org.jage.platform.component.annotation.Inject;
import org.jage.random.INormalizedDoubleRandomGenerator;
import org.jage.utils.CumulativeDistribution;

/**
 * Preselect strategy. Roulette wheel implementation. Scales solutions evaluations using a provided {@link IScaling}.
 *
 * @author AGH AgE Team
 */
public final class RoulettePreselection extends AbstractPreselection {

	private static final Logger LOG = LoggerFactory.getLogger(RoulettePreselection.class);

	@Inject
	private IScaling scaling;

	@Inject
	private INormalizedDoubleRandomGenerator rand;

	@Override
	protected int[] getPreselectedIndices(double[] values) {
		CumulativeDistribution distribution = new CumulativeDistribution(scaling.scale(values));
		final int n = values.length;

		int[] indices = new int[n];
		for (int i = 0; i < n; i++) {
			indices[i] = distribution.getValueFor(rand.nextDouble());
		}

		LOG.debug("Distribution {} resulted in indices {}", distribution, indices);

		return indices;
	}
}
