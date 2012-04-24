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
 * File: CarineProblem.java
 * Created: 2011-11-03
 * Author: Krzywicki
 * $Id: CarineProblem.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.problem.integer;

import org.jage.problem.NonParallelProblem;

/**
 * Defines the default bound for a CarineEvaluator.
 *
 * @author AGH AgE Team
 */
public final class CarineProblem extends NonParallelProblem<Integer> {

	/**
	 * Creates an CarineProblem with default bounds of [0, 0, 1, 0, 1, 1, 0, 0]x[1, 30, 50000, 255, 48, 64, 63, 10].
	 */
	public CarineProblem() {
		super(new Integer[] { 0, 0, 1, 0, 1, 1, 0, 0 }, new Integer[] { 1, 30, 50000, 255, 48, 64, 63, 10 });
	}
}
