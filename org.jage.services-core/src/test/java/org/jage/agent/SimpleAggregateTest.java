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
 * File: SimpleAggregateTest.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: SimpleAggregateTest.java 213 2012-04-08 20:21:35Z krzywick $
 */

package org.jage.agent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.jage.action.Action;
import org.jage.action.SingleAction;
import org.jage.action.context.GetAgentActionContext;
import org.jage.action.context.IActionWithAgentReferenceContext;
import org.jage.action.context.RemoveAgentActionContext;
import org.jage.action.context.SendMessageActionContext;
import org.jage.action.testHelpers.HelperTestAgent;
import org.jage.address.AgentAddress;
import org.jage.address.IAddress;
import org.jage.address.IAgentAddress;
import org.jage.address.node.INodeAddress;
import org.jage.address.selector.UnicastSelector;
import org.jage.communication.message.IMessage;

/**
 * Tests the {@link SimpleAggregate} class.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class SimpleAggregateTest {

	private static final Logger _log = LoggerFactory.getLogger(SimpleAggregateTest.class);

	SimpleAggregate aggregate;

	@Mock
	ISimpleAgentEnvironment agentEnv;

	List<IAddress> path;

	@Before
	@SuppressWarnings("unchecked")
	public void setUp() throws Exception {
		aggregate = new SimpleAggregate();

		// Mockito.when(agentEnv.registerAgentInPath(Matchers.any(IAgentAddress.class), Matchers.anyList())).thenReturn(
		// false);

		aggregate.setAgentEnvironment(agentEnv);
		path = new ArrayList<IAddress>();
		path.add(aggregate.getAddress());
	}

	/**
	 * Tests an adding of an agent to the aggregate.
	 */
	@Test
	public void testAdd() {
		_log.error("testAdd");
		IAgent agent = createTestAgent();
		IAgentAddress address = agent.getAddress();
		assertEquals(0, aggregate.size());
		assertTrue(aggregate.add(agent));
		assertEquals(1, aggregate.size());
		// Mockito.verify(agentEnv).registerAgentInPath(address, path);
	}

	@Test
	public void testAddExisting() {
		_log.error("testAddExisting");
		IAgent agent = createTestAgent();
		assertEquals(0, aggregate.size());
		assertTrue(aggregate.add(agent));
		assertEquals(1, aggregate.size());
		// should not add another time
		assertFalse(aggregate.add(agent));
		assertEquals(1, aggregate.size());
	}

	@Test
	public void testAddAll() throws Exception {
		_log.error("testAddAll");
		List<IAgent> agents = new ArrayList<IAgent>();
		ISimpleAgent testAgent1 = createTestAgentWithAddress("agent1");
		ISimpleAgent testAgent2 = createTestAgentWithAddress("agent2");
		agents.add(testAgent1);
		agents.add(testAgent2);
		// when
		// Mockito.when(agentEnv.registerAgentInPath(testAgent1.getAddress(), path)).thenReturn(true);
		// Mockito.when(agentEnv.registerAgentInPath(testAgent2.getAddress(), path)).thenReturn(true);

		assertTrue(aggregate.addAll(agents));
		assertEquals(2, aggregate.size());
		List<IAddress> addressPath = new ArrayList<IAddress>();
		addressPath.add(aggregate.getAddress());
		// Mockito.verify(agentEnv).registerAgentInPath(testAgent1.getAddress(), addressPath);
		// Mockito.verify(agentEnv).registerAgentInPath(testAgent2.getAddress(), addressPath);
		Mockito.verifyNoMoreInteractions(agentEnv);
	}

	@Test
	public void testClear() {
		_log.error("testClear");
		aggregate.add(createTestAgent());
		assertEquals(1, aggregate.size());
		aggregate.clear();
		assertEquals(0, aggregate.size());
	}

	@Test
	public void testContains() {
		// fail("Not yet implemented");
	}

	@Test
	public void testContainsAll() {
		// fail("Not yet implemented");
	}

	@Test
	public void testIsEmpty() {
		_log.error("testIsEmpty");
		assertTrue(aggregate.isEmpty());
		aggregate.add(createTestAgent());
		assertFalse(aggregate.isEmpty());
	}

	@Test
	public void testIterator() {
		// fail("Not yet implemented");
	}

	@Test
	public void testRemove() {
		_log.error("testRemove");
		ISimpleAgent simpleAgent = createTestAgent();
		aggregate.add(simpleAgent);
		assertEquals(1, aggregate.size());
		aggregate.remove(simpleAgent);
		assertTrue(aggregate.isEmpty());
	}

	@Test
	public void testRemoveAll() throws Exception {
		List<IAgent> agents = new ArrayList<IAgent>();
		ISimpleAgent testAgent1 = createTestAgentWithAddress("agent1");
		ISimpleAgent testAgent2 = createTestAgentWithAddress("agent2");
		agents.add(testAgent1);
		agents.add(testAgent2);
		// when
		// Mockito.when(agentEnv.registerAgentInPath(testAgent1.getAddress(), path)).thenReturn(true);
		// Mockito.when(agentEnv.registerAgentInPath(testAgent2.getAddress(), path)).thenReturn(true);
		aggregate.addAll(agents);
		// then
		assertEquals(2, aggregate.size());
		aggregate.removeAll(aggregate.getAgents());
		assertEquals(0, aggregate.getAgents().size());
	}

	@Test
	public void testRetainAll() {
		// fail("Not yet implemented");
	}

	@Test
	public void testToArray() {
		// fail("Not yet implemented");
	}

	@Test
	public void testToArrayTArray() {
		// fail("Not yet implemented");
	}

	@Test
	public void testDoActionAction() {
		// fail("Not yet implemented");
	}

	@Test
	public void testDoDie() {
		// fail("Not yet implemented");
	}

	@Test
	public void testDoClone() {
		// fail("Not yet implemented");
	}

	@Test
	public void testSendMessageMessageIAgentAddress() {
		// fail("Not yet implemented");
	}

	@Test
	public void testSendMessageMessage() {
		// fail("Not yet implemented");
	}

	@Test
	public void testThatAggregateConformsToDefaultActionPriorites() throws AgentException {
		// Note this test can be written much better when the aggregate will be more open for testing.

		// given
		SingleAction action1 = new SingleAction(mock(UnicastSelector.class), new GetAgentActionContext(mock(IActionWithAgentReferenceContext.class)));
		SingleAction action2 = new SingleAction(mock(UnicastSelector.class), new SendMessageActionContext(mock(IMessage.class)));
		SingleAction action3 = new SingleAction(mock(UnicastSelector.class), new RemoveAgentActionContext(mock(IAgentAddress.class)));

		SimpleAggregate aggregateSpy = spy(aggregate);
		ArgumentCaptor<Action> argument = ArgumentCaptor.forClass(Action.class);

		// add them in reverse order
		aggregateSpy.doAction(action3, null);
		aggregateSpy.doAction(action2, null);
		aggregateSpy.doAction(action1, null);

		willDoNothing().given(aggregateSpy).processAction(any(Action.class));

		// when
		aggregateSpy.processActions();

		// then
		verify(aggregateSpy, times(3)).processAction(argument.capture());

		assertThat(argument.getAllValues().get(0), is(equalTo((Action)action1)));
		assertThat(argument.getAllValues().get(1), is(equalTo((Action)action2)));
		assertThat(argument.getAllValues().get(2), is(equalTo((Action)action3)));
	}

	static ISimpleAgent createTestAgentWithAddress(final String name) {
		return new HelperTestAgent(new AgentAddress(UUID.randomUUID(), mock(INodeAddress.class), name));
	}

	static ISimpleAgent createTestAgent() {
		return new HelperTestAgent();
	}

}
