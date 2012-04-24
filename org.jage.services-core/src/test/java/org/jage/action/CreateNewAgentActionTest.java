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
 * File: CreateNewAgentActionTest.java
 * Created: 2009-05-18
 * Author: kpietak
 * $Id: CreateNewAgentActionTest.java 213 2012-04-08 20:21:35Z krzywick $
 */

package org.jage.action;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.jage.action.context.AddAgentActionContext;
import org.jage.action.testHelpers.HelperTestAgent;
import org.jage.action.testHelpers.TracingActionContext;
import org.jage.address.IAgentAddress;
import org.jage.address.selector.UnicastSelector;
import org.jage.agent.AgentException;
import org.jage.agent.IAgent;
import org.jage.platform.component.provider.ISelfAwareComponentInstanceProvider;

/**
 * Tests for an action of creating a new agent.
 *
 * @author AGH AgE Team
 */
public class CreateNewAgentActionTest extends AbstractActionTest {

	protected ISelfAwareComponentInstanceProvider provider;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		provider = mock(ISelfAwareComponentInstanceProvider.class);
		agent.setInstanceProvider(provider);
	}

	@Test
	public void createNewAgentActionTest() throws AgentException {

		// prepare new agent
		HelperTestAgent newAgent = new HelperTestAgent();

		// create complex action which creates and adds new agent
		// and than perform tracing action on the newly created agent.
		ComplexAction action = new ComplexAction();
		SingleAction addAction = (SingleAction) AgentActions.addToParent(agent, newAgent);
		IAgent agentToAdd = ((AddAgentActionContext)addAction.getContext()).getAgent();

		action.addChild(addAction);

		TracingActionContext tracingContext = new TracingActionContext();
		action.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(agentToAdd.getAddress()), tracingContext,
		        "c1Action"));
		action.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(agentToAdd.getAddress()), tracingContext,
		        "c2Action"));

		// when(
		// agentEnv.registerAgentInPath(
		// eq(agentToAdd.getAddress()), Mockito.anyList())).thenReturn(true);

		agent.runAction(action);
		aggregate.step();
		assertEquals("12", tracingContext.trace);

	}

}
