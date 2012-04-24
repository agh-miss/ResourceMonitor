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
 * File: MultiUsedSelector.java
 * Created: 2009-03-09
 * Author: awos
 * $Id: MultiUsedSelector.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.address.selector;

import java.util.Collection;

import org.jage.address.IAddress;

/**
 * A multicast selector that selects a specified number of random addresses from the list of used addresses.
 * 
 * @param <AddressClass>
 *            type of an address which can be selected by this selector
 * 
 * @author AGH AgE Team
 */
public class MultiUsedSelector<AddressClass extends IAddress> extends AbstractMulticastSelector<AddressClass> {

	private static final long serialVersionUID = 4209807801308643692L;

	/**
	 * Construct the selector by specifying the number of addresses to select.
	 * 
	 * @param numberOfAddresses
	 *            the number of addresses to be selected
	 */
	public MultiUsedSelector(int numberOfAddresses) {
		super(numberOfAddresses);
	}

	@Override
	public void initialize(Collection<AddressClass> allAddresses, Collection<AddressClass> usedAddresses) {
		if (allAddresses == null || usedAddresses == null) {
			throw new IllegalArgumentException("Cannot initialize selector with null sets of addresses");
		}

		SelectorHelper.checkSetsInconsistence(allAddresses, usedAddresses);

		initialiseFromCollection(usedAddresses);
	}

}
