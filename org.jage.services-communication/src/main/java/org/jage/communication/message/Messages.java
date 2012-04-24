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
 * File: Messages.java
 * Created: 2012-02-08
 * Author: faber
 * $Id: Messages.java 134 2012-03-18 20:17:01Z faber $
 */

package org.jage.communication.message;

import java.io.Serializable;
import java.util.NoSuchElementException;

import static java.lang.String.format;

import org.jage.address.IAddress;

import com.google.common.collect.Iterables;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Message-related utilities.
 * 
 * @author AGH AgE Team
 */
public final class Messages {

	/**
	 * Returns the address of the only receiver selected by the selector in the provided message.
	 * 
	 * @param message
	 *            a message to extract receiver from.
	 * @param <A>
	 *            a type of addresses.
	 * @return An extracted receiver.
	 * 
	 * @throws NoSuchElementException
	 *             if there is no selected receiver.
	 * @throws IllegalArgumentException
	 *             if the selector selects multiple addresses.
	 */
	public static <A extends IAddress> A getOnlyReceiverAddress(IMessage<A, ?> message) {
		checkNotNull(message);
		return Iterables.getOnlyElement(message.getHeader().getReceiverSelector().addresses());
	}

	/**
	 * Returns the payload carried by a message if it is of a given type. Otherwise, it throws an exception.
	 * 
	 * @param message
	 *            a message.
	 * @param klass
	 *            a supposed class of the payload.
	 * @return the message payload.
	 * @throws IllegalArgumentException
	 *             if the payload is of incorrect type.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T getPayloadOfTypeOrThrow(IMessage<? extends IAddress, ? super T> message,
	        Class<T> klass) {
		checkNotNull(message);
		checkNotNull(klass);

		if (!klass.isAssignableFrom(message.getPayload().getClass())) {
			throw new IllegalArgumentException(format(
			        "Message payload has incorrect type. %s was expected but %s was received.", klass.getClass(),
			        message.getPayload().getClass()));
		}
		return (T)message.getPayload();
	}
}
