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
package org.jage.property.annotations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.jage.property.DuplicatePropertyNameException;
import org.jage.property.IClassPropertiesFactory;
import org.jage.property.InvalidPropertyDefinitionException;
import org.jage.property.InvalidPropertyOperationException;
import org.jage.property.MetaPropertiesSet;
import org.jage.property.MetaProperty;
import org.jage.property.PropertiesSet;
import org.jage.property.PropertyException;

public final class ClassPropertiesFactory implements IClassPropertiesFactory {

	private final HashMap<Class<?>, Set<MetaProperty>> classMetaProperties = new HashMap<Class<?>, Set<MetaProperty>>();

	private final IPropertiesReader propertiesReader;

	public ClassPropertiesFactory(IPropertiesReader propertiesReader) {
		this.propertiesReader = propertiesReader;
	}

	@Override
	public PropertiesSet getAllProperties(Object object) throws InvalidPropertyDefinitionException {
		try {
			Set<MetaProperty> metaProperties = loadMetaPropertiesFor(object.getClass());

			PropertiesSet propertiesSet = new PropertiesSet();
			for (MetaProperty metaProperty : metaProperties) {
				propertiesSet.addProperty(metaProperty.createPropertyFor(object));
			}

			return propertiesSet;
		} catch (InvalidPropertyOperationException ex) {
			throw new InvalidPropertyDefinitionException(String.format("Unable to create a property for class %s.",
			        object.getClass()), ex);
		} catch (DuplicatePropertyNameException ex) {
			throw new InvalidPropertyDefinitionException(String.format(
			        "Properties with duplicated name exist in class %s.", object.getClass()), ex);
		} catch (PropertyException e) {
			throw new InvalidPropertyDefinitionException(e);
        }
	}

	@Override
	public MetaPropertiesSet getAllMetaProperties(Object object) throws InvalidPropertyDefinitionException {
		try {
			Set<MetaProperty> metaProperties = loadMetaPropertiesFor(object.getClass());

			MetaPropertiesSet metaPropertiesSet = new MetaPropertiesSet();
			metaPropertiesSet.addAllMetaProperties(metaProperties);

			return metaPropertiesSet;
		} catch (PropertyException e) {
			throw new InvalidPropertyDefinitionException(e);
        }
	}

	private Set<MetaProperty> loadMetaPropertiesFor(Class<?> clazz) throws InvalidPropertyDefinitionException, PropertyException {
		Set<MetaProperty> metaProperties = classMetaProperties.get(clazz);
		if (metaProperties == null) {
			metaProperties = new HashSet<MetaProperty>();
			metaProperties.addAll(propertiesReader.readFieldMetaProperties(clazz));
			metaProperties.addAll(propertiesReader.readGetterSetterMetaProperties(clazz));
			classMetaProperties.put(clazz, metaProperties);
		}

		return metaProperties;
	}
}
