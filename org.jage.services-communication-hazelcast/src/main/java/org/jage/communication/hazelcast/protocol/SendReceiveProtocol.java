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
 * File: SendReceiveProtocol.java
 * Created: 2012-02-07
 * Author: faber
 * $Id: SendReceiveProtocol.java 139 2012-03-19 22:54:18Z faber $
 */

package org.jage.communication.hazelcast.protocol;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.address.node.IComponentAddress;
import org.jage.address.node.INodeAddress;
import org.jage.communication.common.protocol.IMessageReceivedListener;
import org.jage.communication.message.IMessage;
import org.jage.communication.message.Messages;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.MessageListener;

/**
 * A Hazelcast based communication protocol implementing a topic based send / receive model.
 * <p>
 * In other words, every receiver has it's own dedicated unicast topic, identified by a string which is a variation of
 * the receiver's jAgE address. This topic is a Hazelcast's distributed topic.
 * 
 * @author AGH AgE Team
 */
public class SendReceiveProtocol extends HazelcastCommonProtocol implements
        MessageListener<IMessage<IComponentAddress, Serializable>> {

	private static final Logger log = LoggerFactory.getLogger(SendReceiveProtocol.class);

	private ITopic<IMessage<IComponentAddress, Serializable>> myUnicast;

	/**
	 * Creates a new Hazelcast-based send-receive protocol.
	 */
	public SendReceiveProtocol() {
		this(null);
	}

	/**
	 * Package-visible constructor for mocking.
	 * 
	 * @param hazelcastInstance
	 *            a Hazelcast instance to use.
	 */
	SendReceiveProtocol(HazelcastInstance hazelcastInstance) {
		super(hazelcastInstance);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Connects this protocol to it's dedicated unicast topic.
	 */
	@Override
	public void init() {
		log.info("Initializing the send-receive protocol.");
		super.init();

		myUnicast = hazelcastInstance.getTopic(getUnicastChannelFor(localAddress));
		myUnicast.addMessageListener(this);

		log.info("Done with initialization of the send-receive protocol.");
	}

	/**
	 * Destroys the dedicated unicast channel.
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public boolean finish() {
		log.info("Finalizing the send-receive protocol.");

		// destroy the unicast channel
		myUnicast.destroy();

		super.finish();

		log.info("Done with finalization of the send-receive protocol.");

		return true;
	}

	@Override
	public void send(IMessage<IComponentAddress, Serializable> message) {
		String channel = getUnicastChannelFor(Messages.getOnlyReceiverAddress(message).getNodeAddress());
		ITopic<IMessage<IComponentAddress, Serializable>> topic = Hazelcast.getTopic(channel);

		topic.publish(message);
	}

	/**
	 * Callback for arrival of unicast messages sent to the protocol's channel.
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public void onMessage(com.hazelcast.core.Message<IMessage<IComponentAddress, Serializable>> hazelcastMessage) {
		IMessage<IComponentAddress, Serializable> message = hazelcastMessage.getMessageObject();
		log.info("Got a message on my unicast channel from {}.", message.getHeader().getSenderAddress());

		for (IMessageReceivedListener listener : listeners) {
			listener.onMessageReceived(message);
		}
	}

	/**
	 * Generates a channel name from the node address.
	 * 
	 * @param address
	 *            A node address to use.
	 * @return name of the unicast channel for the given node address.
	 */
	public static String getUnicastChannelFor(INodeAddress address) {
		return "jd-" + address;
	}

}
