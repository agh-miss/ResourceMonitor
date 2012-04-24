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
 * File: ComponentInjectorFactory.java
 * Created: 2012-03-01
 * Author: Krzywicki
 * $Id: ComponentInjectorFactory.java 175 2012-03-31 17:21:19Z krzywick $
 */

package org.jage.platform.component.pico.injector.factory;

import java.util.List;

import org.picocontainer.ComponentAdapter;

import org.jage.platform.component.definition.ComponentDefinition;
import org.jage.platform.component.pico.injector.AutowiringInjector;
import org.jage.platform.component.pico.injector.ComponentInjector;
import org.jage.platform.component.pico.injector.ComponentInstanceProviderAwareInjector;
import org.jage.platform.component.pico.injector.ConstructorInjector;
import org.jage.platform.component.pico.injector.Injector;
import org.jage.platform.component.pico.injector.PropertiesInjector;
import org.jage.platform.component.pico.injector.StatefulComponentInjector;

import com.google.common.collect.ImmutableList;

/**
 * Factory for ComponentInjectors.
 *
 * @author AGH AgE Team
 */
final class ComponentInjectorFactory implements InjectorFactory<ComponentDefinition> {

	@Override
	public <T> Injector<T> createAdapter(final ComponentDefinition definition) {
		ComponentAdapter<T> instantiator = new ConstructorInjector<T>(definition);
		List<Injector<T>> injectors = ImmutableList.<Injector<T>> of(
				new AutowiringInjector<T>(definition),
				new PropertiesInjector<T>(definition),
				new ComponentInstanceProviderAwareInjector<T>(definition),
		        new StatefulComponentInjector<T>(definition));

		return new ComponentInjector<T>(definition, instantiator, injectors);
	}
}
