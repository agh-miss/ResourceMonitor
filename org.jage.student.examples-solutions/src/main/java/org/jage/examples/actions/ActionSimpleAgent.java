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
 * File: ActionSimpleAgent.java
 * Created: 2011-03-15
 * Author: kpietak
 * $Id: ActionSimpleAgent.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.examples.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.action.SingleAction;
import org.jage.address.IAgentAddress;
import org.jage.address.selector.UnicastSelector;
import org.jage.agent.AgentException;
import org.jage.agent.SimpleAgent;
import org.jage.platform.component.annotation.Inject;
import org.jage.platform.component.exception.ComponentException;

/**
 * An agent showing how to order an action to perform by aggregate.
 * 
 * @author AGH AgE Team
 */
public class ActionSimpleAgent extends SimpleAgent {

	private static final long serialVersionUID = 1L;

	private final Logger log = LoggerFactory.getLogger(ActionSimpleAgent.class);

	/**
	 * Constructs a new ActionSimpleAgent without an address.
	 */
	@Inject
	public ActionSimpleAgent() {
		log.info("Constructing Action Simple Agent");
	}

	@Override
	public void step() {
		log.info("{} says Hello World from {}", getAddress(), getParentAddress());
		try {
			doAction(new SingleAction(new UnicastSelector<IAgentAddress>(getAddress()), new SampleActionContext()));
		} catch (AgentException e) {
			log.error("Agent exception", e);
			return;
		}

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			log.error("Interrupted", e);
		}

	}

	@Override
	public boolean finish() {
		log.info("Finishing Action Simple Agent: {}", getAddress());
		return true;
	}

	@Override
	public void init() throws ComponentException {
		super.init();
		log.info("Initializing Action Simple Agent: {}", getAddress());
	}

}
