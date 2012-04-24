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
 * File: SendMessageActionContext.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: SendMessageActionContext.java 177 2012-03-31 18:32:12Z faber $
 */

package org.jage.action.context;

import org.jage.address.IAgentAddress;
import org.jage.communication.message.IMessage;

import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The action of sending a message to an agent.
 * 
 * @author AGH AgE Team
 */
@AgentActionContext(SendMessageActionContext.ACTION_NAME)
public class SendMessageActionContext extends AbstractAgentActionContext {

	/**
	 * The action name of this context.
	 */
	public static final String ACTION_NAME = "sendMessage";

	/**
	 * The message to send.
	 */
	private final IMessage<IAgentAddress, ?> message;

	/**
	 * Constructor.
	 * 
	 * @param message
	 *            the message to send
	 */
	public SendMessageActionContext(final IMessage<IAgentAddress, ?> message) {
		this.message = checkNotNull(message);
	}

	/**
	 * Returns the message to send.
	 * 
	 * @return the message to send
	 */
	public IMessage<IAgentAddress, ?> getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("sender", message.getHeader().getSenderAddress()).toString();
	}

}
