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
 * File: SampleActionStrategy.java
 * Created: 2011-03-15
 * Author: kpietak
 * $Id: SampleActionStrategy.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.examples.actions;

import java.util.Collection;

import org.jage.action.IActionContext;
import org.jage.action.IPerformActionStategy;
import org.jage.action.SingleAction;
import org.jage.address.IAgentAddress;
import org.jage.agent.AgentException;
import org.jage.agent.IAgent;
import org.jage.agent.ISimpleAggregate;
import org.jage.property.ClassPropertyContainer;

/**
 * The sample action implemented as a strategy.
 * 
 * @author AGH AgE Team
 */
public class SampleActionStrategy extends ClassPropertyContainer implements IPerformActionStategy {

	@Override
	public void finish(IAgent target, IActionContext context) {
		// Empty
	}

	@Override
	public Collection<IAgentAddress> init(ISimpleAggregate aggregate, SingleAction action) throws AgentException {
		return aggregate.validateAction(action);

	}

	@Override
	public void perfom(IAgent target, IActionContext context) throws AgentException {
		log.info("Performing action on {}", target.getAddress());

	}

}
