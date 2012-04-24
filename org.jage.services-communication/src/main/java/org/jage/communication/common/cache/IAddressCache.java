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
 * File: IAddressCache.java
 * Created: 2012-02-07
 * Author: faber
 * $Id: IAddressCache.java 134 2012-03-18 20:17:01Z faber $
 */

package org.jage.communication.common.cache;

import java.util.Collection;

import org.jage.address.node.IComponentAddress;

/**
 * A model object containing node addresses of local node's neighbours.
 * <p>
 * It's a mediator standing between an INeighbourhoodScanner and an ICommunicationManager. Scanner fills the
 * IAddressCache with addresses which a service will use each time it's initializing a selector.
 * <p>
 * It's assumed that there's only one INeighbourhoodScanner per node and that the scanner is the only component who's
 * filling the cache with addresses.
 * <p>
 * Sends events to interested listeners each time the underlying database changes.
 * 
 * @author AGH AgE Team
 */
public interface IAddressCache {

	/**
	 * Returns all addresses in the cache but represented as component addresses of communication services for nodes.
	 * 
	 * @return addresses of all the known nodes; be aware that the component id included in each of those can make no
	 *         sense and you should provide your own if necessary.
	 */
	Collection<IComponentAddress> getAddressesOfRemoteNodes(); // FIXME: The name lies. [LF]

	/**
	 * Adds a listener which will be notified each time the underlying database of addresses changes.
	 * 
	 * @param listener
	 *            the listener to add.
	 */
	void addCacheModificationListener(ICacheModificationListener listener);

	/**
	 * Returns a count of currently known foreign nodes.
	 * 
	 * @return a count of currently known foreign nodes.
	 */
	int getNodesCount();

}
