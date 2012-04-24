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
 * File: AbstractBroadcastSelectorTest.java
 * Created: 2011-08-29
 * Author: faber
 * $Id: AbstractBroadcastSelectorTest.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.address.selector;

import java.util.Collection;
import java.util.UUID;

import org.junit.Ignore;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import static org.mockito.Mockito.mock;

import org.jage.address.AgentAddress;
import org.jage.address.IAgentAddress;
import org.jage.address.node.INodeAddress;

/**
 * An abstract address selector test for broadcast selectors.
 * <p>
 * The only difference from {@link AbstractAddressSelectorTest} is handling of the <code>selected()</code> method for an
 * uninitialised selector.
 * 
 * @author AGH AgE Team
 */
@Ignore
public abstract class AbstractBroadcastSelectorTest extends AbstractAddressSelectorTest {

	private INodeAddress nodeAddress = mock(INodeAddress.class);

	@Test
	@Override
	public void testSelectedDifferent() {
		IAgentAddress addr = new AgentAddress(UUID.randomUUID(), nodeAddress, null);
		assertTrue(getSelectorUnderTest().selected(addr));
		getSelectorUnderTest().initialize(all, used);
		assertFalse(getSelectorUnderTest().selected(addr));
	}

	@Override
	@Ignore
	protected void testSelected(Collection<IAgentAddress> all, Collection<IAgentAddress> used,
	        Collection<IAgentAddress> expected) {

		for (IAgentAddress addr : all) {
			assertTrue(getSelectorUnderTest().selected(addr));
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
}
