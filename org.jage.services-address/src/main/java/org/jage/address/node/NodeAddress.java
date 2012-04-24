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
 * File: NodeAddress.java
 * Created: 2011-07-19
 * Author: faber
 * $Id: NodeAddress.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.address.node;

/**
 * A default implementation of a node address.
 * 
 * @author AGH AgE Team
 */
public class NodeAddress implements INodeAddress {

	private static final long serialVersionUID = 3L;

	private static final String SEPARATOR = "@";

	private String localPart;

	private String hostname;

	/**
	 * Constructs a new <code>NodeAddress</code> consisting of a local part and a hostname.
	 * 
	 * @param localPart
	 *            A name identifying this node in the <code>hostname</code>, cannot be <code>null</code>.
	 * @param hostname
	 *            A name of the host the node is located in.
	 *            
	 * @throws IllegalArgumentException If <code>localPart</code> is <code>null</code>.
	 */
	public NodeAddress(String localPart, String hostname) {
		if (localPart == null) {
			throw new IllegalArgumentException();
		}
		this.localPart = localPart;
		this.hostname = hostname;
	}

	@Override
	public String toString() {
		return localPart + SEPARATOR + hostname;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hostname == null) ? 0 : hostname.hashCode());
		result = prime * result + ((localPart == null) ? 0 : localPart.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		NodeAddress other = (NodeAddress)obj;
		if (hostname == null) {
			if (other.hostname != null) {
				return false;
			}
		} else if (!hostname.equals(other.hostname)) {
			return false;
		}
		if (localPart == null) {
			if (other.localPart != null) {
				return false;
			}
		} else if (!localPart.equals(other.localPart)) {
			return false;
		}
		return true;
	}
}
