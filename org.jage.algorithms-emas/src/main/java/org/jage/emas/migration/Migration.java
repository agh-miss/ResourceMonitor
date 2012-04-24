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
 * File: IMigration.java
 * Created: 2012-01-30
 * Author: Krzywicki
 * $Id: Migration.java 210 2012-04-08 20:04:41Z krzywick $
 */

package org.jage.emas.migration;

import org.jage.agent.AgentException;
import org.jage.agent.IAgent;
import org.jage.strategy.IStrategy;

/**
 * Strategy for agents migrations.
 *
 * @param <A>
 *            the type of agents
 *
 * @author AGH AgE Team
 */
public interface Migration<A extends IAgent> extends IStrategy {

	/**
	 * Migrates the given agent.
	 *
	 * @param agent
	 *            an agent to be migrated
	 * @throws AgentException
	 *             if the migration fails
	 */
	void migrate(A agent) throws AgentException;
}
