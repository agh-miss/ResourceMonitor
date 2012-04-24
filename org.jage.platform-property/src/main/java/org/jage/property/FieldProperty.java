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
 * File: AbstractFieldProperty.java
 * Created: 2010-06-23
 * Author: lukasik
 * $Id: FieldProperty.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.property;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * {@link Property} implementation that uses field to read/write property value.
 *
 * @author AGH AgE Team
 */
public class FieldProperty extends Property implements Serializable {

	private static final long serialVersionUID = 8911682340571121747L;

	private static Logger log = LoggerFactory.getLogger(FieldProperty.class);

	private Object instance;

	private FieldMetaProperty metaProperty;

	/**
	 * Creates a FieldProperty using the given AbstractFieldMetaProperty and Object instance.
	 *
	 * @param metaProperty
	 *            meta data for this property.
	 * @param instance
	 *            instance object that contains field referred to by this property.
	 */
	public FieldProperty(FieldMetaProperty metaProperty, Object instance) {
		this.metaProperty = metaProperty;
		this.instance = instance;
		initializeMonitoringStrategy();
	}

	@Override
	public final MetaProperty getMetaProperty() {
		return metaProperty;
	}

	@Override
	public final Object getValue() {
		try {
			return metaProperty.getPropertyField().get(instance);
		} catch (Exception ex) {
			log.error("Cannot get value of property " + metaProperty.getName() + ".", ex);
			return null;
		}
	}

	@Override
	public final void setValue(Object value) throws InvalidPropertyOperationException {
		try {
			metaProperty.getPropertyField().set(instance, value);
			if (getMetaProperty().isMonitorable()) {
				notifyMonitors(value);
			}
		} catch (Exception ex) {
			throw new InvalidPropertyOperationException("object type: " + value.getClass() + ", field type: "
			        + metaProperty.getPropertyField().getType(), ex);
		}
	}
}
