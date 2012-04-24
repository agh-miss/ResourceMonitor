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
 * File: CrawlingSimpleAgent.java
 * Created: 2011-03-15
 * Author: kpietak
 * $Id: CrawlingSimpleAgent.java 213 2012-04-08 20:21:35Z krzywick $
 */

package org.jage.examples.migration;

import java.util.Collection;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.action.AgentActions;
import org.jage.address.IAgentAddress;
import org.jage.agent.AgentException;
import org.jage.agent.IAgent;
import org.jage.agent.SimpleAgent;
import org.jage.platform.component.annotation.Inject;
import org.jage.platform.component.exception.ComponentException;
import org.jage.property.InvalidPropertyPathException;
import org.jage.query.AgentEnvironmentQuery;

/**
 * This agent finds environments where it can migrate to. If there is any suitable in migrates to a random one every few
 * seconds.
 *
 * @author AGH AgE Team
 */
public class CrawlingSimpleAgent extends SimpleAgent {

	private final Logger log = LoggerFactory.getLogger(CrawlingSimpleAgent.class);

	private static final long serialVersionUID = 2L;

	/**
	 * Random number generator
	 */
	private final Random random = new Random();

	/**
	 * Constructs a new migrating agent without an address.
	 */
	@Inject
	public CrawlingSimpleAgent() {
		log.info("Constructing");
	}

	@Override
	public void init() throws ComponentException {
		super.init();
		log.info("Initializing agent: {}", getAddress());
	}

	/**
	 * Steps counter
	 */
	private transient int counter = 0;

	@Override
	public void step() {
		counter++;
		if ((counter + hashCode()) % 50 == 0) {
			considerMigration();
		}

		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			log.error("Interrupted", e);
		}
	}

	/**
	 * Considers migration and migrates randomly.
	 */
	private void considerMigration() {
		Collection<IAgent> answer;
		IAgentAddress target = null;
		try {
			log.info("Queyring parent...");
			AgentEnvironmentQuery<IAgent, IAgent> query = new AgentEnvironmentQuery<IAgent, IAgent>();

			answer = queryParentEnvironment(query);
			if (answer.size() > 1) {
				log.info("Agent: {} can migrate from {} to following environments:", getAddress(), getParentAddress());
				float max = 0;
				for (IAgent entry : answer) {
					IAgentAddress possibleTargetAddress = (IAgentAddress)entry.getProperty("address").getValue();
					if (possibleTargetAddress != getParentAddress()) {
						log.info("   {}", possibleTargetAddress);
					}
					float rand = random.nextFloat();
					if (max < rand) {
						max = rand;
						target = possibleTargetAddress;
					}
				}
			} else {
				log.info("Agent: {} can not migrate anywhere from: {}", getAddress(), getParentAddress());
			}

			if (target != null) {
				if (target != getParentAddress()) {
					log.info("Agent: {} decides to migrate to environment: {}", getAddress(), target);
					try {
						doAction(AgentActions.migrate(this, target));
					} catch (AgentException e) {
						log.error("Can't move to: {}.", target, e);
					}
				} else {
					log.info("Agent: {} decides to stay in environment: {}", getAddress(), target);
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
