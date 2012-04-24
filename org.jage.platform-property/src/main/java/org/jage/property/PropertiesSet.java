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
import java.util.HashMap;
import java.util.Iterator;

import org.jage.event.AbstractEvent;

/**
 * Set that store properties.
 *
 * @author Tomek
 */
public class PropertiesSet implements IPropertiesSet, Serializable {

	/** Required be Serializable interface */
	public static final long serialVersionUID = 2L;

	/**
	 * All properties
	 */
	private final HashMap<String, Property> _properties;

	/**
	 * Constructor.
	 */
	public PropertiesSet() {
		_properties = new HashMap<String, Property>();
	}

	/**
	 * Copy constructor.
	 *
	 * @param source
	 *            properties set from all items will be copied to the new set.
	 */
	public PropertiesSet(IPropertiesSet source) {
		_properties = new HashMap<String, Property>();

		for (MetaProperty metaProperty : source.getMetaPropertiesSet()) {
			Property property = source.getProperty(metaProperty.getName());
			_properties.put(metaProperty.getName(), property);
		}
	}

	/**
	 * Returns MetaPropertyContainer that stores metadata for all properties
	 * from the set.
	 */
	public MetaPropertiesSet getMetaPropertiesSet() {
		MetaPropertiesSet result = new MetaPropertiesSet();
		for (Property property : _properties.values()) {
			result.addMetaProperty(property.getMetaProperty());
		}
		return result;
	}

	/**
	 * Adds new property to the set.
	 */
	public void addProperty(Property property)
			throws DuplicatePropertyNameException {
		String propertyName = property.getMetaProperty().getName();
		if (_properties.containsKey(propertyName)) {
			throw new DuplicatePropertyNameException("Property with name "
					+ propertyName + " already exists in this set.");
		}
		_properties.put(property.getMetaProperty().getName(), property);
	}

	/**
	 * Creates new property set that stores read-only, not-monitorable property
	 * with values read from the original set.
	 */
	public IPropertiesSet clonePropertyValues() {
		PropertiesSet result = new PropertiesSet();
		for (Property property : _properties.values()) {
			MetaProperty metaProperty;
            try { // XXX: Copy generics!
	            metaProperty = new MetaProperty(property
	            		.getMetaProperty().getName(), property.getMetaProperty()
	            		.getPropertyClass(), false, false);
	            SimpleProperty copy = new SimpleProperty(metaProperty, property
						.getValue());
				result._properties.put(metaProperty.getName(), copy);
            } catch (PropertyException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
            }

		}
		return result;
	}

	/**
	 * Removes property from the set.
	 */
	public void removeProperty(Property property) {
		_properties.remove(property.getMetaProperty().getName());
	}

	/**
	 * Checks whether property with a given name is in this set.
	 */
	public boolean containsProperty(String propertyName) {
		return _properties.containsKey(propertyName);
	}

	/**
	 * Returns property with a given name.
	 */
	public Property getProperty(String name) {
		return _properties.get(name);
	}

	/**
	 * Required by Iterable<Property> interface. Returns iterator that can be
	 * used to iterate through all items.
	 */
	public Iterator<Property> iterator() {
		return _properties.values().iterator();
	}

	/**
	 * Method invoked when the object is no longer needed.
	 *
	 * @param event
	 */
	public void objectDeleted(AbstractEvent event) {
		for (Property property : _properties.values()) {
			property.objectDeleted(event);
		}
	}

	/**
	 * Returns number of elements in the set.
	 *
	 * @return number of elements in the set.
	 */
	public int size() {
		return _properties.size();
	}

	/**
	 * Whether this set is empty
	 * @return true if the set is empty.
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * @see org.jage.property.IPropertiesSet#getSimplePropertiesSet()
	 */
	public IPropertiesSet getSimplePropertiesSet() {
		PropertiesSet result = new PropertiesSet();
		for (Property property : _properties.values()) {
			SimpleProperty simple = property.getSimpleProperty();
			try {
				result.addProperty(simple);
			} catch (DuplicatePropertyNameException e) {
				// not possible
			}
		}
		return result;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[ PropertiesSet " + _properties.toString() + " ]";
	}
}
