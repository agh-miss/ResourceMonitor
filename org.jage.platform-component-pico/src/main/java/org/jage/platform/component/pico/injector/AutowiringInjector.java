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
 * File: AutowiringInjector.java
 * Created: 2012-03-12
 * Author: Krzywicki
 * $Id: AutowiringInjector.java 172 2012-03-30 16:08:11Z krzywick $
 */

package org.jage.platform.component.pico.injector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.String.format;
import static java.lang.reflect.Modifier.isAbstract;
import static java.lang.reflect.Modifier.isFinal;
import static java.util.Collections.emptyList;

import org.picocontainer.Parameter;
import org.picocontainer.PicoCompositionException;
import org.picocontainer.PicoContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.platform.component.annotation.Inject;
import org.jage.platform.component.definition.ComponentDefinition;
import org.jage.platform.reflect.predicates.MethodPredicates;

import static org.jage.platform.reflect.Methods.getSetterName;
import static org.jage.platform.reflect.Methods.isSetter;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import static com.google.common.collect.Iterables.removeIf;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newLinkedHashMap;

/**
 * An injector for autowiring annotation-based injection. This implementation is compliant with the JSR-330
 * specification. It does however currently not support {@code Provider<T>} and {@code @Qualifier}s.
 *
 * @param <T>
 *            the type of components this injector can inject into
 *
 * @author AGH AgE Team
 */
public final class AutowiringInjector<T> extends AbstractInjector<T> {

	private static final Logger log = LoggerFactory.getLogger(AutowiringInjector.class);

	private static final Class<? extends Annotation> ANNOTATION_CLASS = Inject.class;

	private static final long serialVersionUID = 1L;

	private final Map<Field, Parameter> fields;

	private final Map<Method, Parameter[]> methods;

	/**
	 * Creates an AutowiringInjector using a given component definition.
	 *
	 * @param definition
	 *            the component definition to use
	 */
	public AutowiringInjector(final ComponentDefinition definition) {
		super(definition);

		final Set<String> explicitPropertiesNames = definition.getPropertyArguments().keySet();
		fields = ImmutableMap.copyOf(initializeFields(explicitPropertiesNames));
		methods = ImmutableMap.copyOf(initializeMethods(explicitPropertiesNames));
	}

	private Map<Field, Parameter> initializeFields(final Set<String> names) {
		final Class<?> componentType = getComponentImplementation();
		Map<Field, Parameter> fields = newLinkedHashMap();
		for (final Field field : Introspector.getFilteredInjectableFields(componentType, names)) {
			fields.put(field, Parameters.from(field.getType()));
		}
		return fields;
	}

	private Map<Method, Parameter[]> initializeMethods(final Set<String> names) {
		final Class<?> componentType = getComponentImplementation();
		Map<Method, Parameter[]> methods = newLinkedHashMap();
		for (final Method method : Introspector.getFilteredInjectableMethods(componentType, names)) {
			methods.put(method, Parameters.from(method.getParameterTypes()));
		}
		return methods;
	}

	@Override
	public void doInject(final PicoContainer container, final T instance) throws PicoCompositionException {
		log.debug("Will autowire instance {}", instance);

		for (Field field : fields.keySet()) {
			Object value = doResolve(fields.get(field), container, field.getType());
			injectField(instance, field, value);
		}

		for (Method method : methods.keySet()) {
			Object[] values = doResolveAll(methods.get(method), container, method.getParameterTypes());
			injectMethod(instance, method, values);
		}
	}

	private Object doResolve(final Parameter parameter, final PicoContainer container, final Type expectedType) {
		return parameter.resolve(container, this, null, expectedType, null, false, null).resolveInstance();
	}

	private Object[] doResolveAll(final Parameter[] parameters, final PicoContainer container,
	        final Type[] expectedTypes) {
		final Object[] values = new Object[parameters.length];
		for (int i = 0; i < values.length; i++) {
			values[i] = doResolve(parameters[i], container, expectedTypes[i]);
		}
		return values;
	}

	private void injectField(final T instance, final Field field, final Object argument) {
		log.trace("Will inject argument '{}' into field '{}'", argument, field);
		try {
			field.setAccessible(true);
			field.set(instance, argument);
		} catch (final IllegalArgumentException e) {
			throw new AssertionError(); // should not happen
		} catch (final IllegalAccessException e) {
			throw new PicoCompositionException(format("Unable to autowire field '%1$s' of %2$s: access denied.", field,
			        instance), e);
		}
	}

	private void injectMethod(final T instance, final Method method, final Object[] arguments) {
		log.trace("Will inject arguments '{}' into method '{}'", arguments, method);
		try {
			method.setAccessible(true);
			method.invoke(instance, arguments);
		} catch (final IllegalArgumentException e) {
			throw new AssertionError(); // should not happen
		} catch (final IllegalAccessException e) {
			throw new PicoCompositionException(format("Unable to autowire method '%1$s' of %2$s: access denied.",
			        method, instance), e);
		} catch (final InvocationTargetException e) {
			throw new PicoCompositionException(format(
			        "Unable to autowire method '%1$s' of %2$s: an exception happened.", method, instance), e);
		}
	}

	@Override
	protected void doVerify(final PicoContainer container) {
		for (Field field : fields.keySet()) {
			Parameter parameter = fields.get(field);
			try {
				parameter.verify(container, this, field.getType(), null, false, null);
			} catch (final PicoCompositionException e) {
				throw new PicoCompositionException(format("field '%1$s': %2$s", field.getName(), e.getMessage()), e);
			}
		}

		for (Method method : methods.keySet()) {
			Parameter[] parameters = methods.get(method);
			Class<?>[] types = method.getParameterTypes();
			for (int i = 0; i < parameters.length; i++) {
				try {
					parameters[i].verify(container, this, types[i], null, false, null);
				} catch (final PicoCompositionException e) {
					throw new PicoCompositionException(format("argument %1$s of method '%2$s': %3$s", i,
					        method.getName(), e.getMessage()), e);
				}
			}
		}
	}

	@Override
	public String getDescriptor() {
		return "Autowiring injector";
	}

	/**
	 * Helper class encapsulating class introspection.
	 *
	 * @author AGH AgE Team
	 */
	private static class Introspector {

		/**
		 * Return all injectable fields for the given class, without those which names are in the given set.
		 */
		public static Iterable<Field> getFilteredInjectableFields(final Class<?> clazz, final Set<String> filteringNames) {
			List<Field> injectableFields = getInjectableFields(clazz);
			return Iterables.filter(injectableFields, new Predicate<Field>() {
				@Override
				public boolean apply(final Field field) {
					return !filteringNames.contains(field.getName());
				}
			});
		}

		/**
		 * Return all injectable methods for the given class, without setters which names are in the given set.
		 */
		public static Iterable<Method> getFilteredInjectableMethods(final Class<?> clazz,
		        final Set<String> filteringNames) {
			List<Method> injectableMethods = getInjectableMethods(clazz);
			return Iterables.filter(injectableMethods, new Predicate<Method>() {
				@Override
				public boolean apply(final Method method) {
					return !(isSetter(method) && filteringNames.contains(getSetterName(method)));
				}
			});
		}

		/**
		 * Recursively computes a list of injectable fields. Fields from superclasses will be ordered before those from
		 * subclasses.
		 */
		public static List<Field> getInjectableFields(final Class<?> clazz) {
			// recursive stop condition
			if (clazz == null) {
				return emptyList();
			}

			// recursively get injectable fields from superclass
			final List<Field> injectableFields = newArrayList(getInjectableFields(clazz.getSuperclass()));
			for (final Field field : clazz.getDeclaredFields()) {
				if (isInjectable(field)) {
					injectableFields.add(field);
				}
			}

			return injectableFields;
		}

		/**
		 * Recursively computes a list of injectable methods. Methods from superclasses will be ordered before those
		 * from subclasses.
		 * <p>
		 * In accordance with JSR-330, any overridden superclass method will not be present in the list. The overriding
		 * subclass method will, only if it is itself injectable (injectability is not inherited in case of overriden
		 * methods).
		 */
		public static List<Method> getInjectableMethods(final Class<?> clazz) {
			// recursive stop condition
			if (clazz == null) {
				return emptyList();
			}

			// recursively get injectable methods from superclass
			final List<Method> allInjectableMethods = Lists.newLinkedList(getInjectableMethods(clazz.getSuperclass()));
			final List<Method> injectableMethods = newArrayList();

			// any overridden method will be present in the final list only if it is injectable in clazz
			for (final Method method : clazz.getDeclaredMethods()) {
				removeIf(allInjectableMethods, MethodPredicates.overriddenBy(method));
				if (isInjectable(method)) {
					injectableMethods.add(method);
				}
			}
			allInjectableMethods.addAll(injectableMethods);

			return allInjectableMethods;
		}

		/**
		 * According to JSR-330, injectable fields:
		 * <ul>
		 * <li>are annotated with @Inject.</li>
		 * <li>are not final.</li>
		 * <li>may have any otherwise valid name.</li>
		 * </ul>
		 */
		public static boolean isInjectable(final Field field) {
			return field.isAnnotationPresent(ANNOTATION_CLASS) && !isFinal(field.getModifiers());
		}

		/**
		 * According to JSR-330, injectable methods:
		 * <ul>
		 * <li>are annotated with @Inject.</li>
		 * <li>are not abstract.</li>
		 * <li>do not declare type parameters of their own.</li>
		 * <li>may return a result</li>
		 * <li>may have any otherwise valid name.</li>
		 * <li>accept zero or more dependencies as arguments.</li>
		 * </ul>
		 */
		public static boolean isInjectable(final Method method) {
			return method.isAnnotationPresent(ANNOTATION_CLASS) && !isAbstract(method.getModifiers())
			        && method.getTypeParameters().length == 0;
		}
	}
}
