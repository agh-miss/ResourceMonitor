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
 * File: MapInjectorFactory.java
 * Created: 2012-03-01
 * Author: Krzywicki
 * $Id: MapInjectorFactory.java 175 2012-03-31 17:21:19Z krzywick $
 */

package org.jage.platform.component.pico.injector.factory;

import org.jage.platform.component.definition.MapDefinition;
import org.jage.platform.component.pico.injector.Injector;
import org.jage.platform.component.pico.injector.MapInjector;

/**
 * Factory for MapInjectors.
 *
 * @author AGH AgE Team
 */
final class MapInjectorFactory implements InjectorFactory<MapDefinition> {
	@Override
	public <T> Injector<T> createAdapter(final MapDefinition definition) {
		return new MapInjector<T>(definition);
	}
}
