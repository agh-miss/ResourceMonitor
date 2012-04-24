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
 * File: BroadUnusedSelectorTest.java
 * Created: 2009-03-12
 * Author: kpietak
 * $Id: BroadUnusedSelectorTest.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.address.selector;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import org.jage.address.IAgentAddress;

/**
 * Tests for {@link BroadUnusedSelector} class.
 * 
 * @author AGH AgE Team
 */
public class BroadUnusedSelectorTest extends AbstractBroadcastSelectorTest {

	private BroadUnusedSelector<IAgentAddress> selector;

	@Before
	public void setUp() {
		selector = new BroadUnusedSelector<IAgentAddress>();
	}

	@Override
	protected IAddressSelector<IAgentAddress> getSelectorUnderTest() {
		return selector;
	}

	/*---------------------------------------------------------------------
	 *			Main stream
	 *---------------------------------------------------------------------*/
	@Test
	public void testAddresses() {
		testAddresses(unused);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testAddressesWithEmptyUsed() {
		testAddresses(all, Collections.EMPTY_SET, all);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testAddressesWithAllUsed() {
		testAddresses(all, all, Collections.EMPTY_SET);
	}

	@Test
	public void testSelected() {
		testSelected(unused);
	}

}
