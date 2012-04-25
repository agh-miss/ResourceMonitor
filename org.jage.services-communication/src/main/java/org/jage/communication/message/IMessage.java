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
 * File: IMessage.java
 * Created: 2011-07-27
 * Author: faber
 * $Id: IMessage.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.communication.message;

import java.io.Serializable;

import org.jage.address.IAddress;

/**
 * Base interface for messages that are used by the system. It is comprised of a header ({@link IHeader}) and content
 * which can be any object.
 * 
 * @param <A>
 *            An address type of a sender and a receiver
 * @param <T>
 *            A type of the payload transported within this message.
 * 
 * @author AGH AgE Team
 */
public interface IMessage<A extends IAddress, T extends Serializable> extends Serializable {

	/**
	 * Returns a message header.
	 * 
	 * @return A message header, never null.
	 */
	IHeader<A> getHeader();

	/**
	 * Returns message contents.
	 * 
	 * @return A message contents.
	 */
	T getPayload();
}