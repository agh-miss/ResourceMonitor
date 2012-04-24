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
 * File: PollingCommunicationManager.java
 * Created: 2012-02-07
 * Author: faber
 * $Id: PollingCommunicationManager.java 139 2012-03-19 22:54:18Z faber $
 */

package org.jage.communication.common.manager;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.address.node.ComponentAddress;
import org.jage.address.node.IComponentAddress;
import org.jage.address.node.INodeAddress;
import org.jage.address.node.provider.IAgENodeAddressProvider;
import org.jage.address.selector.AbstractBroadcastSelector;
import org.jage.address.selector.IAddressSelector;
import org.jage.address.selector.UnicastSelector;
import org.jage.communication.IIncomingMessageListener;
import org.jage.communication.common.cache.IAddressCache;
import org.jage.communication.common.protocol.ICommunicationProtocol;
import org.jage.communication.common.scanner.INeighbourhoodScanner;
import org.jage.communication.message.Header;
import org.jage.communication.message.IHeader;
import org.jage.communication.message.IMessage;
import org.jage.communication.message.Message;
import org.jage.communication.message.Messages;
import org.jage.platform.component.annotation.Inject;
import org.jage.platform.component.provider.IComponentInstanceProvider;
import org.jage.platform.component.provider.IComponentInstanceProviderAware;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * A polling implementation of ICommunicationManager.
 * <p>
 * After a PollingCommunicationManager is initialized, it will every few seconds send all the messages from the
 * "sending queue" to other nodes and also deliver all the messages from the "delivering queue" to their corresponding
 * receivers on the local node.
 * <p>
 * This means that PollingCommunicationManager does all its work asynchronously and in a polling manner thus every
 * requested operation might be slightly delayed.
 *
 * @author AGH AgE Team
 */
public class PollingCommunicationManager implements ICommunicationManager, IComponentInstanceProviderAware {

	private static final Logger log = LoggerFactory.getLogger(PollingCommunicationManager.class);

	private INodeAddress localAddress;

	private IComponentInstanceProvider instanceProvider;

	@Inject
	private IAgENodeAddressProvider addressProvider;

	@Inject
	private ICommunicationProtocol communicationProtocol;

	@Inject
	private IAddressCache addressCache;

	@SuppressWarnings("unused")
    @Inject
	private INeighbourhoodScanner neighbourhoodScanner;

	// the "sending queue"
	private final List<IMessage<IComponentAddress, Serializable>> toSend = Collections
	        .synchronizedList(new LinkedList<IMessage<IComponentAddress, Serializable>>());

	// the "delivering queue"
	private final List<IMessage<IComponentAddress, Serializable>> toDeliver = Collections
	        .synchronizedList(new LinkedList<IMessage<IComponentAddress, Serializable>>());

	// the queue for messages destined for asynchronous clients (those who do
	// not implement the IIncomingMessageListener interface)
	private final Multimap<String, IMessage<IComponentAddress, Serializable>> received = Multimaps
	        .synchronizedMultimap(HashMultimap.<String, IMessage<IComponentAddress, Serializable>> create());

	private volatile boolean finished = false;

	/**
	 * Registers the communication service as an IMessageReceivedListener with the ICommunicationStrategy from jAgE's
	 * context.
	 * <p>
	 * Also, starts the queue handling thread and gets the local jAgE address from the instance provider.
	 */
	@Override
	public void init() {
		log.info("Initializing the PollingCommunicationManager...");

		// listen for the protocol's "message-received" events
		communicationProtocol.addMessageReceivedListener(this);

		// get the local jAgE address
		localAddress = addressProvider.getNodeAddress();

		new Thread(this).start();

		log.info("Done with initialization of the PollingCommunicationManager...");
	}

	@Override
	public boolean finish() {
		log.info("Finalizing the PollingCommunicationManager.");
		log.info("Done with finalization of the PollingCommunicationManager.");

		return (finished = true);
	}

	@Override
	public void run() {
		while (!finished) {
			sendOutgoingMessages();
			deliverIncomingMessages();

			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	/**
	 * Sends all the queued messages.
	 */
	private void sendOutgoingMessages() {
		synchronized (toSend) {
			Iterator<IMessage<IComponentAddress, Serializable>> iter = toSend.iterator();
			while (iter.hasNext()) {
				IMessage<IComponentAddress, Serializable> msg = iter.next();
				try {
					sendOutgoingMessage(msg);
					iter.remove();
				} catch (Exception ex) {
					// log the error; we'll try to send the message later
					ex.printStackTrace();
					log.error("Could not send the message.", ex);
				}
			}
		}
	}

	/**
	 * Sends the given message.
	 * <p>
	 *
	 * Note, that in the current implementation a broadcast message is translated to many unicast messages (one for
	 * every node, as we do not have the ability to confirm the existence of the receiver component).
	 */
	private void sendOutgoingMessage(IMessage<IComponentAddress, Serializable> msg) {
		IHeader<IComponentAddress> header = msg.getHeader();
		IAddressSelector<IComponentAddress> selector = header.getReceiverSelector();
		boolean isBroadcast = selector instanceof AbstractBroadcastSelector<?>;
		String senderComponentName = header.getSenderAddress().getComponentName();

		checkArgument(!isBroadcast || (isBroadcast && senderComponentName != null),
		        "Unknown sender's componentId. We need it here since it's also the receiver's id in broadcast.");

		selector.initialize(addressCache.getAddressesOfRemoteNodes(), null);

		// For broadcast selectors, there will be more than one message in the result list.
		// XXX: Why not just use the natural broadcast capabilities of some protocols? [LF]
		List<IMessage<IComponentAddress, Serializable>> messages = Lists.newArrayList();
		for (IComponentAddress current : selector.addresses()) {
			// if it's broadcast we use the sender's id as the receiver's id
			// we should have a prettier way to detect broadcast here...
			String clientId = isBroadcast ? senderComponentName : current.getComponentName();

			// prepare final message for this receiver
			IComponentAddress corrected = new ComponentAddress(clientId, current.getNodeAddress());
			IMessage<IComponentAddress, Serializable> newMessage = new Message<IComponentAddress, Serializable>(
			        new Header<IComponentAddress>(header.getSenderAddress(), new UnicastSelector<IComponentAddress>(
			                corrected)), msg.getPayload());

			messages.add(newMessage);
		}

		// we need at least one receiver
		if (messages.isEmpty()) {
			log.debug("no receiver(s) available - won't send the message");
		}

		// sending the message to all receivers, one by one
		for (IMessage<IComponentAddress, Serializable> current : messages) {
			communicationProtocol.send(current);
		}
	}

	/**
	 * Delivers all queued messages.
	 */
	private void deliverIncomingMessages() {
		synchronized (toDeliver) {
			Iterator<IMessage<IComponentAddress, Serializable>> iter = toDeliver.iterator();
			while (iter.hasNext()) {
				IMessage<IComponentAddress, Serializable> msg = iter.next();
				try {
					deliverIncomingMessage(msg);
					iter.remove();
				} catch (Exception ex) {
					// log the error; we'll try to deliver the message later
					log.error("Could not deliver the message to the local component.", ex);
				}
			}
		}
	}

	/**
	 * Delivers the given message.
	 */
	private void deliverIncomingMessage(IMessage<IComponentAddress, Serializable> message) {

		String nameOfClient = Messages.getOnlyReceiverAddress(message).getComponentName();

		Object client = instanceProvider.getInstance(nameOfClient);

		if (client == null) {
			log.debug("No such client component: {}. I will remember his message anyway.", nameOfClient);
		}

		// Every ICommunicationService deals with delivering of messages in two ways:
		// - if the message was sent to a component which implements the
		// IIncomingMessageListener interface, we have a "push" model;
		// we deliver the message now and forget about it
		// - otherwise, we have a "pull" model; we cache the message - the client component can query us for its
		// messages later
		if (client instanceof IIncomingMessageListener) {
			((IIncomingMessageListener)client).deliver(message);
		} else {
			received.put(nameOfClient, message);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void send(IMessage<? extends IComponentAddress, ? extends Serializable> message) {
		toSend.add((IMessage<IComponentAddress, Serializable>)message);
	}

	@Override
	public IMessage<IComponentAddress, Serializable> receive(String clientId) {
		Collection<IMessage<IComponentAddress, Serializable>> list = received.get(clientId);

		synchronized (received) {
			Iterator<IMessage<IComponentAddress, Serializable>> iter = list.iterator();
			if (iter.hasNext()) {
				IMessage<IComponentAddress, Serializable> result = iter.next();
				iter.remove();

				return result;
			}
			return null;
		}
	}

	/**
	 * Adds a message that an ICommunicationStrategy has just received to the "delivering queue".
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public void onMessageReceived(IMessage<IComponentAddress, Serializable> message) {
		toDeliver.add(message);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation of {@link ICommunicationManager} requires the provider to deliver messages to its synchronous
	 * (i.e. implementing the {@link IIncomingMessageListener} interface) clients.
	 */
	@Override
	public void setInstanceProvider(IComponentInstanceProvider instanceProvider) {
		this.instanceProvider = instanceProvider;
	}

	@Override
	public void barrier(String key) throws InterruptedException {
		communicationProtocol.barrier(key, addressCache.getNodesCount() + 1);
	}

}
