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
 * File: AbstractTransferEvent.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: AbstractTransferEvent.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.event;

import org.jage.address.IAgentAddress;

/**
 * This is abstract class for transfer events.
 * 
 * @param <A>
 *            an agent address
 * @author AGH AgE Team
 */
public abstract class AbstractTransferEvent<A extends IAgentAddress> extends AbstractConnectionEvent<A> {

	/**
	 * The object that was being transferred.
	 */
	private final Object transferredObject;

	/**
	 * Constructor.
	 * 
	 * @param localAddress
	 *            an address of an object which was involed in this event or <code>null</code> if unknown.
	 * @param remoteAddress
	 *            an address of an object which was involved in this event or <code>null</code> if unknown.
	 * @param transferredObject
	 *            object that is transferred
	 */
	protected AbstractTransferEvent(A localAddress, A remoteAddress, Object transferredObject) {
		super(localAddress, remoteAddress);
		this.transferredObject = transferredObject;
	}

	/**
	 * Returns the object that was being transferred.
	 * 
	 * @return the object
	 */
	public Object getTransferredObject() {
		return transferredObject;
	}

	@Override
	public String toString() {
		return "Transfer event [localAddress: " + getLocalAddress() + "; remoteAddress: " + getRemoteAddress()
		        + "; transferredObject: " + transferredObject + "]";
	}
}
