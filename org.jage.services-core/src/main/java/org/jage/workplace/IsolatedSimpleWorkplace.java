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
 * File: IsolatedSimpleWorkplace.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: IsolatedSimpleWorkplace.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.workplace;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.agent.IAgent;
import org.jage.query.AgentEnvironmentQuery;

/**
 * This is a complete workplace which doesn't communicate with any other workplaces (works as single/isolated
 * workplace).
 * 
 * @author AGH AgE Team
 */
public class IsolatedSimpleWorkplace extends SimpleWorkplace {

	private static final long serialVersionUID = 257256225676180951L;

	protected final Logger log = LoggerFactory.getLogger(IsolatedSimpleWorkplace.class);

	/**
	 * {@inheritDoc}
	 * <p>
	 * The isolated workplace cannot query a parent. This method always throws the {@link IllegalOperationException}.
	 */
	@Override
	public <E extends IAgent, T> Collection<T> queryParent(AgentEnvironmentQuery<E, T> query) {
		throw new IllegalOperationException("Isolated workplace cannot query a parent.");
	}

	@Override
	public void run() {
		setRunning();
		log.info("{} has been started", getAddress());
		while (isRunning()) {
			step();
		}
		setStopped();
	}

}
