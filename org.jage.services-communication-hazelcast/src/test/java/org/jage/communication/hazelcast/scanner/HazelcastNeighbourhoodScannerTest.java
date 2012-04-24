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
 * File: HazelcastNeighbourhoodScannerTest.java
 * Created: 2012-02-07
 * Author: faber
 * $Id: HazelcastNeighbourhoodScannerTest.java 134 2012-03-18 20:17:01Z faber $
 */

package org.jage.communication.hazelcast.scanner;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import org.jage.address.node.INodeAddress;
import org.jage.address.node.NodeAddress;
import org.jage.address.node.provider.IAgENodeAddressProvider;
import org.jage.communication.common.cache.IAddressSet;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.LifecycleService;

/**
 * Tests for the {@link HazelcastNeighbourhoodScanner} class.
 * 
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class HazelcastNeighbourhoodScannerTest {

	private static final String CONTROL_CHANNEL = "jd-control";

	@Mock
	private IAddressSet addressSet;

	@Mock
	private IAgENodeAddressProvider addressProvider;

	@Mock
	private HazelcastInstance hazelcastInstance;

	@Mock
	private LifecycleService lifecycleService;

	@InjectMocks
	private HazelcastNeighbourhoodScanner scanner = new HazelcastNeighbourhoodScanner(50, 1, null);

	@Mock
	private ITopic<ControlChannelMessage> topic;

	@Mock
	private com.hazelcast.core.Message<ControlChannelMessage> hazelcastMessage;

	@Test
	public void testSendingRequestAndOffers() throws InterruptedException {
		// given
		final INodeAddress address1 = new NodeAddress("test1", "hostname");
		final ArgumentCaptor<ControlChannelMessage> captor = ArgumentCaptor.forClass(ControlChannelMessage.class);

		given(hazelcastInstance.<ControlChannelMessage> getTopic(anyString())).willReturn(topic);
		given(hazelcastInstance.getLifecycleService()).willReturn(lifecycleService);
		given(lifecycleService.isRunning()).willReturn(true);
		given(addressProvider.getNodeAddress()).willReturn(address1);

		// when
		scanner.init();

		// then
		Thread.sleep(100);
		scanner.finish();

		verify(topic).addMessageListener(scanner);

		verify(topic, atLeast(2)).publish(captor.capture());

		final List<ControlChannelMessage> capturedValues = captor.getAllValues();
		
		assertThat(capturedValues.size(), greaterThanOrEqualTo(2));

		assertThat(capturedValues.get(0).getType(), equalTo(ControlChannelMessage.Type.REQUEST));
		assertThat(capturedValues.get(0).getSender(), equalTo(address1));

		assertThat(capturedValues.get(1).getType(), equalTo(ControlChannelMessage.Type.OFFER));
		assertThat(capturedValues.get(1).getSender(), equalTo(address1));
	}

	@Test
	public void testReactionToOffer() {
		// given
		final NodeAddress address1 = new NodeAddress("test1", "hostname");
		final NodeAddress address2 = new NodeAddress("test2", "hostname");

		final ControlChannelMessage message = ControlChannelMessage.createOffer(address2);

		given(hazelcastInstance.<ControlChannelMessage> getTopic(anyString())).willReturn(topic);
		given(hazelcastInstance.getLifecycleService()).willReturn(lifecycleService);
		given(lifecycleService.isRunning()).willReturn(true);
		given(hazelcastMessage.getMessageObject()).willReturn(message);
		given(addressProvider.getNodeAddress()).willReturn(address1);

		// when
		scanner.init();
		scanner.onMessage(hazelcastMessage);

		// then
		scanner.finish();
		verify(topic).addMessageListener(scanner);
		verify(addressSet, only()).addAddress(address2);
	}

	// XXX: Methods below are to be moved to integration tests.

	// // Warning: this test takes a lot of time!
	// @Test(timeout = 10000)
	// public void testSendingRequestAndOffers() throws InterruptedException {
	//
	// // given
	// System.setProperty("hazelcast.wait.seconds.before.join", "0");
	// System.setProperty("hazelcast.initial.wait.seconds", "0");
	// final INodeAddress address1 = new NodeAddress("test1", "hostname");
	// final Semaphore semaphore = new Semaphore(0);
	//
	// final ControlChannelMessage[] messages = new ControlChannelMessage[2];
	//
	// MessageListener<ControlChannelMessage> listener = new MessageListener<ControlChannelMessage>() {
	// private boolean inRequest = true;
	//
	// @Override
	// public void onMessage(com.hazelcast.core.Message<ControlChannelMessage> hazelcastMessage) {
	// ControlChannelMessage message = hazelcastMessage.getMessageObject();
	// if (inRequest) {
	// messages[0] = message;
	// inRequest = false;
	// } else {
	// messages[1] = message;
	// semaphore.release();
	// }
	// }
	// };
	// ITopic<ControlChannelMessage> topic = Hazelcast.getTopic(CONTROL_CHANNEL);
	// topic.addMessageListener(listener);
	//
	// given(addressProvider.getNodeAddress()).willReturn(address1);
	//
	// // when
	// scanner.init();
	//
	// // then
	// semaphore.acquire();
	// scanner.finish();
	//
	// assertThat(messages[0].getType(), equalTo(ControlChannelMessage.Type.REQUEST));
	// assertThat(messages[0].getSender(), equalTo(address1));
	//
	// assertThat(messages[1].getType(), equalTo(ControlChannelMessage.Type.OFFER));
	// assertThat(messages[1].getSender(), equalTo(address1));
	// }
	//
	// @Test(timeout = 10000)
	// public void testReactionToOffer() {
	//
	// // given
	// System.setProperty("hazelcast.wait.seconds.before.join", "0");
	// System.setProperty("hazelcast.initial.wait.seconds", "0");
	// final NodeAddress address1 = new NodeAddress("test1", "hostname");
	// final NodeAddress address2 = new NodeAddress("test2", "hostname");
	//
	// ControlChannelMessage message = ControlChannelMessage.createOffer(address2);
	//
	// given(hazelcastMessage.getMessageObject()).willReturn(message);
	// given(addressProvider.getNodeAddress()).willReturn(address1);
	//
	// // when
	// scanner.init();
	// scanner.onMessage(hazelcastMessage);
	//
	// // then
	// scanner.finish();
	// verify(addressSet, only()).addAddress(address2);
	// }
	//
	// @After
	// public void teardown() {
	// Hazelcast.shutdownAll();
	// }
}
