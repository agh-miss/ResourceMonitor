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
 * File: IMutate.java
 * Created: 2011-10-20
 * Author: Krzywicki
 * $Id: IMutateSolution.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.variation.mutation;

import org.jage.solution.ISolution;
import org.jage.strategy.IStrategy;

/**
 * Strategy interface for mutating a solution.
 *
 * @param <S>
 *            the type of the solutions to be mutated
 * @author AGH AgE Team
 */
public interface IMutateSolution<S extends ISolution> extends IStrategy {

	/**
	 * Mutates the given solution.
	 *
	 * @param solution
	 *            The solution to be mutated
	 */
	public void mutateSolution(S solution);
}
