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
 * File: DefaultNodeAddressProvider.java
 * Created: 2011-07-19
 * Author: faber
 * $Id: DefaultNodeAddressProvider.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.address.node.provider;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import org.jage.address.node.INodeAddress;
import org.jage.address.node.NodeAddress;

/**
 * A default implementation of the node address provider.
 * <p>
 * This implementation generates addresses consisting of two parts: PID of JVM that runs the node and the local
 * hostname. If PID is unavailable it falls back a UUID-based string.
 * 
 * @see NodeAddress
 * 
 * @author AGH AgE Team
 */
public class DefaultNodeAddressProvider implements IAgENodeAddressProvider {

	private NodeAddress nodeAddress;

	@Override
	public INodeAddress getNodeAddress() {
		if (nodeAddress == null) {
			String hostname;
			try {
				hostname = getHostname();
			} catch (UnknownHostException e) {
				hostname = "";
			}
			String localPart = getLocalPart();

			nodeAddress = new NodeAddress(localPart, hostname);
		}
		return nodeAddress;
	}

	private static String getHostname() throws UnknownHostException {
		InetAddress addr = InetAddress.getLocalHost();
		return addr.getHostName();
	}

	private static String getLocalPart() {
		String pid = getPid();
		if (pid == null) {
			return UUID.randomUUID().toString();
		}
		return pid;
	}

	// XXX: This method was only tested with Sun/Oracle JVM.
	private static String getPid() {
		String name = ManagementFactory.getRuntimeMXBean().getName();
		int pos = name.indexOf("@");
		if (pos <= 1) { // If 0 - there is no real PID
			return null;
		}
		return name.substring(0, pos);
	}
}
