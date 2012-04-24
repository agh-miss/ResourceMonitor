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
 * File: AbstractPropertyContainer.java
 * Created: 2010-06-23
 * Author: kmiecik
 * $Id: AbstractPropertyContainer.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.property;

import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jage.event.AbstractEvent;
import org.jage.event.ObjectChangedEvent;
import org.jage.event.PropertyEvent;
import org.jage.monitor.IChangesNotifierMonitor;
import org.jage.property.functions.DefaultFunctionArgumentsResolver;
import org.jage.property.functions.IFunctionArgumentsResolver;
import org.jage.property.functions.PropertyFunction;
import org.jage.property.monitors.DefaultPropertyMonitorRule;
import org.jage.property.monitors.IPropertyMonitorRule;
import org.jage.property.monitors.AbstractPropertyMonitor;

/**
 * Abstract implementation of {@link IPropertyContainer} which implements the basic operations.
 *
 * @author AGH AgE Team
 *
 */
public abstract class AbstractPropertyContainer implements IPropertyContainer {

	/**
	 * Logger.
	 */
	protected final Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * Properties set held in this container.
	 */
	protected PropertiesSet properties;

	/**
	 * Property path parser.
	 */
	protected PropertyPathParser pathParser;

	/**
	 * Registered property monitors.
	 */
	protected HashSet<IChangesNotifierMonitor> changesNotifierMonitors;

	/**
	 * Arguments resolver.
	 */
	protected IFunctionArgumentsResolver argumentsResolver;

	/**
	 * Default constructor.
	 */
	public AbstractPropertyContainer() {
		properties = new PropertiesSet();
		changesNotifierMonitors = new HashSet<IChangesNotifierMonitor>();
		pathParser = new PropertyPathParser(this);
	}

	// BEGIN Properties Accessors Methods

	/**
	 * {@inheritDoc}
	 *
	 * @see org.jage.property.IPropertyContainer#getMetaProperties()
	 */
	public MetaPropertiesSet getMetaProperties() {
		return properties.getMetaPropertiesSet();
	}

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see org.jage.property.IPropertyContainer#getProperties()
	 */
	public IPropertiesSet getProperties() {
		return properties;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.jage.property.IPropertyContainer#getProperty(java.lang.String)
	 */
	public Property getProperty(String propertyPath) throws InvalidPropertyPathException {
		return pathParser.getPropertyForPath(propertyPath);
	}

	// END Properties Accessors Methods

	// BEGIN Property Monitors Methods

	/**
	 * {@inheritDoc}
	 *
	 * @see org.jage.monitor.IChangesNotifier#addMonitor(org.jage.monitor.IChangesNotifierMonitor)
	 */
	public void addMonitor(IChangesNotifierMonitor monitor) {
		changesNotifierMonitors.add(monitor);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.jage.monitor.IChangesNotifier#removeMonitor(org.jage.monitor.IChangesNotifierMonitor)
	 */
	public void removeMonitor(IChangesNotifierMonitor monitor) {
		changesNotifierMonitors.remove(monitor);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.jage.property.IPropertyContainer#addPropertyMonitor(java.lang.String,
	 *      org.jage.property.monitors.AbstractPropertyMonitor)
	 */
	public void addPropertyMonitor(String propertyPath, AbstractPropertyMonitor monitor)
	        throws InvalidPropertyOperationException, InvalidPropertyPathException {
		addPropertyMonitor(propertyPath, monitor, new DefaultPropertyMonitorRule());
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.jage.property.IPropertyContainer#addPropertyMonitor(java.lang.String,
	 *      org.jage.property.monitors.AbstractPropertyMonitor, org.jage.property.monitors.IPropertyMonitorRule)
	 */
	public void addPropertyMonitor(String propertyPath, AbstractPropertyMonitor monitor, IPropertyMonitorRule rule)
	        throws InvalidPropertyOperationException, InvalidPropertyPathException {
		Property property = pathParser.getPropertyForPath(propertyPath);
		property.addMonitor(monitor, rule);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.jage.property.IPropertyContainer#removePropertyMonitor(java.lang.String,
	 *      org.jage.property.monitors.AbstractPropertyMonitor)
	 */
	public void removePropertyMonitor(String propertyPath, AbstractPropertyMonitor monitor)
	        throws InvalidPropertyOperationException, InvalidPropertyPathException {
		Property property = pathParser.getPropertyForPath(propertyPath);
		property.removeMonitor(monitor);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.jage.property.IPropertyContainer#objectDeleted(org.jage.event.AbstractEvent)
	 */
	public void objectDeleted(AbstractEvent event) {
		properties.objectDeleted(event);
	}

	/**
	 * Attaches new monitors to the all monitorable properties kept in this property container.
	 */
	protected void attachMonitorsToProperties() {
		for (Property property : properties) {
			try {
				if (property.getMetaProperty().isMonitorable()) {
					property.addMonitor(new PropertyMonitorForChangesNotifier(property),
					        new DefaultPropertyMonitorRule());
				}
			} catch (InvalidPropertyOperationException ex) {
				log.error("Cannot attach monitor to monitorable property.", ex);
			}
		}
	}

	/**
	 * Notifies all attached monitors ({@link IChangesNotifierMonitor}) that this object has been changed.
	 */
	protected void notifyChangeNotiferMonitors() {
		for (IChangesNotifierMonitor monitor : changesNotifierMonitors) {
			monitor.objectChanged(this, new ObjectChangedEvent(this));
		}
	}

	// END Property Monitors Methods

	// BEGIN Property Function Methods

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see org.jage.property.IPropertyContainer#addFunction(org.jage.property.functions.PropertyFunction)
	 */
	public void addFunction(PropertyFunction function) throws DuplicatePropertyNameException {
		properties.addProperty(function);
		function.setArgumentsResolver(getFunctionArgumentsResolver());
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.jage.property.IPropertyContainer#removeFunction(org.jage.property.functions.PropertyFunction)
	 */
	public void removeFunction(PropertyFunction function) {
		if (properties.containsProperty(function.getMetaProperty().getName())) {
			properties.removeProperty(function);
			function.setArgumentsResolver(null);
		}
	}

	/**
	 * Factory method. Gets function arguments resolver for this container.
	 *
	 * @return function arguments resolver for this property container
	 */
	protected IFunctionArgumentsResolver getFunctionArgumentsResolver() {
		if (argumentsResolver == null) {
			argumentsResolver = new DefaultFunctionArgumentsResolver(this);
		}
		return argumentsResolver;
	}

	// END Property Function Methods

	/**
	 * This class is used to listen to changes in the PropertyContainer's inner properties.
	 *
	 * @author AGH AgE Team
	 *
	 */
	private class PropertyMonitorForChangesNotifier extends AbstractPropertyMonitor {

		private Property property;

		public PropertyMonitorForChangesNotifier(Property property) {
			this.property = property;
		}

		public void propertyChanged(PropertyEvent event) {
			notifyChangeNotiferMonitors();
		}

		public void ownerDeleted(AbstractEvent event) {
			try {
				property.removeMonitor(this);
			} catch (InvalidPropertyOperationException ex) {
				log.error("Cannot unregister monitor from property.", ex);
			}
		}
	}

}
