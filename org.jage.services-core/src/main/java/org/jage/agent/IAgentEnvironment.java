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
 * File: IAgentEnvironment.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: IAgentEnvironment.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.agent;

import java.util.Collection;

import org.jage.address.IAgentAddress;
import org.jage.query.AgentEnvironmentQuery;

/**
 * Interface used by agents to contact with the external environment (their parents).
 * 
 * @author AGH AgE Team
 */
public interface IAgentEnvironment {

	/**
	 * Queries an environment of the parent.
	 * <p>
	 * 
	 * <pre>
	 *         T
	 *        /|\
	 *       / | \
	 *      A  B  C
	 *     /|\
	 *    / | \
	 *  *X  Y  Z
	 * </pre>
	 * 
	 * The parent environment in the above situation (when seen from the agent X (with an asterisk)) consists of agents:
	 * A, B and C (agents of the T aggregate).
	 * 
	 * @param <E>
	 *            A type of the agents in the aggregate.
	 * @param <T>
	 *            A type of the elements in the result.
	 * @param query
	 *            The query to perform.
	 * @return The result of the query.
	 */
	public <E extends IAgent, T> Collection<T> queryParent(AgentEnvironmentQuery<E, T> query);

	/**
	 * Returns the parent's address.
	 * 
	 * @return The parent's address
	 */
	public IAgentAddress getAddress();

}
