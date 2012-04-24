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
 * File: MultiUnusedSelectorTest.java
 * Created: 2011-08-29
 * Author: faber
 * $Id: MultiUnusedSelectorTest.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.address.selector;

import java.util.Collection;
import java.util.Collections;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import org.jage.address.IAgentAddress;

/**
 * Tests for {@link MultiUnusedSelector} class.
 * 
 * @author AGH AgE Team
 */
public class MultiUnusedSelectorTest extends AbstractAddressSelectorTest {

	private MultiUnusedSelector<IAgentAddress> selector;

	private int addressCount;

	@Before
	public void setUp() {
		addressCount = unused.size() / 2;
		selector = new MultiUnusedSelector<IAgentAddress>(addressCount);
	}

	@Override
	protected IAddressSelector<IAgentAddress> getSelectorUnderTest() {
		return selector;
	}

	/**
	 * Tests addresses() method in the standard, correct situation.
	 */
	@Test
	public void testAddresses() {
		assertEquals(Collections.EMPTY_SET, collectionFromIterable(getSelectorUnderTest().addresses()));

		getSelectorUnderTest().initialize(all, used);

		Collection<IAgentAddress> selectedAddresses = collectionFromIterable(getSelectorUnderTest().addresses());
		assertEquals(addressCount, selectedAddresses.size());

		for (IAgentAddress agentAddress : selectedAddresses) {
			assertTrue(unused.contains(agentAddress));
			assertFalse(used.contains(agentAddress));
		}
	}

	/**
	 * Tests selected() method in the standard, correct situation.
	 */
	@Test
	public void testSelected() {
		for (IAgentAddress addr : all) {
			assertFalse(getSelectorUnderTest().selected(addr));
		}

		getSelectorUnderTest().initialize(all, used);

		int selectedCount = 0;
		for (IAgentAddress addr : unused) {
			if (getSelectorUnderTest().selected(addr)) {
				selectedCount++;
			}
		}
		assertEquals(addressCount, selectedCount);

		for (IAgentAddress addr : used) {
			assertFalse(getSelectorUnderTest().selected(addr));
		}
	}

	/**
	 * This test is ignored because consistency is not required.
	 */
	@Override
	@Ignore
	@Test
	public void testInitializeWithInconsistentSets() {
		// Empty
	}
}
