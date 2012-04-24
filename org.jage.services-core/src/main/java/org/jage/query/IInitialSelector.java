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
 * File: IInitialSelector.java
 * Created: 2011-09-12
 * Author: faber
 * $Id: IInitialSelector.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.query;

/**
 * The interface for selectors performing initial decision about including an element in further processing.
 * 
 * @author AGH AgE Team
 */
public interface IInitialSelector {

	/**
	 * Initialises this selector with the number of elements in collection to process.
	 * 
	 * @param elementsCount
	 *            Number of elements.
	 */
	void initialise(long elementsCount);

	/**
	 * Informs whether an element should be included in the further processing.
	 * 
	 * @return True if an element should be included in further processing, false otherwise.
	 */
	boolean include();

}
