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
 * File: ActionDrivenAgentTest.java
 * Created: 2011-04-30
 * Author: Krzywicki
 * $Id: ActionDrivenAgentTest.java 213 2012-04-08 20:21:35Z krzywick $
 */

package org.jage.agent;

import static java.util.Collections.singletonList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jage.action.Action;
import org.jage.action.preparators.IActionPreparator;
import org.jage.property.Property;
import org.jage.property.monitors.AbstractPropertyMonitor;
import org.jage.utils.BaseTest;

import static org.jage.agent.ActionDrivenAgent.Properties.STEP;

/**
 * Tests for {@link ActionDrivenAgent}.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class ActionDrivenAgentTest extends BaseTest {

	@Mock
	private IActionPreparator<IAgent> preparator;

	@Mock
	private Action action;

	@Mock
	private ISimpleAgentEnvironment environment;

	@Mock
	private AbstractPropertyMonitor monitor;

	@InjectMocks
	private final ActionDrivenAgent agent = new ActionDrivenAgent();

	@Before
	public void setup() throws Exception {
		when(preparator.prepareActions(any(IAgent.class))).thenReturn(singletonList(action));
	}

	@Test
	public void initialStepShouldBe0() throws Exception {
		// when
		final long step = (Long)agent.getProperty(ActionDrivenAgent.Properties.STEP).getValue();

		// then
		assertEquals(0, step);
	}

	@Test
	public void shouldIncreaseStepOnCall() throws Exception {
		// given
		final long initialStep = (Long)agent.getProperty(STEP).getValue();

		// when
		agent.step();
		final long step = (Long)agent.getProperty(STEP).getValue();

		// then
		assertEquals(initialStep + 1, step);
	}

	@Test
	public void shouldAskPreparatorForActionOnCall() {
		// when
		agent.step();

		// then
		verify(preparator).prepareActions(agent);
	}

	@Test
	public void shouldExecuteActionOnCall() {
		// when
		agent.step();

		// then
		verify(environment).doAction(action, agent.getAddress());
	}

	@Test
	public void shouldNotifyListenersOnCall() throws Exception {
		// given
		agent.addPropertyMonitor(STEP, monitor);

		// when
		agent.step();

		// then
		verify(monitor).propertyChanged(any(Property.class), any());
	}
}
