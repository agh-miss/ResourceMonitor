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

import java.util.ArrayList;

import org.jage.event.AbstractEvent;
import org.jage.monitor.IChangesNotifier;
import org.jage.monitor.IMonitor;
import org.jage.property.monitors.IPropertyMonitorRule;
import org.jage.property.monitors.AbstractPropertyMonitor;
import org.jage.property.monitors.PropertyMonitorRulePair;
import org.jage.property.monitors.PropertyMonitorsSet;

/**
 * Base, abstract class for all properties.
 * @author Tomek
 */
public abstract class Property {
	
	/**
	 * Monitors
	 */
	private final PropertyMonitorsSet _monitors;
	
	/**
	 * Strategies
	 */
	private PropertyValueMonitoringStrategy _monitoringStrategy;

	/**
	 * Constructor.
	 */
	public Property() {
		_monitors = new PropertyMonitorsSet();
	}
	
	/**
	 * Initializes monitoring strategy for the property.
	 */
	protected void initializeMonitoringStrategy() {		
		_monitoringStrategy = getMonitoringStrategy();
	}
	
	/**
	 * Creates monitoring strategy for this property. 
	 * @return
	 */
	protected PropertyValueMonitoringStrategy getMonitoringStrategy() {
		Class<?> type = getMetaProperty().getPropertyClass();
		if (IChangesNotifier.class.isAssignableFrom(type)) {
			return new ChangesNotifierMonitoringStrategy(this);
		}
		else if (type.isArray() && IChangesNotifier.class.isAssignableFrom(type.getComponentType())) {
			return new ChangesNotifierArrayMonitoringStrategy(this);
		}
		else {
			return new NoMonitoringStrategy(this);
		}
	}
	
	/**
	 * Returns metadata for this property.
	 * @return metadata for this property.
	 */
	public abstract MetaProperty getMetaProperty();
	
	/**
	 * Returns value of this property.
	 * @return value of this property.
	 */
	public abstract Object getValue();

	/**
	 * Sets value of this property.
	 * @param value new value.
	 * @throws InvalidPropertyOperationException property is read-only.
	 */
	public abstract void setValue(Object value) 
		throws InvalidPropertyOperationException;
	
	/**
	 * Registers monitor for this property.
	 * @param monitor monitor to register.
	 * @param rule monitor rule.
	 * @throws InvalidPropertyOperationException property is not monitorable.
	 */
	public void addMonitor(AbstractPropertyMonitor monitor, IPropertyMonitorRule rule) 
		throws InvalidPropertyOperationException {
		if (!getMetaProperty().isMonitorable()) {
			throw new InvalidPropertyOperationException("Property is not monitorable.");
		}
		
		_monitors.addMonitor(monitor, rule);
		monitor.setOldValue(getValue());
	}
	
	/**
	 * Unregisters monitor from this property.
	 * @param monitor monitor to unregister.
	 * @throws InvalidPropertyOperationException property is not monitorable.
	 */
	public void removeMonitor(AbstractPropertyMonitor monitor)
		throws InvalidPropertyOperationException {
		if (!getMetaProperty().isMonitorable()) {
			throw new InvalidPropertyOperationException("Property is not monitorable.");
		}
		
		_monitors.removeMonitor(monitor);
	}
	
	/**
	 * Notify monitors about property change. Monitors are notified even if the new value
	 * is the same as the old one.
	 * @param newValue new value of the property.
	 */
	public void notifyMonitors(Object newValue) {
		notifyMonitors(newValue, true);
	}
	
	/**
	 * Notify monitors about property change.
	 * @param newValue new value of the property.
	 * @param foceNotyfying if this parameter is true, monitors are notified even if the
	 * new value is the same as the old one.
	 */
	public void notifyMonitors(Object newValue, boolean forceNotifying) {
		_monitors.notifyMonitors(this, newValue, forceNotifying);
		_monitoringStrategy.propertyValueChanged(newValue);
	}	

	/**
	 * Informs the property that is has been deleted.
	 * @param event
	 */
	public void objectDeleted(AbstractEvent event) {
		ArrayList<IMonitor> monitors = new ArrayList<IMonitor>();
		synchronized(_monitors) {
			// must create a copy because otherwise throws exception about
			// concurent _monitors modification (an event handler modifies _monitors
			// (in the same thread) while the _montiors is iterated here).
			for (PropertyMonitorRulePair rulePair : _monitors) {
				monitors.add(rulePair.getPropertyMonitor());
			}
		}
		for(IMonitor monitor : monitors) {
			monitor.ownerDeleted(event);
		}
	}
	
	/**
	 * Provides simple version of this property
	 * that does not have any monitors and additional stuff.
	 * Suitable for serialization.
	 * 
	 * @return instance of simple property
	 */
	public SimpleProperty getSimpleProperty() {
		return new SimpleProperty(getMetaProperty(), getValue());
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[ Property name: " + getMetaProperty().getName() + " value: " + getValue() + " ]";
	}
}
