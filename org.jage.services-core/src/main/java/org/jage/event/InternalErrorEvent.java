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
 * File: InternalErrorEvent.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: InternalErrorEvent.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.event;

/**
 * This event is created due to an internal error.
 * 
 * @author AGH AgE Team
 */
public class InternalErrorEvent extends AbstractEvent {

	/**
	 * The error that occured.
	 */
	protected final Throwable error;

	/**
	 * Constructor.
	 * 
	 * @param error
	 *            the error that occured
	 */
	public InternalErrorEvent(Throwable error) {
		super();
		this.error = error;
	}

	/**
	 * Returns the error that occured.
	 * 
	 * @return the error that occured
	 */
	public Throwable getError() {
		return error;
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Internal error event [message: " + error.getMessage() + "]";
	}

}
