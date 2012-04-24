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
 * File: SimpleAggregatePerformActionTest.java
 * Created: 2009-04-29
 * Author: kpietak
 * $Id: SimpleAggregatePerformActionTest.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.action;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jage.action.testHelpers.HelperTestAggregate;
import org.jage.action.testHelpers.TracingActionContext;
import org.jage.address.AgentAddress;
import org.jage.address.IAgentAddress;
import org.jage.address.node.INodeAddress;
import org.jage.address.provider.IAgentAddressProvider;
import org.jage.address.selector.BroadUnusedSelector;
import org.jage.address.selector.IAddressSelector;
import org.jage.address.selector.UnicastSelector;
import org.jage.agent.AgentException;
import org.jage.agent.IAgent;
import org.jage.agent.IAgentEnvironment;
import org.jage.agent.SimpleAgent;
import org.jage.agent.SimpleAggregate;
import org.jage.agent.testHelpers.HelperSimpleAgent;
import org.jage.platform.component.provider.IComponentInstanceProvider;

/**
 * Test for {@link SimpleAggregate}'s action mechanism.
 * 
 * @author AGH AgE Team
 * 
 */
public class SimpleAggregatePerformActionTest {

	private static final int AGENT_COUNT = 10;

	private HelperTestAggregate aggregate; // helper test aggregate decorates

	// SimpleAggregate for testing purpose

	private IAgentEnvironment agentEnv;

	private IAgent[] agents;

	private IAgentAddress[] addresses;

	private final List<UnicastSelector<IAgentAddress>> unicasts = new LinkedList<UnicastSelector<IAgentAddress>>();

	private final BroadUnusedSelector<IAgentAddress> broadcast = new BroadUnusedSelector<IAgentAddress>();

	private Action action;

	private TracingActionContext context;

	private IComponentInstanceProvider componentInstanceProvider;

	private IAgentAddressProvider agentAddressProvider;

	@Before
	@SuppressWarnings("unchecked")
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
		aggregate = new HelperTestAggregate();
		aggregate.setInstanceProvider(componentInstanceProvider);
		aggregate.init();

		agentEnv = Mockito.mock(IAgentEnvironment.class);
		// Mockito.when(agentEnv.registerAgentInPath(Matchers.any(IAgentAddress.class), Matchers.anyList())).thenReturn(
		// true);

		agents = new SimpleAgent[AGENT_COUNT];
		addresses = new IAgentAddress[AGENT_COUNT];

		for (int i = 0; i < 10; i++) {
			HelperSimpleAgent agent = new HelperSimpleAgent();
			agent.setInstanceProvider(componentInstanceProvider);
			agent.init();
			agents[i] = agent;
			addresses[i] = agent.getAddress();
		}

		aggregate.setAgents(Arrays.asList(agents));
		aggregate.setAgentEnvironment(agentEnv);

		createAction();
	}

	public void createAction() {
		unicasts.add(new UnicastSelector<IAgentAddress>(addresses[0]));
		unicasts.add(new UnicastSelector<IAgentAddress>(addresses[1]));
		unicasts.add(new UnicastSelector<IAgentAddress>(addresses[2]));

		context = new TracingActionContext();
		SingleAction sa1 = new SingleAction(unicasts.get(0), context, "c1Action");
		SingleAction sa2 = new SingleAction(unicasts.get(1), context, "c2Action");
		SingleAction sa3 = new SingleAction(unicasts.get(2), context, "c3Action");
		SingleAction sa4 = new SingleAction(broadcast, context, "c4Action");

		ComplexAction ca = new ComplexAction();
		ca.addChild(sa1);
		ca.addChild(sa2);
		ca.addChild(sa3);
		ca.addChild(sa4);

		action = ca;
	}

	@Test
	public void processActionValidationTest() {
		try {
			Collection<IAgentAddress> used = aggregate.processActionInitialization(action);
			assertNotNull(used);

			assertEquals(unicasts.size(), used.size());

			Collection<IAgentAddress> usedCopy = new LinkedList<IAgentAddress>(used);

			for (IAddressSelector<IAgentAddress> selector : unicasts) {
				for (IAgentAddress address : selector.addresses()) {
					assertTrue(usedCopy.remove(address));
				}
			}
		} catch (AgentException e) {
			assertTrue(false);
		}
	}

	@Test
	public void processActionAddressGenerationTest() {
		try {
			Collection<IAgentAddress> used = aggregate.processActionInitialization(action);
			assertNotNull(used);

			List<IAgentAddress> all = Arrays.asList(addresses);
			aggregate.processActionAddressGeneration(action, all, used);

			for (IAgentAddress address : all) {
				assertTrue(used.contains(address) != broadcast.selected(address));
			}
		} catch (AgentException e) {
			assertTrue(false);
		}
	}

	@Test
	public void processActionPerformTest() {
		try {
			Collection<IAgentAddress> used = aggregate.processActionInitialization(action);
			assertNotNull(used);
			List<IAgentAddress> all = Arrays.asList(addresses);
			aggregate.processActionAddressGeneration(action, all, used);

			aggregate.processActionPerform(action);
			assertEquals("1234444444", context.trace);
		} catch (AgentException e) {
			assertTrue(false);
		}
	}

}
