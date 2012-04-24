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
 * File: AddressSetTest.java
 * Created: 2012-02-07
 * Author: faber
 * $Id: AddressSetTest.java 66 2012-02-15 14:15:24Z faber $
 */

package org.jage.communication.common.cache;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.jage.address.node.INodeAddress;

import com.google.common.collect.Lists;

/**
 * Tests for the {@link AddressSet} class.
 * 
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class AddressSetTest {

	private AddressSet addressSet;

	@Before
	public void setup() {
		addressSet = new AddressSet(1, 1);
	}

	@Test
	public void testAddAddress() {
		// given
		INodeAddress nodeAddress = mock(INodeAddress.class);

		// when
		addressSet.addAddress(nodeAddress);

		// then
		assertThat(addressSet.getAddressesOfRemoteNodes().size(), equalTo(1));
	}

	@Test
	public void testAddAddressBehavesLikeSet() {
		// given
		INodeAddress nodeAddress = mock(INodeAddress.class);

		// when
		addressSet.addAddress(nodeAddress);
		addressSet.addAddress(nodeAddress);

		// then
		assertThat(addressSet.getAddressesOfRemoteNodes().size(), equalTo(1));
	}

	@Test
	public void testRemoveAddress() {
		// given
		INodeAddress nodeAddress = mock(INodeAddress.class);
		addressSet.addAddress(nodeAddress);

		// when
		addressSet.removeAddress(nodeAddress);

		// then
		assertThat(addressSet.getAddressesOfRemoteNodes().size(), equalTo(0));
	}

	@Test
	public void testRemoveAllAddresses() {
		// given
		List<INodeAddress> listOfAddresses = Lists.newArrayList();
		for (int i = 0; i < 10; i++) {
			INodeAddress nodeAddress = mock(INodeAddress.class);
			listOfAddresses.add(nodeAddress);
			addressSet.addAddress(nodeAddress);
		}

		// when
		addressSet.removeAllAddresses();

		// then
		assertThat(addressSet.getAddressesOfRemoteNodes().size(), equalTo(0));
	}

	@Test
	public void testAddCacheModificationListener() {
		// given
		INodeAddress nodeAddress = mock(INodeAddress.class);
		ICacheModificationListener cacheModificationListener = mock(ICacheModificationListener.class);

		// when
		addressSet.addCacheModificationListener(cacheModificationListener);
		addressSet.addAddress(nodeAddress);
		addressSet.removeAddress(nodeAddress);
		addressSet.removeAllAddresses();

		// then
		verify(cacheModificationListener, times(3)).onCacheChanged();
	}

	@Test(timeout = 4000)
	public void testTimeoutingEntries() throws InterruptedException {
		// given
		INodeAddress nodeAddress = mock(INodeAddress.class);
		ICacheModificationListener cacheModificationListener = mock(ICacheModificationListener.class);
		addressSet.init();

		// when
		addressSet.addCacheModificationListener(cacheModificationListener);
		addressSet.addAddress(nodeAddress);

		while (addressSet.getAddressesOfRemoteNodes().size() > 0) {
			Thread.sleep(500);
		}

		// then
		addressSet.finish();
		verify(cacheModificationListener, atLeastOnce()).onCacheChanged();
		assertThat(addressSet.getAddressesOfRemoteNodes().size(), equalTo(0));
	}

}
