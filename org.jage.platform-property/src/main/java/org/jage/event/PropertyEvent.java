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
 * File: PropertyEvent.java
 * Created: 2010-06-23
 * Author: karolczyk
 * $Id: PropertyEvent.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.event;

import org.jage.property.Property;

/**
 * Event informing that value of the property has changed.
 *
 * @author AGH AgE Team
 *
 */
public class PropertyEvent extends AbstractEvent {

	private Object oldValue;

	private Object newValue;

	private Property property;

	/**
	 * Constructor.
	 *
	 * @param property
	 *            property that has changed
	 * @param oldValue
	 *            old value of the property
	 * @param newValue
	 *            new value of the property
	 */
	public PropertyEvent(Property property, Object oldValue, Object newValue) {
		this.property = property;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	/**
	 * Gets changed property.
	 *
	 * @return property that has changed
	 */
	public Property getProperty() {
		return property;
	}

	/**
	 * Gets old value of the property.
	 *
	 * @return old value
	 */
	public Object getOldValue() {
		return oldValue;
	}

	/**
	 * Gets new value of the property.
	 *
	 * @return new value
	 */
	public Object getNewValue() {
		return newValue;
	}

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return property.getMetaProperty().getName() + " has changed.";
	}
}
