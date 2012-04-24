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
 * File: XMLBasedGetterSetterProperty.java
 * Created: 2010-06-23
 * Author: lukasik
 * $Id: XMLBasedGetterSetterProperty.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.property.xml;

import org.jage.property.GetterSetterProperty;

/**
 * <code>AbstractFieldProperty</code> implementation that uses getter and setter to read / write property value.
 *
 * @author AGH AgE Team
 *
 * @since 2.4.0
 */
public class XMLBasedGetterSetterProperty extends GetterSetterProperty {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 *
	 * @param metaProperty
	 *            meta property for this property.
	 * @param instance
	 *            object for which the property should be created.
	 */
	public XMLBasedGetterSetterProperty(XMLBasedGetterSetterMetaProperty metaProperty, Object instance) {
		super(metaProperty, instance);
	}

}
