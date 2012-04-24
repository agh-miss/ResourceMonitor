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
 * File: AgentSelectors.java
 * Created: 2012-04-07
 * Author: Krzywicki
 * $Id: AgentSelectors.java 213 2012-04-08 20:21:35Z krzywick $
 */

package org.jage.address.selector;

import org.jage.address.IAgentAddress;

/**
 * Utility class for {@link IAgentAddress} selectors.
 *
 * @author AGH AgE Team
 */
public class AgentSelectors {

	/**
	 * Creates a unicast selector for the given agent address.
	 *
	 * @param address
	 *            some agent address
	 * @return a unicast selector for the given address
	 */
	public static UnicastSelector<IAgentAddress> unicastFor(final IAgentAddress address) {
		return new UnicastSelector<IAgentAddress>(address);
	}

	private AgentSelectors() {
	}
}
