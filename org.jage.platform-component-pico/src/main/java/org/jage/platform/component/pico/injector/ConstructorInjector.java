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
 * File: ConstructorInjector.java
 * Created: 2012-03-12
 * Author: Krzywicki
 * $Id: ConstructorInjector.java 213 2012-04-08 20:21:35Z krzywick $
 */

package org.jage.platform.component.pico.injector;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.Parameter;
import org.picocontainer.Parameter.Resolver;
import org.picocontainer.PicoCompositionException;
import org.picocontainer.PicoContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.platform.component.annotation.Inject;
import org.jage.platform.component.definition.ComponentDefinition;
import org.jage.platform.component.definition.IArgumentDefinition;

import static org.jage.platform.component.pico.injector.Parameters.fromList;
import static org.jage.platform.reflect.Classes.getDeclaredConstructors;
import static org.jage.platform.reflect.predicates.ConstructorPredicates.matchingActualParameters;
import static org.jage.platform.reflect.predicates.ConstructorPredicates.withAnnotation;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.getOnlyElement;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.newArrayListWithCapacity;

/**
 * A constructor injector. It injects dependencies using explicit arguments, if such are provided, or tries to autowire
 * them, if some constructor is marked for autowiring, or falls back on an empty, default constructor otherwise.
 * <p>
 * When looking for constructors matching some list of parameters, an isAssignableTo relation is used instead of an
 * equality one. This way, actual parameters can be upcasted to match more general formal ones. Auto boxing/unboxing
 * primitives is also supported.
 *
 * @param <T>
 *            the type of components this injector can create
 *
 * @author AGH AgE Team
 */
public final class ConstructorInjector<T> extends AbstractInjector<T> {

	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(ConstructorInjector.class);

	private final List<Parameter> parameters;

	private String postponedError;

	/**
	 * Constructs a new ConstructorInjector that uses a given component definition.
	 *
	 * @param definition
	 *            the component definition to use to create components
	 */
	public ConstructorInjector(final ComponentDefinition definition) {
		super(definition);
		parameters = initializeParameters(definition);
	}

	private List<Parameter> initializeParameters(final ComponentDefinition definition) {
		final List<IArgumentDefinition> explicitArguments = definition.getConstructorArguments();
		if (!explicitArguments.isEmpty()) {
			return fromList(explicitArguments);
		} else {
			@SuppressWarnings("unchecked")
			final Class<T> componentType = (Class<T>)getComponentImplementation();
			final List<Constructor<T>> constructors = getDeclaredConstructors(componentType);
			final List<Constructor<T>> annotated = newArrayList(filter(constructors, withAnnotation(Inject.class)));

			if (!annotated.isEmpty()) {
				if (annotated.size() > 1) {
					postponedError = "At most one constructor can be annotated with @Inject";
				} else {
					return asList(Parameters.from(getOnlyElement(annotated).getParameterTypes()));
				}
			}
		}
		return Collections.emptyList();
	}

	@Override
	protected void doVerify(final PicoContainer container) throws PicoCompositionException {
		log.debug("Verifing ConstructorInjector for component with key {} and type {}", getComponentKey(),
		        getComponentImplementation());

		// First check if there are no postponed errors from the constructor
		checkForPostponedErrors();

		// Then check if all parameters are resolvable
		for (Parameter parameter : parameters) {
			parameter.verify(container, this, Object.class, null, false, null);
		}

		// Finally check if there is a matching constructor
		List<Class<?>> parameterTypes = getParameterTypes(parameters, container);
		findOnlyMatchingConstructor(getComponentImplementation(), parameterTypes);
	}

	@Override
	protected T doCreate(final PicoContainer container) throws PicoCompositionException {
		log.debug("ConstructorInjector is creating component with key {} and type {}", getComponentKey(),
		        getComponentImplementation());

		checkForPostponedErrors();

		List<Object> argumentValues = resolveAllInstances(parameters, container);
		List<Class<?>> argumentTypes = getArgumentTypes(argumentValues);
		Constructor<? extends T> constructor = findOnlyMatchingConstructor(getComponentImplementation(), argumentTypes);

		log.trace("Injecting arguments {} of types {} in constructor {}", new Object[] { argumentValues, argumentTypes,
		        constructor });
		return doCreateInstance(constructor, argumentValues.toArray());
	}

	private void checkForPostponedErrors() {
		if (postponedError != null) {
			throw new PicoCompositionException(postponedError);
		}
	}

	private Resolver resolve(final Parameter parameter, final PicoContainer container) {
		return parameter.resolve(container, this, null, Object.class, null, false, null);
	}

	private List<Class<?>> getParameterTypes(final List<Parameter> parameters, final PicoContainer container) {
		List<Class<?>> parameterTypes = newArrayListWithCapacity(parameters.size());
		for (Parameter parameter : parameters) {
			Resolver resolver = resolve(parameter, container);
			ComponentAdapter<?> adapter = resolver.getComponentAdapter();
			if (adapter != null) {
				parameterTypes.add(adapter.getComponentImplementation());
			} else {
				parameterTypes.add(resolver.resolveInstance().getClass());
			}
		}
		return parameterTypes;
	}

	private List<Object> resolveAllInstances(final List<Parameter> parameters, final PicoContainer container) {
		final List<Object> resolvers = newArrayListWithCapacity(parameters.size());
		for (Parameter parameter : parameters) {
			resolvers.add(resolve(parameter, container).resolveInstance());
		}
		return resolvers;
	}

	private List<Class<?>> getArgumentTypes(final List<Object> argumentsValues) {
		final List<Class<?>> argumentTypes = newArrayListWithCapacity(argumentsValues.size());
		for (final Object value : argumentsValues) {
			argumentTypes.add(value.getClass());
		}
		return argumentTypes;
	}

	private <TT> Constructor<TT> findOnlyMatchingConstructor(final Class<TT> componentType,
	        final List<Class<?>> argumentsTypes) throws PicoCompositionException {
		List<Constructor<TT>> constructors = getDeclaredConstructors(componentType);
		final List<Constructor<TT>> matching = newArrayList(filter(constructors,
		        matchingActualParameters(argumentsTypes)));

		if (matching.isEmpty()) {
			throw new PicoCompositionException("No matching constructor found for arguments types: " + argumentsTypes);
		} else if (matching.size() > 1) {
			throw new PicoCompositionException("Multiple ambiguous constructors found for arguments types: "
			        + argumentsTypes + " : " + matching);
		}

		return getOnlyElement(matching);
	}

	private <TT> TT doCreateInstance(final Constructor<TT> constructor, final Object[] arguments)
	        throws PicoCompositionException {
		try {
			constructor.setAccessible(true);
			return constructor.newInstance(arguments);
		} catch (final IllegalArgumentException e) {
			throw new AssertionError(e); // should not happen
		} catch (final InstantiationException e) {
			// TODO Disallow abstract classes for components in definitions
			throw new PicoCompositionException("The component class cannot be abstract", e);
		} catch (final IllegalAccessException e) {
			throw new PicoCompositionException("The component constructor is not accessible", e);
		} catch (final InvocationTargetException e) {
			throw new PicoCompositionException("An exception was thrown when creating component", e);
		}
	}

	@Override
	public String getDescriptor() {
		return "Constructor injector";
	}
}
