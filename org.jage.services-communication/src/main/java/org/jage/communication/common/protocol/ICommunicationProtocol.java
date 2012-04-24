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
 * File: ICommunicationProtocol.java
 * Created: 2012-02-07
 * Author: faber
 * $Id: ICommunicationProtocol.java 139 2012-03-19 22:54:18Z faber $
 */

package org.jage.communication.common.protocol;

import java.io.Serializable;

import org.jage.address.node.IComponentAddress;
import org.jage.communication.CommunicationException;
import org.jage.communication.message.IMessage;
import org.jage.platform.component.IStatefulComponent;

/**
 * Encapsulates the way in which the ICommunicationManager is dealing with sending and receiving of messages on the
 * wire.
 * <p>
 * It sends messages on request.
 * <p>
 * When it comes to receiving messages, it allows other to register with it as listeners of "message-received" events. A
 * protocol will inform those listeners each time a message from a remote node arrives.
 * 
 * @author AGH AgE Team
 */
public interface ICommunicationProtocol extends IStatefulComponent {

	/**
	 * Initializes this protocol.
	 */
	@Override
	void init();

	/**
	 * Stops the protocol, possibly with a short delay.
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	boolean finish();

	/**
	 * Sends the given message to its receiver.
	 * 
	 * @param message
	 *            A message to send.
	 */
	void send(IMessage<IComponentAddress, Serializable> message);

	/**
	 * Registers a listener interested in knowing when a new message arrives from a remote node.
	 * 
	 * @param listener
	 *            A listener to register.
	 */
	void addMessageReceivedListener(IMessageReceivedListener listener);

	/**
	 * Enters a global barrier. "Global" means that all nodes in the known environment can participate. Exactly
	 * <code>count</code> nodes need to enter the barrier. The barrier is created for the given key (that needs to be
	 * common among all the nodes). Different node services may (and should) use different keys to block on independent
	 * barriers.
	 * <p>
	 * 
	 * This call blocks a current thread until <code>count</code> of the nodes call it with the same key.
	 * <p>
	 * 
	 * If any node is interrupted while waiting, then all other waiting nodes will throw {@link CommunicationException}
	 * and the barrier is destroyed.
	 * 
	 * @param key
	 *            a key for the barrier. Must be the same for all the participating nodes.
	 * @param count
	 *            a number of nodes that need to participate in the barrier.
	 * 
	 * @throws InterruptedException
	 *             if the thread waiting on the barrier was interrupted.
	 * @throws CommunicationException
	 *             if the barrier cannot be created or when it was destroyed due to any reason.
	 */
	void barrier(String key, int count) throws InterruptedException;
}
