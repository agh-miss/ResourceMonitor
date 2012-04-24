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
 * File: NeverStopCondition.java
 * Created: 2012-02-09
 * Author: faber
 * $Id: NeverStopCondition.java 134 2012-03-18 20:17:01Z faber $
 */

package org.jage.examples.distributed;

import org.jage.platform.component.exception.ComponentException;
import org.jage.services.core.ICoreComponent;
import org.jage.workplace.IStopCondition;

/**
 * An {@link IStopCondition} implementation that is never satisfied.
 * 
 * @author AGH AgE Team
 */
public class NeverStopCondition implements IStopCondition {

	@Override
	public void init() throws ComponentException {
		// Empty
	}

	@Override
	public boolean finish() throws ComponentException {
		// Empty
		return false;
	}

	@Override
	public void notifyStarting(ICoreComponent coreComponent) {
		// Empty
	}

	@Override
	public void notifyStopped(ICoreComponent coreComponent) {
		// Empty
	}

}
