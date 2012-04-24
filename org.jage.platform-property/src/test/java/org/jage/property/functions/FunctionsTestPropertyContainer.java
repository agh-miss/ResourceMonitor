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
package org.jage.property.functions;

import java.util.ArrayList;
import java.util.List;

import org.jage.event.AbstractEvent;
import org.jage.monitor.IChangesNotifierMonitor;
import org.jage.property.DuplicatePropertyNameException;
import org.jage.property.IPropertiesSet;
import org.jage.property.IPropertyContainer;
import org.jage.property.InvalidPropertyOperationException;
import org.jage.property.InvalidPropertyPathException;
import org.jage.property.MetaPropertiesSet;
import org.jage.property.MetaProperty;
import org.jage.property.PropertiesSet;
import org.jage.property.Property;
import org.jage.property.PropertyException;
import org.jage.property.PropertyPathParser;
import org.jage.property.monitors.DefaultPropertyMonitorRule;
import org.jage.property.monitors.IPropertyMonitorRule;
import org.jage.property.monitors.AbstractPropertyMonitor;
import org.junit.Ignore;

@Ignore
public class FunctionsTestPropertyContainer implements IFunctionArgumentsResolver,
		IPropertyContainer {

	private PropertiesSet _properties;
	private PropertyPathParser _pathParser;

	public FunctionsTestPropertyContainer() {
		_properties = new PropertiesSet();
		_pathParser = new PropertyPathParser(this);
	}

	public void addProperty(String name, Object value)
			throws DuplicatePropertyNameException {
		_properties.addProperty(new SimpleProperty(name, value));
	}

	public void addFunction(PropertyFunction function)
			throws DuplicatePropertyNameException {
		_properties.addProperty(function);
	}

	public void removeFunction(PropertyFunction function) {
		_properties.removeProperty(function);
	}

	public List<FunctionArgument> resolveArguments(String argumentsPattern) {
		ArrayList<FunctionArgument> result = new ArrayList<FunctionArgument>();

		for (Property property : _properties)
			if (matchProperty(argumentsPattern, property))
				result.add(new FunctionArgument(this, property
						.getMetaProperty().getName()));

		return result;
	}

	private boolean matchProperty(String argumentsNames, Property property) {
		return property.getMetaProperty().getName().startsWith(argumentsNames);
	}

	public MetaPropertiesSet getMetaPropertyContainer(
			boolean includeSubproperties) {
		return _properties.getMetaPropertiesSet();
	}

	public MetaProperty getMetaProperty(String propertyPath)
			throws InvalidPropertyOperationException,
			InvalidPropertyPathException {
		return getProperty(propertyPath).getMetaProperty();
	}

	public Property getProperty(String propertyPath)
			throws InvalidPropertyPathException {
		return _pathParser.getPropertyForPath(propertyPath);
	}

	public void addPropertyMonitor(String propertyPath, AbstractPropertyMonitor monitor)
			throws InvalidPropertyOperationException,
			InvalidPropertyPathException {
		getProperty(propertyPath).addMonitor(monitor,
				new DefaultPropertyMonitorRule());
	}

	public void addPropertyMonitor(String propertyPath,
			AbstractPropertyMonitor monitor, IPropertyMonitorRule rule)
			throws InvalidPropertyOperationException,
			InvalidPropertyPathException {
		getProperty(propertyPath).addMonitor(monitor, rule);

	}

	public void removePropertyMonitor(String propertyPath,
			AbstractPropertyMonitor monitor) throws InvalidPropertyOperationException,
			InvalidPropertyPathException {
		getProperty(propertyPath).removeMonitor(monitor);
	}

	public MetaPropertiesSet getMetaPropertyContainer() {
		return _properties.getMetaPropertiesSet();
	}

	public void addMonitor(IChangesNotifierMonitor monitor) {

	}

	public void removeMonitor(IChangesNotifierMonitor monitor) {

	}

	public IPropertiesSet getProperties() {
		return _properties;
	}

	public MetaPropertiesSet getMetaProperties() {
		return _properties.getMetaPropertiesSet();
	}

	public void objectDeleted(AbstractEvent event) {
	}

	private class SimpleProperty extends Property {

		private Object _value;
		private String _name;

		public SimpleProperty(String name, Object value) {
			_name = name;
			_value = value;
		}

		@Override
		public MetaProperty getMetaProperty() {
			Class<?> propertyClass = (_value == null ? Object.class : _value
					.getClass());
			try {
	            return new MetaProperty(_name, propertyClass, true, false);
            } catch (PropertyException e) {
	            throw new RuntimeException(e);
            }
		}

		@Override
		public Object getValue() {
			return _value;
		}

		@Override
		public void setValue(Object value)
				throws InvalidPropertyOperationException {
			_value = value;
		}
	}

}
