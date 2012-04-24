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
 * File: AbstractAddressSelectorTest.java
 * Created: 2009-03-11
 * Author: awos
 * $Id: AbstractAddressSelectorTest.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.address.selector;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jage.address.AgentAddress;
import org.jage.address.IAddress;
import org.jage.address.IAgentAddress;
import org.jage.address.node.INodeAddress;

/**
 * An abstract address selector test. Tests that the selector iterates over the same set of addresses every time after
 * initialize() is called.
 * 
 * @author AGH AgE Team
 */
@Ignore
public abstract class AbstractAddressSelectorTest {

	private static final int AGENTS_COUNT = 20;

	private static final int REPEAT_COUNT = 3;

	protected final Collection<IAgentAddress> all;

	protected final Collection<IAgentAddress> used;

	protected Collection<IAgentAddress> unused;

	protected IAgentAddress[] addresses;

	private INodeAddress nodeAddress = mock(INodeAddress.class);

	// XXX note that max number in this table cannot be greater or equal than
	// max size of all, used, unused collections
	private final int[] RANDOM_ORDER = { 1, 3, 0, 2 };

	protected Random random;

	public AbstractAddressSelectorTest() {
		this.all = new HashSet<IAgentAddress>();
		this.used = new HashSet<IAgentAddress>();
	}

	protected abstract IAddressSelector<IAgentAddress> getSelectorUnderTest();

	@Before
	public void initSets() {
		addresses = new IAgentAddress[AGENTS_COUNT];
		for (int i = 0; i < AGENTS_COUNT; i++) {
			addresses[i] = new AgentAddress(UUID.randomUUID(), nodeAddress, null);
			all.add(addresses[i]);
		}
		for (int i = 1; i < AGENTS_COUNT; i += 4) {
			used.add(addresses[i]);
		}

		// prepare unused set
		unused = new HashSet<IAgentAddress>();
		for (IAgentAddress addr : all) {
			if (!used.contains(addr)) {
				unused.add(addr);
			}
		}

		// create random mock
		random = mock(Random.class);
	}

	@Ignore
	protected <E extends IAddress> Collection<E> collectionFromIterable(Iterable<E> iterable) {
		Collection<E> ret = new HashSet<E>();
		Assert.assertNotNull("addresses() was null", iterable);
		for (E item : iterable) {
			ret.add(item);
		}
		return ret;
	}

	@Test
	public void testInitializeInLoop() {
		IAddressSelector<IAgentAddress> selectorUnderTest = getSelectorUnderTest();

		selectorUnderTest.initialize(all, used);

		Collection<IAgentAddress> firstSet = collectionFromIterable(selectorUnderTest.addresses());

		for (int repetition = 1; repetition <= REPEAT_COUNT; repetition++) {
			selectorUnderTest.initialize(all, used);
			Collection<IAgentAddress> newSet = collectionFromIterable(selectorUnderTest.addresses());

			Assert.assertTrue(String.format("set %1$s different in repetition %2$d from firstSet %3$s",
			        newSet.toString(), repetition + 1, firstSet), firstSet.equals(newSet));
		}
	}

	@Ignore
	protected void testAddressesOneOf(Collection<IAgentAddress> expected) {
		testAddressesOneOf(all, used, expected);
	}

	/**
	 * Checks if <code>expected</code> collection contains all addresses returned by
	 * {@link IAddressSelector#addresses()}.
	 * 
	 * @param all
	 * @param used
	 * @param expected
	 */
	@Ignore
	protected void testAddressesOneOf(Collection<IAgentAddress> all, Collection<IAgentAddress> used,
	        Collection<IAgentAddress> expected) {

		assertEquals(Collections.EMPTY_SET, collectionFromIterable(getSelectorUnderTest().addresses()));

		when(random.nextInt(expected.size())).thenReturn(RANDOM_ORDER[1]);

		getSelectorUnderTest().initialize(all, used);

		IAgentAddress expectedAddress = null;
		int i = 0;
		for (IAgentAddress addr : expected) {
			if (i++ == RANDOM_ORDER[1]) {
				expectedAddress = addr;
				break;
			}
		}

		i = 0;
		for (IAgentAddress addr : getSelectorUnderTest().addresses()) {
			if (i++ == 0) { // collection should have only one address
				assertEquals(expectedAddress, addr);
			} else {
				fail("Collection of addresses should have only one address");
			}
		}

		verify(random).nextInt(expected.size());
	}

	@Ignore
	protected void testAddresses(Collection<IAgentAddress> expected) {
		testAddresses(all, used, expected);
	}

	@Ignore
	protected void testAddresses(Collection<IAgentAddress> all, Collection<IAgentAddress> used,
	        Collection<IAgentAddress> expected) {

		assertEquals(Collections.EMPTY_SET, collectionFromIterable(getSelectorUnderTest().addresses()));

		getSelectorUnderTest().initialize(all, used);
		assertEquals(expected, collectionFromIterable(getSelectorUnderTest().addresses()));
	}

	@Ignore
	protected void testSelectedOneOf(Collection<IAgentAddress> expected) {
		testSelectedOneOf(all, used, expected);
	}

	/**
	 * Checks if at least one address from <code>expected</code> collection is selected.
	 * 
	 * @param all
	 * @param used
	 * @param expected
	 */
	@Ignore
	protected void testSelectedOneOf(Collection<IAgentAddress> all, Collection<IAgentAddress> used,
	        Collection<IAgentAddress> expected) {
		for (IAgentAddress addr : all) {
			assertFalse(getSelectorUnderTest().selected(addr));
		}

		when(random.nextInt(expected.size())).thenReturn(RANDOM_ORDER[0]);

		getSelectorUnderTest().initialize(all, used);

		IAgentAddress expectedAddress = null;
		int i = 0;
		for (IAgentAddress addr : expected) {
			if (i++ == RANDOM_ORDER[0]) {
				expectedAddress = addr;
				break;
			}
		}
		assertTrue(getSelectorUnderTest().selected(expectedAddress));
		verify(random).nextInt(expected.size());

	}

	@Ignore
	protected void testSelected(Collection<IAgentAddress> expected) {
		testSelected(all, used, expected);
	}

	@Ignore
	protected void testSelected(Collection<IAgentAddress> all, Collection<IAgentAddress> used,
	        Collection<IAgentAddress> expected) {

		for (IAgentAddress addr : all) {
			assertFalse(getSelectorUnderTest().selected(addr));
		}

		getSelectorUnderTest().initialize(all, used);

		for (IAgentAddress addr : all) {
			if (expected.contains(addr)) {
				assertTrue(getSelectorUnderTest().selected(addr));
			} else {
				assertFalse(getSelectorUnderTest().selected(addr));
			}
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInitializeWithNullAll() {
		getSelectorUnderTest().initialize(null, used);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInitializeWithNullUsed() {
		getSelectorUnderTest().initialize(all, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInitializeBothNull() {
		getSelectorUnderTest().initialize(null, null);
	}

	/**
	 * Checks the case when used addresses set contains at least one address which is not in all available addresses
	 * set.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInitializeWithInconsistentSets() {
		Collection<IAgentAddress> toRemove = new HashSet<IAgentAddress>();
		for (IAgentAddress addr : all) {
			if (used.contains(addr)) {
				toRemove.add(addr);
			}
		}
		all.removeAll(toRemove);
		getSelectorUnderTest().initialize(all, used);
	}

	@Test
	public void testSelectedDifferent() {
		IAgentAddress addr = new AgentAddress(UUID.randomUUID(), nodeAddress, null);
		assertFalse(getSelectorUnderTest().selected(addr));
		getSelectorUnderTest().initialize(all, used);
		assertFalse(getSelectorUnderTest().selected(addr));
	}

	@Test
	public void testSelectedNull() {
		assertFalse(getSelectorUnderTest().selected(null));
		getSelectorUnderTest().initialize(all, used);
		assertFalse(getSelectorUnderTest().selected(null));
	}
}
