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
 * File: IIslandAgent.java
 * Created: 2012-01-30
 * Author: Krzywicki
 * $Id: IslandAgent.java 211 2012-04-08 20:06:03Z krzywick $
 */

package org.jage.emas.agent;

import java.util.List;

import org.jage.agent.IAggregate;
import org.jage.agent.ISimpleAgentEnvironment;

/**
 * EMAS island agent interface.
 *
 * @author AGH AgE Team
 */
public interface IslandAgent extends EmasAgent, IAggregate, ISimpleAgentEnvironment {

	/**
	 * Get the current step.
	 */
	long getStep();

	/**
	 * Get a list of individual agents.
	 */
	List<IndividualAgent> getIndividualAgents();
}
