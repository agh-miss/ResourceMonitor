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
 * File: MessageTestUtils.java
 * Created: 2012-02-07
 * Author: faber
 * $Id: MessageTestUtils.java 134 2012-03-18 20:17:01Z faber $
 */

package org.jage.communication.message;

import java.io.Serializable;

import static org.mockito.Mockito.mock;

import org.jage.address.node.ComponentAddress;
import org.jage.address.node.IComponentAddress;
import org.jage.address.node.INodeAddress;
import org.jage.address.selector.UnicastSelector;

/**
 * Utilities for message-related tests.
 * 
 * @author AGH AgE Team
 */
public final class MessageTestUtils {

	/**
	 * Constructs an empty message from a component to another component.
	 * 
	 * @param fromComponent
	 *            a name of the sending component.
	 * @param toComponent
	 *            a name of the receiving component.
	 * @return An empty message.
	 */
	public static IMessage<IComponentAddress, Serializable> createEmptyComponentMessage(String fromComponent,
	        String toComponent) {
		return createEmptyComponentMessage(fromComponent, toComponent, mock(INodeAddress.class));
	}

	/**
	 * Constructs an empty message from a component to another component in the given node.
	 * 
	 * @param fromComponent
	 *            a name of the sending component.
	 * @param toComponent
	 *            a name of the receiving component.
	 * @param toNodeAddress
	 *            an address of the target node.
	 * @return An empty message.
	 */
	public static IMessage<IComponentAddress, Serializable> createEmptyComponentMessage(String fromComponent,
	        String toComponent, INodeAddress toNodeAddress) {
		return createComponentMessageWithPayload(fromComponent, toComponent, toNodeAddress, (Serializable)"payload");
	}

	/**
	 * Constructs an empty message from a component to another component in the given node.
	 * 
	 * @param fromComponent
	 *            a name of the sending component.
	 * @param toComponent
	 *            a name of the receiving component.
	 * @param toNodeAddress
	 *            an address of the target node.
	 * @param payload
	 *            a payload to put into the message.
	 * @return a message with the given payload.
	 */
	public static <T extends Serializable> IMessage<IComponentAddress, T> createComponentMessageWithPayload(
	        String fromComponent, String toComponent, INodeAddress toNodeAddress, T payload) {
		INodeAddress sourceNodeAddress = mock(INodeAddress.class);
		IComponentAddress sourceComponentAddress = new ComponentAddress(fromComponent, sourceNodeAddress);

		INodeAddress targetNodeAddress = toNodeAddress;
		IComponentAddress targetComponentAddress = new ComponentAddress(toComponent, targetNodeAddress);

		IHeader<IComponentAddress> header = new Header<IComponentAddress>(sourceComponentAddress,
		        new UnicastSelector<IComponentAddress>(targetComponentAddress));
		return new Message<IComponentAddress, T>(header, payload);
	}
}
