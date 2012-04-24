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
package org.jage.property.functions;

/**
 * Exception thrown when invalid argument was passed to a function (for example, 
 * string argument for numeric function). 
 * @author Tomek
 *
 */
public class InvalidFunctionArgumentException extends Exception {
	
	private static final long serialVersionUID = 1;
	
	/**
	 * Constructor.
	 */
	public InvalidFunctionArgumentException() {
		super();
	}

	/**
	 * Constructor.
	 * @param message message for the exception.
	 */
	public InvalidFunctionArgumentException(String message) {
		super(message);
	}
	
	/**
	 * Constructor.
	 * @param message message for the exception.
	 * @param throwable inner exception.
	 */
	public InvalidFunctionArgumentException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * Constructor.
	 * @param throwable inner exception.
	 */
	public InvalidFunctionArgumentException(Throwable throwable) {
		super(throwable);
	}
}
