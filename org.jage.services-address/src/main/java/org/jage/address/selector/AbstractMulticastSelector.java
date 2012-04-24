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
 * File: AbstractMulticastSelector.java
 * Created: 2009-03-10
 * Author: awos
 * $Id: AbstractMulticastSelector.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.address.selector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.jage.address.IAddress;

/**
 * An abstract implementation of a multicast selector. All multicast selectors choose multiple random addresses (hence
 * the "multi" part of the name) from provided lists of available and used addresses.
 * 
 * <p>
 * Before initialization, this selector returns no addresses in {@link #addresses()}. After initialization,
 * {@link #addresses()} iterates over a list of chosen addresses (which may be chosen randomly or deterministically).
 * 
 * <p>
 * Multiple calls to {@link #initialize(Collection, Collection)} do not change the chosen addresses.
 * 
 * @param <AddressClass>
 *            type of address which can be selected by this selector
 * 
 * @author AGH AgE Team
 */
public abstract class AbstractMulticastSelector<AddressClass extends IAddress> implements
        IAddressSelector<AddressClass> {

	private static final long serialVersionUID = -5136767741362764131L;

	final protected int numberOfAddresses;

	protected Collection<AddressClass> resultAddresses;

	/**
	 * Construct the selector by specifying the number of addresses to select.
	 * 
	 * @param numberOfAddresses
	 *            the number of addresses to be selected
	 */
	public AbstractMulticastSelector(int numberOfAddresses) {
		this.numberOfAddresses = numberOfAddresses;
	}

	@Override
	public Iterable<AddressClass> addresses() {
		if (resultAddresses == null) {
			return Collections.emptySet();
		}
		return Collections.unmodifiableCollection(resultAddresses);
	}

	@Override
	public boolean selected(AddressClass address) {
		if (resultAddresses == null || address == null) {
			return false;
		}
		return resultAddresses.contains(address);
	}

	protected void initialiseFromCollection(Collection<AddressClass> addresses) {
		resultAddresses = new ArrayList<AddressClass>(numberOfAddresses);
		int i = 0;
		for (AddressClass address : addresses) {
			resultAddresses.add(address);
			if (++i >= numberOfAddresses) {
				break;
			}
		}
	}

}
