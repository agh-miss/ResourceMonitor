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
 * File: AgentActionEvent.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: AgentActionEvent.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.event;

import org.jage.action.Action;
import org.jage.address.IAgentAddress;
import org.jage.agent.IAggregate;

/**
 * This event is created when an action is to be performed on an aggregate.
 * 
 * @author AGH AgE Team
 */
public class AgentActionEvent extends AggregateEvent {

	/**
	 * The action to perform on the aggregate.
	 */
	private final Action action;

	/**
	 * The address of the entity which invoked the action. TODO: check if the invoker is essential
	 */
	private final IAgentAddress invoker;

	/**
	 * Constructor.
	 * 
	 * @param eventCreator
	 *            the aggregate which creates this event
	 * @param action
	 *            the action to perform on the aggregate
	 * @param invoker
	 *            the address of the entity which invoked the action
	 */
	public AgentActionEvent(IAggregate eventCreator, Action action, IAgentAddress invoker) {
		super(eventCreator);
		this.action = action;
		this.invoker = invoker;
	}

	/**
	 * Returns the action to perform on the aggregate.
	 * 
	 * @return the action to perform on the aggregate
	 */
	public Action getAction() {
		return action;
	}

	/**
	 * Returns the address of the entity which invoked the action.
	 * 
	 * @return the address of the entity which invoked the action
	 */
	public IAgentAddress getInvoker() {
		return invoker;
	}

	/**
	 * Add new agent action event to the events list.
	 * 
	 * @param event
	 */
	public void addAgentActionEvent(AgentActionEvent event) {
		if (event == null) {
			throw new IllegalArgumentException("Cannot add null event to events tree");
		}

	}

	@Override
	public String toString() {
		return "Agent action event [creator: " + (parent == null ? "Workplace" : parent.getAddress().toString())
		        + ", action: " + action + ", invoker: " + invoker + "]";
	}
}
