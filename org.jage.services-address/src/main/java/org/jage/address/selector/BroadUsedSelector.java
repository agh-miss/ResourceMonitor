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
 * File: BroadUsedSelector.java
 * Created: 2009-03-11
 * Author: kpietak
 * $Id: BroadUsedSelector.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.address.selector;

import java.util.Collection;

import org.jage.address.IAddress;

/**
 * Broadcast selector which selects all addresses from set of used addresses specified in
 * {@link #initialize(Collection, Collection)} method.
 * 
 * @param <AddressClass>
 *            type of address which can be selected by this selector
 * 
 * @author AGH AgE Team
 * 
 */
public class BroadUsedSelector<AddressClass extends IAddress> extends AbstractBroadcastSelector<AddressClass> {

	/**
     * 
     */
	private static final long serialVersionUID = -5503568458806554389L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize(Collection<AddressClass> allAddresses, Collection<AddressClass> usedAddresses) {
		if (allAddresses == null || usedAddresses == null) {
			throw new IllegalArgumentException("Cannot initialize selector with null sets of addresses");
		}

		SelectorHelper.checkSetsInconsistence(allAddresses, usedAddresses);

		this.resultAddresses = usedAddresses;
	}

}
