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
 * File: ICoreComponent.java
 * Created: 2009-05-18
 * Author: kpietak
 * $Id: ICoreComponent.java 129 2012-03-18 18:29:12Z krzywick $
 */

package org.jage.services.core;

import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.exception.ComponentException;

/**
 * An interface which represents core component responsible for computation processing. It add two additional methods to
 * component's life-cycle (start and stop) which represents computation state.
 * 
 * @author AGH AgE Team
 * 
 */
public interface ICoreComponent extends IStatefulComponent {

	/**
	 * Starts the component. This method is executed after component initialization and establishing initial values of
	 * its properties.
	 * 
	 * @throws ComponentException
	 *             occurs when the component cannot be started
	 */
	void start() throws ComponentException;

	/**
	 * Stops the component. It is asynchronous method which order components to stop. When component is stopped it calls
	 * <code>IComponentEnvironment.notifyStopped</code> method.
	 * 
	 */
	void stop();

	/**
	 * Registers a core component listener.
	 * 
	 * @param listener
	 *            listener to be registered; must be not <code>null</code>
	 */
	void registerListener(ICoreComponentListener listener);

	/**
	 * Unregisters a core component listener. If a given listener has not been registered it is ignored - no error
	 * occurs.
	 * 
	 * @param listener
	 *            listener to be registered; must be not <code>null</code>
	 */
	void unregisterListener(ICoreComponentListener listener);

}
