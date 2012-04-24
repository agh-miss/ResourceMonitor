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
 * File: ActionValidationTest.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: ActionValidationTest.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.action;

import java.util.UUID;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import org.jage.action.context.PassToParentActionContext;
import org.jage.action.testHelpers.ActionTestException;
import org.jage.action.testHelpers.DoNotPassToParentTestActionContext;
import org.jage.action.testHelpers.HelperTestAgent;
import org.jage.action.testHelpers.HelperTestAggregate;
import org.jage.action.testHelpers.PassToParentTestActionContext;
import org.jage.address.AgentAddress;
import org.jage.address.IAgentAddress;
import org.jage.address.node.INodeAddress;
import org.jage.address.provider.IAgentAddressProvider;
import org.jage.address.selector.UnicastSelector;
import org.jage.address.selector.agent.ParentAgentAddressSelector;
import org.jage.agent.AgentException;
import org.jage.agent.IAgentEnvironment;
import org.jage.agent.ISimpleAgentEnvironment;
import org.jage.platform.component.provider.IComponentInstanceProvider;

/**
 * Test for validating actions.
 * 
 * These test use the following agent structure: - aggregate (defined in AbstractActionTest) - agent (defined in
 * AbstractActionTest) - aggregate2 - agent2 and agent2 tries to invoke an action on aggregate2, agent and aggregate.
 * 
 * @author AGH AgE Team
 */
public class ActionValidationTest extends AbstractActionTest {

	HelperTestAggregate aggregate2;

	HelperTestAgent agent2;

	private IComponentInstanceProvider componentInstanceProvider;

	private IAgentAddressProvider agentAddressProvider;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		aggregate2 = new HelperTestAggregate();
		aggregate.add(aggregate2);
		agent2 = new HelperTestAgent();
		aggregate2.add(agent2);
		aggregate2.setInstanceProvider(new NullInstanceProvider());
	}

	// FIXME
	@Ignore
	@Test
	public void testNoSuchAgent() throws Exception {
		DoNotPassToParentTestActionContext context = new DoNotPassToParentTestActionContext();
		agent.runAction(new SingleAction(new UnicastSelector<IAgentAddress>(new AgentAddress(UUID.randomUUID(),
		        mock(INodeAddress.class), "aggr")), context));
		try {
			aggregate.step();
			fail();
		} catch (ActionTestException e) {
			assertTrue(e.getCause() instanceof AgentException);
			assertTrue(e.getCause().getMessage().contains("doesn't exist in: aggr"));
		}
	}

	@Test
	public void testInvokeOnSelf() throws Exception {
		DoNotPassToParentTestActionContext context = new DoNotPassToParentTestActionContext();
		agent2.runAction(new SingleAction(new UnicastSelector<IAgentAddress>(agent2.getAddress()), context));
		aggregate.step();
		assertEquals(agent2, context.actionTarget);
	}

	@Test
	public void testInvokeOnOwnAggregate() throws Exception {
		DoNotPassToParentTestActionContext context = new DoNotPassToParentTestActionContext();
		agent2.runAction(new SingleAction(new ParentAgentAddressSelector(agent2.getAddress()), context));
		try {
			aggregate.step();
			assertEquals(aggregate2, context.actionTarget);

		} catch (ActionTestException e) {
			fail();
		}
	}

	/**
	 * This action fails because there is an attempt to invoke action on agent in parent aggregate, but action is not
	 * wrapped in {@link PassToParentActionContext}. Correct usage is shown in
	 * {@link #testPassToParentInvokeOnAgentInParent()}
	 * 
	 * @throws Exception
	 */
	// FIXME
	@Ignore
	@Test
	public void testInvokeOnAgentInParent() throws Exception {
		DoNotPassToParentTestActionContext context = new DoNotPassToParentTestActionContext();
		agent2.runAction(new SingleAction(new UnicastSelector<IAgentAddress>(agent.getAddress()), context));
		try {
			aggregate.step();
			fail();
		} catch (ActionTestException e) {
			assertNull(context.actionTarget);
			assertTrue(e.getCause() instanceof AgentException);
			assertTrue(e.getCause().getMessage().contains("doesn't exist in: aggr"));
		}
	}

	/**
	 * This action fails because there is an attempt to invoke action on parent aggregate, but action is not wrapped in
	 * {@link PassToParentActionContext} . Correct usage is shown in {@link #testPassToParentInvokeOnParentAggregate()}
	 * 
	 * @throws Exception
	 */
	// FIXME
	@Ignore
	@Test
	public void testInvokeOnParentAggregate() throws Exception {
		DoNotPassToParentTestActionContext context = new DoNotPassToParentTestActionContext();
		agent2.runAction(new SingleAction(new UnicastSelector<IAgentAddress>(aggregate.getAddress()), context));
		try {
			aggregate.step();
			fail();
		} catch (ActionTestException e) {
			assertNull(context.actionTarget);
			assertTrue(e.getCause() instanceof AgentException);
			assertTrue(e.getCause().getMessage().contains("doesn't exist in: aggr"));
		}
	}

	@Test
	public void testPassToParentInvokeOnSelf() throws Exception {
		PassToParentTestActionContext context = new PassToParentTestActionContext();
		agent2.runAction(new SingleAction(new UnicastSelector<IAgentAddress>(agent2.getAddress()), context));
		aggregate.step();
		assertEquals(agent2, context.actionTarget);
	}

	@Test
	public void testPassToParentInvokeOnOwnAggregate() throws Exception {
		PassToParentTestActionContext context = new PassToParentTestActionContext();
		agent2.runAction(new SingleAction(new ParentAgentAddressSelector(agent2.getAddress()), context));
		aggregate.step();
		assertEquals(aggregate2, context.actionTarget);
	}

	@Test
	public void testPassToParentInvokeOnAgentInParent() throws Exception {
		PassToParentTestActionContext context = new PassToParentTestActionContext();
		Action action = new SingleAction(new UnicastSelector<IAgentAddress>(agent.getAddress()), context);
		PassToParentActionContext outerContext = new PassToParentActionContext(agent2.getAddress(), action);
		agent2.runAction(new SingleAction(new ParentAgentAddressSelector(agent2.getAddress()), outerContext));
		aggregate.step();
		assertEquals(agent, context.actionTarget);
	}

	// XXX: This test does not really have to be correct - are targets really ok?
	@Test
	public void testPassToParentInvokeOnParentAggregate() throws Exception {
		PassToParentTestActionContext context = new PassToParentTestActionContext();
		SingleAction action = new SingleAction(new ParentAgentAddressSelector(aggregate2.getAddress()), context);

		PassToParentActionContext outerContext = new PassToParentActionContext(agent2.getAddress(), action);
		agent2.runAction(new SingleAction(new ParentAgentAddressSelector(agent2.getAddress()), outerContext));
		aggregate.setAgentEnvironment(null);

		try {
			aggregate.step();
			assertEquals(aggregate, context.actionTarget);
		} catch (ActionTestException e) {
			fail();
		}
	}

	/**
	 * This action fails because there is an attempt to perform action on parent aggregate, but the aggregate has no
	 * agent environment.
	 */
	@Test
	public void testPassToParentInvokeOnUnavailableParentAggregate() throws Exception {
		aggregate.setAgentEnvironment(null);
		PassToParentTestActionContext context = new PassToParentTestActionContext();
		SingleAction action = new SingleAction(new ParentAgentAddressSelector(agent.getAddress()), context);

		PassToParentActionContext outerContext = new PassToParentActionContext(agent.getAddress(), action);
		agent.runAction(new SingleAction(new ParentAgentAddressSelector(agent.getAddress()), outerContext));

		try {
			aggregate.step();
			fail();
		} catch (ActionTestException e) {
			assertNull(context.actionTarget);
			assertTrue(e.getCause() instanceof AgentException);
			assertTrue(e.getCause().getCause().getMessage().contains("it is not instance of ISimpleAgentEnvironment"));
		}
	}

	/**
	 * This action fails because there is an attempt to perform action on parent aggregate, but the aggregate
	 * environment which is not {@link ISimpleAgentEnvironment}.
	 */
	@Test
	public void testPassToParentInvokeOnWrongTypeParentAggregate() throws Exception {
		aggregate.setAgentEnvironment(null);
		aggregate.setAgentEnvironment(mock(IAgentEnvironment.class));
		PassToParentTestActionContext context = new PassToParentTestActionContext();
		SingleAction action = new SingleAction(new ParentAgentAddressSelector(agent.getAddress()), context);

		PassToParentActionContext outerContext = new PassToParentActionContext(agent.getAddress(), action);
		agent.runAction(new SingleAction(new ParentAgentAddressSelector(agent.getAddress()), outerContext));

		try {
			aggregate.step();
			fail();
		} catch (ActionTestException e) {
			assertNull(context.actionTarget);
			assertTrue(e.getCause() instanceof AgentException);
			assertTrue(e.getCause().getCause().getMessage().contains("it is not instance of ISimpleAgentEnvironment"));
		}
	}
}
