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
 * File: DelegationSimpleAggregate.java
 * Created: 2011-04-07
 * Author: faber
 * $Id: DelegationSimpleAggregate.java 64 2012-02-13 17:24:51Z faber $
 */

package org.jage.examples.delegation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.agent.AlreadyExistsException;
import org.jage.agent.IAgent;
import org.jage.agent.IAgentEnvironment;
import org.jage.agent.SimpleAggregate;
import org.jage.platform.component.annotation.Inject;
import org.jage.platform.component.exception.ComponentException;
import org.jage.property.PropertyField;

/**
 * This aggregate presents an example delegation of strategies. It basically enforces its children to use a strategy
 * that it has configured.
 * 
 * @author AGH AgE Team
 */

public class DelegationSimpleAggregate extends SimpleAggregate {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2L;

	private final Logger log = LoggerFactory.getLogger(DelegationSimpleAggregate.class);

	@Inject
	@PropertyField(propertyName = "echoStrategy")
	private IEchoStrategy echoStrategy;

	@Inject
	@PropertyField(propertyName = "childStrategy")
	private String childStrategy;

	/**
	 * @see org.jage.agent.SimpleAggregate#SimpleAggregate()
	 */
	@Inject
	public DelegationSimpleAggregate() {
		log.info("Constructing Delegation Simple Aggregate");
	}

	@Override
	public void init() throws ComponentException {
		super.init();

		log.info("Initializing Delegation Simple Aggregate: {}", getAddress());
	}

	/**
	 * Initialises this aggregate. It also sets an <em>echo strategy</em> for all its children agents that are instances
	 * of the {@link IEchoStrategyAcceptingAgent} class.
	 * <p>
	 * {@inheritDoc}
	 * 
	 * @see org.jage.agent.SimpleAggregate#init()
	 */

	@Override
	public void setAgentEnvironment(IAgentEnvironment localEnvironment) throws AlreadyExistsException {
		super.setAgentEnvironment(localEnvironment);

		try {
			getAgentsLock().readLock().lockInterruptibly();
			try {
				for (IAgent agent : agents.values()) {
					log.info("Agent: {}", agent.getClass());
					if (agent instanceof IEchoStrategyAcceptingAgent) {
						((IEchoStrategyAcceptingAgent)agent).acceptEchoStrategy(childStrategy);
					}
				}
			} finally {
				getAgentsLock().readLock().unlock();
			}
		} catch (InterruptedException e) {
			log.error("Interrupted in run", e);
		}
	}

	/**
	 * Executes a step of the aggregate. It uses an <em>echo strategy</em> and then delegates execution to the parent
	 * class.
	 * <p>
	 * {@inheritDoc}
	 * 
	 * @see org.jage.agent.SimpleAggregate#step()
	 */
	@Override
	public void step() {
		log.info("{} says Hello World from {}", getAddress(), getParentAddress());

		echoStrategy.echo(getParentAddress().toString());

		super.step();

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			log.error("Interrupted", e);
		}
	}

	@Override
	public boolean finish() throws ComponentException {
		log.info("Finishing Delegation Simple Aggregate: {}", getAddress());

		return super.finish();
	}
}
