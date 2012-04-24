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
 * File: ControlChannelMessage.java
 * Created: 2012-02-07
 * Author: faber
 * $Id: ControlChannelMessage.java 134 2012-03-18 20:17:01Z faber $
 */

package org.jage.communication.hazelcast.scanner;

import java.io.Serializable;

import org.jage.address.node.INodeAddress;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Message used by the control channel protocol.
 * <p>
 * We have two types of those:
 * <ul>
 * <li>REQUEST which tells other nodes to post their addresses on the control channel immediately; it's a kind of global
 * refreshing mechanism
 * <li>OFFER is a form in which nodes are posting their addresses to the channel; it's done when a REQUEST was issued
 * and also involuntarily periodically, between REQUESTs
 * </ul>
 * 
 * @author AGH AgE Team
 */
public class ControlChannelMessage implements Serializable {

	private static final long serialVersionUID = 4996968735716015365L;

	private INodeAddress sender;

	private Type type;

	protected ControlChannelMessage(INodeAddress sender, Type type) {
		this.sender = checkNotNull(sender);
		this.type = checkNotNull(type);
	}

	/**
	 * Creates a REQUEST message.
	 * 
	 * @param sender
	 *            A source of the message.
	 * @return New REQUEST message targeted for the communication channel.
	 */
	public static ControlChannelMessage createRequest(INodeAddress sender) {
		return new ControlChannelMessage(sender, Type.REQUEST);
	}

	/**
	 * Creates an OFFER message.
	 * 
	 * @param sender
	 *            A source of the message.
	 * @return New OFFER message targeted for the communication channel.
	 */
	public static ControlChannelMessage createOffer(INodeAddress sender) {
		return new ControlChannelMessage(sender, Type.OFFER);
	}

	/**
	 * Returns the sender of this message.
	 * 
	 * @return Sender (node) address.
	 */
	public INodeAddress getSender() {
		return sender;
	}

	/**
	 * Returns the type of this message.
	 * 
	 * @return Type of the message.
	 */
	public Type getType() {
		return type;
	}

	@Override
	public String toString() {
		return toStringHelper(this).add("type", type).add("sender", sender).toString();
	}

	/**
	 * Hazelcast control channel message types.
	 * 
	 * @author AGH AgE Team
	 */
	public static enum Type {
		/**
		 * REQUEST tells other nodes to post their addresses on the control channel immediately.
		 */
		REQUEST,
		/**
		 * OFFER is a message in which nodes are posting their addresses to the channel.
		 */
		OFFER;
	}

}
