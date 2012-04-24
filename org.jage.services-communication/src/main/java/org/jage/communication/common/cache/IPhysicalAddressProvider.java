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
 * File: IPhysicalAddressProvider.java
 * Created: 2012-02-07
 * Author: faber
 * $Id: IPhysicalAddressProvider.java 58 2012-02-10 14:25:54Z faber $
 */

package org.jage.communication.common.cache;

/**
 * In every environment that uses an IAddressMap, there should be an IPhysicalAddressProvider available. In such an
 * environment, the neighbourhood scanner will need to know it's node and physical address in order to announce those to
 * other scanner's in the neighbourhood.
 * <p>
 * While it can always get the node address from AgE's IAddressProvider, it might be unable to obtain the physical
 * address because it may be, for example, managed by the ICommunicationProtocol.
 * <p>
 * That's why an INeighbourhoodScanner will always learn the physical address from an IPhysicalAddressProvider. Every
 * environment with a map should have exactly one such provider.
 * 
 * @author AGH AgE Team
 */
public interface IPhysicalAddressProvider {

	/**
	 * Returns the physical address (e.g. IP) of the node.
	 * 
	 * @return the local physical communication related address.
	 */
	public String getLocalPhysicalAddress();

}
