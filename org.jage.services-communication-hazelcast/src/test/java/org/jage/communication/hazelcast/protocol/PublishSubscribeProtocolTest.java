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
 * File: PublishSubscribeProtocolTest.java
 * Created: 2012-02-07
 * Author: faber
 * $Id: PublishSubscribeProtocolTest.java 134 2012-03-18 20:17:01Z faber $
 */

package org.jage.communication.hazelcast.protocol;

import java.io.Serializable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import org.jage.address.node.IComponentAddress;
import org.jage.address.node.NodeAddress;
import org.jage.address.node.provider.IAgENodeAddressProvider;
import org.jage.communication.common.protocol.IMessageReceivedListener;
import org.jage.communication.message.IMessage;
import org.jage.communication.message.MessageTestUtils;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;

/**
 * Tests for the {@link PublishSubscribeProtocol} class.
 * 
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class PublishSubscribeProtocolTest {

	@Mock
	private IAgENodeAddressProvider addressProvider;
	
	@Mock
	private HazelcastInstance hazelcastInstance;

	@InjectMocks
	private PublishSubscribeProtocol pubsubProtocol = new PublishSubscribeProtocol(null);
	
	@Mock
	private ITopic<IMessage<IComponentAddress, Serializable>> topic;

	@Mock
	private com.hazelcast.core.Message<IMessage<IComponentAddress, Serializable>> hazelcastMessage;

	@Test
	public void testMessageListenerInteraction() {
		// given
		NodeAddress address = new NodeAddress("test1", "hostname");
		IMessageReceivedListener listener = mock(IMessageReceivedListener.class);
		IMessage<IComponentAddress, Serializable> message = MessageTestUtils.createEmptyComponentMessage("from", "to",
		        address);

		given(hazelcastInstance.<IMessage<IComponentAddress, Serializable>> getTopic(anyString())).willReturn(topic);
		given(hazelcastMessage.getMessageObject()).willReturn(message);
		given(addressProvider.getNodeAddress()).willReturn(address);

		// when
		pubsubProtocol.addMessageReceivedListener(listener);
		pubsubProtocol.init();
		pubsubProtocol.onMessage(hazelcastMessage);

		// then
		verify(topic, only()).addMessageListener(pubsubProtocol);
		verify(listener, only()).onMessageReceived(message);
	}
	
	// XXX: Methods below are to be moved to integration tests.
	
//	public void testMessageListenerInteraction_() {
//		// given
//		System.setProperty("hazelcast.wait.seconds.before.join", "0");
//		System.setProperty("hazelcast.initial.wait.seconds", "0");
//		NodeAddress address = new NodeAddress("test1", "hostname");
//		IMessageReceivedListener listener = mock(IMessageReceivedListener.class);
//		IMessage<IComponentAddress, Serializable> message = MessageTestUtils.createEmptyComponentMessage("from", "to",
//		        address);
//
//		given(hazelcastMessage.getMessageObject()).willReturn(message);
//		given(addressProvider.getNodeAddress()).willReturn(address);
//
//		// when
//		pubsubProtocol.addMessageReceivedListener(listener);
//		pubsubProtocol.init();
//		pubsubProtocol.onMessage(hazelcastMessage);
//
//		// then
//		verify(listener, only()).onMessageReceived(message);
//	}
//
//	public void teardown_() {
//		Hazelcast.shutdownAll();
//	}
}
