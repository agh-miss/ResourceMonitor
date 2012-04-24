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
package org.jage.property.testHelpers;

import org.jage.event.AbstractEvent;
import org.jage.event.PropertyEvent;
import org.jage.property.monitors.AbstractPropertyMonitor;

public class PropertyMonitorStub extends AbstractPropertyMonitor {

	private int _propertyChangedInvokationCounter = 0;
	
	@Override
	protected void propertyChanged(PropertyEvent event) {
		_propertyChangedInvokationCounter++;
	}
	
	public int getPropertyChangedInvokationCounter() {
		return _propertyChangedInvokationCounter;
	}

	public void ownerDeleted(AbstractEvent event) { }
}
