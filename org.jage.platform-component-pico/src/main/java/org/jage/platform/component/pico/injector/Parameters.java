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
 * File: Parameters.java
 * Created: 2012-03-27
 * Author: Krzywicki
 * $Id: Parameters.java 172 2012-03-30 16:08:11Z krzywick $
 */

package org.jage.platform.component.pico.injector;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static java.lang.String.format;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.NameBinding;
import org.picocontainer.Parameter;
import org.picocontainer.PicoCompositionException;
import org.picocontainer.PicoContainer;
import org.picocontainer.injectors.AbstractInjector.UnsatisfiableDependenciesException;
import org.picocontainer.parameters.BasicComponentParameter;
import org.picocontainer.parameters.ConstantParameter;

import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.component.definition.IArgumentDefinition;
import org.jage.platform.component.definition.ReferenceDefinition;
import org.jage.platform.component.definition.SimpleTypeParsers;
import org.jage.platform.component.definition.ValueDefinition;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Utilities to consistently create Parameters.
 *
 * @author AGH AgE Team
 */
public final class Parameters {

	private Parameters() {
	}

	public static List<Parameter> fromList(final List<IArgumentDefinition> argumentDefinitions) {
		List<Parameter> parameters = Lists.newArrayListWithCapacity(argumentDefinitions.size());
		for (IArgumentDefinition argumentDefinition : argumentDefinitions) {
			parameters.add(from(argumentDefinition));
		}
		return parameters;
	}

	public static Map<Parameter, Parameter> fromMap(
	        final Map<IArgumentDefinition, IArgumentDefinition> argumentDefinitions) {
		Map<Parameter, Parameter> parameters = Maps.newHashMapWithExpectedSize(argumentDefinitions.size());
		for (Entry<IArgumentDefinition, IArgumentDefinition> e : argumentDefinitions.entrySet()) {
			parameters.put(from(e.getKey()), from(e.getValue()));
		}
		return parameters;
	}

	public static <K> Map<K, Parameter> fromMapping(final Map<K, IArgumentDefinition> mapping) {
		Map<K, Parameter> parameters = Maps.newHashMapWithExpectedSize(mapping.size());
		for (Entry<K, IArgumentDefinition> e : mapping.entrySet()) {
			parameters.put(e.getKey(), from(e.getValue()));
		}
		return parameters;
	}

	public static Parameter[] from(final Class<?>[] types) {
		final Parameter[] parameters = new Parameter[types.length];
		for (int i = 0; i < parameters.length; i++) {
			parameters[i] = from(types[i]);
		}
		return parameters;
	}

	public static Parameter from(final IArgumentDefinition argumentDefinition) {
		if (argumentDefinition instanceof ReferenceDefinition) {
			ReferenceDefinition referenceDefinition = (ReferenceDefinition)argumentDefinition;
			return new StrictComponentParameter(referenceDefinition.getTargetName());
		} else {
			ValueDefinition valueDefinition = (ValueDefinition)argumentDefinition;
			try {
				Object value = SimpleTypeParsers.parse(valueDefinition.getStringValue(),
				        valueDefinition.getDesiredClass());
				return new ConstantParameter(value);
			} catch (ConfigurationException e) {
				throw new PicoCompositionException(e);
			}
		}
	}

	public static Parameter from(final Class<?> type) {
		return new StrictComponentParameter(type);
	}

	static final class StrictComponentParameter extends BasicComponentParameter {

		private static final long serialVersionUID = 1L;

		private final Object key;

		public StrictComponentParameter(final Object key) {
			this.key = checkNotNull(key);
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		protected <T> ComponentAdapter<T> resolveAdapter(final PicoContainer container, final ComponentAdapter adapter,
		        final Class<T> expectedType, final NameBinding expectedNameBinding, final boolean useNames,
		        final Annotation binding) {
			// First search by value
			ComponentAdapter<T> result = (ComponentAdapter<T>)container.getComponentAdapter(key);
			if (result == null && (key instanceof Class)) {
				// Otherwise, if the key is a class, try to look up by type
				result = container.getComponentAdapter((Class<T>)key, (NameBinding)null);
			}

			if (result == null || !expectedType.isAssignableFrom(result.getComponentImplementation())) {
				throw new UnsatisfiableDependenciesException(format("unsatisfied dependency '%1$s' in %2$s ", key,
				        container));
			}
			return result;
		}
	}
}
