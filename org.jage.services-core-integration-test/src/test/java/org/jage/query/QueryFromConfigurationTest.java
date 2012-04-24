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
 * File: QueryFromConfigurationTest.java
 * Created: 2011-09-19
 * Author: faber
 * $Id: QueryFromConfigurationTest.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.query;

import java.awt.Color;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.jage.query.ValueFilters.allOf;
import static org.jage.query.ValueFilters.lessOrEqual;
import static org.jage.query.ValueFilters.lessThan;
import static org.jage.query.ValueFilters.moreOrEqual;
import static org.jage.query.ValueFilters.moreThan;
import static org.jage.query.ValueFilters.pattern;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jage.address.AgentAddress;
import org.jage.address.IAgentAddress;
import org.jage.address.node.INodeAddress;
import org.jage.address.provider.IAgentAddressProvider;
import org.jage.agent.testHelpers.HelperSimpleWorkplace;
import org.jage.platform.component.provider.IComponentInstanceProvider;
import org.jage.property.IPropertiesSet;
import org.jage.workplace.IWorkplaceEnvironment;
import org.jage.workplace.SimpleWorkplace;

/**
 * Simple tests for querying agents in aggregate.
 * 
 * @author AGH AgE Team
 */
@SuppressWarnings("unchecked")
public class QueryFromConfigurationTest {

	private SimpleWorkplace workplace;

	private PlainAggregate aggregate;

	private PlainAgent agent1;

	private PlainAgent agent2;

	private PlainAgent agent3;

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
		workplace = new HelperSimpleWorkplace();
		IWorkplaceEnvironment workplaceEnvironment = mock(IWorkplaceEnvironment.class);
		// when(workplaceEnvironment.registerAgentInPath(Mockito.any(IAgentAddress.class),
		// Mockito.anyList())).thenReturn(true);
		workplace.setWorkplaceEnvironment(workplaceEnvironment);

		aggregate = new PlainAggregate();
		aggregate.setInstanceProvider(componentInstanceProvider);
		aggregate.init();
		aggregate.setAgentEnvironment(workplace);

		// agent 1
		agent1 = new PlainAgent();
		agent1.setInstanceProvider(componentInstanceProvider);
		agent1.setWidth(1.0f);
		agent1.setHeight(4.5f);
		agent1.setDescription("my new description");
		agent1.setWeight(55.0);
		agent1.setColor(Color.RED);
		agent1.setDynamicFactor((short)1000);
		agent1.setStaticFactor(500);
		agent1.setMode((byte)30);
		agent1.setUpperLimit(new BigInteger("9999999999999"));
		agent1.init();

		aggregate.add(agent1);

		// agent 2
		agent2 = new PlainAgent();
		agent2.setInstanceProvider(componentInstanceProvider);
		agent2.setWidth(1.5f);
		agent2.setHeight(3.2f);
		agent2.setDescription("hello world");
		agent2.setWeight(95.0);
		agent2.setColor(Color.BLACK);
		agent2.setDynamicFactor((short)5000);
		agent2.setStaticFactor(50);
		agent2.setMode((byte)12);
		agent2.setUpperLimit(new BigInteger("100"));
		agent2.init();

		aggregate.add(agent2);

		// agent 3
		agent3 = new PlainAgent();
		agent3.setInstanceProvider(componentInstanceProvider);
		agent3.setWidth(2.45f);
		agent3.setHeight(3.9f);
		agent3.setDescription("simple description");
		agent3.setWeight(150.0);
		agent3.setColor(Color.BLUE);
		agent3.setDynamicFactor((short)50);
		agent3.setStaticFactor(5000);
		agent3.setMode((byte)1);
		agent3.setUpperLimit(new BigInteger("300000000"));
		agent3.init();

		aggregate.add(agent3);

		List<IAgentAddress> addresses = new LinkedList<IAgentAddress>();
		addresses.add(agent1.getAddress());
		addresses.add(agent2.getAddress());
		addresses.add(agent3.getAddress());
	}

	/**
	 * Tests an execution of the empty query.
	 */
	@Test
	public void testEmptyQuery() {
		AgentEnvironmentQuery<PlainAgent, PlainAgent> generalPurposeQuery = new AgentEnvironmentQuery<PlainAgent, PlainAgent>();
		Collection<PlainAgent> result = generalPurposeQuery.execute(aggregate);
		Assert.assertEquals(3, result.size());

		Map<IAgentAddress, IPropertiesSet> entries = getPropertyContainerMap(result);

		Assert.assertTrue(entries.containsKey(agent1.getAddress()));
		Assert.assertTrue(entries.containsKey(agent2.getAddress()));
		Assert.assertTrue(entries.containsKey(agent3.getAddress()));

		IPropertiesSet agent1Props = entries.get(agent1.getAddress());
		Assert.assertNotNull(agent1Props);

		Assert.assertEquals(agent1Props.getProperty("width").getValue(), new Float(agent1.getWidth()));

		Assert.assertEquals(agent1Props.getProperty("height").getValue(), new Float(agent1.getHeight()));
		Assert.assertEquals(agent1Props.getProperty("description").getValue(), agent1.getDescription());

		IPropertiesSet agent2Props = entries.get(agent2.getAddress());
		Assert.assertNotNull(agent2Props);

		Assert.assertEquals(agent2Props.getProperty("width").getValue(), new Float(agent2.getWidth()));
		Assert.assertEquals(agent2Props.getProperty("height").getValue(), new Float(agent2.getHeight()));
		Assert.assertEquals(agent2Props.getProperty("description").getValue(), agent2.getDescription());

		IPropertiesSet agent3Props = entries.get(agent3.getAddress());
		Assert.assertNotNull(agent3Props);

		Assert.assertEquals(agent3Props.getProperty("width").getValue(), new Float(agent3.getWidth()));
		Assert.assertEquals(agent3Props.getProperty("height").getValue(), new Float(agent3.getHeight()));
		Assert.assertEquals(agent3Props.getProperty("description").getValue(), agent3.getDescription());

	}

	@Test
	public void testRangeConstraint1() {
		AgentEnvironmentQuery<PlainAgent, PlainAgent> generalPurposeQuery = new AgentEnvironmentQuery<PlainAgent, PlainAgent>();
		generalPurposeQuery.matching("height", allOf(moreThan((float)3.0), lessThan((float)4.0)));
		Collection<PlainAgent> result = generalPurposeQuery.execute(aggregate);
		Assert.assertEquals(result.size(), 2);

		Map<IAgentAddress, IPropertiesSet> entries = getPropertyContainerMap(result);
		Assert.assertTrue(entries.containsKey(agent2.getAddress()));
		Assert.assertTrue(entries.containsKey(agent3.getAddress()));
	}

	@Test
	public void testRangeConstraint2() {
		AgentEnvironmentQuery<PlainAgent, PlainAgent> generalPurposeQuery = new AgentEnvironmentQuery<PlainAgent, PlainAgent>();
		generalPurposeQuery.matching("staticFactor", allOf(moreOrEqual(50), lessOrEqual(5000)));
		Collection<PlainAgent> result = generalPurposeQuery.execute(aggregate);
		Assert.assertEquals(result.size(), 3);

		final Map<IAgentAddress, IPropertiesSet> entries = getPropertyContainerMap(result);
		Assert.assertTrue(entries.containsKey(agent1.getAddress()));
		Assert.assertTrue(entries.containsKey(agent2.getAddress()));
		Assert.assertTrue(entries.containsKey(agent3.getAddress()));
	}

	@Test
	public void testRangeConstraint3() {
		AgentEnvironmentQuery<PlainAgent, PlainAgent> generalPurposeQuery = new AgentEnvironmentQuery<PlainAgent, PlainAgent>();
		generalPurposeQuery.matching("staticFactor", allOf(moreOrEqual(50), lessThan(5000)));
		Collection<PlainAgent> result = generalPurposeQuery.execute(aggregate);
		Assert.assertEquals(result.size(), 2);

		final Map<IAgentAddress, IPropertiesSet> entries = getPropertyContainerMap(result);
		Assert.assertTrue(entries.containsKey(agent1.getAddress()));
		Assert.assertTrue(entries.containsKey(agent2.getAddress()));

		AgentEnvironmentQuery<PlainAgent, PlainAgent> queryEdge = new AgentEnvironmentQuery<PlainAgent, PlainAgent>();
		queryEdge.matching("staticFactor", allOf(moreThan(50), lessThan(5000)));
		Collection<PlainAgent> resultEdge = queryEdge.execute(aggregate);
		Assert.assertEquals(resultEdge.size(), 1);

		final Map<IAgentAddress, IPropertiesSet> entriesEdge = getPropertyContainerMap(result);
		Assert.assertTrue(entriesEdge.containsKey(agent1.getAddress()));

		AgentEnvironmentQuery<PlainAgent, PlainAgent> queryEdge2 = new AgentEnvironmentQuery<PlainAgent, PlainAgent>();
		queryEdge2.matching("staticFactor", allOf(moreThan(50), lessThan(500)));
		Collection<PlainAgent> resultEdge2 = queryEdge2.execute(aggregate);
		Assert.assertEquals(resultEdge2.size(), 0);
	}

	@Test
	public void testPatternConstraint() {
		AgentEnvironmentQuery<PlainAgent, PlainAgent> generalPurposeQuery = new AgentEnvironmentQuery<PlainAgent, PlainAgent>();
		generalPurposeQuery.matching("description", pattern(".*new.*"));
		Collection<PlainAgent> result = generalPurposeQuery.execute(aggregate);
		Assert.assertEquals(result.size(), 1);

		Map<IAgentAddress, IPropertiesSet> entries = getPropertyContainerMap(result);
		Assert.assertTrue(entries.containsKey(agent1.getAddress()));

		AgentEnvironmentQuery<PlainAgent, PlainAgent> generalPurposeQuery2 = new AgentEnvironmentQuery<PlainAgent, PlainAgent>();
		generalPurposeQuery2.matching("description", pattern("hello world"));
		Collection<PlainAgent> result2 = generalPurposeQuery2.execute(aggregate);
		Assert.assertEquals(result2.size(), 1);

		Map<IAgentAddress, IPropertiesSet> entries2 = getPropertyContainerMap(result2);
		Assert.assertTrue(entries2.containsKey(agent2.getAddress()));
	}

	@Test
	public void testValueConstraint() {
		AgentEnvironmentQuery<PlainAgent, PlainAgent> generalPurposeQuery = new AgentEnvironmentQuery<PlainAgent, PlainAgent>();
		generalPurposeQuery.matching("upperLimit", ValueFilters.eq(new BigInteger("100")));
		Collection<PlainAgent> result = generalPurposeQuery.execute(aggregate);
		Assert.assertEquals(1, result.size());

		Map<IAgentAddress, IPropertiesSet> entries = getPropertyContainerMap(result);
		Assert.assertTrue(entries.containsKey(agent2.getAddress()));
	}

	private static Map<IAgentAddress, IPropertiesSet> getPropertyContainerMap(Collection<PlainAgent> result) {
		Map<IAgentAddress, IPropertiesSet> entries = new HashMap<IAgentAddress, IPropertiesSet>();
		for (PlainAgent entry : result) {
			entries.put(entry.getAddress(), entry.getProperties());
		}
		return entries;
	}
}
