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
 * File: DelegationSimpleLeaf.java
 * Created: 2011-04-07
 * Author: faber
 * $Id: DelegationSimpleLeaf.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.examples.delegation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.agent.SimpleAgent;
import org.jage.platform.component.annotation.Inject;
import org.jage.platform.component.exception.ComponentException;

/**
 * This agent presents an example delegation of strategies. It is a leaf of tree of agents. It can accept an echo
 * strategy coming from a parent.
 * 
 * @author AGH AgE Team
 */
public class DelegationSimpleLeaf extends SimpleAgent implements IEchoStrategyAcceptingAgent {

	private static final long serialVersionUID = 2L;

	private final Logger log = LoggerFactory.getLogger(DelegationSimpleLeaf.class);

	private IEchoStrategy echoStrategy;

	/**
	 * Constructs a new DelegationSimpleLeaf agent without an address.
	 */
	@Inject
	public DelegationSimpleLeaf() {
		log.info("Constructing Delegation Simple Leaf");
	}

	@Override
	public void init() throws ComponentException {
		super.init();
		log.info("Initializing Delegation Simple Leaf: {}", getAddress());
	}

	/**
	 * Executes a step of the agent. This agent delegates its execution to the <em>echo strategy</em> and then sleeps
	 * for a while. {@inheritDoc}
	 * 
	 * @see org.jage.agent.SimpleAgent#step()
	 */
	@Override
	public void step() {
		log.info("{} says Hello World from {}", getAddress(), getParentAddress());

		echoStrategy.echo(getParentAddress().toString());

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			log.error("Interrupted", e);
		}
	}

	@Override
	public boolean finish() {
		log.info("Finishing Delegation Simple Leaf: {}", getAddress());
		return true;
	}

	@Override
	public void acceptEchoStrategy(String echoStrategyName) {
		log.info("{} asked to accept a strategy {}", getAddress(), echoStrategyName);

		echoStrategy = (IEchoStrategy)instanceProvider.getInstance(echoStrategyName);
	}
}
