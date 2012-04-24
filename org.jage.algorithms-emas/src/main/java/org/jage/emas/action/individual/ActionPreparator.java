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
 * File: ActionPreparator.java
 * Created: 2012-01-30
 * Author: Krzywicki
 * $Id$
 */

package org.jage.emas.action.individual;

import java.util.Collections;
import java.util.List;

import org.jage.action.Action;
import org.jage.action.SingleAction;
import org.jage.action.preparators.IActionPreparator;
import org.jage.address.IAgentAddress;
import org.jage.address.selector.UnicastSelector;
import org.jage.emas.agent.IndividualAgent;
import org.jage.emas.util.ChainingContext;
import org.jage.emas.util.ChainingContext.ChainingContextBuilder;
import org.jage.platform.component.annotation.Inject;
import org.jage.strategy.AbstractStrategy;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Action preparator for individual agents. It creates a chaining action context from the list provided in its
 * constructor, and applies it on each target agent.
 * <p>
 * If an agents step is 0, it is skipped (in order to allow aggregates to initialize it before). This might be changed
 * when some preprocess phase for initialization is added.
 *
 * @author AGH AgE Team
 */
public class ActionPreparator extends AbstractStrategy implements IActionPreparator<IndividualAgent> {

	private final List<ChainingContext> contexts;

	/**
	 * Create an {@link ActionPreparator} for the given list of contexts.
	 *
	 * @param contexts
	 *            the contexts for this preparator, may be empty
	 */
	@Inject
	public ActionPreparator(final List<ChainingContext> contexts) {
		this.contexts = checkNotNull(contexts);
	}

	@Override
	public List<Action> prepareActions(final IndividualAgent agent) {
		boolean condition = agent.getEnvironment().getStep() != 0;
		ChainingContextBuilder builder = ChainingContext.builder().appendAllIf(condition, contexts);
		if (builder.isEmpty()) {
			return Collections.emptyList();
		}

		final UnicastSelector<IAgentAddress> target = new UnicastSelector<IAgentAddress>(agent.getAddress());
		return Collections.<Action> singletonList(new SingleAction(target, builder.build()));
	}
}
