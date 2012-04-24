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
 * File: SimpleAgent.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: SimpleAgent.java 213 2012-04-08 20:21:35Z krzywick $
 */

package org.jage.agent;

import java.util.Collection;

import org.jage.action.Action;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * Base class for agents processed in step simulation. For each step of simulation the parent of the agent executes
 * {@link #step()} method.
 *
 * @author AGH AgE Team
 */
public abstract class SimpleAgent extends AbstractAgent implements ISimpleAgent {

	private static final long serialVersionUID = 6487323250205969364L;

	/**
	 * Current local environment. Can change only after migration.
	 */
	private transient ISimpleAgentEnvironment agentEnv = null;

	@Override
	public void setAgentEnvironment(final IAgentEnvironment environment) throws AlreadyExistsException {
		if(environment == null) {
			agentEnv = null;
		} else if (agentEnv == null) {
			checkArgument(environment instanceof ISimpleAgentEnvironment);
			agentEnv = (ISimpleAgentEnvironment)environment;
		} else {
			throw new AlreadyExistsException("Agent environment is already set.");
		}
	}

	@Override
	protected ISimpleAgentEnvironment getAgentEnvironment() {
		return agentEnv;
	}

	/**
	 * Submits an action to be executed in the local environment.
	 *
	 * @param action
	 *            the action to be executed
	 * @throws AgentException
	 *             if the action is incorrect
	 */
	protected void doAction(final Action action) throws AgentException {
		checkState(agentEnv != null, "Agent environment is not available.");
		agentEnv.doAction(action, address);
	}

	protected void doActions(final Collection<? extends Action> actions) throws AgentException {
		checkState(agentEnv != null, "Agent environment is not available.");
		for (Action action : actions) {
			agentEnv.doAction(action, address);
		}
	}
}
