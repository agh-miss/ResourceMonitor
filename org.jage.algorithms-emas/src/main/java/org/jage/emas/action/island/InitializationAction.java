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
 * File: InitializationAction.java
 * Created: 2012-01-30
 * Author: Krzywicki
 * $Id: InitializationAction.java 211 2012-04-08 20:06:03Z krzywick $
 */

package org.jage.emas.action.island;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.agent.AgentException;
import org.jage.emas.agent.IndividualAgent;
import org.jage.emas.agent.IslandAgent;
import org.jage.emas.util.ChainingAction;
import org.jage.evaluation.ISolutionEvaluator;
import org.jage.platform.component.annotation.Inject;
import org.jage.solution.ISolution;
import org.jage.solution.ISolutionFactory;

/**
 * This action handler performs initialization actions on island agents. Each child agent is initialized with a
 * solution, evaluated and given some initial energy.
 *
 * @author AGH AgE Team
 */
public final class InitializationAction extends ChainingAction<IslandAgent> {

	private static final Logger log = LoggerFactory.getLogger(InitializationAction.class);

	@Inject
	private ISolutionFactory<ISolution> solutionFactory;

	@Inject
	private ISolutionEvaluator<ISolution, Double> evaluator;

	private double initialEnergy;

	public void setInitialEnergy(final double initialEnergy) {
		this.initialEnergy = initialEnergy;
	}

	@Override
	protected void doPerform(final IslandAgent agent) throws AgentException {
		log.debug("Performing initialization action on {}", agent);

		for (final IndividualAgent child : agent.getIndividualAgents()) {
			ISolution solution = solutionFactory.createInitializedSolution();
			child.setSolution(solution);
			child.setOriginalFitness(evaluator.evaluate(solution));
			child.changeEnergyBy(initialEnergy);
		}
	}
}
