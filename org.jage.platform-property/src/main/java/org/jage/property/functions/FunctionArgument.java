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

import org.jage.property.IPropertyContainer;
import org.jage.property.InvalidPropertyPathException;
import org.jage.property.MetaProperty;

/**
 * A single argument for function. It is a pair of property container and name
 * of property within that container.
 * @author Tomek
 *
 */
public class FunctionArgument {
	private IPropertyContainer _container;
	private String _propertyPath;
	
	/**
	 * Constructor.
	 * @param container the property container.
	 * @param propertyPath path for property within the property container.
	 */
	public FunctionArgument(IPropertyContainer container, String propertyPath) {
		_container = container;
		_propertyPath = propertyPath;
	}
	
	/**
	 * Gets the property container.
	 * @return
	 */
	public IPropertyContainer getPropertyContainer() {
		return _container;
	}
	
	/**
	 * Gets path for property within the property container.
	 * @return
	 */
	public String getPropertyPath() {
		return _propertyPath;
	}
	
	/**
	 * Gets value of the argument. 
	 * @return
	 * @throws InvalidPropertyPathException
	 */
	public Object getValue() throws InvalidPropertyPathException {
		return _container.getProperty(_propertyPath).getValue();
	}

	/**
	 * Gets metadata for argument's value.
	 * @return metadata for argument's value.
	 * @throws InvalidPropertyPathException
	 */
	public MetaProperty getMetaProperty() throws InvalidPropertyPathException {
		return _container.getProperty(_propertyPath).getMetaProperty();
	}
}

