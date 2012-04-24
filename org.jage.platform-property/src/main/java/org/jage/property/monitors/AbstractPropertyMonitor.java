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
 * File: AbstractPropertyMonitor.java
 * Created: 2010-06-23
 * Author: kmiecik
 * $Id: AbstractPropertyMonitor.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.property.monitors;

import org.jage.event.PropertyEvent;
import org.jage.monitor.IMonitor;
import org.jage.property.Property;

/**
 * Interface for property monitors.
 *
 * @author AGH AgE Team
 */
public abstract class AbstractPropertyMonitor implements IMonitor {

	private Object oldValue;

	/**
	 * Constructor. It initializes the previous value (stored in this object, not in property) to null.
	 */
	protected AbstractPropertyMonitor() {
		oldValue = null;
	}

	/**
	 * This method is called be monitored object to inform the monitor that the property has been changed.
	 *
	 * @param property
	 *            property that has been changed
	 * @param newValue
	 *            new value of the property.
	 */
	public void propertyChanged(Property property, Object newValue) {
		propertyChanged(new PropertyEvent(property, oldValue, newValue));
		oldValue = newValue;
	}

	/**
	 * This method is invoked every time the property has been changed. Implement it to provide your own action.
	 *
	 * @param event
	 */
	protected abstract void propertyChanged(PropertyEvent event);

	/**
	 * Gets the previous value of the monitored property.
	 *
	 * @return old value of property
	 */
	public Object getOldValue() {
		return oldValue;
	}

	/**
	 * Sets the previous value of the monitored property.
	 *
	 * @param value
	 *            old value of property
	 */
	public void setOldValue(Object value) {
		oldValue = value;
	}
}
