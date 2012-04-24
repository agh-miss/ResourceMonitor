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
 * File: DefaultAgentAddressProvider.java
 * Created: 2011-07-27
 * Author: faber
 * $Id: DefaultAgentAddressProvider.java 124 2012-03-18 10:27:39Z krzywick $
 */

package org.jage.address.provider;

import java.util.UUID;

import org.jage.address.AgentAddress;
import org.jage.address.IAgentAddress;
import org.jage.address.node.provider.DefaultNodeAddressProvider;
import org.jage.address.node.provider.IAgENodeAddressProvider;
import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.exception.ComponentException;

/**
 * A default implementation of the agent address provider.
 * <p>
 * When not configured, {@link DefaultNameProvider} is used as an implementation of {@link INameProvider}.
 * <p>
 * When not configured, {@link DefaultNodeAddressProvider} is used as an implementation of
 * {@link IAgENodeAddressProvider}.
 *
 * @see INameProvider
 * @see IAgENodeAddressProvider
 * @author AGH AgE Team
 */
public class DefaultAgentAddressProvider implements IAgentAddressProvider, IStatefulComponent {

	private final INameProvider nameProvider = new DefaultNameProvider();

	private final IAgENodeAddressProvider nodeAddressProvider = new DefaultNodeAddressProvider();

	@Override
	public IAgentAddress obtainAddress(final String nameInitializer) {
		String name = null;
		if (nameInitializer != null) {
			name = nameProvider.generateNameFromTemplate(nameInitializer);
		}

		final IAgentAddress address = new AgentAddress(UUID.randomUUID(), nodeAddressProvider.getNodeAddress(), name);
		return address;
	}

	@Override
    public void init() throws ComponentException {
	    // do nothing
    }

	@Override
	public boolean finish() throws ComponentException {
		return true;
	}
}
