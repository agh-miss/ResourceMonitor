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
 * File: PropertiesSimpleAgent.java
 * Created: 2011-03-15
 * Author: kpietak
 * $Id: PropertiesSimpleAgent.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.examples.properties;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.address.IAgentAddress;
import org.jage.agent.AgentException;
import org.jage.agent.SimpleAgent;
import org.jage.platform.component.annotation.Inject;
import org.jage.platform.component.exception.ComponentException;
import org.jage.property.InvalidPropertyPathException;
import org.jage.property.Property;
import org.jage.property.PropertyField;
import org.jage.query.AgentEnvironmentQuery;

/**
 * This agent publishes some properties and watches its environment for other agents.
 * 
 * @author AGH AgE Team
 */
public class PropertiesSimpleAgent extends SimpleAgent {

	private static final long serialVersionUID = 2L;

	private final Logger log = LoggerFactory.getLogger(PropertiesSimpleAgent.class);

	@SuppressWarnings("unused")
	@Inject
	@PropertyField(propertyName = "actor")
	private String actor = null;

	/**
	 * Steps counter
	 */
	private transient int counter = 0;

	/**
	 * Constructs a new PropertiesSimpleAgent agent without an address.
	 */
	@Inject
	public PropertiesSimpleAgent() {
		log.info("Constructing");
	}

	@Override
	public void init() throws ComponentException {
		super.init();
		log.info("Initializing agent: {}", getAddress());
	}

	/**
	 * Executes a step of the agent. This agent queries its environment every few steps.
	 * <p>
	 * {@inheritDoc}
	 * 
	 * @see org.jage.agent.SimpleAgent#step()
	 */
	@Override
	public void step() {
		counter++;
		log.info("Agent {}: step {}", getAddress(), counter);
		if ((counter + this.hashCode()) % 3 == 0) {
			watch();
		}

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			log.error("Interrupted", e);
		}
	}

	private void watch() {
		Collection<SimpleAgent> answer;
		try {
			AgentEnvironmentQuery<SimpleAgent, SimpleAgent> query = new AgentEnvironmentQuery<SimpleAgent, SimpleAgent>();

			answer = queryEnvironment(query);
			log.info("Agent: {} can see in its environment: {} following agents:", getAddress(), getParentAddress());
			for (SimpleAgent entry : answer) {
				IAgentAddress agentAddress = (IAgentAddress)entry.getProperty("address").getValue();
				if (agentAddress != getAddress()) {
					log.info("    agent: {} with properties:", agentAddress);

					for (Property property : entry.getProperties()) {
						log.info("        {}: {}", property.getMetaProperty().getName(), property.getValue());
					}
				}
			}
		} catch (AgentException e) {
			log.error("Agent exception", e);
		} catch (InvalidPropertyPathException e) {
			log.error("Invalid property", e);
		}

	}

	@Override
	public boolean finish() {
		log.info("Finishing {}", getAddress());
		return true;
	}

}
