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
 * File: SequentialPopulationEvaluator.java
 * Created: 2011-12-01
 * Author: Krzywicki
 * $Id: SequentialPopulationEvaluator.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.evaluation;

import org.jage.platform.component.annotation.Inject;
import org.jage.population.IPopulation;
import org.jage.property.ClassPropertyContainer;
import org.jage.solution.ISolution;

/**
 * Simple {@link IPopulationEvaluator} strategy, which evaluates solutions sequentially, one by one.
 *
 * @param <S>
 *            The type of {@link ISolution} to be evaluated
 * @param <E>
 *            the type of evaluation
 * @author AGH AgE Team
 */
public class SequentialPopulationEvaluator<S extends ISolution, E> extends ClassPropertyContainer implements
        IPopulationEvaluator<S, E> {

	@Inject
	private ISolutionEvaluator<S, E> evaluator;

	@Override
	public void evaluatePopulation(IPopulation<S, E> population) {
		for (S solution : population) {
			population.setEvaluation(solution, evaluator.evaluate(solution));
		}
	}
}
