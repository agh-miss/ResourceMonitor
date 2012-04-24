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
 * File: ConnectedSimpleWorkplace.java
 * Created: 2011-09-02
 * Author: faber
 * $Id: ConnectedSimpleWorkplace.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.workplace;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.action.AgentAction;
import org.jage.action.context.PassToParentActionContext;
import org.jage.address.IAgentAddress;
import org.jage.agent.AgentException;
import org.jage.agent.IAgent;
import org.jage.agent.ISimpleAgent;
import org.jage.communication.message.IMessage;
import org.jage.query.AgentEnvironmentQuery;

/**
 * Workplace that can be used in a multiworkplace environment.
 * <p>
 * The only mechanism that it provides and that is not provided by a {@link SimpleWorkplace} is the query forwarding.
 * 
 * @author AGH AgE Team
 */
public class ConnectedSimpleWorkplace extends SimpleWorkplace {

	private static final long serialVersionUID = -3126941681607203590L;

	private final Logger log = LoggerFactory.getLogger(ConnectedSimpleWorkplace.class);

	@Override
	public <E extends IAgent, T> Collection<T> queryParent(AgentEnvironmentQuery<E, T> query) {
		try {
			return queryEnvironment(query);
		} catch (AgentException ae) {
			log.error("Can't query parent environment.", ae);
			return null;
		}
	}

	@Override
	protected <E extends IAgent, T> Collection<T> queryEnvironment(AgentEnvironmentQuery<E, T> query)
	        throws AgentException {
		IWorkplaceEnvironment workplaceEnvironment = getWorkplaceEnvironment();
		if (workplaceEnvironment == null) {
			throw new AgentException("Workplace environment is not available");
		}
		return workplaceEnvironment.queryWorkplaces(query);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * In the case of this workplace a message will be sent to other workplaces.
	 * 
	 * @see org.jage.agent.SimpleAggregate#sendMessage(org.jage.communication.message.IMessage)
	 */
	@Override
	protected void sendMessage(IMessage<IAgentAddress, ?> message) {
		IWorkplaceEnvironment workplaceEnvironment = getWorkplaceEnvironment();
		if (workplaceEnvironment == null) {
			throw new RuntimeException(new WorkplaceException("Workplace does not have a parent."));
		}
		workplaceEnvironment.sendMessage(message);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Actions cannot be executed in a non-simple agent environment. When a workplace receives a PassToParent action it
	 * must throw an exception.
	 */
	@Override
	@AgentAction(name = "passToParent")
	protected void performPassToParentAction(ISimpleAgent target, PassToParentActionContext context)
	        throws AgentException {
		throw new AgentException(String.format(
		        "Cannot pass action %1$s to environment - it is not instance of ISimpleAgentEnvironment.",
		        context.getAction()));
	}
}
