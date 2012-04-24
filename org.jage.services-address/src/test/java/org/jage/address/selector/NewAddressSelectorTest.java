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
 * File: NewAddressSelectorTest.java
 * Created: 2009-03-11
 * Author: awos
 * $Id: NewAddressSelectorTest.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.address.selector;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.jage.address.AgentAddress;
import org.jage.address.IAgentAddress;
import org.jage.address.node.INodeAddress;

/**
 * TODO describe this type
 * 
 * @author Adam Wos <adam.wos@gmail.com>
 */
public class NewAddressSelectorTest extends AbstractAddressSelectorTest {

	private UUID uuid;

	private INodeAddress nodeAddress = mock(INodeAddress.class);

	private IAddressSelector<IAgentAddress> selector;

	private IAgentAddress address;

	private Collection<IAgentAddress> all;

	private Collection<IAgentAddress> used;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() {
		uuid = UUID.randomUUID();

		address = new AgentAddress(uuid, nodeAddress, null);
		selector = new NewAddressSelector<IAgentAddress>(address);
		all = new HashSet<IAgentAddress>();
		used = new HashSet<IAgentAddress>();
	}

	@Override
	protected IAddressSelector<IAgentAddress> getSelectorUnderTest() {
		return selector;
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullAddress() {
		selector = new NewAddressSelector<IAgentAddress>(null);
	}

	/**
	 * Test method for {@link org.jage.address.selector.NewAddressSelector#addresses()}.
	 */
	@Test
	public void testAddressesBeforeInit() {
		assertEquals(Collections.emptySet(), collectionFromIterable(selector.addresses()));
	}

	/**
	 * Test method for {@link org.jage.address.selector.NewAddressSelector#addresses()}.
	 */
	@Test
	public void testAddressesAfterInit() {
		selector.initialize(all, used);
		assertEquals(Collections.singleton(address), collectionFromIterable(selector.addresses()));
	}

	/**
	 * Test method for
	 * {@link org.jage.address.selector.NewAddressSelector#initialize(java.util.Collection, java.util.Collection)} .
	 */
	@Test
	public void testInitializeNull() {
		selector.initialize(null, null);
		assertEquals(Collections.singleton(address), collectionFromIterable(selector.addresses()));
	}

	/**
	 * Test method for {@link org.jage.address.selector.NewAddressSelector#selected(org.jage.address.IAddress)} .
	 */
	@Test
	public void testSelected() {
		Assert.assertFalse(selector.selected(null));
		Assert.assertFalse(selector.selected(new AgentAddress(UUID.randomUUID(), nodeAddress, null)));
		Assert.assertFalse(selector.selected(address));

		selector.initialize(all, used);

		Assert.assertFalse(selector.selected(null));
		Assert.assertFalse(selector.selected(new AgentAddress(UUID.randomUUID(), nodeAddress, null)));
		Assert.assertTrue(selector.selected(address));
	}

	@Override
	@Test
	public void testInitializeBothNull() {
		// override, but do not throw exception
		super.testInitializeBothNull();
	}

	@Override
	@Test
	public void testInitializeWithInconsistentSets() {
		// override, but do not throw exception
		super.testInitializeWithInconsistentSets();
	}

	@Override
	@Test
	public void testInitializeWithNullAll() {
		// override, but do not throw exception
		super.testInitializeWithNullAll();
	}

	@Override
	@Test
	public void testInitializeWithNullUsed() {
		// override, but do not throw exception
		super.testInitializeWithNullUsed();
	}
}
