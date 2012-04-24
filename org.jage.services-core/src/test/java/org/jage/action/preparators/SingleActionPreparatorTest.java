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
 * File: SimpleActionPreparatorTest.java
 * Created: 2011-05-06
 * Author: Krzywicki
 * $Id: SingleActionPreparatorTest.java 183 2012-03-31 20:33:32Z krzywick $
 */

package org.jage.action.preparators;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jage.action.Action;
import org.jage.action.IActionContext;
import org.jage.action.SingleAction;
import org.jage.agent.IAgent;

/**
 * Tests for {@link SingleActionPreparator}.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class SingleActionPreparatorTest extends AbstractActionPreparatorTest {

	@Mock
	private IActionContext actionContext;

	@InjectMocks
	private final SingleActionPreparator<IAgent> preparator = new SingleActionPreparator<IAgent>();

	@Test
	public void testPrepareAction() {
		// when
		Collection<Action> actions = preparator.prepareActions(agent);

		// then
		assertEquals(1, actions.size());
		Action action = actions.iterator().next();
		assertTrue(action instanceof SingleAction);
		SingleAction singleAction = (SingleAction)action;
		assertTrue(singleAction.getTarget().selected(agentAddress));
		assertEquals(actionContext, singleAction.getContext());
	}
}
