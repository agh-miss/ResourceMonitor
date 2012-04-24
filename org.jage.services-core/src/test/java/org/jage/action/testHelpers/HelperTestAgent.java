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
 * File: HelperTestAgent.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: HelperTestAgent.java 213 2012-04-08 20:21:35Z krzywick $
 */

package org.jage.action.testHelpers;

import java.util.UUID;

import org.junit.Ignore;

import static org.mockito.Mockito.mock;

import org.jage.action.Action;
import org.jage.address.AgentAddress;
import org.jage.address.IAgentAddress;
import org.jage.address.node.INodeAddress;
import org.jage.agent.AgentException;
import org.jage.agent.SimpleAgent;

/**
 * A sample, helper agent implementation that does nothing.
 *
 * @author AGH AgE Team
 */
@Ignore
public class HelperTestAgent extends SimpleAgent {

	private static final long serialVersionUID = 1374519285747616223L;

	public static final IAgentAddress ADDRESS = new AgentAddress(UUID.randomUUID(), mock(INodeAddress.class), null);

	public HelperTestAgent() {
		setAddress(ADDRESS);
	}

	public HelperTestAgent(final IAgentAddress newAddress) {
		setAddress(newAddress);
	}

	@Override
	public void step() {
		// Empty
	}

	@Override
	public boolean finish() {
		return true;
	}

	@Override
	public void init() {
		// Empty
	}

	public void runAction(final Action action) throws AgentException {
		doAction(action);
	}
}
