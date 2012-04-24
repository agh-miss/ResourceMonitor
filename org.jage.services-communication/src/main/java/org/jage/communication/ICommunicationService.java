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
 * File: ICommunicationService.java
 * Created: 2010-09-08
 * Author: kpietak
 * $Id: ICommunicationService.java 139 2012-03-19 22:54:18Z faber $
 */

package org.jage.communication;

import java.io.Serializable;

import org.jage.address.node.IComponentAddress;
import org.jage.communication.message.IMessage;

/**
 * Simple interface for a communication service which provides communication between nodes in distributed environment.
 * It is particularly provided for all components that want to use communication service explicitly.
 * 
 * @author AGH AgE Team
 */
public interface ICommunicationService {

	/**
	 * Sends the message to another component, possibly located in another node.
	 * 
	 * @param message
	 *            A message to send, cannot be <code>null</code>.
	 */
	void send(IMessage<? extends IComponentAddress, ? extends Serializable> message);

	/**
	 * Receives a next message for a given component name. This is non-blocking method --- if there is no message for
	 * the component, <code>null</code> is returned instead.
	 * <p>
	 * Please note, that if component realizes the {@link IIncomingMessageListener} interface, this method will most of
	 * the time return <code>null</code>, as messages will be delivered through the listener.
	 * 
	 * @param componentName
	 *            A name of the requesting component.
	 * @return A new message or <code>null</code>.
	 */
	IMessage<IComponentAddress, Serializable> receive(String componentName);

	/**
	 * Enters a global barrier. "Global" means that all nodes in the known environment must participate. The barrier is
	 * created for the given key (that needs to be common among all the nodes). Different node services may (and should)
	 * use different keys to block on independent barriers.
	 * <p>
	 * 
	 * This call blocks a current thread until all of the nodes call it with the same key.
	 * <p>
	 * 
	 * If any node is interrupted while waiting, then all other waiting nodes will throw {@link CommunicationException}
	 * and the barrier is destroyed.
	 * 
	 * @param key
	 *            a key for the barrier. Must be the same for all the participating nodes.
	 *            
	 * @throws InterruptedException
	 *             if the thread waiting on the barrier was interrupted.
	 * @throws CommunicationException
	 *             if the barrier cannot be created or when it was destroyed due to any reason.
	 */
	void barrier(String key) throws InterruptedException;
}
