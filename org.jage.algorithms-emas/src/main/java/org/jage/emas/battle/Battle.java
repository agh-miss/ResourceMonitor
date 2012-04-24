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
 * File: Battle.java
 * Created: 2012-03-18
 * Author: Krzywicki
 * $Id: Battle.java 194 2012-04-05 14:13:59Z krzywick $
 */

package org.jage.emas.battle;

import org.jage.agent.IAgent;
import org.jage.strategy.IStrategy;

/**
 * Strategy for battles between agents.
 *
 * @param <A>
 *            the type of agents
 *
 * @author AGH AgE Team
 */
public interface Battle<A extends IAgent> extends IStrategy {

	/**
	 * Carry out a fight between two agents.
	 *
	 * @param first
	 *            the first agent
	 * @param second
	 *            the second agent
	 * @return the winner
	 */
	A fight(A first, A second);
}
