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
package org.jage.property;

/**
 * This class is used by Property class to monitor it's value. Different strategies
 * know how to monitor different types.
 * @author Tomek
 */
public abstract class PropertyValueMonitoringStrategy {

	private Property _property;
	private Object _oldValue;
	
	/**
	 * Constructor.
	 * @param property property that uses this strategy.
	 */
	public PropertyValueMonitoringStrategy(Property property) {
		_property = property;
		_oldValue = property.getValue();
	}
	
	/**
	 * Returns property that uses this strategy.
	 * @return property that uses this strategy.
	 */
	protected Property getProperty() {
		return _property;
	}
	
	/**
	 * Returns previously read value of the property.
	 * @return previously read value of the property.
	 */
	protected Object getOldValue() {
		return _oldValue;
	}
	
	/**
	 * Updates previously read value of the property.
	 * @param oldValue
	 */
	protected void setOldValue(Object oldValue) {
		_oldValue = oldValue;
	}
	
	/**
	 * Informs the strategy that property value has been changed.
	 * @param oldValue old property value.
	 * @param newValue new property value.
	 */
	public abstract void propertyValueChanged(Object newValue);
}
