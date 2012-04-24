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
 * File: NoMigration.java
 * Created: 2012-03-16
 * Author: Krzywicki
 * $Id: NoMigration.java 210 2012-04-08 20:04:41Z krzywick $
 */

package org.jage.emas.migration;

import org.jage.agent.AgentException;
import org.jage.agent.IAgent;
import org.jage.property.ClassPropertyContainer;

/**
 * No-migration strategy. Does nothing.
 *
 * @param <A>
 *            the type of agents
 *
 * @author AGH AgE Team
 */
public class NoMigration<A extends IAgent> extends ClassPropertyContainer implements Migration<A> {

	@Override
	public void migrate(final A agent) throws AgentException {
		// do nothing
	}
}
