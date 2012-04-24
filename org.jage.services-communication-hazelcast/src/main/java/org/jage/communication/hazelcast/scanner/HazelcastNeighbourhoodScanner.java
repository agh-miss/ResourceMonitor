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
 * File: HazelcastNeighbourhoodScanner.java
 * Created: 2012-02-07
 * Author: faber
 * $Id: HazelcastNeighbourhoodScanner.java 139 2012-03-19 22:54:18Z faber $
 */

package org.jage.communication.hazelcast.scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.address.node.INodeAddress;
import org.jage.address.node.provider.IAgENodeAddressProvider;
import org.jage.communication.common.cache.IAddressSet;
import org.jage.communication.common.scanner.INeighbourhoodScanner;
import org.jage.platform.component.annotation.Inject;
import org.jage.platform.component.annotation.Require;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.MessageListener;

/**
 * A Hazelcast based neighbourhood scanner for ICommunicationManager.
 * <p>
 * The scanner uses the so called control channel, common for all the nodes. It posts the address of its node every few
 * seconds so that all nodes in the network know it's out there.
 * <p>
 * The control channel is a Hazelcast's distributed topic.
 *
 * @author AGH AgE Team
 */
public class HazelcastNeighbourhoodScanner implements MessageListener<ControlChannelMessage>, INeighbourhoodScanner {

	private static final Logger log = LoggerFactory.getLogger(HazelcastNeighbourhoodScanner.class);

	@Inject
	private IAgENodeAddressProvider addressProvider;

	@Inject
	private IAddressSet cache;

	private INodeAddress localAddress;

	private ITopic<ControlChannelMessage> control;

	private HazelcastInstance hazelcastInstance;

	private volatile boolean finished = false;

	private static final String CONTROL_CHANNEL = "jd-control";

	private static final int DEFAULT_MESSAGE_DELAY = 3000;

	private static final int DEFAULT_OFFERS_BEFORE_REQUEST = 4;

	private int messageDelay;

	private int offersBeforeRequest;

	private Thread scannerThread;

	/**
	 * Default constructor - creates the scanner with default configuration values.
	 */
	public HazelcastNeighbourhoodScanner() {
		this(DEFAULT_MESSAGE_DELAY, DEFAULT_OFFERS_BEFORE_REQUEST);
	}

	/**
	 * Constructs a new scanner instance.
	 *
	 * @param messageDelay
	 *            specifies how long (in ms) should scanner wait before sending a next message.
	 * @param offersBeforeRequest
	 *            specifies a number of offers sent before sending a request.
	 */
	public HazelcastNeighbourhoodScanner(int messageDelay, int offersBeforeRequest) {
		this(messageDelay, offersBeforeRequest, null);
	}

	/**
	 * Package-visible constructor for mocking.
	 *
	 * @param messageDelay
	 *            specifies how long (in ms) should scanner wait before sending a next message.
	 * @param offersBeforeRequest
	 *            specifies a number of offers sent before sending a request.
	 * @param hazelcastInstance
	 *            a Hazelcast instance to use.
	 */
	HazelcastNeighbourhoodScanner(int messageDelay, int offersBeforeRequest, HazelcastInstance hazelcastInstance) {
		super();
		this.messageDelay = messageDelay;
		this.offersBeforeRequest = offersBeforeRequest;
		if (hazelcastInstance == null) {
			System.setProperty("hazelcast.logging.type", "slf4j");
			this.hazelcastInstance = Hazelcast.newHazelcastInstance(null);
		} else {
			this.hazelcastInstance = hazelcastInstance;
		}
	}

	/**
	 * Connects this scanner to the, so called, control topic.
	 * <p>
	 * Also, starts the main thread of the scanner and gets the local AgE address and an IAddressSet instance from the
	 * provider.
	 */
	@Override
	public void init() {
		log.info("Initializing the HazelcastNeighbourhoodScanner.");

		// get the local AgE address
		localAddress = addressProvider.getNodeAddress();

		// join the control channel
		control = hazelcastInstance.getTopic(CONTROL_CHANNEL);
		control.addMessageListener(this);

		// start the control protocol implementing thread
		scannerThread = new Thread(this);
		scannerThread.start();
		log.info("Done with initialization of the HazelcastNeighbourhoodScanner.");
	}

	@Override
	public boolean finish() {
		log.info("Finalizing the HazelcastNeighbourhoodScanner.");

		finished = true;
		try {
			scannerThread.join(offersBeforeRequest * messageDelay);
		} catch (InterruptedException e) {
			log.error("Interrupted while waiting for thread.", e);
		}
		// TODO: who destroys the control channel?
		// control.destroy();

		if (hazelcastInstance.getLifecycleService().isRunning()) {
			hazelcastInstance.getLifecycleService().shutdown();
		}

		log.info("Done with finalization of the HazelcastNeighbourhoodScanner.");

		return true;
	}

	/**
	 * A loop implementing the control protocol.
	 */
	@Override
	public void run() {
		while (!finished) {
			// a REQUEST for addresses of other nodes (important especially for
			// a new node which has just connected to the control channel)
			log.trace("Sending REQUEST for addresses.");
			control.publish(ControlChannelMessage.createRequest(localAddress));
			try {
				Thread.sleep(messageDelay);
			} catch (InterruptedException ex) {
				log.warn("Interrupted", ex);
			}

			// now we republish our address periodically a few times before
			// we refresh our cache completely using REQUEST
			for (int i = 0; i < offersBeforeRequest; i++) {
				log.trace("Sending an address OFFER.");
				control.publish(ControlChannelMessage.createOffer(localAddress));

				try {
					Thread.sleep(messageDelay);
				} catch (InterruptedException ex) {
					log.warn("Interrupted", ex);
				}
			}
		}
	}

	@Override
	public void onMessage(com.hazelcast.core.Message<ControlChannelMessage> hazelcastMessage) {
		ControlChannelMessage message = hazelcastMessage.getMessageObject();
		log.trace("Message received: {}.", message);
		// ignore your own messages
		if (message.getSender().equals(localAddress)) {
			log.trace("Ignoring my own message.");
			return;
		}

		// it's an offering - let's refresh the cache
		if (message.getType() == ControlChannelMessage.Type.OFFER) {
			log.trace("Got an address OFFER from {}.", message.getSender());

			cache.addAddress(message.getSender());

			// it's a request for addresses so we publish our address on the
			// control channel
		} else {
			log.trace("Got a REQUEST for addresses from {}.", message.getSender());

			control.publish(ControlChannelMessage.createOffer(localAddress));
		}
	}

}
