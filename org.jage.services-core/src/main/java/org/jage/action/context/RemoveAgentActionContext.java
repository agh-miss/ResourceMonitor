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
 * File: RemoveAgentActionContext.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: RemoveAgentActionContext.java 177 2012-03-31 18:32:12Z faber $
 */

package org.jage.action.context;

import org.jage.address.IAgentAddress;

import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The action of removing an agent from an aggregate.
 * 
 * @author AGH AgE Team
 */
@AgentActionContext(RemoveAgentActionContext.ACTION_NAME)
public class RemoveAgentActionContext extends AbstractAgentActionContext {

	/**
	 * The action name of this context.
	 */
	public static final String ACTION_NAME = "removeAgent";

	/**
	 * The address of agent to remove.
	 */
	private final IAgentAddress agentAddress;

	/**
	 * Constructor.
	 * 
	 * @param agentAddress
	 *            the address of agent to remove
	 */
	public RemoveAgentActionContext(final IAgentAddress agentAddress) {
		this.agentAddress = checkNotNull(agentAddress);
	}

	/**
	 * Returns the address of agent to remove.
	 * 
	 * @return the address of agent to remove
	 */
	public IAgentAddress getAgentAddress() {
		return agentAddress;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("agentAddress", agentAddress).toString();
	}

}
