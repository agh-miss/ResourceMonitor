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
 * File: PublishSubscribeProtocol.java
 * Created: 2012-02-07
 * Author: faber
 * $Id: PublishSubscribeProtocol.java 139 2012-03-19 22:54:18Z faber $
 */

package org.jage.communication.hazelcast.protocol;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.address.node.IComponentAddress;
import org.jage.communication.common.protocol.IMessageReceivedListener;
import org.jage.communication.message.IMessage;
import org.jage.communication.message.Messages;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.MessageListener;

/**
 * A Hazelcast based communication protocol implementing a topic based publish / subscribe model.
 * <p>
 * There is one only one topic for messages, common to all the PublishSubscribeStrategy components in the environment.
 * All those components are listening to incoming messages at the same time but they are handling only those messages
 * which were destined for them. In other words, every protocol is constantly filtering the common channel to get only
 * the messages it's interested in.
 * <p>
 * The common topic is a Hazelcast's distributed topic.
 * 
 * @author AGH AgE Team
 */
public class PublishSubscribeProtocol extends HazelcastCommonProtocol implements
        MessageListener<IMessage<IComponentAddress, Serializable>> {

	private static final Logger log = LoggerFactory.getLogger(PublishSubscribeProtocol.class);

	private ITopic<IMessage<IComponentAddress, Serializable>> commonUnicast;

	private static final String CHANNEL = "jd-business";

	/**
	 * Creates a new Hazelcast-based pub-sub protocol.
	 */
	public PublishSubscribeProtocol() {
		this(null);
	}

	/**
	 * Package-visible constructor for mocking.
	 * 
	 * @param hazelcastInstance
	 *            a Hazelcast instance to use.
	 */
	PublishSubscribeProtocol(HazelcastInstance hazelcastInstance) {
		super(hazelcastInstance);
	}

	/**
	 * Connects this protocol to the common unicast topic.
	 * <p>
	 * Also, gets the local AgE address from the provider.
	 */
	@Override
	public void init() {
		log.info("Initializing the pub-sub protocol.");
		super.init();

		commonUnicast = hazelcastInstance.getTopic(CHANNEL);
		commonUnicast.addMessageListener(this);

		log.info("Done with initialization of the pub-sub protocol.");
	}

	@Override
	public boolean finish() {
		log.info("Finalizing the pub-sub protocol.");

		// TODO: who destroys the channel?
		// commonUnicast.destroy();

		super.finish();

		log.info("Done with finalization of the pub-sub protocol.");

		return true;
	}

	@Override
	public void send(IMessage<IComponentAddress, Serializable> msg) {
		commonUnicast.publish(msg);
	}

	/**
	 * Callback for arrival of unicast messages sent to the business channel.
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public void onMessage(com.hazelcast.core.Message<IMessage<IComponentAddress, Serializable>> hazelcastMessage) {
		IMessage<IComponentAddress, Serializable> message = hazelcastMessage.getMessageObject();
		// check if the message was destined for this node
		if (Messages.getOnlyReceiverAddress(message).getNodeAddress().equals(localAddress)) {
			log.info("Got a message on the business channel from {}.", message.getHeader().getSenderAddress());

			for (IMessageReceivedListener listener : listeners) {
				listener.onMessageReceived(message);
			}
		}
	}

}
