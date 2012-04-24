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
 * File: IComponentInstanceProvider.java
 * Created: 2008-10-07
 * Author: kpietak
 * $Id: IComponentInstanceProvider.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.platform.component.provider;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

/**
 * An interface which defines a component provider. The component provider is as implementation of Service Locator
 * design pattern and allows for accessing component instances by their types of name (i.e. text identificators).
 * 
 * 
 * @author AGH AgE Team
 * 
 */
public interface IComponentInstanceProvider {

	/**
	 * Returns object by name which is in the container included in adapter.
	 * 
	 * @param name
	 *            the component string name (id)
	 * @return required object or null if object name not found
	 */
	public Object getInstance(String name);

	/**
	 * Returns object by type which is in the container included in the adapter.
	 * 
	 * @param type
	 *            the component class
	 * @param <T>
	 *            the component type
	 * @return required object or null if object of this type not found
	 */
	public <T> T getInstance(Class<T> type);

	/**
	 * Returns an object that has a provided type and which is in the container included in the adapter. Additionally,
	 * when performing a look-up generic type parameters are considered.
	 * 
	 * @param type
	 *            the component class
	 * @param typeParameters
	 *            A list of type parameters.
	 * @param <T>
	 *            the component type
	 * @return required object or null if object of this type not found
	 */
	<T> T getParametrizedInstance(Class<T> type, Type[] typeParameters);

	/**
	 * Returns a collection of component instances of a given type.
	 * 
	 * @param <T>
	 *            type of returned components
	 * @param type
	 *            a class of components which should be returned; it can be the class, abstract class or any interface
	 *            implemented by the component instances
	 * @return a collection of component instances; if no component is found, the empty collection is returned
	 */
	<T> Collection<T> getInstances(Class<T> type);

	/**
	 * Returns a type of the component with the given name.
	 * 
	 * @param name
	 *            The component name.
	 * @return A class of the component.
	 */
	Class<?> getComponentType(String name);

}
