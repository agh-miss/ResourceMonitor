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
 * File: AddressMapTest.java
 * Created: 2012-02-07
 * Author: faber
 * $Id: AddressMapTest.java 66 2012-02-15 14:15:24Z faber $
 */

package org.jage.communication.common.cache;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.jage.address.node.INodeAddress;

import com.google.common.collect.Lists;

/**
 * Tests for the {@link AddressMap} class.
 * 
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class AddressMapTest {

	private AddressMap addressMap;

	@Before
	public void setup() {
		addressMap = new AddressMap(1, 1);
	}

	@Test
	public void testAddAddressMapping() {
		// given
		INodeAddress nodeAddress = mock(INodeAddress.class);
		String physicalAddress = "physicalAddress";

		// when
		addressMap.addAddressMapping(nodeAddress, physicalAddress);

		// then
		assertThat(addressMap.getPhysicalAddressFor(nodeAddress), equalTo(physicalAddress));
	}

	@Test
	public void testRemoveAddress() {
		// given
		INodeAddress nodeAddress = mock(INodeAddress.class);
		String physicalAddress = "physicalAddress";

		// when
		addressMap.addAddressMapping(nodeAddress, physicalAddress);
		addressMap.removeAddress(nodeAddress);

		// then
		assertThat(addressMap.getPhysicalAddressFor(nodeAddress), is(nullValue()));
	}

	@Test
	public void testRemoveAllAddresses() {
		// given
		List<INodeAddress> listOfAddresses = Lists.newArrayList();
		for (int i = 0; i < 10; i++) {
			INodeAddress nodeAddress = mock(INodeAddress.class);
			listOfAddresses.add(nodeAddress);
			addressMap.addAddressMapping(nodeAddress, "physicalAddress" + i);
		}

		// when
		addressMap.removeAllAddresses();

		// then
		for (INodeAddress nodeAddress : listOfAddresses) {
			assertThat(addressMap.getPhysicalAddressFor(nodeAddress), is(nullValue()));
		}
	}

	@Test
	public void testCacheModificationListeners() {
		// given
		INodeAddress nodeAddress = mock(INodeAddress.class);
		String physicalAddress = "physicalAddress";
		ICacheModificationListener cacheModificationListener = mock(ICacheModificationListener.class);

		// when
		addressMap.addCacheModificationListener(cacheModificationListener);
		addressMap.addAddressMapping(nodeAddress, physicalAddress);
		addressMap.removeAddress(nodeAddress);
		addressMap.removeAllAddresses();

		// then
		verify(cacheModificationListener, times(3)).onCacheChanged();
	}

	@Test(timeout = 4000)
	public void testTimeoutingEntries() throws InterruptedException {
		// given
		INodeAddress nodeAddress = mock(INodeAddress.class);
		String physicalAddress = "physicalAddress";
		ICacheModificationListener cacheModificationListener = mock(ICacheModificationListener.class);
		addressMap.init();

		// when
		addressMap.addCacheModificationListener(cacheModificationListener);
		addressMap.addAddressMapping(nodeAddress, physicalAddress);

		while (addressMap.getAddressesOfRemoteNodes().size() > 0) {
			Thread.sleep(500);
		}

		// then
		addressMap.finish();
		verify(cacheModificationListener, atLeastOnce()).onCacheChanged();
		assertThat(addressMap.getAddressesOfRemoteNodes().size(), equalTo(0));
	}

}
