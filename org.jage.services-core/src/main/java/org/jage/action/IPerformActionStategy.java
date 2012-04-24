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
 * File: IPerformActionStategy.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: IPerformActionStategy.java 166 2012-03-30 08:26:53Z faber $
 */

package org.jage.action;

import java.util.Collection;

import org.jage.address.IAgentAddress;
import org.jage.address.selector.BroadcastSelector;
import org.jage.agent.AgentException;
import org.jage.agent.IAgent;
import org.jage.agent.ISimpleAggregate;

/**
 * An interface for all actions implemented as strategies.
 * 
 * @author AGH AgE Team
 */
public interface IPerformActionStategy {

	/**
	 * Performs initialization steps and validates addresses used in action. This method can be used to perform some
	 * additional steps before addresses validation, i.e. a new agent can be added to aggregate so that other actions be
	 * executed on it.
	 * 
	 * @param aggregate
	 *            aggregate which executes action
	 * @param action
	 *            single action to validate
	 * @return collection of agent addresses used in action, if no addresses is used (action has selectors such as
	 *         {@link BroadcastSelector}, etc.) the empty collection is returned; <code>null</code> is returned when
	 *         action didn't validate addresses - then a default validation is performed
	 * @throws AgentException
	 *             when validation fails.
	 * 
	 * @see ActionPhase#INIT
	 */
	Collection<IAgentAddress> init(ISimpleAggregate aggregate, SingleAction action) throws AgentException;

	/**
	 * Performs main phase of action ({@link ActionPhase#MAIN}) using data in object of IActionContext type which is in
	 * event object parameter.
	 * 
	 * @param target
	 *            reference to the target agent, which action will be performed on
	 * @param context
	 *            action context
	 * @throws AgentException
	 *             when action execution fails.
	 */
	void perfom(IAgent target, IActionContext context) throws AgentException;

	/**
	 * Executes finalization phase (({@link ActionPhase#FINISH}) which is executed after performing main phase of
	 * action.
	 * 
	 * @param target
	 *            reference to the target agent, which action will be performed on
	 * @param context
	 *            action context
	 */
	void finish(IAgent target, IActionContext context);

}
