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
 * File: XMLBasedPropertyContainer.java
 * Created: 2010-06-23
 * Author: lukasik
 * $Id: XMLBasedPropertyContainer.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.property.xml;

import org.jage.property.AbstractPropertyContainer;
import org.jage.property.DuplicatePropertyNameException;
import org.jage.property.MetaPropertiesSet;
import org.jage.property.MetaProperty;
import org.jage.property.Property;

/**
 * Adapter that allows external classes, which do not implement IPropertyContainer, to perform some operations on them
 * as if they were IPropertyContainers.
 *
 * @author AGH AgE Team
 * @see XMLBasedPropertyProvider
 *
 * @since 2.4.0
 */
public class XMLBasedPropertyContainer extends AbstractPropertyContainer {

	@SuppressWarnings("unused")
	private Object realComponent;

	/**
	 * Constructor.
	 *
	 * @param metaPropertiesSet
	 *            Set of properties containing {@link XMLBasedFieldMetaProperty} and/or
	 *            {@link XMLBasedGetterSetterMetaProperty}
	 * @param instance
	 *            instance of component to be adapted
	 */
	public XMLBasedPropertyContainer(MetaPropertiesSet metaPropertiesSet, Object instance) {
		realComponent = instance;
		for (MetaProperty metaProperty : metaPropertiesSet) {
			try {
				if (metaProperty instanceof XMLBasedFieldMetaProperty) {
					properties
					        .addProperty(new XMLBasedFieldProperty((XMLBasedFieldMetaProperty)metaProperty, instance));
				} else if (metaProperty instanceof XMLBasedGetterSetterMetaProperty) {
					properties.addProperty(new XMLBasedGetterSetterProperty(
					        (XMLBasedGetterSetterMetaProperty)metaProperty, instance));
				}
			} catch (DuplicatePropertyNameException e) {
				log.error("", e);
			}
		}

		for (Property property : properties) {
			Object propertyValue = property.getValue();
			property.notifyMonitors(propertyValue);
		}

		attachMonitorsToProperties();

	}

}
