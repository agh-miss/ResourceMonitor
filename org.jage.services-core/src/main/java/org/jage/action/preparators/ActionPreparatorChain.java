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
 * File: ChainingActionPreparator.java
 * Created: 2011-04-30
 * Author: Krzywicki
 * $Id: ActionPreparatorChain.java 177 2012-03-31 18:32:12Z faber $
 */

package org.jage.action.preparators;

import java.util.List;

import static java.util.Collections.singletonList;

import org.jage.action.Action;
import org.jage.action.ComplexAction;
import org.jage.agent.IAgent;
import org.jage.platform.component.annotation.Inject;
import org.jage.strategy.AbstractStrategy;

/**
 * A {@link IActionPreparator} that expects a list of other {@link IActionPreparator}s and build an aggregated complex
 * action upon the actions they prepare. This class allows to chain IActionPreparators.
 *
 * @param <T>
 *            a type of the agent that the preparator operates on.
 *
 * @author AGH AgE Team
 */
public class ActionPreparatorChain<T extends IAgent> extends AbstractStrategy implements IActionPreparator<T> {

	@Inject
	private List<IActionPreparator<IAgent>> actionPreparators;

	@Override
	public List<Action> prepareActions(final T agent) {

		final ComplexAction complexAction = new ComplexAction();
		for (IActionPreparator<IAgent> actionPreparator : actionPreparators) {
			for (Action action : actionPreparator.prepareActions(agent)) {
				complexAction.addChild(action);
			}
		}

		return singletonList((Action)complexAction);
	}

	/**
	 * Sets the action preparators that are to be chained by this preparator.
	 *
	 * @param actionPreparators
	 *            action preparators to be chained.
	 */
	public final void setActionPreparators(final List<IActionPreparator<IAgent>> actionPreparators) {
		this.actionPreparators = actionPreparators;
	}
}
