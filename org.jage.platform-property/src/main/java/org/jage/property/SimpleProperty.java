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

import java.io.Serializable;

/**
 * Simple Property implementation. It was desinged for QueryableQueryResult class,
 * but can be used everywhere else.
 * @author Tomek
 */
public class SimpleProperty extends Property implements Serializable {

	private static final long serialVersionUID = 1;
	
	/**
	 * Value
	 */
	private Object _value;
	
	/**
	 * Meta property
	 */
	private MetaProperty _metaProperty;

	/**
	 * Constructor. 
	 * @param metaProperty metadata for the property. It cannot be null.
	 * @param value value of the property.
	 */
	public SimpleProperty(MetaProperty metaProperty, Object value) {
		if (metaProperty == null) {
			throw new NullPointerException("MetaProperty cannot be null.");
		}
		_metaProperty = new MetaProperty(metaProperty);
		_value = value;
	}

	/**
	 * Returns metadata for this property.
	 * @return metadata for this property.
	 */
	@Override
	public MetaProperty getMetaProperty() {
		return _metaProperty;
	}

	/**
	 * Returns value of this property.
	 * @return value of this property.
	 * @exception InvalidPropertyOperationException
	 */
	@Override
	public Object getValue() {
		return _value;
	}

	/**
	 * Sets value of this property.
	 * @param value new value.
	 * @throws InvalidPropertyOperationException property is read-only.
	 */
	@Override
	public void setValue(Object value) throws InvalidPropertyOperationException {
		if (!_metaProperty.isWriteable()) {
			throw new InvalidPropertyOperationException("Property is not writeable.");
		}
		_value = value;
	}
}


