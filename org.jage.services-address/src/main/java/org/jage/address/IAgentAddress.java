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
 * File: IAgentAddress.java
 * Created: 2011-07-19
 * Author: faber
 * $Id: IAgentAddress.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.address;

import java.util.UUID;

/**
 * Represents an address which is unique in the system.
 * 
 * Uniqueness is based on uniqueness of age node address in the system and uniqueness rest of the address in the age
 * node address, where owner of this address was born.
 * 
 * @author AGH AgE Team
 */
public interface IAgentAddress extends IAddress {

	/**
	 * Returns a UUID representation of the address. UUID is a main, unique part of the address.
	 * 
	 * @return The id, not null.
	 */
	UUID getId();

	/**
	 * Returns a unique String representation of the address. Uniqueness is guaranteed thanks to usage of UUID in the
	 * returned string.
	 * 
	 * @return The unique address as a string, not null.
	 */
	String toUniqueString();

}
