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
 * File: AgentEnvironmentHelper.java
 * Created: 2009-03-16
 * Author: drush
 * $Id: AgentEnvironmentHelper.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.workplace;

import java.util.Collection;
import java.util.UUID;

import static org.mockito.Mockito.mock;

import org.jage.address.AgentAddress;
import org.jage.address.IAgentAddress;
import org.jage.address.node.INodeAddress;
import org.jage.agent.IAgent;
import org.jage.agent.IAgentEnvironment;
import org.jage.query.AgentEnvironmentQuery;

/**
 * A helper that implements an agent environment.
 *
 * @author AGH AgE Team
 */
public class AgentEnvironmentHelper implements IAgentEnvironment {

	public boolean registerAddress(IAgentAddress address) {
		return true;
	}

	public boolean unregisterAddress(IAgentAddress address) {
		return true;
	}

	@Override
	public IAgentAddress getAddress() {
		return new AgentAddress(UUID.randomUUID(), mock(INodeAddress.class), null);
	}

	@Override
	public <E extends IAgent, T> Collection<T> queryParent(AgentEnvironmentQuery<E, T> query) {
		return null;
	}

}
