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
 * File: DeathAction.java
 * Created: 2012-01-30
 * Author: Krzywicki
 * $Id: DeathAction.java 211 2012-04-08 20:06:03Z krzywick $
 */

package org.jage.emas.action.individual;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.action.SingleAction;
import org.jage.action.context.KillAgentActionContext;
import org.jage.address.IAgentAddress;
import org.jage.agent.AgentException;
import org.jage.emas.agent.IndividualAgent;
import org.jage.emas.predicate.IPredicate;
import org.jage.emas.util.ChainingAction;
import org.jage.platform.component.annotation.Inject;

import static org.jage.address.selector.AgentSelectors.unicastFor;

/**
 * This action handler performs death actions on agent. Depending on some predicate, an agent may die.
 *
 * @author AGH AgE Team
 */
public final class DeathAction extends ChainingAction<IndividualAgent> {

	private static final Logger log = LoggerFactory.getLogger(DeathAction.class);

	@Inject
	private IPredicate<IndividualAgent> deathPredicate;

	@Override
	protected void doPerform(final IndividualAgent agent) throws AgentException {
		if (deathPredicate.apply(agent)) {
			log.debug("Agent {} is dying.", agent);
			IAgentAddress agentAddress = agent.getAddress();
			SingleAction deathAction = new SingleAction(unicastFor(agentAddress), new KillAgentActionContext());
			agent.getEnvironment().doAction(deathAction, agentAddress);
		}
	}
}
