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
 * File: CollectionComponent.java
 * Created: 2010-03-10
 * Author: kpietak
 * $Id: CollectionComponent.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.platform.component.sample;

import java.util.LinkedList;
import java.util.List;

import org.jage.platform.component.definition.CollectionDefinition;

/**
 * Component example which realize collection interface
 * 
 * @author Maria Szymczak
 * 
 */
public class CollectionComponent extends CollectionDefinition {
	
	public static final class Properties {

		public static final String STRING_LIST = "stringList";
	}
	
	private List<String> stringList = new LinkedList<String>();

	public CollectionComponent(String name, boolean isSingleton) {
		super(name, LinkedList.class, isSingleton);
	}
	
	/**
	 * @return the stringList
	 */
	public List<String> getStringList() {
		return stringList;
	}

	/**
	 * @param stringList
	 *            the stringList to set
	 */
	public void setStringList(List<String> stringList) {
		this.stringList = stringList;
	}

}
