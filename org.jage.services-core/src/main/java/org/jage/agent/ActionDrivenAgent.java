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
 * File: ActionDrivenAgent.java
 * Created: 2011-04-30
 * Author: Krzywicki
 * $Id: ActionDrivenAgent.java 213 2012-04-08 20:21:35Z krzywick $
 */

package org.jage.agent;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.action.Action;
import org.jage.action.preparators.IActionPreparator;
import org.jage.platform.component.annotation.Inject;
import org.jage.property.PropertyField;

/**
 * This agent implementation relies on a {@link IActionPreparator} to provide its actual behavior.
 * <p>
 *
 * Given the agent's state and environment, the {@link IActionPreparator} prepares an appropriate action, which is then
 * run by the agent.
 *
 * @author AGH AgE Team
 */
public class ActionDrivenAgent extends SimpleAgent {

	/**
	 * ActionDrivenAgent properties.
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

	private static final Logger LOG = LoggerFactory.getLogger(ActionDrivenAgent.class);

	@Inject
	private IActionPreparator<ActionDrivenAgent> actionPreparator;

	@PropertyField(propertyName = Properties.STEP)
	private long step = 0;

    public long getStep() {
	    return step;
    }

	@Override
	public void step() {
		try {
			List<Action> actions = actionPreparator.prepareActions(this);
			log.debug("Submitting actions: {}", actions);
			doActions(actions);
		} catch (AgentException e) {
			LOG.error("An exception occurred during the action call", e);
		}

		step++;
		notifyMonitorsForChangedProperties();
	}
}
