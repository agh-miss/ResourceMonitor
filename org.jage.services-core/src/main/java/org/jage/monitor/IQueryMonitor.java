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
 * File: IQueryMonitor.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: IQueryMonitor.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.monitor;

import org.jage.event.QueryEvent;

/**
 * The interface for monitors of events of performing queries. It enables an agent container (for instance an aggregate)
 * to inform an external object about performing a query by an agent.
 * 
 * @author AGH AgE Team
 */
public interface IQueryMonitor extends IMonitor {

	/**
	 * This method is executed when a query has been performed. Note: a monitor can get informed about the event in a
	 * short time after is is unregistered.
	 * 
	 * @param event
	 *            the event of performing a query
	 */
	public void queryPerformed(QueryEvent event);
}
