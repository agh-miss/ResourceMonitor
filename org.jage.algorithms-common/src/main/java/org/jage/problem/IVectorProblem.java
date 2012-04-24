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
 * File: IVectorProblem.java
 * Created: 2011-10-07
 * Author: Krzywicki
 * $Id: IVectorProblem.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.problem;

/**
 * Interface for vector problems, i.e. problems which have some number of dimensions and each one is bounded.
 *
 * @param <R>
 *            The type of the problem bounds
 *
 * @author AGH AgE Team
 */
public interface IVectorProblem<R> extends IProblem {

	/**
	 * Returns the problem's dimension.
	 *
	 * @return the problem's dimension.
	 */
	public int getDimension();

	/**
	 * Returns the problem's lower bound in a given dimension, indexed from 0. It must be not greater than the
	 * {@link #upperBound(int)} in the same dimension.
	 *
	 * @param atDimension
	 *            the given dimension
	 * @return the problem's lower bound in the given dimension
	 * @throws IllegalArgumentException
	 *             if the given dimension is greater than this problem's one or negative
	 */
	public R lowerBound(int atDimension);

	/**
	 * Returns the problem's upper bound in a given dimension, indexed from 0. It must be not smaller than the
	 * {@link #lowerBound(int)} in the same dimension.
	 *
	 * @param atDimension
	 *            the given dimension
	 * @return the problem's upper bound in the given dimension
	 * @throws IllegalArgumentException
	 *             if the given dimension is greater than this problem's one or negative
	 */
	public R upperBound(int atDimension);
}
