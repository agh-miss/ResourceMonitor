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
 * File: IAgentMonitor.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: IAgentMonitor.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.monitor;

import org.jage.event.AggregateEvent;

/**
 * The interface for monitors of events involving agents. It enables an agent container (for instance an aggregate) to
 * inform an external object about adding/removing/killing/cloning/moving an agent.
 * 
 * @author AGH AgE Team
 */
public interface IAgentMonitor extends IMonitor {

	/**
	 * This method is executed when an agent has been added to the monitored container of agents (for instance,
	 * aggregate). Note: the monitor can be notified in a short time after it is unregistered.
	 * 
	 * @param event
	 *            the event of adding the agent
	 */
	public void agentAdded(AggregateEvent event);

	/**
	 * This method is executed when an agent has been removed from the monitored container of agents.
	 * 
	 * @param event
	 *            the event of removing the agent
	 */
	public void agentRemoved(AggregateEvent event);

	/**
	 * This method is executed when an agent has been killed in the monitored container of agents.
	 * 
	 * @param event
	 *            the event of killing the agent
	 */
	public void agentKilled(AggregateEvent event);

	/**
	 * This method is executed when an agent has been cloned in the monitored container of agents.
	 * 
	 * @param event
	 *            the event of cloning the agent
	 */
	public void agentCloned(AggregateEvent event);

	/**
	 * This method is executed when an agent has moved in the monitored container of agents.
	 * 
	 * @param event
	 *            the event of moving the agent
	 */
	public void agentMovedIn(AggregateEvent event);

	/**
	 * This method is executed when an agent has moved out of the monitored container of agents.
	 * 
	 * @param event
	 *            the event of moving the agent
	 */
	public void agentMovedOut(AggregateEvent event);
}
