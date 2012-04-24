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
 * File: ISimpleAgentEnvironment.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: ISimpleAgentEnvironment.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.agent;

import org.jage.action.Action;
import org.jage.address.IAgentAddress;

/**
 * An environment for SimpleAgent's which add an ability to performing actions by the environment.
 * 
 * @author AGH AgE Team
 */
public interface ISimpleAgentEnvironment extends IAgentEnvironment {

	/**
	 * Performs an action in the local environment.
	 * 
	 * @param action
	 *            the action to perform
	 * @param invoker
	 *            the address of the entity that invokes the action
	 */
	public void doAction(Action action, IAgentAddress invoker);

}
