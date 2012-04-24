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
 * File: AbstractPerformActionStrategy.java
 * Created: 2009-05-17
 * Author: kpietak
 * $Id: AbstractPerformActionStrategy.java 177 2012-03-31 18:32:12Z faber $
 */

package org.jage.action;

import java.util.Collection;

import org.jage.address.IAgentAddress;
import org.jage.agent.AgentException;
import org.jage.agent.IAgent;
import org.jage.agent.ISimpleAggregate;
import org.jage.property.ClassPropertyContainer;

/**
 * Abstract action strategy. Contains empty implementations of action strategy methods. Should be extended by actions
 * which define only a subset of methods.
 *
 * @author AGH AgE Team
 *
 */
public class AbstractPerformActionStrategy extends ClassPropertyContainer implements IPerformActionStategy {

	@Override
	public Collection<IAgentAddress> init(final ISimpleAggregate aggregate, final SingleAction action)
			throws AgentException {
		return aggregate.validateAction(action);
	}

	@Override
	public void perfom(final IAgent target, final IActionContext context) throws AgentException {
		// do nothing by default
	}

	@Override
	public void finish(final IAgent target, final IActionContext context) {
		// do nothing by default
	}

}
