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
 * File: AgentAddress.java
 * Created: 2011-07-19
 * Author: faber
 * $Id: AgentAddress.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.address;

import java.util.UUID;

import org.jage.address.node.INodeAddress;

/**
 * This class provides a default implementation of the agent address.
 * 
 * @author AGH AgE Team
 */
public class AgentAddress extends AbstractAddress implements IAgentAddress {

	private static final long serialVersionUID = 3L;

	private static final String SEPARATOR = "@";

	private UUID id;

	/**
	 * Constructs a new agent address with given parameters.
	 * 
	 * @param id
	 *            A UUID that identifies this address, cannot be null.
	 * @param nodeAddress
	 *            A current node address, cannot be null.
	 * @param userFriendlyName
	 *            A user friendly name of an agent.
	 * 
	 * @throws IllegalArgumentException
	 *             if either <code>id</code> or <code>nodeAddress</code> is <code>null</code>.
	 */
	public AgentAddress(UUID id, INodeAddress nodeAddress, String userFriendlyName) {
		super(nodeAddress, userFriendlyName);
		if (id == null) {
			throw new IllegalArgumentException("Id cannot be null.");
		}
		this.id = id;
	}

	@Override
	public String toUniqueString() {
		return getId().toString() + SEPARATOR + getNodeAddress();
	}

	@Override
	public UUID getId() {
		return id;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation uses a stringified version of the UUID if the name provided in the constructor was
	 * <code>null</code>.
	 * 
	 * @see #getId()
	 */
	@Override
	public String getName() {
		String name = super.getName();
		if (name == null) {
			return id.toString();
		}
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((getNodeAddress() == null) ? 0 : getNodeAddress().hashCode());
		return result;
	}

	/**
	 * Tests whether this <code>AgentAddress</code> is equal to another.
	 * <p>
	 * {@inheritDoc}
	 * <p>
	 * This implementation is based on equality of <code>id</code> and the attached node address.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
		AgentAddress other = (AgentAddress)obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (getNodeAddress() == null) {
			if (other.getNodeAddress() != null) {
				return false;
			}
		} else if (!getNodeAddress().equals(other.getNodeAddress())) {
			return false;
		}
		return true;
	}

}
