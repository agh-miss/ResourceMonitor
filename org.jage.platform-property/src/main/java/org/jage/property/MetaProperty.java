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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Stores meta data for a property.
 *
 * @author AGH AgE Team
 */
public class MetaProperty implements Serializable {

	private static Logger log = LoggerFactory.getLogger(MetaProperty.class);

	private static final long serialVersionUID = 2;

	private final String name;

	private final boolean isReadable;

	private final boolean isWriteable;

	private final boolean isMonitorable;

	/**
	 * Name of class
	 */
	private final String propertyClassName;

	/**
	 * Generic types for collections : MapDefinition, SetDefinition, MapDefinition for ListDefinition, SetDefinition:
	 * [class] for MapDefinition : [keyClass, valueClass]
	 */
	private List<Class<?>> genericTypes = new LinkedList<Class<?>>(); // FIXME: AGE-29 will require using Type

	/**
	 * Class of this property. If null, must be recovered from {@link #propertyClassName}
	 */
	private transient Class<?> propertyClass;

	/**
	 * Constructs a new instance of MetaProperty.
	 *
	 * @param name
	 *            name of the property.
	 * @param propertyType
	 *            a generic type of the property.
	 * @param isWriteable
	 *            indicates whether this property is writeable.
	 * @param isMonitorable
	 *            indicates whether this property is monitorable.
	 * @throws PropertyException
	 */
	public MetaProperty(String name, Type propertyType, boolean isReadable, boolean isWriteable, boolean isMonitorable)
	        throws PropertyException {
		this.name = name;

		if (propertyType instanceof Class<?>) {
			this.propertyClass = (Class<?>)propertyType;
		} else if (propertyType instanceof ParameterizedType) {
			ParameterizedType propertyParametrizedType = (ParameterizedType)propertyType;
			Type[] propertyTypes = propertyParametrizedType.getActualTypeArguments();
			List<Class<?>> genericClasses = new LinkedList<Class<?>>();
			// TODO: AGE-29 fix it for nested generic constructions such as List<Set<String>>
			for (Type type : propertyTypes) {
				if (type instanceof Class<?>) {
					genericClasses.add((Class<?>)type);
				} else {
					genericClasses.add(Object.class); // XXX: AGE-29
				}
			}

			if (propertyParametrizedType.getRawType() instanceof Class<?>) {
				this.propertyClass = (Class<?>)propertyParametrizedType.getRawType();
				this.genericTypes = genericClasses;

			} else {
				throw new PropertyException(String.format(
				        "Property %1$s: %2$s - the configuraiton mechanism does not support such nested types.", name,
				        propertyParametrizedType));
			}

		} else if (propertyType instanceof TypeVariable<?>) {
			// TODO: Add more support for generic fields XXX
			this.propertyClass = Object.class;
		} else {
			throw new PropertyException(String.format("Property %1$s: %2$s - type not supported.", name, propertyType));
		}

		this.propertyClassName = propertyClass.getClass().getName();
		this.isReadable = isReadable;
		this.isWriteable = isWriteable;
		this.isMonitorable = isMonitorable;
	}

	public MetaProperty(String name, Type propertyType, boolean isWriteable, boolean isMonitorable) throws PropertyException {
		this(name, propertyType, true, isWriteable, isMonitorable);
	}

	public MetaProperty(String name, Type propertyClass) throws PropertyException {
		this(name, propertyClass, true, true, true);
	}

	/**
	 * Copy constructor.
	 *
	 * @param source
	 *            original meta property.
	 */
	public MetaProperty(MetaProperty source) {
		name = source.getName();
		propertyClass = source.getPropertyClass();
		propertyClassName = propertyClass.getClass().getName();
		genericTypes = source.getGenericClasses();
		isReadable = source.isReadable;
		isWriteable = source.isWriteable();
		isMonitorable = source.isMonitorable();
	}

	/**
	 * Returns name of the property.
	 *
	 * @return name of the property.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns type (class) of the property.
	 *
	 * @return type (class) of the property.
	 */
	public Class<?> getPropertyClass() {
		if (propertyClass == null) {
			try {
				propertyClass = Class.forName(propertyClassName);
			} catch (ClassNotFoundException e) {
				log.error("Can't recover property class.", e);
			}
		}
		return propertyClass;
	}

	/**
	 * Returns generic types associated with this property.
	 *
	 * @return A list of generic types (never null).
	 */
	public List<Class<?>> getGenericClasses() {
		return genericTypes;
	}

	/**
	 * Returns true, if the property is monitorable; otherwise, returns false.
	 *
	 * @return true, if the property is monitorable; otherwise, returns false.
	 */
	public boolean isMonitorable() {
		return isMonitorable;
	}

	/**
	 * Returns true, if the property is writeable; otherwise, returns false.
	 *
	 * @return true, if the property is writeable; otherwise, returns false.
	 */
	public boolean isWriteable() {
		return isWriteable;
	}

	/**
	 * Returns true, if the property is readable; otherwise, returns false.
	 *
	 * @return true, if the property is readable; otherwise, returns false.
	 */
	public boolean isReadable() {
		return isReadable;
	}

	/**
	 * Creates a property, based on this metaProperty and the given object.
	 * @param instance An object on which a property will be created.
	 * @return The property
	 * @throws InvalidPropertyOperationException if the instance's type is unsupported for this metaProperty.
	 */
	public Property createPropertyFor(Object instance) throws InvalidPropertyOperationException {
		throw new UnsupportedOperationException("Subclasses are meant to provide this functionality");
	}

	@Override
    public String toString() {
		return String.format("[MetaProperty: name=%s; type=%s]", name, propertyClass);
	}
}
