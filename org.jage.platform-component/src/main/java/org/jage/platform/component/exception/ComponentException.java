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
 * File: ComponentException.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: ComponentException.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.platform.component.exception;

/**
 * The exception thrown by an AgE component if it cannot be created or cannot work properly.
 * 
 * @author KrzS
 */
public class ComponentException extends Exception {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @see Exception#Exception(String)
	 */
	public ComponentException(String msg) {
		super(msg);

	}

	/**
	 * 
	 * @see Exception#Exception(Throwable)
	 */
	public ComponentException(Throwable e) {
		super(e);

	}

	/**
	 * @see Exception#Exception(String, Throwable)
	 */
	public ComponentException(String message, Throwable cause) {
		super(message, cause);
	}

}
