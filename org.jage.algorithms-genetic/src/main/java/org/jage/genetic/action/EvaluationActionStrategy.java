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
 * File: EvaluationActionStrategy.java
 * Created: 2011-12-19
 * Author: Krzywicki
 * $Id: EvaluationActionStrategy.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.genetic.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.action.AbstractPerformActionStrategy;
import org.jage.action.IActionContext;
import org.jage.agent.AgentException;
import org.jage.agent.IAgent;
import org.jage.evaluation.IPopulationEvaluator;
import org.jage.platform.component.annotation.Inject;
import org.jage.population.IPopulation;
import org.jage.solution.ISolution;
import org.jage.utils.JageUtils;

import static org.jage.genetic.agent.GeneticActionDrivenAgent.Properties.POPULATION;
import static org.jage.utils.JageUtils.getPropertyValueOrThrowException;

/**
 * This action handler evaluates the population of an agent.
 *
 * @param <S>
 *            the type of solutions
 * @param <E>
 *            the type of evaluations
 *
 * @author AGH AgE Team
 */
public final class EvaluationActionStrategy<S extends ISolution, E> extends AbstractPerformActionStrategy {

	private static final Logger LOG = LoggerFactory.getLogger(EvaluationActionStrategy.class);

	@Inject
	private IPopulationEvaluator<S, E> populationEvaluator;

	@Override
	public void perfom(IAgent target, IActionContext context) throws AgentException {
		LOG.debug("Performing evaluation on agent {}.", target.getAddress());

		IPopulation<S, E> population = getPropertyValueOrThrowException(target, POPULATION);
		populationEvaluator.evaluatePopulation(population);

		if (LOG.isDebugEnabled()) {
			LOG.debug(JageUtils.getPopulationLog(population, "Population Evaluations"));
		}
	}
}
