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
 * File: MultipleActionPreparatorTest.java
 * Created: 2011-05-06
 * Author: Krzywicki
 * $Id: MultipleActionPreparatorTest.java 183 2012-03-31 20:33:32Z krzywick $
 */

package org.jage.action.preparators;

import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jage.action.Action;
import org.jage.action.IActionContext;
import org.jage.action.SingleAction;
import org.jage.agent.IAgent;

import com.google.common.collect.ImmutableList;

/**
 * Tests for {@link MultipleActionPreparatorTest}.
 *
 * @author AGH AgE Team
 */
public class MultipleActionPreparatorTest extends AbstractActionPreparatorTest {

	@Mock
	private IActionContext actionContext1;

	@Mock
	private IActionContext actionContext2;

	private final MultipleActionPreparator<IAgent> preparator = new MultipleActionPreparator<IAgent>();

	@Test
	public void testPrepareAction() {
		// given
		List<IActionContext> list = ImmutableList.of(actionContext1, actionContext2);
		preparator.setActionContexts(list);

		// when
		Collection<Action> actions = preparator.prepareActions(agent);

		// then
		assertEquals(2, actions.size());

		for (Action action : actions) {
			assertTrue(action instanceof SingleAction);
			SingleAction singleAction = (SingleAction)action;
			assertTrue(singleAction.getTarget().selected(agentAddress));
			assertTrue(list.contains(singleAction.getContext()));
		}
	}
}
