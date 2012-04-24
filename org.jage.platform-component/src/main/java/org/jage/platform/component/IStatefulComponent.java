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
 * File: IStatefulComponent.java
 * Created: Jun 8, 2010
 * Author: kamil
 * $Id: IStatefulComponent.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.platform.component;

import org.jage.platform.component.exception.ComponentException;

/**
 * 
 * @author AGH AgE Team
 */
public interface IStatefulComponent {

	/**
	 * Called just after the component instance is created and before the instance is returned to the client (note: this
	 * method is called only once while creating a component instance).
	 * 
	 * @throws ComponentException
	 *             when any error during initialization occurs
	 */
	void init() throws ComponentException;

	/**
	 * Called just before the component instance is unregistered; this is usually performed when the platform is going
	 * to shutdown. The method should be used to perform a cleanup, for instance closing network connections, removing
	 * temporary files.
	 * 
	 * @return <code>true</code> when the component is finished, <code>false</code> when component is still finishing
	 *         (it is long running operation)
	 * @throws ComponentException
	 *             when any error during finishing occurs
	 */
	boolean finish() throws ComponentException;
}
