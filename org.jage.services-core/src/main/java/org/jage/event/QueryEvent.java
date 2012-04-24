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
 * File: QueryEvent.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: QueryEvent.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.event;

import org.jage.agent.IAgent;
import org.jage.agent.IAggregate;
import org.jage.query.IQuery;

/**
 * This event is created when a query has been performed.
 * 
 * @author AGH AgE Team
 */
public class QueryEvent extends AggregateEvent {

	/**
	 * The query that has been performed.
	 */
	protected final IQuery<?, ?> query;

	/**
	 * Constructor.
	 * 
	 * @param eventCreator
	 *            the creator of this event
	 * @param query
	 *            the query that has been performed
	 */
	public QueryEvent(IAggregate eventCreator, IQuery<?, ?> query) {
		super(eventCreator);
		this.query = query;
	}

	/**
	 * Returns the query that has been performed.
	 * 
	 * @return the query that has been performed
	 */
	public IQuery<?, ?> getQuery() {
		return query;
	}

	@Override
	public String toString() {
		return "Query event [creator: " + (parent == null ? "IWorkplace" : ((IAgent)parent).getAddress().toString())
		        + ", query: " + query + "]";
	}
}
