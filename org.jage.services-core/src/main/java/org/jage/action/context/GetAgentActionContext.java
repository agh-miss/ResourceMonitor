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
 * File: GetAgentActionContext.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: GetAgentActionContext.java 177 2012-03-31 18:32:12Z faber $
 */

package org.jage.action.context;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The action of getting agent reference. Used for complex actions which operates on more than one agents.
 * 
 * @author AGH AgE Team
 */
@AgentActionContext(GetAgentActionContext.ACTION_NAME)
public class GetAgentActionContext extends AbstractAgentActionContext {

	/**
	 * The action name of this context.
	 */
	public static final String ACTION_NAME = "getAgent";

	private final IActionWithAgentReferenceContext context;

	/**
	 * Constructs a new "get agent" action context.
	 * 
	 * @param context
	 *            a context providing a reference to an agent.
	 * @throws NullPointerException
	 *             if context is <code>null</code>.
	 */
	public GetAgentActionContext(final IActionWithAgentReferenceContext context) {
		this.context = checkNotNull(context);
	}

	/**
	 * Returns the target context for the action results.
	 * 
	 * @return the target context (to which the agent reference will be injected).
	 */
	public IActionWithAgentReferenceContext getActionWithAgentReferenceContext() {
		return context;
	}
}
