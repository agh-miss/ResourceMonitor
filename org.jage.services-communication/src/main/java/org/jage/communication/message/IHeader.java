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
 * File: IHeader.java
 * Created: 2011-07-27
 * Author: faber
 * $Id: IHeader.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.communication.message;

import java.io.Serializable;

import org.jage.address.IAddress;
import org.jage.address.selector.IAddressSelector;

/**
 * Message header which contains sender and receiver addresses. The header is parametrised by the type of address.
 * 
 * @param <A>
 *            A type of sender and receiver address.
 * 
 * @author AGH AgE Team
 */
public interface IHeader<A extends IAddress> extends Serializable {

	/**
	 * Returns an address of the sender.
	 * 
	 * @return The address of the sender.
	 */
	public A getSenderAddress();

	/**
	 * Returns a selector that selects all receivers.
	 * 
	 * @return A selector for receivers.
	 */
	public IAddressSelector<A> getReceiverSelector();
}
