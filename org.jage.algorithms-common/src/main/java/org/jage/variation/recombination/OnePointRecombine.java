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
 * File: OnePointRecombine.java
 * Created: 2011-10-20
 * Author: Krzywicki
 * $Id: OnePointRecombine.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.variation.recombination;

import java.util.List;

import org.jage.platform.component.annotation.Inject;
import org.jage.random.IIntRandomGenerator;
import org.jage.solution.IVectorSolution;
import org.jage.strategy.AbstractStrategy;

/**
 * Recombines the representations of two binary solutions at a random point.
 *
 * @param <R>
 *            the representation type of the solution to be recombined
 * @author AGH AgE Team
 */
public class OnePointRecombine<R> extends AbstractStrategy implements IRecombine<IVectorSolution<R>> {

	@Inject
	private IIntRandomGenerator rand;

	@Override
	public final void recombine(IVectorSolution<R> solution1, IVectorSolution<R> solution2) {
		List<R> representation1 = solution1.getRepresentation();
		List<R> representation2 = solution2.getRepresentation();

		int size = representation1.size();
		int recombinePoint = rand.nextInt(size);
		for (int i = recombinePoint; i < size; i++) {
			swap(representation1, representation2, i);
		}
	}

	/**
	 * Swaps the representations at the given index. <br />
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
	 *            the index at which swapping should occur
	 */
	protected void swap(List<R> representation1, List<R> representation2, int index) {
		R element = representation1.get(index);
		representation1.set(index, representation2.get(index));
		representation2.set(index, element);
	}
}
