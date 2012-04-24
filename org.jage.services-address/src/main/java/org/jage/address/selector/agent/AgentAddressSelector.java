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
 * File: AgentAddressSelector.java
 * Created: 2009-03-11
 * Author: awos
 * $Id: AgentAddressSelector.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.address.selector.agent;

import org.jage.address.IAgentAddress;
import org.jage.address.selector.UnicastSelector;

/**
 * A {@link UnicastSelector} for an {@link IAgentAddress}. Present for convenience only.
 * 
 * @author AGH AgE Team
 */
public final class AgentAddressSelector extends UnicastSelector<IAgentAddress> {

	private static final long serialVersionUID = -4278275788478772206L;

	/**
	 * Default constructor.
	 * 
	 * @param address
	 *            agent address to be selected
	 */
	public AgentAddressSelector(IAgentAddress address) {
		super(address);
	}
}
