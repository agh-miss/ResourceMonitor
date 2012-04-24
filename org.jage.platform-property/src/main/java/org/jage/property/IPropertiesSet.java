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

/**
 * Interface for property sets.
 * @author Tomek
  */
public interface IPropertiesSet extends Iterable<Property> {

	/**
	 * Returns MetaPropertiesSet that stores metadata for all properties
	 * from the set.
	 */
	public MetaPropertiesSet getMetaPropertiesSet();
	
	/**
	 * Adds new property to the set.
	 */
	public void addProperty(Property property) throws DuplicatePropertyNameException;
	
	/**
	 * Creates new property set that stores read-only, 
	 * not-monitorable property with values read from the
	 * original set.
	 * @return
	 */
	public IPropertiesSet clonePropertyValues();
	
	/**
	 * Removes property from the set.
	 */
	public void removeProperty(Property property);
	
	/**
	 * Returns property with a given name.
	 */
	public Property getProperty(String name);
	
	/**
	 * Checks whether property with a given name is in this set.
	 * @param name name of the property.
	 * @return true, if property with the given name is in this set; otherwise, returns false.
	 */
	public boolean containsProperty(String propertyName);
	
	/**
	 * Provides instance of simple properties
	 * @return the same set of simple properties
	 */
	public IPropertiesSet getSimplePropertiesSet();
}


