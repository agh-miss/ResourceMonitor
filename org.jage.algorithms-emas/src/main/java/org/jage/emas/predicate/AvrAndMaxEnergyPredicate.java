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
 * File: AvrAndMaxEnergyPredicate.java
 * Created: 2012-01-30
 * Author: Krzywicki
 * $Id: AvrAndMaxEnergyPredicate.java 211 2012-04-08 20:06:03Z krzywick $
 */

package org.jage.emas.predicate;

import java.util.Collection;

import org.jage.emas.agent.IndividualAgent;
import org.jage.query.AgentEnvironmentQuery;

/**
 * Predicate applying to agents which energy is higher than {@code (averageEnergy + energyMax) / 2} of their population.
 *
 * @author AGH AgE Team
 */
public class AvrAndMaxEnergyPredicate implements IPredicate<IndividualAgent> {

	@Override
	public boolean apply(final IndividualAgent agent) {
		Collection<IndividualAgent> agents = new AgentEnvironmentQuery<IndividualAgent, IndividualAgent>()
		        .execute(agent.getEnvironment());

		int agentsNumber = agents.size();
		if (agentsNumber <= 1) {
			return true;
		}

		double energySum = 0.0;
		double energyMax = Double.MIN_VALUE;
		for (IndividualAgent other : agents) {
			double energy = other.getEnergy();
			if (energy > energyMax) {
				energyMax = energy;
			}
			energySum += energy;
		}
		double averageEnergy = energySum / agentsNumber;

		double threshold = (averageEnergy + energyMax) / 2;
		return agent.getEnergy() >= threshold;
	}
}
