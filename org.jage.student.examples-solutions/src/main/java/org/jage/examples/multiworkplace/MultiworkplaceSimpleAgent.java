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
package org.jage.examples.multiworkplace;

import java.util.Collection;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.action.AgentActions;
import org.jage.address.IAgentAddress;
import org.jage.address.selector.agent.ParentAgentAddressSelector;
import org.jage.agent.AgentException;
import org.jage.agent.IAgent;
import org.jage.agent.SimpleAgent;
import org.jage.communication.message.Header;
import org.jage.communication.message.IHeader;
import org.jage.communication.message.Message;
import org.jage.platform.component.annotation.Inject;
import org.jage.platform.component.exception.ComponentException;
import org.jage.query.AgentEnvironmentQuery;

/**
 * This agent should be located just beneath a workplace.
 *
 * @author AGH AgE Team
 */
public class MultiworkplaceSimpleAgent extends SimpleAgent {

	private static Logger log = LoggerFactory.getLogger(MultiworkplaceSimpleAgent.class);

	private static final long serialVersionUID = 2L;

	private static final Random random = new Random();

	/**
	 * @see SimpleAgent#SimpleAgent()
	 */
	@Inject
	public MultiworkplaceSimpleAgent() {
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
		try {
			IHeader<IAgentAddress> header = new Header<IAgentAddress>(getAddress(), new ParentAgentAddressSelector(
			        getAddress()));
			Message<IAgentAddress, String> message = new Message<IAgentAddress, String>(header, "Message to parent");
			doAction(AgentActions.sendMessage(message));
		} catch (AgentException e1) {
			log.error("Could not send a message.", e1);
		}

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
	 * Considers migration and migrates eventually
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
					IAgentAddress possibleTargetAddress = entry.getAddress();
					if (possibleTargetAddress != getParentAddress()) {
						log.info("   " + possibleTargetAddress);
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
						log.error("Can't move to: " + target + ".", e);
					}
				} else {
					log.info("Agent: {}  decides to stay in environment: {}", getAddress(), target);
				}
			}
		} catch (AgentException e) {
			log.error("Agent exception", e);
		}
	}

	@Override
	public boolean finish() throws ComponentException {
		log.info("Finishing {}", getAddress());
		return super.finish();
	}

}
