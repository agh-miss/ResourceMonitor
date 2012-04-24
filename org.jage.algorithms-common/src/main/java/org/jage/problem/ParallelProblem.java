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
 * File: ParallelProblem.java
 * Created: 2011-10-20
 * Author: Krzywicki
 * $Id: ParallelProblem.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.problem;

import org.jage.property.ClassPropertyContainer;

/**
 * A parallel problem that is bounded by the following hypercube
 *
 * <pre>
 * [min, max] x [min, max] x ... x [min, max]
 * </pre>
 *
 * @param <R>
 *            The type of the problem bounds
 *
 * @author AGH AgE Team
 */
public class ParallelProblem<R> extends ClassPropertyContainer implements IVectorProblem<R> {

	private int dimension;

	private R min;

	private R max;

	/**
	 * Creates a ParallelRoblem with a range of [min, max] and a given dimension.
	 *
	 * @param dimension
	 *            This problem dimension
	 * @param min
	 *            This problem lower bound
	 * @param max
	 *            This problem upper bound
	 */
	public ParallelProblem(int dimension, R min, R max) {
		this.dimension = dimension;
		this.min = min;
		this.max = max;
	}

	@Override
	public final int getDimension() {
		return dimension;
	}

	@Override
	public final R lowerBound(int atDimension) {
		checkDimension(atDimension);
		return min;
	}

	@Override
	public final R upperBound(int atDimension) {
		checkDimension(atDimension);
		return max;
	}

	private void checkDimension(int atDimension) {
		if (atDimension < 0 || atDimension >= this.dimension) {
			throw new IllegalArgumentException("Dimension out of range");
		}
	}
}
