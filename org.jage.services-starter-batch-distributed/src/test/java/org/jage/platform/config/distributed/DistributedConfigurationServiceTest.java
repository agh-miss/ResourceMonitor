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
 * File: DistributedConfigurationServiceTest.java
 * Created: 2012-03-15
 * Author: faber
 * $Id: DistributedConfigurationServiceTest.java 131 2012-03-18 18:53:46Z faber $
 */

package org.jage.platform.config.distributed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import org.jage.address.node.IComponentAddress;
import org.jage.address.node.INodeAddress;
import org.jage.address.node.NodeAddress;
import org.jage.address.node.provider.IAgENodeAddressProvider;
import org.jage.address.selector.BroadcastSelector;
import org.jage.communication.ICommunicationService;
import org.jage.communication.message.IMessage;
import org.jage.platform.component.definition.IComponentDefinition;
import org.jage.platform.component.exception.ComponentException;
import org.jage.platform.component.provider.IComponentInstanceProvider;
import org.jage.platform.config.ConfigurationChangeListener;
import org.jage.services.core.ICoreComponent;

import static org.jage.communication.message.MessageTestUtils.createComponentMessageWithPayload;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Tests for the {@link DistributedConfigurationService} class.
 * 
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class DistributedConfigurationServiceTest {

	@Mock
	private ICommunicationService communicationService;

	@Mock
	private IAgENodeAddressProvider addressProvider;

	@Mock
	private IComponentInstanceProvider instanceProvider;

	@Mock
	private ICoreComponent coreComponent;

	@Mock
	private ConfigurationChangeListener configurationChangeListener;

	@InjectMocks
	private DistributedConfigurationService configurationService;

	private final INodeAddress address = new NodeAddress("test1", "hostname");

	@SuppressWarnings("unchecked")
	@Test
	public void testDistributeConfigurationSend() throws ComponentException {
		// given
		final Collection<IComponentDefinition> componentDefinitions = newArrayList(mock(IComponentDefinition.class));
		final ArgumentCaptor<IMessage> messageCaptor = ArgumentCaptor.forClass(IMessage.class);

		given(addressProvider.getNodeAddress()).willReturn(address);

		// when
		configurationService.distributeConfiguration(componentDefinitions);

		// then
		verify(communicationService).send(messageCaptor.capture());

		final IMessage<IComponentAddress, Serializable> capturedMessage = messageCaptor.getValue();

		assertThat(capturedMessage.getHeader().getSenderAddress().getNodeAddress(), is(equalTo(address)));
		assertThat(capturedMessage.getHeader().getSenderAddress().getComponentName(), is(notNullValue()));
		assertThat(capturedMessage.getHeader().getReceiverSelector(), is(instanceOf(BroadcastSelector.class)));

		assertThat(capturedMessage.getPayload(), is(instanceOf(Collection.class)));
		assertThat((Collection<IComponentDefinition>)capturedMessage.getPayload(), is(equalTo(componentDefinitions)));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testDistributeConfigurationLocal() throws ComponentException {
		// given
		final Collection<IComponentDefinition> componentDefinitions = newArrayList(mock(IComponentDefinition.class));
		final ArgumentCaptor<Collection> definitionsCaptor = ArgumentCaptor.forClass(Collection.class);

		given(addressProvider.getNodeAddress()).willReturn(address);
		given(instanceProvider.getInstances(ConfigurationChangeListener.class)).willReturn(
		        newArrayList(configurationChangeListener));

		// when
		configurationService.distributeConfiguration(componentDefinitions);

		// then
		verify(configurationChangeListener).computationConfigurationUpdated(definitionsCaptor.capture());
		verify(coreComponent).start();

		final Collection<IComponentDefinition> capturedCollection = definitionsCaptor.getValue();

		assertThat(capturedCollection, is(equalTo(componentDefinitions)));
	}

	@SuppressWarnings("unchecked")
    @Test
	public void testReceiveConfiguration() throws ComponentException, InterruptedException {
		// given
		final ArrayList<IComponentDefinition> componentDefinitions = newArrayList(mock(IComponentDefinition.class));
		final IMessage<IComponentAddress, ? extends Serializable> message = createComponentMessageWithPayload("test", "test", address, componentDefinitions);
		final ArgumentCaptor<ArrayList> definitionsCaptor = ArgumentCaptor.forClass(ArrayList.class);
		
		given(addressProvider.getNodeAddress()).willReturn(address);
		given(communicationService.receive(anyString())).willReturn((IMessage<IComponentAddress, Serializable>)message);
		given(instanceProvider.getInstances(ConfigurationChangeListener.class)).willReturn(
		        newArrayList(configurationChangeListener));
		
		// when
		configurationService.receiveConfiguration();
		
		// then
		verify(configurationChangeListener, timeout(2000)).computationConfigurationUpdated(definitionsCaptor.capture());
		verify(coreComponent).start();

		final Collection<IComponentDefinition> capturedCollection = definitionsCaptor.getValue();

		assertThat(capturedCollection, is(equalTo((Collection<IComponentDefinition>)componentDefinitions)));
	}
}
