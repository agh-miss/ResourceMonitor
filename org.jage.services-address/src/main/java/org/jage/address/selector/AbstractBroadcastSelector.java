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
 * File: AbstractBroadcastSelector.java
 * Created: 2010-03-11
 * Author: kpietak
 * $Id: AbstractBroadcastSelector.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.address.selector;

import java.util.Collection;
import java.util.Collections;

import org.jage.address.IAddress;

/**
 * An abstract class for a broadcast address selectors. All broadcast selectors choose all addresses from a specified
 * set of addresses created on base of collection of all available and unused addresses.
 * 
 * <p>
 * Before initialization, {@link addresses} returns no addresses. After initialization {@link #addresses()} iterates
 * over a list of all addresses from a chosen set (e.g. all unused addresses, all available addresses, etc.)
 * 
 * <p>
 * Multiple calls to {@link #initialize(Collection, Collection)} do not change the chosen addresses.
 * 
 * @param <AddressClass>
 *            type of address which can be selected by this selector
 * 
 * @author AGH AgE Team
 * 
 */
public abstract class AbstractBroadcastSelector<AddressClass extends IAddress> implements
        IAddressSelector<AddressClass> {

	private static final long serialVersionUID = 1568562272465487568L;

	/**
	 * Collection of selected addresses.
	 */
	protected Collection<AddressClass> resultAddresses;

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Iterable<AddressClass> addresses() {
		if (resultAddresses == null) {
			return Collections.EMPTY_SET;
		} else {
			return resultAddresses;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean selected(AddressClass address) {
		if (address == null) {
			return false;
		} else if (resultAddresses == null) {
			return true;
		} else {
			return resultAddresses.contains(address);
		}
	}

}
