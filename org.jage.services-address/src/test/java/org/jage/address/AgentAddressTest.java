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
 * File: AgentAddressTest.java
 * Created: 2009-03-24
 * Author: pkarolcz
 * $Id: AgentAddressTest.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.address;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jage.address.node.NodeAddress;

/**
 * Tests for the AgentAddress class.
 * 
 * @author AGH AgE Team
 */
public class AgentAddressTest {

	private AgentAddress firstAddress;

	private NodeAddress nodeAddress;

	private UUID uuid;

	private final String userFriendlyName = "friendly";

	@Before
	public void setUp() {
		uuid = UUID.randomUUID();
		nodeAddress = new NodeAddress("local", "hostname");
		firstAddress = new AgentAddress(uuid, nodeAddress, userFriendlyName);
	}

	/**
	 * Tests some basic equality scenarios.
	 */
	@Test
	public void testEquals() {
		AgentAddress secondAddress = new AgentAddress(uuid, nodeAddress, userFriendlyName);

		assertTrue(firstAddress.equals(secondAddress));
		assertTrue(firstAddress.equals(new AgentAddress(uuid, nodeAddress, "notFriendly")));
		assertFalse(firstAddress.equals(new AgentAddress(UUID.randomUUID(), nodeAddress, "notFriendly")));
		assertFalse(firstAddress.equals(null));
	}

}
