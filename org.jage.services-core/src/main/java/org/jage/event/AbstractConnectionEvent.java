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
 * File: AbstractConnectionEvent.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: AbstractConnectionEvent.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.event;

import org.jage.address.IAgentAddress;

/**
 * This is an abstraction of connection events.
 * 
 * @param <A>
 *            an agent address
 * @author AGH AgE Team
 */
public abstract class AbstractConnectionEvent<A extends IAgentAddress> extends AbstractEvent {
	/**
	 * The address of the local connector which was involved in this event.
	 */
	private final A localAddress;

	/**
	 * The address of the remote connector which was involved in this event.
	 */
	private final A remoteAddress;

	/**
	 * Constructor.
	 * 
	 * @param localAddress
	 *            an address of the object which was involed in this event or <code>null</code> if unknown.
	 * @param remoteAddress
	 *            an address of the remote object which was involved in this event or <code>null</code> if unknown.
	 */
	protected AbstractConnectionEvent(A localAddress, A remoteAddress) {
		this.localAddress = localAddress;
		this.remoteAddress = remoteAddress;
	}

	/**
	 * Returns the address of the local object which was involved in this event.
	 * 
	 * @return the address or <code>null</code> if unknown
	 */
	public A getLocalAddress() {
		return localAddress;
	}

	/**
	 * Returns the address of the remote object which was involved in this event.
	 * 
	 * @return the address or <code>null</code> if unknown
	 */
	public A getRemoteAddress() {
		return remoteAddress;
	}

	@Override
	public String toString() {
		return "Connection event [remoteAddress: " + remoteAddress + "]";
	}
}
