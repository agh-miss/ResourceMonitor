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
 * File: IComponentAddress.java
 * Created: 2010-09-09
 * Author: kpietak
 * $Id: IComponentAddress.java 134 2012-03-18 20:17:01Z faber $
 */

package org.jage.address.node;

import org.jage.address.IAddress;

/**
 * The interface for a component address.<p>
 * 
 * Every component address consists of two required parts:
 * <ul>
 * <li>a component name (a string),
 * <li>an address of the node the component is located in.
 * </ul>
 * 
 * @author AGH AgE Team
 */
public interface IComponentAddress extends IAddress {

	/**
	 * Returns the name of the component.
	 * 
	 * @return The name of the component, never <code>null</code>.
	 */
	String getComponentName();

}
