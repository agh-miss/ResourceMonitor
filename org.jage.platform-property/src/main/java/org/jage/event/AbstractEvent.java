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
 * File: AbstractEvent.java
 * Created: 2010-06-23
 * Author: krzs
 * $Id: AbstractEvent.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.event;

import java.util.Calendar;
import java.util.Date;

/**
 * The base class for all events. It only contains information about the event creation time.
 *
 * @author AGH AgE Team
 */
public abstract class AbstractEvent {
	/**
	 * The creation time of this event.
	 */
	private final Date creationTime;

	/**
	 * Constructor. It sets the creation time of this event.
	 */
	public AbstractEvent() {
		creationTime = new Date(Calendar.getInstance().getTimeInMillis());
	}

	/**
	 * Returns the creation time of this event.
	 *
	 * @return the creation time of this event
	 */
	public final Date getCreationTime() {
		return creationTime;
	}
}
