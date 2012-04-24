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
 * File: IWorkplaceEnvironment.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: IWorkplaceEnvironment.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.workplace;

import java.util.Collection;

import org.jage.address.IAgentAddress;
import org.jage.agent.IAgent;
import org.jage.communication.message.IMessage;
import org.jage.query.AgentEnvironmentQuery;

/**
 * Interface used by agents to communicate with the workplace.
 * 
 * @author AGH AgE Team
 */
public interface IWorkplaceEnvironment {

	/**
	 * Notify stopped.
	 * 
	 * @param workplace
	 *            A workplace that stopped.
	 */
	void notifyStopped(IWorkplace workplace);

	/**
	 * Queries workplaces located in this environment.
	 * 
	 * @param query
	 *            The query to perform.
	 * @param <E>
	 *            A type of elements in the collection. E must be a realisation of {@link IAgent}.
	 * @param <T>
	 *            A type of elements in the result.
	 * @return the result of the query
	 */
	<E extends IAgent, T> Collection<T> queryWorkplaces(AgentEnvironmentQuery<E, T> query);

	/**
	 * Sends a message to other workplaces that are located in this environment.
	 * 
	 * @param message
	 *            The message to send.
	 */
	void sendMessage(IMessage<IAgentAddress, ?> message);
}
