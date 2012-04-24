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
 * File: MultiAnnotatedActionTest.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: MultiAnnotatedActionTest.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.action;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jage.action.testHelpers.ActionTestException;
import org.jage.action.testHelpers.HelperTestAgent;
import org.jage.action.testHelpers.MultipleActionContext;
import org.jage.address.IAgentAddress;
import org.jage.address.selector.UnicastSelector;
import org.jage.agent.AgentException;

/**
 * Tests for actions with multi-annotated contexts.
 * 
 * @author AGH AgE Team
 */
public class MultiAnnotatedActionTest extends AbstractActionTest {

	@Test
	public void testFailNoDefaultAction() throws Exception {
		MultipleActionContext context = new MultipleActionContext();
		agent.runAction(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context));
		try {
			aggregate.step();
			fail();
		} catch (ActionTestException e) {
			assertTrue(e.getCause() instanceof AgentException);
			assertFalse(context.actionRun);
		}
	}

	@Test
	public void testExecuteSpecificAction() throws Exception {
		MultipleActionContext context = new MultipleActionContext();
		agent.runAction(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        "mult1Action"));
		aggregate.step();
		assertTrue(context.actionRun);
	}

	@Test
	public void testActionNotFoundInContext() throws Exception {
		MultipleActionContext context = new MultipleActionContext();
		agent.runAction(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        "mult3Action"));
		try {
			aggregate.step();
			fail();
		} catch (ActionTestException e) {
			assertTrue(e.getCause() instanceof AgentException);
			assertFalse(context.actionRun);
		}
	}
}
