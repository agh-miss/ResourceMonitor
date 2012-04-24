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
 * File: ParentAgentAddressSelectorTest.java
 * Created: 2009-04-29
 * Author: kpietak
 * $Id: ParentAgentAddressSelectorTest.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.address.selector.agent;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

import org.jage.address.AgentAddress;
import org.jage.address.IAgentAddress;
import org.jage.address.node.INodeAddress;
import org.jage.address.selector.AbstractAddressSelectorTest;
import org.jage.address.selector.IAddressSelector;

public class ParentAgentAddressSelectorTest extends AbstractAddressSelectorTest {

	private IAgentAddress address;

	private Collection<IAgentAddress> allAddresses;

	private Collection<IAgentAddress> usedAddresses;

	private ParentAgentAddressSelector selector;

	private INodeAddress nodeAddress;

	@Before
	public void setUp() {
		nodeAddress = mock(INodeAddress.class);
		address = new AgentAddress(UUID.randomUUID(), nodeAddress, null);

		allAddresses = new HashSet<IAgentAddress>();
		allAddresses.add(address);
		// used addresses is empty
		usedAddresses = new HashSet<IAgentAddress>();

		selector = new ParentAgentAddressSelector(address);
	}

	@Override
	protected IAddressSelector<IAgentAddress> getSelectorUnderTest() {
		return selector;
	}

	@Test
	public void testEmptySetsInitialize() {
		selector.initialize(new HashSet<IAgentAddress>(), new HashSet<IAgentAddress>());
		assertEquals(Collections.singleton(address), collectionFromIterable(selector.addresses()));
	}

	@Test
	public void testEmptyUsedSetInitialize() {
		selector.initialize(allAddresses, usedAddresses);
		assertEquals(Collections.singleton(address), collectionFromIterable(selector.addresses()));
	}

	@Test
	public void testInconsistentSetInitialize() {
		usedAddresses.add(new AgentAddress(UUID.randomUUID(), nodeAddress, null));
		selector.initialize(allAddresses, usedAddresses);
		assertEquals(Collections.singleton(address), collectionFromIterable(selector.addresses()));
	}

	@Test
	public void testAddresses() {
		assertFalse(collectionFromIterable(selector.addresses()).isEmpty());
		selector.initialize(allAddresses, usedAddresses);
		assertEquals(Collections.singleton(address), collectionFromIterable(selector.addresses()));
	}

	@Override
	@Test
	public void testSelectedNull() {
		assertFalse(selector.selected(null));
		selector.initialize(allAddresses, usedAddresses);
		assertFalse(selector.selected(null));
	}

	@Override
	@Test
	public void testSelectedDifferent() {
		assertFalse(selector.selected(new AgentAddress(UUID.randomUUID(), nodeAddress, null)));
		selector.initialize(allAddresses, usedAddresses);
		assertFalse(selector.selected(new AgentAddress(UUID.randomUUID(), nodeAddress, null)));
	}

	@Override
	@Test
	public void testInitializeWithInconsistentSets() {
		// override, but do not throw exception
		super.testInitializeWithInconsistentSets();
	}

	@Override
	@Test
	public void testInitializeWithNullUsed() {
		// override, but do not throw exception
		super.testInitializeWithNullUsed();
	}

	@Override
	@Test
	public void testInitializeWithNullAll() {
		// override, but do not throw exception
		super.testInitializeWithNullAll();
	}

	@Override
    @Test
	public void testInitializeBothNull() {
		// override, but do not throw exception
		super.testInitializeBothNull();
	}
}
