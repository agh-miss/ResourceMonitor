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
 * File: RandomDestinationMigration.java
 * Created: 2012-01-30
 * Author: Krzywicki
 * $Id: RandomDestinationMigration.java 211 2012-04-08 20:06:03Z krzywick $
 */

package org.jage.emas.migration;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.action.AgentActions;
import org.jage.agent.AgentException;
import org.jage.agent.IAgent;
import org.jage.agent.IAgentEnvironment;
import org.jage.emas.agent.IndividualAgent;
import org.jage.platform.component.annotation.Inject;
import org.jage.query.AgentEnvironmentQuery;
import org.jage.random.IIntRandomGenerator;

import com.google.common.base.Predicate;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;

/**
 * Random destination migration strategy. Chooses a destination at random from the agent's parent's siblings.
 *
 * @author AGH AgE Team
 */
public class RandomDestinationMigration implements Migration<IndividualAgent> {

	private static final Logger log = LoggerFactory.getLogger(RandomDestinationMigration.class);

	@Inject
	private IIntRandomGenerator rand;

	@Override
	public void migrate(final IndividualAgent agent) throws AgentException {
		List<IAgent> islands = getSurroundingIslands(agent.getEnvironment());
		if (!islands.isEmpty()) {
			IAgent destination = islands.get(rand.nextInt(islands.size()));
			doMigrate(agent, destination);
			log.debug("Migrating {} to {}", agent, destination);
		}
	}

	private List<IAgent> getSurroundingIslands(final IAgentEnvironment environment) {
		Collection<IAgent> islands = environment.queryParent(new AgentEnvironmentQuery<IAgent, IAgent>());
		Predicate<IAgent> predicate = new Predicate<IAgent>() {
			@Override
			public boolean apply(final IAgent input) {
				return !input.getAddress().equals(environment.getAddress());
			}
		};
		return newArrayList(filter(islands, predicate));
	}

    private void doMigrate(final IndividualAgent agent, final IAgent destination) throws AgentException {
    	agent.getEnvironment().doAction(AgentActions.migrate(agent, destination.getAddress()), agent.getAddress());
    }
}
