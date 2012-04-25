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
 * File: IMutableAddressCache.java
 * Created: 2012-02-07
 * Author: faber
 * $Id: IMutableAddressCache.java 58 2012-02-10 14:25:54Z faber $
 */

package org.jage.communication.common.cache;

import org.jage.address.node.INodeAddress;

/**
 * A mutable IAddressCache. Allows its users to remove addresses from the database.
 * 
 * @author AGH AgE Team
 */
public interface IMutableAddressCache extends IAddressCache {

	/**
	 * Removes an address from the database.
	 * 
	 * @param nodeAddress
	 *            a node's address to remove
	 */
	public void removeAddress(INodeAddress nodeAddress);

	/**
	 * Removes all elements from cache.
	 */
	public void removeAllAddresses();

}