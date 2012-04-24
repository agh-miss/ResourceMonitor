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
 * File: MulticastSelector.java
 * Created: 2009-03-10
 * Author: awos
 * $Id: MulticastSelector.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.address.selector;

import java.util.Collection;

import org.jage.address.IAddress;

/**
 * The simplest multicast selector, which selects a specified number of addresses from all available addresses.
 * 
 * @param <AddressClass>
 *            type of address which can be selected by this selector
 * 
 * @author AGH AgE Team
 */
public class MulticastSelector<AddressClass extends IAddress> extends AbstractMulticastSelector<AddressClass> {

	private static final long serialVersionUID = -6759133584407548130L;

	/**
	 * Construct the selector by specifying the number of addresses to select.
	 * 
	 * @param numberOfAddresses
	 *            the number of addresses to be selected
	 */
	public MulticastSelector(int numberOfAddresses) {
		super(numberOfAddresses);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize(Collection<AddressClass> allAddresses, Collection<AddressClass> usedAddresses) {
		if (allAddresses == null || usedAddresses == null) {
			throw new IllegalArgumentException("Cannot initialize MulticastSelector with null addresses sets.");
		}

		initialiseFromCollection(allAddresses);
	}

}
