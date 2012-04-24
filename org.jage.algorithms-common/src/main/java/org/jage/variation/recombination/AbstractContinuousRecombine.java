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
 * File: AbstractContinuousRecombine.java
 * Created: 2011-10-20
 * Author: Krzywicki
 * $Id: AbstractContinuousRecombine.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.variation.recombination;

import java.util.List;

import org.jage.solution.IVectorSolution;
import org.jage.strategy.AbstractStrategy;

/**
 * Abstract implementation of a continuous recombination. That is, the recombined value is located continuously between
 * the parents values. <br />
 * <br />
 * Concrete subclasses are supposed to provide the actual computation.
 *
 * @param <R>
 *            the representation type of the solution to be recombined
 *
 * @author AGH AgE Team
 */
public abstract class AbstractContinuousRecombine<R> extends AbstractStrategy implements IRecombine<IVectorSolution<R>> {

	@Override
	public final void recombine(IVectorSolution<R> solution1, IVectorSolution<R> solution2) {
		List<R> representation1 = solution1.getRepresentation();
		List<R> representation2 = solution2.getRepresentation();

		for (int i = 0, n = representation1.size(); i < n; i++) {
			doRecombine(representation1, representation2, i);
		}
	}

	/**
	 * Recombines the representations at the given index. <br />
	 * <br />
	 * This method purpose is to allow efficient unboxing in case of representations of primitives. Subclasses can then
	 * cast the given representation in the corresponding fastutil collection.
	 *
	 *
	 * @param representation1
	 *            the first representation
	 * @param representation2
	 *            the second representation
	 * @param index
	 *            the index at which recombination should occur
	 */
	protected abstract void doRecombine(List<R> representation1, List<R> representation2, int index);
}
