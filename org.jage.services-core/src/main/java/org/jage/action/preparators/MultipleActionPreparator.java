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
 * File: AggregatingActionPreparator.java
 * Created: 2011-04-30
 * Author: Krzywicki
 * $Id: MultipleActionPreparator.java 183 2012-03-31 20:33:32Z krzywick $
 */

package org.jage.action.preparators;

import java.util.List;

import org.jage.action.Action;
import org.jage.action.IActionContext;
import org.jage.action.SingleAction;
import org.jage.address.IAgentAddress;
import org.jage.address.selector.UnicastSelector;
import org.jage.agent.IAgent;
import org.jage.platform.component.annotation.Inject;
import org.jage.strategy.AbstractStrategy;

import static com.google.common.collect.Lists.newArrayListWithCapacity;

/**
 * A {@link IActionPreparator} that expects a list of {@link IActionContext}s and build an aggregated complex action
 * upon them.
 *
 * @param <T>
 *            a type of the agent that the preparator operates on.
 *
 * @author AGH AgE Team
 */
public class MultipleActionPreparator<T extends IAgent> extends AbstractStrategy implements IActionPreparator<T> {

	@Inject
	private List<IActionContext> actionContexts;

	@Override
	public List<Action> prepareActions(final IAgent agent) {
		IAgentAddress agentAddress = agent.getAddress();

		final List<Action> actions = newArrayListWithCapacity(actionContexts.size());
		for (final IActionContext actionContext : actionContexts) {
			actions.add(new SingleAction(new UnicastSelector<IAgentAddress>(agentAddress), actionContext));
		}

		return actions;
	}

	/**
	 * Sets action contexts that will be used in prepared actions (one action per single context).
	 *
	 * @param actionContexts
	 *            action contexts to set.
	 */
	public void setActionContexts(final List<IActionContext> actionContexts) {
		this.actionContexts = actionContexts;
	}
}
