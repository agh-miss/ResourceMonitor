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
 * File: IActionWithAgentReferenceContext.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: IActionWithAgentReferenceContext.java 166 2012-03-30 08:26:53Z faber $
 */

package org.jage.action.context;

import org.jage.action.IActionContext;
import org.jage.agent.IAgent;
import org.jage.agent.IAggregate;

/**
 * Action context which contains a reference to an agent.
 * 
 * @author AGH AgE Team
 */
public interface IActionWithAgentReferenceContext extends IActionContext {

	/**
	 * Sets the agent reference for the context.
	 * 
	 * @param agent
	 *            the agent reference for the context.
	 */
	void setAgent(IAgent agent);

	/**
	 * Returns the agent reference for the context.
	 * 
	 * @return the agent reference for the context.
	 */
	IAgent getAgent();

	/**
	 * Sets the parent of the agent that is contained in this context.
	 * 
	 * @param parent
	 *            the parent aggregate.
	 */
	void setParent(IAggregate parent);

	/**
	 * Returns the parent of the agent that is contained in this context.
	 * 
	 * @return the parent aggregate.
	 */
	IAggregate getParent();
}
