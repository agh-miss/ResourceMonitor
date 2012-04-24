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
 * File: AvrPlusStdDevEnergyPredicate.java
 * Created: 2012-01-30
 * Author: Krzywicki
 * $Id: AvrPlusStdDevEnergyPredicate.java 211 2012-04-08 20:06:03Z krzywick $
 */

package org.jage.emas.predicate;

import java.util.Collection;

import static java.lang.Math.sqrt;

import org.jage.emas.agent.IndividualAgent;
import org.jage.query.AgentEnvironmentQuery;

/**
 * Predicate applying to agents which energy is higher than {@code averageEnergy + stdDev * sigma} of their
 * population for some given {@code sigma}.
 *
 * @author AGH AgE Team
 */
public class AvrPlusStdDevEnergyPredicate implements IPredicate<IndividualAgent> {

	private double sigma;

    public void setSigma(final double sigma) {
	    this.sigma = sigma;
    }

	@Override
	public boolean apply(final IndividualAgent agent) {
		Collection<IndividualAgent> agents = new AgentEnvironmentQuery<IndividualAgent, IndividualAgent>()
		        .execute(agent.getEnvironment());

		int agentsNumber = agents.size();
		if (agentsNumber <= 1) {
			return true;
		}

		double energySum = 0.0;
		double sumOfSquares = 0.0;

		for (IndividualAgent other : agents) {
			double energy = other.getEnergy();
			energySum += energy;
			sumOfSquares += energy * energy;
		}
		double averageEnergy = energySum / agentsNumber;
		double stdDev = sqrt((agentsNumber / (agentsNumber - 1))
		        * ((sumOfSquares / agentsNumber) - averageEnergy * averageEnergy));

		double threshold = averageEnergy + stdDev * sigma;
		return agent.getEnergy() >= threshold;
	}
}
