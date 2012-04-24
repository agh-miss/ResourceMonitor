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
 * File: PollingCommunicationManagerTest.java
 * Created: 2012-02-07
 * Author: faber
 * $Id: PollingCommunicationManagerTest.java 134 2012-03-18 20:17:01Z faber $
 */

package org.jage.communication.common.manager;

import java.io.Serializable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.jage.address.node.IComponentAddress;
import org.jage.address.node.INodeAddress;
import org.jage.address.node.provider.IAgENodeAddressProvider;
import org.jage.communication.common.cache.IAddressSet;
import org.jage.communication.common.protocol.ICommunicationProtocol;
import org.jage.communication.message.IMessage;
import org.jage.communication.message.MessageTestUtils;
import org.jage.communication.message.Messages;
import org.jage.platform.component.provider.IComponentInstanceProvider;

/**
 * Tests for the {@link PollingCommunicationManager} class.
 * 
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class PollingCommunicationManagerTest {

	@SuppressWarnings("unused")
	@Mock
	private IComponentInstanceProvider instanceProvider;

	@SuppressWarnings("unused")
	@Mock
	private IAddressSet addressSet;

	@Mock
	private IAgENodeAddressProvider addressProvider;

	@Mock
	private ICommunicationProtocol communicationProtocol;

	@InjectMocks
	private PollingCommunicationManager communicationManager;

	@SuppressWarnings("unchecked")
	@Test
	public void testSend() throws InterruptedException {
		// given
		given(addressProvider.getNodeAddress()).willReturn(mock(INodeAddress.class));
		communicationManager.init();

		IMessage<IComponentAddress, Serializable> message = MessageTestUtils.createEmptyComponentMessage("from", "to");

		// when
		communicationManager.send(message);

		// then
		Thread.sleep(1000);
		verify(communicationProtocol).addMessageReceivedListener(communicationManager);
		verify(communicationProtocol).send(any(IMessage.class));
	}

	@Test
	public void testReceive() throws InterruptedException {
		// given
		given(addressProvider.getNodeAddress()).willReturn(mock(INodeAddress.class));
		communicationManager.init();

		IMessage<IComponentAddress, Serializable> message = MessageTestUtils.createEmptyComponentMessage("from", "to");
		IComponentAddress receiverAddress = Messages.getOnlyReceiverAddress(message);

		// when
		communicationManager.onMessageReceived(message);

		// then
		Thread.sleep(3000);
		IMessage<IComponentAddress, Serializable> received = communicationManager.receive("to");
		assertThat(received, is(notNullValue()));
		assertThat(received.getPayload(), equalTo(message.getPayload()));
		assertThat(received.getHeader().getReceiverSelector().selected(receiverAddress), is(true));
	}

}
