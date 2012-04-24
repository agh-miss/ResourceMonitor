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
 * File: Methods.java
 * Created: 2012-02-23
 * Author: Krzywicki
 * $Id: Methods.java 122 2012-03-18 09:41:07Z krzywick $
 */

package org.jage.platform.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import static java.lang.reflect.Modifier.isPrivate;
import static java.lang.reflect.Modifier.isStatic;

import static org.jage.platform.reflect.Classes.isAssignable;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import static com.google.common.base.Preconditions.checkArgument;

/**
 * Method related reflection utilities.
 *
 * @since 2.6
 * @author AGH AgE Team
 */
public final class Methods {

	/**
	 * Enum representing the access modifiers of methods.
	 * <p>
	 * Instances are comparable and represent the access order from less to more restrictive.
	 *
	 * @since 2.6
	 * @author AGH AgE Team
	 */
	public enum MethodModifier {
		PUBLIC, PROTECTED, PACKAGE, PRIVATE;

		/**
		 * Returns the access modifier of a given method.
		 *
		 * @param method
		 *            a method
		 * @return the access modifier of the given method
		 *
		 * @since 2.6
		 */
		public static MethodModifier of(final Method method) {
			final int modifiers = method.getModifiers();
			MethodModifier modifier = PACKAGE;
			if (Modifier.isPrivate(modifiers)) {
				modifier = PRIVATE;
			} else if (Modifier.isProtected(modifiers)) {
				modifier = PROTECTED;
			} else if (Modifier.isPublic(modifiers)) {
				modifier = PUBLIC;
			}
			return modifier;
		}
	}

	/**
	 * Checks whether the first method is overridden by the second one.
	 * <p>
	 * That is, if:
	 * <ul>
	 * <li>the two methods are not equal as of {@link Object#equals(Object)}</li>
	 * <li>The parent method is neither static nor private</li>
	 * <li>Both have the same name, formal parameters and return types</li>
	 * <li>The declaring class of the second method extends or implements the declaring class of the first one</li>
	 * <li>The child method does not reduce the visibility of the parent one</li>
	 * </ul>
	 *
	 * @param parent
	 *            the parent method
	 * @param child
	 *            the child method
	 * @return true if the parent method is overriden by the child method
	 * @since 2.6
	 */
	public static boolean isOverridenBy(final Method parent, final Method child) {
		return !parent.equals(child) && parent.getName().equals(child.getName()) && !isStatic(parent.getModifiers())
		        && !isPrivate(parent.getModifiers())
		        && MethodModifier.of(parent).compareTo(MethodModifier.of(child)) >= 0
		        && parent.getDeclaringClass().isAssignableFrom(child.getDeclaringClass())
		        && parent.getReturnType().isAssignableFrom(child.getReturnType())
		        && Arrays.equals(parent.getParameterTypes(), child.getParameterTypes());
	}

	/**
	 * Checks whether the given method is a boolean getter. That is, it should take no parameters, return a boolean or
	 * Boolean and have a name starting in 'is'.
	 *
	 * @param method
	 *            a method
	 * @return true if the method is a boolean getter
	 * @since 2.6
	 */
	public static boolean isBooleanGetter(final Method method) {
		if (method.getName().startsWith("is") && method.getName().length() > 2
		        && method.getParameterTypes().length == 0 && isAssignable(method.getReturnType(), Boolean.class)) {
			return true;
		}
		return false;
	}

	/**
	 * Checks whether the given method is a getter. That is, it should either be a boolean getter or take no parameters,
	 * return a non-void value and have a name starting in 'get'.
	 *
	 * @param method
	 *            a method
	 * @return true if the method is a getter
	 * @since 2.6
	 */
	public static boolean isGetter(final Method method) {
		if (isBooleanGetter(method)) {
			return true;
		}
		if (method.getName().startsWith("get") && method.getName().length() > 3
		        && method.getParameterTypes().length == 0 && !isAssignable(method.getReturnType(), Void.class)) {
			return true;
		}
		return false;
	}

	/**
	 * Checks whether the given method is a setter. That is, it should take a single argument and have a name starting
	 * in 'set'.
	 *
	 * @param method
	 *            a method
	 * @return true if the method is a setter
	 * @since 2.6
	 */
	public static boolean isSetter(final Method method) {
		if (method.getName().startsWith("set") && method.getName().length() > 3
		        && method.getParameterTypes().length == 1) {
			return true;
		}
		return false;
	}

	/**
	 * Returns the name of this getter. That is, its name without the prefix 'is' or 'get'.
	 *
	 * @param method
	 *            a method
	 * @return the getter's name
	 * @throws IllegalArgumentException
	 *             if the method is not a getter
	 * @since 2.6
	 */
	public static String getGetterName(final Method method) {
		checkGetter(method);
		final String methodName = method.getName();
		String name = null;
		if (isBooleanGetter(method)) {
			name = methodName.substring(2, methodName.length());
		} else {
			name = methodName.substring(3, methodName.length());

		}

		return UPPER_CAMEL.to(LOWER_CAMEL, name);
	}

	/**
	 * Returns the return value's type of this getter.
	 *
	 * @param method
	 *            a method
	 * @return the getter's type
	 * @throws IllegalArgumentException
	 *             if the method is not a getter
	 * @since 2.6
	 */
	public static Class<?> getGetterType(final Method method) {
		checkGetter(method);
		return method.getReturnType();
	}

	/**
	 * Returns the name of this setter. That is, its name without the prefix 'set'.
	 *
	 * @param method
	 *            a method
	 * @return the setter's name
	 * @throws IllegalArgumentException
	 *             if the method is not a setter
	 * @since 2.6
	 */
	public static String getSetterName(final Method method) {
		checkSetter(method);
		final String methodName = method.getName();
		final String name = methodName.substring(3, methodName.length());

		return UPPER_CAMEL.to(LOWER_CAMEL, name);
	}

	/**
	 * Returns the argument type of this setter.
	 *
	 * @param method
	 *            a method
	 * @return the setter's type
	 * @throws IllegalArgumentException
	 *             if the method is not a setter
	 * @since 2.6
	 */
	public static Class<?> getSetterType(final Method method) {
		checkSetter(method);
		return method.getParameterTypes()[0];
	}

	private static void checkGetter(final Method method) {
		checkArgument(isGetter(method), "The given method is not a getter");
	}

	private static void checkSetter(final Method method) {
		checkArgument(isSetter(method), "The given method is not a setter");
	}

	private Methods() {
	}
}
