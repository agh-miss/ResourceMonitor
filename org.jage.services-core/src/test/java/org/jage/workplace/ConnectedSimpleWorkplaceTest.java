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
 * File: ConnectedSimpleWorkplaceTest.java
 * Created: 2011-10-19
 * Author: faber
 * $Id: ConnectedSimpleWorkplaceTest.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.workplace;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.jage.agent.AlreadyExistsException;
import org.jage.agent.IAgent;
import org.jage.query.AgentEnvironmentQuery;

/**
 * Tests for the {@link ConnectedSimpleWorkplace} class.
 * 
 * @author AGH AgE Team
 */
public class ConnectedSimpleWorkplaceTest {

	private ConnectedSimpleWorkplace connectedSimpleWorkplace;

	private IWorkplaceEnvironment workplaceEnvironment;

	@Before
	public void setUp() throws Exception {
		connectedSimpleWorkplace = new ConnectedSimpleWorkplace();
		workplaceEnvironment = mock(IWorkplaceEnvironment.class);
	}

	/**
	 * Tests the {@link ConnectedSimpleWorkplace#queryParent(AgentEnvironmentQuery)} method.
	 * 
	 * @throws AlreadyExistsException
	 */
	@Test
	public void testQueryParent() throws AlreadyExistsException {
		connectedSimpleWorkplace.setWorkplaceEnvironment(workplaceEnvironment);
		AgentEnvironmentQuery<IAgent, ?> agentEnvironmentQuery = new AgentEnvironmentQuery<IAgent, IAgent>();
		connectedSimpleWorkplace.queryParent(agentEnvironmentQuery);

		verify(workplaceEnvironment).queryWorkplaces(agentEnvironmentQuery);
	}

}
