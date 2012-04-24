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
 * File: IMessageReceivedListener.java
 * Created: 2012-02-07
 * Author: faber
 * $Id: IMessageReceivedListener.java 58 2012-02-10 14:25:54Z faber $
 */

package org.jage.communication.common.protocol;

import java.io.Serializable;

import org.jage.address.node.IComponentAddress;
import org.jage.communication.message.IMessage;

/**
 * Interface for those who want to be informed every time a new message arrives to ICommunicationProtocol from a remote
 * node.
 * 
 * @author AGH AgE Team
 */
public interface IMessageReceivedListener {

	/**
	 * Informs that a new message has arrived from a remote node.
	 * 
	 * @param msg
	 *            the received message
	 */
	void onMessageReceived(IMessage<IComponentAddress, Serializable> msg);

}
