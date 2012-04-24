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
 * File: InitializationActionStrategy.java
 * Created: 2011-12-16
 * Author: Krzywicki
 * $Id: InitializationActionStrategy.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.genetic.action;

import java.util.Comparator;

import static java.util.Collections.max;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.action.AbstractPerformActionStrategy;
import org.jage.action.IActionContext;
import org.jage.agent.AgentException;
import org.jage.agent.IAgent;
import org.jage.evaluation.IPopulationEvaluator;
import org.jage.platform.component.annotation.Inject;
import org.jage.population.IPopulation;
import org.jage.population.IPopulation.Tuple;
import org.jage.population.IPopulationFactory;
import org.jage.solution.ISolution;
import org.jage.solution.ISolutionFactory;
import org.jage.utils.JageUtils;

import static org.jage.genetic.agent.GeneticActionDrivenAgent.Properties.BEST_EVER;
import static org.jage.genetic.agent.GeneticActionDrivenAgent.Properties.BEST_EVER_STEP;
import static org.jage.genetic.agent.GeneticActionDrivenAgent.Properties.CURRENT_BEST;
import static org.jage.genetic.agent.GeneticActionDrivenAgent.Properties.POPULATION;
import static org.jage.population.IPopulation.Tuple.newTuple;
import static org.jage.utils.JageUtils.setPropertyValueOrThrowException;

/**
 * This action handler performs initialization of an agent. A initial population is created, and initial statistics
 * updated.
 *
 * @param <S>
 *            the type of solutions
 *
 * @author AGH AgE Team
 */
public final class InitializationActionStrategy<S extends ISolution> extends AbstractPerformActionStrategy {

	private static final Logger LOG = LoggerFactory.getLogger(InitializationActionStrategy.class);

	@Inject
	private IPopulationFactory<S> populationFactory;

	@Inject
	private ISolutionFactory<S> solutionFactory;

	@Inject
	private IPopulationEvaluator<S, Double> populationEvaluator;

	private final TupleComparator<S> comparator = new TupleComparator<S>();

	@Override
	public void perfom(IAgent target, IActionContext context) throws AgentException {
		LOG.debug("Performing initialization on agent {}.", target.getAddress());

		final IPopulation<S, Double> population = populationFactory.createPopulation();
		populationEvaluator.evaluatePopulation(population);
		setPropertyValueOrThrowException(target, POPULATION, population);

		Tuple<S, Double> currentBest = copyTuple(max(population.asTupleList(), comparator));
		setPropertyValueOrThrowException(target, CURRENT_BEST, currentBest);
		setPropertyValueOrThrowException(target, BEST_EVER, currentBest);
		setPropertyValueOrThrowException(target, BEST_EVER_STEP, 0);

		if (LOG.isDebugEnabled()) {
			LOG.debug(JageUtils.getPopulationLog(population, "Initial population"));
		}
	}

	/**
	 * Helper Tuple<?, Double> comparator.
	 *
	 * @author AGH AgE Team
	 */
	private static final class TupleComparator<S extends ISolution> implements Comparator<Tuple<S, Double>> {
		@Override
		public int compare(Tuple<S, Double> t1, Tuple<S, Double> t2) {
			return t1.getEvaluation().compareTo(t2.getEvaluation());
		}
	}

	private Tuple<S, Double> copyTuple(Tuple<S, Double> tuple) {
		return newTuple(solutionFactory.copySolution(tuple.getSolution()), tuple.getEvaluation());
	}
}
