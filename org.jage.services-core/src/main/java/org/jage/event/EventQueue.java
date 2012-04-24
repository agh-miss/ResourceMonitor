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
 * File: EventQueue.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: EventQueue.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.event;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The queue of agent events. The class is synchronized.
 * 
 * @author AGH AgE Team
 */
public class EventQueue {

	private static Logger log = LoggerFactory.getLogger(EventQueue.class);

	/**
	 * The list of events.
	 */
	protected LinkedList<AggregateEvent> eventList;

	/**
	 * Constructor.
	 */
	public EventQueue() {
		eventList = new LinkedList<AggregateEvent>();
	}

	/**
	 * Adds an event to this queue.
	 * 
	 * @param event
	 *            an event to add
	 */
	public synchronized void add(AggregateEvent event) {
		eventList.addLast(event);
		log.debug("Event added: {}", event.toString());
	}

	/**
	 * Removes the first event from the queue.
	 * 
	 * @return the first (removed) event from the queue or null if the queue is empty
	 */
	public synchronized AggregateEvent removeFirst() {
		AggregateEvent removedEvent = eventList.isEmpty() ? null : (AggregateEvent)eventList.removeFirst();
		if (removedEvent != null) {
			log.debug("Event removed: {}", removedEvent.toString());
		}
		return removedEvent;
	}
}
