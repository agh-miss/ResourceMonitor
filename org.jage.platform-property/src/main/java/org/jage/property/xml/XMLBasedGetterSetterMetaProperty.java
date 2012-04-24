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
 * File: XMLBasedGetterSetterMetaProperty.java
 * Created: 2010-06-23
 * Author: lukasik
 * $Id: XMLBasedGetterSetterMetaProperty.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.property.xml;

import java.lang.reflect.Method;

import org.jage.property.GetterSetterMetaProperty;
import org.jage.property.PropertyException;

/**
 * Meta property for XMLBasedProperty class. Doesn't support monitoring.
 *
 * @author AGH AgE Team
 *
 * @since 2.4.0
 */
public class XMLBasedGetterSetterMetaProperty extends GetterSetterMetaProperty {

	private static final long serialVersionUID = 1;

	/**
	 * Creates a read-write XMLBasedGetterSetterMetaProperty.
	 *
	 * @param name
	 *            name of the property.
	 * @param propertyClass
	 *            type of the property.
	 * @param getter
	 *            method that can be used to read property value.
	 * @param setter
	 *            method that can be used to set property value.
	 * @throws PropertyException
	 */
	public XMLBasedGetterSetterMetaProperty(String name, Class<?> propertyClass, Method getter, Method setter) throws PropertyException {
		super(name, getter, setter, false);
	}

	/**
	 * Creates a read-only XMLBasedGetterSetterMetaProperty.
	 */
	public XMLBasedGetterSetterMetaProperty(String name, Class<?> propertyClass, Method getter) throws PropertyException {
		super(name, getter, false);
	}

}
