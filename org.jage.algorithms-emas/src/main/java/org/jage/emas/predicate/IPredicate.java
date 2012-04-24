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
 * File: IPredicate.java
 * Created: 2012-01-30
 * Author: Krzywicki
 * $Id: IPredicate.java 201 2012-04-07 20:21:39Z krzywick $
 */

package org.jage.emas.predicate;

import org.jage.agent.IAgent;
import org.jage.strategy.IStrategy;

/**
 * Interface for agent predicates.
 *
 * @param <A>
 *            the type of agent this predicate applies to
 *
 * @author AGH AgE Team
 */
public interface IPredicate<A extends IAgent> extends IStrategy {

	/**
	 * Returns true if the predicate applies to the given agent.
	 *
	 * @param agent
	 *            some agent
	 * @return true if the predicate applies to that agent
	 */
	boolean apply(A agent);
}
