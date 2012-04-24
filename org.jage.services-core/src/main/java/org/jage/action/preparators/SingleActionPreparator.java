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
 * File: SimpleActionPreparator.java
 * Created: 2011-04-30
 * Author: Krzywicki
 * $Id: SingleActionPreparator.java 183 2012-03-31 20:33:32Z krzywick $
 */

package org.jage.action.preparators;

import java.util.List;

import static java.util.Collections.singletonList;

import org.jage.action.Action;
import org.jage.action.IActionContext;
import org.jage.action.SingleAction;
import org.jage.address.IAgentAddress;
import org.jage.address.selector.UnicastSelector;
import org.jage.agent.IAgent;
import org.jage.platform.component.annotation.Inject;
import org.jage.strategy.AbstractStrategy;

/**
 * A simple {@link IActionPreparator} that expects a single {@link IActionContext} and build an action upon it.
 *
 * @param <T>
 *            a type of the agent that the preparator operates on.
 *
 * @author AGH AgE Team
 */
public class SingleActionPreparator<T extends IAgent> extends AbstractStrategy implements IActionPreparator<T> {

	@Inject
	private IActionContext actionContext;

	@Override
	public List<Action> prepareActions(final T agent) {
		IAgentAddress agentAddress = agent.getAddress();
		return singletonList((Action)new SingleAction(new UnicastSelector<IAgentAddress>(agentAddress), actionContext));
	}

	/**
	 * Sets the action context that will be used in the prepared action.
	 *
	 * @param actionContext
	 *            the action context to set
	 */
	public void setActionContext(final IActionContext actionContext) {
		this.actionContext = actionContext;
	}
}
