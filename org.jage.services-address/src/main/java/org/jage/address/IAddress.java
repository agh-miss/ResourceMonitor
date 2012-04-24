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
 * File: IAddress.java
 * Created: 2011-07-19
 * Author: faber
 * $Id: IAddress.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.address;

import java.io.Serializable;

import org.jage.address.node.INodeAddress;

/**
 * A generic type of all addresses used in a single jAgE node.
 * <p>
 * An address consists of two elements:
 * <ul>
 * <li>a name dependent on the type of the address,
 * <li>a node address.
 * </ul>
 * 
 * @author AGH AgE Team
 */
public interface IAddress extends Serializable {

	/**
	 * Provides a user friendly name included in this address.
	 * <p>
	 * A "name" is the first part of the address - it does not contain the node address.
	 * <p>
	 * This user friendly name does not have to be unique.
	 * 
	 * @return A user friendly name, not null.
	 */
	String getName();

	/**
	 * Provides a standardised string representation of the address. It does not have to be unique.
	 * 
	 * @return A user friendly form of the address, not null.
	 * @see #getName()
	 */
	@Override
    String toString();

	/**
	 * Returns an address of the node on which this address was created.
	 * 
	 * @return The node address, not null.
	 */
	INodeAddress getNodeAddress();

}
