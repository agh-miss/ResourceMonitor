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
package org.jage.examples.multiworkplace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.address.IAgentAddress;
import org.jage.address.selector.BroadcastSelector;
import org.jage.communication.message.Header;
import org.jage.communication.message.IHeader;
import org.jage.communication.message.IMessage;
import org.jage.communication.message.Message;
import org.jage.workplace.ConnectedSimpleWorkplace;

/**
 * This is an example workplace that communicates with other workspaces in its environment.
 * 
 * @author AGH AgE Team
 */
public class ExampleCommunicatingWorkplace extends ConnectedSimpleWorkplace {

	private static final long serialVersionUID = 9146385144435844359L;

	private final Logger log = LoggerFactory.getLogger(ExampleCommunicatingWorkplace.class);

	/**
	 * Performs step of all agents and processes events.
	 */
	@Override
	public void step() {
		IMessage<IAgentAddress, String> message = null;
		while (null != (message = receiveMessage())) {
			log.info("{} received message from {}.", getAddress(), message.getHeader().getSenderAddress());
		}

		if (getStep() % 10 == 0) {
			log.info("{} is sending a message.", getAddress());
			IHeader<IAgentAddress> header = new Header<IAgentAddress>(getAddress(), new BroadcastSelector<IAgentAddress>());
			IMessage<IAgentAddress, String> messageToSend = new Message<IAgentAddress, String>(header, "Message");
			getWorkplaceEnvironment().sendMessage(messageToSend);
		}

		super.step();

	}

}
