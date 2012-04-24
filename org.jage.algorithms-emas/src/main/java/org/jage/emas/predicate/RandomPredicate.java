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
 * File: RandomPredicate.java
 * Created: 2012-01-30
 * Author: Krzywicki
 * $Id: RandomPredicate.java 201 2012-04-07 20:21:39Z krzywick $
 */

package org.jage.emas.predicate;

import org.jage.agent.IAgent;
import org.jage.platform.component.annotation.Inject;
import org.jage.random.INormalizedDoubleRandomGenerator;

/**
 * Predicate applying to agents with some probability.
 *
 * @param <A>
 *            the type of agent this predicate applies to
 *
 * @author AGH AgE Team
 */
public class RandomPredicate<A extends IAgent> implements IPredicate<A> {

	@Inject
	private INormalizedDoubleRandomGenerator rand;

	private double probability;

	public void setProbability(final double probability) {
		this.probability = probability;
	}

	@Override
	public boolean apply(final A agent) {
		return rand.nextDouble() < probability;
	}
}
