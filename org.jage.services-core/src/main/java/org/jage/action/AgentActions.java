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
 * File: AgentActions.java
 * Created: 2012-04-07
 * Author: Krzywicki
 * $Id: AgentActions.java 213 2012-04-08 20:21:35Z krzywick $
 */

package org.jage.action;

import org.jage.action.context.AddAgentActionContext;
import org.jage.action.context.GetAgentActionContext;
import org.jage.action.context.KillAgentActionContext;
import org.jage.action.context.MoveAgentActionContext;
import org.jage.action.context.PassToParentActionContext;
import org.jage.action.context.SendMessageActionContext;
import org.jage.address.IAgentAddress;
import org.jage.address.selector.agent.ParentAgentAddressSelector;
import org.jage.agent.IAgent;
import org.jage.communication.message.IMessage;

import static org.jage.address.selector.AgentSelectors.unicastFor;

/**
 * Factory methods for agent actions.
 *
 * @author AGH AgE Team
 */
public class AgentActions {

	/**
	 * Creates an action which adds the second agent to the parent of the first one.
	 *
	 * @param agent
	 *            the agent to which parent the new agent will be added
	 * @param newAgent
	 *            the agent to be added
	 * @return an addToParent action
	 */
	public static Action addToParent(final IAgent agent, final IAgent newAgent) {
		AddAgentActionContext context = new AddAgentActionContext(newAgent);
		return new SingleAction(new ParentAgentAddressSelector(agent.getAddress()), context);
	}

	/**
	 * Creates a death action.
	 *
	 * @param agent
	 *            the agent to be killed
	 * @return a death action
	 */
	public static Action death(final IAgent agent) {
		return new SingleAction(unicastFor(agent.getAddress()), new KillAgentActionContext());
	}

	/**
	 * Creates a migration action.
	 *
	 * @param agent
	 *            the agent to be migrated
	 * @param destination
	 *            the migration destination
	 * @return a migration action
	 */
	public static Action migrate(final IAgent agent, final IAgentAddress destination) {
		MoveAgentActionContext moveContext = new MoveAgentActionContext();
		SingleAction moveAction = new SingleAction(unicastFor(destination), moveContext);
		PassToParentActionContext parentContext = new PassToParentActionContext(agent.getAddress(), moveAction);
		GetAgentActionContext getAgentContext = new GetAgentActionContext(moveContext);

		ComplexAction action = new ComplexAction();
		action.addChild(new SingleAction(unicastFor(agent.getAddress()), getAgentContext));
		action.addChild(new SingleAction(new ParentAgentAddressSelector(agent.getAddress()), parentContext));
		return action;
	}

	/**
	 * Creates a send message action.
	 *
	 * @param message
	 *            the message to be sent
	 * @return a send message action
	 */
	public static Action sendMessage(final IMessage<IAgentAddress, ?> message) {
		return new SingleAction(message.getHeader().getReceiverSelector(), new SendMessageActionContext(message));
	}
}
