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
 * File: IAgent.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: IAgent.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.agent;

import java.io.Serializable;

import org.jage.address.IAgentAddress;
import org.jage.communication.message.IMessage;
import org.jage.platform.component.IStatefulComponent;
import org.jage.property.IPropertyContainer;

/**
 * Interface of an agent.
 * 
 * @author AGH AgE Team
 */
public interface IAgent extends IPropertyContainer, IStatefulComponent, Serializable {

	/**
	 * Delivers a message to the agent.
	 * 
	 * @param message
	 *            A message to be delivered.
	 */
	void deliverMessage(IMessage<IAgentAddress, ?> message);

	/**
	 * Returns the agent's address. See also property address of this class.
	 * 
	 * @return The agent's address.
	 */
	public IAgentAddress getAddress();

	/**
	 * Sets new agent environment to this agent. Setting null means that an agent is removed from the environment. Agent
	 * should not be moved to new environment without removing from previous one.
	 * 
	 * @param agentEnvironment
	 *            The environment for the agent.
	 * @throws AlreadyExistsException
	 *             when environment is already set.
	 */
	public void setAgentEnvironment(IAgentEnvironment agentEnvironment) throws AlreadyExistsException;

}
