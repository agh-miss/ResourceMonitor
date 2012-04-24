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
 * File: HelperTestAggregate.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: HelperTestAggregate.java 166 2012-03-30 08:26:53Z faber $
 */

package org.jage.action.testHelpers;

import java.util.Collection;
import java.util.UUID;

import org.junit.Ignore;

import static org.mockito.Mockito.mock;

import org.jage.action.Action;
import org.jage.action.ActionPhase;
import org.jage.action.AgentAction;
import org.jage.action.SingleAction;
import org.jage.address.AgentAddress;
import org.jage.address.IAgentAddress;
import org.jage.address.node.INodeAddress;
import org.jage.agent.AgentException;
import org.jage.agent.IAgent;
import org.jage.agent.SimpleAggregate;
import org.jage.strategy.SupportedAgent;

/**
 * A sample implementation of aggregate that does nothing.
 *
 * @author AGH AgE Team
 */
@Ignore
@SupportedAgent(HelperTestAgent.class)
public class HelperTestAggregate extends SimpleAggregate {
	private static final long serialVersionUID = 8863710909609149082L;

	public static final IAgentAddress ADDRESS = new AgentAddress(UUID.randomUUID(), mock(INodeAddress.class), null);

	public HelperTestAggregate() {
		this.setAddress(ADDRESS);
	}

	/**
	 * This is our version of {@link org.jage.agent.AbstractAggregate}'s processEvent method, that instead of logging
	 * the gotten exceptions, rethrows them (wrapping them in ActionTestException (a subclass of RuntimeException)
	 * because that's the only thing we can do without redefining the moment)
	 */
	@Override
	protected void processActions() {
		Action action;
		while ((action = actionQueue.poll()) != null) {
			try {
				processAction(action);
			} catch (AgentException e) {
				throw new ActionTestException("Unknown event to process: " + e, e);
			}
		}
	}

	/**
	 * Exposes overridden method as public for testing purpose. {@inheritDoc}
	 */
	@Override
	public Collection<IAgentAddress> processActionInitialization(Action action) throws AgentException {
		return super.processActionInitialization(action);
	}

	/**
	 * Exposes overridden method as public for testing purpose. {@inheritDoc}
	 */
	@Override
	public void processActionAddressGeneration(Action action, Collection<IAgentAddress> all,
	        Collection<IAgentAddress> used) {
		super.processActionAddressGeneration(action, all, used);
	}

	/**
	 * Exposes overridden method as public for testing purpose. {@inheritDoc}
	 */
	@Override
	public void processActionPerform(Action action) throws AgentException {
		super.processActionPerform(action);
	}

	@AgentAction(name = "simpleTestAction")
	protected void performSimpleTestAction(IAgent target, SimpleTestActionContext context) {
		context.actionRun = true;
	}

	@SuppressWarnings("unused")
	@AgentAction(name = "privateTestAction")
	private void performPrivateTestAction(IAgent target, PrivateTestActionContext context) throws Exception {
		context.actionRun = true;
	}

	@AgentAction(name = "packageTestAction")
	void performPackageTestAction(IAgent target, PackageTestActionContext context) throws Exception {
		context.actionRun = true;
	}

	@AgentAction(name = "mult1Action")
	protected void performMult1Action(IAgent target, MultipleActionContext context) throws Exception {
		context.actionRun = true;
	}

	@AgentAction(name = "mult2Action")
	protected void performMult2Action(IAgent target, MultipleActionContext context) throws Exception {
		context.actionRun = true;
	}

	@AgentAction(name = "c1Action")
	protected void performComplex1Action(IAgent target, TracingActionContext context) throws Exception {
		context.trace += "1";
	}

	@AgentAction(name = "c2Action")
	protected void performComplex2Action(IAgent target, TracingActionContext context) throws Exception {
		context.trace += "2";
	}

	@AgentAction(name = "c3Action")
	protected void performComplex3Action(IAgent target, TracingActionContext context) throws Exception {
		context.trace += "3";
	}

	@AgentAction(name = "c4Action")
	protected void performComplex4Action(IAgent target, TracingActionContext context) throws Exception {
		context.trace += "4";
	}

	@AgentAction(name = "passToParentAction")
	protected void performPassToParentTestAction(IAgent target, PassToParentTestActionContext context) {
		context.actionTarget = target;
	}

	@AgentAction(name = "doNotPassToParentAction")
	protected void performDoNotPassToParentTestAction(IAgent target, DoNotPassToParentTestActionContext context) {
		context.actionTarget = target;
	}

	@AgentAction(name = "mixedAggregate", phase = ActionPhase.INIT)
	protected void performMixedAggregateAction(SingleAction action) {
		((MixedActionContext)action.getContext()).setExecAggr(true);
	}
}
