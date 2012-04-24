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
 * File: UnicastSelector.java
 * Created: 2009-03-09
 * Author: awos
 * $Id: UnicastSelector.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.address.selector;

import java.util.Collection;
import java.util.Collections;

import org.jage.address.IAddress;

/**
 * The most simple address selector. Always returns the address given in {@link #UnicastSelector(IAddress)}.
 * Initialization of this selector doesn't change its state.
 * 
 * @param <AddressClass>
 *            type of an address which can be selected by this selector
 * @author AGH AgE Team
 */
public class UnicastSelector<AddressClass extends IAddress> implements IAddressSelector<AddressClass> {

	/**
     * 
     */
	private static final long serialVersionUID = 5814016849899074368L;

	private AddressClass address;

	/**
	 * The only constructor. Wraps the provided address inside a {@link UnicastSelector}.
	 * 
	 * @param address
	 *            address to select
	 */
	public UnicastSelector(AddressClass address) {
		if (null == address) {
			throw new IllegalArgumentException();
		}
		this.address = address;
	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.jage.address.selector.IAddressSelector#addresses()
	 */
	@Override
	public Iterable<AddressClass> addresses() {
		return Collections.singleton(address);
	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.jage.address.selector.IAddressSelector#initialize(java.util.Collection, java.util.Collection)
	 */
	@Override
	public void initialize(Collection<AddressClass> allAddresses, Collection<AddressClass> usedAddresses) {
		// do nothing
	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.jage.address.selector.IAddressSelector#selected(org.jage.address.IAddress)
	 */
	@Override
	public boolean selected(AddressClass address) {
		return this.address.equals(address);
	}
}
