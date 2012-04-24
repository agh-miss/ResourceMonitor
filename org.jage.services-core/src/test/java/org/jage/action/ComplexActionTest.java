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
 * File: ComplexActionTest.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: ComplexActionTest.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.action;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import org.jage.action.testHelpers.HelperTestAgent;
import org.jage.action.testHelpers.TracingActionContext;
import org.jage.address.IAgentAddress;
import org.jage.address.selector.UnicastSelector;

/**
 * Test for complex actions.
 * 
 * @author AGH AgE Team
 */
public class ComplexActionTest extends AbstractActionTest {

	@Test
	public void testEmptyComplex() throws Exception {
		TracingActionContext context = new TracingActionContext();
		/* Create an empty complex action */
		ComplexAction action = new ComplexAction();
		agent.runAction(action);
		aggregate.step();
		assertEquals("", context.trace);
	}

	@Test
	public void testSimpleTrace() throws Exception {
		TracingActionContext context = new TracingActionContext();
		agent.runAction(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        "c1Action"));
		aggregate.step();
		assertEquals("1", context.trace);
	}

	@Test
	public void testSimpleComplex() throws Exception {
		TracingActionContext context = new TracingActionContext();
		/* Create a flat structure of complex actions */
		ComplexAction action = new ComplexAction();
		action.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        "c1Action"));
		action.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        "c3Action"));
		action.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        "c2Action"));

		agent.runAction(action);
		aggregate.step();
		assertEquals("132", context.trace);
	}

	@Test
	public void testComplexAction1() throws Exception {
		TracingActionContext context = new TracingActionContext();
		/**
		 * Create a tree structure of complex actions, looking like this:
		 * 
		 * <pre>
		 *  c 
		 *   1 
		 *  2
		 * </pre>
		 */
		ComplexAction action = new ComplexAction();
		ComplexAction action1 = new ComplexAction();
		action1.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        "c1Action"));
		action.addChild(action1);
		action.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        "c2Action"));

		agent.runAction(action);
		aggregate.step();
		assertEquals("12", context.trace);
	}

	@Test
	public void testComplexAction2() throws Exception {
		TracingActionContext context = new TracingActionContext();
		/**
		 * Create a tree structure of complex actions, looking like this:
		 * 
		 * <pre>
		 *  c 
		 *   1 
		 *  c
		 *   2
		 * </pre>
		 */
		ComplexAction action = new ComplexAction();
		ComplexAction action1 = new ComplexAction();
		action1.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        "c1Action"));
		action.addChild(action1);
		ComplexAction action2 = new ComplexAction();
		action2.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        "c2Action"));
		action.addChild(action2);

		agent.runAction(action);
		aggregate.step();
		assertEquals("12", context.trace);
	}

	@Test
	public void testComplexAction3() throws Exception {
		TracingActionContext context = new TracingActionContext();
		/**
		 * Create a tree structure of complex actions, looking like this:
		 * 
		 * <pre>
		 *  c 
		 *   1 
		 *  c
		 *   2
		 *   3
		 * </pre>
		 */
		ComplexAction action = new ComplexAction();
		ComplexAction action1 = new ComplexAction();
		action1.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        "c1Action"));
		action.addChild(action1);
		ComplexAction action2 = new ComplexAction();
		action2.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        "c2Action"));
		action2.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        "c3Action"));
		action.addChild(action2);

		agent.runAction(action);
		aggregate.step();
		assertEquals("123", context.trace);
	}

	@Test
	public void testComplexAction4() throws Exception {
		TracingActionContext context = new TracingActionContext();
		/**
		 * Create a tree structure of complex actions, looking like this:
		 * 
		 * <pre>
		 *  1 
		 *  c 
		 *   2
		 *   3
		 * </pre>
		 */
		ComplexAction action = new ComplexAction();
		action.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        "c1Action"));
		ComplexAction action1 = new ComplexAction();
		action1.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        "c2Action"));
		action1.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        "c3Action"));
		action.addChild(action1);

		agent.runAction(action);
		aggregate.step();
		assertEquals("123", context.trace);
	}

	@Test
	public void testComplexAction5() throws Exception {
		TracingActionContext context = new TracingActionContext();
		/**
		 * Create a tree structure of complex actions, looking like this:
		 * 
		 * <pre>
		 *  1
		 *  c 
		 *   2
		 *   3 
		 *   4
		 *   4
		 * </pre>
		 */
		ComplexAction action = new ComplexAction();
		action.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        "c1Action"));
		ComplexAction action1 = new ComplexAction();
		action1.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        "c2Action"));
		action1.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        "c3Action"));
		action1.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        "c4Action"));
		action1.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        "c4Action"));
		action.addChild(action1);

		agent.runAction(action);
		aggregate.step();
		assertEquals("12344", context.trace);
	}

	@Test
	public void testComplexAction6() throws Exception {
		TracingActionContext context = new TracingActionContext();
		/**
		 * Create a tree structure of complex actions, looking like this:
		 * 
		 * <pre>
		 *  1 
		 *  2 
		 *  c
		 *   3 
		 *   4
		 * </pre>
		 */
		ComplexAction action = new ComplexAction();
		action.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        "c1Action"));
		action.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        "c2Action"));
		ComplexAction action1 = new ComplexAction();
		action1.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        "c3Action"));
		action1.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        "c4Action"));
		action.addChild(action1);

		agent.runAction(action);
		aggregate.step();
		assertEquals("1234", context.trace);
	}

	@Test
	public void testConvolutedComplexAction() throws Exception {
		TracingActionContext context = new TracingActionContext();
		/**
		 * Create a complex tree structure of complex actions, looking like this:
		 * 
		 * <pre>
		 *  c 
		 *   2 
		 *   3 
		 *  c 
		 *   3 
		 *   4 
		 *   1 
		 *  c // action3
		 *   c // action32
		 *    c // action324
		 *     1 
		 *   3 
		 *  c 
		 *   1 
		 *  1
		 * </pre>
		 */
		ComplexAction action = new ComplexAction();
		ComplexAction action1 = new ComplexAction();
		action1.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        "c2Action"));
		action1.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        "c3Action"));
		action.addChild(action1);
		ComplexAction action2 = new ComplexAction();
		action2.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        "c3Action"));
		action2.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        "c4Action"));
		action2.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        "c1Action"));
		action.addChild(action2);
		ComplexAction action3 = new ComplexAction();
		ComplexAction action32 = new ComplexAction();
		ComplexAction action324 = new ComplexAction();
		action324.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        "c1Action"));
		action32.addChild(action324);
		action3.addChild(action32);
		action3.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        "c3Action"));
		action.addChild(action3);
		ComplexAction action4 = new ComplexAction();
		action4.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        "c1Action"));
		action.addChild(action4);
		action.addChild(new SingleAction(new UnicastSelector<IAgentAddress>(HelperTestAgent.ADDRESS), context,
		        "c1Action"));

		agent.runAction(action);
		aggregate.step();
		assertEquals("233411311", context.trace);
	}
}
