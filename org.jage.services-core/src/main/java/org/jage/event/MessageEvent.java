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
 * File: MessageEvent.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: MessageEvent.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.event;

import org.jage.address.IAgentAddress;
import org.jage.agent.IAgent;
import org.jage.agent.IAggregate;
import org.jage.communication.message.IMessage;

/**
 * This event is created when a message has been sent by/delivered to an agent.
 * 
 * @author AGH AgE Team
 */
public class MessageEvent extends AggregateEvent {

	/**
	 * The sent/delivered message.
	 */
	private final IMessage<IAgentAddress, ?> message;

	/**
	 * The receiver of the message.
	 */
	private final IAgentAddress receiver;

	/**
	 * Constructor.
	 * 
	 * @param eventCreator
	 *            the creator of this event
	 * @param message
	 *            the sent/delivered message
	 * @param receiver
	 *            the receiver of the message
	 */
	public MessageEvent(IAggregate eventCreator, IMessage<IAgentAddress, ?> message, IAgentAddress receiver) {
		super(eventCreator);
		this.message = message;
		this.receiver = receiver;
	}

	/**
	 * Returns the sent/delivered message.
	 * 
	 * @return the sent/delivered message
	 */
	public IMessage<IAgentAddress, ?> getMessage() {
		return message;
	}

	/**
	 * Returns the receiver of the message.
	 * 
	 * @return the receiver of the message
	 */
	public IAgentAddress getReceiver() {
		return receiver;
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Message event [creator: "
		        + (parent == null ? "IWorkplace" : ((IAgent)parent).getAddress().toString()) + ", message: "
		        + message + ", receiver: " + receiver + "]";
	}

}
