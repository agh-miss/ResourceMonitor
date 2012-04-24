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
 * File: CommunicatingCoreComponent.java
 * Created: 2012-02-09
 * Author: faber
 * $Id: CommunicatingCoreComponent.java 134 2012-03-18 20:17:01Z faber $
 */

package org.jage.examples.distributed;

import java.io.Serializable;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.address.node.ComponentAddress;
import org.jage.address.node.IComponentAddress;
import org.jage.address.node.provider.IAgENodeAddressProvider;
import org.jage.address.selector.BroadcastSelector;
import org.jage.communication.ICommunicationService;
import org.jage.communication.message.Header;
import org.jage.communication.message.IHeader;
import org.jage.communication.message.IMessage;
import org.jage.communication.message.Message;
import org.jage.platform.component.annotation.Inject;
import org.jage.platform.component.annotation.Require;
import org.jage.platform.component.exception.ComponentException;
import org.jage.services.core.ICoreComponent;
import org.jage.services.core.ICoreComponentListener;

import com.google.common.collect.Sets;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This is an example component that uses the communication service to send and receive messages. It simply tries to
 * send 10 broadcast messages and checks its delivery queue the same number of times.
 *
 * @author AGH AgE Team
 */
public class CommunicatingCoreComponent implements ICoreComponent {

	private static final Logger log = LoggerFactory.getLogger(CommunicatingCoreComponent.class);

	private Set<ICoreComponentListener> listeners = Sets.newHashSet();

	private IComponentAddress myAddress;

	private static final String NAME = "communicatingCoreComponent";

	@Inject
	private ICommunicationService communicationService;

	@Inject
	private IAgENodeAddressProvider addressProvider;

	@Override
	public void init() throws ComponentException {
		myAddress = new ComponentAddress(NAME, addressProvider.getNodeAddress());
	}

	@Override
	public boolean finish() throws ComponentException {
		// Empty
		return false;
	}

	@Override
	public void start() throws ComponentException {
		for (ICoreComponentListener listener : listeners) {
			listener.notifyStarting(this);
		}

		for (int i = 0; i < 10; i++) {
			IHeader<IComponentAddress> header = new Header<IComponentAddress>(myAddress,
			        new BroadcastSelector<IComponentAddress>());
			IMessage<IComponentAddress, Serializable> message = new Message<IComponentAddress, Serializable>(header,
			        "Hello world!");

			log.info("Sending message: {}.", message);
			communicationService.send(message);

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				log.error("Interrupted.", e);
			}

			IMessage<IComponentAddress, Serializable> receivedMessage = communicationService.receive(NAME);
			log.info("Received message: {}.", receivedMessage);
		}

		for (ICoreComponentListener listener : listeners) {
			listener.notifyStopped(this);
		}
	}

	@Override
	public void stop() {
		// Empty
	}

	@Override
	public void registerListener(ICoreComponentListener listener) {
		checkNotNull(listener);
		listeners.add(listener);
	}

	@Override
	public void unregisterListener(ICoreComponentListener listener) {
		checkNotNull(listener);
		listeners.remove(listener);
	}

}
