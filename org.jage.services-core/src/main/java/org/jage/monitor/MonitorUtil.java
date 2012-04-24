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
 * File: MonitorUtil.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: MonitorUtil.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.monitor;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A monitor utility class.
 * 
 * @author AGH AgE Team
 */
public final class MonitorUtil {

	/**
	 * Provides copy of given collection. The copy is created in a critical section.
	 * 
	 * Copying collections of listeners/monitors is necessary in order to prevent deadlocks.
	 * 
	 * @param <T>
	 *            type of elements in the collection
	 * @param source
	 *            input collection
	 * @return collection that has the same elements
	 */
	public static <T extends IMonitor> Collection<T> getCopy(Collection<T> source) {
		synchronized (source) {
			return new ArrayList<T>(source);
		}
	}

	private MonitorUtil() {
		// prevent instantiation
	}
}
