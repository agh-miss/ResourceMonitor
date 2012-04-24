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
 * File: IComponentDefinition.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: IComponentDefinition.java 175 2012-03-31 17:21:19Z krzywick $
 */

package org.jage.platform.component.definition;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

/**
 * A definition of a single component used in the AgE configuration.
 * <p>
 * Instances need to be serializable, so that they could be sent to remote nodes.
 *
 * @author AGH AgE Team
 */
public interface IComponentDefinition extends Serializable {

	/**
	 * Returns the name of the component described in this definition.
	 *
	 * @return The name of the component.
	 */
	public String getName();

	/**
	 * Returns a class of the component described in this definition.
	 *
	 * @return A class of the component.
	 */
	public Class<?> getType();

	/**
	 * Returns a list of generic type parameters.
	 * <p>
	 * The returned list contains parameters in the order defined in the configuration.
	 *
	 * @return A list of Type instances.
	 */
	public List<Type> getTypeParameters();

	/**
	 * Returns information about the scope of the component.
	 *
	 * @return True, if the component is in a singleton scope, false otherwise.
	 */
	public boolean isSingleton();

	/**
	 * Returns a list of value providers for all constructor arguments that were defined for this component.
	 *
	 * @return A list of single value providers.
	 */
	List<IArgumentDefinition> getConstructorArguments();

	/**
	 * Returns a list of components that were defined in this component.
	 *
	 * @return A list of component definitions.
	 */
	public List<IComponentDefinition> getInnerComponentDefinitions();
}
