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
 * File: AbstractActionTest.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: AbstractActionTest.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.action;

import org.junit.Before;

import static org.mockito.Mockito.mock;

import org.jage.action.testHelpers.HelperTestAgent;
import org.jage.action.testHelpers.HelperTestAggregate;
import org.jage.agent.IAgentEnvironment;
import org.jage.agent.ISimpleAgentEnvironment;

/**
 * Abstract class for all action tests. Suppresses output from AbstractSimpleAggregate for the duration of the test.
 * 
 * @author AGH AgE Team
 */
public abstract class AbstractActionTest {

	HelperTestAggregate aggregate;

	HelperTestAgent agent;

	IAgentEnvironment agentEnv;

	@Before
	@SuppressWarnings("unchecked")
	public void setUp() throws Exception {
		agent = new HelperTestAgent();
		aggregate = new HelperTestAggregate();
		agentEnv = mock(ISimpleAgentEnvironment.class);
		// Mockito.when(
		// agentEnv.registerAgentInPath(
		// Mockito.any(IAgentAddress.class), Mockito.anyList())).thenReturn(true);
		aggregate.setAgentEnvironment(agentEnv);
		// aggregate.addAgent(agent);

		aggregate.add(agent);
		aggregate.setInstanceProvider(new NullInstanceProvider());
	}

}
