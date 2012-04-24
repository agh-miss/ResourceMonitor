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
 * File: SingleAction.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: SingleAction.java 166 2012-03-30 08:26:53Z faber $
 */

package org.jage.action;

import org.jage.address.IAgentAddress;
import org.jage.address.selector.IAddressSelector;

import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A single action. It contains a target address selector and an action context. The selector contains addresses on
 * which action will be processed.
 * 
 * @author AGH AgE Team
 * 
 */
public class SingleAction extends Action {

	private final IAddressSelector<IAgentAddress> target;

	/**
	 * An action context to be processed.
	 */
	private final IActionContext context;

	/**
	 * A name of the action to be executed (appropriate only if context is annotated with more than one action name).
	 */
	private final String actionToExecute;

	/**
	 * Constructs a new single action.
	 * 
	 * @param target
	 *            address selector that will choose target agents of this action.
	 * @param context
	 *            this action context.
	 * 
	 * @throws NullPointerException
	 *             if any of {@code target} or {@code context} is null.
	 */
	public SingleAction(final IAddressSelector<IAgentAddress> target, final IActionContext context) {
		this(target, context, null);
	}

	/**
	 * Constructs a new single action.
	 * 
	 * @param target
	 *            address selector that will choose target agents of this action.
	 * @param context
	 *            this action context.
	 * @param actionToExecute
	 *            which action should be executed.
	 * 
	 * @throws NullPointerException
	 *             if any of {@code target} or {@code context} is null.
	 */
	public SingleAction(final IAddressSelector<IAgentAddress> target, final IActionContext context,
	        final String actionToExecute) {
		this.target = checkNotNull(target, "Cannot create an action with the null address selector.");
		this.context = checkNotNull(context, "Context cannot be null.");
		this.actionToExecute = actionToExecute;
	}

	/**
	 * Returns the target of this action.
	 * 
	 * @return the target of this action.
	 */
	public IAddressSelector<IAgentAddress> getTarget() {
		return target;
	}

	/**
	 * Returns the context of this action.
	 * 
	 * @return the context of this action.
	 */
	public IActionContext getContext() {
		return context;
	}

	/**
	 * Returns the name of the action to execute.
	 * 
	 * @return the name of the action to execute (possibly {@code null}).
	 */
	public String getActionToExecute() {
		return actionToExecute;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("target", target).add("context", context)
		        .add("actionToExecute", actionToExecute).toString();
	}
}
