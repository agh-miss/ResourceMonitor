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
 * File: ActionPreparatorChainTest.java
 * Created: 2011-05-06
 * Author: Krzywicki
 * $Id: ActionPreparatorChainTest.java 183 2012-03-31 20:33:32Z krzywick $
 */

package org.jage.action.preparators;

import java.util.Collection;
import java.util.List;

import static java.util.Collections.singletonList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jage.action.Action;
import org.jage.action.ComplexAction;
import org.jage.agent.IAgent;

import com.google.common.collect.ImmutableList;

/**
 * Tests for {@link ActionPreparatorChain}.
 *
 * @author AGH AgE Team
 */
public class ActionPreparatorChainTest extends AbstractActionPreparatorTest {

	@Mock
	private IActionPreparator<IAgent> preparator1;

	@Mock
	private IActionPreparator<IAgent> preparator2;

	@Mock
	private Action action1;

	@Mock
	private Action action2;

	private ActionPreparatorChain<IAgent> chain;

	@SuppressWarnings("unchecked")
    @Override
    @Before
	public void setUp() {
		super.setUp();
		Mockito.when(preparator1.prepareActions(agent)).thenReturn(singletonList(action1));
		Mockito.when(preparator2.prepareActions(agent)).thenReturn(singletonList(action2));

		chain = new ActionPreparatorChain<IAgent>();
		chain.setActionPreparators(ImmutableList.of(preparator1, preparator2));
	}

	@Test
	public void testDelegation() {
		// when
		Collection<Action> actions = chain.prepareActions(agent);

		// then
		assertEquals(1, actions.size());
		Action action = actions.iterator().next();
		assertTrue(action instanceof ComplexAction);
		ComplexAction complexAction = (ComplexAction)action;

		List<Action> children = complexAction.getChildren();
		assertEquals(2, children.size());

		assertEquals(action1, children.get(0));
		assertEquals(action2, children.get(1));
	}

}
