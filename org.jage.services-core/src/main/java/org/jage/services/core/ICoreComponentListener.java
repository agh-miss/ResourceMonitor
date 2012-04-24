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
 * File: ICoreComponentListener.java
 * Created: 2010-01-07
 * Author: kpietak
 * $Id: ICoreComponentListener.java 129 2012-03-18 18:29:12Z krzywick $
 */

package org.jage.services.core;

/**
 * An interface for listeners of {@link ICoreComponent}. Realize this interface to receive notifications when
 * {@link ICoreComponent} is about to start and has just stopped.
 * 
 * @see ICoreComponent
 * 
 * @author AGH AgE Team
 * 
 */
public interface ICoreComponentListener {

	/**
	 * This method is called at the beginning of {@link ICoreComponent#start()} method and notifies that core component
	 * is about to start.
	 * 
	 * @param coreComponent
	 *            core component which is about to start
	 */
	void notifyStarting(ICoreComponent coreComponent);

	/**
	 * This methods is called when core component has just stopped - at the end on {@link ICoreComponent#stop()} method.
	 * 
	 * @param coreComponent
	 *            core component which has just stopped
	 */
	void notifyStopped(ICoreComponent coreComponent);

}
