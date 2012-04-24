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
 * File: StrategySimpleAgent.java
 * Created: 2011-03-15
 * Author: kpietak
 * $Id: StrategySimpleAgent.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.examples.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.agent.SimpleAgent;
import org.jage.platform.component.annotation.Inject;
import org.jage.platform.component.exception.ComponentException;
import org.jage.property.PropertyGetter;
import org.jage.property.PropertySetter;

/**
 * This agent presents an example of using strategies.
 * 
 * @author AGH AgE Team
 */
public class StrategySimpleAgent extends SimpleAgent {

	private static final long serialVersionUID = 2L;

	private final Logger log = LoggerFactory.getLogger(StrategySimpleAgent.class);

	private IEchoStrategy echoStrategy;

	/**
	 * Constructs a new StrategySimpleAgent agent without an address.
	 */
	@Inject
	public StrategySimpleAgent() {
		log.info("Constructing Strategy Simple Agent");
	}

	@Override
	public void init() throws ComponentException {
		super.init();
		log.info("Initializing Hello World Simple Agent: {}", getAddress());
	}

	/**
	 * Executes a step of the agent. This agent simply executes an echo strategy that was configured for it.
	 * <p>
	 * {@inheritDoc}
	 * 
	 * @see org.jage.agent.SimpleAgent#step()
	 */
	@Override
	public void step() {
		log.info("{} says Hello World from {}", getAddress(), getParentAddress());

		echoStrategy.echo();

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			log.error("Interrupted", e);
		}
	}

	@Override
	public boolean finish() {
		log.info("Finishing Hello World Simple Agent: {}", getAddress());
		return true;
	}

	@Inject
	@PropertyGetter(propertyName = "echoStr")
	public IEchoStrategy getEchoStr() {
		return echoStrategy;
	}

	@Inject
	@PropertySetter(propertyName = "echoStr")
	public void setEchoStr(IEchoStrategy val) {
		echoStrategy = val;
	}

}
