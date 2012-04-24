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
 * File: SchwefelProblem.java
 * Created: 2011-11-03
 * Author: Krzywicki
 * $Id: SchwefelProblem.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.problem.realvalued;

import org.jage.problem.ParallelProblem;

/**
 * This class represents the problem domain for a floating-point coded Schwefel function. <br />
 * http://www-optima.amp.i.kyoto-u.ac.jp/member/student/hedar/Hedar_files/TestGO_files/Page2530.htm <br />
 * Default range: [-500, 500]
 *
 * @author AGH AgE Team
 */
public final class SchwefelProblem extends ParallelProblem<Double> {

	private static final double DEFAULT_RANGE = 500;

	/**
	 * Creates a SchwefelProblem with a default range of [-500, 500].
	 *
	 * @param dimension
	 *            The dimension of this problem
	 */
	public SchwefelProblem(Integer dimension) {
		super(dimension, -DEFAULT_RANGE, DEFAULT_RANGE);
	}
}
