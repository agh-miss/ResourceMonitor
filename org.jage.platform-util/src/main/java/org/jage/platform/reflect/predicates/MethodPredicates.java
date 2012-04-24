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
 * File: MethodPredicates.java
 * Created: 2012-02-24
 * Author: Krzywicki
 * $Id: MethodPredicates.java 122 2012-03-18 09:41:07Z krzywick $
 */

package org.jage.platform.reflect.predicates;

import java.lang.reflect.Method;

import org.jage.platform.reflect.Methods;

import static org.jage.platform.reflect.Methods.isBooleanGetter;
import static org.jage.platform.reflect.Methods.isGetter;
import static org.jage.platform.reflect.Methods.isSetter;

import com.google.common.base.Predicate;

/**
 * Utility predicates for Methods.
 *
 * @since 2.6
 * @author AGH AgE Team
 */
public final class MethodPredicates {

	/**
	 * Stateless predicates.
	 *
	 * @author AGH AgE Team
	 */
	private static enum FixedPredicates implements Predicate<Method> {
		BOOLEAN_GETTER {
			@Override
			public boolean apply(final Method input) {
				return isBooleanGetter(input);
			}
		},
		GETTER {
			@Override
			public boolean apply(final Method input) {
				return isGetter(input);
			}
		},
		SETTER {
			@Override
			public boolean apply(final Method input) {
				return isSetter(input);
			}
		};
	}

	/**
	 * Returns a predicate checking if methods are boolean getters.
	 *
	 * @return a predicate
	 *
	 * @see Methods#isBooleanGetter(Method)
	 * @since 2.6
	 */
	public static Predicate<Method> forBooleanGetters() {
		return FixedPredicates.BOOLEAN_GETTER;
	}

	/**
	 * Returns a predicate checking if methods are getters.
	 *
	 * @return a predicate
	 *
	 * @see Methods#isGetter(Method)
	 * @since 2.6
	 */
	public static Predicate<Method> forGetters() {
		return FixedPredicates.GETTER;
	}

	/**
	 * Returns a predicate checking if methods are setters.
	 *
	 * @return a predicate
	 *
	 * @see Methods#isSetter(Method)
	 * @since 2.6
	 */
	public static Predicate<Method> forSetters() {
		return FixedPredicates.SETTER;
	}

	/**
	 * Returns a predicate checking if methods are overridden by a given child method.
	 *
	 * @param child
	 *            the child method
	 * @return a predicate
	 *
	 * @see Methods#isOverridenBy(Method, Method)
	 * @since 2.6
	 */
	public static Predicate<Method> overriddenBy(final Method child) {
		return new Predicate<Method>() {
			@Override
			public boolean apply(final Method parent) {
				return Methods.isOverridenBy(parent, child);
			}
		};
	}

	private MethodPredicates() {
	}
}
