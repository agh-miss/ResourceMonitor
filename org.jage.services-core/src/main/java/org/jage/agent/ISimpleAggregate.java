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
 * File: ISimpleAggregate.java
 * Created: 2009-05-20
 * Author: kpietak
 * $Id: ISimpleAggregate.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.agent;

import java.util.Collection;

import org.jage.action.SingleAction;
import org.jage.address.IAgentAddress;
import org.jage.address.selector.BroadcastSelector;
import org.jage.platform.component.provider.IComponentInstanceProviderAware;

/**
 * An interface for simple aggregates. It adds methods for performing actions.
 * 
 * @author AGH AgE Team
 * 
 */
public interface ISimpleAggregate extends IAggregate, ISimpleAgent, IComponentInstanceProviderAware {

	/**
	 * Validate given action. It checks if used addresses point to agents in current aggregate.
	 * 
	 * @param action
	 *            single action to validate
	 * @return collection of agent addresses used in action, if no addresses is used (action has selectors such as
	 *         {@link BroadcastSelector}, etc.) the empty collection is returned; <code>null</code> is returned when
	 *         action didn't validate addresses - then a default validation is performed
	 * @throws AgentException
	 *             when validation fails.
	 */
	public Collection<IAgentAddress> validateAction(SingleAction action) throws AgentException;
}
