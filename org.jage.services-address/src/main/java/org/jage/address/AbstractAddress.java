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
 * File: AbstractAddress.java
 * Created: 2011-07-19
 * Author: faber
 * $Id: AbstractAddress.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.address;

import org.jage.address.node.INodeAddress;

/**
 * This class provides a standard implementation of basic methods of addresses.
 * 
 * @author AGH AgE Team
 */
public abstract class AbstractAddress implements IAddress {

	private static final long serialVersionUID = 1L;

	private static final String SEPARATOR = "@";

	private String userFriendlyName;

	private INodeAddress nodeAddress;

	/**
	 * Creates a new address.
	 * 
	 * @param nodeAddress
	 *            The current node address, cannot be null.
	 * @param userFriendlyName
	 *            A user friendly name to use.
	 */
	public AbstractAddress(INodeAddress nodeAddress, String userFriendlyName) {
		if (nodeAddress == null) {
			throw new IllegalArgumentException("A node address cannot be null.");
		}
		this.nodeAddress = nodeAddress;
		this.userFriendlyName = userFriendlyName;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation returns a string of form <code>userFriendlyName@nodeAddress</code>.
	 * 
	 * @see org.jage.address.IAddress#getUserFriendlyAddress()
	 */
	@Override
	public String toString() {
		return getName() + SEPARATOR + getNodeAddress();
	}

	@Override
	public String getName() {
		return userFriendlyName;
	}

	@Override
	public INodeAddress getNodeAddress() {
		return nodeAddress;
	}

}
