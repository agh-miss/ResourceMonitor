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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Set that stores meta properties.
 * @author Tomek
 */
public class MetaPropertiesSet implements Iterable<MetaProperty> {

	private HashMap<String, MetaProperty> _metaProperties;

	/** Constructor. */
	public MetaPropertiesSet() {
		_metaProperties = new HashMap<String, MetaProperty>();
	}

	/**
	 * Adds new meta property to this set.
	 * @param metaProperty meta property to add.
	 */
	public void addMetaProperty(MetaProperty metaProperty) {
		_metaProperties.put(metaProperty.getName(), metaProperty);
	}

	/**
	 * Add all the metaProperties from a collection to this set.
	 * @param metaProperties A collection of metaProperties
	 */
	public void addAllMetaProperties(Collection<? extends MetaProperty> metaProperties) {
		for (MetaProperty metaProperty : metaProperties) {
			addMetaProperty(metaProperty);
		}
	}

	/**
	 * Checks whether meta property with a given name is within this set.
	 * @param propertyName name of the meta property to find.
	 * @return true, if the meta property with the given name was found in this set;
	 * otherwise, returns false.
	 */
	public boolean hasMetaProperty(String propertyName) {
		return _metaProperties.containsKey(propertyName);
	}

	/**
	 * Removes meta property from this set.
	 * @param metaProperty meta property to remove.
	 */
	public void removeMetaProperty(MetaProperty metaProperty) {
		_metaProperties.remove(metaProperty.getName());
	}

	/**
	 * Returns meta property with a givne name.
	 * @param propertyName name of the meta property.
	 * @return meta property with the given name.
	 */
	public MetaProperty getMetaProperty(String propertyName) {
		return _metaProperties.get(propertyName);
	}

	/**
	 * Required by Iterable<MetaProperty> interface. Returns iterator that can be used
	 * to iterate through all meta properties from this set.
	 */
	@Override
    public Iterator<MetaProperty> iterator() {
		return _metaProperties.values().iterator();
	}

	/**
	 * Gets size of the set.
	 * @return number of items in this set.
	 */
	public int size() {
		return _metaProperties.size();
	}

	/**
	 * Whether this set is empty
	 * @return true if the set is empty.
	 */
	public boolean isEmpty() {
		return size() == 0;
	}
}


