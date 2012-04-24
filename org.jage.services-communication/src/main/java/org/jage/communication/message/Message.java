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
 * File: Message.java
 * Created: 2011-07-27
 * Author: faber
 * $Id: Message.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.communication.message;

import java.io.Serializable;

import org.jage.address.IAddress;

/**
 * This class provides a default implementation of {@link IMessage}.
 * 
 * @param <A>
 *            An address type of a sender and a receiver
 * @param <T>
 *            A type of the payload transported within this message.
 * @author AGH AgE Team
 */
public class Message<A extends IAddress, T extends Serializable> implements IMessage<A, T> {

	private static final long serialVersionUID = 2037459335733049533L;

	private final IHeader<A> header;

	private final T payload;

	/**
	 * Constructs a new message with the given header and the payload.
	 * 
	 * @param header
	 *            A header that contains metadata for this message.
	 * @param payload
	 *            A payload to transport.
	 */
	public Message(IHeader<A> header, T payload) {
		this.header = header;
		this.payload = payload;
	}

	@Override
	public IHeader<A> getHeader() {
		return header;
	}

	@Override
	public T getPayload() {
		return payload;
	}

	@Override
	public String toString() {
		return String.format("Message[header=%s, payloadClass=%s]", getHeader(), getPayload().getClass());
	}
}
