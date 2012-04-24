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
 * File: IWorkplace.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: IWorkplace.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.workplace;

import org.jage.agent.AlreadyExistsException;
import org.jage.agent.IAggregate;
import org.jage.agent.IThreadAgent;
import org.jage.platform.component.exception.ComponentException;

/**
 * The component that carries out simulation. It is responsible for agents, their life and activities.
 * 
 * @author AGH AgE Team
 */
public interface IWorkplace extends IThreadAgent, IAggregate {

	/**
	 * The Enum State.
	 */
	enum State {
		/** The RUNNING. */
		RUNNING,
		/** The STOPPING. */
		STOPPING,
		/** The STOPPED. */
		STOPPED
	}

	/**
	 * Sets new workplace environment to this workplace.
	 * 
	 * @param workplaceEnvironment
	 *            A workplace environment to set.
	 * 
	 * @throws AlreadyExistsException
	 *             when environment is already set
	 */
	void setWorkplaceEnvironment(IWorkplaceEnvironment workplaceEnvironment) throws AlreadyExistsException;

	/**
	 * Initializes workplace. After calling this method workplace is ready to start or can be moved to another workplace
	 * manager.
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	void init() throws ComponentException;

	/**
	 * Starts the workplace.
	 * 
	 * @throws IllegalOperationException
	 *             if the workplace is already started.
	 */
	void start() throws IllegalOperationException;

	/**
	 * Stops the workplace. After calling this method workplace can be restarted - using the {@link #start} method.
	 * 
	 * @throws IllegalOperationException
	 *             if the workplace has been already stopped.
	 */
	void stop() throws IllegalOperationException;

	/**
	 * Finishes workplace. After calling this method workplace cannot be used any more.
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	boolean finish() throws ComponentException;

}
