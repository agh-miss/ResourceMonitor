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
/**
 * 
 */
package org.jage.property.xml.testHelpers;

/**
 * @author Tomasz Lukasik
 * 
 */
public class NoFieldComponent {

	@SuppressWarnings("unused")
	private static final String version = "1.0.0";

	private int intProperty = 0;
	private String stringProperty = "";

	public int getIntProperty() {
		return intProperty;
	}

	public void setIntProperty(int value) {
		intProperty = value;
	}

	public String getStringProperty() {
		return stringProperty;
	}

	public void setStringProperty(String value) {
		stringProperty = value;
	}

}
