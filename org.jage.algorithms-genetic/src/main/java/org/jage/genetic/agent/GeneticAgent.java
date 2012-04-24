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
 * File: GeneticAgent.java
 * Created: 2008-10-12
 * Author: awos
 * $Id: GeneticAgent.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.genetic.agent;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.agent.SimpleAgent;
import org.jage.evaluation.ISolutionEvaluator;
import org.jage.genetic.preselection.IPreselection;
import org.jage.platform.component.annotation.Inject;
import org.jage.population.IPopulation;
import org.jage.property.PropertyField;
import org.jage.solution.ISolution;
import org.jage.solution.ISolutionFactory;
import org.jage.variation.IVariationOperator;

import static org.jage.population.Populations.newPopulation;

/**
 * An agent implementation which performs genetic computation.
 *
 * <br /><br />
 * Deprecated. Use {@link GeneticActionDrivenAgent} instead.
 *
 * @author AGH AgE Team
 */
@Deprecated
public class GeneticAgent extends SimpleAgent {

	private static Logger log = LoggerFactory.getLogger(GeneticAgent.class);

	private static final long serialVersionUID = 1L;

	// BEGIN Dependencies

	/**
	 * The size of the population.
	 */
	@Inject
	@PropertyField(propertyName = "populationSize")
	private int populationSize;

	@Inject
	private ISolutionFactory<ISolution> solutionFactory;

	/**
	 * The preselection strategy.
	 */
	@Inject
	@PropertyField(propertyName = "preselect")
	private IPreselection<ISolution, Double> preselect;

	/**
	 * The variation strategy.
	 */
	@Inject
	@PropertyField(propertyName = "geneticOperators")
	private IVariationOperator<ISolution> geneticOperators;

	@Inject
	private ISolutionEvaluator<ISolution, Double> evaluator;

	// END Dependencies

	// BEGIN Properties

	/**
	 * The actual step of computation.
	 */
	@PropertyField(propertyName = "step")
	private int step = 0;

	/**
	 * The number of steps after which statistics will be updated.
	 */
	@PropertyField(propertyName = "resolution")
	private int resolution = 20;

	/**
	 * The population.
	 */
	private IPopulation<ISolution, Double> population;

	/**
	 * The best solution in the current population.
	 */
	@PropertyField(propertyName = "bestSolution")
	private ISolution bestSolution = null;

	/**
	 * The average evaluation of the current population.
	 */
	@PropertyField(propertyName = "avgEvaluation")
	private double avgEvaluation;

	/**
	 * The best solution ever.
	 */
	@PropertyField(propertyName = "bestSolutionEver")
	private ISolution bestSolutionEver = null;

	/**
	 * The step on which the best solution ever happened.
	 */
	@PropertyField(propertyName = "bestSolutionEverStep")
	private int bestSolutionEverStep = 0;

	// END Properties

	// BEGIN Getters

	/**
	 * Returns the best solution in the current population.
	 *
	 * @return The best solution
	 */
	public ISolution getBestSolution() {
		return bestSolution;
	}

	/**
	 * Returns the average evaluation of the current population.
	 *
	 * @return The average evaluation
	 */
	public double getAvgEvaluation() {
		return avgEvaluation;
	}

	// END Getters

	// BEGIN Logic

	@Override
	public void init() {
		createInitialPopulation();
		updatePopulationStatistics();

		log.debug("{}{}", getStepLog(), getPopulationLog("Initial population"));
		log.info("Agent {} initialized", this);
	}

	@Override
	public void step() {
		step++;

		preselectPopulation();
		if (log.isDebugEnabled()) {
			log.debug("{}{}", getStepLog(), getPopulationLog("Preselected population"));
		}

		transformPopulation();
		if (log.isDebugEnabled()) {
			log.debug("{}{}", getStepLog(), getPopulationLog("Transformed population"));
		}

		if (step % resolution == 0) {
			updatePopulationStatistics();
			log.info("{}{}", getStepLog(), getStatisticsLog());
		}

		notifyMonitorsForChangedProperties();
	}

	// END Logic

	// BEGIN Private

	private void createInitialPopulation() {
		List<ISolution> solutions = new ArrayList<ISolution>(populationSize);
		ISolution prototype = solutionFactory.createInitializedSolution();
		for (int i = 0; i < populationSize; i++) {
			solutions.add(solutionFactory.copySolution(prototype));
		}
		population = newPopulation(solutions);
	}

	private void preselectPopulation() {
		population = preselect.preselect(population);
	}

	private void transformPopulation() {
		geneticOperators.transformPopulation(population);
	}

	private void updatePopulationStatistics() {
		bestSolution = population.iterator().next();
		double bestEvaluation = evaluator.evaluate(bestSolution);
		avgEvaluation = 0.0;

		for (ISolution solution: population) {
			double evaluation = evaluator.evaluate(solution);
			avgEvaluation += evaluation;

			if (evaluation > bestEvaluation) {
				this.bestSolution = solutionFactory.copySolution(solution);
				bestEvaluation = evaluation;
			}
		}

		avgEvaluation /= population.size();

		if (bestSolutionEver == null) {
			bestSolutionEver = bestSolution;
			bestSolutionEverStep = step;
		} else if (bestEvaluation > evaluator.evaluate(bestSolutionEver)) {
			bestSolutionEver = solutionFactory.copySolution(bestSolution);
			bestSolutionEverStep = step;
		}

		populationSize = population.size();
	}

	// END Private

	// BEGIN Logging

	private String getStepLog() {
		return this + " at step " + step;
	}

	private String getPopulationLog(String msg) {
		StringBuilder builder = new StringBuilder("\n\t---=== " + msg + " ===---");
		for (ISolution solution : population) {
			builder.append(String.format("\n\t%1$s, Evaluation = %2$g", solution,
			        evaluator.evaluate(solution)));

		}
		return builder.toString();
	}

	private String getStatisticsLog() {
		StringBuilder builder = new StringBuilder();
		builder.append(String.format("\n\tBest solution = %1$s, Evaluation = %2$s, Average evaluation = %3$g",
		        bestSolution, evaluator.evaluate(bestSolution), avgEvaluation));
		builder.append(String.format("\n\tBest solution ever = %1$s, Evaluation = %2$s, at step = %3$d",
		        bestSolutionEver, evaluator.evaluate(bestSolutionEver), bestSolutionEverStep));

		return builder.toString();
	}

	// END Logging
}
