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
 * File: SimpleAggregateAddressTest.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: SimpleAggregateAddressTest.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.agent;

import java.util.Arrays;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jage.address.AgentAddress;
import org.jage.address.IAgentAddress;
import org.jage.address.node.INodeAddress;
import org.jage.address.provider.IAgentAddressProvider;
import org.jage.agent.testHelpers.HelperSimpleAgent;
import org.jage.platform.component.provider.IComponentInstanceProvider;

/**
 * Tests for {@link SimpleAggregate}'s getAddress functionality
 * 
 * @author AGH AgE Team
 */
public class SimpleAggregateAddressTest {

	private static final int AGENTS_COUNT = 10;

	SimpleAggregate underTest;

	IAgent[] agents;

	IAgentAddress[] addresses;

	private IComponentInstanceProvider componentInstanceProvider;

	private IAgentAddressProvider agentAddressProvider;

	@Before
	public void setUp() throws Exception {
		// Configure mocks
		agentAddressProvider = mock(IAgentAddressProvider.class);
		when(agentAddressProvider.obtainAddress(anyString())).thenAnswer(new Answer<AgentAddress>() {
			@Override
			public AgentAddress answer(InvocationOnMock invocation) throws Throwable {
				return new AgentAddress(UUID.randomUUID(), mock(INodeAddress.class), null);
			}
		});
		componentInstanceProvider = mock(IComponentInstanceProvider.class);
		when(componentInstanceProvider.getInstance(IAgentAddressProvider.class)).thenReturn(agentAddressProvider);

		// Configure agents
		underTest = new SimpleAggregate();
		underTest.setInstanceProvider(componentInstanceProvider);
		underTest.init();

		IAgentEnvironment agentEnv = Mockito.mock(IAgentEnvironment.class);
		// Mockito.when(agentEnv.registerAgentInPath(Matchers.any(IAgentAddress.class), Matchers.anyList())).thenReturn(
		// true);

		agents = new SimpleAgent[AGENTS_COUNT];
		addresses = new IAgentAddress[AGENTS_COUNT];
		for (int i = 0; i < AGENTS_COUNT; i++) {
			HelperSimpleAgent agent = new HelperSimpleAgent();
			agent.setInstanceProvider(componentInstanceProvider);
			agent.init();
			agents[i] = agent;
			addresses[i] = agent.getAddress();
		}

		underTest.setAgents(Arrays.asList(agents));
		underTest.setAgentEnvironment(agentEnv);
	}

	@Test
	public void testGetAgentSingleFound() {
		assertEquals(agents[2], underTest.getAgent(addresses[2]));
	}

	@Test
	public void testGetAgentSingleNotFound() {
		assertNull(underTest.getAgent(new AgentAddress(UUID.randomUUID(), mock(INodeAddress.class), null)));
	}

}
