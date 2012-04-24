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
 * File: AggregateEvent.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: AggregateEvent.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.event;

import org.jage.agent.IAggregate;

/**
 * The base class for events created in an aggregate. This event may contain an action to perform on this aggregate.
 * 
 * @author AGH AgE Team
 */
public abstract class AggregateEvent extends AbstractEvent {

	/**
	 * The aggregate which created this event.
	 */
	protected final IAggregate parent;

	/**
	 * Constructor.
	 * 
	 * @param eventCreator
	 *            the aggregate who creates this event
	 */
	public AggregateEvent(IAggregate eventCreator) {
		super();
		this.parent = eventCreator;
	}

	/**
	 * Returns the agent who created this event.
	 * 
	 * @return the agent who created this event
	 */
	public final IAggregate getParent() {
		return parent;
	}
}
