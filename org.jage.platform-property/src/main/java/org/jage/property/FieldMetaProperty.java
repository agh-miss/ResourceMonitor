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
 * File: AbstractFieldMetaProperty.java
 * Created: 2010-06-23
 * Author: lukasik
 * $Id: FieldMetaProperty.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.property;

import java.io.Serializable;
import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Field-based {@link MetaProperty} implementation. By definition always writeable.
 *
 * @author AGH AgE Team
 */
public class FieldMetaProperty extends MetaProperty implements Serializable {

	private static final long serialVersionUID = 182352715318861310L;

	private static Logger log = LoggerFactory.getLogger(FieldMetaProperty.class);

	/**
	 * Field which holds the property value.
	 */
	private transient Field propertyField;

	/**
	 * Name of the property class.
	 */
	private String className;

	/**
	 * Name of the field which holds the property.
	 */
	private String fieldName;

	/**
	 * Constructor.
	 *
	 * @param name
	 *            name of the property.
	 * @param field
	 *            field that stores property value.
	 * @param isMonitorable
	 *            indicates whether this property is monitorable.
	 * @throws PropertyException
	 */
	public FieldMetaProperty(String name, Field field, boolean isMonitorable) throws PropertyException {
		super(name, field.getGenericType(), true, true, isMonitorable);
		this.propertyField = field;
		this.propertyField.setAccessible(true);
		this.className = propertyField.getDeclaringClass().getName();
		this.fieldName = propertyField.getName();
	}

	/**
	 * Constructor.
	 *
	 * @param name
	 *            the name
	 * @param propertyClass
	 *            the property class
	 * @param isMonitorable
	 *            the is monitorable
	 * @throws PropertyException
	 */
	public FieldMetaProperty(String name, Class<?> propertyClass, boolean isMonitorable) throws PropertyException {
		super(name, propertyClass, true, true, isMonitorable);
	}

	@Override
	public FieldProperty createPropertyFor(Object instance) throws InvalidPropertyOperationException {
	    return new FieldProperty(this, instance);
	}

	/**
	 * Returns field that stores property value.
	 *
	 * @return field that stores property value.
	 */
	public final Field getPropertyField() {
		if (propertyField == null) {
			try {
				Class<?> aClass = Class.forName(className);
				propertyField = aClass.getField(fieldName);
			} catch (Exception ex) {
				log.error(ex.getMessage(), ex);
			}
		}
		return propertyField;
	}

}
