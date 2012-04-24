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
 * File: IAgentAddressProvider.java
 * Created: 2011-07-11
 * Author: faber
 * $Id: IAgentAddressProvider.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.address.provider;

import org.jage.address.IAgentAddress;

/**
 * A local provider for agent addresses. It generates a new, unique agent addresses on demand.
 * <p>
 * "Local" means that it should be used locally by agents, not by an environmental proxy or another component.
 * 
 * @see IAgentAddress
 * @author AGH AgE Team
 */
public interface IAgentAddressProvider {

	/**
	 * Obtains a new agent address based on a given name initialization value. The effect of this initializer on a final
	 * address depends on the implementation.
	 * 
	 * @param nameInitializer
	 *            An initialization value for an address, can be <code>null</code> or an empty string.
	 * @return A completely new and unique agent address, never <code>null</code>.
	 */
	IAgentAddress obtainAddress(String nameInitializer);

}
