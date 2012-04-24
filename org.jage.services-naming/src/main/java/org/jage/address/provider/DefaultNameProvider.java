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
 * File: DefaultNameProvider.java
 * Created: 2011-07-19
 * Author: faber
 * $Id: DefaultNameProvider.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.address.provider;

import java.util.HashMap;
import java.util.Map;

/**
 * This class provides a default implementation of the name provider.
 *
 * @author AGH AgE Team
 */
public class DefaultNameProvider implements INameProvider {

	/**
	 * Map of used prefixes on this host. Key: core of prefix that was present Value: if null - the prefix has occurred
	 * without a counter if integer - the prefix has occurred with some number equal or below it
	 * 
	 * Core of a prefix is a part of the prefix before first dot, or whole prefix if no dot is present.
	 * 
	 * The collection must be explicitly synchronised by locking its instance.
	 */
	private final Map<String, Long> corePrefixToCounters = new HashMap<String, Long>();

	/**
	 * Generates an unique address for an agent. The address will has prefix in form: <tt>corePrefix.id</tt> corePrefix
	 * is a part before first dot
	 * 
	 * @param nameTemplate A template to generate a new name.
	 * @return AgentAddress, not null
	 */
	@Override
    public String generateNameFromTemplate(String nameTemplate) {
		String corePrefix = getCorePrefix(nameTemplate);
		synchronized (corePrefixToCounters) {
			if (corePrefixToCounters.containsKey(corePrefix)) {
				Long counter = corePrefixToCounters.get(corePrefix);
				if (counter == null) {
					corePrefixToCounters.put(corePrefix, Long.valueOf(0));
					return getPrefix(corePrefix, 0);
				}
				int c = counter.intValue() + 1;
                corePrefixToCounters.put(corePrefix, Long.valueOf(c));
                return getPrefix(corePrefix, c);
			}
			corePrefixToCounters.put(corePrefix, Long.valueOf(0));
            return getPrefix(corePrefix, 0);
		}
	}

	/**
	 * Returns new prefix with given core prefix and counter
	 * 
	 * @param corePrefix
	 *            corePrefix, not null
	 * @param counter
	 *            counter, not negative
	 * @return new prefix of the agent address at this node
	 */
	private static String getPrefix(String corePrefix, int counter) {
		if (counter >= 1) {
			return corePrefix + "." + Integer.toString(counter);
		}
		return corePrefix;
	}

	/**
	 * Provides core of given prefix, that is part before first dot.
	 * 
	 * @param prefix
	 *            prefix of an agent address, not null
	 * @return core of the prefix, not null
	 */
	private static String getCorePrefix(String prefix) {
		if (prefix.endsWith("*")) {
			return prefix.substring(0, prefix.length() - 1);
		}
		int dot = prefix.indexOf('.');
		return (dot > 0) ? prefix.substring(0, dot - 1) : prefix;
	}

}
