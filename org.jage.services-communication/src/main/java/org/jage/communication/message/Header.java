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
 * File: Header.java
 * Created: 2011-07-27
 * Author: faber
 * $Id: Header.java 134 2012-03-18 20:17:01Z faber $
 */

package org.jage.communication.message;

import org.jage.address.IAddress;
import org.jage.address.selector.IAddressSelector;

/**
 * This class provides a default implementation of {@link IHeader}.
 * 
 * @param <A>
 *            A type of sender and receiver address.
 * @author AGH AgE Team
 */
public class Header<A extends IAddress> implements IHeader<A> {

	private static final long serialVersionUID = 1L;

	private IAddressSelector<A> receiverSelector;

	private A senderAddress;

	/**
	 * Constructs a new header specifying a sender and a selector for receivers.
	 * 
	 * @param senderAddress
	 *            An address of the sender of the message.
	 * @param receiverSelector
	 *            A selector for selecting receivers.
	 */
	public Header(A senderAddress, IAddressSelector<A> receiverSelector) {
		this.senderAddress = senderAddress;
		this.receiverSelector = receiverSelector;
	}

	@Override
	public A getSenderAddress() {
		return senderAddress;
	}

	@Override
	public IAddressSelector<A> getReceiverSelector() {
		return receiverSelector;
	}

	@Override
	public String toString() {
		return String.format("Message header[sender=%s, receivers=%s]", getSenderAddress(), getReceiverSelector());
	}
}
