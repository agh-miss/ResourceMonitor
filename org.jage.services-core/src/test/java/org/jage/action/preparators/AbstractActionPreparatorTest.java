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
 * File: AbstractActionPreparatorTest.java
 * Created: 2011-05-06
 * Author: Krzywicki
 * $Id: AbstractActionPreparatorTest.java 177 2012-03-31 18:32:12Z faber $
 */

package org.jage.action.preparators;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

import org.jage.address.IAgentAddress;
import org.jage.agent.IAgent;
import org.jage.agent.IAgentEnvironment;

/**
 * Base class for {@link IActionPreparator} Tests.
 * 
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractActionPreparatorTest {

	@Mock
	protected IAgent agent;

	@Mock
	protected IAgentEnvironment environment;

	@Mock
	protected IAgentAddress agentAddress;

	@Before
	public void setUp() {
		when(agent.getAddress()).thenReturn(agentAddress);
	}
}
