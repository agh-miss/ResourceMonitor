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
 * File: DecodingEvaluator.java
 * Created: 2011-11-03
 * Author: Krzywicki
 * $Id: DecodingEvaluator.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.evaluation.binary;

import org.jage.evaluation.ISolutionEvaluator;
import org.jage.platform.component.annotation.Inject;
import org.jage.property.ClassPropertyContainer;
import org.jage.solution.ISolution;

/**
 * An evaluator implementation which applies a {@link ISolutionDecoder} before delegating to some other
 * {@link ISolutionEvaluator}, effectively acting as a bridge.
 *
 * @param <S>
 *            The decoder source solution type
 * @param <T>
 *            The decoder target solution type
 * @author AGH AgE Team
 */
public final class DecodingEvaluator<S extends ISolution, T extends ISolution> extends ClassPropertyContainer implements
        ISolutionEvaluator<S, Double> {

	@Inject
	private ISolutionDecoder<S, T> solutionDecoder;

	@Inject
	private ISolutionEvaluator<T, Double> solutionEvaluator;

	@Override
	public Double evaluate(S solution) {
		T decodedSolution = solutionDecoder.decodeSolution(solution);
		return solutionEvaluator.evaluate(decodedSolution);
	}
}
