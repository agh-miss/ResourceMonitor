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
 * File: InvalidRuntimeParametersException.java
 * Created: 2010-06-08
 * Author: kamil
 * $Id: InvalidRuntimeArgumentsException.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.platform.argument;

/**
 * Exception thrown when runtime arguments are invalid.
 * 
 * @author AGH AgE Team
 */
public class InvalidRuntimeArgumentsException extends Exception {

	private static final long serialVersionUID = 2576011626413016314L;

	/**
	 * Constructs a new exception with <code>null</code> as its detail message.
	 * 
	 * @see Exception#Exception()
	 */
	public InvalidRuntimeArgumentsException() {
		// Empty
	}

	/**
	 * Constructs a new exception with the specified detail message.
	 * 
	 * @param message
	 *            the detail message.
	 * 
	 * @see Exception#Exception(String)
	 */
	public InvalidRuntimeArgumentsException(String message) {
		super(message);
	}

	/**
	 * Constructs a new exception with the specified detail message.
	 * 
	 * @param cause
	 *            the cause.
	 * 
	 * @see Exception#Exception(String)
	 */
	public InvalidRuntimeArgumentsException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a new exception with the specified cause and a detail message of
	 * <tt>(cause==null ? null : cause.toString())</tt>.
	 * 
	 * @param message
	 *            the detail message.
	 * @param cause
	 *            the cause.
	 *            
	 * @see Exception#Exception(String, Throwable)
	 */
	public InvalidRuntimeArgumentsException(String message, Throwable cause) {
		super(message, cause);
	}
}
