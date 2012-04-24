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
 * File: ActionDrivenAggregate.java
 * Created: 2012-02-02
 * Author: Krzywicki
 * $Id: ActionDrivenAggregate.java 213 2012-04-08 20:21:35Z krzywick $
 */

package org.jage.agent;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.action.Action;
import org.jage.action.IActionContext;
import org.jage.action.SingleAction;
import org.jage.action.preparators.IActionPreparator;
import org.jage.platform.component.annotation.Inject;
import org.jage.property.PropertyField;

import com.google.common.collect.Lists;

import static com.google.common.collect.Iterables.consumingIterable;
import static com.google.common.collect.Iterators.consumingIterator;

/**
 * This aggregate implementation relies on a {@link IActionPreparator} to provide its actual behavior.
 * <p>
 * Given the aggregate state and environment, the {@link IActionPreparator} prepares an appropriate action, which is
 * then run by the aggregate.
 * <p>
 * Then, the aggregate runs the step methods of its children.
 *
 * @author AGH AgE Team
 */
public class ActionDrivenAggregate extends SimpleAggregate {

	/**
	 * ActionDrivenAggregate properties.
	 *
	 * @author AGH AgE Team
	 */
	public static class Properties {

		/**
		 * The actual step of computation.
		 */
		public static final String STEP = "step";
	}

	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(ActionDrivenAggregate.class);

	@Inject
	private IActionPreparator<ActionDrivenAggregate> actionPreparator;

	@PropertyField(propertyName = Properties.STEP)
	private long step = 0;

	public long getStep() {
		return step;
	}

	@Override
	public void step() {
		// invoke on children
		super.step();
		performPostponedActions();

		try {
			List<Action> actions = actionPreparator.prepareActions(this);
			log.debug("Step {}, Submitting actions: {}", step, actions);
			doActions(actions);
		} catch (AgentException e) {
			log.error("An exception occurred during the action call", e);
		}

		step++;
		notifyMonitorsForChangedProperties();
	}

	private final List<SingleAction> addAgentActions = Lists.newLinkedList();
	private final List<ISimpleAgent> killAgentActionTargets = Lists.newLinkedList();
	private final List<IActionContext> killAgentActionContexts = Lists.newLinkedList();

	@Override
	protected void performAddAgentAction(final SingleAction action) {
	    addAgentActions.add(action);
	}

	@Override
	protected void performKillAgentAction(final ISimpleAgent target, final IActionContext context) {
		killAgentActionTargets.add(target);
		killAgentActionContexts.add(context);
	}

    private void performPostponedActions() {
    	log.debug("Performing postponed actions of {}", this);
    	// Execute all postponed actions. Don't forget to remove them from the collections.

    	for (SingleAction action : consumingIterable(addAgentActions)) {
	        super.performAddAgentAction(action);
        }

    	Iterator<ISimpleAgent> targets = consumingIterator(killAgentActionTargets.iterator());
    	Iterator<IActionContext> contexts = consumingIterator(killAgentActionContexts.iterator());
    	while(targets.hasNext() && contexts.hasNext()) {
    		super.performKillAgentAction(targets.next(), contexts.next());
    	}
    }
}
