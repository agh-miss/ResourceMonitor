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
 * File: UnknownSchemeException.java
 * Created: 2011-08-20
 * Author: faber
 * $Id: UnknownSchemeException.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.util.io;

/**
 * Exception thrown when URI scheme (e.g. <code>file</code> in <code>file:///tmp/example</code>) in unknown.
 * 
 * @author AGH AgE Team
 */
public class UnknownSchemeException extends Exception {

	private static final long serialVersionUID = 7865372754878950465L;

	/**
	 * Constructs a new exception with <code>null</code> as its detail message.
	 * 
	 * @see Exception#Exception()
	 */
	public UnknownSchemeException() {
		super();
	}

	/**
	 * Constructs a new exception with a given message.
	 * 
	 * @param message
	 *            the detail message.
	 * @see Exception#Exception(String)
	 */
	public UnknownSchemeException(String message) {
		super(message);
	}

	/**
	 * Constructs a new exception with the specified cause and a detail message of
	 * <tt>(cause==null ? null : cause.toString())</tt>.
	 * 
	 * @param cause
	 *            the cause
	 * @see Exception#Exception(Throwable)
	 */
	public UnknownSchemeException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a new exception with the specified detail message and cause.
	 * 
	 * @param message
	 *            the detail message.
	 * @param cause
	 *            the cause.
	 * @see Exception#Exception(String, Throwable)
	 */
	public UnknownSchemeException(String message, Throwable cause) {
		super(message, cause);
	}

}
