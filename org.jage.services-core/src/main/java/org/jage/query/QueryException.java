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
 * File: QueryException.java
 * Created: 2011-09-12
 * Author: faber
 * $Id: QueryException.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.query;

/**
 * An exception thrown during runtime when query execution results in an error.
 * 
 * @author AGH AgE Team
 */
public class QueryException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new runtime exception with <code>null</code> as its detail message.
	 */
	public QueryException() {
		// Empty
	}

	/**
	 * Constructs a new runtime exception with the specified detail message.
	 * 
	 * @param message
	 *            the detail message.
	 */
	public QueryException(String message) {
		super(message);
	}

	/**
	 * Constructs a new runtime exception with the specified cause and a detail message of
	 * <tt>(cause==null ? null : cause.toString())</tt>.
	 * 
	 * @param cause
	 *            the cause.
	 */
	public QueryException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a new runtime exception with the specified detail message and cause.
	 * 
	 * @param message
	 *            the detail message.
	 * @param cause
	 *            the cause.
	 */
	public QueryException(String message, Throwable cause) {
		super(message, cause);
	}

}
