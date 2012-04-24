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
 * File: AbstractGetterSetterMetaProperty.java
 * Created: 2010-06-23
 * Author: lukasik
 * $Id: GetterSetterMetaProperty.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.property;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Getter/Setter-based {@link MetaProperty} implementation.
 *
 * @author AGH AgE Team
 *
 */
public class GetterSetterMetaProperty extends MetaProperty implements Serializable {

	private static final long serialVersionUID = -2945022231525468363L;

	private static Logger log = LoggerFactory.getLogger(GetterSetterMetaProperty.class);

	/**
	 * Getter method which access the property value.
	 */
	private transient Method getter;

	/**
	 * Setter method which sets the property value.
	 */
	private transient Method setter;

	/**
	 * Name of property class.
	 */
	private String className;

	/**
	 * Getter method name.
	 */
	private String getterName;

	/**
	 * Setter method name.
	 */
	private String setterName;

	/**
	 * Creates a read-write GetterSetterMetaProperty.
	 */
	public GetterSetterMetaProperty(String name, Method getter, Method setter, boolean isMonitorable) throws PropertyException {
		super(name, getter.getGenericReturnType(), true, true, isMonitorable);
		this.getter = getter;
		this.setter = setter;
		this.getterName = getter.getName();
		this.setterName = setter.getName();
		this.className = getter.getDeclaringClass().getName();
	}

	/**
	 * Creates a read-only GetterSetterMetaProperty.
	 */
	public GetterSetterMetaProperty(String name, Method getter, boolean isMonitorable) throws PropertyException {
		super(name, getter.getGenericReturnType(), true, false, isMonitorable);
		this.getter = getter;
		this.getterName = getter.getName();
		this.className = getter.getDeclaringClass().getName();
	}

	/**
	 * Creates a write-only GetterSetterMetaProperty
	 */
	public GetterSetterMetaProperty(String name, Method setter) throws PropertyException {
		super(name, setter.getGenericParameterTypes()[0], false, true, false);
		this.setter = setter;
		this.setterName = setter.getName();
		this.className = setter.getDeclaringClass().getName();
	}

	/**
	 * Returns method that can be used to read property value.
	 *
	 * @return method that can be used to read property value.
	 */
	public Method getGetter() {
		if (getter == null) {
			recreateGetterAndSetter();
		}
		return getter;
	}

	/**
	 * Returns method that can be used to set property value.
	 *
	 * @return method that can be used to set property value.
	 */
	public Method getSetter() {
		if (setter == null) {
			recreateGetterAndSetter();
		}
		return setter;
	}

	@Override
	public GetterSetterProperty createPropertyFor(Object instance) throws InvalidPropertyOperationException {
	    return new GetterSetterProperty(this, instance);
	}

	private void recreateGetterAndSetter() {
		try {
			Class<?> aClass = Class.forName(className);
			if (getterName != null) {
				getter = aClass.getMethod(getterName, new Class[0]);
			}
			if (setterName != null) {
				setter = aClass.getMethod(setterName, new Class[] { getPropertyClass() });
			}
		} catch (Exception ex) {
			log.error("Could not recreate getter and setter", ex);
		}
	}
}
