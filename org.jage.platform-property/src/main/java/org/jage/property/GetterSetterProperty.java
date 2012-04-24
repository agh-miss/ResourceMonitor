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
 * File: AbstractGetterSetterProperty.java
 * Created: 2010-06-23
 * Author: lukasik
 * $Id: GetterSetterProperty.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.property;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link Property} implementation that uses methods to read/write property value.
 *
 * @author AGH AgE Team
 */
public class GetterSetterProperty extends Property implements Serializable {

	private static final long serialVersionUID = -81066180816156488L;

	private static Logger log = LoggerFactory.getLogger(GetterSetterProperty.class);

	/**
	 * An object that owns the getter and setter methods which access/set the property value.
	 */
	private Object instance;

	/**
	 * Meta property which describes the type of this property.
	 */
	private GetterSetterMetaProperty metaProperty;

	/**
	 * Constructor.
	 *
	 * @param metaProperty
	 *            meta property for this property.
	 * @param instance
	 *            object for which the property should be created.
	 */
	public GetterSetterProperty(GetterSetterMetaProperty metaProperty, Object instance) {
		this.metaProperty = metaProperty;
		this.instance = instance;
		initializeMonitoringStrategy();
	}

	@Override
	public MetaProperty getMetaProperty() {
		return metaProperty;
	}

	@Override
	public Object getValue() {
		try {
			Method getter = metaProperty.getGetter();
			return getter.invoke(instance, new Object[0]);
		} catch (Exception ex) {
			log.error("Cannot get value of property " + metaProperty.getName(), ex);
			return null;
		}
	}

	@Override
	public void setValue(Object value) throws InvalidPropertyOperationException {
		if (!metaProperty.isWriteable()) {
			throw new InvalidPropertyOperationException("Property: " + metaProperty.getName() + " is not writeable.");
		}

		try {
			Method setter = metaProperty.getSetter();
			setter.invoke(instance, new Object[] { value });
		} catch (Exception ex) {
			throw new InvalidPropertyOperationException(ex);
		}
	}
}
