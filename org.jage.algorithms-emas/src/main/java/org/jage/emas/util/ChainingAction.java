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
 * File: ChainedAction.java
 * Created: 2012-03-16
 * Author: Krzywicki
 * $Id: ChainingAction.java 210 2012-04-08 20:04:41Z krzywick $
 */

package org.jage.emas.util;

import org.jage.action.AbstractPerformActionStrategy;
import org.jage.action.IActionContext;
import org.jage.action.SingleAction;
import org.jage.address.IAgentAddress;
import org.jage.address.selector.UnicastSelector;
import org.jage.agent.AgentException;
import org.jage.agent.IAgent;
import org.jage.emas.agent.EmasAgent;

import com.google.common.base.Optional;

/**
 * Utility class to chain actions. Subclasses need only implement the abstract template method. The next action to
 * perform will be inferred from the chaining context, and if present, submitted for execution.
 * <p>
 * This class can be removed when proper interleaving of actions is implemented.
 *
 * @param <A>
 *            the type of agent to perform the action on
 *
 * @author AGH AgE Team
 */
public abstract class ChainingAction<A extends EmasAgent> extends AbstractPerformActionStrategy {

	@Override
	public final void perfom(final IAgent target, final IActionContext context) throws AgentException {
		@SuppressWarnings("unchecked")
		// classCastExc is appropriate here
		final A agent = (A)target;
		doPerform(agent);

		final ChainingContext chainingContext = (ChainingContext)context;
		final Optional<ChainingContext> nextContext = chainingContext.getNextContext();
		if (nextContext.isPresent()) {
			final UnicastSelector<IAgentAddress> selector = new UnicastSelector<IAgentAddress>(target.getAddress());
			agent.getEnvironment().doAction(new SingleAction(selector, nextContext.get()), agent.getAddress());
		}
	}

	/**
	 * Sublasses should implement this template method to actually perform some action.
	 *
	 * @param target
	 *            the target agent
	 * @throws AgentException
	 *             if something goes wrong.
	 */
	protected abstract void doPerform(A target) throws AgentException;
}
