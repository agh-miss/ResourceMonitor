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
 * File: HelloWorldSimpleAgent.java
 * Created: 2011-03-15
 * Author: kpietak
 * $Id: HelloWorldSimpleAgent.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.examples.helloworld;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.agent.SimpleAgent;
import org.jage.platform.component.annotation.Inject;
import org.jage.platform.component.exception.ComponentException;

/**
 * This agent only logs basic information. It doesn't perform any other operations.
 * <p>
 * Make sure that logger configuration allows to log at info level from this class.
 * 
 * @author AGH AgE Team
 */
public class HelloWorldSimpleAgent extends SimpleAgent {

	private static final long serialVersionUID = 3L;

	/**
	 * Logger
	 */
	private final Logger log = LoggerFactory.getLogger(HelloWorldSimpleAgent.class);

	/**
	 * Constructs a new hello world agent without an address.
	 */
	@Inject
	public HelloWorldSimpleAgent() {
		log.info("Constructing Hello World Simple Agent");
	}

	/**
	 * This method initialises the agent.
	 * <p>
	 * {@inheritDoc}
	 * 
	 * @throws ComponentException
	 *             Thrown when initialisation fails.
	 * 
	 * @see org.jage.agent.AbstractAgent#init()
	 */
	@Override
	public void init() throws ComponentException {
		super.init();
		log.info("Initializing Hello World Simple Agent: {}", getAddress());
	}

	/**
	 * In this method, the agent performs its work.
	 * <p>
	 * {@inheritDoc}
	 * 
	 * @see org.jage.agent.SimpleAgent#step()
	 */
	@Override
	public void step() {
		log.info("{} says Hello World from {}", getAddress(), getParentAddress());
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			log.error("Interrupted", e);
		}
	}

	/**
	 * This method is called when the agent is to be removed (e.g. when the system is shutting down).
	 * <p>
	 * {@inheritDoc}
	 * 
	 * @see org.jage.agent.AbstractAgent#finish()
	 */
	@Override
	public boolean finish() {
		log.info("Finishing Hello World Simple Agent: {}", getAddress());
		return true;
	}

}
