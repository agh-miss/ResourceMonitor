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
 * File: PropertyException.java
 * Created: 2010-06-23
 * Author: kpietak
 * $Id: PropertyException.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.property;

/**
 * Exception for any issues connected with property mechanism.
 *
 * @author AGH AgE Team
 *
 */
public class PropertyException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public PropertyException() {

	}

	/**
	 * Constructor.
	 *
	 * @param msg
	 *            message
	 */
	public PropertyException(String msg) {
		super(msg);
	}

	/**
	 * Constructor.
	 *
	 * @param e
	 *            cause exception
	 */
	public PropertyException(Exception e) {
		super(e);
	}

	/**
	 * Constructor.
	 *
	 * @param msg
	 *            message
	 * @param e
	 *            cause
	 */
	public PropertyException(String msg, Exception e) {
		super(msg, e);
	}
}
