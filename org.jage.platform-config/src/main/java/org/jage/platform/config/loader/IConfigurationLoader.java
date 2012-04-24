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
 * File: IConfigurationLoader.java
 * Created: 2010-06-08
 * Author: kpietak
 * $Id: IConfigurationLoader.java 17 2012-01-29 15:02:49Z krzywick $
 */

package org.jage.platform.config.loader;

import java.util.Collection;

import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.component.definition.IComponentDefinition;

/**
 * The service responsible for loading configuration from a given source. The loaded configuration is returned to the
 * service client.
 *
 * @since 2.5.0
 *
 * @author AGH AgE Team
 */
public interface IConfigurationLoader {

	/**
	 * Loads a configuration from a given source.
	 *
	 * @param source
	 *            object containing configuration in a source format or a connector to configuration; type of the source
	 *            is dependent on the particular implementation
	 * @return loaded configuration as collection of {@link IComponentDefinition} objects
	 * @throws ConfigurationException
	 *             when any error occurs during loading the configuration; particular implementation define the cases
	 *             when exception is thrown
	 */
	Collection<IComponentDefinition> loadConfiguration(Object source) throws ConfigurationException;
}
