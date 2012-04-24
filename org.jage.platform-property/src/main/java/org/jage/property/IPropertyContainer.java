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
 * File: IPropertyContainer.java
 * Created: 2010-06-23
 * Author: kmiecik
 * $Id: IPropertyContainer.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.property;

import org.jage.event.AbstractEvent;
import org.jage.monitor.IChangesNotifier;
import org.jage.property.functions.PropertyFunction;
import org.jage.property.monitors.IPropertyMonitorRule;
import org.jage.property.monitors.AbstractPropertyMonitor;

/**
 * Interface which defines a property container with property monitors and property functions mechanisms.
 *
 * @author AGH AgE Team
 */
public interface IPropertyContainer extends IChangesNotifier {

	/**
	 * Returns property with a given path.
	 *
	 * @param propertyPath
	 *            path to the property.
	 * @return Property with a given path.
	 * @throws InvalidPropertyPathException
	 *             invalid path to the property.
	 */
	public Property getProperty(String propertyPath) throws InvalidPropertyPathException;

	/**
	 * Adds new property function to the container.
	 *
	 * @param function
	 *            function to add.
	 * @throws DuplicatePropertyNameException
	 *             property or function with the same name already exists in this container.
	 */
	public void addFunction(PropertyFunction function) throws DuplicatePropertyNameException;

	/**
	 * Adds monitor to property with a given path.
	 *
	 * @param propertyPath
	 *            path to the property.
	 * @param monitor
	 *            monitor to add.
	 * @throws InvalidPropertyOperationException
	 *             property with the given path is not monitorable.
	 * @throws InvalidPropertyPathException
	 *             invalid path to the property.
	 */
	public void addPropertyMonitor(String propertyPath, AbstractPropertyMonitor monitor)
	        throws InvalidPropertyOperationException, InvalidPropertyPathException;

	/**
	 * Adds monitor with a rule to property with a given path.
	 *
	 * @param propertyPath
	 *            path to the property.
	 * @param monitor
	 *            monitor to add.
	 * @param rule
	 *            monitor rule.
	 * @throws InvalidPropertyOperationException
	 *             property with the given path is not monitorable.
	 * @throws InvalidPropertyPathException
	 *             invlaid path to the property.
	 */
	public void addPropertyMonitor(String propertyPath, AbstractPropertyMonitor monitor, IPropertyMonitorRule rule)
	        throws InvalidPropertyOperationException, InvalidPropertyPathException;

	/**
	 * Unregisters monitor from property with a given path.
	 *
	 * @param propertyPath
	 *            path to the property.
	 * @param monitor
	 *            monitor to unergister.
	 * @throws InvalidPropertyOperationException
	 *             property with the given path is not monitorable.
	 * @throws InvalidPropertyPathException
	 *             invalid path to the property.
	 */
	public void removePropertyMonitor(String propertyPath, AbstractPropertyMonitor monitor)
	        throws InvalidPropertyOperationException, InvalidPropertyPathException;

	/**
	 * Removes property function. If the function doesn't belong to this container, this method doesn't throw any
	 * exception.
	 *
	 * @param function
	 *            function to remove.
	 */
	public void removeFunction(PropertyFunction function);

	/**
	 * Returns set that stores all properties (not including subproperties).
	 *
	 * @return set that stores all properties.
	 */
	public IPropertiesSet getProperties();

	/**
	 * Returns set that stores all meta properties (not including subproperties).
	 *
	 * @return set that stores all subproperties.
	 */
	public MetaPropertiesSet getMetaProperties();

	/**
	 * Informs the container that it has been deleted.
	 *
	 * @param event
	 *            the event
	 */
	public void objectDeleted(AbstractEvent event);

}
