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
 * $Id: ActionPreparator.java 211 2012-04-08 20:06:03Z krzywick $
 */

package org.jage.emas.action.island;

import java.util.Collections;
import java.util.List;

import org.jage.action.Action;
import org.jage.action.SingleAction;
import org.jage.action.preparators.IActionPreparator;
import org.jage.address.IAgentAddress;
import org.jage.address.selector.UnicastSelector;
import org.jage.emas.agent.IslandAgent;
import org.jage.emas.util.ChainingContext;
import org.jage.emas.util.ChainingContext.ChainingContextBuilder;
import org.jage.platform.component.annotation.Inject;
import org.jage.strategy.AbstractStrategy;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Action preparator for island agents. It creates a chaining action context from the list provided in its constructor,
 * and applies it on each target agent.
 * <p>
 * If an agents step is 0, an initialization action is prepended to the chain. This might be changed when some
 * preprocess phase for initialization is added.
 *
 * @author AGH AgE Team
 */
public class ActionPreparator extends AbstractStrategy implements IActionPreparator<IslandAgent> {

	private final ChainingContext initializationContext;

	private final List<ChainingContext> otherContexts;

	/**
	 * Create an {@link ActionPreparator} for the given initialization context and list of other contexts.
	 *
	 * @param initializationContext
	 *            the initialization context
	 * @param otherContexts
	 *            a list of other contexts, may be empty
	 */
	@Inject
	public ActionPreparator(final ChainingContext initializationContext, final List<ChainingContext> otherContexts) {
		this.initializationContext = checkNotNull(initializationContext);
		this.otherContexts = checkNotNull(otherContexts);
	}

	@Override
	public List<Action> prepareActions(final IslandAgent agent) {
		ChainingContextBuilder builder = ChainingContext.builder()
		        .appendIf(agent.getStep() == 0, initializationContext).appendAll(otherContexts);
		if (builder.isEmpty()) {
			return Collections.emptyList();
		}

		final UnicastSelector<IAgentAddress> target = new UnicastSelector<IAgentAddress>(agent.getAddress());
		return Collections.<Action> singletonList(new SingleAction(target, builder.build()));
	}
}
