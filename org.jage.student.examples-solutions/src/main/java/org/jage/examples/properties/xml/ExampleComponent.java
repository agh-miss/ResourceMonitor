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
 * File: ExampleComponent.java
 * Created: 2011-03-15
 * Author: kpietak
 * $Id: ExampleComponent.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.examples.properties.xml;

import org.jage.platform.component.annotation.Inject;

/**
 * The sample component to use with XML-defined properties. It is described in the file ExampleComponent.contract.xml.
 * 
 * @author AGH AgE Team
 */
public class ExampleComponent {

	@SuppressWarnings("unused")
	private static final String VERSION = "1.0.0";

	@Inject
	private String name = "";

	@Inject
	private int version = 0;

	@Inject
	private Object holder = null;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getVersionNo() {
		return version;
	}

	public Object getHoldedObject() {
		return holder;
	}

	/**
	 * Prints a summary information about the component on the stdout.
	 */
	public void printComponentInfo() {
		System.out.println("My name is " + name);
		System.out.println("My version is " + version);
		System.out.println("I'm holding reference to " + holder);
	}
}
