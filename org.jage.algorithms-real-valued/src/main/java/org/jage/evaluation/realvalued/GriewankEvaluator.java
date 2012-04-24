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
 * File: GriewankProblem.java
 * Created: 2011-12-02
 * Author: Krzywicki
 * $Id: GriewankEvaluator.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.evaluation.realvalued;

import org.jage.evaluation.ISolutionEvaluator;
import org.jage.property.ClassPropertyContainer;
import org.jage.solution.IVectorSolution;

import it.unimi.dsi.fastutil.doubles.DoubleList;

/**
 * This class represents a floating-point coded Griewank function. <br />
 * Solution: min=0.0, xi=0, i=1..n <br />
 * http://www-optima.amp.i.kyoto-u.ac.jp/member/student /hedar/Hedar_files/TestGO_files/Page1905.htm <br />
 * <br />
 * The original problem is a minimalization one but it is much convenient to maximize the problem function. So the
 * original function is modified g(x)=-f(x)
 *
 * @author AGH AgE Team
 */
public final class GriewankEvaluator extends ClassPropertyContainer implements
        ISolutionEvaluator<IVectorSolution<Double>, Double> {

	@Override
	public Double evaluate(IVectorSolution<Double> solution) {
		DoubleList representation = (DoubleList)solution.getRepresentation();

		double sum = 0.0;
		double prod = 1.0;
		for (int i = 0, n = representation.size(); i < n; i++) {
			double value = representation.getDouble(i);
			sum += (value * value) / 4000;
			prod *= Math.cos(value / Math.sqrt(i + 1));

		}

		return -1 - sum + prod;
	}
}
