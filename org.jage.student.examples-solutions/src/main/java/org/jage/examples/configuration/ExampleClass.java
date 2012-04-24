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
 * File: ExampleClass.java
 * Created: 2011-07-11
 * Author: faber
 * $Id: ExampleClass.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.examples.configuration;

/**
 * This class is a simple example class to use in collections.
 * 
 * @author AGH AgE Team
 */
public class ExampleClass {

	private String argument;

	/**
	 * Creates a simple example class instance.
	 * 
	 * @param argument
	 *            A sample string argument.
	 */
	public ExampleClass(String argument) {
		this.argument = argument;
	}

	public String getArgument() {
		return argument;
	}

	public void setArgument(String argument) {
		this.argument = argument;
	}

}
