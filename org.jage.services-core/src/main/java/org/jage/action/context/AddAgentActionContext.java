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
 * File: AddAgentActionContext.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: AddAgentActionContext.java 177 2012-03-31 18:32:12Z faber $
 */

package org.jage.action.context;

import org.jage.agent.IAgent;

import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The action of adding an agent to an aggregate.
 *
 * @author AGH AgE Team
 */
@AgentActionContext(AddAgentActionContext.ACTION_NAME)
public class AddAgentActionContext extends AbstractAgentActionContext {

	/**
	 * The action name of this context.
	 */
	public static final String ACTION_NAME = "addAgent";

	/**
	 * The agent to add to an aggregate.
	 */
	private final IAgent agent;

	/**
	 * Constructor.
	 *
	 * @param agent
	 *            the agent to add
	 */
	public AddAgentActionContext(final IAgent agent) {
		this.agent = checkNotNull(agent);
	}

	/**
	 * Returns the agent to add.
	 *
	 * @return the agent to add
	 */
	public IAgent getAgent() {
		return agent;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("agent", agent).toString();
	}
}
