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
 * File: ParentAgentAddressSelector.java
 * Created: 2011-07-19
 * Author: faber
 * $Id: ParentAgentAddressSelector.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.address.selector.agent;

import java.util.Collection;
import java.util.Collections;

import org.jage.address.IAgentAddress;
import org.jage.address.selector.IAddressSelector;

/**
 * This class provides the address selector used for choosing a parent agent.
 * <p>
 * This selector works a little not intuitively. It does not select any addresses, it is an aggregate that needs to check
 * if it is selected. An aggregate is selected if an address returned by {@link #addresses} method is one of its children
 * addresses.
 * 
 * @author AGH AgE Team
 */
public class ParentAgentAddressSelector implements IAddressSelector<IAgentAddress> {

	private static final long serialVersionUID = -720158404815435693L;

	private IAgentAddress childAddress;

	/**
	 * Constructs a new selector that selects a parent of the child with the provided address.
	 * 
	 * @param childAddress
	 *            An address of <b>the child</b>.
	 */
	public ParentAgentAddressSelector(IAgentAddress childAddress) {
		this.childAddress = childAddress;
	}

	public IAgentAddress getChildAddress() {
		return childAddress;
	}

	@Override
	public Iterable<IAgentAddress> addresses() {
		return Collections.singleton(childAddress);
	}

	@Override
	public void initialize(Collection<IAgentAddress> allAddresses, Collection<IAgentAddress> usedAddresses) {
		// Empty
	}

	@Override
	public boolean selected(IAgentAddress address) {
		return false;
	}

}
