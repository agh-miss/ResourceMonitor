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
 * File: IValueFilter.java
 * Created: 2011-09-12
 * Author: faber
 * $Id: IValueFilter.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.query;

/**
 * The interface for filters that perform matching on values of elements.
 * 
 * @param <T>
 *            A type of object that this filter can be used against.
 * @author AGH AgE Team
 */
public interface IValueFilter<T> {

	/**
	 * Checks whether given object matches a condition represented by this filter.
	 * 
	 * @param object
	 *            An object to test.
	 * @return True if the given object matches a condition, false otherwise.
	 */
	boolean matches(T object);
}
