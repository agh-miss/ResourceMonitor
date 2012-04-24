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
 * File: CommunicationException.java
 * Created: 2012-03-15
 * Author: faber
 * $Id: CommunicationException.java 139 2012-03-19 22:54:18Z faber $
 */

package org.jage.communication;

/**
 * {@code CommunicationException} is thrown whenever there is problem with communication between different nodes.
 * 
 * @author AGH AgE Team
 */
public class CommunicationException extends RuntimeException {

	private static final long serialVersionUID = -2313554547438876828L;

	/**
	 * Constructs a new communication exception with <code>null</code> as its detail message.
	 */
	public CommunicationException() {
		super();
	}

	/**
	 * Constructs a new communication exception.
	 * 
	 * @param message
	 *            the detail message.
	 * @param cause
	 *            the cause of this exception.
	 */
	public CommunicationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new communication exception.
	 * 
	 * @param message
	 *            the detail message.
	 */
	public CommunicationException(String message) {
		super(message);
	}

	/**
	 * Constructs a new communication exception with <code>null</code> as its detail message.
	 * 
	 * @param cause
	 *            the cause of this exception.
	 */
	public CommunicationException(Throwable cause) {
		super(cause);
	}

}
