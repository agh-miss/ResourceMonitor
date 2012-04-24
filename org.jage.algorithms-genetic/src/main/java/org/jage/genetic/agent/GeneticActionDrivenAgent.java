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
 * File: GeneticActionDrivenAgent.java
 * Created: 2011-04-30
 * Author: Krzywicki
 * $Id: GeneticActionDrivenAgent.java 213 2012-04-08 20:21:35Z krzywick $
 */

package org.jage.genetic.agent;

import static java.lang.String.format;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.agent.ActionDrivenAgent;
import org.jage.platform.component.exception.ComponentException;
import org.jage.population.IPopulation;
import org.jage.population.IPopulation.Tuple;
import org.jage.property.PropertyField;
import org.jage.solution.ISolution;

/**
 * A {@link ActionDrivenAgent} subclass, which adds several genetic properties. <br />
 * <br />
 * It also provide initialization logic, which might be subject to be moved in some actions as well.
 *
 * @author AGH AgE Team
 */
public class GeneticActionDrivenAgent extends ActionDrivenAgent {

	/**
	 * GeneticActionDrivenAgent properties.
	 *
	 * @author AGH AgE Team
	 */
	public static class Properties extends ActionDrivenAgent.Properties {

		/**
		 * The agent's current population.
		 */
		public static final String POPULATION = "population";

		/**
		 * The current best solution.
		 */
		public static final String CURRENT_BEST = "currentBest";

		/**
		 * The best solution ever.
		 */
		public static final String BEST_EVER = "bestEver";

		/**
		 * The step on which the best solution ever happened.
		 */
		public static final String BEST_EVER_STEP = "bestEverStep";
	}

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(GeneticActionDrivenAgent.class);

	@SuppressWarnings("unused")
	@PropertyField(propertyName = Properties.POPULATION)
	private IPopulation<ISolution, ?> population;

	@SuppressWarnings("unused")
	@PropertyField(propertyName = Properties.CURRENT_BEST)
	private Tuple<ISolution, ?> currentBest;

	@PropertyField(propertyName = Properties.BEST_EVER)
	private Tuple<ISolution, ?> bestEver;

	@PropertyField(propertyName = Properties.BEST_EVER_STEP)
	private long bestEverStep;

	@Override
	public void init() throws ComponentException {
		super.init();
		LOG.info("Agent {} initialized", this);
	}

	@Override
	public boolean finish() throws ComponentException {
		LOG.info(getResultLog());
		return super.finish();
	}

	private String getResultLog() {
		final StringBuilder builder = new StringBuilder("\n\t---=== Computation finished ===---");
		builder.append(format("\n\tBest solution ever (evaluation = %1$.2f, at step = %2$d): %3$s",
		        bestEver.getEvaluation(), bestEverStep, bestEver.getSolution()));
		return builder.toString();
	}
}
