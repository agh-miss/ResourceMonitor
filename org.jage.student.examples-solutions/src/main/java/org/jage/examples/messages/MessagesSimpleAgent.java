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
 * File: MessagesSimpleAgent.java
 * Created: 2011-03-15
 * Author: kpietak
 * $Id: MessagesSimpleAgent.java 213 2012-04-08 20:21:35Z krzywick $
 */

package org.jage.examples.messages;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.action.AgentActions;
import org.jage.address.IAgentAddress;
import org.jage.address.selector.UnicastSelector;
import org.jage.agent.AgentException;
import org.jage.agent.SimpleAgent;
import org.jage.communication.message.Header;
import org.jage.communication.message.IMessage;
import org.jage.communication.message.Message;
import org.jage.platform.component.annotation.Inject;
import org.jage.platform.component.exception.ComponentException;
import org.jage.property.InvalidPropertyPathException;
import org.jage.query.AgentEnvironmentQuery;

/**
 * This agent sends and receives messages.
 *
 * @author AGH AgE Team
 */
public class MessagesSimpleAgent extends SimpleAgent {

	private static final long serialVersionUID = 2L;

	private final Logger log = LoggerFactory.getLogger(MessagesSimpleAgent.class);

	/**
	 * Steps counter
	 */
	private transient int counter = 0;

	/**
	 * Message receiver
	 */
	private String receiver = null;

	private IAgentAddress receiverAddress = null;

	/**
	 * Content of the message
	 */
	private String message = null;

	/**
	 * Constructs a new MessagesSimpleAgent without an address.
	 */
	@Inject
	public MessagesSimpleAgent() {
		log.info("Constructing");
	}

	@Override
	public void init() throws ComponentException {
		super.init();
		log.info("Initializing agent: {}", getAddress());
	}

	/**
	 * Executes a step of this agent. This agent sometimes sends a message and reads received messages from other
	 * agents.
	 * <p>
	 * {@inheritDoc}
	 *
	 * @see org.jage.agent.SimpleAgent#step()
	 */
	@Override
	public void step() {
		if (receiverAddress == null) {
			try {
				AgentEnvironmentQuery<SimpleAgent, SimpleAgent> query = new AgentEnvironmentQuery<SimpleAgent, SimpleAgent>();
				Collection<SimpleAgent> answer = queryEnvironment(query);

				for (SimpleAgent entry : answer) {
					IAgentAddress agentAddress = (IAgentAddress)entry.getProperty("address").getValue();
					if (agentAddress.getName().startsWith(receiver)) {
						receiverAddress = agentAddress;
					}
				}
			} catch (AgentException e) {
				log.error("Agent exception", e);
			} catch (InvalidPropertyPathException e) {
				log.error("Agent exception", e);
			}
		}

		counter++;
		if ((counter + hashCode()) % 20 == 0) {
			send();
		}

		IMessage<IAgentAddress, String> message = null;
		do {
			message = receiveMessage();
			if (message != null) {
				log.info("Agent: {} received message from: {}:", getAddress(), message.getHeader().getSenderAddress());
				log.info("    {}", message.getPayload());
			} else {
				break;
			}
		} while (true);

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			log.error("Interrupted", e);
		}
	}

	@Override
	public boolean finish() {
		log.info("Finishing {}", getAddress());
		return true;
	}

	/**
	 * Sends a message to the specified receiver.
	 */
	private void send() {
		if (message != null) {
			Header<IAgentAddress> header = new Header<IAgentAddress>(getAddress(), new UnicastSelector<IAgentAddress>(
			        receiverAddress));
			Message<IAgentAddress, String> textMessage = new Message<IAgentAddress, String>(header, message);
			try {
				doAction(AgentActions.sendMessage(textMessage));
			} catch (AgentException e) {
				log.error("Can't send a message.", e);
			}
		}
	}

	/**
	 * Sets the receiver of messages.
	 *
	 * @param receiver
	 *            the receiver.
	 */
	public void setReceiver(final String receiver) {
		this.receiver = receiver;
	}

	/**
	 * Sets the message contents.
	 *
	 * @param message
	 *            message contents.
	 */
	public void setMessage(final String message) {
		this.message = message;
	}
}
