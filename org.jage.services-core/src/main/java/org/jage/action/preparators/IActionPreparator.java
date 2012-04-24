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
 * File: IActionPreparator.java
 * Created: 2011-04-29
 * Author: Krzywicki
 * $Id: IActionPreparator.java 166 2012-03-30 08:26:53Z faber $
 */

package org.jage.action.preparators;

import java.util.List;

import org.jage.action.Action;
import org.jage.agent.IAgent;

/**
 * A factory strategy being used by an ActionDrivenAgent. Given a set of properties, reflecting the calling agent state,
 * and a queryable interface, reflecting the calling agent's environment, it prepares a list of Action objects, to be
 * executed by the agent.
 * <p>
 * 
 * These actions encapsulate the actual behavior of the Agent. They may be either simple or complex.
 * 
 * @param <T>
 *            a type of the agent that the preparator operates on.
 * @author AGH AgE Team
 */
public interface IActionPreparator<T extends IAgent> {

	/**
	 * Returns the list of actions to be executed, given the state and environment of an agent.
	 * 
	 * @param agent
	 *            an agent whose state will be used.
	 * @return the list of actions to be executed.
	 */
	List<Action> prepareActions(T agent);
}
