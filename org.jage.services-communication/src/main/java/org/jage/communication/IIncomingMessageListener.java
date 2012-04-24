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
 * File: IMessageListener.java
 * Created: 2010-09-09
 * Author: kpietak
 * $Id: IIncomingMessageListener.java 63 2012-02-13 13:56:38Z faber $
 */

package org.jage.communication;

import java.io.Serializable;

import org.jage.address.node.IComponentAddress;
import org.jage.communication.message.IMessage;

/**
 * The interface for all components that want to be notified about messages coming to the node from remote nodes.
 * <p>
 * The message delivered via this interface is deleted from the communication service.
 * 
 * @author AGH AgE Team
 */
public interface IIncomingMessageListener {

	/**
	 * Delivers a received message to this listener.
	 * 
	 * @param message
	 *            A message to be delivered to the component.
	 */
	void deliver(IMessage<? extends IComponentAddress, ? extends Serializable> message);
}
