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
 * File: IAggregate.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: IAggregate.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.agent;

import java.util.Collection;

import org.jage.address.IAgentAddress;
import org.jage.monitor.IAgentMonitor;
import org.jage.monitor.IMessageMonitor;
import org.jage.monitor.IQueryMonitor;

/**
 * Interface to all agents which can contain other agents.
 * 
 * They execute their code and are responsible for their "life".
 * 
 * @author AGH AgE Team
 */
public interface IAggregate extends IAgent, Collection<IAgent> {

	/**
	 * Checks if this aggregate contains an agent with the given address.
	 * 
	 * @param address
	 *            the address of agent to check.
	 * @return <code>true</code> if this aggregate contains the agent; <code>false
	 *         </code>- otherwise.
	 */
	public boolean containsAgent(IAgentAddress address);

	/**
	 * Returns the agent with the specified address.
	 * 
	 * @param address
	 *            the address of an agent
	 * @return the agent with the specified address
	 */
	public IAgent getAgent(IAgentAddress address);

	/**
	 * Removes the agent from this aggregate.
	 * 
	 * @param address
	 *            Address of the agent to remove.
	 * @throws AgentException
	 *             occurs when the aggregate cannot remove the agent
	 */
	public void removeAgent(IAgentAddress address) throws AgentException;

	/**
	 * Adds a monitor for incoming and outgoing messages.
	 * 
	 * @param monitor
	 *            the monitor to add
	 */
	public void addMessageMonitor(IMessageMonitor monitor);

	/**
	 * Removes the monitor for incoming and outgoing messages.
	 * 
	 * @param monitor
	 *            the monitor to remove
	 * @return the removed monitor or <TT>null</TT> if there was no such monitor
	 */
	public IMessageMonitor removeMessageMonitor(IMessageMonitor monitor);

	/**
	 * Adds a monitor for queries.
	 * 
	 * @param monitor
	 *            the monitor to add
	 */
	public void addQueryMonitor(IQueryMonitor monitor);

	/**
	 * Removes the monitor for queries.
	 * 
	 * @param monitor
	 *            the monitor to remove
	 * @return the removed monitor or <TT>null</TT> if there was no such monitor
	 */
	public IQueryMonitor removeQueryMonitor(IQueryMonitor monitor);

	/**
	 * Adds a monitor for events involving agents (cloning, migration, etc.).
	 * 
	 * @param monitor
	 *            the monitor to add
	 */
	public void addAgentMonitor(IAgentMonitor monitor);

	/**
	 * Removes the monitor for events involving agents.
	 * 
	 * @param monitor
	 *            the monitor to remove
	 * @return the removed monitor or <TT>null</TT> if there was no such monitor
	 */
	public IAgentMonitor removeAgentMonitor(IAgentMonitor monitor);

}
