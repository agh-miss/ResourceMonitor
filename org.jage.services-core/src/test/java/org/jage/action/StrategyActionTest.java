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
 * File: StrategyActionTest.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: StrategyActionTest.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.action;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jage.action.testHelpers.ActionTestException;
import org.jage.action.testHelpers.BadStrategyTestActionContext;
import org.jage.action.testHelpers.HelperTestActionStrategy;
import org.jage.action.testHelpers.HelperTestAgent;
import org.jage.action.testHelpers.HelperTestAggregate;
import org.jage.action.testHelpers.HelperTestBadStrategy;
import org.jage.action.testHelpers.MixedActionContext;
import org.jage.action.testHelpers.MixedActionStrategy;
import org.jage.action.testHelpers.NonExistentStrategyTestActionContext;
import org.jage.action.testHelpers.SimpleTestActionContext;
import org.jage.action.testHelpers.StrategyTestActionContext;
import org.jage.address.IAgentAddress;
import org.jage.address.selector.UnicastSelector;
import org.jage.agent.AgentException;
import org.jage.platform.component.provider.IComponentInstanceProvider;

/**
 * Test for actions implemented as strategies.
 * 
 * @author AGH AgE Team
 */
public class StrategyActionTest extends AbstractActionTest {

	IComponentInstanceProvider instanceProvider;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();

		instanceProvider = mock(IComponentInstanceProvider.class);
		aggregate.setInstanceProvider(instanceProvider);

	}

	@Test
	public void testBasicStrategyAction() throws Exception {

		when(
		// once for each action class
		        instanceProvider.getInstance(StrategyTestActionContext.ACTION_NAME)).thenReturn(
		        new HelperTestActionStrategy());

		StrategyTestActionContext context = new StrategyTestActionContext();
		agent.runAction(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context));
		aggregate.step();

		verify(instanceProvider, times(3)).getInstance(StrategyTestActionContext.ACTION_NAME);
		assertTrue(context.actionRun);

	}

	@Test
	public void testStrategyAndMethodNotFound() throws Exception {

		when(instanceProvider.getInstance(NonExistentStrategyTestActionContext.ACTION_NAME)).thenReturn(null);

		NonExistentStrategyTestActionContext context = new NonExistentStrategyTestActionContext();
		agent.runAction(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context));
		try {
			aggregate.step();
			fail();
		} catch (ActionTestException e) {
			verify(instanceProvider).getInstance(NonExistentStrategyTestActionContext.ACTION_NAME);
			assertTrue(e.getCause() instanceof AgentException);
			assertFalse(context.actionRun);

		}
	}

	@Test
	public void testNotAnActionStrategy() throws Exception {
		when(instanceProvider.getInstance(BadStrategyTestActionContext.ACTION_NAME)).thenReturn(
		        new HelperTestBadStrategy());

		BadStrategyTestActionContext context = new BadStrategyTestActionContext();
		agent.runAction(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context));
		try {
			aggregate.step();
			fail();
		} catch (ActionTestException e) {
			verify(instanceProvider).getInstance(BadStrategyTestActionContext.ACTION_NAME);
			assertTrue(e.getCause() instanceof AgentException);
			assertFalse(context.actionRun);
		}
	}

	@Test
	public void testFallbackToAggregateMethod() throws Exception {
		when(instanceProvider.getInstance("simpleTestAction")).thenReturn(null);

		SimpleTestActionContext context = new SimpleTestActionContext();
		agent.runAction(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context));
		aggregate.step();
		assertTrue(context.actionRun);
		verify(instanceProvider, times(3)).getInstance("simpleTestAction");
	}

	/**
	 * Test a complex action consisting of two simple actions, using the same context. One of the actions is strategic,
	 * the other is executed in the aggregate.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMixedComplexAction() throws Exception {

		when(instanceProvider.getInstance(MixedActionContext.STRATEGIC_ACTION_ID))
		        .thenReturn(new MixedActionStrategy());
		when(instanceProvider.getInstance(MixedActionContext.AGGREGATE_ACTION_ID)).thenReturn(null);

		MixedActionContext context = new MixedActionContext();

		ComplexAction c = new ComplexAction();
		c.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        MixedActionContext.STRATEGIC_ACTION_ID));
		c.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        MixedActionContext.AGGREGATE_ACTION_ID));

		agent.runAction(c);
		aggregate.step();

		verify(instanceProvider, times(3)).getInstance(MixedActionContext.STRATEGIC_ACTION_ID);
		verify(instanceProvider, times(3)).getInstance(MixedActionContext.AGGREGATE_ACTION_ID);
		assertTrue(context.getExecAggr());
		assertTrue(context.getExecStrat());
	}

	/**
	 * Test a complex action consisting of three simple actions, using the same context. One of the actions is
	 * strategic, the other is executed in the aggregate, and the last one is an unknown action.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUnknownAction() throws Exception {

		when(
		// all actions will perform only initialization phase
		// in this phase unknown action won't be found and
		// AgentException will be thrown

		        instanceProvider.getInstance(MixedActionContext.STRATEGIC_ACTION_ID)).thenReturn(
		        new MixedActionStrategy());
		when(instanceProvider.getInstance(MixedActionContext.AGGREGATE_ACTION_ID)).thenReturn(null);
		when(instanceProvider.getInstance(MixedActionContext.UNKNOWN_ACTION_ID)).thenReturn(null);

		MixedActionContext context = new MixedActionContext();

		ComplexAction c = new ComplexAction();
		c.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        MixedActionContext.STRATEGIC_ACTION_ID));
		c.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        MixedActionContext.AGGREGATE_ACTION_ID));
		c.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        MixedActionContext.UNKNOWN_ACTION_ID));

		agent.runAction(c);
		try {
			aggregate.step();
			fail();
		} catch (ActionTestException e) {
			assertTrue(e.getCause() instanceof AgentException);
			assertTrue(context.getExecAggr());
			assertTrue(context.getExecStrat());
			assertFalse(context.getExecUnknown());
		}

		verify(instanceProvider).getInstance(MixedActionContext.STRATEGIC_ACTION_ID);
		verify(instanceProvider).getInstance(MixedActionContext.AGGREGATE_ACTION_ID);
		verify(instanceProvider).getInstance(MixedActionContext.UNKNOWN_ACTION_ID);
	}

	/**
	 * Test a complex action consisting of three simple actions, using the same context. One of the actions is
	 * strategic, the other is executed in the aggregate, and the last one is unknown. Use an aggregate with overridden
	 * unknown action handling.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUnknownActionWithCustomHandling() throws Exception {

		when(
		// all actions will perform in all phases because default handling of unknown action
		// does not throw any exception so it does not stop actions processing

		        instanceProvider.getInstance(MixedActionContext.STRATEGIC_ACTION_ID)).thenReturn(
		        new MixedActionStrategy());
		when(instanceProvider.getInstance(MixedActionContext.AGGREGATE_ACTION_ID)).thenReturn(null);
		when(instanceProvider.getInstance(MixedActionContext.UNKNOWN_ACTION_ID)).thenReturn(null);

		MixedActionContext context = new MixedActionContext();

		ComplexAction c = new ComplexAction();
		c.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        MixedActionContext.STRATEGIC_ACTION_ID));
		c.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        MixedActionContext.AGGREGATE_ACTION_ID));
		c.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        MixedActionContext.UNKNOWN_ACTION_ID));

		agent = new HelperTestAgent();
		aggregate = new HelperTestAggregate() {
			private static final long serialVersionUID = -8365302797062164776L;

			@Override
			protected void handleUnknownAction(String actionName, IActionContext context) throws AgentException {
				assertTrue(context instanceof MixedActionContext);
				assertEquals(MixedActionContext.UNKNOWN_ACTION_ID, actionName);
				((MixedActionContext)context).setExecUnknown(true);
			}
		};

		aggregate.setAgentEnvironment(agentEnv);
		aggregate.setInstanceProvider(instanceProvider);

		aggregate.add(agent);

		agent.runAction(c);
		aggregate.step();

		assertTrue(context.getExecAggr());
		assertTrue(context.getExecStrat());
		assertTrue(context.getExecUnknown());

		verify(instanceProvider, times(3)).getInstance(MixedActionContext.STRATEGIC_ACTION_ID);
		verify(instanceProvider, times(3)).getInstance(MixedActionContext.AGGREGATE_ACTION_ID);
		verify(instanceProvider, times(3)).getInstance(MixedActionContext.UNKNOWN_ACTION_ID);
	}
}
